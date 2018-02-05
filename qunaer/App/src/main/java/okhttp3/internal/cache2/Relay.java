package okhttp3.internal.cache2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Relay {
    private static final long FILE_HEADER_SIZE = 32;
    static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
    static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    final Buffer buffer = new Buffer();
    final long bufferMaxSize;
    boolean complete;
    RandomAccessFile file;
    private final ByteString metadata;
    int sourceCount;
    Source upstream;
    final Buffer upstreamBuffer = new Buffer();
    long upstreamPos;
    Thread upstreamReader;

    class RelaySource implements Source {
        private FileOperator fileOperator = new FileOperator(Relay.this.file.getChannel());
        private long sourcePos;
        private final Timeout timeout = new Timeout();

        RelaySource() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long read(okio.Buffer r13, long r14) {
            /*
            r12 = this;
            r0 = r12.fileOperator;
            if (r0 != 0) goto L_0x000c;
        L_0x0004:
            r0 = new java.lang.IllegalStateException;
            r1 = "closed";
            r0.<init>(r1);
            throw r0;
        L_0x000c:
            r6 = okhttp3.internal.cache2.Relay.this;
            monitor-enter(r6);
        L_0x000f:
            r0 = r12.sourcePos;	 Catch:{ all -> 0x0031 }
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r7 = r2.upstreamPos;	 Catch:{ all -> 0x0031 }
            r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
            if (r0 != 0) goto L_0x005a;
        L_0x0019:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r0 = r0.complete;	 Catch:{ all -> 0x0031 }
            if (r0 == 0) goto L_0x0023;
        L_0x001f:
            r4 = -1;
            monitor-exit(r6);	 Catch:{ all -> 0x0031 }
        L_0x0022:
            return r4;
        L_0x0023:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r0 = r0.upstreamReader;	 Catch:{ all -> 0x0031 }
            if (r0 == 0) goto L_0x0034;
        L_0x0029:
            r0 = r12.timeout;	 Catch:{ all -> 0x0031 }
            r1 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r0.waitUntilNotified(r1);	 Catch:{ all -> 0x0031 }
            goto L_0x000f;
        L_0x0031:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0031 }
            throw r0;
        L_0x0034:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0031 }
            r0.upstreamReader = r1;	 Catch:{ all -> 0x0031 }
            r0 = 1;
            monitor-exit(r6);	 Catch:{ all -> 0x0031 }
        L_0x003e:
            r1 = 2;
            if (r0 != r1) goto L_0x0088;
        L_0x0041:
            r0 = r12.sourcePos;
            r0 = r7 - r0;
            r4 = java.lang.Math.min(r14, r0);
            r0 = r12.fileOperator;
            r1 = 32;
            r6 = r12.sourcePos;
            r1 = r1 + r6;
            r3 = r13;
            r0.read(r1, r3, r4);
            r0 = r12.sourcePos;
            r0 = r0 + r4;
            r12.sourcePos = r0;
            goto L_0x0022;
        L_0x005a:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r0 = r0.buffer;	 Catch:{ all -> 0x0031 }
            r0 = r0.size();	 Catch:{ all -> 0x0031 }
            r1 = r7 - r0;
            r3 = r12.sourcePos;	 Catch:{ all -> 0x0031 }
            r0 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
            if (r0 >= 0) goto L_0x006d;
        L_0x006a:
            r0 = 2;
            monitor-exit(r6);	 Catch:{ all -> 0x0031 }
            goto L_0x003e;
        L_0x006d:
            r3 = r12.sourcePos;	 Catch:{ all -> 0x0031 }
            r3 = r7 - r3;
            r4 = java.lang.Math.min(r14, r3);	 Catch:{ all -> 0x0031 }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0031 }
            r0 = r0.buffer;	 Catch:{ all -> 0x0031 }
            r7 = r12.sourcePos;	 Catch:{ all -> 0x0031 }
            r2 = r7 - r1;
            r1 = r13;
            r0.copyTo(r1, r2, r4);	 Catch:{ all -> 0x0031 }
            r0 = r12.sourcePos;	 Catch:{ all -> 0x0031 }
            r0 = r0 + r4;
            r12.sourcePos = r0;	 Catch:{ all -> 0x0031 }
            monitor-exit(r6);	 Catch:{ all -> 0x0031 }
            goto L_0x0022;
        L_0x0088:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r0 = r0.upstream;	 Catch:{ all -> 0x012b }
            r1 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r1 = r1.upstreamBuffer;	 Catch:{ all -> 0x012b }
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r2 = r2.bufferMaxSize;	 Catch:{ all -> 0x012b }
            r10 = r0.read(r1, r2);	 Catch:{ all -> 0x012b }
            r0 = -1;
            r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
            if (r0 != 0) goto L_0x00b8;
        L_0x009e:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r0.commit(r7);	 Catch:{ all -> 0x012b }
            r4 = -1;
            r1 = okhttp3.internal.cache2.Relay.this;
            monitor-enter(r1);
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x00b5 }
            r2 = 0;
            r0.upstreamReader = r2;	 Catch:{ all -> 0x00b5 }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x00b5 }
            r0.notifyAll();	 Catch:{ all -> 0x00b5 }
            monitor-exit(r1);	 Catch:{ all -> 0x00b5 }
            goto L_0x0022;
        L_0x00b5:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x00b5 }
            throw r0;
        L_0x00b8:
            r4 = java.lang.Math.min(r10, r14);	 Catch:{ all -> 0x012b }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r0 = r0.upstreamBuffer;	 Catch:{ all -> 0x012b }
            r2 = 0;
            r1 = r13;
            r0.copyTo(r1, r2, r4);	 Catch:{ all -> 0x012b }
            r0 = r12.sourcePos;	 Catch:{ all -> 0x012b }
            r0 = r0 + r4;
            r12.sourcePos = r0;	 Catch:{ all -> 0x012b }
            r6 = r12.fileOperator;	 Catch:{ all -> 0x012b }
            r0 = 32;
            r7 = r7 + r0;
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            r0 = r0.upstreamBuffer;	 Catch:{ all -> 0x012b }
            r9 = r0.clone();	 Catch:{ all -> 0x012b }
            r6.write(r7, r9, r10);	 Catch:{ all -> 0x012b }
            r1 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x012b }
            monitor-enter(r1);	 Catch:{ all -> 0x012b }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r0 = r0.buffer;	 Catch:{ all -> 0x0128 }
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r2 = r2.upstreamBuffer;	 Catch:{ all -> 0x0128 }
            r0.write(r2, r10);	 Catch:{ all -> 0x0128 }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r0 = r0.buffer;	 Catch:{ all -> 0x0128 }
            r2 = r0.size();	 Catch:{ all -> 0x0128 }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r6 = r0.bufferMaxSize;	 Catch:{ all -> 0x0128 }
            r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r0 <= 0) goto L_0x010d;
        L_0x00f9:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r0 = r0.buffer;	 Catch:{ all -> 0x0128 }
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r2 = r2.buffer;	 Catch:{ all -> 0x0128 }
            r2 = r2.size();	 Catch:{ all -> 0x0128 }
            r6 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r6 = r6.bufferMaxSize;	 Catch:{ all -> 0x0128 }
            r2 = r2 - r6;
            r0.skip(r2);	 Catch:{ all -> 0x0128 }
        L_0x010d:
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0128 }
            r2 = r0.upstreamPos;	 Catch:{ all -> 0x0128 }
            r2 = r2 + r10;
            r0.upstreamPos = r2;	 Catch:{ all -> 0x0128 }
            monitor-exit(r1);	 Catch:{ all -> 0x0128 }
            r1 = okhttp3.internal.cache2.Relay.this;
            monitor-enter(r1);
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0125 }
            r2 = 0;
            r0.upstreamReader = r2;	 Catch:{ all -> 0x0125 }
            r0 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x0125 }
            r0.notifyAll();	 Catch:{ all -> 0x0125 }
            monitor-exit(r1);	 Catch:{ all -> 0x0125 }
            goto L_0x0022;
        L_0x0125:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0125 }
            throw r0;
        L_0x0128:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0128 }
            throw r0;	 Catch:{ all -> 0x012b }
        L_0x012b:
            r0 = move-exception;
            r1 = okhttp3.internal.cache2.Relay.this;
            monitor-enter(r1);
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x013b }
            r3 = 0;
            r2.upstreamReader = r3;	 Catch:{ all -> 0x013b }
            r2 = okhttp3.internal.cache2.Relay.this;	 Catch:{ all -> 0x013b }
            r2.notifyAll();	 Catch:{ all -> 0x013b }
            monitor-exit(r1);	 Catch:{ all -> 0x013b }
            throw r0;
        L_0x013b:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x013b }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.Relay.RelaySource.read(okio.Buffer, long):long");
        }

        public Timeout timeout() {
            return this.timeout;
        }

        public void close() {
            Closeable closeable = null;
            if (this.fileOperator != null) {
                this.fileOperator = null;
                synchronized (Relay.this) {
                    Relay relay = Relay.this;
                    relay.sourceCount--;
                    if (Relay.this.sourceCount == 0) {
                        closeable = Relay.this.file;
                        Relay.this.file = null;
                    }
                }
                if (closeable != null) {
                    Util.closeQuietly(closeable);
                }
            }
        }
    }

    private Relay(RandomAccessFile randomAccessFile, Source source, long j, ByteString byteString, long j2) {
        this.file = randomAccessFile;
        this.upstream = source;
        this.complete = source == null;
        this.upstreamPos = j;
        this.metadata = byteString;
        this.bufferMaxSize = j2;
    }

    public static Relay edit(File file, Source source, ByteString byteString, long j) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        Relay relay = new Relay(randomAccessFile, source, 0, byteString, j);
        randomAccessFile.setLength(0);
        relay.writeHeader(PREFIX_DIRTY, -1, -1);
        return relay;
    }

    public static Relay read(File file) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
        Buffer buffer = new Buffer();
        fileOperator.read(0, buffer, 32);
        if (buffer.readByteString((long) PREFIX_CLEAN.size()).equals(PREFIX_CLEAN)) {
            long readLong = buffer.readLong();
            long readLong2 = buffer.readLong();
            Buffer buffer2 = new Buffer();
            fileOperator.read(32 + readLong, buffer2, readLong2);
            return new Relay(randomAccessFile, null, readLong, buffer2.readByteString(), 0);
        }
        throw new IOException("unreadable cache file");
    }

    private void writeHeader(ByteString byteString, long j, long j2) {
        Buffer buffer = new Buffer();
        buffer.write(byteString);
        buffer.writeLong(j);
        buffer.writeLong(j2);
        if (buffer.size() != 32) {
            throw new IllegalArgumentException();
        }
        new FileOperator(this.file.getChannel()).write(0, buffer, 32);
    }

    private void writeMetadata(long j) {
        Buffer buffer = new Buffer();
        buffer.write(this.metadata);
        new FileOperator(this.file.getChannel()).write(32 + j, buffer, (long) this.metadata.size());
    }

    void commit(long j) {
        writeMetadata(j);
        this.file.getChannel().force(false);
        writeHeader(PREFIX_CLEAN, j, (long) this.metadata.size());
        this.file.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
        }
        Util.closeQuietly(this.upstream);
        this.upstream = null;
    }

    boolean isClosed() {
        return this.file == null;
    }

    public ByteString metadata() {
        return this.metadata;
    }

    public Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            this.sourceCount++;
            return new RelaySource();
        }
    }
}
