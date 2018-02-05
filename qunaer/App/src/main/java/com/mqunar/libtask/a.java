package com.mqunar.libtask;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

final class a implements ThreadFactory {
    private final AtomicInteger a = new AtomicInteger(1);

    a() {
    }

    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "QTask #" + this.a.getAndIncrement());
    }
}
