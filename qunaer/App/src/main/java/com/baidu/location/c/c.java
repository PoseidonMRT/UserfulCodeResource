package com.baidu.location.c;

import android.content.SharedPreferences.Editor;
import com.baidu.location.Jni;
import com.baidu.location.h.d;
import com.baidu.location.h.f;
import com.baidu.location.h.h;
import com.baidu.location.h.i;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;

public class c {
    private static c i = null;
    private static final String k = (h.a + "/conlts.dat");
    private static int l = -1;
    private static int m = -1;
    private static int n = 0;
    public boolean a = true;
    public boolean b = true;
    public boolean c = false;
    public boolean d = true;
    public boolean e = true;
    public boolean f = true;
    public boolean g = true;
    public boolean h = false;
    private a j = null;

    class a extends f {
        String a;
        boolean b;
        boolean c;
        final /* synthetic */ c d;

        public a(c cVar) {
            this.d = cVar;
            this.a = null;
            this.b = false;
            this.c = false;
            this.k = new HashMap();
        }

        public void a() {
            this.h = i.c();
            this.i = 2;
            String encode = Jni.encode(this.a);
            this.a = null;
            if (this.b) {
                this.k.put("qt", "grid");
            } else {
                this.k.put("qt", "conf");
            }
            this.k.put("req", encode);
        }

        public void a(String str, boolean z) {
            if (!this.c) {
                this.c = true;
                this.a = str;
                this.b = z;
                if (z) {
                    b(true);
                } else {
                    e();
                }
            }
        }

        public void a(boolean z) {
            if (!z || this.j == null) {
                this.d.c(null);
            } else if (this.b) {
                this.d.a(this.m);
            } else {
                this.d.c(this.j);
            }
            if (this.k != null) {
                this.k.clear();
            }
            this.c = false;
        }
    }

    private c() {
    }

    public static c a() {
        if (i == null) {
            i = new c();
        }
        return i;
    }

    private void a(int i) {
        boolean z = true;
        this.a = (i & 1) == 1;
        this.b = (i & 2) == 2;
        this.c = (i & 4) == 4;
        this.d = (i & 8) == 8;
        this.f = (i & 65536) == 65536;
        if ((i & 131072) != 131072) {
            z = false;
        }
        this.g = z;
        if ((i & 16) == 16) {
            this.e = false;
        }
    }

    private void a(JSONObject jSONObject) {
        boolean z = false;
        if (jSONObject != null) {
            int i = 14400000;
            int i2 = 10;
            try {
                if (!(jSONObject.has("ipen") && jSONObject.getInt("ipen") == 0)) {
                    z = true;
                }
                if (jSONObject.has("ipvt")) {
                    i = jSONObject.getInt("ipvt");
                }
                if (jSONObject.has("ipvn")) {
                    i2 = jSONObject.getInt("ipvn");
                }
                Editor edit = com.baidu.location.f.getServiceContext().getSharedPreferences("MapCoreServicePre", 0).edit();
                edit.putBoolean("ipLocInfoUpload", z);
                edit.putInt("ipValidTime", i);
                edit.putInt("ipLocInfoUploadTimesPerDay", i2);
                edit.commit();
            } catch (Exception e) {
            }
        }
    }

    private void a(byte[] bArr) {
        int i = 0;
        if (bArr != null) {
            if (bArr.length < 640) {
                i.x = false;
                i.u = i.s + 0.025d;
                i.t = i.r - 0.025d;
                i = 1;
            } else {
                i.x = true;
                i.t = Double.longBitsToDouble(((((((((((long) bArr[7]) & 255) << 56) | ((((long) bArr[6]) & 255) << 48)) | ((((long) bArr[5]) & 255) << 40)) | ((((long) bArr[4]) & 255) << 32)) | ((((long) bArr[3]) & 255) << 24)) | ((((long) bArr[2]) & 255) << 16)) | ((((long) bArr[1]) & 255) << 8)) | (((long) bArr[0]) & 255));
                i.u = Double.longBitsToDouble(((((((((((long) bArr[15]) & 255) << 56) | ((((long) bArr[14]) & 255) << 48)) | ((((long) bArr[13]) & 255) << 40)) | ((((long) bArr[12]) & 255) << 32)) | ((((long) bArr[11]) & 255) << 24)) | ((((long) bArr[10]) & 255) << 16)) | ((((long) bArr[9]) & 255) << 8)) | (((long) bArr[8]) & 255));
                i.w = new byte[625];
                while (i < 625) {
                    i.w[i] = bArr[i + 16];
                    i++;
                }
                i = 1;
            }
        }
        if (i != 0) {
            try {
                g();
            } catch (Exception e) {
            }
        }
    }

    private void b(int i) {
        File file = new File(k);
        if (!file.exists()) {
            i();
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(4);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            randomAccessFile.seek((long) ((readInt * n) + 128));
            byte[] bytes = (com.baidu.location.h.c.d + '\u0000').getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            randomAccessFile.writeInt(i);
            if (readInt2 == n) {
                randomAccessFile.seek(8);
                randomAccessFile.writeInt(readInt2 + 1);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    private boolean b(String str) {
        boolean z = true;
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("ipconf")) {
                    try {
                        a(jSONObject.getJSONObject("ipconf"));
                    } catch (Exception e) {
                    }
                }
                int parseInt = Integer.parseInt(jSONObject.getString("ver"));
                if (parseInt > i.y) {
                    String[] split;
                    i.y = parseInt;
                    if (jSONObject.has("gps")) {
                        split = jSONObject.getString("gps").split("\\|");
                        if (split.length > 10) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.z = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.A = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.B = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.C = Float.parseFloat(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                i.D = Integer.parseInt(split[4]);
                            }
                            if (!(split[5] == null || split[5].equals(""))) {
                                i.E = Integer.parseInt(split[5]);
                            }
                            if (!(split[6] == null || split[6].equals(""))) {
                                i.F = Integer.parseInt(split[6]);
                            }
                            if (!(split[7] == null || split[7].equals(""))) {
                                i.G = Integer.parseInt(split[7]);
                            }
                            if (!(split[8] == null || split[8].equals(""))) {
                                i.H = Integer.parseInt(split[8]);
                            }
                            if (!(split[9] == null || split[9].equals(""))) {
                                i.I = Integer.parseInt(split[9]);
                            }
                            if (!(split[10] == null || split[10].equals(""))) {
                                i.J = Integer.parseInt(split[10]);
                            }
                        }
                    }
                    if (jSONObject.has("up")) {
                        split = jSONObject.getString("up").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.K = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.L = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.M = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.N = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("wf")) {
                        split = jSONObject.getString("wf").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.O = Integer.parseInt(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.P = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.Q = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.R = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("ab")) {
                        split = jSONObject.getString("ab").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.S = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.T = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.U = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.V = Integer.parseInt(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("zxd")) {
                        split = jSONObject.getString("zxd").split("\\|");
                        if (split.length > 4) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.aq = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.ar = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.as = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.at = Integer.parseInt(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                i.au = Integer.parseInt(split[4]);
                            }
                        }
                    }
                    if (jSONObject.has("gpc")) {
                        split = jSONObject.getString("gpc").split("\\|");
                        if (split.length > 5) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                if (Integer.parseInt(split[0]) > 0) {
                                    i.aa = true;
                                } else {
                                    i.aa = false;
                                }
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                if (Integer.parseInt(split[1]) > 0) {
                                    i.ab = true;
                                } else {
                                    i.ab = false;
                                }
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.ac = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                i.ae = Integer.parseInt(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                int parseInt2 = Integer.parseInt(split[4]);
                                if (parseInt2 > 0) {
                                    i.aj = (long) parseInt2;
                                    i.af = (i.aj * 1000) * 60;
                                    i.ak = i.af >> 2;
                                } else {
                                    i.p = false;
                                }
                            }
                            if (!(split[5] == null || split[5].equals(""))) {
                                i.am = Integer.parseInt(split[5]);
                            }
                        }
                    }
                    if (jSONObject.has("shak")) {
                        split = jSONObject.getString("shak").split("\\|");
                        if (split.length > 2) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                i.an = Integer.parseInt(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                i.ao = Integer.parseInt(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                i.ap = Float.parseFloat(split[2]);
                            }
                        }
                    }
                    if (jSONObject.has("dmx")) {
                        i.al = jSONObject.getInt("dmx");
                    }
                    return z;
                }
            } catch (Exception e2) {
                return false;
            }
        }
        z = false;
        return z;
    }

    private void c(String str) {
        m = -1;
        if (str != null) {
            try {
                if (b(str)) {
                    f();
                }
            } catch (Exception e) {
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("ctr")) {
                    m = Integer.parseInt(jSONObject.getString("ctr"));
                }
            } catch (Exception e2) {
            }
            try {
                int i;
                j();
                if (m != -1) {
                    i = m;
                    b(m);
                } else {
                    i = l != -1 ? l : -1;
                }
                if (i != -1) {
                    a(i);
                }
                if (!com.baidu.location.f.isServing) {
                }
            } catch (Exception e3) {
            }
        }
    }

    private void e() {
        String str = "&ver=" + i.y + "&usr=" + com.baidu.location.h.c.a().b() + "&app=" + com.baidu.location.h.c.d + "&prod=" + com.baidu.location.h.c.e;
        if (this.j == null) {
            this.j = new a(this);
        }
        this.j.a(str, false);
    }

    private void f() {
        String str = h.a + "/config.dat";
        int i = i.aa ? 1 : 0;
        int i2 = i.ab ? 1 : 0;
        byte[] bytes = String.format(Locale.CHINA, "{\"ver\":\"%d\",\"gps\":\"%.1f|%.1f|%.1f|%.1f|%d|%d|%d|%d|%d|%d|%d\",\"up\":\"%.1f|%.1f|%.1f|%.1f\",\"wf\":\"%d|%.1f|%d|%.1f\",\"ab\":\"%.2f|%.2f|%d|%d\",\"gpc\":\"%d|%d|%d|%d|%d|%d\",\"zxd\":\"%.1f|%.1f|%d|%d|%d\",\"shak\":\"%d|%d|%.1f\",\"dmx\":%d}", new Object[]{Integer.valueOf(i.y), Float.valueOf(i.z), Float.valueOf(i.A), Float.valueOf(i.B), Float.valueOf(i.C), Integer.valueOf(i.D), Integer.valueOf(i.E), Integer.valueOf(i.F), Integer.valueOf(i.G), Integer.valueOf(i.H), Integer.valueOf(i.I), Integer.valueOf(i.J), Float.valueOf(i.K), Float.valueOf(i.L), Float.valueOf(i.M), Float.valueOf(i.N), Integer.valueOf(i.O), Float.valueOf(i.P), Integer.valueOf(i.Q), Float.valueOf(i.R), Float.valueOf(i.S), Float.valueOf(i.T), Integer.valueOf(i.U), Integer.valueOf(i.V), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i.ac), Integer.valueOf(i.ae), Long.valueOf(i.aj), Integer.valueOf(i.am), Float.valueOf(i.aq), Float.valueOf(i.ar), Integer.valueOf(i.as), Integer.valueOf(i.at), Integer.valueOf(i.au), Integer.valueOf(i.an), Integer.valueOf(i.ao), Float.valueOf(i.ap), Integer.valueOf(i.al)}).getBytes();
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(str);
            if (!file.exists()) {
                File file2 = new File(h.a);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(2);
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes);
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    private void g() {
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(h.a + "/config.dat");
            if (!file.exists()) {
                File file2 = new File(h.a);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(1);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(1024);
            randomAccessFile.writeDouble(i.t);
            randomAccessFile.writeDouble(i.u);
            randomAccessFile.writeBoolean(i.x);
            if (i.x && i.w != null) {
                randomAccessFile.write(i.w);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    private void h() {
        try {
            File file = new File(h.a + "/config.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(2);
                    int readInt = randomAccessFile.readInt();
                    byte[] bArr = new byte[readInt];
                    randomAccessFile.read(bArr, 0, readInt);
                    b(new String(bArr));
                }
                randomAccessFile.seek(1);
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(1024);
                    i.t = randomAccessFile.readDouble();
                    i.u = randomAccessFile.readDouble();
                    i.x = randomAccessFile.readBoolean();
                    if (i.x) {
                        i.w = new byte[625];
                        randomAccessFile.read(i.w, 0, 625);
                    }
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
        c(null);
    }

    private void i() {
        try {
            File file = new File(k);
            if (!file.exists()) {
                File file2 = new File(h.a);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (!file.createNewFile()) {
                    file = null;
                }
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(0);
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(128);
                randomAccessFile.writeInt(0);
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    private void j() {
        int i = 0;
        try {
            File file = new File(k);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(4);
                int readInt = randomAccessFile.readInt();
                if (readInt > HttpConnectionPool.CONNECTION_TIMEOUT) {
                    randomAccessFile.close();
                    n = 0;
                    i();
                    return;
                }
                int readInt2 = randomAccessFile.readInt();
                randomAccessFile.seek(128);
                byte[] bArr = new byte[readInt];
                while (i < readInt2) {
                    randomAccessFile.seek((long) ((readInt * i) + 128));
                    int readInt3 = randomAccessFile.readInt();
                    if (readInt3 > 0 && readInt3 < readInt) {
                        randomAccessFile.read(bArr, 0, readInt3);
                        if (bArr[readInt3 - 1] == (byte) 0) {
                            String str = new String(bArr, 0, readInt3 - 1);
                            com.baidu.location.h.c.a();
                            if (str.equals(com.baidu.location.h.c.d)) {
                                l = randomAccessFile.readInt();
                                n = i;
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (i == readInt2) {
                    n = readInt2;
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public void a(String str) {
        if (this.j == null) {
            this.j = new a(this);
        }
        this.j.a(str, true);
    }

    public void b() {
        h();
    }

    public void c() {
    }

    public void d() {
        if (System.currentTimeMillis() - d.a().d() > DateTimeUtils.ONE_DAY) {
            d.a().c(System.currentTimeMillis());
            e();
        }
    }
}
