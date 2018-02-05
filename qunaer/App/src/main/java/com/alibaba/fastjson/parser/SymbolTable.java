package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;

public class SymbolTable {
    public static final int DEFAULT_TABLE_SIZE = 512;
    public static final int MAX_BUCKET_LENTH = 8;
    public static final int MAX_SIZE = 4096;
    private final Entry[] buckets;
    private final int indexMask;
    private int size;
    private final String[] symbols;
    private final char[][] symbols_char;

    public final class Entry {
        public final byte[] bytes;
        public final char[] characters;
        public final int hashCode;
        public Entry next;
        public final String symbol;

        public Entry(char[] cArr, int i, int i2, int i3, Entry entry) {
            this.characters = new char[i2];
            System.arraycopy(cArr, i, this.characters, 0, i2);
            this.symbol = new String(this.characters).intern();
            this.next = entry;
            this.hashCode = i3;
            this.bytes = null;
        }

        public Entry(String str, int i, int i2, int i3, Entry entry) {
            this.symbol = SymbolTable.subString(str, i, i2).intern();
            this.characters = this.symbol.toCharArray();
            this.next = entry;
            this.hashCode = i3;
            this.bytes = null;
        }
    }

    public SymbolTable() {
        this(512);
        addSymbol("$ref", 0, 4, "$ref".hashCode());
        addSymbol(JSON.DEFAULT_TYPE_KEY, 0, 5, JSON.DEFAULT_TYPE_KEY.hashCode());
    }

    public SymbolTable(int i) {
        this.size = 0;
        this.indexMask = i - 1;
        this.buckets = new Entry[i];
        this.symbols = new String[i];
        this.symbols_char = new char[i][];
    }

    public String addSymbol(char[] cArr, int i, int i2) {
        return addSymbol(cArr, i, i2, hash(cArr, i, i2));
    }

    public String addSymbol(char[] cArr, int i, int i2, int i3) {
        int i4;
        Object obj;
        int i5 = i3 & this.indexMask;
        String str = this.symbols[i5];
        if (str == null) {
            int i6 = 1;
        } else if (str.length() == i2) {
            Object obj2;
            char[] cArr2 = this.symbols_char[i5];
            for (i4 = 0; i4 < i2; i4++) {
                if (cArr[i + i4] != cArr2[i4]) {
                    obj2 = null;
                    break;
                }
            }
            i4 = 1;
            if (obj2 != null) {
                return str;
            }
            obj = obj2;
        } else {
            obj = null;
        }
        Entry entry = this.buckets[i5];
        i4 = 0;
        while (entry != null) {
            char[] cArr3 = entry.characters;
            if (i2 == cArr3.length && i3 == entry.hashCode) {
                int i7;
                Object obj3;
                for (i7 = 0; i7 < i2; i7++) {
                    if (cArr[i + i7] != cArr3[i7]) {
                        obj3 = null;
                        break;
                    }
                }
                i7 = 1;
                if (obj3 != null) {
                    return entry.symbol;
                }
                i4++;
            }
            entry = entry.next;
        }
        if (i4 >= 8) {
            return new String(cArr, i, i2);
        }
        if (this.size >= 4096) {
            return new String(cArr, i, i2);
        }
        Entry entry2 = new Entry(cArr, i, i2, i3, this.buckets[i5]);
        this.buckets[i5] = entry2;
        if (obj != null) {
            this.symbols[i5] = entry2.symbol;
            this.symbols_char[i5] = entry2.characters;
        }
        this.size++;
        return entry2.symbol;
    }

    public String addSymbol(String str, int i, int i2, int i3) {
        int i4;
        Object obj;
        int i5 = i3 & this.indexMask;
        String str2 = this.symbols[i5];
        if (str2 == null) {
            int i6 = 1;
        } else if (str2.length() == i2) {
            Object obj2;
            char[] cArr = this.symbols_char[i5];
            for (i4 = 0; i4 < i2; i4++) {
                if (str.charAt(i + i4) != cArr[i4]) {
                    obj2 = null;
                    break;
                }
            }
            i4 = 1;
            if (obj2 != null) {
                return str2;
            }
            obj = obj2;
        } else {
            obj = null;
        }
        Entry entry = this.buckets[i5];
        i4 = 0;
        while (entry != null) {
            char[] cArr2 = entry.characters;
            if (i2 == cArr2.length && i3 == entry.hashCode) {
                int i7;
                Object obj3;
                for (i7 = 0; i7 < i2; i7++) {
                    if (str.charAt(i + i7) != cArr2[i7]) {
                        obj3 = null;
                        break;
                    }
                }
                i7 = 1;
                if (obj3 != null) {
                    return entry.symbol;
                }
                i4++;
            }
            entry = entry.next;
        }
        if (i4 >= 8) {
            return subString(str, i, i2);
        }
        if (this.size >= 4096) {
            return subString(str, i, i2);
        }
        Entry entry2 = new Entry(str, i, i2, i3, this.buckets[i5]);
        this.buckets[i5] = entry2;
        if (obj != null) {
            this.symbols[i5] = entry2.symbol;
            this.symbols_char[i5] = entry2.characters;
        }
        this.size++;
        return entry2.symbol;
    }

    private static String subString(String str, int i, int i2) {
        char[] cArr = new char[i2];
        for (int i3 = i; i3 < i + i2; i3++) {
            cArr[i3 - i] = str.charAt(i3);
        }
        return new String(cArr);
    }

    public int size() {
        return this.size;
    }

    public static final int hash(char[] cArr, int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            int i5 = i4 * 31;
            i3++;
            i++;
            i4 = i5 + cArr[i];
        }
        return i4;
    }
}
