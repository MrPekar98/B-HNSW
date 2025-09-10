package dk.aau.cs.dess.structurs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents an immutable vector of numbers
 * @param <V> A numeric type of the items stored in the vector
 */
public final class Vector<V extends Number> implements Comparable<Vector<V>>
{
    private final List<V> vector;
    private final float[] primitiveVector;
    private static Vector<Number> IDENTITY_VECTOR = null;

    public Vector(Collection<V> collection)
    {
        this.vector = new ArrayList<>(collection);
        this.primitiveVector = primitive();
    }

    public Vector(V[] vector)
    {
        this(List.of(vector));
    }

    public Vector(Vector<V> other)
    {
        this.vector = other.vector;
        this.primitiveVector = other.primitiveVector;
    }

    /**
     * Identity vector, where every dimensional value is 1
     * @param capacity Dimension of the identity vector
     * @return Identity vector of Numbers
     */
    public static Vector<Number> identityVector(int capacity)
    {
        if (IDENTITY_VECTOR != null && IDENTITY_VECTOR.dimension() == capacity)
        {
            return IDENTITY_VECTOR;
        }

        IDENTITY_VECTOR = new Vector<>(Collections.nCopies(capacity, 1));
        return IDENTITY_VECTOR;
    }

    /**
     * Converts the Vector into a primitive datatype
     * @return Array of floats
     */
    public float[] primitive()
    {
        float[] primitive = new float[this.vector.size()];

        for (int i = 0; i < primitive.length; i++)
        {
            primitive[i] = this.vector.get(i).floatValue();
        }

        return primitive;
    }

    /**
     * Modifies a single value at a specified dimension
     * @param index Dimensional index in the vector
     * @param value New value at the given index
     */
    public void set(int index, V value)
    {
        if (index < 0 || index >= vector.size())
        {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for limit " + this.vector.size());
        }

        this.vector.set(index, value);
    }

    /**
     * Dimension of the vector
     * @return Dimension
     */
    public int dimension()
    {
        return this.vector.size();
    }

    /**
     * Pythagorean length of the vector
     * @return Length of the vector
     */
    public double length()
    {
        double length = 0;

        for (int i = 0; i < this.primitiveVector.length; i++)
        {
            length += Math.pow(this.primitiveVector[i], 2);
        }

        return Math.sqrt(length);
    }

    /**
     * Euclidean distance to another vector
     * @param other Another vector
     * @return The distance to the given vector
     */
    public double euclideanDistance(Vector<? extends Number> other)
    {
        if (this.vector.size() != other.vector.size())
        {
            throw new IllegalArgumentException("Dimensions do not match");
        }

        double distance = 0;

        for (int i = 0; i < this.primitiveVector.length; i++)
        {
            distance += Math.pow(this.primitiveVector[i] - other.primitiveVector[i], 2);
        }

        return Math.sqrt(distance);
    }

    /**
     * Cosine similarity to another vector
     * @param other Another vector we are comparing to
     * @return The similarity to the given vector
     */
    public double cosineSimilarity(Vector<? extends Number> other)
    {
        if (this.vector.size() != other.vector.size())
        {
            throw new IllegalArgumentException("Dimensions do not match");
        }

        double dot = 0;

        for (int i = 0; i < this.primitiveVector.length; i++)
        {
            dot += this.primitiveVector[i] * other.primitiveVector[i];
        }

        return dot / (length() * other.length());
    }

    /**
     * Compares to vectors by measuring their individual Euclidean distances to the identity vector
     * @param o The object to be compared
     * @return -1, 0, or 1 depending on which is closest to the identity vector
     */
    @Override
    public int compareTo(Vector<V> o)
    {
        int dim = dimension();
        double thisDistance = euclideanDistance(Vector.identityVector(dim));
        double otherDistance = o.euclideanDistance(Vector.identityVector(dim));
        return Double.compare(thisDistance, otherDistance);
    }

    @Override
    public String toString()
    {
        return this.vector.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Vector<?> other))
        {
            return false;
        }

        return this.vector.equals(other.vector);
    }
}
