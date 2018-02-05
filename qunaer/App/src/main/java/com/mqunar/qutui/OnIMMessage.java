package com.mqunar.qutui;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface OnIMMessage extends IInterface {

    public abstract class Stub extends Binder implements OnIMMessage {
        public Stub() {
            attachInterface(this, "com.mqunar.qutui.OnIMMessage");
        }

        public static OnIMMessage asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.mqunar.qutui.OnIMMessage");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof OnIMMessage)) {
                return new f(iBinder);
            }
            return (OnIMMessage) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.mqunar.qutui.OnIMMessage");
                    onImMessage(parcel.createByteArray());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.mqunar.qutui.OnIMMessage");
                    onConnected();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.mqunar.qutui.OnIMMessage");
                    onClosed();
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.mqunar.qutui.OnIMMessage");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onClosed();

    void onConnected();

    void onImMessage(byte[] bArr);
}
