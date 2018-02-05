package com.mapquest.android.maps;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.iflytek.cloud.SpeechConstant;
import com.mapquest.android.maps.TileCacher.CacheType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.HttpRequestWriter;
import org.apache.http.impl.io.HttpResponseParser;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.BasicLineParser;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

class ThreadBasedTileDownloader implements TileDownloader {
    private static final String TAG = ThreadBasedTileDownloader.class.getSimpleName();
    HttpConnectionPool httpConnectionPool = null;
    private NetworkListener listener = new NetworkListener();
    private MapView mapView;
    private int maxRunningDownloads = 2;
    private boolean networkAvailable = true;
    private Map<String, Tile> queue;
    Set<String> runningDownloads = Collections.synchronizedSet(new HashSet());
    private ArrayList<TileDownloadThread> threads = null;
    private TileCacher tileCacher;

    class BlockQueue<E> implements Queue<E> {
        private volatile boolean blocked = false;
        private Queue<E> queue;

        public BlockQueue(Queue<E> queue) {
            this.queue = queue;
        }

        public void block() {
            this.blocked = true;
        }

        public boolean isBlocked() {
            return this.blocked;
        }

        public void throwExceptionIfBlocked() {
            if (this.blocked) {
                throw new BlockedQueueException("Queue instance is blocked and cannot be used further");
            }
        }

        public E element() {
            throwExceptionIfBlocked();
            return this.queue.element();
        }

        public boolean offer(E e) {
            throwExceptionIfBlocked();
            return this.queue.offer(e);
        }

        public E peek() {
            throwExceptionIfBlocked();
            return this.queue.peek();
        }

        public E poll() {
            throwExceptionIfBlocked();
            return this.queue.poll();
        }

        public E remove() {
            throwExceptionIfBlocked();
            return this.queue.remove();
        }

        public boolean add(E e) {
            throwExceptionIfBlocked();
            return this.queue.add(e);
        }

        public boolean addAll(Collection<? extends E> collection) {
            throwExceptionIfBlocked();
            return this.queue.addAll(collection);
        }

        public void clear() {
            this.queue.clear();
        }

        public boolean contains(Object obj) {
            throwExceptionIfBlocked();
            return this.queue.contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            throwExceptionIfBlocked();
            return this.queue.containsAll(collection);
        }

        public boolean isEmpty() {
            throwExceptionIfBlocked();
            return this.queue.isEmpty();
        }

        public Iterator<E> iterator() {
            throwExceptionIfBlocked();
            return this.queue.iterator();
        }

        public boolean remove(Object obj) {
            throwExceptionIfBlocked();
            return this.queue.remove(obj);
        }

        public boolean removeAll(Collection<?> collection) {
            throwExceptionIfBlocked();
            return this.queue.removeAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            throwExceptionIfBlocked();
            return this.queue.removeAll(collection);
        }

        public int size() {
            return this.queue.size();
        }

        public Object[] toArray() {
            throwExceptionIfBlocked();
            return this.queue.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            throwExceptionIfBlocked();
            return this.queue.toArray(tArr);
        }
    }

    class BlockedQueueException extends IllegalStateException {
        public BlockedQueueException(String str, Throwable th) {
            super(str, th);
        }

        public BlockedQueueException(String str) {
            super(str);
        }

        public BlockedQueueException(Throwable th) {
            super(th);
        }
    }

    class TileDownloadThread extends Thread implements Comparator<Tile> {
        BlockQueue<Tile> queue;
        volatile boolean stop;

        private TileDownloadThread() {
            this.queue = new BlockQueue(new PriorityBlockingQueue(50, this));
            this.stop = false;
        }

        void addToQueue(Tile tile) {
            this.queue.add(tile);
        }

        public void clearQueue() {
            this.queue.clear();
        }

        void shutdown() {
            this.queue.clear();
            this.stop = true;
            interrupt();
        }

        public int compare(Tile tile, Tile tile2) {
            return ThreadBasedTileDownloader.this.tileCacher.contains(tile2) ? 1 : -1;
        }

        boolean constructTile(Tile tile) {
            if (ThreadBasedTileDownloader.this.tileCacher == null || tile.getBytes() == null || tile.getBitmap() != null) {
                return false;
            }
            try {
                byte[] bytes = tile.getBytes();
                tile.setBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                return true;
            } catch (OutOfMemoryError e) {
                return false;
            }
        }

        Tile fetchTileFromCache(Tile tile) {
            return ThreadBasedTileDownloader.this.tileCacher.getCache(CacheType.ALL).getTile(tile);
        }

        Tile fetchTileFromNetwork(Tile tile) {
            byte[] bArr = ThreadBasedTileDownloader.this.getHttpConnectionPoolInstance().get(tile.getUrl());
            tile.setBytes(bArr);
            tile.setBytes(bArr);
            return tile;
        }

        Tile fetchTile(Tile tile) {
            Tile fetchTileFromCache = fetchTileFromCache(tile);
            if (fetchTileFromCache == null || !fetchTileFromCache.isValid()) {
                return fetchTileFromNetwork(tile);
            }
            return fetchTileFromCache;
        }

        void addToMemoryCache(Tile tile) {
            if (tile.getBitmap() != null && ThreadBasedTileDownloader.this.tileCacher != null) {
                ITileCache cache = ThreadBasedTileDownloader.this.tileCacher.getCache(CacheType.MEMORY);
                if (cache != null) {
                    cache.addTile(tile);
                }
            }
        }

        void addToDiskCache(Tile tile) {
            if (ThreadBasedTileDownloader.this.tileCacher != null && tile != null && tile.getBytes() != null) {
                ITileCache cache = ThreadBasedTileDownloader.this.tileCacher.getCache(CacheType.DB);
                if (!(cache == null || cache.contains(tile))) {
                    cache.addTile(tile);
                }
                tile.setBytes((byte[]) null);
            }
        }

        public void processTile(Tile tile) {
            if (tile != null) {
                ThreadBasedTileDownloader.this.addToRunning(tile);
                try {
                    if (ThreadBasedTileDownloader.this.tileCacher != null) {
                        fetchTile(tile);
                        if (constructTile(tile)) {
                            addToMemoryCache(tile);
                            addToDiskCache(tile);
                            ThreadBasedTileDownloader.this.finishedDownload(tile);
                        }
                        if (!tile.isValid()) {
                            ThreadBasedTileDownloader.this.mapView.preLoadDelayed(5000);
                        }
                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                    }
                } catch (Exception e) {
                } catch (OutOfMemoryError e2) {
                } finally {
                    if (!tile.isValid()) {
                        ThreadBasedTileDownloader.this.mapView.preLoadDelayed(5000);
                    }
                    ThreadBasedTileDownloader.this.removeFromRunning(tile);
                }
            }
        }

        public void run() {
            while (!this.stop) {
                try {
                    processTile((Tile) this.queue.poll());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        public void endQueue() {
        }
    }

    class HttpPipelinerThread extends TileDownloadThread {
        final int CLOSE_SOCKET;
        final int CONNECTION_TIME_OUT;
        final int DO_PROCESS;
        final int FORCE_CLOSE_SOCKET;
        final int STALE_CONNECTION_TIME_OUT;
        private ByteArrayOutputStream buffer;
        Handler handler;
        private String host;
        BlockQueue<Tile> inProcessQueue;
        private int keepAliveTimeout;
        Looper looper;
        private HttpParams params;
        private int port;
        HttpRequestWriter requestWriter;
        HttpResponseParser responseParser;
        private Socket socket;
        private SocketInputBuffer socketIn;
        private SocketOutputBuffer socketOut;

        class DownloadHandler extends Handler {
            final String TAG = ("mq.maps.downloader_" + Thread.currentThread().getId());
            final MapView mapView;

            DownloadHandler() {
                this.mapView = ThreadBasedTileDownloader.this.mapView;
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        Object obj = null;
                        Tile tile;
                        try {
                            HttpPipelinerThread.this.inProcessQueue.clear();
                            while (true) {
                                tile = (Tile) HttpPipelinerThread.this.queue.poll();
                                if (tile == null) {
                                    HttpPipelinerThread.this.flushSocket();
                                    while (true) {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.peek();
                                        if (tile == null) {
                                            sendEmptyMessageDelayed(2, (long) HttpPipelinerThread.this.keepAliveTimeout);
                                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                                while (true) {
                                                    try {
                                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                                        if (tile == null) {
                                                            break;
                                                        }
                                                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                                    } catch (BlockedQueueException e) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        tile.setBytes(HttpPipelinerThread.this.readTileResponse(HttpPipelinerThread.this.responseParser));
                                        if (HttpPipelinerThread.this.constructTile(tile)) {
                                            HttpPipelinerThread.this.addToMemoryCache(tile);
                                            HttpPipelinerThread.this.addToDiskCache(tile);
                                            ThreadBasedTileDownloader.this.finishedDownload(tile);
                                        }
                                        HttpPipelinerThread.this.inProcessQueue.poll();
                                    }
                                } else {
                                    Object obj2;
                                    ThreadBasedTileDownloader.this.addToRunning(tile);
                                    Tile fetchTileFromCache = HttpPipelinerThread.this.fetchTileFromCache(tile);
                                    if (fetchTileFromCache == null || !fetchTileFromCache.isValid()) {
                                        if (ThreadBasedTileDownloader.this.networkAvailable) {
                                            HttpPipelinerThread.this.inProcessQueue.add(tile);
                                            HttpPipelinerThread.this.sendTileRequest(tile);
                                            if (obj == null) {
                                                removeMessages(2);
                                                obj2 = 1;
                                            }
                                        }
                                        obj2 = obj;
                                    } else {
                                        if (HttpPipelinerThread.this.constructTile(fetchTileFromCache)) {
                                            HttpPipelinerThread.this.addToMemoryCache(fetchTileFromCache);
                                            ThreadBasedTileDownloader.this.finishedDownload(fetchTileFromCache);
                                            obj2 = obj;
                                        }
                                        obj2 = obj;
                                    }
                                    obj = obj2;
                                }
                            }
                        } catch (BlockedQueueException e2) {
                            HttpPipelinerThread.this.inProcessQueue.clear();
                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                while (true) {
                                    try {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                        if (tile == null) {
                                            break;
                                        }
                                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                    } catch (BlockedQueueException e3) {
                                        break;
                                    }
                                }
                            }
                        } catch (IOException e4) {
                            HttpPipelinerThread.this.close();
                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                while (true) {
                                    try {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                        if (tile == null) {
                                            break;
                                        }
                                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                    } catch (BlockedQueueException e5) {
                                        break;
                                    }
                                }
                            }
                        } catch (HttpException e6) {
                            HttpPipelinerThread.this.close();
                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                while (true) {
                                    try {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                        if (tile == null) {
                                            break;
                                        }
                                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                    } catch (BlockedQueueException e7) {
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e8) {
                            HttpPipelinerThread.this.close();
                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                while (true) {
                                    try {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                        if (tile == null) {
                                            break;
                                        }
                                        ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                    } catch (BlockedQueueException e9) {
                                        break;
                                    }
                                }
                            }
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            if (HttpPipelinerThread.this.inProcessQueue.size() > 0) {
                                while (true) {
                                    try {
                                        tile = (Tile) HttpPipelinerThread.this.inProcessQueue.poll();
                                        if (tile != null) {
                                            ThreadBasedTileDownloader.this.removeFromRunning(tile);
                                        }
                                    } catch (BlockedQueueException e10) {
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                    case 3:
                        HttpPipelinerThread.this.handler.removeMessages(2);
                        HttpPipelinerThread.this.handler.removeMessages(3);
                        HttpPipelinerThread.this.close();
                        break;
                }
                super.handleMessage(message);
            }
        }

        public HttpPipelinerThread() {
            super();
            this.CONNECTION_TIME_OUT = 5000;
            this.DO_PROCESS = 1;
            this.CLOSE_SOCKET = 2;
            this.FORCE_CLOSE_SOCKET = 3;
            this.STALE_CONNECTION_TIME_OUT = 30000;
            this.host = "";
            this.keepAliveTimeout = 5000;
            this.params = new BasicHttpParams();
            this.requestWriter = null;
            this.responseParser = null;
            this.inProcessQueue = new BlockQueue(new LinkedList());
            this.buffer = new ByteArrayOutputStream();
            this.params = new BasicHttpParams();
            HttpProtocolParams.setVersion(this.params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setUserAgent(this.params, "mqandroid/1.1");
        }

        private void connect(String str, int i) {
            if (i == -1) {
                i = 80;
            }
            if (!(str.equals(this.host) || i == this.port)) {
                close();
            }
            if (this.socket == null) {
                this.host = str;
                this.port = i;
                this.socket = new Socket(str, i);
                if (this.handler != null) {
                    this.handler.sendEmptyMessageDelayed(3, 30000);
                }
                this.socket.setSoLinger(false, 0);
                this.socket.setSoTimeout(5000);
                this.socketIn = new SocketInputBuffer(this.socket, 1500, this.params);
                this.socketOut = new SocketOutputBuffer(this.socket, 1500, this.params);
                this.requestWriter = new HttpRequestWriter(this.socketOut, new BasicLineFormatter(), this.params);
                this.responseParser = new HttpResponseParser(this.socketIn, new BasicLineParser(), new DefaultHttpResponseFactory(), this.params);
            }
        }

        private void sendTileRequest(Tile tile) {
            URI uri = new URI(tile.getUrl());
            connect(uri.getHost(), uri.getPort());
            HttpMessage basicHttpRequest = new BasicHttpRequest(HttpGet.METHOD_NAME, uri.getPath());
            String host = uri.getHost();
            if (uri.getPort() > 0) {
                host = host + ':' + uri.getPort();
            }
            basicHttpRequest.addHeader("Host", host);
            basicHttpRequest.addHeader(HTTP.CONN_DIRECTIVE, "keep-alive");
            this.requestWriter.write(basicHttpRequest);
        }

        private byte[] readTileResponse(HttpResponseParser httpResponseParser) {
            HttpMessage parse = httpResponseParser.parse();
            HttpResponse httpResponse = (HttpResponse) parse;
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            Header firstHeader = parse.getFirstHeader(HTTP.CONTENT_ENCODING);
            if (firstHeader != null) {
                basicHttpEntity.setContentEncoding(firstHeader);
            }
            firstHeader = parse.getFirstHeader("Content-Type");
            if (firstHeader != null) {
                basicHttpEntity.setContentType(firstHeader);
            }
            firstHeader = parse.getFirstHeader(HTTP.CONTENT_LEN);
            if (firstHeader != null) {
                long parseLong = Long.parseLong(firstHeader.getValue());
                basicHttpEntity.setContentLength(parseLong);
                basicHttpEntity.setContent(new ContentLengthInputStream(this.socketIn, parseLong));
            }
            Header firstHeader2 = parse.getFirstHeader(HTTP.TRANSFER_ENCODING);
            if (firstHeader2 != null && firstHeader2.getValue().indexOf(HTTP.CHUNK_CODING) >= 0) {
                basicHttpEntity.setChunked(true);
                basicHttpEntity.setContent(new ChunkedInputStream(this.socketIn));
            }
            BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (basicHeaderElementIterator.hasNext()) {
                HeaderElement nextElement = basicHeaderElementIterator.nextElement();
                String name = nextElement.getName();
                String value = nextElement.getValue();
                if (value != null && name.equalsIgnoreCase(SpeechConstant.NET_TIMEOUT)) {
                    try {
                        this.keepAliveTimeout = Integer.parseInt(value) * 1000;
                        if (this.keepAliveTimeout > 5000) {
                            this.keepAliveTimeout = 5000;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            httpResponse.setEntity(basicHttpEntity);
            this.buffer.reset();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                basicHttpEntity.writeTo(this.buffer);
                this.buffer.flush();
                this.buffer.close();
                return this.buffer.toByteArray();
            }
            basicHttpEntity.writeTo(this.buffer);
            return null;
        }

        private void flushSocket() {
            if (this.socketOut != null) {
                this.socketOut.flush();
            }
        }

        public void run() {
            try {
                Looper.prepare();
                this.looper = Looper.myLooper();
                this.handler = new DownloadHandler();
                if (this.queue.size() > 0) {
                    this.handler.sendEmptyMessage(1);
                }
                Looper.loop();
            } catch (Exception e) {
            }
            this.inProcessQueue.clear();
            close();
        }

        public void close() {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                }
            }
            this.socket = null;
            this.socketIn = null;
            this.socketOut = null;
            this.requestWriter = null;
            this.responseParser = null;
        }

        public void shutdown() {
            this.stop = true;
            this.queue.block();
            this.queue.clear();
            this.inProcessQueue.block();
            if (this.handler != null) {
                this.handler.removeMessages(1);
                this.handler.removeMessages(2);
                this.handler.removeMessages(3);
                this.handler = null;
            }
            if (this.looper != null) {
                this.looper.quit();
            }
        }

        public void endQueue() {
            if (this.handler != null) {
                this.handler.sendEmptyMessage(1);
            }
        }
    }

    class NetworkListener extends Handler {
        private NetworkListener() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 61:
                    ThreadBasedTileDownloader.this.networkAvailable = true;
                    break;
                case 62:
                    ThreadBasedTileDownloader.this.networkAvailable = false;
                    break;
            }
            super.handleMessage(message);
        }
    }

    public ThreadBasedTileDownloader(MapView mapView, TileCacher tileCacher) {
        this.mapView = mapView;
        this.tileCacher = tileCacher;
        this.queue = new LinkedHashMap(30);
        this.threads = new ArrayList();
        this.networkAvailable = NetworkConnectivityListener.getLastKnownNetworkState();
        EventDispatcher.registerHandler(this.listener);
        for (int i = 0; i < this.maxRunningDownloads; i++) {
            HttpPipelinerThread httpPipelinerThread = new HttpPipelinerThread();
            this.threads.add(httpPipelinerThread);
            httpPipelinerThread.start();
        }
    }

    private void setThreadPriority(int i) {
        Iterator it = this.threads.iterator();
        while (it.hasNext()) {
            TileDownloadThread tileDownloadThread = (TileDownloadThread) it.next();
            if (tileDownloadThread.isAlive()) {
                tileDownloadThread.setPriority(i);
            }
        }
    }

    private HttpConnectionPool getHttpConnectionPoolInstance() {
        if (this.httpConnectionPool == null) {
            synchronized (this) {
                if (this.httpConnectionPool == null) {
                    this.httpConnectionPool = new HttpConnectionPool(this.maxRunningDownloads, this.maxRunningDownloads, HttpConnectionPool.CONNECTION_TIMEOUT, 5000);
                }
            }
        }
        return this.httpConnectionPool;
    }

    public void destroy() {
        this.queue.clear();
        this.runningDownloads.clear();
        Iterator it = this.threads.iterator();
        while (it.hasNext()) {
            ((TileDownloadThread) it.next()).shutdown();
        }
        this.threads.clear();
        EventDispatcher.removeHandler(this.listener);
        this.tileCacher = null;
        if (this.httpConnectionPool != null) {
            this.httpConnectionPool.shutdown();
        }
        this.mapView = null;
    }

    public void beginQueue() {
        this.queue.clear();
    }

    public void queueTile(Tile tile) {
        String buildCacheKey = tile.buildCacheKey();
        if (this.tileCacher == null) {
            this.queue.put(buildCacheKey, tile);
        } else {
            this.queue.put(buildCacheKey, tile);
        }
    }

    public void endQueue() {
        int i = 0;
        int size = this.runningDownloads.size();
        int size2 = this.queue.size();
        if (size2 != 0) {
            int i2;
            Iterator it;
            TileDownloadThread tileDownloadThread;
            int i3 = 0;
            for (Entry value : this.queue.entrySet()) {
                if (this.runningDownloads.contains(((Tile) value.getValue()).buildCacheKey())) {
                    i2 = i3 + 1;
                } else {
                    i2 = i3;
                }
                i3 = i2;
            }
            if ((size2 - i3) + (size - i3) > ((int) (((double) size2) * 1.25d))) {
                it = this.threads.iterator();
                while (it.hasNext()) {
                    tileDownloadThread = (TileDownloadThread) it.next();
                    if (tileDownloadThread.isAlive()) {
                        tileDownloadThread.shutdown();
                    }
                    it.remove();
                }
                this.runningDownloads.clear();
            }
            i2 = this.maxRunningDownloads;
            Iterator it2 = this.threads.iterator();
            i3 = i2;
            while (it2.hasNext()) {
                tileDownloadThread = (TileDownloadThread) it2.next();
                if (tileDownloadThread.isAlive()) {
                    i3--;
                } else {
                    it2.remove();
                }
                tileDownloadThread.clearQueue();
            }
            if (i3 > 0) {
                for (i2 = 0; i2 < i3; i2++) {
                    HttpPipelinerThread httpPipelinerThread = new HttpPipelinerThread();
                    this.threads.add(httpPipelinerThread);
                    httpPipelinerThread.start();
                }
            }
            it2 = this.queue.entrySet().iterator();
            while (it2.hasNext()) {
                ((TileDownloadThread) this.threads.get(i % this.maxRunningDownloads)).addToQueue((Tile) ((Entry) it2.next()).getValue());
                it2.remove();
                i++;
            }
            it = this.threads.iterator();
            while (it.hasNext()) {
                ((TileDownloadThread) it.next()).endQueue();
            }
        }
    }

    private void addToRunning(Tile tile) {
        this.runningDownloads.add(tile.buildCacheKey());
    }

    private void removeFromRunning(Tile tile) {
        this.runningDownloads.remove(tile.buildCacheKey());
    }

    public void clearQueue() {
        beginQueue();
    }

    public void finishedDownload(Tile tile) {
        removeFromRunning(tile);
        if (tile.isValid() && this.mapView != null) {
            this.mapView.addTile(tile);
        }
    }
}
