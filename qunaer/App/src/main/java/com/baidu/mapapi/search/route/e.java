package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;

final class e implements Creator<DrivingStep> {
    e() {
    }

    public DrivingStep a(Parcel parcel) {
        return new DrivingStep(parcel);
    }

    public DrivingStep[] a(int i) {
        return new DrivingStep[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
