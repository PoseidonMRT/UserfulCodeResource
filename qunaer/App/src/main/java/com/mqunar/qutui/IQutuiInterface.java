package com.mqunar.qutui;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IQutuiInterface extends IInterface {

    public abstract class Stub extends Binder implements IQutuiInterface {
        public Stub() {
            attachInterface(this, "com.mqunar.qutui.IQutuiInterface");
        }

        public static IQutuiInterface asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.mqunar.qutui.IQutuiInterface");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IQutuiInterface)) {
                return new a(iBinder);
            }
            return (IQutuiInterface) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.mqunar.qutui.IQutuiInterface");
                    registerCallBack(com.mqunar.qutui.OnIMMessage.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.mqunar.qutui.IQutuiInterface");
                    unregisterCallBack(com.mqunar.qutui.OnIMMessage.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.mqunar.qutui.IQutuiInterface");
                    sendImMessage(parcel.createByteArray());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.mqunar.qutui.IQutuiInterface");
                    boolean oneKeyCremation = oneKeyCremation();
                    parcel2.writeNoException();
                    parcel2.writeInt(oneKeyCremation ? 1 : 0);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.mqunar.qutui.IQutuiInterface");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean oneKeyCremation();

    void registerCallBack(OnIMMessage onIMMessage);

    void sendImMessage(byte[] bArr);

    void unregisterCallBack(OnIMMessage onIMMessage);
}
