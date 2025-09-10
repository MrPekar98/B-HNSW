package dk.aau.cs.dess.search;

import dk.aau.cs.dess.index.Index;
import dk.aau.cs.dess.structurs.Vector;

public interface NearestNeighbor extends Index<Integer, Vector<? extends Number>>
{
    ResultSet search(Vector<? extends Number> input, int topK);
}
