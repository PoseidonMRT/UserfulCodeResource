package okhttp3.internal.cache2;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

final class FileOperator {
    private static final int BUFFER_SIZE = 8192;
    private final byte[] byteArray = new byte[8192];
    private final ByteBuffer byteBuffer = ByteBuffer.wrap(this.byteArray);
    private final FileChannel fileChannel;

    public void read(long r6, okio.Buffer r8, long r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r5 = this;
        r3 = 0;
        r0 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0021;
    L_0x0006:
        r0 = new java.lang.IndexOutOfBoundsException;
        r0.<init>();
        throw r0;
    L_0x000c:
        r0 = r5.byteBuffer;	 Catch:{ all -> 0x0042 }
        r0 = r0.position();	 Catch:{ all -> 0x0042 }
        r1 = r5.byteArray;	 Catch:{ all -> 0x0042 }
        r2 = 0;	 Catch:{ all -> 0x0042 }
        r8.write(r1, r2, r0);	 Catch:{ all -> 0x0042 }
        r1 = (long) r0;
        r6 = r6 + r1;
        r0 = (long) r0;
        r9 = r9 - r0;
        r0 = r5.byteBuffer;
        r0.clear();
    L_0x0021:
        r0 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r0 <= 0) goto L_0x0049;
    L_0x0025:
        r0 = r5.byteBuffer;	 Catch:{ all -> 0x0042 }
        r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;	 Catch:{ all -> 0x0042 }
        r1 = java.lang.Math.min(r1, r9);	 Catch:{ all -> 0x0042 }
        r1 = (int) r1;	 Catch:{ all -> 0x0042 }
        r0.limit(r1);	 Catch:{ all -> 0x0042 }
        r0 = r5.fileChannel;	 Catch:{ all -> 0x0042 }
        r1 = r5.byteBuffer;	 Catch:{ all -> 0x0042 }
        r0 = r0.read(r1, r6);	 Catch:{ all -> 0x0042 }
        r1 = -1;	 Catch:{ all -> 0x0042 }
        if (r0 != r1) goto L_0x000c;	 Catch:{ all -> 0x0042 }
    L_0x003c:
        r0 = new java.io.EOFException;	 Catch:{ all -> 0x0042 }
        r0.<init>();	 Catch:{ all -> 0x0042 }
        throw r0;	 Catch:{ all -> 0x0042 }
    L_0x0042:
        r0 = move-exception;
        r1 = r5.byteBuffer;
        r1.clear();
        throw r0;
    L_0x0049:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.FileOperator.read(long, okio.Buffer, long):void");
    }

    public void write(long r8, okio.Buffer r10, long r11) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r7 = this;
        r5 = 0;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 < 0) goto L_0x000e;
    L_0x0006:
        r0 = r10.size();
        r0 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x004c;
    L_0x000e:
        r0 = new java.lang.IndexOutOfBoundsException;
        r0.<init>();
        throw r0;
    L_0x0014:
        r2 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r2 <= 0) goto L_0x004b;
    L_0x0018:
        r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r2 = java.lang.Math.min(r2, r11);	 Catch:{ all -> 0x0044 }
        r2 = (int) r2;	 Catch:{ all -> 0x0044 }
        r3 = r7.byteArray;	 Catch:{ all -> 0x0044 }
        r4 = 0;	 Catch:{ all -> 0x0044 }
        r10.read(r3, r4, r2);	 Catch:{ all -> 0x0044 }
        r3 = r7.byteBuffer;	 Catch:{ all -> 0x0044 }
        r3.limit(r2);	 Catch:{ all -> 0x0044 }
    L_0x002a:
        r3 = r7.fileChannel;	 Catch:{ all -> 0x0044 }
        r4 = r7.byteBuffer;	 Catch:{ all -> 0x0044 }
        r3 = r3.write(r4, r0);	 Catch:{ all -> 0x0044 }
        r3 = (long) r3;	 Catch:{ all -> 0x0044 }
        r0 = r0 + r3;	 Catch:{ all -> 0x0044 }
        r3 = r7.byteBuffer;	 Catch:{ all -> 0x0044 }
        r3 = r3.hasRemaining();	 Catch:{ all -> 0x0044 }
        if (r3 != 0) goto L_0x002a;
    L_0x003c:
        r2 = (long) r2;
        r11 = r11 - r2;
        r2 = r7.byteBuffer;
        r2.clear();
        goto L_0x0014;
    L_0x0044:
        r0 = move-exception;
        r1 = r7.byteBuffer;
        r1.clear();
        throw r0;
    L_0x004b:
        return;
    L_0x004c:
        r0 = r8;
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.FileOperator.write(long, okio.Buffer, long):void");
    }

    public FileOperator(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }
}
