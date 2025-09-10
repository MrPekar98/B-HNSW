package dk.aau.cs.dess.index;

public interface Index<K, V>
{
    void insert(K key, V value);
    void remove(K key);
    V lookup(K key);
    boolean exists(K key);
    int size();
    void clear();
}
