package dk.aau.cs.dess.index;

import com.stepstone.search.hnswlib.jna.Index;
import com.stepstone.search.hnswlib.jna.QueryTuple;
import dk.aau.cs.dess.search.NearestNeighbor;
import dk.aau.cs.dess.search.Result;
import dk.aau.cs.dess.search.ResultSet;
import dk.aau.cs.dess.structures.Vector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class of the HNSW library for approximate nearest neighbor (ANN) search
 */
public class HNSW extends BaseHNSW implements NearestNeighbor<Vector<? extends Number>, ResultSet>
{
    public HNSW(int ef, int efConstruction, int m, int dimension, int capacity)
    {
        super(ef, efConstruction, m, dimension, capacity);
    }

    public HNSW(Path diskPath, int ef, int dimension, int capacity)
    {
        super(diskPath, ef, dimension, capacity);
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

        QueryTuple query = super.hnsw.knnNormalizedQuery(primitiveInput, topK);
        double order = 1;

        for (int id : query.getIds())
        {
            Result res = new Result(id, order);
            results.add(res);
        }

        return new ResultSet(topK, results);
    }
}
