package qunar.sdk.mapapi;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class SDKInitializer {
    private static Application context;
    private static SDKInitializer singleInstance = null;
    private boolean isbdMapInit = false;
    private SDKReceiver mReceiver;
    private QunarMapType mapType;

    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!action.equals(com.baidu.mapapi.SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR) && action.equals(com.baidu.mapapi.SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(context, "网络出错", 0).show();
            }
        }
    }

    private SDKInitializer(Application application) {
        if (this.mReceiver == null) {
            registerReceiver(application);
        }
    }

    public static SDKInitializer getSDKInitializer() {
        return singleInstance;
    }

    public QunarMapType getInitMapType() {
        return singleInstance.mapType;
    }

    protected void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.baidu.mapapi.SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        intentFilter.addAction(com.baidu.mapapi.SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        this.mReceiver = new SDKReceiver();
        context.registerReceiver(this.mReceiver, intentFilter);
    }

    public static void destroy() {
        if (singleInstance != null) {
            if (!(singleInstance.mReceiver == null || context == null)) {
                try {
                    context.unregisterReceiver(singleInstance.mReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    singleInstance.mReceiver = null;
                }
            }
            singleInstance = null;
        }
    }

    public static void initialize(Application application, QunarMapType qunarMapType, boolean z) {
        if (singleInstance == null) {
            singleInstance = new SDKInitializer(application);
        }
        context = application;
        if (singleInstance.mapType != qunarMapType) {
            singleInstance.mapType = qunarMapType;
            switch (qunarMapType) {
                case BAIDU:
                    if (!singleInstance.isbdMapInit) {
                        com.baidu.mapapi.SDKInitializer.initialize(application);
                        singleInstance.isbdMapInit = true;
                        return;
                    }
                    return;
                case GAODE:
                    return;
                default:
                    return;
            }
        }
    }

    @Deprecated
    public static void initialize(Application application, QunarMapType qunarMapType) {
        if (singleInstance == null) {
            singleInstance = new SDKInitializer(application);
        }
        context = application;
        if (singleInstance.mapType != qunarMapType) {
            QunarMapFacade.mapType = qunarMapType;
            singleInstance.mapType = qunarMapType;
            switch (qunarMapType) {
                case BAIDU:
                    com.baidu.mapapi.SDKInitializer.initialize(application);
                    return;
                case GAODE:
                    return;
                default:
                    return;
            }
        }
    }
}
