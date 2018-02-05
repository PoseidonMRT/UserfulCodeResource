package com.mqunar.cock.network;

import com.mqunar.cock.model.BaseResult;
import com.mqunar.cock.model.YaccaListResult;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.CrossConductor;

public enum ServiceMap implements IServiceMap {
    IM_GET_YACCA("getHost", YaccaListResult.class);
    
    private final Class<? extends BaseResult> mClazz;
    private final Class<? extends AbsConductor> mTaskType;
    private final String mType;

    private ServiceMap(String str, Class<? extends BaseResult> cls) {
        this(r7, r8, str, cls, CrossConductor.class);
    }

    private ServiceMap(String str, Class<? extends BaseResult> cls, Class<? extends AbsConductor> cls2) {
        this.mType = str;
        this.mClazz = cls;
        this.mTaskType = cls2;
    }

    public String getDesc() {
        return this.mType;
    }

    public Class<? extends BaseResult> getClazz() {
        return this.mClazz;
    }

    public Class<? extends AbsConductor> getTaskType() {
        return this.mTaskType;
    }
}
