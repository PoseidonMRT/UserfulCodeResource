package com.mqunar.cock.network;

import com.mqunar.cock.model.BaseResult;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.tools.EnumUtils.ITypeDesc;

public interface IServiceMap extends ITypeDesc {
    Class<? extends BaseResult> getClazz();

    Class<? extends AbsConductor> getTaskType();

    String name();
}
