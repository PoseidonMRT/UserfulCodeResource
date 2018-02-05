package com.mqunar.qutui;

import java.io.File;
import java.io.FilenameFilter;

final class j implements FilenameFilter {
    j() {
    }

    public boolean accept(File file, String str) {
        return file.isDirectory() && str.matches("^\\d+$");
    }
}
