package com.mqunar.core;

import android.text.TextUtils;
import com.mqunar.module.ModuleInfo;
import com.mqunar.module.ModuleInfo.DexInfo;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class DexPathList {
    private final ClassLoader a;
    private Element[] b;
    private final File[] c;

    public class Element {
        final /* synthetic */ DexPathList a;
        public DexFile dexFile;
        public final File file;
        public File optimizedDirectory;
        public final ZipFile zipFile;

        public Element(DexPathList dexPathList, File file, ZipFile zipFile, DexFile dexFile) {
            this.a = dexPathList;
            this.file = file;
            this.zipFile = zipFile;
            this.dexFile = dexFile;
        }

        public Class findClass(String str) {
            if (this.dexFile != null) {
                Class loadClass = this.dexFile.loadClass(str, this.a.a);
                if (loadClass != null) {
                    return loadClass;
                }
            }
            return null;
        }

        public URL findResource(String str) {
            if (this.zipFile == null || this.zipFile.getEntry(str) == null) {
                return null;
            }
            try {
                return new URL("jar:" + this.file.toURL() + "!/" + str);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DexPathList(ClassLoader classLoader, ModuleInfo moduleInfo, String str, File file) {
        int i = 0;
        if (classLoader == null) {
            throw new NullPointerException("definingContext == null");
        } else if (moduleInfo == null || TextUtils.isEmpty(moduleInfo.fileName)) {
            throw new NullPointerException("dexPath == null");
        } else {
            int i2;
            if (file != null) {
                if (!file.exists()) {
                    file = null;
                } else if (!(file.canRead() && file.canWrite())) {
                    file = null;
                }
            }
            this.a = classLoader;
            this.c = a(str);
            if (moduleInfo.dexList == null) {
                i2 = 1;
            } else {
                i2 = moduleInfo.dexList.size() + 1;
            }
            this.b = new Element[i2];
            File file2 = new File(moduleInfo.fileName);
            this.b[0] = new Element(this, file2, moduleInfo.zipFile, a(file2, file));
            if (moduleInfo.dexList != null) {
                while (i < moduleInfo.dexList.size()) {
                    DexInfo dexInfo = (DexInfo) moduleInfo.dexList.get(i);
                    dexInfo.indexInElement = i + 1;
                    this.b[dexInfo.indexInElement] = new Element(this, dexInfo.dexOutPath, null, null);
                    this.b[dexInfo.indexInElement].optimizedDirectory = file;
                    i++;
                }
            }
        }
    }

    public void loadMultiDex(int i) {
        Element element = this.b[i];
        element.dexFile = a(element.file, element.optimizedDirectory);
    }

    public Element getMultiDexElement(int i) {
        return this.b[i];
    }

    private static File[] a(String str) {
        ArrayList a = a(str, System.getProperty("java.library.path", "."), true);
        return (File[]) a.toArray(new File[a.size()]);
    }

    private static ArrayList<File> a(String str, String str2, boolean z) {
        ArrayList arrayList = new ArrayList();
        a(str, z, arrayList);
        a(str2, z, arrayList);
        return arrayList;
    }

    private static void a(String str, boolean z, ArrayList<File> arrayList) {
        if (str != null) {
            for (String file : str.split(Pattern.quote(File.pathSeparator))) {
                File file2 = new File(file);
                if (file2.exists() && file2.canRead()) {
                    if (z) {
                        if (!file2.isDirectory()) {
                        }
                    } else if (!file2.isFile()) {
                    }
                    arrayList.add(file2);
                }
            }
        }
    }

    private static DexFile a(File file, File file2) {
        if (file2 == null) {
            return new DexFile(file);
        }
        try {
            return DexFile.loadDex(file.getAbsolutePath(), optimizedPathFor(file, file2), 0);
        } catch (Throwable e) {
            throw new IOException("load file error,file is " + file + ",length is " + file.length() + ",md5 is " + ModuleParser.getMd5(file), e);
        }
    }

    public static String optimizedPathFor(File file, File file2) {
        return optimizedPathFor(file.getName(), file2);
    }

    public static String optimizedPathFor(String str, File file) {
        if (!str.endsWith(".dex")) {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf < 0) {
                str = str + ".dex";
            } else {
                StringBuilder stringBuilder = new StringBuilder(lastIndexOf + 4);
                stringBuilder.append(str, 0, lastIndexOf);
                stringBuilder.append(".dex");
                str = stringBuilder.toString();
            }
        }
        return new File(file, str).getPath();
    }

    public Class findClass(String str) {
        for (Element findClass : this.b) {
            Class findClass2 = findClass.findClass(str);
            if (findClass2 != null) {
                return findClass2;
            }
        }
        return null;
    }

    public URL findResource(String str) {
        for (Element findResource : this.b) {
            URL findResource2 = findResource.findResource(str);
            if (findResource2 != null) {
                return findResource2;
            }
        }
        return null;
    }

    public Enumeration<URL> findResources(String str) {
        Collection arrayList = new ArrayList();
        for (Element findResource : this.b) {
            URL findResource2 = findResource.findResource(str);
            if (findResource2 != null) {
                arrayList.add(findResource2);
            }
        }
        return Collections.enumeration(arrayList);
    }

    public String findLibrary(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        for (File file : this.c) {
            File file2 = new File(file, mapLibraryName);
            if (file2.exists() && file2.isFile() && file2.canRead()) {
                return file2.getPath();
            }
        }
        return null;
    }
}
