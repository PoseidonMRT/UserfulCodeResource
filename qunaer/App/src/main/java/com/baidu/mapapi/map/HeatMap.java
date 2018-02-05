package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.util.LongSparseArray;
import android.util.SparseIntArray;
import com.baidu.mapapi.model.LatLng;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class HeatMap {
    public static final Gradient DEFAULT_GRADIENT = new Gradient(d, e);
    public static final double DEFAULT_OPACITY = 0.6d;
    public static final int DEFAULT_RADIUS = 12;
    private static final String b = HeatMap.class.getSimpleName();
    private static final SparseIntArray c = new SparseIntArray();
    private static final int[] d = new int[]{Color.rgb(0, 0, 200), Color.rgb(0, 225, 0), Color.rgb(255, 0, 0)};
    private static final float[] e = new float[]{0.08f, 0.4f, 1.0f};
    private static int r = 0;
    BaiduMap a;
    private l<WeightedLatLng> f;
    private Collection<WeightedLatLng> g;
    private int h;
    private Gradient i;
    private double j;
    private f k;
    private int[] l;
    private double[] m;
    private double[] n;
    private HashMap<String, Tile> o;
    private ExecutorService p;
    private HashSet<String> q;

    public class Builder {
        private Collection<WeightedLatLng> a;
        private int b = 12;
        private Gradient c = HeatMap.DEFAULT_GRADIENT;
        private double d = HeatMap.DEFAULT_OPACITY;

        public HeatMap build() {
            if (this.a != null) {
                return new HeatMap();
            }
            throw new IllegalStateException("No input data: you must use either .data or .weightedData before building");
        }

        public Builder data(Collection<LatLng> collection) {
            if (collection == null || collection.isEmpty()) {
                throw new IllegalArgumentException("No input points.");
            } else if (!collection.contains(null)) {
                return weightedData(HeatMap.c((Collection) collection));
            } else {
                throw new IllegalArgumentException("input points can not contain null.");
            }
        }

        public Builder gradient(Gradient gradient) {
            if (gradient == null) {
                throw new IllegalArgumentException("gradient can not be null");
            }
            this.c = gradient;
            return this;
        }

        public Builder opacity(double d) {
            this.d = d;
            if (this.d >= 0.0d && this.d <= WeightedLatLng.DEFAULT_INTENSITY) {
                return this;
            }
            throw new IllegalArgumentException("Opacity must be in range [0, 1]");
        }

        public Builder radius(int i) {
            this.b = i;
            if (this.b >= 10 && this.b <= 50) {
                return this;
            }
            throw new IllegalArgumentException("Radius not within bounds.");
        }

        public Builder weightedData(Collection<WeightedLatLng> collection) {
            if (collection == null || collection.isEmpty()) {
                throw new IllegalArgumentException("No input points.");
            } else if (collection.contains(null)) {
                throw new IllegalArgumentException("input points can not contain null.");
            } else {
                Collection arrayList = new ArrayList();
                for (WeightedLatLng weightedLatLng : collection) {
                    LatLng latLng = weightedLatLng.latLng;
                    if (latLng.latitude < 0.37532d || latLng.latitude > 54.562495d || latLng.longitude < 72.508319d || latLng.longitude > 135.942198d) {
                        arrayList.add(weightedLatLng);
                    }
                }
                collection.removeAll(arrayList);
                this.a = collection;
                return this;
            }
        }
    }

    static {
        c.put(3, 8388608);
        c.put(4, 4194304);
        c.put(5, 2097152);
        c.put(6, 1048576);
        c.put(7, 524288);
        c.put(8, 262144);
        c.put(9, 131072);
        c.put(10, 65536);
        c.put(11, 32768);
        c.put(12, 16384);
        c.put(13, 8192);
        c.put(14, 4096);
        c.put(15, 2048);
        c.put(16, 1024);
        c.put(17, 512);
        c.put(18, 256);
        c.put(19, 128);
        c.put(20, 64);
    }

    private HeatMap(Builder builder) {
        this.o = new HashMap();
        this.p = Executors.newFixedThreadPool(1);
        this.q = new HashSet();
        this.g = builder.a;
        this.h = builder.b;
        this.i = builder.c;
        this.j = builder.d;
        this.m = a(this.h, ((double) this.h) / 3.0d);
        a(this.i);
        b(this.g);
    }

    private static double a(Collection<WeightedLatLng> collection, f fVar, int i, int i2) {
        double d = fVar.a;
        double d2 = fVar.c;
        double d3 = fVar.b;
        double d4 = fVar.d;
        double d5 = ((double) ((int) (((double) (i2 / (i * 2))) + 0.5d))) / (d2 - d > d4 - d3 ? d2 - d : d4 - d3);
        LongSparseArray longSparseArray = new LongSparseArray();
        d4 = 0.0d;
        for (WeightedLatLng weightedLatLng : collection) {
            LongSparseArray longSparseArray2;
            int i3 = (int) ((((double) weightedLatLng.a().x) - d) * d5);
            int i4 = (int) ((((double) weightedLatLng.a().y) - d3) * d5);
            LongSparseArray longSparseArray3 = (LongSparseArray) longSparseArray.get((long) i3);
            if (longSparseArray3 == null) {
                longSparseArray3 = new LongSparseArray();
                longSparseArray.put((long) i3, longSparseArray3);
                longSparseArray2 = longSparseArray3;
            } else {
                longSparseArray2 = longSparseArray3;
            }
            Double d6 = (Double) longSparseArray2.get((long) i4);
            if (d6 == null) {
                d6 = Double.valueOf(0.0d);
            }
            Double valueOf = Double.valueOf(weightedLatLng.intensity + d6.doubleValue());
            longSparseArray2.put((long) i4, valueOf);
            d4 = valueOf.doubleValue() > d4 ? valueOf.doubleValue() : d4;
        }
        return d4;
    }

    private static Bitmap a(double[][] dArr, int[] iArr, double d) {
        int i = iArr[iArr.length - 1];
        double length = ((double) (iArr.length - 1)) / d;
        int length2 = dArr.length;
        int[] iArr2 = new int[(length2 * length2)];
        for (int i2 = 0; i2 < length2; i2++) {
            for (int i3 = 0; i3 < length2; i3++) {
                double d2 = dArr[i3][i2];
                int i4 = (i2 * length2) + i3;
                int i5 = (int) (d2 * length);
                if (d2 == 0.0d) {
                    iArr2[i4] = 0;
                } else if (i5 < iArr.length) {
                    iArr2[i4] = iArr[i5];
                } else {
                    iArr2[i4] = i;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(length2, length2, Config.ARGB_8888);
        createBitmap.setPixels(iArr2, 0, length2, 0, 0, length2, length2);
        return createBitmap;
    }

    private static Tile a(Bitmap bitmap) {
        Buffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
        bitmap.copyPixelsToBuffer(allocate);
        return new Tile(256, 256, allocate.array());
    }

    private void a(Gradient gradient) {
        this.i = gradient;
        this.l = gradient.a(this.j);
    }

    private synchronized void a(String str, Tile tile) {
        this.o.put(str, tile);
    }

    private synchronized boolean a(String str) {
        return this.q.contains(str);
    }

    private double[] a(int i) {
        int i2 = 11;
        double[] dArr = new double[20];
        for (int i3 = 5; i3 < 11; i3++) {
            dArr[i3] = a(this.g, this.k, i, (int) (1280.0d * Math.pow(2.0d, (double) (i3 - 3))));
            if (i3 == 5) {
                for (int i4 = 0; i4 < i3; i4++) {
                    dArr[i4] = dArr[i3];
                }
            }
        }
        while (i2 < 20) {
            dArr[i2] = dArr[10];
            i2++;
        }
        return dArr;
    }

    private static double[] a(int i, double d) {
        double[] dArr = new double[((i * 2) + 1)];
        for (int i2 = -i; i2 <= i; i2++) {
            dArr[i2 + i] = Math.exp(((double) ((-i2) * i2)) / ((2.0d * d) * d));
        }
        return dArr;
    }

    private static double[][] a(double[][] dArr, double[] dArr2) {
        int i;
        int floor = (int) Math.floor(((double) dArr2.length) / 2.0d);
        int length = dArr.length;
        int i2 = length - (floor * 2);
        int i3 = (floor + i2) - 1;
        double[][] dArr3 = (double[][]) Array.newInstance(Double.TYPE, new int[]{length, length});
        int i4 = 0;
        while (i4 < length) {
            for (i = 0; i < length; i++) {
                int i5;
                double d = dArr[i4][i];
                if (d != 0.0d) {
                    i5 = (i3 < i4 + floor ? i3 : i4 + floor) + 1;
                    int i6 = floor > i4 - floor ? floor : i4 - floor;
                    while (i6 < i5) {
                        double[] dArr4 = dArr3[i6];
                        dArr4[i] = dArr4[i] + (dArr2[i6 - (i4 - floor)] * d);
                        i6++;
                    }
                }
            }
            i4++;
        }
        double[][] dArr5 = (double[][]) Array.newInstance(Double.TYPE, new int[]{i2, i2});
        for (i2 = floor; i2 < i3 + 1; i2++) {
            i4 = 0;
            while (i4 < length) {
                d = dArr3[i2][i4];
                if (d != 0.0d) {
                    i5 = (i3 < i4 + floor ? i3 : i4 + floor) + 1;
                    i = floor > i4 - floor ? floor : i4 - floor;
                    while (i < i5) {
                        dArr4 = dArr5[i2 - floor];
                        int i7 = i - floor;
                        dArr4[i7] = dArr4[i7] + (dArr2[i - (i4 - floor)] * d);
                        i++;
                    }
                }
                i4++;
            }
        }
        return dArr5;
    }

    private void b(int i, int i2, int i3) {
        double d = (double) c.get(i3);
        double d2 = (((double) this.h) * d) / 256.0d;
        double d3 = ((2.0d * d2) + d) / ((double) ((this.h * 2) + 256));
        if (i >= 0 && i2 >= 0) {
            double d4 = (((double) i) * d) - d2;
            d = (d * ((double) (i2 + 1))) + d2;
            f fVar = new f(d4, (((double) (i + 1)) * d) + d2, (((double) i2) * d) - d2, d);
            if (fVar.a(new f(this.k.a - d2, this.k.c + d2, this.k.b - d2, d2 + this.k.d))) {
                Collection<WeightedLatLng> a = this.f.a(fVar);
                if (!a.isEmpty()) {
                    double[][] dArr = (double[][]) Array.newInstance(Double.TYPE, new int[]{(this.h * 2) + 256, (this.h * 2) + 256});
                    for (WeightedLatLng weightedLatLng : a) {
                        Point a2 = weightedLatLng.a();
                        int i4 = (int) ((((double) a2.x) - d4) / d3);
                        int i5 = (int) ((d - ((double) a2.y)) / d3);
                        if (i4 >= (this.h * 2) + 256) {
                            i4 = ((this.h * 2) + 256) - 1;
                        }
                        if (i5 >= (this.h * 2) + 256) {
                            i5 = ((this.h * 2) + 256) - 1;
                        }
                        double[] dArr2 = dArr[i4];
                        dArr2[i5] = dArr2[i5] + weightedLatLng.intensity;
                    }
                    Bitmap a3 = a(a(dArr, this.m), this.l, this.n[i3 - 1]);
                    Tile a4 = a(a3);
                    a3.recycle();
                    a(i + "_" + i2 + "_" + i3, a4);
                    if (this.o.size() > r) {
                        a();
                    }
                    if (this.a != null) {
                        this.a.a();
                    }
                }
            }
        }
    }

    private synchronized void b(String str) {
        this.q.add(str);
    }

    private void b(Collection<WeightedLatLng> collection) {
        this.g = collection;
        if (this.g.isEmpty()) {
            throw new IllegalArgumentException("No input points.");
        }
        this.k = d(this.g);
        this.f = new l(this.k);
        for (a a : this.g) {
            this.f.a(a);
        }
        this.n = a(this.h);
    }

    private synchronized Tile c(String str) {
        Tile tile;
        if (this.o.containsKey(str)) {
            tile = (Tile) this.o.get(str);
            this.o.remove(str);
        } else {
            tile = null;
        }
        return tile;
    }

    private static Collection<WeightedLatLng> c(Collection<LatLng> collection) {
        Collection arrayList = new ArrayList();
        for (LatLng weightedLatLng : collection) {
            arrayList.add(new WeightedLatLng(weightedLatLng));
        }
        return arrayList;
    }

    private static f d(Collection<WeightedLatLng> collection) {
        Iterator it = collection.iterator();
        WeightedLatLng weightedLatLng = (WeightedLatLng) it.next();
        double d = (double) weightedLatLng.a().x;
        double d2 = (double) weightedLatLng.a().x;
        double d3 = (double) weightedLatLng.a().y;
        double d4 = (double) weightedLatLng.a().y;
        while (it.hasNext()) {
            weightedLatLng = (WeightedLatLng) it.next();
            double d5 = (double) weightedLatLng.a().x;
            double d6 = (double) weightedLatLng.a().y;
            if (d5 < d) {
                d = d5;
            }
            if (d5 > d2) {
                d2 = d5;
            }
            if (d6 < d3) {
                d3 = d6;
            }
            if (d6 > d4) {
                d4 = d6;
            }
        }
        return new f(d, d2, d3, d4);
    }

    private synchronized void d() {
        this.o.clear();
    }

    Tile a(int i, int i2, int i3) {
        String str = i + "_" + i2 + "_" + i3;
        Tile c = c(str);
        if (c != null) {
            return c;
        }
        if (!a(str)) {
            if (this.a != null && r == 0) {
                MapStatus mapStatus = this.a.getMapStatus();
                r = ((((mapStatus.a.j.bottom - mapStatus.a.j.top) / 256) + 2) * (((mapStatus.a.j.right - mapStatus.a.j.left) / 256) + 2)) * 4;
            }
            if (this.o.size() > r) {
                a();
            }
            if (!this.p.isShutdown()) {
                try {
                    this.p.execute(new g(this, i, i2, i3));
                    b(str);
                } catch (RejectedExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    synchronized void a() {
        this.q.clear();
        this.o.clear();
    }

    void b() {
        d();
    }

    void c() {
        this.p.shutdownNow();
    }

    public void removeHeatMap() {
        if (this.a != null) {
            this.a.a(this);
        }
    }
}
