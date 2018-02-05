package com.mapquest.android.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import com.baidu.mapapi.map.WeightedLatLng;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

public class Util {
    private static final String ANDROID_API_VERSION_NUMBER = "android-api-1.0.4";
    private static final String LOG_TAG = "mq.android.maps.util";
    private static int canWriteFlag = 0;

    Util() {
    }

    public static double from1E6(int i) {
        return ((double) i) * 1.0E-6d;
    }

    public static int to1E6(double d) {
        return (int) (1000000.0d * d);
    }

    public static int hypotenuse(int i, int i2) {
        return (int) hypotenuse((double) i, (double) i2);
    }

    public static float hypotenuse(float f, float f2) {
        return (float) hypotenuse((double) f, (double) f2);
    }

    public static double hypotenuse(double d, double d2) {
        return Math.sqrt((d * d) + (d2 * d2));
    }

    public static float distance(float f, float f2, float f3, float f4) {
        return (float) Math.sqrt((double) (((f - f3) * (f - f3)) + ((f2 - f4) * (f2 - f4))));
    }

    public static float distance(Point point, Point point2) {
        return distance((float) point.x, (float) point.y, (float) point2.x, (float) point2.y);
    }

    public static float distance(PointF pointF, PointF pointF2) {
        return distance(pointF.x, pointF.y, pointF2.x, pointF2.y);
    }

    public static int distanceSquared(int i, int i2, int i3, int i4) {
        int i5 = i - i3;
        int i6 = i2 - i4;
        return (i5 * i5) + (i6 * i6);
    }

    public static double log2(double d) {
        return Math.log(d) / Math.log(2.0d);
    }

    public static Rect createOriginRectFromBoundingBox(BoundingBox boundingBox, MapView mapView) {
        Projection projection;
        Projection projection2 = mapView.getProjection();
        if (projection2 instanceof RotatableProjection) {
            projection = ((RotatableProjection) projection2).getProjection();
        } else {
            projection = projection2;
        }
        Point toPixels = projection.toPixels(boundingBox.ul, (Point) null);
        Point toPixels2 = projection.toPixels(boundingBox.lr, (Point) null);
        return new Rect(toPixels.x, toPixels.y, toPixels2.x, toPixels2.y);
    }

    public static BoundingBox createOriginBoundingBoxFromRect(Rect rect, MapView mapView) {
        try {
            Projection projection = mapView.getProjection();
            if (projection instanceof RotatableProjection) {
                projection = ((RotatableProjection) projection).getProjection();
            }
            GeoPoint fromPixels = projection.fromPixels(rect.left, rect.top);
            GeoPoint fromPixels2 = projection.fromPixels(rect.right, rect.bottom);
            return new BoundingBox(new GeoPoint(fromPixels.getLatitudeE6(), fromPixels.getLongitudeE6()), new GeoPoint(fromPixels2.getLatitudeE6(), fromPixels2.getLongitudeE6()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Rect createRectFromBoundingBox(BoundingBox boundingBox, MapView mapView) {
        int i = 0;
        Projection projection = mapView.getProjection();
        Point toPixels = projection.toPixels(new GeoPoint(boundingBox.ul.getLatitudeE6(), boundingBox.ul.getLongitudeE6()), (Point) null);
        Point toPixels2 = projection.toPixels(new GeoPoint(boundingBox.ul.getLatitudeE6(), boundingBox.lr.getLongitudeE6()), (Point) null);
        Point toPixels3 = projection.toPixels(new GeoPoint(boundingBox.lr.getLatitudeE6(), boundingBox.ul.getLongitudeE6()), (Point) null);
        Point toPixels4 = projection.toPixels(new GeoPoint(boundingBox.lr.getLatitudeE6(), boundingBox.lr.getLongitudeE6()), (Point) null);
        Point[] pointArr = new Point[]{toPixels, toPixels2, toPixels3, toPixels4};
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < pointArr.length; i5++) {
            if (pointArr[i5].x < i4 || i4 == 0) {
                i4 = pointArr[i5].x;
            }
            if (pointArr[i5].x > i3 || i3 == 0) {
                i3 = pointArr[i5].x;
            }
            if (pointArr[i5].y < i2 || i2 == 0) {
                i2 = pointArr[i5].y;
            }
            if (pointArr[i5].y > i || i == 0) {
                i = pointArr[i5].y;
            }
        }
        return new Rect(i4, i2, i3, i);
    }

    public static BoundingBox createBoundingBoxFromRect(Rect rect, MapView mapView) {
        int i = 0;
        Projection projection = mapView.getProjection();
        GeoPoint fromPixels = projection.fromPixels(rect.left, rect.top);
        GeoPoint fromPixels2 = projection.fromPixels(rect.right, rect.top);
        GeoPoint fromPixels3 = projection.fromPixels(rect.left, rect.bottom);
        GeoPoint fromPixels4 = projection.fromPixels(rect.right, rect.bottom);
        GeoPoint[] geoPointArr = new GeoPoint[]{fromPixels, fromPixels2, fromPixels3, fromPixels4};
        int to1E6 = to1E6(180.0d);
        int to1E62 = to1E6(-180.0d);
        int to1E63 = to1E6(-90.0d);
        int to1E64 = to1E6(90.0d);
        while (i < geoPointArr.length) {
            if (geoPointArr[i].getLongitudeE6() < to1E6) {
                to1E6 = geoPointArr[i].getLongitudeE6();
            }
            if (geoPointArr[i].getLongitudeE6() > to1E62) {
                to1E62 = geoPointArr[i].getLongitudeE6();
            }
            if (geoPointArr[i].getLatitudeE6() > to1E63) {
                to1E63 = geoPointArr[i].getLatitudeE6();
            }
            if (geoPointArr[i].getLatitudeE6() < to1E64) {
                to1E64 = geoPointArr[i].getLatitudeE6();
            }
            i++;
        }
        return new BoundingBox(new GeoPoint(to1E63, to1E6), new GeoPoint(to1E64, to1E62));
    }

    public static Point closestPoint(Point point, Point point2, Point point3, Point point4) {
        if (point4 == null) {
            point4 = new Point();
        }
        int i = point2.x;
        int i2 = point2.y;
        int i3 = point3.x - i;
        int i4 = point3.y - i2;
        if (i3 == 0 && i4 == 0) {
            point4.set(point2.x, point2.y);
        } else {
            int i5 = ((point.x - i) * i3) + ((point.y - i2) * i4);
            if (i5 <= 0) {
                point4.set(point2.x, point2.y);
            } else {
                int i6 = (i3 * i3) + (i4 * i4);
                if (i6 <= i5) {
                    point4.set(point3.x, point3.y);
                } else {
                    double d = ((double) i6) / ((double) i5);
                    point4.set((int) (((double) i) + (((double) i3) * d)), (int) (((double) i2) + (((double) i4) * d)));
                }
            }
        }
        return point4;
    }

    public static GeoPoint closestPoint(GeoPoint geoPoint, List<GeoPoint> list) {
        Object obj = 1;
        double d = 2.147483647E9d;
        long longitudeE6 = (long) geoPoint.getLongitudeE6();
        long latitudeE6 = (long) geoPoint.getLatitudeE6();
        int i = 0;
        int i2 = 0;
        int size = list.size() - 1;
        int i3 = 0;
        while (i3 < size) {
            int i4;
            Object obj2;
            double d2;
            int longitudeE62 = ((GeoPoint) list.get(i3)).getLongitudeE6();
            int latitudeE62 = ((GeoPoint) list.get(i3)).getLatitudeE6();
            long longitudeE63 = (long) (((GeoPoint) list.get(i3 + 1)).getLongitudeE6() - longitudeE62);
            long latitudeE63 = (long) (((GeoPoint) list.get(i3 + 1)).getLatitudeE6() - latitudeE62);
            if (((float) longitudeE63) == 0.0f && ((float) latitudeE63) == 0.0f) {
                i4 = i2;
                obj2 = obj;
                i2 = i;
                d2 = d;
            } else {
                long j = longitudeE6 - ((long) longitudeE62);
                long j2 = latitudeE6 - ((long) latitudeE62);
                long j3 = (longitudeE63 * j) + (latitudeE63 * j2);
                if (((float) j3) <= 0.0f) {
                    double d3 = (double) ((j * j) + (j2 * j2));
                    if (d3 < d) {
                        i2 = ((GeoPoint) list.get(i3)).getLatitudeE6();
                        i4 = ((GeoPoint) list.get(i3)).getLongitudeE6();
                        d2 = d3;
                        obj2 = null;
                    } else {
                        i4 = i2;
                        obj2 = null;
                        i2 = i;
                        d2 = d;
                    }
                } else {
                    double d4 = ((double) j3) / ((double) ((longitudeE63 * longitudeE63) + (latitudeE63 * latitudeE63)));
                    if (d4 >= WeightedLatLng.DEFAULT_INTENSITY) {
                        longitudeE62 = 1;
                        i4 = i2;
                        i2 = i;
                        d2 = d;
                    } else {
                        i4 = ((int) (((double) longitudeE63) * d4)) + longitudeE62;
                        int i5 = ((int) (((double) latitudeE63) * d4)) + latitudeE62;
                        double distSqr = distSqr((double) (longitudeE6 - ((long) i4)), (double) (latitudeE6 - ((long) i5)));
                        if (distSqr < d) {
                            i2 = i5;
                            d2 = distSqr;
                            obj2 = null;
                        } else {
                            i4 = i2;
                            obj2 = null;
                            i2 = i;
                            d2 = d;
                        }
                    }
                }
            }
            i3++;
            d = d2;
            i = i2;
            obj = obj2;
            i2 = i4;
        }
        if (obj != null && distSqr((double) (longitudeE6 - ((long) ((GeoPoint) list.get(size)).getLongitudeE6())), (double) (latitudeE6 - ((long) ((GeoPoint) list.get(size)).getLatitudeE6()))) < d) {
            i = ((GeoPoint) list.get(size)).getLatitudeE6();
            i2 = ((GeoPoint) list.get(size)).getLongitudeE6();
        }
        return new GeoPoint(i, i2);
    }

    public static double distSqr(double d, double d2) {
        return (d * d) + (d2 * d2);
    }

    public static BitmapDrawable getDrawable(Context context, String str) {
        FileInputStream fileInputStream;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        switch (displayMetrics.densityDpi) {
            case 240:
                stringBuilder.append("_hdpi.png");
                break;
            default:
                stringBuilder.append("_mdpi.png");
                break;
        }
        File file = new File(context.getCacheDir() + File.separator + stringBuilder.toString());
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (Exception e) {
                fileInputStream = null;
            }
        } else {
            fileInputStream = null;
        }
        if (fileInputStream == null) {
            fileInputStream = context.getClass().getResourceAsStream("/com/mapquest/android/maps/" + stringBuilder.toString());
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(fileInputStream);
        if (bitmapDrawable == null) {
            return null;
        }
        bitmapDrawable.setTargetDensity(displayMetrics.densityDpi);
        return bitmapDrawable;
    }

    public static BitmapDrawable getDrawableByName(Context context, String str) {
        FileInputStream fileInputStream;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        File file = new File(context.getCacheDir() + File.separator + str);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (Exception e) {
                fileInputStream = null;
            }
        } else {
            fileInputStream = null;
        }
        if (fileInputStream == null) {
            fileInputStream = context.getClass().getResourceAsStream("/com/mapquest/android/maps/" + str);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(fileInputStream);
        if (bitmapDrawable == null) {
            return null;
        }
        bitmapDrawable.setTargetDensity(displayMetrics.densityDpi);
        return bitmapDrawable;
    }

    public static Drawable addStringToMarker(Context context, Drawable drawable, String str, Paint paint) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(new Rect(0, 0, intrinsicWidth, intrinsicHeight));
        drawable.draw(canvas);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, (float) ((((intrinsicWidth - rect.width()) % 2) + ((intrinsicWidth - rect.width()) / 2)) - 1), (float) ((intrinsicHeight / 2) + 2), paint);
        return new BitmapDrawable(context.getResources(), createBitmap);
    }

    public static String convertStreamToString(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        char[] cArr = new char[1024];
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (true) {
                int read = bufferedReader.read(cArr);
                if (read == -1) {
                    break;
                }
                stringWriter.write(cArr, 0, read);
            }
            return stringWriter.toString();
        } finally {
            inputStream.close();
        }
    }

    static boolean checkIfSameThread(Handler handler) {
        return handler.getLooper().getThread() == Thread.currentThread();
    }

    static String getApiVersion() {
        return "android-api-1.0.5";
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    @TargetApi(8)
    private static File getExternalFilesDir(Context context, String str) {
        if (hasFroyo()) {
            File externalFilesDir = context.getExternalFilesDir(str);
            if (externalFilesDir != null) {
                return externalFilesDir;
            }
        }
        return new File(Environment.getExternalStorageDirectory(), "/Android/data/" + context.getPackageName() + "/files/" + str);
    }

    public static File getAppFileDir(Context context, String str) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        Throwable th;
        File file = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File externalFilesDir = getExternalFilesDir(context, str);
            if (canWriteFlag == 0) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    File file2 = new File(externalFilesDir, uuid);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    File file3 = new File(file2, uuid);
                    try {
                        fileOutputStream = new FileOutputStream(file3);
                        try {
                            fileOutputStream.write(0);
                            fileOutputStream.flush();
                            canWriteFlag = 1;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                file3.delete();
                                file3.getParentFile().delete();
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            file = file3;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                                file.delete();
                                file.getParentFile().delete();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                        file = file3;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                            file.delete();
                            file.getParentFile().delete();
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fileOutputStream = null;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        file.delete();
                        file.getParentFile().delete();
                    }
                    throw th;
                }
            }
            if (canWriteFlag == 1) {
                return externalFilesDir;
            }
        }
        return context.getFileStreamPath(str);
    }
}
