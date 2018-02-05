package com.megvii.livenessdetection;

import android.content.Context;
import com.megvii.licensemanager.ILicenseManager;
import com.megvii.livenessdeteciton.obf.b;
import com.megvii.livenessdeteciton.obf.c;
import com.megvii.livenessdeteciton.obf.e;
import java.security.InvalidParameterException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LivenessLicenseManager implements ILicenseManager {
    private Context a;
    private e b;

    private native long nativeCheckLicense(Context context, String str);

    private native String nativeGenAuthMsg(Context context, String str, String str2, String str3, String str4, String str5, String str6);

    public LivenessLicenseManager(Context context) {
        if (context == null) {
            throw new InvalidParameterException("mContext cannot be null");
        }
        this.a = context;
        c.a(this.a).a("livenessdetection", "v2.4.2");
        this.b = new e(this.a);
    }

    public String getContext(String str) {
        String b;
        JSONObject a = b.a();
        String str2 = System.currentTimeMillis();
        this.b.a("809bd36cf78612fd1f11b739c382bfac", b.a(this.a.getPackageName().getBytes()));
        this.b.a("37dbd151eb3ca24477bc27cf0febcbe3", str2);
        String a2 = this.b.a("cb072839e1e240a23baae123ca6cf165");
        Context context = this.a;
        String packageName = this.a.getPackageName();
        String jSONObject = a.toString();
        String str3 = (System.currentTimeMillis() / 1000);
        Context context2 = this.a;
        JSONArray jSONArray = new JSONArray();
        if (!(context2 == null || "false".equals(this.b.a("49668163590f816aaf863df014568115")))) {
            b = this.b.b("8cd0604ba33e2ba7f38a56f0aec08a54");
            if (b != null) {
                jSONArray.put(b);
            }
        }
        try {
            int parseInt;
            JSONObject jSONObject2 = new JSONObject(nativeGenAuthMsg(context, packageName, str, jSONObject, str3, jSONArray.toString(), a2));
            b = jSONObject2.getString("auth");
            packageName = jSONObject2.getString("seed");
            String string = jSONObject2.getString("key");
            try {
                parseInt = Integer.parseInt(this.b.a("5f389fef5fd41c84a33a91c6574cbf51"));
            } catch (Exception e) {
                parseInt = 0;
            }
            this.b.a("5f389fef5fd41c84a33a91c6574cbf51", (parseInt + 1));
            this.b.a("b62f7aea9613b98976498a9ecabe537b", string);
            if (packageName.equals(a2)) {
                return b;
            }
            this.b.a("cb072839e1e240a23baae123ca6cf165", packageName);
            return b;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public long setLicense(String str) {
        if (this.a == null || str == null || str.length() == 0) {
            return 0;
        }
        this.a = this.a.getApplicationContext();
        if (this.b.a("b62f7aea9613b98976498a9ecabe537b") == null || this.b.a("cb072839e1e240a23baae123ca6cf165") == null) {
            return 0;
        }
        long nativeCheckLicense = nativeCheckLicense(this.a, this.b.a("cb072839e1e240a23baae123ca6cf165") + ":" + this.b.a("b62f7aea9613b98976498a9ecabe537b") + ":" + str);
        if (nativeCheckLicense == 0) {
            return nativeCheckLicense;
        }
        this.b.a("a01625815f3428cb69100cc5d613fa7d", nativeCheckLicense);
        this.b.a("e2380b201325a8f252636350338aeae8", this.b.a("b62f7aea9613b98976498a9ecabe537b") + ":" + str);
        this.b.a("bc8f6a70d138545889109d126886bd98", Detector.getVersion());
        return nativeCheckLicense;
    }

    public long checkCachedLicense() {
        String a = this.b.a("a01625815f3428cb69100cc5d613fa7d");
        if (a == null || !Detector.getVersion().equals(this.b.a("bc8f6a70d138545889109d126886bd98"))) {
            return 0;
        }
        try {
            long parseLong = Long.parseLong(a);
            if (System.currentTimeMillis() / 1000 <= parseLong) {
                return parseLong;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public String getVersion() {
        return Detector.getVersion();
    }

    static {
        try {
            System.loadLibrary("livenessdetection_v2.4.2");
        } catch (Exception e) {
        }
    }
}
