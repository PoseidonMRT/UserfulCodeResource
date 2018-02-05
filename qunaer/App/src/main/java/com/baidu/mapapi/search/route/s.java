package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;

final class s implements Creator<WalkingStep> {
    s() {
    }

    public WalkingStep a(Parcel parcel) {
        return new WalkingStep(parcel);
    }

    public WalkingStep[] a(int i) {
        return new WalkingStep[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
