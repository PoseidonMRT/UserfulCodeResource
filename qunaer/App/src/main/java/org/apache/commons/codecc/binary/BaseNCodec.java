package org.apache.commons.codecc.binary;

import org.apache.commons.codecc.BinaryDecoder;
import org.apache.commons.codecc.BinaryEncoder;
import org.apache.commons.codecc.DecoderException;
import org.apache.commons.codecc.EncoderException;

public abstract class BaseNCodec implements BinaryDecoder, BinaryEncoder {
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected static final int MASK_8BITS = 255;
    public static final int MIME_CHUNK_SIZE = 76;
    protected static final byte PAD_DEFAULT = (byte) 61;
    public static final int PEM_CHUNK_SIZE = 64;
    protected final byte PAD = PAD_DEFAULT;
    protected byte[] buffer;
    private final int chunkSeparatorLength;
    protected int currentLinePos;
    private final int encodedBlockSize;
    protected boolean eof;
    protected final int lineLength;
    protected int modulus;
    protected int pos;
    private int readPos;
    private final int unencodedBlockSize;

    abstract void decode(byte[] bArr, int i, int i2);

    abstract void encode(byte[] bArr, int i, int i2);

    protected abstract boolean isInAlphabet(byte b);

    protected BaseNCodec(int i, int i2, int i3, int i4) {
        this.unencodedBlockSize = i;
        this.encodedBlockSize = i2;
        int i5 = (i3 <= 0 || i4 <= 0) ? 0 : (i3 / i2) * i2;
        this.lineLength = i5;
        this.chunkSeparatorLength = i4;
    }

    boolean hasData() {
        return this.buffer != null;
    }

    int available() {
        return this.buffer != null ? this.pos - this.readPos : 0;
    }

    protected int getDefaultBufferSize() {
        return 8192;
    }

    private void resizeBuffer() {
        if (this.buffer == null) {
            this.buffer = new byte[getDefaultBufferSize()];
            this.pos = 0;
            this.readPos = 0;
            return;
        }
        Object obj = new byte[(this.buffer.length * 2)];
        System.arraycopy(this.buffer, 0, obj, 0, this.buffer.length);
        this.buffer = obj;
    }

    protected void ensureBufferSize(int i) {
        if (this.buffer == null || this.buffer.length < this.pos + i) {
            resizeBuffer();
        }
    }

    int readResults(byte[] bArr, int i, int i2) {
        if (this.buffer == null) {
            return this.eof ? -1 : 0;
        } else {
            int min = Math.min(available(), i2);
            System.arraycopy(this.buffer, this.readPos, bArr, i, min);
            this.readPos += min;
            if (this.readPos < this.pos) {
                return min;
            }
            this.buffer = null;
            return min;
        }
    }

    protected static boolean isWhiteSpace(byte b) {
        switch (b) {
            case (byte) 9:
            case (byte) 10:
            case (byte) 13:
            case (byte) 32:
                return true;
            default:
                return false;
        }
    }

    private void reset() {
        this.buffer = null;
        this.pos = 0;
        this.readPos = 0;
        this.currentLinePos = 0;
        this.modulus = 0;
        this.eof = false;
    }

    public Object encode(Object obj) {
        if (obj instanceof byte[]) {
            return encode((byte[]) obj);
        }
        throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
    }

    public String encodeToString(byte[] bArr) {
        return StringUtils.newStringUtf8(encode(bArr));
    }

    public Object decode(Object obj) {
        if (obj instanceof byte[]) {
            return decode((byte[]) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
    }

    public byte[] decode(String str) {
        return decode(StringUtils.getBytesUtf8(str));
    }

    public byte[] decode(byte[] bArr) {
        reset();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        decode(bArr, 0, bArr.length);
        decode(bArr, 0, -1);
        bArr = new byte[this.pos];
        readResults(bArr, 0, bArr.length);
        return bArr;
    }

    public byte[] encode(byte[] bArr) {
        reset();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        encode(bArr, 0, bArr.length);
        encode(bArr, 0, -1);
        bArr = new byte[(this.pos - this.readPos)];
        readResults(bArr, 0, bArr.length);
        return bArr;
    }

    public String encodeAsString(byte[] bArr) {
        return StringUtils.newStringUtf8(encode(bArr));
    }

    public boolean isInAlphabet(byte[] bArr, boolean z) {
        int i = 0;
        while (i < bArr.length) {
            if (!isInAlphabet(bArr[i])) {
                if (!z) {
                    return false;
                }
                if (!(bArr[i] == PAD_DEFAULT || isWhiteSpace(bArr[i]))) {
                    return false;
                }
            }
            i++;
        }
        return true;
    }

    public boolean isInAlphabet(String str) {
        return isInAlphabet(StringUtils.getBytesUtf8(str), true);
    }

    protected boolean containsAlphabetOrPad(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        int i = 0;
        while (i < bArr.length) {
            if (PAD_DEFAULT == bArr[i] || isInAlphabet(bArr[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    public long getEncodedLength(byte[] bArr) {
        long length = ((long) (((bArr.length + this.unencodedBlockSize) - 1) / this.unencodedBlockSize)) * ((long) this.encodedBlockSize);
        if (this.lineLength > 0) {
            return length + ((((((long) this.lineLength) + length) - 1) / ((long) this.lineLength)) * ((long) this.chunkSeparatorLength));
        }
        return length;
    }
}
