package okhttp3.internal.ws;

import android.support.v4.media.TransportMediator;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

final class WebSocketWriter {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketWriter.class.desiredAssertionStatus());
    boolean activeWriter;
    final Buffer buffer = new Buffer();
    final FrameSink frameSink = new FrameSink();
    final boolean isClient;
    final byte[] maskBuffer;
    final byte[] maskKey;
    final Random random;
    final BufferedSink sink;
    boolean writerClosed;

    final class FrameSink implements Sink {
        boolean closed;
        long contentLength;
        int formatOpcode;
        boolean isFirstFrame;

        FrameSink() {
        }

        public void write(Buffer buffer, long j) {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter.this.buffer.write(buffer, j);
            boolean z = this.isFirstFrame && this.contentLength != -1 && WebSocketWriter.this.buffer.size() > this.contentLength - PlaybackStateCompat.ACTION_PLAY_FROM_URI;
            long completeSegmentByteCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount > 0 && !z) {
                synchronized (WebSocketWriter.this) {
                    WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, completeSegmentByteCount, this.isFirstFrame, false);
                }
                this.isFirstFrame = false;
            }
        }

        public void flush() {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, false);
            }
            this.isFirstFrame = false;
        }

        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }

        public void close() {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, true);
            }
            this.closed = true;
            WebSocketWriter.this.activeWriter = false;
        }
    }

    WebSocketWriter(boolean z, BufferedSink bufferedSink, Random random) {
        byte[] bArr = null;
        if (bufferedSink == null) {
            throw new NullPointerException("sink == null");
        } else if (random == null) {
            throw new NullPointerException("random == null");
        } else {
            byte[] bArr2;
            this.isClient = z;
            this.sink = bufferedSink;
            this.random = random;
            if (z) {
                bArr2 = new byte[4];
            } else {
                bArr2 = null;
            }
            this.maskKey = bArr2;
            if (z) {
                bArr = new byte[8192];
            }
            this.maskBuffer = bArr;
        }
    }

    void writePing(ByteString byteString) {
        synchronized (this) {
            writeControlFrameSynchronized(9, byteString);
        }
    }

    void writePong(ByteString byteString) {
        synchronized (this) {
            writeControlFrameSynchronized(10, byteString);
        }
    }

    void writeClose(int i, ByteString byteString) {
        ByteString byteString2 = ByteString.EMPTY;
        if (!(i == 0 && byteString == null)) {
            if (i != 0) {
                WebSocketProtocol.validateCloseCode(i);
            }
            Buffer buffer = new Buffer();
            buffer.writeShort(i);
            if (byteString != null) {
                buffer.write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        synchronized (this) {
            try {
                writeControlFrameSynchronized(8, byteString2);
                this.writerClosed = true;
            } catch (Throwable th) {
                this.writerClosed = true;
            }
        }
    }

    private void writeControlFrameSynchronized(int i, ByteString byteString) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.writerClosed) {
            throw new IOException("closed");
        } else {
            int size = byteString.size();
            if (((long) size) > 125) {
                throw new IllegalArgumentException("Payload size must be less than or equal to 125");
            }
            this.sink.writeByte(i | 128);
            if (this.isClient) {
                this.sink.writeByte(size | 128);
                this.random.nextBytes(this.maskKey);
                this.sink.write(this.maskKey);
                byte[] toByteArray = byteString.toByteArray();
                WebSocketProtocol.toggleMask(toByteArray, (long) toByteArray.length, this.maskKey, 0);
                this.sink.write(toByteArray);
            } else {
                this.sink.writeByte(size);
                this.sink.write(byteString);
            }
            this.sink.flush();
        }
    }

    Sink newMessageSink(int i, long j) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        this.frameSink.formatOpcode = i;
        this.frameSink.contentLength = j;
        this.frameSink.isFirstFrame = true;
        this.frameSink.closed = false;
        return this.frameSink;
    }

    void writeMessageFrameSynchronized(int i, long j, boolean z, boolean z2) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.writerClosed) {
            throw new IOException("closed");
        } else {
            int i2 = z ? i : 0;
            if (z2) {
                i2 |= 128;
            }
            this.sink.writeByte(i2);
            if (this.isClient) {
                i2 = 128;
            } else {
                i2 = 0;
            }
            if (j <= 125) {
                this.sink.writeByte(i2 | ((int) j));
            } else if (j <= 65535) {
                this.sink.writeByte(i2 | TransportMediator.KEYCODE_MEDIA_PLAY);
                this.sink.writeShort((int) j);
            } else {
                this.sink.writeByte(i2 | TransportMediator.KEYCODE_MEDIA_PAUSE);
                this.sink.writeLong(j);
            }
            if (this.isClient) {
                this.random.nextBytes(this.maskKey);
                this.sink.write(this.maskKey);
                long j2 = 0;
                while (j2 < j) {
                    int read = this.buffer.read(this.maskBuffer, 0, (int) Math.min(j, (long) this.maskBuffer.length));
                    if (read == -1) {
                        throw new AssertionError();
                    }
                    WebSocketProtocol.toggleMask(this.maskBuffer, (long) read, this.maskKey, j2);
                    this.sink.write(this.maskBuffer, 0, read);
                    j2 += (long) read;
                }
            } else {
                this.sink.write(this.buffer, j);
            }
            this.sink.emit();
        }
    }
}
