package com.huawei.hwid.openapi.quicklogin.d;

import android.content.Context;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.lang.reflect.Field;

public class b {
    public static int a(Context context, String str, String str2) {
        try {
            int identifier = context.getResources().getIdentifier(str2, str, context.getPackageName());
            if (identifier != 0) {
                return identifier;
            }
            Field field = Class.forName(context.getPackageName() + ".R$" + str).getField(str2);
            identifier = Integer.parseInt(field.get(field.getName()).toString());
            if (identifier != 0) {
                return identifier;
            }
            d.b("ResourceLoader", "Error-resourceType=" + str + "--resourceName=" + str2 + "--resourceId =" + identifier);
            return identifier;
        } catch (Throwable e) {
            d.b("ResourceLoader", "!!!! ResourceLoader: reflect resource error-resourceType=" + str + "--resourceName=" + str2, e);
            return 0;
        } catch (Throwable e2) {
            d.b("ResourceLoader", "!!!! ResourceLoader: reflect resource error-resourceType=" + str + "--resourceName=" + str2, e2);
            return 0;
        }
    }

    public static int a(Context context, String str) {
        return a(context, "string", str);
    }

    public static int b(Context context, String str) {
        return a(context, "xml", str);
    }

    public static int c(Context context, String str) {
        return a(context, "layout", str);
    }

    public static int d(Context context, String str) {
        return a(context, "id", str);
    }

    public static int e(Context context, String str) {
        return a(context, "style", str);
    }

    public static int f(Context context, String str) {
        return a(context, "drawable", str);
    }
}
