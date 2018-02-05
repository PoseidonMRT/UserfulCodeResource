package qunar.lego.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.protocol.HTTP;

public class MultipartEntity {
    private static final byte[] CR_LF = IOUtils.LINE_SEPARATOR_WINDOWS.getBytes(Charset.defaultCharset());
    private static final byte[] FIELD_SEP = ": ".getBytes(Charset.defaultCharset());
    private static final byte[] TWO_DASHES = "--".getBytes(Charset.defaultCharset());
    private final byte[] boundary;
    private final String contentType;
    private final List<FormPart> parts;

    interface ContentBody {
        String getCharset();

        String getFilename();

        String getMimeType();

        String getTransferEncoding();

        void writeTo(OutputStream outputStream);
    }

    class FileBody implements ContentBody {
        private final String charset;
        private final File file;
        private final String filename;
        private final String mimeType;

        public FileBody(File file, String str, String str2, String str3) {
            if (str2 == null) {
                str2 = "application/octet-stream";
            }
            this.mimeType = str2;
            if (file == null) {
                throw new IllegalArgumentException("File may not be null");
            }
            this.file = file;
            if (str == null) {
                str = file.getName();
            }
            this.filename = str;
            this.charset = str3;
        }

        public FileBody(File file, String str, String str2) {
            this(file, null, str, str2);
        }

        public FileBody(File file, String str) {
            this(file, str, null);
        }

        public FileBody(File file) {
            this(file, null);
        }

        public void writeTo(OutputStream outputStream) {
            if (outputStream == null) {
                throw new IllegalArgumentException("Output stream may not be null");
            }
            InputStream fileInputStream = new FileInputStream(this.file);
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, read);
                }
                outputStream.flush();
            } finally {
                fileInputStream.close();
            }
        }

        public String getTransferEncoding() {
            return "binary";
        }

        public String getCharset() {
            return this.charset;
        }

        public String getFilename() {
            return this.filename;
        }

        public String getMimeType() {
            return this.mimeType;
        }
    }

    class StringBody implements ContentBody {
        private final Charset charset;
        private final byte[] content;
        private final String mimeType;

        public StringBody(String str, String str2, Charset charset) {
            if (str == null) {
                throw new IllegalArgumentException("Text may not be null");
            }
            if (str2 == null) {
                str2 = "text/plain";
            }
            this.mimeType = str2;
            if (charset == null) {
                charset = Charset.defaultCharset();
            }
            this.charset = charset;
            this.content = str.getBytes(this.charset);
        }

        public StringBody(String str, Charset charset) {
            this(str, null, charset);
        }

        public StringBody(String str) {
            this(str, null);
        }

        public void writeTo(OutputStream outputStream) {
            if (outputStream == null) {
                throw new IllegalArgumentException("Output stream may not be null");
            }
            InputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = byteArrayInputStream.read(bArr);
                if (read != -1) {
                    outputStream.write(bArr, 0, read);
                } else {
                    outputStream.flush();
                    return;
                }
            }
        }

        public String getTransferEncoding() {
            return "8bit";
        }

        public String getCharset() {
            return this.charset.name();
        }

        public String getFilename() {
            return null;
        }

        public String getMimeType() {
            return this.mimeType;
        }
    }

    public MultipartEntity(String str, Charset charset, List<FormPart> list) {
        if (str == null) {
            str = "-_-_-_-" + System.currentTimeMillis() + "-_-_-_-";
        }
        this.boundary = str.getBytes(Charset.defaultCharset());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("multipart/form-data; boundary=");
        stringBuilder.append(str);
        if (charset != null) {
            stringBuilder.append(HTTP.CHARSET_PARAM);
            stringBuilder.append(charset.name());
        }
        this.contentType = stringBuilder.toString();
        this.parts = list;
    }

    public MultipartEntity(List<FormPart> list) {
        this(null, null, list);
    }

    public String getContentType() {
        return this.contentType;
    }

    public void writeTo(OutputStream outputStream) {
        for (FormPart formPart : this.parts) {
            outputStream.write(TWO_DASHES, 0, TWO_DASHES.length);
            outputStream.write(this.boundary, 0, this.boundary.length);
            outputStream.write(CR_LF, 0, CR_LF.length);
            Iterator it = formPart.header.iterator();
            while (it.hasNext()) {
                SimpleEntry simpleEntry = (SimpleEntry) it.next();
                byte[] bytes = ((String) simpleEntry.getKey()).getBytes(Charset.defaultCharset());
                outputStream.write(bytes, 0, bytes.length);
                outputStream.write(FIELD_SEP, 0, FIELD_SEP.length);
                byte[] bytes2 = ((String) simpleEntry.getValue()).getBytes(Charset.defaultCharset());
                outputStream.write(bytes2, 0, bytes2.length);
                outputStream.write(CR_LF, 0, CR_LF.length);
            }
            outputStream.write(CR_LF, 0, CR_LF.length);
            formPart.body.writeTo(outputStream);
            outputStream.write(CR_LF, 0, CR_LF.length);
        }
        outputStream.write(TWO_DASHES, 0, TWO_DASHES.length);
        outputStream.write(this.boundary, 0, this.boundary.length);
        outputStream.write(TWO_DASHES, 0, TWO_DASHES.length);
        outputStream.write(CR_LF, 0, CR_LF.length);
    }
}
