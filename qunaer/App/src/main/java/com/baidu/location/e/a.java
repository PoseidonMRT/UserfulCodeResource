package com.baidu.location.e;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.a.e;
import com.baidu.location.c.f;
import com.baidu.location.f.c;
import com.baidu.location.f.i;
import com.baidu.location.f.k;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;
import qunar.sdk.mapapi.utils.MapConstant.QUNAR_GPS_FIELD;

public final class a {
    private static a a = null;
    private static final String k = (Environment.getExternalStorageDirectory().getPath() + "/baidu/tempdata/");
    private static final String l = (Environment.getExternalStorageDirectory().getPath() + "/baidu/tempdata" + "/ls.db");
    private String b = null;
    private boolean c = false;
    private boolean d = false;
    private double e = 0.0d;
    private double f = 0.0d;
    private double g = 0.0d;
    private double h = 0.0d;
    private double i = 0.0d;
    private volatile boolean j = false;
    private Handler m = new Handler();

    class a extends AsyncTask<Boolean, Void, Boolean> {
        final /* synthetic */ a a;

        private a(a aVar) {
            this.a = aVar;
        }

        protected Boolean a(Boolean... boolArr) {
            SQLiteDatabase sQLiteDatabase = null;
            if (boolArr.length != 4) {
                return Boolean.valueOf(false);
            }
            try {
                sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(a.l, null);
            } catch (Exception e) {
            }
            if (sQLiteDatabase == null) {
                return Boolean.valueOf(false);
            }
            int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
            try {
                sQLiteDatabase.beginTransaction();
                if (boolArr[0].booleanValue()) {
                    try {
                        sQLiteDatabase.execSQL("delete from wof where ac < " + (currentTimeMillis - 35));
                    } catch (Exception e2) {
                    }
                }
                if (boolArr[1].booleanValue()) {
                    try {
                        sQLiteDatabase.execSQL("delete from bdcltb09 where ac is NULL or ac < " + (currentTimeMillis - 130));
                    } catch (Exception e3) {
                    }
                }
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                sQLiteDatabase.close();
            } catch (Exception e4) {
            }
            return Boolean.valueOf(true);
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((Boolean[]) objArr);
        }
    }

    class b extends AsyncTask<Object, Void, Boolean> {
        final /* synthetic */ a a;

        private b(a aVar) {
            this.a = aVar;
        }

        protected Boolean a(Object... objArr) {
            SQLiteDatabase sQLiteDatabase = null;
            if (objArr.length != 4) {
                this.a.j = false;
                return Boolean.valueOf(false);
            }
            SQLiteDatabase openOrCreateDatabase;
            try {
                openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(a.l, null);
            } catch (Exception e) {
                openOrCreateDatabase = sQLiteDatabase;
            }
            if (openOrCreateDatabase == null) {
                this.a.j = false;
                return Boolean.valueOf(false);
            }
            try {
                openOrCreateDatabase.beginTransaction();
                this.a.a((String) objArr[0], (com.baidu.location.f.a) objArr[1], openOrCreateDatabase);
                this.a.a((i) objArr[2], (BDLocation) objArr[3], openOrCreateDatabase);
                openOrCreateDatabase.setTransactionSuccessful();
                openOrCreateDatabase.endTransaction();
                openOrCreateDatabase.close();
            } catch (Exception e2) {
            }
            this.a.j = false;
            return Boolean.valueOf(true);
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return a(objArr);
        }
    }

    private a() {
        try {
            File file = new File(k);
            File file2 = new File(l);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!file2.exists()) {
                file2.createNewFile();
            }
            if (file2.exists()) {
                SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file2, null);
                openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS bdcltb09(id CHAR(40) PRIMARY KEY,time DOUBLE,tag DOUBLE, type DOUBLE , ac INT);");
                openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS wof(id CHAR(15) PRIMARY KEY,mktime DOUBLE,time DOUBLE, ac INT, bc INT, cc INT);");
                openOrCreateDatabase.setVersion(1);
                openOrCreateDatabase.close();
            }
        } catch (Exception e) {
        }
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a();
            }
            aVar = a;
        }
        return aVar;
    }

    private void a(i iVar, BDLocation bDLocation, SQLiteDatabase sQLiteDatabase) {
        if (bDLocation != null && bDLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            if (("wf".equals(bDLocation.getNetworkLocationType()) || bDLocation.getRadius() < 300.0f) && iVar.a != null) {
                int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
                System.currentTimeMillis();
                int i = 0;
                for (ScanResult scanResult : iVar.a) {
                    if (scanResult.level != 0) {
                        int i2 = i + 1;
                        if (i2 <= 6) {
                            ContentValues contentValues = new ContentValues();
                            String encode2 = Jni.encode2(scanResult.BSSID.replace(":", ""));
                            try {
                                int i3;
                                int i4;
                                double d;
                                Object obj;
                                double d2;
                                Cursor rawQuery = sQLiteDatabase.rawQuery("select * from wof where id = \"" + encode2 + "\";", null);
                                if (rawQuery == null || !rawQuery.moveToFirst()) {
                                    i3 = 0;
                                    i4 = 0;
                                    d = 0.0d;
                                    obj = null;
                                    d2 = 0.0d;
                                } else {
                                    double d3 = rawQuery.getDouble(1) - 113.2349d;
                                    double d4 = rawQuery.getDouble(2) - 432.1238d;
                                    int i5 = rawQuery.getInt(4);
                                    i3 = rawQuery.getInt(5);
                                    i4 = i5;
                                    d2 = d4;
                                    obj = 1;
                                    d = d3;
                                }
                                if (rawQuery != null) {
                                    rawQuery.close();
                                }
                                if (obj == null) {
                                    contentValues.put("mktime", Double.valueOf(bDLocation.getLongitude() + 113.2349d));
                                    contentValues.put(QUNAR_GPS_FIELD.TIME_FIELD, Double.valueOf(bDLocation.getLatitude() + 432.1238d));
                                    contentValues.put("bc", Integer.valueOf(1));
                                    contentValues.put("cc", Integer.valueOf(1));
                                    contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                    contentValues.put("id", encode2);
                                    sQLiteDatabase.insert("wof", null, contentValues);
                                } else if (i3 == 0) {
                                    i = i2;
                                } else {
                                    float[] fArr = new float[1];
                                    Location.distanceBetween(d2, d, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                                    if (fArr[0] > 1500.0f) {
                                        int i6 = i3 + 1;
                                        if (i6 <= 10 || i6 <= i4 * 3) {
                                            contentValues.put("cc", Integer.valueOf(i6));
                                        } else {
                                            contentValues.put("mktime", Double.valueOf(bDLocation.getLongitude() + 113.2349d));
                                            contentValues.put(QUNAR_GPS_FIELD.TIME_FIELD, Double.valueOf(bDLocation.getLatitude() + 432.1238d));
                                            contentValues.put("bc", Integer.valueOf(1));
                                            contentValues.put("cc", Integer.valueOf(1));
                                            contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                        }
                                    } else {
                                        d2 = ((d2 * ((double) i4)) + bDLocation.getLatitude()) / ((double) (i4 + 1));
                                        contentValues.put("mktime", Double.valueOf((((d * ((double) i4)) + bDLocation.getLongitude()) / ((double) (i4 + 1))) + 113.2349d));
                                        contentValues.put(QUNAR_GPS_FIELD.TIME_FIELD, Double.valueOf(d2 + 432.1238d));
                                        contentValues.put("bc", Integer.valueOf(i4 + 1));
                                        contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                    }
                                    try {
                                        if (sQLiteDatabase.update("wof", contentValues, "id = \"" + encode2 + "\"", null) <= 0) {
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            } catch (Exception e2) {
                            }
                            i = i2;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.lang.String r7, android.database.sqlite.SQLiteDatabase r8) {
        /*
        r6 = this;
        r0 = 0;
        if (r7 == 0) goto L_0x000b;
    L_0x0003:
        r1 = r6.b;
        r1 = r7.equals(r1);
        if (r1 == 0) goto L_0x000c;
    L_0x000b:
        return;
    L_0x000c:
        r1 = 0;
        r6.c = r1;
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r1.<init>();	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r2 = "select * from bdcltb09 where id = \"";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r2 = "\";";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r2 = 0;
        r0 = r8.rawQuery(r1, r2);	 Catch:{ Exception -> 0x0067, all -> 0x0070 }
        r6.b = r7;	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r1 = r0.moveToFirst();	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        if (r1 == 0) goto L_0x005f;
    L_0x0035:
        r1 = 1;
        r1 = r0.getDouble(r1);	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r3 = 4653148304163072062; // 0x40934dbaacd9e83e float:-6.193295E-12 double:1235.4323;
        r1 = r1 - r3;
        r6.f = r1;	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r1 = 2;
        r1 = r0.getDouble(r1);	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r3 = 4661478502002851840; // 0x40b0e60000000000 float:0.0 double:4326.0;
        r1 = r1 - r3;
        r6.e = r1;	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r1 = 3;
        r1 = r0.getDouble(r1);	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r3 = 4657424210545395263; // 0x40a27ea4b5dcc63f float:-1.6448975E-6 double:2367.3217;
        r1 = r1 - r3;
        r6.g = r1;	 Catch:{ Exception -> 0x0067, all -> 0x007c }
        r1 = 1;
        r6.c = r1;	 Catch:{ Exception -> 0x0067, all -> 0x007c }
    L_0x005f:
        if (r0 == 0) goto L_0x000b;
    L_0x0061:
        r0.close();	 Catch:{ Exception -> 0x0065 }
        goto L_0x000b;
    L_0x0065:
        r0 = move-exception;
        goto L_0x000b;
    L_0x0067:
        r1 = move-exception;
        if (r0 == 0) goto L_0x000b;
    L_0x006a:
        r0.close();	 Catch:{ Exception -> 0x006e }
        goto L_0x000b;
    L_0x006e:
        r0 = move-exception;
        goto L_0x000b;
    L_0x0070:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0074:
        if (r1 == 0) goto L_0x0079;
    L_0x0076:
        r1.close();	 Catch:{ Exception -> 0x007a }
    L_0x0079:
        throw r0;
    L_0x007a:
        r1 = move-exception;
        goto L_0x0079;
    L_0x007c:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0074;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.a.a(java.lang.String, android.database.sqlite.SQLiteDatabase):void");
    }

    private void a(String str, com.baidu.location.f.a aVar, SQLiteDatabase sQLiteDatabase) {
        if (aVar.b() && e.b().g()) {
            System.currentTimeMillis();
            double d = 0.0d;
            double d2 = 0.0d;
            float f = 0.0f;
            int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
            String g = aVar.g();
            Object obj = 1;
            try {
                JSONObject jSONObject = new JSONObject(str);
                int parseInt = Integer.parseInt(jSONObject.getJSONObject(SpeechUtility.TAG_RESOURCE_RESULT).getString(ParamStr.RET_RES_ERROR));
                if (parseInt == BDLocation.TypeNetWorkLocation) {
                    jSONObject = jSONObject.getJSONObject(AIUIConstant.KEY_CONTENT);
                    if (jSONObject.has("clf")) {
                        obj = null;
                        String string = jSONObject.getString("clf");
                        if (string.equals("0")) {
                            JSONObject jSONObject2 = jSONObject.getJSONObject("point");
                            d = Double.parseDouble(jSONObject2.getString(MapViewConstants.ATTR_X));
                            d2 = Double.parseDouble(jSONObject2.getString(MapViewConstants.ATTR_Y));
                            f = Float.parseFloat(jSONObject.getString("radius"));
                        } else {
                            String[] split = string.split("\\|");
                            d = Double.parseDouble(split[0]);
                            d2 = Double.parseDouble(split[1]);
                            f = Float.parseFloat(split[2]);
                        }
                    }
                } else if (parseInt == BDLocation.TypeServerError) {
                    sQLiteDatabase.delete("bdcltb09", "id = \"" + g + "\"", null);
                    return;
                }
                if (obj == null) {
                    d += 1235.4323d;
                    d2 += 2367.3217d;
                    float f2 = 4326.0f + f;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(QUNAR_GPS_FIELD.TIME_FIELD, Double.valueOf(d));
                    contentValues.put(AIUIConstant.KEY_TAG, Float.valueOf(f2));
                    contentValues.put("type", Double.valueOf(d2));
                    contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                    try {
                        if (sQLiteDatabase.update("bdcltb09", contentValues, "id = \"" + g + "\"", null) <= 0) {
                            contentValues.put("id", g);
                            sQLiteDatabase.insert("bdcltb09", null, contentValues);
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
            }
        }
    }

    private void a(String str, List<ScanResult> list) {
        SQLiteDatabase sQLiteDatabase;
        if (str == null || str.equals(this.b)) {
            sQLiteDatabase = null;
        } else {
            sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(l, null);
            a(str, sQLiteDatabase);
        }
        if (list != null) {
            if (sQLiteDatabase == null) {
                sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(l, null);
            }
            a((List) list, sQLiteDatabase);
        }
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            sQLiteDatabase.close();
        }
    }

    private void a(List<ScanResult> list, SQLiteDatabase sQLiteDatabase) {
        Throwable th;
        System.currentTimeMillis();
        this.d = false;
        if (list != null && sQLiteDatabase != null && list != null) {
            double d = 0.0d;
            double d2 = 0.0d;
            int i = 0;
            Object obj = null;
            double[] dArr = new double[8];
            Object obj2 = null;
            int i2 = 0;
            StringBuffer stringBuffer = new StringBuffer();
            int i3 = 0;
            for (ScanResult scanResult : list) {
                if (i3 > 10) {
                    break;
                }
                if (i3 > 0) {
                    stringBuffer.append(",");
                }
                i3++;
                stringBuffer.append("\"").append(Jni.encode2(scanResult.BSSID.replace(":", ""))).append("\"");
            }
            Cursor cursor = null;
            Cursor rawQuery;
            try {
                rawQuery = sQLiteDatabase.rawQuery("select * from wof where id in (" + stringBuffer.toString() + ");", null);
                try {
                    if (rawQuery.moveToFirst()) {
                        while (!rawQuery.isAfterLast()) {
                            double d3 = rawQuery.getDouble(1) - 113.2349d;
                            double d4 = rawQuery.getDouble(2) - 432.1238d;
                            int i4 = rawQuery.getInt(4);
                            int i5 = rawQuery.getInt(5);
                            if (i5 <= 8 || i5 <= i4) {
                                int i6;
                                Object obj3;
                                float[] fArr;
                                if (!this.c) {
                                    if (obj == null) {
                                        int i7;
                                        if (obj2 != null) {
                                            int i8 = 0;
                                            while (i8 < i2) {
                                                Object obj4;
                                                double d5;
                                                double d6;
                                                fArr = new float[1];
                                                Location.distanceBetween(d4, d3, dArr[i8 + 1], dArr[i8], fArr);
                                                if (fArr[0] < 1000.0f) {
                                                    obj4 = 1;
                                                    d5 = d + dArr[i8];
                                                    d6 = d2 + dArr[i8 + 1];
                                                    i5 = i + 1;
                                                } else {
                                                    obj4 = obj;
                                                    i5 = i;
                                                    d6 = d2;
                                                    d5 = d;
                                                }
                                                i8 += 2;
                                                d2 = d6;
                                                d = d5;
                                                obj = obj4;
                                                i = i5;
                                            }
                                            if (obj == null) {
                                                if (i2 >= 8) {
                                                    break;
                                                }
                                                i4 = i2 + 1;
                                                dArr[i2] = d3;
                                                i7 = i4 + 1;
                                                dArr[i4] = d4;
                                                i6 = i7;
                                                obj3 = obj2;
                                            } else {
                                                d += d3;
                                                d2 += d4;
                                                i++;
                                                i6 = i2;
                                                obj3 = obj2;
                                            }
                                        } else {
                                            i4 = i2 + 1;
                                            dArr[i2] = d3;
                                            i7 = i4 + 1;
                                            dArr[i4] = d4;
                                            i3 = 1;
                                            i6 = i7;
                                        }
                                    } else {
                                        fArr = new float[1];
                                        Location.distanceBetween(d4, d3, d2 / ((double) i), d / ((double) i), fArr);
                                        if (fArr[0] > 1000.0f) {
                                            rawQuery.moveToNext();
                                        } else {
                                            i6 = i2;
                                            obj3 = obj2;
                                        }
                                    }
                                } else {
                                    fArr = new float[1];
                                    Location.distanceBetween(d4, d3, this.g, this.f, fArr);
                                    if (((double) fArr[0]) > this.e + 2000.0d) {
                                        rawQuery.moveToNext();
                                    } else {
                                        obj = 1;
                                        d += d3;
                                        d2 += d4;
                                        i++;
                                        i6 = i2;
                                        obj3 = obj2;
                                    }
                                }
                                if (i > 4) {
                                    break;
                                }
                                rawQuery.moveToNext();
                                i2 = i6;
                                obj2 = obj3;
                            } else {
                                rawQuery.moveToNext();
                            }
                        }
                        if (i > 0) {
                            this.d = true;
                            this.h = d / ((double) i);
                            this.i = d2 / ((double) i);
                        }
                    }
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e2) {
                    cursor = rawQuery;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e4) {
                    }
                }
            } catch (Throwable th3) {
                rawQuery = null;
                th = th3;
                if (rawQuery != null) {
                    try {
                        rawQuery.close();
                    } catch (Exception e5) {
                    }
                }
                throw th;
            }
        }
    }

    private String b(boolean z) {
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        Object obj = null;
        boolean z2 = false;
        if (this.d) {
            obj = 1;
            d = this.h;
            d2 = this.i;
            d3 = 246.4d;
            z2 = true;
        } else if (this.c) {
            obj = 1;
            d = this.f;
            d2 = this.g;
            d3 = this.e;
            z2 = e.b().g();
        }
        if (obj == null) {
            return z ? "{\"result\":{\"time\":\"" + com.baidu.location.h.i.a() + "\",\"error\":\"67\"}}" : "{\"result\":{\"time\":\"" + com.baidu.location.h.i.a() + "\",\"error\":\"63\"}}";
        } else {
            if (z) {
                return String.format(Locale.CHINA, "{\"result\":{\"time\":\"" + com.baidu.location.h.i.a() + "\",\"error\":\"66\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(d), Double.valueOf(d2), Double.valueOf(d3), Boolean.valueOf(true)});
            }
            return String.format(Locale.CHINA, "{\"result\":{\"time\":\"" + com.baidu.location.h.i.a() + "\",\"error\":\"66\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(d), Double.valueOf(d2), Double.valueOf(d3), Boolean.valueOf(z2)});
        }
    }

    private void d() {
        SQLiteDatabase openOrCreateDatabase;
        SQLiteDatabase sQLiteDatabase = null;
        boolean z = true;
        try {
            openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(l, null);
        } catch (Exception e) {
            openOrCreateDatabase = sQLiteDatabase;
        }
        if (openOrCreateDatabase != null) {
            try {
                long queryNumEntries = DatabaseUtils.queryNumEntries(openOrCreateDatabase, "wof");
                long queryNumEntries2 = DatabaseUtils.queryNumEntries(openOrCreateDatabase, "bdcltb09");
                boolean z2 = queryNumEntries > 10000;
                if (queryNumEntries2 <= 10000) {
                    z = false;
                }
                if (z2 || z) {
                    new a().execute(new Boolean[]{Boolean.valueOf(z2), Boolean.valueOf(z)});
                }
                openOrCreateDatabase.close();
            } catch (Exception e2) {
            }
        }
    }

    public BDLocation a(final String str, final List<ScanResult> list, boolean z) {
        String str2 = "{\"result\":\"null\"}";
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        String str3 = (FutureTask) newSingleThreadExecutor.submit(new Callable<String>(this) {
            final /* synthetic */ a c;

            public String a() {
                this.c.a(str, list);
                return this.c.b(true);
            }

            public /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            str3 = (String) str3.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            str3.cancel(true);
            str3 = str2;
            return new BDLocation(str3);
        } catch (ExecutionException e2) {
            str3.cancel(true);
            str3 = str2;
            return new BDLocation(str3);
        } catch (TimeoutException e3) {
            if (z) {
                f.a().a("old offlineLocation Timeout Exception!");
            }
            str3.cancel(true);
            str3 = str2;
            return new BDLocation(str3);
        } finally {
            newSingleThreadExecutor.shutdown();
        }
        return new BDLocation(str3);
    }

    public BDLocation a(boolean z) {
        BDLocation bDLocation = null;
        com.baidu.location.f.a f = c.a().f();
        String g = f != null ? f.g() : null;
        i j = k.a().j();
        if (j != null) {
            bDLocation = a(g, j.a, true);
        }
        if (bDLocation != null && bDLocation.getLocType() == 66) {
            StringBuffer stringBuffer = new StringBuffer(1024);
            stringBuffer.append(String.format(Locale.CHINA, "&ofl=%f|%f|%f", new Object[]{Double.valueOf(bDLocation.getLatitude()), Double.valueOf(bDLocation.getLongitude()), Float.valueOf(bDLocation.getRadius())}));
            if (j != null && j.a() > 0) {
                stringBuffer.append("&wf=");
                stringBuffer.append(j.b(15));
            }
            if (f != null) {
                stringBuffer.append(f.h());
            }
            stringBuffer.append("&uptype=oldoff");
            stringBuffer.append(com.baidu.location.h.i.e(com.baidu.location.f.getServiceContext()));
            stringBuffer.append(com.baidu.location.h.c.a().a(false));
            stringBuffer.append(com.baidu.location.a.a.a().c());
            f.a(f.a, Jni.encode(stringBuffer.toString()));
        }
        return bDLocation;
    }

    public void a(String str, com.baidu.location.f.a aVar, i iVar, BDLocation bDLocation) {
        int i = (aVar.b() && e.b().g()) ? 0 : true;
        int i2 = (bDLocation == null || bDLocation.getLocType() != BDLocation.TypeNetWorkLocation || (!"wf".equals(bDLocation.getNetworkLocationType()) && bDLocation.getRadius() >= 300.0f)) ? true : 0;
        if (iVar.a == null) {
            i2 = true;
        }
        if ((i == 0 || r0 == 0) && !this.j) {
            this.j = true;
            new b().execute(new Object[]{str, aVar, iVar, bDLocation});
        }
    }

    public void b() {
        this.m.postDelayed(new Runnable(this) {
            final /* synthetic */ a a;

            {
                this.a = r1;
            }

            public void run() {
                if (com.baidu.location.f.isServing) {
                    this.a.d();
                }
            }
        }, 3000);
    }
}
