package org.apache.commons.codecc.binary;

import com.alibaba.fastjson.parser.JSONLexer;

public class Base32 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 5;
    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;
    private static final byte[] CHUNK_SEPARATOR = new byte[]{(byte) 13, (byte) 10};
    private static final byte[] DECODE_TABLE;
    private static final byte[] ENCODE_TABLE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55};
    private static final byte[] HEX_DECODE_TABLE;
    private static final byte[] HEX_ENCODE_TABLE = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86};
    private static final int MASK_5BITS = 31;
    private long bitWorkArea;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    static {
        byte[] bArr = new byte[91];
        bArr[0] = (byte) -1;
        bArr[1] = (byte) -1;
        bArr[2] = (byte) -1;
        bArr[3] = (byte) -1;
        bArr[4] = (byte) -1;
        bArr[5] = (byte) -1;
        bArr[6] = (byte) -1;
        bArr[7] = (byte) -1;
        bArr[8] = (byte) -1;
        bArr[9] = (byte) -1;
        bArr[10] = (byte) -1;
        bArr[11] = (byte) -1;
        bArr[12] = (byte) -1;
        bArr[13] = (byte) -1;
        bArr[14] = (byte) -1;
        bArr[15] = (byte) -1;
        bArr[16] = (byte) -1;
        bArr[17] = (byte) -1;
        bArr[18] = (byte) -1;
        bArr[19] = (byte) -1;
        bArr[20] = (byte) -1;
        bArr[21] = (byte) -1;
        bArr[22] = (byte) -1;
        bArr[23] = (byte) -1;
        bArr[24] = (byte) -1;
        bArr[25] = (byte) -1;
        bArr[26] = (byte) -1;
        bArr[27] = (byte) -1;
        bArr[28] = (byte) -1;
        bArr[29] = (byte) -1;
        bArr[30] = (byte) -1;
        bArr[31] = (byte) -1;
        bArr[32] = (byte) -1;
        bArr[33] = (byte) -1;
        bArr[34] = (byte) -1;
        bArr[35] = (byte) -1;
        bArr[36] = (byte) -1;
        bArr[37] = (byte) -1;
        bArr[38] = (byte) -1;
        bArr[39] = (byte) -1;
        bArr[40] = (byte) -1;
        bArr[41] = (byte) -1;
        bArr[42] = (byte) -1;
        bArr[43] = (byte) -1;
        bArr[44] = (byte) -1;
        bArr[45] = (byte) -1;
        bArr[46] = (byte) -1;
        bArr[47] = (byte) 63;
        bArr[48] = (byte) -1;
        bArr[49] = (byte) -1;
        bArr[50] = JSONLexer.EOI;
        bArr[51] = (byte) 27;
        bArr[52] = (byte) 28;
        bArr[53] = (byte) 29;
        bArr[54] = (byte) 30;
        bArr[55] = (byte) 31;
        bArr[56] = (byte) -1;
        bArr[57] = (byte) -1;
        bArr[58] = (byte) -1;
        bArr[59] = (byte) -1;
        bArr[60] = (byte) -1;
        bArr[61] = (byte) -1;
        bArr[62] = (byte) -1;
        bArr[63] = (byte) -1;
        bArr[64] = (byte) -1;
        bArr[66] = (byte) 1;
        bArr[67] = (byte) 2;
        bArr[68] = (byte) 3;
        bArr[69] = (byte) 4;
        bArr[70] = (byte) 5;
        bArr[71] = (byte) 6;
        bArr[72] = (byte) 7;
        bArr[73] = (byte) 8;
        bArr[74] = (byte) 9;
        bArr[75] = (byte) 10;
        bArr[76] = (byte) 11;
        bArr[77] = (byte) 12;
        bArr[78] = (byte) 13;
        bArr[79] = (byte) 14;
        bArr[80] = (byte) 15;
        bArr[81] = (byte) 16;
        bArr[82] = (byte) 17;
        bArr[83] = (byte) 18;
        bArr[84] = (byte) 19;
        bArr[85] = (byte) 20;
        bArr[86] = (byte) 21;
        bArr[87] = (byte) 22;
        bArr[88] = (byte) 23;
        bArr[89] = (byte) 24;
        bArr[90] = (byte) 25;
        DECODE_TABLE = bArr;
        bArr = new byte[88];
        bArr[0] = (byte) -1;
        bArr[1] = (byte) -1;
        bArr[2] = (byte) -1;
        bArr[3] = (byte) -1;
        bArr[4] = (byte) -1;
        bArr[5] = (byte) -1;
        bArr[6] = (byte) -1;
        bArr[7] = (byte) -1;
        bArr[8] = (byte) -1;
        bArr[9] = (byte) -1;
        bArr[10] = (byte) -1;
        bArr[11] = (byte) -1;
        bArr[12] = (byte) -1;
        bArr[13] = (byte) -1;
        bArr[14] = (byte) -1;
        bArr[15] = (byte) -1;
        bArr[16] = (byte) -1;
        bArr[17] = (byte) -1;
        bArr[18] = (byte) -1;
        bArr[19] = (byte) -1;
        bArr[20] = (byte) -1;
        bArr[21] = (byte) -1;
        bArr[22] = (byte) -1;
        bArr[23] = (byte) -1;
        bArr[24] = (byte) -1;
        bArr[25] = (byte) -1;
        bArr[26] = (byte) -1;
        bArr[27] = (byte) -1;
        bArr[28] = (byte) -1;
        bArr[29] = (byte) -1;
        bArr[30] = (byte) -1;
        bArr[31] = (byte) -1;
        bArr[32] = (byte) -1;
        bArr[33] = (byte) -1;
        bArr[34] = (byte) -1;
        bArr[35] = (byte) -1;
        bArr[36] = (byte) -1;
        bArr[37] = (byte) -1;
        bArr[38] = (byte) -1;
        bArr[39] = (byte) -1;
        bArr[40] = (byte) -1;
        bArr[41] = (byte) -1;
        bArr[42] = (byte) -1;
        bArr[43] = (byte) -1;
        bArr[44] = (byte) -1;
        bArr[45] = (byte) -1;
        bArr[46] = (byte) -1;
        bArr[47] = (byte) 63;
        bArr[49] = (byte) 1;
        bArr[50] = (byte) 2;
        bArr[51] = (byte) 3;
        bArr[52] = (byte) 4;
        bArr[53] = (byte) 5;
        bArr[54] = (byte) 6;
        bArr[55] = (byte) 7;
        bArr[56] = (byte) 8;
        bArr[57] = (byte) 9;
        bArr[58] = (byte) -1;
        bArr[59] = (byte) -1;
        bArr[60] = (byte) -1;
        bArr[61] = (byte) -1;
        bArr[62] = (byte) -1;
        bArr[63] = (byte) -1;
        bArr[64] = (byte) -1;
        bArr[65] = (byte) 10;
        bArr[66] = (byte) 11;
        bArr[67] = (byte) 12;
        bArr[68] = (byte) 13;
        bArr[69] = (byte) 14;
        bArr[70] = (byte) 15;
        bArr[71] = (byte) 16;
        bArr[72] = (byte) 17;
        bArr[73] = (byte) 18;
        bArr[74] = (byte) 19;
        bArr[75] = (byte) 20;
        bArr[76] = (byte) 21;
        bArr[77] = (byte) 22;
        bArr[78] = (byte) 23;
        bArr[79] = (byte) 24;
        bArr[80] = (byte) 25;
        bArr[81] = JSONLexer.EOI;
        bArr[82] = (byte) 27;
        bArr[83] = (byte) 28;
        bArr[84] = (byte) 29;
        bArr[85] = (byte) 30;
        bArr[86] = (byte) 31;
        bArr[87] = (byte) 32;
        HEX_DECODE_TABLE = bArr;
    }

    public Base32() {
        this(false);
    }

    public Base32(boolean z) {
        this(0, null, z);
    }

    public Base32(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base32(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base32(int i, byte[] bArr, boolean z) {
        super(5, 8, i, bArr == null ? 0 : bArr.length);
        if (z) {
            this.encodeTable = HEX_ENCODE_TABLE;
            this.decodeTable = HEX_DECODE_TABLE;
        } else {
            this.encodeTable = ENCODE_TABLE;
            this.decodeTable = DECODE_TABLE;
        }
        if (i <= 0) {
            this.encodeSize = 8;
            this.lineSeparator = null;
        } else if (bArr == null) {
            throw new IllegalArgumentException("lineLength " + i + " > 0, but lineSeparator is null");
        } else if (containsAlphabetOrPad(bArr)) {
            throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + StringUtils.newStringUtf8(bArr) + "]");
        } else {
            this.encodeSize = bArr.length + 8;
            this.lineSeparator = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.lineSeparator, 0, bArr.length);
        }
        this.decodeSize = this.encodeSize - 1;
    }

    void decode(byte[] bArr, int i, int i2) {
        if (!this.eof) {
            int i3;
            if (i2 < 0) {
                this.eof = true;
            }
            int i4 = 0;
            while (i4 < i2) {
                i3 = i + 1;
                byte b = bArr[i];
                if (b == (byte) 61) {
                    this.eof = true;
                    break;
                }
                ensureBufferSize(this.decodeSize);
                if (b >= (byte) 0 && b < this.decodeTable.length) {
                    b = this.decodeTable[b];
                    if (b >= (byte) 0) {
                        this.modulus = (this.modulus + 1) % 8;
                        this.bitWorkArea = (this.bitWorkArea << 5) + ((long) b);
                        if (this.modulus == 0) {
                            byte[] bArr2 = this.buffer;
                            int i5 = this.pos;
                            this.pos = i5 + 1;
                            bArr2[i5] = (byte) ((int) ((this.bitWorkArea >> 32) & 255));
                            bArr2 = this.buffer;
                            i5 = this.pos;
                            this.pos = i5 + 1;
                            bArr2[i5] = (byte) ((int) ((this.bitWorkArea >> 24) & 255));
                            bArr2 = this.buffer;
                            i5 = this.pos;
                            this.pos = i5 + 1;
                            bArr2[i5] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                            bArr2 = this.buffer;
                            i5 = this.pos;
                            this.pos = i5 + 1;
                            bArr2[i5] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                            bArr2 = this.buffer;
                            i5 = this.pos;
                            this.pos = i5 + 1;
                            bArr2[i5] = (byte) ((int) (this.bitWorkArea & 255));
                        }
                    }
                }
                i4++;
                i = i3;
            }
            if (this.eof && this.modulus >= 2) {
                ensureBufferSize(this.decodeSize);
                byte[] bArr3;
                switch (this.modulus) {
                    case 2:
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 2) & 255));
                        return;
                    case 3:
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 7) & 255));
                        return;
                    case 4:
                        this.bitWorkArea >>= 4;
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 5:
                        this.bitWorkArea >>= 1;
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 6:
                        this.bitWorkArea >>= 6;
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 7:
                        this.bitWorkArea >>= 3;
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 24) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i3 = this.pos;
                        this.pos = i3 + 1;
                        bArr3[i3] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    default:
                        return;
                }
            }
        }
    }

    void encode(byte[] bArr, int i, int i2) {
        if (!this.eof) {
            int i3;
            int i4;
            if (i2 < 0) {
                this.eof = true;
                if (this.modulus != 0 || this.lineLength != 0) {
                    ensureBufferSize(this.encodeSize);
                    i3 = this.pos;
                    byte[] bArr2;
                    switch (this.modulus) {
                        case 1:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 3)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 2)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 2:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 11)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 6)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 1)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 4)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 3:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 19)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 14)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 9)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 4)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 1)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 4:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 27)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 22)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 17)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 12)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 7)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 2)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 3)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                    }
                    this.currentLinePos = (this.pos - i3) + this.currentLinePos;
                    if (this.lineLength > 0 && this.currentLinePos > 0) {
                        System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        this.pos += this.lineSeparator.length;
                        return;
                    }
                    return;
                }
                return;
            }
            int i5 = 0;
            while (i5 < i2) {
                ensureBufferSize(this.encodeSize);
                this.modulus = (this.modulus + 1) % 5;
                i4 = i + 1;
                i3 = bArr[i];
                if (i3 < 0) {
                    i3 += 256;
                }
                this.bitWorkArea = (this.bitWorkArea << 8) + ((long) i3);
                if (this.modulus == 0) {
                    byte[] bArr3 = this.buffer;
                    int i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 35)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 30)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 25)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 20)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 15)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 10)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) (this.bitWorkArea >> 5)) & 31];
                    bArr3 = this.buffer;
                    i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr3[i6] = this.encodeTable[((int) this.bitWorkArea) & 31];
                    this.currentLinePos += 8;
                    if (this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
                        System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        this.pos += this.lineSeparator.length;
                        this.currentLinePos = 0;
                    }
                }
                i5++;
                i = i4;
            }
        }
    }

    public boolean isInAlphabet(byte b) {
        return b >= (byte) 0 && b < this.decodeTable.length && this.decodeTable[b] != (byte) -1;
    }
}
