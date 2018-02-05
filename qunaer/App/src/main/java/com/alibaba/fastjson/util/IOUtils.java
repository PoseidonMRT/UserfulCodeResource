package com.alibaba.fastjson.util;

import android.support.v4.media.TransportMediator;
import com.mqunar.libtask.ProgressType;
import java.io.Closeable;

public class IOUtils {
    public static final char[] ASCII_CHARS = new char[]{'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
    public static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char[] DigitOnes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static final char[] DigitTens = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
    static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    public static final char[] replaceChars = new char[128];
    static final int[] sizeTable = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, ProgressType.PRO_END};
    public static final byte[] specicalFlags_doubleQuotes = new byte[256];
    public static final byte[] specicalFlags_singleQuotes = new byte[256];

    static {
        int i = 0;
        while (i < firstIdentifierFlags.length) {
            if (i >= 65 && i <= 90) {
                firstIdentifierFlags[i] = true;
            } else if (i >= 97 && i <= 122) {
                firstIdentifierFlags[i] = true;
            } else if (i == 95) {
                firstIdentifierFlags[i] = true;
            }
            i = (char) (i + 1);
        }
        i = 0;
        while (i < identifierFlags.length) {
            if (i >= 65 && i <= 90) {
                identifierFlags[i] = true;
            } else if (i >= 97 && i <= 122) {
                identifierFlags[i] = true;
            } else if (i == 95) {
                identifierFlags[i] = true;
            } else if (i >= 48 && i <= 57) {
                identifierFlags[i] = true;
            }
            i = (char) (i + 1);
        }
        specicalFlags_doubleQuotes[0] = (byte) 4;
        specicalFlags_doubleQuotes[1] = (byte) 4;
        specicalFlags_doubleQuotes[2] = (byte) 4;
        specicalFlags_doubleQuotes[3] = (byte) 4;
        specicalFlags_doubleQuotes[4] = (byte) 4;
        specicalFlags_doubleQuotes[5] = (byte) 4;
        specicalFlags_doubleQuotes[6] = (byte) 4;
        specicalFlags_doubleQuotes[7] = (byte) 4;
        specicalFlags_doubleQuotes[8] = (byte) 1;
        specicalFlags_doubleQuotes[9] = (byte) 1;
        specicalFlags_doubleQuotes[10] = (byte) 1;
        specicalFlags_doubleQuotes[11] = (byte) 4;
        specicalFlags_doubleQuotes[12] = (byte) 1;
        specicalFlags_doubleQuotes[13] = (byte) 1;
        specicalFlags_doubleQuotes[34] = (byte) 1;
        specicalFlags_doubleQuotes[92] = (byte) 1;
        specicalFlags_singleQuotes[0] = (byte) 4;
        specicalFlags_singleQuotes[1] = (byte) 4;
        specicalFlags_singleQuotes[2] = (byte) 4;
        specicalFlags_singleQuotes[3] = (byte) 4;
        specicalFlags_singleQuotes[4] = (byte) 4;
        specicalFlags_singleQuotes[5] = (byte) 4;
        specicalFlags_singleQuotes[6] = (byte) 4;
        specicalFlags_singleQuotes[7] = (byte) 4;
        specicalFlags_singleQuotes[8] = (byte) 1;
        specicalFlags_singleQuotes[9] = (byte) 1;
        specicalFlags_singleQuotes[10] = (byte) 1;
        specicalFlags_singleQuotes[11] = (byte) 4;
        specicalFlags_singleQuotes[12] = (byte) 1;
        specicalFlags_singleQuotes[13] = (byte) 1;
        specicalFlags_singleQuotes[92] = (byte) 1;
        specicalFlags_singleQuotes[39] = (byte) 1;
        for (i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = (byte) 4;
            specicalFlags_singleQuotes[i] = (byte) 4;
        }
        for (i = TransportMediator.KEYCODE_MEDIA_PAUSE; i <= 160; i++) {
            specicalFlags_doubleQuotes[i] = (byte) 4;
            specicalFlags_singleQuotes[i] = (byte) 4;
        }
        replaceChars[0] = '0';
        replaceChars[1] = '1';
        replaceChars[2] = '2';
        replaceChars[3] = '3';
        replaceChars[4] = '4';
        replaceChars[5] = '5';
        replaceChars[6] = '6';
        replaceChars[7] = '7';
        replaceChars[8] = 'b';
        replaceChars[9] = 't';
        replaceChars[10] = 'n';
        replaceChars[11] = 'v';
        replaceChars[12] = 'f';
        replaceChars[13] = 'r';
        replaceChars[34] = '\"';
        replaceChars[39] = '\'';
        replaceChars[47] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX;
        replaceChars[92] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static int stringSize(long j) {
        long j2 = 10;
        for (int i = 1; i < 19; i++) {
            if (j < j2) {
                return i;
            }
            j2 *= 10;
        }
        return 19;
    }

    public static void getChars(long j, int i, char[] cArr) {
        char c;
        int i2;
        int i3;
        int i4;
        if (j < 0) {
            j = -j;
            c = '-';
            i2 = i;
        } else {
            c = '\u0000';
            i2 = i;
        }
        while (j > 2147483647L) {
            long j2 = j / 100;
            i3 = (int) (j - (((j2 << 6) + (j2 << 5)) + (j2 << 2)));
            i2--;
            cArr[i2] = DigitOnes[i3];
            i = i2 - 1;
            cArr[i] = DigitTens[i3];
            i2 = i;
            j = j2;
        }
        i3 = (int) j;
        while (i3 >= 65536) {
            i4 = i3 / 100;
            i3 -= ((i4 << 6) + (i4 << 5)) + (i4 << 2);
            i2--;
            cArr[i2] = DigitOnes[i3];
            i2--;
            cArr[i2] = DigitTens[i3];
            i3 = i4;
        }
        i4 = i2;
        while (true) {
            i2 = (52429 * i3) >>> 19;
            int i5 = i3 - ((i2 << 3) + (i2 << 1));
            i3 = i4 - 1;
            cArr[i3] = digits[i5];
            if (i2 == 0) {
                break;
            }
            i4 = i3;
            i3 = i2;
        }
        if (c != '\u0000') {
            cArr[i3 - 1] = c;
        }
    }

    public static int stringSize(int i) {
        int i2 = 0;
        while (i > sizeTable[i2]) {
            i2++;
        }
        return i2 + 1;
    }
}
