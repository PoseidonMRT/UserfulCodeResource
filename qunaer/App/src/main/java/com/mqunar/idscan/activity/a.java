package com.mqunar.idscan.activity;

import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.idscan.R;
import com.mqunar.idscan.a.c;
import com.mqunar.idscan.a.f;
import com.mqunar.idscan.decode.CaptureActivityHandler;
import com.mqunar.idscan.decode.d;
import com.mqunar.idscan.decode.e;
import com.mqunar.idscan.model.ScanPassportResultData;
import com.mqunar.idscan.view.ViewfinderView;
import com.mqunar.qav.uelog.QAVLog;
import com.mqunar.tools.log.QLog;
import java.io.IOException;

public abstract class a extends FragmentActivity implements Callback {
    private static final String d = a.class.getSimpleName();
    protected ViewfinderView a;
    protected SurfaceView b;
    protected boolean c = true;
    private boolean e;
    private c f;
    private SurfaceHolder g;
    private e h;
    private f i;
    private CaptureActivityHandler j;
    private Handler k;
    private final String l = "isSurfaceCreatedFirst";

    private void a(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        } else if (this.i.a()) {
            QLog.w(d, "initCamera() while already open -- late SurfaceView callback?", new Object[0]);
        } else {
            try {
                this.i.a(surfaceHolder);
                if (this.j == null) {
                    this.j = new CaptureActivityHandler(this, this.i);
                }
            } catch (IOException e) {
                QLog.w(d, e);
                f();
            } catch (RuntimeException e2) {
                QLog.w(d, "Unexpected error initializing camera", e2);
                f();
            }
        }
    }

    private void f() {
        Builder builder = new Builder(this);
        builder.setIcon(R.drawable.idscan_ic_launcher);
        builder.setTitle(getString(R.string.idscan_app_name));
        builder.setMessage(getString(R.string.idscan_camera_framework_bug));
        builder.setPositiveButton(getString(R.string.idscan_sure), new d(this));
        builder.setOnCancelListener(new d(this));
        builder.show();
    }

    public final void a() {
        this.a.a();
    }

    public final void a(ScanPassportResultData scanPassportResultData) {
        QAVLog.getInstance(getApplicationContext()).log("InterFlightScanPassport", "scan passport successed");
        this.f.b();
        b(scanPassportResultData);
    }

    public final ViewfinderView b() {
        return this.a;
    }

    public abstract void b(ScanPassportResultData scanPassportResultData);

    public final Handler c() {
        return this.j;
    }

    public final f d() {
        return this.i;
    }

    protected final void e() {
        if (this.j != null) {
            this.j.sendEmptyMessageDelayed(8, 0);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.k = new Handler();
        if (bundle != null) {
            this.c = bundle.getBoolean("isSurfaceCreatedFirst", true);
        }
        Window window = getWindow();
        window.addFlags(128);
        window.addFlags(1024);
        requestWindowFeature(1);
        this.e = false;
        this.h = new e(this);
        this.f = new c(this);
        this.i = new f(getApplication());
    }

    protected void onDestroy() {
        this.h.d();
        super.onDestroy();
        this.k.removeCallbacks(null);
    }

    protected void onPause() {
        QLog.d("onpause invoked............", new Object[0]);
        Log.d(AIUIConstant.KEY_TAG, "ffffffffffffffff");
        if (this.j != null) {
            this.j.a();
            this.j = null;
        }
        this.h.b();
        this.i.b();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.h.a();
        this.a.setCameraManager(this.i);
        this.g = this.b.getHolder();
        if (this.e) {
            a(this.g);
        } else {
            this.g.addCallback(this);
        }
        this.f.a();
        this.h.c();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("isSurfaceCreatedFirst", this.c);
        super.onSaveInstanceState(bundle);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            QLog.e(d, "*** WARNING *** surfaceCreated() gave us a null surface!", new Object[0]);
        }
        if (!this.e) {
            this.e = true;
            if (this.c) {
                this.k.postDelayed(new b(this, surfaceHolder), 100);
                this.c = false;
            } else {
                a(surfaceHolder);
            }
        }
        QAVLog.getInstance(getApplicationContext()).log("InterFlightScanPassport", "scan passport begin");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.e = false;
    }
}
