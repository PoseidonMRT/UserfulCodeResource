package bolts;

import android.content.Context;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import bolts.AppLink.Target;
import bolts.Task.TaskCompletionSource;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebViewAppLinkResolver implements AppLinkResolver {
    private static final String KEY_AL_VALUE = "value";
    private static final String KEY_ANDROID = "android";
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_CLASS = "class";
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_SHOULD_FALLBACK = "should_fallback";
    private static final String KEY_URL = "url";
    private static final String KEY_WEB = "web";
    private static final String KEY_WEB_URL = "url";
    private static final String META_TAG_PREFIX = "al";
    private static final String PREFER_HEADER = "Prefer-Html-Meta-Tags";
    private static final String TAG_EXTRACTION_JAVASCRIPT = "javascript:boltsWebViewAppLinkResolverResult.setValue((function() {  var metaTags = document.getElementsByTagName('meta');  var results = [];  for (var i = 0; i < metaTags.length; i++) {    var property = metaTags[i].getAttribute('property');    if (property && property.substring(0, 'al:'.length) === 'al:') {      var tag = { \"property\": metaTags[i].getAttribute('property') };      if (metaTags[i].hasAttribute('content')) {        tag['content'] = metaTags[i].getAttribute('content');      }      results.push(tag);    }  }  return JSON.stringify(results);})())";
    private final Context context;

    public WebViewAppLinkResolver(Context context) {
        this.context = context;
    }

    public Task<AppLink> getAppLinkFromUrlInBackground(final Uri uri) {
        final Capture capture = new Capture();
        final Capture capture2 = new Capture();
        return Task.callInBackground(new Callable<Void>() {
            public Void call() {
                URL url = new URL(uri.toString());
                URLConnection uRLConnection = null;
                while (url != null) {
                    URLConnection openConnection = HttpInstrumentation.openConnection(url.openConnection());
                    if (openConnection instanceof HttpURLConnection) {
                        ((HttpURLConnection) openConnection).setInstanceFollowRedirects(true);
                    }
                    openConnection.setRequestProperty(WebViewAppLinkResolver.PREFER_HEADER, WebViewAppLinkResolver.META_TAG_PREFIX);
                    openConnection.connect();
                    if (openConnection instanceof HttpURLConnection) {
                        URL url2;
                        HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
                        if (httpURLConnection.getResponseCode() < 300 || httpURLConnection.getResponseCode() >= HttpStatus.SC_BAD_REQUEST) {
                            Object obj = null;
                        } else {
                            URL url3 = new URL(httpURLConnection.getHeaderField("Location"));
                            httpURLConnection.disconnect();
                            url2 = url3;
                        }
                        URLConnection uRLConnection2 = openConnection;
                        url = url2;
                        uRLConnection = uRLConnection2;
                    } else {
                        uRLConnection = openConnection;
                        url = null;
                    }
                }
                try {
                    capture.set(WebViewAppLinkResolver.readFromConnection(uRLConnection));
                    capture2.set(uRLConnection.getContentType());
                    return null;
                } finally {
                    if (uRLConnection instanceof HttpURLConnection) {
                        ((HttpURLConnection) uRLConnection).disconnect();
                    }
                }
            }
        }).onSuccessTask(new Continuation<Void, Task<JSONArray>>() {
            public Task<JSONArray> then(Task<Void> task) {
                String str;
                final TaskCompletionSource create = Task.create();
                WebView webView = new WebView(WebViewAppLinkResolver.this.context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setNetworkAvailable(false);
                webView.setWebViewClient(new WebViewClient() {
                    private boolean loaded = false;

                    private void runJavaScript(WebView webView) {
                        if (!this.loaded) {
                            this.loaded = true;
                            webView.loadUrl(WebViewAppLinkResolver.TAG_EXTRACTION_JAVASCRIPT);
                        }
                    }

                    public void onPageFinished(WebView webView, String str) {
                        super.onPageFinished(webView, str);
                        runJavaScript(webView);
                    }

                    public void onLoadResource(WebView webView, String str) {
                        super.onLoadResource(webView, str);
                        runJavaScript(webView);
                    }
                });
                webView.addJavascriptInterface(new Object() {
                    @JavascriptInterface
                    public void setValue(String str) {
                        try {
                            create.trySetResult(new JSONArray(str));
                        } catch (Exception e) {
                            create.trySetError(e);
                        }
                    }
                }, "boltsWebViewAppLinkResolverResult");
                if (capture2.get() != null) {
                    str = ((String) capture2.get()).split(VoiceWakeuperAidl.PARAMS_SEPARATE)[0];
                } else {
                    str = null;
                }
                webView.loadDataWithBaseURL(uri.toString(), (String) capture.get(), str, null, null);
                return create.getTask();
            }
        }, Task.UI_THREAD_EXECUTOR).onSuccess(new Continuation<JSONArray, AppLink>() {
            public AppLink then(Task<JSONArray> task) {
                return WebViewAppLinkResolver.makeAppLinkFromAlData(WebViewAppLinkResolver.parseAlData((JSONArray) task.getResult()), uri);
            }
        });
    }

    private static Map<String, Object> parseAlData(JSONArray jSONArray) {
        Map hashMap = new HashMap();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String[] split = jSONObject.getString("property").split(":");
            if (split[0].equals(META_TAG_PREFIX)) {
                int i2 = 1;
                Map map = hashMap;
                while (i2 < split.length) {
                    List list;
                    Map map2;
                    List list2 = (List) map.get(split[i2]);
                    if (list2 == null) {
                        ArrayList arrayList = new ArrayList();
                        map.put(split[i2], arrayList);
                        list = arrayList;
                    } else {
                        list = list2;
                    }
                    if (list.size() > 0) {
                        map2 = (Map) list.get(list.size() - 1);
                    } else {
                        map2 = null;
                    }
                    if (map2 == null || i2 == split.length - 1) {
                        HashMap hashMap2 = new HashMap();
                        list.add(hashMap2);
                        map = hashMap2;
                    } else {
                        map = map2;
                    }
                    i2++;
                }
                if (jSONObject.has(AIUIConstant.KEY_CONTENT)) {
                    if (jSONObject.isNull(AIUIConstant.KEY_CONTENT)) {
                        map.put(KEY_AL_VALUE, null);
                    } else {
                        map.put(KEY_AL_VALUE, jSONObject.getString(AIUIConstant.KEY_CONTENT));
                    }
                }
            }
        }
        return hashMap;
    }

    private static List<Map<String, Object>> getAlList(Map<String, Object> map, String str) {
        List<Map<String, Object>> list = (List) map.get(str);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    private static AppLink makeAppLinkFromAlData(Map<String, Object> map, Uri uri) {
        Uri uri2;
        List arrayList = new ArrayList();
        List list = (List) map.get(KEY_ANDROID);
        if (list == null) {
            list = Collections.emptyList();
        }
        for (Map map2 : r0) {
            Map map22;
            List alList = getAlList(map22, "url");
            List alList2 = getAlList(map22, KEY_PACKAGE);
            List alList3 = getAlList(map22, KEY_CLASS);
            List alList4 = getAlList(map22, KEY_APP_NAME);
            int max = Math.max(alList.size(), Math.max(alList2.size(), Math.max(alList3.size(), alList4.size())));
            int i = 0;
            while (i < max) {
                arrayList.add(new Target((String) (alList2.size() > i ? ((Map) alList2.get(i)).get(KEY_AL_VALUE) : null), (String) (alList3.size() > i ? ((Map) alList3.get(i)).get(KEY_AL_VALUE) : null), tryCreateUrl((String) (alList.size() > i ? ((Map) alList.get(i)).get(KEY_AL_VALUE) : null)), (String) (alList4.size() > i ? ((Map) alList4.get(i)).get(KEY_AL_VALUE) : null)));
                i++;
            }
        }
        list = (List) map.get(KEY_WEB);
        if (list == null || list.size() <= 0) {
            uri2 = uri;
        } else {
            map22 = (Map) list.get(0);
            List list2 = (List) map22.get("url");
            list = (List) map22.get(KEY_SHOULD_FALLBACK);
            if (list != null && list.size() > 0) {
                if (Arrays.asList(new String[]{"no", "false", "0"}).contains(((String) ((Map) list.get(0)).get(KEY_AL_VALUE)).toLowerCase())) {
                    uri2 = null;
                    if (!(uri2 == null || list2 == null || list2.size() <= 0)) {
                        uri2 = tryCreateUrl((String) ((Map) list2.get(0)).get(KEY_AL_VALUE));
                    }
                }
            }
            uri2 = uri;
            uri2 = tryCreateUrl((String) ((Map) list2.get(0)).get(KEY_AL_VALUE));
        }
        return new AppLink(uri, arrayList, uri2);
    }

    private static Uri tryCreateUrl(String str) {
        if (str == null) {
            return null;
        }
        return Uri.parse(str);
    }

    private static String readFromConnection(URLConnection uRLConnection) {
        InputStream inputStream;
        int i = 0;
        if (uRLConnection instanceof HttpURLConnection) {
            InputStream inputStream2;
            HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnection;
            try {
                inputStream2 = uRLConnection.getInputStream();
            } catch (Exception e) {
                inputStream2 = httpURLConnection.getErrorStream();
            }
            inputStream = inputStream2;
        } else {
            inputStream = uRLConnection.getInputStream();
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            String contentEncoding = uRLConnection.getContentEncoding();
            if (contentEncoding == null) {
                String[] split = uRLConnection.getContentType().split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                int length = split.length;
                while (i < length) {
                    String trim = split[i].trim();
                    if (trim.startsWith("charset=")) {
                        contentEncoding = trim.substring("charset=".length());
                        break;
                    }
                    i++;
                }
                if (contentEncoding == null) {
                    contentEncoding = "UTF-8";
                }
            }
            String str = new String(byteArrayOutputStream.toByteArray(), contentEncoding);
            return str;
        } finally {
            inputStream.close();
        }
    }
}
