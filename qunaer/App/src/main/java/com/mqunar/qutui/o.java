package com.mqunar.qutui;

import android.content.SharedPreferences.Editor;
import com.alibaba.fastjson.JSON;
import com.mqunar.qutui.model.LogModel;
import com.mqunar.qutui.model.LogModel.CallData;
import com.mqunar.tools.log.QLog;
import java.util.Map;
import java.util.Map.Entry;

class o implements Runnable {
    final /* synthetic */ Map a;
    final /* synthetic */ Map b;
    final /* synthetic */ m c;

    o(m mVar, Map map, Map map2) {
        this.c = mVar;
        this.a = map;
        this.b = map2;
    }

    public void run() {
        LogModel logModel;
        int i;
        Editor edit = this.c.a.edit();
        int i2 = 0;
        int i3 = 0;
        for (Entry entry : this.c.a().entrySet()) {
            if (((String) entry.getKey()).matches("^\\d+$")) {
                boolean z;
                long parseLong = Long.parseLong((String) entry.getKey());
                QLog.i("localLog:" + entry.getValue(), new Object[0]);
                LogModel logModel2 = (LogModel) JSON.parseObject((String) entry.getValue(), LogModel.class);
                if (600000 + parseLong >= System.currentTimeMillis() || logModel2 == null) {
                    if (logModel2 == null) {
                        logModel2 = new LogModel();
                        logModel2.startTime = parseLong;
                        logModel = logModel2;
                    } else {
                        logModel = logModel2;
                    }
                    if (this.a != null && this.a.size() > 0) {
                        logModel.installMap.putAll(this.a);
                    }
                    if (this.b != null && this.b.size() > 0) {
                        for (String str : this.b.keySet()) {
                            int i4 = 0;
                            while (i4 < logModel.callDatas.size()) {
                                if (((CallData) logModel.callDatas.get(i4)).key.equals(str)) {
                                    CallData callData;
                                    if (((Boolean) this.b.get(str)).booleanValue()) {
                                        callData = (CallData) logModel.callDatas.get(i4);
                                        callData.effectiveCount++;
                                    } else {
                                        callData = (CallData) logModel.callDatas.get(i4);
                                        callData.invalidCount++;
                                    }
                                    i2 = 1;
                                    if (i2 == 0) {
                                        logModel.callDatas.add(new CallData(str, ((Boolean) this.b.get(str)).booleanValue()));
                                    }
                                } else {
                                    i4++;
                                }
                            }
                            i2 = 0;
                            if (i2 == 0) {
                                logModel.callDatas.add(new CallData(str, ((Boolean) this.b.get(str)).booleanValue()));
                            }
                        }
                    }
                    logModel.endTime = System.currentTimeMillis();
                    edit.putString((String) entry.getKey(), JSON.toJSONString(logModel));
                    i = 1;
                    z = i3;
                } else if (i3 != false) {
                    i = i2;
                    z = i3;
                } else {
                    i = i2;
                    z = true;
                }
                QLog.i("needUpload = " + z, new Object[0]);
                if (z && i != 0) {
                    boolean z2 = z;
                    break;
                } else {
                    i2 = i;
                    i3 = z;
                }
            }
        }
        i = i2;
        if (i == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            logModel = new LogModel();
            logModel.startTime = currentTimeMillis;
            if (this.a != null) {
                logModel.installMap.putAll(this.a);
            }
            if (this.b != null && this.b.size() > 0) {
                for (Entry entry2 : this.b.entrySet()) {
                    logModel.callDatas.add(new CallData((String) entry2.getKey(), ((Boolean) entry2.getValue()).booleanValue()));
                }
            }
            logModel.endTime = System.currentTimeMillis();
            edit.putString(currentTimeMillis + "", JSON.toJSONString(logModel));
        }
        this.c.a(edit);
        if (z2) {
            this.c.d();
        }
    }
}
