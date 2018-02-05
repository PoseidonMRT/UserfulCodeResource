package qunar.sdk.mapapi.entity;

import java.io.Serializable;
import java.util.List;
import qunar.sdk.location.QLocation;

public class QunarRouteNode implements Serializable {
    private static final long serialVersionUID = -1;
    public int distance;
    public int duration;
    public String instructions;
    public QLocation nodeLocation;
    public RouteNodeType routeNodeType = RouteNodeType.POSITIONNAME;
    public List<QLocation> wayPoints;

    public QunarRouteNode(QLocation qLocation, String str) {
        this.nodeLocation = qLocation;
        this.instructions = str;
    }

    public String toString() {
        return "QunarRouteNode [nodeLocation=" + this.nodeLocation + ", instructions=" + this.instructions + "]";
    }
}
