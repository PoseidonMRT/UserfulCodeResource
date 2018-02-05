package com.baidu.location.e;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.baidu.location.BDLocation;
import com.baidu.location.c.f;
import com.baidu.location.f.i;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class d {
    static final String a = "http://loc.map.baidu.com/offline_loc";
    static final String b = "com.baidu.lbs.offlinelocationprovider";
    private static Context c;
    private static volatile d d;
    private final File e;
    private final f f;
    private final b g;
    private final g h;
    private final c i;

    public enum a {
        NEED_TO_LOG,
        NO_NEED_TO_LOG
    }

    public enum b {
        IS_MIX_MODE,
        IS_NOT_MIX_MODE
    }

    enum c {
        NETWORK_UNKNOWN,
        NETWORK_WIFI,
        NETWORK_2G,
        NETWORK_3G,
        NETWORK_4G
    }

    private d() {
        File file;
        try {
            file = new File(c.getFilesDir(), "ofld");
            try {
                if (!file.exists()) {
                    file.mkdir();
                }
            } catch (Exception e) {
            }
        } catch (Exception e2) {
            file = null;
        }
        this.e = file;
        this.g = new b(this);
        this.f = new f(this.g.a());
        this.i = new c(this, this.g.a());
        this.h = new g(this, this.g.a(), this.i.n());
    }

    private BDLocation a(final String[] strArr) {
        BDLocation bDLocation = new BDLocation();
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        bDLocation = (FutureTask) newSingleThreadExecutor.submit(new Callable<BDLocation>(this) {
            final /* synthetic */ d b;

            public BDLocation a() {
                BDLocation a;
                Cursor cursor;
                Throwable th;
                Cursor cursor2 = null;
                BDLocation bDLocation = new BDLocation();
                if (strArr.length > 0) {
                    ProviderInfo resolveContentProvider;
                    try {
                        resolveContentProvider = d.c.getPackageManager().resolveContentProvider(d.b, 0);
                    } catch (Exception e) {
                        resolveContentProvider = null;
                    }
                    if (resolveContentProvider == null) {
                        String[] o = this.b.i.o();
                        for (String resolveContentProvider2 : o) {
                            try {
                                resolveContentProvider = d.c.getPackageManager().resolveContentProvider(resolveContentProvider2, 0);
                            } catch (Exception e2) {
                                resolveContentProvider = null;
                            }
                            if (resolveContentProvider != null) {
                                break;
                            }
                        }
                    }
                    if (resolveContentProvider != null) {
                        try {
                            Cursor query = d.c.getContentResolver().query(d.c(resolveContentProvider.authority), strArr, null, null, null);
                            try {
                                a = e.a(query);
                                if (query != null) {
                                    try {
                                        query.close();
                                    } catch (Exception e3) {
                                    }
                                }
                            } catch (Exception e4) {
                                cursor = query;
                                if (cursor == null) {
                                    try {
                                        cursor.close();
                                        a = bDLocation;
                                    } catch (Exception e5) {
                                        a = bDLocation;
                                    }
                                } else {
                                    a = bDLocation;
                                }
                                bDLocation = a;
                                bDLocation.setLocType(66);
                                return bDLocation;
                            } catch (Throwable th2) {
                                th = th2;
                                cursor2 = query;
                                if (cursor2 != null) {
                                    try {
                                        cursor2.close();
                                    } catch (Exception e6) {
                                    }
                                }
                                throw th;
                            }
                        } catch (Exception e7) {
                            cursor = null;
                            if (cursor == null) {
                                a = bDLocation;
                            } else {
                                cursor.close();
                                a = bDLocation;
                            }
                            bDLocation = a;
                            bDLocation.setLocType(66);
                            return bDLocation;
                        } catch (Throwable th3) {
                            th = th3;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            throw th;
                        }
                        bDLocation = a;
                    } else {
                        try {
                            cursor2 = this.b.g.a(new a(strArr));
                            bDLocation = e.a(cursor2);
                            if (cursor2 != null) {
                                try {
                                    cursor2.close();
                                } catch (Exception e8) {
                                }
                            }
                        } catch (Exception e9) {
                            if (cursor2 != null) {
                                try {
                                    cursor2.close();
                                } catch (Exception e10) {
                                }
                            }
                        } catch (Throwable th4) {
                            if (cursor2 != null) {
                                try {
                                    cursor2.close();
                                } catch (Exception e11) {
                                }
                            }
                        }
                    }
                    if (!(bDLocation == null || bDLocation.getLocType() == 67)) {
                        bDLocation.setLocType(66);
                    }
                }
                return bDLocation;
            }

            public /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            bDLocation = (BDLocation) bDLocation.get(2000, TimeUnit.MILLISECONDS);
            return bDLocation;
        } catch (InterruptedException e) {
            bDLocation.cancel(true);
            return null;
        } catch (ExecutionException e2) {
            bDLocation.cancel(true);
            return null;
        } catch (TimeoutException e3) {
            f.a().a("offlineLocation Timeout Exception!");
            bDLocation.cancel(true);
            return null;
        } finally {
            newSingleThreadExecutor.shutdown();
        }
    }

    public static d a() {
        if (d == null) {
            synchronized (d.class) {
                if (d == null) {
                    if (c == null) {
                        a(com.baidu.location.f.getServiceContext());
                    }
                    d = new d();
                }
            }
        }
        d.q();
        return d;
    }

    public static void a(Context context) {
        if (c == null) {
            c = context;
            com.baidu.location.h.c.a().a(c);
        }
    }

    private static final Uri c(String str) {
        return Uri.parse(String.format("content://%s/", new Object[]{str}));
    }

    private void q() {
        this.i.g();
    }

    private boolean r() {
        ProviderInfo resolveContentProvider;
        ProviderInfo providerInfo;
        String packageName = c.getPackageName();
        try {
            resolveContentProvider = c.getPackageManager().resolveContentProvider(b, 0);
        } catch (Exception e) {
            resolveContentProvider = null;
        }
        if (resolveContentProvider == null) {
            String[] o = this.i.o();
            providerInfo = resolveContentProvider;
            for (String resolveContentProvider2 : o) {
                try {
                    providerInfo = c.getPackageManager().resolveContentProvider(resolveContentProvider2, 0);
                } catch (Exception e2) {
                    providerInfo = null;
                }
                if (providerInfo != null) {
                    break;
                }
            }
        } else {
            providerInfo = resolveContentProvider;
        }
        return providerInfo == null ? true : packageName.equals(providerInfo.packageName);
    }

    public long a(String str) {
        return this.i.a(str);
    }

    public BDLocation a(com.baidu.location.f.a aVar, i iVar, BDLocation bDLocation, b bVar, a aVar2) {
        int a;
        String str;
        if (bVar == b.IS_MIX_MODE) {
            a = this.i.a();
            str = com.baidu.location.h.c.a().d() + "&mixMode=1";
        } else {
            str = com.baidu.location.h.c.a().d();
            a = 0;
        }
        String[] a2 = e.a(aVar, iVar, bDLocation, str, (aVar2 == a.NEED_TO_LOG ? Boolean.valueOf(true) : Boolean.valueOf(false)).booleanValue(), a);
        BDLocation bDLocation2 = null;
        if (a2.length > 0) {
            bDLocation2 = a(a2);
            if (bDLocation2 == null || bDLocation2.getLocType() != 67) {
                return bDLocation2;
            }
        }
        return bDLocation2;
    }

    public Context b() {
        return c;
    }

    File c() {
        return this.e;
    }

    public boolean d() {
        return this.i.h();
    }

    public boolean e() {
        return this.i.i();
    }

    public boolean f() {
        return this.i.j();
    }

    public boolean g() {
        return this.i.k();
    }

    public boolean h() {
        return this.i.m();
    }

    public void i() {
        this.f.a();
    }

    f j() {
        return this.f;
    }

    g k() {
        return this.h;
    }

    c l() {
        return this.i;
    }

    public void m() {
        if (r()) {
            this.g.b();
        }
    }

    public void n() {
    }

    public double o() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) c.getSystemService("connectivity")).getActiveNetworkInfo();
        c cVar = c.NETWORK_UNKNOWN;
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == 1) {
                cVar = c.NETWORK_WIFI;
            }
            if (activeNetworkInfo.getType() == 0) {
                int subtype = activeNetworkInfo.getSubtype();
                if (subtype == 1 || subtype == 2 || subtype == 4 || subtype == 7 || subtype == 11) {
                    cVar = c.NETWORK_2G;
                } else if (subtype == 3 || subtype == 5 || subtype == 6 || subtype == 8 || subtype == 9 || subtype == 10 || subtype == 12 || subtype == 14 || subtype == 15) {
                    cVar = c.NETWORK_3G;
                } else if (subtype == 13) {
                    cVar = c.NETWORK_4G;
                }
            }
        }
        return cVar == c.NETWORK_UNKNOWN ? this.i.b() : cVar == c.NETWORK_WIFI ? this.i.c() : cVar == c.NETWORK_2G ? this.i.d() : cVar == c.NETWORK_3G ? this.i.e() : cVar == c.NETWORK_4G ? this.i.f() : 0.0d;
    }
}
