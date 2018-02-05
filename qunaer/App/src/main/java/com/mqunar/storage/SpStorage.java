package com.mqunar.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SpStorage implements IStorage {
    private static HandlerThread e;
    private static Handler f;
    private SharedPreferences a;
    private String b;
    private String c;
    private Map<String, Object> d = new HashMap();

    private SpStorage(Context context, String str, String str2) {
        this.a = context.getSharedPreferences(str, 0);
        this.b = str2;
        this.c = str;
    }

    public static IStorage newInstance(Context context, String str, String str2) {
        return new SpStorage(context, str, str2);
    }

    private boolean a(int i, String str, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Editor edit = this.a.edit();
            edit.putString(a() + str, "");
            edit.apply();
            return true;
        }
        try {
            Object obj = new byte[(bArr.length + 1)];
            obj[0] = (byte) i;
            System.arraycopy(bArr, 0, obj, 1, bArr.length);
            byte[] ea = EggRoll.ea(obj);
            Editor edit2 = this.a.edit();
            edit2.putString(a() + str, Base64.encodeToString(ea, 2));
            edit2.apply();
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private boolean b(int i, String str, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Editor edit = this.a.edit();
            edit.putString(a() + str, "");
            a(edit, str);
            return true;
        }
        try {
            Object obj = new byte[(bArr.length + 1)];
            obj[0] = (byte) i;
            System.arraycopy(bArr, 0, obj, 1, bArr.length);
            byte[] ea = EggRoll.ea(obj);
            Editor edit2 = this.a.edit();
            edit2.putString(a() + str, Base64.encodeToString(ea, 2));
            a(edit2, str);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private byte[] a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return EggRoll.da(Base64.decode(str, 2));
    }

    private byte[] b(String str) {
        return a(this.a.getString(a() + str, null));
    }

    private byte[] a(int i, String str) {
        Object b = b(str);
        if (b == null || b.length <= 0) {
            return b;
        }
        if (b[0] != ((byte) i)) {
            throw new RuntimeException("类型不匹配");
        }
        Object obj = new byte[(b.length - 1)];
        System.arraycopy(b, 1, obj, 0, obj.length);
        return obj;
    }

    public boolean putBytes(String str, byte[] bArr) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(0, str, bArr);
        }
        return a;
    }

    public boolean putSmoothBytes(String str, byte[] bArr) {
        boolean b;
        synchronized (this) {
            a(str, (Object) bArr);
            b = b(0, str, bArr);
        }
        return b;
    }

    public byte[] getBytes(String str, byte[] bArr) {
        synchronized (this) {
            try {
                Object c = c(str);
                byte[] bArr2;
                if (c != null) {
                    bArr2 = (byte[]) c;
                    return bArr2;
                }
                bArr2 = a(0, str);
                if (!(bArr2 == null || bArr2.length == 0)) {
                    bArr = bArr2;
                }
                return bArr;
            } catch (Throwable th) {
                return bArr;
            }
        }
    }

    public boolean putInt(String str, int i) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(2, str, i2b(i));
        }
        return a;
    }

    public boolean putSmoothInt(String str, int i) {
        boolean b;
        synchronized (this) {
            a(str, Integer.valueOf(i));
            b = b(2, str, i2b(i));
        }
        return b;
    }

    public boolean putShort(String str, short s) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(1, str, new byte[]{(byte) ((s >> 8) & 255), (byte) (s & 255)});
        }
        return a;
    }

    public boolean putSmoothShort(String str, short s) {
        boolean b;
        synchronized (this) {
            a(str, Short.valueOf(s));
            b = b(1, str, new byte[]{(byte) ((s >> 8) & 255), (byte) (s & 255)});
        }
        return b;
    }

    public boolean putLong(String str, long j) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(3, str, a(j));
        }
        return a;
    }

    public boolean putSmoothLong(String str, long j) {
        boolean b;
        synchronized (this) {
            a(str, Long.valueOf(j));
            b = b(3, str, a(j));
        }
        return b;
    }

    public boolean putFloat(String str, float f) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(4, str, i2b(Float.floatToIntBits(f)));
        }
        return a;
    }

    public boolean putSmoothFloat(String str, float f) {
        boolean b;
        synchronized (this) {
            a(str, Float.valueOf(f));
            b = b(4, str, i2b(Float.floatToIntBits(f)));
        }
        return b;
    }

    public boolean putDouble(String str, double d) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            a = a(5, str, a(Double.doubleToLongBits(d)));
        }
        return a;
    }

    public boolean putSmoothDouble(String str, double d) {
        boolean b;
        synchronized (this) {
            a(str, Double.valueOf(d));
            b = b(5, str, a(Double.doubleToLongBits(d)));
        }
        return b;
    }

    public boolean putString(String str, String str2) {
        boolean a;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            try {
                a = a(7, str, str2 == null ? null : str2.getBytes("UTF-8"));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return a;
    }

    public boolean putSmoothString(String str, String str2) {
        boolean b;
        synchronized (this) {
            a(str, (Object) str2);
            try {
                b = b(7, str, str2 == null ? null : str2.getBytes("UTF-8"));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return b;
    }

    public boolean putBoolean(String str, boolean z) {
        boolean a;
        int i = 1;
        synchronized (this) {
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            if (!z) {
                i = 0;
            }
            a = a(6, str, new byte[]{(byte) i});
        }
        return a;
    }

    public boolean putSmoothBoolean(String str, boolean z) {
        boolean b;
        int i = 1;
        synchronized (this) {
            a(str, Boolean.valueOf(z));
            if (!z) {
                i = 0;
            }
            b = b(6, str, new byte[]{(byte) i});
        }
        return b;
    }

    public boolean putSerializable(String str, Serializable serializable) {
        boolean z;
        Throwable th;
        Closeable closeable = null;
        synchronized (this) {
            byte[] toByteArray;
            if (this.d != null && this.d.containsKey(str)) {
                this.d.remove(str);
            }
            if (serializable != null) {
                Closeable byteArrayOutputStream;
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        Closeable objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        try {
                            objectOutputStream.writeObject(serializable);
                            objectOutputStream.flush();
                            toByteArray = byteArrayOutputStream.toByteArray();
                            a(objectOutputStream);
                            a(byteArrayOutputStream);
                        } catch (IOException e) {
                            closeable = objectOutputStream;
                            z = false;
                            a(closeable);
                            a(byteArrayOutputStream);
                            return z;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            closeable = objectOutputStream;
                            th = th3;
                            a(closeable);
                            a(byteArrayOutputStream);
                            throw th;
                        }
                    } catch (IOException e2) {
                        z = false;
                        a(closeable);
                        a(byteArrayOutputStream);
                        return z;
                    } catch (Throwable th4) {
                        th = th4;
                        a(closeable);
                        a(byteArrayOutputStream);
                        throw th;
                    }
                } catch (IOException e3) {
                    byteArrayOutputStream = null;
                    z = false;
                    a(closeable);
                    a(byteArrayOutputStream);
                    return z;
                } catch (Throwable th5) {
                    th = th5;
                    byteArrayOutputStream = null;
                    a(closeable);
                    a(byteArrayOutputStream);
                    throw th;
                }
            }
            z = a(8, str, toByteArray);
        }
        return z;
    }

    public boolean putSmoothSerializable(String str, Serializable serializable) {
        Closeable byteArrayOutputStream;
        boolean z;
        Throwable th;
        Closeable closeable = null;
        synchronized (this) {
            byte[] toByteArray;
            a(str, (Object) serializable);
            if (serializable != null) {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        Closeable objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        try {
                            objectOutputStream.writeObject(serializable);
                            objectOutputStream.flush();
                            toByteArray = byteArrayOutputStream.toByteArray();
                            a(objectOutputStream);
                            a(byteArrayOutputStream);
                        } catch (IOException e) {
                            closeable = objectOutputStream;
                            z = false;
                            a(closeable);
                            a(byteArrayOutputStream);
                            return z;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            closeable = objectOutputStream;
                            th = th3;
                            a(closeable);
                            a(byteArrayOutputStream);
                            throw th;
                        }
                    } catch (IOException e2) {
                        z = false;
                        a(closeable);
                        a(byteArrayOutputStream);
                        return z;
                    } catch (Throwable th4) {
                        th = th4;
                        a(closeable);
                        a(byteArrayOutputStream);
                        throw th;
                    }
                } catch (IOException e3) {
                    byteArrayOutputStream = null;
                    z = false;
                    a(closeable);
                    a(byteArrayOutputStream);
                    return z;
                } catch (Throwable th5) {
                    th = th5;
                    byteArrayOutputStream = null;
                    a(closeable);
                    a(byteArrayOutputStream);
                    throw th;
                }
            }
            z = b(8, str, toByteArray);
        }
        return z;
    }

    public short getShort(String str, short s) {
        short shortValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    shortValue = ((Short) c).shortValue();
                } else {
                    byte[] a = a(1, str);
                    if (a != null) {
                        s = (short) 0;
                        for (int i = 0; i < 2; i++) {
                            s = (short) (((a[i] & 255) << ((1 - i) * 8)) + s);
                        }
                    }
                    shortValue = s;
                }
            } catch (Throwable th) {
                shortValue = s;
            }
        }
        return shortValue;
    }

    public int getInt(String str, int i) {
        int intValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    intValue = ((Integer) c).intValue();
                } else {
                    byte[] a = a(2, str);
                    if (a != null) {
                        i = b2i(a);
                    }
                    intValue = i;
                }
            } catch (Throwable th) {
                intValue = i;
            }
        }
        return intValue;
    }

    public long getLong(String str, long j) {
        long longValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    longValue = ((Long) c).longValue();
                } else {
                    byte[] a = a(3, str);
                    if (a != null) {
                        j = a(a);
                    }
                    longValue = j;
                }
            } catch (Throwable th) {
                longValue = j;
            }
        }
        return longValue;
    }

    public float getFloat(String str, float f) {
        float floatValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    floatValue = ((Float) c).floatValue();
                } else {
                    byte[] a = a(4, str);
                    if (a != null) {
                        f = Float.intBitsToFloat(b2i(a));
                    }
                    floatValue = f;
                }
            } catch (Throwable th) {
                floatValue = f;
            }
        }
        return floatValue;
    }

    public double getDouble(String str, double d) {
        double doubleValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    doubleValue = ((Double) c).doubleValue();
                } else {
                    byte[] a = a(5, str);
                    if (a != null) {
                        d = Double.longBitsToDouble(a(a));
                    }
                    doubleValue = d;
                }
            } catch (Throwable th) {
                doubleValue = d;
            }
        }
        return doubleValue;
    }

    public String getString(String str, String str2) {
        String str3;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    str3 = (String) c;
                } else {
                    byte[] a = a(7, str);
                    if (a != null) {
                        str2 = new String(a, "UTF-8");
                    }
                    str3 = str2;
                }
            } catch (Throwable th) {
                str3 = str2;
            }
        }
        return str3;
    }

    public boolean getBoolean(String str, boolean z) {
        boolean booleanValue;
        synchronized (this) {
            try {
                Object c = c(str);
                if (c != null) {
                    booleanValue = ((Boolean) c).booleanValue();
                } else {
                    byte[] a = a(6, str);
                    booleanValue = a != null ? a[0] == (byte) 1 : z;
                }
            } catch (Throwable th) {
                booleanValue = z;
            }
        }
        return booleanValue;
    }

    public <T extends Serializable> T getSerializable(String str, Class<T> cls, T t) {
        T t2;
        Closeable closeable;
        Throwable th;
        synchronized (this) {
            Closeable closeable2 = null;
            try {
                Object c = c(str);
                if (c != null) {
                    t2 = (Serializable) c;
                    a(null);
                } else {
                    Closeable objectInputStream;
                    byte[] a = a(8, str);
                    if (a != null) {
                        objectInputStream = new ObjectInputStream(new ByteArrayInputStream(a));
                        try {
                            t2 = (Serializable) objectInputStream.readObject();
                        } catch (Throwable th2) {
                            th = th2;
                            closeable2 = objectInputStream;
                            a(closeable2);
                            throw th;
                        }
                    }
                    t2 = t;
                    objectInputStream = null;
                    a(objectInputStream);
                }
            } catch (Throwable th3) {
                th = th3;
                a(closeable2);
                throw th;
            }
        }
        return t2;
    }

    public boolean remove(String str) {
        boolean z;
        synchronized (this) {
            try {
                if (!(this.d == null || this.d.isEmpty())) {
                    this.d.remove(str);
                }
                Editor edit = this.a.edit();
                edit.remove(a() + str);
                edit.apply();
                z = true;
            } catch (Exception e) {
                z = false;
            }
        }
        return z;
    }

    public boolean contains(String str) {
        boolean z = false;
        synchronized (this) {
            try {
                if (this.d.containsKey(str) || this.a.contains(a() + str)) {
                    z = true;
                }
            } catch (Exception e) {
            }
        }
        return z;
    }

    public List<String> getKeys() {
        Collection collection;
        synchronized (this) {
            Map all = this.a.getAll();
            if (this.d == null || this.d.isEmpty()) {
                collection = null;
            } else {
                collection = this.d.keySet();
            }
        }
        Collection hashSet = new HashSet();
        if (!all.isEmpty()) {
            String a = a();
            for (String str : all.keySet()) {
                if (str.startsWith(a)) {
                    hashSet.add(str.substring(a.length()));
                }
            }
        }
        if (!(collection == null || collection.isEmpty())) {
            hashSet.addAll(collection);
        }
        return new ArrayList(hashSet);
    }

    public Map<String, Object> getAll() {
        Map map;
        Object valueOf;
        Closeable closeable;
        Throwable th;
        Closeable closeable2 = null;
        Map<String, Object> hashMap = new HashMap();
        synchronized (this) {
            Map all = this.a.getAll();
            if (!(all == null || all.isEmpty())) {
                String a = a();
                for (String str : all.keySet()) {
                    if (str.startsWith(a)) {
                        hashMap.put(str.substring(a.length()), all.get(str));
                    }
                }
            }
            if (this.d == null || this.d.isEmpty()) {
                map = null;
            } else {
                map = new HashMap(this.d);
            }
        }
        for (String str2 : hashMap.keySet()) {
            Object a2 = a(String.valueOf(hashMap.get(str2)));
            if (a2 != null && a2.length > 0) {
                byte b = a2[0];
                byte[] bArr = new byte[(a2.length - 1)];
                System.arraycopy(a2, 1, bArr, 0, bArr.length);
                switch (b) {
                    case (byte) 0:
                        continue;
                    case (byte) 1:
                        short s = (short) 0;
                        for (int i = 0; i < 2; i++) {
                            s = (short) (s + ((bArr[i] & 255) << ((1 - i) * 8)));
                        }
                        valueOf = Short.valueOf(s);
                        continue;
                    case (byte) 2:
                        valueOf = Integer.valueOf(b2i(bArr));
                        continue;
                    case (byte) 3:
                        valueOf = Long.valueOf(a(bArr));
                        continue;
                    case (byte) 4:
                        valueOf = Float.valueOf(Float.intBitsToFloat(b2i(bArr)));
                        continue;
                    case (byte) 5:
                        valueOf = Double.valueOf(Double.longBitsToDouble(a(bArr)));
                        continue;
                    case (byte) 6:
                        valueOf = Boolean.valueOf(bArr[0] == (byte) 1);
                        continue;
                    case (byte) 7:
                        try {
                            String str3 = new String(bArr, "UTF-8");
                            continue;
                        } catch (UnsupportedEncodingException e) {
                            valueOf = null;
                            break;
                        }
                    case (byte) 8:
                        try {
                            Closeable objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bArr));
                            try {
                                valueOf = objectInputStream.readObject();
                                a(objectInputStream);
                                continue;
                            } catch (Throwable th2) {
                                th = th2;
                                closeable2 = objectInputStream;
                                break;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            break;
                        }
                }
            }
            valueOf = null;
            hashMap.put(str2, valueOf);
        }
        if (!(map == null || map.isEmpty())) {
            hashMap.putAll(map);
        }
        return hashMap;
        a(closeable2);
        throw th;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cleanAllStorage() {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.d;	 Catch:{ all -> 0x0044 }
        r0.clear();	 Catch:{ all -> 0x0044 }
        r0 = r5.c();	 Catch:{ all -> 0x0044 }
        r1 = r5.c;	 Catch:{ all -> 0x0044 }
        r0.removeCallbacksAndMessages(r1);	 Catch:{ all -> 0x0044 }
        r0 = r5.a;	 Catch:{ all -> 0x0044 }
        r0 = r0.getAll();	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x006d;
    L_0x0017:
        r1 = r0.isEmpty();	 Catch:{ all -> 0x0044 }
        if (r1 != 0) goto L_0x006d;
    L_0x001d:
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0044 }
        r1.<init>();	 Catch:{ all -> 0x0044 }
        r2 = r5.a();	 Catch:{ all -> 0x0044 }
        r0 = r0.keySet();	 Catch:{ all -> 0x0044 }
        r3 = r0.iterator();	 Catch:{ all -> 0x0044 }
    L_0x002e:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x0047;
    L_0x0034:
        r0 = r3.next();	 Catch:{ all -> 0x0044 }
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0044 }
        r4 = r0.startsWith(r2);	 Catch:{ all -> 0x0044 }
        if (r4 == 0) goto L_0x002e;
    L_0x0040:
        r1.add(r0);	 Catch:{ all -> 0x0044 }
        goto L_0x002e;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0044 }
        throw r0;
    L_0x0047:
        r0 = r1.isEmpty();	 Catch:{ all -> 0x0044 }
        if (r0 != 0) goto L_0x006d;
    L_0x004d:
        r0 = r5.a;	 Catch:{ all -> 0x0044 }
        r2 = r0.edit();	 Catch:{ all -> 0x0044 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0044 }
    L_0x0057:
        r0 = r1.hasNext();	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x0067;
    L_0x005d:
        r0 = r1.next();	 Catch:{ all -> 0x0044 }
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0044 }
        r2.remove(r0);	 Catch:{ all -> 0x0044 }
        goto L_0x0057;
    L_0x0067:
        r2.apply();	 Catch:{ all -> 0x0044 }
        r0 = 1;
        monitor-exit(r5);	 Catch:{ all -> 0x0044 }
    L_0x006c:
        return r0;
    L_0x006d:
        monitor-exit(r5);	 Catch:{ all -> 0x0044 }
        r0 = 0;
        goto L_0x006c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.storage.SpStorage.cleanAllStorage():boolean");
    }

    private String a() {
        return hashKeyForDisk(this.b) + "_";
    }

    private void a(Editor editor, String str) {
        Message obtain = Message.obtain(c(), new a(this, str, editor));
        obtain.obj = this.c;
        c().sendMessage(obtain);
    }

    private boolean a(String str, Object obj) {
        if (this.d == null) {
            return false;
        }
        this.d.put(str, obj);
        return true;
    }

    private Object c(String str) {
        if (this.d == null || !this.d.containsKey(str)) {
            return null;
        }
        return this.d.get(str);
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    private HandlerThread b() {
        if (e == null) {
            e = new HandlerThread("SmoothSpStorage");
            e.start();
        }
        return e;
    }

    private Handler c() {
        if (f == null) {
            synchronized (SpStorage.class) {
                if (f == null) {
                    f = new Handler(b().getLooper());
                }
            }
        }
        return f;
    }

    public static byte[] i2b(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    private static byte[] a(long j) {
        return new byte[]{(byte) ((int) ((j >> 56) & 255)), (byte) ((int) ((j >> 48) & 255)), (byte) ((int) ((j >> 40) & 255)), (byte) ((int) ((j >> 32) & 255)), (byte) ((int) ((j >> 24) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) (j & 255))};
    }

    public static int b2i(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i < 4) {
            i2 += (bArr[i] & 255) << ((3 - i) * 8);
            i++;
        }
        return i2;
    }

    private static long a(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            j += (((long) bArr[i]) & 255) << ((7 - i) * 8);
        }
        return j;
    }

    public static String hashKeyForDisk(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            return b(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(str.hashCode());
        }
    }

    private static String b(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }
}
