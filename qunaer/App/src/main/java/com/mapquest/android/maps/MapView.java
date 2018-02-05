package com.mapquest.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;
import android.widget.ZoomControls;
import com.iflytek.cloud.SpeechEvent;
import com.mapquest.android.maps.TileCacher.CacheType;
import com.mqunar.libtask.ProgressType;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MapView extends ViewGroup {
    private static final float GUARANTEE_TILELOAD_PERCENTAGE = 0.5f;
    private static final String LOG_TAG = MapView.class.getSimpleName();
    private static final int MAP_VIEW_BACKGROUND = Color.rgb(238, 238, 238);
    private static final int PRELOAD = 31459;
    Queue<Animator> animators;
    private int bottomMargin;
    private boolean builtInZoomControls;
    GeoPoint centerGeoPoint;
    Configuration configuration;
    private Context context;
    float currentScale;
    private Paint customTilePaint;
    private Paint defaultTilePaint;
    TouchEventHandler eventHandler;
    private boolean firedMapLoaded;
    Point focalPoint;
    private int height;
    private boolean isPause;
    private int leftMargin;
    private Bitmap loadingTile;
    private boolean logo;
    private BitmapDrawable logoDrawable;
    private MapConfiguration mapConfiguration;
    private MapController mapController;
    Handler mapEventCallback;
    private HashMap<MapViewEventListener, Handler> mapViewEventListeners;
    private NetworkConnectivityListener networkConnectivityListener;
    OverlayController overlayController;
    private MapProvider provider;
    private ReticleDrawMode reticalMode;
    private int rightMargin;
    private Rect rotRect;
    private RotatableProjection rotatableProjection;
    private float rotateDegrees;
    Point scalePoint;
    boolean scaling;
    private boolean showTerms;
    private TileCacher tileCacher;
    private int tileCount;
    private TileFactory tileFactory;
    private TileDownloader tileProvider;
    private int topMargin;
    private int totalTileCount;
    private DefaultTrackBallHandler trackBallHandler;
    TrafficManager trafficManager;
    Rect visibleRect;
    private int width;
    private ZoomButtonsController zoomButtonsController;
    private ZoomControls zoomControls;
    private int zoomLevel;

    public interface MapViewEventListener {
        void longTouch(MapView mapView, Point point);

        void mapLoaded(MapView mapView);

        void move(MapView mapView);

        void moveEnd(MapView mapView);

        void moveStart(MapView mapView);

        void touch(MapView mapView);

        void zoomEnd(MapView mapView);

        void zoomStart(MapView mapView);
    }

    class AssetUpdater extends Thread {
        private final String LOG_TAG = AssetUpdater.class.getName();
        private final String[] assets = new String[]{"logo_hdpi.png", "logo_mdpi.png", "navteqlogo_hdpi.png", "navteqlogo_mdpi.png", "osmlogo_hdpi.png", "osmlogo_mdpi.png"};
        private final String cdnRoot = "http://content.mqcdn.com/mobile/android/";
        private long staleDate;

        public AssetUpdater() {
            Calendar instance = Calendar.getInstance();
            instance.add(5, -14);
            this.staleDate = instance.getTimeInMillis();
        }

        public void run() {
            try {
                for (String updateFile : this.assets) {
                    updateFile(updateFile);
                }
            } catch (Exception e) {
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void updateFile(java.lang.String r7) {
            /*
            r6 = this;
            r0 = 0;
            r2 = new java.io.File;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r3 = com.mapquest.android.maps.MapView.this;
            r3 = r3.getContext();
            r3 = r3.getCacheDir();
            r3 = r3.getAbsoluteFile();
            r1 = r1.append(r3);
            r3 = java.io.File.separator;
            r1 = r1.append(r3);
            r1 = r1.append(r7);
            r1 = r1.toString();
            r2.<init>(r1);
            r1 = r6.needsUpdating(r2);
            if (r1 == 0) goto L_0x0067;
        L_0x0031:
            r1 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x009b, all -> 0x0075 }
            r1.<init>(r2);	 Catch:{ Exception -> 0x009b, all -> 0x0075 }
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r2.<init>();	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r3 = "http://content.mqcdn.com/mobile/android/";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r2 = r2.append(r7);	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r2 = r2.toString();	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r0 = com.mapquest.android.maps.HttpUtil.executeAsStream(r2);	 Catch:{ Exception -> 0x005c, all -> 0x008f }
            r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
            r2 = new byte[r2];	 Catch:{ Exception -> 0x005c, all -> 0x0095 }
        L_0x0051:
            r3 = r0.read(r2);	 Catch:{ Exception -> 0x005c, all -> 0x0095 }
            if (r3 <= 0) goto L_0x0068;
        L_0x0057:
            r4 = 0;
            r1.write(r2, r4, r3);	 Catch:{ Exception -> 0x005c, all -> 0x0095 }
            goto L_0x0051;
        L_0x005c:
            r2 = move-exception;
        L_0x005d:
            if (r1 == 0) goto L_0x0062;
        L_0x005f:
            r1.close();	 Catch:{ Exception -> 0x0087 }
        L_0x0062:
            if (r0 == 0) goto L_0x0067;
        L_0x0064:
            r0.close();	 Catch:{ Exception -> 0x0089 }
        L_0x0067:
            return;
        L_0x0068:
            if (r1 == 0) goto L_0x006d;
        L_0x006a:
            r1.close();	 Catch:{ Exception -> 0x0085 }
        L_0x006d:
            if (r0 == 0) goto L_0x0067;
        L_0x006f:
            r0.close();	 Catch:{ Exception -> 0x0073 }
            goto L_0x0067;
        L_0x0073:
            r0 = move-exception;
            goto L_0x0067;
        L_0x0075:
            r1 = move-exception;
            r2 = r0;
            r5 = r0;
            r0 = r1;
            r1 = r5;
        L_0x007a:
            if (r2 == 0) goto L_0x007f;
        L_0x007c:
            r2.close();	 Catch:{ Exception -> 0x008b }
        L_0x007f:
            if (r1 == 0) goto L_0x0084;
        L_0x0081:
            r1.close();	 Catch:{ Exception -> 0x008d }
        L_0x0084:
            throw r0;
        L_0x0085:
            r1 = move-exception;
            goto L_0x006d;
        L_0x0087:
            r1 = move-exception;
            goto L_0x0062;
        L_0x0089:
            r0 = move-exception;
            goto L_0x0067;
        L_0x008b:
            r2 = move-exception;
            goto L_0x007f;
        L_0x008d:
            r1 = move-exception;
            goto L_0x0084;
        L_0x008f:
            r2 = move-exception;
            r5 = r2;
            r2 = r1;
            r1 = r0;
            r0 = r5;
            goto L_0x007a;
        L_0x0095:
            r2 = move-exception;
            r5 = r2;
            r2 = r1;
            r1 = r0;
            r0 = r5;
            goto L_0x007a;
        L_0x009b:
            r1 = move-exception;
            r1 = r0;
            goto L_0x005d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.MapView.AssetUpdater.updateFile(java.lang.String):void");
        }

        private boolean needsUpdating(File file) {
            return file.lastModified() < this.staleDate;
        }
    }

    public class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public static final int BOTTOM = 32;
        public static final int BOTTOM_CENTER = 33;
        public static final int CENTER = 3;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_VERTICAL = 2;
        public static final int LEFT = 4;
        public static final int MODE_MAP = 0;
        public static final int MODE_VIEW = 1;
        public static final int RIGHT = 8;
        public static final int TOP = 16;
        public static final int TOP_LEFT = 20;
        public int alignment = 3;
        public int mode = 1;
        public GeoPoint point;
        public int x = ProgressType.PRO_END;
        public int y = ProgressType.PRO_END;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.x = attributeSet.getAttributeIntValue(MapViewConstants.MAPQUEST_NAMESPACE, MapViewConstants.ATTR_X, ProgressType.PRO_END);
            this.y = attributeSet.getAttributeIntValue(MapViewConstants.MAPQUEST_NAMESPACE, MapViewConstants.ATTR_X, ProgressType.PRO_END);
            String attributeValue = attributeSet.getAttributeValue(MapViewConstants.MAPQUEST_NAMESPACE, MapViewConstants.ATTR_GEO_POINT);
            if (attributeValue != null && attributeValue.length() > 0) {
                String[] split = attributeValue.split(",");
                if (split.length > 1) {
                    try {
                        this.point = new GeoPoint(Double.parseDouble(split[0].trim()), Double.parseDouble(split[1].trim()));
                        this.mode = 0;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            if (layoutParams instanceof LayoutParams) {
                LayoutParams layoutParams2 = (LayoutParams) layoutParams;
                this.x = layoutParams2.x;
                this.y = layoutParams2.y;
                this.point = layoutParams2.point;
                this.mode = layoutParams2.mode;
                this.alignment = layoutParams2.alignment;
            }
        }

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3) {
            super(i, i2);
            this.point = geoPoint;
            this.alignment = i3;
            if (geoPoint != null) {
                this.mode = 0;
            }
        }

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3, int i4, int i5) {
            super(i, i2);
            this.point = geoPoint;
            this.x = i3;
            this.y = i4;
            this.alignment = i5;
            if (geoPoint != null) {
                this.mode = 0;
            }
        }

        public LayoutParams(int i, int i2, int i3, int i4, int i5) {
            super(i, i2);
            this.x = i3;
            this.y = i4;
            this.alignment = i5;
        }
    }

    class MapEventHandler extends Handler {
        public void handleMessage(Message message) {
            if (!MapView.this.isPause) {
                switch (message.what) {
                    case 11:
                        if (MapView.this.currentScale != 1.0f) {
                            int round;
                            if (MapView.this.scaling) {
                                round = (int) Math.round(((double) MapView.this.zoomLevel) + Util.log2((double) MapView.this.currentScale));
                            } else {
                                round = MapView.this.zoomLevel;
                            }
                            if (MapView.this.validateZoomLevel(round)) {
                                int access$200 = MapView.this.zoomLevel;
                                if (!(MapView.this.scalePoint.x == MapView.this.focalPoint.x && MapView.this.scalePoint.y == MapView.this.focalPoint.y)) {
                                    MapView.this.zoomLevel = round;
                                    MapView.this.centerGeoPoint = MapView.this.getProjection().fromPixels(MapView.this.scalePoint.x, MapView.this.scalePoint.y);
                                    round = MapView.this.focalPoint.x + (MapView.this.focalPoint.x - MapView.this.scalePoint.x);
                                    int i = MapView.this.focalPoint.y + (MapView.this.focalPoint.y - MapView.this.scalePoint.y);
                                    MapView.this.centerGeoPoint = MapView.this.getProjection().fromPixels(round, i);
                                }
                                if (MapView.this.scaling) {
                                    MapView.this.zoomLevel = access$200;
                                } else {
                                    MapView.this.zoomLevel = (int) Math.round(((double) MapView.this.zoomLevel) - Util.log2((double) MapView.this.currentScale));
                                }
                            }
                        }
                        MapView.this.scaling = true;
                        return;
                    case 21:
                    case 22:
                        MapView.this.moved();
                        return;
                    case 23:
                    case 33:
                        MapView.this.preLoad();
                        MapView.this.moved();
                        return;
                    case 61:
                        MapView.this.preLoad();
                        MapView.this.postInvalidate();
                        return;
                    case MapView.PRELOAD /*31459*/:
                        MapView.this.preLoad();
                        MapView.this.postInvalidate();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    class Reticle {
        static Paint paint;

        private Reticle() {
        }

        public static void draw(Canvas canvas, View view, Point point) {
            int min = Math.min(view.getWidth(), view.getHeight()) / 10;
            int i = (min * 2) + 10;
            int i2 = point.x - (i >> 1);
            int i3 = point.y - (i >> 1);
            canvas.drawCircle((float) point.x, (float) point.y, (float) min, getPaint());
            canvas.drawLine((float) point.x, (float) i3, (float) point.x, (float) (i3 + i), paint);
            canvas.drawLine((float) i2, (float) point.y, (float) (i2 + i), (float) point.y, paint);
        }

        private static Paint getPaint() {
            if (paint == null) {
                paint = new Paint(1);
                paint.setDither(true);
                paint.setStyle(Style.STROKE);
                paint.setColor(-7829368);
                paint.setStrokeWidth(1.0f);
                paint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f}, 1.0f));
            }
            return paint;
        }
    }

    public enum ReticleDrawMode {
        DRAW_RETICLE_NEVER,
        DRAW_RETICLE_OVER,
        DRAW_RETICLE_UNDER
    }

    public MapView(Context context) {
        super(context);
        this.isPause = false;
        this.builtInZoomControls = true;
        this.logo = true;
        this.showTerms = true;
        this.zoomLevel = 2;
        this.centerGeoPoint = new GeoPoint(Util.to1E6(39.833322799504d), Util.to1E6(-98.583068847656d));
        this.scaling = false;
        this.defaultTilePaint = new Paint(1);
        this.customTilePaint = new Paint(1);
        this.firedMapLoaded = false;
        this.tileCount = 0;
        this.totalTileCount = 0;
        this.currentScale = 1.0f;
        this.scalePoint = new Point();
        this.rotateDegrees = 0.0f;
        this.animators = new LinkedBlockingQueue();
        this.provider = MapProvider.MAPQUEST;
        this.reticalMode = ReticleDrawMode.DRAW_RETICLE_NEVER;
        this.mapEventCallback = null;
        this.logoDrawable = null;
        this.topMargin = 0;
        this.bottomMargin = 0;
        this.rightMargin = 0;
        this.leftMargin = 0;
        this.mapViewEventListeners = new HashMap();
        this.focalPoint = new Point();
        this.rotRect = new Rect();
        this.visibleRect = new Rect();
        initialize(context, (String) null);
    }

    public MapView(Context context, String str) {
        super(context);
        this.isPause = false;
        this.builtInZoomControls = true;
        this.logo = true;
        this.showTerms = true;
        this.zoomLevel = 2;
        this.centerGeoPoint = new GeoPoint(Util.to1E6(39.833322799504d), Util.to1E6(-98.583068847656d));
        this.scaling = false;
        this.defaultTilePaint = new Paint(1);
        this.customTilePaint = new Paint(1);
        this.firedMapLoaded = false;
        this.tileCount = 0;
        this.totalTileCount = 0;
        this.currentScale = 1.0f;
        this.scalePoint = new Point();
        this.rotateDegrees = 0.0f;
        this.animators = new LinkedBlockingQueue();
        this.provider = MapProvider.MAPQUEST;
        this.reticalMode = ReticleDrawMode.DRAW_RETICLE_NEVER;
        this.mapEventCallback = null;
        this.logoDrawable = null;
        this.topMargin = 0;
        this.bottomMargin = 0;
        this.rightMargin = 0;
        this.leftMargin = 0;
        this.mapViewEventListeners = new HashMap();
        this.focalPoint = new Point();
        this.rotRect = new Rect();
        this.visibleRect = new Rect();
        initialize(context, str);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isPause = false;
        this.builtInZoomControls = true;
        this.logo = true;
        this.showTerms = true;
        this.zoomLevel = 2;
        this.centerGeoPoint = new GeoPoint(Util.to1E6(39.833322799504d), Util.to1E6(-98.583068847656d));
        this.scaling = false;
        this.defaultTilePaint = new Paint(1);
        this.customTilePaint = new Paint(1);
        this.firedMapLoaded = false;
        this.tileCount = 0;
        this.totalTileCount = 0;
        this.currentScale = 1.0f;
        this.scalePoint = new Point();
        this.rotateDegrees = 0.0f;
        this.animators = new LinkedBlockingQueue();
        this.provider = MapProvider.MAPQUEST;
        this.reticalMode = ReticleDrawMode.DRAW_RETICLE_NEVER;
        this.mapEventCallback = null;
        this.logoDrawable = null;
        this.topMargin = 0;
        this.bottomMargin = 0;
        this.rightMargin = 0;
        this.leftMargin = 0;
        this.mapViewEventListeners = new HashMap();
        this.focalPoint = new Point();
        this.rotRect = new Rect();
        this.visibleRect = new Rect();
        String str = "";
        for (int i2 = 0; i2 < attributeSet.getAttributeCount(); i2++) {
            if (MapViewConstants.ATTR_API_KEY.equals(attributeSet.getAttributeName(i2))) {
                if (attributeSet.getAttributeResourceValue(i2, -1) == -1) {
                    str = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", MapViewConstants.ATTR_API_KEY);
                } else {
                    str = context.getString(attributeSet.getAttributeResourceValue(i2, 0));
                }
            }
        }
        initialize(context, str);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    void initialize(Context context, String str) {
        if (context instanceof MapActivity) {
            ((MapActivity) context).addMapView(this);
        }
        this.context = context;
        this.configuration = new Configuration(this);
        this.mapController = new MapController(this);
        this.mapEventCallback = new MapEventHandler();
        EventDispatcher.registerHandler(this.mapEventCallback);
        this.tileCacher = new TileCacher(context);
        this.tileProvider = new ThreadBasedTileDownloader(this, this.tileCacher);
        this.mapConfiguration = new MapConfiguration(true, this);
        if (str == null || str.length() <= 0) {
            setMapProvider(MapProvider.OSM);
        } else {
            this.configuration.setApiKey(str);
            this.configuration.setPlatformApiKey(str);
            setMapProvider(MapProvider.MAPQUEST);
        }
        setMapCenter(this.centerGeoPoint, this.zoomLevel);
        this.defaultTilePaint.setDither(true);
        this.defaultTilePaint.setFilterBitmap(true);
        this.customTilePaint.setDither(true);
        this.customTilePaint.setFilterBitmap(true);
        setFocusable(true);
        setBackgroundColor(MAP_VIEW_BACKGROUND);
        this.eventHandler = new TouchEventHandler(this);
        this.rotatableProjection = new RotatableProjection(this, this.tileFactory.getProjection());
        this.overlayController = new OverlayController(this);
        this.networkConnectivityListener = new NetworkConnectivityListener(context);
        this.networkConnectivityListener.startListening();
        this.logoDrawable = Util.getDrawable(context, "logo");
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.width = defaultDisplay.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        this.height = defaultDisplay.getHeight() - ((int) ((25.0d * ((double) displayMetrics.density)) + 0.5d));
        new AssetUpdater().start();
    }

    void queueAnimator(Animator animator) {
        this.animators.add(animator);
        postInvalidate();
    }

    void clearAnimators() {
        this.animators.clear();
    }

    void setMapFactory(MapProvider mapProvider) {
        if (mapProvider != getMapProvider()) {
            if (mapProvider.equals(MapProvider.OSM)) {
                this.tileFactory = new MapQuestOSMTileFactory(this, this.mapConfiguration);
            } else if (mapProvider.equals(MapProvider.MAPQUEST)) {
                this.tileFactory = new MapQuestTileFactory(this, this.mapConfiguration);
            } else if (mapProvider.equals(MapProvider.TOMTOM)) {
                this.tileFactory = new TomTomTileFactory(this, this.mapConfiguration);
            }
            this.rotatableProjection = new RotatableProjection(this, this.tileFactory.getProjection());
            ITileCache cache = this.tileCacher.getCache(CacheType.MEMORY);
            if (cache != null) {
                cache.clear();
            }
            if (getWidth() > 0 && getHeight() > 0) {
                postInvalidate();
            }
        }
    }

    void setLogoShown(boolean z) {
        this.logo = z;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i > 0 || i2 > 0) {
            if (this.rotateDegrees != 0.0f) {
                setMapRotation(this.rotateDegrees);
            } else {
                this.width = getWidth();
                this.height = getHeight();
            }
            this.focalPoint.set(getWidth() >> 1, getHeight() >> 1);
            this.scalePoint.x = this.focalPoint.x;
            this.scalePoint.y = this.focalPoint.y;
            if (getTileCacher() != null) {
                getTileCacher().checkCacheSize(this.height, this.width);
            }
            if (!this.firedMapLoaded) {
                this.firedMapLoaded = true;
                EventDispatcher.sendEmptyMessage(1);
            }
            EventDispatcher.sendEmptyMessage(5);
            preLoad();
        }
    }

    private ZoomControls createZoomControls() {
        if (this.zoomControls == null) {
            this.zoomControls = new ZoomControls(this.context);
            this.zoomControls.setZoomSpeed(2000);
            this.zoomControls.setOnZoomInClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MapView.this.mapController.zoomIn();
                }
            });
            this.zoomControls.setOnZoomOutClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MapView.this.mapController.zoomOut();
                }
            });
        }
        if (this.builtInZoomControls) {
            this.zoomControls.setVisibility(4);
        }
        return this.zoomControls;
    }

    private ZoomButtonsController createZoomButtonsController() {
        if (this.zoomButtonsController == null) {
            this.zoomButtonsController = new ZoomButtonsController(this);
            this.zoomButtonsController.setZoomSpeed(2000);
            this.zoomButtonsController.setOnZoomListener(new OnZoomListener() {
                public void onZoom(boolean z) {
                    if (z) {
                        MapView.this.mapController.zoomIn();
                    } else {
                        MapView.this.mapController.zoomOut();
                    }
                }

                public void onVisibilityChanged(boolean z) {
                }
            });
        }
        return this.zoomButtonsController;
    }

    protected void setMapCenter(GeoPoint geoPoint, int i) {
        synchronized (this) {
            this.zoomLevel = i;
            this.centerGeoPoint = geoPoint;
        }
        updateZoomControls();
        if (getWidth() != 0) {
            postInvalidate();
        }
    }

    private void updateZoomControls() {
        boolean z = false;
        boolean z2 = this.zoomLevel < this.tileFactory.getMaxZoomLevel();
        if (this.zoomLevel > this.tileFactory.getMinZoomLevel()) {
            z = true;
        }
        if (this.zoomControls != null) {
            this.zoomControls.setIsZoomInEnabled(z2);
            this.zoomControls.setIsZoomInEnabled(z);
        }
        if (this.builtInZoomControls && this.zoomButtonsController != null) {
            ZoomControls zoomControls = (ZoomControls) this.zoomButtonsController.getZoomControls();
            zoomControls.setIsZoomInEnabled(z2);
            zoomControls.setIsZoomOutEnabled(z);
        }
    }

    public Point getFocalPoint() {
        return new Point(this.focalPoint);
    }

    public void setFocalPoint(Point point) {
        this.focalPoint.set(point.x, point.y);
        this.rotatableProjection.setRotate(this.rotateDegrees, point.x, point.y);
        moved();
        EventDispatcher.sendEmptyMessage(23);
        postInvalidate();
    }

    void addTile(Tile tile) {
        if (this.tileFactory != null && tile.getZoomLevel() == this.zoomLevel) {
            if (this.tileFactory.getTileType() == TileType.MAP) {
                if (tile.getTileType() != TileType.MAP) {
                    return;
                }
            } else if (tile.getTileType() == TileType.MAP) {
                return;
            }
            if (tile.getBitmap() == null || tile.getBitmap().isRecycled()) {
                this.tileCacher.getCache(CacheType.MEMORY).removeTile(tile);
            } else {
                postInvalidate();
            }
        }
    }

    private void drawTile(Tile tile, Canvas canvas, boolean z) {
        if (this.tileFactory != null && tile.getZoomLevel() == this.zoomLevel) {
            Tile tile2 = this.tileCacher.getCache(CacheType.MEMORY).getTile(tile);
            this.totalTileCount++;
            Bitmap bitmap = null;
            if (tile2 != null) {
                bitmap = tile2.getBitmap();
                if (bitmap == null || bitmap.isRecycled()) {
                    this.tileCacher.getCache(CacheType.MEMORY).removeTile(tile);
                    return;
                }
                this.tileCount++;
            } else if (z && tile.getTileType() != TileType.HYB) {
                bitmap = getLoadingTile();
            }
            Rect rect = tile.getRect();
            if (rect != null && canvas != null && bitmap != null) {
                Paint paint;
                Rect rect2 = new Rect(0, 0, 256, 256);
                if (tile.getTileType() == TileType.MAP) {
                    paint = this.customTilePaint;
                } else {
                    paint = this.defaultTilePaint;
                }
                canvas.drawBitmap(bitmap, rect2, rect, paint);
            }
        }
    }

    private int drawTiles(Canvas canvas, GeoPoint geoPoint, int i, boolean z) {
        int i2 = 0;
        if (!(getWidth() == 0 || getHeight() == 0 || this.tileCacher == null || this.tileFactory == null || i == 0)) {
            int i3 = this.zoomLevel;
            GeoPoint geoPoint2 = this.centerGeoPoint;
            if (i != this.zoomLevel && i >= 0) {
                this.zoomLevel = i;
            }
            if (geoPoint != this.centerGeoPoint) {
                this.centerGeoPoint = geoPoint;
            }
            int i4 = this.zoomLevel;
            this.tileCount = 0;
            this.totalTileCount = 0;
            try {
                if (this.tileFactory.getTileType() == TileType.HYB) {
                    iterateTiles(i4, TileType.SAT, false, canvas, z);
                }
                iterateTiles(i4, this.tileFactory.getTileType(), false, canvas, z);
                i2 = this.tileCount;
                if (i3 != this.zoomLevel) {
                    this.zoomLevel = i3;
                }
                if (geoPoint2 != this.centerGeoPoint) {
                    this.centerGeoPoint = geoPoint2;
                }
            } catch (Throwable th) {
                if (i3 != this.zoomLevel) {
                    this.zoomLevel = i3;
                }
                if (geoPoint2 != this.centerGeoPoint) {
                    this.centerGeoPoint = geoPoint2;
                }
            }
        }
        return i2;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void iterateTiles(int r18, com.mapquest.android.maps.TileType r19, boolean r20, android.graphics.Canvas r21, boolean r22) {
        /*
        r17 = this;
        r3 = r17.getWidth();
        if (r3 == 0) goto L_0x0152;
    L_0x0006:
        r3 = r17.getHeight();
        if (r3 == 0) goto L_0x0152;
    L_0x000c:
        r0 = r17;
        r3 = r0.tileCacher;
        if (r3 == 0) goto L_0x0152;
    L_0x0012:
        r0 = r17;
        r3 = r0.tileFactory;
        if (r3 == 0) goto L_0x0152;
    L_0x0018:
        r3 = r17.getZoomLevel();
        if (r3 == 0) goto L_0x0152;
    L_0x001e:
        r0 = r17;
        r3 = r0.focalPoint;
        r12 = r3.y;
        r0 = r17;
        r3 = r0.focalPoint;
        r13 = r3.x;
        r0 = r17;
        r3 = r0.tileFactory;
        r4 = r3.getTileSize();
        r9 = 0;
        r8 = 0;
        r11 = new android.graphics.Point;
        r11.<init>();
        r0 = r17;
        r3 = r0.rotRect;
        r5 = 0;
        r6 = 0;
        r7 = r17.getWidth();
        r10 = r17.getHeight();
        r3.set(r5, r6, r7, r10);
        r0 = r17;
        r3 = r0.rotateDegrees;
        r5 = 0;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 == 0) goto L_0x005e;
    L_0x0053:
        r0 = r17;
        r3 = r0.rotatableProjection;
        r0 = r17;
        r5 = r0.rotRect;
        r3.rotateMapRect(r5);
    L_0x005e:
        r10 = 0;
    L_0x005f:
        if (r9 == 0) goto L_0x0063;
    L_0x0061:
        if (r8 != 0) goto L_0x0152;
    L_0x0063:
        r6 = 0;
        r5 = 0;
        r7 = 0;
        r3 = r10 % 2;
        if (r3 != 0) goto L_0x00bb;
    L_0x006a:
        r3 = -r4;
    L_0x006b:
        r14 = r10 / 2;
        r15 = r10 % 2;
        r14 = r14 + r15;
        r3 = r3 * r14;
        r14 = r12 + r3;
        r3 = 0;
    L_0x0074:
        if (r6 == 0) goto L_0x0078;
    L_0x0076:
        if (r5 != 0) goto L_0x015b;
    L_0x0078:
        r7 = r3 % 2;
        if (r7 != 0) goto L_0x00bd;
    L_0x007c:
        r7 = -r4;
    L_0x007d:
        r15 = r3 / 2;
        r16 = r3 % 2;
        r15 = r15 + r16;
        r7 = r7 * r15;
        r7 = r7 + r13;
        r0 = r17;
        r15 = r0.rotatableProjection;
        r11 = r15.offsetFromFocalPoint(r7, r14, r11);
        r0 = r17;
        r7 = r0.tileFactory;
        r15 = r11.x;
        r0 = r11.y;
        r16 = r0;
        r0 = r16;
        r1 = r18;
        r2 = r19;
        r7 = r7.buildTile(r15, r0, r1, r2);
        if (r7 != 0) goto L_0x00bf;
    L_0x00a3:
        r5 = r7;
        r7 = r11;
    L_0x00a5:
        if (r5 != 0) goto L_0x012c;
    L_0x00a7:
        r3 = r10 % 2;
        if (r3 != 0) goto L_0x0128;
    L_0x00ab:
        r3 = 1;
        r5 = r9;
    L_0x00ad:
        r6 = r10 % 2;
        if (r6 == 0) goto L_0x014c;
    L_0x00b1:
        if (r3 == 0) goto L_0x0153;
    L_0x00b3:
        r6 = r10 + 1;
    L_0x00b5:
        r10 = r6 + 1;
        r11 = r7;
        r8 = r3;
        r9 = r5;
        goto L_0x005f;
    L_0x00bb:
        r3 = r4;
        goto L_0x006b;
    L_0x00bd:
        r7 = r4;
        goto L_0x007d;
    L_0x00bf:
        r0 = r17;
        r15 = r0.rotatableProjection;
        r16 = r7.getRect();
        r15.offsetToFocalPoint(r16);
        r0 = r17;
        r15 = r0.rotRect;
        r16 = r7.getRect();
        r15 = android.graphics.Rect.intersects(r15, r16);
        if (r15 == 0) goto L_0x00df;
    L_0x00d8:
        if (r20 == 0) goto L_0x0119;
    L_0x00da:
        r0 = r17;
        r0.queueTile(r7);
    L_0x00df:
        r15 = r3 % 2;
        if (r15 == 0) goto L_0x0123;
    L_0x00e3:
        if (r5 == 0) goto L_0x00e7;
    L_0x00e5:
        r3 = r3 + 1;
    L_0x00e7:
        r15 = r7.getRect();
        r15 = r15.left;
        r0 = r17;
        r0 = r0.rotRect;
        r16 = r0;
        r0 = r16;
        r0 = r0.left;
        r16 = r0;
        r0 = r16;
        if (r15 >= r0) goto L_0x00fe;
    L_0x00fd:
        r5 = 1;
    L_0x00fe:
        r15 = r7.getRect();
        r15 = r15.right;
        r0 = r17;
        r0 = r0.rotRect;
        r16 = r0;
        r0 = r16;
        r0 = r0.right;
        r16 = r0;
        r0 = r16;
        if (r15 <= r0) goto L_0x0115;
    L_0x0114:
        r6 = 1;
    L_0x0115:
        r3 = r3 + 1;
        goto L_0x0074;
    L_0x0119:
        r0 = r17;
        r1 = r21;
        r2 = r22;
        r0.drawTile(r7, r1, r2);
        goto L_0x00df;
    L_0x0123:
        if (r6 == 0) goto L_0x00e7;
    L_0x0125:
        r3 = r3 + 1;
        goto L_0x00e7;
    L_0x0128:
        r3 = 1;
        r5 = r3;
        r3 = r8;
        goto L_0x00ad;
    L_0x012c:
        r3 = r5.getRect();
        r3 = r3.top;
        r0 = r17;
        r6 = r0.rotRect;
        r6 = r6.top;
        if (r3 >= r6) goto L_0x0159;
    L_0x013a:
        r3 = 1;
    L_0x013b:
        r5 = r5.getRect();
        r5 = r5.bottom;
        r0 = r17;
        r6 = r0.rotRect;
        r6 = r6.bottom;
        if (r5 <= r6) goto L_0x0156;
    L_0x0149:
        r5 = 1;
        goto L_0x00ad;
    L_0x014c:
        if (r5 == 0) goto L_0x0153;
    L_0x014e:
        r6 = r10 + 1;
        goto L_0x00b5;
    L_0x0152:
        return;
    L_0x0153:
        r6 = r10;
        goto L_0x00b5;
    L_0x0156:
        r5 = r9;
        goto L_0x00ad;
    L_0x0159:
        r3 = r8;
        goto L_0x013b;
    L_0x015b:
        r5 = r7;
        r7 = r11;
        goto L_0x00a5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.MapView.iterateTiles(int, com.mapquest.android.maps.TileType, boolean, android.graphics.Canvas, boolean):void");
    }

    private void queueTile(Tile tile) {
        this.totalTileCount++;
        if (this.tileCacher.getCache(CacheType.MEMORY).getTile(tile) == null) {
            this.tileProvider.queueTile(tile);
        }
    }

    void preLoadDelayed(long j) {
        this.mapEventCallback.sendEmptyMessageDelayed(PRELOAD, j);
    }

    public void preLoad() {
        if (getWidth() != 0 && getHeight() != 0 && this.tileCacher != null && this.tileFactory != null && getZoomLevel() != 0) {
            if (this.tileProvider == null) {
                this.tileProvider = new ThreadBasedTileDownloader(this, getTileCacher());
            }
            this.tileProvider.beginQueue();
            try {
                if (this.tileFactory.getTileType() == TileType.HYB && this.tileFactory.isSupportedTileType(TileType.SAT)) {
                    iterateTiles(getZoomLevel(), TileType.SAT, true, (Canvas) null, false);
                }
                if (this.tileFactory.isSupportedTileType(this.tileFactory.getTileType())) {
                    iterateTiles(getZoomLevel(), this.tileFactory.getTileType(), true, (Canvas) null, false);
                }
                this.tileProvider.endQueue();
            } catch (Throwable th) {
                this.tileProvider.endQueue();
            }
        }
    }

    boolean validateZoomLevel(int i) {
        return i <= this.tileFactory.getMaxZoomLevel() && i >= this.tileFactory.getMinZoomLevel();
    }

    void setZoomLevel(int i) {
        if (i > this.tileFactory.getMaxZoomLevel()) {
            i = this.tileFactory.getMaxZoomLevel();
        }
        if (i < this.tileFactory.getMinZoomLevel()) {
            i = this.tileFactory.getMinZoomLevel();
        }
        setMapCenter(this.centerGeoPoint, i);
    }

    void zoomToSpan(int i, int i2) {
        int i3 = i >> 1;
        int i4 = i2 >> 1;
        zoomToSpan(new BoundingBox(new GeoPoint(this.centerGeoPoint.getLatitudeE6() + i3, ((this.centerGeoPoint.getLongitudeE6() - i4) + Util.to1E6(360.0d)) % Util.to1E6(360.0d)), new GeoPoint(this.centerGeoPoint.getLatitudeE6() - i3, (i4 + this.centerGeoPoint.getLongitudeE6()) % Util.to1E6(360.0d))), false);
    }

    void zoomToSpan(BoundingBox boundingBox, boolean z) {
        if (this.zoomLevel < 6) {
            this.zoomLevel = 6;
        }
        Rect visibleRegion = visibleRegion();
        int calculateZoomLevel = ((ProjectionX) getProjection()).calculateZoomLevel(boundingBox, getWidth() - visibleRegion.width(), getHeight() - visibleRegion.height());
        int centerY;
        if (validateZoomLevel(calculateZoomLevel)) {
            EventDispatcher.sendEmptyMessage(11);
            if (z) {
                EventDispatcher.sendEmptyMessage(21);
                this.centerGeoPoint = boundingBox.getCenter();
                EventDispatcher.sendEmptyMessage(23);
            }
            this.zoomLevel = calculateZoomLevel;
            if (z) {
                this.centerGeoPoint = boundingBox.getCenter();
                calculateZoomLevel = this.focalPoint.x + (this.focalPoint.x - visibleRegion.centerX());
                centerY = (this.focalPoint.y - visibleRegion.centerY()) + this.focalPoint.y;
                EventDispatcher.sendEmptyMessage(21);
                this.centerGeoPoint = getProjection().fromPixels(calculateZoomLevel, centerY);
                EventDispatcher.sendEmptyMessage(23);
            }
            EventDispatcher.sendEmptyMessage(12);
        } else if (z) {
            this.centerGeoPoint = boundingBox.getCenter();
            calculateZoomLevel = this.focalPoint.x + (this.focalPoint.x - visibleRegion.centerX());
            centerY = (this.focalPoint.y - visibleRegion.centerY()) + this.focalPoint.y;
            EventDispatcher.sendEmptyMessage(21);
            this.centerGeoPoint = getProjection().fromPixels(calculateZoomLevel, centerY);
            EventDispatcher.sendEmptyMessage(23);
        }
    }

    public Overlay getOverlayByKey(String str) {
        if (getOverlays() == null) {
            return null;
        }
        for (Overlay overlay : getOverlays()) {
            if (overlay.getKey().equals(str)) {
                return overlay;
            }
        }
        return null;
    }

    public void removeOverlayByKey(String str) {
        Overlay overlayByKey = getOverlayByKey(str);
        if (overlayByKey != null) {
            getOverlays().remove(overlayByKey);
        }
        postInvalidate();
    }

    MapProvider getMapProvider() {
        return this.tileFactory != null ? this.tileFactory.getMapProvider() : null;
    }

    public BoundingBox getBoundingBox(BoundingBox boundingBox) {
        if (boundingBox == null) {
            boundingBox = new BoundingBox();
        }
        Projection projection = getProjection();
        if (projection == null) {
            return null;
        }
        int width = getWidth();
        int height = getHeight();
        if (width == 0 && height == 0) {
            width = this.width;
            height = this.height;
        }
        GeoPoint fromPixels = projection.fromPixels(0, 0);
        GeoPoint fromPixels2 = projection.fromPixels(width, height);
        if (this.rotateDegrees != 0.0f) {
            GeoPoint fromPixels3 = projection.fromPixels(width, 0);
            GeoPoint fromPixels4 = projection.fromPixels(0, height);
            GeoPoint[] geoPointArr = new GeoPoint[]{fromPixels, fromPixels3, fromPixels4, fromPixels2};
            int to1E6 = Util.to1E6(180.0d);
            int to1E62 = Util.to1E6(-180.0d);
            int to1E63 = Util.to1E6(-90.0d);
            width = Util.to1E6(90.0d);
            for (height = 0; height < geoPointArr.length; height++) {
                if (geoPointArr[height].getLongitudeE6() < to1E6) {
                    to1E6 = geoPointArr[height].getLongitudeE6();
                }
                if (geoPointArr[height].getLongitudeE6() > to1E62) {
                    to1E62 = geoPointArr[height].getLongitudeE6();
                }
                if (geoPointArr[height].getLatitudeE6() > to1E63) {
                    to1E63 = geoPointArr[height].getLatitudeE6();
                }
                if (geoPointArr[height].getLatitudeE6() < width) {
                    width = geoPointArr[height].getLatitudeE6();
                }
            }
            boundingBox.ul = new GeoPoint(to1E63, to1E6);
            boundingBox.lr = new GeoPoint(width, to1E62);
            return boundingBox;
        }
        boundingBox.ul = fromPixels;
        boundingBox.lr = fromPixels2;
        return boundingBox;
    }

    public boolean canCoverCenter() {
        int mapWidth = getMapWidth() >> 1;
        int mapHeight = getMapHeight() >> 1;
        if (this.tileFactory.getTileType() != TileType.HYB) {
            if (this.tileCacher.getCache(CacheType.MEMORY).getTile(this.tileFactory.buildTile(mapWidth, mapHeight, getZoomLevel(), this.tileFactory.getTileType())) != null) {
                return true;
            }
            return false;
        }
        Tile tile = this.tileCacher.getCache(CacheType.MEMORY).getTile(this.tileFactory.buildTile(mapWidth, mapHeight, getZoomLevel(), TileType.HYB));
        if (this.tileCacher.getCache(CacheType.MEMORY).getTile(this.tileFactory.buildTile(mapWidth, mapHeight, getZoomLevel(), TileType.SAT)) == null || tile == null) {
            return false;
        }
        return true;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void displayZoomControls(boolean z) {
        if (this.builtInZoomControls) {
            this.zoomButtonsController = createZoomButtonsController();
            if (z) {
                this.zoomButtonsController.setFocusable(true);
                this.zoomButtonsController.setVisible(true);
            }
        } else if (this.zoomControls != null && z) {
            this.zoomControls.setFocusable(true);
            this.zoomControls.show();
            this.zoomControls.requestFocus();
        }
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, new GeoPoint(0.0d, 0.0d), 3);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public MapController getController() {
        return this.mapController;
    }

    public int getLatitudeSpan() {
        return Math.abs(getProjection().fromPixels(0, getHeight()).getLatitudeE6() - getProjection().fromPixels(0, 0).getLatitudeE6());
    }

    public int getLongitudeSpan() {
        int longitudeE6 = getProjection().fromPixels(0, 0).getLongitudeE6();
        int longitudeE62 = getProjection().fromPixels(getWidth(), 0).getLongitudeE6();
        return longitudeE62 < longitudeE6 ? (longitudeE62 + Util.to1E6(360.0d)) - longitudeE6 : Math.abs(longitudeE62 - longitudeE6);
    }

    public List<Overlay> getOverlays() {
        return this.overlayController.getOverlays();
    }

    public GeoPoint getMapCenter() {
        return new GeoPoint(this.centerGeoPoint.getLatitudeE6(), this.centerGeoPoint.getLongitudeE6());
    }

    public int getMaxZoomLevel() {
        return this.tileFactory.getMaxZoomLevel();
    }

    public Projection getProjection() {
        return this.rotatableProjection;
    }

    public int getZoomLevel() {
        return this.zoomLevel;
    }

    public View getZoomControls() {
        if (this.zoomControls == null) {
            this.zoomControls = createZoomControls();
        }
        if (this.builtInZoomControls) {
            this.zoomControls.setVisibility(4);
        }
        return this.zoomControls;
    }

    public boolean isSatellite() {
        return this.configuration.isSatellite();
    }

    public boolean isStreetView() {
        return false;
    }

    public boolean isTraffic() {
        return this.trafficManager == null ? false : this.trafficManager.isEnabled();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.zoomButtonsController != null && this.builtInZoomControls) {
            this.zoomButtonsController.setVisible(true);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.zoomButtonsController != null) {
            this.zoomButtonsController.setVisible(false);
        }
    }

    public float getMapRotation() {
        return this.rotateDegrees;
    }

    void setMapRotation(float f) {
        if (this.rotateDegrees != f) {
            float f2 = (f + 360.0f) % 360.0f;
            this.rotateDegrees = f2;
            this.rotatableProjection.setRotate(f2, this.focalPoint.x, this.focalPoint.y);
            this.rotRect.set(0, 0, getWidth(), getHeight());
            this.rotatableProjection.rotateMapRect(this.rotRect);
            this.width = this.rotRect.width();
            this.height = this.rotRect.height();
            moved();
            postInvalidate();
        }
    }

    public int getMapWidth() {
        return this.width == 0 ? getWidth() : this.width;
    }

    public int getMapHeight() {
        return this.height == 0 ? getHeight() : this.height;
    }

    public void setTileLayerPaint(Paint paint) {
        if (paint == null) {
            paint = new Paint();
        }
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        this.customTilePaint = paint;
    }

    protected final void onDraw(Canvas canvas) {
        if (getProjection() != null && getHeight() != 0 && getWidth() != 0 && canvas != null) {
            try {
                Rect clipBounds = canvas.getClipBounds();
                if (clipBounds.width() > 10 && clipBounds.height() > 10) {
                    if (this.animators.size() > 0) {
                        if (clipBounds.width() == getWidth() && clipBounds.height() == getHeight()) {
                            if (!((Animator) this.animators.peek()).animate()) {
                                this.animators.poll();
                            }
                            if (this.animators.size() > 0) {
                                invalidate();
                            }
                        } else {
                            invalidate();
                        }
                    }
                    canvas.save();
                    if (this.rotateDegrees != 0.0f) {
                        Object obj;
                        if (clipBounds.width() == getWidth() && clipBounds.height() == getHeight()) {
                            obj = null;
                        } else {
                            canvas.save(2);
                            canvas.clipRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), Op.REPLACE);
                            obj = 1;
                        }
                        canvas.rotate(this.rotateDegrees, (float) this.focalPoint.x, (float) this.focalPoint.y);
                        canvas.getClipBounds(clipBounds);
                        if (obj != null) {
                            canvas.restore();
                        }
                    }
                    if (this.currentScale != 1.0f) {
                        int i;
                        canvas.save();
                        Point mapPoint = this.rotatableProjection.mapPoint(this.scalePoint.x, this.scalePoint.y, (Point) null);
                        this.rotatableProjection.offsetToFocalPoint(mapPoint.x, mapPoint.y, mapPoint);
                        canvas.scale(this.currentScale, this.currentScale, (float) mapPoint.x, (float) mapPoint.y);
                        if (this.scaling) {
                            i = this.zoomLevel;
                        } else {
                            i = (int) Math.round(((double) this.zoomLevel) - Util.log2((double) this.currentScale));
                        }
                        GeoPoint geoPoint = this.centerGeoPoint;
                        if (!(this.scalePoint.x == this.focalPoint.x && this.scalePoint.y == this.focalPoint.y) && validateZoomLevel(i)) {
                            int i2 = this.zoomLevel;
                            GeoPoint geoPoint2 = this.centerGeoPoint;
                            this.centerGeoPoint = getProjection().fromPixels(this.scalePoint.x, this.scalePoint.y);
                            this.zoomLevel = i;
                            geoPoint = getProjection().fromPixels(this.focalPoint.x + (this.focalPoint.x - this.scalePoint.x), this.focalPoint.y + (this.focalPoint.y - this.scalePoint.y));
                            this.zoomLevel = i2;
                            this.centerGeoPoint = geoPoint2;
                        }
                        drawTiles(canvas, geoPoint, i, true);
                        canvas.restore();
                    }
                    if (!this.scaling || this.currentScale == 1.0f) {
                        boolean z = this.currentScale == 1.0f;
                        if (this.currentScale != 1.0f) {
                            preLoad();
                        }
                        if (((float) drawTiles(canvas, this.centerGeoPoint, this.zoomLevel, z)) >= GUARANTEE_TILELOAD_PERCENTAGE * ((float) this.totalTileCount) && this.currentScale != 1.0f) {
                            setScale(1.0f, 1.0f, (float) this.focalPoint.x, (float) this.focalPoint.y);
                        }
                    }
                    canvas.restore();
                    if (this.reticalMode == ReticleDrawMode.DRAW_RETICLE_UNDER) {
                        Reticle.draw(canvas, this, this.focalPoint);
                    }
                    renderOverlays(canvas);
                    if (this.reticalMode == ReticleDrawMode.DRAW_RETICLE_OVER) {
                        Reticle.draw(canvas, this, this.focalPoint);
                    }
                    if (this.logo) {
                        this.logoDrawable.draw(canvas);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void renderOverlays(Canvas canvas) {
        try {
            if (this.currentScale != 1.0f && this.scaling) {
                canvas.save(1);
                canvas.scale(this.currentScale, this.currentScale, (float) this.scalePoint.x, (float) this.scalePoint.y);
            }
            if (this.trafficManager != null) {
                this.trafficManager.draw(canvas, this);
            }
            this.overlayController.renderOverlays(canvas, this);
        } finally {
            if (this.currentScale != 1.0f && this.scaling) {
                canvas.restore();
            }
        }
    }

    private Point scalePoint(int i, int i2, Point point) {
        if (this.scaling) {
            Point point2 = this.scalePoint;
            point.y = (int) ((((float) point2.y) + (((float) (i2 - point2.y)) * this.currentScale)) + GUARANTEE_TILELOAD_PERCENTAGE);
            point.x = (int) (((((float) (i - point2.x)) * this.currentScale) + ((float) point2.x)) + GUARANTEE_TILELOAD_PERCENTAGE);
        } else {
            point.x = i;
            point.y = i2;
        }
        return point;
    }

    private void redoLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        Point point = new Point();
        int height = getHeight();
        int width = getWidth();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8 && (childAt.getLayoutParams() instanceof LayoutParams)) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mode != 0) {
                    point.x = layoutParams.x;
                    point.y = layoutParams.y;
                } else if (layoutParams.point == null) {
                    point.x = layoutParams.x;
                    point.y = layoutParams.y;
                } else {
                    point = getProjection().toPixels(layoutParams.point, point);
                    if (this.currentScale != 1.0f) {
                        point = scalePoint(point.x, point.y, point);
                    }
                    point.x = (layoutParams.x != ProgressType.PRO_END ? layoutParams.x : 0) + point.x;
                    point.y = (layoutParams.y != ProgressType.PRO_END ? layoutParams.y : 0) + point.y;
                }
                int i6 = layoutParams.alignment;
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i7 = point.x != ProgressType.PRO_END ? point.x : width >> 1;
                int i8 = point.y != ProgressType.PRO_END ? point.y : height >> 1;
                int i9 = i7 + measuredWidth;
                int i10 = i8 + measuredHeight;
                int paddingLeft = getPaddingLeft() - getPaddingRight();
                int paddingTop = getPaddingTop() - getPaddingRight();
                int i11 = i6;
                i6 = i7;
                i7 = i8;
                i8 = 0;
                while (i11 != 0) {
                    int i12 = i8 + 1;
                    if (i8 >= 3) {
                        break;
                    } else if ((i11 & 32) == 32) {
                        if (point.y == ProgressType.PRO_END) {
                            i7 = height;
                        }
                        i11 ^= 32;
                        i10 = i7;
                        i7 -= measuredHeight;
                        i8 = i12;
                    } else if ((i11 & 16) == 16) {
                        if (point.y == ProgressType.PRO_END) {
                            i7 = 0;
                        }
                        i11 ^= 16;
                        i10 = i7 + measuredHeight;
                        i8 = i12;
                    } else if ((i11 & 8) == 8) {
                        if (point.x != ProgressType.PRO_END) {
                            i8 = point.x;
                        } else {
                            i8 = width;
                        }
                        i11 ^= 8;
                        i6 = i8 - measuredWidth;
                        i9 = i8;
                        i8 = i12;
                    } else if ((i11 & 4) == 4) {
                        i8 = point.x != ProgressType.PRO_END ? point.x : 0;
                        i10 = i8 + measuredWidth;
                        i11 ^= 4;
                        i6 = i8;
                        i8 = i12;
                    } else if ((i11 & 1) == 1) {
                        i9 = i6 - (measuredWidth >> 1);
                        i11 ^= 1;
                        i6 = i9;
                        i9 += measuredWidth;
                        i8 = i12;
                    } else if ((i11 & 2) == 2) {
                        i8 = i7 - (measuredHeight >> 1);
                        i11 ^= 2;
                        i10 = i8 + measuredHeight;
                        i7 = i8;
                        i8 = i12;
                    } else {
                        i8 = i12;
                    }
                }
                childAt.layout(i6 + paddingLeft, i7 + paddingTop, i9 + paddingLeft, i10 + paddingTop);
            }
        }
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        redoLayout(z, i, i2, i3, i4);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        measureChildren(i, i2);
        int resolveSize = resolveSize(getMeasuredWidth(), i);
        int resolveSize2 = resolveSize(getMeasuredHeight(), i2);
        setMeasuredDimension(resolveSize, resolveSize2);
        if (this.logoDrawable != null) {
            resolveSize2 -= 5;
            this.logoDrawable.setBounds(5, resolveSize2 - this.logoDrawable.getIntrinsicHeight(), this.logoDrawable.getIntrinsicWidth() + 5, resolveSize2);
        }
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.overlayController.onKeyDown(i, keyEvent, this)) {
            return true;
        }
        if (i == 19) {
            getController().scrollBy(0, getHeight() / 4);
            return true;
        } else if (i == 20) {
            getController().scrollBy(0, -(getHeight() / 4));
            return true;
        } else if (i == 22) {
            getController().scrollBy(getWidth() / 4, 0);
            return true;
        } else if (i != 21) {
            return false;
        } else {
            getController().scrollBy(-(getWidth() / 4), 0);
            return true;
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.overlayController.onKeyUp(i, keyEvent, this);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle.containsKey(MapViewConstants.STATE_CENTER_LAT) && bundle.containsKey(MapViewConstants.STATE_CENTER_LNG)) {
            this.centerGeoPoint = new GeoPoint(bundle.getInt(MapViewConstants.STATE_CENTER_LAT), bundle.getInt(MapViewConstants.STATE_CENTER_LNG));
        }
        if (bundle.containsKey(MapViewConstants.STATE_ZOOM_LEVEL)) {
            int i = bundle.getInt(MapViewConstants.STATE_ZOOM_LEVEL);
            if (validateZoomLevel(i)) {
                this.zoomLevel = i;
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.centerGeoPoint != null) {
            bundle.putInt(MapViewConstants.STATE_CENTER_LAT, this.centerGeoPoint.getLatitudeE6());
            bundle.putInt(MapViewConstants.STATE_CENTER_LNG, this.centerGeoPoint.getLongitudeE6());
        }
        bundle.putInt(MapViewConstants.STATE_ZOOM_LEVEL, this.zoomLevel);
    }

    boolean handleOverlayEvent(MotionEvent motionEvent) {
        return this.overlayController.onTouchEvent(motionEvent, this);
    }

    boolean onTap(GeoPoint geoPoint) {
        return this.overlayController.onTap(geoPoint, this);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        ((InputMethodManager) this.context.getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        if (isClickable() && isEnabled()) {
            if (!this.builtInZoomControls || this.zoomButtonsController == null) {
                if (this.zoomControls != null) {
                    this.zoomControls.show();
                }
            } else if (!this.zoomButtonsController.isVisible()) {
                this.zoomButtonsController.setVisible(true);
            }
            requestFocus();
            if (handleOverlayEvent(motionEvent)) {
                return true;
            }
            if (this.eventHandler != null && this.eventHandler.handleTouchEvent(motionEvent)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (this.overlayController.onTrackballEvent(motionEvent, this)) {
            return true;
        }
        if (this.trackBallHandler == null) {
            this.trackBallHandler = new DefaultTrackBallHandler(this);
        }
        return this.trackBallHandler.handleTrackballEvent(motionEvent);
    }

    public void setBuiltInZoomControls(boolean z) {
        this.builtInZoomControls = z;
        if (z) {
            this.zoomButtonsController = createZoomButtonsController();
            this.zoomButtonsController.setAutoDismissed(true);
            if (this.zoomControls != null) {
                this.zoomControls.setVisibility(4);
                return;
            }
            return;
        }
        if (this.zoomButtonsController != null) {
            this.zoomButtonsController.setVisible(false);
        }
        if (this.zoomControls != null) {
            this.zoomControls.setVisibility(0);
        }
    }

    public void setReticleDrawMode(ReticleDrawMode reticleDrawMode) {
        this.reticalMode = reticleDrawMode;
    }

    public void setSatellite(boolean z) {
        this.configuration.setSatellite(z);
    }

    void setSatellite(boolean z, boolean z2) {
        if (this.tileFactory != null || this.tileProvider != null || this.tileCacher != null) {
            TileType tileType = this.configuration.isSatellite() ? this.configuration.isSatelliteLabeled() ? TileType.SATHYB : TileType.SAT : TileType.MAP;
            if (this.tileFactory != null) {
                this.tileFactory.setType(tileType);
            }
            if (this.tileProvider != null) {
                this.tileProvider.clearQueue();
            }
            if (getWidth() > 0 && getHeight() > 0) {
                preLoad();
                postInvalidate();
            }
        }
    }

    public void setStreetView(boolean z) {
    }

    public void setTraffic(boolean z) {
        if (z) {
            this.trafficManager = new TrafficManager(this, getConfiguration().getTrafficURL(), this.configuration.getPlatformApiKey());
            this.trafficManager.setTraffic(z);
        } else if (this.trafficManager != null) {
            this.trafficManager.destroy();
            this.trafficManager = null;
            invalidate();
        }
    }

    void setMapProvider(MapProvider mapProvider) {
        if (mapProvider.equals(MapProvider.OSM) || getConfiguration().getApiKey() == null) {
            this.tileFactory = new MapQuestOSMTileFactory(this, this.mapConfiguration);
        } else if (mapProvider.equals(MapProvider.TOMTOM)) {
            this.tileFactory = new TomTomTileFactory(this, this.mapConfiguration);
        } else {
            this.tileFactory = new MapQuestTileFactory(this, this.mapConfiguration);
        }
        this.rotatableProjection = new RotatableProjection(this, this.tileFactory.getProjection());
        setSatellite(this.configuration.isSatellite(), this.configuration.isSatelliteLabeled());
    }

    void setScale(float f, float f2, float f3, float f4) {
        this.currentScale = f;
        this.scalePoint.x = (int) f3;
        this.scalePoint.y = (int) f4;
        moved();
    }

    public void destroy() {
        this.isPause = true;
        if (this.trafficManager != null) {
            this.trafficManager.destroy();
            this.trafficManager = null;
        }
        if (this.mapController != null) {
            this.mapController.destroy();
            this.mapController = null;
        }
        if (this.tileProvider != null) {
            this.tileProvider.destroy();
            this.tileProvider = null;
        }
        if (this.tileCacher != null) {
            this.tileCacher.destroy();
            this.tileCacher = null;
        }
        this.tileFactory = null;
        this.overlayController.destroy();
        if (this.eventHandler != null) {
            if (this.eventHandler instanceof TouchEventHandler) {
                this.eventHandler.destroy();
            }
            this.eventHandler = null;
        }
        if (this.networkConnectivityListener != null) {
            this.networkConnectivityListener.stopListening();
            this.networkConnectivityListener = null;
        }
        if (this.zoomButtonsController != null) {
            this.zoomButtonsController.setVisible(false);
            this.zoomButtonsController = null;
            this.builtInZoomControls = false;
        }
        if (this.mapEventCallback != null) {
            EventDispatcher.removeHandler(this.mapEventCallback);
            this.mapEventCallback = null;
        }
        this.rotatableProjection = null;
        this.zoomControls = null;
        if (this.loadingTile != null) {
            this.loadingTile.recycle();
            this.loadingTile = null;
        }
        destroyDrawingCache();
        if (this.context instanceof MapActivity) {
            ((MapActivity) this.context).removeMapView(this);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    android.graphics.Bitmap getLoadingTile() {
        /*
        r2 = this;
        r0 = r2.loadingTile;
        if (r0 != 0) goto L_0x0018;
    L_0x0004:
        r0 = "/com/mapquest/android/maps/loading.jpg";
        r1 = r2.getClass();
        r0 = r1.getResourceAsStream(r0);
        r0 = android.graphics.BitmapFactory.decodeStream(r0);
        r2.loadingTile = r0;
        r0 = r2.loadingTile;
        if (r0 != 0) goto L_0x0018;
    L_0x0018:
        r0 = r2.loadingTile;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.MapView.getLoadingTile():android.graphics.Bitmap");
    }

    public void setLoadingTile(Bitmap bitmap) {
        this.loadingTile = bitmap;
    }

    void moved() {
        redoLayout(true, getLeft(), getTop(), getWidth() + getLeft(), getHeight() + getTop());
    }

    void zoomStart() {
        this.scaling = true;
    }

    void zoomEnd() {
        this.scaling = false;
        preLoad();
    }

    public void clearTilesInMemory() {
        if (this.tileCacher != null) {
            ITileCache cache = this.tileCacher.getCache(CacheType.MEMORY);
            if (cache != null) {
                cache.clear();
            }
        }
    }

    Rect visibleRegion() {
        this.visibleRect.set(this.leftMargin, this.topMargin, getWidth() - this.rightMargin, getHeight() - this.bottomMargin);
        return this.visibleRect;
    }

    public int getTopMargin() {
        return this.topMargin;
    }

    public void setTopMargin(int i) {
        this.topMargin = i;
    }

    public int getBottomMargin() {
        return this.bottomMargin;
    }

    public void setBottomMargin(int i) {
        this.bottomMargin = i;
    }

    public int getRightMargin() {
        return this.rightMargin;
    }

    public void setRightMargin(int i) {
        this.rightMargin = i;
    }

    public int getLeftMargin() {
        return this.leftMargin;
    }

    public void setLeftMargin(int i) {
        this.leftMargin = i;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    TileCacher getTileCacher() {
        return this.tileCacher;
    }

    public void onResume() {
        this.isPause = false;
        if (this.tileProvider == null) {
            this.tileProvider = new ThreadBasedTileDownloader(this, this.tileCacher);
        }
        preLoad();
    }

    public void onPause() {
        this.isPause = true;
        if (this.tileProvider != null) {
            this.tileProvider.destroy();
            this.tileProvider = null;
        }
    }

    public void onStop() {
        this.isPause = true;
        if (this.tileCacher != null) {
            ITileCache cache = this.tileCacher.getCache(CacheType.MEMORY);
            if (cache != null) {
                cache.clear();
            }
        }
    }

    public void addMapViewEventListener(final MapViewEventListener mapViewEventListener) {
        Handler handler = new Handler(getContext().getApplicationContext().getMainLooper(), new Callback() {
            public boolean handleMessage(Message message) {
                if (!MapView.this.isPause) {
                    switch (message.what) {
                        case 1:
                            mapViewEventListener.mapLoaded(MapView.this);
                            break;
                        case 3:
                            mapViewEventListener.touch(MapView.this);
                            break;
                        case 4:
                            Bundle data = message.getData();
                            Point point = null;
                            if (data != null) {
                                MotionEvent motionEvent = (MotionEvent) data.getParcelable(SpeechEvent.KEY_EVENT_RECORD_DATA);
                                point = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            }
                            mapViewEventListener.longTouch(MapView.this, point);
                            break;
                        case 11:
                            mapViewEventListener.zoomStart(MapView.this);
                            break;
                        case 12:
                            mapViewEventListener.zoomEnd(MapView.this);
                            break;
                        case 21:
                            mapViewEventListener.moveStart(MapView.this);
                            break;
                        case 22:
                            mapViewEventListener.move(MapView.this);
                            break;
                        case 23:
                            mapViewEventListener.moveEnd(MapView.this);
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        this.mapViewEventListeners.put(mapViewEventListener, handler);
        EventDispatcher.registerHandler(handler);
    }

    public void removeMapViewEventListener(MapViewEventListener mapViewEventListener) {
        if (this.mapViewEventListeners.containsKey(mapViewEventListener)) {
            EventDispatcher.removeHandler((Handler) this.mapViewEventListeners.get(mapViewEventListener));
        }
    }

    private static int getSdkVersion() {
        return new Integer(VERSION.SDK).intValue();
    }

    TileFactory getTileFactory() {
        return this.tileFactory;
    }
}
