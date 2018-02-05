package com.mqunar.storage;

import qunar.lego.utils.Goblin;

public class EggRoll {
    private static De a = new GoblinDe();

    public interface De {
        byte[] da(byte[] bArr);

        byte[] ea(byte[] bArr);
    }

    public class GoblinDe implements De {
        public byte[] ea(byte[] bArr) {
            try {
                bArr = Goblin.ea(bArr);
            } catch (Throwable th) {
            }
            return bArr;
        }

        public byte[] da(byte[] bArr) {
            try {
                bArr = Goblin.da(bArr);
            } catch (Throwable th) {
            }
            return bArr;
        }
    }

    public static void setDe(De de) {
        a = de;
    }

    public static byte[] ea(byte[] bArr) {
        return a.ea(bArr);
    }

    public static byte[] da(byte[] bArr) {
        return a.da(bArr);
    }
}
