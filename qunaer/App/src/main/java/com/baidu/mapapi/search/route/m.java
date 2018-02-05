package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class m implements Creator<PlanNode> {
    m() {
    }

    public PlanNode a(Parcel parcel) {
        return new PlanNode(parcel);
    }

    public PlanNode[] a(int i) {
        return new PlanNode[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
