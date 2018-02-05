package com.mqunar.yacca;

import com.mqunar.tools.DateTimeUtils;
import com.mqunar.yacca.Cmd.Builder;
import com.mqunar.yacca.Msg.OnMsg;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class YaccaBridge {
    public static final int READ_BUFFER_SIZE = 65536;
    public static final int WRITE_BUFFER_SIZE = 65536;
    private YaccaCallback callback;
    private final String hostname;
    private OnMsg msgCallback;
    private final int port;
    private Queue<Cmd> queue;
    private ByteBuffer readBuffer;
    private AtomicBoolean runFlag = new AtomicBoolean(false);
    private final Runnable runnable;
    private Selector selector;
    private SocketChannel socketChannel;
    private long startTime;
    private final ThreadFactory threadFactory;
    private long timeout = DateTimeUtils.ONE_MINUTE;
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    private ByteBuffer writeBuffer;
    private Thread yaccaThread;

    public YaccaBridge(String str, int i) {
        this.hostname = str;
        this.port = i;
        this.runnable = new Runnable() {
            public void run() {
                try {
                    YaccaBridge.this.initConn();
                    YaccaBridge.this.callback.onOpen(YaccaBridge.this);
                    YaccaBridge.this.loop2();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        };
        this.uncaughtExceptionHandler = new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
                YaccaBridge.this.yaccaThread = null;
                YaccaBridge.this.readBuffer.clear();
                YaccaBridge.this.writeBuffer.clear();
                YaccaBridge.this.queue.clear();
                YaccaBridge.this.msgCallback = null;
                if (YaccaBridge.this.callback != null) {
                    YaccaBridge.this.callback.onClose(YaccaBridge.this);
                }
            }
        };
        this.threadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Yacca @" + YaccaBridge.this.hashCode() + " #" + this.mCount.getAndIncrement());
                thread.setUncaughtExceptionHandler(YaccaBridge.this.uncaughtExceptionHandler);
                return thread;
            }
        };
        this.queue = new ArrayBlockingQueue(100);
        this.readBuffer = ByteBuffer.allocateDirect(65536);
        this.writeBuffer = ByteBuffer.allocateDirect(65536);
    }

    private void initConn() {
        this.socketChannel = SocketChannel.open();
        this.socketChannel.configureBlocking(false);
        this.socketChannel.socket().setTcpNoDelay(true);
        this.selector = SelectorProvider.provider().openSelector();
        this.socketChannel.register(this.selector, 8);
        this.socketChannel.connect(new InetSocketAddress(this.hostname, this.port));
    }

    private void loop2() {
        this.runFlag.set(true);
        this.readBuffer.clear();
        this.writeBuffer.limit(0);
        long currentTimeMillis = System.currentTimeMillis();
        while (this.runFlag.get()) {
            long currentTimeMillis2 = System.currentTimeMillis();
            if (currentTimeMillis2 - currentTimeMillis >= this.timeout / 2) {
                send(Builder.heartbeat());
            }
            try {
                this.selector.select(this.timeout / 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterator it = this.selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) it.next();
                it.remove();
                try {
                    if (selectionKey.isValid()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        if (selectionKey.isConnectable()) {
                            socketChannel.finishConnect();
                            selectionKey.interestOps(5);
                        }
                        if (selectionKey.isReadable()) {
                            if (socketChannel.read(this.readBuffer) == -1) {
                                throw new IOException();
                            }
                            this.readBuffer.flip();
                            while (true) {
                                Msg readMsg = Msg.readMsg(this.readBuffer);
                                if (readMsg == null) {
                                    this.readBuffer.compact();
                                } else if (this.msgCallback != null) {
                                    switch (readMsg.protocol) {
                                        case 0:
                                            this.msgCallback.onConnected();
                                            break;
                                        case 15:
                                            this.msgCallback.onMessage((byte[]) readMsg.data.get("appName"), ((Integer) readMsg.data.get("agentId")).intValue(), (byte[]) readMsg.data.get("msg"));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        if (!selectionKey.isWritable()) {
                            continue;
                        } else if (this.writeBuffer.remaining() > 0 && ((long) socketChannel.write(this.writeBuffer)) <= 0) {
                            throw new IOException();
                        } else if (this.writeBuffer.remaining() <= 0) {
                            Cmd cmd = (Cmd) this.queue.poll();
                            if (cmd != null) {
                                this.writeBuffer.clear();
                                cmd.getByteBuffer2(this.writeBuffer);
                                this.writeBuffer.flip();
                            } else {
                                selectionKey.interestOps(1);
                            }
                        }
                    } else {
                        throw new IOException();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    try {
                        selectionKey.channel().close();
                    } catch (IOException e3) {
                    }
                    selectionKey.cancel();
                    stop();
                }
            }
            currentTimeMillis = currentTimeMillis2;
        }
        if (this.callback != null) {
            this.callback.onClose(this);
        }
    }

    public void setTimeout(long j) {
        this.timeout = j;
    }

    public void listen() {
        if (this.yaccaThread == null) {
            this.yaccaThread = this.threadFactory.newThread(this.runnable);
            this.yaccaThread.start();
        }
    }

    public void stop() {
        if (this.runFlag.getAndSet(false)) {
            this.yaccaThread = null;
            this.readBuffer.clear();
            this.writeBuffer.clear();
            this.queue.clear();
            this.msgCallback = null;
            if (this.selector != null) {
                this.selector.wakeup();
            }
        }
    }

    public boolean send(Cmd cmd) {
        if (cmd == null) {
            return false;
        }
        boolean offer = this.queue.offer(cmd);
        if (offer && this.selector != null) {
            SelectionKey keyFor = this.socketChannel.keyFor(this.selector);
            if (keyFor.isValid()) {
                keyFor.interestOps(5);
            } else {
                try {
                    this.socketChannel.register(this.selector, 5);
                } catch (ClosedChannelException e) {
                    return false;
                }
            }
            this.selector.wakeup();
        }
        return offer;
    }

    public void setYaccaCallback(YaccaCallback yaccaCallback) {
        this.callback = yaccaCallback;
    }

    public void setMsgCallback(OnMsg onMsg) {
        this.msgCallback = onMsg;
    }
}
