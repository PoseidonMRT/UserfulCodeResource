package com.mqunar.tools;

import com.mqunar.libtask.ProgressType;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.RandomAccess;

public class ArrayUtils {
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int INDEX_NOT_FOUND = -1;

    class ReadOnlyArrayList<E> extends AbstractList<E> implements Serializable, List<E>, RandomAccess {
        private static final long serialVersionUID = 1;
        private final E[] a;

        ReadOnlyArrayList(E[] eArr) {
            this.a = eArr;
        }

        public boolean contains(Object obj) {
            if (this.a == null) {
                return false;
            }
            if (obj != null) {
                for (Object equals : this.a) {
                    if (obj.equals(equals)) {
                        return true;
                    }
                }
                return false;
            }
            for (Object equals2 : this.a) {
                if (equals2 == null) {
                    return true;
                }
            }
            return false;
        }

        public E get(int i) {
            try {
                return this.a[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException();
            } catch (NullPointerException e2) {
                throw new IndexOutOfBoundsException();
            }
        }

        public int indexOf(Object obj) {
            int i = 0;
            if (this.a == null) {
                return -1;
            }
            if (obj != null) {
                while (i < this.a.length) {
                    if (obj.equals(this.a[i])) {
                        return i;
                    }
                    i++;
                }
            } else {
                while (i < this.a.length) {
                    if (this.a[i] == null) {
                        return i;
                    }
                    i++;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object obj) {
            if (this.a == null) {
                return -1;
            }
            int length;
            if (obj != null) {
                for (length = this.a.length - 1; length >= 0; length--) {
                    if (obj.equals(this.a[length])) {
                        return length;
                    }
                }
                return -1;
            }
            for (length = this.a.length - 1; length >= 0; length--) {
                if (this.a[length] == null) {
                    return length;
                }
            }
            return -1;
        }

        public E set(int i, E e) {
            if (this.a == null) {
                throw new IndexOutOfBoundsException();
            }
            try {
                E e2 = this.a[i];
                this.a[i] = e;
                return e2;
            } catch (ArrayIndexOutOfBoundsException e3) {
                throw new IndexOutOfBoundsException();
            } catch (ArrayStoreException e4) {
                throw new ClassCastException();
            }
        }

        public int size() {
            return this.a == null ? 0 : this.a.length;
        }

        public Object[] toArray() {
            if (this.a == null) {
                return new Object[0];
            }
            return (Object[]) this.a.clone();
        }

        public <T> T[] toArray(T[] tArr) {
            if (this.a == null) {
                return tArr;
            }
            Object obj;
            int size = size();
            if (size > tArr.length) {
                obj = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            } else {
                obj = tArr;
            }
            System.arraycopy(this.a, 0, obj, 0, size);
            if (size < obj.length) {
                obj[size] = null;
            }
            return obj;
        }
    }

    public static Map<Object, Object> toMap(Object[] objArr) {
        if (objArr == null) {
            return null;
        }
        Map<Object, Object> hashMap = new HashMap((int) (((double) objArr.length) * 1.5d));
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                hashMap.put(entry.getKey(), entry.getValue());
            } else if (obj instanceof Object[]) {
                Object[] objArr2 = (Object[]) obj;
                if (objArr2.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '" + obj + "', has a length less than 2");
                }
                hashMap.put(objArr2[0], objArr2[1]);
            } else {
                throw new IllegalArgumentException("Array element " + i + ", '" + obj + "', is neither of type Map.Entry nor an Array");
            }
        }
        return hashMap;
    }

    public static <T> T[] toArray(T... tArr) {
        return tArr;
    }

    public static <T> T[] clone(T[] tArr) {
        if (tArr == null) {
            return null;
        }
        return (Object[]) tArr.clone();
    }

    public static long[] clone(long[] jArr) {
        if (jArr == null) {
            return null;
        }
        return (long[]) jArr.clone();
    }

    public static int[] clone(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        return (int[]) iArr.clone();
    }

    public static short[] clone(short[] sArr) {
        if (sArr == null) {
            return null;
        }
        return (short[]) sArr.clone();
    }

    public static char[] clone(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        return (char[]) cArr.clone();
    }

    public static byte[] clone(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public static double[] clone(double[] dArr) {
        if (dArr == null) {
            return null;
        }
        return (double[]) dArr.clone();
    }

    public static float[] clone(float[] fArr) {
        if (fArr == null) {
            return null;
        }
        return (float[]) fArr.clone();
    }

    public static boolean[] clone(boolean[] zArr) {
        if (zArr == null) {
            return null;
        }
        return (boolean[]) zArr.clone();
    }

    public static Object[] nullToEmpty(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            return EMPTY_OBJECT_ARRAY;
        }
        return objArr;
    }

    public static String[] nullToEmpty(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return strArr;
    }

    public static long[] nullToEmpty(long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        return jArr;
    }

    public static int[] nullToEmpty(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        return iArr;
    }

    public static short[] nullToEmpty(short[] sArr) {
        if (sArr == null || sArr.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        return sArr;
    }

    public static char[] nullToEmpty(char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        return cArr;
    }

    public static byte[] nullToEmpty(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        return bArr;
    }

    public static double[] nullToEmpty(double[] dArr) {
        if (dArr == null || dArr.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        return dArr;
    }

    public static float[] nullToEmpty(float[] fArr) {
        if (fArr == null || fArr.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        return fArr;
    }

    public static boolean[] nullToEmpty(boolean[] zArr) {
        if (zArr == null || zArr.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        return zArr;
    }

    public static Long[] nullToEmpty(Long[] lArr) {
        if (lArr == null || lArr.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        return lArr;
    }

    public static Integer[] nullToEmpty(Integer[] numArr) {
        if (numArr == null || numArr.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        return numArr;
    }

    public static Short[] nullToEmpty(Short[] shArr) {
        if (shArr == null || shArr.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        return shArr;
    }

    public static Character[] nullToEmpty(Character[] chArr) {
        if (chArr == null || chArr.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        return chArr;
    }

    public static Byte[] nullToEmpty(Byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        return bArr;
    }

    public static Double[] nullToEmpty(Double[] dArr) {
        if (dArr == null || dArr.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        return dArr;
    }

    public static Float[] nullToEmpty(Float[] fArr) {
        if (fArr == null || fArr.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        return fArr;
    }

    public static Boolean[] nullToEmpty(Boolean[] boolArr) {
        if (boolArr == null || boolArr.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        return boolArr;
    }

    public static <T> T[] subarray(T[] tArr, int i, int i2) {
        if (tArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > tArr.length) {
            i2 = tArr.length;
        }
        int i3 = i2 - i;
        Class componentType = tArr.getClass().getComponentType();
        if (i3 <= 0) {
            return (Object[]) Array.newInstance(componentType, 0);
        }
        Object[] objArr = (Object[]) Array.newInstance(componentType, i3);
        System.arraycopy(tArr, i, objArr, 0, i3);
        return objArr;
    }

    public static long[] subarray(long[] jArr, int i, int i2) {
        if (jArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > jArr.length) {
            i2 = jArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_LONG_ARRAY;
        }
        Object obj = new long[i3];
        System.arraycopy(jArr, i, obj, 0, i3);
        return obj;
    }

    public static int[] subarray(int[] iArr, int i, int i2) {
        if (iArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > iArr.length) {
            i2 = iArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_INT_ARRAY;
        }
        Object obj = new int[i3];
        System.arraycopy(iArr, i, obj, 0, i3);
        return obj;
    }

    public static short[] subarray(short[] sArr, int i, int i2) {
        if (sArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > sArr.length) {
            i2 = sArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_SHORT_ARRAY;
        }
        Object obj = new short[i3];
        System.arraycopy(sArr, i, obj, 0, i3);
        return obj;
    }

    public static char[] subarray(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > cArr.length) {
            i2 = cArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_CHAR_ARRAY;
        }
        Object obj = new char[i3];
        System.arraycopy(cArr, i, obj, 0, i3);
        return obj;
    }

    public static byte[] subarray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > bArr.length) {
            i2 = bArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        Object obj = new byte[i3];
        System.arraycopy(bArr, i, obj, 0, i3);
        return obj;
    }

    public static double[] subarray(double[] dArr, int i, int i2) {
        if (dArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > dArr.length) {
            i2 = dArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        Object obj = new double[i3];
        System.arraycopy(dArr, i, obj, 0, i3);
        return obj;
    }

    public static float[] subarray(float[] fArr, int i, int i2) {
        if (fArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > fArr.length) {
            i2 = fArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        Object obj = new float[i3];
        System.arraycopy(fArr, i, obj, 0, i3);
        return obj;
    }

    public static boolean[] subarray(boolean[] zArr, int i, int i2) {
        if (zArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > zArr.length) {
            i2 = zArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        Object obj = new boolean[i3];
        System.arraycopy(zArr, i, obj, 0, i3);
        return obj;
    }

    public static boolean isSameLength(Object[] objArr, Object[] objArr2) {
        if ((objArr != null || objArr2 == null || objArr2.length <= 0) && ((objArr2 != null || objArr == null || objArr.length <= 0) && (objArr == null || objArr2 == null || objArr.length == objArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(long[] jArr, long[] jArr2) {
        if ((jArr != null || jArr2 == null || jArr2.length <= 0) && ((jArr2 != null || jArr == null || jArr.length <= 0) && (jArr == null || jArr2 == null || jArr.length == jArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(int[] iArr, int[] iArr2) {
        if ((iArr != null || iArr2 == null || iArr2.length <= 0) && ((iArr2 != null || iArr == null || iArr.length <= 0) && (iArr == null || iArr2 == null || iArr.length == iArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(short[] sArr, short[] sArr2) {
        if ((sArr != null || sArr2 == null || sArr2.length <= 0) && ((sArr2 != null || sArr == null || sArr.length <= 0) && (sArr == null || sArr2 == null || sArr.length == sArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(char[] cArr, char[] cArr2) {
        if ((cArr != null || cArr2 == null || cArr2.length <= 0) && ((cArr2 != null || cArr == null || cArr.length <= 0) && (cArr == null || cArr2 == null || cArr.length == cArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(byte[] bArr, byte[] bArr2) {
        if ((bArr != null || bArr2 == null || bArr2.length <= 0) && ((bArr2 != null || bArr == null || bArr.length <= 0) && (bArr == null || bArr2 == null || bArr.length == bArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(double[] dArr, double[] dArr2) {
        if ((dArr != null || dArr2 == null || dArr2.length <= 0) && ((dArr2 != null || dArr == null || dArr.length <= 0) && (dArr == null || dArr2 == null || dArr.length == dArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(float[] fArr, float[] fArr2) {
        if ((fArr != null || fArr2 == null || fArr2.length <= 0) && ((fArr2 != null || fArr == null || fArr.length <= 0) && (fArr == null || fArr2 == null || fArr.length == fArr2.length))) {
            return true;
        }
        return false;
    }

    public static boolean isSameLength(boolean[] zArr, boolean[] zArr2) {
        if ((zArr != null || zArr2 == null || zArr2.length <= 0) && ((zArr2 != null || zArr == null || zArr.length <= 0) && (zArr == null || zArr2 == null || zArr.length == zArr2.length))) {
            return true;
        }
        return false;
    }

    public static int getLength(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Array.getLength(obj);
    }

    public static boolean isSameType(Object obj, Object obj2) {
        if (obj != null && obj2 != null) {
            return obj.getClass().getName().equals(obj2.getClass().getName());
        }
        throw new IllegalArgumentException("The Array must not be null");
    }

    public static void reverse(Object[] objArr) {
        if (objArr != null) {
            int length = objArr.length - 1;
            for (int i = 0; length > i; i++) {
                Object obj = objArr[length];
                objArr[length] = objArr[i];
                objArr[i] = obj;
                length--;
            }
        }
    }

    public static void reverse(long[] jArr) {
        if (jArr != null) {
            int length = jArr.length - 1;
            for (int i = 0; length > i; i++) {
                long j = jArr[length];
                jArr[length] = jArr[i];
                jArr[i] = j;
                length--;
            }
        }
    }

    public static void reverse(int[] iArr) {
        if (iArr != null) {
            int length = iArr.length - 1;
            for (int i = 0; length > i; i++) {
                int i2 = iArr[length];
                iArr[length] = iArr[i];
                iArr[i] = i2;
                length--;
            }
        }
    }

    public static void reverse(short[] sArr) {
        if (sArr != null) {
            int length = sArr.length - 1;
            for (int i = 0; length > i; i++) {
                short s = sArr[length];
                sArr[length] = sArr[i];
                sArr[i] = s;
                length--;
            }
        }
    }

    public static void reverse(char[] cArr) {
        if (cArr != null) {
            int length = cArr.length - 1;
            for (int i = 0; length > i; i++) {
                char c = cArr[length];
                cArr[length] = cArr[i];
                cArr[i] = c;
                length--;
            }
        }
    }

    public static void reverse(byte[] bArr) {
        if (bArr != null) {
            int length = bArr.length - 1;
            for (int i = 0; length > i; i++) {
                byte b = bArr[length];
                bArr[length] = bArr[i];
                bArr[i] = b;
                length--;
            }
        }
    }

    public static void reverse(double[] dArr) {
        if (dArr != null) {
            int length = dArr.length - 1;
            for (int i = 0; length > i; i++) {
                double d = dArr[length];
                dArr[length] = dArr[i];
                dArr[i] = d;
                length--;
            }
        }
    }

    public static void reverse(float[] fArr) {
        if (fArr != null) {
            int length = fArr.length - 1;
            for (int i = 0; length > i; i++) {
                float f = fArr[length];
                fArr[length] = fArr[i];
                fArr[i] = f;
                length--;
            }
        }
    }

    public static void reverse(boolean[] zArr) {
        if (zArr != null) {
            int length = zArr.length - 1;
            for (int i = 0; length > i; i++) {
                boolean z = zArr[length];
                zArr[length] = zArr[i];
                zArr[i] = z;
                length--;
            }
        }
    }

    public static int indexOf(Object[] objArr, Object obj) {
        return indexOf(objArr, obj, 0);
    }

    public static int indexOf(Object[] objArr, Object obj, int i) {
        if (objArr == null) {
            return -1;
        }
        int i2;
        if (i < 0) {
            i2 = 0;
        } else {
            i2 = i;
        }
        if (obj == null) {
            while (i2 < objArr.length) {
                if (objArr[i2] == null) {
                    return i2;
                }
                i2++;
            }
        } else if (objArr.getClass().getComponentType().isInstance(obj)) {
            while (i2 < objArr.length) {
                if (obj.equals(objArr[i2])) {
                    return i2;
                }
                i2++;
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object[] objArr, Object obj) {
        return lastIndexOf(objArr, obj, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(Object[] objArr, Object obj, int i) {
        if (objArr == null || i < 0) {
            return -1;
        }
        int length;
        if (i >= objArr.length) {
            length = objArr.length - 1;
        } else {
            length = i;
        }
        if (obj == null) {
            while (length >= 0) {
                if (objArr[length] == null) {
                    return length;
                }
                length--;
            }
            return -1;
        } else if (!objArr.getClass().getComponentType().isInstance(obj)) {
            return -1;
        } else {
            while (length >= 0) {
                if (obj.equals(objArr[length])) {
                    return length;
                }
                length--;
            }
            return -1;
        }
    }

    public static boolean contains(Object[] objArr, Object obj) {
        return indexOf(objArr, obj) != -1;
    }

    public static int indexOf(long[] jArr, long j) {
        return indexOf(jArr, j, 0);
    }

    public static int indexOf(long[] jArr, long j, int i) {
        if (jArr == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < jArr.length) {
            if (j == jArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(long[] jArr, long j) {
        return lastIndexOf(jArr, j, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(long[] jArr, long j, int i) {
        if (jArr == null || i < 0) {
            return -1;
        }
        if (i >= jArr.length) {
            i = jArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (j == jArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(long[] jArr, long j) {
        return indexOf(jArr, j) != -1;
    }

    public static int indexOf(int[] iArr, int i) {
        return indexOf(iArr, i, 0);
    }

    public static int indexOf(int[] iArr, int i, int i2) {
        if (iArr == null) {
            return -1;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        while (i2 < iArr.length) {
            if (i == iArr[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int lastIndexOf(int[] iArr, int i) {
        return lastIndexOf(iArr, i, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(int[] iArr, int i, int i2) {
        if (iArr == null || i2 < 0) {
            return -1;
        }
        if (i2 >= iArr.length) {
            i2 = iArr.length - 1;
        }
        for (int i3 = i2; i3 >= 0; i3--) {
            if (i == iArr[i3]) {
                return i3;
            }
        }
        return -1;
    }

    public static boolean contains(int[] iArr, int i) {
        return indexOf(iArr, i) != -1;
    }

    public static int indexOf(short[] sArr, short s) {
        return indexOf(sArr, s, 0);
    }

    public static int indexOf(short[] sArr, short s, int i) {
        if (sArr == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < sArr.length) {
            if (s == sArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(short[] sArr, short s) {
        return lastIndexOf(sArr, s, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(short[] sArr, short s, int i) {
        if (sArr == null || i < 0) {
            return -1;
        }
        if (i >= sArr.length) {
            i = sArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (s == sArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(short[] sArr, short s) {
        return indexOf(sArr, s) != -1;
    }

    public static int indexOf(char[] cArr, char c) {
        return indexOf(cArr, c, 0);
    }

    public static int indexOf(char[] cArr, char c, int i) {
        if (cArr == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < cArr.length) {
            if (c == cArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(char[] cArr, char c) {
        return lastIndexOf(cArr, c, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(char[] cArr, char c, int i) {
        if (cArr == null || i < 0) {
            return -1;
        }
        if (i >= cArr.length) {
            i = cArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (c == cArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(char[] cArr, char c) {
        return indexOf(cArr, c) != -1;
    }

    public static int indexOf(byte[] bArr, byte b) {
        return indexOf(bArr, b, 0);
    }

    public static int indexOf(byte[] bArr, byte b, int i) {
        if (bArr == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < bArr.length) {
            if (b == bArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(byte[] bArr, byte b) {
        return lastIndexOf(bArr, b, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(byte[] bArr, byte b, int i) {
        if (bArr == null || i < 0) {
            return -1;
        }
        if (i >= bArr.length) {
            i = bArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (b == bArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(byte[] bArr, byte b) {
        return indexOf(bArr, b) != -1;
    }

    public static int indexOf(double[] dArr, double d) {
        return indexOf(dArr, d, 0);
    }

    public static int indexOf(double[] dArr, double d, double d2) {
        return indexOf(dArr, d, 0, d2);
    }

    public static int indexOf(double[] dArr, double d, int i) {
        if (isEmpty(dArr)) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < dArr.length) {
            if (d == dArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(double[] dArr, double d, int i, double d2) {
        if (isEmpty(dArr)) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        double d3 = d - d2;
        double d4 = d + d2;
        while (i < dArr.length) {
            if (dArr[i] >= d3 && dArr[i] <= d4) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(double[] dArr, double d) {
        return lastIndexOf(dArr, d, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(double[] dArr, double d, double d2) {
        return lastIndexOf(dArr, d, ProgressType.PRO_END, d2);
    }

    public static int lastIndexOf(double[] dArr, double d, int i) {
        if (isEmpty(dArr) || i < 0) {
            return -1;
        }
        if (i >= dArr.length) {
            i = dArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (d == dArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] dArr, double d, int i, double d2) {
        if (isEmpty(dArr) || i < 0) {
            return -1;
        }
        if (i >= dArr.length) {
            i = dArr.length - 1;
        }
        double d3 = d - d2;
        double d4 = d + d2;
        int i2 = i;
        while (i2 >= 0) {
            if (dArr[i2] >= d3 && dArr[i2] <= d4) {
                return i2;
            }
            i2--;
        }
        return -1;
    }

    public static boolean contains(double[] dArr, double d) {
        return indexOf(dArr, d) != -1;
    }

    public static boolean contains(double[] dArr, double d, double d2) {
        return indexOf(dArr, d, 0, d2) != -1;
    }

    public static int indexOf(float[] fArr, float f) {
        return indexOf(fArr, f, 0);
    }

    public static int indexOf(float[] fArr, float f, int i) {
        if (isEmpty(fArr)) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < fArr.length) {
            if (f == fArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(float[] fArr, float f) {
        return lastIndexOf(fArr, f, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(float[] fArr, float f, int i) {
        if (isEmpty(fArr) || i < 0) {
            return -1;
        }
        if (i >= fArr.length) {
            i = fArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (f == fArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(float[] fArr, float f) {
        return indexOf(fArr, f) != -1;
    }

    public static int indexOf(boolean[] zArr, boolean z) {
        return indexOf(zArr, z, 0);
    }

    public static int indexOf(boolean[] zArr, boolean z, int i) {
        if (isEmpty(zArr)) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        while (i < zArr.length) {
            if (z == zArr[i]) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] zArr, boolean z) {
        return lastIndexOf(zArr, z, (int) ProgressType.PRO_END);
    }

    public static int lastIndexOf(boolean[] zArr, boolean z, int i) {
        if (isEmpty(zArr) || i < 0) {
            return -1;
        }
        if (i >= zArr.length) {
            i = zArr.length - 1;
        }
        for (int i2 = i; i2 >= 0; i2--) {
            if (z == zArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean contains(boolean[] zArr, boolean z) {
        return indexOf(zArr, z) != -1;
    }

    public static char[] toPrimitive(Character[] chArr) {
        if (chArr == null) {
            return null;
        }
        if (chArr.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] cArr = new char[chArr.length];
        for (int i = 0; i < chArr.length; i++) {
            cArr[i] = chArr[i].charValue();
        }
        return cArr;
    }

    public static char[] toPrimitive(Character[] chArr, char c) {
        if (chArr == null) {
            return null;
        }
        if (chArr.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] cArr = new char[chArr.length];
        for (int i = 0; i < chArr.length; i++) {
            Character ch = chArr[i];
            cArr[i] = ch == null ? c : ch.charValue();
        }
        return cArr;
    }

    public static Character[] toObject(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        if (cArr.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        Character[] chArr = new Character[cArr.length];
        for (int i = 0; i < cArr.length; i++) {
            chArr[i] = Character.valueOf(cArr[i]);
        }
        return chArr;
    }

    public static long[] toPrimitive(Long[] lArr) {
        if (lArr == null) {
            return null;
        }
        if (lArr.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] jArr = new long[lArr.length];
        for (int i = 0; i < lArr.length; i++) {
            jArr[i] = lArr[i].longValue();
        }
        return jArr;
    }

    public static long[] toPrimitive(Long[] lArr, long j) {
        if (lArr == null) {
            return null;
        }
        if (lArr.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] jArr = new long[lArr.length];
        for (int i = 0; i < lArr.length; i++) {
            Long l = lArr[i];
            jArr[i] = l == null ? j : l.longValue();
        }
        return jArr;
    }

    public static Long[] toObject(long[] jArr) {
        if (jArr == null) {
            return null;
        }
        if (jArr.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        Long[] lArr = new Long[jArr.length];
        for (int i = 0; i < jArr.length; i++) {
            lArr[i] = Long.valueOf(jArr[i]);
        }
        return lArr;
    }

    public static int[] toPrimitive(Integer[] numArr) {
        if (numArr == null) {
            return null;
        }
        if (numArr.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] iArr = new int[numArr.length];
        for (int i = 0; i < numArr.length; i++) {
            iArr[i] = numArr[i].intValue();
        }
        return iArr;
    }

    public static int[] toPrimitive(Integer[] numArr, int i) {
        if (numArr == null) {
            return null;
        }
        if (numArr.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] iArr = new int[numArr.length];
        for (int i2 = 0; i2 < numArr.length; i2++) {
            Integer num = numArr[i2];
            iArr[i2] = num == null ? i : num.intValue();
        }
        return iArr;
    }

    public static Integer[] toObject(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        if (iArr.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        Integer[] numArr = new Integer[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            numArr[i] = Integer.valueOf(iArr[i]);
        }
        return numArr;
    }

    public static short[] toPrimitive(Short[] shArr) {
        if (shArr == null) {
            return null;
        }
        if (shArr.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] sArr = new short[shArr.length];
        for (int i = 0; i < shArr.length; i++) {
            sArr[i] = shArr[i].shortValue();
        }
        return sArr;
    }

    public static short[] toPrimitive(Short[] shArr, short s) {
        if (shArr == null) {
            return null;
        }
        if (shArr.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] sArr = new short[shArr.length];
        for (int i = 0; i < shArr.length; i++) {
            Short sh = shArr[i];
            sArr[i] = sh == null ? s : sh.shortValue();
        }
        return sArr;
    }

    public static Short[] toObject(short[] sArr) {
        if (sArr == null) {
            return null;
        }
        if (sArr.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        Short[] shArr = new Short[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            shArr[i] = Short.valueOf(sArr[i]);
        }
        return shArr;
    }

    public static byte[] toPrimitive(Byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        if (bArr.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[i].byteValue();
        }
        return bArr2;
    }

    public static byte[] toPrimitive(Byte[] bArr, byte b) {
        if (bArr == null) {
            return null;
        }
        if (bArr.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            Byte b2 = bArr[i];
            bArr2[i] = b2 == null ? b : b2.byteValue();
        }
        return bArr2;
    }

    public static Byte[] toObject(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        if (bArr.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        Byte[] bArr2 = new Byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = Byte.valueOf(bArr[i]);
        }
        return bArr2;
    }

    public static double[] toPrimitive(Double[] dArr) {
        if (dArr == null) {
            return null;
        }
        if (dArr.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] dArr2 = new double[dArr.length];
        for (int i = 0; i < dArr.length; i++) {
            dArr2[i] = dArr[i].doubleValue();
        }
        return dArr2;
    }

    public static double[] toPrimitive(Double[] dArr, double d) {
        if (dArr == null) {
            return null;
        }
        if (dArr.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] dArr2 = new double[dArr.length];
        for (int i = 0; i < dArr.length; i++) {
            Double d2 = dArr[i];
            dArr2[i] = d2 == null ? d : d2.doubleValue();
        }
        return dArr2;
    }

    public static Double[] toObject(double[] dArr) {
        if (dArr == null) {
            return null;
        }
        if (dArr.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        Double[] dArr2 = new Double[dArr.length];
        for (int i = 0; i < dArr.length; i++) {
            dArr2[i] = Double.valueOf(dArr[i]);
        }
        return dArr2;
    }

    public static float[] toPrimitive(Float[] fArr) {
        if (fArr == null) {
            return null;
        }
        if (fArr.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] fArr2 = new float[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            fArr2[i] = fArr[i].floatValue();
        }
        return fArr2;
    }

    public static float[] toPrimitive(Float[] fArr, float f) {
        if (fArr == null) {
            return null;
        }
        if (fArr.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] fArr2 = new float[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            Float f2 = fArr[i];
            fArr2[i] = f2 == null ? f : f2.floatValue();
        }
        return fArr2;
    }

    public static Float[] toObject(float[] fArr) {
        if (fArr == null) {
            return null;
        }
        if (fArr.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        Float[] fArr2 = new Float[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            fArr2[i] = Float.valueOf(fArr[i]);
        }
        return fArr2;
    }

    public static boolean[] toPrimitive(Boolean[] boolArr) {
        if (boolArr == null) {
            return null;
        }
        if (boolArr.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] zArr = new boolean[boolArr.length];
        for (int i = 0; i < boolArr.length; i++) {
            zArr[i] = boolArr[i].booleanValue();
        }
        return zArr;
    }

    public static boolean[] toPrimitive(Boolean[] boolArr, boolean z) {
        if (boolArr == null) {
            return null;
        }
        if (boolArr.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] zArr = new boolean[boolArr.length];
        for (int i = 0; i < boolArr.length; i++) {
            Boolean bool = boolArr[i];
            zArr[i] = bool == null ? z : bool.booleanValue();
        }
        return zArr;
    }

    public static Boolean[] toObject(boolean[] zArr) {
        if (zArr == null) {
            return null;
        }
        if (zArr.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        Boolean[] boolArr = new Boolean[zArr.length];
        for (int i = 0; i < zArr.length; i++) {
            boolArr[i] = zArr[i] ? Boolean.TRUE : Boolean.FALSE;
        }
        return boolArr;
    }

    public static boolean isEmpty(Object[] objArr) {
        return objArr == null || objArr.length == 0;
    }

    public static boolean isEmpty(long[] jArr) {
        return jArr == null || jArr.length == 0;
    }

    public static boolean isEmpty(int[] iArr) {
        return iArr == null || iArr.length == 0;
    }

    public static boolean isEmpty(short[] sArr) {
        return sArr == null || sArr.length == 0;
    }

    public static boolean isEmpty(char[] cArr) {
        return cArr == null || cArr.length == 0;
    }

    public static boolean isEmpty(byte[] bArr) {
        return bArr == null || bArr.length == 0;
    }

    public static boolean isEmpty(double[] dArr) {
        return dArr == null || dArr.length == 0;
    }

    public static boolean isEmpty(float[] fArr) {
        return fArr == null || fArr.length == 0;
    }

    public static boolean isEmpty(boolean[] zArr) {
        return zArr == null || zArr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] tArr) {
        return (tArr == null || tArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(long[] jArr) {
        return (jArr == null || jArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(int[] iArr) {
        return (iArr == null || iArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(short[] sArr) {
        return (sArr == null || sArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(char[] cArr) {
        return (cArr == null || cArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(byte[] bArr) {
        return (bArr == null || bArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(double[] dArr) {
        return (dArr == null || dArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(float[] fArr) {
        return (fArr == null || fArr.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(boolean[] zArr) {
        return (zArr == null || zArr.length == 0) ? false : true;
    }

    public static <T> T[] addAll(T[] tArr, T... tArr2) {
        if (tArr == null) {
            return clone((Object[]) tArr2);
        }
        if (tArr2 == null) {
            return clone((Object[]) tArr);
        }
        Class componentType = tArr.getClass().getComponentType();
        Object[] objArr = (Object[]) Array.newInstance(componentType, tArr.length + tArr2.length);
        System.arraycopy(tArr, 0, objArr, 0, tArr.length);
        try {
            System.arraycopy(tArr2, 0, objArr, tArr.length, tArr2.length);
            return objArr;
        } catch (Throwable e) {
            Class componentType2 = tArr2.getClass().getComponentType();
            if (componentType.isAssignableFrom(componentType2)) {
                throw e;
            }
            throw new IllegalArgumentException("Cannot store " + componentType2.getName() + " in an array of " + componentType.getName(), e);
        }
    }

    public static boolean[] addAll(boolean[] zArr, boolean... zArr2) {
        if (zArr == null) {
            return clone(zArr2);
        }
        if (zArr2 == null) {
            return clone(zArr);
        }
        boolean[] zArr3 = new boolean[(zArr.length + zArr2.length)];
        System.arraycopy(zArr, 0, zArr3, 0, zArr.length);
        System.arraycopy(zArr2, 0, zArr3, zArr.length, zArr2.length);
        return zArr3;
    }

    public static char[] addAll(char[] cArr, char... cArr2) {
        if (cArr == null) {
            return clone(cArr2);
        }
        if (cArr2 == null) {
            return clone(cArr);
        }
        char[] cArr3 = new char[(cArr.length + cArr2.length)];
        System.arraycopy(cArr, 0, cArr3, 0, cArr.length);
        System.arraycopy(cArr2, 0, cArr3, cArr.length, cArr2.length);
        return cArr3;
    }

    public static byte[] addAll(byte[] bArr, byte... bArr2) {
        if (bArr == null) {
            return clone(bArr2);
        }
        if (bArr2 == null) {
            return clone(bArr);
        }
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static short[] addAll(short[] sArr, short... sArr2) {
        if (sArr == null) {
            return clone(sArr2);
        }
        if (sArr2 == null) {
            return clone(sArr);
        }
        short[] sArr3 = new short[(sArr.length + sArr2.length)];
        System.arraycopy(sArr, 0, sArr3, 0, sArr.length);
        System.arraycopy(sArr2, 0, sArr3, sArr.length, sArr2.length);
        return sArr3;
    }

    public static int[] addAll(int[] iArr, int... iArr2) {
        if (iArr == null) {
            return clone(iArr2);
        }
        if (iArr2 == null) {
            return clone(iArr);
        }
        int[] iArr3 = new int[(iArr.length + iArr2.length)];
        System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
        System.arraycopy(iArr2, 0, iArr3, iArr.length, iArr2.length);
        return iArr3;
    }

    public static long[] addAll(long[] jArr, long... jArr2) {
        if (jArr == null) {
            return clone(jArr2);
        }
        if (jArr2 == null) {
            return clone(jArr);
        }
        long[] jArr3 = new long[(jArr.length + jArr2.length)];
        System.arraycopy(jArr, 0, jArr3, 0, jArr.length);
        System.arraycopy(jArr2, 0, jArr3, jArr.length, jArr2.length);
        return jArr3;
    }

    public static float[] addAll(float[] fArr, float... fArr2) {
        if (fArr == null) {
            return clone(fArr2);
        }
        if (fArr2 == null) {
            return clone(fArr);
        }
        float[] fArr3 = new float[(fArr.length + fArr2.length)];
        System.arraycopy(fArr, 0, fArr3, 0, fArr.length);
        System.arraycopy(fArr2, 0, fArr3, fArr.length, fArr2.length);
        return fArr3;
    }

    public static double[] addAll(double[] dArr, double... dArr2) {
        if (dArr == null) {
            return clone(dArr2);
        }
        if (dArr2 == null) {
            return clone(dArr);
        }
        double[] dArr3 = new double[(dArr.length + dArr2.length)];
        System.arraycopy(dArr, 0, dArr3, 0, dArr.length);
        System.arraycopy(dArr2, 0, dArr3, dArr.length, dArr2.length);
        return dArr3;
    }

    public static <T> T[] add(T[] tArr, T t) {
        Class cls;
        if (tArr != null) {
            cls = tArr.getClass();
        } else if (t != null) {
            cls = t.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        Object[] objArr = (Object[]) a((Object) tArr, cls);
        objArr[objArr.length - 1] = t;
        return objArr;
    }

    public static boolean[] add(boolean[] zArr, boolean z) {
        boolean[] zArr2 = (boolean[]) a((Object) zArr, Boolean.TYPE);
        zArr2[zArr2.length - 1] = z;
        return zArr2;
    }

    public static byte[] add(byte[] bArr, byte b) {
        byte[] bArr2 = (byte[]) a((Object) bArr, Byte.TYPE);
        bArr2[bArr2.length - 1] = b;
        return bArr2;
    }

    public static char[] add(char[] cArr, char c) {
        char[] cArr2 = (char[]) a((Object) cArr, Character.TYPE);
        cArr2[cArr2.length - 1] = c;
        return cArr2;
    }

    public static double[] add(double[] dArr, double d) {
        double[] dArr2 = (double[]) a((Object) dArr, Double.TYPE);
        dArr2[dArr2.length - 1] = d;
        return dArr2;
    }

    public static float[] add(float[] fArr, float f) {
        float[] fArr2 = (float[]) a((Object) fArr, Float.TYPE);
        fArr2[fArr2.length - 1] = f;
        return fArr2;
    }

    public static int[] add(int[] iArr, int i) {
        int[] iArr2 = (int[]) a((Object) iArr, Integer.TYPE);
        iArr2[iArr2.length - 1] = i;
        return iArr2;
    }

    public static long[] add(long[] jArr, long j) {
        long[] jArr2 = (long[]) a((Object) jArr, Long.TYPE);
        jArr2[jArr2.length - 1] = j;
        return jArr2;
    }

    public static short[] add(short[] sArr, short s) {
        short[] sArr2 = (short[]) a((Object) sArr, Short.TYPE);
        sArr2[sArr2.length - 1] = s;
        return sArr2;
    }

    private static Object a(Object obj, Class<?> cls) {
        if (obj == null) {
            return Array.newInstance(cls, 1);
        }
        int length = Array.getLength(obj);
        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), length + 1);
        System.arraycopy(obj, 0, newInstance, 0, length);
        return newInstance;
    }

    public static <T> T[] add(T[] tArr, int i, T t) {
        Class componentType;
        if (tArr != null) {
            componentType = tArr.getClass().getComponentType();
        } else if (t != null) {
            componentType = t.getClass();
        } else {
            throw new IllegalArgumentException("Array and element cannot both be null");
        }
        return (Object[]) a(tArr, i, t, componentType);
    }

    public static boolean[] add(boolean[] zArr, int i, boolean z) {
        return (boolean[]) a(zArr, i, Boolean.valueOf(z), Boolean.TYPE);
    }

    public static char[] add(char[] cArr, int i, char c) {
        return (char[]) a(cArr, i, Character.valueOf(c), Character.TYPE);
    }

    public static byte[] add(byte[] bArr, int i, byte b) {
        return (byte[]) a(bArr, i, Byte.valueOf(b), Byte.TYPE);
    }

    public static short[] add(short[] sArr, int i, short s) {
        return (short[]) a(sArr, i, Short.valueOf(s), Short.TYPE);
    }

    public static int[] add(int[] iArr, int i, int i2) {
        return (int[]) a(iArr, i, Integer.valueOf(i2), Integer.TYPE);
    }

    public static long[] add(long[] jArr, int i, long j) {
        return (long[]) a(jArr, i, Long.valueOf(j), Long.TYPE);
    }

    public static float[] add(float[] fArr, int i, float f) {
        return (float[]) a(fArr, i, Float.valueOf(f), Float.TYPE);
    }

    public static double[] add(double[] dArr, int i, double d) {
        return (double[]) a(dArr, i, Double.valueOf(d), Double.TYPE);
    }

    private static Object a(Object obj, int i, Object obj2, Class<?> cls) {
        Object newInstance;
        if (obj != null) {
            int length = Array.getLength(obj);
            if (i > length || i < 0) {
                throw new IndexOutOfBoundsException("Index: " + i + ", Length: " + length);
            }
            newInstance = Array.newInstance(cls, length + 1);
            System.arraycopy(obj, 0, newInstance, 0, i);
            Array.set(newInstance, i, obj2);
            if (i >= length) {
                return newInstance;
            }
            System.arraycopy(obj, i, newInstance, i + 1, length - i);
            return newInstance;
        } else if (i != 0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Length: 0");
        } else {
            newInstance = Array.newInstance(cls, 1);
            Array.set(newInstance, 0, obj2);
            return newInstance;
        }
    }

    public static <T> T[] remove(T[] tArr, int i) {
        return (Object[]) a((Object) tArr, i);
    }

    public static <T> T[] removeElement(T[] tArr, Object obj) {
        int indexOf = indexOf((Object[]) tArr, obj);
        if (indexOf == -1) {
            return clone((Object[]) tArr);
        }
        return remove((Object[]) tArr, indexOf);
    }

    public static boolean[] remove(boolean[] zArr, int i) {
        return (boolean[]) a((Object) zArr, i);
    }

    public static boolean[] removeElement(boolean[] zArr, boolean z) {
        int indexOf = indexOf(zArr, z);
        if (indexOf == -1) {
            return clone(zArr);
        }
        return remove(zArr, indexOf);
    }

    public static byte[] remove(byte[] bArr, int i) {
        return (byte[]) a((Object) bArr, i);
    }

    public static byte[] removeElement(byte[] bArr, byte b) {
        int indexOf = indexOf(bArr, b);
        if (indexOf == -1) {
            return clone(bArr);
        }
        return remove(bArr, indexOf);
    }

    public static char[] remove(char[] cArr, int i) {
        return (char[]) a((Object) cArr, i);
    }

    public static char[] removeElement(char[] cArr, char c) {
        int indexOf = indexOf(cArr, c);
        if (indexOf == -1) {
            return clone(cArr);
        }
        return remove(cArr, indexOf);
    }

    public static double[] remove(double[] dArr, int i) {
        return (double[]) a((Object) dArr, i);
    }

    public static double[] removeElement(double[] dArr, double d) {
        int indexOf = indexOf(dArr, d);
        if (indexOf == -1) {
            return clone(dArr);
        }
        return remove(dArr, indexOf);
    }

    public static float[] remove(float[] fArr, int i) {
        return (float[]) a((Object) fArr, i);
    }

    public static float[] removeElement(float[] fArr, float f) {
        int indexOf = indexOf(fArr, f);
        if (indexOf == -1) {
            return clone(fArr);
        }
        return remove(fArr, indexOf);
    }

    public static int[] remove(int[] iArr, int i) {
        return (int[]) a((Object) iArr, i);
    }

    public static int[] removeElement(int[] iArr, int i) {
        int indexOf = indexOf(iArr, i);
        if (indexOf == -1) {
            return clone(iArr);
        }
        return remove(iArr, indexOf);
    }

    public static long[] remove(long[] jArr, int i) {
        return (long[]) a((Object) jArr, i);
    }

    public static long[] removeElement(long[] jArr, long j) {
        int indexOf = indexOf(jArr, j);
        if (indexOf == -1) {
            return clone(jArr);
        }
        return remove(jArr, indexOf);
    }

    public static short[] remove(short[] sArr, int i) {
        return (short[]) a((Object) sArr, i);
    }

    public static short[] removeElement(short[] sArr, short s) {
        int indexOf = indexOf(sArr, s);
        if (indexOf == -1) {
            return clone(sArr);
        }
        return remove(sArr, indexOf);
    }

    private static Object a(Object obj, int i) {
        int length = getLength(obj);
        if (i < 0 || i >= length) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Length: " + length);
        }
        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), length - 1);
        System.arraycopy(obj, 0, newInstance, 0, i);
        if (i < length - 1) {
            System.arraycopy(obj, i + 1, newInstance, i, (length - i) - 1);
        }
        return newInstance;
    }

    public static <T> T[] removeAll(T[] tArr, int... iArr) {
        return (Object[]) a((Object) tArr, clone(iArr));
    }

    public static byte[] removeAll(byte[] bArr, int... iArr) {
        return (byte[]) a((Object) bArr, clone(iArr));
    }

    public static short[] removeAll(short[] sArr, int... iArr) {
        return (short[]) a((Object) sArr, clone(iArr));
    }

    public static int[] removeAll(int[] iArr, int... iArr2) {
        return (int[]) a((Object) iArr, clone(iArr2));
    }

    public static char[] removeAll(char[] cArr, int... iArr) {
        return (char[]) a((Object) cArr, clone(iArr));
    }

    public static long[] removeAll(long[] jArr, int... iArr) {
        return (long[]) a((Object) jArr, clone(iArr));
    }

    public static float[] removeAll(float[] fArr, int... iArr) {
        return (float[]) a((Object) fArr, clone(iArr));
    }

    public static double[] removeAll(double[] dArr, int... iArr) {
        return (double[]) a((Object) dArr, clone(iArr));
    }

    public static boolean[] removeAll(boolean[] zArr, int... iArr) {
        return (boolean[]) a((Object) zArr, clone(iArr));
    }

    private static Object a(Object obj, int... iArr) {
        int i;
        int length = getLength(obj);
        int i2;
        int length2;
        if (isNotEmpty(iArr)) {
            Arrays.sort(iArr);
            i2 = length;
            length2 = iArr.length;
            i = 0;
            while (true) {
                int i3 = length2 - 1;
                if (i3 < 0) {
                    break;
                }
                length2 = iArr[i3];
                if (length2 >= 0 && length2 < length) {
                    if (length2 >= i2) {
                        length2 = i3;
                    } else {
                        i++;
                        i2 = length2;
                        length2 = i3;
                    }
                }
            }
            throw new IndexOutOfBoundsException("Index: " + length2 + ", Length: " + length);
        }
        i = 0;
        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), length - i);
        if (i < length) {
            i = length - i;
            i2 = iArr.length - 1;
            while (i2 >= 0) {
                length2 = iArr[i2];
                if (length - length2 > 1) {
                    length = (length - length2) - 1;
                    i -= length;
                    System.arraycopy(obj, length2 + 1, newInstance, i, length);
                }
                i2--;
                length = length2;
            }
            if (length > 0) {
                System.arraycopy(obj, 0, newInstance, 0, length);
            }
        }
        return newInstance;
    }

    public static <T> ArrayList<T> asList(T... tArr) {
        if (tArr == null) {
            return new ArrayList(0);
        }
        return new ArrayList(Arrays.asList(tArr));
    }

    public static <T> List<T> asReadOnlyList(T... tArr) {
        return new ReadOnlyArrayList(tArr);
    }

    public static boolean[] copyOf(boolean[] zArr, int i) {
        Object obj = new boolean[i];
        System.arraycopy(zArr, 0, obj, 0, Math.min(zArr.length, i));
        return obj;
    }

    public static byte[] copyOf(byte[] bArr, int i) {
        Object obj = new byte[i];
        System.arraycopy(bArr, 0, obj, 0, Math.min(bArr.length, i));
        return obj;
    }

    public static char[] copyOf(char[] cArr, int i) {
        Object obj = new char[i];
        System.arraycopy(cArr, 0, obj, 0, Math.min(cArr.length, i));
        return obj;
    }

    public static double[] copyOf(double[] dArr, int i) {
        Object obj = new double[i];
        System.arraycopy(dArr, 0, obj, 0, Math.min(dArr.length, i));
        return obj;
    }

    public static float[] copyOf(float[] fArr, int i) {
        Object obj = new float[i];
        System.arraycopy(fArr, 0, obj, 0, Math.min(fArr.length, i));
        return obj;
    }

    public static int[] copyOf(int[] iArr, int i) {
        Object obj = new int[i];
        System.arraycopy(iArr, 0, obj, 0, Math.min(iArr.length, i));
        return obj;
    }

    public static long[] copyOf(long[] jArr, int i) {
        Object obj = new long[i];
        System.arraycopy(jArr, 0, obj, 0, Math.min(jArr.length, i));
        return obj;
    }

    public static short[] copyOf(short[] sArr, int i) {
        Object obj = new short[i];
        System.arraycopy(sArr, 0, obj, 0, Math.min(sArr.length, i));
        return obj;
    }

    public static <T> T[] copyOf(T[] tArr, int i) {
        return copyOf(tArr, i, tArr.getClass());
    }

    public static <T, U> T[] copyOf(U[] uArr, int i, Class<? extends T[]> cls) {
        Object obj;
        if (cls == Object[].class) {
            obj = new Object[i];
        } else {
            Object[] objArr = (Object[]) Array.newInstance(cls.getComponentType(), i);
        }
        System.arraycopy(uArr, 0, obj, 0, Math.min(uArr.length, i));
        return obj;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    public static <T> T[] join(T[] tArr, T[] tArr2) {
        if (tArr == null) {
            return tArr2;
        }
        if (tArr2 == null) {
            return tArr;
        }
        T[] tArr3 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), tArr.length + tArr2.length);
        System.arraycopy(tArr, 0, tArr3, 0, tArr.length);
        System.arraycopy(tArr2, 0, tArr3, tArr.length, tArr2.length);
        return tArr3;
    }
}
