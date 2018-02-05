package qunar.sdk;

import android.support.annotation.NonNull;

public interface PermissionsListener {
    void onRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr);

    void requestPermission(@NonNull String[] strArr, int i);
}
