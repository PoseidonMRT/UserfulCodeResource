package com.mqunar.core;

final class b extends ClassLoader {
    b(ClassLoader classLoader) {
        super(classLoader);
    }

    protected Class<?> findClass(String str) {
        return QunarApkLoader.loadFromDexs(null, str);
    }
}
