package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double LoadFactor;
    private Set<K> HashSet;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap()
    {
        buckets = createTable(16);
        LoadFactor = 0.75;
        HashSet = new HashSet<>(16);
    }

    public MyHashMap(int initialSize)
    {
        buckets = createTable(initialSize);
        LoadFactor = 0.75;
        HashSet = new HashSet<>(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad)
    {
        buckets = createTable(initialSize);
        LoadFactor = maxLoad;
        HashSet = new HashSet<>(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value)
    {
        return new  Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket()
    {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize)
    {
        Collection<Node>[] Buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++)
        {
            Buckets[i] = createBucket();
        }
        return Buckets;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private void resize(int newCapacity) {
        Collection<Node>[] oldBuckets = buckets;
        buckets = createTable(newCapacity);

        for (Collection<Node> bucket : oldBuckets) {
            for (Node node : bucket) {
                int index = (node.key.hashCode() & 0x7FFFFFFF) % newCapacity;
                buckets[index].add(node);   // 注意：直接插入，不调用 put()
            }
        }
    }

    @Override
    public V get(K key)
    {
        int index = (key.hashCode() & 0x7FFFFFFF) % buckets.length;
        for  (Node bucket : buckets[index])
        {
            if(bucket.key.equals(key))
            {
                return bucket.value;
            }
        }
        return null;
    }

    @Override
    public void clear()
    {
        buckets = createTable(buckets.length);
        HashSet.clear();
    }
    private int hash(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % buckets.length;
    }

    @Override
    public boolean containsKey(K key)
    {
        return HashSet.contains(key);
    }

    @Override
    public int size()
    {
        return HashSet.size();
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value; // 覆盖旧值
                return;
            }
        }

        // 插入新节点
        buckets[index].add(createNode(key, value));
        HashSet.add(key);

        // 判断是否需要扩容
        if ((double) HashSet.size() / buckets.length > LoadFactor) {
            resize(buckets.length * 2);
        }
    }

    @Override
    public Set<K> keySet()
    {
        return new HashSet<>(HashSet);
    }

    @Override
    public Iterator<K> iterator() //后面改成返回内部私有类迭代器
    {
        return HashSet.iterator();
    }


    @Override
    public V remove(K key)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value)
    {
        throw new UnsupportedOperationException();
    }
}
