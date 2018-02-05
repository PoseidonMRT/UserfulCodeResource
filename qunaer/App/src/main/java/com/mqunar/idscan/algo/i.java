package com.mqunar.idscan.algo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import com.mqunar.idscan.image.ImageNativeLibrary;
import com.mqunar.idscan.image.a;
import com.mqunar.idscan.image.b;
import com.mqunar.tools.log.QLog;
import java.nio.IntBuffer;
import java.util.List;
import org.apache.http.HttpStatus;

public final class i {
    private static i a;
    private a b = new a(f.g);
    private e c = null;
    private b d;
    private c e;
    private d f;
    private int[] g;
    private IntBuffer h;
    private Bitmap i;
    private Paint j;
    private int[] k;
    private int[] l;
    private Bitmap m;

    public i() {
        String str = f.e;
        this.d = new b();
        str = f.f;
        this.e = new c();
        this.f = new d();
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
        str = f.d;
        this.c = new e(this.e);
    }

    public static String a(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6) {
        Throwable e;
        List[] listArr;
        Throwable th;
        if (!CNN.a || !ImageNativeLibrary.a) {
            return "-200";
        }
        List[] listArr2 = null;
        Bitmap bitmap;
        try {
            if (a == null) {
                a = new i();
            }
            i iVar = a;
            if (iVar.g == null) {
                iVar.g = new int[(i5 * i6)];
            }
            if (iVar.h == null) {
                iVar.h = IntBuffer.allocate(i5 * i6);
            }
            if (iVar.i == null) {
                iVar.i = Bitmap.createBitmap(i5, i6, Config.ARGB_8888);
            }
            ImageNativeLibrary.RegionYUVtoRBGA(bArr, i, i2, i3, i4, i5, i6, iVar.g);
            int[] iArr = iVar.g;
            i iVar2 = a;
            if (iVar2.m == null) {
                iVar2.m = Bitmap.createBitmap(i5, i6, Config.ARGB_8888);
            }
            iVar2.m.copyPixelsFromBuffer(IntBuffer.wrap(iArr));
            bitmap = iVar2.m;
            iVar2 = a;
            if (iVar2.j == null) {
                Paint paint = new Paint();
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0.0f);
                paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }
            new Canvas(iVar2.i).drawBitmap(bitmap, 0.0f, 0.0f, iVar2.j);
            iVar2.i.copyPixelsToBuffer(IntBuffer.wrap(iVar2.g));
            iArr = iVar2.g;
            iVar2 = a;
            if (iVar2.l == null) {
                iVar2.l = new int[(i5 * i6)];
            }
            if (iVar2.k == null) {
                iVar2.k = new int[(i5 * i6)];
            }
            ImageNativeLibrary.AdaptiveThreshold(iArr, i5, i6, iVar2.l, iVar2.k);
            int[] iArr2 = iVar2.k;
            int[] a = a.a(iArr2, i5, i6);
            if (a == null || a[1] - a[0] < 20 || a[3] - a[2] < HttpStatus.SC_BAD_REQUEST) {
                return null;
            }
            List[] a2 = b.a(iArr2, iArr, a, i5);
            if (a2 != null) {
                try {
                    if (a2.length == 2) {
                        Object obj;
                        String a3 = h.a(a(a2));
                        if (f.h.contains(Character.valueOf(a3.charAt(0)))) {
                            if (a3.charAt(0) == 'P' && a3.charAt(1) == 'O' && a3.charAt(2) == 'C' && a3.charAt(3) == 'H' && a3.charAt(4) == 'N') {
                                char charAt = a3.charAt(44);
                                if (!(charAt == 'E' || charAt == 'G')) {
                                    obj = null;
                                }
                            }
                            obj = 1;
                        } else {
                            obj = null;
                        }
                        if (obj == null) {
                            if (a2 != null) {
                                for (List<Bitmap> it : a2) {
                                    for (Bitmap bitmap2 : it) {
                                        bitmap2.recycle();
                                    }
                                }
                            }
                            return null;
                        } else if (!b.a(a3)) {
                            if (a2 != null) {
                                for (List<Bitmap> it2 : a2) {
                                    for (Bitmap bitmap22 : it2) {
                                        bitmap22.recycle();
                                    }
                                }
                            }
                            return null;
                        } else if (!a.b.a(a3)) {
                            if (a2 != null) {
                                for (List<Bitmap> it3 : a2) {
                                    for (Bitmap bitmap222 : it3) {
                                        bitmap222.recycle();
                                    }
                                }
                            }
                            return null;
                        } else if (!a.c.a(a3)) {
                            if (a2 != null) {
                                for (List<Bitmap> it4 : a2) {
                                    for (Bitmap bitmap2222 : it4) {
                                        bitmap2222.recycle();
                                    }
                                }
                            }
                            return null;
                        } else if (!a.e.a(a3)) {
                            if (a2 != null) {
                                for (List<Bitmap> it5 : a2) {
                                    for (Bitmap bitmap22222 : it5) {
                                        bitmap22222.recycle();
                                    }
                                }
                            }
                            return null;
                        } else if (a3.charAt(64) == 'M' || a3.charAt(64) == 'F') {
                            i iVar3 = a;
                            iVar3.g = null;
                            iVar3.h.clear();
                            iVar3.h = null;
                            iVar3.i.recycle();
                            iVar3.i = null;
                            iVar3.j = null;
                            iVar3.l = null;
                            iVar3.k = null;
                            iVar3.m.recycle();
                            iVar3.m = null;
                            if (a2 != null) {
                                for (List<Bitmap> it6 : a2) {
                                    for (Bitmap bitmap222222 : it6) {
                                        bitmap222222.recycle();
                                    }
                                }
                            }
                            return a3;
                        } else {
                            if (a2 != null) {
                                for (List<Bitmap> it7 : a2) {
                                    for (Bitmap bitmap2222222 : it7) {
                                        bitmap2222222.recycle();
                                    }
                                }
                            }
                            return null;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    listArr = a2;
                    try {
                        QLog.e(e);
                        if (listArr != null) {
                            for (List<Bitmap> it8 : listArr) {
                                for (Bitmap bitmap22222222 : it8) {
                                    bitmap22222222.recycle();
                                }
                            }
                        }
                        return null;
                    } catch (Throwable e3) {
                        listArr2 = listArr;
                        th = e3;
                        if (listArr2 != null) {
                            for (List<Bitmap> it9 : listArr2) {
                                for (Bitmap bitmap222222222 : it9) {
                                    bitmap222222222.recycle();
                                }
                            }
                        }
                        throw th;
                    }
                } catch (Throwable e32) {
                    th = e32;
                    listArr2 = a2;
                    if (listArr2 != null) {
                        while (r4 < r5) {
                            while (r6.hasNext()) {
                                bitmap222222222.recycle();
                            }
                        }
                    }
                    throw th;
                }
            }
            if (a2 != null) {
                for (List<Bitmap> it10 : a2) {
                    for (Bitmap bitmap2222222222 : it10) {
                        bitmap2222222222.recycle();
                    }
                }
            }
            return null;
        } catch (Exception e4) {
            e32 = e4;
            listArr = null;
            QLog.e(e32);
            if (listArr != null) {
                while (r4 < r5) {
                    while (r6.hasNext()) {
                        bitmap2222222222.recycle();
                    }
                }
            }
            return null;
        } catch (Throwable e322) {
            th = e322;
            if (listArr2 != null) {
                while (r4 < r5) {
                    while (r6.hasNext()) {
                        bitmap2222222222.recycle();
                    }
                }
            }
            throw th;
        }
    }

    private static String a(List[] listArr) {
        StringBuilder stringBuilder = new StringBuilder();
        int[] iArr = new int[784];
        int i = 1;
        int length = listArr.length;
        int i2 = 0;
        Object obj = null;
        while (i2 < length) {
            Object obj2 = obj;
            int i3 = i;
            for (Bitmap bitmap : listArr[i2]) {
                int i4;
                if (obj2 != null) {
                    if (i3 == 6) {
                        for (i4 = 0; i4 < 39; i4++) {
                            stringBuilder.append("<");
                        }
                        i3 = 45;
                        i2++;
                        i = i3;
                        obj = obj2;
                    }
                } else if (i3 < 45 && stringBuilder.indexOf("<<") != stringBuilder.lastIndexOf("<<")) {
                    i = stringBuilder.length();
                    for (i4 = 0; i4 < 44 - i; i4++) {
                        stringBuilder.append("<");
                    }
                    i3 = 45;
                    i2++;
                    i = i3;
                    obj = obj2;
                }
                if (i3 < 73 || i3 > 88) {
                    bitmap.getPixels(iArr, 0, 28, 0, 0, 28, 28);
                    i = 0;
                    i4 = 0;
                    while (i4 < 784) {
                        int i5 = i + 1;
                        iArr[i] = Color.green(iArr[i4]);
                        i4++;
                        i = i5;
                    }
                    String str = (String) f.g.get(CNN.predict(iArr));
                    if (i3 == 1 && (str == "W" || str == "D" || str == "T")) {
                        obj2 = 1;
                    }
                    stringBuilder.append(str);
                    obj = obj2;
                } else {
                    stringBuilder.append("<");
                    obj = obj2;
                }
                obj2 = obj;
                i3++;
            }
            i2++;
            i = i3;
            obj = obj2;
        }
        return stringBuilder.toString();
    }
}
