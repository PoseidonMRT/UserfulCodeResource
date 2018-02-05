package org.acra.collections;

import java.util.Iterator;

class e<E> implements Iterator<E> {
    private final Iterator<E> a;

    e(Iterator<E> it) {
        this.a = it;
    }

    public boolean hasNext() {
        return this.a.hasNext();
    }

    public E next() {
        return this.a.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
