package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class l implements Creator<k> {
    l() {
    }

    public k a(Parcel parcel) {
        return new k(parcel);
    }

    public k[] a(int i) {
        return new k[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
