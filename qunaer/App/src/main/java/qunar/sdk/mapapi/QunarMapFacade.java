package qunar.sdk.mapapi;

import android.content.Context;
import android.view.ViewGroup;
import com.baidu.mapapi.map.MapView;
import qunar.sdk.mapapi.baiduMapImp.BaiduGeoCoder;
import qunar.sdk.mapapi.baiduMapImp.BaiduMapControl;
import qunar.sdk.mapapi.baiduMapImp.BaiduMapStrategy;
import qunar.sdk.mapapi.baiduMapImp.BaiduMapView;
import qunar.sdk.mapapi.mapquest.MapQuestControl;
import qunar.sdk.mapapi.mapquest.MapQuestStrategy;
import qunar.sdk.mapapi.mapquest.MapQuestView;

public class QunarMapFacade {
    @Deprecated
    public static QunarMapType mapType = QunarMapType.BAIDU;

    public static IMapView initMapView(Context context, QunarMapInitOptions qunarMapInitOptions) {
        switch (qunarMapInitOptions.mapType) {
            case BAIDU:
                return new BaiduMapView(context, qunarMapInitOptions);
            case MAPQUEST:
                return new MapQuestView(context, qunarMapInitOptions);
            default:
                return null;
        }
    }

    public static QunarMap initMap(ViewGroup viewGroup, QunarMapType qunarMapType) {
        switch (qunarMapType) {
            case BAIDU:
                if (viewGroup instanceof MapView) {
                    return new BaiduMapStrategy(((MapView) viewGroup).getMap());
                }
                return null;
            case MAPQUEST:
                if (viewGroup instanceof com.mapquest.android.maps.MapView) {
                    return new MapQuestStrategy((com.mapquest.android.maps.MapView) viewGroup);
                }
                return null;
            default:
                return null;
        }
    }

    public static QunarMapControl initMapControl(QunarMapView qunarMapView) {
        if (qunarMapView == null) {
            return null;
        }
        switch (qunarMapView.getQunarMapType()) {
            case BAIDU:
                return new BaiduMapControl((BaiduMapStrategy) qunarMapView.getQunarMap());
            case GAODE:
                return null;
            case MAPQUEST:
                return new MapQuestControl((com.mapquest.android.maps.MapView) qunarMapView.getDisplayMap());
            default:
                return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static qunar.sdk.mapapi.QunarRoutePlanSearch initRoutePlan(qunar.sdk.mapapi.QunarMapView r4) {
        /*
        r1 = 0;
        r0 = r4.getDisplayMap();
        if (r0 != 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r2 = qunar.sdk.mapapi.QunarMapFacade.AnonymousClass1.$SwitchMap$qunar$sdk$mapapi$QunarMapType;
        r3 = r4.getQunarMapType();
        r3 = r3.ordinal();
        r2 = r2[r3];
        switch(r2) {
            case 1: goto L_0x001a;
            case 2: goto L_0x002b;
            case 3: goto L_0x002d;
            default: goto L_0x0017;
        };
    L_0x0017:
        r0 = r1;
    L_0x0018:
        r1 = r0;
        goto L_0x0007;
    L_0x001a:
        r2 = r0 instanceof com.baidu.mapapi.map.MapView;
        if (r2 == 0) goto L_0x0017;
    L_0x001e:
        r0 = (com.baidu.mapapi.map.MapView) r0;
        r1 = new qunar.sdk.mapapi.baiduMapImp.BDRoutePlanSearch;
        r0 = r0.getMap();
        r1.<init>(r0);
        r0 = r1;
        goto L_0x0018;
    L_0x002b:
        r0 = r1;
        goto L_0x0018;
    L_0x002d:
        r2 = r0 instanceof com.mapquest.android.maps.MapView;
        if (r2 == 0) goto L_0x0017;
    L_0x0031:
        r0 = (com.mapquest.android.maps.MapView) r0;
        r1 = new qunar.sdk.mapapi.mapquest.CustomerRouteManager;
        r1.<init>(r0);
        r0 = r1;
        goto L_0x0018;
        */
        throw new UnsupportedOperationException("Method not decompiled: qunar.sdk.mapapi.QunarMapFacade.initRoutePlan(qunar.sdk.mapapi.QunarMapView):qunar.sdk.mapapi.QunarRoutePlanSearch");
    }

    public static QunarGeoCoder initQunarGeoCoder(QunarMapType qunarMapType) {
        switch (qunarMapType) {
            case BAIDU:
            case MAPQUEST:
                return new BaiduGeoCoder();
            default:
                return null;
        }
    }

    @Deprecated
    public static QunarGeoCoder initQunarGeoCoder() {
        switch (mapType) {
            case BAIDU:
            case MAPQUEST:
                return new BaiduGeoCoder();
            default:
                return null;
        }
    }
}
