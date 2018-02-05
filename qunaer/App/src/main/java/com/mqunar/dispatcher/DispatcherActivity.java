package com.mqunar.dispatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ScrollView;
import com.Qunar.R;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.atomenv.SwitchEnv;
import com.mqunar.core.QSpider;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.basectx.SchemeDispatcher;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;

public class DispatcherActivity extends Activity {
    private Handler a = new a(this, Looper.getMainLooper());

    private void a() {
        if (TextUtils.isEmpty(QSpider.crashTouchUrl)) {
            Intent a = a(getIntent());
            Object dataString = a.getDataString();
            if (TextUtils.isEmpty(dataString)) {
                finish();
                return;
            } else if (dataString.endsWith("from=meizu_3dtouch")) {
                Storage newStorage = Storage.newStorage(this, OwnerConstant.STORAGE_OWNER_SYS);
                if (newStorage.getBoolean("isNotice", SwitchEnv.getInstance().isShowNetTips())) {
                    ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.spider_splash_dialog, null);
                    AlertDialog create = new Builder(this).setTitle("提示").setView(scrollView).setPositiveButton("退出", new c(this)).setNegativeButton(17039370, new b(this, newStorage, (CheckBox) scrollView.findViewById(R.id.spider_checkbox), a)).create();
                    create.setOnKeyListener(new d(this));
                    create.setCancelable(false);
                    create.setCanceledOnTouchOutside(false);
                    create.show();
                    return;
                }
                b(a);
                return;
            } else {
                b(a);
                return;
            }
        }
        throw new RuntimeException("init fail!");
    }

    private Intent a(Intent intent) {
        setIntent(intent);
        if (!"qn24bql60fz2ql".equals(getIntent().getScheme()) && !"qn4136854c1659".equals(getIntent().getScheme())) {
            return intent;
        }
        Uri parse = Uri.parse(getIntent().getDataString().replaceFirst(getIntent().getScheme(), GlobalEnv.getInstance().getScheme()));
        intent = new Intent(getIntent());
        intent.setData(parse);
        setIntent(intent);
        return intent;
    }

    private void b(Intent intent) {
        Intent intent2 = new Intent(intent);
        Uri data = intent2.getData();
        if (!(data == null || "scrimmage.qunar.com".equals(data.getHost()))) {
            intent2.removeExtra(SchemeDispatcher.SPIDER_REQUESTCODE);
            intent2.addFlags(33554432);
        }
        c(intent);
        DispatcherLogic.processIntent(this, intent2);
    }

    private void c(Intent intent) {
        try {
            Intent intent2 = new Intent("GO_INTENT_EVENT_FROM_" + getPackageName());
            intent2.putExtra(AIUIConstant.WORK_MODE_INTENT, intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
        } catch (Throwable th) {
            QLog.e(th);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        QunarApkLoader.loadResourceWithoutBroadcast(this);
        requestWindowFeature(1);
        if (bundle == null) {
            this.a.sendEmptyMessage(0);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        c(a(intent));
    }

    public void finish() {
        super.finish();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        setResult(i2, intent);
        finish();
    }

    protected void onDestroy() {
        this.a.removeMessages(0);
        super.onDestroy();
    }
}
