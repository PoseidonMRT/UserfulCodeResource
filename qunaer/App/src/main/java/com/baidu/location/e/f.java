package com.baidu.location.e;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.mqunar.tools.DateTimeUtils;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class f {
    private static final String d = String.format(Locale.US, "DELETE FROM LOG WHERE timestamp NOT IN (SELECT timestamp FROM LOG ORDER BY timestamp DESC LIMIT %d);", new Object[]{Integer.valueOf(HttpConnectionPool.CONNECTION_TIMEOUT)});
    private static final String e = String.format(Locale.US, "SELECT * FROM LOG ORDER BY timestamp DESC LIMIT %d;", new Object[]{Integer.valueOf(3)});
    private String a = null;
    private final SQLiteDatabase b;
    private final a c;

    class a extends com.baidu.location.h.f {
        final /* synthetic */ f a;
        private int b;
        private long c;
        private String d;
        private boolean e;
        private boolean f;
        private f p;

        a(f fVar, f fVar2) {
            this.a = fVar;
            this.p = fVar2;
            this.d = null;
            this.e = false;
            this.f = false;
            this.k = new HashMap();
            this.b = 0;
            this.c = -1;
        }

        private void b() {
            if (!this.e) {
                this.d = this.p.b();
                if (this.c != -1 && this.c + DateTimeUtils.ONE_DAY <= System.currentTimeMillis()) {
                    this.b = 0;
                    this.c = -1;
                }
                if (this.d != null && this.b < 2) {
                    this.e = true;
                    e();
                }
            }
        }

        public void a() {
            this.k.clear();
            this.k.put("qt", "ofbh");
            this.k.put("req", this.d);
            this.h = d.a;
        }

        public void a(boolean z) {
            this.f = false;
            if (z && this.j != null) {
                try {
                    JSONObject jSONObject = new JSONObject(this.j);
                    if (jSONObject != null && jSONObject.has(ParamStr.RET_RES_ERROR) && jSONObject.getInt(ParamStr.RET_RES_ERROR) == BDLocation.TypeNetWorkLocation) {
                        this.f = true;
                    }
                } catch (Exception e) {
                }
            }
            this.e = false;
            if (!this.f) {
                this.b++;
                this.c = System.currentTimeMillis();
            }
            new Thread(this) {
                final /* synthetic */ a a;

                {
                    this.a = r1;
                }

                public void run() {
                    super.run();
                    this.a.p.a(this.a.f);
                }
            }.start();
        }
    }

    f(SQLiteDatabase sQLiteDatabase) {
        this.b = sQLiteDatabase;
        this.c = new a(this, this);
        if (this.b != null && this.b.isOpen()) {
            try {
                this.b.execSQL("CREATE TABLE IF NOT EXISTS LOG(timestamp LONG PRIMARY KEY, log VARCHAR(4000))");
            } catch (Exception e) {
            }
        }
    }

    private void a(boolean z) {
        if (z && this.a != null) {
            String format = String.format("DELETE FROM LOG WHERE timestamp in (%s);", new Object[]{this.a});
            try {
                if (this.a.length() > 0) {
                    this.b.execSQL(format);
                }
            } catch (Exception e) {
            }
        }
        this.a = null;
    }

    private String b() {
        Cursor rawQuery;
        Throwable th;
        String str = null;
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        try {
            rawQuery = this.b.rawQuery(e, null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.getCount() > 0) {
                        StringBuffer stringBuffer = new StringBuffer();
                        rawQuery.moveToFirst();
                        while (!rawQuery.isAfterLast()) {
                            jSONArray.put(rawQuery.getString(1));
                            if (stringBuffer.length() != 0) {
                                stringBuffer.append(",");
                            }
                            stringBuffer.append(rawQuery.getLong(0));
                            rawQuery.moveToNext();
                        }
                        try {
                            jSONObject.put("ofloc", jSONArray);
                            str = jSONObject.toString();
                        } catch (JSONException e) {
                        }
                        this.a = stringBuffer.toString();
                    }
                } catch (Exception e2) {
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e3) {
                        }
                    }
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            }
            if (rawQuery != null) {
                try {
                    rawQuery.close();
                } catch (Exception e5) {
                }
            }
        } catch (Exception e6) {
            Object obj = str;
            if (rawQuery != null) {
                rawQuery.close();
            }
            return str;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            rawQuery = str;
            th = th4;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
        return str;
    }

    void a() {
        this.c.b();
    }

    void a(String str) {
        String encodeOfflineLocationUpdateRequest = Jni.encodeOfflineLocationUpdateRequest(str);
        try {
            this.b.execSQL(String.format(Locale.US, "INSERT OR IGNORE INTO LOG VALUES (%d,\"%s\");", new Object[]{Long.valueOf(System.currentTimeMillis()), encodeOfflineLocationUpdateRequest}));
            this.b.execSQL(d);
        } catch (Exception e) {
        }
    }
}
