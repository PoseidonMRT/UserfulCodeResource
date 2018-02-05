package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class g implements Creator<IndoorRouteLine> {
    g() {
    }

    public IndoorRouteLine a(Parcel parcel) {
        return new IndoorRouteLine(parcel);
    }

    public IndoorRouteLine[] a(int i) {
        return new IndoorRouteLine[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
