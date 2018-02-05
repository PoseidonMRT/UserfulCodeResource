package com.mqunar.hy.res.utils;

import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
    private static final long serialVersionUID = -7754090372962971524L;
    final List<E> list;

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
        return super.containsAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ Object[] toArray() {
        return super.toArray();
    }

    public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
        return super.toArray(objArr);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public SynchronizedList(List<E> list) {
        super(list);
        this.list = list;
    }

    public SynchronizedList(List<E> list, Object obj) {
        super(list, obj);
        this.list = list;
    }

    public void add(int i, E e) {
        synchronized (this.mutex) {
            this.list.add(i, e);
        }
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        boolean addAll;
        synchronized (this.mutex) {
            addAll = this.list.addAll(i, collection);
        }
        return addAll;
    }

    public boolean equals(Object obj) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(obj);
        }
        return equals;
    }

    public E get(int i) {
        E e;
        synchronized (this.mutex) {
            e = this.list.get(i);
        }
        return e;
    }

    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    public int indexOf(Object obj) {
        int i = 0;
        synchronized (this.mutex) {
            int size = this.list.size();
            Object[] objArr = new Object[size];
            this.list.toArray(objArr);
        }
        if (obj != null) {
            while (i < size) {
                if (obj.equals(objArr[i])) {
                    return i;
                }
                i++;
            }
        } else {
            while (i < size) {
                if (objArr[i] == null) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        synchronized (this.mutex) {
            int size = this.list.size();
            Object[] objArr = new Object[size];
            this.list.toArray(objArr);
        }
        if (obj != null) {
            for (size--; size >= 0; size--) {
                if (obj.equals(objArr[size])) {
                    return size;
                }
            }
        } else {
            for (size--; size >= 0; size--) {
                if (objArr[size] == null) {
                    return size;
                }
            }
        }
        return -1;
    }

    public ListIterator<E> listIterator() {
        ListIterator<E> listIterator;
        synchronized (this.mutex) {
            listIterator = this.list.listIterator();
        }
        return listIterator;
    }

    public ListIterator<E> listIterator(int i) {
        ListIterator<E> listIterator;
        synchronized (this.mutex) {
            listIterator = this.list.listIterator(i);
        }
        return listIterator;
    }

    public E remove(int i) {
        E remove;
        synchronized (this.mutex) {
            remove = this.list.remove(i);
        }
        return remove;
    }

    public E set(int i, E e) {
        E e2;
        synchronized (this.mutex) {
            e2 = this.list.set(i, e);
        }
        return e2;
    }

    public List<E> subList(int i, int i2) {
        List synchronizedList;
        synchronized (this.mutex) {
            synchronizedList = new SynchronizedList(this.list.subList(i, i2), this.mutex);
        }
        return synchronizedList;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        synchronized (this.mutex) {
            objectOutputStream.defaultWriteObject();
        }
    }

    private Object readResolve() {
        if (this.list instanceof RandomAccess) {
            return new SynchronizedRandomAccessList(this.list, this.mutex);
        }
        return this;
    }
}
