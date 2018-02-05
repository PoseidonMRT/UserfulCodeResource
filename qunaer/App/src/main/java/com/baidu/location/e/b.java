package com.baidu.location.e;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.h.f;
import com.baidu.mapapi.map.WeightedLatLng;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.mqunar.BuildConfig;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

final class b {
    private final d a;
    private int b;
    private double c;
    private double d;
    private Long e;
    private final c f = new c(this, this, true);
    private final c g = new c(this, this, false);
    private final SQLiteDatabase h;
    private final SQLiteDatabase i;
    private StringBuffer j = null;
    private StringBuffer k = null;
    private HashSet<Long> l = new HashSet();
    private ConcurrentHashMap<Long, Integer> m = new ConcurrentHashMap();
    private ConcurrentHashMap<Long, String> n = new ConcurrentHashMap();
    private StringBuffer o = new StringBuffer();
    private boolean p = false;

    class b extends Thread {
        private String a;
        final /* synthetic */ b b;
        private Long c;
        private BDLocation d;
        private BDLocation e;
        private BDLocation f;
        private String g;
        private LinkedHashMap<String, Integer> h;

        private b(b bVar, String str, Long l, BDLocation bDLocation, BDLocation bDLocation2, BDLocation bDLocation3, String str2, LinkedHashMap<String, Integer> linkedHashMap) {
            this.b = bVar;
            this.a = str;
            this.c = l;
            this.d = bDLocation;
            this.e = bDLocation2;
            this.f = bDLocation3;
            this.g = str2;
            this.h = linkedHashMap;
        }

        public void run() {
            try {
                this.b.a(this.a, this.c, this.d);
                this.b.j = null;
                this.b.k = null;
                this.b.a(this.h);
                this.b.a(this.f, this.d, this.e, this.a, this.c);
                if (this.g != null) {
                    this.b.a.j().a(this.g);
                }
            } catch (Exception e) {
            }
            this.h = null;
            this.a = null;
            this.g = null;
            this.c = null;
            this.d = null;
            this.e = null;
            this.f = null;
        }
    }

    final class a {
        double a;
        double b;
        double c;

        private a(double d, double d2, double d3) {
            this.a = d;
            this.b = d2;
            this.c = d3;
        }
    }

    final class c extends f {
        final /* synthetic */ b a;
        private String b;
        private final String c;
        private String d;
        private b e;
        private boolean f = false;
        private int p = 0;
        private long q = -1;
        private long r = -1;
        private long s = -1;
        private long t = -1;

        c(b bVar, b bVar2, boolean z) {
            this.a = bVar;
            this.e = bVar2;
            if (z) {
                this.c = "load";
            } else {
                this.c = "update";
            }
            this.k = new HashMap();
            this.b = d.a;
        }

        private void a(String str, String str2, String str3) {
            this.d = str3;
            this.b = String.format("http://%s/%s", new Object[]{str, str2});
            e();
        }

        private void c() {
            this.p++;
            this.q = System.currentTimeMillis();
        }

        private boolean f() {
            if (this.p < 2) {
                return true;
            }
            if (this.q + 43200000 >= System.currentTimeMillis()) {
                return false;
            }
            this.p = 0;
            this.q = -1;
            return true;
        }

        private void g() {
            this.d = null;
            if (!l()) {
                this.d = i();
            } else if (this.r == -1 || this.r + DateTimeUtils.ONE_DAY <= System.currentTimeMillis()) {
                this.d = h();
            }
            if (this.d == null && (this.s == -1 || this.s + DateTimeUtils.ONE_DAY <= System.currentTimeMillis())) {
                if (this.a.a.k().a()) {
                    this.d = j();
                } else {
                    this.d = k();
                }
            }
            if (this.d != null) {
                e();
            }
        }

        private String h() {
            JSONObject jSONObject;
            try {
                jSONObject = new JSONObject();
                jSONObject.put("type", "0");
                jSONObject.put("cuid", com.baidu.location.h.c.a().b);
                jSONObject.put("ver", "1");
                jSONObject.put(BuildConfig.EPYT_YOLPED, com.baidu.location.h.c.e + ":" + com.baidu.location.h.c.d);
            } catch (Exception e) {
                jSONObject = null;
            }
            return jSONObject != null ? Jni.encodeOfflineLocationUpdateRequest(jSONObject.toString()) : null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.String i() {
            /*
            r11 = this;
            r4 = 0;
            r1 = 0;
            r6 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0175, all -> 0x015a }
            r6.<init>();	 Catch:{ Exception -> 0x0175, all -> 0x015a }
            r0 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0175, all -> 0x015a }
            r0.<init>();	 Catch:{ Exception -> 0x0175, all -> 0x015a }
            r2 = r11.a;	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r2 = r2.i;	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r3 = "SELECT * FROM %s WHERE frequency>%d ORDER BY frequency DESC LIMIT %d;";
            r5 = 3;
            r5 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r7 = 0;
            r8 = "CL";
            r5[r7] = r8;	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r7 = 1;
            r8 = 5;
            r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r5[r7] = r8;	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r7 = 2;
            r8 = 50;
            r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r5[r7] = r8;	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r3 = java.lang.String.format(r3, r5);	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            r5 = 0;
            r2 = r2.rawQuery(r3, r5);	 Catch:{ Exception -> 0x017b, all -> 0x015a }
            if (r2 == 0) goto L_0x0186;
        L_0x0038:
            r3 = r2.moveToFirst();	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            if (r3 == 0) goto L_0x0186;
        L_0x003e:
            r3 = r2.getCount();	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r5 = new org.json.JSONArray;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r5.<init>();	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
        L_0x0047:
            r7 = r2.isAfterLast();	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            if (r7 != 0) goto L_0x00a8;
        L_0x004d:
            r7 = 1;
            r7 = r2.getString(r7);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r5.put(r7);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r2.moveToNext();	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            goto L_0x0047;
        L_0x0059:
            r3 = move-exception;
            r3 = r1;
        L_0x005b:
            if (r3 == 0) goto L_0x0060;
        L_0x005d:
            r3.close();	 Catch:{ Exception -> 0x0169 }
        L_0x0060:
            if (r2 == 0) goto L_0x0183;
        L_0x0062:
            r2.close();	 Catch:{ Exception -> 0x0156 }
            r2 = r0;
        L_0x0066:
            if (r2 == 0) goto L_0x0180;
        L_0x0068:
            r0 = "model";
            r0 = r2.has(r0);
            if (r0 != 0) goto L_0x0180;
        L_0x0070:
            r3 = r11.t;
            r5 = -1;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 == 0) goto L_0x0086;
        L_0x0078:
            r3 = r11.t;
            r5 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
            r3 = r3 + r5;
            r5 = java.lang.System.currentTimeMillis();
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 >= 0) goto L_0x0180;
        L_0x0086:
            r0 = r2.toString();
            r1 = com.baidu.location.Jni.encodeOfflineLocationUpdateRequest(r0);
            r3 = java.lang.System.currentTimeMillis();
            r11.t = r3;
            r0 = r1;
        L_0x0095:
            if (r2 == 0) goto L_0x00a7;
        L_0x0097:
            r1 = "model";
            r1 = r2.has(r1);
            if (r1 == 0) goto L_0x00a7;
        L_0x009f:
            r0 = r2.toString();
            r0 = com.baidu.location.Jni.encodeOfflineLocationUpdateRequest(r0);
        L_0x00a7:
            return r0;
        L_0x00a8:
            r7 = "cell";
            r6.put(r7, r5);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r5 = r3;
        L_0x00ae:
            r3 = r11.a;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r3 = r3.i;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r7 = "SELECT * FROM %s WHERE frequency>%d ORDER BY frequency DESC LIMIT %d;";
            r8 = 3;
            r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r9 = 0;
            r10 = "AP";
            r8[r9] = r10;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r9 = 1;
            r10 = 5;
            r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r8[r9] = r10;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r9 = 2;
            r10 = 50;
            r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r8[r9] = r10;	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r7 = java.lang.String.format(r7, r8);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            r8 = 0;
            r3 = r3.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0059, all -> 0x0170 }
            if (r3 == 0) goto L_0x0103;
        L_0x00da:
            r7 = r3.moveToFirst();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            if (r7 == 0) goto L_0x0103;
        L_0x00e0:
            r4 = r3.getCount();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7 = new org.json.JSONArray;	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7.<init>();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
        L_0x00e9:
            r8 = r3.isAfterLast();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            if (r8 != 0) goto L_0x00fe;
        L_0x00ef:
            r8 = 1;
            r8 = r3.getString(r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7.put(r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r3.moveToNext();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            goto L_0x00e9;
        L_0x00fb:
            r4 = move-exception;
            goto L_0x005b;
        L_0x00fe:
            r8 = "ap";
            r6.put(r8, r7);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
        L_0x0103:
            r7 = "type";
            r8 = "1";
            r0.put(r7, r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7 = "cuid";
            r8 = com.baidu.location.h.c.a();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r8 = r8.b;	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r0.put(r7, r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7 = "ver";
            r8 = "1";
            r0.put(r7, r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r7 = "prod";
            r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r8.<init>();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r9 = com.baidu.location.h.c.e;	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r8 = r8.append(r9);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r9 = ":";
            r8 = r8.append(r9);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r9 = com.baidu.location.h.c.d;	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r8 = r8.append(r9);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r8 = r8.toString();	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            r0.put(r7, r8);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
            if (r5 != 0) goto L_0x0140;
        L_0x013e:
            if (r4 == 0) goto L_0x0145;
        L_0x0140:
            r4 = "model";
            r0.put(r4, r6);	 Catch:{ Exception -> 0x00fb, all -> 0x0172 }
        L_0x0145:
            if (r3 == 0) goto L_0x014a;
        L_0x0147:
            r3.close();	 Catch:{ Exception -> 0x0167 }
        L_0x014a:
            if (r2 == 0) goto L_0x0183;
        L_0x014c:
            r2.close();	 Catch:{ Exception -> 0x0152 }
            r2 = r0;
            goto L_0x0066;
        L_0x0152:
            r2 = move-exception;
            r2 = r0;
            goto L_0x0066;
        L_0x0156:
            r2 = move-exception;
            r2 = r0;
            goto L_0x0066;
        L_0x015a:
            r0 = move-exception;
            r2 = r1;
        L_0x015c:
            if (r1 == 0) goto L_0x0161;
        L_0x015e:
            r1.close();	 Catch:{ Exception -> 0x016c }
        L_0x0161:
            if (r2 == 0) goto L_0x0166;
        L_0x0163:
            r2.close();	 Catch:{ Exception -> 0x016e }
        L_0x0166:
            throw r0;
        L_0x0167:
            r3 = move-exception;
            goto L_0x014a;
        L_0x0169:
            r3 = move-exception;
            goto L_0x0060;
        L_0x016c:
            r1 = move-exception;
            goto L_0x0161;
        L_0x016e:
            r1 = move-exception;
            goto L_0x0166;
        L_0x0170:
            r0 = move-exception;
            goto L_0x015c;
        L_0x0172:
            r0 = move-exception;
            r1 = r3;
            goto L_0x015c;
        L_0x0175:
            r0 = move-exception;
            r0 = r1;
            r2 = r1;
            r3 = r1;
            goto L_0x005b;
        L_0x017b:
            r2 = move-exception;
            r2 = r1;
            r3 = r1;
            goto L_0x005b;
        L_0x0180:
            r0 = r1;
            goto L_0x0095;
        L_0x0183:
            r2 = r0;
            goto L_0x0066;
        L_0x0186:
            r5 = r4;
            goto L_0x00ae;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.b.c.i():java.lang.String");
        }

        private String j() {
            JSONObject jSONObject;
            try {
                jSONObject = new JSONObject();
                try {
                    jSONObject.put("type", "2");
                    jSONObject.put("ver", "1");
                    jSONObject.put("cuid", com.baidu.location.h.c.a().b);
                    jSONObject.put(BuildConfig.EPYT_YOLPED, com.baidu.location.h.c.e + ":" + com.baidu.location.h.c.d);
                    this.s = System.currentTimeMillis();
                } catch (Exception e) {
                }
            } catch (Exception e2) {
                jSONObject = null;
            }
            return jSONObject != null ? Jni.encodeOfflineLocationUpdateRequest(jSONObject.toString()) : null;
        }

        private String k() {
            JSONObject jSONObject;
            try {
                JSONObject b = this.a.a.k().b();
                if (b != null) {
                    jSONObject = new JSONObject();
                    try {
                        jSONObject.put("type", "3");
                        jSONObject.put("ver", "1");
                        jSONObject.put("cuid", com.baidu.location.h.c.a().b);
                        jSONObject.put(BuildConfig.EPYT_YOLPED, com.baidu.location.h.c.e + ":" + com.baidu.location.h.c.d);
                        jSONObject.put("rgc", b);
                        this.s = System.currentTimeMillis();
                    } catch (Exception e) {
                    }
                } else {
                    jSONObject = null;
                }
            } catch (Exception e2) {
                jSONObject = null;
            }
            return jSONObject != null ? Jni.encodeOfflineLocationUpdateRequest(jSONObject.toString()) : null;
        }

        private boolean l() {
            Cursor rawQuery;
            Throwable th;
            Cursor cursor = null;
            boolean z = true;
            try {
                rawQuery = this.a.h.rawQuery("SELECT COUNT(*) FROM AP;", null);
                try {
                    cursor = this.a.h.rawQuery("SELECT COUNT(*) FROM CL", null);
                    if (!(rawQuery == null || !rawQuery.moveToFirst() || cursor == null || !cursor.moveToFirst() || (rawQuery.getInt(0) == 0 && cursor.getInt(0) == 0))) {
                        z = false;
                    }
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e2) {
                        }
                    }
                } catch (Exception e3) {
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e4) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e5) {
                        }
                    }
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e6) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e7) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e8) {
                rawQuery = cursor;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
                return z;
            } catch (Throwable th3) {
                th = th3;
                rawQuery = cursor;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
            return z;
        }

        public void a() {
            this.f = true;
            this.h = this.b;
            this.k.clear();
            this.k.put("qt", this.c);
            this.k.put("req", this.d);
        }

        public void a(boolean z) {
            if (!z || this.j == null) {
                this.f = false;
                c();
                return;
            }
            new Thread(this) {
                final /* synthetic */ c a;

                {
                    this.a = r1;
                }

                public void run() {
                    Exception exception;
                    Exception exception2;
                    Iterator keys;
                    StringBuffer stringBuffer;
                    StringBuffer stringBuffer2;
                    StringBuffer stringBuffer3;
                    Object obj;
                    Object obj2;
                    Object obj3;
                    int i;
                    int i2;
                    String str;
                    String string;
                    Double valueOf;
                    int i3;
                    Object obj4;
                    int i4;
                    int i5;
                    int i6;
                    Object obj5;
                    Object obj6;
                    Object obj7;
                    int i7;
                    super.run();
                    if (this.a.a.h == null || this.a.a.i == null || !this.a.a.h.isOpen() || !this.a.a.i.isOpen()) {
                        this.a.f = false;
                        return;
                    }
                    JSONObject jSONObject;
                    JSONObject jSONObject2;
                    JSONObject jSONObject3;
                    int i8;
                    Object obj8;
                    JSONObject jSONObject4 = null;
                    try {
                        if (this.a.j != null) {
                            jSONObject = new JSONObject(this.a.j);
                            try {
                                jSONObject2 = jSONObject.has("model") ? jSONObject.getJSONObject("model") : null;
                            } catch (Exception e) {
                                exception = e;
                                jSONObject2 = null;
                                exception2 = exception;
                                exception2.printStackTrace();
                                this.a.a.h.beginTransaction();
                                this.a.a.i.beginTransaction();
                                if (jSONObject4 != null) {
                                    this.a.a.a.k().a(jSONObject4);
                                }
                                this.a.r = System.currentTimeMillis();
                                this.a.e.a(jSONObject.getString("bdlist").split(VoiceWakeuperAidl.PARAMS_SEPARATE));
                                this.a.e.a(jSONObject.getJSONObject("loadurl").getString("host"), jSONObject.getJSONObject("loadurl").getString("module"), jSONObject.getJSONObject("loadurl").getString("req"));
                                jSONObject3 = jSONObject2.getJSONObject("cell");
                                keys = jSONObject3.keys();
                                stringBuffer = new StringBuffer();
                                stringBuffer2 = new StringBuffer();
                                stringBuffer3 = new StringBuffer();
                                obj = 1;
                                obj2 = 1;
                                obj3 = 1;
                                i = 0;
                                i8 = 0;
                                i2 = 0;
                                while (keys.hasNext()) {
                                    str = (String) keys.next();
                                    string = jSONObject3.getString(str);
                                    valueOf = Double.valueOf(string.split(",")[3]);
                                    if (obj2 == null) {
                                        stringBuffer2.append(',');
                                    } else {
                                        obj2 = null;
                                    }
                                    stringBuffer2.append(str);
                                    i8++;
                                    if (valueOf.doubleValue() <= 0.0d) {
                                        if (obj == null) {
                                            stringBuffer.append(',');
                                        } else {
                                            obj = null;
                                        }
                                        try {
                                            stringBuffer.append(str);
                                            i3 = i + 1;
                                            obj4 = obj;
                                        } catch (Exception e2) {
                                            this.a.c();
                                            return;
                                        } finally {
                                            try {
                                                if (this.a.a.h != null && this.a.a.h.isOpen()) {
                                                    this.a.a.h.endTransaction();
                                                }
                                                if (this.a.a.i != null && this.a.a.i.isOpen()) {
                                                    this.a.a.i.endTransaction();
                                                }
                                            } catch (Exception e3) {
                                            }
                                            this.a.j = null;
                                            this.a.f = false;
                                        }
                                    } else {
                                        if (obj3 == null) {
                                            stringBuffer3.append(',');
                                        } else {
                                            obj3 = null;
                                        }
                                        stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                        i2++;
                                        i3 = i;
                                        obj4 = obj;
                                    }
                                    if (i8 >= 100) {
                                        this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                        obj2 = 1;
                                        stringBuffer2.setLength(0);
                                        i8 -= 100;
                                    }
                                    if (i2 >= 100) {
                                        this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                        obj3 = 1;
                                        stringBuffer3.setLength(0);
                                        i2 -= 100;
                                    }
                                    if (i3 < 100) {
                                        this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                        obj4 = 1;
                                        stringBuffer.setLength(0);
                                        i3 -= 100;
                                    }
                                    obj = obj4;
                                    i = i3;
                                }
                                if (i8 > 0) {
                                    this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                }
                                if (i2 > 0) {
                                    this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                }
                                if (i > 0) {
                                    this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                }
                                jSONObject3 = jSONObject2.getJSONObject("ap");
                                keys = jSONObject3.keys();
                                i4 = 0;
                                i5 = 0;
                                i6 = 0;
                                obj5 = 1;
                                obj6 = 1;
                                obj4 = 1;
                                stringBuffer = new StringBuffer();
                                stringBuffer2 = new StringBuffer();
                                stringBuffer3 = new StringBuffer();
                                while (keys.hasNext()) {
                                    str = (String) keys.next();
                                    string = jSONObject3.getString(str);
                                    valueOf = Double.valueOf(string.split(",")[3]);
                                    if (obj6 == null) {
                                        stringBuffer2.append(',');
                                    } else {
                                        obj6 = null;
                                    }
                                    stringBuffer2.append(str);
                                    i5++;
                                    if (valueOf.doubleValue() <= 0.0d) {
                                        if (obj5 == null) {
                                            stringBuffer.append(',');
                                        } else {
                                            obj5 = null;
                                        }
                                        stringBuffer.append(str);
                                        obj8 = obj4;
                                        i = i6;
                                        i6 = i4 + 1;
                                        obj7 = obj8;
                                    } else {
                                        if (obj4 == null) {
                                            stringBuffer3.append(',');
                                        } else {
                                            obj4 = null;
                                        }
                                        stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                        i3 = i6 + 1;
                                        i6 = i4;
                                        i7 = i3;
                                        obj7 = obj4;
                                        i = i7;
                                    }
                                    if (i5 >= 100) {
                                        this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                        obj6 = 1;
                                        stringBuffer2.setLength(0);
                                        i5 -= 100;
                                    }
                                    if (i >= 100) {
                                        this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                        obj7 = 1;
                                        stringBuffer3.setLength(0);
                                        i -= 100;
                                    }
                                    if (i6 <= 0) {
                                        this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                    }
                                    i4 = i6;
                                    i6 = i;
                                    obj4 = obj7;
                                }
                                if (i5 > 0) {
                                    this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                }
                                if (i6 > 0) {
                                    this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                }
                                if (i4 > 0) {
                                    this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                }
                                this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(200000)}));
                                this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(200000)}));
                                this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                                this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                                this.a.c();
                                this.a.a.h.setTransactionSuccessful();
                                this.a.a.i.setTransactionSuccessful();
                                this.a.a.h.endTransaction();
                                this.a.a.i.endTransaction();
                                this.a.j = null;
                                this.a.f = false;
                            }
                            try {
                                if (jSONObject.has("rgc")) {
                                    jSONObject4 = jSONObject.getJSONObject("rgc");
                                }
                            } catch (Exception e4) {
                                exception2 = e4;
                                exception2.printStackTrace();
                                this.a.a.h.beginTransaction();
                                this.a.a.i.beginTransaction();
                                if (jSONObject4 != null) {
                                    this.a.a.a.k().a(jSONObject4);
                                }
                                this.a.r = System.currentTimeMillis();
                                this.a.e.a(jSONObject.getString("bdlist").split(VoiceWakeuperAidl.PARAMS_SEPARATE));
                                this.a.e.a(jSONObject.getJSONObject("loadurl").getString("host"), jSONObject.getJSONObject("loadurl").getString("module"), jSONObject.getJSONObject("loadurl").getString("req"));
                                jSONObject3 = jSONObject2.getJSONObject("cell");
                                keys = jSONObject3.keys();
                                stringBuffer = new StringBuffer();
                                stringBuffer2 = new StringBuffer();
                                stringBuffer3 = new StringBuffer();
                                obj = 1;
                                obj2 = 1;
                                obj3 = 1;
                                i = 0;
                                i8 = 0;
                                i2 = 0;
                                while (keys.hasNext()) {
                                    str = (String) keys.next();
                                    string = jSONObject3.getString(str);
                                    valueOf = Double.valueOf(string.split(",")[3]);
                                    if (obj2 == null) {
                                        stringBuffer2.append(',');
                                    } else {
                                        obj2 = null;
                                    }
                                    stringBuffer2.append(str);
                                    i8++;
                                    if (valueOf.doubleValue() <= 0.0d) {
                                        if (obj == null) {
                                            stringBuffer.append(',');
                                        } else {
                                            obj = null;
                                        }
                                        stringBuffer.append(str);
                                        i3 = i + 1;
                                        obj4 = obj;
                                    } else {
                                        if (obj3 == null) {
                                            stringBuffer3.append(',');
                                        } else {
                                            obj3 = null;
                                        }
                                        stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                        i2++;
                                        i3 = i;
                                        obj4 = obj;
                                    }
                                    if (i8 >= 100) {
                                        this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                        obj2 = 1;
                                        stringBuffer2.setLength(0);
                                        i8 -= 100;
                                    }
                                    if (i2 >= 100) {
                                        this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                        obj3 = 1;
                                        stringBuffer3.setLength(0);
                                        i2 -= 100;
                                    }
                                    if (i3 < 100) {
                                        this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                        obj4 = 1;
                                        stringBuffer.setLength(0);
                                        i3 -= 100;
                                    }
                                    obj = obj4;
                                    i = i3;
                                }
                                if (i8 > 0) {
                                    this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                }
                                if (i2 > 0) {
                                    this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                }
                                if (i > 0) {
                                    this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                }
                                jSONObject3 = jSONObject2.getJSONObject("ap");
                                keys = jSONObject3.keys();
                                i4 = 0;
                                i5 = 0;
                                i6 = 0;
                                obj5 = 1;
                                obj6 = 1;
                                obj4 = 1;
                                stringBuffer = new StringBuffer();
                                stringBuffer2 = new StringBuffer();
                                stringBuffer3 = new StringBuffer();
                                while (keys.hasNext()) {
                                    str = (String) keys.next();
                                    string = jSONObject3.getString(str);
                                    valueOf = Double.valueOf(string.split(",")[3]);
                                    if (obj6 == null) {
                                        stringBuffer2.append(',');
                                    } else {
                                        obj6 = null;
                                    }
                                    stringBuffer2.append(str);
                                    i5++;
                                    if (valueOf.doubleValue() <= 0.0d) {
                                        if (obj5 == null) {
                                            stringBuffer.append(',');
                                        } else {
                                            obj5 = null;
                                        }
                                        stringBuffer.append(str);
                                        obj8 = obj4;
                                        i = i6;
                                        i6 = i4 + 1;
                                        obj7 = obj8;
                                    } else {
                                        if (obj4 == null) {
                                            stringBuffer3.append(',');
                                        } else {
                                            obj4 = null;
                                        }
                                        stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                        i3 = i6 + 1;
                                        i6 = i4;
                                        i7 = i3;
                                        obj7 = obj4;
                                        i = i7;
                                    }
                                    if (i5 >= 100) {
                                        this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                        obj6 = 1;
                                        stringBuffer2.setLength(0);
                                        i5 -= 100;
                                    }
                                    if (i >= 100) {
                                        this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                        obj7 = 1;
                                        stringBuffer3.setLength(0);
                                        i -= 100;
                                    }
                                    if (i6 <= 0) {
                                        this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                    }
                                    i4 = i6;
                                    i6 = i;
                                    obj4 = obj7;
                                }
                                if (i5 > 0) {
                                    this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                }
                                if (i6 > 0) {
                                    this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                }
                                if (i4 > 0) {
                                    this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                }
                                this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(200000)}));
                                this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(200000)}));
                                this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                                this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                                this.a.c();
                                this.a.a.h.setTransactionSuccessful();
                                this.a.a.i.setTransactionSuccessful();
                                this.a.a.h.endTransaction();
                                this.a.a.i.endTransaction();
                                this.a.j = null;
                                this.a.f = false;
                            }
                        }
                        jSONObject = null;
                        jSONObject2 = null;
                    } catch (Exception e5) {
                        exception = e5;
                        jSONObject = null;
                        jSONObject2 = null;
                        exception2 = exception;
                        exception2.printStackTrace();
                        this.a.a.h.beginTransaction();
                        this.a.a.i.beginTransaction();
                        if (jSONObject4 != null) {
                            this.a.a.a.k().a(jSONObject4);
                        }
                        this.a.r = System.currentTimeMillis();
                        this.a.e.a(jSONObject.getString("bdlist").split(VoiceWakeuperAidl.PARAMS_SEPARATE));
                        this.a.e.a(jSONObject.getJSONObject("loadurl").getString("host"), jSONObject.getJSONObject("loadurl").getString("module"), jSONObject.getJSONObject("loadurl").getString("req"));
                        jSONObject3 = jSONObject2.getJSONObject("cell");
                        keys = jSONObject3.keys();
                        stringBuffer = new StringBuffer();
                        stringBuffer2 = new StringBuffer();
                        stringBuffer3 = new StringBuffer();
                        obj = 1;
                        obj2 = 1;
                        obj3 = 1;
                        i = 0;
                        i8 = 0;
                        i2 = 0;
                        while (keys.hasNext()) {
                            str = (String) keys.next();
                            string = jSONObject3.getString(str);
                            valueOf = Double.valueOf(string.split(",")[3]);
                            if (obj2 == null) {
                                obj2 = null;
                            } else {
                                stringBuffer2.append(',');
                            }
                            stringBuffer2.append(str);
                            i8++;
                            if (valueOf.doubleValue() <= 0.0d) {
                                if (obj3 == null) {
                                    obj3 = null;
                                } else {
                                    stringBuffer3.append(',');
                                }
                                stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                i2++;
                                i3 = i;
                                obj4 = obj;
                            } else {
                                if (obj == null) {
                                    obj = null;
                                } else {
                                    stringBuffer.append(',');
                                }
                                stringBuffer.append(str);
                                i3 = i + 1;
                                obj4 = obj;
                            }
                            if (i8 >= 100) {
                                this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                obj2 = 1;
                                stringBuffer2.setLength(0);
                                i8 -= 100;
                            }
                            if (i2 >= 100) {
                                this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                obj3 = 1;
                                stringBuffer3.setLength(0);
                                i2 -= 100;
                            }
                            if (i3 < 100) {
                                this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                obj4 = 1;
                                stringBuffer.setLength(0);
                                i3 -= 100;
                            }
                            obj = obj4;
                            i = i3;
                        }
                        if (i8 > 0) {
                            this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                        }
                        if (i2 > 0) {
                            this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                        }
                        if (i > 0) {
                            this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                        }
                        jSONObject3 = jSONObject2.getJSONObject("ap");
                        keys = jSONObject3.keys();
                        i4 = 0;
                        i5 = 0;
                        i6 = 0;
                        obj5 = 1;
                        obj6 = 1;
                        obj4 = 1;
                        stringBuffer = new StringBuffer();
                        stringBuffer2 = new StringBuffer();
                        stringBuffer3 = new StringBuffer();
                        while (keys.hasNext()) {
                            str = (String) keys.next();
                            string = jSONObject3.getString(str);
                            valueOf = Double.valueOf(string.split(",")[3]);
                            if (obj6 == null) {
                                obj6 = null;
                            } else {
                                stringBuffer2.append(',');
                            }
                            stringBuffer2.append(str);
                            i5++;
                            if (valueOf.doubleValue() <= 0.0d) {
                                if (obj4 == null) {
                                    obj4 = null;
                                } else {
                                    stringBuffer3.append(',');
                                }
                                stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                i3 = i6 + 1;
                                i6 = i4;
                                i7 = i3;
                                obj7 = obj4;
                                i = i7;
                            } else {
                                if (obj5 == null) {
                                    obj5 = null;
                                } else {
                                    stringBuffer.append(',');
                                }
                                stringBuffer.append(str);
                                obj8 = obj4;
                                i = i6;
                                i6 = i4 + 1;
                                obj7 = obj8;
                            }
                            if (i5 >= 100) {
                                this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                obj6 = 1;
                                stringBuffer2.setLength(0);
                                i5 -= 100;
                            }
                            if (i >= 100) {
                                this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                obj7 = 1;
                                stringBuffer3.setLength(0);
                                i -= 100;
                            }
                            if (i6 <= 0) {
                                this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                            }
                            i4 = i6;
                            i6 = i;
                            obj4 = obj7;
                        }
                        if (i5 > 0) {
                            this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                        }
                        if (i6 > 0) {
                            this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                        }
                        if (i4 > 0) {
                            this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                        }
                        this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(200000)}));
                        this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(200000)}));
                        this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                        this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                        this.a.c();
                        this.a.a.h.setTransactionSuccessful();
                        this.a.a.i.setTransactionSuccessful();
                        this.a.a.h.endTransaction();
                        this.a.a.i.endTransaction();
                        this.a.j = null;
                        this.a.f = false;
                    }
                    this.a.a.h.beginTransaction();
                    this.a.a.i.beginTransaction();
                    if (jSONObject4 != null) {
                        this.a.a.a.k().a(jSONObject4);
                    }
                    if (jSONObject != null && jSONObject.has("type") && jSONObject.getString("type").equals("0")) {
                        this.a.r = System.currentTimeMillis();
                    }
                    if (jSONObject != null && jSONObject.has("bdlist")) {
                        this.a.e.a(jSONObject.getString("bdlist").split(VoiceWakeuperAidl.PARAMS_SEPARATE));
                    }
                    if (jSONObject != null && jSONObject.has("loadurl")) {
                        this.a.e.a(jSONObject.getJSONObject("loadurl").getString("host"), jSONObject.getJSONObject("loadurl").getString("module"), jSONObject.getJSONObject("loadurl").getString("req"));
                    }
                    if (jSONObject2 != null && jSONObject2.has("cell")) {
                        jSONObject3 = jSONObject2.getJSONObject("cell");
                        keys = jSONObject3.keys();
                        stringBuffer = new StringBuffer();
                        stringBuffer2 = new StringBuffer();
                        stringBuffer3 = new StringBuffer();
                        obj = 1;
                        obj2 = 1;
                        obj3 = 1;
                        i = 0;
                        i8 = 0;
                        i2 = 0;
                        while (keys.hasNext()) {
                            str = (String) keys.next();
                            string = jSONObject3.getString(str);
                            valueOf = Double.valueOf(string.split(",")[3]);
                            if (obj2 == null) {
                                obj2 = null;
                            } else {
                                stringBuffer2.append(',');
                            }
                            stringBuffer2.append(str);
                            i8++;
                            if (valueOf.doubleValue() <= 0.0d) {
                                if (obj3 == null) {
                                    obj3 = null;
                                } else {
                                    stringBuffer3.append(',');
                                }
                                stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                i2++;
                                i3 = i;
                                obj4 = obj;
                            } else {
                                if (obj == null) {
                                    obj = null;
                                } else {
                                    stringBuffer.append(',');
                                }
                                stringBuffer.append(str);
                                i3 = i + 1;
                                obj4 = obj;
                            }
                            if (i8 >= 100) {
                                this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                obj2 = 1;
                                stringBuffer2.setLength(0);
                                i8 -= 100;
                            }
                            if (i2 >= 100) {
                                this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                obj3 = 1;
                                stringBuffer3.setLength(0);
                                i2 -= 100;
                            }
                            if (i3 < 100) {
                                this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                                obj4 = 1;
                                stringBuffer.setLength(0);
                                i3 -= 100;
                            }
                            obj = obj4;
                            i = i3;
                        }
                        if (i8 > 0) {
                            this.a.a.i.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                        }
                        if (i2 > 0) {
                            this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO CL (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                        }
                        if (i > 0) {
                            this.a.a.h.execSQL(String.format("DELETE FROM CL WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                        }
                    }
                    if (jSONObject2 != null && jSONObject2.has("ap")) {
                        jSONObject3 = jSONObject2.getJSONObject("ap");
                        keys = jSONObject3.keys();
                        i4 = 0;
                        i5 = 0;
                        i6 = 0;
                        obj5 = 1;
                        obj6 = 1;
                        obj4 = 1;
                        stringBuffer = new StringBuffer();
                        stringBuffer2 = new StringBuffer();
                        stringBuffer3 = new StringBuffer();
                        while (keys.hasNext()) {
                            str = (String) keys.next();
                            string = jSONObject3.getString(str);
                            valueOf = Double.valueOf(string.split(",")[3]);
                            if (obj6 == null) {
                                obj6 = null;
                            } else {
                                stringBuffer2.append(',');
                            }
                            stringBuffer2.append(str);
                            i5++;
                            if (valueOf.doubleValue() <= 0.0d) {
                                if (obj4 == null) {
                                    obj4 = null;
                                } else {
                                    stringBuffer3.append(',');
                                }
                                stringBuffer3.append('(').append(str).append(',').append(string).append("," + (System.currentTimeMillis() / 1000)).append(')');
                                i3 = i6 + 1;
                                i6 = i4;
                                i7 = i3;
                                obj7 = obj4;
                                i = i7;
                            } else {
                                if (obj5 == null) {
                                    obj5 = null;
                                } else {
                                    stringBuffer.append(',');
                                }
                                stringBuffer.append(str);
                                obj8 = obj4;
                                i = i6;
                                i6 = i4 + 1;
                                obj7 = obj8;
                            }
                            if (i5 >= 100) {
                                this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                                obj6 = 1;
                                stringBuffer2.setLength(0);
                                i5 -= 100;
                            }
                            if (i >= 100) {
                                this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                                obj7 = 1;
                                stringBuffer3.setLength(0);
                                i -= 100;
                            }
                            if (i6 <= 0) {
                                this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                            }
                            i4 = i6;
                            i6 = i;
                            obj4 = obj7;
                        }
                        if (i5 > 0) {
                            this.a.a.i.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer2.toString()}));
                        }
                        if (i6 > 0) {
                            this.a.a.h.execSQL(String.format("INSERT OR REPLACE INTO AP (id,x,y,r,cl,timestamp) VALUES %s;", new Object[]{stringBuffer3.toString()}));
                        }
                        if (i4 > 0) {
                            this.a.a.h.execSQL(String.format("DELETE FROM AP WHERE id IN (%s);", new Object[]{stringBuffer.toString()}));
                        }
                    }
                    this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(200000)}));
                    this.a.a.h.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY timestamp DESC, frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(200000)}));
                    this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"AP", "AP", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                    this.a.a.i.execSQL(String.format("DELETE FROM %s WHERE id NOT IN (SELECT id FROM %s ORDER BY frequency DESC LIMIT %d);", new Object[]{"CL", "CL", Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL)}));
                    if (!(jSONObject2 == null || jSONObject2.has("ap") || jSONObject2.has("cell"))) {
                        this.a.c();
                    }
                    this.a.a.h.setTransactionSuccessful();
                    this.a.a.i.setTransactionSuccessful();
                    try {
                        if (this.a.a.h != null && this.a.a.h.isOpen()) {
                            this.a.a.h.endTransaction();
                        }
                        if (this.a.a.i != null && this.a.a.i.isOpen()) {
                            this.a.a.i.endTransaction();
                        }
                    } catch (Exception e6) {
                    }
                    this.a.j = null;
                    this.a.f = false;
                }
            }.start();
        }

        void b() {
            if (f() && !this.f) {
                this.a.g.g();
            }
        }
    }

    b(d dVar) {
        SQLiteDatabase openOrCreateDatabase;
        SQLiteDatabase sQLiteDatabase = null;
        this.a = dVar;
        try {
            File file;
            file = new File(this.a.c(), "ofl_location.db");
            if (!file.exists()) {
                file.createNewFile();
            }
            openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
        } catch (Exception e) {
            openOrCreateDatabase = null;
        }
        this.h = openOrCreateDatabase;
        if (this.h != null) {
            try {
                this.h.execSQL("CREATE TABLE IF NOT EXISTS AP (id LONG PRIMARY KEY,x DOUBLE,y DOUBLE,r INTEGER,cl DOUBLE,timestamp INTEGER, frequency INTEGER DEFAULT 0);");
                this.h.execSQL("CREATE TABLE IF NOT EXISTS CL (id LONG PRIMARY KEY,x DOUBLE,y DOUBLE,r INTEGER,cl DOUBLE,timestamp INTEGER, frequency INTEGER DEFAULT 0);");
            } catch (Exception e2) {
            }
        }
        try {
            file = new File(this.a.c(), "ofl_statistics.db");
            if (!file.exists()) {
                file.createNewFile();
            }
            sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
        } catch (Exception e3) {
        }
        this.i = sQLiteDatabase;
        if (this.i != null) {
            try {
                this.i.execSQL("CREATE TABLE IF NOT EXISTS AP (id LONG PRIMARY KEY, originid VARCHAR(15), frequency INTEGER DEFAULT 0);");
                this.i.execSQL("CREATE TABLE IF NOT EXISTS CL (id LONG PRIMARY KEY, originid VARCHAR(40), frequency INTEGER DEFAULT 0);");
            } catch (Exception e4) {
            }
        }
    }

    private double a(double d, double d2, double d3, double d4) {
        double d5 = d4 - d2;
        double d6 = d3 - d;
        double toRadians = Math.toRadians(d);
        Math.toRadians(d2);
        double toRadians2 = Math.toRadians(d3);
        Math.toRadians(d4);
        d5 = Math.toRadians(d5);
        d6 = Math.toRadians(d6);
        d5 = (Math.sin(d5 / 2.0d) * ((Math.cos(toRadians) * Math.cos(toRadians2)) * Math.sin(d5 / 2.0d))) + (Math.sin(d6 / 2.0d) * Math.sin(d6 / 2.0d));
        return (Math.atan2(Math.sqrt(d5), Math.sqrt(WeightedLatLng.DEFAULT_INTENSITY - d5)) * 2.0d) * 6378137.0d;
    }

    private int a(ArrayList<a> arrayList, double d) {
        if (arrayList.size() == 0) {
            return 0;
        }
        int i = 0;
        while (true) {
            int i2;
            int i3;
            if (arrayList.size() >= 3) {
                double d2 = 0.0d;
                i2 = 0;
                double d3 = 0.0d;
                while (i2 < arrayList.size()) {
                    double d4 = ((a) arrayList.get(i2)).a + d3;
                    i2++;
                    d2 = ((a) arrayList.get(i2)).b + d2;
                    d3 = d4;
                }
                double size = d3 / ((double) arrayList.size());
                double size2 = d2 / ((double) arrayList.size());
                int i4 = 0;
                int i5 = -1;
                double d5 = -1.0d;
                while (i4 < arrayList.size()) {
                    int i6;
                    double a = a(size2, size, ((a) arrayList.get(i4)).b, ((a) arrayList.get(i4)).a);
                    if (a > d5) {
                        i6 = i4;
                    } else {
                        i6 = i5;
                        a = d5;
                    }
                    i4++;
                    i5 = i6;
                    d5 = a;
                }
                if (d5 > d && i5 >= 0 && i5 < arrayList.size()) {
                    i++;
                    arrayList.remove(i5);
                    i2 = 1;
                    i3 = i;
                    if (i2 == 1) {
                        return i3;
                    }
                    i = i3;
                }
            }
            i2 = 0;
            i3 = i;
            if (i2 == 1) {
                return i3;
            }
            i = i3;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.baidu.location.BDLocation a(java.lang.Long r19) {
        /*
        r18 = this;
        r2 = 0;
        r0 = r18;
        r0.p = r2;
        r8 = 0;
        r6 = 0;
        r4 = 0;
        r3 = 0;
        r0 = r18;
        r2 = r0.e;
        if (r2 == 0) goto L_0x0046;
    L_0x0011:
        r0 = r18;
        r2 = r0.e;
        r0 = r19;
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0046;
    L_0x001d:
        r3 = 1;
        r0 = r18;
        r6 = r0.c;
        r0 = r18;
        r4 = r0.d;
        r0 = r18;
        r8 = r0.b;
    L_0x002a:
        if (r3 == 0) goto L_0x010c;
    L_0x002c:
        r2 = new com.baidu.location.BDLocation;
        r2.<init>();
        r3 = (float) r8;
        r2.setRadius(r3);
        r2.setLatitude(r4);
        r2.setLongitude(r6);
        r3 = "cl";
        r2.setNetworkLocationType(r3);
        r3 = 66;
        r2.setLocType(r3);
    L_0x0045:
        return r2;
    L_0x0046:
        r2 = 0;
        r9 = java.util.Locale.US;
        r10 = "SELECT * FROM CL WHERE id = %d AND timestamp + %d > %d;";
        r11 = 3;
        r11 = new java.lang.Object[r11];
        r12 = 0;
        r11[r12] = r19;
        r12 = 1;
        r13 = 15552000; // 0xed4e00 float:2.1792994E-38 double:7.683709E-317;
        r13 = java.lang.Integer.valueOf(r13);
        r11[r12] = r13;
        r12 = 2;
        r13 = java.lang.System.currentTimeMillis();
        r15 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r13 = r13 / r15;
        r13 = java.lang.Long.valueOf(r13);
        r11[r12] = r13;
        r9 = java.lang.String.format(r9, r10, r11);
        r0 = r18;
        r10 = r0.h;	 Catch:{ Exception -> 0x00f5, all -> 0x0100 }
        r11 = 0;
        r2 = r10.rawQuery(r9, r11);	 Catch:{ Exception -> 0x00f5, all -> 0x0100 }
        if (r2 == 0) goto L_0x00e4;
    L_0x0078:
        r9 = r2.moveToFirst();	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        if (r9 == 0) goto L_0x00e4;
    L_0x007e:
        r9 = "cl";
        r9 = r2.getColumnIndex(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r9 = r2.getDouble(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r11 = 0;
        r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r9 <= 0) goto L_0x00e4;
    L_0x008e:
        r3 = 1;
        r9 = "x";
        r9 = r2.getColumnIndex(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r6 = r2.getDouble(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r9 = "y";
        r9 = r2.getColumnIndex(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r4 = r2.getDouble(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r9 = "r";
        r9 = r2.getColumnIndex(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r8 = r2.getInt(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r9 = "timestamp";
        r9 = r2.getColumnIndex(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r9 = r2.getInt(r9);	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r10 = 604800; // 0x93a80 float:8.47505E-40 double:2.98811E-318;
        r9 = r9 + r10;
        r9 = (long) r9;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r11 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r13 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r11 = r11 / r13;
        r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r9 >= 0) goto L_0x00cc;
    L_0x00c7:
        r9 = 1;
        r0 = r18;
        r0.p = r9;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
    L_0x00cc:
        r9 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r8 >= r9) goto L_0x00ee;
    L_0x00d0:
        r8 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
    L_0x00d2:
        r0 = r18;
        r0.c = r6;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r0 = r18;
        r0.d = r4;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r0 = r18;
        r0.b = r8;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
        r0 = r19;
        r1 = r18;
        r1.e = r0;	 Catch:{ Exception -> 0x00f5, all -> 0x0111 }
    L_0x00e4:
        if (r2 == 0) goto L_0x002a;
    L_0x00e6:
        r2.close();	 Catch:{ Exception -> 0x00eb }
        goto L_0x002a;
    L_0x00eb:
        r2 = move-exception;
        goto L_0x002a;
    L_0x00ee:
        r9 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        if (r9 >= r8) goto L_0x00d2;
    L_0x00f2:
        r8 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        goto L_0x00d2;
    L_0x00f5:
        r9 = move-exception;
        if (r2 == 0) goto L_0x002a;
    L_0x00f8:
        r2.close();	 Catch:{ Exception -> 0x00fd }
        goto L_0x002a;
    L_0x00fd:
        r2 = move-exception;
        goto L_0x002a;
    L_0x0100:
        r3 = move-exception;
        r17 = r3;
        r3 = r2;
        r2 = r17;
    L_0x0106:
        if (r3 == 0) goto L_0x010b;
    L_0x0108:
        r3.close();	 Catch:{ Exception -> 0x010f }
    L_0x010b:
        throw r2;
    L_0x010c:
        r2 = 0;
        goto L_0x0045;
    L_0x010f:
        r3 = move-exception;
        goto L_0x010b;
    L_0x0111:
        r3 = move-exception;
        r17 = r3;
        r3 = r2;
        r2 = r17;
        goto L_0x0106;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.b.a(java.lang.Long):com.baidu.location.BDLocation");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.baidu.location.BDLocation a(java.util.LinkedHashMap<java.lang.String, java.lang.Integer> r35, com.baidu.location.BDLocation r36, int r37) {
        /*
        r34 = this;
        r0 = r34;
        r2 = r0.o;
        r3 = 0;
        r2.setLength(r3);
        r5 = 0;
        r3 = 0;
        r2 = 0;
        if (r36 == 0) goto L_0x03b7;
    L_0x000f:
        r2 = 1;
        r3 = r36.getLatitude();
        r5 = r36.getLongitude();
        r19 = r2;
    L_0x001a:
        r26 = 0;
        r24 = 0;
        r22 = 0;
        r21 = 0;
        r10 = new java.lang.StringBuffer;
        r10.<init>();
        r7 = 1;
        r2 = r35.entrySet();
        r11 = r2.iterator();
        r2 = 0;
        r8 = r2;
        r9 = r7;
    L_0x0033:
        r2 = r35.size();
        r7 = 30;
        r2 = java.lang.Math.min(r2, r7);
        if (r8 >= r2) goto L_0x0088;
    L_0x003f:
        r2 = r11.next();
        r2 = (java.util.Map.Entry) r2;
        r7 = r2.getKey();
        r7 = (java.lang.String) r7;
        r2 = r2.getValue();
        r2 = (java.lang.Integer) r2;
        r12 = r2.intValue();
        if (r12 >= 0) goto L_0x0060;
    L_0x0057:
        r2 = r2.intValue();
        r2 = -r2;
        r2 = java.lang.Integer.valueOf(r2);
    L_0x0060:
        r12 = com.baidu.location.Jni.encode3(r7);
        if (r12 != 0) goto L_0x006c;
    L_0x0066:
        r7 = r9;
    L_0x0067:
        r2 = r8 + 1;
        r8 = r2;
        r9 = r7;
        goto L_0x0033;
    L_0x006c:
        r0 = r34;
        r13 = r0.n;
        r13.put(r12, r7);
        if (r9 == 0) goto L_0x0082;
    L_0x0075:
        r9 = 0;
    L_0x0076:
        r0 = r34;
        r7 = r0.m;
        r7.put(r12, r2);
        r10.append(r12);
        r7 = r9;
        goto L_0x0067;
    L_0x0082:
        r7 = 44;
        r10.append(r7);
        goto L_0x0076;
    L_0x0088:
        r2 = java.util.Locale.US;
        r7 = "SELECT * FROM AP WHERE id IN (%s) AND timestamp+%d>%d;";
        r8 = 3;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r8[r9] = r10;
        r9 = 1;
        r10 = 7776000; // 0x76a700 float:1.0896497E-38 double:3.8418545E-317;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 2;
        r10 = java.lang.System.currentTimeMillis();
        r12 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r10 = r10 / r12;
        r10 = java.lang.Long.valueOf(r10);
        r8[r9] = r10;
        r7 = java.lang.String.format(r2, r7, r8);
        r2 = 0;
        r0 = r34;
        r8 = r0.h;	 Catch:{ Exception -> 0x0363, all -> 0x035d }
        r9 = 0;
        r20 = r8.rawQuery(r7, r9);	 Catch:{ Exception -> 0x0363, all -> 0x035d }
        if (r20 == 0) goto L_0x03ae;
    L_0x00ba:
        r2 = r20.moveToFirst();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        if (r2 == 0) goto L_0x03ae;
    L_0x00c0:
        r27 = new java.util.ArrayList;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r27.<init>();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
    L_0x00c5:
        r2 = r20.isAfterLast();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        if (r2 != 0) goto L_0x022f;
    L_0x00cb:
        r2 = 0;
        r0 = r20;
        r7 = r0.getLong(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r11 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = 1;
        r0 = r20;
        r9 = r0.getDouble(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = 2;
        r0 = r20;
        r7 = r0.getDouble(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = 3;
        r0 = r20;
        r13 = r0.getInt(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = 4;
        r0 = r20;
        r14 = r0.getDouble(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = 5;
        r0 = r20;
        r2 = r0.getInt(r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r34;
        r12 = r0.l;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r12.add(r11);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r12 = 604800; // 0x93a80 float:8.47505E-40 double:2.98811E-318;
        r2 = r2 + r12;
        r0 = (long) r2;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = r0;
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r30 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r28 = r28 / r30;
        r2 = (r16 > r28 ? 1 : (r16 == r28 ? 0 : -1));
        if (r2 >= 0) goto L_0x0160;
    L_0x0113:
        r0 = r34;
        r2 = r0.o;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = r2.length();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        if (r2 <= 0) goto L_0x0126;
    L_0x011d:
        r0 = r34;
        r2 = r0.o;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r12 = ",";
        r2.append(r12);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
    L_0x0126:
        r0 = r34;
        r2 = r0.o;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = "(%d,\"%s\",%d)";
        r17 = 3;
        r0 = r17;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17 = r0;
        r18 = 0;
        r17[r18] = r11;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r18 = 1;
        r0 = r34;
        r0 = r0.n;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r28 = r0;
        r0 = r28;
        r28 = r0.get(r11);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17[r18] = r28;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r18 = 2;
        r28 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
        r28 = java.lang.Integer.valueOf(r28);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17[r18] = r28;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r16;
        r1 = r17;
        r12 = java.lang.String.format(r12, r0, r1);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2.append(r12);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
    L_0x0160:
        r16 = 0;
        r2 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r2 > 0) goto L_0x0197;
    L_0x0166:
        r20.moveToNext();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        goto L_0x00c5;
    L_0x016b:
        r2 = move-exception;
        r2 = r20;
        r3 = r21;
        r10 = r22;
        r8 = r24;
        r4 = r26;
    L_0x0176:
        if (r2 == 0) goto L_0x017b;
    L_0x0178:
        r2.close();	 Catch:{ Exception -> 0x0357 }
    L_0x017b:
        if (r3 == 0) goto L_0x0354;
    L_0x017d:
        r2 = new com.baidu.location.BDLocation;
        r2.<init>();
        r3 = (float) r4;
        r2.setRadius(r3);
        r2.setLatitude(r10);
        r2.setLongitude(r8);
        r3 = "wf";
        r2.setNetworkLocationType(r3);
        r3 = 66;
        r2.setLocType(r3);
    L_0x0196:
        return r2;
    L_0x0197:
        r14 = 0;
        r2 = (r9 > r14 ? 1 : (r9 == r14 ? 0 : -1));
        if (r2 <= 0) goto L_0x01a9;
    L_0x019d:
        r14 = 0;
        r2 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));
        if (r2 <= 0) goto L_0x01a9;
    L_0x01a3:
        if (r13 <= 0) goto L_0x01a9;
    L_0x01a5:
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r13 < r2) goto L_0x01b5;
    L_0x01a9:
        r20.moveToNext();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        goto L_0x00c5;
    L_0x01ae:
        r2 = move-exception;
    L_0x01af:
        if (r20 == 0) goto L_0x01b4;
    L_0x01b1:
        r20.close();	 Catch:{ Exception -> 0x035a }
    L_0x01b4:
        throw r2;
    L_0x01b5:
        r2 = 1;
        r0 = r19;
        if (r0 != r2) goto L_0x01ce;
    L_0x01ba:
        r2 = r34;
        r14 = r2.a(r3, r5, r7, r9);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = 4666723172467343360; // 0x40c3880000000000 float:0.0 double:10000.0;
        r2 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r2 <= 0) goto L_0x01ce;
    L_0x01c9:
        r20.moveToNext();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        goto L_0x00c5;
    L_0x01ce:
        r0 = r34;
        r2 = r0.m;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = (java.lang.Integer) r2;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = r2.intValue();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r11 = 30;
        r2 = java.lang.Math.max(r11, r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r11 = 100;
        r2 = java.lang.Math.min(r11, r2);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r11 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r14 = 70;
        if (r2 <= r14) goto L_0x0226;
    L_0x01ee:
        r2 = r2 + -70;
        r14 = (double) r2;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
        r14 = r14 / r16;
        r11 = r11 + r14;
    L_0x01f6:
        r14 = 4632233691727265792; // 0x4049000000000000 float:0.0 double:50.0;
        r0 = (double) r13;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = r0;
        r13 = java.lang.Math.max(r14, r16);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r15 = 4603579539098121011; // 0x3fe3333333333333 float:4.172325E-8 double:0.6;
        r13 = java.lang.Math.pow(r13, r15);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r15 = -4634023872579145564; // 0xbfb0a3d70a3d70a4 float:9.121204E-33 double:-0.065;
        r13 = r13 * r15;
        r11 = r11 * r13;
        r16 = java.lang.Math.exp(r11);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r11 = new com.baidu.location.e.b$a;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r18 = 0;
        r12 = r9;
        r14 = r7;
        r11.<init>(r12, r14, r16);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r27;
        r0.add(r11);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r20.moveToNext();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        goto L_0x00c5;
    L_0x0226:
        r2 = r2 + -70;
        r14 = (double) r2;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r16 = 4632233691727265792; // 0x4049000000000000 float:0.0 double:50.0;
        r14 = r14 / r16;
        r11 = r11 + r14;
        goto L_0x01f6;
    L_0x022f:
        r7 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r0 = r34;
        r1 = r27;
        r0.a(r1, r7);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r7 = 0;
        r12 = 0;
        r10 = 0;
        r2 = 0;
        r16 = r2;
    L_0x0244:
        r2 = r27.size();	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r16;
        if (r0 >= r2) goto L_0x0287;
    L_0x024c:
        r0 = r27;
        r1 = r16;
        r2 = r0.get(r1);	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r2 = (com.baidu.location.e.b.a) r2;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r14 = r2.c;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17 = 0;
        r9 = (r14 > r17 ? 1 : (r14 == r17 ? 0 : -1));
        if (r9 > 0) goto L_0x026f;
    L_0x025e:
        r32 = r10;
        r9 = r12;
        r11 = r7;
        r7 = r32;
    L_0x0264:
        r2 = r16 + 1;
        r16 = r2;
        r32 = r7;
        r7 = r11;
        r12 = r9;
        r10 = r32;
        goto L_0x0244;
    L_0x026f:
        r14 = r2.a;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r2.c;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17 = r0;
        r14 = r14 * r17;
        r14 = r14 + r7;
        r7 = r2.b;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r0 = r2.c;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r17 = r0;
        r7 = r7 * r17;
        r12 = r12 + r7;
        r7 = r2.c;	 Catch:{ Exception -> 0x016b, all -> 0x01ae }
        r7 = r7 + r10;
        r9 = r12;
        r11 = r14;
        goto L_0x0264;
    L_0x0287:
        r14 = 0;
        r2 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1));
        if (r2 <= 0) goto L_0x03a4;
    L_0x028d:
        r14 = 0;
        r2 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));
        if (r2 <= 0) goto L_0x03a4;
    L_0x0293:
        r14 = 0;
        r2 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r2 <= 0) goto L_0x03a4;
    L_0x0299:
        r8 = r7 / r10;
        r10 = r12 / r10;
        r16 = 1;
        r7 = 0;
        r2 = 0;
        r17 = r2;
        r2 = r7;
    L_0x02a4:
        r7 = r27.size();	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r0 = r17;
        if (r0 >= r7) goto L_0x02d6;
    L_0x02ac:
        r0 = (double) r2;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r21 = r0;
        r0 = r27;
        r1 = r17;
        r2 = r0.get(r1);	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r2 = (com.baidu.location.e.b.a) r2;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r12 = r2.a;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r0 = r27;
        r1 = r17;
        r2 = r0.get(r1);	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r2 = (com.baidu.location.e.b.a) r2;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r14 = r2.b;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r7 = r34;
        r12 = r7.a(r8, r10, r12, r14);	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r12 = r12 + r21;
        r7 = (float) r12;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r2 = r17 + 1;
        r17 = r2;
        r2 = r7;
        goto L_0x02a4;
    L_0x02d6:
        r7 = r27.size();	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r7 = (float) r7;	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r2 = r2 / r7;
        r26 = java.lang.Math.round(r2);	 Catch:{ Exception -> 0x036e, all -> 0x01ae }
        r2 = 30;
        r0 = r26;
        if (r0 >= r2) goto L_0x0342;
    L_0x02e6:
        r26 = 30;
        r2 = r16;
        r12 = r26;
        r32 = r8;
        r7 = r10;
        r9 = r32;
    L_0x02f1:
        if (r19 != 0) goto L_0x02fb;
    L_0x02f3:
        r11 = r27.size();	 Catch:{ Exception -> 0x0377, all -> 0x01ae }
        r13 = 1;
        if (r11 > r13) goto L_0x02fb;
    L_0x02fa:
        r2 = 0;
    L_0x02fb:
        r11 = r27.size();	 Catch:{ Exception -> 0x0377, all -> 0x01ae }
        r0 = r37;
        if (r11 >= r0) goto L_0x0397;
    L_0x0303:
        r13 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r11 = r27.size();	 Catch:{ Exception -> 0x0377, all -> 0x01ae }
        r15 = (double) r11;	 Catch:{ Exception -> 0x0377, all -> 0x01ae }
        r13 = r13 * r15;
        r11 = r35.size();	 Catch:{ Exception -> 0x0377, all -> 0x01ae }
        r15 = (double) r11;
        r13 = r13 / r15;
        r15 = 4602678819172646912; // 0x3fe0000000000000 float:0.0 double:0.5;
        r11 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1));
        if (r11 >= 0) goto L_0x0397;
    L_0x0317:
        r2 = 0;
        r11 = r2;
    L_0x0319:
        r2 = 1;
        r0 = r19;
        if (r0 != r2) goto L_0x038f;
    L_0x031e:
        r2 = 1;
        if (r11 != r2) goto L_0x038f;
    L_0x0321:
        r2 = r34;
        r2 = r2.a(r3, r5, r7, r9);	 Catch:{ Exception -> 0x0383, all -> 0x01ae }
        r4 = 4666723172467343360; // 0x40c3880000000000 float:0.0 double:10000.0;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x038f;
    L_0x0330:
        r11 = 0;
        r3 = r11;
        r4 = r12;
        r32 = r9;
        r10 = r7;
        r8 = r32;
    L_0x0338:
        if (r20 == 0) goto L_0x017b;
    L_0x033a:
        r20.close();	 Catch:{ Exception -> 0x033f }
        goto L_0x017b;
    L_0x033f:
        r2 = move-exception;
        goto L_0x017b;
    L_0x0342:
        r2 = 100;
        r0 = r26;
        if (r2 >= r0) goto L_0x0399;
    L_0x0348:
        r26 = 100;
        r2 = r16;
        r12 = r26;
        r32 = r8;
        r7 = r10;
        r9 = r32;
        goto L_0x02f1;
    L_0x0354:
        r2 = 0;
        goto L_0x0196;
    L_0x0357:
        r2 = move-exception;
        goto L_0x017b;
    L_0x035a:
        r3 = move-exception;
        goto L_0x01b4;
    L_0x035d:
        r3 = move-exception;
        r20 = r2;
        r2 = r3;
        goto L_0x01af;
    L_0x0363:
        r3 = move-exception;
        r3 = r21;
        r10 = r22;
        r8 = r24;
        r4 = r26;
        goto L_0x0176;
    L_0x036e:
        r2 = move-exception;
        r2 = r20;
        r3 = r16;
        r4 = r26;
        goto L_0x0176;
    L_0x0377:
        r3 = move-exception;
        r3 = r2;
        r4 = r12;
        r2 = r20;
        r32 = r9;
        r10 = r7;
        r8 = r32;
        goto L_0x0176;
    L_0x0383:
        r2 = move-exception;
        r2 = r20;
        r3 = r11;
        r4 = r12;
        r32 = r9;
        r10 = r7;
        r8 = r32;
        goto L_0x0176;
    L_0x038f:
        r3 = r11;
        r4 = r12;
        r32 = r9;
        r10 = r7;
        r8 = r32;
        goto L_0x0338;
    L_0x0397:
        r11 = r2;
        goto L_0x0319;
    L_0x0399:
        r2 = r16;
        r12 = r26;
        r32 = r8;
        r7 = r10;
        r9 = r32;
        goto L_0x02f1;
    L_0x03a4:
        r2 = r21;
        r7 = r22;
        r9 = r24;
        r12 = r26;
        goto L_0x02f1;
    L_0x03ae:
        r3 = r21;
        r10 = r22;
        r8 = r24;
        r4 = r26;
        goto L_0x0338;
    L_0x03b7:
        r19 = r2;
        goto L_0x001a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.b.a(java.util.LinkedHashMap, com.baidu.location.BDLocation, int):com.baidu.location.BDLocation");
    }

    private void a(BDLocation bDLocation, BDLocation bDLocation2, BDLocation bDLocation3, String str, Long l) {
        if (bDLocation != null && bDLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            String format;
            String format2;
            if (bDLocation2 != null && bDLocation.getNetworkLocationType() != null && bDLocation.getNetworkLocationType().equals("cl") && a(bDLocation2.getLatitude(), bDLocation2.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude()) > 300.0d) {
                format = String.format(Locale.US, "UPDATE CL SET cl = 0 WHERE id = %d;", new Object[]{l});
                format2 = String.format(Locale.US, "INSERT OR REPLACE INTO CL VALUES (%d,\"%s\",%d);", new Object[]{l, str, Integer.valueOf(100000)});
                try {
                    this.h.execSQL(format);
                    this.i.execSQL(format2);
                } catch (Exception e) {
                }
            }
            if (bDLocation3 != null && bDLocation.getNetworkLocationType() != null && bDLocation.getNetworkLocationType().equals("wf") && a(bDLocation3.getLatitude(), bDLocation3.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude()) > 100.0d) {
                try {
                    format = String.format("UPDATE AP SET cl = 0 WHERE id In (%s);", new Object[]{this.j.toString()});
                    format2 = String.format("INSERT OR REPLACE INTO AP VALUES %s;", new Object[]{this.k.toString()});
                    this.h.execSQL(format);
                    this.i.execSQL(format2);
                } catch (Exception e2) {
                }
            }
        }
    }

    private void a(String str, Long l, BDLocation bDLocation) {
        if (str != null) {
            if (bDLocation != null) {
                try {
                    this.h.execSQL(String.format(Locale.US, "UPDATE CL SET frequency=frequency+1 WHERE id = %d;", new Object[]{l}));
                } catch (Exception e) {
                }
            } else {
                String format = String.format(Locale.US, "INSERT OR IGNORE INTO CL VALUES (%d,\"%s\",0);", new Object[]{l, str});
                String format2 = String.format(Locale.US, "UPDATE CL SET frequency=frequency+1 WHERE id = %d;", new Object[]{l});
                try {
                    this.i.execSQL(format);
                    this.i.execSQL(format2);
                } catch (Exception e2) {
                }
            }
            if (this.p) {
                try {
                    this.i.execSQL(String.format(Locale.US, "INSERT OR IGNORE INTO CL VALUES (%d,\"%s\",%d);", new Object[]{l, str, Integer.valueOf(100000)}));
                } catch (Exception e3) {
                }
            }
        }
    }

    private void a(String str, String str2, String str3) {
        this.f.a(str, str2, str3);
    }

    private void a(LinkedHashMap<String, Integer> linkedHashMap) {
        if (linkedHashMap != null && linkedHashMap.size() > 0) {
            String str;
            this.j = new StringBuffer();
            this.k = new StringBuffer();
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            if (!(this.m == null || this.m.keySet() == null)) {
                int i = 1;
                int i2 = 1;
                for (Long l : this.m.keySet()) {
                    try {
                        int i3;
                        int i4;
                        if (this.l.contains(l)) {
                            if (i2 != 0) {
                                i2 = 0;
                            } else {
                                this.j.append(',');
                                this.k.append(',');
                            }
                            this.j.append(l);
                            this.k.append('(').append(l).append(',').append('\"').append((String) this.n.get(l)).append('\"').append(',').append(100000).append(')');
                            i3 = i;
                            i4 = i2;
                        } else {
                            str = (String) this.n.get(l);
                            if (i != 0) {
                                i = 0;
                            } else {
                                stringBuffer.append(',');
                                stringBuffer2.append(',');
                            }
                            stringBuffer.append(l);
                            stringBuffer2.append('(').append(l).append(',').append('\"').append(str).append('\"').append(",0)");
                            i3 = i;
                            i4 = i2;
                        }
                        i = i3;
                        i2 = i4;
                    } catch (Exception e) {
                        i = i;
                        i2 = i2;
                    }
                }
            }
            try {
                this.h.execSQL(String.format(Locale.US, "UPDATE AP SET frequency=frequency+1 WHERE id IN(%s)", new Object[]{this.j.toString()}));
            } catch (Exception e2) {
            }
            if (this.o != null && this.o.length() > 0) {
                if (stringBuffer2.length() > 0) {
                    stringBuffer2.append(",");
                }
                stringBuffer2.append(this.o);
            }
            try {
                String format = String.format("INSERT OR IGNORE INTO AP VALUES %s;", new Object[]{stringBuffer2.toString()});
                str = String.format("UPDATE AP SET frequency=frequency+1 WHERE id in (%s);", new Object[]{stringBuffer.toString()});
                if (stringBuffer2.length() > 0) {
                    this.i.execSQL(format);
                }
                if (stringBuffer.length() > 0) {
                    this.i.execSQL(str);
                }
            } catch (Exception e3) {
            }
        }
    }

    private void a(String[] strArr) {
        this.a.l().a(strArr);
    }

    Cursor a(a aVar) {
        BDLocation bDLocation;
        BDLocation bDLocation2 = new BDLocation();
        bDLocation2.setLocType(67);
        int i = 0;
        if (aVar.c) {
            double[] coorEncrypt;
            List list;
            String str = aVar.b;
            LinkedHashMap linkedHashMap = aVar.i;
            int i2 = aVar.f;
            BDLocation bDLocation3 = aVar.g;
            BDLocation bDLocation4 = null;
            Long valueOf = Long.valueOf(Long.MIN_VALUE);
            if (!(str == null || this.h == null)) {
                valueOf = Jni.encode3(str);
                if (valueOf != null) {
                    bDLocation4 = a(valueOf);
                }
            }
            BDLocation bDLocation5 = null;
            if (!(linkedHashMap == null || linkedHashMap.size() <= 0 || this.h == null)) {
                this.m.clear();
                this.l.clear();
                this.n.clear();
                bDLocation5 = a(linkedHashMap, bDLocation4, i2);
            }
            Double d = null;
            Double d2 = null;
            Double d3 = null;
            Double d4 = null;
            if (bDLocation4 != null) {
                d = Double.valueOf(bDLocation4.getLongitude());
                d2 = Double.valueOf(bDLocation4.getLatitude());
                coorEncrypt = Jni.coorEncrypt(bDLocation4.getLongitude(), bDLocation4.getLatitude(), BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
                bDLocation4.setCoorType("gcj");
                bDLocation4.setLatitude(coorEncrypt[1]);
                bDLocation4.setLongitude(coorEncrypt[0]);
                bDLocation4.setNetworkLocationType("cl");
            }
            if (bDLocation5 != null) {
                d3 = Double.valueOf(bDLocation5.getLongitude());
                d4 = Double.valueOf(bDLocation5.getLatitude());
                coorEncrypt = Jni.coorEncrypt(bDLocation5.getLongitude(), bDLocation5.getLatitude(), BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
                bDLocation5.setCoorType("gcj");
                bDLocation5.setLatitude(coorEncrypt[1]);
                bDLocation5.setLongitude(coorEncrypt[0]);
                bDLocation5.setNetworkLocationType("wf");
            }
            if (bDLocation4 != null && bDLocation5 == null) {
                i = 1;
            } else if (bDLocation4 == null && bDLocation5 != null) {
                i = 2;
            } else if (!(bDLocation4 == null || bDLocation5 == null)) {
                i = 4;
            }
            Object obj = aVar.f > 0 ? 1 : null;
            Object obj2 = (linkedHashMap == null || linkedHashMap.size() <= 0) ? 1 : null;
            if (obj != null) {
                if (bDLocation5 != null) {
                    d2 = d3;
                    bDLocation = bDLocation5;
                } else {
                    if (!(obj2 == null || bDLocation4 == null)) {
                        d4 = d2;
                        bDLocation = bDLocation4;
                        d2 = d;
                    }
                    d4 = null;
                    d2 = null;
                    bDLocation = bDLocation2;
                }
            } else if (bDLocation5 != null) {
                d2 = d3;
                bDLocation = bDLocation5;
            } else {
                if (bDLocation4 != null) {
                    d4 = d2;
                    bDLocation = bDLocation4;
                    d2 = d;
                }
                d4 = null;
                d2 = null;
                bDLocation = bDLocation2;
            }
            if (aVar.e && this.a.l().l() && d4 != null && d2 != null) {
                bDLocation.setAddr(this.a.k().a(d2.doubleValue(), d4.doubleValue()));
            }
            if (obj != null && aVar.e && bDLocation.getAddrStr() == null) {
                d4 = null;
                d2 = null;
                i = 0;
                bDLocation = bDLocation2;
            }
            if ((!aVar.d && !aVar.h) || d4 == null || d2 == null) {
                list = null;
            } else {
                List b = this.a.k().b(d2.doubleValue(), d4.doubleValue());
                if (aVar.d) {
                    bDLocation.setPoiList(b);
                }
                list = b;
            }
            if (obj == null || !aVar.d || (list != null && list.size() > 0)) {
                i2 = i;
            } else {
                i2 = 0;
                bDLocation = bDLocation2;
            }
            String str2 = null;
            if (aVar.h && list != null && list.size() > 0) {
                str2 = String.format(Locale.CHINA, "在%s附近", new Object[]{((Poi) list.get(0)).getName()});
                bDLocation.setLocationDescribe(str2);
            }
            if (obj != null && aVar.h && r2 == null) {
                i2 = 0;
                bDLocation = bDLocation2;
            }
            StringBuffer stringBuffer = new StringBuffer();
            String str3 = null;
            if (aVar.a != null) {
                stringBuffer.append(aVar.a);
                stringBuffer.append(e.a(bDLocation5, bDLocation4, aVar));
                stringBuffer.append(e.a(bDLocation, i2));
                str3 = stringBuffer.toString();
            }
            new b(this, str, valueOf, bDLocation4, bDLocation5, bDLocation3, str3, linkedHashMap) {
                final /* synthetic */ b a;
            }.start();
        } else {
            bDLocation = bDLocation2;
        }
        return e.a(bDLocation);
    }

    SQLiteDatabase a() {
        return this.i;
    }

    void b() {
        this.g.b();
    }
}
