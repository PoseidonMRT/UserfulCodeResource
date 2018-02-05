package com.huawei.hwid.openapi.auth.dump;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

class a implements AccountManagerCallback {
    final /* synthetic */ OpenDumpActivity a;
    private String b;
    private String c;

    public a(OpenDumpActivity openDumpActivity, String str, String str2) {
        this.a = openDumpActivity;
        this.c = str;
        this.b = str2;
    }

    public void run(AccountManagerFuture accountManagerFuture) {
        try {
            Bundle bundle = (Bundle) accountManagerFuture.getResult();
            this.a.a(null);
            Intent intent = new Intent();
            intent.setAction("com.huawei.cloudserive.getSTSuccess");
            Bundle bundle2 = new Bundle();
            int i = bundle.getInt("siteId", 0);
            String string = bundle.getString("userId");
            bundle2.putString("authToken", this.b);
            bundle2.putInt("siteId", i);
            bundle2.putString("userId", string);
            bundle2.putString("loginUserName", this.a.c);
            intent.putExtra("bundle", bundle2);
            d.b("OpenDumpActivity", "getPackageName:" + this.a.getPackageName());
            c.a("OpenDumpActivity", "mAccountName isEmpty " + TextUtils.isEmpty(this.c));
            com.huawei.hwid.openapi.e.c.a(this.a).a(this.a, intent);
        } catch (Throwable e) {
            c.b("OpenDumpActivity", "OperationCanceledException / " + e.getMessage(), e);
        } catch (Throwable e2) {
            c.b("OpenDumpActivity", "AuthenticatorException / " + e2.getMessage(), e2);
        } catch (Throwable e22) {
            c.b("OpenDumpActivity", "IOException / " + e22.getMessage(), e22);
        } finally {
            this.a.finish();
        }
    }
}
