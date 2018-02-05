package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class c implements Creator<CoachInfo> {
    c() {
    }

    public CoachInfo a(Parcel parcel) {
        return new CoachInfo(parcel);
    }

    public CoachInfo[] a(int i) {
        return new CoachInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
