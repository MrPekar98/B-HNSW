package dk.aau.cs.dess.search;

import java.util.*;

public class ResultSet
{
    //private final Queue<Result> resultSet;
    private final List<Result> resultSet;
    private final int topK;

    public ResultSet(int topK)
    {
        this.topK = topK;
        //this.resultSet = new PriorityQueue<>(topK);
        this.resultSet = new ArrayList<>(topK);
    }

    public ResultSet(int topK, Collection<Result> results)
    {
        this(topK);
        this.resultSet.addAll(results);
    }

    public void addResult(Result result)
    {
        this.resultSet.add(result);
    }

    public List<Result> getTopK(int k)
    {
        List<Result> allResults = getResults();
        return allResults.subList(0, k);
    }

    public List<Result> getResults()
    {
        List<Result> results = new ArrayList<>(this.resultSet.size());
        results.addAll(this.resultSet);
        Collections.sort(results);
        return results.subList(0, this.topK);
    }

    public int getTopK()
    {
        return this.topK;
    }
}
