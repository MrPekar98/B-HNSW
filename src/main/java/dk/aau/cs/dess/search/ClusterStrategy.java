package dk.aau.cs.dess.search;

import java.util.Map;

public interface ClusterStrategy<K, B extends Batch<?>>
{
    Map<K, B> cluster(B batch);
    B deCluster();

    static CentroidCluster centroidStrategy(CentroidCluster.Measure measure)
    {
        return new CentroidCluster(measure);
    }
}
