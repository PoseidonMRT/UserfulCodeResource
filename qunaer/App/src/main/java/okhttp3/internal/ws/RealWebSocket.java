package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.ws.WebSocketReader.FrameCallback;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;

public final class RealWebSocket implements WebSocket, FrameCallback {
    static final /* synthetic */ boolean $assertionsDisabled = (!RealWebSocket.class.desiredAssertionStatus());
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000;
    private static final long MAX_QUEUE_SIZE = 16777216;
    private static final List<Protocol> ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue = new ArrayDeque();
    private final Request originalRequest;
    int pingCount;
    int pongCount;
    private final ArrayDeque<ByteString> pongQueue = new ArrayDeque();
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode = -1;
    private String receivedCloseReason;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;

    public abstract class Streams implements Closeable {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;

        public Streams(boolean z, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.client = z;
            this.source = bufferedSource;
            this.sink = bufferedSink;
        }
    }

    final class CancelRunnable implements Runnable {
        CancelRunnable() {
        }

        public void run() {
            RealWebSocket.this.cancel();
        }
    }

    final class Close {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;

        Close(int i, ByteString byteString, long j) {
            this.code = i;
            this.reason = byteString;
            this.cancelAfterCloseMillis = j;
        }
    }

    final class Message {
        final ByteString data;
        final int formatOpcode;

        Message(int i, ByteString byteString) {
            this.formatOpcode = i;
            this.data = byteString;
        }
    }

    final class PingRunnable implements Runnable {
        PingRunnable() {
        }

        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }

    public RealWebSocket(Request request, WebSocketListener webSocketListener, Random random) {
        if (HttpGet.METHOD_NAME.equals(request.method())) {
            this.originalRequest = request;
            this.listener = webSocketListener;
            this.random = random;
            byte[] bArr = new byte[16];
            random.nextBytes(bArr);
            this.key = ByteString.of(bArr).base64();
            this.writerRunnable = new Runnable() {
                public void run() {
                    do {
                        try {
                        } catch (Exception e) {
                            RealWebSocket.this.failWebSocket(e, null);
                            return;
                        }
                    } while (RealWebSocket.this.writeOneFrame());
                }
            };
            return;
        }
        throw new IllegalArgumentException("Request must be GET: " + request.method());
    }

    public Request request() {
        return this.originalRequest;
    }

    public synchronized long queueSize() {
        return this.queueSize;
    }

    public void cancel() {
        this.call.cancel();
    }

    public void connect(OkHttpClient okHttpClient) {
        OkHttpClient build = okHttpClient.newBuilder().protocols(ONLY_HTTP1).build();
        final int pingIntervalMillis = build.pingIntervalMillis();
        final Request build2 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header(HTTP.CONN_DIRECTIVE, "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        this.call = Internal.instance.newWebSocketCall(build, build2);
        this.call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                try {
                    RealWebSocket.this.checkResponse(response);
                    StreamAllocation streamAllocation = Internal.instance.streamAllocation(call);
                    streamAllocation.noNewStreams();
                    Streams newWebSocketStreams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                    try {
                        RealWebSocket.this.listener.onOpen(RealWebSocket.this, response);
                        RealWebSocket.this.initReaderAndWriter("OkHttp WebSocket " + build2.url().redact(), (long) pingIntervalMillis, newWebSocketStreams);
                        streamAllocation.connection().socket().setSoTimeout(0);
                        RealWebSocket.this.loopReader();
                    } catch (Exception e) {
                        RealWebSocket.this.failWebSocket(e, null);
                    }
                } catch (Exception e2) {
                    RealWebSocket.this.failWebSocket(e2, response);
                    Util.closeQuietly((Closeable) response);
                }
            }

            public void onFailure(Call call, IOException iOException) {
                RealWebSocket.this.failWebSocket(iOException, null);
            }
        });
    }

    void checkResponse(Response response) {
        if (response.code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        String header = response.header(HTTP.CONN_DIRECTIVE);
        if ("Upgrade".equalsIgnoreCase(header)) {
            header = response.header("Upgrade");
            if ("websocket".equalsIgnoreCase(header)) {
                header = response.header("Sec-WebSocket-Accept");
                String base64 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
                if (!base64.equals(header)) {
                    throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + base64 + "' but was '" + header + "'");
                }
                return;
            }
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + header + "'");
        }
        throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + header + "'");
    }

    public void initReaderAndWriter(String str, long j, Streams streams) {
        synchronized (this) {
            this.streams = streams;
            this.writer = new WebSocketWriter(streams.client, streams.sink, this.random);
            this.executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(str, false));
            if (j != 0) {
                this.executor.scheduleAtFixedRate(new PingRunnable(), j, j, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                runWriter();
            }
        }
        this.reader = new WebSocketReader(streams.client, streams.source, this);
    }

    public void loopReader() {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }

    boolean processNextFrame() {
        try {
            this.reader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            failWebSocket(e, null);
            return false;
        }
    }

    void awaitTermination(int i, TimeUnit timeUnit) {
        this.executor.awaitTermination((long) i, timeUnit);
    }

    void tearDown() {
        if (this.cancelFuture != null) {
            this.cancelFuture.cancel(false);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    synchronized int pingCount() {
        return this.pingCount;
    }

    synchronized int pongCount() {
        return this.pongCount;
    }

    public void onReadMessage(String str) {
        this.listener.onMessage((WebSocket) this, str);
    }

    public void onReadMessage(ByteString byteString) {
        this.listener.onMessage((WebSocket) this, byteString);
    }

    public synchronized void onReadPing(ByteString byteString) {
        if (!(this.failed || (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()))) {
            this.pongQueue.add(byteString);
            runWriter();
            this.pingCount++;
        }
    }

    public synchronized void onReadPong(ByteString byteString) {
        this.pongCount++;
    }

    public void onReadClose(int i, String str) {
        if (i == -1) {
            throw new IllegalArgumentException();
        }
        Closeable closeable;
        synchronized (this) {
            if (this.receivedCloseCode != -1) {
                throw new IllegalStateException("already closed");
            }
            this.receivedCloseCode = i;
            this.receivedCloseReason = str;
            if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                Streams streams = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                this.executor.shutdown();
                closeable = streams;
            } else {
                closeable = null;
            }
        }
        try {
            this.listener.onClosing(this, i, str);
            if (closeable != null) {
                this.listener.onClosed(this, i, str);
            }
            Util.closeQuietly(closeable);
        } catch (Throwable th) {
            Util.closeQuietly(closeable);
        }
    }

    public boolean send(String str) {
        if (str != null) {
            return send(ByteString.encodeUtf8(str), 1);
        }
        throw new NullPointerException("text == null");
    }

    public boolean send(ByteString byteString) {
        if (byteString != null) {
            return send(byteString, 2);
        }
        throw new NullPointerException("bytes == null");
    }

    private synchronized boolean send(ByteString byteString, int i) {
        boolean z = false;
        synchronized (this) {
            if (!(this.failed || this.enqueuedClose)) {
                if (this.queueSize + ((long) byteString.size()) > MAX_QUEUE_SIZE) {
                    close(1001, null);
                } else {
                    this.queueSize += (long) byteString.size();
                    this.messageAndCloseQueue.add(new Message(i, byteString));
                    runWriter();
                    z = true;
                }
            }
        }
        return z;
    }

    synchronized boolean pong(ByteString byteString) {
        boolean z;
        if (this.failed || (this.enqueuedClose && this.messageAndCloseQueue.isEmpty())) {
            z = false;
        } else {
            this.pongQueue.add(byteString);
            runWriter();
            z = true;
        }
        return z;
    }

    public boolean close(int i, String str) {
        return close(i, str, 60000);
    }

    synchronized boolean close(int i, String str, long j) {
        boolean z = true;
        synchronized (this) {
            WebSocketProtocol.validateCloseCode(i);
            ByteString byteString = null;
            if (str != null) {
                byteString = ByteString.encodeUtf8(str);
                if (((long) byteString.size()) > 123) {
                    throw new IllegalArgumentException("reason.size() > 123: " + str);
                }
            }
            if (this.failed || this.enqueuedClose) {
                z = false;
            } else {
                this.enqueuedClose = true;
                this.messageAndCloseQueue.add(new Close(i, byteString, j));
                runWriter();
            }
        }
        return z;
    }

    private void runWriter() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean writeOneFrame() {
        /*
        r13 = this;
        r2 = 0;
        r7 = -1;
        r4 = 0;
        monitor-enter(r13);
        r1 = r13.failed;	 Catch:{ all -> 0x0060 }
        if (r1 == 0) goto L_0x000b;
    L_0x0008:
        monitor-exit(r13);	 Catch:{ all -> 0x0060 }
        r1 = r2;
    L_0x000a:
        return r1;
    L_0x000b:
        r8 = r13.writer;	 Catch:{ all -> 0x0060 }
        r1 = r13.pongQueue;	 Catch:{ all -> 0x0060 }
        r1 = r1.poll();	 Catch:{ all -> 0x0060 }
        r1 = (okio.ByteString) r1;	 Catch:{ all -> 0x0060 }
        if (r1 != 0) goto L_0x00ba;
    L_0x0017:
        r3 = r13.messageAndCloseQueue;	 Catch:{ all -> 0x0060 }
        r3 = r3.poll();	 Catch:{ all -> 0x0060 }
        r5 = r3 instanceof okhttp3.internal.ws.RealWebSocket.Close;	 Catch:{ all -> 0x0060 }
        if (r5 == 0) goto L_0x005b;
    L_0x0021:
        r6 = r13.receivedCloseCode;	 Catch:{ all -> 0x0060 }
        r5 = r13.receivedCloseReason;	 Catch:{ all -> 0x0060 }
        if (r6 == r7) goto L_0x0041;
    L_0x0027:
        r2 = r13.streams;	 Catch:{ all -> 0x0060 }
        r4 = 0;
        r13.streams = r4;	 Catch:{ all -> 0x0060 }
        r4 = r13.executor;	 Catch:{ all -> 0x0060 }
        r4.shutdown();	 Catch:{ all -> 0x0060 }
        r4 = r5;
        r5 = r6;
        r12 = r2;
        r2 = r3;
        r3 = r12;
    L_0x0036:
        monitor-exit(r13);	 Catch:{ all -> 0x0060 }
        if (r1 == 0) goto L_0x0063;
    L_0x0039:
        r8.writePong(r1);	 Catch:{ all -> 0x0095 }
    L_0x003c:
        r1 = 1;
        okhttp3.internal.Util.closeQuietly(r3);
        goto L_0x000a;
    L_0x0041:
        r7 = r13.executor;	 Catch:{ all -> 0x0060 }
        r9 = new okhttp3.internal.ws.RealWebSocket$CancelRunnable;	 Catch:{ all -> 0x0060 }
        r9.<init>();	 Catch:{ all -> 0x0060 }
        r0 = r3;
        r0 = (okhttp3.internal.ws.RealWebSocket.Close) r0;	 Catch:{ all -> 0x0060 }
        r2 = r0;
        r10 = r2.cancelAfterCloseMillis;	 Catch:{ all -> 0x0060 }
        r2 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ all -> 0x0060 }
        r2 = r7.schedule(r9, r10, r2);	 Catch:{ all -> 0x0060 }
        r13.cancelFuture = r2;	 Catch:{ all -> 0x0060 }
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0036;
    L_0x005b:
        if (r3 != 0) goto L_0x00b5;
    L_0x005d:
        monitor-exit(r13);	 Catch:{ all -> 0x0060 }
        r1 = r2;
        goto L_0x000a;
    L_0x0060:
        r1 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x0060 }
        throw r1;
    L_0x0063:
        r1 = r2 instanceof okhttp3.internal.ws.RealWebSocket.Message;	 Catch:{ all -> 0x0095 }
        if (r1 == 0) goto L_0x009a;
    L_0x0067:
        r0 = r2;
        r0 = (okhttp3.internal.ws.RealWebSocket.Message) r0;	 Catch:{ all -> 0x0095 }
        r1 = r0;
        r1 = r1.data;	 Catch:{ all -> 0x0095 }
        r2 = (okhttp3.internal.ws.RealWebSocket.Message) r2;	 Catch:{ all -> 0x0095 }
        r2 = r2.formatOpcode;	 Catch:{ all -> 0x0095 }
        r4 = r1.size();	 Catch:{ all -> 0x0095 }
        r4 = (long) r4;	 Catch:{ all -> 0x0095 }
        r2 = r8.newMessageSink(r2, r4);	 Catch:{ all -> 0x0095 }
        r2 = okio.Okio.buffer(r2);	 Catch:{ all -> 0x0095 }
        r2.write(r1);	 Catch:{ all -> 0x0095 }
        r2.close();	 Catch:{ all -> 0x0095 }
        monitor-enter(r13);	 Catch:{ all -> 0x0095 }
        r4 = r13.queueSize;	 Catch:{ all -> 0x0092 }
        r1 = r1.size();	 Catch:{ all -> 0x0092 }
        r1 = (long) r1;	 Catch:{ all -> 0x0092 }
        r1 = r4 - r1;
        r13.queueSize = r1;	 Catch:{ all -> 0x0092 }
        monitor-exit(r13);	 Catch:{ all -> 0x0092 }
        goto L_0x003c;
    L_0x0092:
        r1 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x0092 }
        throw r1;	 Catch:{ all -> 0x0095 }
    L_0x0095:
        r1 = move-exception;
        okhttp3.internal.Util.closeQuietly(r3);
        throw r1;
    L_0x009a:
        r1 = r2 instanceof okhttp3.internal.ws.RealWebSocket.Close;	 Catch:{ all -> 0x0095 }
        if (r1 == 0) goto L_0x00af;
    L_0x009e:
        r2 = (okhttp3.internal.ws.RealWebSocket.Close) r2;	 Catch:{ all -> 0x0095 }
        r1 = r2.code;	 Catch:{ all -> 0x0095 }
        r2 = r2.reason;	 Catch:{ all -> 0x0095 }
        r8.writeClose(r1, r2);	 Catch:{ all -> 0x0095 }
        if (r3 == 0) goto L_0x003c;
    L_0x00a9:
        r1 = r13.listener;	 Catch:{ all -> 0x0095 }
        r1.onClosed(r13, r5, r4);	 Catch:{ all -> 0x0095 }
        goto L_0x003c;
    L_0x00af:
        r1 = new java.lang.AssertionError;	 Catch:{ all -> 0x0095 }
        r1.<init>();	 Catch:{ all -> 0x0095 }
        throw r1;	 Catch:{ all -> 0x0095 }
    L_0x00b5:
        r5 = r7;
        r2 = r3;
        r3 = r4;
        goto L_0x0036;
    L_0x00ba:
        r3 = r4;
        r5 = r7;
        r2 = r4;
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.RealWebSocket.writeOneFrame():boolean");
    }

    void writePingFrame() {
        synchronized (this) {
            if (this.failed) {
                return;
            }
            WebSocketWriter webSocketWriter = this.writer;
            try {
                webSocketWriter.writePing(ByteString.EMPTY);
            } catch (Exception e) {
                failWebSocket(e, null);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void failWebSocket(java.lang.Exception r4, okhttp3.Response r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.failed;	 Catch:{ all -> 0x002c }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);	 Catch:{ all -> 0x002c }
    L_0x0006:
        return;
    L_0x0007:
        r0 = 1;
        r3.failed = r0;	 Catch:{ all -> 0x002c }
        r1 = r3.streams;	 Catch:{ all -> 0x002c }
        r0 = 0;
        r3.streams = r0;	 Catch:{ all -> 0x002c }
        r0 = r3.cancelFuture;	 Catch:{ all -> 0x002c }
        if (r0 == 0) goto L_0x0019;
    L_0x0013:
        r0 = r3.cancelFuture;	 Catch:{ all -> 0x002c }
        r2 = 0;
        r0.cancel(r2);	 Catch:{ all -> 0x002c }
    L_0x0019:
        r0 = r3.executor;	 Catch:{ all -> 0x002c }
        if (r0 == 0) goto L_0x0022;
    L_0x001d:
        r0 = r3.executor;	 Catch:{ all -> 0x002c }
        r0.shutdown();	 Catch:{ all -> 0x002c }
    L_0x0022:
        monitor-exit(r3);	 Catch:{ all -> 0x002c }
        r0 = r3.listener;	 Catch:{ all -> 0x002f }
        r0.onFailure(r3, r4, r5);	 Catch:{ all -> 0x002f }
        okhttp3.internal.Util.closeQuietly(r1);
        goto L_0x0006;
    L_0x002c:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x002c }
        throw r0;
    L_0x002f:
        r0 = move-exception;
        okhttp3.internal.Util.closeQuietly(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.RealWebSocket.failWebSocket(java.lang.Exception, okhttp3.Response):void");
    }
}
