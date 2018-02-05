package com.baidu.android.bbalbs.common.util;

import java.util.Comparator;

class c implements Comparator<a> {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public int a(a aVar, a aVar2) {
        int i = aVar2.b - aVar.b;
        return i == 0 ? (aVar.d && aVar2.d) ? 0 : aVar.d ? -1 : aVar2.d ? 1 : i : i;
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((a) obj, (a) obj2);
    }
}
