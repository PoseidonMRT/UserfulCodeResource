package com.mqunar.core.android.content.res;

import java.io.IOException;

class b {
    static final void a(c cVar, int i) {
        int b = cVar.b();
        if (b != i) {
            throw new IOException("Expected chunk of type 0x" + Integer.toHexString(i) + ", read 0x" + Integer.toHexString(b) + ".");
        }
    }
}
