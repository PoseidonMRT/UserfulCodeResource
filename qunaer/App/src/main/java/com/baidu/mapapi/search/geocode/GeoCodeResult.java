package com.baidu.mapapi.search.geocode;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;

public class GeoCodeResult extends SearchResult implements Parcelable {
    public static final Creator<GeoCodeResult> CREATOR = new a();
    private LatLng a;
    private String b;

    protected GeoCodeResult(Parcel parcel) {
        this.a = (LatLng) parcel.readValue(LatLng.class.getClassLoader());
        this.b = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return this.b;
    }

    public LatLng getLocation() {
        return this.a;
    }

    public void setAddress(String str) {
        this.b = str;
    }

    public void setLocation(LatLng latLng) {
        this.a = latLng;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.a);
        parcel.writeString(this.b);
    }
}
