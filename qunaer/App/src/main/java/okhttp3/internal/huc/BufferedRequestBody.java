package okhttp3.internal.huc;

import okhttp3.Request;
import okio.Buffer;
import okio.BufferedSink;
import org.apache.http.protocol.HTTP;

final class BufferedRequestBody extends OutputStreamRequestBody {
    final Buffer buffer = new Buffer();
    long contentLength = -1;

    BufferedRequestBody(long j) {
        initOutputStream(this.buffer, j);
    }

    public long contentLength() {
        return this.contentLength;
    }

    public Request prepareToSendRequest(Request request) {
        if (request.header(HTTP.CONTENT_LEN) != null) {
            return request;
        }
        outputStream().close();
        this.contentLength = this.buffer.size();
        return request.newBuilder().removeHeader(HTTP.TRANSFER_ENCODING).header(HTTP.CONTENT_LEN, Long.toString(this.buffer.size())).build();
    }

    public void writeTo(BufferedSink bufferedSink) {
        this.buffer.copyTo(bufferedSink.buffer(), 0, this.buffer.size());
    }
}
