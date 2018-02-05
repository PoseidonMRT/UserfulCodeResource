package com.baidu.mapapi.search.route;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteLine.TYPE;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.core.VehicleInfo;
import java.util.List;

public final class TransitRouteLine extends RouteLine<TransitStep> implements Parcelable {
    public static final Creator<TransitRouteLine> CREATOR = new o();
    private TaxiInfo b;

    public class TransitStep extends RouteStep implements Parcelable {
        public static final Creator<TransitStep> CREATOR = new p();
        private VehicleInfo c;
        private RouteNode d;
        private RouteNode e;
        private TransitRouteStepType f;
        private String g;
        private String h;

        public enum TransitRouteStepType {
            BUSLINE,
            SUBWAY,
            WAKLING
        }

        protected TransitStep(Parcel parcel) {
            super(parcel);
            this.c = (VehicleInfo) parcel.readParcelable(VehicleInfo.class.getClassLoader());
            this.d = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            this.e = (RouteNode) parcel.readParcelable(RouteNode.class.getClassLoader());
            int readInt = parcel.readInt();
            this.f = readInt == -1 ? null : TransitRouteStepType.values()[readInt];
            this.g = parcel.readString();
            this.h = parcel.readString();
        }

        public int describeContents() {
            return 0;
        }

        public RouteNode getEntrance() {
            return this.d;
        }

        public RouteNode getExit() {
            return this.e;
        }

        public String getInstructions() {
            return this.g;
        }

        public TransitRouteStepType getStepType() {
            return this.f;
        }

        public VehicleInfo getVehicleInfo() {
            return this.c;
        }

        public List<LatLng> getWayPoints() {
            if (this.mWayPoints == null) {
                this.mWayPoints = CoordUtil.decodeLocationList(this.h);
            }
            return this.mWayPoints;
        }

        public void setEntrace(RouteNode routeNode) {
            this.d = routeNode;
        }

        public void setExit(RouteNode routeNode) {
            this.e = routeNode;
        }

        public void setInstructions(String str) {
            this.g = str;
        }

        public void setPathString(String str) {
            this.h = str;
        }

        public void setStepType(TransitRouteStepType transitRouteStepType) {
            this.f = transitRouteStepType;
        }

        public void setVehicleInfo(VehicleInfo vehicleInfo) {
            this.c = vehicleInfo;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.c, 1);
            parcel.writeParcelable(this.d, 1);
            parcel.writeParcelable(this.e, 1);
            parcel.writeInt(this.f == null ? -1 : this.f.ordinal());
            parcel.writeString(this.g);
            parcel.writeString(this.h);
        }
    }

    protected TransitRouteLine(Parcel parcel) {
        super(parcel);
        this.b = (TaxiInfo) parcel.readParcelable(TaxiInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    @Deprecated
    public TaxiInfo getTaxitInfo() {
        return this.b;
    }

    public void setTaxitInfo(TaxiInfo taxiInfo) {
        this.b = taxiInfo;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.setType(TYPE.TRANSITSTEP);
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.b, 1);
    }
}
