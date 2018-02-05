package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.route.MassTransitRouteLine.TransitStep.TrafficCondition;

final class k implements Creator<TrafficCondition> {
    k() {
    }

    public TrafficCondition a(Parcel parcel) {
        return new TrafficCondition(parcel);
    }

    public TrafficCondition[] a(int i) {
        return new TrafficCondition[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
