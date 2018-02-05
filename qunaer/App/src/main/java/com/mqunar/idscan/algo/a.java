package com.mqunar.idscan.algo;

import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;

public final class a {
    private static final int[] b = new int[]{7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1};
    private Map a = new HashMap(37);

    public a(SparseArray sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            if (sparseArray.keyAt(i) == 36) {
                this.a.put(Character.valueOf('<'), Integer.valueOf(0));
            } else {
                this.a.put(Character.valueOf(((String) sparseArray.get(i)).toCharArray()[0]), Integer.valueOf(sparseArray.keyAt(i)));
            }
        }
    }

    private boolean a(String str, char c) {
        char[] toCharArray = str.toCharArray();
        int i = 0;
        for (int i2 = 0; i2 < toCharArray.length; i2++) {
            i += ((Integer) this.a.get(Character.valueOf(toCharArray[i2]))).intValue() * b[i2];
        }
        return i % 10 == c + -48;
    }

    private static boolean b(String str) {
        for (char isDigit : str.toCharArray()) {
            if (!Character.isDigit(isDigit)) {
                return false;
            }
        }
        return true;
    }

    public final boolean a(String str) {
        String substring = str.substring(44, 53);
        boolean b = (str.charAt(2) == 'C' && str.charAt(3) == 'H' && str.charAt(4) == 'N' && str.charAt(52) != '<' && !Character.isLetter(substring.charAt(0))) ? false : b(substring.substring(1, 9).replace("<", ""));
        if (!b || !a(substring, str.charAt(53))) {
            return false;
        }
        String substring2 = str.substring(57, 63);
        if (!b(substring2) || !a(substring2, str.charAt(63))) {
            return false;
        }
        substring2 = str.substring(65, 71);
        return b(substring2) && a(substring2, str.charAt(71));
    }
}
