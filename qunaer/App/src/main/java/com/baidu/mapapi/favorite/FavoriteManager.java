package com.baidu.mapapi.favorite;

import android.util.Log;
import com.baidu.mapapi.BMapManager;
import com.baidu.platform.comapi.favrite.FavSyncPoi;
import com.baidu.platform.comapi.favrite.a;
import com.baidu.platform.comapi.map.C0009i;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteManager {
    private static FavoriteManager a;
    private static a b;

    private FavoriteManager() {
    }

    public static FavoriteManager getInstance() {
        if (a == null) {
            a = new FavoriteManager();
        }
        return a;
    }

    public int add(FavoritePoiInfo favoritePoiInfo) {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return 0;
        } else if (favoritePoiInfo == null || favoritePoiInfo.c == null) {
            Log.e("baidumapsdk", "object or pt can not be null!");
            return 0;
        } else if (favoritePoiInfo.b == null || favoritePoiInfo.b.equals("")) {
            Log.e("baidumapsdk", "poiName can not be null or empty!");
            return -1;
        } else {
            FavSyncPoi a = a.a(favoritePoiInfo);
            int a2 = b.a(a.b, a);
            if (a2 != 1) {
                return a2;
            }
            favoritePoiInfo.a = a.a;
            favoritePoiInfo.g = Long.parseLong(a.h);
            return a2;
        }
    }

    public boolean clearAllFavPois() {
        if (b != null) {
            return b.c();
        }
        Log.e("baidumapsdk", "you may have not call init method!");
        return false;
    }

    public boolean deleteFavPoi(String str) {
        if (b != null) {
            return (str == null || str.equals("")) ? false : b.a(str);
        } else {
            Log.e("baidumapsdk", "you may have not call init method!");
            return false;
        }
    }

    public void destroy() {
        if (b != null) {
            b.b();
            b = null;
            BMapManager.destroy();
            C0009i.b();
        }
    }

    public List<FavoritePoiInfo> getAllFavPois() {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        }
        String f = b.f();
        if (f == null || f.equals("")) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(f);
            if (jSONObject.optInt("favpoinum") == 0) {
                return null;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("favcontents");
            if (optJSONArray == null || optJSONArray.length() <= 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                if (jSONObject2 != null) {
                    arrayList.add(a.a(jSONObject2));
                }
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FavoritePoiInfo getFavPoi(String str) {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        } else if (str == null || str.equals("")) {
            return null;
        } else {
            FavSyncPoi b = b.b(str);
            return b != null ? a.a(b) : null;
        }
    }

    public void init() {
        if (b == null) {
            C0009i.a();
            BMapManager.init();
            b = a.a();
        }
    }

    public boolean updateFavPoi(String str, FavoritePoiInfo favoritePoiInfo) {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return false;
        } else if (str == null || str.equals("") || favoritePoiInfo == null) {
            return false;
        } else {
            if (favoritePoiInfo == null || favoritePoiInfo.c == null) {
                Log.e("baidumapsdk", "object or pt can not be null!");
                return false;
            } else if (favoritePoiInfo.b == null || favoritePoiInfo.b.equals("")) {
                Log.e("baidumapsdk", "poiName can not be null or empty!");
                return false;
            } else {
                favoritePoiInfo.a = str;
                return b.b(str, a.a(favoritePoiInfo));
            }
        }
    }
}
