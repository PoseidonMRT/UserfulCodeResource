package com.huawei.hwid.openapi.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import java.io.File;

class i implements Runnable {
    private String a = null;
    private Context b = null;

    i(Context context, String str) {
        this.b = context;
        this.a = str;
    }

    public void run() {
        if (this.b == null) {
            c.b("InstallProcessor", "mContext is null");
            return;
        }
        try {
            File file = new File(this.a);
            if (VERSION.SDK_INT <= 23 || !k.b(this.b)) {
                b(file);
            } else {
                a(file);
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            c.d("InstallProcessor", "Thread.sleep error");
        } catch (Exception e2) {
            c.d("InstallProcessor", "install exception: " + e2.getMessage());
        }
    }

    private void a(File file) {
        c.b("InstallProcessor", "installFromProvider");
        Intent data = new Intent("android.intent.action.INSTALL_PACKAGE").setData(OtaFileProvider.a(this.b, this.b.getPackageName() + ".hwid.sdk.otafileprovider", file));
        data.setFlags(1);
        this.b.startActivity(data);
    }

    private void b(File file) {
        c.b("InstallProcessor", "installFromFile");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        this.b.startActivity(intent);
    }
}
