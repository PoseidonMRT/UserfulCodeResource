package qunar.sdk.mapapi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import java.io.InputStream;
import qunar.sdk.mapapi.entity.QMarker;

public class MapHelperUtils {
    public static BitmapDescriptor createBitmapDescriptor(QMarker qMarker) {
        switch (qMarker.paramerCase) {
            case ASSETNAME_TYPE:
                return BitmapDescriptorFactory.fromAsset(qMarker.imagePath);
            case FILENAME_TYPE:
                return BitmapDescriptorFactory.fromFile(qMarker.imagePath);
            case ABSOLUTEPATH_TYPE:
                return BitmapDescriptorFactory.fromPath(qMarker.imagePath);
            case IMAGE_TYPE:
                return BitmapDescriptorFactory.fromBitmap(qMarker.bitmap);
            case RESOURCEID_TYPE:
                return BitmapDescriptorFactory.fromResource(qMarker.resourceId);
            case VIEW_TYPE:
                if (qMarker.view != null) {
                    return BitmapDescriptorFactory.fromView(qMarker.view);
                }
                return null;
            default:
                return null;
        }
    }

    public static Drawable createDrawable(Context context, QMarker qMarker) {
        Drawable drawable = null;
        if (qMarker == null) {
            return drawable;
        }
        InputStream open;
        Bitmap decodeFile;
        switch (qMarker.paramerCase) {
            case ASSETNAME_TYPE:
                if (context == null) {
                    return drawable;
                }
                try {
                    open = context.getAssets().open(qMarker.imagePath);
                    drawable = Drawable.createFromStream(open, null);
                    open.close();
                    return drawable;
                } catch (Exception e) {
                    return drawable;
                }
            case FILENAME_TYPE:
                if (qMarker.imagePath == null || qMarker.imagePath.equals("") || context == null || context == null) {
                    return drawable;
                }
                try {
                    open = context.openFileInput(qMarker.imagePath);
                    drawable = Drawable.createFromStream(open, null);
                    open.close();
                    return drawable;
                } catch (Exception e2) {
                    return drawable;
                }
            case ABSOLUTEPATH_TYPE:
                try {
                    decodeFile = BitmapFactory.decodeFile(qMarker.imagePath);
                    if (decodeFile == null || decodeFile == null) {
                        return drawable;
                    }
                    Drawable bitmapDrawable = new BitmapDrawable(context.getResources(), decodeFile);
                    try {
                        decodeFile.recycle();
                        return bitmapDrawable;
                    } catch (Exception e3) {
                        return bitmapDrawable;
                    }
                } catch (Exception e4) {
                    return drawable;
                }
            case IMAGE_TYPE:
                try {
                    return new BitmapDrawable(context.getResources(), qMarker.bitmap);
                } catch (Exception e5) {
                    return drawable;
                }
            case RESOURCEID_TYPE:
                try {
                    return context.getResources().getDrawable(qMarker.resourceId);
                } catch (Exception e6) {
                    return drawable;
                }
            case VIEW_TYPE:
                try {
                    decodeFile = view2Bitmap(qMarker.view);
                    if (decodeFile != null) {
                        return new BitmapDrawable(context.getResources(), decodeFile);
                    }
                    return drawable;
                } catch (Exception e7) {
                    return drawable;
                }
            default:
                return drawable;
        }
    }

    public static Bitmap view2Bitmap(View view) {
        if (view == null) {
            return null;
        }
        view.destroyDrawingCache();
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<qunar.sdk.mapapi.entity.QMarker> checkOverlap(android.view.ViewGroup r12, qunar.sdk.mapapi.entity.QMarker r13, java.util.List<qunar.sdk.mapapi.entity.QMarker> r14, int r15, int r16, qunar.sdk.mapapi.QunarMapType r17) {
        /*
        r1 = 0;
        if (r13 == 0) goto L_0x000b;
    L_0x0003:
        if (r14 == 0) goto L_0x000b;
    L_0x0005:
        r2 = r14.size();
        if (r2 != 0) goto L_0x000c;
    L_0x000b:
        return r1;
    L_0x000c:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r1 = r13.position;
        r3 = qunar.sdk.mapapi.QunarMapType.BAIDU;
        r0 = r17;
        if (r0 != r3) goto L_0x0089;
    L_0x0019:
        r3 = r12 instanceof com.baidu.mapapi.map.MapView;
        if (r3 == 0) goto L_0x008f;
    L_0x001d:
        r12 = (com.baidu.mapapi.map.MapView) r12;
        r3 = new com.baidu.mapapi.model.LatLng;
        r4 = r1.getLatitude();
        r6 = r1.getLongitude();
        r3.<init>(r4, r6);
        r1 = r12.getMap();
        r4 = r1.getProjection();
        r3 = r4.toScreenLocation(r3);
        r5 = r14.iterator();
    L_0x003c:
        r1 = r5.hasNext();
        if (r1 == 0) goto L_0x008f;
    L_0x0042:
        r1 = r5.next();
        r1 = (qunar.sdk.mapapi.entity.QMarker) r1;
        r6 = r1.position;
        r7 = new com.baidu.mapapi.model.LatLng;
        r8 = r6.getLatitude();
        r10 = r6.getLongitude();
        r7.<init>(r8, r10);
        r6 = r4.toScreenLocation(r7);
        r7 = r3.x;
        r8 = r6.x;
        if (r7 != r8) goto L_0x0067;
    L_0x0061:
        r7 = r3.y;
        r8 = r6.y;
        if (r7 == r8) goto L_0x003c;
    L_0x0067:
        r7 = r3.x;
        r7 = r7 + r15;
        r8 = r6.x;
        if (r7 <= r8) goto L_0x003c;
    L_0x006e:
        r7 = r3.x;
        r8 = r6.x;
        r8 = r8 + r15;
        if (r7 >= r8) goto L_0x003c;
    L_0x0075:
        r7 = r3.y;
        r7 = r7 + r16;
        r8 = r6.y;
        if (r7 <= r8) goto L_0x003c;
    L_0x007d:
        r7 = r3.y;
        r6 = r6.y;
        r6 = r6 + r16;
        if (r7 >= r6) goto L_0x003c;
    L_0x0085:
        r2.add(r1);
        goto L_0x003c;
    L_0x0089:
        r1 = qunar.sdk.mapapi.QunarMapType.GAODE;
        r0 = r17;
        if (r0 != r1) goto L_0x008f;
    L_0x008f:
        r1 = r2;
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: qunar.sdk.mapapi.utils.MapHelperUtils.checkOverlap(android.view.ViewGroup, qunar.sdk.mapapi.entity.QMarker, java.util.List, int, int, qunar.sdk.mapapi.QunarMapType):java.util.List<qunar.sdk.mapapi.entity.QMarker>");
    }
}
