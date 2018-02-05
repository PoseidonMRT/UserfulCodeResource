package com.mqunar.atomenv;

import com.mqunar.atomenv.Caller.CallerObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodCaller<T> extends Caller<MethodBuilder, T> {
    protected Method method;
    protected String methodName;
    protected Class[] parameters;

    public class MethodBuilder<T> {
        private String a;
        private Class<? extends T> b;
        private int c;
        private Class[] d;
        private String e;

        public MethodBuilder setClassName(String str) {
            this.a = str;
            return this;
        }

        public MethodBuilder setReturnType(Class<? extends T> cls) {
            this.b = cls;
            return this;
        }

        public MethodBuilder setModifierType(int i) {
            this.c = i;
            return this;
        }

        public MethodBuilder setParameters(Class... clsArr) {
            this.d = clsArr;
            return this;
        }

        public MethodBuilder setMethodName(String str) {
            this.e = str;
            return this;
        }

        public MethodCaller<T> build() {
            MethodCaller<T> methodCaller = new MethodCaller();
            methodCaller.sync(this);
            return methodCaller;
        }
    }

    protected void sync(MethodBuilder methodBuilder) {
        this.className = methodBuilder.a;
        this.returnType = methodBuilder.b;
        this.modifierType = methodBuilder.c;
        this.parameters = methodBuilder.d;
        this.methodName = methodBuilder.e;
        this.method = a();
    }

    private Method a() {
        try {
            Method declaredMethod = findClass(this.className).getDeclaredMethod(this.methodName, this.parameters);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Throwable th) {
            return null;
        }
    }

    protected void callInternal(CallerObject<T> callerObject, Object obj, Object... objArr) {
        if (this.method == null) {
            throw new NullPointerException("method not found!");
        }
        if (Modifier.isStatic(this.method.getModifiers())) {
            callerObject.returns = this.method.invoke(null, objArr);
        } else {
            callerObject.returns = this.method.invoke(obj, objArr);
        }
        callerObject.success = true;
    }
}
