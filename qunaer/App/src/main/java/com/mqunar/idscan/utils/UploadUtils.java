package com.mqunar.idscan.utils;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.idscan.a;
import com.mqunar.idscan.model.ScanPassportParam;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.ChiefGuard;
import com.mqunar.libtask.FormPart;
import com.mqunar.libtask.HotdogConductor;
import com.mqunar.libtask.Ticket.RequestFeature;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class UploadUtils {
    public static AbsConductor a(ScanPassportParam scanPassportParam, String str) {
        return uploadFile(GlobalEnv.getInstance().getHotelUploadPicUrl(), "f_passport_upload", scanPassportParam, str);
    }

    public static AbsConductor uploadFailed(Context context, ScanPassportParam scanPassportParam) {
        File file = new File(context.getExternalFilesDir(null), "failedOcrImage.jpg");
        return file.exists() ? a(scanPassportParam, file.getPath()) : null;
    }

    public static AbsConductor uploadFile(String str, String str2, ScanPassportParam scanPassportParam, String str3) {
        Object obj = "";
        if (scanPassportParam != null) {
            obj = JSON.toJSONString(scanPassportParam);
        }
        ArrayList arrayList = new ArrayList(7);
        arrayList.add(str);
        arrayList.add(str2);
        arrayList.add(obj);
        arrayList.add(Collections.EMPTY_MAP);
        arrayList.add(obj);
        arrayList.add(Collections.EMPTY_MAP);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList2.add(new FormPart("file", str3, ""));
        arrayList.add(arrayList2);
        AbsConductor hotdogConductor = new HotdogConductor(new c(str3));
        hotdogConductor.setParams(arrayList.toArray());
        ChiefGuard.getInstance().addTask(a.a(), hotdogConductor, RequestFeature.CACHE_NEVER, RequestFeature.CANCELABLE);
        QLog.d("UploadUtils", "success upload file :" + str3, new Object[0]);
        return hotdogConductor;
    }

    public static AbsConductor uploadSuccessed(Context context, ScanPassportParam scanPassportParam) {
        File file = new File(context.getExternalFilesDir(null), "successedOcrImage.jpg");
        return file.exists() ? a(scanPassportParam, file.getPath()) : null;
    }
}
