package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.LatLng;

public class PlanNode implements Parcelable {
    public static final Creator<PlanNode> CREATOR = new m();
    private LatLng a = null;
    private String b = null;
    private String c = null;

    protected PlanNode(Parcel parcel) {
        this.a = (LatLng) parcel.readValue(LatLng.class.getClassLoader());
        this.b = parcel.readString();
        this.c = parcel.readString();
    }

    PlanNode(LatLng latLng, String str, String str2) {
        this.a = latLng;
        this.b = str;
        this.c = str2;
    }

    public static PlanNode withCityCodeAndPlaceName(int i, String str) {
        return new PlanNode(null, String.valueOf(i), str);
    }

    public static PlanNode withCityNameAndPlaceName(String str, String str2) {
        return new PlanNode(null, str, str2);
    }

    public static PlanNode withLocation(LatLng latLng) {
        return new PlanNode(latLng, null, null);
    }

    public int describeContents() {
        return 0;
    }

    public String getCity() {
        return this.b;
    }

    public LatLng getLocation() {
        return this.a;
    }

    public String getName() {
        return this.c;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
    }
}
