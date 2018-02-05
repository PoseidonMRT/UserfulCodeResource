package org.acra.sender;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.a;
import org.acra.collections.d;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.util.f;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URLEncodedUtils;

public class HttpSender implements ReportSender {
    private final ACRAConfiguration a;
    @Nullable
    private final Uri b;
    private final Map<ReportField, String> c;
    private final Method d;
    private final Type e;
    @Nullable
    private String f;
    @Nullable
    private String g;

    public enum Method {
        POST,
        PUT
    }

    public enum Type {
        FORM {
            @NonNull
            public String getContentType() {
                return URLEncodedUtils.CONTENT_TYPE;
            }
        },
        JSON {
            @NonNull
            public String getContentType() {
                return "application/json";
            }
        };

        @NonNull
        public abstract String getContentType();
    }

    public HttpSender(@NonNull ACRAConfiguration aCRAConfiguration, @NonNull Method method, @NonNull Type type, @Nullable Map<ReportField, String> map) {
        this(aCRAConfiguration, method, type, null, map);
    }

    public HttpSender(@NonNull ACRAConfiguration aCRAConfiguration, @NonNull Method method, @NonNull Type type, @Nullable String str, @Nullable Map<ReportField, String> map) {
        this.a = aCRAConfiguration;
        this.d = method;
        this.b = str == null ? null : Uri.parse(str);
        this.c = map;
        this.e = type;
        this.f = null;
        this.g = null;
    }

    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        try {
            URL url;
            String jSONObject;
            if (this.b == null) {
                url = new URL(aCRAConfiguration.formUri());
            } else {
                url = new URL(this.b.toString());
            }
            ACRA.f.b(ACRA.e, "Connect to " + url.toString());
            String formUriBasicAuthLogin = this.f != null ? this.f : a(aCRAConfiguration.formUriBasicAuthLogin()) ? null : aCRAConfiguration.formUriBasicAuthLogin();
            String formUriBasicAuthPassword = this.g != null ? this.g : a(aCRAConfiguration.formUriBasicAuthPassword()) ? null : aCRAConfiguration.formUriBasicAuthPassword();
            f fVar = new f(aCRAConfiguration);
            fVar.a(aCRAConfiguration.connectionTimeout());
            fVar.b(aCRAConfiguration.socketTimeout());
            fVar.a(formUriBasicAuthLogin);
            fVar.b(formUriBasicAuthPassword);
            fVar.a(aCRAConfiguration.getHttpHeaders());
            switch (this.e) {
                case JSON:
                    jSONObject = crashReportData.toJSON().toString();
                    break;
                default:
                    jSONObject = f.b(a((Map) crashReportData));
                    break;
            }
            switch (this.d) {
                case POST:
                    break;
                case PUT:
                    url = new URL(url.toString() + IOUtils.DIR_SEPARATOR_UNIX + crashReportData.getProperty(ReportField.REPORT_ID));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown method: " + this.d.name());
            }
            fVar.a(context, url, this.d, jSONObject, this.e);
        } catch (Throwable e) {
            throw new ReportSenderException("Error while sending " + aCRAConfiguration.reportType() + " report via Http " + this.d.name(), e);
        } catch (Throwable e2) {
            throw new ReportSenderException("Error while sending " + aCRAConfiguration.reportType() + " report via Http " + this.d.name(), e2);
        }
    }

    @NonNull
    private Map<String, String> a(@NonNull Map<ReportField, String> map) {
        Set reportFields = this.a.getReportFields();
        if (reportFields.isEmpty()) {
            reportFields = new d(a.c);
        }
        Map<String, String> hashMap = new HashMap(map.size());
        for (ReportField reportField : r0) {
            if (this.c == null || this.c.get(reportField) == null) {
                hashMap.put(reportField.toString(), map.get(reportField));
            } else {
                hashMap.put(this.c.get(reportField), map.get(reportField));
            }
        }
        return hashMap;
    }

    private boolean a(@Nullable String str) {
        return str == null || "ACRA-NULL-STRING".equals(str);
    }
}
