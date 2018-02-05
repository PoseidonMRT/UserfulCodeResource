package org.acra.sender;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.tools.DateTimeUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.collector.l;
import org.acra.collector.w;
import org.acra.config.ACRAConfiguration;
import org.acra.prefs.b;
import org.acra.util.a;
import org.acra.util.d;
import org.acra.util.i;
import org.apache.http.HttpStatus;
import qunar.lego.utils.FormPart;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;
import qunar.lego.utils.PitcherResponse;

public class QSenderProxy implements ReportSender {
    private String[] currentUploadScreenFrequencyInfo = new String[2];
    private String[] lastUploadScreenFrequencyInfo = new String[2];
    private String mCParam = "";
    private ACRAConfiguration mConfig;
    private Context mContext;
    private boolean mIsSilent = false;
    private String mReleaseHostUrl = "";
    private String mReleasePitcherUrl = "";
    private boolean mSendScreenShotSwitch = false;

    public QSenderProxy(@NonNull String str, @NonNull String str2) {
        this.mReleaseHostUrl = str;
        this.mReleasePitcherUrl = str2;
    }

    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        this.mContext = context;
        this.mCParam = str;
        this.mConfig = aCRAConfiguration;
        sendCrash(crashReportData, file, i);
    }

    private void sendCrash(@NonNull CrashReportData crashReportData, @NonNull File file, int i) {
        File file2 = null;
        try {
            file2 = dealScreenshot(crashReportData, i);
        } catch (Throwable th) {
            ACRA.f.e(ACRA.e, "deal screen shot failed when send log :" + th.toString());
        }
        File a = a.a(getErrorString(crashReportData), file.getPath() + ".acrajava.gz");
        if (a == null) {
            return;
        }
        if (crashReportData.getProperty(ReportField.NATIVE_CRASH) != null) {
            sendNativeCrash(a, crashReportData, file, file2, i);
        } else {
            sendJavaCrash(a, file, file2, i);
        }
    }

    @Nullable
    private File dealScreenshot(@NonNull CrashReportData crashReportData, int i) {
        if ("true".equals(crashReportData.getProperty(ReportField.IS_SILENT))) {
            this.mIsSilent = true;
        }
        getCurrentUploadFrequencyInfo();
        File screenShot = getScreenShot(crashReportData);
        this.mSendScreenShotSwitch = isSendScreenShot(screenShot, crashReportData, i);
        return screenShot;
    }

    private String getErrorString(CrashReportData crashReportData) {
        String str = "";
        if (crashReportData != null) {
            try {
                str = crashReportData.toJSON().toString();
            } catch (i.a e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private void sendJavaCrash(@NonNull File file, @NonNull File file2, @Nullable File file3, int i) {
        ArrayList arrayList = new ArrayList();
        FormPart formPart = new FormPart("c", this.mCParam);
        formPart.addHeader("X-ClientEncoding", "none");
        arrayList.add(formPart);
        formPart = new FormPart(ParamStr.RET_RES_ERROR, file.getAbsolutePath(), "");
        formPart.addHeader("X-ClientEncoding", "gzip");
        arrayList.add(formPart);
        if (this.mSendScreenShotSwitch && file3 != null) {
            formPart = new FormPart("snapshot.png", file3.getAbsolutePath(), "");
            formPart.addHeader("X-ClientEncoding", "gzip");
            arrayList.add(formPart);
        }
        PitcherResponse request = getPitcher(this.mContext, arrayList).request();
        if (request.e != null) {
            ACRA.f.e(ACRA.e, "send java crash error : " + request.e.toString());
            if (i == 1) {
                b.a(file3);
            }
        } else if (request.respcode > HttpStatus.SC_BAD_REQUEST) {
            ACRA.f.e(ACRA.e, "send java crash failed，respcode is " + request.respcode + ".");
            if (i == 1) {
                b.a(file3);
            }
        } else {
            ACRA.f.e(ACRA.e, "send java crash successfully");
            b.a(file2);
            if (file3 != null) {
                updateUploadFrequencyInfo();
            }
            b.a(file3);
        }
        b.a(file);
    }

    private void sendNativeCrash(@NonNull File file, @NonNull CrashReportData crashReportData, @NonNull File file2, @Nullable File file3, int i) {
        File file4 = new File(crashReportData.getProperty(ReportField.NATIVE_CRASH));
        if (file4.exists()) {
            File a = a.a(file4, false);
            if (a != null && a.exists()) {
                ArrayList arrayList = new ArrayList();
                FormPart formPart = new FormPart("c", this.mCParam);
                formPart.addHeader("X-ClientEncoding", "none");
                arrayList.add(formPart);
                formPart = new FormPart(ParamStr.RET_RES_ERROR, file.getAbsolutePath(), "");
                formPart.addHeader("X-ClientEncoding", "gzip");
                arrayList.add(formPart);
                formPart = new FormPart("dump", a.getAbsolutePath(), "");
                formPart.addHeader("X-ClientEncoding", "gzip");
                arrayList.add(formPart);
                if (this.mSendScreenShotSwitch && file3 != null) {
                    formPart = new FormPart("snapshot.png", file3.getAbsolutePath(), "");
                    formPart.addHeader("X-ClientEncoding", "gzip");
                    arrayList.add(formPart);
                }
                PitcherResponse request = getPitcher(this.mContext, arrayList).request();
                if (request.e != null) {
                    ACRA.f.e(ACRA.e, "send native crash error : " + request.e.toString());
                    if (i == 1) {
                        b.a(file3);
                    }
                } else if (request.respcode > HttpStatus.SC_BAD_REQUEST) {
                    ACRA.f.e(ACRA.e, "send native crash failed，respcode is " + request.respcode + ".");
                    if (i == 1) {
                        b.a(file3);
                    }
                } else {
                    ACRA.f.e(ACRA.e, "send native crash successfully");
                    b.a(file2);
                    b.a(file4);
                    if (file3 != null) {
                        updateUploadFrequencyInfo();
                    }
                    b.a(file3);
                }
                b.a(file);
                b.a(a);
                return;
            }
            return;
        }
        b.a(file2);
        b.a(file);
        b.a(file3);
    }

    @NonNull
    private Pitcher getPitcher(@NonNull Context context, @NonNull ArrayList<FormPart> arrayList) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("X-ClientEncoding", "none");
        Pitcher pitcher = new Pitcher(context, this.mReleaseHostUrl, (List) arrayList, httpHeader);
        pitcher.setProxyUrl(this.mReleasePitcherUrl);
        ACRA.f.a(ACRA.e, "pitcher url: " + this.mReleasePitcherUrl);
        ACRA.f.a(ACRA.e, "host url: " + this.mReleaseHostUrl);
        return pitcher;
    }

    @Nullable
    private File getScreenShot(@NonNull CrashReportData crashReportData) {
        String property = crashReportData.getProperty(ReportField.SCREENSHOT);
        if (property == null || !property.contains("ACRA-screen")) {
            return null;
        }
        return new File(property);
    }

    private boolean isSendScreenShot(@Nullable File file, @NonNull CrashReportData crashReportData, int i) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            crashReportData.put(ReportField.SCREENSHOT, "第一次上传失败，截屏被果断删除！！！");
            return false;
        } else if (i != 1 || crashReportData.getProperty(ReportField.CUSTOM_DATA).contains("cloud") || crashReportData.getProperty(ReportField.CUSTOM_DATA).contains("monkey")) {
            return true;
        } else {
            if (file.length() > 512000) {
                crashReportData.put(ReportField.SCREENSHOT, "截屏大小是 " + (file.length() / 1024) + "K > 500K 不上传截屏！！！");
                return false;
            }
            Object a = l.a(this.mContext);
            if (!"wifi".equals(a) && !TextUtils.isEmpty(a)) {
                crashReportData.put(ReportField.SCREENSHOT, "当前网络为 " + a + " 并不是wifi，不上传截屏！！！");
                return false;
            } else if (isUploadScreenFrequencyPermission()) {
                return true;
            } else {
                String str;
                if (this.mIsSilent) {
                    str = "1次(silent)";
                } else {
                    str = "10次";
                }
                crashReportData.put(ReportField.SCREENSHOT, "自" + d.a(Long.parseLong(this.currentUploadScreenFrequencyInfo[0]), DateTimeUtils.yyyy_MM_dd_HH_mm_ss) + "开始，在1个小时内截屏 " + this.currentUploadScreenFrequencyInfo[1] + "次 > " + str + "，不上传截屏！！！");
                return false;
            }
        }
    }

    private boolean isUploadScreenFrequencyPermission() {
        if (this.mIsSilent) {
            if (Math.abs(System.currentTimeMillis() - Long.parseLong(this.lastUploadScreenFrequencyInfo[0])) > DateTimeUtils.ONE_HOUR || Integer.valueOf(this.lastUploadScreenFrequencyInfo[1]).intValue() < 1) {
                return true;
            }
            return false;
        } else if (Math.abs(System.currentTimeMillis() - Long.parseLong(this.lastUploadScreenFrequencyInfo[0])) > DateTimeUtils.ONE_HOUR || Integer.valueOf(this.lastUploadScreenFrequencyInfo[1]).intValue() < 10) {
            return true;
        } else {
            return false;
        }
    }

    private void getCurrentUploadFrequencyInfo() {
        String[] a;
        if (this.mIsSilent) {
            a = w.a(this.mContext, this.mConfig, "acra.uploadScreenFrequencySilent");
        } else {
            a = w.a(this.mContext, this.mConfig, "acra.uploadScreenFrequency");
        }
        this.lastUploadScreenFrequencyInfo[0] = a[0];
        this.lastUploadScreenFrequencyInfo[1] = a[1];
        if (Math.abs(System.currentTimeMillis() - Long.parseLong(a[0])) > DateTimeUtils.ONE_HOUR) {
            this.currentUploadScreenFrequencyInfo[0] = String.valueOf(System.currentTimeMillis());
            this.currentUploadScreenFrequencyInfo[1] = "1";
            return;
        }
        this.currentUploadScreenFrequencyInfo[0] = a[0];
        this.currentUploadScreenFrequencyInfo[1] = String.valueOf(Integer.valueOf(a[1]).intValue() + 1);
    }

    private void updateUploadFrequencyInfo() {
        Editor edit = new b(this.mContext, this.mConfig).a().edit();
        if (this.mIsSilent) {
            edit.putString("acra.uploadScreenFrequencySilent", this.currentUploadScreenFrequencyInfo[0] + Contact.NUMBER + this.currentUploadScreenFrequencyInfo[1]);
        } else {
            edit.putString("acra.uploadScreenFrequency", this.currentUploadScreenFrequencyInfo[0] + Contact.NUMBER + this.currentUploadScreenFrequencyInfo[1]);
        }
        edit.commit();
    }
}
