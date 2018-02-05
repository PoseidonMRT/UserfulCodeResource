package com.squareup.wire;

import java.nio.charset.Charset;

final class Stringer {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    class Decoder {
        private static final int[] DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private int op;
        private final byte[] output;
        private int state = 0;
        private int value = 0;

        public Decoder(byte[] bArr) {
            this.output = bArr;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean process(byte[] r11, int r12, int r13, boolean r14) {
            /*
            r10 = this;
            r0 = r10.state;
            r1 = 6;
            if (r0 != r1) goto L_0x0007;
        L_0x0005:
            r0 = 0;
        L_0x0006:
            return r0;
        L_0x0007:
            r4 = r13 + r12;
            r3 = r10.state;
            r1 = r10.value;
            r0 = 0;
            r5 = r10.output;
            r6 = DECODE;
            r2 = r12;
        L_0x0013:
            if (r2 >= r4) goto L_0x0167;
        L_0x0015:
            if (r3 != 0) goto L_0x0067;
        L_0x0017:
            r7 = r2 + 4;
            if (r7 > r4) goto L_0x005a;
        L_0x001b:
            r1 = r11[r2];
            r1 = r1 & 255;
            r1 = r6[r1];
            r1 = r1 << 18;
            r7 = r2 + 1;
            r7 = r11[r7];
            r7 = r7 & 255;
            r7 = r6[r7];
            r7 = r7 << 12;
            r1 = r1 | r7;
            r7 = r2 + 2;
            r7 = r11[r7];
            r7 = r7 & 255;
            r7 = r6[r7];
            r7 = r7 << 6;
            r1 = r1 | r7;
            r7 = r2 + 3;
            r7 = r11[r7];
            r7 = r7 & 255;
            r7 = r6[r7];
            r1 = r1 | r7;
            if (r1 < 0) goto L_0x005a;
        L_0x0044:
            r7 = r0 + 2;
            r8 = (byte) r1;
            r5[r7] = r8;
            r7 = r0 + 1;
            r8 = r1 >> 8;
            r8 = (byte) r8;
            r5[r7] = r8;
            r7 = r1 >> 16;
            r7 = (byte) r7;
            r5[r0] = r7;
            r0 = r0 + 3;
            r2 = r2 + 4;
            goto L_0x0017;
        L_0x005a:
            if (r2 < r4) goto L_0x0067;
        L_0x005c:
            r2 = r1;
        L_0x005d:
            if (r14 != 0) goto L_0x011e;
        L_0x005f:
            r10.state = r3;
            r10.value = r2;
            r10.op = r0;
            r0 = 1;
            goto L_0x0006;
        L_0x0067:
            r12 = r2 + 1;
            r2 = r11[r2];
            r2 = r2 & 255;
            r2 = r6[r2];
            switch(r3) {
                case 0: goto L_0x008b;
                case 1: goto L_0x009f;
                case 2: goto L_0x00b0;
                case 3: goto L_0x00d0;
                case 4: goto L_0x0106;
                case 5: goto L_0x0115;
                default: goto L_0x0072;
            };
        L_0x0072:
            r0 = new java.lang.AssertionError;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "state = ";
            r1 = r1.append(r2);
            r1 = r1.append(r3);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x008b:
            if (r2 < 0) goto L_0x0096;
        L_0x008d:
            r1 = r3 + 1;
            r9 = r2;
            r2 = r1;
            r1 = r9;
        L_0x0092:
            r3 = r2;
            r2 = r12;
            goto L_0x0013;
        L_0x0096:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x0099:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x009f:
            if (r2 < 0) goto L_0x00a7;
        L_0x00a1:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r3 + 1;
            goto L_0x0092;
        L_0x00a7:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x00aa:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x00b0:
            if (r2 < 0) goto L_0x00b8;
        L_0x00b2:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r3 + 1;
            goto L_0x0092;
        L_0x00b8:
            r7 = -2;
            if (r2 != r7) goto L_0x00c7;
        L_0x00bb:
            r2 = r0 + 1;
            r3 = r1 >> 4;
            r3 = (byte) r3;
            r5[r0] = r3;
            r0 = 4;
            r9 = r2;
            r2 = r0;
            r0 = r9;
            goto L_0x0092;
        L_0x00c7:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x00ca:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x00d0:
            if (r2 < 0) goto L_0x00ea;
        L_0x00d2:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r0 + 2;
            r3 = (byte) r1;
            r5[r2] = r3;
            r2 = r0 + 1;
            r3 = r1 >> 8;
            r3 = (byte) r3;
            r5[r2] = r3;
            r2 = r1 >> 16;
            r2 = (byte) r2;
            r5[r0] = r2;
            r0 = r0 + 3;
            r2 = 0;
            goto L_0x0092;
        L_0x00ea:
            r7 = -2;
            if (r2 != r7) goto L_0x00fd;
        L_0x00ed:
            r2 = r0 + 1;
            r3 = r1 >> 2;
            r3 = (byte) r3;
            r5[r2] = r3;
            r2 = r1 >> 10;
            r2 = (byte) r2;
            r5[r0] = r2;
            r0 = r0 + 2;
            r2 = 5;
            goto L_0x0092;
        L_0x00fd:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x0100:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x0106:
            r7 = -2;
            if (r2 != r7) goto L_0x010c;
        L_0x0109:
            r2 = r3 + 1;
            goto L_0x0092;
        L_0x010c:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x010f:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x0115:
            r7 = -1;
            if (r2 == r7) goto L_0x0164;
        L_0x0118:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x011e:
            switch(r3) {
                case 0: goto L_0x0148;
                case 1: goto L_0x013a;
                case 2: goto L_0x0140;
                case 3: goto L_0x014f;
                case 4: goto L_0x015e;
                case 5: goto L_0x0148;
                default: goto L_0x0121;
            };
        L_0x0121:
            r0 = new java.lang.AssertionError;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "state = ";
            r1 = r1.append(r2);
            r1 = r1.append(r3);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x013a:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x0140:
            r1 = r0 + 1;
            r2 = r2 >> 4;
            r2 = (byte) r2;
            r5[r0] = r2;
            r0 = r1;
        L_0x0148:
            r10.state = r3;
            r10.op = r0;
            r0 = 1;
            goto L_0x0006;
        L_0x014f:
            r1 = r0 + 1;
            r4 = r2 >> 10;
            r4 = (byte) r4;
            r5[r0] = r4;
            r0 = r1 + 1;
            r2 = r2 >> 2;
            r2 = (byte) r2;
            r5[r1] = r2;
            goto L_0x0148;
        L_0x015e:
            r0 = 6;
            r10.state = r0;
            r0 = 0;
            goto L_0x0006;
        L_0x0164:
            r2 = r3;
            goto L_0x0092;
        L_0x0167:
            r2 = r1;
            goto L_0x005d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.squareup.wire.Stringer.Decoder.process(byte[], int, int, boolean):boolean");
        }
    }

    class Encoder {
        private static final byte[] ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        private final byte[] output;
        private final byte[] tail = new byte[2];
        private int tailLen = 0;

        public Encoder(byte[] bArr) {
            this.output = bArr;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void process(byte[] r10, int r11, int r12, boolean r13) {
            /*
            r9 = this;
            r5 = ENCODE;
            r6 = r9.output;
            r0 = 0;
            r2 = r12 + r11;
            r1 = -1;
            r3 = r9.tailLen;
            switch(r3) {
                case 0: goto L_0x0028;
                case 1: goto L_0x0093;
                case 2: goto L_0x00b6;
                default: goto L_0x000d;
            };
        L_0x000d:
            r0 = new java.lang.AssertionError;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "tailLen = ";
            r1 = r1.append(r2);
            r2 = r9.tailLen;
            r1 = r1.append(r2);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0028:
            r3 = r11;
        L_0x0029:
            r4 = -1;
            if (r1 == r4) goto L_0x004e;
        L_0x002c:
            r4 = 1;
            r7 = r1 >> 18;
            r7 = r7 & 63;
            r7 = r5[r7];
            r6[r0] = r7;
            r0 = 2;
            r7 = r1 >> 12;
            r7 = r7 & 63;
            r7 = r5[r7];
            r6[r4] = r7;
            r4 = 3;
            r7 = r1 >> 6;
            r7 = r7 & 63;
            r7 = r5[r7];
            r6[r0] = r7;
            r0 = 4;
            r1 = r1 & 63;
            r1 = r5[r1];
            r6[r4] = r1;
        L_0x004e:
            r4 = r0;
        L_0x004f:
            r0 = r3 + 3;
            if (r0 > r2) goto L_0x00d9;
        L_0x0053:
            r0 = r10[r3];
            r0 = r0 & 255;
            r0 = r0 << 16;
            r1 = r3 + 1;
            r1 = r10[r1];
            r1 = r1 & 255;
            r1 = r1 << 8;
            r0 = r0 | r1;
            r1 = r3 + 2;
            r1 = r10[r1];
            r1 = r1 & 255;
            r0 = r0 | r1;
            r1 = r0 >> 18;
            r1 = r1 & 63;
            r1 = r5[r1];
            r6[r4] = r1;
            r1 = r4 + 1;
            r7 = r0 >> 12;
            r7 = r7 & 63;
            r7 = r5[r7];
            r6[r1] = r7;
            r1 = r4 + 2;
            r7 = r0 >> 6;
            r7 = r7 & 63;
            r7 = r5[r7];
            r6[r1] = r7;
            r1 = r4 + 3;
            r0 = r0 & 63;
            r0 = r5[r0];
            r6[r1] = r0;
            r3 = r3 + 3;
            r0 = r4 + 4;
            r4 = r0;
            goto L_0x004f;
        L_0x0093:
            r3 = r11 + 2;
            if (r3 > r2) goto L_0x01a3;
        L_0x0097:
            r1 = r9.tail;
            r3 = 0;
            r1 = r1[r3];
            r1 = r1 & 255;
            r1 = r1 << 16;
            r3 = r11 + 1;
            r4 = r10[r11];
            r4 = r4 & 255;
            r4 = r4 << 8;
            r1 = r1 | r4;
            r11 = r3 + 1;
            r3 = r10[r3];
            r3 = r3 & 255;
            r1 = r1 | r3;
            r3 = 0;
            r9.tailLen = r3;
            r3 = r11;
            goto L_0x0029;
        L_0x00b6:
            r3 = r11 + 1;
            if (r3 > r2) goto L_0x01a3;
        L_0x00ba:
            r1 = r9.tail;
            r3 = 0;
            r1 = r1[r3];
            r1 = r1 & 255;
            r1 = r1 << 16;
            r3 = r9.tail;
            r4 = 1;
            r3 = r3[r4];
            r3 = r3 & 255;
            r3 = r3 << 8;
            r1 = r1 | r3;
            r3 = r11 + 1;
            r4 = r10[r11];
            r4 = r4 & 255;
            r1 = r1 | r4;
            r4 = 0;
            r9.tailLen = r4;
            goto L_0x0029;
        L_0x00d9:
            if (r13 == 0) goto L_0x0172;
        L_0x00db:
            r0 = r9.tailLen;
            r0 = r3 - r0;
            r1 = r2 + -1;
            if (r0 != r1) goto L_0x0118;
        L_0x00e3:
            r2 = 0;
            r0 = r9.tailLen;
            if (r0 <= 0) goto L_0x0114;
        L_0x00e8:
            r0 = r9.tail;
            r1 = 1;
            r0 = r0[r2];
        L_0x00ed:
            r0 = r0 & 255;
            r0 = r0 << 4;
            r2 = r9.tailLen;
            r1 = r2 - r1;
            r9.tailLen = r1;
            r1 = r4 + 1;
            r2 = r0 >> 6;
            r2 = r2 & 63;
            r2 = r5[r2];
            r6[r4] = r2;
            r2 = r1 + 1;
            r0 = r0 & 63;
            r0 = r5[r0];
            r6[r1] = r0;
            r0 = r2 + 1;
            r1 = 61;
            r6[r2] = r1;
            r1 = 61;
            r6[r0] = r1;
        L_0x0113:
            return;
        L_0x0114:
            r0 = r10[r3];
            r1 = r2;
            goto L_0x00ed;
        L_0x0118:
            r0 = r9.tailLen;
            r0 = r3 - r0;
            r1 = r2 + -2;
            if (r0 != r1) goto L_0x0113;
        L_0x0120:
            r2 = 0;
            r0 = r9.tailLen;
            r1 = 1;
            if (r0 <= r1) goto L_0x0167;
        L_0x0126:
            r0 = r9.tail;
            r1 = 1;
            r0 = r0[r2];
            r2 = r3;
        L_0x012c:
            r0 = r0 & 255;
            r3 = r0 << 10;
            r0 = r9.tailLen;
            if (r0 <= 0) goto L_0x016f;
        L_0x0134:
            r0 = r9.tail;
            r2 = r1 + 1;
            r0 = r0[r1];
            r1 = r2;
        L_0x013b:
            r0 = r0 & 255;
            r0 = r0 << 2;
            r0 = r0 | r3;
            r2 = r9.tailLen;
            r1 = r2 - r1;
            r9.tailLen = r1;
            r1 = r4 + 1;
            r2 = r0 >> 12;
            r2 = r2 & 63;
            r2 = r5[r2];
            r6[r4] = r2;
            r2 = r1 + 1;
            r3 = r0 >> 6;
            r3 = r3 & 63;
            r3 = r5[r3];
            r6[r1] = r3;
            r1 = r2 + 1;
            r0 = r0 & 63;
            r0 = r5[r0];
            r6[r2] = r0;
            r0 = 61;
            r6[r1] = r0;
            goto L_0x0113;
        L_0x0167:
            r1 = r3 + 1;
            r0 = r10[r3];
            r8 = r2;
            r2 = r1;
            r1 = r8;
            goto L_0x012c;
        L_0x016f:
            r0 = r10[r2];
            goto L_0x013b;
        L_0x0172:
            r0 = r2 + -1;
            if (r3 != r0) goto L_0x0183;
        L_0x0176:
            r0 = r9.tail;
            r1 = r9.tailLen;
            r2 = r1 + 1;
            r9.tailLen = r2;
            r2 = r10[r3];
            r0[r1] = r2;
            goto L_0x0113;
        L_0x0183:
            r0 = r2 + -2;
            if (r3 != r0) goto L_0x0113;
        L_0x0187:
            r0 = r9.tail;
            r1 = r9.tailLen;
            r2 = r1 + 1;
            r9.tailLen = r2;
            r2 = r10[r3];
            r0[r1] = r2;
            r0 = r9.tail;
            r1 = r9.tailLen;
            r2 = r1 + 1;
            r9.tailLen = r2;
            r2 = r3 + 1;
            r2 = r10[r2];
            r0[r1] = r2;
            goto L_0x0113;
        L_0x01a3:
            r3 = r11;
            goto L_0x0029;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.squareup.wire.Stringer.Encoder.process(byte[], int, int, boolean):void");
        }
    }

    private Stringer() {
    }

    public static byte[] decode(String str) {
        byte[] bytes = str.getBytes(UTF_8);
        Decoder decoder = new Decoder(new byte[((bytes.length * 3) / 4)]);
        if (!decoder.process(bytes, 0, bytes.length, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            bytes = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, bytes, 0, decoder.op);
            return bytes;
        }
    }

    public static String encode(byte[] bArr) {
        int length = (bArr.length / 3) * 4;
        if (bArr.length % 3 > 0) {
            length += 4;
        }
        Encoder encoder = new Encoder(new byte[length]);
        encoder.process(bArr, 0, bArr.length, true);
        return new String(encoder.output, UTF_8);
    }
}
