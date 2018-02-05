package com.mqunar.libtask;

public class ACRAErrorReporterBridge {
    private static ACRAErrorReporterBridge b = null;
    private Object a;

    private ACRAErrorReporterBridge() {
        this.a = null;
        this.a = r.a("org.acra.ACRA", "getErrorReporter", new Class[0], new Object[0]);
    }

    static ACRAErrorReporterBridge a() {
        if (b == null) {
            synchronized (ACRAErrorReporterBridge.class) {
                if (b == null) {
                    b = new ACRAErrorReporterBridge();
                }
            }
        }
        return b;
    }

    void a(Throwable th) {
        r.a("handleSilentException", this.a, new Class[]{Throwable.class}, new Object[]{th});
    }
}
