package com.baidu.mapapi.map.offline;

import android.support.v4.internal.view.SupportMenu;
import com.baidu.platform.comapi.map.y;
import java.util.List;

class a implements y {
    final /* synthetic */ MKOfflineMap a;

    a(MKOfflineMap mKOfflineMap) {
        this.a = mKOfflineMap;
    }

    public void a(int i, int i2) {
        switch (i) {
            case 4:
                List<MKOLUpdateElement> allUpdateInfo = this.a.getAllUpdateInfo();
                if (allUpdateInfo != null) {
                    for (MKOLUpdateElement mKOLUpdateElement : allUpdateInfo) {
                        if (mKOLUpdateElement.update) {
                            this.a.c.onGetOfflineMapState(4, mKOLUpdateElement.cityID);
                        }
                    }
                    return;
                }
                return;
            case 6:
                this.a.c.onGetOfflineMapState(6, i2);
                return;
            case 8:
                this.a.c.onGetOfflineMapState(0, SupportMenu.USER_MASK & (i2 >> 8));
                return;
            case 10:
                this.a.c.onGetOfflineMapState(2, i2);
                return;
            case 12:
                this.a.b.a(true, false);
                return;
            default:
                return;
        }
    }
}
