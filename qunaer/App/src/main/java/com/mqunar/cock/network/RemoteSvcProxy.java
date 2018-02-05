package com.mqunar.cock.network;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import com.mqunar.cock.model.BaseParam;
import com.mqunar.cock.utils.IJsonProcessor;
import com.mqunar.cock.utils.JsonProcessorBasedFastJson;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.ChiefGuard;
import com.mqunar.libtask.CrossConductor;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.libtask.Ticket.RequestFeature;
import com.mqunar.tools.log.QLog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteSvcProxy {
    private final Handler a;
    private final IJsonProcessor b = new JsonProcessorBasedFastJson();
    private final Context c;
    private List<AbsConductor> d = new CopyOnWriteArrayList();
    private List<AcyncParam> e = new CopyOnWriteArrayList();
    private n f;

    public class AcyncParam {
        public BaseParam baseParam;
        public TaskCallbackAdapter callback;
        public ServiceMap serviceMap;

        public AcyncParam(ServiceMap serviceMap, BaseParam baseParam, TaskCallbackAdapter taskCallbackAdapter) {
            this.serviceMap = serviceMap;
            this.baseParam = baseParam;
            this.callback = taskCallbackAdapter;
        }
    }

    public Context getContext() {
        return this.c;
    }

    public RemoteSvcProxy(Context context, Handler handler) {
        this.c = context.getApplicationContext();
        this.a = handler;
    }

    public AbsConductor sendAsync(ServiceMap serviceMap, BaseParam baseParam, TaskCallbackAdapter taskCallbackAdapter) {
        AbsConductor absConductor = null;
        QLog.d("sendAsync", new Object[0]);
        if (serviceMap.getDesc().equals("getHost") || OneKeyCremation.getInstance().oneKeyCremation()) {
            absConductor = createAdaptedImpConductor(taskCallbackAdapter);
            taskCallbackAdapter.b = serviceMap;
            baseParam.t = serviceMap.getDesc();
            if (CockHandler.getInstance().isRelease()) {
                absConductor.setParams("http://yaccahost.qunar.com", baseParam);
            } else {
                absConductor.setParams("http://l-bizcat2.wap.dev.cn0.qunar.com:8080", baseParam);
            }
            ChiefGuard.getInstance().addTask(this.c, absConductor, RequestFeature.ADD_ONORDER, RequestFeature.CACHE_NEVER);
            this.d.add(absConductor);
        } else {
            this.f = new n();
            LocalBroadcastManager.getInstance(this.c).registerReceiver(this.f, new IntentFilter());
            this.e.add(new AcyncParam(serviceMap, baseParam, taskCallbackAdapter));
        }
        return absConductor;
    }

    public AbsConductor sendByteAsync(String str, HashMap<String, String> hashMap, byte[] bArr, TaskCallbackAdapter taskCallbackAdapter, Serializable serializable) {
        AbsConductor createAdaptedCrossConductor = createAdaptedCrossConductor(taskCallbackAdapter);
        createAdaptedCrossConductor.setParams(str, bArr, hashMap, serializable);
        ChiefGuard.getInstance().addTask(this.c, createAdaptedCrossConductor, RequestFeature.ADD_ONORDER, RequestFeature.CACHE_NEVER);
        this.d.add(createAdaptedCrossConductor);
        QLog.d("sendByteAsync", new Object[0]);
        return createAdaptedCrossConductor;
    }

    public AbsConductor sendGetAsync(String str, TaskCallbackAdapter taskCallbackAdapter) {
        AbsConductor createAdaptedCrossConductor = createAdaptedCrossConductor(taskCallbackAdapter);
        createAdaptedCrossConductor.setParams(str);
        ChiefGuard.getInstance().addTask(this.c, createAdaptedCrossConductor, RequestFeature.ADD_ONORDER, RequestFeature.CACHE_NEVER);
        this.d.add(createAdaptedCrossConductor);
        QLog.d("sendGetAsync", new Object[0]);
        return createAdaptedCrossConductor;
    }

    public AbsConductor createAdaptedImpConductor(TaskCallback taskCallback) {
        return new ImpConductor(new k(this, taskCallback));
    }

    public AbsConductor createAdaptedCrossConductor(TaskCallback taskCallback) {
        return new CrossConductor(new l(this, taskCallback));
    }

    public AbsConductor createAdaptedYaccaConductor(TaskCallback taskCallback) {
        return new YaccaConductor(new m(this, taskCallback));
    }

    public <T> T parseFrom(Class<T> cls, byte[] bArr) {
        try {
            return this.b.deserialize(bArr, cls);
        } catch (Exception e) {
            QLog.e("spy", e);
            return null;
        }
    }

    public void postDelay(Runnable runnable, int i) {
        this.a.postDelayed(runnable, (long) i);
    }
}
