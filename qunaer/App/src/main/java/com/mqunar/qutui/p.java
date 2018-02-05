package com.mqunar.qutui;

import android.content.SharedPreferences.Editor;
import com.alibaba.fastjson.JSON;
import com.mqunar.qutui.model.LogModel;
import com.mqunar.qutui.model.LogModel.CallData;
import com.mqunar.tools.log.QLog;
import java.util.Map.Entry;

class p implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ boolean b;
    final /* synthetic */ m c;

    p(m mVar, String str, boolean z) {
        this.c = mVar;
        this.a = str;
        this.b = z;
    }

    public void run() {
        int i;
        boolean z;
        Editor edit = this.c.a.edit();
        int i2 = 0;
        int i3 = 0;
        for (Entry entry : this.c.a().entrySet()) {
            if (((String) entry.getKey()).matches("^\\d+$")) {
                long parseLong = Long.parseLong((String) entry.getKey());
                QLog.i("localLog:" + entry.getValue(), new Object[0]);
                LogModel logModel = (LogModel) JSON.parseObject((String) entry.getValue(), LogModel.class);
                if (600000 + parseLong >= System.currentTimeMillis() || logModel == null) {
                    LogModel logModel2;
                    if (logModel == null) {
                        logModel = new LogModel();
                        logModel.startTime = parseLong;
                        logModel2 = logModel;
                    } else {
                        logModel2 = logModel;
                    }
                    int i4 = 0;
                    while (i4 < logModel2.calledDatas.size()) {
                        if (((CallData) logModel2.calledDatas.get(i4)).key.equals(this.a)) {
                            CallData callData;
                            if (this.b) {
                                callData = (CallData) logModel2.calledDatas.get(i4);
                                callData.effectiveCount++;
                            } else {
                                callData = (CallData) logModel2.calledDatas.get(i4);
                                callData.invalidCount++;
                            }
                            i2 = 1;
                            if (i2 == 0) {
                                logModel2.calledDatas.add(new CallData(this.a, this.b));
                            }
                            logModel2.endTime = System.currentTimeMillis();
                            edit.putString((String) entry.getKey(), JSON.toJSONString(logModel2));
                            i = 1;
                            z = i3;
                        } else {
                            i4++;
                        }
                    }
                    if (i2 == 0) {
                        logModel2.calledDatas.add(new CallData(this.a, this.b));
                    }
                    logModel2.endTime = System.currentTimeMillis();
                    edit.putString((String) entry.getKey(), JSON.toJSONString(logModel2));
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
                    break;
                }
                i2 = i;
                i3 = z;
            }
        }
        i = i2;
        z = i3;
        if (i == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            LogModel logModel3 = new LogModel();
            logModel3.startTime = currentTimeMillis;
            logModel3.calledDatas.add(new CallData(this.a, this.b));
            logModel3.endTime = System.currentTimeMillis();
            edit.putString(currentTimeMillis + "", JSON.toJSONString(logModel3));
        }
        this.c.a(edit);
        if (z) {
            this.c.d();
        }
    }
}
