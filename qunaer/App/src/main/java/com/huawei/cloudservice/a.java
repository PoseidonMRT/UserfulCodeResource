package com.huawei.cloudservice;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;

class a implements ICloudAccount {
    private IBinder a;

    a(IBinder iBinder) {
        this.a = iBinder;
    }

    public IBinder asBinder() {
        return this.a;
    }

    public void a(String str, Bundle bundle, IHwIDCallback iHwIDCallback) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.huawei.cloudservice.ICloudAccount");
            obtain.writeString(str);
            if (bundle != null) {
                obtain.writeInt(1);
                bundle.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            obtain.writeStrongBinder(iHwIDCallback != null ? iHwIDCallback.asBinder() : null);
            this.a.transact(1, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }
}
