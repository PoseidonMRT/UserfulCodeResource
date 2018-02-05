package org.acra.collections;

import java.util.ListIterator;

class f<E> implements ListIterator<E> {
    private final ListIterator<E> a;

    f(ListIterator<E> listIterator) {
        this.a = listIterator;
    }

    public void add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return this.a.hasNext();
    }

    public boolean hasPrevious() {
        return this.a.hasPrevious();
    }

    public E next() {
        return this.a.next();
    }

    public int nextIndex() {
        return this.a.nextIndex();
    }

    public E previous() {
        return this.a.previous();
    }

    public int previousIndex() {
        return this.a.previousIndex();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void set(E e) {
        throw new UnsupportedOperationException();
    }
}
