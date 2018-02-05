package org.apache.commons.io;

import com.mqunar.libtask.ProgressType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FileSystemUtils {
    private static final String DF;
    private static final int INIT_PROBLEM = -1;
    private static final FileSystemUtils INSTANCE = new FileSystemUtils();
    private static final int OS;
    private static final int OTHER = 0;
    private static final int POSIX_UNIX = 3;
    private static final int UNIX = 2;
    private static final int WINDOWS = 1;

    static {
        int i = 3;
        String str = "df";
        try {
            String property = System.getProperty("os.name");
            if (property == null) {
                throw new IOException("os.name not found");
            }
            property = property.toLowerCase(Locale.ENGLISH);
            if (property.indexOf("windows") != -1) {
                i = 1;
            } else if (property.indexOf("linux") != -1 || property.indexOf("mpe/ix") != -1 || property.indexOf("freebsd") != -1 || property.indexOf("irix") != -1 || property.indexOf("digital unix") != -1 || property.indexOf("unix") != -1 || property.indexOf("mac os x") != -1) {
                i = 2;
            } else if (property.indexOf("sun os") != -1 || property.indexOf("sunos") != -1 || property.indexOf("solaris") != -1) {
                str = "/usr/xpg4/bin/df";
            } else if (property.indexOf("hp-ux") == -1 && property.indexOf("aix") == -1) {
                i = 0;
            }
            OS = i;
            DF = str;
        } catch (Exception e) {
            i = -1;
        }
    }

    @Deprecated
    public static long freeSpace(String str) {
        return INSTANCE.freeSpaceOS(str, OS, false, -1);
    }

    public static long freeSpaceKb(String str) {
        return freeSpaceKb(str, -1);
    }

    public static long freeSpaceKb(String str, long j) {
        return INSTANCE.freeSpaceOS(str, OS, true, j);
    }

    public static long freeSpaceKb() {
        return freeSpaceKb(-1);
    }

    public static long freeSpaceKb(long j) {
        return freeSpaceKb(new File(".").getAbsolutePath(), j);
    }

    long freeSpaceOS(String str, int i, boolean z, long j) {
        if (str == null) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        switch (i) {
            case 0:
                throw new IllegalStateException("Unsupported operating system");
            case 1:
                return z ? freeSpaceWindows(str, j) / 1024 : freeSpaceWindows(str, j);
            case 2:
                return freeSpaceUnix(str, z, false, j);
            case 3:
                return freeSpaceUnix(str, z, true, j);
            default:
                throw new IllegalStateException("Exception caught when determining operating system");
        }
    }

    long freeSpaceWindows(String str, long j) {
        String str2;
        String normalize = FilenameUtils.normalize(str, false);
        if (normalize.length() <= 0 || normalize.charAt(0) == '\"') {
            str2 = normalize;
        } else {
            str2 = "\"" + normalize + "\"";
        }
        List performCommand = performCommand(new String[]{"cmd.exe", "/C", "dir /a /-c " + str2}, ProgressType.PRO_END, j);
        for (int size = performCommand.size() - 1; size >= 0; size--) {
            normalize = (String) performCommand.get(size);
            if (normalize.length() > 0) {
                return parseDir(normalize, str2);
            }
        }
        throw new IOException("Command line 'dir /-c' did not return any info for path '" + str2 + "'");
    }

    long parseDir(String str, String str2) {
        int i;
        int i2;
        int i3 = 0;
        int length = str.length() - 1;
        while (length >= 0) {
            if (Character.isDigit(str.charAt(length))) {
                i = length + 1;
                i2 = length;
                break;
            }
            length--;
        }
        i2 = length;
        i = 0;
        while (i2 >= 0) {
            char charAt = str.charAt(i2);
            if (!Character.isDigit(charAt) && charAt != ',' && charAt != FilenameUtils.EXTENSION_SEPARATOR) {
                length = i2 + 1;
                break;
            }
            i2--;
        }
        length = 0;
        if (i2 < 0) {
            throw new IOException("Command line 'dir /-c' did not return valid info for path '" + str2 + "'");
        }
        StringBuilder stringBuilder = new StringBuilder(str.substring(length, i));
        while (i3 < stringBuilder.length()) {
            if (stringBuilder.charAt(i3) == ',' || stringBuilder.charAt(i3) == FilenameUtils.EXTENSION_SEPARATOR) {
                length = i3 - 1;
                stringBuilder.deleteCharAt(i3);
                i3 = length;
            }
            i3++;
        }
        return parseBytes(stringBuilder.toString(), str2);
    }

    long freeSpaceUnix(String str, boolean z, boolean z2, long j) {
        if (str.length() == 0) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        String[] strArr;
        String str2 = "-";
        if (z) {
            str2 = str2 + "k";
        }
        if (z2) {
            str2 = str2 + "P";
        }
        if (str2.length() > 1) {
            strArr = new String[]{DF, str2, str};
        } else {
            strArr = new String[]{DF, str};
        }
        List performCommand = performCommand(strArr, 3, j);
        if (performCommand.size() < 2) {
            throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + str + "'- response was " + performCommand);
        }
        StringTokenizer stringTokenizer;
        StringTokenizer stringTokenizer2 = new StringTokenizer((String) performCommand.get(1), " ");
        if (stringTokenizer2.countTokens() >= 4) {
            stringTokenizer2.nextToken();
            stringTokenizer = stringTokenizer2;
        } else if (stringTokenizer2.countTokens() != 1 || performCommand.size() < 3) {
            throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + str + "'- check path is valid");
        } else {
            stringTokenizer = new StringTokenizer((String) performCommand.get(2), " ");
        }
        stringTokenizer.nextToken();
        stringTokenizer.nextToken();
        return parseBytes(stringTokenizer.nextToken(), str);
    }

    long parseBytes(String str, String str2) {
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong >= 0) {
                return parseLong;
            }
            throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + str2 + "'- check path is valid");
        } catch (Throwable e) {
            throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + str2 + "'- check path is valid", e);
        }
    }

    List<String> performCommand(String[] strArr, int i, long j) {
        InputStream inputStream;
        OutputStream outputStream;
        InputStream errorStream;
        Throwable e;
        OutputStream outputStream2;
        InputStream inputStream2;
        Process process;
        InputStream inputStream3 = null;
        List<String> arrayList = new ArrayList(20);
        Process openProcess;
        Reader bufferedReader;
        try {
            Thread start = ThreadMonitor.start(j);
            openProcess = openProcess(strArr);
            try {
                inputStream = openProcess.getInputStream();
                try {
                    outputStream = openProcess.getOutputStream();
                    try {
                        errorStream = openProcess.getErrorStream();
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        } catch (InterruptedException e2) {
                            e = e2;
                            bufferedReader = null;
                            inputStream3 = errorStream;
                            outputStream2 = outputStream;
                            inputStream2 = inputStream;
                            process = openProcess;
                            try {
                                throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
                            } catch (Throwable th) {
                                e = th;
                                openProcess = process;
                                inputStream = inputStream2;
                                outputStream = outputStream2;
                                errorStream = inputStream3;
                                IOUtils.closeQuietly(inputStream);
                                IOUtils.closeQuietly(outputStream);
                                IOUtils.closeQuietly(errorStream);
                                IOUtils.closeQuietly(bufferedReader);
                                if (openProcess != null) {
                                    openProcess.destroy();
                                }
                                throw e;
                            }
                        } catch (Throwable th2) {
                            e = th2;
                            bufferedReader = null;
                            IOUtils.closeQuietly(inputStream);
                            IOUtils.closeQuietly(outputStream);
                            IOUtils.closeQuietly(errorStream);
                            IOUtils.closeQuietly(bufferedReader);
                            if (openProcess != null) {
                                openProcess.destroy();
                            }
                            throw e;
                        }
                    } catch (InterruptedException e3) {
                        e = e3;
                        bufferedReader = null;
                        outputStream2 = outputStream;
                        inputStream2 = inputStream;
                        process = openProcess;
                        throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
                    } catch (Throwable th3) {
                        e = th3;
                        bufferedReader = null;
                        errorStream = null;
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(outputStream);
                        IOUtils.closeQuietly(errorStream);
                        IOUtils.closeQuietly(bufferedReader);
                        if (openProcess != null) {
                            openProcess.destroy();
                        }
                        throw e;
                    }
                    try {
                        for (String readLine = bufferedReader.readLine(); readLine != null && arrayList.size() < i; readLine = bufferedReader.readLine()) {
                            arrayList.add(readLine.toLowerCase(Locale.ENGLISH).trim());
                        }
                        openProcess.waitFor();
                        ThreadMonitor.stop(start);
                        if (openProcess.exitValue() != 0) {
                            throw new IOException("Command line returned OS error code '" + openProcess.exitValue() + "' for command " + Arrays.asList(strArr));
                        } else if (arrayList.isEmpty()) {
                            throw new IOException("Command line did not return any info for command " + Arrays.asList(strArr));
                        } else {
                            IOUtils.closeQuietly(inputStream);
                            IOUtils.closeQuietly(outputStream);
                            IOUtils.closeQuietly(errorStream);
                            IOUtils.closeQuietly(bufferedReader);
                            if (openProcess != null) {
                                openProcess.destroy();
                            }
                            return arrayList;
                        }
                    } catch (InterruptedException e4) {
                        e = e4;
                        inputStream3 = errorStream;
                        outputStream2 = outputStream;
                        inputStream2 = inputStream;
                        process = openProcess;
                        throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
                    } catch (Throwable th4) {
                        e = th4;
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(outputStream);
                        IOUtils.closeQuietly(errorStream);
                        IOUtils.closeQuietly(bufferedReader);
                        if (openProcess != null) {
                            openProcess.destroy();
                        }
                        throw e;
                    }
                } catch (InterruptedException e5) {
                    e = e5;
                    bufferedReader = null;
                    outputStream2 = null;
                    inputStream2 = inputStream;
                    process = openProcess;
                    throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
                } catch (Throwable th5) {
                    e = th5;
                    bufferedReader = null;
                    errorStream = null;
                    outputStream = null;
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                    IOUtils.closeQuietly(errorStream);
                    IOUtils.closeQuietly(bufferedReader);
                    if (openProcess != null) {
                        openProcess.destroy();
                    }
                    throw e;
                }
            } catch (InterruptedException e6) {
                e = e6;
                bufferedReader = null;
                outputStream2 = null;
                inputStream2 = null;
                process = openProcess;
                throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
            } catch (Throwable th6) {
                e = th6;
                bufferedReader = null;
                errorStream = null;
                outputStream = null;
                inputStream = null;
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errorStream);
                IOUtils.closeQuietly(bufferedReader);
                if (openProcess != null) {
                    openProcess.destroy();
                }
                throw e;
            }
        } catch (InterruptedException e7) {
            e = e7;
            bufferedReader = null;
            outputStream2 = null;
            inputStream2 = null;
            process = null;
            throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(strArr) + " timeout=" + j, e);
        } catch (Throwable th7) {
            e = th7;
            bufferedReader = null;
            errorStream = null;
            outputStream = null;
            inputStream = null;
            openProcess = null;
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(errorStream);
            IOUtils.closeQuietly(bufferedReader);
            if (openProcess != null) {
                openProcess.destroy();
            }
            throw e;
        }
    }

    Process openProcess(String[] strArr) {
        return Runtime.getRuntime().exec(strArr);
    }
}
