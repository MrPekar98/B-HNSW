package dk.aau.cs.dess.search;

public record Result(Integer key, double score) implements Comparable<Double>
{
    @Override
    public int compareTo(Double o)
    {
        return Double.compare(this.score, o);
    }
}
