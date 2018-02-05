package com.mqunar.hy.res.utils;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

class SynchronizedCollection<E> implements Serializable, Collection<E> {
    private static final long serialVersionUID = 3053995032091335093L;
    final Collection<E> c;
    final Object mutex;

    SynchronizedCollection(Collection<E> collection) {
        this.c = collection;
        this.mutex = this;
    }

    SynchronizedCollection(Collection<E> collection, Object obj) {
        this.c = collection;
        this.mutex = obj;
    }

    public boolean add(E e) {
        boolean add;
        synchronized (this.mutex) {
            add = this.c.add(e);
        }
        return add;
    }

    public boolean addAll(Collection<? extends E> collection) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.c.addAll(collection);
        }
        return addAll;
    }

    public void clear() {
        synchronized (this.mutex) {
            this.c.clear();
        }
    }

    public boolean contains(Object obj) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.c.contains(obj);
        }
        return contains;
    }

    public boolean containsAll(Collection<?> collection) {
        boolean containsAll;
        synchronized (this.mutex) {
            containsAll = this.c.containsAll(collection);
        }
        return containsAll;
    }

    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.c.isEmpty();
        }
        return isEmpty;
    }

    public Iterator<E> iterator() {
        Iterator<E> it;
        synchronized (this.mutex) {
            it = this.c.iterator();
        }
        return it;
    }

    public boolean remove(Object obj) {
        boolean remove;
        synchronized (this.mutex) {
            remove = this.c.remove(obj);
        }
        return remove;
    }

    public boolean removeAll(Collection<?> collection) {
        boolean removeAll;
        synchronized (this.mutex) {
            removeAll = this.c.removeAll(collection);
        }
        return removeAll;
    }

    public boolean retainAll(Collection<?> collection) {
        boolean retainAll;
        synchronized (this.mutex) {
            retainAll = this.c.retainAll(collection);
        }
        return retainAll;
    }

    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.c.size();
        }
        return size;
    }

    public Object[] toArray() {
        Object[] toArray;
        synchronized (this.mutex) {
            toArray = this.c.toArray();
        }
        return toArray;
    }

    public String toString() {
        String obj;
        synchronized (this.mutex) {
            obj = this.c.toString();
        }
        return obj;
    }

    public <T> T[] toArray(T[] tArr) {
        T[] toArray;
        synchronized (this.mutex) {
            toArray = this.c.toArray(tArr);
        }
        return toArray;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        synchronized (this.mutex) {
            objectOutputStream.defaultWriteObject();
        }
    }
}
