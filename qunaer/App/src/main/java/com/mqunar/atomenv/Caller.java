package com.mqunar.atomenv;

public abstract class Caller<T, R> {
    protected String className;
    protected int modifierType;
    protected Class returnType;

    public class CallerObject<R> {
        public Throwable cause;
        public R returns;
        public boolean success = false;
    }

    protected abstract void callInternal(CallerObject<R> callerObject, Object obj, Object... objArr);

    protected abstract void sync(T t);

    public CallerObject<R> call(Object obj, Object... objArr) {
        CallerObject<R> callerObject = new CallerObject();
        try {
            callInternal(callerObject, obj, objArr);
        } catch (Throwable th) {
            callerObject.success = false;
            callerObject.cause = th;
        }
        return callerObject;
    }

    protected Class findClass(String str) {
        return Class.forName(str);
    }
}
