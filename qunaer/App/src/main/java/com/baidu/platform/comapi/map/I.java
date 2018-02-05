package com.baidu.platform.comapi.map;

import android.graphics.Point;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comjni.map.basemap.a;
import org.json.JSONException;
import org.json.JSONObject;

public class I {
    private a a;

    public I(a aVar) {
        this.a = aVar;
    }

    public Point a(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return null;
        }
        Point point = new Point(0, 0);
        String b = this.a.b((int) geoPoint.getLongitudeE6(), (int) geoPoint.getLatitudeE6());
        if (b == null) {
            return point;
        }
        try {
            JSONObject jSONObject = new JSONObject(b);
            point.x = jSONObject.getInt("scrx");
            point.y = jSONObject.getInt("scry");
            return point;
        } catch (JSONException e) {
            e.printStackTrace();
            return point;
        }
    }

    public GeoPoint a(int i, int i2) {
        String a = this.a.a(i, i2);
        GeoPoint geoPoint = new GeoPoint(0.0d, 0.0d);
        if (a != null) {
            try {
                JSONObject jSONObject = new JSONObject(a);
                geoPoint.setLongitudeE6((double) jSONObject.getInt("geox"));
                geoPoint.setLatitudeE6((double) jSONObject.getInt("geoy"));
                return geoPoint;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
