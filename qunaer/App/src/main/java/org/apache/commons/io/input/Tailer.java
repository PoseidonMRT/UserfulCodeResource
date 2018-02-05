package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Tailer implements Runnable {
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private final long delayMillis;
    private final boolean end;
    private final File file;
    private final byte[] inbuf;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    public Tailer(File file, TailerListener tailerListener) {
        this(file, tailerListener, 1000);
    }

    public Tailer(File file, TailerListener tailerListener, long j) {
        this(file, tailerListener, j, false);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z) {
        this(file, tailerListener, j, z, 4096);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, boolean z2) {
        this(file, tailerListener, j, z, z2, 4096);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, int i) {
        this(file, tailerListener, j, z, false, i);
    }

    public Tailer(File file, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        this.run = true;
        this.file = file;
        this.delayMillis = j;
        this.end = z;
        this.inbuf = new byte[i];
        this.listener = tailerListener;
        tailerListener.init(this);
        this.reOpen = z2;
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, int i) {
        Object tailer = new Tailer(file, tailerListener, j, z, i);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, boolean z2, int i) {
        Object tailer = new Tailer(file, tailerListener, j, z, z2, i);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z) {
        return create(file, tailerListener, j, z, 4096);
    }

    public static Tailer create(File file, TailerListener tailerListener, long j, boolean z, boolean z2) {
        return create(file, tailerListener, j, z, z2, 4096);
    }

    public static Tailer create(File file, TailerListener tailerListener, long j) {
        return create(file, tailerListener, j, false);
    }

    public static Tailer create(File file, TailerListener tailerListener) {
        return create(file, tailerListener, 1000, false);
    }

    public File getFile() {
        return this.file;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    public void run() {
        Exception e;
        RandomAccessFile randomAccessFile;
        long j;
        Closeable closeable;
        Throwable th;
        Exception exception;
        Object obj;
        Throwable th2;
        Closeable closeable2 = null;
        long j2 = 0;
        long j3 = 0;
        while (this.run && r1 == null) {
            try {
                closeable2 = new RandomAccessFile(this.file, RAF_MODE);
            } catch (FileNotFoundException e2) {
                try {
                    this.listener.fileNotFound();
                } catch (Exception e3) {
                    e = e3;
                }
            }
            if (closeable2 == null) {
                try {
                    Thread.sleep(this.delayMillis);
                } catch (InterruptedException e4) {
                }
            } else {
                j2 = this.end ? this.file.length() : 0;
                j3 = System.currentTimeMillis();
                closeable2.seek(j2);
            }
        }
        while (this.run) {
            boolean isFileNewer = FileUtils.isFileNewer(this.file, j3);
            long length = this.file.length();
            if (length < j2) {
                this.listener.fileRotated();
                try {
                    randomAccessFile = new RandomAccessFile(this.file, RAF_MODE);
                    try {
                        IOUtils.closeQuietly(closeable2);
                        j2 = 0;
                        closeable2 = randomAccessFile;
                    } catch (FileNotFoundException e5) {
                        Object obj2 = randomAccessFile;
                        j = 0;
                        try {
                            this.listener.fileNotFound();
                            closeable2 = closeable;
                            j2 = j;
                        } catch (Exception e6) {
                            e = e6;
                            closeable2 = closeable;
                        } catch (Throwable th3) {
                            th = th3;
                            closeable2 = closeable;
                        }
                    } catch (Exception e7) {
                        exception = e7;
                        obj = randomAccessFile;
                        e = exception;
                    } catch (Throwable th4) {
                        th2 = th4;
                        obj = randomAccessFile;
                        th = th2;
                    }
                } catch (FileNotFoundException e8) {
                    long j4 = j2;
                    closeable = closeable2;
                    j = j4;
                    this.listener.fileNotFound();
                    closeable2 = closeable;
                    j2 = j;
                }
            } else {
                if (length > j2) {
                    j2 = readLines(closeable2);
                    j3 = System.currentTimeMillis();
                } else if (isFileNewer) {
                    closeable2.seek(0);
                    j2 = readLines(closeable2);
                    j3 = System.currentTimeMillis();
                }
                if (this.reOpen) {
                    IOUtils.closeQuietly(closeable2);
                }
                try {
                    Thread.sleep(this.delayMillis);
                } catch (InterruptedException e9) {
                }
                if (this.run && this.reOpen) {
                    randomAccessFile = new RandomAccessFile(this.file, RAF_MODE);
                    try {
                        randomAccessFile.seek(j2);
                    } catch (Exception e72) {
                        exception = e72;
                        obj = randomAccessFile;
                        e = exception;
                    } catch (Throwable th42) {
                        th2 = th42;
                        obj = randomAccessFile;
                        th = th2;
                    }
                } else {
                    Closeable closeable3 = closeable2;
                }
                closeable2 = randomAccessFile;
            }
        }
        IOUtils.closeQuietly(closeable2);
        return;
        try {
            this.listener.handle(e);
            IOUtils.closeQuietly(closeable2);
        } catch (Throwable th5) {
            th = th5;
            IOUtils.closeQuietly(closeable2);
            throw th;
        }
    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile randomAccessFile) {
        StringBuilder stringBuilder = new StringBuilder();
        long filePointer = randomAccessFile.getFilePointer();
        int i = 0;
        long j = filePointer;
        while (this.run) {
            int read = randomAccessFile.read(this.inbuf);
            if (read != -1) {
                for (int i2 = 0; i2 < read; i2++) {
                    byte b = this.inbuf[i2];
                    switch (b) {
                        case (byte) 10:
                            this.listener.handle(stringBuilder.toString());
                            stringBuilder.setLength(0);
                            filePointer = (((long) i2) + j) + 1;
                            i = 0;
                            break;
                        case (byte) 13:
                            if (i != 0) {
                                stringBuilder.append('\r');
                            }
                            i = 1;
                            break;
                        default:
                            if (i != 0) {
                                this.listener.handle(stringBuilder.toString());
                                stringBuilder.setLength(0);
                                filePointer = (((long) i2) + j) + 1;
                                i = 0;
                            }
                            stringBuilder.append((char) b);
                            break;
                    }
                }
                j = randomAccessFile.getFilePointer();
            } else {
                randomAccessFile.seek(filePointer);
                return filePointer;
            }
        }
        randomAccessFile.seek(filePointer);
        return filePointer;
    }
}
