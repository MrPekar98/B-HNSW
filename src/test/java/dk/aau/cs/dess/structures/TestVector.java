package dk.aau.cs.dess.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestVector
{
    private static final Vector<Float> v1 = new Vector<>(new Float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f});
    private static final Vector<Float> v2 = new Vector<>(new Float[]{2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f});

    @Test
    public void testLength()
    {
        assertEquals(9.53, v1.length(), 0.01);
        assertEquals(4.9, v2.length(), 0.01);
    }

    @Test
    public void testDimension()
    {
        assertEquals(6, v1.dimension());
        assertEquals(6, v2.dimension());
    }

    @Test
    public void testEquals()
    {
        assertNotEquals(v1, v2);
    }

    @Test
    public void testCompare()
    {
        assertEquals(0, v1.compareTo(v1));
        assertEquals(1, v1.compareTo(v2));
    }

    @Test
    public void testEuclideanDistance()
    {
        assertEquals(5.56, v1.euclideanDistance(v2), 0.01);
        assertEquals(7.41, v1.euclideanDistance(Vector.identityVector(6)), 0.01);
        assertEquals(2.44, v2.euclideanDistance(Vector.identityVector(6)), 0.01);
    }

    @Test
    public void testCosineSimilarity()
    {
        assertEquals(0.89, v1.cosineSimilarity(v2), 0.01);
        assertEquals(0.89, v1.cosineSimilarity(Vector.identityVector(6)), 0.01);
    }
}
