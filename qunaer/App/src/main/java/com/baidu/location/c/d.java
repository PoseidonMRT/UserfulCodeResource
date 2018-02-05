package com.baidu.location.c;

import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.location.Jni;
import com.baidu.location.f;
import com.baidu.location.f.k;
import com.baidu.location.g.a;
import com.baidu.location.h.b;
import com.baidu.location.h.c;
import com.baidu.location.h.i;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.methods.HttpPost;

@Instrumented
public class d implements UncaughtExceptionHandler {
    private static d a = null;
    private int b = 0;

    private d() {
    }

    public static d a() {
        if (a == null) {
            a = new d();
        }
        return a;
    }

    private String a(Throwable th) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private void a(File file, String str, String str2) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(280);
            randomAccessFile.writeInt(12346);
            randomAccessFile.seek(300);
            randomAccessFile.writeLong(System.currentTimeMillis());
            byte[] bytes = str.getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            randomAccessFile.seek(600);
            bytes = str2.getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            if (!a(str, str2)) {
                randomAccessFile.seek(280);
                randomAccessFile.writeInt(1326);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    private boolean a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        if (!k.a().h()) {
            return false;
        }
        try {
            URL url = new URL(i.e);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("e0");
            stringBuffer.append("=");
            stringBuffer.append(str);
            stringBuffer.append("&");
            stringBuffer.append("e1");
            stringBuffer.append("=");
            stringBuffer.append(str2);
            stringBuffer.append("&");
            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(url.openConnection());
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(b.b);
            httpURLConnection.setReadTimeout(b.b);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(stringBuffer.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            return httpURLConnection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public void b() {
        String str = null;
        try {
            File file = new File((Environment.getExternalStorageDirectory().getPath() + "/traces") + "/error_fs2.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(280);
                if (1326 == randomAccessFile.readInt()) {
                    String str2;
                    byte[] bArr;
                    randomAccessFile.seek(308);
                    int readInt = randomAccessFile.readInt();
                    if (readInt <= 0 || readInt >= 2048) {
                        str2 = null;
                    } else {
                        bArr = new byte[readInt];
                        randomAccessFile.read(bArr, 0, readInt);
                        str2 = new String(bArr, 0, readInt);
                    }
                    randomAccessFile.seek(600);
                    readInt = randomAccessFile.readInt();
                    if (readInt > 0 && readInt < 2048) {
                        bArr = new byte[readInt];
                        randomAccessFile.read(bArr, 0, readInt);
                        str = new String(bArr, 0, readInt);
                    }
                    if (a(str2, str)) {
                        randomAccessFile.seek(280);
                        randomAccessFile.writeInt(12346);
                    }
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public void uncaughtException(Thread thread, Throwable th) {
        File file;
        String str;
        String str2;
        Object obj;
        File file2 = null;
        this.b++;
        if (this.b > 2) {
            Process.killProcess(Process.myPid());
            return;
        }
        String a;
        Object obj2;
        File file3;
        RandomAccessFile randomAccessFile;
        if (System.currentTimeMillis() - a.b() < 10000 && 6.33f > f.getFrameVersion()) {
            if (System.currentTimeMillis() - com.baidu.location.h.d.a().c() < 40000) {
                file = new File(i.g() + File.separator + f.getJarFileName());
                if (file.exists()) {
                    file.delete();
                }
            } else {
                com.baidu.location.h.d.a().b(System.currentTimeMillis());
            }
        }
        try {
            Object obj3;
            String str3;
            String encode;
            a = a(th);
            if (a != null) {
                try {
                    if (a.contains("com.baidu.location")) {
                        obj3 = 1;
                        Log.d(b.a, "errorhandle = " + a);
                        str3 = c.a().a(false) + com.baidu.location.a.a.a().c();
                        encode = str3 == null ? Jni.encode(str3) : null;
                        obj2 = obj3;
                        str = encode;
                        if (obj2 != null) {
                            try {
                                str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                                file = new File(str2 + "/error_fs2.dat");
                                if (file.exists()) {
                                    file3 = new File(str2);
                                    if (!file3.exists()) {
                                        file3.mkdirs();
                                    }
                                    if (file.createNewFile()) {
                                        file2 = file;
                                    }
                                    a(file2, str, a);
                                } else {
                                    randomAccessFile = new RandomAccessFile(file, "rw");
                                    randomAccessFile.seek(300);
                                    if (System.currentTimeMillis() - randomAccessFile.readLong() > DateTimeUtils.ONE_DAY) {
                                        a(file, str, a);
                                    }
                                    randomAccessFile.close();
                                }
                            } catch (Exception e) {
                            }
                        }
                        Process.killProcess(Process.myPid());
                    }
                } catch (Exception e2) {
                    obj2 = a;
                    obj = file;
                    str = null;
                    obj2 = null;
                    if (obj2 != null) {
                        str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                        file = new File(str2 + "/error_fs2.dat");
                        if (file.exists()) {
                            randomAccessFile = new RandomAccessFile(file, "rw");
                            randomAccessFile.seek(300);
                            if (System.currentTimeMillis() - randomAccessFile.readLong() > DateTimeUtils.ONE_DAY) {
                                a(file, str, a);
                            }
                            randomAccessFile.close();
                        } else {
                            file3 = new File(str2);
                            if (file3.exists()) {
                                file3.mkdirs();
                            }
                            if (file.createNewFile()) {
                                file2 = file;
                            }
                            a(file2, str, a);
                        }
                    }
                    Process.killProcess(Process.myPid());
                }
            }
            obj3 = null;
            Log.d(b.a, "errorhandle = " + a);
            str3 = c.a().a(false) + com.baidu.location.a.a.a().c();
            if (str3 == null) {
            }
            encode = str3 == null ? Jni.encode(str3) : null;
            obj2 = obj3;
            str = encode;
        } catch (Exception e3) {
            file = null;
            obj = file;
            str = null;
            obj2 = null;
            if (obj2 != null) {
                str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                file = new File(str2 + "/error_fs2.dat");
                if (file.exists()) {
                    file3 = new File(str2);
                    if (file3.exists()) {
                        file3.mkdirs();
                    }
                    if (file.createNewFile()) {
                        file2 = file;
                    }
                    a(file2, str, a);
                } else {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(300);
                    if (System.currentTimeMillis() - randomAccessFile.readLong() > DateTimeUtils.ONE_DAY) {
                        a(file, str, a);
                    }
                    randomAccessFile.close();
                }
            }
            Process.killProcess(Process.myPid());
        }
        if (obj2 != null) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
            file = new File(str2 + "/error_fs2.dat");
            if (file.exists()) {
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(300);
                if (System.currentTimeMillis() - randomAccessFile.readLong() > DateTimeUtils.ONE_DAY) {
                    a(file, str, a);
                }
                randomAccessFile.close();
            } else {
                file3 = new File(str2);
                if (file3.exists()) {
                    file3.mkdirs();
                }
                if (file.createNewFile()) {
                    file2 = file;
                }
                a(file2, str, a);
            }
        }
        Process.killProcess(Process.myPid());
    }
}
