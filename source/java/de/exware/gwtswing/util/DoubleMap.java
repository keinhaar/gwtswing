package de.exware.gwtswing.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a Map that allows you to quickly lookup values by key, but also keys for values.
 * @author martin
 *
 */
public class DoubleMap<K, V> implements Map<K, V>
{
    private Map<K,V> forwardMap = new HashMap<>();
    private Map<V,K> reverseMap = new HashMap<>();
    
    @Override
    public int size()
    {
        return forwardMap.size();
    }

    @Override
    public boolean isEmpty()
    {
        return forwardMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return forwardMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return reverseMap.containsKey(value);
    }

    @Override
    public V get(Object key)
    {
        return forwardMap.get(key);
    }

    public K reverseGet(Object value)
    {
        return reverseMap.get(value);
    }

    @Override
    public V put(K key, V value)
    {
        reverseMap.put(value, key);
        return forwardMap.put(key, value);
    }

    @Override
    public V remove(Object key)
    {
        V v = forwardMap.remove(key);
        reverseMap.remove(v);
        return v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m)
    {
        forwardMap.putAll(m);
        for(K k : m.keySet())
        {
            reverseMap.put(m.get(k), k);
        }
    }

    @Override
    public void clear()
    {
        forwardMap.clear();
        reverseMap.clear();
    }

    @Override
    public Set<K> keySet()
    {
        return forwardMap.keySet();
    }

    @Override
    public Collection<V> values()
    {
        return forwardMap.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        return forwardMap.entrySet();
    }
}
