package org.apache.commons.codecc.language;

import org.apache.commons.codecc.EncoderException;
import org.apache.commons.codecc.StringEncoder;

public class Soundex implements StringEncoder {
    public static final Soundex US_ENGLISH = new Soundex();
    private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    private int maxLength;
    private final char[] soundexMapping;

    public int difference(String str, String str2) {
        return SoundexUtils.difference(this, str, str2);
    }

    public Soundex() {
        this.maxLength = 4;
        this.soundexMapping = US_ENGLISH_MAPPING;
    }

    public Soundex(char[] cArr) {
        this.maxLength = 4;
        this.soundexMapping = new char[cArr.length];
        System.arraycopy(cArr, 0, this.soundexMapping, 0, cArr.length);
    }

    public Soundex(String str) {
        this.maxLength = 4;
        this.soundexMapping = str.toCharArray();
    }

    public Object encode(Object obj) {
        if (obj instanceof String) {
            return soundex((String) obj);
        }
        throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
    }

    public String encode(String str) {
        return soundex(str);
    }

    private char getMappingCode(String str, int i) {
        char map = map(str.charAt(i));
        if (i <= 1 || map == '0') {
            return map;
        }
        char charAt = str.charAt(i - 1);
        if ('H' != charAt && 'W' != charAt) {
            return map;
        }
        charAt = str.charAt(i - 2);
        if (map(charAt) == map || 'H' == charAt || 'W' == charAt) {
            return '\u0000';
        }
        return map;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    private char[] getSoundexMapping() {
        return this.soundexMapping;
    }

    private char map(char c) {
        int i = c - 65;
        if (i >= 0 && i < getSoundexMapping().length) {
            return getSoundexMapping()[i];
        }
        throw new IllegalArgumentException("The character is not mapped: " + c);
    }

    public void setMaxLength(int i) {
        this.maxLength = i;
    }

    public String soundex(String str) {
        int i = 1;
        if (str == null) {
            return null;
        }
        String clean = SoundexUtils.clean(str);
        if (clean.length() == 0) {
            return clean;
        }
        char[] cArr = new char[]{'0', '0', '0', '0'};
        cArr[0] = clean.charAt(0);
        char mappingCode = getMappingCode(clean, 0);
        int i2 = 1;
        while (i < clean.length() && i2 < cArr.length) {
            int i3 = i + 1;
            char mappingCode2 = getMappingCode(clean, i);
            if (mappingCode2 != '\u0000') {
                int i4;
                if (mappingCode2 == '0' || mappingCode2 == r0) {
                    i4 = i2;
                } else {
                    i4 = i2 + 1;
                    cArr[i2] = mappingCode2;
                }
                i2 = i4;
                mappingCode = mappingCode2;
                i = i3;
            } else {
                i = i3;
            }
        }
        return new String(cArr);
    }
}
