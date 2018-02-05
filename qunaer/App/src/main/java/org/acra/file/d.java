package org.acra.file;

import android.support.annotation.NonNull;
import java.io.File;
import java.util.Comparator;

final class d implements Comparator<File> {
    d() {
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((File) obj, (File) obj2);
    }

    public int a(@NonNull File file, @NonNull File file2) {
        return (int) (file.lastModified() - file2.lastModified());
    }
}
