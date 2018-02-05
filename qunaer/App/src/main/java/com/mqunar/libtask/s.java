package com.mqunar.libtask;

import android.content.Context;
import java.io.Serializable;
import qunar.lego.utils.Goblin;

final class s {
    public static Serializable[] a(Context context, byte[] bArr, String str, boolean z) {
        if (bArr.length < 9) {
            return null;
        }
        ByteArray byteArray = new ByteArray(bArr);
        byteArray.setCursor(8);
        if (byteArray.get(8) != (byte) 0) {
            return null;
        }
        byteArray.inc();
        if (!byteArray.valid()) {
            return null;
        }
        int i;
        byte b = byteArray.getByte();
        int i2 = (b & 240) >> 4;
        int i3 = b & 15;
        if (i3 != 0) {
            byteArray.getInt();
            i = byteArray.getInt();
        } else {
            i = byteArray.getInt();
        }
        ByteArray byteArray2;
        if (i2 == 0) {
            if (i3 != 0) {
                byteArray2 = new ByteArray(t.a(byteArray.rawBytes(), byteArray.getCursor(), i));
            } else {
                byteArray2 = byteArray;
            }
            if (byteArray2.getIntOfByte() == 1) {
                a(byteArray2, context);
            }
            i = byteArray2.getInt();
            return new Serializable[]{"json", byteArray2.copyBytes(i)};
        } else if (i2 != 1) {
            return null;
        } else {
            byte[] copyBytes;
            if (z) {
                copyBytes = byteArray.copyBytes(i);
            } else {
                copyBytes = Goblin.dn1(byteArray.copyBytes(i), str);
            }
            if (i3 == 0) {
                byteArray2 = new ByteArray(copyBytes);
            } else {
                byteArray2 = new ByteArray(t.a(copyBytes, 0, copyBytes.length));
            }
            if (byteArray2.getIntOfByte() == 1) {
                a(byteArray2, context);
            }
            copyBytes = byteArray2.copyBytes(byteArray2.getInt());
            if (i3 == 3) {
                return new Serializable[]{"proto", copyBytes};
            }
            return new Serializable[]{"json", copyBytes};
        }
    }

    protected static void a(ByteArray byteArray, Context context) {
        if (byteArray.getInt() != 0) {
            int i = byteArray.getInt();
            for (int i2 = 0; i2 < i; i2++) {
                byte[] bArr;
                String string = byteArray.getString(byteArray.getIntOfByte());
                int intOfByte = byteArray.getIntOfByte();
                int i3 = byteArray.getInt();
                if (intOfByte > 1 || i3 <= 0) {
                    bArr = null;
                } else {
                    bArr = byteArray.getByteArray(i3);
                }
                if (!(bArr == null || string == null || string.length() <= 0)) {
                    Class cls;
                    Object invoke;
                    try {
                        cls = Class.forName("com.mqunar.imagecache.ImageLoader");
                        invoke = cls.getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{context});
                        cls.getDeclaredMethod("putDataByKey", new Class[]{String.class, byte[].class}).invoke(invoke, new Object[]{string, bArr});
                    } catch (Throwable th) {
                    }
                    try {
                        cls = Class.forName("com.squareup.picasso.Picasso");
                        invoke = cls.getDeclaredMethod("with", new Class[]{Context.class}).invoke(null, new Object[]{context});
                        Object invoke2 = cls.getDeclaredMethod("loadByKey", new Class[]{String.class}).invoke(invoke, new Object[]{string});
                        invoke2.getClass().getDeclaredMethod("putDataToKey", new Class[]{byte[].class}).invoke(invoke2, new Object[]{bArr});
                    } catch (Throwable th2) {
                    }
                }
            }
        }
    }
}
