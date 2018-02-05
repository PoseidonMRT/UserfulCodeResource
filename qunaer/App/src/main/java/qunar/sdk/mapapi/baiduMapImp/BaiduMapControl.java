package qunar.sdk.mapapi.baiduMapImp;

import android.graphics.Point;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarLocationConfigeration;
import qunar.sdk.mapapi.QunarMapControl;
import qunar.sdk.mapapi.baiduMapImp.BaiduMapStrategy.OnMapLoadListener;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;

public class BaiduMapControl implements QunarMapControl {
    private BaiduMap bdMap;
    private BaiduMapStrategy bdMapStrategy;

    public BaiduMapControl(BaiduMapStrategy baiduMapStrategy) {
        this.bdMapStrategy = baiduMapStrategy;
        this.bdMap = baiduMapStrategy.getBaiduMap();
    }

    public void setMapCenter(List<QLocation> list, boolean z, int i) {
        if (list != null && list.size() != 0) {
            Builder builder = new Builder();
            Builder builder2 = builder;
            for (QLocation qLocation : list) {
                builder2 = builder2.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            LatLngBounds build = builder2.build();
            setMapCenter(new QLocation(build.getCenter().latitude, build.getCenter().longitude), z, i);
        }
    }

    public void setMapCenter(final QLocation qLocation, final boolean z, final int i) {
        if (qLocation != null) {
            List executeListener = this.bdMapStrategy.executeListener();
            synchronized (executeListener) {
                if (this.bdMapStrategy.isMapLoaded()) {
                    mapCenter(qLocation, z, i);
                } else {
                    executeListener.add(new OnMapLoadListener() {
                        public void onMapLoad() {
                            BaiduMapControl.this.mapCenter(qLocation, z, i);
                        }
                    });
                }
            }
        }
    }

    public void mapCenterTo(int i, int i2, boolean z) {
        if (this.bdMap != null) {
            MapStatus mapStatus = this.bdMap.getMapStatus();
            MapStatusUpdate newMapStatus = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().overlook(mapStatus.overlook).rotate(mapStatus.rotate).targetScreen(new Point(i, i2)).build());
            if (z) {
                this.bdMap.animateMapStatus(newMapStatus);
            } else {
                this.bdMap.setMapStatus(newMapStatus);
            }
        }
    }

    private void mapCenter(QLocation qLocation, boolean z, int i) {
        if (this.bdMap != null) {
            MapStatusUpdate newLatLng = MapStatusUpdateFactory.newLatLng(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            if (z) {
                this.bdMap.animateMapStatus(newLatLng, i);
            } else {
                this.bdMap.setMapStatus(newLatLng);
            }
        }
    }

    public void setMapBounds(List<QLocation> list, boolean z, int i) {
        setMapBounds(list, 0, 0, false, 0);
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z) {
        setMapBounds(list, i, i2, z, 300);
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z, int i3) {
        if (list != null && !list.isEmpty() && this.bdMapStrategy != null && this.bdMap != null) {
            Builder builder = new Builder();
            for (QLocation qLocation : list) {
                builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            LatLngBounds build = builder.build();
            List executeListener = this.bdMapStrategy.executeListener();
            synchronized (executeListener) {
                if (this.bdMapStrategy.isMapLoaded()) {
                    mapBounds(build, i, i2, z, i3);
                } else {
                    final LatLngBounds latLngBounds = build;
                    final int i4 = i;
                    final int i5 = i2;
                    final boolean z2 = z;
                    final int i6 = i3;
                    executeListener.add(new OnMapLoadListener() {
                        public void onMapLoad() {
                            BaiduMapControl.this.mapBounds(latLngBounds, i4, i5, z2, i6);
                        }
                    });
                }
            }
        }
    }

    private void mapBounds(LatLngBounds latLngBounds, int i, int i2, boolean z, int i3) {
        if (this.bdMap != null) {
            MapStatusUpdate newLatLngBounds;
            if (i <= 0 || i2 <= 0) {
                newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(latLngBounds);
            } else {
                newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(latLngBounds, i, i2);
            }
            if (z) {
                this.bdMap.animateMapStatus(newLatLngBounds, i3);
            } else {
                this.bdMap.setMapStatus(newLatLngBounds);
            }
        }
    }

    public void setMapCenterZoom(QLocation qLocation, float f, boolean z, int i) {
        if (qLocation != null && this.bdMap != null && this.bdMap != null) {
            MapStatusUpdate newLatLngZoom = MapStatusUpdateFactory.newLatLngZoom(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()), f);
            if (z) {
                this.bdMap.animateMapStatus(newLatLngZoom, i);
            } else {
                this.bdMap.setMapStatus(newLatLngZoom);
            }
        }
    }

    public void overlook(float f, boolean z, int i) {
        if (this.bdMap != null) {
            MapStatus.Builder builder = new MapStatus.Builder(this.bdMap.getMapStatus());
            builder.overlook(f);
            MapStatusUpdate newMapStatus = MapStatusUpdateFactory.newMapStatus(builder.build());
            if (z) {
                this.bdMap.animateMapStatus(newMapStatus, i);
            } else {
                this.bdMap.setMapStatus(newMapStatus);
            }
        }
    }

    public void rotate(float f, boolean z, int i) {
        if (this.bdMap != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.rotate(f);
            MapStatusUpdate newMapStatus = MapStatusUpdateFactory.newMapStatus(builder.build());
            if (z) {
                this.bdMap.animateMapStatus(newMapStatus, i);
            } else {
                this.bdMap.setMapStatus(newMapStatus);
            }
        }
    }

    public void zoomBy(float f, boolean z, int i) {
        if (this.bdMap != null) {
            MapStatusUpdate zoomBy = MapStatusUpdateFactory.zoomBy(f);
            if (z) {
                this.bdMap.animateMapStatus(zoomBy, i);
            } else {
                this.bdMap.setMapStatus(zoomBy);
            }
        }
    }

    public void zoomIn() {
        if (this.bdMap != null) {
            this.bdMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
        }
    }

    public void zoomOut() {
        if (this.bdMap != null) {
            this.bdMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
        }
    }

    public void zoomTo(float f, boolean z, int i) {
        if (this.bdMap != null) {
            MapStatusUpdate zoomTo = MapStatusUpdateFactory.zoomTo(f);
            if (z) {
                this.bdMap.animateMapStatus(zoomTo, i);
            } else {
                this.bdMap.setMapStatus(zoomTo);
            }
        }
    }

    public void setAllGesturesEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setAllGesturesEnabled(z);
        }
    }

    public void setCompassEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setCompassEnabled(z);
        }
    }

    public void setOverlookingGesturesEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setOverlookingGesturesEnabled(z);
        }
    }

    public void setRotateGesturesEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setRotateGesturesEnabled(z);
        }
    }

    public void setScrollGesturesEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setScrollGesturesEnabled(z);
        }
    }

    public void setZoomGesturesEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.getUiSettings().setZoomGesturesEnabled(z);
        }
    }

    public void setMyLocationEnabled(boolean z) {
        if (this.bdMap != null) {
            this.bdMap.setMyLocationEnabled(z);
        }
    }

    public void setMyLocationConfigeration(QunarLocationConfigeration qunarLocationConfigeration) {
        if (this.bdMap != null) {
            LocationMode locationMode;
            switch (qunarLocationConfigeration) {
                case COMPASS:
                    locationMode = LocationMode.COMPASS;
                    break;
                case NORMAL:
                    locationMode = LocationMode.NORMAL;
                    break;
                case FOLLOWING:
                    locationMode = LocationMode.FOLLOWING;
                    break;
                default:
                    locationMode = LocationMode.NORMAL;
                    break;
            }
            this.bdMap.setMyLocationConfigeration(new MyLocationConfiguration(locationMode, true, null));
        }
    }

    @Deprecated
    public void destroy(String str) {
        if (str != null && TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str) && this.bdMap != null) {
            this.bdMap = null;
        }
    }
}
