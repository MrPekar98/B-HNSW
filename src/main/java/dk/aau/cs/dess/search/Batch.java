package dk.aau.cs.dess.search;

import java.util.Collection;

public interface Batch<T> extends Iterable<T>
{
    static <E> Batch<E> toBatch(Collection<E> collection)
    {
        return new SimpleBatch<>(collection);
    }

    void add(T item);
    void remove(T item);
    int size();
}
