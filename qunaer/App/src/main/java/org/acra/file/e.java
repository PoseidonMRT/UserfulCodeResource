package org.acra.file;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public final class e {
    private final Context a;
    private File b;
    private File c;
    private File d;
    private File e;

    public e(@NonNull Context context) {
        this.a = context;
    }

    @NonNull
    public File a() {
        if (this.b != null && this.b.exists()) {
            return this.b;
        }
        this.b = new File(this.a.getFilesDir() + "/acra/" + "ACRA-unapproved");
        this.b.mkdirs();
        return this.b;
    }

    @NonNull
    public File[] b() {
        File[] listFiles = a().listFiles();
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }

    @NonNull
    public File c() {
        if (this.c != null && this.c.exists()) {
            return this.c;
        }
        this.c = new File(this.a.getFilesDir() + "/acra/" + "ACRA-approved");
        this.c.mkdirs();
        return this.c;
    }

    @NonNull
    public File d() {
        if (this.d != null && this.d.exists()) {
            return this.d;
        }
        this.d = new File(this.a.getFilesDir() + "/acra/" + "ACRA-dump");
        this.d.mkdirs();
        return this.d;
    }

    @NonNull
    public File e() {
        if (this.e != null && this.e.exists()) {
            return this.e;
        }
        this.e = new File(this.a.getFilesDir() + "/acra/" + "ACRA-screen");
        this.e.mkdirs();
        return this.e;
    }

    @NonNull
    public File[] f() {
        File[] listFiles = c().listFiles(a(".stacktrace"));
        if (listFiles == null) {
            return new File[0];
        }
        Arrays.sort(listFiles, new d());
        return listFiles;
    }

    @NonNull
    public File[] g() {
        File[] listFiles = c().listFiles(a(".acrajava.gz"));
        if (listFiles == null) {
            return new File[0];
        }
        Arrays.sort(listFiles, new d());
        return listFiles;
    }

    @NonNull
    public File[] h() {
        File[] listFiles = d().listFiles(a(".dmp"));
        if (listFiles == null) {
            return new File[0];
        }
        Arrays.sort(listFiles, new d());
        return listFiles;
    }

    @NonNull
    public File[] i() {
        File[] listFiles = d().listFiles(a(".dmp.gz"));
        if (listFiles == null) {
            return new File[0];
        }
        Arrays.sort(listFiles, new d());
        return listFiles;
    }

    public File[] j() {
        File[] listFiles = e().listFiles();
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }

    @NonNull
    private FilenameFilter a(@NonNull final String str) {
        return new FilenameFilter(this) {
            final /* synthetic */ e b;

            public boolean accept(File file, String str) {
                return str.endsWith(str);
            }
        };
    }
}
