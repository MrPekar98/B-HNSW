package dk.aau.cs.dess.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The simplest definition of a batch, which is basically a simplified Collection<E>
 * This class is thread-safe
 */
class SimpleBatch<E> implements Batch<E>
{
    private final Set<E> items = new HashSet<>();

    SimpleBatch(Collection<E> items)
    {
        this.items.addAll(items);
    }

    @Override
    public Iterator<E> iterator()
    {
        return this.items.iterator();
    }

    @Override
    public synchronized void add(E item)
    {
        this.items.add(item);
    }

    @Override
    public synchronized void remove(E item)
    {
        this.items.remove(item);
    }

    @Override
    public synchronized int size()
    {
        return this.items.size();
    }
}
