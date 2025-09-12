package dk.aau.cs.dess.search;

public record Result(Integer key, double score) implements Comparable<Result>
{
    @Override
    public int compareTo(Result o)
    {
        return Double.compare(o.score(), this.score);
    }

    @Override
    public String toString()
    {
        return "[" + this.key + ", " + this.score + "]";
    }
}
