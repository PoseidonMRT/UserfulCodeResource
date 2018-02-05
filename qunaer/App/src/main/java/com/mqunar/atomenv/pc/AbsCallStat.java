package com.mqunar.atomenv.pc;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbsCallStat implements Serializable {
    private AtomicInteger count = new AtomicInteger(1);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String key();

    public abstract String toCallString();

    public abstract int type();

    public int count() {
        return this.count.get();
    }

    public final void inc() {
        this.count.incrementAndGet();
    }
}
