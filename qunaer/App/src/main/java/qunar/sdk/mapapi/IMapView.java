package qunar.sdk.mapapi;

import android.os.Bundle;
import android.view.ViewGroup;

public interface IMapView {
    QunarMapInitOptions getInitOptions();

    ViewGroup getMapView();

    void onDestroy();

    void onPause();

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    void onStop();
}
