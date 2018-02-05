package com.mqunar.idscan.image;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public final class a {
    private static List a(int[] iArr, int[] iArr2, int i) {
        List arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = 0;
            for (int i4 = iArr2[0]; i4 <= iArr2[1]; i4++) {
                i3 += Color.green(iArr[(i4 * i) + i2]) == 0 ? 1 : 0;
            }
            arrayList.add(Integer.valueOf(i3));
        }
        return arrayList;
    }

    private static int[] a(List list) {
        int size = list.size() / 2;
        int i = 0;
        int i2 = size;
        while (i2 > 0) {
            int intValue = ((Integer) list.get(i2)).intValue();
            if (i > 20) {
                break;
            }
            i2--;
            i = intValue < 6 ? i + 1 : 0;
        }
        if (i2 != 0) {
            i2 += 20;
        }
        i = size;
        size = 0;
        while (i < list.size()) {
            intValue = ((Integer) list.get(i)).intValue();
            if (size > 20) {
                break;
            }
            i++;
            size = intValue < 6 ? size + 1 : 0;
        }
        if (i != list.size()) {
            i -= 20;
        }
        return new int[]{i2, i};
    }

    private static int[] a(List list, int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = i;
        while (i5 < i2) {
            int intValue = ((Integer) list.get(i5)).intValue();
            if (i4 < intValue) {
                i3 = intValue;
                intValue = i5;
            } else {
                intValue = i3;
                i3 = i4;
            }
            i5++;
            i4 = i3;
            i3 = intValue;
        }
        return new int[]{i3, i4};
    }

    private static int[] a(List list, int[] iArr, int i) {
        int i2 = iArr[0];
        int i3 = 1;
        int i4 = i2;
        while (i3 < i) {
            int i5 = iArr[0] - i3;
            if (i5 == -1) {
                System.out.println("");
            }
            if (((Integer) list.get(i5)).intValue() <= 1) {
                break;
            }
            i3++;
            i4 = i5;
        }
        i3 = i2;
        i2 = 1;
        while (i2 < i) {
            i5 = iArr[0] + i2;
            if (i5 >= list.size()) {
                System.out.println("");
            }
            if (((Integer) list.get(i5)).intValue() <= 1) {
                break;
            }
            i2++;
            i3 = i5;
        }
        return new int[]{i4, i3};
    }

    public static int[] a(int[] iArr, int i, int i2) {
        int green;
        int i3;
        List arrayList = new ArrayList(i2);
        for (int i4 = 0; i4 < i2; i4++) {
            green = Color.green(iArr[i4 * i]);
            int i5 = 1;
            i3 = 0;
            while (i5 < i) {
                int green2 = Color.green(iArr[(i4 * i) + i5]);
                i3 += green2 == green ? 0 : 1;
                i5++;
                green = green2;
            }
            arrayList.add(Integer.valueOf(i3));
        }
        int[] a = a(arrayList, 0, i2);
        green = a[0] < arrayList.size() / 2 ? 1 : 0;
        i3 = Math.min(i2 / 6, 30);
        if (a[0] < i3 || a[0] + i3 >= i2 || a[0] == 0 || a[0] == i2 - 1) {
            a = null;
        } else {
            int[] a2 = a(arrayList, a, i3);
            a = green != 0 ? a(arrayList, a2[1] + 1, i2) : a(arrayList, 0, a2[0] - 1);
            if (a[0] < i3 || a[0] + i3 >= i2 || a[0] == 0 || a[0] == i2 - 1) {
                a = null;
            } else {
                a = a(arrayList, a, i3);
                a = green != 0 ? new int[]{a2[0], a[1]} : new int[]{a[0], a2[1]};
            }
        }
        if (a == null) {
            return null;
        }
        int[] a3 = a(a(iArr, a, i));
        return new int[]{a[0] - 10, a[1] + 15, a3[0] - 5, a3[1] + 5};
    }
}
