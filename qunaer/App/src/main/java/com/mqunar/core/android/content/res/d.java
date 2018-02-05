package com.mqunar.core.android.content.res;

import android.support.v4.internal.view.SupportMenu;
import java.io.IOException;

class d {
    private int[] a;
    private int[] b;
    private int[] c;
    private int[] d;

    static d a(c cVar) {
        b.a(cVar, 1835009);
        int b = cVar.b();
        int b2 = cVar.b();
        int b3 = cVar.b();
        cVar.b();
        int b4 = cVar.b();
        int b5 = cVar.b();
        d dVar = new d();
        dVar.a = cVar.b(b2);
        if (b3 != 0) {
            dVar.c = cVar.b(b3);
        }
        if (b5 == 0) {
            b2 = b;
        } else {
            b2 = b5;
        }
        b2 -= b4;
        if (b2 % 4 != 0) {
            throw new IOException("String data size is not multiple of 4 (" + b2 + ").");
        }
        dVar.b = cVar.b(b2 / 4);
        if (b5 != 0) {
            b2 = b - b5;
            if (b2 % 4 != 0) {
                throw new IOException("Style data size is not multiple of 4 (" + b2 + ").");
            }
            dVar.d = cVar.b(b2 / 4);
        }
        return dVar;
    }

    String a(int i) {
        if (i < 0 || this.a == null || i >= this.a.length) {
            return null;
        }
        int i2 = this.a[i];
        int a = a(this.b, i2);
        StringBuilder stringBuilder = new StringBuilder(a);
        while (a != 0) {
            i2 += 2;
            stringBuilder.append((char) a(this.b, i2));
            a--;
        }
        return stringBuilder.toString();
    }

    int a(String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i != this.a.length; i++) {
            int i2 = this.a[i];
            int a = a(this.b, i2);
            if (a == str.length()) {
                int i3 = i2;
                i2 = 0;
                while (i2 != a) {
                    i3 += 2;
                    if (str.charAt(i2) != a(this.b, i3)) {
                        break;
                    }
                    i2++;
                }
                if (i2 == a) {
                    return i;
                }
            }
        }
        return -1;
    }

    private d() {
    }

    private static final int a(int[] iArr, int i) {
        int i2 = iArr[i / 4];
        if ((i % 4) / 2 == 0) {
            return i2 & SupportMenu.USER_MASK;
        }
        return i2 >>> 16;
    }
}
