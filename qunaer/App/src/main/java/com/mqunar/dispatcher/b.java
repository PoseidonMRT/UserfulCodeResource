package com.mqunar.dispatcher;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.CheckBox;
import com.mqunar.storage.Storage;

class b implements OnClickListener {
    final /* synthetic */ Storage a;
    final /* synthetic */ CheckBox b;
    final /* synthetic */ Intent c;
    final /* synthetic */ DispatcherActivity d;

    b(DispatcherActivity dispatcherActivity, Storage storage, CheckBox checkBox, Intent intent) {
        this.d = dispatcherActivity;
        this.a = storage;
        this.b = checkBox;
        this.c = intent;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.putBoolean("isNotice", !this.b.isChecked());
        dialogInterface.dismiss();
        this.d.b(this.c);
    }
}
