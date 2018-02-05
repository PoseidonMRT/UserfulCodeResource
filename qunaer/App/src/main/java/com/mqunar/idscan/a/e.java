package com.mqunar.idscan.a;

import android.hardware.Camera.Size;
import java.util.Comparator;

final class e implements Comparator {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public final /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
        Size size = (Size) obj;
        Size size2 = (Size) obj2;
        int i = size.height * size.width;
        int i2 = size2.height * size2.width;
        return i2 < i ? -1 : i2 > i ? 1 : 0;
    }
}
