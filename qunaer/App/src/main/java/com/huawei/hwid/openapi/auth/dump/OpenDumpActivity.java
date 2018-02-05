package com.huawei.hwid.openapi.auth.dump;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.quicklogin.datatype.e;

public class OpenDumpActivity extends Activity {
    private final int a = 1;
    private boolean b = false;
    private String c = "";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        Intent intent = getIntent();
        Bundle bundle2 = new Bundle();
        if (intent == null) {
            d.e("OpenDumpActivity", "we got a wrong intent");
            return;
        }
        a();
        if (intent.getBooleanExtra("FULLSCREEN", false)) {
            getWindow().setFlags(1024, 1024);
        }
        String stringExtra = intent.getStringExtra("tokenType");
        if (TextUtils.isEmpty(stringExtra)) {
            d.b("OpenDumpActivity", "params invalid: tokenType is null");
            return;
        }
        d.e("OpenDumpActivity", "TokenType =" + stringExtra);
        if (h.a(this, "com.huawei.hwid.GET_AUTH_TOKEN")) {
            d.e("OpenDumpActivity", "have the Access to HwID");
            bundle2.putString("ServiceType", stringExtra);
            boolean booleanExtra = intent.getBooleanExtra("chooseAccount", false);
            d.b("OpenDumpActivity", "chooseAccount =" + booleanExtra);
            bundle2.putBoolean("chooseAccount", booleanExtra);
            bundle2.putInt("scope", 1);
            bundle2.putInt("sdkType", 1);
            booleanExtra = intent.getBooleanExtra("needAuth", false);
            d.b("OpenDumpActivity", "needAuth =" + booleanExtra);
            bundle2.putBoolean("needAuth", booleanExtra);
            bundle2.putInt("loginChannel", intent.getIntExtra("loginChannel", 90000100));
            intent = new Intent("com.huawei.hwid.GET_AUTH_TOKEN");
            intent.putExtras(bundle2);
            intent.setPackage("com.huawei.hwid");
            c.b("OpenDumpActivity", k.b(intent.toURI()));
            try {
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                Bundle creatReturn = OutReturn.creatReturn(100, "getAuthTokenByFeatures : " + e + " occur");
                Intent intent2 = new Intent();
                intent2.setAction("com.huawei.cloudserive.getSTCancel");
                intent2.putExtra("bundle", creatReturn);
                d.b("OpenDumpActivity", "getPackageName:" + getPackageName());
                com.huawei.hwid.openapi.e.c.a((Context) this).b(this, intent2);
            }
            this.b = a(getApplicationContext());
            com.huawei.hwid.openapi.quicklogin.d.a.d.a().a(getApplicationContext(), new e(getApplicationContext(), this.b ? "105" : "101"));
            return;
        }
        d.e("OpenDumpActivity", "did not have the Access to HwID");
    }

    private static boolean a(Context context) {
        return b.c(context);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        d.e("OpenDumpActivity", "onActivityResult::requestCode==>" + i);
        if (i == 1) {
            Bundle creatReturn;
            String str;
            Object obj;
            Bundle extras;
            d.e("OpenDumpActivity", "onActivityResult::resultCode==>" + i2);
            if (i2 != -1) {
                creatReturn = OutReturn.creatReturn(2, "getAuthTokenByFeatures : OperationCanceledException occur");
                d.a("OpenDumpActivity", "AuthTokenCallBack OperationCanceledException");
                str = null;
                obj = null;
            } else if (intent == null) {
                d.d("OpenDumpActivity", "data == null");
                return;
            } else {
                String str2;
                String str3;
                extras = intent.getExtras();
                if (extras != null) {
                    str2 = (String) extras.get("Exception");
                } else {
                    str2 = null;
                }
                d.a("OpenDumpActivity", k.a(intent));
                if (TextUtils.isEmpty(str2)) {
                    str2 = (String) extras.get("authAccount");
                    String str4 = (String) extras.get("accountType");
                    str3 = (String) extras.get("authtoken");
                    this.c = extras.getString("loginUserName");
                    d.e("OpenDumpActivity", "AuthTokenCallBack: accountName=" + k.a(str2) + " accountType=" + str4 + "authToken" + k.a(str3));
                    creatReturn = null;
                } else {
                    str3 = null;
                    creatReturn = a(str2);
                    str2 = null;
                }
                str = str2;
                obj = str3;
            }
            if (creatReturn != null) {
                d.a("OpenDumpActivity", "get authToken ERROR");
                a(creatReturn);
                Intent intent2 = new Intent();
                intent2.setAction("com.huawei.cloudserive.getSTCancel");
                intent2.putExtra("bundle", creatReturn);
                d.b("OpenDumpActivity", "getPackageName:" + getPackageName());
                com.huawei.hwid.openapi.e.c.a((Context) this).b(this, intent2);
            } else if (!TextUtils.isEmpty(obj)) {
                d.a("OpenDumpActivity", "get authToken OK");
                Account account = new Account(str, "com.huawei.hwid");
                AccountManager accountManager = AccountManager.get(this);
                extras = new Bundle();
                extras.putBoolean("getUserId", true);
                accountManager.updateCredentials(account, getPackageName(), extras, this, new a(this, str, obj), null);
                return;
            }
            finish();
        }
    }

    private Bundle a(String str) {
        Bundle creatReturn;
        if ("AuthenticatorException".equals(str)) {
            creatReturn = OutReturn.creatReturn(100, "getAuthTokenByFeatures : AuthenticatorException occur");
            d.c("OpenDumpActivity", "AuthTokenCallBack AuthenticatorException:");
            return creatReturn;
        } else if ("IOException".equals(str) || "AccessException".equals(str) || "HwIDNotAllowException".equals(str)) {
            creatReturn = OutReturn.creatReturn(100, "getAuthTokenByFeatures : " + str + " occur");
            d.c("OpenDumpActivity", "AuthTokenCallBack IOException");
            return creatReturn;
        } else {
            creatReturn = OutReturn.creatReturn(2, "getAuthTokenByFeatures : OperationCanceledException occur");
            d.a("OpenDumpActivity", "AuthTokenCallBack OperationCanceledException");
            return creatReturn;
        }
    }

    private void a(Bundle bundle) {
        if (this.b) {
            e eVar = new e(getApplicationContext(), "105");
            eVar.a(System.currentTimeMillis());
            if (bundle == null) {
                eVar.b("");
                eVar.a("no_user");
                eVar.c("success");
            } else {
                eVar.b(String.valueOf(OutReturn.getRetCode(bundle)));
                eVar.a("no_user");
                eVar.c(bundle.getString(ParamStr.Err_Info));
            }
            com.huawei.hwid.openapi.quicklogin.d.a.d.a().b(getApplicationContext(), eVar);
        }
    }

    private void a() {
        Window window = getWindow();
        window.setFlags(67108864, 67108864);
        if (VERSION.SDK_INT >= 19) {
            window.setFlags(134217728, 134217728);
        }
        if (a((Activity) this, Boolean.valueOf(true))) {
            a((Activity) this, true);
        }
    }

    private boolean a(Activity activity, Boolean bool) {
        try {
            Window window = activity.getWindow();
            c.b("OpenDumpActivity", "setHwFloating");
            return ((Boolean) window.getClass().getMethod("setHwFloating", new Class[]{Boolean.TYPE}).invoke(window, new Object[]{bool})).booleanValue();
        } catch (RuntimeException e) {
            c.d("OpenDumpActivity", e.getMessage());
            return false;
        } catch (Exception e2) {
            c.d("OpenDumpActivity", e2.getMessage());
            return false;
        }
    }

    private void a(Activity activity, boolean z) {
        if (VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();
            LayoutParams attributes = window.getAttributes();
            if (z) {
                attributes.flags |= 67108864;
            } else {
                attributes.flags &= -67108865;
            }
            window.setAttributes(attributes);
        }
    }
}
