package com.bixuebihui.algorithm;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>LRULinkedHashMap class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V>
{
    /**
     *
     */
    private static final long serialVersionUID = -1890473042891051691L;
    private final int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final transient Lock lock = new ReentrantLock();
    private transient IRemoveAction afterRemove;

    /**
     * <p>Constructor for LRULinkedHashMap.</p>
     *
     * @param maxCapacity a int.
     */
    public LRULinkedHashMap(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    /**
     * <p>Constructor for LRULinkedHashMap.</p>
     *
     * @param maxCapacity       a int.
     * @param afterRemoveAction a {@link IRemoveAction} object.
     */
    public LRULinkedHashMap(int maxCapacity, IRemoveAction afterRemoveAction) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
        this.afterRemove = afterRemoveAction;
    }


    /** {@inheritDoc} */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        boolean res = size() > maxCapacity*1.1;
        if(res && afterRemove!=null){
            afterRemove.actionAfterRemove(eldest.getValue());
        }
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }


    /**
     * <p>Setter for the field <code>afterRemove</code>.</p>
     *
     * @param afterRemove a {@link IRemoveAction} object.
     */
    public void setAfterRemove(IRemoveAction afterRemove) {
        this.afterRemove = afterRemove;
    }
}
