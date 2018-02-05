package com.mapquest.android.maps;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.webkit.WebView;
import com.baidu.location.LocationClientOption;
import com.mapquest.android.maps.RouteResponse.Location;
import com.mapquest.android.maps.RouteResponse.Route;
import com.mapquest.android.maps.RouteResponse.Route.Leg;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver.Sign;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import qunar.sdk.mapapi.entity.QMarker;

public class RouteManager {
    private final String TAG = "gis";
    private String apiKey = "";
    private boolean bestFitRoute = true;
    private Context context;
    private boolean debug = false;
    private boolean handleAmbiguities = true;
    private boolean ignoreAmbiguities = true;
    private WebView itinerary;
    private Paint letterPaint;
    private MapView mapView;
    private String options;
    private RouteCallback routeCallback;
    private ItemizedOverlay<? extends OverlayItem> routeLocationsItemizedOverlay;
    private LineOverlay routeOverlay;
    private List<String> routeRequestLocations;
    private Paint routeRibbonPaint;

    public interface RouteCallback {
        void onError(RouteResponse routeResponse);

        void onSuccess(RouteResponse routeResponse);
    }

    class RouteTask extends AsyncTask<RouteRequest, Void, RouteResponse> {
        private RouteTask() {
        }

        protected RouteResponse doInBackground(RouteRequest... routeRequestArr) {
            RouteRequest routeRequest = routeRequestArr[0];
            Map hashMap = new HashMap();
            String url = routeRequest.getURL();
            String requestJSON = routeRequest.getRequestJSON();
            if (RouteManager.this.debug) {
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
            RouteManager.this.handleResponse(routeResponse);
        }
    }

    public RouteManager(Context context) {
        if (context instanceof MapActivity) {
            this.context = context;
            if (context instanceof MapActivity) {
                this.apiKey = ((MapActivity) context).getKey();
            }
            init();
            return;
        }
        throw new IllegalArgumentException("the context used in instantiating a RouteManager must be a MapActivity.  If you are not using a MapActivity then use the RouteManager(Context context,String apiKey) constructor");
    }

    public RouteManager(Context context, String str) {
        this.apiKey = str;
        this.context = context;
        init();
    }

    public void setHandleAmbiguities(boolean z) {
        this.handleAmbiguities = z;
    }

    public void setIgnoreAmbiguities(boolean z) {
        this.ignoreAmbiguities = z;
    }

    public void setOptions(String str) {
        this.options = str;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }

    public void setBestFitRoute(boolean z) {
        this.bestFitRoute = z;
    }

    public void setItineraryView(WebView webView) {
        this.itinerary = webView;
    }

    public void setRouteCallback(RouteCallback routeCallback) {
        this.routeCallback = routeCallback;
    }

    private void init() {
        this.routeRibbonPaint = new Paint(1);
        this.routeRibbonPaint.setColor(-16776961);
        this.routeRibbonPaint.setAlpha(100);
        this.routeRibbonPaint.setStyle(Style.STROKE);
        this.routeRibbonPaint.setStrokeJoin(Join.ROUND);
        this.routeRibbonPaint.setStrokeCap(Cap.ROUND);
        this.routeRibbonPaint.setStrokeWidth(5.0f);
        this.letterPaint = new Paint(1);
        this.letterPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
        this.letterPaint.setTextAlign(Align.LEFT);
        this.letterPaint.setColor(Color.rgb(255, 255, 255));
    }

    public void setRouteRibbonPaint(Paint paint) {
        this.routeRibbonPaint = paint;
    }

    public void createRoute(String str, String str2) {
        List arrayList = new ArrayList();
        arrayList.add(str);
        arrayList.add(str2);
        createRoute(arrayList);
    }

    public void createRoute(List<String> list) {
        if (this.mapView != null || this.itinerary != null || this.routeCallback != null) {
            this.routeRequestLocations = list;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ locations:[");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(locationify((String) list.get(i)));
                if (i + 1 != list.size()) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("],options:").append(generateOptionsJSON(this.options)).append("}");
            if (this.apiKey != null && !this.apiKey.equals("")) {
                RouteRequest routeRequest = new RouteRequest(stringBuilder.toString(), this.apiKey, this.ignoreAmbiguities);
                new RouteTask().execute(new RouteRequest[]{routeRequest});
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

    private String generateOptionsJSON(String str) {
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
            jSONObject.put("generalize", 0);
            jSONObject.put("shapeFormat", "cmp6");
            if (this.itinerary != null) {
                jSONObject.put("narrativeType", "microformat");
            }
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
        try {
            int i = routeResponse.info.statusCode;
            if (i == 0) {
                if (this.mapView != null) {
                    this.routeLocationsItemizedOverlay = buildLocationsItemizedOverlay(routeResponse.route.locations);
                    if (this.routeLocationsItemizedOverlay != null) {
                        addEventHandlersLocationsItemizedOverlay(this.routeLocationsItemizedOverlay);
                        this.mapView.getOverlays().add(this.routeLocationsItemizedOverlay);
                    }
                    this.routeOverlay = buildRouteOverlay(routeResponse);
                    if (this.routeOverlay != null) {
                        this.mapView.getOverlays().add(this.routeOverlay);
                        if (this.bestFitRoute) {
                            bestFit(routeResponse);
                        }
                    }
                }
                if (this.itinerary != null) {
                    String buildHTMLItinerary = buildHTMLItinerary(routeResponse);
                    if (buildHTMLItinerary != null) {
                        this.itinerary.loadDataWithBaseURL("", buildHTMLItinerary, "text/html", "UTF-8", (String) null);
                    }
                }
                if (this.routeCallback != null) {
                    this.routeCallback.onSuccess(routeResponse);
                }
                this.routeRequestLocations = null;
            } else if (i == 610 && this.handleAmbiguities) {
                List list = routeResponse.collections.locationList;
                for (int i2 = 0; i2 < list.size(); i2++) {
                    List list2 = (List) list.get(i2);
                    if (list2.size() > 1) {
                        showAmbiguityPicker(list2, i2, routeResponse);
                        return;
                    }
                }
            } else {
                if (this.routeCallback != null) {
                    this.routeCallback.onError(routeResponse);
                }
                this.routeRequestLocations = null;
            }
        } catch (Exception e) {
            if (this.routeCallback != null) {
                RouteResponse routeResponse2 = new RouteResponse();
                routeResponse2.info.messages.add(e.getMessage());
                this.routeCallback.onError(routeResponse2);
            }
        }
    }

    private void showAmbiguityPicker(final List<Location> list, final int i, final RouteResponse routeResponse) {
        ArrayList arrayList = new ArrayList();
        for (Location location : list) {
            arrayList.add(location.toString());
        }
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        Builder builder = new Builder(this.context);
        builder.setTitle("Resolve Location:");
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                if (RouteManager.this.routeCallback != null) {
                    RouteManager.this.routeCallback.onError(routeResponse);
                }
            }
        });
        builder.setItems(strArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RouteManager.this.routeRequestLocations.set(i, ((Location) list.get(i)).locationJSON.toString());
                RouteManager.this.createRoute(RouteManager.this.routeRequestLocations);
            }
        }).show();
    }

    protected String buildHTMLItinerary(RouteResponse routeResponse) {
        int i = 0;
        Route route = routeResponse.route;
        List list = route.locations;
        List list2 = route.legs;
        String str = "M".equals(route.options.unit) ? " miles" : " kilometers";
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder stringBuilder = new StringBuilder();
        if (route.legs.size() > 0) {
            stringBuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
            stringBuilder.append("<html>");
            stringBuilder.append("<head>");
            stringBuilder.append("<title>Route Itinerary</title>");
            stringBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
            stringBuilder.append("<style type=\"text/css\">");
            stringBuilder.append("  .locationSummary{font-weight: bold;}");
            stringBuilder.append("  .distTimeSummary{font-size:12px;}");
            stringBuilder.append("  .first{font-weight:bold;}");
            stringBuilder.append("  img {margin:2px;}");
            stringBuilder.append("  th {text-align:left; font-size:20; background-color:#eeeeee;}");
            stringBuilder.append("  td {vertical-align:top;}");
            stringBuilder.append("</style>");
            stringBuilder.append("</head>");
            stringBuilder.append("<body>");
            stringBuilder.append("<table>");
            stringBuilder.append("  <tr><td>Start:</td><td><span class=\"locationSummary\">").append(((Location) list.get(0)).toString()).append("</span></td></tr>");
            stringBuilder.append("  <tr><td>End:</td><td><span class=\"locationSummary\">").append(((Location) list.get(list.size() - 1)).toString()).append("</span></td></tr>");
            stringBuilder.append("  <tr><td colspan=\"2\"><div class=\"distTimeSummary\">Distance: ").append(decimalFormat.format(route.distance)).append(str).append(" / Time: ").append(route.formattedTime).append("</div></td></tr>");
            stringBuilder.append("</table>");
            stringBuilder.append("<hr>");
            stringBuilder.append("<table>");
            while (i < list2.size()) {
                Leg leg = (Leg) list2.get(i);
                stringBuilder.append("<tr><th colspan=\"2\">").append(((Location) list.get(i)).toString()).append("</th></tr>");
                for (Maneuver maneuver : leg.maneuvers) {
                    stringBuilder.append("<tr>");
                    stringBuilder.append("<td>");
                    if (!"".equals(maneuver.iconUrl)) {
                        stringBuilder.append("<img alt=\"").append(maneuver.directionName).append("\" src=\"").append(maneuver.iconUrl).append("\">");
                    }
                    for (Sign sign : maneuver.signs) {
                        if (!"".equals(sign.url)) {
                            stringBuilder.append("<img alt=\"").append(sign.text).append("\" src=\"").append(sign.url).append("\">");
                        }
                    }
                    stringBuilder.append("</td>");
                    stringBuilder.append("<td>").append(maneuver.narrative).append("</td>");
                    stringBuilder.append("</tr>");
                    stringBuilder.append("<tr><td colspan=\"2\"><hr></td></tr>");
                }
                i++;
            }
            stringBuilder.append("<tr><th colspan=\"2\">").append(((Location) list.get(list.size() - 1)).toString()).append("</th></tr>");
            stringBuilder.append("</table>");
            stringBuilder.append("</body>");
            stringBuilder.append("</html>");
        }
        return stringBuilder.toString();
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

    protected ItemizedOverlay<? extends OverlayItem> buildLocationsItemizedOverlay(List<Location> list) {
        if (this.routeLocationsItemizedOverlay == null) {
            this.routeLocationsItemizedOverlay = new DefaultItemizedOverlay(Util.getDrawable(this.mapView.getContext(), "poi"));
            this.routeLocationsItemizedOverlay.setZIndex(20000);
        } else {
            this.routeLocationsItemizedOverlay.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            ((DefaultItemizedOverlay) this.routeLocationsItemizedOverlay).addItem(buildLocationOverlayItem((Location) list.get(i), i));
        }
        return this.routeLocationsItemizedOverlay;
    }

    protected OverlayItem buildLocationOverlayItem(Location location, int i) {
        GeoPoint geoPoint = location.displayLatLng != null ? location.displayLatLng : location.latLng;
        Drawable drawable = Util.getDrawable(this.mapView.getContext(), "poi");
        this.letterPaint.setTextSize((float) (drawable.getIntrinsicHeight() / 2));
        drawable = Util.addStringToMarker(this.mapView.getContext(), drawable, new String(new char[]{(char) (i + 65)}), this.letterPaint);
        OverlayItem overlayItem = new OverlayItem(geoPoint, "", "");
        overlayItem.setMarker(drawable);
        return overlayItem;
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
        if (this.itinerary != null) {
            this.itinerary.clearView();
        }
    }
}
