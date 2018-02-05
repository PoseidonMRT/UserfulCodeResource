package okhttp3.internal.http;

import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;
import org.apache.http.cookie.SM;
import org.apache.http.protocol.HTTP;

public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public Response intercept(Chain chain) {
        boolean z = false;
        Request request = chain.request();
        Builder newBuilder = request.newBuilder();
        RequestBody body = request.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header(HTTP.CONTENT_LEN, Long.toString(contentLength));
                newBuilder.removeHeader(HTTP.TRANSFER_ENCODING);
            } else {
                newBuilder.header(HTTP.TRANSFER_ENCODING, HTTP.CHUNK_CODING);
                newBuilder.removeHeader(HTTP.CONTENT_LEN);
            }
        }
        if (request.header("Host") == null) {
            newBuilder.header("Host", Util.hostHeader(request.url(), false));
        }
        if (request.header(HTTP.CONN_DIRECTIVE) == null) {
            newBuilder.header(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        }
        if (request.header("Accept-Encoding") == null && request.header("Range") == null) {
            z = true;
            newBuilder.header("Accept-Encoding", "gzip");
        }
        List loadForRequest = this.cookieJar.loadForRequest(request.url());
        if (!loadForRequest.isEmpty()) {
            newBuilder.header(SM.COOKIE, cookieHeader(loadForRequest));
        }
        if (request.header(HTTP.USER_AGENT) == null) {
            newBuilder.header(HTTP.USER_AGENT, Version.userAgent());
        }
        Response proceed = chain.proceed(newBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), proceed.headers());
        Response.Builder request2 = proceed.newBuilder().request(request);
        if (z && "gzip".equalsIgnoreCase(proceed.header(HTTP.CONTENT_ENCODING)) && HttpHeaders.hasBody(proceed)) {
            Source gzipSource = new GzipSource(proceed.body().source());
            Headers build = proceed.headers().newBuilder().removeAll(HTTP.CONTENT_ENCODING).removeAll(HTTP.CONTENT_LEN).build();
            request2.headers(build);
            request2.body(new RealResponseBody(build, Okio.buffer(gzipSource)));
        }
        return request2.build();
    }

    private String cookieHeader(List<Cookie> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append("; ");
            }
            Cookie cookie = (Cookie) list.get(i);
            stringBuilder.append(cookie.name()).append('=').append(cookie.value());
        }
        return stringBuilder.toString();
    }
}
