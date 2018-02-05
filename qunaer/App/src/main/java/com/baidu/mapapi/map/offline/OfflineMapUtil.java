package com.baidu.mapapi.map.offline;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.t;
import com.baidu.platform.comapi.map.w;
import java.util.ArrayList;
import java.util.Iterator;

public class OfflineMapUtil {
    public static MKOLSearchRecord getSearchRecordFromLocalCityInfo(t tVar) {
        if (tVar == null) {
            return null;
        }
        int i;
        MKOLSearchRecord mKOLSearchRecord = new MKOLSearchRecord();
        mKOLSearchRecord.cityID = tVar.a;
        mKOLSearchRecord.cityName = tVar.b;
        mKOLSearchRecord.cityType = tVar.d;
        if (tVar.a() != null) {
            ArrayList arrayList = new ArrayList();
            Iterator it = tVar.a().iterator();
            i = 0;
            while (it.hasNext()) {
                t tVar2 = (t) it.next();
                arrayList.add(getSearchRecordFromLocalCityInfo(tVar2));
                int i2 = tVar2.c + i;
                mKOLSearchRecord.childCities = arrayList;
                i = i2;
            }
        } else {
            i = 0;
        }
        if (mKOLSearchRecord.cityType == 1) {
            mKOLSearchRecord.size = i;
        } else {
            mKOLSearchRecord.size = tVar.c;
        }
        return mKOLSearchRecord;
    }

    public static MKOLUpdateElement getUpdatElementFromLocalMapElement(w wVar) {
        if (wVar == null) {
            return null;
        }
        MKOLUpdateElement mKOLUpdateElement = new MKOLUpdateElement();
        mKOLUpdateElement.cityID = wVar.a;
        mKOLUpdateElement.cityName = wVar.b;
        if (wVar.g != null) {
            mKOLUpdateElement.geoPt = CoordUtil.mc2ll(wVar.g);
        }
        mKOLUpdateElement.level = wVar.e;
        mKOLUpdateElement.ratio = wVar.i;
        mKOLUpdateElement.serversize = wVar.h;
        if (wVar.i == 100) {
            mKOLUpdateElement.size = wVar.h;
        } else {
            mKOLUpdateElement.size = (wVar.h / 100) * wVar.i;
        }
        mKOLUpdateElement.status = wVar.l;
        mKOLUpdateElement.update = wVar.j;
        return mKOLUpdateElement;
    }
}
