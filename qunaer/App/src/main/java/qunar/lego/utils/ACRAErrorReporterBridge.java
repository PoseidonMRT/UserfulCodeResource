package qunar.lego.utils;

public class ACRAErrorReporterBridge {
    private static final String ACRA_CLASS_STR = "org.acra.ACRA";
    private static ACRAErrorReporterBridge instance = null;
    private Object mErrorReporter;

    private ACRAErrorReporterBridge() {
        this.mErrorReporter = null;
        this.mErrorReporter = GetInfoUtils.invokeStaticMethod(ACRA_CLASS_STR, "getErrorReporter", new Class[0], new Object[0]);
    }

    public static ACRAErrorReporterBridge getInstance() {
        if (instance == null) {
            synchronized (ACRAErrorReporterBridge.class) {
                if (instance == null) {
                    instance = new ACRAErrorReporterBridge();
                }
            }
        }
        return instance;
    }

    public void handleSilentException(Throwable th) {
        GetInfoUtils.invokeMethod("handleSilentException", this.mErrorReporter, new Class[]{Throwable.class}, new Object[]{th});
    }
}
