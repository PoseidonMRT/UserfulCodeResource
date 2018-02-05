package qunar.sdk.mapapi;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import qunar.sdk.PermissionsListener;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;

public class QunarMapView extends LinearLayout implements PermissionsListener {
    private static final int REQUEST_CODE_MAP = 51;
    private Context context;
    private ViewGroup displayMap = null;
    private QunarMapInitOptions mapOptions;
    private IMapView mapView;
    private PermissionsListener permissionsListener;
    private QunarMap qunarMap = null;
    private QunarMapControl qunarMapControl = null;

    public QunarMapView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public QunarMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        QunarMapInitOptions qunarMapInitOptions = new QunarMapInitOptions();
        qunarMapInitOptions.compassEnabled = false;
        qunarMapInitOptions.zoomControlsEnabled = false;
        qunarMapInitOptions.scaleControlEnabled = false;
        SDKInitializer sDKInitializer = SDKInitializer.getSDKInitializer();
        if (sDKInitializer != null) {
            QunarMapType initMapType = sDKInitializer.getInitMapType();
            if (initMapType != null) {
                qunarMapInitOptions.mapType = initMapType;
            }
        }
        init(qunarMapInitOptions);
    }

    public QunarMapView(Context context, QunarMapInitOptions qunarMapInitOptions) {
        super(context);
        this.context = context;
        init(qunarMapInitOptions);
    }

    public void setPermissionsListener(PermissionsListener permissionsListener) {
        this.permissionsListener = permissionsListener;
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.READ_PHONE_STATE") != 0) {
            requestPermission(new String[]{"android.permission.READ_PHONE_STATE"}, 51);
        }
    }

    private void init(QunarMapInitOptions qunarMapInitOptions) {
        if (qunarMapInitOptions == null) {
            qunarMapInitOptions = new QunarMapInitOptions();
            qunarMapInitOptions.compassEnabled = false;
            qunarMapInitOptions.zoomControlsEnabled = false;
            qunarMapInitOptions.scaleControlEnabled = false;
            SDKInitializer sDKInitializer = SDKInitializer.getSDKInitializer();
            if (sDKInitializer != null) {
                QunarMapType initMapType = sDKInitializer.getInitMapType();
                if (initMapType != null) {
                    qunarMapInitOptions.mapType = initMapType;
                }
            }
        }
        this.mapOptions = qunarMapInitOptions;
        createViewWithPermission();
    }

    private void createViewWithPermission() {
        this.mapView = QunarMapFacade.initMapView(this.context, this.mapOptions);
        if (this.mapView != null) {
            LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
            this.displayMap = this.mapView.getMapView();
            this.displayMap.setClickable(true);
            this.displayMap.setEnabled(true);
            addView(this.displayMap, layoutParams);
        }
    }

    public QunarMap getQunarMap() {
        if (this.qunarMap == null) {
            this.qunarMap = QunarMapFacade.initMap(this.displayMap, getQunarMapType());
        }
        return this.qunarMap;
    }

    public QunarMapControl getQunarMapControl() {
        if (this.qunarMap == null) {
            this.qunarMap = QunarMapFacade.initMap(this.displayMap, getQunarMapType());
        }
        if (this.qunarMapControl == null) {
            this.qunarMapControl = QunarMapFacade.initMapControl(this);
        }
        return this.qunarMapControl;
    }

    public ViewGroup getDisplayMap() {
        return this.displayMap;
    }

    public void onDestroy() {
        if (this.qunarMap != null) {
            this.qunarMap.destroy(TEMPTOKEN.MAP_DESTROY_TOKEN);
        }
        if (this.qunarMapControl != null) {
            this.qunarMapControl.destroy(TEMPTOKEN.MAP_DESTROY_TOKEN);
        }
        if (this.mapView != null) {
            this.mapView.onDestroy();
        }
        this.qunarMapControl = null;
        this.qunarMap = null;
        this.displayMap = null;
    }

    public void onPause() {
        if (this.mapView != null) {
            this.mapView.onPause();
        }
    }

    public void onResume() {
        if (this.mapView != null) {
            this.mapView.onResume();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mapView != null) {
            this.mapView.onSaveInstanceState(bundle);
        }
    }

    public void onStop() {
        if (this.mapView != null) {
            this.mapView.onStop();
        }
    }

    public QunarMapType getQunarMapType() {
        return this.mapView == null ? null : this.mapView.getInitOptions().mapType;
    }

    public void requestPermission(@NonNull String[] strArr, int i) {
        if (this.permissionsListener != null && VERSION.SDK_INT >= 23) {
            this.permissionsListener.requestPermission(strArr, i);
        }
    }

    public void onRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 51) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            createViewWithPermission();
        } else if (this.context != null) {
            Toast.makeText(this.context, "读取设备信息失败，无法提供地图服务……", 0).show();
        }
    }
}
