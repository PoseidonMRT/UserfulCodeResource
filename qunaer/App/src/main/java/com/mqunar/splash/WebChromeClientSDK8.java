package com.mqunar.splash;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.mqunar.core.basectx.webview.QWebChromeClient;

@TargetApi(8)
public class WebChromeClientSDK8 extends QWebChromeClient {
    private IWebViewObserver mCallback;

    public WebChromeClientSDK8() {
        this.mCallback = null;
    }

    public WebChromeClientSDK8(IWebViewObserver iWebViewObserver) {
        this.mCallback = iWebViewObserver;
    }

    public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
        callback.invoke(str, true, false);
    }

    public void onProgressChanged(WebView webView, int i) {
        if (this.mCallback != null) {
            this.mCallback.onProgressChanged(webView, i);
        } else {
            super.onProgressChanged(webView, i);
        }
    }

    public void onReceivedTitle(WebView webView, String str) {
        if (this.mCallback != null) {
            this.mCallback.onReceivedTitle(webView, str);
        } else {
            super.onReceivedTitle(webView, str);
        }
    }

    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        Builder builder = new Builder(webView.getContext());
        builder.setTitle("提示");
        builder.setMessage(str2);
        builder.setPositiveButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                jsResult.confirm();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                jsResult.cancel();
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
        return true;
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        Builder builder = new Builder(webView.getContext());
        builder.setTitle("提示");
        builder.setMessage(str2);
        builder.setPositiveButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                jsResult.confirm();
            }
        });
        builder.setNegativeButton(17039360, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                jsResult.cancel();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                jsResult.cancel();
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
        if (this.mCallback != null) {
            this.mCallback.openFileChooser(valueCallback, str, str2);
        }
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str) {
        if (this.mCallback != null) {
            this.mCallback.openFileChooser(valueCallback, str);
        }
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        if (this.mCallback != null) {
            this.mCallback.openFileChooser(valueCallback);
        }
    }
}
