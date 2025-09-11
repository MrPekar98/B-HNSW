package dk.aau.cs.dess.index;

import dk.aau.cs.dess.search.CentroidCluster;
import dk.aau.cs.dess.search.ClusterStrategy;
import dk.aau.cs.dess.search.ResultSet;
import dk.aau.cs.dess.search.VectorBatch;
import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestBHNSW
{
    private BHNSW bhnsw;
    private final Vector<Float> v1 = new Vector<>(List.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)),
            v2 = new Vector<>(List.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f)),
            v3 = new Vector<>(List.of(113.3f, 124.2f, -322.8f, 100.1f, 1.2f)),
            v4 = new Vector<>(List.of(44.0f, 55.0f, 66.0f, 77.0f, 88.0f)),
            v5 = new Vector<>(List.of(54.2f, -123.8f, 21.9f, -554.3f, 38.2f));

    @BeforeEach
    public void setup()
    {
        this.bhnsw = new BHNSW(1, 5, 3, 5, 5, ClusterStrategy.centroidStrategy(CentroidCluster.Measure.COSINE));
    }

    @Test
    public void testGetEF()
    {
        assertEquals(1, this.bhnsw.getEf());
    }

    @Test
    public void testGetEFConstruction()
    {
        assertEquals(5, this.bhnsw.getEfConstruction());
    }

    @Test
    public void testGetM()
    {
        assertEquals(3, this.bhnsw.getM());
    }

    @Test
    public void testGetCapacity()
    {
        assertEquals(5, this.bhnsw.getCapacity());
    }

    @Test
    public void testSize()
    {
        assertEquals(5, this.bhnsw.size());

        this.bhnsw.remove(3);
        assertEquals(4, this.bhnsw.size());
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
        assertFalse(this.bhnsw.exists(6));
    }

    @Test
    public void testSerialization()
    {
        Path path = Path.of(".hnsw.ser");
        this.bhnsw.save(path);
        this.bhnsw.clear();

        BaseHNSW copy = new HNSW(path, 1, 5, 5);
        path.toFile().delete();
        assertEquals(1, copy.getEf());
        assertEquals(5, copy.getEfConstruction());
        assertEquals(3, copy.getM());
        assertEquals(5, copy.getCapacity());
    }

    @Test
    public void testSearch()
    {
        VectorBatch queryBatch = new VectorBatch(List.of(this.v1, this.v2, this.v3, this.v4, this.v5), this.v1.dimension());
        Map<Vector<? extends Number>, ResultSet> results = this.bhnsw.search(queryBatch, 5);
    }
}
