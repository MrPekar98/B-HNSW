package dk.aau.cs.dess.index;

import dk.aau.cs.dess.search.CentroidCluster;
import dk.aau.cs.dess.search.ClusterStrategy;
import dk.aau.cs.dess.search.ResultSet;
import dk.aau.cs.dess.search.VectorBatch;
import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBHNSW
{
    private BHNSW bhnsw;
    private final Vector<Float> v1 = new Vector<>(List.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)),
            v2 = new Vector<>(List.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f)),
            v3 = new Vector<>(List.of(113.3f, 124.2f, -322.8f, 100.1f, 1.2f)),
            v4 = new Vector<>(List.of(44.0f, 55.0f, 66.0f, 77.0f, 88.0f)),
            v5 = new Vector<>(List.of(54.2f, -123.8f, 21.9f, -554.3f, 38.2f)),
            v6 = new Vector<>(List.of(0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f)),
            v7 = new Vector<>(List.of(-10.0f, -9.0f, -8.0f, -7.0f, -6.0f)),
            v8 = new Vector<>(List.of(1000.0f, 2000.0f, -1000.0f, -2000.0f, 0.0f)),
            v9 = new Vector<>(List.of(-100.0f, -100.0f, -100.0f, -100.0f, -100.0f)),
            v10 = new Vector<>(List.of(-150.0f, 150.0f, 0.0f, 5000.5f, -5000.5f));

    @BeforeEach
    public void setup()
    {
        this.bhnsw = new BHNSW(4, 5, 5, 5, 10, ClusterStrategy.centroidStrategy(CentroidCluster.Measure.COSINE));
        this.bhnsw.insert(1, this.v1);
        this.bhnsw.insert(2, this.v2);
        this.bhnsw.insert(3, this.v3);
        this.bhnsw.insert(4, this.v4);
        this.bhnsw.insert(5, this.v5);
        this.bhnsw.insert(6, this.v6);
        this.bhnsw.insert(7, this.v7);
        this.bhnsw.insert(8, this.v8);
        this.bhnsw.insert(9, this.v9);
        this.bhnsw.insert(10, this.v10);
    }

    @Test
    public void testGetEF()
    {
        assertEquals(4, this.bhnsw.getEf());
    }

    @Test
    public void testGetEFConstruction()
    {
        assertEquals(5, this.bhnsw.getEfConstruction());
    }

    @Test
    public void testGetM()
    {
        assertEquals(5, this.bhnsw.getM());
    }

    @Test
    public void testGetCapacity()
    {
        assertEquals(10, this.bhnsw.getCapacity());
    }

    @Test
    public void testSize()
    {
        assertEquals(10, this.bhnsw.size());

        this.bhnsw.remove(3);
        assertEquals(9, this.bhnsw.size());
    }

    @Test
    public void testClear()
    {
        this.bhnsw.clear();
        assertEquals(0, this.bhnsw.size());
    }

    @Test
    public void testExists()
    {
        assertTrue(this.bhnsw.exists(1));
        assertFalse(this.bhnsw.exists(11));
    }

    @Test
    public void testSerialization()
    {
        Path path = Path.of(".hnsw.ser");
        this.bhnsw.save(path);
        this.bhnsw.clear();

        BHNSW copy = new BHNSW(path, 4, 5, 5, ClusterStrategy.centroidStrategy(CentroidCluster.Measure.COSINE));
        path.toFile().delete();
        assertEquals(4, copy.getEf());
        assertEquals(5, copy.getEfConstruction());
        assertEquals(5, copy.getM());
        assertEquals(5, copy.getCapacity());
    }

    @Test
    public void testSearch()
    {
        Set<Vector<? extends Number>> queryVectors = new HashSet<>(List.of(this.v1, this.v2, this.v3, this.v4, this.v5));
        VectorBatch queryBatch = new VectorBatch(queryVectors, this.v1.dimension());
        Map<Vector<? extends Number>, ResultSet> results = this.bhnsw.search(queryBatch, 5);
        assertEquals(5, results.size());

        for (var vector : queryBatch)
        {
            assertTrue(results.containsKey(vector));
        }

        for (var entry : results.entrySet())
        {
            assertEquals(5, entry.getValue().getTopK());
        }
    }
}
