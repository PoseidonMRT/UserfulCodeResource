package com.mqunar.hy.res.utils;

import java.util.List;
import java.util.RandomAccess;

class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
    private static final long serialVersionUID = 1530674583602358482L;

    SynchronizedRandomAccessList(List<E> list) {
        super(list);
    }

    SynchronizedRandomAccessList(List<E> list, Object obj) {
        super(list, obj);
    }

    public List<E> subList(int i, int i2) {
        List synchronizedRandomAccessList;
        synchronized (this.mutex) {
            synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList(i, i2), this.mutex);
        }
        return synchronizedRandomAccessList;
    }

    private Object writeReplace() {
        return new SynchronizedList(this.list);
    }
}
