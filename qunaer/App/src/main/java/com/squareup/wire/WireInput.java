package com.squareup.wire;

import android.support.v4.media.TransportMediator;
import com.mqunar.libtask.ProgressType;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class WireInput {
    private static final int BUFFER_SIZE = 1024;
    private static final String ENCOUNTERED_A_MALFORMED_VARINT = "WireInput encountered a malformed varint.";
    private static final String ENCOUNTERED_A_NEGATIVE_SIZE = "Encountered a negative size";
    private static final String INPUT_ENDED_UNEXPECTEDLY = "The input ended unexpectedly in the middle of a field";
    private static final String PROTOCOL_MESSAGE_CONTAINED_AN_INVALID_TAG_ZERO = "Protocol message contained an invalid tag (zero).";
    private static final String PROTOCOL_MESSAGE_END_GROUP_TAG_DID_NOT_MATCH_EXPECTED_TAG = "Protocol message end-group tag did not match expected tag.";
    public static final int RECURSION_LIMIT = 64;
    private static final String UTF_8 = "UTF-8";
    private final byte[] buffer;
    private long bufferOffset;
    private int currentLimit;
    private final InputStream input;
    private boolean inputStreamAtEof;
    private int lastTag;
    private int limit;
    private int pos;
    public int recursionDepth;

    public static WireInput newInstance(byte[] bArr) {
        return new WireInput(bArr, 0, bArr.length);
    }

    public static WireInput newInstance(byte[] bArr, int i, int i2) {
        return new WireInput(bArr, i, i2);
    }

    public static WireInput newInstance(InputStream inputStream) {
        return new WireInput(inputStream);
    }

    public int readTag() {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readVarint32();
        if (this.lastTag != 0) {
            return this.lastTag;
        }
        throw new IOException(PROTOCOL_MESSAGE_CONTAINED_AN_INVALID_TAG_ZERO);
    }

    public void checkLastTagWas(int i) {
        if (this.lastTag != i) {
            throw new IOException(PROTOCOL_MESSAGE_END_GROUP_TAG_DID_NOT_MATCH_EXPECTED_TAG);
        }
    }

    public String readString() {
        int readVarint32 = readVarint32();
        if (bytesRemaining() < readVarint32) {
            return new String(readRawBytes(readVarint32), "UTF-8");
        }
        String str = new String(this.buffer, this.pos, readVarint32, "UTF-8");
        this.pos = readVarint32 + this.pos;
        return str;
    }

    private int bytesRemaining() {
        return this.limit - this.pos;
    }

    public ByteString readBytes() {
        return readBytes(readVarint32());
    }

    public ByteString readBytes(int i) {
        if (bytesRemaining() < i) {
            return ByteString.of(readRawBytes(i));
        }
        ByteString of = ByteString.of(this.buffer, this.pos, i);
        this.pos += i;
        return of;
    }

    public int readVarint32() {
        byte readRawByte = readRawByte();
        if (readRawByte >= (byte) 0) {
            return readRawByte;
        }
        int i = readRawByte & TransportMediator.KEYCODE_MEDIA_PAUSE;
        byte readRawByte2 = readRawByte();
        if (readRawByte2 >= (byte) 0) {
            return i | (readRawByte2 << 7);
        }
        i |= (readRawByte2 & TransportMediator.KEYCODE_MEDIA_PAUSE) << 7;
        readRawByte2 = readRawByte();
        if (readRawByte2 >= (byte) 0) {
            return i | (readRawByte2 << 14);
        }
        i |= (readRawByte2 & TransportMediator.KEYCODE_MEDIA_PAUSE) << 14;
        readRawByte2 = readRawByte();
        if (readRawByte2 >= (byte) 0) {
            return i | (readRawByte2 << 21);
        }
        i |= (readRawByte2 & TransportMediator.KEYCODE_MEDIA_PAUSE) << 21;
        readRawByte2 = readRawByte();
        i |= readRawByte2 << 28;
        if (readRawByte2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (readRawByte() >= (byte) 0) {
                return i;
            }
        }
        throw new IOException(ENCOUNTERED_A_MALFORMED_VARINT);
    }

    public long readVarint64() {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte readRawByte = readRawByte();
            j |= ((long) (readRawByte & TransportMediator.KEYCODE_MEDIA_PAUSE)) << i;
            if ((readRawByte & 128) == 0) {
                return j;
            }
        }
        throw new IOException(ENCOUNTERED_A_MALFORMED_VARINT);
    }

    public int readFixed32() {
        return (((readRawByte() & 255) | ((readRawByte() & 255) << 8)) | ((readRawByte() & 255) << 16)) | ((readRawByte() & 255) << 24);
    }

    public long readFixed64() {
        byte readRawByte = readRawByte();
        byte readRawByte2 = readRawByte();
        return ((((((((((long) readRawByte2) & 255) << 8) | (((long) readRawByte) & 255)) | ((((long) readRawByte()) & 255) << 16)) | ((((long) readRawByte()) & 255) << 24)) | ((((long) readRawByte()) & 255) << 32)) | ((((long) readRawByte()) & 255) << 40)) | ((((long) readRawByte()) & 255) << 48)) | ((((long) readRawByte()) & 255) << 56);
    }

    public static int decodeZigZag32(int i) {
        return (i >>> 1) ^ (-(i & 1));
    }

    public static long decodeZigZag64(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    private WireInput(InputStream inputStream) {
        this.bufferOffset = 0;
        this.pos = 0;
        this.currentLimit = ProgressType.PRO_END;
        this.input = inputStream;
        this.buffer = new byte[1024];
    }

    private WireInput(byte[] bArr, int i, int i2) {
        this.bufferOffset = 0;
        this.pos = 0;
        this.currentLimit = ProgressType.PRO_END;
        this.input = null;
        this.buffer = bArr;
        this.bufferOffset = (long) (-i);
        this.pos = i;
        this.limit = i + i2;
        this.inputStreamAtEof = true;
    }

    private void refillBuffer(int i) {
        if (this.pos >= this.limit && !this.inputStreamAtEof) {
            this.bufferOffset += (long) this.pos;
            this.pos = 0;
            int min = Math.min(i, 1024);
            int i2 = 0;
            while (i2 < min) {
                int read = this.input.read(this.buffer, i2, 1024 - i2);
                if (read == -1) {
                    this.limit = i2;
                    this.inputStreamAtEof = true;
                    return;
                }
                i2 += read;
            }
            this.limit = i2;
            this.inputStreamAtEof = false;
        }
    }

    public int pushLimit(int i) {
        if (i < 0) {
            throw new IOException(ENCOUNTERED_A_NEGATIVE_SIZE);
        }
        int i2 = (int) (((long) i) + (this.bufferOffset + ((long) this.pos)));
        int i3 = this.currentLimit;
        if (i2 > i3) {
            throw new EOFException(INPUT_ENDED_UNEXPECTEDLY);
        }
        this.currentLimit = i2;
        return i3;
    }

    public void popLimit(int i) {
        this.currentLimit = i;
    }

    public boolean isAtEnd() {
        if (getPosition() == ((long) this.currentLimit)) {
            return true;
        }
        refillBuffer(1);
        if (bytesRemaining() == 0 && this.inputStreamAtEof) {
            return true;
        }
        return false;
    }

    public long getPosition() {
        return this.bufferOffset + ((long) this.pos);
    }

    public byte readRawByte() {
        refillBuffer(1);
        if (bytesRemaining() == 0) {
            throw new EOFException(INPUT_ENDED_UNEXPECTEDLY);
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(int i) {
        if (i < 0) {
            throw new IOException(ENCOUNTERED_A_NEGATIVE_SIZE);
        }
        Object obj = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            refillBuffer(i - i2);
            if (bytesRemaining() == 0) {
                throw new EOFException(INPUT_ENDED_UNEXPECTEDLY);
            }
            int min = Math.min(i - i2, bytesRemaining());
            System.arraycopy(this.buffer, this.pos, obj, i2, min);
            this.pos += min;
            i2 += min;
        }
        return obj;
    }

    public void skipGroup() {
        int readTag;
        do {
            readTag = readTag();
            if (readTag == 0) {
                return;
            }
        } while (!skipField(readTag));
    }

    private boolean skipField(int i) {
        switch (WireType.valueOf(i)) {
            case VARINT:
                readVarint64();
                return false;
            case FIXED32:
                readFixed32();
                return false;
            case FIXED64:
                readFixed64();
                return false;
            case LENGTH_DELIMITED:
                readRawBytes(readVarint32());
                return false;
            case START_GROUP:
                skipGroup();
                checkLastTagWas((i & -8) | WireType.END_GROUP.value());
                return false;
            case END_GROUP:
                return true;
            default:
                throw new AssertionError();
        }
    }
}
