package com.baidu.mapapi.search.route;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteLine.TYPE;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.RouteStep;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"ParcelCreator"})
public class IndoorRouteLine extends RouteLine<IndoorRouteStep> {
    public static final Creator<IndoorRouteLine> CREATOR = new g();

    public class IndoorRouteStep extends RouteStep {
        private RouteNode c;
        private RouteNode d;
        private String e;
        private String f;
        private String g;
        private List<IndoorStepNode> h;
        private List<Double> i;

        public class IndoorStepNode {
            private String a;
            private int b;
            private LatLng c;
            private String d;

            public String getDetail() {
                return this.d;
            }

            public LatLng getLocation() {
                return this.c;
            }

            public String getName() {
                return this.a;
            }

            public int getType() {
                return this.b;
            }

            public void setDetail(String str) {
                this.d = str;
            }

            public void setLocation(LatLng latLng) {
                this.c = latLng;
            }

            public void setName(String str) {
                this.a = str;
            }

            public void setType(int i) {
                this.b = i;
            }
        }

        private List<LatLng> a(List<Double> list) {
            List<LatLng> arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i += 2) {
                arrayList.add(new LatLng(((Double) list.get(i)).doubleValue(), ((Double) list.get(i + 1)).doubleValue()));
            }
            return arrayList;
        }

        public String getBuildingId() {
            return this.g;
        }

        public RouteNode getEntrace() {
            return this.c;
        }

        public RouteNode getExit() {
            return this.d;
        }

        public String getFloorId() {
            return this.f;
        }

        public String getInstructions() {
            return this.e;
        }

        public List<IndoorStepNode> getStepNodes() {
            return this.h;
        }

        public List<LatLng> getWayPoints() {
            if (this.mWayPoints == null) {
                this.mWayPoints = a(this.i);
            }
            return this.mWayPoints;
        }

        public void setBuildingId(String str) {
            this.g = str;
        }

        public void setEntrace(RouteNode routeNode) {
            this.c = routeNode;
        }

        public void setExit(RouteNode routeNode) {
            this.d = routeNode;
        }

        public void setFloorId(String str) {
            this.f = str;
        }

        public void setInstructions(String str) {
            this.e = str;
        }

        public void setPath(List<Double> list) {
            this.i = list;
        }

        public void setStepNodes(List<IndoorStepNode> list) {
            this.h = list;
        }
    }

    public IndoorRouteLine() {
        setType(TYPE.WALKSTEP);
    }

    protected IndoorRouteLine(Parcel parcel) {
        super(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public List<IndoorRouteStep> getAllStep() {
        return super.getAllStep();
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }
}
