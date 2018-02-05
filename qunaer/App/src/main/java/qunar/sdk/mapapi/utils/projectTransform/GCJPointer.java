package qunar.sdk.mapapi.utils.projectTransform;

public class GCJPointer extends GeoPointer {
    public GCJPointer(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public WSGPointer toWSGPointer() {
        if (TransformUtil.outOfChina(this.latitude, this.longitude)) {
            return new WSGPointer(this.latitude, this.longitude);
        }
        double[] delta = TransformUtil.delta(this.latitude, this.longitude);
        return new WSGPointer(this.latitude - delta[0], this.longitude - delta[1]);
    }

    public WSGPointer toExactWSGPointer() {
        double d = 0.01d + this.longitude;
        WSGPointer wSGPointer = null;
        double d2 = this.latitude + 0.01d;
        double d3 = this.longitude - 0.01d;
        int i = 0;
        double d4 = this.latitude - 0.01d;
        double d5 = d2;
        while (i < 30) {
            double d6 = (d4 + d5) / 2.0d;
            double d7 = (d3 + d) / 2.0d;
            WSGPointer wSGPointer2 = new WSGPointer(d6, d7);
            GCJPointer toGCJPointer = wSGPointer2.toGCJPointer();
            double latitude = toGCJPointer.getLatitude() - getLatitude();
            double longitude = toGCJPointer.getLongitude() - getLongitude();
            if (Math.abs(latitude) < 1.0E-6d && Math.abs(longitude) < 1.0E-6d) {
                return wSGPointer2;
            }
            if (latitude > 0.0d) {
                d5 = d6;
            } else {
                d4 = d6;
            }
            if (longitude > 0.0d) {
                d = d3;
            } else {
                double d8 = d;
                d = d7;
                d7 = d8;
            }
            i++;
            d3 = d;
            d = d7;
            wSGPointer = wSGPointer2;
        }
        return wSGPointer;
    }
}
