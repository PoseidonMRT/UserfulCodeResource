package com.mqunar.libtask;

import com.mqunar.libtask.AsyncTask.Status;

/* synthetic */ class d {
    static final /* synthetic */ int[] a = new int[Status.values().length];

    static {
        try {
            a[Status.RUNNING.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[Status.FINISHED.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
    }
}
