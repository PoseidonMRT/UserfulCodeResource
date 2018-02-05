package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.TaxiInfo;
import java.util.ArrayList;
import java.util.List;

public final class TransitRouteResult extends SearchResult implements Parcelable {
    public static final Creator<TransitRouteResult> CREATOR = new q();
    private TaxiInfo a;
    private List<TransitRouteLine> b;
    private SuggestAddrInfo c;

    protected TransitRouteResult(Parcel parcel) {
        this.a = (TaxiInfo) parcel.readParcelable(TaxiInfo.class.getClassLoader());
        this.b = new ArrayList();
        parcel.readList(this.b, TransitRouteLine.class.getClassLoader());
        this.c = (SuggestAddrInfo) parcel.readParcelable(SuggestAddrInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public List<TransitRouteLine> getRouteLines() {
        return this.b;
    }

    public SuggestAddrInfo getSuggestAddrInfo() {
        return this.c;
    }

    public TaxiInfo getTaxiInfo() {
        return this.a;
    }

    public void setRoutelines(List<TransitRouteLine> list) {
        this.b = list;
    }

    public void setSuggestAddrInfo(SuggestAddrInfo suggestAddrInfo) {
        this.c = suggestAddrInfo;
    }

    public void setTaxiInfo(TaxiInfo taxiInfo) {
        this.a = taxiInfo;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.a, 1);
        parcel.writeList(this.b);
        parcel.writeParcelable(this.c, 1);
    }
}
