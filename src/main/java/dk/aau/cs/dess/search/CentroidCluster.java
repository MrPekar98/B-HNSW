package dk.aau.cs.dess.search;

import dk.aau.cs.dess.structures.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clusters a batch of vectors into sub-batches by their vector centroids
 * It uses a simple K-Means approach
 */
public class CentroidCluster implements ClusterStrategy<Vector<? extends Number>, VectorBatch>
{
    public enum Measure
    {
        COSINE, EUCLIDEAN
    }

    private final Measure measure;
    private Map<Vector<? extends Number>, VectorBatch> clusters = null;
    private int dimension = -1;

    CentroidCluster(Measure measure)
    {
        this.measure = measure;
    }

    private static double distance(Measure measure, Vector<? extends Number> v1, Vector<? extends Number> v2)
    {
        return switch (measure) {
            case COSINE -> 1.0 / v1.cosineSimilarity(v2);
            case EUCLIDEAN -> v1.euclideanDistance(v2);
        };
    }

    private static int computeK(int batchSize)
    {
        return (int) Math.ceil(Math.log(batchSize));
    }

    private static Map<Vector<? extends Number>, VectorBatch> initCentroids(List<Vector<? extends Number>> vectors, int dimension, int k)
    {
        Map<Vector<? extends Number>, VectorBatch> centroids = new HashMap<>(k);
        int batchSize = vectors.size();
        Random random = new Random();

        for (int i = 0; i < k; i++)
        {
            Vector<? extends Number> centroid = vectors.get(random.nextInt(batchSize));
            centroids.put(centroid, new VectorBatch(new ArrayList<>(), dimension));
        }

        return centroids;
    }

    /**
     * Dynamically determines the number of clusters (K) and iteratively computes centroids of the vectors until convergence
     * @param batch Batch of vectors to be clustered according to centroids
     * @return Clusters of vectors
     */
    @Override
    public Map<Vector<? extends Number>, VectorBatch> cluster(VectorBatch batch)
    {
        boolean converged = false;
        int k = computeK(batch.size()), batchSize = batch.size();
        List<Vector<? extends Number>> vectors = new ArrayList<>(batch.getItems());
        Map<Vector<? extends Number>, VectorBatch> clusters = initCentroids(vectors, batch.getDimension(), k);

        while (!converged)
        {
            for (int i = 0; i < batchSize; i++)
            {
                double minDistance = Double.MAX_VALUE;
                Vector<? extends Number> closestVector = null;

                for (var vector : clusters.keySet())
                {
                    double distance = distance(this.measure, vectors.get(i), vector);

                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        closestVector = vector;
                    }
                }

                clusters.get(closestVector).add(vectors.get(i));
            }

            Map<Vector<? extends Number>, VectorBatch> newClusters = new HashMap<>();

            for (var entry : clusters.entrySet())
            {
                Vector<? extends Number> newCentroid = entry.getValue().centroid();
                newClusters.put(newCentroid, new VectorBatch(new ArrayList<>(), batch.getDimension()));
            }

            if (newClusters.keySet().equals(clusters.keySet()))
            {
                converged = true;
            }

            else
            {
                clusters = newClusters;
            }
        }

        this.clusters = clusters;
        this.dimension = batch.getDimension();

        return clusters;
    }

    @Override
    public VectorBatch deCluster()
    {
        if (this.clusters == null)
        {
            throw new NullPointerException("De-clustering only allowed after clustering");
        }

        List<VectorBatch> vectors = new ArrayList<>(this.clusters.values());
        return new VectorBatch(vectors.stream()
                                        .map(BaseBatch::getItems)
                                        .flatMap(Set::stream)
                                        .collect(Collectors.toSet()), this.dimension);
    }
}
