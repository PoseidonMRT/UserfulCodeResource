package okhttp3.internal.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheStrategy.Factory;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AUTH;
import org.apache.http.protocol.HTTP;

public final class CacheInterceptor implements Interceptor {
    final InternalCache cache;

    public CacheInterceptor(InternalCache internalCache) {
        this.cache = internalCache;
    }

    public Response intercept(Chain chain) {
        Response response = null;
        Response response2 = this.cache != null ? this.cache.get(chain.request()) : response;
        CacheStrategy cacheStrategy = new Factory(System.currentTimeMillis(), chain.request(), response2).get();
        Request request = cacheStrategy.networkRequest;
        Response response3 = cacheStrategy.cacheResponse;
        if (this.cache != null) {
            this.cache.trackResponse(cacheStrategy);
        }
        if (response2 != null && response3 == null) {
            Util.closeQuietly(response2.body());
        }
        if (request == null && response3 == null) {
            return new Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        if (request == null) {
            return response3.newBuilder().cacheResponse(stripBody(response3)).build();
        }
        try {
            response = chain.proceed(request);
            if (response3 != null) {
                if (response.code() == HttpStatus.SC_NOT_MODIFIED) {
                    response2 = response3.newBuilder().headers(combine(response3.headers(), response.headers())).sentRequestAtMillis(response.sentRequestAtMillis()).receivedResponseAtMillis(response.receivedResponseAtMillis()).cacheResponse(stripBody(response3)).networkResponse(stripBody(response)).build();
                    response.body().close();
                    this.cache.trackConditionalCacheHit();
                    this.cache.update(response3, response2);
                    return response2;
                }
                Util.closeQuietly(response3.body());
            }
            response2 = response.newBuilder().cacheResponse(stripBody(response3)).networkResponse(stripBody(response)).build();
            if (HttpHeaders.hasBody(response2)) {
                return cacheWritingResponse(maybeCache(response2, response.request(), this.cache), response2);
            }
            return response2;
        } finally {
            if (response == null && response2 != null) {
                Util.closeQuietly(response2.body());
            }
        }
    }

    private static Response stripBody(Response response) {
        if (response == null || response.body() == null) {
            return response;
        }
        return response.newBuilder().body(null).build();
    }

    private CacheRequest maybeCache(Response response, Request request, InternalCache internalCache) {
        if (internalCache == null) {
            return null;
        }
        if (CacheStrategy.isCacheable(response, request)) {
            return internalCache.put(response);
        }
        if (!HttpMethod.invalidatesCache(request.method())) {
            return null;
        }
        try {
            internalCache.remove(request);
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private Response cacheWritingResponse(final CacheRequest cacheRequest, Response response) {
        if (cacheRequest == null) {
            return response;
        }
        Sink body = cacheRequest.body();
        if (body == null) {
            return response;
        }
        final BufferedSource source = response.body().source();
        final BufferedSink buffer = Okio.buffer(body);
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(new Source() {
            boolean cacheRequestClosed;

            public long read(Buffer buffer, long j) {
                try {
                    long read = source.read(buffer, j);
                    if (read == -1) {
                        if (!this.cacheRequestClosed) {
                            this.cacheRequestClosed = true;
                            buffer.close();
                        }
                        return -1;
                    }
                    buffer.copyTo(buffer.buffer(), buffer.size() - read, read);
                    buffer.emitCompleteSegments();
                    return read;
                } catch (IOException e) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        cacheRequest.abort();
                    }
                    throw e;
                }
            }

            public Timeout timeout() {
                return source.timeout();
            }

            public void close() {
                if (!(this.cacheRequestClosed || Util.discard(this, 100, TimeUnit.MILLISECONDS))) {
                    this.cacheRequestClosed = true;
                    cacheRequest.abort();
                }
                source.close();
            }
        }))).build();
    }

    private static Headers combine(Headers headers, Headers headers2) {
        int i;
        int i2 = 0;
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (i = 0; i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            if (!("Warning".equalsIgnoreCase(name) && value.startsWith("1")) && (!isEndToEnd(name) || headers2.get(name) == null)) {
                Internal.instance.addLenient(builder, name, value);
            }
        }
        i = headers2.size();
        while (i2 < i) {
            String name2 = headers2.name(i2);
            if (!HTTP.CONTENT_LEN.equalsIgnoreCase(name2) && isEndToEnd(name2)) {
                Internal.instance.addLenient(builder, name2, headers2.value(i2));
            }
            i2++;
        }
        return builder.build();
    }

    static boolean isEndToEnd(String str) {
        if (HTTP.CONN_DIRECTIVE.equalsIgnoreCase(str) || HTTP.CONN_KEEP_ALIVE.equalsIgnoreCase(str) || AUTH.PROXY_AUTH.equalsIgnoreCase(str) || AUTH.PROXY_AUTH_RESP.equalsIgnoreCase(str) || "TE".equalsIgnoreCase(str) || "Trailers".equalsIgnoreCase(str) || HTTP.TRANSFER_ENCODING.equalsIgnoreCase(str) || "Upgrade".equalsIgnoreCase(str)) {
            return false;
        }
        return true;
    }
}
