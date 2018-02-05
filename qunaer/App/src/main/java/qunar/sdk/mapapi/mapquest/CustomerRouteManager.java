package qunar.sdk.mapapi.mapquest;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import com.baidu.location.LocationClientOption;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.HttpUtil;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MercatorProjection;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteRequest;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.RouteResponse.Location;
import com.mapquest.android.maps.RouteResponse.Route;
import com.mapquest.android.maps.RouteResponse.Route.Leg;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver;
import com.mapquest.android.maps.Util;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarRoutePlanSearch;
import qunar.sdk.mapapi.QunarRouteType;
import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.entity.QunarRouteNode;
import qunar.sdk.mapapi.listener.QunarRoutePlanCallback;
import qunar.sdk.mapapi.listener.RoutePlaneResultType;
import qunar.sdk.mapapi.utils.MapHelperUtils;

public class CustomerRouteManager implements QunarRoutePlanSearch {
    private final String TAG;
    private String apiKey;
    private boolean bestFitRoute;
    private String cityName;
    private boolean debug;
    DecimalFormat df;
    Drawable endPointDrawable;
    private boolean ignoreAmbiguities;
    private boolean isCancle;
    private boolean isFinishFlag;
    private Paint letterPaint;
    private MapView mapView;
    private Handler myHandler;
    private String options;
    private QunarRouteType qunarRouteType;
    private DefaultItemizedOverlay routeLocationsItemizedOverlay;
    private LineOverlay routeOverlay;
    private QunarRoutePlanCallback routePlanCallback;
    private List<String> routeRequestLocations;
    private Paint routeRibbonPaint;
    private RouteTask routeTask;
    Drawable startPointDrawable;

    class RouteTask extends AsyncTask<RouteRequest, Void, RouteResponse> {
        private RouteTask() {
        }

        protected void onPreExecute() {
            if (CustomerRouteManager.this.routePlanCallback == null) {
                cancel(true);
                CustomerRouteManager.this.isCancle = true;
                return;
            }
            CustomerRouteManager.this.routePlanCallback.routeAnalysisStartCallback();
            CustomerRouteManager.this.isCancle = false;
            CustomerRouteManager.this.isFinishFlag = false;
            CustomerRouteManager.this.startTimer();
        }

        protected RouteResponse doInBackground(RouteRequest... routeRequestArr) {
            if (CustomerRouteManager.this.isCancle || CustomerRouteManager.this.isFinishFlag) {
                return null;
            }
            RouteRequest routeRequest = routeRequestArr[0];
            Map hashMap = new HashMap();
            String url = routeRequest.getURL();
            String requestJSON = routeRequest.getRequestJSON();
            if (CustomerRouteManager.this.debug) {
                hashMap.put("json", requestJSON);
            } else {
                hashMap.put("json", requestJSON);
            }
            try {
                return new RouteResponse(new JSONObject(HttpUtil.runPost(url, hashMap)));
            } catch (Exception e) {
                Exception exception = e;
                exception.printStackTrace();
                RouteResponse routeResponse = new RouteResponse();
                routeResponse.info.statusCode = 0;
                routeResponse.info.messages.add(exception.getMessage());
                return routeResponse;
            }
        }

        protected void onPostExecute(RouteResponse routeResponse) {
            if (!CustomerRouteManager.this.isCancle && !CustomerRouteManager.this.isFinishFlag) {
                CustomerRouteManager.this.handleResponse(routeResponse);
            }
        }
    }

    public CustomerRouteManager(MapView mapView) {
        this.TAG = "gis";
        this.debug = false;
        this.bestFitRoute = true;
        this.apiKey = "";
        this.ignoreAmbiguities = true;
        this.routePlanCallback = null;
        this.isFinishFlag = false;
        this.isCancle = false;
        this.myHandler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 10010:
                        if (!CustomerRouteManager.this.isFinishFlag) {
                            CustomerRouteManager.this.isCancle = true;
                            if (CustomerRouteManager.this.routePlanCallback != null) {
                                CustomerRouteManager.this.routePlanCallback.onRouteTimeOut(CustomerRouteManager.this.qunarRouteType, CustomerRouteManager.this.cityName);
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.apiKey = MapQuestView.MAPQUEST_KEY;
        this.mapView = mapView;
        init();
    }

    public void setIgnoreAmbiguities(boolean z) {
        this.ignoreAmbiguities = z;
    }

    public void setOptions(String str) {
        this.options = str;
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }

    public void setBestFitRoute(boolean z) {
        this.bestFitRoute = z;
    }

    private void init() {
        this.routeRibbonPaint = new Paint(1);
        this.routeRibbonPaint.setColor(Color.parseColor("#E51b88ff"));
        this.routeRibbonPaint.setAlpha(100);
        this.routeRibbonPaint.setStyle(Style.STROKE);
        this.routeRibbonPaint.setStrokeJoin(Join.ROUND);
        this.routeRibbonPaint.setStrokeCap(Cap.ROUND);
        this.routeRibbonPaint.setStrokeWidth(8.0f);
        this.letterPaint = new Paint(1);
        this.letterPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
        this.letterPaint.setTextAlign(Align.LEFT);
        this.letterPaint.setColor(Color.rgb(255, 255, 255));
    }

    public void setRouteRibbonPaint(Paint paint) {
        this.routeRibbonPaint = paint;
    }

    private void createRoute(List<String> list, QunarRouteType qunarRouteType) {
        if (this.mapView != null || this.routePlanCallback != null) {
            this.routeRequestLocations = list;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ locations:[");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(locationify((String) list.get(i)));
                if (i + 1 != list.size()) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("],options:").append(generateOptionsJSON(this.options, qunarRouteType)).append("}");
            if (this.apiKey != null && !this.apiKey.equals("")) {
                RouteRequest routeRequest = new RouteRequest(stringBuilder.toString(), this.apiKey, this.ignoreAmbiguities);
                if (!(this.routeTask == null || this.routeTask.getStatus() == Status.FINISHED)) {
                    this.routeTask.cancel(true);
                    this.routeTask = null;
                }
                this.routeTask = new RouteTask();
                this.routeTask.execute(new RouteRequest[]{routeRequest});
            }
        }
    }

    private String locationify(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            return str;
        } catch (Exception e) {
            return "\"" + str + "\"";
        }
    }

    private String generateOptionsJSON(String str, QunarRouteType qunarRouteType) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                jSONObject = new JSONObject(str);
            } catch (Exception e) {
                jSONObject = null;
            }
        } else {
            jSONObject = null;
        }
        if (jSONObject == null) {
            try {
                jSONObject = new JSONObject();
            } catch (Exception e2) {
            }
        }
        try {
            Object obj;
            jSONObject.put("generalize", 0);
            jSONObject.put("shapeFormat", "cmp6");
            jSONObject.put("narrativeType", "text");
            jSONObject.put("unit", "k");
            switch (qunarRouteType) {
                case DRIVING:
                    obj = "fastest";
                    break;
                case TRANSIT:
                    obj = "multimodal";
                    break;
                case WALKING:
                    obj = "pedestrian";
                    break;
                default:
                    obj = "fastest";
                    break;
            }
            jSONObject.put("routeType", obj);
            if (this.mapView != null) {
                JSONObject jSONObject2 = new JSONObject();
                JSONObject jSONObject3 = new JSONObject();
                if (this.mapView.getProjection() instanceof MercatorProjection) {
                    GeoPoint mapCenter = this.mapView.getMapCenter();
                    jSONObject3.put("lat", mapCenter.getLatitude());
                    jSONObject3.put("lng", mapCenter.getLongitude());
                    jSONObject2.put("center", jSONObject3);
                    jSONObject2.put("scale", ((MercatorProjection) this.mapView.getProjection()).getScaleFactor(this.mapView.getZoomLevel()));
                    jSONObject2.put(QMarker.MARKER_HEIGHT, this.mapView.getHeight());
                    jSONObject2.put("width", this.mapView.getWidth());
                    jSONObject.put("mapState", jSONObject2);
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return jSONObject.toString();
    }

    private void handleResponse(RouteResponse routeResponse) {
        routePlaneEnd();
        if (this.routePlanCallback != null) {
            if (this.isCancle) {
                this.routePlanCallback.routeAnalysisEndCallback(RoutePlaneResultType.CANCLE);
                return;
            }
            if (routeResponse != null) {
                try {
                    if (!(routeResponse.info == null || routeResponse.route == null)) {
                        int i = routeResponse.info.statusCode;
                        if (i == 0) {
                            if (this.mapView != null) {
                                removeFromMap();
                                this.routeOverlay = buildRouteOverlay(routeResponse);
                                this.routePlanCallback.routeAnalysisEndCallback(RoutePlaneResultType.SUCCESS);
                                if (this.routeOverlay != null) {
                                    this.mapView.getOverlays().add(this.routeOverlay);
                                    if (this.bestFitRoute) {
                                        bestFit(routeResponse);
                                    }
                                }
                                getRouteNodes(routeResponse);
                                this.routeRequestLocations = null;
                                return;
                            }
                            this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线！", null, 0);
                            this.routeRequestLocations = null;
                            return;
                        } else if (i == 610) {
                            getSuggestStartEndNodes(routeResponse);
                            return;
                        } else {
                            this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线！", null, 0);
                            this.routeRequestLocations = null;
                            return;
                        }
                    }
                } catch (Exception e) {
                    this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线！", null, 0);
                    return;
                }
            }
            this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线！", null, 0);
        }
    }

    private void getSuggestStartEndNodes(RouteResponse routeResponse) {
        if (routeResponse != null && this.routePlanCallback != null) {
            List list = routeResponse.collections.locationList;
            if (list != null && !list.isEmpty()) {
                List arrayList = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    List<Location> list2 = (List) list.get(i);
                    if (!(list2 == null || list2.isEmpty())) {
                        for (Location location : list2) {
                            GeoPoint geoPoint = location.latLng;
                            arrayList.add(new QunarRouteNode(new QLocation(geoPoint.getLatitude(), geoPoint.getLongitude()), location.street));
                        }
                    }
                }
                this.routePlanCallback.onStartEndNodeAmbiguityCallback(this.qunarRouteType, arrayList, arrayList);
            }
        }
    }

    private void getRouteNodes(RouteResponse routeResponse) {
        if (this.df == null) {
            this.df = new DecimalFormat("#.##");
        }
        Route route = routeResponse.route;
        List list = route.legs;
        if (route.legs.size() > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                for (Maneuver maneuver : ((Leg) list.get(i)).maneuvers) {
                    GeoPoint geoPoint = maneuver.startPoint;
                    QunarRouteNode qunarRouteNode = new QunarRouteNode(new QLocation(geoPoint.getLatitude(), geoPoint.getLongitude()), maneuver.narrative + " about " + this.df.format(maneuver.distance * 1000.0d) + " Meter.");
                    if (maneuver.distance != 0.0d) {
                        arrayList.add(qunarRouteNode);
                    }
                }
            }
            List list2 = routeResponse.route.locations;
            if (list2.size() > 1) {
                buildLocationsItemizedOverlay(((Location) list2.get(0)).displayLatLng, ((Location) list2.get(list2.size() - 1)).displayLatLng);
            }
            this.routePlanCallback.onRoutePlanCallback(true, "", arrayList, new Double(route.distance * 1000.0d).intValue());
            return;
        }
        this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线！", null, 0);
    }

    protected LineOverlay buildRouteOverlay(RouteResponse routeResponse) {
        if (this.routeOverlay == null) {
            this.routeOverlay = new LineOverlay(this.routeRibbonPaint);
            this.routeOverlay.setZIndex(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL);
        }
        this.routeOverlay.setSimplify(true, -1);
        this.routeOverlay.setData(routeResponse.route.shape.shapePoints);
        addEventHandlersRouteOverlay(this.routeOverlay);
        return this.routeOverlay;
    }

    protected void buildLocationsItemizedOverlay(GeoPoint geoPoint, GeoPoint geoPoint2) {
        Drawable drawableByName;
        if (this.routeLocationsItemizedOverlay == null) {
            this.routeLocationsItemizedOverlay = new DefaultItemizedOverlay(Util.getDrawable(this.mapView.getContext(), "poi"));
            this.routeLocationsItemizedOverlay.setZIndex(20000);
            this.mapView.getOverlays().add(this.routeLocationsItemizedOverlay);
        } else {
            this.routeLocationsItemizedOverlay.clear();
        }
        OverlayItem overlayItem = new OverlayItem(geoPoint, "", "");
        if (this.startPointDrawable == null) {
            drawableByName = Util.getDrawableByName(this.mapView.getContext(), "start_mdpi.png");
        } else {
            drawableByName = this.startPointDrawable;
        }
        Overlay.setAlignment(drawableByName, 33);
        overlayItem.setMarker(drawableByName);
        this.routeLocationsItemizedOverlay.addItem(overlayItem);
        overlayItem = new OverlayItem(geoPoint2, "", "");
        if (this.endPointDrawable == null) {
            drawableByName = Util.getDrawableByName(this.mapView.getContext(), "end_mdpi.png");
        } else {
            drawableByName = this.endPointDrawable;
        }
        Overlay.setAlignment(drawableByName, 33);
        overlayItem.setMarker(drawableByName);
        this.routeLocationsItemizedOverlay.addItem(overlayItem);
    }

    protected void addEventHandlersLocationsItemizedOverlay(ItemizedOverlay<? extends OverlayItem> itemizedOverlay) {
    }

    protected void addEventHandlersRouteOverlay(Overlay overlay) {
    }

    private void bestFit(RouteResponse routeResponse) {
        this.mapView.getController().zoomToSpan(routeResponse.route.boundingBox);
    }

    public void clearRoute() {
        if (this.routeOverlay != null) {
            if (this.mapView != null) {
                this.mapView.getOverlays().remove(this.routeOverlay);
            }
            this.routeOverlay.destroy();
            this.routeOverlay = null;
        }
        if (this.routeLocationsItemizedOverlay != null) {
            if (this.mapView != null) {
                this.mapView.getOverlays().remove(this.routeLocationsItemizedOverlay);
            }
            this.routeLocationsItemizedOverlay.destroy();
            this.routeLocationsItemizedOverlay = null;
        }
        if (this.mapView != null) {
            this.mapView.invalidate();
        }
    }

    public void startRoutPlane(QunarRouteType qunarRouteType, QunarRouteNode qunarRouteNode, QunarRouteNode qunarRouteNode2, String str) {
        if (this.mapView != null && this.routePlanCallback != null) {
            if (qunarRouteNode == null || qunarRouteNode2 == null || qunarRouteNode.nodeLocation == null || qunarRouteNode2.nodeLocation == null) {
                this.routePlanCallback.onRoutePlanCallback(false, "抱歉，未找到合适的路线", null, 0);
                return;
            }
            this.qunarRouteType = qunarRouteType;
            this.cityName = str;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ locations:[");
            stringBuilder.append("{\"latLng\":{\"lat\":" + qunarRouteNode.nodeLocation.getLatitude() + ",\"lng\":" + qunarRouteNode.nodeLocation.getLongitude() + "}}," + "{\"latLng\":{\"lat\":" + qunarRouteNode2.nodeLocation.getLatitude() + ",\"lng\":" + qunarRouteNode2.nodeLocation.getLongitude() + "}}");
            stringBuilder.append("],options:").append(generateOptionsJSON(this.options, qunarRouteType)).append("}");
            if (this.apiKey == null || this.apiKey.equals("")) {
                this.routePlanCallback.onRoutePlanCallback(false, "无法访问路径分析服务", null, 0);
                return;
            }
            RouteRequest routeRequest = new RouteRequest(stringBuilder.toString(), this.apiKey, this.ignoreAmbiguities);
            if (!(this.routeTask == null || this.routeTask.getStatus() == Status.FINISHED)) {
                this.routeTask.cancel(true);
                this.routeTask = null;
            }
            this.routeTask = new RouteTask();
            this.routeTask.execute(new RouteRequest[]{routeRequest});
        }
    }

    public void setRotePlanCallback(QunarRoutePlanCallback qunarRoutePlanCallback) {
        this.routePlanCallback = qunarRoutePlanCallback;
    }

    public void destory() {
        cancelRoutePlane();
        clearRoute();
        this.mapView = null;
    }

    public void cancelRoutePlane() {
        cancelTimer();
        this.isCancle = true;
        if (!(this.routeTask == null || this.routeTask.getStatus() == Status.FINISHED)) {
            this.routeTask.cancel(true);
            this.routeTask = null;
        }
        if (this.routePlanCallback != null) {
            this.routePlanCallback.routeAnalysisEndCallback(RoutePlaneResultType.CANCLE);
        }
    }

    public void removeFromMap() {
        clearRoute();
    }

    private void startTimer() {
        this.myHandler.sendEmptyMessageDelayed(10010, 15000);
    }

    private void cancelTimer() {
        if (this.myHandler != null) {
            this.myHandler.removeMessages(10010);
        }
    }

    private void routePlaneEnd() {
        cancelTimer();
        this.isFinishFlag = true;
    }

    public void appointStartEndShowMarker(QMarker qMarker, QMarker qMarker2) {
        if (qMarker != null) {
            this.startPointDrawable = MapHelperUtils.createDrawable(this.mapView.getContext(), qMarker);
        }
        if (qMarker2 != null) {
            this.endPointDrawable = MapHelperUtils.createDrawable(this.mapView.getContext(), qMarker2);
        }
    }
}
