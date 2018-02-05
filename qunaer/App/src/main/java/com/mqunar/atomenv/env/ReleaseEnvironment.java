package com.mqunar.atomenv.env;

import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import com.mqunar.BuildConfig;
import com.mqunar.atomenv.AndroidUtils;
import com.mqunar.atomenv.AtomEnvConstants;
import com.mqunar.atomenv.IEnvironment;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.atomenv.SystemPropertyProxy;
import com.mqunar.atomenv.jni.CidInfo;
import com.mqunar.atomenv.model.Config;
import com.mqunar.atomenv.model.ServerTime;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.json.JsonUtils;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import qunar.lego.utils.Goblin;

public class ReleaseEnvironment implements IEnvironment {
    public static final String FILE_NAME = ".unique";
    public static final String FILE_NAME_SID = ".sunique";
    private Storage a = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SW);
    private Storage b = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_AD);
    private Storage c = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_GLOBAL);
    protected String gid = "";
    protected String sid = "";
    protected Storage storage_sys = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SYS);

    public String getUid() {
        String string = this.storage_sys.getString(AtomEnvConstants.SYS_UID, "");
        if (TextUtils.isEmpty(string) || "000000000000000".equals(string) || "0".equals(string) || "1111".equals(string) || "baidu".equals(string) || "00000000".equals(string)) {
            string = AndroidUtils.getIMEI();
            if (TextUtils.isEmpty(string)) {
                string = "";
            }
            if (ContextCompat.checkSelfPermission(QApplication.getContext(), "android.permission.READ_PHONE_STATE") != -1) {
                QLog.i("has READ_PHONE_STATE permission, cache uid", new Object[0]);
                this.storage_sys.putString(AtomEnvConstants.SYS_UID, string);
            }
        }
        return string;
    }

    public String getPid() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_PID, "_10010");
    }

    public String getVid() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_VID, "");
    }

    public String getCidForXiaomi() {
        boolean booleanValue;
        try {
            booleanValue = ((Boolean) Class.forName("miui.os.MiuiInit").getMethod("isPreinstalledPackage", new Class[]{String.class}).invoke(null, new Object[]{BuildConfig.APPLICATION_ID})).booleanValue();
        } catch (Exception e) {
            booleanValue = false;
        }
        if (booleanValue) {
            return "C3065";
        }
        return "";
    }

    public String getCid() {
        if (BuildConfig.APPLICATION_ID.equals(QApplication.getContext().getPackageName())) {
            Object stringFromJNI;
            try {
                stringFromJNI = new CidInfo().stringFromJNI();
                if (!TextUtils.isEmpty(stringFromJNI)) {
                    return new String(Goblin.da(stringFromJNI.getBytes()));
                }
            } catch (Throwable th) {
            }
            try {
                stringFromJNI = getCidFromFile();
                if (!TextUtils.isEmpty(stringFromJNI)) {
                    return new String(Goblin.da(stringFromJNI.getBytes()));
                }
            } catch (Throwable th2) {
            }
        }
        return this.storage_sys.getString(AtomEnvConstants.SYS_CID, "");
    }

    protected String getCidFromFile() {
        String str;
        File file;
        Exception e;
        Throwable th;
        Exception exception;
        String str2 = SystemPropertyProxy.get(QApplication.getContext(), "ro.cid.path.qunar");
        if (TextUtils.isEmpty(str2)) {
            str2 = "/system/etc/";
        }
        if (str2.endsWith("/") || str2.endsWith("\\")) {
            str = str2 + "QunarCid.conf";
        } else {
            str = str2 + "/" + "QunarCid.conf";
        }
        File file2 = new File(str);
        if (file2.exists() && file2.isFile()) {
            file = file2;
        } else {
            if (str2.endsWith("/") || str2.endsWith("\\")) {
                str2 = str2 + "Cinfo.conf";
            } else {
                str2 = str2 + "/" + "Cinfo.conf";
            }
            File file3 = new File(str2);
            file = (file3.exists() && file3.isFile()) ? file3 : new File("/system/etc/appchannel/Cinfo.conf");
        }
        str = "";
        if (!file.exists() || !file.isFile()) {
            return str;
        }
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2;
        try {
            bufferedReader2 = new BufferedReader(new FileReader(file));
            try {
                str2 = bufferedReader2.readLine();
                try {
                    bufferedReader2.close();
                    if (bufferedReader2 == null) {
                        return str2;
                    }
                    try {
                        bufferedReader2.close();
                        return str2;
                    } catch (Exception e2) {
                        return str2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        if (bufferedReader2 != null) {
                            return str2;
                        }
                        try {
                            bufferedReader2.close();
                            return str2;
                        } catch (Exception e4) {
                            return str2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
                exception = e6;
                str2 = str;
                e = exception;
                e.printStackTrace();
                if (bufferedReader2 != null) {
                    return str2;
                }
                bufferedReader2.close();
                return str2;
            }
        } catch (Exception e62) {
            bufferedReader2 = null;
            exception = e62;
            str2 = str;
            e = exception;
            e.printStackTrace();
            if (bufferedReader2 != null) {
                return str2;
            }
            bufferedReader2.close();
            return str2;
        } catch (Throwable th3) {
            th = th3;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    public String getSid() {
        try {
            if (TextUtils.isEmpty(this.sid)) {
                b();
            }
            return this.sid;
        } catch (Throwable th) {
            QLog.e(th);
            return "";
        }
    }

    public String getGid() {
        try {
            if (TextUtils.isEmpty(this.gid)) {
                a();
            }
            return this.gid;
        } catch (Throwable th) {
            QLog.e(th);
            return "";
        }
    }

    public boolean isAutoSwapImage() {
        return this.a.getBoolean(AtomEnvConstants.SW_AUTOSWAPIMAGE, false);
    }

    public void putAutoSwapImage(boolean z) {
        this.a.putBoolean(AtomEnvConstants.SW_AUTOSWAPIMAGE, z);
    }

    private void a() {
        Object readLine;
        Throwable th;
        Throwable e;
        Object obj;
        Throwable th2;
        BufferedReader bufferedReader = null;
        Object string = this.storage_sys.getString(AtomEnvConstants.SYS_GID, "");
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/", FILE_NAME);
                if (file.exists()) {
                    BufferedReader bufferedReader2;
                    try {
                        bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        try {
                            readLine = bufferedReader2.readLine();
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e2) {
                                    try {
                                        e2.printStackTrace();
                                    } catch (Throwable e3) {
                                        th = e3;
                                        obj = readLine;
                                        th2 = th;
                                        QLog.e(th2);
                                        readLine = obj;
                                        if (readLine == null) {
                                        }
                                        if (TextUtils.isEmpty(string)) {
                                            this.gid = string;
                                            a(this.gid);
                                        } else if (TextUtils.isEmpty(readLine)) {
                                            this.gid = readLine;
                                            a(this.gid);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            e3 = th3;
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            throw e3;
                        }
                    } catch (Throwable th4) {
                        e3 = th4;
                        bufferedReader2 = bufferedReader;
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        throw e3;
                    }
                }
            }
        } catch (Throwable e32) {
            th = e32;
            BufferedReader bufferedReader3 = bufferedReader;
            th2 = th;
            QLog.e(th2);
            readLine = obj;
            if (readLine == null) {
            }
            if (TextUtils.isEmpty(string)) {
                this.gid = string;
                a(this.gid);
            } else if (TextUtils.isEmpty(readLine)) {
                this.gid = readLine;
                a(this.gid);
            }
        }
        if (readLine == null && string != null && string.equals(readLine)) {
            this.gid = string;
        } else if (TextUtils.isEmpty(string)) {
            this.gid = string;
            a(this.gid);
        } else if (TextUtils.isEmpty(readLine)) {
            this.gid = readLine;
            a(this.gid);
        }
    }

    private void a(String str) {
        Throwable e;
        QLog.d(str, new Object[0]);
        this.storage_sys.putString(AtomEnvConstants.SYS_GID, str);
        BufferedWriter bufferedWriter = null;
        BufferedWriter bufferedWriter2;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/", FILE_NAME), false)));
                try {
                    bufferedWriter2.write(str);
                    bufferedWriter2.flush();
                } catch (Exception e2) {
                    e = e2;
                    try {
                        QLog.e(e);
                        if (bufferedWriter2 != null) {
                            try {
                                bufferedWriter2.close();
                            } catch (IOException e3) {
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        e = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw e;
                    }
                }
            }
            bufferedWriter2 = null;
            if (bufferedWriter2 != null) {
                try {
                    bufferedWriter2.close();
                } catch (IOException e5) {
                }
            }
        } catch (Exception e6) {
            e = e6;
            bufferedWriter2 = null;
            QLog.e(e);
            if (bufferedWriter2 != null) {
                bufferedWriter2.close();
            }
        } catch (Throwable th2) {
            e = th2;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw e;
        }
    }

    private void b() {
        BufferedReader bufferedReader;
        Object readLine;
        Throwable th;
        Throwable e;
        Object obj;
        Throwable th2;
        BufferedReader bufferedReader2 = null;
        Object string = this.storage_sys.getString(AtomEnvConstants.SYS_SID, "");
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/", FILE_NAME_SID);
                if (file.exists()) {
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        try {
                            readLine = bufferedReader.readLine();
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e2) {
                                    try {
                                        e2.printStackTrace();
                                    } catch (Throwable e3) {
                                        th = e3;
                                        obj = readLine;
                                        th2 = th;
                                        QLog.e(th2);
                                        readLine = obj;
                                        if (readLine == null) {
                                        }
                                        if (TextUtils.isEmpty(string)) {
                                            this.sid = string;
                                            b(this.sid);
                                        } else if (TextUtils.isEmpty(readLine)) {
                                            this.sid = readLine;
                                            b(this.sid);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            e3 = th3;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            throw e3;
                        }
                    } catch (Throwable th4) {
                        e3 = th4;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        throw e3;
                    }
                }
            }
        } catch (Throwable e32) {
            th = e32;
            BufferedReader bufferedReader3 = bufferedReader2;
            th2 = th;
            QLog.e(th2);
            readLine = obj;
            if (readLine == null) {
            }
            if (TextUtils.isEmpty(string)) {
                this.sid = string;
                b(this.sid);
            } else if (TextUtils.isEmpty(readLine)) {
                this.sid = readLine;
                b(this.sid);
            }
        }
        if (readLine == null && string != null && string.equals(readLine)) {
            this.sid = string;
        } else if (TextUtils.isEmpty(string)) {
            this.sid = string;
            b(this.sid);
        } else if (TextUtils.isEmpty(readLine)) {
            this.sid = readLine;
            b(this.sid);
        }
    }

    private void b(String str) {
        Throwable e;
        QLog.d(str, new Object[0]);
        this.storage_sys.putString(AtomEnvConstants.SYS_SID, str);
        BufferedWriter bufferedWriter = null;
        BufferedWriter bufferedWriter2;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/", FILE_NAME_SID), false)));
                try {
                    bufferedWriter2.write(str);
                    bufferedWriter2.flush();
                } catch (Exception e2) {
                    e = e2;
                    try {
                        QLog.e(e);
                        if (bufferedWriter2 != null) {
                            try {
                                bufferedWriter2.close();
                            } catch (IOException e3) {
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        e = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw e;
                    }
                }
            }
            bufferedWriter2 = null;
            if (bufferedWriter2 != null) {
                try {
                    bufferedWriter2.close();
                } catch (IOException e5) {
                }
            }
        } catch (Exception e6) {
            e = e6;
            bufferedWriter2 = null;
            QLog.e(e);
            if (bufferedWriter2 != null) {
                bufferedWriter2.close();
            }
        } catch (Throwable th2) {
            e = th2;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw e;
        }
    }

    public ServerTime getServerTime() {
        ServerTime serverTime;
        Throwable th;
        try {
            serverTime = new ServerTime();
            try {
                serverTime.tint = this.c.getLong(AtomEnvConstants.GLOBAL_TINT, 0);
                serverTime.tstr = this.c.getString(AtomEnvConstants.GLOBAL_TSTR, "");
            } catch (Throwable th2) {
                th = th2;
                QLog.e(th);
                return serverTime;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            serverTime = null;
            th = th4;
            QLog.e(th);
            return serverTime;
        }
        return serverTime;
    }

    public void putTint(long j) {
        this.c.putLong(AtomEnvConstants.GLOBAL_TINT, j);
    }

    public void putTstr(String str) {
        this.c.putString(AtomEnvConstants.GLOBAL_TSTR, str);
    }

    public String getSplashAdUrl() {
        return this.b.getString(AtomEnvConstants.AD_SPLASH, "");
    }

    public void putSplashAdUrl(String str) {
        this.b.putString(AtomEnvConstants.AD_SPLASH, str);
    }

    public String getSplashWebUrl() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_WELCOMEURL, "");
    }

    public String getHotDogUrl() {
        return "http://pitcher.corp.qunar.com/fca";
    }

    public String getCarPullUrl() {
        return "http://capi.qunar.com/crypt/orderdetail";
    }

    public String getBaiduVoiceUrl() {
        return "http://vse.baidu.com/echo.fcgi";
    }

    public String getHotelUploadPicUrl() {
        return "http://ud.client.qunar.com/ud";
    }

    public String getLocalLifeUrl() {
        return "http://live.qunar.com";
    }

    public String getCarAboutTouchUrl() {
        return "http://car.qunar.com/CharteredCar/about.jsp";
    }

    public String getPayUrl() {
        return "https://mpkq.qunar.com";
    }

    public String getOuterCarUrl() {
        return "http://intercar.qunar.com";
    }

    public String getScheme() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_SCHEME, "");
    }

    public String getSchemeWap() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_SCHEMEWAP, "");
    }

    public String getMac() {
        if (!this.storage_sys.contains(AtomEnvConstants.SYS_MAC)) {
            initMac();
        }
        return this.storage_sys.getString(AtomEnvConstants.SYS_MAC, "");
    }

    public void initMac() {
        try {
            c(AndroidUtils.getMac());
        } catch (Throwable th) {
        }
    }

    private void c(String str) {
        this.storage_sys.putString(AtomEnvConstants.SYS_MAC, str);
    }

    public boolean isRelease() {
        return true;
    }

    public boolean isBeta() {
        return false;
    }

    public boolean isDev() {
        return false;
    }

    public String getDBPath() {
        return this.c.getString(AtomEnvConstants.GLOBAL_DBPATH, "");
    }

    public void putDBPath(String str) {
        this.c.putString(AtomEnvConstants.GLOBAL_DBPATH, str);
    }

    public String getWXAppId() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_WXAPPID, "");
    }

    public Config getConfig() {
        Object string = this.storage_sys.getString(AtomEnvConstants.SYS_CONFIG, "");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (Config) JsonUtils.parseObject(string, Config.class);
    }

    public String getBetaLongitude() {
        return "";
    }

    public String getBetaLatitude() {
        return "";
    }

    public String getBetaString() {
        return "";
    }

    public String getConfigJson() {
        return this.storage_sys.getString(AtomEnvConstants.SYS_CONFIG, "");
    }

    static {
        try {
            System.loadLibrary("cid-info");
        } catch (Throwable th) {
        }
    }
}
