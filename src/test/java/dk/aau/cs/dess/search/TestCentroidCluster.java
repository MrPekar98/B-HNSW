package dk.aau.cs.dess.search;

import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    }

    @Test
    public void testDeCluster()
    {

    }
}
