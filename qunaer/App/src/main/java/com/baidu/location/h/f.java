package com.baidu.location.h;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;
import android.util.Log;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.network.NetRequestManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

public abstract class f {
    private static String a = "10.0.0.172";
    private static int b = 80;
    public static int g = b.g;
    protected static int o = 0;
    public String h = null;
    public int i = 3;
    public String j = null;
    public Map<String, Object> k = null;
    public String l = null;
    public byte[] m = null;
    public String n = null;

    private static int a(Context context, NetworkInfo networkInfo) {
        String toLowerCase;
        if (!(networkInfo == null || networkInfo.getExtraInfo() == null)) {
            toLowerCase = networkInfo.getExtraInfo().toLowerCase();
            if (toLowerCase != null) {
                if (toLowerCase.startsWith("cmwap") || toLowerCase.startsWith("uniwap") || toLowerCase.startsWith("3gwap")) {
                    toLowerCase = Proxy.getDefaultHost();
                    if (toLowerCase == null || toLowerCase.equals("") || toLowerCase.equals("null")) {
                        toLowerCase = "10.0.0.172";
                    }
                    a = toLowerCase;
                    return b.d;
                } else if (toLowerCase.startsWith(NetRequestManager.CTWAP_APN_NAME_2)) {
                    toLowerCase = Proxy.getDefaultHost();
                    if (toLowerCase == null || toLowerCase.equals("") || toLowerCase.equals("null")) {
                        toLowerCase = "10.0.0.200";
                    }
                    a = toLowerCase;
                    return b.d;
                } else if (toLowerCase.startsWith("cmnet") || toLowerCase.startsWith("uninet") || toLowerCase.startsWith("ctnet") || toLowerCase.startsWith("3gnet")) {
                    return b.e;
                }
            }
        }
        toLowerCase = Proxy.getDefaultHost();
        if (toLowerCase != null && toLowerCase.length() > 0) {
            if ("10.0.0.172".equals(toLowerCase.trim())) {
                a = "10.0.0.172";
                return b.d;
            } else if ("10.0.0.200".equals(toLowerCase.trim())) {
                a = "10.0.0.200";
                return b.d;
            }
        }
        return b.e;
    }

    private void b() {
        g = c();
    }

    private int c() {
        Context serviceContext = com.baidu.location.f.getServiceContext();
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) serviceContext.getSystemService("connectivity");
            if (connectivityManager == null) {
                return b.g;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return b.g;
            }
            if (activeNetworkInfo.getType() != 1) {
                return a(serviceContext, activeNetworkInfo);
            }
            String defaultHost = Proxy.getDefaultHost();
            return (defaultHost == null || defaultHost.length() <= 0) ? b.f : b.h;
        } catch (Exception e) {
            return b.g;
        }
    }

    public abstract void a();

    public abstract void a(boolean z);

    public void b(final boolean z) {
        new Thread(this) {
            final /* synthetic */ f b;

            public void run() {
                java.net.Proxy proxy;
                HttpURLConnection httpURLConnection;
                int i;
                Throwable th;
                this.b.h = i.c();
                this.b.b();
                this.b.a();
                int i2 = this.b.i;
                if (TextUtils.isEmpty(i.h)) {
                    proxy = null;
                    httpURLConnection = null;
                    i = i2;
                } else {
                    proxy = new java.net.Proxy(Type.HTTP, new InetSocketAddress(i.h, i.i));
                    httpURLConnection = null;
                    i = i2;
                }
                loop0:
                while (i > 0) {
                    HttpURLConnection httpURLConnection2;
                    try {
                        URL url = new URL(this.b.h);
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Entry entry : this.b.k.entrySet()) {
                            stringBuffer.append((String) entry.getKey());
                            stringBuffer.append("=");
                            stringBuffer.append(entry.getValue());
                            stringBuffer.append("&");
                        }
                        if (stringBuffer.length() > 0) {
                            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                        }
                        httpURLConnection2 = proxy != null ? (HttpURLConnection) HttpInstrumentation.openConnectionWithProxy(url.openConnection(proxy)) : (HttpURLConnection) HttpInstrumentation.openConnection(url.openConnection());
                        try {
                            httpURLConnection2.setRequestMethod(HttpPost.METHOD_NAME);
                            httpURLConnection2.setDoInput(true);
                            httpURLConnection2.setDoOutput(true);
                            httpURLConnection2.setUseCaches(false);
                            httpURLConnection2.setConnectTimeout(b.b);
                            httpURLConnection2.setReadTimeout(b.b);
                            httpURLConnection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                            httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                            httpURLConnection2.setRequestProperty("Accept-Encoding", "gzip");
                            httpURLConnection2.setRequestProperty("Host", "loc.map.baidu.com");
                            OutputStream outputStream = httpURLConnection2.getOutputStream();
                            outputStream.write(stringBuffer.toString().getBytes());
                            outputStream.flush();
                            outputStream.close();
                            if (httpURLConnection2.getResponseCode() == 200) {
                                InputStream inputStream = httpURLConnection2.getInputStream();
                                String contentEncoding = httpURLConnection2.getContentEncoding();
                                InputStream gZIPInputStream = (contentEncoding == null || !contentEncoding.contains("gzip")) ? inputStream : new GZIPInputStream(new BufferedInputStream(inputStream));
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int read = gZIPInputStream.read(bArr);
                                    if (read == -1) {
                                        break loop0;
                                    }
                                    byteArrayOutputStream.write(bArr, 0, read);
                                }
                                gZIPInputStream.close();
                                byteArrayOutputStream.close();
                                this.b.j = new String(byteArrayOutputStream.toByteArray(), "utf-8");
                                if (z) {
                                    this.b.m = byteArrayOutputStream.toByteArray();
                                }
                                this.b.a(true);
                                httpURLConnection2.disconnect();
                                if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                if (i > 0) {
                                    f.o++;
                                    this.b.j = null;
                                    this.b.a(false);
                                    return;
                                }
                                f.o = 0;
                                return;
                            }
                            httpURLConnection2.disconnect();
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            httpURLConnection = httpURLConnection2;
                            i--;
                        } catch (Exception e) {
                        } catch (Error e2) {
                            try {
                                Log.d(b.a, "NetworkCommunicationError!");
                                if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                httpURLConnection = httpURLConnection2;
                                i--;
                            } catch (Throwable th2) {
                                httpURLConnection = httpURLConnection2;
                                th = th2;
                            }
                        }
                    } catch (Exception e3) {
                        httpURLConnection2 = httpURLConnection;
                        Log.d(b.a, "NetworkCommunicationException!");
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        httpURLConnection = httpURLConnection2;
                        i--;
                    } catch (Error e4) {
                        httpURLConnection2 = httpURLConnection;
                        Log.d(b.a, "NetworkCommunicationError!");
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        httpURLConnection = httpURLConnection2;
                        i--;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
                if (i > 0) {
                    f.o = 0;
                    return;
                }
                f.o++;
                this.b.j = null;
                this.b.a(false);
                return;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw th;
            }
        }.start();
    }

    public void d() {
        new Thread(this) {
            final /* synthetic */ f a;

            {
                this.a = r1;
            }

            public void run() {
                HttpURLConnection httpURLConnection;
                Throwable th;
                this.a.h = i.c();
                this.a.b();
                this.a.a();
                HttpURLConnection httpURLConnection2 = null;
                int i = this.a.i;
                while (i > 0) {
                    try {
                        httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(new URL(this.a.h).openConnection());
                        try {
                            httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setUseCaches(false);
                            httpURLConnection.setConnectTimeout(b.b);
                            httpURLConnection.setReadTimeout(b.b);
                            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                            if (httpURLConnection.getResponseCode() == 200) {
                                InputStream inputStream = httpURLConnection.getInputStream();
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int read = inputStream.read(bArr);
                                    if (read == -1) {
                                        break;
                                    }
                                    byteArrayOutputStream.write(bArr, 0, read);
                                }
                                inputStream.close();
                                byteArrayOutputStream.close();
                                this.a.j = new String(byteArrayOutputStream.toByteArray(), "utf-8");
                                this.a.a(true);
                                httpURLConnection.disconnect();
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                if (i > 0) {
                                    f.o++;
                                    this.a.j = null;
                                    this.a.a(false);
                                    return;
                                }
                                f.o = 0;
                                return;
                            }
                            httpURLConnection.disconnect();
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            i--;
                            httpURLConnection2 = httpURLConnection;
                        } catch (Exception e) {
                        }
                    } catch (Exception e2) {
                        httpURLConnection = httpURLConnection2;
                        try {
                            Log.d(b.a, "NetworkCommunicationException!");
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            i--;
                            httpURLConnection2 = httpURLConnection;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            httpURLConnection2 = httpURLConnection;
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
                if (i > 0) {
                    f.o = 0;
                    return;
                }
                f.o++;
                this.a.j = null;
                this.a.a(false);
                return;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
        }.start();
    }

    public void e() {
        b(false);
    }
}
