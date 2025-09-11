package dk.aau.cs.dess.search;

import java.util.Map;

public interface ClusterStrategy<K, B extends Batch<?>>
{
    Map<K, B> cluster();
    B deCluster();
}
