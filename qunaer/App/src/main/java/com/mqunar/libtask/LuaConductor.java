package com.mqunar.libtask;

import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import qunar.lego.utils.Hallua;

public class LuaConductor extends AbsConductor {
    private String a;
    private String b;
    private int c;

    public class LuaRunnerResult implements Serializable {
        public static final int TYPE_EXCEPTION = 2;
        public static final int TYPE_FAILURE = 1;
        public static final int TYPE_SUCCESS = 0;
        private static final long serialVersionUID = 1;
        public long endtime = System.currentTimeMillis();
        public Map<String, byte[]> mapping;
        public String msg;
        public long runtime;
        public long starttime = System.currentTimeMillis();
        public int type;
    }

    public LuaConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }

    public void setParams(Object... objArr) {
        super.setParams(objArr);
        if (objArr != null) {
            try {
                if (objArr.length > 0) {
                    this.a = (String) objArr[0];
                }
                if (objArr.length > 1) {
                    this.b = (String) objArr[1];
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("input params must be String and String");
            }
        }
    }

    protected void doingTask() {
        if (this.status.get() != TaskCode.TASK_CANCEL) {
            LuaRunnerResult a = a(this.a, this.b);
            putExtraData(Statistics.KEY_TASK_RUN_BEGIN, Long.valueOf(a.starttime));
            putExtraData(Statistics.KEY_TASK_RUN_END, Long.valueOf(a.endtime));
            if (this.status.get() != TaskCode.TASK_CANCEL) {
                switch (a.type) {
                    case 0:
                        this.status.set(TaskCode.TASK_RESULT);
                        break;
                    case 1:
                        this.status.set(TaskCode.TASK_ERROR);
                        this.c = -1;
                        break;
                    case 2:
                        this.status.set(TaskCode.TASK_ERROR);
                        this.c = -2;
                        break;
                    default:
                        this.status.set(TaskCode.TASK_ERROR);
                        this.c = -3;
                        break;
                }
                this.result = a;
                this.msgd.onMessage((TaskCode) this.status.get(), this);
            }
        }
    }

    public LuaRunnerResult getResult() {
        return (LuaRunnerResult) super.getResult();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LuaConductor luaConductor = (LuaConductor) obj;
        if (!this.a.equals(luaConductor.a)) {
            return false;
        }
        if (this.b != null) {
            if (this.b.equals(luaConductor.b)) {
                return true;
            }
        } else if (luaConductor.b == null) {
            return true;
        }
        return false;
    }

    public <T extends AbsConductor> boolean sameAs(T t) {
        return equals(t);
    }

    public int hashCode() {
        return (this.b != null ? this.b.hashCode() : 0) + (this.a.hashCode() * 31);
    }

    protected int getEmpId() {
        return hashCode();
    }

    protected String getEmpName() {
        return this.b;
    }

    public Object findCache(boolean z) {
        throw new UnsupportedOperationException("implement later");
    }

    private static LuaRunnerResult a(String str, String str2) {
        return a("lua.error", str, str2);
    }

    private static LuaRunnerResult a(String str, String str2, String str3) {
        LuaRunnerResult luaRunnerResult = new LuaRunnerResult();
        if (CheckUtils.isEmpty(str) || CheckUtils.isEmpty(str2)) {
            luaRunnerResult.type = 1;
            luaRunnerResult.msg = new String("NOT ALLOW NULL!");
            return luaRunnerResult;
        }
        Map map = null;
        if (!CheckUtils.isEmpty(str3)) {
            map = new HashMap();
            map.put("json", str3.getBytes());
            QLog.v("request", "params = %s", str3);
        }
        try {
            QLog.v("request", "luaCode = %s", str2);
            long currentTimeMillis = System.currentTimeMillis();
            map = Hallua.runLua(str, str2, map);
            long currentTimeMillis2 = System.currentTimeMillis();
            if (map == null) {
                throw new RuntimeException("the lua reuslt is null...");
            }
            luaRunnerResult.starttime = currentTimeMillis;
            luaRunnerResult.endtime = currentTimeMillis2;
            luaRunnerResult.runtime = currentTimeMillis2 - currentTimeMillis;
            luaRunnerResult.mapping = map;
            if (map.containsKey("json")) {
                luaRunnerResult.type = 0;
                luaRunnerResult.msg = new String((byte[]) map.remove("json"));
            } else {
                luaRunnerResult.type = 1;
                luaRunnerResult.msg = new String((byte[]) map.get(str));
            }
            QLog.v("response", String.valueOf(luaRunnerResult.msg), new Object[0]);
            return luaRunnerResult;
        } catch (Throwable th) {
            luaRunnerResult.type = 2;
            luaRunnerResult.msg = th.toString();
        }
    }
}
