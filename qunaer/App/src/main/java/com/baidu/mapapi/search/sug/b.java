package com.baidu.mapapi.search.sug;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;

final class b implements Creator<SuggestionInfo> {
    b() {
    }

    public SuggestionInfo a(Parcel parcel) {
        return new SuggestionInfo(parcel);
    }

    public SuggestionInfo[] a(int i) {
        return new SuggestionInfo[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}
