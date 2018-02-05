package com.mqunar.libtask;

import com.mqunar.tools.EnumUtils.IType;

public enum TaskCode implements IType {
    TASK_NONE,
    TASK_REQUEST,
    TASK_RESULT,
    TASK_ERROR,
    TASK_PENDING,
    TASK_CACHE_HIT,
    TASK_CANCEL;

    public int getCode() {
        return ordinal() + 5000;
    }

    public String getDesc() {
        String toLowerCase = name().toLowerCase();
        return toLowerCase.substring(toLowerCase.indexOf("_") + 1);
    }
}
