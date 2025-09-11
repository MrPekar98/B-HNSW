package dk.aau.cs.dess.index;

import com.stepstone.search.hnswlib.jna.Index;
import com.stepstone.search.hnswlib.jna.SpaceName;
import dk.aau.cs.dess.structures.Vector;

import java.nio.file.Path;

/**
 * Base HNSW class for approximate nearest neighbor (ANN) search
 */
public abstract class BaseHNSW implements dk.aau.cs.dess.index.Index<Integer, Vector<? extends Number>>
{
    protected final transient Index hnsw;
    private final int m, efConstruction, capacity;
    private int ef, size = 0;
    private transient final Object lock = new Object();

    public BaseHNSW(int ef, int efConstruction, int m, int dimension, int capacity)
    {
        this.m = m;
        this.efConstruction = efConstruction;
        this.ef = ef;
        this.capacity = capacity;
        this.hnsw = new Index(SpaceName.COSINE, dimension);
        this.hnsw.initialize(capacity, m, efConstruction, 100);
    }

    public BaseHNSW(Path diskPath, int ef, int dimension, int capacity)
    {
        this.hnsw = new Index(SpaceName.COSINE, dimension);
        this.hnsw.initialize();
        load(diskPath, capacity);

        this.m = this.hnsw.getM();
        this.efConstruction = this.hnsw.getEfConstruction();
        this.ef = ef;
        this.capacity = capacity;
    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public void setEf(int ef)
    {
        this.ef = ef;
        this.hnsw.setEf(ef);
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
            this.size--;
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
     * Existence check of key
     * @param key Key of node to check for existence for
     * @return Whether a node with the given ID exists
     */
    @Override
    public boolean exists(Integer key)
    {
        return this.hnsw.hasId(key);
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
            this.size = 0;
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
