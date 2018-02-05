package com.baidu.mapapi.search.sug;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import java.util.ArrayList;
import java.util.List;

public class SuggestionResult extends SearchResult implements Parcelable {
    public static final Creator<SuggestionResult> CREATOR = new a();
    private ArrayList<SuggestionInfo> a;

    public class SuggestionInfo implements Parcelable {
        public static final Creator<SuggestionInfo> CREATOR = new b();
        public String city;
        public String district;
        public String key;
        public LatLng pt;
        public String uid;

        protected SuggestionInfo(Parcel parcel) {
            this.key = parcel.readString();
            this.city = parcel.readString();
            this.district = parcel.readString();
            this.pt = (LatLng) parcel.readValue(LatLng.class.getClassLoader());
            this.uid = parcel.readString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.key);
            parcel.writeString(this.city);
            parcel.writeString(this.district);
            parcel.writeValue(this.pt);
            parcel.writeString(this.uid);
        }
    }

    protected SuggestionResult(Parcel parcel) {
        this.a = parcel.readArrayList(SuggestionInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public List<SuggestionInfo> getAllSuggestions() {
        return this.a;
    }

    public void setSuggestionInfo(ArrayList<SuggestionInfo> arrayList) {
        this.a = arrayList;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.a);
    }
}
