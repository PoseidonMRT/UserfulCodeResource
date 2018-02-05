package com.mqunar.idscan.algo;

import java.util.HashMap;

public final class h {
    private static HashMap a;
    private static HashMap b;

    static {
        HashMap hashMap = new HashMap();
        a = hashMap;
        hashMap.put(Character.valueOf('0'), Character.valueOf('O'));
        a.put(Character.valueOf('5'), Character.valueOf('S'));
        a.put(Character.valueOf('1'), Character.valueOf('I'));
        a.put(Character.valueOf('8'), Character.valueOf('B'));
        a.put(Character.valueOf('9'), Character.valueOf('R'));
        a.put(Character.valueOf('4'), Character.valueOf('P'));
        a.put(Character.valueOf('7'), Character.valueOf('Z'));
        hashMap = new HashMap();
        b = hashMap;
        hashMap.put(Character.valueOf('M'), Character.valueOf('8'));
        b.put(Character.valueOf('S'), Character.valueOf('5'));
        b.put(Character.valueOf('I'), Character.valueOf('1'));
        b.put(Character.valueOf('B'), Character.valueOf('8'));
        b.put(Character.valueOf('R'), Character.valueOf('9'));
        b.put(Character.valueOf('P'), Character.valueOf('4'));
        b.put(Character.valueOf('L'), Character.valueOf('0'));
        b.put(Character.valueOf('O'), Character.valueOf('0'));
        b.put(Character.valueOf('U'), Character.valueOf('0'));
    }

    public static String a(String str) {
        char[] toCharArray = str.toCharArray();
        if (toCharArray[1] == 'C') {
            toCharArray[1] = '<';
        }
        if (toCharArray[1] == 'O' && toCharArray[0] != 'P') {
            toCharArray[0] = 'P';
        }
        if (toCharArray[0] == '0' || toCharArray[41] == '0') {
            toCharArray[0] = 'D';
        }
        if (toCharArray[0] == 'A') {
            toCharArray[0] = 'P';
        }
        if (toCharArray[5] == 'K' && toCharArray[6] == 'I' && toCharArray[7] == 'E') {
            toCharArray[5] = 'X';
        }
        if (toCharArray[2] == 'C' && toCharArray[3] == 'H' && toCharArray[4] == 'N' && toCharArray[8] == '<' && toCharArray[5] == 'E' && toCharArray[6] == 'H' && toCharArray[7] == 'I') {
            toCharArray[5] = 'S';
        }
        int i = 1;
        while (i < 44 && i != 41) {
            if (a.containsKey(Character.valueOf(toCharArray[i]))) {
                toCharArray[i] = ((Character) a.get(Character.valueOf(toCharArray[i]))).charValue();
            }
            i++;
        }
        int i2 = 6;
        while (i2 < 43) {
            if ((toCharArray[i2] == 'C' || toCharArray[i2] == 'A') && toCharArray[i2 - 1] == '<' && toCharArray[i2 + 1] == '<') {
                toCharArray[i2] = '<';
            }
            if (toCharArray[i2] == 'C' && toCharArray[i2 + 1] == 'C' && toCharArray[i2 + 2] == '<' && toCharArray[i2 + 3] == '<') {
                toCharArray[i2] = '<';
                toCharArray[i2 + 1] = '<';
            }
            i2++;
        }
        if (toCharArray[64] == '8' || toCharArray[64] == 'H') {
            toCharArray[64] = 'M';
        }
        if (toCharArray[2] == 'C' && toCharArray[3] == 'H' && toCharArray[4] == 'N' && (toCharArray[44] == 'A' || toCharArray[44] == 'F')) {
            toCharArray[44] = 'E';
        }
        i = 44;
        while (i < 88) {
            if ((toCharArray[i] == 'O' || toCharArray[i] == 'D' || toCharArray[i] == 'C' || toCharArray[i] == 'B') && ((i == 87 && Character.isDigit(toCharArray[i - 1])) || (i != 44 && i != 87 && Character.isDigit(toCharArray[i - 1]) && Character.isDigit(toCharArray[i + 1])))) {
                toCharArray[i] = '0';
            }
            if (toCharArray[i] == '0' && i != 44 && i != 87 && Character.isLetter(toCharArray[i - 1]) && Character.isLetter(toCharArray[i + 1]) && toCharArray[i + 1] != 'Q') {
                toCharArray[i] = 'O';
            }
            if (i == 87 && toCharArray[i] == 'Z') {
                toCharArray[i] = '2';
            }
            if (i < 86 && toCharArray[i] == 'D' && toCharArray[i + 1] == 'O' && toCharArray[i + 2] == '<') {
                toCharArray[i] = '\u0000';
                toCharArray[i + 1] = '\u0000';
            }
            if (i > 44 && i < 54 && b.containsKey(Character.valueOf(toCharArray[i]))) {
                toCharArray[i] = ((Character) b.get(Character.valueOf(toCharArray[i]))).charValue();
            }
            i++;
        }
        return new String(toCharArray);
    }
}
