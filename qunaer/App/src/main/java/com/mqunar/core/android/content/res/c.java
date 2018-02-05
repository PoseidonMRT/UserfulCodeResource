package com.mqunar.core.android.content.res;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class c {
    private InputStream a;
    private boolean b;
    private int c;

    c() {
    }

    c(InputStream inputStream, boolean z) {
        a(inputStream, z);
    }

    final void a(InputStream inputStream, boolean z) {
        this.a = inputStream;
        this.b = z;
        this.c = 0;
    }

    final void a() {
        if (this.a != null) {
            try {
                this.a.close();
            } catch (IOException e) {
            }
            a(null, false);
        }
    }

    final int b() {
        return a(4);
    }

    final int a(int i) {
        int i2 = 0;
        if (i < 0 || i > 4) {
            throw new IllegalArgumentException();
        }
        int i3;
        int read;
        if (this.b) {
            i3 = (i - 1) * 8;
            while (i3 >= 0) {
                read = this.a.read();
                if (read == -1) {
                    throw new EOFException();
                }
                this.c++;
                read = (read << i3) | i2;
                i3 -= 8;
                i2 = read;
            }
        } else {
            int i4 = i * 8;
            i3 = 0;
            while (i3 != i4) {
                read = this.a.read();
                if (read == -1) {
                    throw new EOFException();
                }
                this.c++;
                read = (read << i3) | i2;
                i3 += 8;
                i2 = read;
            }
        }
        return i2;
    }

    final int[] b(int i) {
        int[] iArr = new int[i];
        a(iArr, 0, i);
        return iArr;
    }

    final void a(int[] iArr, int i, int i2) {
        while (i2 > 0) {
            int i3 = i + 1;
            iArr[i] = b();
            i2--;
            i = i3;
        }
    }

    final void c(int i) {
        if (i > 0) {
            long skip = this.a.skip((long) i);
            this.c = (int) (((long) this.c) + skip);
            if (skip != ((long) i)) {
                throw new EOFException();
            }
        }
    }

    final void c() {
        c(4);
    }
}
