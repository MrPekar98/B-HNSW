package dk.aau.cs.dess.search;

import dk.aau.cs.dess.structures.Vector;

import java.util.*;
import java.util.function.BiFunction;

/**
 * This represents a batch of numeric vectors specifically
 * The class is thread-safe
 */
public class VectorBatch extends BaseBatch<Vector<? extends Number>>
{
    private final int dimension;

    public VectorBatch(int dimension)
    {
        this.dimension = dimension;
    }

    public VectorBatch(Collection<Vector<? extends Number>> vectors, int dimension)
    {
        this(dimension);
        super.items.addAll(vectors);
    }

    public int getDimension()
    {
        return this.dimension;
    }

    /**
     * Adds a vector to the batch as long as the vector conforms to the specified dimension
     * @param vector Vector to be added to the batch
     */
    @Override
    public synchronized void add(Vector<? extends Number> vector)
    {
        if (vector.dimension() != this.dimension)
        {
            throw new IllegalArgumentException("Dimension of the vector (" + vector.dimension() + ") doesn't match the dimension of the batched vectors (" + this.dimension + ")");
        }

        super.items.add(vector);
    }

    private List<Float> aggregate(BiFunction<Float, Float, Float> function, float initialValue)
    {
        List<Float> aggregated = new ArrayList<>(Collections.nCopies(size(), initialValue));
        Iterator<Vector<? extends Number>> iterator = iterator();

        while (iterator.hasNext())
        {
            Vector<? extends Number> vector = iterator.next();

            for (int dimension = 0; dimension < this.dimension; dimension++)
            {
                aggregated.set(dimension, function.apply(aggregated.get(dimension), vector.get(dimension).floatValue()));
            }
        }

        return aggregated;
    }

    private List<Float> aggregate(BiFunction<Float, Float, Float> function)
    {
        return aggregate(function, 0.0f);
    }

    /**
     * Computes the mean vector
     * @return Centroid of the vectors in this batch
     */
    public synchronized Vector<Float> centroid()
    {
        List<Float> sum = aggregate(Float::sum);
        int batchSize = size();
        return new Vector<>(sum.stream().map(value -> value / batchSize).toList());
    }

    /**
     * Vector of sums
     * @return Summed vector
     */
    public synchronized Vector<Float> sum()
    {
        List<Float> sum = aggregate(Float::sum);
        return new Vector<>(sum);
    }

    /**
     * Computes the max vector
     * @return Max vector
     */
    public synchronized Vector<Float> max()
    {
        List<Float> max = aggregate(Math::max, Float.MIN_VALUE);
        return new Vector<>(max);
    }

    /**
     * Computes the min vector
     * @return Min vector
     */
    public synchronized Vector<Float> min()
    {
        List<Float> min = aggregate(Math::min, Float.MAX_VALUE);
        return new Vector<>(min);
    }
}
