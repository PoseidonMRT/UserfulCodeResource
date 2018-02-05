package com.mqunar.contacts.basis.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.mqunar.contacts.R;

public class MainActivity extends Activity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
