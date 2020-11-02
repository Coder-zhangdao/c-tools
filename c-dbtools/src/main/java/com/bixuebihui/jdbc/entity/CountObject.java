package com.bixuebihui.jdbc.entity;
import java.io.Serializable;

/**
 * <p>CountObject class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class CountObject<V> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7022753581609128376L;

    private String key;
    private long count;
    private V value;

    /**
     * <p>Getter for the field <code>count</code>.</p>
     *
     * @return a long.
     */
    public long getCount() {
        return count;
    }

    /**
     * <p>Setter for the field <code>count</code>.</p>
     *
     * @param count a long.
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a V object.
     */
    public V getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a V object.
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>Setter for the field <code>key</code>.</p>
     *
     * @param key a {@link java.lang.String} object.
     */
    public void setKey(String key) {
        this.key = key;
    }

}
