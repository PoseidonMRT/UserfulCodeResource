package com.mqunar.core.res;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.exception.ResNotFoundException;
import com.mqunar.spider.QunarApp;
import com.mqunar.tools.log.QLog;
import java.io.InputStream;
import java.util.List;

public class ResourcesInfo {
    public static Resources qResources;
    public static Resources sysResources;

    public class QResources extends Resources {
        public QResources(AssetManager assetManager, DisplayMetrics displayMetrics, Configuration configuration) {
            super(assetManager, displayMetrics, configuration);
        }

        public int getIdentifier(String str, String str2, String str3) {
            int identifier = super.getIdentifier(str, str2, str3);
            if (identifier != 0) {
                return identifier;
            }
            List<String> allResPackageName = QunarApkLoader.getAllResPackageName();
            allResPackageName.add(QunarApkLoader.getAppContext().getPackageName());
            for (String str4 : allResPackageName) {
                if (!str4.equals(str3)) {
                    identifier = super.getIdentifier(str, str2, str4);
                    if (identifier != 0) {
                        return identifier;
                    }
                }
            }
            return 0;
        }

        public CharSequence getText(int i) {
            try {
                return super.getText(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public CharSequence getQuantityText(int i, int i2) {
            try {
                return super.getQuantityText(i, i2);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getString(int i) {
            try {
                return super.getString(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getString(int i, Object... objArr) {
            try {
                return super.getString(i, objArr);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getQuantityString(int i, int i2, Object... objArr) {
            try {
                return super.getQuantityString(i, i2, objArr);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getQuantityString(int i, int i2) {
            try {
                return super.getQuantityString(i, i2);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public CharSequence getText(int i, CharSequence charSequence) {
            try {
                return super.getText(i, charSequence);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public CharSequence[] getTextArray(int i) {
            try {
                return super.getTextArray(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String[] getStringArray(int i) {
            try {
                return super.getStringArray(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public int[] getIntArray(int i) {
            try {
                return super.getIntArray(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public TypedArray obtainTypedArray(int i) {
            try {
                return super.obtainTypedArray(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public float getDimension(int i) {
            try {
                return super.getDimension(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public int getDimensionPixelOffset(int i) {
            try {
                return super.getDimensionPixelOffset(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public int getDimensionPixelSize(int i) {
            try {
                return super.getDimensionPixelSize(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public float getFraction(int i, int i2, int i3) {
            try {
                return super.getFraction(i, i2, i3);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public Drawable getDrawable(int i) {
            try {
                return super.getDrawable(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public Drawable getDrawable(int i, Theme theme) {
            try {
                return super.getDrawable(i, theme);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public Drawable getDrawableForDensity(int i, int i2) {
            try {
                return super.getDrawableForDensity(i, i2);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public Drawable getDrawableForDensity(int i, int i2, Theme theme) {
            try {
                return super.getDrawableForDensity(i, i2, theme);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public Movie getMovie(int i) {
            try {
                return super.getMovie(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public int getColor(int i) {
            try {
                return super.getColor(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public ColorStateList getColorStateList(int i) {
            try {
                return super.getColorStateList(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public boolean getBoolean(int i) {
            try {
                return super.getBoolean(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public int getInteger(int i) {
            try {
                return super.getInteger(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public XmlResourceParser getLayout(int i) {
            try {
                return super.getLayout(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public XmlResourceParser getAnimation(int i) {
            try {
                return super.getAnimation(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public XmlResourceParser getXml(int i) {
            try {
                return super.getXml(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public InputStream openRawResource(int i) {
            try {
                return super.openRawResource(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public InputStream openRawResource(int i, TypedValue typedValue) {
            try {
                return super.openRawResource(i, typedValue);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public AssetFileDescriptor openRawResourceFd(int i) {
            try {
                return super.openRawResourceFd(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public void getValue(int i, TypedValue typedValue, boolean z) {
            try {
                super.getValue(i, typedValue, z);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public void getValueForDensity(int i, int i2, TypedValue typedValue, boolean z) {
            try {
                super.getValueForDensity(i, i2, typedValue, z);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public void getValue(String str, TypedValue typedValue, boolean z) {
            try {
                super.getValue(str, typedValue, z);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(getIdentifier(str, "string", null), e);
            }
        }

        public String getResourceName(int i) {
            try {
                return super.getResourceName(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getResourcePackageName(int i) {
            try {
                return super.getResourcePackageName(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getResourceTypeName(int i) {
            try {
                return super.getResourceTypeName(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }

        public String getResourceEntryName(int i) {
            try {
                return super.getResourceEntryName(i);
            } catch (NotFoundException e) {
                throw new ResNotFoundException(i, e);
            }
        }
    }

    public static Resources genNewResources() {
        QLog.d("fuck", "AssetManager.class.getDeclaredMethod", new Object[0]);
        QLog.d("fuck", "QunarApkLoader.getAppContext().getResources()", new Object[0]);
        if (sysResources == null) {
            sysResources = QunarApkLoader.getAppContext().getResources();
        }
        QLog.d("fuck", "AssetManager.class.newInstance()", new Object[0]);
        try {
            AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
            QLog.d("fuck", "addModulesAssetsPath", new Object[0]);
            QunarApkLoader.addModulesAssetsPath(sysResources, assetManager);
            QLog.d("fuck", "new Resources", new Object[0]);
            qResources = new QResources(assetManager, sysResources.getDisplayMetrics(), sysResources.getConfiguration());
            QLog.d("fuck", "qResources.newTheme()", new Object[0]);
            Theme newTheme = qResources.newTheme();
            QLog.d("fuck", " mTheme.setTo(QunarApkLoader.getAppContext().getTheme());", new Object[0]);
            Theme theme = QunarApkLoader.getAppContext().getTheme();
            if (theme != null) {
                newTheme.setTo(theme);
            }
            return qResources;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Resources getCurrentResources() {
        if (qResources != null) {
            return qResources;
        }
        if (sysResources == null) {
            sysResources = ((QunarApp) QunarApkLoader.getAppContext()).getSuperResources();
        }
        return genNewResources();
    }
}
