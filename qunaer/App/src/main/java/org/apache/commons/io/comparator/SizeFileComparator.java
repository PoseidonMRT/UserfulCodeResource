package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator extends AbstractFileComparator implements Serializable {
    public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();
    public static final Comparator<File> SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
    public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
    public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
    private final boolean sumDirectoryContents;

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    public SizeFileComparator(boolean z) {
        this.sumDirectoryContents = z;
    }

    public int compare(File file, File file2) {
        long length;
        long sizeOfDirectory = file.isDirectory() ? (this.sumDirectoryContents && file.exists()) ? FileUtils.sizeOfDirectory(file) : 0 : file.length();
        if (!file2.isDirectory()) {
            length = file2.length();
        } else if (this.sumDirectoryContents && file2.exists()) {
            length = FileUtils.sizeOfDirectory(file2);
        } else {
            length = 0;
        }
        sizeOfDirectory -= length;
        if (sizeOfDirectory < 0) {
            return -1;
        }
        if (sizeOfDirectory > 0) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
    }
}
