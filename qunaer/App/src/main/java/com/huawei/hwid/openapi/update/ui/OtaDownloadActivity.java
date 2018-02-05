package com.huawei.hwid.openapi.update.ui;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b;
import com.huawei.hwid.openapi.quicklogin.d.d;
import com.huawei.hwid.openapi.update.b.f;
import com.huawei.hwid.openapi.update.e;
import com.huawei.hwid.openapi.update.h;
import com.huawei.hwid.openapi.update.j;
import com.huawei.hwid.openapi.update.k;
import java.io.File;
import java.util.Locale;

public class OtaDownloadActivity extends BaseActivity {
    TextView a;
    ProgressBar b;
    ImageView c;
    private b d;
    private b e;
    private b f;
    private int g = -1;
    private boolean h = false;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        c.b("OtaDownloadActivity", "onCreate");
        requestWindowFeature(1);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.h = extras.getBoolean("updateApk");
        }
        c();
    }

    private void c() {
        CharSequence string;
        this.d = new b(this);
        this.d.setCanceledOnTouchOutside(false);
        if (this.h) {
            this.d.setTitle(b.a(this, "CS_update_hwid"));
            this.d.setMessage(getString(b.a(this, "CS_update_old_hwid_notes")));
            string = getString(b.a(this, "CS_update"));
        } else {
            this.d.setTitle(b.a(this, "CS_install_hwid"));
            this.d.setMessage(getString(b.a(this, "CS_update_notes")));
            string = getString(b.a(this, "CS_install"));
        }
        this.d.setButton(-1, string, new d(this));
        this.d.setButton(-2, getString(17039360), new f(this));
        this.d.setOnKeyListener(new g(this));
        if (!isFinishing() && !this.d.isShowing()) {
            this.d.show();
        }
    }

    protected void onDestroy() {
        c.b("OtaDownloadActivity", "onDestroy");
        super.onDestroy();
        if (this.d != null) {
            this.d.a(true);
            this.d.dismiss();
            this.d = null;
        }
        if (this.e != null) {
            this.e.dismiss();
            this.e = null;
        }
        if (this.f != null) {
            this.f.dismiss();
            this.f = null;
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 10002) {
            return;
        }
        if (iArr == null || iArr.length <= 0 || iArr[0] != 0) {
            finish();
            return;
        }
        c.b("OtaDownloadActivity", "startCheckVersion");
        a(new n(this));
    }

    public void a(n nVar) {
        c.b("OtaDownloadActivity", "startCheckVersion");
        if (!com.huawei.hwid.openapi.quicklogin.d.a.b.a((Context) this)) {
            a(com.huawei.hwid.openapi.update.b.c.a((Context) this, b.a(this, "CS_network_connect_error"), b.a(this, "CS_server_unavailable_title"), false).show());
        } else if (e.a().b()) {
            c.b("OtaDownloadActivity", "OtaDownloadManager.getInstance().isMutiDownloading()");
        } else {
            c.b("OtaDownloadActivity", "mIsUpdateApk = " + this.h);
            if (this.h) {
                if (f.d(this).toUpperCase(Locale.ENGLISH).endsWith("_OVE")) {
                    this.g = 49846;
                } else {
                    this.g = 49827;
                }
            } else if (f.a() || "cn".equalsIgnoreCase(f.a(this)) || d.a((Context) this, -999).startsWith("460")) {
                this.g = 49827;
            } else {
                this.g = 49846;
            }
            a(null);
            e.a().a((Context) this, this.g, (Handler) nVar);
        }
    }

    public void a(o oVar) {
        c.b("OtaDownloadActivity", "entry startDownload");
        com.huawei.hwid.openapi.update.a.b b = e.a().b(this.g);
        if (b == null) {
            c.b("OtaDownloadActivity", "versionInfo == null");
            finish();
            return;
        }
        if (this.h) {
            Object a = b.a();
            if (!TextUtils.isEmpty(a)) {
                try {
                    if (f.e(this) >= Integer.valueOf(a).intValue()) {
                        c.b("OtaDownloadActivity", "local version is newest");
                        finish();
                        return;
                    }
                } catch (Exception e) {
                    c.b("versionCode", "e = " + e.getMessage());
                }
            }
        }
        if (k.a((Context) this, (long) b.c())) {
            c.b("OtaDownloadActivity", "start startDownloadVersion");
            e.a().a((Context) this, (Handler) oVar, this.g);
            return;
        }
        c.b("OtaDownloadActivity", "!OtaUtils.isEnoughSpaceToDown");
        Toast.makeText(this, getString(b.a(this, "CS_download_no_space")), 0).show();
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            View inflate;
            this.e = new b(this);
            this.e.setCanceledOnTouchOutside(false);
            if (f.b(this)) {
                inflate = View.inflate(this, b.c(this, "cs_download_progress_dialog_3"), null);
            } else {
                inflate = View.inflate(this, b.c(this, "cs_download_progress_dialog"), null);
            }
            this.a = (TextView) inflate.findViewById(b.d(this, "information"));
            this.b = (ProgressBar) inflate.findViewById(b.d(this, "progressbar"));
            this.c = (ImageView) inflate.findViewById(b.d(this, "cancel_download"));
            this.e.setView(inflate);
            this.c.setOnClickListener(new h(this));
            this.e.setOnKeyListener(new i(this));
        }
        if (!(isFinishing() || this.e.isShowing())) {
            this.d.a(true);
            this.d.dismiss();
            this.d = null;
            this.e.show();
        }
        b(i, i2);
    }

    private void d() {
        Dialog create = new Builder(this, com.huawei.hwid.openapi.update.b.c.a(this)).setMessage(b.a(this, "CS_update_stop")).setPositiveButton(b.a(this, "CS_terminate"), new j(this)).setNegativeButton(17039360, null).create();
        if (!isFinishing() && !create.isShowing()) {
            a(create);
            create.show();
        }
    }

    private void e() {
        this.f = new b(this);
        this.f.setMessage(getString(b.a(this, "CS_download_failed_notes")));
        this.f.setButton(-1, getString(b.a(this, "CS_retry")), new k(this));
        this.f.setButton(-2, getString(17039360), new l(this));
        this.f.setOnKeyListener(new m(this));
        if (!isFinishing() && !this.f.isShowing()) {
            this.f.show();
        }
    }

    private void b(int i, int i2) {
        int i3 = (i * 100) / i2;
        c.b("OtaDownloadActivity", "progress: " + i3);
        this.a.setText(getString(b.a(this, "CS_downloading_new"), new Object[]{String.valueOf(i3)}));
        this.b.setProgress(i3);
    }

    private void f() {
        this.e.dismiss();
        this.e = null;
    }

    private void g() {
        com.huawei.hwid.openapi.update.a.b b = e.a().b(this.g);
        if (b != null) {
            h.a().a(this, b.f());
        } else {
            c.b("OtaDownloadActivity", "versionInfo is null");
        }
    }

    private void h() {
        e.a().e();
        b(e.a().b(this.g));
        e.a().a(this.g);
        finish();
    }

    public boolean a(com.huawei.hwid.openapi.update.a.b bVar) {
        if (bVar == null) {
            return false;
        }
        String b = j.a(this).b(this);
        String c = j.a(this).c(this);
        if ("".equals(c)) {
            return false;
        }
        File file = new File(c);
        if (!file.exists()) {
            return false;
        }
        if (b.equals(bVar.b())) {
            h.a().a(this, c);
            return true;
        }
        try {
            if (file.delete()) {
                return false;
            }
            c.d("OtaDownloadActivity", "delete old apk error");
            return false;
        } catch (Exception e) {
            c.d("OtaDownloadActivity", "delete old apk error,error is " + e.getMessage());
            return false;
        }
    }

    public void b(com.huawei.hwid.openapi.update.a.b bVar) {
        if (bVar != null) {
            String c = j.a(this).c(this);
            if (!"".equals(c)) {
                File file = new File(c);
                if (file.exists()) {
                    try {
                        if (!file.delete()) {
                            c.d("OtaDownloadActivity", "delete uninstallApk error");
                        }
                    } catch (Exception e) {
                        c.d("OtaDownloadActivity", "delete uninstallApk error, error is " + e.getMessage());
                    }
                }
            }
        }
    }

    private void i() {
        a(com.huawei.hwid.openapi.update.b.c.a((Context) this, b.a(this, "CS_ERR_for_unable_get_data"), b.a(this, "CS_server_unavailable_title"), true).show());
    }

    private void j() {
        new Handler().postDelayed(new e(this), 200);
    }
}
