package com.baidu.vi;

import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class c {
    public String a;
    public int b;
    public int c;

    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a = new int[State.values().length];

        static {
            try {
                a[State.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[State.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[State.DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[State.DISCONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[State.SUSPENDED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public c(NetworkInfo networkInfo) {
        this.a = networkInfo.getTypeName();
        this.b = networkInfo.getType();
        switch (AnonymousClass1.a[networkInfo.getState().ordinal()]) {
            case 1:
                this.c = 2;
                return;
            case 2:
                this.c = 1;
                return;
            default:
                this.c = 0;
                return;
        }
    }
}
