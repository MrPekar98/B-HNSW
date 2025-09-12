package dk.aau.cs.dess.search;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A batch is a simple collection of unique items
 */
public abstract class BaseBatch<T> implements Batch<T>
{
    protected final Set<T> items = new HashSet<>();

    @Override
    public synchronized void add(T item)
    {
        this.items.add(item);
    }

    @Override
    public synchronized void remove(T item)
    {
        this.items.remove(item);
    }

    @Override
    public synchronized int size()
    {
        return this.items.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.items.iterator();
    }

    public Set<T> getItems()
    {
        return this.items;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof BaseBatch<?> other))
        {
            return false;
        }

        return this.items.equals(other.items);
    }
}
