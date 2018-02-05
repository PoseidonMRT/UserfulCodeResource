package com.baidu.location.c;

import com.baidu.location.Jni;
import com.baidu.location.h.c;
import com.baidu.location.h.h;
import com.baidu.location.h.i;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class f {
    public static final String a = (h.a + "/llin.dat");
    private static volatile f b = null;
    private static String c = "LogSDK";
    private static int d = 5;
    private static int e = 1024;
    private static final String f = (h.a + "/llg.dat");
    private static final String g = (h.a + "/ller.dat");
    private SimpleDateFormat h = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
    private g i = null;
    private g j = null;
    private a k = null;
    private long l = 0;

    class a extends com.baidu.location.h.f {
        final /* synthetic */ f a;
        private String b;
        private boolean c;

        a(f fVar) {
            this.a = fVar;
            this.b = null;
            this.c = false;
            this.k = new HashMap();
        }

        public void a() {
            this.k.clear();
            this.k.put("qt", "stat");
            this.k.put("req", this.b);
            this.h = "http://loc.map.baidu.com/statloc";
        }

        public void a(String str) {
            this.b = str;
            if (this.b != null) {
                e();
                this.c = true;
            }
        }

        public void a(boolean z) {
            this.c = false;
            if (!z || this.j == null) {
                this.a.l = System.currentTimeMillis();
                return;
            }
            try {
                String str = this.j;
            } catch (Exception e) {
            }
        }

        public boolean b() {
            return this.c;
        }
    }

    private f() {
        if (this.i == null) {
            this.i = new g();
        }
    }

    public static f a() {
        if (b == null) {
            synchronized (f.class) {
                if (b == null) {
                    b = new f();
                }
            }
        }
        return b;
    }

    public static synchronized void a(String str, String str2) {
        synchronized (f.class) {
            File file = new File(str);
            if (!file.exists()) {
                b(str);
            }
            try {
                int i;
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(4);
                int readInt = randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                int readInt3 = randomAccessFile.readInt();
                int readInt4 = randomAccessFile.readInt();
                int readInt5 = randomAccessFile.readInt();
                if (readInt3 < readInt) {
                    randomAccessFile.seek((long) ((readInt2 * readInt3) + 128));
                    byte[] bytes = (str2 + '\u0000').getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes, 0, bytes.length);
                    i = readInt3 + 1;
                } else {
                    randomAccessFile.seek((long) ((readInt4 * readInt2) + 128));
                    byte[] bytes2 = (str2 + '\u0000').getBytes();
                    randomAccessFile.writeInt(bytes2.length);
                    randomAccessFile.write(bytes2, 0, bytes2.length);
                    readInt4++;
                    if (readInt4 > readInt3) {
                        readInt4 = 0;
                        i = readInt3;
                    } else {
                        i = readInt3;
                    }
                }
                randomAccessFile.seek(12);
                randomAccessFile.writeInt(i);
                randomAccessFile.writeInt(readInt4);
                randomAccessFile.writeInt(readInt5);
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean a(String str, List<String> list) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(8);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            byte[] bArr = new byte[e];
            int i = readInt2;
            readInt2 = d + 1;
            boolean z = false;
            while (readInt2 > 0 && i > 0) {
                if (i < readInt3) {
                    readInt3 = 0;
                }
                try {
                    randomAccessFile.seek((long) (((i - 1) * readInt) + 128));
                    int readInt4 = randomAccessFile.readInt();
                    if (readInt4 > 0 && readInt4 < readInt) {
                        randomAccessFile.read(bArr, 0, readInt4);
                        if (bArr[readInt4 - 1] == (byte) 0) {
                            list.add(0, new String(bArr, 0, readInt4 - 1));
                            z = true;
                        }
                    }
                    readInt2--;
                    i--;
                } catch (Exception e) {
                    return z;
                }
            }
            randomAccessFile.seek(12);
            randomAccessFile.writeInt(i);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.close();
            return z;
        } catch (Exception e2) {
            return false;
        }
    }

    private static void b(String str) {
        try {
            File file = new File(str);
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
                randomAccessFile.writeInt(32);
                randomAccessFile.writeInt(1000);
                randomAccessFile.writeInt(1040);
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(0);
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public void a(g gVar) {
        if (gVar != null) {
            a(f, Jni.encode(gVar.b()));
        }
    }

    public void a(String str) {
        if (str != null) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                String format = this.h.format(new Date());
                stringBuffer.append("&time=");
                stringBuffer.append(format);
                stringBuffer.append("&err=");
                stringBuffer.append(str);
                stringBuffer.append(c.a().a(false));
                stringBuffer.append(com.baidu.location.a.a.a().c());
                a(g, Jni.encode(stringBuffer.toString()));
                if ((str.contains("Criteria") || str.contains("locType")) && !i.e(com.baidu.location.f.getServiceContext()).equals("&netc=-1")) {
                    d();
                }
            } catch (Exception e) {
            }
        }
    }

    public g b() {
        return this.i;
    }

    public void c() {
        if (this.i != null) {
            a(f, Jni.encode(this.i.b()));
            this.i.a();
        }
    }

    public void d() {
        if (this.k == null) {
            this.k = new a(this);
        }
        if (System.currentTimeMillis() - this.l >= DateTimeUtils.ONE_HOUR && !this.k.b()) {
            try {
                Object obj;
                Object obj2;
                List arrayList = new ArrayList();
                a(g, arrayList);
                if (arrayList.size() > 0) {
                    obj = null;
                    obj2 = 1;
                } else {
                    a(f, arrayList);
                    if (arrayList.size() == 0) {
                        a(a, arrayList);
                        int i = 1;
                        obj2 = null;
                    } else {
                        obj = null;
                        obj2 = null;
                    }
                }
                JSONArray jSONArray = new JSONArray();
                JSONObject jSONObject = new JSONObject();
                if (arrayList.size() > 0) {
                    int size = arrayList.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        jSONArray.put((String) arrayList.get(i2));
                    }
                    if (obj2 != null) {
                        jSONObject.put("locpt", jSONArray);
                    } else if (obj != null) {
                        jSONObject.put("locup", jSONArray);
                    } else {
                        jSONObject.put("loctc", jSONArray);
                    }
                    this.k.a(jSONObject.toString());
                }
            } catch (Exception e) {
            }
        }
    }
}
