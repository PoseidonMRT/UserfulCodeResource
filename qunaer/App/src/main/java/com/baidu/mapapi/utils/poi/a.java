package com.baidu.mapapi.utils.poi;

import android.content.Context;
import android.util.Log;
import com.baidu.mapapi.http.HttpClient.HttpStateError;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch.AnonymousClass1;
import com.baidu.platform.comapi.pano.c;

final class a implements com.baidu.platform.comapi.pano.a.a<c> {
    final /* synthetic */ Context a;

    a(Context context) {
        this.a = context;
    }

    public void a(HttpStateError httpStateError) {
        switch (AnonymousClass1.b[httpStateError.ordinal()]) {
            case 1:
                Log.d("baidumapsdk", "current network is not available");
                return;
            case 2:
                Log.d("baidumapsdk", "network inner error, please check network");
                return;
            default:
                return;
        }
    }

    public void a(c cVar) {
        if (cVar == null) {
            Log.d("baidumapsdk", "pano info is null");
            return;
        }
        switch (AnonymousClass1.a[cVar.a().ordinal()]) {
            case 1:
                Log.d("baidumapsdk", "pano uid is error, please check param poi uid");
                return;
            case 2:
                Log.d("baidumapsdk", "pano id not found for this poi point");
                return;
            case 3:
                Log.d("baidumapsdk", "please check ak for permission");
                return;
            case 4:
                if (cVar.c() == 1) {
                    try {
                        BaiduMapPoiSearch.b(cVar.b(), this.a);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Log.d("baidumapsdk", "this point do not support for pano show");
                return;
            default:
                return;
        }
    }
}
