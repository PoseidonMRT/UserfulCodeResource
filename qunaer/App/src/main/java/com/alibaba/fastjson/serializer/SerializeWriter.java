package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.nio.charset.Charset;

public final class SerializeWriter extends Writer {
    private static final ThreadLocal<SoftReference<char[]>> bufLocal = new ThreadLocal();
    protected char[] buf;
    protected int count;
    private int features;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer) {
        this.writer = writer;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;
        SoftReference softReference = (SoftReference) bufLocal.get();
        if (softReference != null) {
            this.buf = (char[]) softReference.get();
            bufLocal.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this(null, serializerFeatureArr);
    }

    public SerializeWriter(Writer writer, SerializerFeature... serializerFeatureArr) {
        int i = 0;
        this.writer = writer;
        SoftReference softReference = (SoftReference) bufLocal.get();
        if (softReference != null) {
            this.buf = (char[]) softReference.get();
            bufLocal.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
        for (SerializerFeature mask : serializerFeatureArr) {
            i |= mask.getMask();
        }
        this.features = i;
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int i) {
        this(null, i);
    }

    public SerializeWriter(Writer writer, int i) {
        this.writer = writer;
        if (i <= 0) {
            throw new IllegalArgumentException("Negative initial size: " + i);
        }
        this.buf = new char[i];
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (z) {
            this.features |= serializerFeature.getMask();
        } else {
            this.features &= serializerFeature.getMask() ^ -1;
        }
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return SerializerFeature.isEnabled(this.features, serializerFeature);
    }

    public void write(int i) {
        int i2 = this.count + 1;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                flush();
                i2 = 1;
            }
        }
        this.buf[this.count] = (char) i;
        this.count = i2;
    }

    public void write(char c) {
        int i = this.count + 1;
        if (i > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i);
            } else {
                flush();
                i = 1;
            }
        }
        this.buf[this.count] = c;
        this.count = i;
    }

    public void write(char[] cArr, int i, int i2) {
        if (i < 0 || i > cArr.length || i2 < 0 || i + i2 > cArr.length || i + i2 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            int i3 = this.count + i2;
            int i4;
            if (i3 <= this.buf.length) {
                i4 = i3;
                i3 = i2;
                i2 = i4;
            } else if (this.writer == null) {
                expandCapacity(i3);
                i4 = i3;
                i3 = i2;
                i2 = i4;
            } else {
                do {
                    i3 = this.buf.length - this.count;
                    System.arraycopy(cArr, i, this.buf, this.count, i3);
                    this.count = this.buf.length;
                    flush();
                    i2 -= i3;
                    i += i3;
                } while (i2 > this.buf.length);
                i3 = i2;
            }
            System.arraycopy(cArr, i, this.buf, this.count, i3);
            this.count = i2;
        }
    }

    public void expandCapacity(int i) {
        int length = ((this.buf.length * 3) / 2) + 1;
        if (length >= i) {
            i = length;
        }
        Object obj = new char[i];
        System.arraycopy(this.buf, 0, obj, 0, this.count);
        this.buf = obj;
    }

    public void write(String str, int i, int i2) {
        int i3 = this.count + i2;
        int i4;
        if (i3 <= this.buf.length) {
            i4 = i3;
            i3 = i2;
            i2 = i4;
        } else if (this.writer == null) {
            expandCapacity(i3);
            i4 = i3;
            i3 = i2;
            i2 = i4;
        } else {
            do {
                i3 = this.buf.length - this.count;
                str.getChars(i, i + i3, this.buf, this.count);
                this.count = this.buf.length;
                flush();
                i2 -= i3;
                i += i3;
            } while (i2 > this.buf.length);
            i3 = i2;
        }
        str.getChars(i, i3 + i, this.buf, this.count);
        this.count = i2;
    }

    public void writeTo(Writer writer) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        writer.write(this.buf, 0, this.count);
    }

    public void writeTo(OutputStream outputStream, String str) {
        writeTo(outputStream, Charset.forName(str));
    }

    public void writeTo(OutputStream outputStream, Charset charset) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        outputStream.write(new String(this.buf, 0, this.count).getBytes(charset.name()));
    }

    public SerializeWriter append(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? "null" : charSequence.toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        String charSequence2 = charSequence.subSequence(i, i2).toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write(c);
        return this;
    }

    public void reset() {
        this.count = 0;
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        Object obj = new char[this.count];
        System.arraycopy(this.buf, 0, obj, 0, this.count);
        return obj;
    }

    public byte[] toBytes(String str) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        if (str == null) {
            str = "UTF-8";
        }
        try {
            return new String(this.buf, 0, this.count).getBytes(str);
        } catch (Throwable e) {
            throw new JSONException("toBytes error", e);
        }
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 8192) {
            bufLocal.set(new SoftReference(this.buf));
        }
        this.buf = null;
    }

    public void write(String str) {
        if (str == null) {
            writeNull();
        } else {
            write(str, 0, str.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int stringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int i2 = this.count + stringSize;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                char[] cArr = new char[stringSize];
                IOUtils.getChars((long) i, stringSize, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
        }
        IOUtils.getChars((long) i, i2, this.buf);
        this.count = i2;
    }

    public void writeByteArray(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        boolean isEnabled = isEnabled(SerializerFeature.UseSingleQuotes);
        char c = isEnabled ? '\'' : '\"';
        if (length == 0) {
            write(isEnabled ? "''" : "\"\"");
            return;
        }
        char c2;
        char[] cArr = Base64.CA;
        int i2 = (length / 3) * 3;
        int i3 = (((length - 1) / 3) + 1) << 2;
        int i4 = this.count;
        int i5 = (i3 + this.count) + 2;
        if (i5 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                i3 = 0;
                while (i3 < i2) {
                    i4 = i3 + 1;
                    i5 = i4 + 1;
                    i4 = ((bArr[i4] & 255) << 8) | ((bArr[i3] & 255) << 16);
                    i3 = i5 + 1;
                    i4 |= bArr[i5] & 255;
                    write(cArr[(i4 >>> 18) & 63]);
                    write(cArr[(i4 >>> 12) & 63]);
                    write(cArr[(i4 >>> 6) & 63]);
                    write(cArr[i4 & 63]);
                }
                i3 = length - i2;
                if (i3 > 0) {
                    i4 = (bArr[i2] & 255) << 10;
                    if (i3 == 2) {
                        i = (bArr[length - 1] & 255) << 2;
                    }
                    i |= i4;
                    write(cArr[i >> 12]);
                    write(cArr[(i >>> 6) & 63]);
                    if (i3 == 2) {
                        c2 = cArr[i & 63];
                    } else {
                        c2 = '=';
                    }
                    write(c2);
                    write('=');
                }
                write(c);
                return;
            }
            expandCapacity(i5);
        }
        this.count = i5;
        i3 = i4 + 1;
        this.buf[i4] = c;
        i4 = 0;
        while (i4 < i2) {
            int i6 = i4 + 1;
            int i7 = i6 + 1;
            i6 = ((bArr[i6] & 255) << 8) | ((bArr[i4] & 255) << 16);
            i4 = i7 + 1;
            i6 |= bArr[i7] & 255;
            int i8 = i3 + 1;
            this.buf[i3] = cArr[(i6 >>> 18) & 63];
            i7 = i8 + 1;
            this.buf[i8] = cArr[(i6 >>> 12) & 63];
            i8 = i7 + 1;
            this.buf[i7] = cArr[(i6 >>> 6) & 63];
            i3 = i8 + 1;
            this.buf[i8] = cArr[i6 & 63];
        }
        i3 = length - i2;
        if (i3 > 0) {
            i4 = (bArr[i2] & 255) << 10;
            if (i3 == 2) {
                i = (bArr[length - 1] & 255) << 2;
            }
            i |= i4;
            this.buf[i5 - 5] = cArr[i >> 12];
            this.buf[i5 - 4] = cArr[(i >>> 6) & 63];
            char[] cArr2 = this.buf;
            length = i5 - 3;
            if (i3 == 2) {
                c2 = cArr[i & 63];
            } else {
                c2 = '=';
            }
            cArr2[length] = c2;
            this.buf[i5 - 2] = '=';
        }
        this.buf[i5 - 1] = c;
    }

    public void writeLongAndChar(long j, char c) {
        if (j == Long.MIN_VALUE) {
            write("-9223372036854775808");
            write(c);
            return;
        }
        int stringSize = (j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j)) + this.count;
        int i = stringSize + 1;
        if (i > this.buf.length) {
            if (this.writer != null) {
                writeLong(j);
                write(c);
                return;
            }
            expandCapacity(i);
        }
        IOUtils.getChars(j, stringSize, this.buf);
        this.buf[stringSize] = c;
        this.count = i;
    }

    public void writeLong(long j) {
        if (j == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }
        int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int i = this.count + stringSize;
        if (i > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i);
            } else {
                char[] cArr = new char[stringSize];
                IOUtils.getChars(j, stringSize, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
        }
        IOUtils.getChars(j, i, this.buf);
        this.count = i;
    }

    public void writeNull() {
        write("null");
    }

    private void writeStringWithDoubleQuote(String str, char c) {
        writeStringWithDoubleQuote(str, c, true);
    }

    private void writeStringWithDoubleQuote(String str, char c, boolean z) {
        if (str == null) {
            writeNull();
            if (c != '\u0000') {
                write(c);
                return;
            }
            return;
        }
        int length = str.length();
        int i = (this.count + length) + 2;
        if (c != '\u0000') {
            i++;
        }
        if (i > this.buf.length) {
            if (this.writer != null) {
                write('\"');
                for (i = 0; i < str.length(); i++) {
                    char charAt = str.charAt(i);
                    if (!isEnabled(SerializerFeature.BrowserCompatible)) {
                        if ((charAt < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt] != (byte) 0) || (charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                            write(IOUtils.replaceChars[charAt]);
                        }
                        write(charAt);
                    } else if (charAt == '\b' || charAt == '\f' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == '\"' || charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX || charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS) {
                        write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                        write(IOUtils.replaceChars[charAt]);
                    } else if (charAt < ' ') {
                        write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                        write('u');
                        write('0');
                        write('0');
                        write(IOUtils.ASCII_CHARS[charAt * 2]);
                        write(IOUtils.ASCII_CHARS[(charAt * 2) + 1]);
                    } else {
                        if (charAt >= '') {
                            write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                            write('u');
                            write(IOUtils.DIGITS[(charAt >>> 12) & 15]);
                            write(IOUtils.DIGITS[(charAt >>> 8) & 15]);
                            write(IOUtils.DIGITS[(charAt >>> 4) & 15]);
                            write(IOUtils.DIGITS[charAt & 15]);
                        }
                        write(charAt);
                    }
                }
                write('\"');
                if (c != '\u0000') {
                    write(c);
                    return;
                }
                return;
            }
            expandCapacity(i);
        }
        int i2 = this.count + 1;
        int i3 = i2 + length;
        this.buf[this.count] = '\"';
        str.getChars(0, length, this.buf, i2);
        this.count = i;
        int i4;
        char c2;
        char c3;
        if (isEnabled(SerializerFeature.BrowserCompatible)) {
            i4 = i;
            i = -1;
            for (length = i2; length < i3; length++) {
                c2 = this.buf[length];
                if (c2 == '\"' || c2 == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX || c2 == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS) {
                    i4++;
                    i = length;
                } else if (c2 == '\b' || c2 == '\f' || c2 == '\n' || c2 == '\r' || c2 == '\t') {
                    i4++;
                    i = length;
                } else if (c2 < ' ') {
                    i4 += 5;
                    i = length;
                } else if (c2 >= '') {
                    i4 += 5;
                    i = length;
                }
            }
            if (i4 > this.buf.length) {
                expandCapacity(i4);
            }
            this.count = i4;
            i = i3;
            for (length = i; length >= i2; length--) {
                c3 = this.buf[length];
                if (c3 == '\b' || c3 == '\f' || c3 == '\n' || c3 == '\r' || c3 == '\t') {
                    System.arraycopy(this.buf, length + 1, this.buf, length + 2, (i - length) - 1);
                    this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    this.buf[length + 1] = IOUtils.replaceChars[c3];
                    i++;
                } else if (c3 == '\"' || c3 == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX || c3 == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS) {
                    System.arraycopy(this.buf, length + 1, this.buf, length + 2, (i - length) - 1);
                    this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    this.buf[length + 1] = c3;
                    i++;
                } else if (c3 < ' ') {
                    System.arraycopy(this.buf, length + 1, this.buf, length + 6, (i - length) - 1);
                    this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    this.buf[length + 1] = 'u';
                    this.buf[length + 2] = '0';
                    this.buf[length + 3] = '0';
                    this.buf[length + 4] = IOUtils.ASCII_CHARS[c3 * 2];
                    this.buf[length + 5] = IOUtils.ASCII_CHARS[(c3 * 2) + 1];
                    i += 5;
                } else if (c3 >= '') {
                    System.arraycopy(this.buf, length + 1, this.buf, length + 6, (i - length) - 1);
                    this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    this.buf[length + 1] = 'u';
                    this.buf[length + 2] = IOUtils.DIGITS[(c3 >>> 12) & 15];
                    this.buf[length + 3] = IOUtils.DIGITS[(c3 >>> 8) & 15];
                    this.buf[length + 4] = IOUtils.DIGITS[(c3 >>> 4) & 15];
                    this.buf[length + 5] = IOUtils.DIGITS[c3 & 15];
                    i += 5;
                }
            }
            if (c != '\u0000') {
                this.buf[this.count - 2] = '\"';
                this.buf[this.count - 1] = c;
                return;
            }
            this.buf[this.count - 1] = '\"';
            return;
        }
        int i5 = 0;
        int i6 = -1;
        c2 = '\u0000';
        if (z) {
            int i7;
            int i8;
            length = i2;
            int i9 = i;
            i = -1;
            while (length < i3) {
                char c4;
                c3 = this.buf[length];
                if (c3 == ' ') {
                    i7 = i5 + 1;
                    i6 = i9 + 4;
                    if (i == -1) {
                        c4 = c3;
                        i5 = i6;
                        i6 = i7;
                        i4 = length;
                        i7 = length;
                    }
                    i5 = i6;
                    i6 = i7;
                    i7 = length;
                    i8 = i;
                    c4 = c3;
                    i4 = i8;
                } else {
                    if (c3 >= ']') {
                        if (c3 >= '' && c3 <= ' ') {
                            if (i == -1) {
                                i = length;
                            }
                            i7 = i5 + 1;
                            i5 = i9 + 4;
                            i6 = i7;
                            i7 = length;
                            i8 = i;
                            c4 = c3;
                            i4 = i8;
                        }
                    } else if (isSpecial(c3, this.features)) {
                        i7 = i5 + 1;
                        if (c3 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c3] != (byte) 4) {
                            i6 = i9;
                        } else {
                            i6 = i9 + 4;
                        }
                        if (i == -1) {
                            c4 = c3;
                            i5 = i6;
                            i6 = i7;
                            i4 = length;
                            i7 = length;
                        }
                        i5 = i6;
                        i6 = i7;
                        i7 = length;
                        i8 = i;
                        c4 = c3;
                        i4 = i8;
                    }
                    i4 = i;
                    c4 = c2;
                    i7 = i6;
                    i6 = i5;
                    i5 = i9;
                }
                length++;
                i9 = i5;
                i5 = i6;
                i6 = i7;
                c2 = c4;
                i = i4;
            }
            if (i5 > 0) {
                length = i9 + i5;
                if (length > this.buf.length) {
                    expandCapacity(length);
                }
                this.count = length;
                if (i5 == 1) {
                    if (c2 == ' ') {
                        System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 6, (i3 - i6) - 1);
                        this.buf[i6] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        length = i6 + 1;
                        this.buf[length] = 'u';
                        length++;
                        this.buf[length] = '2';
                        length++;
                        this.buf[length] = '0';
                        length++;
                        this.buf[length] = '2';
                        this.buf[length + 1] = '8';
                    } else if (c2 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c2] != (byte) 4) {
                        System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 2, (i3 - i6) - 1);
                        this.buf[i6] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        this.buf[i6 + 1] = IOUtils.replaceChars[c2];
                    } else {
                        System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 6, (i3 - i6) - 1);
                        length = i6 + 1;
                        this.buf[i6] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        i4 = length + 1;
                        this.buf[length] = 'u';
                        length = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(c2 >>> 12) & 15];
                        i4 = length + 1;
                        this.buf[length] = IOUtils.DIGITS[(c2 >>> 8) & 15];
                        length = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(c2 >>> 4) & 15];
                        i4 = length + 1;
                        this.buf[length] = IOUtils.DIGITS[c2 & 15];
                    }
                } else if (i5 > 1) {
                    i4 = i3;
                    i8 = i;
                    length = i8;
                    for (i -= i2; i < str.length(); i++) {
                        char charAt2 = str.charAt(i);
                        if ((charAt2 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt2] != (byte) 0) || (charAt2 == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            i5 = length + 1;
                            this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                            if (IOUtils.specicalFlags_doubleQuotes[charAt2] == (byte) 4) {
                                i7 = i5 + 1;
                                this.buf[i5] = 'u';
                                i5 = i7 + 1;
                                this.buf[i7] = IOUtils.DIGITS[(charAt2 >>> 12) & 15];
                                i7 = i5 + 1;
                                this.buf[i5] = IOUtils.DIGITS[(charAt2 >>> 8) & 15];
                                i5 = i7 + 1;
                                this.buf[i7] = IOUtils.DIGITS[(charAt2 >>> 4) & 15];
                                length = i5 + 1;
                                this.buf[i5] = IOUtils.DIGITS[charAt2 & 15];
                                i4 += 5;
                            } else {
                                length = i5 + 1;
                                this.buf[i5] = IOUtils.replaceChars[charAt2];
                                i4++;
                            }
                        } else if (charAt2 == ' ') {
                            i5 = length + 1;
                            this.buf[length] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                            i7 = i5 + 1;
                            this.buf[i5] = 'u';
                            i5 = i7 + 1;
                            this.buf[i7] = IOUtils.DIGITS[(charAt2 >>> 12) & 15];
                            i7 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[(charAt2 >>> 8) & 15];
                            i5 = i7 + 1;
                            this.buf[i7] = IOUtils.DIGITS[(charAt2 >>> 4) & 15];
                            length = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[charAt2 & 15];
                            i4 += 5;
                        } else {
                            i7 = length + 1;
                            this.buf[length] = charAt2;
                            length = i7;
                        }
                    }
                }
            }
        }
        if (c != '\u0000') {
            this.buf[this.count - 2] = '\"';
            this.buf[this.count - 1] = c;
            return;
        }
        this.buf[this.count - 1] = '\"';
    }

    public void write(boolean z) {
        if (z) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char c, String str, long j) {
        if (j == Long.MIN_VALUE || !isEnabled(SerializerFeature.QuoteFieldNames)) {
            writeFieldValue1(c, str, j);
            return;
        }
        char c2 = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
        int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int length = str.length();
        stringSize += (this.count + length) + 4;
        if (stringSize > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeFieldName(str);
                writeLong(j);
                return;
            }
            expandCapacity(stringSize);
        }
        int i = this.count;
        this.count = stringSize;
        this.buf[i] = c;
        stringSize = (i + length) + 1;
        this.buf[i + 1] = c2;
        str.getChars(0, length, this.buf, i + 2);
        this.buf[stringSize + 1] = c2;
        this.buf[stringSize + 2] = ':';
        IOUtils.getChars(j, this.count, this.buf);
    }

    public void writeFieldValue1(char c, String str, long j) {
        write(c);
        writeFieldName(str);
        writeLong(j);
    }

    public void writeFieldValue(char c, String str, String str2) {
        if (!isEnabled(SerializerFeature.QuoteFieldNames)) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
            } else {
                writeString(str2);
            }
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
            } else {
                writeString(str2);
            }
        } else if (isEnabled(SerializerFeature.BrowserCompatible)) {
            write(c);
            writeStringWithDoubleQuote(str, ':');
            writeStringWithDoubleQuote(str2, '\u0000');
        } else {
            writeFieldValueStringWithDoubleQuote(c, str, str2, true);
        }
    }

    private void writeFieldValueStringWithDoubleQuote(char c, String str, String str2, boolean z) {
        int i;
        int length = str.length();
        int i2 = this.count;
        if (str2 == null) {
            i = 4;
            i2 += length + 8;
        } else {
            i = str2.length();
            i2 += (length + i) + 6;
        }
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeStringWithDoubleQuote(str, ':', z);
                writeStringWithDoubleQuote(str2, '\u0000', z);
                return;
            }
            expandCapacity(i2);
        }
        this.buf[this.count] = c;
        int i3 = this.count + 2;
        int i4 = i3 + length;
        this.buf[this.count + 1] = '\"';
        str.getChars(0, length, this.buf, i3);
        this.count = i2;
        this.buf[i4] = '\"';
        length = i4 + 1;
        i4 = length + 1;
        this.buf[length] = ':';
        if (str2 == null) {
            i2 = i4 + 1;
            this.buf[i4] = 'n';
            length = i2 + 1;
            this.buf[i2] = 'u';
            i2 = length + 1;
            this.buf[length] = 'l';
            length = i2 + 1;
            this.buf[i2] = 'l';
            return;
        }
        int i5 = i4 + 1;
        this.buf[i4] = '\"';
        int i6 = i5 + i;
        str2.getChars(0, i, this.buf, i5);
        if (z && !isEnabled(SerializerFeature.DisableCheckSpecialChar)) {
            int i7;
            int i8 = 0;
            i4 = -1;
            i = -1;
            char c2 = '\u0000';
            int i9 = i2;
            i2 = i5;
            while (i2 < i6) {
                char c3;
                char c4 = this.buf[i2];
                if (c4 == ' ') {
                    i3 = i8 + 1;
                    i4 = i9 + 4;
                    if (i == -1) {
                        c3 = c4;
                        i8 = i4;
                        i4 = i3;
                        length = i2;
                        i3 = i2;
                    }
                    i8 = i4;
                    i4 = i3;
                    i3 = i2;
                    i7 = i;
                    c3 = c4;
                    length = i7;
                } else {
                    if (c4 >= ']') {
                        if (c4 >= '' && c4 <= ' ') {
                            if (i == -1) {
                                i = i2;
                            }
                            i3 = i8 + 1;
                            i8 = i9 + 4;
                            i4 = i3;
                            i3 = i2;
                            i7 = i;
                            c3 = c4;
                            length = i7;
                        }
                    } else if (isSpecial(c4, this.features)) {
                        i3 = i8 + 1;
                        if (c4 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c4] != (byte) 4) {
                            i4 = i9;
                        } else {
                            i4 = i9 + 4;
                        }
                        if (i == -1) {
                            c3 = c4;
                            i8 = i4;
                            i4 = i3;
                            length = i2;
                            i3 = i2;
                        }
                        i8 = i4;
                        i4 = i3;
                        i3 = i2;
                        i7 = i;
                        c3 = c4;
                        length = i7;
                    }
                    length = i;
                    c3 = c2;
                    i3 = i4;
                    i4 = i8;
                    i8 = i9;
                }
                i2++;
                i9 = i8;
                i8 = i4;
                i4 = i3;
                c2 = c3;
                i = length;
            }
            if (i8 > 0) {
                i2 = i9 + i8;
                if (i2 > this.buf.length) {
                    expandCapacity(i2);
                }
                this.count = i2;
                if (i8 == 1) {
                    if (c2 == ' ') {
                        System.arraycopy(this.buf, i4 + 1, this.buf, i4 + 6, (i6 - i4) - 1);
                        this.buf[i4] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        i2 = i4 + 1;
                        this.buf[i2] = 'u';
                        i2++;
                        this.buf[i2] = '2';
                        i2++;
                        this.buf[i2] = '0';
                        i2++;
                        this.buf[i2] = '2';
                        this.buf[i2 + 1] = '8';
                    } else if (c2 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c2] != (byte) 4) {
                        System.arraycopy(this.buf, i4 + 1, this.buf, i4 + 2, (i6 - i4) - 1);
                        this.buf[i4] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        this.buf[i4 + 1] = IOUtils.replaceChars[c2];
                    } else {
                        System.arraycopy(this.buf, i4 + 1, this.buf, i4 + 6, (i6 - i4) - 1);
                        i2 = i4 + 1;
                        this.buf[i4] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                        length = i2 + 1;
                        this.buf[i2] = 'u';
                        i2 = length + 1;
                        this.buf[length] = IOUtils.DIGITS[(c2 >>> 12) & 15];
                        length = i2 + 1;
                        this.buf[i2] = IOUtils.DIGITS[(c2 >>> 8) & 15];
                        i2 = length + 1;
                        this.buf[length] = IOUtils.DIGITS[(c2 >>> 4) & 15];
                        length = i2 + 1;
                        this.buf[i2] = IOUtils.DIGITS[c2 & 15];
                    }
                } else if (i8 > 1) {
                    length = i6;
                    i7 = i;
                    i2 = i7;
                    for (i -= i5; i < str2.length(); i++) {
                        char charAt = str2.charAt(i);
                        if ((charAt < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt] != (byte) 0) || (charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            i8 = i2 + 1;
                            this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                            if (IOUtils.specicalFlags_doubleQuotes[charAt] == (byte) 4) {
                                i3 = i8 + 1;
                                this.buf[i8] = 'u';
                                i8 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[(charAt >>> 12) & 15];
                                i3 = i8 + 1;
                                this.buf[i8] = IOUtils.DIGITS[(charAt >>> 8) & 15];
                                i8 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[(charAt >>> 4) & 15];
                                i2 = i8 + 1;
                                this.buf[i8] = IOUtils.DIGITS[charAt & 15];
                                length += 5;
                            } else {
                                i2 = i8 + 1;
                                this.buf[i8] = IOUtils.replaceChars[charAt];
                                length++;
                            }
                        } else if (charAt == ' ') {
                            i8 = i2 + 1;
                            this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                            i3 = i8 + 1;
                            this.buf[i8] = 'u';
                            i8 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(charAt >>> 12) & 15];
                            i3 = i8 + 1;
                            this.buf[i8] = IOUtils.DIGITS[(charAt >>> 8) & 15];
                            i8 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(charAt >>> 4) & 15];
                            i2 = i8 + 1;
                            this.buf[i8] = IOUtils.DIGITS[charAt & 15];
                            length += 5;
                        } else {
                            i3 = i2 + 1;
                            this.buf[i2] = charAt;
                            i2 = i3;
                        }
                    }
                }
            }
        }
        this.buf[this.count - 1] = '\"';
    }

    static final boolean isSpecial(char c, int i) {
        if (c == ' ') {
            return false;
        }
        if (c == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && SerializerFeature.isEnabled(i, SerializerFeature.WriteSlashAsSpecial)) {
            return true;
        }
        if (c > '#' && c != org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS) {
            return false;
        }
        if (c <= '\u001f' || c == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS || c == '\"') {
            return true;
        }
        return false;
    }

    public void writeString(String str) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeStringWithSingleQuote(str);
        } else {
            writeStringWithDoubleQuote(str, '\u0000');
        }
    }

    private void writeStringWithSingleQuote(String str) {
        int i = 0;
        int i2;
        if (str == null) {
            i2 = this.count + 4;
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = i2;
            return;
        }
        char charAt;
        i2 = str.length();
        int i3 = (this.count + i2) + 2;
        if (i3 > this.buf.length) {
            if (this.writer != null) {
                write('\'');
                while (i < str.length()) {
                    charAt = str.charAt(i);
                    if (charAt <= '\r' || charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS || charAt == '\'' || (charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                        write(IOUtils.replaceChars[charAt]);
                    } else {
                        write(charAt);
                    }
                    i++;
                }
                write('\'');
                return;
            }
            expandCapacity(i3);
        }
        int i4 = this.count + 1;
        int i5 = i4 + i2;
        this.buf[this.count] = '\'';
        str.getChars(0, i2, this.buf, i4);
        this.count = i3;
        int i6 = -1;
        int i7 = i4;
        int i8 = 0;
        while (i7 < i5) {
            charAt = this.buf[i7];
            if (charAt <= '\r' || charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS || charAt == '\'' || (charAt == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                i6 = i8 + 1;
                i = charAt;
                i2 = i7;
            } else {
                i2 = i6;
                i6 = i8;
            }
            i7++;
            i8 = i6;
            i6 = i2;
        }
        i2 = i3 + i8;
        if (i2 > this.buf.length) {
            expandCapacity(i2);
        }
        this.count = i2;
        if (i8 == 1) {
            System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
            this.buf[i6 + 1] = IOUtils.replaceChars[i];
        } else if (i8 > 1) {
            System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
            i6++;
            this.buf[i6] = IOUtils.replaceChars[i];
            i = i5 + 1;
            for (i2 = i6 - 2; i2 >= i4; i2--) {
                char c = this.buf[i2];
                if (c <= '\r' || c == org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS || c == '\'' || (c == org.apache.commons.io.IOUtils.DIR_SEPARATOR_UNIX && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    System.arraycopy(this.buf, i2 + 1, this.buf, i2 + 2, (i - i2) - 1);
                    this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    this.buf[i2 + 1] = IOUtils.replaceChars[c];
                    i++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String str) {
        writeFieldName(str, false);
    }

    public void writeFieldName(String str, boolean z) {
        if (str == null) {
            write("null:");
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            if (isEnabled(SerializerFeature.QuoteFieldNames)) {
                writeStringWithSingleQuote(str);
                write(':');
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(str);
        } else if (isEnabled(SerializerFeature.QuoteFieldNames)) {
            writeStringWithDoubleQuote(str, ':', z);
        } else {
            writeKeyWithDoubleQuoteIfHasSpecial(str);
        }
    }

    private void writeKeyWithDoubleQuoteIfHasSpecial(String str) {
        int i;
        Object obj;
        int i2;
        byte[] bArr = IOUtils.specicalFlags_doubleQuotes;
        int length = str.length();
        int i3 = (this.count + length) + 1;
        if (i3 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i3);
            } else if (length == 0) {
                write('\"');
                write('\"');
                write(':');
                return;
            } else {
                char charAt;
                for (i = 0; i < length; i++) {
                    charAt = str.charAt(i);
                    if (charAt < bArr.length && bArr[charAt] != (byte) 0) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj != null) {
                    write('\"');
                }
                for (i2 = 0; i2 < length; i2++) {
                    charAt = str.charAt(i2);
                    if (charAt >= bArr.length || bArr[charAt] == (byte) 0) {
                        write(charAt);
                    } else {
                        write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                        write(IOUtils.replaceChars[charAt]);
                    }
                }
                if (obj != null) {
                    write('\"');
                }
                write(':');
                return;
            }
        }
        if (length == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = '\"';
            cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = '\"';
            cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = ':';
            return;
        }
        int i4 = this.count;
        int i5 = i4 + length;
        str.getChars(0, length, this.buf, i4);
        this.count = i3;
        obj = null;
        i2 = i4;
        while (i2 < i5) {
            char c = this.buf[i2];
            if (c < bArr.length && bArr[c] != (byte) 0) {
                if (obj == null) {
                    i3 += 3;
                    if (i3 > this.buf.length) {
                        expandCapacity(i3);
                    }
                    this.count = i3;
                    System.arraycopy(this.buf, i2 + 1, this.buf, i2 + 3, (i5 - i2) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i2);
                    this.buf[i4] = '\"';
                    i2++;
                    this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    i2++;
                    this.buf[i2] = IOUtils.replaceChars[c];
                    i5 += 2;
                    this.buf[this.count - 2] = '\"';
                    obj = 1;
                } else {
                    i3++;
                    if (i3 > this.buf.length) {
                        expandCapacity(i3);
                    }
                    this.count = i3;
                    System.arraycopy(this.buf, i2 + 1, this.buf, i2 + 2, i5 - i2);
                    this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    i2++;
                    this.buf[i2] = IOUtils.replaceChars[c];
                    i5++;
                }
            }
            i2++;
        }
        this.buf[this.count - 1] = ':';
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String str) {
        int i;
        Object obj;
        int i2;
        byte[] bArr = IOUtils.specicalFlags_singleQuotes;
        int length = str.length();
        int i3 = (this.count + length) + 1;
        if (i3 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i3);
            } else if (length == 0) {
                write('\'');
                write('\'');
                write(':');
                return;
            } else {
                char charAt;
                for (i = 0; i < length; i++) {
                    charAt = str.charAt(i);
                    if (charAt < bArr.length && bArr[charAt] != (byte) 0) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj != null) {
                    write('\'');
                }
                for (i2 = 0; i2 < length; i2++) {
                    charAt = str.charAt(i2);
                    if (charAt >= bArr.length || bArr[charAt] == (byte) 0) {
                        write(charAt);
                    } else {
                        write((char) org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS);
                        write(IOUtils.replaceChars[charAt]);
                    }
                }
                if (obj != null) {
                    write('\'');
                }
                write(':');
                return;
            }
        }
        if (length == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = '\'';
            cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = '\'';
            cArr = this.buf;
            i = this.count;
            this.count = i + 1;
            cArr[i] = ':';
            return;
        }
        int i4 = this.count;
        int i5 = i4 + length;
        str.getChars(0, length, this.buf, i4);
        this.count = i3;
        obj = null;
        i2 = i4;
        while (i2 < i5) {
            char c = this.buf[i2];
            if (c < bArr.length && bArr[c] != (byte) 0) {
                if (obj == null) {
                    i3 += 3;
                    if (i3 > this.buf.length) {
                        expandCapacity(i3);
                    }
                    this.count = i3;
                    System.arraycopy(this.buf, i2 + 1, this.buf, i2 + 3, (i5 - i2) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i2);
                    this.buf[i4] = '\'';
                    i2++;
                    this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    i2++;
                    this.buf[i2] = IOUtils.replaceChars[c];
                    i5 += 2;
                    this.buf[this.count - 2] = '\'';
                    obj = 1;
                } else {
                    i3++;
                    if (i3 > this.buf.length) {
                        expandCapacity(i3);
                    }
                    this.count = i3;
                    System.arraycopy(this.buf, i2 + 1, this.buf, i2 + 2, i5 - i2);
                    this.buf[i2] = org.apache.commons.io.IOUtils.DIR_SEPARATOR_WINDOWS;
                    i2++;
                    this.buf[i2] = IOUtils.replaceChars[c];
                    i5++;
                }
            }
            i2++;
        }
        this.buf[i3 - 1] = ':';
    }

    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (Throwable e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }
}
