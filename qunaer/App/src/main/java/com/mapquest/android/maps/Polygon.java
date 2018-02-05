package com.mapquest.android.maps;

import java.util.ArrayList;
import java.util.List;

class Polygon {
    private List<GeoPoint> data;

    Polygon() {
    }

    public void setData(List<GeoPoint> list) {
        this.data = list;
        validateData();
    }

    public List<GeoPoint> getData() {
        if (this.data == null) {
            this.data = new ArrayList();
        }
        return this.data;
    }

    private void validateData() {
        if (this.data != null && this.data.size() > 1) {
            GeoPoint geoPoint = (GeoPoint) this.data.get(0);
            if (!geoPoint.equals((GeoPoint) this.data.get(this.data.size() - 1))) {
                this.data.add(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
            }
        }
    }

    public boolean contains(GeoPoint geoPoint) {
        int i = 0;
        int size = this.data.size() - 1;
        boolean z = false;
        double longitudeE6 = (double) geoPoint.getLongitudeE6();
        double latitudeE6 = (double) geoPoint.getLatitudeE6();
        int i2 = 0;
        while (i2 < size) {
            boolean z2;
            int i3 = i + 1;
            if (i3 == size) {
                i = 0;
            } else {
                i = i3;
            }
            if (((((double) ((GeoPoint) this.data.get(i2)).getLatitudeE6()) >= latitudeE6 || ((double) ((GeoPoint) this.data.get(r1)).getLatitudeE6()) < latitudeE6) && (((double) ((GeoPoint) this.data.get(r1)).getLatitudeE6()) >= latitudeE6 || ((double) ((GeoPoint) this.data.get(i2)).getLatitudeE6()) < latitudeE6)) || ((double) ((GeoPoint) this.data.get(i2)).getLongitudeE6()) + (((latitudeE6 - ((double) ((GeoPoint) this.data.get(i2)).getLatitudeE6())) / ((double) (((GeoPoint) this.data.get(r1)).getLatitudeE6() - ((GeoPoint) this.data.get(i2)).getLatitudeE6()))) * ((double) (((GeoPoint) this.data.get(r1)).getLongitudeE6() - ((GeoPoint) this.data.get(i2)).getLongitudeE6()))) >= longitudeE6) {
                z2 = z;
            } else if (z) {
                z2 = false;
            } else {
                z2 = true;
            }
            i2++;
            z = z2;
        }
        return z;
    }
}
