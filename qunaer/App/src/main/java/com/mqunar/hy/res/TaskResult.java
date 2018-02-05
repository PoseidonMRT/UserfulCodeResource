package com.mqunar.hy.res;

public interface TaskResult<T> {
    void error(int i, String str);

    void sucess(T t);
}
