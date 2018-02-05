package com.megvii.livenessdeteciton.obf;

import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import com.megvii.livenessdetection.bean.FaceInfo;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.cookie.ClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    private float[] a = new float[5];
    private int b = 0;
    private boolean c = false;

    public static String a(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA");
            instance.update(bArr);
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                String toHexString = Integer.toHexString(b & 255);
                if (toHexString.length() == 2) {
                    stringBuilder.append(toHexString);
                } else {
                    stringBuilder.append("0");
                    stringBuilder.append(toHexString);
                }
            }
            return stringBuilder.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ClientCookie.VERSION_ATTR, VERSION.SDK_INT);
            jSONObject.put("release", VERSION.RELEASE);
            jSONObject.put("model", Build.MODEL);
            jSONObject.put("brand", Build.BRAND);
            jSONObject.put("manufacturer", Build.MANUFACTURER);
            jSONObject.put("arch", Build.CPU_ABI);
            jSONObject.put("fingerprint", Build.FINGERPRINT);
            return jSONObject;
        } catch (JSONException e) {
            return null;
        }
    }

    private static void a(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] a(String str) {
        InputStream fileInputStream;
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        try {
            fileInputStream = new FileInputStream(str);
            while (true) {
                try {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        byteArrayOutputStream.write(bArr, 0, read);
                    } else {
                        byteArrayOutputStream.close();
                        a(fileInputStream);
                        return byteArrayOutputStream.toByteArray();
                    }
                } catch (IOException e) {
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        } catch (IOException e2) {
            fileInputStream = null;
            a(fileInputStream);
            return null;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            fileInputStream = null;
            th = th4;
            a(fileInputStream);
            throw th;
        }
    }

    public static boolean b(String str) {
        if (str == null) {
            return false;
        }
        try {
            System.load(str);
            return true;
        } catch (UnsatisfiedLinkError e) {
            d.b("load dynamic library failed, please check library path");
            return false;
        }
    }

    public static String a(Rect rect) {
        return "242 " + rect.left + "," + rect.top + "," + rect.right + "," + rect.bottom;
    }

    public synchronized void a(boolean z) {
        synchronized (this) {
            this.c = true;
            for (int i = 0; i < 5; i++) {
                this.a[i] = 0.0f;
            }
            this.b = 0;
        }
    }

    public void a(FaceInfo faceInfo) {
        float[] fArr;
        int i;
        if (faceInfo == null) {
            if (this.c) {
                fArr = this.a;
                i = this.b;
                this.b = i + 1;
                fArr[i % 5] = 0.0f;
            }
        } else if (this.c) {
            fArr = this.a;
            i = this.b;
            this.b = i + 1;
            fArr[i % 5] = faceInfo.faceQuality;
            float f = 100.0f;
            float[] fArr2 = this.a;
            int length = fArr2.length;
            int i2 = 0;
            while (i2 < length) {
                float f2 = fArr2[i2];
                if (f2 >= f) {
                    f2 = f;
                }
                i2++;
                f = f2;
            }
            faceInfo.smoothQuality = f;
        } else {
            faceInfo.smoothQuality = faceInfo.faceQuality;
        }
    }
}
