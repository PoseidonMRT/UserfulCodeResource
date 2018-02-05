package com.alibaba.fastjson.parser;

import android.support.v4.view.MotionEventCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.baidu.mapapi.UIMsg.k_event;
import com.iflytek.cloud.SpeechError;
import java.io.Closeable;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codecc.binary.BaseNCodec;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public abstract class JSONLexerBase implements JSONLexer, Closeable {
    private static final Map<String, Integer> DEFAULT_KEYWORDS;
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final int INT_N_MULTMAX_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    protected static final long N_MULTMAX_RADIX_TEN = -922337203685477580L;
    private static final ThreadLocal<SoftReference<char[]>> SBUF_REF_LOCAL = new ThreadLocal();
    protected static final int[] digits = new int[103];
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected static boolean[] whitespaceFlags = new boolean[256];
    protected int bp;
    protected Calendar calendar = null;
    protected char ch;
    protected int eofPos;
    protected int features = JSON.DEFAULT_PARSER_FEATURE;
    protected boolean hasSpecial;
    protected Map<String, Integer> keywods = DEFAULT_KEYWORDS;
    public int matchStat = 0;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected int token;

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    public abstract byte[] bytesValue();

    public abstract char charAt(int i);

    protected abstract void copyTo(int i, int i2, char[] cArr);

    public abstract int indexOf(char c, int i);

    public abstract boolean isEOF();

    public abstract char next();

    public abstract String numberString();

    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    static {
        int i;
        Map hashMap = new HashMap();
        hashMap.put("null", Integer.valueOf(8));
        hashMap.put("new", Integer.valueOf(9));
        hashMap.put("true", Integer.valueOf(6));
        hashMap.put("false", Integer.valueOf(7));
        hashMap.put("undefined", Integer.valueOf(23));
        DEFAULT_KEYWORDS = hashMap;
        whitespaceFlags[32] = true;
        whitespaceFlags[10] = true;
        whitespaceFlags[13] = true;
        whitespaceFlags[9] = true;
        whitespaceFlags[12] = true;
        whitespaceFlags[8] = true;
        for (i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (i = 97; i <= 102; i++) {
            digits[i] = (i - 97) + 10;
        }
        for (i = 65; i <= 70; i++) {
            digits[i] = (i - 65) + 10;
        }
    }

    protected void lexError(String str, Object... objArr) {
        this.token = 1;
    }

    public JSONLexerBase() {
        SoftReference softReference = (SoftReference) SBUF_REF_LOCAL.get();
        if (softReference != null) {
            this.sbuf = (char[]) softReference.get();
            SBUF_REF_LOCAL.set(null);
        }
        if (this.sbuf == null) {
            this.sbuf = new char[64];
        }
    }

    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch == '\"') {
                scanString();
                return;
            } else if (this.ch == ',') {
                next();
                this.token = 16;
                return;
            } else if (this.ch >= '0' && this.ch <= '9') {
                scanNumber();
                return;
            } else if (this.ch == '-') {
                scanNumber();
                return;
            } else {
                switch (this.ch) {
                    case '\b':
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        next();
                    case '\'':
                        if (isEnabled(Feature.AllowSingleQuotes)) {
                            scanStringSingleQuote();
                            return;
                        }
                        throw new JSONException("Feature.AllowSingleQuotes is false");
                    case '(':
                        next();
                        this.token = 10;
                        return;
                    case ')':
                        next();
                        this.token = 11;
                        return;
                    case SpeechError.TIP_ERROR_IVP_MUCH_NOISE /*58*/:
                        next();
                        this.token = 17;
                        return;
                    case k_event.V_S /*83*/:
                        scanSet();
                        return;
                    case 'T':
                        scanTreeSet();
                        return;
                    case '[':
                        next();
                        this.token = 14;
                        return;
                    case ']':
                        next();
                        this.token = 15;
                        return;
                    case 'f':
                        scanFalse();
                        return;
                    case 'n':
                        scanNullOrNew();
                        return;
                    case 't':
                        scanTrue();
                        return;
                    case 'u':
                        scanUndefined();
                        return;
                    case '{':
                        next();
                        this.token = 12;
                        return;
                    case '}':
                        next();
                        this.token = 13;
                        return;
                    default:
                        if (!isEOF()) {
                            lexError("illegal.char", String.valueOf(this.ch));
                            next();
                            return;
                        } else if (this.token == 20) {
                            throw new JSONException("EOF error");
                        } else {
                            this.token = 20;
                            int i = this.eofPos;
                            this.bp = i;
                            this.pos = i;
                            return;
                        }
                }
            }
        }
    }

    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            switch (i) {
                case 2:
                    if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 4:
                    if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 12:
                    if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    }
                    break;
                case 14:
                    if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 15:
                    if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    }
                    break;
                case 16:
                    if (this.ch == ',') {
                        this.token = 16;
                        next();
                        return;
                    } else if (this.ch == '}') {
                        this.token = 13;
                        next();
                        return;
                    } else if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    } else if (this.ch == '\u001a') {
                        this.token = 20;
                        return;
                    }
                    break;
                case 18:
                    nextIdent();
                    return;
                case 20:
                    break;
            }
            if (this.ch == '\u001a') {
                this.token = 20;
                return;
            }
            if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (this.ch == '_' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char c) {
        this.sp = 0;
        while (this.ch != c) {
            if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else {
                throw new JSONException("not match " + c + " - " + this.ch);
            }
        }
        next();
        nextToken();
    }

    public final int token() {
        return this.token;
    }

    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    public final int pos() {
        return this.pos;
    }

    public final int getBufferPosition() {
        return this.bp;
    }

    public final String stringDefaultValue() {
        if (isEnabled(Feature.InitStringFieldAsEmpty)) {
            return "";
        }
        return null;
    }

    public final Number integerValue() {
        long j;
        Object obj;
        int i;
        int i2;
        long j2 = 0;
        if (this.np == -1) {
            this.np = 0;
        }
        int i3 = this.np;
        int i4 = this.sp + this.np;
        int i5 = 32;
        switch (charAt(i4 - 1)) {
            case 'B':
                i4--;
                i5 = 66;
                break;
            case BaseNCodec.MIME_CHUNK_SIZE /*76*/:
                i4--;
                i5 = 76;
                break;
            case k_event.V_S /*83*/:
                i4--;
                i5 = 83;
                break;
        }
        if (charAt(this.np) == '-') {
            j = Long.MIN_VALUE;
            obj = 1;
            i = i3 + 1;
        } else {
            j = -9223372036854775807L;
            obj = null;
            i = i3;
        }
        long j3 = obj != null ? -922337203685477580L : -922337203685477580L;
        if (i < i4) {
            i2 = i + 1;
            j2 = (long) (-digits[charAt(i)]);
        } else {
            i2 = i;
        }
        while (i2 < i4) {
            i = i2 + 1;
            i2 = digits[charAt(i2)];
            if (j2 < j3) {
                return new BigInteger(numberString());
            }
            j2 *= 10;
            if (j2 < ((long) i2) + j) {
                return new BigInteger(numberString());
            }
            j2 -= (long) i2;
            i2 = i;
        }
        if (obj == null) {
            long j4 = -j2;
            if (j4 > 2147483647L || i5 == 76) {
                return Long.valueOf(j4);
            }
            if (i5 == 83) {
                return Short.valueOf((short) ((int) j4));
            }
            if (i5 == 66) {
                return Byte.valueOf((byte) ((int) j4));
            }
            return Integer.valueOf((int) j4);
        } else if (i2 <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (j2 < -2147483648L || i5 == 76) {
            return Long.valueOf(j2);
        } else {
            if (i5 == 83) {
                return Short.valueOf((short) ((int) j2));
            }
            if (i5 == 66) {
                return Byte.valueOf((byte) ((int) j2));
            }
            return Integer.valueOf((int) j2);
        }
    }

    public final void nextTokenWithColon(int i) {
        nextTokenWithChar(':');
    }

    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean z) {
        this.features = Feature.config(this.features, feature, z);
    }

    public final boolean isEnabled(Feature feature) {
        return Feature.isEnabled(this.features, feature);
    }

    public final char getCurrent() {
        return this.ch;
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (this.ch == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        } else if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        } else if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        } else if (this.ch == '\u001a') {
            this.token = 20;
            return null;
        } else if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable, char c) {
        this.np = this.bp;
        this.sp = 0;
        char c2 = '\u0000';
        int i = 0;
        while (true) {
            char next = next();
            int i2;
            if (next == c) {
                String addSymbol;
                this.token = 4;
                if (c2 == '\u0000') {
                    if (this.np == -1) {
                        i2 = 0;
                    } else {
                        i2 = this.np + 1;
                    }
                    addSymbol = addSymbol(i2, this.sp, i, symbolTable);
                } else {
                    addSymbol = symbolTable.addSymbol(this.sbuf, 0, this.sp, i);
                }
                this.sp = 0;
                next();
                return addSymbol;
            } else if (next == '\u001a') {
                throw new JSONException("unclosed.str");
            } else if (next == IOUtils.DIR_SEPARATOR_WINDOWS) {
                if (c2 == '\u0000') {
                    if (this.sp >= this.sbuf.length) {
                        i2 = this.sbuf.length * 2;
                        if (this.sp > i2) {
                            i2 = this.sp;
                        }
                        Object obj = new char[i2];
                        System.arraycopy(this.sbuf, 0, obj, 0, this.sbuf.length);
                        this.sbuf = obj;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                    c2 = '\u0001';
                }
                next = next();
                char next2;
                switch (next) {
                    case '\"':
                        i = (i * 31) + 34;
                        putChar('\"');
                        break;
                    case '\'':
                        i = (i * 31) + 39;
                        putChar('\'');
                        break;
                    case MotionEventCompat.AXIS_GENERIC_16 /*47*/:
                        i = (i * 31) + 47;
                        putChar(IOUtils.DIR_SEPARATOR_UNIX);
                        break;
                    case k_event.V_WM_LONGPRESS /*48*/:
                        i = (i * 31) + next;
                        putChar('\u0000');
                        break;
                    case k_event.V_WM_STREET_JUMP /*49*/:
                        i = (i * 31) + next;
                        putChar('\u0001');
                        break;
                    case '2':
                        i = (i * 31) + next;
                        putChar('\u0002');
                        break;
                    case EventDispatcher.TRAFFIC_ENABLED /*51*/:
                        i = (i * 31) + next;
                        putChar('\u0003');
                        break;
                    case EventDispatcher.TRAFFIC_DISABLED /*52*/:
                        i = (i * 31) + next;
                        putChar('\u0004');
                        break;
                    case '5':
                        i = (i * 31) + next;
                        putChar('\u0005');
                        break;
                    case '6':
                        i = (i * 31) + next;
                        putChar('\u0006');
                        break;
                    case SpeechError.TIP_ERROR_IVP_GENERAL /*55*/:
                        i = (i * 31) + next;
                        putChar('\u0007');
                        break;
                    case SpeechError.TIP_ERROR_OVERFLOW_IN_GROUP /*70*/:
                    case 'f':
                        i = (i * 31) + 12;
                        putChar('\f');
                        break;
                    case '\\':
                        i = (i * 31) + 92;
                        putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                        break;
                    case 'b':
                        i = (i * 31) + 8;
                        putChar('\b');
                        break;
                    case 'n':
                        i = (i * 31) + 10;
                        putChar('\n');
                        break;
                    case 'r':
                        i = (i * 31) + 13;
                        putChar('\r');
                        break;
                    case 't':
                        i = (i * 31) + 9;
                        putChar('\t');
                        break;
                    case 'u':
                        next = next();
                        next2 = next();
                        char next3 = next();
                        char next4 = next();
                        int parseInt = Integer.parseInt(new String(new char[]{next, next2, next3, next4}), 16);
                        i = (i * 31) + parseInt;
                        putChar((char) parseInt);
                        break;
                    case 'v':
                        i = (i * 31) + 11;
                        putChar('\u000b');
                        break;
                    case 'x':
                        next = next();
                        this.ch = next;
                        next2 = next();
                        this.ch = next2;
                        next = (char) ((digits[next] * 16) + digits[next2]);
                        i = (i * 31) + next;
                        putChar(next);
                        break;
                    default:
                        this.ch = next;
                        throw new JSONException("unclosed.str.lit");
                }
            } else {
                i = (i * 31) + next;
                if (c2 == '\u0000') {
                    this.sp++;
                } else if (this.sp == this.sbuf.length) {
                    putChar(next);
                } else {
                    char[] cArr = this.sbuf;
                    int i3 = this.sp;
                    this.sp = i3 + 1;
                    cArr[i3] = next;
                }
            }
        }
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean[] zArr = com.alibaba.fastjson.util.IOUtils.firstIdentifierFlags;
        int i = this.ch;
        int i2 = (this.ch >= zArr.length || zArr[i]) ? 1 : 0;
        if (i2 == 0) {
            throw new JSONException("illegal identifier : " + this.ch);
        }
        zArr = com.alibaba.fastjson.util.IOUtils.identifierFlags;
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char next = next();
            if (next < zArr.length && !zArr[next]) {
                break;
            }
            i = (i * 31) + next;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && i == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
            return null;
        }
        return addSymbol(this.np, this.sp, i, symbolTable);
    }

    public final void scanString() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            } else if (next == '\u001a') {
                throw new JSONException("unclosed string : " + next);
            } else if (next == IOUtils.DIR_SEPARATOR_WINDOWS) {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp >= this.sbuf.length) {
                        int length = this.sbuf.length * 2;
                        if (this.sp > length) {
                            length = this.sp;
                        }
                        Object obj = new char[length];
                        System.arraycopy(this.sbuf, 0, obj, 0, this.sbuf.length);
                        this.sbuf = obj;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                next = next();
                switch (next) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case MotionEventCompat.AXIS_GENERIC_16 /*47*/:
                        putChar(IOUtils.DIR_SEPARATOR_UNIX);
                        break;
                    case k_event.V_WM_LONGPRESS /*48*/:
                        putChar('\u0000');
                        break;
                    case k_event.V_WM_STREET_JUMP /*49*/:
                        putChar('\u0001');
                        break;
                    case '2':
                        putChar('\u0002');
                        break;
                    case EventDispatcher.TRAFFIC_ENABLED /*51*/:
                        putChar('\u0003');
                        break;
                    case EventDispatcher.TRAFFIC_DISABLED /*52*/:
                        putChar('\u0004');
                        break;
                    case '5':
                        putChar('\u0005');
                        break;
                    case '6':
                        putChar('\u0006');
                        break;
                    case SpeechError.TIP_ERROR_IVP_GENERAL /*55*/:
                        putChar('\u0007');
                        break;
                    case SpeechError.TIP_ERROR_OVERFLOW_IN_GROUP /*70*/:
                    case 'f':
                        putChar('\f');
                        break;
                    case '\\':
                        putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case 'r':
                        putChar('\r');
                        break;
                    case 't':
                        putChar('\t');
                        break;
                    case 'u':
                        next = next();
                        char next2 = next();
                        char next3 = next();
                        char next4 = next();
                        putChar((char) Integer.parseInt(new String(new char[]{next, next2, next3, next4}), 16));
                        break;
                    case 'v':
                        putChar('\u000b');
                        break;
                    case 'x':
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = next;
                        throw new JSONException("unclosed string : " + next);
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(next);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = next;
            }
        }
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public final int intValue() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (this.np == -1) {
            this.np = 0;
        }
        int i5 = this.np;
        int i6 = this.np + this.sp;
        if (charAt(this.np) == '-') {
            i = 1;
            i2 = Integer.MIN_VALUE;
            i3 = i5 + 1;
        } else {
            i2 = -2147483647;
            i = 0;
            i3 = i5;
        }
        char charAt;
        if (i != 0) {
            if (i3 >= i6) {
                i5 = i3 + 1;
                i4 = -digits[charAt(i3)];
            } else {
                i5 = i3;
            }
            while (i5 < i6) {
                i3 = i5 + 1;
                charAt = charAt(i5);
                if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                    break;
                }
                i5 = digits[charAt];
                if (i4 >= -214748364) {
                    throw new NumberFormatException(numberString());
                }
                i4 *= 10;
                if (i4 >= i2 + i5) {
                    throw new NumberFormatException(numberString());
                }
                i4 -= i5;
                i5 = i3;
            }
            i3 = i5;
            if (i != 0) {
                return -i4;
            }
            if (i3 > this.np + 1) {
                return i4;
            }
            throw new NumberFormatException(numberString());
        }
        if (i3 >= i6) {
            i5 = i3;
        } else {
            i5 = i3 + 1;
            i4 = -digits[charAt(i3)];
        }
        while (i5 < i6) {
            i3 = i5 + 1;
            charAt = charAt(i5);
            i5 = digits[charAt];
            if (i4 >= -214748364) {
                i4 *= 10;
                if (i4 >= i2 + i5) {
                    i4 -= i5;
                    i5 = i3;
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
            throw new NumberFormatException(numberString());
        }
        i3 = i5;
        if (i != 0) {
            return -i4;
        }
        if (i3 > this.np + 1) {
            return i4;
        }
        throw new NumberFormatException(numberString());
    }

    public void close() {
        if (this.sbuf.length <= 8192) {
            SBUF_REF_LOCAL.set(new SoftReference(this.sbuf));
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f') {
            return true;
        }
        return false;
    }

    public String scanString(char c) {
        int i = 0;
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt((this.bp + 1) + 1) != 'l' || charAt((this.bp + 1) + 2) != 'l') {
                this.matchStat = -1;
                return null;
            } else if (charAt(this.bp + 4) == c) {
                this.bp += 4;
                next();
                this.matchStat = 3;
                return null;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (charAt != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        } else {
            int i2 = this.bp + 1;
            int indexOf = indexOf('\"', i2);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            String subString = subString(this.bp + 1, indexOf - i2);
            for (int i3 = this.bp + 1; i3 < indexOf; i3++) {
                if (charAt(i3) == IOUtils.DIR_SEPARATOR_WINDOWS) {
                    i = 1;
                    break;
                }
            }
            if (i != 0) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            i = ((indexOf - (this.bp + 1)) + 1) + 1;
            indexOf = i + 1;
            if (charAt(i + this.bp) == c) {
                this.bp += indexOf - 1;
                next();
                this.matchStat = 3;
                return subString;
            }
            this.matchStat = -1;
            return subString;
        }
    }

    public Enum<?> scanEnum(Class<?> cls, SymbolTable symbolTable, char c) {
        String scanSymbolWithSeperator = scanSymbolWithSeperator(symbolTable, c);
        if (scanSymbolWithSeperator == null) {
            return null;
        }
        return Enum.valueOf(cls, scanSymbolWithSeperator);
    }

    public String scanSymbolWithSeperator(SymbolTable symbolTable, char c) {
        int i = 0;
        this.matchStat = 0;
        int i2 = 1;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt((this.bp + 1) + 1) != 'l' || charAt((this.bp + 1) + 2) != 'l') {
                this.matchStat = -1;
                return null;
            } else if (charAt(this.bp + 4) == c) {
                this.bp += 4;
                next();
                this.matchStat = 3;
                return null;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (charAt != '\"') {
            this.matchStat = -1;
            return null;
        } else {
            int i3;
            while (true) {
                i3 = i2 + 1;
                char charAt2 = charAt(i2 + this.bp);
                if (charAt2 == '\"') {
                    break;
                }
                i = (i * 31) + charAt2;
                if (charAt2 == IOUtils.DIR_SEPARATOR_WINDOWS) {
                    this.matchStat = -1;
                    return null;
                }
                i2 = i3;
            }
            i2 = (this.bp + 0) + 1;
            String addSymbol = addSymbol(i2, ((this.bp + i3) - i2) - 1, i, symbolTable);
            int i4 = i3 + 1;
            if (charAt(this.bp + i3) == c) {
                this.bp += i4 - 1;
                next();
                this.matchStat = 3;
                return addSymbol;
            }
            this.matchStat = -1;
            return addSymbol;
        }
    }

    public int scanInt(char c) {
        this.matchStat = 0;
        int i = 1;
        char charAt = charAt(this.bp + 0);
        if (charAt < '0' || charAt > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i2 = digits[charAt];
        while (true) {
            int i3 = i + 1;
            char charAt2 = charAt(i + this.bp);
            if (charAt2 >= '0' && charAt2 <= '9') {
                i2 = (i2 * 10) + digits[charAt2];
                i = i3;
            }
        }
        if (charAt2 == FilenameUtils.EXTENSION_SEPARATOR) {
            this.matchStat = -1;
            return 0;
        } else if (i2 < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt2 == c) {
            this.bp += i3 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return i2;
        } else {
            this.matchStat = -1;
            return i2;
        }
    }

    public long scanLong(char c) {
        this.matchStat = 0;
        int i = 1;
        char charAt = charAt(this.bp + 0);
        if (charAt < '0' || charAt > '9') {
            this.matchStat = -1;
            return 0;
        }
        long j = (long) digits[charAt];
        while (true) {
            int i2 = i + 1;
            char charAt2 = charAt(i + this.bp);
            if (charAt2 >= '0' && charAt2 <= '9') {
                j = (j * 10) + ((long) digits[charAt2]);
                i = i2;
            }
        }
        if (charAt2 == FilenameUtils.EXTENSION_SEPARATOR) {
            this.matchStat = -1;
            return 0;
        } else if (j < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt2 == c) {
            this.bp += i2 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return j;
        } else {
            this.matchStat = -1;
            return j;
        }
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 6;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanTreeSet() {
        if (this.ch != 'T') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 22;
            return;
        }
        throw new JSONException("scan set error");
    }

    public final void scanNullOrNew() {
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        if (this.ch == 'u') {
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
                this.token = 8;
                return;
            }
            throw new JSONException("scan true error");
        } else if (this.ch != 'e') {
            throw new JSONException("error parse e");
        } else {
            next();
            if (this.ch != 'w') {
                throw new JSONException("error parse w");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
                this.token = 9;
                return;
            }
            throw new JSONException("scan true error");
        }
    }

    public final void scanUndefined() {
        if (this.ch != 'u') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'i') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 23;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 7;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        Integer keyword = getKeyword(stringVal());
        if (keyword != null) {
            this.token = keyword.intValue();
        } else {
            this.token = 18;
        }
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            if (charAt == '\u001a') {
                return true;
            }
            if (!isWhitespace(charAt)) {
                return false;
            }
            i++;
        }
    }

    public final void skipWhitespace() {
        while (this.ch < whitespaceFlags.length && whitespaceFlags[this.ch]) {
            next();
        }
    }

    private final void scanStringSingleQuote() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\'') {
                this.token = 4;
                next();
                return;
            } else if (next == '\u001a') {
                throw new JSONException("unclosed single-quote string");
            } else if (next == IOUtils.DIR_SEPARATOR_WINDOWS) {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp > this.sbuf.length) {
                        Object obj = new char[(this.sp * 2)];
                        System.arraycopy(this.sbuf, 0, obj, 0, this.sbuf.length);
                        this.sbuf = obj;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                next = next();
                switch (next) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case MotionEventCompat.AXIS_GENERIC_16 /*47*/:
                        putChar(IOUtils.DIR_SEPARATOR_UNIX);
                        break;
                    case k_event.V_WM_LONGPRESS /*48*/:
                        putChar('\u0000');
                        break;
                    case k_event.V_WM_STREET_JUMP /*49*/:
                        putChar('\u0001');
                        break;
                    case '2':
                        putChar('\u0002');
                        break;
                    case EventDispatcher.TRAFFIC_ENABLED /*51*/:
                        putChar('\u0003');
                        break;
                    case EventDispatcher.TRAFFIC_DISABLED /*52*/:
                        putChar('\u0004');
                        break;
                    case '5':
                        putChar('\u0005');
                        break;
                    case '6':
                        putChar('\u0006');
                        break;
                    case SpeechError.TIP_ERROR_IVP_GENERAL /*55*/:
                        putChar('\u0007');
                        break;
                    case SpeechError.TIP_ERROR_OVERFLOW_IN_GROUP /*70*/:
                    case 'f':
                        putChar('\f');
                        break;
                    case '\\':
                        putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case 'r':
                        putChar('\r');
                        break;
                    case 't':
                        putChar('\t');
                        break;
                    case 'u':
                        next = next();
                        char next2 = next();
                        char next3 = next();
                        char next4 = next();
                        putChar((char) Integer.parseInt(new String(new char[]{next, next2, next3, next4}), 16));
                        break;
                    case 'v':
                        putChar('\u000b');
                        break;
                    case 'x':
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = next;
                        throw new JSONException("unclosed single-quote string");
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(next);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = next;
            }
        }
    }

    public final void scanSet() {
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 21;
            return;
        }
        throw new JSONException("scan set error");
    }

    protected final void putChar(char c) {
        if (this.sp == this.sbuf.length) {
            Object obj = new char[(this.sbuf.length * 2)];
            System.arraycopy(this.sbuf, 0, obj, 0, this.sbuf.length);
            this.sbuf = obj;
        }
        char[] cArr = this.sbuf;
        int i = this.sp;
        this.sp = i + 1;
        cArr[i] = c;
    }

    public final void scanNumber() {
        this.np = this.bp;
        if (this.ch == '-') {
            this.sp++;
            next();
        }
        while (this.ch >= '0' && this.ch <= '9') {
            this.sp++;
            next();
        }
        Object obj = null;
        if (this.ch == FilenameUtils.EXTENSION_SEPARATOR) {
            this.sp++;
            next();
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
            obj = 1;
        }
        if (this.ch == 'L') {
            this.sp++;
            next();
        } else if (this.ch == 'S') {
            this.sp++;
            next();
        } else if (this.ch == 'B') {
            this.sp++;
            next();
        } else if (this.ch == 'F') {
            this.sp++;
            next();
            r0 = 1;
        } else if (this.ch == 'D') {
            this.sp++;
            next();
            r0 = 1;
        } else if (this.ch == 'e' || this.ch == 'E') {
            this.sp++;
            next();
            if (this.ch == '+' || this.ch == '-') {
                this.sp++;
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
            if (this.ch == 'D' || this.ch == 'F') {
                this.sp++;
                next();
            }
            r0 = 1;
        }
        if (obj != null) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public final long longValue() {
        Object obj;
        long j;
        int i;
        long j2 = 0;
        int i2 = this.np;
        int i3 = this.np + this.sp;
        int i4;
        if (charAt(this.np) == '-') {
            obj = 1;
            i4 = i2 + 1;
            j = Long.MIN_VALUE;
            i = i4;
        } else {
            obj = null;
            i4 = i2;
            j = -9223372036854775807L;
            i = i4;
        }
        int i5;
        char charAt;
        if (obj != null) {
            if (i >= i3) {
                i5 = i + 1;
                j2 = (long) (-digits[charAt(i)]);
            } else {
                i5 = i;
            }
            while (i5 < i3) {
                i = i5 + 1;
                charAt = charAt(i5);
                if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                    break;
                }
                i5 = digits[charAt];
                if (j2 >= -922337203685477580L) {
                    throw new NumberFormatException(numberString());
                }
                j2 *= 10;
                if (j2 >= ((long) i5) + j) {
                    throw new NumberFormatException(numberString());
                }
                j2 -= (long) i5;
                i5 = i;
            }
            i = i5;
            if (obj != null) {
                return -j2;
            }
            if (i > this.np + 1) {
                return j2;
            }
            throw new NumberFormatException(numberString());
        }
        if (i >= i3) {
            i5 = i;
        } else {
            i5 = i + 1;
            j2 = (long) (-digits[charAt(i)]);
        }
        while (i5 < i3) {
            i = i5 + 1;
            charAt = charAt(i5);
            i5 = digits[charAt];
            if (j2 >= -922337203685477580L) {
                j2 *= 10;
                if (j2 >= ((long) i5) + j) {
                    j2 -= (long) i5;
                    i5 = i;
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
            throw new NumberFormatException(numberString());
        }
        i = i5;
        if (obj != null) {
            return -j2;
        }
        if (i > this.np + 1) {
            return j2;
        }
        throw new NumberFormatException(numberString());
    }

    public final Number decimalValue(boolean z) {
        char charAt = charAt((this.np + this.sp) - 1);
        if (charAt == 'F') {
            return Float.valueOf(Float.parseFloat(numberString()));
        }
        if (charAt == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        }
        if (z) {
            return decimalValue();
        }
        return Double.valueOf(doubleValue());
    }

    public final BigDecimal decimalValue() {
        return new BigDecimal(numberString());
    }

    public static final boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b';
    }

    public Integer getKeyword(String str) {
        return (Integer) this.keywods.get(str);
    }
}
