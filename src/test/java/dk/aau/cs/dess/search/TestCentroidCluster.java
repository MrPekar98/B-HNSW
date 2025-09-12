package dk.aau.cs.dess.search;

import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCentroidCluster
{
    private CentroidCluster cluster;
    private final Vector<Float> v1 = new Vector<>(List.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)),
            v2 = new Vector<>(List.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f)),
            v3 = new Vector<>(List.of(113.3f, 124.2f, -322.8f, 100.1f, 1.2f)),
            v4 = new Vector<>(List.of(44.0f, 55.0f, 66.0f, 77.0f, 88.0f)),
            v5 = new Vector<>(List.of(54.2f, -123.8f, 21.9f, -554.3f, 38.2f));

    @BeforeEach
    public void setup()
    {
        this.cluster = ClusterStrategy.centroidStrategy(CentroidCluster.Measure.EUCLIDEAN);
    }

    @Test
    public void testCluster()
    {
        VectorBatch batch = new VectorBatch(List.of(this.v1, this.v2, this.v3, this.v4, this.v5), this.v1.dimension());
        var clusters = this.cluster.cluster(batch);
        assertEquals(3, clusters.size());

        for (Vector<Float> vector : List.of(this.v1, this.v2, this.v3, this.v4, this.v5))
        {
            boolean found = false;

            for (Map.Entry<Vector<? extends Number>, VectorBatch> entry : clusters.entrySet())
            {
                if (entry.getValue().getItems().contains(vector))
                {
                    found = true;
                }
            }

            assertTrue(found);
        }

        for (Map.Entry<Vector<? extends Number>, VectorBatch> entry : clusters.entrySet())
        {
            assertEquals(entry.getKey(), entry.getValue().centroid());
        }
    }

    @Test
    public void testDeCluster()
    {
        VectorBatch batch = new VectorBatch(List.of(this.v1, this.v2, this.v3, this.v4, this.v5), this.v1.dimension());
        this.cluster.cluster(batch);
        VectorBatch deClusteredBatch = this.cluster.deCluster();
        assertEquals(batch, deClusteredBatch);
    }
}
