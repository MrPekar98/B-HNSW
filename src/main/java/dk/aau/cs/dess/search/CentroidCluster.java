package dk.aau.cs.dess.search;

import java.util.Map;
import java.util.Vector;

/**
 * Clusters a batch of vectors into sub-batches by their vector centroids
 */
public class CentroidCluster implements ClusterStrategy<Vector<? extends Number>, VectorBatch>
{
    @Override
    public Map<Vector<? extends Number>, VectorBatch> cluster()
    {
        return Map.of();
    }

    @Override
    public VectorBatch deCluster()
    {
        return null;
    }
}
