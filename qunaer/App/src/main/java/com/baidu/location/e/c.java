package com.baidu.location.e;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.os.EnvironmentCompat;
import com.baidu.location.Jni;
import com.baidu.location.h.f;
import com.baidu.location.h.i;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import org.json.JSONObject;

final class c {
    private final d a;
    private final SQLiteDatabase b;
    private final a c;
    private boolean d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean h;
    private String[] i;
    private boolean j;
    private int k;
    private int l;
    private int m;
    private double n;
    private double o;
    private double p;
    private double q;
    private double r;
    private int s;
    private boolean t = true;
    private long u = 8000;
    private long v = 5000;
    private long w = 5000;
    private long x = 5000;
    private long y = 5000;

    final class a extends f {
        final /* synthetic */ c a;
        private int b;
        private long c;
        private long d;
        private boolean e;
        private final String f;

        private a(c cVar) {
            this.a = cVar;
            this.b = 0;
            this.e = false;
            this.c = -1;
            this.d = -1;
            this.k = new HashMap();
            this.f = Jni.encodeOfflineLocationUpdateRequest(String.format(Locale.US, "&ver=%s&cuid=%s&prod=%s:%s&sdk=%.2f", new Object[]{"1", com.baidu.location.h.c.a().b, com.baidu.location.h.c.e, com.baidu.location.h.c.d, Float.valueOf(6.33f)}));
        }

        private void b() {
            if (!this.e) {
                boolean z = false;
                try {
                    File file = new File(this.a.a.c(), "ofl.config");
                    if (this.d == -1 && file.exists()) {
                        JSONObject jSONObject;
                        Scanner scanner = new Scanner(file);
                        String next = scanner.next();
                        scanner.close();
                        JSONObject jSONObject2 = new JSONObject(next);
                        this.a.d = jSONObject2.getBoolean("ol");
                        this.a.e = jSONObject2.getBoolean("fl");
                        this.a.f = jSONObject2.getBoolean("on");
                        this.a.g = jSONObject2.getBoolean("wn");
                        this.a.h = jSONObject2.getBoolean("oc");
                        this.d = jSONObject2.getLong("t");
                        if (jSONObject2.has("cplist")) {
                            this.a.i = jSONObject2.getString("cplist").split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        }
                        if (jSONObject2.has("rgcgp")) {
                            this.a.k = jSONObject2.getInt("rgcgp");
                        }
                        if (jSONObject2.has("rgcon")) {
                            this.a.j = jSONObject2.getBoolean("rgcon");
                        }
                        if (jSONObject2.has("addrup")) {
                            this.a.m = jSONObject2.getInt("addrup");
                        }
                        if (jSONObject2.has("poiup")) {
                            this.a.l = jSONObject2.getInt("poiup");
                        }
                        if (jSONObject2.has("oflp")) {
                            jSONObject = jSONObject2.getJSONObject("oflp");
                            if (jSONObject.has("0")) {
                                this.a.n = jSONObject.getDouble("0");
                            }
                            if (jSONObject.has("1")) {
                                this.a.o = jSONObject.getDouble("1");
                            }
                            if (jSONObject.has("2")) {
                                this.a.p = jSONObject.getDouble("2");
                            }
                            if (jSONObject.has("3")) {
                                this.a.q = jSONObject.getDouble("3");
                            }
                            if (jSONObject.has("4")) {
                                this.a.r = jSONObject.getDouble("4");
                            }
                        }
                        if (jSONObject2.has("onlt")) {
                            jSONObject = jSONObject2.getJSONObject("onlt");
                            if (jSONObject.has("0")) {
                                this.a.y = jSONObject.getLong("0");
                            }
                            if (jSONObject.has("1")) {
                                this.a.x = jSONObject.getLong("1");
                            }
                            if (jSONObject.has("2")) {
                                this.a.u = jSONObject.getLong("2");
                            }
                            if (jSONObject.has("3")) {
                                this.a.v = jSONObject.getLong("3");
                            }
                            if (jSONObject.has("4")) {
                                this.a.w = jSONObject.getLong("4");
                            }
                        }
                        if (jSONObject2.has("minapn")) {
                            this.a.s = jSONObject2.getInt("minapn");
                        }
                    }
                    if (this.d == -1 && file.exists()) {
                    }
                    if (this.d != -1 && this.d + DateTimeUtils.ONE_DAY <= System.currentTimeMillis()) {
                        z = true;
                    }
                } catch (Exception e) {
                }
                if ((this.d == -1 || r0) && c() && i.a(this.a.a.b())) {
                    this.e = true;
                    e();
                }
            }
        }

        private boolean c() {
            boolean z = true;
            if (this.b >= 2) {
                if (this.c + DateTimeUtils.ONE_DAY < System.currentTimeMillis()) {
                    this.b = 0;
                    this.c = -1;
                } else {
                    z = false;
                }
            }
            return !z ? z : z;
        }

        public void a() {
            this.k.clear();
            this.k.put("qt", "conf");
            this.k.put("req", this.f);
            this.h = d.a;
        }

        public void a(boolean z) {
            if (!z || this.j == null) {
                this.b++;
                this.c = System.currentTimeMillis();
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(this.j);
                    Object obj = "1";
                    long j = 0;
                    if (jSONObject.has("ofl")) {
                        j = jSONObject.getLong("ofl");
                    }
                    if (jSONObject.has("ver")) {
                        obj = jSONObject.getString("ver");
                    }
                    if ((j & 1) == 1) {
                        this.a.d = true;
                    }
                    if ((j & 2) == 2) {
                        this.a.e = true;
                    }
                    if ((j & 4) == 4) {
                        this.a.f = true;
                    }
                    if ((j & 8) == 8) {
                        this.a.g = true;
                    }
                    if ((16 & j) == 16) {
                        this.a.h = true;
                    }
                    if ((j & 32) == 32) {
                        this.a.j = true;
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    if (jSONObject.has("cplist")) {
                        this.a.i = jSONObject.getString("cplist").split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        jSONObject2.put("cplist", jSONObject.getString("cplist"));
                    }
                    if (jSONObject.has("bklist")) {
                        this.a.a(jSONObject.getString("bklist").split(VoiceWakeuperAidl.PARAMS_SEPARATE));
                    }
                    if (jSONObject.has("para")) {
                        JSONObject jSONObject3 = jSONObject.getJSONObject("para");
                        if (jSONObject3.has("rgcgp")) {
                            this.a.k = jSONObject3.getInt("rgcgp");
                        }
                        if (jSONObject3.has("addrup")) {
                            this.a.m = jSONObject3.getInt("addrup");
                        }
                        if (jSONObject3.has("poiup")) {
                            this.a.l = jSONObject3.getInt("poiup");
                        }
                        if (jSONObject3.has("oflp")) {
                            jSONObject = jSONObject3.getJSONObject("oflp");
                            if (jSONObject.has("0")) {
                                this.a.n = jSONObject.getDouble("0");
                            }
                            if (jSONObject.has("1")) {
                                this.a.o = jSONObject.getDouble("1");
                            }
                            if (jSONObject.has("2")) {
                                this.a.p = jSONObject.getDouble("2");
                            }
                            if (jSONObject.has("3")) {
                                this.a.q = jSONObject.getDouble("3");
                            }
                            if (jSONObject.has("4")) {
                                this.a.r = jSONObject.getDouble("4");
                            }
                        }
                        if (jSONObject3.has("onlt")) {
                            jSONObject = jSONObject3.getJSONObject("onlt");
                            if (jSONObject.has("0")) {
                                this.a.y = jSONObject.getLong("0");
                            }
                            if (jSONObject.has("1")) {
                                this.a.x = jSONObject.getLong("1");
                            }
                            if (jSONObject.has("2")) {
                                this.a.u = jSONObject.getLong("2");
                            }
                            if (jSONObject.has("3")) {
                                this.a.v = jSONObject.getLong("3");
                            }
                            if (jSONObject.has("4")) {
                                this.a.w = jSONObject.getLong("4");
                            }
                        }
                        if (jSONObject3.has("minapn")) {
                            this.a.s = jSONObject3.getInt("minapn");
                        }
                    }
                    jSONObject2.put("ol", this.a.d);
                    jSONObject2.put("fl", this.a.e);
                    jSONObject2.put("on", this.a.f);
                    jSONObject2.put("wn", this.a.g);
                    jSONObject2.put("oc", this.a.h);
                    this.d = System.currentTimeMillis();
                    jSONObject2.put("t", this.d);
                    jSONObject2.put("ver", obj);
                    jSONObject2.put("rgcon", this.a.j);
                    jSONObject2.put("rgcgp", this.a.k);
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("0", this.a.n);
                    jSONObject4.put("1", this.a.o);
                    jSONObject4.put("2", this.a.p);
                    jSONObject4.put("3", this.a.q);
                    jSONObject4.put("4", this.a.r);
                    jSONObject2.put("oflp", jSONObject4);
                    jSONObject4 = new JSONObject();
                    jSONObject4.put("0", this.a.y);
                    jSONObject4.put("1", this.a.x);
                    jSONObject4.put("2", this.a.u);
                    jSONObject4.put("3", this.a.v);
                    jSONObject4.put("4", this.a.w);
                    jSONObject2.put("onlt", jSONObject4);
                    jSONObject2.put("addrup", this.a.m);
                    jSONObject2.put("poiup", this.a.l);
                    jSONObject2.put("minapn", this.a.s);
                    File file = new File(this.a.a.c(), "ofl.config");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jSONObject2.toString());
                    fileWriter.close();
                } catch (Exception e) {
                    this.b++;
                    this.c = System.currentTimeMillis();
                }
            }
            this.e = false;
        }
    }

    c(d dVar, SQLiteDatabase sQLiteDatabase) {
        this.a = dVar;
        this.d = false;
        this.e = false;
        this.f = false;
        this.g = false;
        this.h = false;
        this.j = false;
        this.k = 6;
        this.l = 30;
        this.m = 30;
        this.n = 0.0d;
        this.o = 0.0d;
        this.p = 0.0d;
        this.q = 0.0d;
        this.r = 0.0d;
        this.s = 8;
        this.i = new String[0];
        this.b = sQLiteDatabase;
        this.c = new a();
        if (this.b != null && this.b.isOpen()) {
            try {
                this.b.execSQL("CREATE TABLE IF NOT EXISTS BLACK (name VARCHAR(100) PRIMARY KEY);");
            } catch (Exception e) {
            }
        }
        g();
    }

    int a() {
        return this.s;
    }

    long a(String str) {
        return str.equals("2G") ? this.u : str.equals("3G") ? this.v : str.equals("4G") ? this.w : str.equals("WIFI") ? this.x : str.equals(EnvironmentCompat.MEDIA_UNKNOWN) ? this.y : 5000;
    }

    void a(String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append("(\"");
            stringBuffer.append(strArr[i]);
            stringBuffer.append("\")");
        }
        if (this.b != null && this.b.isOpen() && stringBuffer.length() > 0) {
            try {
                this.b.execSQL(String.format(Locale.US, "INSERT OR IGNORE INTO BLACK VALUES %s;", new Object[]{stringBuffer.toString()}));
            } catch (Exception e) {
            }
        }
    }

    double b() {
        return this.n;
    }

    double c() {
        return this.o;
    }

    double d() {
        return this.p;
    }

    double e() {
        return this.q;
    }

    double f() {
        return this.r;
    }

    void g() {
        this.c.b();
    }

    boolean h() {
        return this.d;
    }

    boolean i() {
        return this.f;
    }

    boolean j() {
        return this.g;
    }

    boolean k() {
        return this.e;
    }

    boolean l() {
        return this.j;
    }

    boolean m() {
        return this.t;
    }

    int n() {
        return this.k;
    }

    String[] o() {
        return this.i;
    }

    int p() {
        return this.m;
    }

    int q() {
        return this.l;
    }
}
