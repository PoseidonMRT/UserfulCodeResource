package com.huawei.hwid.openapi.quicklogin.c.a;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.huawei.hwid.openapi.quicklogin.d.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.text.NumberFormat;

public class a extends Dialog {
    private ImageView a;
    private TextView b;
    private NumberFormat c;
    private CharSequence d;
    private Context e;
    private Handler f = new Handler();

    public a(Context context) {
        super(context, b.e(context, "Theme.quicklogin"));
        setCancelable(false);
        a();
        this.e = context;
    }

    private void a() {
        this.c = NumberFormat.getPercentInstance();
        this.c.setMaximumFractionDigits(0);
    }

    public void dismiss() {
        try {
            super.dismiss();
        } catch (Throwable e) {
            d.b("MyProgressDialog", e.getMessage(), e);
        }
    }

    public void show() {
        try {
            super.show();
            b();
        } catch (Throwable e) {
            d.b("MyProgressDialog", e.getMessage(), e);
        }
    }

    protected void onCreate(Bundle bundle) {
        setContentView(b.c(this.e, "xh_progress_dialog"));
        this.a = (ImageView) findViewById(b.d(this.e, "progress"));
        this.b = (TextView) findViewById(b.d(this.e, "message"));
        if (this.d != null) {
            a(this.d);
        }
        super.onCreate(bundle);
    }

    public void a(CharSequence charSequence) {
        if (this.a != null) {
            this.b.setText(charSequence);
        } else {
            this.d = charSequence;
        }
    }

    public void onBackPressed() {
        d.a("MyProgressDialog", "onBackPressed");
    }

    private void b() {
        Animation rotateAnimation = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(750);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.a.startAnimation(rotateAnimation);
    }
}
