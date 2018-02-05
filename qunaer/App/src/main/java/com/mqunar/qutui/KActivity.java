package com.mqunar.qutui;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.mqunar.core.basectx.activity.QActivity;
import com.mqunar.tools.log.QLog;

public class KActivity extends QActivity {
    private final String a = "KActivity";
    private BroadcastReceiver b;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        QLog.d("KActivity", "start acitivity", new Object[0]);
        Window window = getWindow();
        window.setGravity(51);
        LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.height = 1;
        attributes.width = 1;
        window.setAttributes(attributes);
        this.b = new b(this);
        registerReceiver(this.b, new IntentFilter("finish activity"));
        a("onCreate");
    }

    private void a(String str) {
        QLog.d("KActivity", "-------from call method: " + str, new Object[0]);
        boolean isScreenOn = ((PowerManager) getSystemService("power")).isScreenOn();
        QLog.d("KActivity", "-------isScreenOn: " + isScreenOn, new Object[0]);
        if (isScreenOn) {
            finish();
        }
    }

    protected void onDestroy() {
        QLog.d("KActivity", "--------KActivity onDestroy", new Object[0]);
        try {
            unregisterReceiver(this.b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        a("onResume");
    }
}
