package com.mqunar.idscan.decode;

import android.app.AlertDialog.Builder;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.idscan.R;
import com.mqunar.idscan.algo.i;
import com.mqunar.idscan.model.ScanPassportResultData;
import com.mqunar.idscan.utils.b;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class a extends Handler {
    public static boolean a = false;
    private final SimpleDateFormat b;
    private final com.mqunar.idscan.activity.a c;
    private boolean d = true;
    private int e = -1;

    a(com.mqunar.idscan.activity.a aVar) {
        this.c = aVar;
        this.b = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd, Locale.US);
        this.b.setLenient(false);
    }

    private void a(String str) {
        if (str == null) {
            try {
                str = "0";
            } catch (Throwable e) {
                QLog.e(e);
                return;
            }
        }
        if (str.length() == 88) {
            this.e = 5;
            a = true;
            return;
        }
        int parseInt = Integer.parseInt(str);
        if (parseInt > this.e) {
            this.e = parseInt;
            a = true;
            return;
        }
        a = false;
    }

    private ScanPassportResultData b(String str) {
        int i = 0;
        ScanPassportResultData scanPassportResultData = new ScanPassportResultData();
        if (str == null || str.length() != 88) {
            return scanPassportResultData;
        }
        if (str.startsWith("P")) {
            scanPassportResultData.isPP = true;
            scanPassportResultData.certs_type = "PP";
            scanPassportResultData.certs_name = "护照";
        } else if (str.startsWith("W<")) {
            scanPassportResultData.certs_type = "GA";
            scanPassportResultData.certs_name = "港澳通行证";
        } else if (str.startsWith("D<")) {
            scanPassportResultData.certs_type = "TW";
            scanPassportResultData.certs_name = "大陆居民往来台湾通行证";
        } else if (str.startsWith("T<")) {
            scanPassportResultData.certs_type = "TB";
            scanPassportResultData.certs_name = "台胞证";
        } else {
            scanPassportResultData.ok = false;
            return scanPassportResultData;
        }
        Object obj = null;
        String[] split = str.substring(0, 44).split("<<");
        if (scanPassportResultData.isPP && split != null && split.length > 1) {
            String str2 = split[0].substring(5) + "/" + split[1];
            scanPassportResultData.cvName = str2;
            scanPassportResultData.name = str2;
            obj = split[0].substring(2, 5);
        }
        String substring = str.substring(44);
        scanPassportResultData.certs_number = substring.substring(0, 9).replaceAll("<", "");
        String substring2 = substring.substring(10, 13);
        scanPassportResultData.birthday = c(substring.substring(13, 19));
        String substring3 = substring.substring(20, 21);
        if (substring3.equals("M")) {
            scanPassportResultData.gender = "1";
        } else if (substring3.equals("F")) {
            scanPassportResultData.gender = "2";
        } else {
            scanPassportResultData.gender = "";
        }
        scanPassportResultData.certs_invalidday = c(substring.substring(21, 27));
        JSONArray b = b.a().b();
        if (b != null) {
            while (i < b.size() && (scanPassportResultData.certs_cardlssuePlace2Code == null || scanPassportResultData.certs_cardlssuePlace2Code.equals("") || scanPassportResultData.nationality2Code == null || scanPassportResultData.nationality2Code.equals(""))) {
                JSONObject parseObject = JSON.parseObject(b.get(i).toString());
                String string = parseObject.getString("country3code");
                String string2 = parseObject.getString("country2code");
                substring3 = parseObject.getString("nameZh");
                if (string.equals(obj)) {
                    scanPassportResultData.certs_cardlssuePlace2Code = string2;
                    scanPassportResultData.certs_cardlssuePlaceName = substring3;
                }
                if (string.equals(substring2)) {
                    scanPassportResultData.nationality2Code = string2;
                    scanPassportResultData.nationalityName = substring3;
                }
                i++;
            }
            if (scanPassportResultData.certs_cardlssuePlace2Code == null || scanPassportResultData.certs_cardlssuePlace2Code.equals("")) {
                scanPassportResultData.certs_cardlssuePlace2Code = "CN";
                scanPassportResultData.certs_cardlssuePlaceName = "中国";
            }
            if (scanPassportResultData.nationality2Code == null || scanPassportResultData.nationality2Code.equals("")) {
                scanPassportResultData.nationality2Code = "CN";
                scanPassportResultData.nationalityName = "中国";
            }
        }
        return scanPassportResultData;
    }

    private String c(String str) {
        int i = 0;
        if (str == null || str.length() != 6) {
            return "";
        }
        if (str.contains("S")) {
            str = str.replace("S", "5");
        }
        if (str.contains("O")) {
            str = str.replace("O", "0");
        }
        if (str.contains("Z")) {
            str = str.replace("Z", "2");
        }
        if (str.contains("U")) {
            str = str.replace("U", "0");
        }
        if (str.contains("D")) {
            str = str.replace("D", "0");
        }
        if (str.contains("I")) {
            str = str.replace("I", "1");
        }
        if (str.contains("B")) {
            str = str.replace("B", "8");
        }
        String substring = str.substring(i, 2);
        String substring2 = str.substring(2, 4);
        String substring3 = str.substring(4, 6);
        try {
            i = Integer.parseInt(substring);
        } catch (Throwable e) {
            QLog.e(e);
        }
        String str2 = (i > 30 ? "19" + substring : "20" + substring) + "-" + substring2 + "-" + substring3;
        try {
            this.b.parse(str2);
            return str2;
        } catch (Throwable e2) {
            QLog.e(e2);
            return "";
        }
    }

    public final void handleMessage(Message message) {
        if (this.d) {
            switch (message.what) {
                case 20:
                    try {
                        byte[] bArr = (byte[]) message.obj;
                        int i = message.arg1;
                        int i2 = message.arg2;
                        Rect g = this.c.d().g();
                        String a = i.a(bArr, i, i2, g.left, g.top, g.right - g.left, g.bottom - g.top);
                        if (!"-200".equals(a)) {
                            if (a != null) {
                                try {
                                    if (a.length() == 88) {
                                        boolean z;
                                        ScanPassportResultData b = b(a);
                                        b.scanResult = a;
                                        if (b.ok) {
                                            if (b.isPP) {
                                                if (b.name == null || "".equals(b.name)) {
                                                    z = false;
                                                } else if (b.cvName == null || "".equals(b.cvName)) {
                                                    z = false;
                                                }
                                            }
                                            z = (b.gender == null || "".equals(b.gender)) ? false : (b.birthday == null || "".equals(b.birthday)) ? false : (b.nationality2Code == null || "".equals(b.nationality2Code)) ? false : (b.nationalityName == null || "".equals(b.nationalityName)) ? false : (b.certs_type == null || "".equals(b.certs_type)) ? false : (b.certs_number == null || "".equals(b.certs_number)) ? false : (b.certs_name == null || "".equals(b.certs_name)) ? false : (b.certs_cardlssuePlace2Code == null || "".equals(b.certs_cardlssuePlace2Code)) ? false : (b.certs_cardlssuePlaceName == null || "".equals(b.certs_cardlssuePlaceName)) ? false : (b.certs_invalidday == null || "".equals(b.certs_invalidday)) ? false : true;
                                        } else {
                                            z = false;
                                        }
                                        if (z) {
                                            QLog.e("isUsable", "图片可用", new Object[0]);
                                            Message.obtain(this.c.c(), 9, b).sendToTarget();
                                            return;
                                        }
                                        QLog.e("isUsable", "字段不全", new Object[0]);
                                        a(a);
                                        Message.obtain(this.c.c(), 10).sendToTarget();
                                        return;
                                    }
                                } catch (Exception e) {
                                    return;
                                }
                            }
                            QLog.e("isUsable", "图片不可用", new Object[0]);
                            a(a);
                            Message.obtain(this.c.c(), 10).sendToTarget();
                            return;
                        } else if (this.c != null) {
                            new Builder(this.c).setMessage("功能暂时无法使用，请您稍后重试").setPositiveButton(R.string.idscan_sure, new b(this)).setCancelable(false).show();
                            return;
                        } else {
                            return;
                        }
                    } catch (Exception e2) {
                        return;
                    }
                case 21:
                    this.d = false;
                    Looper.myLooper().quit();
                    return;
                default:
                    return;
            }
        }
    }
}
