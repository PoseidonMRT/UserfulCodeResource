package com.huawei.cloudservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface ICloudAccount extends IInterface {

    public abstract class Stub extends Binder implements ICloudAccount {
        public Stub() {
            attachInterface(this, "com.huawei.cloudservice.ICloudAccount");
        }

        public static ICloudAccount a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.huawei.cloudservice.ICloudAccount");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ICloudAccount)) {
                return new a(iBinder);
            }
            return (ICloudAccount) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    Bundle bundle;
                    parcel.enforceInterface("com.huawei.cloudservice.ICloudAccount");
                    String readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    a(readString, bundle, com.huawei.cloudservice.IHwIDCallback.Stub.a(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.huawei.cloudservice.ICloudAccount");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void a(String str, Bundle bundle, IHwIDCallback iHwIDCallback);
}
