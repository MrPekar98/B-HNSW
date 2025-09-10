# B-HNSW
Index for batched approximate nearest neighbor (ANN) search using Hierarchical Navigable Small Worlds (HNSW).
The batch of search vector keys are clustered around centroids, which are used as search keys in HNSW.
The ordering for the individual vector keys is computed upon after retrieval of the nearest neighbors of the centroids.

## Prerequisites

- JDK 21