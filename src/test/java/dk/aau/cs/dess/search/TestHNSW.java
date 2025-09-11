package dk.aau.cs.dess.search;

import dk.aau.cs.dess.index.HNSW;
import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestHNSW
{
    private HNSW hnsw;
    private final Vector<Float> v1 = new Vector<>(List.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)),
            v2 = new Vector<>(List.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f)),
            v3 = new Vector<>(List.of(113.3f, 124.2f, -322.8f, 100.1f, 1.2f)),
            v4 = new Vector<>(List.of(44.0f, 55.0f, 66.0f, 77.0f, 88.0f)),
            v5 = new Vector<>(List.of(54.2f, -123.8f, 21.9f, -554.3f, 38.2f));

    @BeforeEach
    public void setup()
    {
        this.hnsw = new HNSW(1, 5, 3, 5, 5);
        this.hnsw.insert(1, this.v1);
        this.hnsw.insert(2, this.v2);
        this.hnsw.insert(3, this.v3);
        this.hnsw.insert(4, this.v4);
        this.hnsw.insert(5, this.v5);
    }

    @Test
    public void testGetEF()
    {
        assertEquals(1, this.hnsw.getEf());
    }

    @Test
    public void testGetEFConstruction()
    {
        assertEquals(5, this.hnsw.getEfConstruction());
    }

    @Test
    public void testGetM()
    {
        assertEquals(3, this.hnsw.getM());
    }

    @Test
    public void testGetCapacity()
    {
        assertEquals(5, this.hnsw.getCapacity());
    }

    @Test
    public void testSearch()
    {
        ResultSet results = this.hnsw.search(this.v1, 5);
        assertEquals(5, results.getResults().size());
        assertEquals(5, results.getTopK());
        assertEquals(3, results.getTopK(3).size());
        assertTrue(results.getResults().stream().anyMatch(result -> result.key() == 1));
    }

    @Test
    public void testFindItself()
    {
        ResultSet results = this.hnsw.search(this.v1, 1);
        assertEquals(1, results.getResults().size());
        assertEquals(1, results.getResults().getFirst().key());
    }

    @Test
    public void testSize()
    {
        assertEquals(5, this.hnsw.size());

        this.hnsw.remove(3);
        assertEquals(4, this.hnsw.size());
    }

    @Test
    public void testClear()
    {
        this.hnsw.clear();
        assertEquals(0, this.hnsw.size());
    }

    @Test
    public void testExists()
    {
        assertTrue(this.hnsw.exists(1));
        assertFalse(this.hnsw.exists(6));
    }

    @Test
    public void testSerialization()
    {
        Path path = Path.of(".hnsw.ser");
        this.hnsw.save(path);
        this.hnsw.clear();

        HNSW copy = new HNSW(path, 1, 5, 5);
        path.toFile().delete();
        assertEquals(1, copy.getEf());
        assertEquals(5, copy.getEfConstruction());
        assertEquals(3, copy.getM());
        assertEquals(5, copy.getCapacity());
    }
}
