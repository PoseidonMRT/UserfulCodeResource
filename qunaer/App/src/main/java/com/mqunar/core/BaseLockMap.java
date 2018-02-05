package com.mqunar.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BaseLockMap<K, V> {
    private ReentrantReadWriteLock a = new ReentrantReadWriteLock();
    protected Map<K, V> map = new LinkedHashMap();

    protected final void readLock() {
        this.a.readLock().lock();
    }

    protected final void readUnlock() {
        this.a.readLock().unlock();
    }

    protected final void writeLock() {
        this.a.writeLock().lock();
    }

    protected final void writeUnlock() {
        this.a.writeLock().unlock();
    }

    public boolean isWriteLocked() {
        return this.a.isWriteLocked();
    }

    public void put(K k, V v) {
        writeLock();
        this.map.put(k, v);
        writeUnlock();
    }

    public void putAll(Map<K, V> map) {
        writeLock();
        this.map.putAll(map);
        writeUnlock();
    }

    public V get(K k) {
        readLock();
        V v = this.map.get(k);
        readUnlock();
        return v;
    }

    public Map<K, V> mapCopy() {
        readLock();
        Map linkedHashMap = new LinkedHashMap(this.map);
        readUnlock();
        return linkedHashMap;
    }

    public List<K> copyKeys() {
        readLock();
        List arrayList = new ArrayList(this.map.keySet());
        readUnlock();
        return arrayList;
    }

    public boolean hasItem(K k) {
        readLock();
        boolean containsKey = this.map.containsKey(k);
        readUnlock();
        return containsKey;
    }

    public int size() {
        readLock();
        int size = this.map.size();
        readUnlock();
        return size;
    }
}
