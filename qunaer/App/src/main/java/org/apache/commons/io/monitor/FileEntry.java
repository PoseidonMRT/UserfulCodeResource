package org.apache.commons.io.monitor;

import java.io.File;
import java.io.Serializable;

public class FileEntry implements Serializable {
    static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];
    private FileEntry[] children;
    private boolean directory;
    private boolean exists;
    private final File file;
    private long lastModified;
    private long length;
    private String name;
    private final FileEntry parent;

    public FileEntry(File file) {
        this((FileEntry) null, file);
    }

    public FileEntry(FileEntry fileEntry, File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is missing");
        }
        this.file = file;
        this.parent = fileEntry;
        this.name = file.getName();
    }

    public boolean refresh(File file) {
        long lastModified;
        long j = 0;
        boolean z = this.exists;
        long j2 = this.lastModified;
        boolean z2 = this.directory;
        long j3 = this.length;
        this.name = file.getName();
        this.exists = file.exists();
        this.directory = this.exists ? file.isDirectory() : false;
        if (this.exists) {
            lastModified = file.lastModified();
        } else {
            lastModified = 0;
        }
        this.lastModified = lastModified;
        if (this.exists && !this.directory) {
            j = file.length();
        }
        this.length = j;
        if (this.exists == z && this.lastModified == j2 && this.directory == z2 && this.length == j3) {
            return false;
        }
        return true;
    }

    public FileEntry newChildInstance(File file) {
        return new FileEntry(this, file);
    }

    public FileEntry getParent() {
        return this.parent;
    }

    public int getLevel() {
        return this.parent == null ? 0 : this.parent.getLevel() + 1;
    }

    public FileEntry[] getChildren() {
        return this.children != null ? this.children : EMPTY_ENTRIES;
    }

    public void setChildren(FileEntry[] fileEntryArr) {
        this.children = fileEntryArr;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long j) {
        this.lastModified = j;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long j) {
        this.length = j;
    }

    public boolean isExists() {
        return this.exists;
    }

    public void setExists(boolean z) {
        this.exists = z;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public void setDirectory(boolean z) {
        this.directory = z;
    }
}
