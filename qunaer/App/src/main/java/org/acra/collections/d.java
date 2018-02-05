package org.acra.collections;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class d<E> implements Serializable, Set<E> {
    private final Set<E> a;

    public final class a<E> {
        private final Set<E> a = new HashSet();

        public void a(E e) {
            this.a.add(e);
        }

        public d<E> a() {
            return new d(this.a);
        }
    }

    public d(E... eArr) {
        this(Arrays.asList(eArr));
    }

    public d(Collection<E> collection) {
        this.a = new HashSet(collection);
    }

    public boolean add(E e) {
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

    public boolean isEmpty() {
        return this.a.isEmpty();
    }

    @NonNull
    public Iterator<E> iterator() {
        return new e(this.a.iterator());
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

    public int size() {
        return this.a.size();
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
