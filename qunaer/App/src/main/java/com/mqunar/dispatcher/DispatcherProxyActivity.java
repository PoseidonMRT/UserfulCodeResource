package com.mqunar.dispatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.mqunar.core.QSpider;
import com.mqunar.core.QunarApkLoader;

public class DispatcherProxyActivity extends Activity {
    private Handler a = new i(this, Looper.getMainLooper());

    private void a() {
        if (TextUtils.isEmpty(QSpider.crashTouchUrl)) {
            Intent intent = getIntent();
            Intent intent2 = new Intent(intent);
            intent2.setClassName(this, "com.mqunar.atom.im.activity.QImChooseSessionActivity");
            if ((intent.getFlags() & 33554432) == 33554432) {
                intent2.addFlags(33554432);
                finish();
                startActivity(intent2);
                return;
            }
            startActivityForResult(intent2, 888);
            return;
        }
        throw new RuntimeException("init fail!");
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        setResult(i2, intent);
        finish();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        QunarApkLoader.loadResourceWithoutBroadcast(this);
        requestWindowFeature(1);
        if (QSpider.loadDone) {
            a();
        } else {
            this.a.sendEmptyMessageDelayed(0, 500);
        }
    }

    protected void onDestroy() {
        this.a.removeMessages(0);
        super.onDestroy();
    }
}
