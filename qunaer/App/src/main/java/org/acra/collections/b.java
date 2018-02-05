package org.acra.collections;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class b<E> implements Serializable, List<E> {
    private final List<E> a;

    public b(E... eArr) {
        this(Arrays.asList(eArr));
    }

    public b(Collection<E> collection) {
        this.a = new ArrayList(collection);
    }

    public void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int i, @NonNull Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(@NonNull Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object obj) {
        return this.a.contains(obj);
    }

    public boolean containsAll(@NonNull Collection<?> collection) {
        return this.a.containsAll(collection);
    }

    public E get(int i) {
        return this.a.get(i);
    }

    public int indexOf(Object obj) {
        return this.a.indexOf(obj);
    }

    public boolean isEmpty() {
        return this.a.isEmpty();
    }

    @NonNull
    public Iterator<E> iterator() {
        return new e(this.a.iterator());
    }

    public int lastIndexOf(Object obj) {
        return this.a.lastIndexOf(obj);
    }

    public ListIterator<E> listIterator() {
        return new f(this.a.listIterator());
    }

    @NonNull
    public ListIterator<E> listIterator(int i) {
        return new f(this.a.listIterator(i));
    }

    public E remove(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(@NonNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(@NonNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.a.size();
    }

    @NonNull
    public List<E> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public Object[] toArray() {
        return this.a.toArray();
    }

    @NonNull
    public <T> T[] toArray(@NonNull T[] tArr) {
        return this.a.toArray(tArr);
    }
}
