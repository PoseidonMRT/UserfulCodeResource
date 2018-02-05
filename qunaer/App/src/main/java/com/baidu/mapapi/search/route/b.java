package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.route.BikingRouteLine.BikingStep;

final class b implements Creator<BikingStep> {
    b() {
    }

    public BikingStep a(Parcel parcel) {
        return new BikingStep(parcel);
    }

    public BikingStep[] a(int i) {
        return new BikingStep[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
