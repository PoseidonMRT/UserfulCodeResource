package com.mqunar.hy.res;

import com.mqunar.hy.res.model.HybridInfo;

public interface DownloadTaskResult<T> {
    void error(HybridInfo hybridInfo, int i, String str);

    void sucess(T t);
}
