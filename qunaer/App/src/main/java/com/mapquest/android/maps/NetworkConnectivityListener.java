package com.mapquest.android.maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

final class NetworkConnectivityListener {
    private static final String LOG_TAG = NetworkConnectivityListener.class.getSimpleName();
    private static boolean lastKnownNetworkState = true;
    private Context context;
    private boolean listening;
    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();

    class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        private ConnectivityBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            int checkPermission = NetworkConnectivityListener.this.context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", NetworkConnectivityListener.this.context.getPackageName());
            if (checkPermission >= 0 || networkInfo == null) {
                if (checkPermission < 0) {
                    NetworkConnectivityListener.lastKnownNetworkState = true;
                    EventDispatcher.sendEmptyMessage(61);
                    return;
                }
                try {
                    if (((ConnectivityManager) NetworkConnectivityListener.this.context.getSystemService("connectivity")).getActiveNetworkInfo().isConnected()) {
                        NetworkConnectivityListener.lastKnownNetworkState = true;
                        EventDispatcher.sendEmptyMessage(61);
                        return;
                    }
                    NetworkConnectivityListener.lastKnownNetworkState = false;
                    EventDispatcher.sendEmptyMessage(62);
                } catch (Exception e) {
                }
            } else if (networkInfo.isConnected()) {
                NetworkConnectivityListener.lastKnownNetworkState = true;
                EventDispatcher.sendEmptyMessage(61);
            } else {
                NetworkConnectivityListener.lastKnownNetworkState = false;
                EventDispatcher.sendEmptyMessage(62);
            }
        }
    }

    NetworkConnectivityListener(Context context) {
        this.context = context;
    }

    public static boolean getLastKnownNetworkState() {
        return lastKnownNetworkState;
    }

    static void resetNetworkState(boolean z) {
        lastKnownNetworkState = z;
    }

    public void startListening() {
        if (!this.listening) {
            try {
                this.context.registerReceiver(this.receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.listening = true;
            } catch (Exception e) {
            }
        }
    }

    public void stopListening() {
        if (this.listening) {
            try {
                this.context.unregisterReceiver(this.receiver);
            } catch (Exception e) {
            }
            this.listening = false;
        }
    }
}
