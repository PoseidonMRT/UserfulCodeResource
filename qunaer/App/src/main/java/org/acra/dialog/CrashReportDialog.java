package org.acra.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.acra.prefs.b;
import org.apache.http.cookie.ClientCookie;

public class CrashReportDialog extends a implements OnClickListener {
    private LinearLayout a;
    private EditText b;
    private EditText c;
    private b d;
    private AlertDialog e;

    @CallSuper
    protected void a(@Nullable Bundle bundle) {
        this.a = new LinearLayout(this);
        this.a.setOrientation(1);
        this.d = new b(getApplicationContext(), b());
        int resDialogTheme = b().resDialogTheme();
        if (resDialogTheme != 0) {
            setTheme(resDialogTheme);
        }
        b(bundle);
    }

    protected void b(@Nullable Bundle bundle) {
        Builder builder = new Builder(this);
        int resDialogTitle = b().resDialogTitle();
        if (resDialogTitle != 0) {
            builder.setTitle(resDialogTitle);
        }
        resDialogTitle = b().resDialogIcon();
        if (resDialogTitle != 0) {
            builder.setIcon(resDialogTitle);
        }
        builder.setView(c(bundle)).setPositiveButton(getText(b().resDialogPositiveButtonText()), this).setNegativeButton(getText(b().resDialogNegativeButtonText()), this);
        this.e = builder.create();
        this.e.setCanceledOnTouchOutside(false);
        this.e.show();
    }

    @NonNull
    protected View c(@Nullable Bundle bundle) {
        CharSequence charSequence = null;
        View scrollView = new ScrollView(this);
        scrollView.setPadding(10, 10, 10, 10);
        scrollView.setLayoutParams(new LayoutParams(-1, -1));
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.addView(this.a);
        a(c());
        View d = d();
        if (d != null) {
            CharSequence string;
            d.setPadding(d.getPaddingLeft(), 10, d.getPaddingRight(), d.getPaddingBottom());
            a(d);
            if (bundle != null) {
                string = bundle.getString(ClientCookie.COMMENT_ATTR);
            } else {
                string = null;
            }
            this.b = a(string);
            a(this.b);
        }
        d = e();
        if (d != null) {
            d.setPadding(d.getPaddingLeft(), 10, d.getPaddingRight(), d.getPaddingBottom());
            a(d);
            if (bundle != null) {
                charSequence = bundle.getString("email");
            }
            this.c = b(charSequence);
            a(this.c);
        }
        return scrollView;
    }

    protected final void a(@NonNull View view) {
        this.a.addView(view);
    }

    @NonNull
    protected View c() {
        View textView = new TextView(this);
        int resDialogText = b().resDialogText();
        if (resDialogText != 0) {
            textView.setText(getText(resDialogText));
        }
        return textView;
    }

    @Nullable
    protected View d() {
        int resDialogCommentPrompt = b().resDialogCommentPrompt();
        if (resDialogCommentPrompt == 0) {
            return null;
        }
        View textView = new TextView(this);
        textView.setText(getText(resDialogCommentPrompt));
        return textView;
    }

    @NonNull
    protected EditText a(@Nullable CharSequence charSequence) {
        EditText editText = new EditText(this);
        editText.setLines(2);
        if (charSequence != null) {
            editText.setText(charSequence);
        }
        return editText;
    }

    @Nullable
    protected View e() {
        int resDialogEmailPrompt = b().resDialogEmailPrompt();
        if (resDialogEmailPrompt == 0) {
            return null;
        }
        View textView = new TextView(this);
        textView.setText(getText(resDialogEmailPrompt));
        return textView;
    }

    @NonNull
    protected EditText b(@Nullable CharSequence charSequence) {
        EditText editText = new EditText(this);
        editText.setSingleLine();
        editText.setInputType(33);
        if (charSequence != null) {
            editText.setText(charSequence);
        } else {
            editText.setText(this.d.a().getString("acra.user.email", ""));
        }
        return editText;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            String obj;
            String obj2 = this.b != null ? this.b.getText().toString() : "";
            SharedPreferences a = this.d.a();
            if (this.c != null) {
                obj = this.c.getText().toString();
                Editor edit = a.edit();
                edit.putString("acra.user.email", obj);
                edit.commit();
            } else {
                obj = a.getString("acra.user.email", "");
            }
            a(obj2, obj);
        } else {
            a();
        }
        finish();
    }

    @CallSuper
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (!(this.b == null || this.b.getText() == null)) {
            bundle.putString(ClientCookie.COMMENT_ATTR, this.b.getText().toString());
        }
        if (this.c != null && this.c.getText() != null) {
            bundle.putString("email", this.c.getText().toString());
        }
    }
}
