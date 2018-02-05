package qunar.sdk.mapapi.utils.projectTransform;

public class WSGPointer extends GeoPointer {
    public WSGPointer(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public GCJPointer toGCJPointer() {
        if (TransformUtil.outOfChina(this.latitude, this.longitude)) {
            return new GCJPointer(this.latitude, this.longitude);
        }
        double[] delta = TransformUtil.delta(this.latitude, this.longitude);
        return new GCJPointer(this.latitude + delta[0], this.longitude + delta[1]);
    }
}
