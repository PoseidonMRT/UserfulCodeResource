package okhttp3.internal.http;

import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    public Response intercept(Chain chain) {
        HttpCodec httpStream = ((RealInterceptorChain) chain).httpStream();
        StreamAllocation streamAllocation = ((RealInterceptorChain) chain).streamAllocation();
        Request request = chain.request();
        long currentTimeMillis = System.currentTimeMillis();
        httpStream.writeRequestHeaders(request);
        Builder builder = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if (HTTP.EXPECT_CONTINUE.equalsIgnoreCase(request.header(HTTP.EXPECT_DIRECTIVE))) {
                httpStream.flushRequest();
                builder = httpStream.readResponseHeaders(true);
            }
            if (builder == null) {
                BufferedSink buffer = Okio.buffer(httpStream.createRequestBody(request, request.body().contentLength()));
                request.body().writeTo(buffer);
                buffer.close();
            }
        }
        httpStream.finishRequest();
        if (builder == null) {
            builder = httpStream.readResponseHeaders(false);
        }
        Response build = builder.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int code = build.code();
        if (this.forWebSocket && code == 101) {
            build = build.newBuilder().body(Util.EMPTY_RESPONSE).build();
        } else {
            build = build.newBuilder().body(httpStream.openResponseBody(build)).build();
        }
        if ("close".equalsIgnoreCase(build.request().header(HTTP.CONN_DIRECTIVE)) || "close".equalsIgnoreCase(build.header(HTTP.CONN_DIRECTIVE))) {
            streamAllocation.noNewStreams();
        }
        if ((code != HttpStatus.SC_NO_CONTENT && code != HttpStatus.SC_RESET_CONTENT) || build.body().contentLength() <= 0) {
            return build;
        }
        throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + build.body().contentLength());
    }
}
