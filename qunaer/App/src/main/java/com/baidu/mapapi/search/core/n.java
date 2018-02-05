package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class n implements Creator<TransitResultNode> {
    n() {
    }

    public TransitResultNode a(Parcel parcel) {
        return new TransitResultNode(parcel);
    }

    public TransitResultNode[] a(int i) {
        return new TransitResultNode[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
