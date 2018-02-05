package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy {
    public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();
    public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
    private final String name;

    class ForceFileDeleteStrategy extends FileDeleteStrategy {
        ForceFileDeleteStrategy() {
            super("Force");
        }

        protected boolean doDelete(File file) {
            FileUtils.forceDelete(file);
            return true;
        }
    }

    protected FileDeleteStrategy(String str) {
        this.name = str;
    }

    public boolean deleteQuietly(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        try {
            return doDelete(file);
        } catch (IOException e) {
            return false;
        }
    }

    public void delete(File file) {
        if (file.exists() && !doDelete(file)) {
            throw new IOException("Deletion failed: " + file);
        }
    }

    protected boolean doDelete(File file) {
        return file.delete();
    }

    public String toString() {
        return "FileDeleteStrategy[" + this.name + "]";
    }
}
