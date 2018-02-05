package com.mapquest.android.maps;

public class GeoPoint {
    private int latitude;
    private int longitude;

    public GeoPoint(GeoPoint geoPoint) {
        this.latitude = geoPoint.getLatitudeE6();
        this.longitude = geoPoint.getLongitudeE6();
    }

    public GeoPoint(int i, int i2) {
        if ((i >= 1000 || i <= -1000) && i2 < 1000 && i2 > -1000) {
            setLatitudeE6(i);
            setLongitudeE6(i2);
        } else {
            setLatitudeE6(i);
            setLongitudeE6(i2);
        }
    }

    public GeoPoint(double d, double d2) {
        setLatitudeE6(new Double(d * 1000000.0d).intValue());
        setLongitudeE6(new Double(d2 * 1000000.0d).intValue());
    }

    public int getLatitudeE6() {
        return this.latitude;
    }

    public double getLatitude() {
        return ((double) this.latitude) * 1.0E-6d;
    }

    private void setLatitudeE6(int i) {
        this.latitude = i;
    }

    public int getLongitudeE6() {
        return this.longitude;
    }

    public double getLongitude() {
        return ((double) this.longitude) * 1.0E-6d;
    }

    private void setLongitudeE6(int i) {
        int i2 = i;
        while (((double) i2) > 1.8E8d) {
            i2 = (int) (((double) i2) - 3.6E8d);
        }
        while (((double) i2) < -1.8E8d) {
            i2 = (int) (((double) i2) + 3.6E8d);
        }
        this.longitude = i2;
    }

    public String toString() {
        return "Lat/Lng: " + getLatitude() + "," + getLongitude();
    }

    public int hashCode() {
        return ((this.latitude + 31) * 31) + this.longitude;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GeoPoint geoPoint = (GeoPoint) obj;
        if (this.latitude == geoPoint.latitude && this.longitude == geoPoint.longitude) {
            return true;
        }
        return false;
    }
}
