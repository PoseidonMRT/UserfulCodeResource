package com.mqunar.necro.agent.instrumentation.io;

import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class CountingInputStream extends InputStream implements StreamCompleteListenerSource {
    public static final int DEFAULT_RESPONSE_BODY_LIMIT = 2048;
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private final ByteBuffer buffer;
    private long count = 0;
    private boolean enableBuffering = false;
    private final InputStream impl;
    private final StreamCompleteListenerManager listenerManager = new StreamCompleteListenerManager();

    public CountingInputStream(InputStream inputStream) {
        this.impl = inputStream;
        if (this.enableBuffering) {
            this.buffer = ByteBuffer.allocate(2048);
            fillBuffer();
            return;
        }
        this.buffer = null;
    }

    public CountingInputStream(InputStream inputStream, boolean z) {
        this.impl = inputStream;
        this.enableBuffering = z;
        if (z) {
            this.buffer = ByteBuffer.allocate(2048);
            fillBuffer();
            return;
        }
        this.buffer = null;
    }

    public void addStreamCompleteListener(StreamCompleteListener streamCompleteListener) {
        this.listenerManager.addStreamCompleteListener(streamCompleteListener);
    }

    public void removeStreamCompleteListener(StreamCompleteListener streamCompleteListener) {
        this.listenerManager.removeStreamCompleteListener(streamCompleteListener);
    }

    public int read() {
        int readBuffer;
        if (this.enableBuffering) {
            ByteBuffer byteBuffer = this.buffer;
            synchronized (this.buffer) {
                if (bufferHasBytes(1)) {
                    readBuffer = readBuffer();
                    if (readBuffer >= 0) {
                        this.count++;
                    }
                }
            }
            return readBuffer;
        }
        try {
            readBuffer = this.impl.read();
            if (readBuffer >= 0) {
                this.count++;
            } else {
                notifyStreamComplete();
            }
            return readBuffer;
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public int read(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        if (this.enableBuffering) {
            ByteBuffer byteBuffer = this.buffer;
            synchronized (this.buffer) {
                if (bufferHasBytes((long) length)) {
                    length = readBufferBytes(bArr);
                    if (length >= 0) {
                        this.count += (long) length;
                        return length;
                    }
                    throw new IOException("readBufferBytes failed");
                }
                int remaining = this.buffer.remaining();
                if (remaining > 0) {
                    i = readBufferBytes(bArr, 0, remaining);
                    if (i < 0) {
                        throw new IOException("partial read from buffer failed");
                    }
                    length -= i;
                    this.count += (long) i;
                }
            }
        }
        try {
            length = this.impl.read(bArr, i, length);
            if (length >= 0) {
                this.count += (long) length;
                return length + i;
            } else if (i > 0) {
                return i;
            } else {
                notifyStreamComplete();
                return length;
            }
        } catch (Exception e) {
            log.error(e.toString());
            System.out.println("NOTIFY STREAM ERROR: " + e);
            e.printStackTrace();
            notifyStreamError(e);
            throw e;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        int readBufferBytes;
        int i3 = 0;
        if (this.enableBuffering) {
            ByteBuffer byteBuffer = this.buffer;
            synchronized (this.buffer) {
                if (bufferHasBytes((long) i2)) {
                    readBufferBytes = readBufferBytes(bArr, i, i2);
                    if (readBufferBytes >= 0) {
                        this.count += (long) readBufferBytes;
                        return readBufferBytes;
                    }
                    throw new IOException("readBufferBytes failed");
                }
                readBufferBytes = this.buffer.remaining();
                if (readBufferBytes > 0) {
                    i3 = readBufferBytes(bArr, i, readBufferBytes);
                    if (i3 < 0) {
                        throw new IOException("partial read from buffer failed");
                    }
                    i2 -= i3;
                    this.count += (long) i3;
                }
            }
        }
        try {
            readBufferBytes = this.impl.read(bArr, i + i3, i2);
            if (readBufferBytes >= 0) {
                this.count += (long) readBufferBytes;
                return readBufferBytes + i3;
            } else if (i3 > 0) {
                return i3;
            } else {
                notifyStreamComplete();
                return readBufferBytes;
            }
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public long skip(long j) {
        if (this.enableBuffering) {
            ByteBuffer byteBuffer = this.buffer;
            synchronized (this.buffer) {
                if (bufferHasBytes(j)) {
                    this.buffer.position((int) j);
                    this.count += j;
                } else {
                    j -= (long) this.buffer.remaining();
                    if (j <= 0) {
                        throw new IOException("partial read from buffer (skip) failed");
                    }
                    this.buffer.position(this.buffer.remaining());
                }
            }
            return j;
        }
        try {
            j = this.impl.skip(j);
            this.count += j;
            return j;
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public int available() {
        int i = 0;
        if (this.enableBuffering) {
            i = this.buffer.remaining();
        }
        try {
            return i + this.impl.available();
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void close() {
        try {
            this.impl.close();
            notifyStreamComplete();
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void mark(int i) {
        if (markSupported()) {
            this.impl.mark(i);
        }
    }

    public boolean markSupported() {
        return this.impl.markSupported();
    }

    public void reset() {
        if (markSupported()) {
            try {
                this.impl.reset();
            } catch (Exception e) {
                notifyStreamError(e);
                throw e;
            }
        }
    }

    private int readBuffer() {
        return bufferEmpty() ? -1 : this.buffer.get();
    }

    private int readBufferBytes(byte[] bArr) {
        return readBufferBytes(bArr, 0, bArr.length);
    }

    private int readBufferBytes(byte[] bArr, int i, int i2) {
        if (bufferEmpty()) {
            return -1;
        }
        int remaining = this.buffer.remaining();
        this.buffer.get(bArr, i, i2);
        return remaining - this.buffer.remaining();
    }

    private boolean bufferHasBytes(long j) {
        return ((long) this.buffer.remaining()) >= j;
    }

    private boolean bufferEmpty() {
        return !this.buffer.hasRemaining();
    }

    public void fillBuffer() {
        if (this.buffer != null && this.buffer.hasArray()) {
            ByteBuffer byteBuffer = this.buffer;
            synchronized (this.buffer) {
                int read;
                try {
                    read = this.impl.read(this.buffer.array(), 0, this.buffer.capacity());
                } catch (IOException e) {
                    log.error(e.toString());
                    read = 0;
                }
                if (read <= 0) {
                    this.buffer.limit(0);
                } else if (read < this.buffer.capacity()) {
                    this.buffer.limit(read);
                }
            }
        }
    }

    private void notifyStreamComplete() {
        if (!this.listenerManager.isComplete()) {
            this.listenerManager.notifyStreamComplete(new StreamCompleteEvent(this, this.count));
        }
    }

    private void notifyStreamError(Exception exception) {
        if (!this.listenerManager.isComplete()) {
            this.listenerManager.notifyStreamError(new StreamCompleteEvent(this, this.count, exception));
        }
    }

    public void setBufferingEnabled(boolean z) {
        this.enableBuffering = z;
    }

    public String getBufferAsString() {
        if (this.buffer == null) {
            return "";
        }
        String str;
        ByteBuffer byteBuffer = this.buffer;
        synchronized (this.buffer) {
            byte[] bArr = new byte[this.buffer.limit()];
            for (int i = 0; i < this.buffer.limit(); i++) {
                bArr[i] = this.buffer.get(i);
            }
            str = new String(bArr);
        }
        return str;
    }
}
