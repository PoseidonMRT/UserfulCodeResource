package com.baidu.mapapi.search.sug;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class a implements Creator<SuggestionResult> {
    a() {
    }

    public SuggestionResult a(Parcel parcel) {
        return new SuggestionResult(parcel);
    }

    public SuggestionResult[] a(int i) {
        return new SuggestionResult[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
