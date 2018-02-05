package com.baidu.platform.comjni.tools;

import android.os.Bundle;
import com.baidu.mapapi.model.inner.Point;
import java.util.ArrayList;

public class a {
    public static double a(Point point, Point point2) {
        Bundle bundle = new Bundle();
        bundle.putDouble("x1", (double) point.x);
        bundle.putDouble("y1", (double) point.y);
        bundle.putDouble("x2", (double) point2.x);
        bundle.putDouble("y2", (double) point2.y);
        JNITools.GetDistanceByMC(bundle);
        return bundle.getDouble("distance");
    }

    public static com.baidu.mapapi.model.inner.a a(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putString("strkey", str);
        JNITools.TransGeoStr2ComplexPt(bundle2);
        com.baidu.mapapi.model.inner.a aVar = new com.baidu.mapapi.model.inner.a();
        Bundle bundle3 = bundle2.getBundle("map_bound");
        if (bundle3 != null) {
            bundle = bundle3.getBundle("ll");
            if (bundle != null) {
                aVar.b = new Point((int) bundle.getDouble("ptx"), (int) bundle.getDouble("pty"));
            }
            bundle3 = bundle3.getBundle("ru");
            if (bundle3 != null) {
                aVar.c = new Point((int) bundle3.getDouble("ptx"), (int) bundle3.getDouble("pty"));
            }
        }
        ParcelItem[] parcelItemArr = (ParcelItem[]) bundle2.getParcelableArray("poly_line");
        for (ParcelItem bundle4 : parcelItemArr) {
            if (aVar.d == null) {
                aVar.d = new ArrayList();
            }
            bundle = bundle4.getBundle();
            if (bundle != null) {
                ParcelItem[] parcelItemArr2 = (ParcelItem[]) bundle.getParcelableArray("point_array");
                ArrayList arrayList = new ArrayList();
                for (ParcelItem bundle5 : parcelItemArr2) {
                    Bundle bundle6 = bundle5.getBundle();
                    if (bundle6 != null) {
                        arrayList.add(new Point((int) bundle6.getDouble("ptx"), (int) bundle6.getDouble("pty")));
                    }
                }
                arrayList.trimToSize();
                aVar.d.add(arrayList);
            }
        }
        aVar.d.trimToSize();
        aVar.a = (int) bundle2.getDouble("type");
        return aVar;
    }

    public static String a() {
        return JNITools.GetToken();
    }

    public static void a(boolean z, int i) {
        JNITools.openLogEnable(z, i);
    }

    public static void b() {
        JNITools.initClass(new Bundle(), 0);
    }
}
