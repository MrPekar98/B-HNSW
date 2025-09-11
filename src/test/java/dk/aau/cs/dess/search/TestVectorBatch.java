package dk.aau.cs.dess.search;

import dk.aau.cs.dess.structures.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestVectorBatch
{
    private VectorBatch batch;
    private final Vector<Float> v1 = new Vector<>(List.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)),
            v2 = new Vector<>(List.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f)),
            v3 = new Vector<>(List.of(113.3f, 124.2f, -322.8f, 100.1f, 1.2f)),
            v4 = new Vector<>(List.of(44.0f, 55.0f, 66.0f, 77.0f, 88.0f)),
            v5 = new Vector<>(List.of(54.2f, -123.8f, 21.9f, -554.3f, 38.2f)),
            v6 = new Vector<>(List.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

    @BeforeEach
    public void setup()
    {
        this.batch = new VectorBatch(List.of(this.v1, this.v2, this.v3, this.v4, this.v5), 5);
    }

    @Test
    public void testSize()
    {
        assertEquals(5, this.batch.size());
    }

    @Test
    public void testAdd()
    {
        this.batch.add(this.v1);
        assertEquals(5, this.batch.size());

        this.batch.add(this.v6);
        assertEquals(6, this.batch.size());
    }

    @Test
    public void testAddWrongDimension()
    {
        Vector<Float> wrongDimensionVector = new Vector<>(List.of(0.0f, 0.0f, 0.0f, 0.0f));
        assertThrows(IllegalArgumentException.class, () -> this.batch.add(wrongDimensionVector));
    }

    @Test
    public void testRemove()
    {
        this.batch.remove(this.v1);
        assertEquals(4, this.batch.size());

        this.batch.remove(this.v3);
        assertEquals(3, this.batch.size());
    }

    @Test
    public void testCentroid()
    {
        Vector<Float> centroid = this.batch.centroid();
        assertEquals(43.5, centroid.get(0), 0.1);
        assertEquals(12.48, centroid.get(1), 0.01);
    }

    @Test
    public void testSum()
    {
        Vector<Float> sum = this.batch.sum();
        assertEquals(217.5, sum.get(0), 0.01);
        assertEquals(62.4, sum.get(1), 0.01);
    }

    @Test
    public void testMax()
    {
        Vector<Float> max = this.batch.max();
        assertEquals(113.3, max.get(0), 0.01);
        assertEquals(124.2, max.get(1), 0.01);
    }

    @Test
    public void testMin()
    {
        Vector<Float> min = this.batch.min();
        assertEquals(1.0, min.get(0), 0.01);
        assertEquals(-123.8, min.get(1), 0.01);
    }
}
