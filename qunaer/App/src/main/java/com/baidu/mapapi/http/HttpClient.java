package com.baidu.mapapi.http;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.baidu.mapapi.JNIInitializer;
import com.baidu.mapapi.common.Logger;
import com.baidu.platform.comapi.util.e;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpStatus;

@Instrumented
public class HttpClient {
    HttpURLConnection a;
    private String b = null;
    private String c = null;
    private int d;
    private int e;
    private String f;
    private ProtoResultCallback g;

    public enum HttpStateError {
        NO_ERROR,
        NETWORK_ERROR,
        INNER_ERROR,
        REQUEST_ERROR,
        SERVER_ERROR
    }

    public abstract class ProtoResultCallback {
        public abstract void onFailed(HttpStateError httpStateError);

        public abstract void onSuccess(String str);
    }

    public HttpClient(String str, ProtoResultCallback protoResultCallback) {
        this.f = str;
        this.g = protoResultCallback;
    }

    private HttpURLConnection a() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(new URL(this.b).openConnection());
            httpURLConnection.setRequestMethod(this.f);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(this.d);
            httpURLConnection.setReadTimeout(this.e);
            return httpURLConnection;
        } catch (Exception e) {
            if (Logger.debugEnable()) {
                e.printStackTrace();
            } else {
                Logger.logW("HttpClient", e.getMessage());
            }
            return null;
        }
    }

    public static String getAuthToken() {
        return e.z;
    }

    public static String getPhoneInfo() {
        return e.c();
    }

    protected boolean checkNetwork() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) JNIInitializer.getCachedContext().getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
        } catch (Exception e) {
            if (Logger.debugEnable()) {
                e.printStackTrace();
            } else {
                Logger.logW("HttpClient", e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    protected void request(String str) {
        InputStream inputStream;
        Exception e;
        Throwable th;
        BufferedReader bufferedReader = null;
        this.b = str;
        if (checkNetwork()) {
            this.a = a();
            if (this.a == null) {
                this.g.onFailed(HttpStateError.INNER_ERROR);
                return;
            } else if (TextUtils.isEmpty(this.b)) {
                this.g.onFailed(HttpStateError.REQUEST_ERROR);
                return;
            } else {
                BufferedReader bufferedReader2 = null;
                try {
                    this.a.connect();
                    try {
                        int responseCode = this.a.getResponseCode();
                        if (200 == responseCode) {
                            inputStream = this.a.getInputStream();
                            try {
                                bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                                try {
                                    StringBuffer stringBuffer = new StringBuffer();
                                    while (true) {
                                        int read = bufferedReader2.read();
                                        if (read == -1) {
                                            break;
                                        }
                                        stringBuffer.append((char) read);
                                    }
                                    this.c = stringBuffer.toString();
                                    if (!(inputStream == null || bufferedReader2 == null)) {
                                        bufferedReader2.close();
                                        inputStream.close();
                                    }
                                    if (this.a != null) {
                                        this.a.disconnect();
                                    }
                                    this.g.onSuccess(this.c);
                                    return;
                                } catch (Exception e2) {
                                    e = e2;
                                    bufferedReader = bufferedReader2;
                                    try {
                                        if (Logger.debugEnable()) {
                                            e.printStackTrace();
                                        } else {
                                            Logger.logW("HttpClient", e.getMessage());
                                        }
                                        this.g.onFailed(HttpStateError.INNER_ERROR);
                                        bufferedReader.close();
                                        inputStream.close();
                                        if (this.a == null) {
                                            this.a.disconnect();
                                            return;
                                        }
                                        return;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        if (!(inputStream == null || bufferedReader == null)) {
                                            bufferedReader.close();
                                            inputStream.close();
                                        }
                                        if (this.a != null) {
                                            this.a.disconnect();
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    bufferedReader = bufferedReader2;
                                    bufferedReader.close();
                                    inputStream.close();
                                    if (this.a != null) {
                                        this.a.disconnect();
                                    }
                                    throw th;
                                }
                            } catch (Exception e3) {
                                e = e3;
                                if (Logger.debugEnable()) {
                                    Logger.logW("HttpClient", e.getMessage());
                                } else {
                                    e.printStackTrace();
                                }
                                this.g.onFailed(HttpStateError.INNER_ERROR);
                                bufferedReader.close();
                                inputStream.close();
                                if (this.a == null) {
                                    this.a.disconnect();
                                    return;
                                }
                                return;
                            }
                        }
                        HttpStateError httpStateError = HttpStateError.NO_ERROR;
                        httpStateError = responseCode >= 500 ? HttpStateError.SERVER_ERROR : responseCode >= HttpStatus.SC_BAD_REQUEST ? HttpStateError.REQUEST_ERROR : HttpStateError.INNER_ERROR;
                        if (Logger.debugEnable()) {
                            inputStream = this.a.getErrorStream();
                            Logger.logW("HttpClient", inputStream.toString());
                        } else {
                            Logger.logW("HttpClient", "Get response from server failed, http response code=" + responseCode + ", error=" + httpStateError);
                            inputStream = null;
                        }
                        this.g.onFailed(httpStateError);
                        if (!(inputStream == null || null == null)) {
                            bufferedReader2.close();
                            inputStream.close();
                        }
                        if (this.a != null) {
                            this.a.disconnect();
                            return;
                        }
                        return;
                    } catch (Exception e4) {
                        e = e4;
                        inputStream = null;
                        if (Logger.debugEnable()) {
                            e.printStackTrace();
                        } else {
                            Logger.logW("HttpClient", e.getMessage());
                        }
                        this.g.onFailed(HttpStateError.INNER_ERROR);
                        if (!(inputStream == null || bufferedReader == null)) {
                            bufferedReader.close();
                            inputStream.close();
                        }
                        if (this.a == null) {
                            this.a.disconnect();
                            return;
                        }
                        return;
                    } catch (Throwable th4) {
                        th = th4;
                        inputStream = null;
                        bufferedReader.close();
                        inputStream.close();
                        if (this.a != null) {
                            this.a.disconnect();
                        }
                        throw th;
                    }
                } catch (Exception e5) {
                    if (Logger.debugEnable()) {
                        e5.printStackTrace();
                    } else {
                        Logger.logW("HttpClient", e5.getMessage());
                    }
                    this.g.onFailed(HttpStateError.INNER_ERROR);
                    return;
                }
            }
        }
        this.g.onFailed(HttpStateError.NETWORK_ERROR);
    }

    public void setMaxTimeOut(int i) {
        this.d = i;
    }

    public void setReadTimeOut(int i) {
        this.e = i;
    }
}
