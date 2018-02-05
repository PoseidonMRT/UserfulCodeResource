package qunar.sdk.mapapi.utils.projectTransform;

import com.baidu.mapapi.map.WeightedLatLng;

public class TransformUtil {
    public static boolean outOfChina(double d, double d2) {
        if (d2 < 72.004d || d2 > 137.8347d || d < 0.8293d || d > 55.8271d) {
            return true;
        }
        return false;
    }

    public static double transformLat(double d, double d2) {
        return (((((((-100.0d + (2.0d * d)) + (3.0d * d2)) + ((0.2d * d2) * d2)) + ((0.1d * d) * d2)) + (0.2d * Math.sqrt(Math.abs(d)))) + ((((20.0d * Math.sin((6.0d * d) * CoordConvertLocal.REAL_PI)) + (20.0d * Math.sin((2.0d * d) * CoordConvertLocal.REAL_PI))) * 2.0d) / 3.0d)) + ((((20.0d * Math.sin(CoordConvertLocal.REAL_PI * d2)) + (40.0d * Math.sin((d2 / 3.0d) * CoordConvertLocal.REAL_PI))) * 2.0d) / 3.0d)) + ((((160.0d * Math.sin((d2 / 12.0d) * CoordConvertLocal.REAL_PI)) + (320.0d * Math.sin((CoordConvertLocal.REAL_PI * d2) / 30.0d))) * 2.0d) / 3.0d);
    }

    public static double transformLon(double d, double d2) {
        return (((((((300.0d + d) + (2.0d * d2)) + ((0.1d * d) * d)) + ((0.1d * d) * d2)) + (0.1d * Math.sqrt(Math.abs(d)))) + ((((20.0d * Math.sin((6.0d * d) * CoordConvertLocal.REAL_PI)) + (20.0d * Math.sin((2.0d * d) * CoordConvertLocal.REAL_PI))) * 2.0d) / 3.0d)) + ((((20.0d * Math.sin(CoordConvertLocal.REAL_PI * d)) + (40.0d * Math.sin((d / 3.0d) * CoordConvertLocal.REAL_PI))) * 2.0d) / 3.0d)) + ((((150.0d * Math.sin((d / 12.0d) * CoordConvertLocal.REAL_PI)) + (300.0d * Math.sin((d / 30.0d) * CoordConvertLocal.REAL_PI))) * 2.0d) / 3.0d);
    }

    public static double[] delta(double d, double d2) {
        double[] dArr = new double[2];
        double transformLat = transformLat(d2 - 105.0d, d - 35.0d);
        double transformLon = transformLon(d2 - 105.0d, d - 35.0d);
        double d3 = (d / 180.0d) * CoordConvertLocal.REAL_PI;
        double sin = Math.sin(d3);
        sin = WeightedLatLng.DEFAULT_INTENSITY - (sin * (CoordConvertLocal.ee * sin));
        double sqrt = Math.sqrt(sin);
        dArr[0] = (transformLat * 180.0d) / ((((WeightedLatLng.DEFAULT_INTENSITY - CoordConvertLocal.ee) * CoordConvertLocal.a) / (sin * sqrt)) * CoordConvertLocal.REAL_PI);
        dArr[1] = (180.0d * transformLon) / (((CoordConvertLocal.a / sqrt) * Math.cos(d3)) * CoordConvertLocal.REAL_PI);
        return dArr;
    }
}
