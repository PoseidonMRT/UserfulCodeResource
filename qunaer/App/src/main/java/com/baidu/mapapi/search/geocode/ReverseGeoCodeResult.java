package com.baidu.mapapi.search.geocode;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import java.util.List;

public class ReverseGeoCodeResult extends SearchResult {
    public static final Creator<ReverseGeoCodeResult> CREATOR = new b();
    private String a;
    private String b;
    private AddressComponent c;
    private LatLng d;
    private int e;
    private List<PoiInfo> f;
    private String g;

    public class AddressComponent implements Parcelable {
        public static final Creator<AddressComponent> CREATOR = new c();
        public int adcode;
        public String city;
        public int countryCode;
        public String countryName;
        public String district;
        public String province;
        public String street;
        public String streetNumber;

        protected AddressComponent(Parcel parcel) {
            this.streetNumber = parcel.readString();
            this.street = parcel.readString();
            this.district = parcel.readString();
            this.city = parcel.readString();
            this.province = parcel.readString();
            this.countryName = parcel.readString();
            this.countryCode = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.streetNumber);
            parcel.writeString(this.street);
            parcel.writeString(this.district);
            parcel.writeString(this.city);
            parcel.writeString(this.province);
            parcel.writeString(this.countryName);
            parcel.writeInt(this.countryCode);
        }
    }

    protected ReverseGeoCodeResult(Parcel parcel) {
        super(parcel);
        this.a = parcel.readString();
        this.b = parcel.readString();
        this.c = (AddressComponent) parcel.readParcelable(AddressComponent.class.getClassLoader());
        this.d = (LatLng) parcel.readValue(LatLng.class.getClassLoader());
        this.f = parcel.createTypedArrayList(PoiInfo.CREATOR);
        this.g = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return this.b;
    }

    public AddressComponent getAddressDetail() {
        return this.c;
    }

    public String getBusinessCircle() {
        return this.a;
    }

    public int getCityCode() {
        return this.e;
    }

    public LatLng getLocation() {
        return this.d;
    }

    public List<PoiInfo> getPoiList() {
        return this.f;
    }

    public String getSematicDescription() {
        return this.g;
    }

    public void setAddress(String str) {
        this.b = str;
    }

    public void setAddressDetail(AddressComponent addressComponent) {
        this.c = addressComponent;
    }

    public void setBusinessCircle(String str) {
        this.a = str;
    }

    public void setCityCode(int i) {
        this.e = i;
    }

    public void setLocation(LatLng latLng) {
        this.d = latLng;
    }

    public void setPoiList(List<PoiInfo> list) {
        this.f = list;
    }

    public void setSematicDescription(String str) {
        this.g = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeParcelable(this.c, 0);
        parcel.writeValue(this.d);
        parcel.writeTypedList(this.f);
        parcel.writeString(this.g);
    }
}
