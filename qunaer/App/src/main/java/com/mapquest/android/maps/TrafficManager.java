package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import java.io.InputStream;

class TrafficManager {
    private static final int FLOW_PADDING = 40;
    private static final String TAG = TrafficManager.class.getName();
    private String baseUrl;
    private TrafficImage currentImage;
    private boolean enabled = false;
    private FlowDownloadTask flowDownloadTask;
    private TrafficOverlay flowOverlay;
    private Handler handler;
    private boolean inFlight = false;
    private String key;
    private boolean keyValid = false;
    private TrafficImage queuedRequest = null;
    private MapView view;

    class FlowDownloadTask extends AsyncTask<Void, Void, Drawable> {
        private TrafficManager manager;
        private TrafficImage trafficImage;

        public FlowDownloadTask(TrafficImage trafficImage, TrafficManager trafficManager) {
            this.manager = trafficManager;
            this.trafficImage = trafficImage;
        }

        protected Drawable doInBackground(Void... voidArr) {
            InputStream executeAsStream;
            InputStream inputStream;
            Throwable th;
            InputStream inputStream2 = null;
            try {
                executeAsStream = HttpUtil.executeAsStream(this.trafficImage.getUrl());
                try {
                    Drawable bitmapDrawable = new BitmapDrawable(executeAsStream);
                    try {
                        executeAsStream.close();
                        return bitmapDrawable;
                    } catch (Exception e) {
                        return bitmapDrawable;
                    }
                } catch (OutOfMemoryError e2) {
                    inputStream = executeAsStream;
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                    }
                    return null;
                } catch (Exception e4) {
                    try {
                        executeAsStream.close();
                    } catch (Exception e5) {
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = executeAsStream;
                    try {
                        inputStream2.close();
                    } catch (Exception e6) {
                    }
                    throw th;
                }
            } catch (OutOfMemoryError e7) {
                inputStream = null;
                inputStream.close();
                return null;
            } catch (Exception e8) {
                executeAsStream = null;
                executeAsStream.close();
                return null;
            } catch (Throwable th3) {
                th = th3;
                inputStream2.close();
                throw th;
            }
        }

        protected void onPostExecute(Drawable drawable) {
            if (!(this.trafficImage == null || drawable == null)) {
                this.trafficImage.setDrawable(drawable);
            }
            this.manager.imageReceived(this.trafficImage);
        }
    }

    class TrafficImage {
        private BoundingBox bbox;
        private Drawable drawable;
        private String url;

        public TrafficImage(String str, BoundingBox boundingBox) {
            this.url = str;
            this.bbox = boundingBox;
        }

        public Drawable getDrawable() {
            return this.drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public BoundingBox getBoundingBox() {
            return this.bbox;
        }

        public void setBoundingBox(BoundingBox boundingBox) {
            this.bbox = boundingBox;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    class TrafficOverlay extends DrawableOverlay {
        private int minZoomLevel;

        private TrafficOverlay() {
            this.minZoomLevel = -1;
        }

        public int getMinZoomLevel() {
            return this.minZoomLevel;
        }

        public void setMinZoomLevel(int i) {
            this.minZoomLevel = i;
        }

        public void setDrawable(Drawable drawable, BoundingBox boundingBox) {
            drawable.setAlpha(180);
            super.setDrawable(drawable, boundingBox);
        }

        public void draw(Canvas canvas, MapView mapView, boolean z) {
            if (this.minZoomLevel != -1 && mapView.getZoomLevel() >= this.minZoomLevel) {
                super.draw(canvas, mapView, z);
            }
        }
    }

    public TrafficManager(MapView mapView, String str, String str2) {
        this.view = mapView;
        this.baseUrl = str;
        this.key = str2;
        if (str2 != null && str2.length() > 0) {
            this.keyValid = true;
        }
        this.handler = new Handler(new Callback() {
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 5:
                        TrafficManager.this.refreshFlow();
                        return true;
                    case 12:
                        if (TrafficManager.this.flowOverlay != null) {
                            TrafficManager.this.flowOverlay.destroy();
                        }
                        TrafficManager.this.refreshFlow();
                        return true;
                    case 23:
                        TrafficManager.this.handleMove();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    protected void setApiKey(String str) {
        this.key = str;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void destroy() {
        removeEventListener();
        this.view = null;
    }

    public void refreshFlow() {
        if (this.view.getMapProvider() != null && this.view.getMapProvider() == MapProvider.OSM) {
            return;
        }
        if (this.view.getZoomLevel() >= this.view.getConfiguration().getTrafficMinimumZoomLevel()) {
            BoundingBox createBoundingBoxFromRect = createBoundingBoxFromRect(new Rect(-40, -40, this.view.getWidth() + 40, this.view.getHeight() + 40), this.view);
            if (createBoundingBoxFromRect != null) {
                Rect createOriginRectFromBoundingBox = Util.createOriginRectFromBoundingBox(createBoundingBoxFromRect, this.view);
                if (this.view != null && this.view.getMapProvider() != MapProvider.OSM) {
                    retrieveImage(constructUrl(createBoundingBoxFromRect, createOriginRectFromBoundingBox.width(), createOriginRectFromBoundingBox.height()), createBoundingBoxFromRect);
                    return;
                }
                return;
            }
            return;
        }
        this.flowOverlay.destroy();
        this.view.invalidate();
    }

    public String constructUrl(BoundingBox boundingBox, int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.baseUrl);
        stringBuilder.append("/flow?key=" + this.key);
        GeoPoint center = boundingBox.getCenter();
        int scaleFactor = ((ProjectionX) this.view.getProjection()).getScaleFactor(this.view.getZoomLevel());
        stringBuilder.append("&mapLat=" + Util.from1E6(center.getLatitudeE6()));
        stringBuilder.append("&mapLng=" + Util.from1E6(center.getLongitudeE6()));
        stringBuilder.append("&mapHeight=" + i2);
        stringBuilder.append("&mapWidth=" + i);
        stringBuilder.append("&mapScale=" + scaleFactor);
        stringBuilder.append("&imageType=png");
        stringBuilder.append("&projection=merc");
        stringBuilder.append("&sdk=" + Util.getApiVersion());
        return stringBuilder.toString();
    }

    private void retrieveImage(String str, BoundingBox boundingBox) {
        if (this.view != null && this.view.getContext() != null) {
            TrafficImage trafficImage = new TrafficImage(str, boundingBox);
            if (this.inFlight) {
                this.queuedRequest = trafficImage;
                return;
            }
            this.inFlight = true;
            this.flowDownloadTask = new FlowDownloadTask(trafficImage, this);
            this.flowDownloadTask.execute((Void[]) null);
        }
    }

    public void addEventListener() {
        EventDispatcher.registerHandler(this.handler);
    }

    public void removeEventListener() {
        EventDispatcher.removeHandler(this.handler);
    }

    protected void imageReceived(TrafficImage trafficImage) {
        this.inFlight = false;
        if (trafficImage.getDrawable() != null && this.flowOverlay != null) {
            if (trafficImage.getDrawable().getIntrinsicWidth() > 1 || trafficImage.getDrawable().getIntrinsicHeight() > 1) {
                if (this.queuedRequest == null || trafficImage.getBoundingBox().contains(this.queuedRequest.getBoundingBox())) {
                    this.queuedRequest = null;
                } else {
                    retrieveImage(this.queuedRequest.getUrl(), this.queuedRequest.getBoundingBox());
                    this.queuedRequest = null;
                }
                this.currentImage = trafficImage;
                this.flowOverlay.setDrawable(trafficImage.getDrawable(), trafficImage.getBoundingBox());
                if (this.view != null) {
                    this.view.invalidate();
                }
            }
        }
    }

    public void setTraffic(boolean z) {
        if (z && this.keyValid) {
            this.flowOverlay = new TrafficOverlay();
            this.flowOverlay.setMinZoomLevel(this.view.getConfiguration().getTrafficMinimumZoomLevel());
            addEventListener();
            refreshFlow();
            this.enabled = true;
            return;
        }
        if (this.flowOverlay != null) {
            this.flowOverlay.destroy();
        }
        this.enabled = false;
        removeEventListener();
        this.view.invalidate();
    }

    public void draw(Canvas canvas, MapView mapView) {
        if (this.enabled) {
            this.flowOverlay.draw(canvas, mapView, false);
        }
    }

    protected BoundingBox createBoundingBoxFromRect(Rect rect, MapView mapView) {
        if (mapView.getProjection() == null) {
            return null;
        }
        if (rect == null) {
            return new BoundingBox(new GeoPoint(0, 0), new GeoPoint(0, 0));
        }
        try {
            return Util.createBoundingBoxFromRect(rect, mapView);
        } catch (Exception e) {
            return new BoundingBox(new GeoPoint(0, 0), new GeoPoint(0, 0));
        }
    }

    private void handleMove() {
        BoundingBox createBoundingBoxFromRect = createBoundingBoxFromRect(new Rect(0, 0, this.view.getWidth(), this.view.getHeight()), this.view);
        if (this.currentImage == null || !this.currentImage.getBoundingBox().contains(createBoundingBoxFromRect)) {
            refreshFlow();
        }
    }
}
