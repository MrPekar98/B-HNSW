package dk.aau.cs.dess.index;

import com.stepstone.search.hnswlib.jna.Index;
import com.stepstone.search.hnswlib.jna.QueryTuple;
import dk.aau.cs.dess.search.*;
import dk.aau.cs.dess.structures.Pair;
import dk.aau.cs.dess.structures.Vector;

import java.nio.file.Path;
import java.util.*;

/**
 * Batched HNSW allowing searching with multiple query vectors at once for runtime benefits
 */
public class BHNSW extends BaseHNSW implements NearestNeighbor<VectorBatch, Map<Vector<? extends Number>, ResultSet>>
{
    private final ClusterStrategy<Vector<? extends Number>, VectorBatch> cluster;

    public BHNSW(int ef, int efConstruction, int m, int dimension, int capacity, ClusterStrategy<Vector<? extends Number>, VectorBatch> clusterStrategy)
    {
        super(ef, efConstruction, m, dimension, capacity);
        this.cluster = clusterStrategy;
    }

    public BHNSW(Path diskPath, int ef, int dimension, int capacity, ClusterStrategy<Vector<? extends Number>, VectorBatch> clusterStrategy)
    {
        super(diskPath, ef, dimension, capacity);
        this.cluster = clusterStrategy;
    }

    /**
     * Clusters the batch of vectors into sub-batches by centroids
     * It retrieves topK * 2 nearest neighbors for each centroid vector
     * Finally, it orders the list of nearest neighbors retrieved with the centroid according to the cosine similarity to the individual vector
     * @param query Batch of query vectors
     * @param topK Result set size per vector
     * @return The top-K nearest neighbors for each vector
     */
    @Override
    public Map<Vector<? extends Number>, ResultSet> search(VectorBatch query, int topK)
    {
        Map<Vector<? extends Number>, VectorBatch> clusters = this.cluster.cluster(query);
        Map<Vector<? extends Number>, ResultSet> results = new HashMap<>(clusters.size());

        for (var entry : clusters.entrySet())
        {
            Vector<? extends Number> centroid = entry.getKey();
            VectorBatch batch = entry.getValue();
            Iterator<Vector<? extends Number>> batchIterator = batch.iterator();
            float[] primitiveCentroid = centroid.primitive();
            Index.normalize(primitiveCentroid);

            QueryTuple centroidQuery = super.hnsw.knnNormalizedQuery(primitiveCentroid, topK * 2);
            List<Pair<Integer, Vector<Float>>> centroidResults = new ArrayList<>(topK * 2);

            for (int id : centroidQuery.getIds())
            {
                Optional<float[]> primitiveResult = super.hnsw.getData(id);

                if (primitiveResult.isPresent())
                {
                    Vector<Float> result = new Vector<>(List.of(primitiveResult.get()));
                    centroidResults.add(new Pair<>(id, result));
                }
            }

            while (batchIterator.hasNext())
            {
                Vector<? extends Number> queryVector = batchIterator.next();
                ResultSet resultSet = new ResultSet(topK);
                centroidResults.forEach(pair -> resultSet.addResult(new Result(pair.first(), pair.second().cosineSimilarity(queryVector))));
                results.put(queryVector, resultSet);
            }
        }

        return results;
    }
}
