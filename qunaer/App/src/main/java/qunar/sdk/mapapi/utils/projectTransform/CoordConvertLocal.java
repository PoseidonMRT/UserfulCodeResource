package qunar.sdk.mapapi.utils.projectTransform;

public class CoordConvertLocal {
    public static final double REAL_PI = 3.141592653589793d;
    public static final double X_PI = 52.35987755982988d;
    public static final double a = 6378245.0d;
    public static final double ee = 0.006693421622965943d;

    public static WSGPointer bd2mar(double d, double d2) {
        double d3 = d2 - 0.0065d;
        double d4 = d - 0.006d;
        double sqrt = Math.sqrt((d3 * d3) + (d4 * d4)) - (2.0E-5d * Math.sin(d4 * X_PI));
        d3 = Math.atan2(d4, d3) - (Math.cos(d3 * X_PI) * 3.0E-6d);
        return new GCJPointer(Math.sin(d3) * sqrt, Math.cos(d3) * sqrt).toExactWSGPointer();
    }
}
