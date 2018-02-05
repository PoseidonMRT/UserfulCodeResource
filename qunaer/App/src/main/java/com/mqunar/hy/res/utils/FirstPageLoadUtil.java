package com.mqunar.hy.res.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.Listener.QpDownloadListener;
import com.mqunar.hy.res.ManualDownloadControler;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirstPageLoadUtil {
    private static int OVER_TIME = 5000;
    private Handler handler;

    public interface CallBack {
        void end();
    }

    class FirstPageLoadUtilHolder {
        private static final FirstPageLoadUtil INSTANCE = new FirstPageLoadUtil();

        private FirstPageLoadUtilHolder() {
        }
    }

    class QpDownloadImpl implements QpDownloadListener {
        private CallBack callBack = null;
        private Handler handler = null;
        private AtomicBoolean hasCallback = new AtomicBoolean(false);
        private String hybridid = null;
        private ManualDownloadControler manualDownloadControler = null;
        Runnable runnable = new Runnable() {
            public void run() {
                QpDownloadImpl.this.manualDownloadControler.unregister();
                QpDownloadImpl.this.callEndOnMain(QpDownloadImpl.this.callBack);
                Timber.i("hyres FirstPage 请求超时 hybridid = " + QpDownloadImpl.this.hybridid, new Object[0]);
                HybridInfo hybridInfo = UpgradeInfoCache.getInstance().getHybridInfo(QpDownloadImpl.this.hybridid);
                StatisticsUtil.qpForceDownloadTimeout(QpDownloadImpl.this.hybridid, hybridInfo != null ? hybridInfo.version + "" : "0");
            }
        };

        public QpDownloadImpl(ManualDownloadControler manualDownloadControler, String str, CallBack callBack, Handler handler) {
            this.hybridid = str;
            this.callBack = callBack;
            this.handler = handler;
            this.manualDownloadControler = manualDownloadControler;
            handler.postDelayed(this.runnable, (long) FirstPageLoadUtil.OVER_TIME);
        }

        public void requestResultNoQp() {
            Timber.i("hyres FirstPage 没有qp包 hybridid = " + this.hybridid, new Object[0]);
            this.handler.removeCallbacks(this.runnable);
            this.manualDownloadControler.unregister();
            callEndOnMain(this.callBack);
        }

        public void requestResultHasQp() {
            Timber.i("hyres FirstPage 有qp包 hybridid = " + this.hybridid, new Object[0]);
            this.handler.removeCallbacks(this.runnable);
            this.manualDownloadControler.startDownload();
        }

        public void onMessageError() {
            this.handler.removeCallbacks(this.runnable);
            this.manualDownloadControler.unregister();
            callEndOnMain(this.callBack);
            Timber.i("hyres FirstPage 请求失败 hybridid = " + this.hybridid, new Object[0]);
        }

        public void onQpDownLoaded() {
            this.handler.removeCallbacks(this.runnable);
            this.manualDownloadControler.unregister();
            callEndOnMain(this.callBack);
            Timber.i("hyres FirstPage 下载成功 hybridid = " + this.hybridid, new Object[0]);
        }

        private void callEndOnMain(final CallBack callBack) {
            if (!this.hasCallback.get()) {
                this.hasCallback.set(true);
                this.handler.post(new Runnable() {
                    public void run() {
                        callBack.end();
                    }
                });
            }
        }
    }

    private FirstPageLoadUtil() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public static FirstPageLoadUtil getInstance() {
        return FirstPageLoadUtilHolder.INSTANCE;
    }

    public void start(String str, CallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException("CallBack is null");
        } else if (TextUtils.isEmpty(str)) {
            callEndOnMain(callBack);
        } else if (HybridManager.getInstance().getHybridInfoById(str) == null) {
            Timber.i("hyres FirstPage 没有qp包，进行单个请求 hybridid = " + str, new Object[0]);
            r0 = new ManualDownloadControler();
            r0.startUpdateRequest(str, new QpDownloadImpl(r0, str, callBack, this.handler));
        } else if (HybridManager.getInstance().isForceUpgrade(str)) {
            Timber.i("hyres FirstPage 进行强制更新 hybridid = " + str, new Object[0]);
            r0 = new ManualDownloadControler();
            r0.startUpdateRequest(str, new QpDownloadImpl(r0, str, callBack, this.handler));
        } else {
            callEndOnMain(callBack);
            Timber.i("hyres FirstPage 直接开启页面 hybridid = " + str, new Object[0]);
        }
    }

    private void callEndOnMain(final CallBack callBack) {
        this.handler.post(new Runnable() {
            public void run() {
                callBack.end();
            }
        });
    }
}
