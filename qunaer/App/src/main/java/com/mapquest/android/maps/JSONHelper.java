package com.mapquest.android.maps;

import org.json.JSONArray;

class JSONHelper {
    public static final String LOG_TAG = JSONHelper.class.getName();
    private boolean debug = false;

    void setDebug(boolean z) {
        this.debug = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.json.JSONObject getJSONObject(java.lang.String r2, org.json.JSONObject r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getJSONObject(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000e }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = 0;
        goto L_0x0006;
    L_0x000e:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0013:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getJSONObject(java.lang.String, org.json.JSONObject):org.json.JSONObject");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.json.JSONArray getJSONArray(java.lang.String r2, org.json.JSONObject r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getJSONArray(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x0012 }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = new org.json.JSONArray;
        r0.<init>();
        goto L_0x0006;
    L_0x0012:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0017:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getJSONArray(java.lang.String, org.json.JSONObject):org.json.JSONArray");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.json.JSONArray getJSONArray(int r2, org.json.JSONArray r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.optJSONArray(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x0012 }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = new org.json.JSONArray;
        r0.<init>();
        goto L_0x0006;
    L_0x0012:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0017:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getJSONArray(int, org.json.JSONArray):org.json.JSONArray");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getInt(java.lang.String r2, org.json.JSONObject r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getInt(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000e }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = -1;
        goto L_0x0006;
    L_0x000e:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0013:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getInt(java.lang.String, org.json.JSONObject):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getString(java.lang.String r2, org.json.JSONObject r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getString(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000f }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = "";
        goto L_0x0006;
    L_0x000f:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0014:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getString(java.lang.String, org.json.JSONObject):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Double getDouble(java.lang.String r3, org.json.JSONObject r4) {
        /*
        r2 = this;
        if (r4 == 0) goto L_0x0010;
    L_0x0002:
        r0 = r4.getDouble(r3);	 Catch:{ Exception -> 0x000b, Error -> 0x0017 }
        r0 = java.lang.Double.valueOf(r0);	 Catch:{ Exception -> 0x000b, Error -> 0x0017 }
    L_0x000a:
        return r0;
    L_0x000b:
        r0 = move-exception;
        r0 = r2.debug;
        if (r0 == 0) goto L_0x0010;
    L_0x0010:
        r0 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
        r0 = java.lang.Double.valueOf(r0);
        goto L_0x000a;
    L_0x0017:
        r0 = move-exception;
        r0 = r2.debug;
        if (r0 == 0) goto L_0x0010;
    L_0x001c:
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getDouble(java.lang.String, org.json.JSONObject):java.lang.Double");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getBoolean(java.lang.String r2, org.json.JSONObject r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getBoolean(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000e }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = 0;
        goto L_0x0006;
    L_0x000e:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0013:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getBoolean(java.lang.String, org.json.JSONObject):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getString(int r2, org.json.JSONArray r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getString(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000f }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = "";
        goto L_0x0006;
    L_0x000f:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0014:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getString(int, org.json.JSONArray):java.lang.String");
    }

    public int getInt(int i, JSONArray jSONArray) {
        if (jSONArray != null) {
            try {
                return jSONArray.getInt(i);
            } catch (Exception e) {
            } catch (Error e2) {
            }
        }
        return -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double getDouble(int r3, org.json.JSONArray r4) {
        /*
        r2 = this;
        if (r4 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r4.getDouble(r3);	 Catch:{ Exception -> 0x0007, Error -> 0x000f }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r2.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
        goto L_0x0006;
    L_0x000f:
        r0 = move-exception;
        r0 = r2.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0014:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getDouble(int, org.json.JSONArray):double");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.json.JSONObject getJSONObject(int r2, org.json.JSONArray r3) {
        /*
        r1 = this;
        if (r3 == 0) goto L_0x000c;
    L_0x0002:
        r0 = r3.getJSONObject(r2);	 Catch:{ Exception -> 0x0007, Error -> 0x000e }
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x000c:
        r0 = 0;
        goto L_0x0006;
    L_0x000e:
        r0 = move-exception;
        r0 = r1.debug;
        if (r0 == 0) goto L_0x000c;
    L_0x0013:
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.JSONHelper.getJSONObject(int, org.json.JSONArray):org.json.JSONObject");
    }
}
