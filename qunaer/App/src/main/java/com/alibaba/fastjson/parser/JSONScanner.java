package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public final class JSONScanner extends JSONLexerBase {
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    public final int ISO8601_LEN_0;
    public final int ISO8601_LEN_1;
    public final int ISO8601_LEN_2;
    private final String text;

    public JSONScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String str, int i) {
        this.ISO8601_LEN_0 = "0000-00-00".length();
        this.ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
        this.ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();
        this.features = i;
        this.text = str;
        this.bp = -1;
        next();
        if (this.ch == '﻿') {
            next();
        }
    }

    public final char charAt(int i) {
        if (i >= this.text.length()) {
            return '\u001a';
        }
        return this.text.charAt(i);
    }

    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char charAt = charAt(i);
        this.ch = charAt;
        return charAt;
    }

    public JSONScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    protected final void copyTo(int i, int i2, char[] cArr) {
        this.text.getChars(i, i + i2, cArr, 0);
    }

    public final int indexOf(char c, int i) {
        return this.text.indexOf(c, i);
    }

    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, i, i2, i3);
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(this.text, this.np + 1, this.sp);
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    public final String subString(int i, int i2) {
        char[] cArr = new char[i2];
        for (int i3 = i; i3 < i + i2; i3++) {
            cArr[i3 - i] = this.text.charAt(i3);
        }
        return new String(cArr);
    }

    public final String numberString() {
        char charAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        char charAt;
        char charAt2;
        int i;
        int i2;
        int length = this.text.length() - this.bp;
        if (!z && length > 13) {
            charAt = charAt(this.bp);
            char charAt3 = charAt(this.bp + 1);
            charAt2 = charAt(this.bp + 2);
            char charAt4 = charAt(this.bp + 3);
            char charAt5 = charAt(this.bp + 4);
            char charAt6 = charAt(this.bp + 5);
            char charAt7 = charAt((this.bp + length) - 1);
            char charAt8 = charAt((this.bp + length) - 2);
            if (charAt == IOUtils.DIR_SEPARATOR_UNIX && charAt3 == 'D' && charAt2 == 'a' && charAt4 == 't' && charAt5 == 'e' && charAt6 == '(' && charAt7 == IOUtils.DIR_SEPARATOR_UNIX && charAt8 == ')') {
                i = -1;
                for (i2 = 6; i2 < length; i2++) {
                    charAt2 = charAt(this.bp + i2);
                    if (charAt2 != '+') {
                        if (charAt2 < '0' || charAt2 > '9') {
                            break;
                        }
                    } else {
                        i = i2;
                    }
                }
                if (i == -1) {
                    return false;
                }
                i2 = this.bp + 6;
                long parseLong = Long.parseLong(subString(i2, i - i2));
                this.calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                this.calendar.setTimeInMillis(parseLong);
                this.token = 5;
                return true;
            }
        }
        int i3;
        if (length == 8 || length == 14 || length == 17) {
            if (z) {
                return false;
            }
            charAt = charAt(this.bp);
            charAt3 = charAt(this.bp + 1);
            charAt2 = charAt(this.bp + 2);
            charAt4 = charAt(this.bp + 3);
            charAt5 = charAt(this.bp + 4);
            charAt6 = charAt(this.bp + 5);
            charAt7 = charAt(this.bp + 6);
            charAt8 = charAt(this.bp + 7);
            if (!checkDate(charAt, charAt3, charAt2, charAt4, charAt5, charAt6, charAt7, charAt8)) {
                return false;
            }
            int i4;
            setCalendar(charAt, charAt3, charAt2, charAt4, charAt5, charAt6, charAt7, charAt8);
            if (length != 8) {
                charAt3 = charAt(this.bp + 8);
                charAt2 = charAt(this.bp + 9);
                charAt4 = charAt(this.bp + 10);
                charAt5 = charAt(this.bp + 11);
                charAt6 = charAt(this.bp + 12);
                charAt7 = charAt(this.bp + 13);
                if (!checkTime(charAt3, charAt2, charAt4, charAt5, charAt6, charAt7)) {
                    return false;
                }
                if (length == 17) {
                    charAt = charAt(this.bp + 14);
                    charAt8 = charAt(this.bp + 15);
                    char charAt9 = charAt(this.bp + 16);
                    if (charAt < '0' || charAt > '9') {
                        return false;
                    }
                    if (charAt8 < '0' || charAt8 > '9') {
                        return false;
                    }
                    if (charAt9 < '0' || charAt9 > '9') {
                        return false;
                    }
                    i = ((digits[charAt] * 100) + (digits[charAt8] * 10)) + digits[charAt9];
                } else {
                    i = 0;
                }
                int i5 = (digits[charAt3] * 10) + digits[charAt2];
                i3 = digits[charAt5] + (digits[charAt4] * 10);
                i2 = (digits[charAt6] * 10) + digits[charAt7];
                i4 = i5;
            } else {
                i4 = 0;
                i3 = 0;
                i2 = 0;
                i = 0;
            }
            this.calendar.set(11, i4);
            this.calendar.set(12, i3);
            this.calendar.set(13, i2);
            this.calendar.set(14, i);
            this.token = 5;
            return true;
        } else if (length < this.ISO8601_LEN_0) {
            return false;
        } else {
            if (charAt(this.bp + 4) != '-') {
                return false;
            }
            if (charAt(this.bp + 7) != '-') {
                return false;
            }
            charAt = charAt(this.bp);
            charAt3 = charAt(this.bp + 1);
            charAt2 = charAt(this.bp + 2);
            charAt4 = charAt(this.bp + 3);
            charAt5 = charAt(this.bp + 5);
            charAt6 = charAt(this.bp + 6);
            charAt7 = charAt(this.bp + 8);
            charAt8 = charAt(this.bp + 9);
            if (!checkDate(charAt, charAt3, charAt2, charAt4, charAt5, charAt6, charAt7, charAt8)) {
                return false;
            }
            setCalendar(charAt, charAt3, charAt2, charAt4, charAt5, charAt6, charAt7, charAt8);
            charAt = charAt(this.bp + 10);
            if (charAt == 'T' || (charAt == ' ' && !z)) {
                if (length < this.ISO8601_LEN_1) {
                    return false;
                }
                if (charAt(this.bp + 13) != ':') {
                    return false;
                }
                if (charAt(this.bp + 16) != ':') {
                    return false;
                }
                charAt3 = charAt(this.bp + 11);
                charAt2 = charAt(this.bp + 12);
                charAt4 = charAt(this.bp + 14);
                charAt5 = charAt(this.bp + 15);
                charAt6 = charAt(this.bp + 17);
                charAt7 = charAt(this.bp + 18);
                if (!checkTime(charAt3, charAt2, charAt4, charAt5, charAt6, charAt7)) {
                    return false;
                }
                i = (digits[charAt3] * 10) + digits[charAt2];
                i2 = (digits[charAt4] * 10) + digits[charAt5];
                i3 = (digits[charAt6] * 10) + digits[charAt7];
                this.calendar.set(11, i);
                this.calendar.set(12, i2);
                this.calendar.set(13, i3);
                if (charAt(this.bp + 19) != FilenameUtils.EXTENSION_SEPARATOR) {
                    this.calendar.set(14, 0);
                    i = this.bp + 19;
                    this.bp = i;
                    this.ch = charAt(i);
                    this.token = 5;
                    return true;
                } else if (length < this.ISO8601_LEN_2) {
                    return false;
                } else {
                    charAt = charAt(this.bp + 20);
                    if (charAt < '0' || charAt > '9') {
                        return false;
                    }
                    i2 = digits[charAt];
                    i = 1;
                    charAt2 = charAt(this.bp + 21);
                    if (charAt2 >= '0' && charAt2 <= '9') {
                        i2 = digits[charAt2] + (i2 * 10);
                        i = 2;
                    }
                    if (i == 2) {
                        charAt2 = charAt(this.bp + 22);
                        if (charAt2 >= '0' && charAt2 <= '9') {
                            i2 = digits[charAt2] + (i2 * 10);
                            i = 3;
                        }
                    }
                    this.calendar.set(14, i2);
                    i2 = 0;
                    charAt4 = charAt((this.bp + 20) + i);
                    if (charAt4 == '+' || charAt4 == '-') {
                        charAt2 = charAt(((this.bp + 20) + i) + 1);
                        if (charAt2 < '0' || charAt2 > '1') {
                            return false;
                        }
                        charAt5 = charAt(((this.bp + 20) + i) + 2);
                        if (charAt5 < '0' || charAt5 > '9') {
                            return false;
                        }
                        charAt3 = charAt(((this.bp + 20) + i) + 3);
                        if (charAt3 == ':') {
                            if (charAt(((this.bp + 20) + i) + 4) != '0') {
                                return false;
                            }
                            if (charAt(((this.bp + 20) + i) + 5) != '0') {
                                return false;
                            }
                            i2 = 6;
                        } else if (charAt3 != '0') {
                            i2 = 3;
                        } else if (charAt(((this.bp + 20) + i) + 4) != '0') {
                            return false;
                        } else {
                            i2 = 5;
                        }
                        i3 = (((digits[charAt2] * 10) + digits[charAt5]) * 3600) * 1000;
                        if (charAt4 == '-') {
                            i3 = -i3;
                        }
                        if (this.calendar.getTimeZone().getRawOffset() != i3) {
                            String[] availableIDs = TimeZone.getAvailableIDs(i3);
                            if (availableIDs.length > 0) {
                                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
                            }
                        }
                    }
                    charAt2 = charAt(this.bp + ((i + 20) + i2));
                    if (charAt2 != '\u001a' && charAt2 != '\"') {
                        return false;
                    }
                    i = ((i + 20) + i2) + this.bp;
                    this.bp = i;
                    this.ch = charAt(i);
                    this.token = 5;
                    return true;
                }
            } else if (charAt != '\"' && charAt != '\u001a') {
                return false;
            } else {
                this.calendar.set(11, 0);
                this.calendar.set(12, 0);
                this.calendar.set(13, 0);
                this.calendar.set(14, 0);
                i = this.bp + 10;
                this.bp = i;
                this.ch = charAt(i);
                this.token = 5;
                return true;
            }
        }
    }

    private boolean checkTime(char c, char c2, char c3, char c4, char c5, char c6) {
        if (c == '0') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c == '1') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c != '2' || c2 < '0') {
            return false;
        } else {
            if (c2 > '4') {
                return false;
            }
        }
        if (c3 < '0' || c3 > '5') {
            if (c3 != '6') {
                return false;
            }
            if (c4 != '0') {
                return false;
            }
        } else if (c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 < '0' || c5 > '5') {
            if (c5 != '6') {
                return false;
            }
            if (c6 != '0') {
                return false;
            }
        } else if (c6 < '0' || c6 > '9') {
            return false;
        }
        return true;
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        int i = ((digits[c5] * 10) + digits[c6]) - 1;
        int i2 = (digits[c7] * 10) + digits[c8];
        this.calendar.set(1, (((digits[c] * 1000) + (digits[c2] * 100)) + (digits[c3] * 10)) + digits[c4]);
        this.calendar.set(2, i);
        this.calendar.set(5, i2);
    }

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if ((c != '1' && c != '2') || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 == '0') {
            if (c6 < '1' || c6 > '9') {
                return false;
            }
        } else if (c5 != '1') {
            return false;
        } else {
            if (!(c6 == '0' || c6 == '1' || c6 == '2')) {
                return false;
            }
        }
        if (i == 48) {
            if (i2 < 49 || i2 > 57) {
                return false;
            }
        } else if (i == 49 || i == 50) {
            if (i2 < 48) {
                return false;
            }
            if (i2 > 57) {
                return false;
            }
        } else if (i != 51) {
            return false;
        } else {
            if (!(i2 == 48 || i2 == 49)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEOF() {
        return this.bp == this.text.length() || (this.ch == '\u001a' && this.bp + 1 == this.text.length());
    }

    protected final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        this.text.getChars(i, i + i3, cArr, i2);
    }
}
