package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class m implements Creator<TransitBaseInfo> {
    m() {
    }

    public TransitBaseInfo a(Parcel parcel) {
        return new TransitBaseInfo(parcel);
    }

    public TransitBaseInfo[] a(int i) {
        return new TransitBaseInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
