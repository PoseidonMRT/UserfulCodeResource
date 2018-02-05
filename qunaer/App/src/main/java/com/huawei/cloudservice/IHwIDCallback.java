package com.huawei.cloudservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IHwIDCallback extends IInterface {

    public abstract class Stub extends Binder implements IHwIDCallback {
        public Stub() {
            attachInterface(this, "com.huawei.cloudservice.IHwIDCallback");
        }

        public static IHwIDCallback a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.huawei.cloudservice.IHwIDCallback");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IHwIDCallback)) {
                return new b(iBinder);
            }
            return (IHwIDCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    Bundle bundle;
                    parcel.enforceInterface("com.huawei.cloudservice.IHwIDCallback");
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    a(readInt, bundle);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.huawei.cloudservice.IHwIDCallback");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void a(int i, Bundle bundle);
}
