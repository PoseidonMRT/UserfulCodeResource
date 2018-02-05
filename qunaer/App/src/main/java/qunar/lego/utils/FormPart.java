package qunar.lego.utils;

import java.io.File;
import org.apache.http.entity.mime.MIME;
import org.apache.http.protocol.HTTP;

public class FormPart {
    final ContentBody body;
    final HttpHeader header;
    final String name;

    private FormPart(String str, ContentBody contentBody) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (contentBody == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = str;
            this.body = contentBody;
            this.header = new HttpHeader();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("form-data; name=\"");
            stringBuilder.append(this.name);
            stringBuilder.append("\"");
            if (contentBody.getFilename() != null) {
                stringBuilder.append("; filename=\"");
                stringBuilder.append(contentBody.getFilename());
                stringBuilder.append("\"");
            }
            this.header.addHeader(MIME.CONTENT_DISPOSITION, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(contentBody.getMimeType());
            if (contentBody.getCharset() != null) {
                stringBuilder.append(HTTP.CHARSET_PARAM);
                stringBuilder.append(contentBody.getCharset());
            }
            this.header.addHeader("Content-Type", stringBuilder.toString());
            this.header.addHeader(MIME.CONTENT_TRANSFER_ENC, contentBody.getTransferEncoding());
        }
    }

    public FormPart(String str, String str2) {
        this(str, new StringBody(str2));
    }

    public FormPart(String str, String str2, String str3) {
        this(str, new FileBody(new File(str2), str3));
    }

    public void addHeader(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Header name may not be null");
        }
        this.header.addHeader(str, str2);
    }
}
