package org.acra.collections;

import android.support.annotation.NonNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public final class a<E> extends LinkedList<E> {
    private final int a;

    public a(int i) {
        this.a = i;
    }

    public boolean add(E e) {
        if (size() == this.a) {
            removeFirst();
        }
        return super.add(e);
    }

    public void add(int i, E e) {
        if (size() == this.a) {
            removeFirst();
        }
        super.add(i, e);
    }

    public boolean addAll(@NonNull Collection<? extends E> collection) {
        int i;
        int size = collection.size();
        if (size > this.a) {
            LinkedList linkedList = new LinkedList(collection);
            for (i = 0; i < size - this.a; i++) {
                linkedList.removeFirst();
            }
            collection = linkedList;
        }
        i = (size() + collection.size()) - this.a;
        if (i > 0) {
            removeRange(0, i);
        }
        return super.addAll(collection);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        if (i == size()) {
            return super.addAll(i, collection);
        }
        throw new UnsupportedOperationException();
    }

    public void addFirst(E e) {
        throw new UnsupportedOperationException();
    }

    public void addLast(E e) {
        add(e);
    }

    @NonNull
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            stringBuilder.append(it.next().toString());
        }
        return stringBuilder.toString();
    }

    public boolean offer(E e) {
        return add(e);
    }

    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e) {
        return add(e);
    }

    public void push(E e) {
        add(e);
    }
}
