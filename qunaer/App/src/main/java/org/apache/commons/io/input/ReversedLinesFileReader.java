package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codecc.CharEncoding;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class ReversedLinesFileReader implements Closeable {
    private final int avoidNewlineSplitBufferSize;
    private final int blockSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private final Charset encoding;
    private final byte[][] newLineSequences;
    private final RandomAccessFile randomAccessFile;
    private final long totalBlockCount;
    private final long totalByteLength;
    private boolean trailingNewlineOfFileSkipped;

    class FilePart {
        private int currentLastBytePos;
        private final byte[] data;
        private byte[] leftOver;
        private final long no;

        private FilePart(long j, int i, byte[] bArr) {
            this.no = j;
            this.data = new byte[((bArr != null ? bArr.length : 0) + i)];
            long access$300 = (j - 1) * ((long) ReversedLinesFileReader.this.blockSize);
            if (j > 0) {
                ReversedLinesFileReader.this.randomAccessFile.seek(access$300);
                if (ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, i) != i) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (bArr != null) {
                System.arraycopy(bArr, 0, this.data, i, bArr.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        private FilePart rollOver() {
            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            } else if (this.no > 1) {
                return new FilePart(this.no - 1, ReversedLinesFileReader.this.blockSize, this.leftOver);
            } else {
                if (this.leftOver == null) {
                    return null;
                }
                throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
            }
        }

        private String readLine() {
            String str;
            int i = this.no == 1 ? 1 : 0;
            int i2 = this.currentLastBytePos;
            while (i2 > -1) {
                if (i == 0 && i2 < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                    createLeftOver();
                    str = null;
                    break;
                }
                int newLineMatchByteCount = getNewLineMatchByteCount(this.data, i2);
                if (newLineMatchByteCount <= 0) {
                    i2 -= ReversedLinesFileReader.this.byteDecrement;
                    if (i2 < 0) {
                        createLeftOver();
                        str = null;
                        break;
                    }
                }
                int i3 = i2 + 1;
                int i4 = (this.currentLastBytePos - i3) + 1;
                if (i4 < 0) {
                    throw new IllegalStateException("Unexpected negative line length=" + i4);
                }
                Object obj = new byte[i4];
                System.arraycopy(this.data, i3, obj, 0, i4);
                str = new String(obj, ReversedLinesFileReader.this.encoding);
                this.currentLastBytePos = i2 - newLineMatchByteCount;
            }
            str = null;
            if (i == 0 || this.leftOver == null) {
                return str;
            }
            String str2 = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
            this.leftOver = null;
            return str2;
        }

        private void createLeftOver() {
            int i = this.currentLastBytePos + 1;
            if (i > 0) {
                this.leftOver = new byte[i];
                System.arraycopy(this.data, 0, this.leftOver, 0, i);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] bArr, int i) {
            for (byte[] bArr2 : ReversedLinesFileReader.this.newLineSequences) {
                int length = bArr2.length - 1;
                int i2 = 1;
                while (length >= 0) {
                    int length2 = (i + length) - (bArr2.length - 1);
                    if (length2 < 0 || bArr[length2] != bArr2[length]) {
                        length2 = 0;
                    } else {
                        length2 = 1;
                    }
                    i2 &= length2;
                    length--;
                }
                if (i2 != 0) {
                    return bArr2.length;
                }
            }
            return 0;
        }
    }

    public ReversedLinesFileReader(File file) {
        this(file, 4096, Charset.defaultCharset().toString());
    }

    public ReversedLinesFileReader(File file, int i, Charset charset) {
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = i;
        this.encoding = charset;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        int i2 = (int) (this.totalByteLength % ((long) i));
        if (i2 > 0) {
            this.totalBlockCount = (this.totalByteLength / ((long) i)) + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / ((long) i);
            if (this.totalByteLength > 0) {
                i2 = i;
            }
        }
        this.currentFilePart = new FilePart(this.totalBlockCount, i2, null);
        Charset toCharset = Charsets.toCharset(charset);
        if (toCharset.newEncoder().maxBytesPerChar() == 1.0f) {
            this.byteDecrement = 1;
        } else if (toCharset == Charset.forName("UTF-8")) {
            this.byteDecrement = 1;
        } else if (toCharset == Charset.forName("Shift_JIS")) {
            this.byteDecrement = 1;
        } else if (toCharset == Charset.forName(CharEncoding.UTF_16BE) || toCharset == Charset.forName(CharEncoding.UTF_16LE)) {
            this.byteDecrement = 2;
        } else if (toCharset == Charset.forName("UTF-16")) {
            throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
        } else {
            throw new UnsupportedEncodingException("Encoding " + charset + " is not supported yet (feel free to submit a patch)");
        }
        this.newLineSequences = new byte[][]{IOUtils.LINE_SEPARATOR_WINDOWS.getBytes(charset), IOUtils.LINE_SEPARATOR_UNIX.getBytes(charset), "\r".getBytes(charset)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
    }

    public ReversedLinesFileReader(File file, int i, String str) {
        this(file, i, Charsets.toCharset(str));
    }

    public String readLine() {
        String access$100 = this.currentFilePart.readLine();
        while (access$100 == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
            access$100 = this.currentFilePart.readLine();
        }
        if (!"".equals(access$100) || this.trailingNewlineOfFileSkipped) {
            return access$100;
        }
        this.trailingNewlineOfFileSkipped = true;
        return readLine();
    }

    public void close() {
        this.randomAccessFile.close();
    }
}
