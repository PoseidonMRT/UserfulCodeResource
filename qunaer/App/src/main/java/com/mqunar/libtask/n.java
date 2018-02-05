package com.mqunar.libtask;

/* synthetic */ class n {
    static final /* synthetic */ int[] a = new int[TaskCode.values().length];

    static {
        try {
            a[TaskCode.TASK_CANCEL.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[TaskCode.TASK_REQUEST.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            a[TaskCode.TASK_RESULT.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            a[TaskCode.TASK_PENDING.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            a[TaskCode.TASK_ERROR.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        try {
            a[TaskCode.TASK_CACHE_HIT.ordinal()] = 6;
        } catch (NoSuchFieldError e6) {
        }
    }
}
