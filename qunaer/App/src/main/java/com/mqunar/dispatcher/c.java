package com.mqunar.dispatcher;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class c implements OnClickListener {
    final /* synthetic */ DispatcherActivity a;

    c(DispatcherActivity dispatcherActivity) {
        this.a = dispatcherActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        this.a.finish();
    }
}
