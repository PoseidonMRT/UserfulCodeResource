package com.mqunar.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.commons.io.IOUtils;

final class a extends Thread {
    public int a = -1;
    private final File b;
    private final String c;
    private final StringBuilder d;
    private Process e;

    public a(File file, String str, StringBuilder stringBuilder) {
        this.b = file;
        this.c = str;
        this.d = stringBuilder;
    }

    public void run() {
        try {
            int read;
            this.b.createNewFile();
            String absolutePath = this.b.getAbsolutePath();
            Runtime.getRuntime().exec("chmod 777 " + absolutePath).waitFor();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(this.b));
            if (new File("/system/bin/sh").exists()) {
                outputStreamWriter.write("#!/system/bin/sh\n");
            }
            outputStreamWriter.write(this.c);
            if (!this.c.endsWith(IOUtils.LINE_SEPARATOR_UNIX)) {
                outputStreamWriter.write(IOUtils.LINE_SEPARATOR_UNIX);
            }
            outputStreamWriter.write("exit\n");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            this.e = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(this.e.getOutputStream());
            dataOutputStream.writeBytes(absolutePath);
            dataOutputStream.flush();
            dataOutputStream.close();
            InputStreamReader inputStreamReader = new InputStreamReader(this.e.getInputStream());
            char[] cArr = new char[1024];
            while (true) {
                read = inputStreamReader.read(cArr);
                if (read == -1) {
                    break;
                } else if (this.d != null) {
                    this.d.append(cArr, 0, read);
                }
            }
            inputStreamReader = new InputStreamReader(this.e.getErrorStream());
            while (true) {
                read = inputStreamReader.read(cArr);
                if (read == -1) {
                    break;
                } else if (this.d != null) {
                    this.d.append(cArr, 0, read);
                }
            }
            if (this.e != null) {
                this.a = this.e.waitFor();
            }
            destroy();
        } catch (InterruptedException e) {
            if (this.d != null) {
                this.d.append("\nOperation timed-out");
            }
            destroy();
        } catch (Exception e2) {
            if (this.d != null) {
                this.d.append(IOUtils.LINE_SEPARATOR_UNIX + e2);
            }
            destroy();
        } catch (Throwable th) {
            destroy();
        }
    }

    public synchronized void destroy() {
        if (this.e != null) {
            this.e.destroy();
        }
        this.e = null;
    }
}
