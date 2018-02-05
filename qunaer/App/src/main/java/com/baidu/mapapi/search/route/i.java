package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class i implements Creator<MassTransitRouteLine> {
    i() {
    }

    public MassTransitRouteLine a(Parcel parcel) {
        return new MassTransitRouteLine(parcel);
    }

    public MassTransitRouteLine[] a(int i) {
        return new MassTransitRouteLine[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
