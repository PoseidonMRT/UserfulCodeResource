package okhttp3;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import okhttp3.Cookie.Builder;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import org.apache.http.cookie.SM;

public final class JavaNetCookieJar implements CookieJar {
    private final CookieHandler cookieHandler;

    public JavaNetCookieJar(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        if (this.cookieHandler != null) {
            List arrayList = new ArrayList();
            for (Cookie cookie : list) {
                arrayList.add(cookie.toString(true));
            }
            try {
                this.cookieHandler.put(httpUrl.uri(), Collections.singletonMap(SM.SET_COOKIE, arrayList));
            } catch (Throwable e) {
                Platform.get().log(5, "Saving cookies failed for " + httpUrl.resolve("/..."), e);
            }
        }
    }

    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        try {
            List list = null;
            for (Entry entry : this.cookieHandler.get(httpUrl.uri(), Collections.emptyMap()).entrySet()) {
                String str = (String) entry.getKey();
                if ((SM.COOKIE.equalsIgnoreCase(str) || SM.COOKIE2.equalsIgnoreCase(str)) && !((List) entry.getValue()).isEmpty()) {
                    List list2 = list;
                    for (String str2 : (List) entry.getValue()) {
                        if (list2 == null) {
                            list2 = new ArrayList();
                        }
                        list2.addAll(decodeHeaderAsJavaNetCookies(httpUrl, str2));
                    }
                    list = list2;
                }
            }
            if (list != null) {
                return Collections.unmodifiableList(list);
            }
            return Collections.emptyList();
        } catch (Throwable e) {
            Platform.get().log(5, "Loading cookies failed for " + httpUrl.resolve("/..."), e);
            return Collections.emptyList();
        }
    }

    private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl httpUrl, String str) {
        List<Cookie> arrayList = new ArrayList();
        int i = 0;
        int length = str.length();
        while (i < length) {
            int delimiterOffset = Util.delimiterOffset(str, i, length, ";,");
            int delimiterOffset2 = Util.delimiterOffset(str, i, delimiterOffset, '=');
            String trimSubstring = Util.trimSubstring(str, i, delimiterOffset2);
            if (!trimSubstring.startsWith("$")) {
                String trimSubstring2 = delimiterOffset2 < delimiterOffset ? Util.trimSubstring(str, delimiterOffset2 + 1, delimiterOffset) : "";
                if (trimSubstring2.startsWith("\"") && trimSubstring2.endsWith("\"")) {
                    trimSubstring2 = trimSubstring2.substring(1, trimSubstring2.length() - 1);
                }
                arrayList.add(new Builder().name(trimSubstring).value(trimSubstring2).domain(httpUrl.host()).build());
            }
            i = delimiterOffset + 1;
        }
        return arrayList;
    }
}
