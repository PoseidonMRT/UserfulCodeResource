package com.baidu.lbsapi.auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import com.mqunar.network.NetRequestManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.methods.HttpPost;

@Instrumented
public class g {
    private Context a;
    private String b = null;
    private HashMap<String, String> c = null;
    private String d = null;

    public g(Context context) {
        this.a = context;
    }

    private String a(Context context) {
        String str = "wifi";
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return null;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return null;
            }
            String extraInfo = activeNetworkInfo.getExtraInfo();
            return (extraInfo == null || !(extraInfo.trim().toLowerCase().equals("cmwap") || extraInfo.trim().toLowerCase().equals("uniwap") || extraInfo.trim().toLowerCase().equals("3gwap") || extraInfo.trim().toLowerCase().equals(NetRequestManager.CTWAP_APN_NAME_2))) ? str : extraInfo.trim().toLowerCase().equals(NetRequestManager.CTWAP_APN_NAME_2) ? NetRequestManager.CTWAP_APN_NAME_2 : "cmwap";
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void a(HttpsURLConnection httpsURLConnection) {
        OutputStream outputStream;
        InputStream inputStream;
        IOException e;
        BufferedReader bufferedReader;
        MalformedURLException e2;
        Throwable th;
        Exception e3;
        OutputStream outputStream2 = null;
        a.a("https Post start,url:" + this.b);
        if (this.c == null) {
            this.d = ErrorMessage.a("httpsPost request paramters is null.");
            return;
        }
        int i;
        Object obj = 1;
        try {
            outputStream = httpsURLConnection.getOutputStream();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(b(this.c));
                a.a(b(this.c));
                bufferedWriter.flush();
                bufferedWriter.close();
                httpsURLConnection.connect();
                try {
                    inputStream = httpsURLConnection.getInputStream();
                    try {
                        BufferedReader bufferedReader2;
                        int responseCode = httpsURLConnection.getResponseCode();
                        if (200 == responseCode) {
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
                                    this.d = stringBuffer.toString();
                                } catch (IOException e4) {
                                    e = e4;
                                    bufferedReader = bufferedReader2;
                                    i = responseCode;
                                    try {
                                        if (a.a) {
                                            e.printStackTrace();
                                            a.a("httpsPost parse failed;" + e.getMessage());
                                        }
                                        this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e.getMessage());
                                        try {
                                            bufferedReader.close();
                                            inputStream.close();
                                            if (httpsURLConnection == null) {
                                                obj = null;
                                            } else {
                                                httpsURLConnection.disconnect();
                                                obj = null;
                                            }
                                            if (outputStream != null) {
                                                try {
                                                    outputStream.close();
                                                } catch (IOException e5) {
                                                    if (a.a) {
                                                        e5.printStackTrace();
                                                    }
                                                }
                                            }
                                        } catch (MalformedURLException e6) {
                                            e2 = e6;
                                            outputStream2 = outputStream;
                                            try {
                                                if (a.a) {
                                                    e2.printStackTrace();
                                                }
                                                this.d = ErrorMessage.a(-11, "httpsPost failed,MalformedURLException:" + e2.getMessage());
                                                if (outputStream2 != null) {
                                                    try {
                                                        outputStream2.close();
                                                        obj = null;
                                                    } catch (IOException e7) {
                                                        if (a.a) {
                                                            e7.printStackTrace();
                                                        }
                                                        obj = null;
                                                    }
                                                    if (obj == null) {
                                                    }
                                                    if (this.d == null) {
                                                        a.a("httpsPost success end,parse result = " + this.d);
                                                    }
                                                    a.a("httpsPost failed,mResult is null");
                                                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                                }
                                                obj = null;
                                                if (obj == null) {
                                                }
                                                if (this.d == null) {
                                                    a.a("httpsPost failed,mResult is null");
                                                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                                }
                                                a.a("httpsPost success end,parse result = " + this.d);
                                            } catch (Throwable th2) {
                                                th = th2;
                                                outputStream = outputStream2;
                                                if (outputStream != null) {
                                                    try {
                                                        outputStream.close();
                                                    } catch (IOException e52) {
                                                        if (a.a) {
                                                            e52.printStackTrace();
                                                        }
                                                    }
                                                }
                                                throw th;
                                            }
                                        } catch (IOException e8) {
                                            e7 = e8;
                                            try {
                                                if (a.a) {
                                                    e7.printStackTrace();
                                                }
                                                this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e7.getMessage());
                                                if (outputStream != null) {
                                                    try {
                                                        outputStream.close();
                                                        obj = null;
                                                    } catch (IOException e72) {
                                                        if (a.a) {
                                                            e72.printStackTrace();
                                                        }
                                                        obj = null;
                                                    }
                                                    if (obj == null) {
                                                    }
                                                    if (this.d == null) {
                                                        a.a("httpsPost success end,parse result = " + this.d);
                                                    }
                                                    a.a("httpsPost failed,mResult is null");
                                                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                                }
                                                obj = null;
                                                if (obj == null) {
                                                }
                                                if (this.d == null) {
                                                    a.a("httpsPost failed,mResult is null");
                                                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                                }
                                                a.a("httpsPost success end,parse result = " + this.d);
                                            } catch (Throwable th3) {
                                                th = th3;
                                                if (outputStream != null) {
                                                    outputStream.close();
                                                }
                                                throw th;
                                            }
                                        } catch (Exception e9) {
                                            e3 = e9;
                                            if (a.a) {
                                                e3.printStackTrace();
                                            }
                                            this.d = ErrorMessage.a(-11, "httpsPost failed,Exception:" + e3.getMessage());
                                            if (outputStream != null) {
                                                try {
                                                    outputStream.close();
                                                    obj = null;
                                                } catch (IOException e722) {
                                                    if (a.a) {
                                                        e722.printStackTrace();
                                                    }
                                                    obj = null;
                                                }
                                                if (obj == null) {
                                                }
                                                if (this.d == null) {
                                                    a.a("httpsPost success end,parse result = " + this.d);
                                                }
                                                a.a("httpsPost failed,mResult is null");
                                                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                            }
                                            obj = null;
                                            if (obj == null) {
                                            }
                                            if (this.d == null) {
                                                a.a("httpsPost failed,mResult is null");
                                                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                            }
                                            a.a("httpsPost success end,parse result = " + this.d);
                                        }
                                        if (obj == null) {
                                        }
                                        if (this.d == null) {
                                            a.a("httpsPost failed,mResult is null");
                                            this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                        }
                                        a.a("httpsPost success end,parse result = " + this.d);
                                    } catch (Throwable th4) {
                                        th = th4;
                                        if (!(inputStream == null || bufferedReader == null)) {
                                            bufferedReader.close();
                                            inputStream.close();
                                        }
                                        if (httpsURLConnection != null) {
                                            httpsURLConnection.disconnect();
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                    bufferedReader = bufferedReader2;
                                    i = responseCode;
                                    bufferedReader.close();
                                    inputStream.close();
                                    if (httpsURLConnection != null) {
                                        httpsURLConnection.disconnect();
                                    }
                                    throw th;
                                }
                            } catch (IOException e10) {
                                e722 = e10;
                                i = responseCode;
                                if (a.a) {
                                    e722.printStackTrace();
                                    a.a("httpsPost parse failed;" + e722.getMessage());
                                }
                                this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
                                bufferedReader.close();
                                inputStream.close();
                                if (httpsURLConnection == null) {
                                    obj = null;
                                } else {
                                    httpsURLConnection.disconnect();
                                    obj = null;
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                                if (obj == null) {
                                }
                                if (this.d == null) {
                                    a.a("httpsPost failed,mResult is null");
                                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                }
                                a.a("httpsPost success end,parse result = " + this.d);
                            } catch (Throwable th6) {
                                th = th6;
                                i = responseCode;
                                bufferedReader.close();
                                inputStream.close();
                                if (httpsURLConnection != null) {
                                    httpsURLConnection.disconnect();
                                }
                                throw th;
                            }
                        }
                        bufferedReader2 = null;
                        if (!(inputStream == null || bufferedReader2 == null)) {
                            try {
                                bufferedReader2.close();
                                inputStream.close();
                            } catch (MalformedURLException e11) {
                                e2 = e11;
                                i = responseCode;
                                outputStream2 = outputStream;
                                if (a.a) {
                                    e2.printStackTrace();
                                }
                                this.d = ErrorMessage.a(-11, "httpsPost failed,MalformedURLException:" + e2.getMessage());
                                if (outputStream2 != null) {
                                    outputStream2.close();
                                    obj = null;
                                    if (obj == null) {
                                    }
                                    if (this.d == null) {
                                        a.a("httpsPost failed,mResult is null");
                                        this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                    }
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                obj = null;
                                if (obj == null) {
                                }
                                if (this.d == null) {
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                a.a("httpsPost failed,mResult is null");
                                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                            } catch (IOException e12) {
                                e722 = e12;
                                i = responseCode;
                                if (a.a) {
                                    e722.printStackTrace();
                                }
                                this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
                                if (outputStream != null) {
                                    outputStream.close();
                                    obj = null;
                                    if (obj == null) {
                                    }
                                    if (this.d == null) {
                                        a.a("httpsPost failed,mResult is null");
                                        this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                    }
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                obj = null;
                                if (obj == null) {
                                }
                                if (this.d == null) {
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                a.a("httpsPost failed,mResult is null");
                                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                            } catch (Exception e13) {
                                e3 = e13;
                                i = responseCode;
                                if (a.a) {
                                    e3.printStackTrace();
                                }
                                this.d = ErrorMessage.a(-11, "httpsPost failed,Exception:" + e3.getMessage());
                                if (outputStream != null) {
                                    outputStream.close();
                                    obj = null;
                                    if (obj == null) {
                                    }
                                    if (this.d == null) {
                                        a.a("httpsPost failed,mResult is null");
                                        this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                                    }
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                obj = null;
                                if (obj == null) {
                                }
                                if (this.d == null) {
                                    a.a("httpsPost success end,parse result = " + this.d);
                                }
                                a.a("httpsPost failed,mResult is null");
                                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                            }
                        }
                        if (httpsURLConnection != null) {
                            httpsURLConnection.disconnect();
                            i = responseCode;
                        } else {
                            i = responseCode;
                        }
                    } catch (IOException e14) {
                        e722 = e14;
                        i = -1;
                        if (a.a) {
                            e722.printStackTrace();
                            a.a("httpsPost parse failed;" + e722.getMessage());
                        }
                        this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
                        bufferedReader.close();
                        inputStream.close();
                        if (httpsURLConnection == null) {
                            httpsURLConnection.disconnect();
                            obj = null;
                        } else {
                            obj = null;
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (obj == null) {
                        }
                        if (this.d == null) {
                            a.a("httpsPost success end,parse result = " + this.d);
                        }
                        a.a("httpsPost failed,mResult is null");
                        this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                    } catch (Throwable th7) {
                        th = th7;
                        i = -1;
                        bufferedReader.close();
                        inputStream.close();
                        if (httpsURLConnection != null) {
                            httpsURLConnection.disconnect();
                        }
                        throw th;
                    }
                } catch (IOException e15) {
                    e722 = e15;
                    inputStream = null;
                    i = -1;
                    if (a.a) {
                        e722.printStackTrace();
                        a.a("httpsPost parse failed;" + e722.getMessage());
                    }
                    this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
                    if (!(inputStream == null || bufferedReader == null)) {
                        bufferedReader.close();
                        inputStream.close();
                    }
                    if (httpsURLConnection == null) {
                        httpsURLConnection.disconnect();
                        obj = null;
                    } else {
                        obj = null;
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (obj == null) {
                    }
                    if (this.d == null) {
                        a.a("httpsPost success end,parse result = " + this.d);
                    }
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                } catch (Throwable th8) {
                    th = th8;
                    inputStream = null;
                    i = -1;
                    bufferedReader.close();
                    inputStream.close();
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                    throw th;
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (MalformedURLException e16) {
                e2 = e16;
                i = -1;
                outputStream2 = outputStream;
                if (a.a) {
                    e2.printStackTrace();
                }
                this.d = ErrorMessage.a(-11, "httpsPost failed,MalformedURLException:" + e2.getMessage());
                if (outputStream2 != null) {
                    outputStream2.close();
                    obj = null;
                    if (obj == null) {
                    }
                    if (this.d == null) {
                        a.a("httpsPost success end,parse result = " + this.d);
                    }
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                a.a("httpsPost success end,parse result = " + this.d);
            } catch (IOException e17) {
                e722 = e17;
                i = -1;
                if (a.a) {
                    e722.printStackTrace();
                }
                this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
                if (outputStream != null) {
                    outputStream.close();
                    obj = null;
                    if (obj == null) {
                    }
                    if (this.d == null) {
                        a.a("httpsPost success end,parse result = " + this.d);
                    }
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                a.a("httpsPost success end,parse result = " + this.d);
            } catch (Exception e18) {
                e3 = e18;
                i = -1;
                if (a.a) {
                    e3.printStackTrace();
                }
                this.d = ErrorMessage.a(-11, "httpsPost failed,Exception:" + e3.getMessage());
                if (outputStream != null) {
                    outputStream.close();
                    obj = null;
                    if (obj == null) {
                    }
                    if (this.d == null) {
                        a.a("httpsPost success end,parse result = " + this.d);
                    }
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost failed,mResult is null");
                    this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
                }
                a.a("httpsPost success end,parse result = " + this.d);
            }
        } catch (MalformedURLException e19) {
            e2 = e19;
            i = -1;
            if (a.a) {
                e2.printStackTrace();
            }
            this.d = ErrorMessage.a(-11, "httpsPost failed,MalformedURLException:" + e2.getMessage());
            if (outputStream2 != null) {
                outputStream2.close();
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost success end,parse result = " + this.d);
                }
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            obj = null;
            if (obj == null) {
            }
            if (this.d == null) {
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            a.a("httpsPost success end,parse result = " + this.d);
        } catch (IOException e20) {
            e722 = e20;
            i = -1;
            outputStream = null;
            if (a.a) {
                e722.printStackTrace();
            }
            this.d = ErrorMessage.a(-11, "httpsPost failed,IOException:" + e722.getMessage());
            if (outputStream != null) {
                outputStream.close();
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost success end,parse result = " + this.d);
                }
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            obj = null;
            if (obj == null) {
            }
            if (this.d == null) {
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            a.a("httpsPost success end,parse result = " + this.d);
        } catch (Exception e21) {
            e3 = e21;
            i = -1;
            outputStream = null;
            if (a.a) {
                e3.printStackTrace();
            }
            this.d = ErrorMessage.a(-11, "httpsPost failed,Exception:" + e3.getMessage());
            if (outputStream != null) {
                outputStream.close();
                obj = null;
                if (obj == null) {
                }
                if (this.d == null) {
                    a.a("httpsPost success end,parse result = " + this.d);
                }
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            obj = null;
            if (obj == null) {
            }
            if (this.d == null) {
                a.a("httpsPost failed,mResult is null");
                this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
            }
            a.a("httpsPost success end,parse result = " + this.d);
        } catch (Throwable th9) {
            th = th9;
            outputStream = null;
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
        if (obj == null && 200 != i) {
            a.a("httpsPost failed,statusCode:" + i);
            this.d = ErrorMessage.a(-11, "httpsPost failed,statusCode:" + i);
        } else if (this.d == null) {
            a.a("httpsPost failed,mResult is null");
            this.d = ErrorMessage.a(-1, "httpsPost failed,internal error");
        } else {
            a.a("httpsPost success end,parse result = " + this.d);
        }
    }

    private static String b(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        for (Entry entry : hashMap.entrySet()) {
            Object obj2;
            if (obj != null) {
                obj2 = null;
            } else {
                stringBuilder.append("&");
                obj2 = obj;
            }
            stringBuilder.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
            obj = obj2;
        }
        return stringBuilder.toString();
    }

    private HttpsURLConnection b() {
        try {
            URL url = new URL(this.b);
            a.a("https URL: " + this.b);
            String a = a(this.a);
            if (a == null || a.equals("")) {
                a.c("Current network is not available.");
                this.d = ErrorMessage.a(-10, "Current network is not available.");
                return null;
            }
            a.a("checkNetwork = " + a);
            HttpsURLConnection httpsURLConnection = a.equals("cmwap") ? (HttpsURLConnection) HttpInstrumentation.openConnectionWithProxy(url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress("10.0.0.172", 80)))) : a.equals(NetRequestManager.CTWAP_APN_NAME_2) ? (HttpsURLConnection) HttpInstrumentation.openConnectionWithProxy(url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress("10.0.0.200", 80)))) : (HttpsURLConnection) HttpInstrumentation.openConnection(url.openConnection());
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpsURLConnection.setConnectTimeout(50000);
            httpsURLConnection.setReadTimeout(50000);
            return httpsURLConnection;
        } catch (MalformedURLException e) {
            if (a.a) {
                e.printStackTrace();
                a.a(e.getMessage());
            }
            this.d = ErrorMessage.a(-11, "Auth server could not be parsed as a URL.");
            return null;
        } catch (Exception e2) {
            if (a.a) {
                e2.printStackTrace();
                a.a(e2.getMessage());
            }
            this.d = ErrorMessage.a(-11, "Init httpsurlconnection failed.");
            return null;
        }
    }

    private HashMap<String, String> c(HashMap<String, String> hashMap) {
        HashMap<String, String> hashMap2 = new HashMap();
        for (String str : hashMap.keySet()) {
            String str2 = str2.toString();
            hashMap2.put(str2, hashMap.get(str2));
        }
        return hashMap2;
    }

    protected String a(HashMap<String, String> hashMap) {
        this.c = c(hashMap);
        this.b = (String) this.c.get("url");
        HttpsURLConnection b = b();
        if (b == null) {
            a.c("syncConnect failed,httpsURLConnection is null");
            return this.d;
        }
        a(b);
        return this.d;
    }

    protected boolean a() {
        a.a("checkNetwork start");
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.a.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return false;
            }
            a.a("checkNetwork end");
            return true;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
