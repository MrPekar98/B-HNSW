package dk.aau.cs.dess.search;

import dk.aau.cs.dess.index.Index;
import dk.aau.cs.dess.structures.Vector;

public interface NearestNeighbor<Q, R> extends Index<Integer, Vector<? extends Number>>
{
    R search(Q query, int topK);
}
