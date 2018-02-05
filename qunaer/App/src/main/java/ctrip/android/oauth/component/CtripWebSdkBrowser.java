package ctrip.android.oauth.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ctrip.android.oauth.R;
import ctrip.android.oauth.RequestProcessor;
import ctrip.android.oauth.RequestProcessor.Request;
import ctrip.android.oauth.utils.DeviceUtils;
import ctrip.android.oauth.utils.LogUtils;
import org.apache.http.HttpHost;

public class CtripWebSdkBrowser extends Activity implements CtripWebBrowserRequestCallBack {
    private static final String TAG = CtripWebSdkBrowser.class.getName();
    private static final int TITLE_BAR_CONTAINER_ID = 1;
    private static final int TITLE_BAR_HEIGHT = 50;
    private boolean isErrorPage;
    private boolean isLoading;
    private CtripWebViewClient mCtripWebViewClient;
    private String mHtmlTitle;
    private Button mLoadErrorRetryBtn;
    private LinearLayout mLoadErrorView;
    private CtripBrowserRequestParamBase mRequestParam;
    private TextView mTitleText;
    private String mUrl;
    private WebView mWebView;

    class CtripChromeClient extends WebChromeClient {
        private CtripChromeClient() {
        }

        public void onProgressChanged(WebView webView, int i) {
            if (i == 100) {
                CtripWebSdkBrowser.this.isLoading = false;
                CtripWebSdkBrowser.this.refreshAllViews();
            } else if (!CtripWebSdkBrowser.this.isLoading) {
                CtripWebSdkBrowser.this.isLoading = true;
                CtripWebSdkBrowser.this.refreshAllViews();
            }
        }

        public void onReceivedTitle(WebView webView, String str) {
            CtripWebSdkBrowser.this.mHtmlTitle = str;
            CtripWebSdkBrowser.this.updateTitleName();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (initDataFromIntent(getIntent())) {
            requestWindowFeature(1);
            setContentView();
            initWebView();
            openUrl(this.mUrl);
            return;
        }
        finish();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyUp(i, keyEvent);
        }
        if (this.mRequestParam != null) {
            this.mRequestParam.execRequest(this, -2);
            goBack();
        }
        finish();
        return true;
    }

    public void onPageStartedCallBack(WebView webView, String str, Bitmap bitmap) {
        LogUtils.d(TAG, "onPageStarted URL: " + str);
        this.mUrl = str;
    }

    public boolean shouldOverrideUrlLoadingCallBack(WebView webView, String str) {
        LogUtils.i(TAG, "shouldOverrideUrlLoading URL: " + str);
        return false;
    }

    public void onPageFinishedCallBack(WebView webView, String str) {
        LogUtils.d(TAG, "onPageFinished URL: " + str);
        if (this.isErrorPage) {
            promptError();
            return;
        }
        this.isErrorPage = false;
        hiddenErrorPrompt();
    }

    public void onReceivedErrorCallBack(WebView webView, int i, String str, String str2) {
        LogUtils.d(TAG, "onReceivedError: errorCode = " + i + ", description = " + str + ", failingUrl = " + str2);
        handleReceivedError(webView, i, str, str2);
    }

    public void onReceivedSslErrorCallBack(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
        Builder builder = new Builder(this);
        builder.setMessage("访问的网站存在安全问题");
        builder.setPositiveButton("继续", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                sslErrorHandler.proceed();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                sslErrorHandler.cancel();
            }
        });
        builder.create().show();
        LogUtils.d(TAG, "onReceivedSslErrorCallBack.........");
    }

    private CtripBrowserRequestParamBase createBrowserRequestParam(Bundle bundle) {
        CtripBrowserRequestParamBase ctripAuthRequestParam = new CtripAuthRequestParam(this);
        ctripAuthRequestParam.setupRequestParam(bundle);
        installAuthWebViewClient(ctripAuthRequestParam);
        return ctripAuthRequestParam;
    }

    private void installAuthWebViewClient(CtripAuthRequestParam ctripAuthRequestParam) {
        this.mCtripWebViewClient = new CtripAuthWebViewClient(this, ctripAuthRequestParam);
        this.mCtripWebViewClient.setBrowserRequestCallBack(this);
    }

    private void setContentView() {
        View relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(-1);
        View linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        linearLayout.setId(1);
        linearLayout.setLayoutParams(new LayoutParams(-1, DeviceUtils.getPixelFromDip(getResources().getDisplayMetrics(), 50.0f)));
        RelativeLayout relativeLayout2 = (RelativeLayout) getLayoutInflater().inflate(R.layout.common_topbar_layout, null);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        this.mTitleText = (TextView) relativeLayout2.findViewById(R.id.common_titleview_text);
        relativeLayout2.findViewById(R.id.common_titleview_btn_left).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CtripWebSdkBrowser.this.goBack();
            }
        });
        relativeLayout2.setLayoutParams(layoutParams);
        linearLayout.addView(relativeLayout2);
        this.mWebView = new WebView(this);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.setBackgroundColor(-1);
        LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.addRule(3, 1);
        this.mWebView.setLayoutParams(layoutParams2);
        initLoadingErrorView();
        relativeLayout.addView(linearLayout);
        relativeLayout.addView(this.mWebView);
        relativeLayout.addView(this.mLoadErrorView);
        setContentView(relativeLayout);
    }

    private void initLoadingErrorView() {
        this.mLoadErrorView = new LinearLayout(this);
        this.mLoadErrorView.setVisibility(8);
        this.mLoadErrorView.setOrientation(1);
        this.mLoadErrorView.setGravity(17);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(3, 1);
        this.mLoadErrorView.setLayoutParams(layoutParams);
        View imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.common_network_unstable);
        LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        int pixelFromDip = DeviceUtils.getPixelFromDip(getResources().getDisplayMetrics(), 8.0f);
        layoutParams2.bottomMargin = pixelFromDip;
        layoutParams2.rightMargin = pixelFromDip;
        layoutParams2.topMargin = pixelFromDip;
        layoutParams2.leftMargin = pixelFromDip;
        imageView.setLayoutParams(layoutParams2);
        this.mLoadErrorView.addView(imageView);
        imageView = new TextView(this);
        imageView.setGravity(1);
        imageView.setTextColor(Color.parseColor("#666666"));
        imageView.setTextSize(2, 14.0f);
        imageView.setText("网络出错啦，请点击按钮重新加载");
        imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.mLoadErrorView.addView(imageView);
        this.mLoadErrorRetryBtn = new Button(this);
        this.mLoadErrorRetryBtn.setGravity(17);
        this.mLoadErrorRetryBtn.setTextColor(-1);
        this.mLoadErrorRetryBtn.setTextSize(2, 16.0f);
        this.mLoadErrorRetryBtn.setText("重新加载");
        layoutParams = new LinearLayout.LayoutParams(DeviceUtils.getPixelFromDip(getResources().getDisplayMetrics(), 142.0f), DeviceUtils.getPixelFromDip(getResources().getDisplayMetrics(), 46.0f));
        layoutParams.topMargin = DeviceUtils.getPixelFromDip(getResources().getDisplayMetrics(), 30.0f);
        this.mLoadErrorRetryBtn.setLayoutParams(layoutParams);
        this.mLoadErrorRetryBtn.setBackgroundResource(R.drawable.common_btn_search_selector);
        this.mLoadErrorRetryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CtripWebSdkBrowser.this.openUrl(CtripWebSdkBrowser.this.mUrl);
                CtripWebSdkBrowser.this.isErrorPage = false;
            }
        });
        this.mLoadErrorView.addView(this.mLoadErrorRetryBtn);
    }

    private boolean initDataFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        this.mRequestParam = createBrowserRequestParam(extras);
        if (this.mRequestParam != null) {
            this.mUrl = this.mRequestParam.getUrl();
        } else {
            String string = extras.getString("key_url");
            if (!TextUtils.isEmpty(string) && string.startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
                this.mUrl = string;
            }
        }
        if (TextUtils.isEmpty(this.mUrl)) {
            return false;
        }
        LogUtils.d(TAG, "LOAD URL : " + this.mUrl);
        return true;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setSavePassword(false);
        this.mWebView.setWebViewClient(this.mCtripWebViewClient);
        this.mWebView.setWebChromeClient(new CtripChromeClient());
        this.mWebView.requestFocus();
        this.mWebView.setScrollBarStyle(0);
        if (VERSION.SDK_INT >= 11) {
            this.mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        } else {
            removeJavascriptInterface(this.mWebView);
        }
    }

    private void openUrl(String str) {
        this.mWebView.loadUrl(str);
    }

    protected void refreshAllViews() {
        if (this.isLoading) {
            setViewLoading();
        } else {
            setViewNormal();
        }
    }

    private void setViewNormal() {
        updateTitleName();
    }

    private void setViewLoading() {
        this.mTitleText.setText("加载中....");
    }

    private void handleReceivedError(WebView webView, int i, String str, String str2) {
        this.isErrorPage = true;
        promptError();
    }

    private void promptError() {
        this.mLoadErrorView.setVisibility(0);
        this.mTitleText.setText("携程授权页");
        this.mWebView.setVisibility(8);
    }

    private void hiddenErrorPrompt() {
        this.mLoadErrorView.setVisibility(8);
        this.mWebView.setVisibility(0);
    }

    private void updateTitleName() {
        CharSequence charSequence = "";
        if (!TextUtils.isEmpty(this.mHtmlTitle)) {
            charSequence = this.mHtmlTitle;
        }
        this.mTitleText.setText(charSequence);
    }

    private void goBack() {
        Request request = new Request();
        Bundle bundle = new Bundle();
        bundle.putString("_ctripapi_sendauth_response_state", "");
        bundle.putString("_ctripapi_sendauth_response_code", "");
        bundle.putInt("_ctripapi_baseresponse_errcode", -2);
        bundle.putString("_ctripapi_baseresponse_errstr", "");
        if (this.mRequestParam instanceof CtripAuthRequestParam) {
            CtripAuthRequestParam ctripAuthRequestParam = (CtripAuthRequestParam) this.mRequestParam;
            bundle.putInt("_ctripapi_command_type", ctripAuthRequestParam.getAuthRequest().getType());
            request.targetPackageName = ctripAuthRequestParam.getPackageName();
            request.targetClassName = ctripAuthRequestParam.getPackageName() + ".ctripapi.CtripEntryActivity";
        }
        request.bundle = bundle;
        request.messageContent = "";
        request.flags = 67108864;
        RequestProcessor.startActivity(this, request);
        finish();
    }

    public void removeJavascriptInterface(WebView webView) {
        if (VERSION.SDK_INT < 11) {
            try {
                webView.getClass().getDeclaredMethod("removeJavascriptInterface", new Class[0]).invoke("searchBoxJavaBridge_", new Object[0]);
            } catch (Exception e) {
                LogUtils.e(TAG, e.toString());
            }
        }
    }
}
