package com.mqunar.idscan.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Matrix;
import com.mqunar.tools.log.QLog;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class b {
    private static List a(List list) {
        List arrayList = new ArrayList();
        int i;
        for (int i2 = 0; i2 <= list.size() - 1; i2 = i) {
            int i3 = i2;
            while (i3 <= list.size() - 1 && ((Integer) list.get(i3)).intValue() < 10) {
                i3++;
            }
            i = i3 + 1;
            while (i <= list.size() - 1 && ((Integer) list.get(i)).intValue() >= 10) {
                i++;
            }
            if (i - i3 >= 10) {
                arrayList.add(new int[]{i3, i - 1});
            }
        }
        return arrayList;
    }

    private static List a(int[] iArr, int[] iArr2, List list, int[] iArr3, int[] iArr4, int i) {
        int[] c;
        int[] iArr5;
        int[] iArr6;
        Throwable e;
        int i2;
        Bitmap createBitmap;
        Matrix matrix;
        float min;
        Bitmap createBitmap2;
        int[] iArr7;
        Bitmap createBitmap3;
        ArrayList arrayList = new ArrayList(list.size());
        int i3 = 0;
        int i4 = 0;
        int[] iArr8 = null;
        int[] iArr9 = null;
        for (int[] iArr10 : list) {
            int i5;
            int i6;
            int i7;
            try {
                c = c(a(iArr, iArr10, iArr3, iArr4, i));
                try {
                    if (c.length == 0) {
                        return arrayList;
                    }
                    i3 = (iArr10[1] - iArr10[0]) + 1;
                    i4 = (c[1] - c[0]) + 1;
                    iArr8 = new int[(i3 * i4)];
                    try {
                        Arrays.fill(iArr8, -7829368);
                        iArr5 = iArr8;
                        iArr6 = c;
                    } catch (Exception e2) {
                        e = e2;
                        QLog.e(e);
                        iArr5 = iArr8;
                        iArr6 = c;
                        i5 = 0;
                        for (i2 = (iArr4[0] + iArr3[0]) + iArr6[0]; i2 < ((iArr4[0] + iArr3[0]) + iArr6[1]) + 1; i2++) {
                            i6 = iArr4[2] + iArr10[0];
                            while (i6 < (iArr4[2] + iArr10[1]) + 1) {
                                if ((i2 * i) + i6 >= iArr2.length) {
                                    i7 = i5;
                                } else {
                                    i7 = i5 + 1;
                                    iArr5[i5] = iArr2[(i2 * i) + i6];
                                }
                                i6++;
                                i5 = i7;
                            }
                        }
                        createBitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
                        createBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iArr5));
                        matrix = new Matrix();
                        min = Math.min(28.0f / ((float) i3), 28.0f / ((float) i4));
                        matrix.postScale(min, min);
                        createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, i3, i4, matrix, true);
                        iArr7 = new int[(createBitmap2.getWidth() * createBitmap2.getHeight())];
                        createBitmap2.getPixels(iArr7, 0, createBitmap2.getWidth(), 0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                        createBitmap3 = Bitmap.createBitmap(28, 28, Config.ARGB_8888);
                        createBitmap3.setPixels(iArr7, 0, createBitmap2.getWidth(), (28 - createBitmap2.getWidth()) / 2, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                        arrayList.add(createBitmap3);
                        createBitmap.recycle();
                        createBitmap2.recycle();
                        iArr9 = iArr5;
                        iArr8 = iArr6;
                    }
                    i5 = 0;
                    try {
                        for (i2 = (iArr4[0] + iArr3[0]) + iArr6[0]; i2 < ((iArr4[0] + iArr3[0]) + iArr6[1]) + 1; i2++) {
                            i6 = iArr4[2] + iArr10[0];
                            while (i6 < (iArr4[2] + iArr10[1]) + 1) {
                                if ((i2 * i) + i6 >= iArr2.length) {
                                    i7 = i5 + 1;
                                    iArr5[i5] = iArr2[(i2 * i) + i6];
                                } else {
                                    i7 = i5;
                                }
                                i6++;
                                i5 = i7;
                            }
                        }
                    } catch (Throwable e3) {
                        QLog.e(e3);
                    }
                    try {
                        createBitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
                        createBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iArr5));
                        matrix = new Matrix();
                        min = Math.min(28.0f / ((float) i3), 28.0f / ((float) i4));
                        matrix.postScale(min, min);
                        createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, i3, i4, matrix, true);
                        iArr7 = new int[(createBitmap2.getWidth() * createBitmap2.getHeight())];
                        createBitmap2.getPixels(iArr7, 0, createBitmap2.getWidth(), 0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                        createBitmap3 = Bitmap.createBitmap(28, 28, Config.ARGB_8888);
                        createBitmap3.setPixels(iArr7, 0, createBitmap2.getWidth(), (28 - createBitmap2.getWidth()) / 2, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                        arrayList.add(createBitmap3);
                        createBitmap.recycle();
                        createBitmap2.recycle();
                        iArr9 = iArr5;
                        iArr8 = iArr6;
                    } catch (Throwable e32) {
                        QLog.e(e32);
                    }
                } catch (Throwable e4) {
                    r23 = e4;
                    iArr8 = iArr9;
                    e = r23;
                    QLog.e(e);
                    iArr5 = iArr8;
                    iArr6 = c;
                    i5 = 0;
                    for (i2 = (iArr4[0] + iArr3[0]) + iArr6[0]; i2 < ((iArr4[0] + iArr3[0]) + iArr6[1]) + 1; i2++) {
                        i6 = iArr4[2] + iArr10[0];
                        while (i6 < (iArr4[2] + iArr10[1]) + 1) {
                            if ((i2 * i) + i6 >= iArr2.length) {
                                i7 = i5;
                            } else {
                                i7 = i5 + 1;
                                iArr5[i5] = iArr2[(i2 * i) + i6];
                            }
                            i6++;
                            i5 = i7;
                        }
                    }
                    createBitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
                    createBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iArr5));
                    matrix = new Matrix();
                    min = Math.min(28.0f / ((float) i3), 28.0f / ((float) i4));
                    matrix.postScale(min, min);
                    createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, i3, i4, matrix, true);
                    iArr7 = new int[(createBitmap2.getWidth() * createBitmap2.getHeight())];
                    createBitmap2.getPixels(iArr7, 0, createBitmap2.getWidth(), 0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                    createBitmap3 = Bitmap.createBitmap(28, 28, Config.ARGB_8888);
                    createBitmap3.setPixels(iArr7, 0, createBitmap2.getWidth(), (28 - createBitmap2.getWidth()) / 2, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                    arrayList.add(createBitmap3);
                    createBitmap.recycle();
                    createBitmap2.recycle();
                    iArr9 = iArr5;
                    iArr8 = iArr6;
                }
            } catch (Throwable e5) {
                Throwable th;
                th = e5;
                c = iArr8;
                iArr8 = iArr9;
                e = th;
                QLog.e(e);
                iArr5 = iArr8;
                iArr6 = c;
                i5 = 0;
                for (i2 = (iArr4[0] + iArr3[0]) + iArr6[0]; i2 < ((iArr4[0] + iArr3[0]) + iArr6[1]) + 1; i2++) {
                    i6 = iArr4[2] + iArr10[0];
                    while (i6 < (iArr4[2] + iArr10[1]) + 1) {
                        if ((i2 * i) + i6 >= iArr2.length) {
                            i7 = i5 + 1;
                            iArr5[i5] = iArr2[(i2 * i) + i6];
                        } else {
                            i7 = i5;
                        }
                        i6++;
                        i5 = i7;
                    }
                }
                createBitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
                createBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iArr5));
                matrix = new Matrix();
                min = Math.min(28.0f / ((float) i3), 28.0f / ((float) i4));
                matrix.postScale(min, min);
                createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, i3, i4, matrix, true);
                iArr7 = new int[(createBitmap2.getWidth() * createBitmap2.getHeight())];
                createBitmap2.getPixels(iArr7, 0, createBitmap2.getWidth(), 0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                createBitmap3 = Bitmap.createBitmap(28, 28, Config.ARGB_8888);
                createBitmap3.setPixels(iArr7, 0, createBitmap2.getWidth(), (28 - createBitmap2.getWidth()) / 2, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                arrayList.add(createBitmap3);
                createBitmap.recycle();
                createBitmap2.recycle();
                iArr9 = iArr5;
                iArr8 = iArr6;
            }
        }
        return arrayList;
    }

    private static List a(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i) {
        List arrayList = new ArrayList();
        for (int i2 = iArr4[0] + iArr3[0]; i2 < (iArr4[0] + iArr3[1]) + 1; i2++) {
            int i3 = 0;
            for (int i4 = iArr4[2] + iArr2[0]; i4 < (iArr4[2] + iArr2[1]) + 1; i4++) {
                i3 += Color.green(iArr[(i2 * i) + i4]) == 0 ? 1 : 0;
            }
            arrayList.add(Integer.valueOf(i3));
        }
        return arrayList;
    }

    public static List[] a(int[] iArr, int[] iArr2, int[] iArr3, int i) {
        List list;
        List arrayList = new ArrayList();
        loop0:
        for (int i2 = iArr3[0]; i2 < iArr3[1] + 1; i2++) {
            int i3 = 0;
            for (int i4 = iArr3[2]; i4 < iArr3[3] + 1; i4++) {
                int i5 = (i2 * i) + i4;
                if (i5 < 0 || i5 >= iArr.length) {
                    list = null;
                    break loop0;
                }
                i3 += Color.green(iArr[(i2 * i) + i4]) == 0 ? 1 : 0;
            }
            arrayList.add(Integer.valueOf(i3));
        }
        list = arrayList;
        if (list == null) {
            return null;
        }
        List a = a(list);
        if (a.size() != 2) {
            return null;
        }
        List b = b(iArr, (int[]) a.get(0), iArr3, i);
        list = b(iArr, (int[]) a.get(1), iArr3, i);
        List b2 = b(b);
        List b3 = b(list);
        if (b2.size() != 44 || b3.size() != 44) {
            return null;
        }
        List a2 = a(iArr, iArr2, b2, (int[]) a.get(0), iArr3, i);
        b = a(iArr, iArr2, b3, (int[]) a.get(1), iArr3, i);
        return new List[]{a2, b};
    }

    private static List b(List list) {
        List arrayList = new ArrayList();
        int i;
        for (int i2 = 0; i2 <= list.size() - 1; i2 = i) {
            int i3 = i2;
            while (i3 <= list.size() - 1 && ((Integer) list.get(i3)).intValue() <= 1) {
                i3++;
            }
            i = i3 + 1;
            while (i <= list.size() - 1 && ((Integer) list.get(i)).intValue() > 1) {
                i++;
            }
            if (i - i3 >= 4 && arrayList.size() < 44) {
                arrayList.add(new int[]{i3 - 2, i + 1});
            }
        }
        return arrayList;
    }

    private static List b(int[] iArr, int[] iArr2, int[] iArr3, int i) {
        List arrayList = new ArrayList();
        int i2 = iArr3[2];
        while (i2 < iArr3[3] + 1) {
            int i3;
            int i4 = 0;
            for (i3 = iArr3[0] + iArr2[0]; i3 < (iArr3[0] + iArr2[1]) + 1; i3++) {
                i4 += Color.green(iArr[(i3 * i) + i2]) == 0 ? 1 : 0;
            }
            i3 = i2 <= 5 ? 0 : i4;
            if (i2 >= i - 5) {
                i3 = 0;
            }
            arrayList.add(Integer.valueOf(i3));
            i2++;
        }
        return arrayList;
    }

    private static int[] c(List list) {
        int[] iArr = new int[0];
        int i;
        for (int i2 = 0; i2 <= list.size() - 1; i2 = i) {
            int i3 = i2;
            while (i3 <= list.size() - 1 && ((Integer) list.get(i3)).intValue() == 0) {
                i3++;
            }
            i = i3 + 1;
            while (i <= list.size() - 1 && ((Integer) list.get(i)).intValue() != 0) {
                i++;
            }
            if (i - i3 >= 4) {
                return new int[]{i3 - 2, i + 1};
            }
        }
        return iArr;
    }
}
