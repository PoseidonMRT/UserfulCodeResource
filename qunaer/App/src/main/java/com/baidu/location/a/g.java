package com.baidu.location.a;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import com.baidu.location.Jni;
import com.baidu.location.f.k;
import com.baidu.location.h.c;
import com.baidu.location.h.d;
import com.baidu.location.h.f;
import com.baidu.location.h.h;
import com.baidu.location.h.i;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import com.mqunar.tools.DateTimeUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

@Instrumented
public class g extends f {
    private static g p = null;
    String a;
    String b;
    String c;
    String d;
    int e;
    Handler f;

    private g() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = 1;
        this.f = null;
        this.f = new Handler();
    }

    public static void a(File file, File file2) {
        BufferedOutputStream bufferedOutputStream;
        Throwable th;
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream = null;
                bufferedInputStream = bufferedInputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                throw th;
            }
            try {
                byte[] bArr = new byte[5120];
                while (true) {
                    int read = bufferedInputStream2.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    bufferedOutputStream.write(bArr, 0, read);
                }
                bufferedOutputStream.flush();
                file.delete();
                if (bufferedInputStream2 != null) {
                    bufferedInputStream2.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedInputStream = bufferedInputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedOutputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            throw th;
        }
    }

    public static boolean a(String str, String str2) {
        File file = new File(i.g() + File.separator + "tmp");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            HttpURLConnection httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(new URL(str).openConnection());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            httpURLConnection.disconnect();
            fileOutputStream.close();
            if (file.length() < 10240) {
                file.delete();
                return false;
            }
            file.renameTo(new File(i.g() + File.separator + str2));
            return true;
        } catch (Exception e) {
            file.delete();
            return false;
        }
    }

    public static g b() {
        if (p == null) {
            p = new g();
        }
        return p;
    }

    private Handler f() {
        return this.f;
    }

    private void g() {
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(h.a + "/grtcf.dat");
            if (!file.exists()) {
                File file2 = new File(h.a);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(2);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.seek(8);
                    byte[] bytes = "1980_01_01:0".getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes);
                    randomAccessFile.seek(200);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.seek(800);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(200);
            randomAccessFile.writeBoolean(true);
            if (this.e == 1) {
                randomAccessFile.writeBoolean(true);
            } else {
                randomAccessFile.writeBoolean(false);
            }
            if (this.d != null) {
                byte[] bytes2 = this.d.getBytes();
                randomAccessFile.writeInt(bytes2.length);
                randomAccessFile.write(bytes2);
            } else {
                randomAccessFile.writeInt(0);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    private void h() {
        if (this.a != null && k.a().h()) {
            new Thread(this) {
                final /* synthetic */ g a;

                {
                    this.a = r1;
                }

                public void run() {
                    if (this.a.i()) {
                        this.a.j();
                    }
                }
            }.start();
        }
    }

    private boolean i() {
        return (this.c == null || new File(i.g() + File.separator + this.c).exists()) ? true : a("http://" + this.a + "/" + this.c, this.c);
    }

    private void j() {
        if (this.b != null) {
            File file = new File(i.g() + File.separator + this.b);
            if (!file.exists() && a("http://" + this.a + "/" + this.b, this.b)) {
                String a = i.a(file);
                if (this.d != null && a != null && this.d.equals(a)) {
                    File file2 = new File(i.g() + File.separator + com.baidu.location.f.replaceFileName);
                    if (file2.exists()) {
                        file2.delete();
                    }
                    try {
                        a(file, file2);
                    } catch (Exception e) {
                        file2.delete();
                    }
                }
            }
        }
    }

    public void a() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&sdk=");
        stringBuffer.append(6.33f);
        stringBuffer.append("&fw=");
        stringBuffer.append(com.baidu.location.f.getFrameVersion());
        stringBuffer.append("&suit=");
        stringBuffer.append(2);
        if (c.a().b == null) {
            stringBuffer.append("&im=");
            stringBuffer.append(c.a().a);
        } else {
            stringBuffer.append("&cu=");
            stringBuffer.append(c.a().b);
        }
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&sv=");
        String str = VERSION.RELEASE;
        if (str != null && str.length() > 10) {
            str = str.substring(0, 10);
        }
        stringBuffer.append(str);
        try {
            if (VERSION.SDK_INT > 20) {
                String[] strArr = Build.SUPPORTED_ABIS;
                str = null;
                while (i < strArr.length) {
                    str = i == 0 ? strArr[i] + VoiceWakeuperAidl.PARAMS_SEPARATE : str + strArr[i] + VoiceWakeuperAidl.PARAMS_SEPARATE;
                    i++;
                }
            } else {
                str = Build.CPU_ABI2;
            }
        } catch (Error e) {
            str = null;
        } catch (Exception e2) {
            str = null;
        }
        if (str != null) {
            stringBuffer.append("&cpuabi=");
            stringBuffer.append(str);
        }
        stringBuffer.append("&pack=");
        stringBuffer.append(c.d);
        this.h = i.d() + "?&it=" + Jni.en1(stringBuffer.toString());
    }

    public void a(boolean z) {
        if (z) {
            try {
                JSONObject jSONObject = new JSONObject(this.j);
                if ("up".equals(jSONObject.getString("res"))) {
                    this.a = jSONObject.getString("upath");
                    if (jSONObject.has("u1")) {
                        this.b = jSONObject.getString("u1");
                    }
                    if (jSONObject.has("u2")) {
                        this.c = jSONObject.getString("u2");
                    }
                    if (jSONObject.has("u1_md5")) {
                        this.d = jSONObject.getString("u1_md5");
                    }
                    f().post(new Runnable(this) {
                        final /* synthetic */ g a;

                        {
                            this.a = r1;
                        }

                        public void run() {
                            this.a.h();
                        }
                    });
                }
                if (jSONObject.has("ison")) {
                    this.e = jSONObject.getInt("ison");
                }
                g();
            } catch (Exception e) {
            }
        }
        d.a().a(System.currentTimeMillis());
    }

    public void c() {
        if (System.currentTimeMillis() - d.a().b() > DateTimeUtils.ONE_DAY) {
            f().postDelayed(new Runnable(this) {
                final /* synthetic */ g a;

                {
                    this.a = r1;
                }

                public void run() {
                    if (k.a().h()) {
                        this.a.d();
                    }
                }
            }, 10000);
            f().postDelayed(new Runnable(this) {
                final /* synthetic */ g a;

                {
                    this.a = r1;
                }

                public void run() {
                    if (k.a().h()) {
                        com.baidu.location.e.d.a().m();
                    }
                }
            }, 5000);
        }
    }
}
