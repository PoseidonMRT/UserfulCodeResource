package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

class OverlayController {
    private MapView mapView;
    public List<Overlay> overlays = null;

    class OverlayArrayList extends ArrayList<Overlay> {
        private static final long serialVersionUID = -1622579671240580437L;

        private OverlayArrayList() {
        }

        public void clear() {
            Iterator it = iterator();
            while (it.hasNext()) {
                ((Overlay) it.next()).destroy();
            }
            super.clear();
        }

        public Overlay remove(int i) {
            Overlay overlay = (Overlay) super.remove(i);
            overlay.destroy();
            return overlay;
        }

        public boolean remove(Object obj) {
            if (obj instanceof List) {
                for (Overlay destroy : (List) obj) {
                    destroy.destroy();
                }
            } else if (obj instanceof Overlay) {
                ((Overlay) obj).destroy();
            }
            return super.remove(obj);
        }

        protected void removeRange(int i, int i2) {
            for (int i3 = i; i3 <= i2; i3++) {
                ((Overlay) get(i3)).destroy();
            }
            super.removeRange(i, i2);
        }

        public void add(int i, Overlay overlay) {
            checkOverlayAdd(overlay);
            super.add(i, overlay);
            sort();
        }

        public boolean add(Overlay overlay) {
            checkOverlayAdd(overlay);
            boolean add = super.add(overlay);
            sort();
            return add;
        }

        public boolean addAll(Collection<? extends Overlay> collection) {
            boolean addAll = super.addAll(collection);
            sort();
            return addAll;
        }

        public boolean addAll(int i, Collection<? extends Overlay> collection) {
            checkOverlays(collection);
            boolean addAll = super.addAll(i, collection);
            sort();
            return addAll;
        }

        private void sort() {
            Collections.sort(this, new Comparator() {
                public int compare(Object obj, Object obj2) {
                    int zIndex = ((Overlay) obj).getZIndex();
                    int zIndex2 = ((Overlay) obj2).getZIndex();
                    if (zIndex == zIndex2) {
                        return 0;
                    }
                    return zIndex < zIndex2 ? -1 : 1;
                }
            });
            EventDispatcher.sendEmptyMessage(41);
        }

        private void checkOverlays(Collection<? extends Overlay> collection) {
            for (Overlay checkOverlayAdd : collection) {
                checkOverlayAdd(checkOverlayAdd);
            }
        }

        private void checkOverlayAdd(Overlay overlay) {
            if (overlay.getKey() != null && overlay.getKey().length() != 0) {
                Object overlayByKey = getOverlayByKey(overlay.getKey());
                if (overlayByKey != null) {
                    remove(overlayByKey);
                }
            }
        }

        public Overlay getOverlayByKey(String str) {
            Iterator it = iterator();
            while (it.hasNext()) {
                Overlay overlay = (Overlay) it.next();
                if (overlay.getKey().equals(str)) {
                    return overlay;
                }
            }
            return null;
        }
    }

    public OverlayController(MapView mapView) {
        this.mapView = mapView;
        this.overlays = Collections.synchronizedList(new OverlayArrayList());
    }

    public List<Overlay> getOverlays() {
        return this.overlays;
    }

    void setBackedList(List<Overlay> list) {
        this.overlays = list;
    }

    public void renderOverlays(Canvas canvas, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay draw : this.overlays) {
                    try {
                        draw.draw(canvas, mapView, true, mapView.getDrawingTime());
                    } catch (Exception e) {
                    }
                }
                for (Overlay draw2 : this.overlays) {
                    try {
                        draw2.draw(canvas, mapView, false, mapView.getDrawingTime());
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay onKeyDown : this.overlays) {
                    if (onKeyDown.onKeyDown(i, keyEvent, mapView)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay onKeyUp : this.overlays) {
                    if (onKeyUp.onKeyUp(i, keyEvent, mapView)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay onTap : this.overlays) {
                    if (onTap.onTap(geoPoint, mapView)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay onTouchEvent : this.overlays) {
                    if (onTouchEvent.onTouchEvent(motionEvent, mapView)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.overlays.size() > 0) {
            List list = this.overlays;
            synchronized (this.overlays) {
                for (Overlay onTrackballEvent : this.overlays) {
                    if (onTrackballEvent.onTrackballEvent(motionEvent, mapView)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void destroy() {
        for (Overlay destroy : this.overlays) {
            destroy.destroy();
        }
        this.overlays.clear();
    }
}
