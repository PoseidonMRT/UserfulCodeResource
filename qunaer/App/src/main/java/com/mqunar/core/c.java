package com.mqunar.core;

import android.app.Instrumentation;
import android.content.Context;
import com.mqunar.module.ModuleInfo;
import com.mqunar.tools.log.QLog;
import java.util.concurrent.atomic.AtomicBoolean;

final class c implements Runnable {
    final /* synthetic */ ModuleInfo a;
    final /* synthetic */ Class b;
    final /* synthetic */ Context c;
    final /* synthetic */ AtomicBoolean d;

    c(ModuleInfo moduleInfo, Class cls, Context context, AtomicBoolean atomicBoolean) {
        this.a = moduleInfo;
        this.b = cls;
        this.c = context;
        this.d = atomicBoolean;
    }

    public void run() {
        try {
            if (this.a.application == null) {
                long currentTimeMillis = System.currentTimeMillis();
                this.a.application = Instrumentation.newApplication(this.b, this.c);
                this.a.application.onCreate();
                QLog.d("WASTE", "application (%s) onCreate waste (%d)", this.b.getName(), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            }
            synchronized (this.d) {
                this.d.set(true);
                this.d.notify();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } catch (Throwable e2) {
            throw new RuntimeException(e2);
        } catch (Throwable e22) {
            throw new RuntimeException(e22);
        } catch (Throwable th) {
            synchronized (this.d) {
                this.d.set(true);
                this.d.notify();
            }
        }
    }
}
