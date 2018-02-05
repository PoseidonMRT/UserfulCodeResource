package com.huawei.hwid.openapi.quicklogin.d.a;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build.VERSION;
import com.huawei.android.os.BuildEx;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class b {
    public static boolean a(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        if (allNetworkInfo == null) {
            return false;
        }
        for (NetworkInfo state : allNetworkInfo) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static String b(Context context) {
        String[] strArr = new String[]{"Unknown", "Unknown"};
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            strArr[0] = "Unknown";
            return strArr[0];
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            strArr[0] = "Unknown";
            return strArr[0];
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo == null || networkInfo.getState() != State.CONNECTED) {
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
            if (networkInfo2 == null || networkInfo2.getState() != State.CONNECTED) {
                return strArr[0];
            }
            strArr[0] = "2G/3G/4G/";
            strArr[1] = networkInfo2.getSubtypeName();
            return strArr[0] + strArr[1];
        }
        strArr[0] = "Wi-Fi";
        return strArr[0];
    }

    public static String a() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date());
    }

    public static int b() {
        int intValue;
        try {
            Object a = a("android.os.UserHandle", "myUserId", null, null);
            if (a != null) {
                intValue = ((Integer) a).intValue();
                c.b("BaseUtil", "getAndroidSystemUserId =" + intValue);
                return intValue;
            }
        } catch (Exception e) {
            c.c("BaseUtil", e.getMessage());
        }
        intValue = -1;
        c.b("BaseUtil", "getAndroidSystemUserId =" + intValue);
        return intValue;
    }

    public static Object a(String str, String str2, Class[] clsArr, Object[] objArr) {
        try {
            return a(Class.forName(str), str2, clsArr, objArr);
        } catch (Throwable e) {
            c.c("BaseUtil", e.getMessage(), e);
            return null;
        } catch (Throwable e2) {
            c.c("BaseUtil", e2.getMessage(), e2);
            return null;
        } catch (Throwable e22) {
            c.c("BaseUtil", e22.getMessage(), e22);
            return null;
        }
    }

    public static Object a(Class cls, String str, Class[] clsArr, Object[] objArr) {
        Object obj = null;
        if (cls == null) {
            throw new Exception("class is null in staticFun");
        }
        if (clsArr == null) {
            if (objArr != null) {
                throw new Exception("paramsType is null, but params is not null");
            }
        } else if (objArr == null) {
            throw new Exception("paramsType or params should be same");
        } else if (clsArr.length != objArr.length) {
            throw new Exception("paramsType len:" + clsArr.length + " should equal params.len:" + objArr.length);
        }
        try {
            try {
                obj = cls.getMethod(str, clsArr).invoke(null, objArr);
            } catch (Throwable e) {
                c.c("BaseUtil", e.getMessage(), e);
            } catch (Throwable e2) {
                c.c("BaseUtil", e2.getMessage(), e2);
            } catch (Throwable e22) {
                c.c("BaseUtil", e22.getMessage(), e22);
            }
        } catch (Throwable e222) {
            c.c("BaseUtil", e222.getMessage(), e222);
        } catch (Throwable e2222) {
            c.b("BaseUtil", e2222.getMessage(), e2222);
        }
        return obj;
    }

    public static boolean c() {
        return b() == 0;
    }

    public static boolean d() {
        return VERSION.SDK_INT > 22;
    }

    public static boolean c(Context context) {
        boolean z;
        Account[] accountsByType;
        Throwable th;
        Cursor query;
        try {
            Object obj;
            query = context.getContentResolver().query(Uri.parse("content://com.huawei.hwid.api.provider/has_login"), null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        if (1 == query.getInt(query.getColumnIndex("hasLogin"))) {
                            z = true;
                        } else {
                            z = false;
                        }
                        c.b("BaseUtil", "Account has Login: " + z);
                        obj = 1;
                        if (obj == null) {
                            c.b("BaseUtil", "There's no permission or APK is not supported");
                            accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
                            if (accountsByType != null || accountsByType.length <= 0) {
                                z = false;
                            } else {
                                z = true;
                            }
                        }
                        if (query != null) {
                            query.close();
                        }
                        return z;
                    }
                } catch (RuntimeException e) {
                    try {
                        accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
                        if (accountsByType != null || accountsByType.length <= 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if (query != null) {
                            query.close();
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (query != null) {
                            query.close();
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
                    if (accountsByType != null || accountsByType.length <= 0) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (query != null) {
                        query.close();
                    }
                    return z;
                }
            }
            obj = null;
            z = false;
            if (obj == null) {
                c.b("BaseUtil", "There's no permission or APK is not supported");
                accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
                if (accountsByType != null) {
                }
                z = false;
            }
            if (query != null) {
                query.close();
            }
        } catch (RuntimeException e3) {
            query = null;
            accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
            if (accountsByType != null) {
            }
            z = false;
            if (query != null) {
                query.close();
            }
            return z;
        } catch (Exception e4) {
            query = null;
            accountsByType = AccountManager.get(context).getAccountsByType("com.huawei.hwid");
            if (accountsByType != null) {
            }
            z = false;
            if (query != null) {
                query.close();
            }
            return z;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return z;
    }

    public static boolean a(Activity activity) {
        if ((activity.getWindow().getAttributes().flags & 1024) == 1024) {
            return true;
        }
        return false;
    }

    public static boolean e() {
        if (!a("com.huawei.android.os.BuildEx") || BuildEx.VERSION.EMUI_SDK_INT != 11) {
            return false;
        }
        c.b("BaseUtil", "BuildEx.VERSION.EMUI_SDK_INT = " + BuildEx.VERSION.EMUI_SDK_INT);
        return true;
    }

    public static boolean a(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            c.d("isExsit", "The class is not existing: " + str);
            return false;
        }
    }
}
