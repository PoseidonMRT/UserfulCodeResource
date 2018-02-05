package org.apache.http.conn.routing;

@Deprecated
public class BasicRouteDirector implements HttpRouteDirector {
    public BasicRouteDirector() {
        throw new RuntimeException("Stub!");
    }

    public int nextStep(RouteInfo routeInfo, RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }

    protected int firstStep(RouteInfo routeInfo) {
        throw new RuntimeException("Stub!");
    }

    protected int directStep(RouteInfo routeInfo, RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }

    protected int proxiedStep(RouteInfo routeInfo, RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }
}
