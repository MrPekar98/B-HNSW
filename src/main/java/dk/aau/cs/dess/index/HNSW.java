package dk.aau.cs.dess.index;

import com.stepstone.search.hnswlib.jna.Index;
import com.stepstone.search.hnswlib.jna.QueryTuple;
import com.stepstone.search.hnswlib.jna.SpaceName;
import dk.aau.cs.dess.search.NearestNeighbor;
import dk.aau.cs.dess.search.Result;
import dk.aau.cs.dess.search.ResultSet;
import dk.aau.cs.dess.structurs.Vector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class of the HNSW library for approximate nearest neighbor (ANN) search
 */
public class HNSW implements NearestNeighbor
{
    private final transient Index hnsw;
    private final int m, efConstruction, capacity;
    private int ef, size = 0;
    private transient final Object lock = new Object();

    public HNSW(int ef, int efConstruction, int m, int dimension, int capacity)
    {
        this.m = m;
        this.efConstruction = efConstruction;
        this.ef = ef;
        this.capacity = capacity;
        this.hnsw = new Index(SpaceName.COSINE, dimension);
        this.hnsw.initialize(capacity, m, efConstruction, 100);
    }

    public HNSW(Path diskPath, int dimension, int capacity)
    {
        this.hnsw = new Index(SpaceName.COSINE, dimension);
        load(diskPath, capacity);

        this.m = this.hnsw.getM();
        this.ef = this.hnsw.getEf();
        this.efConstruction = this.hnsw.getEfConstruction();
        this.capacity = capacity;
    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public void setEf(int ef)
    {
        this.ef = ef;
    }

    public int getEf()
    {
        return this.ef;
    }

    public int getM()
    {
        return this.m;
    }

    public int getEfConstruction()
    {
        return this.efConstruction;
    }

    /**
     * Performs ANN search over the HNSW index
     * @param input Query vector
     * @param topK Number of approximate neighbors to retrieve
     * @return Ranked list of approximate nearest neighbors to the input vector
     */
    @Override
    public ResultSet search(Vector<? extends Number> input, int topK)
    {
        List<Result> results = new ArrayList<>(topK);
        float[] primitiveInput = input.primitive();
        Index.normalize(primitiveInput);

        QueryTuple query = this.hnsw.knnNormalizedQuery(primitiveInput, topK);
        int order = 1;

        for (int id : query.getIds())
        {
            Result res = new Result(id, order);
            results.add(res);
        }

        return new ResultSet(topK, results);
    }

    /**
     * Inserts a node in the HNSW index
     * Note that the parameters M and EF affects the index performance in terms of ranking quality and runtime
     * @param key ID of inserted node
     * @param value Vector representation of the inserted node ID
     */
    @Override
    public void insert(Integer key, Vector<? extends Number> value)
    {
        float[] primitive = value.primitive();
        Index.normalize(primitive);

        synchronized (this.lock)
        {
            this.hnsw.addNormalizedItem(primitive, key);
            this.size++;
        }
    }

    /**
     * Removes a node in the HNSW index
     * @param key Key of node to be removed from the index
     */
    @Override
    public void remove(Integer key)
    {
        synchronized (this.lock)
        {
            this.hnsw.markDeleted(key);
        }
    }

    /**
     * Not supported in HNSW, as a vector must be given
     */
    @Override
    public Vector<? extends Number> lookup(Integer key)
    {
        throw new UnsupportedOperationException("Not supported in HNSW");
    }

    /**
     * This is not supported in HNSW
     */
    @Override
    public boolean exists(Integer key)
    {
        throw new UnsupportedOperationException("Not supported in HNSW");
    }

    /**
     * Returns the number of nodes stored in the HNSW index
     * @return Size of HNSW index
     */
    @Override
    public int size()
    {
        return this.size;
    }

    /**
     * Clears the HNSW index
     */
    @Override
    public void clear()
    {
        synchronized (this.lock)
        {
            this.hnsw.clear();
        }
    }

    public void save(Path path)
    {
        synchronized (this.lock)
        {
            this.hnsw.save(path);
        }
    }

    private void load(Path path, int capacity)
    {
        this.hnsw.load(path, capacity);
    }
}
