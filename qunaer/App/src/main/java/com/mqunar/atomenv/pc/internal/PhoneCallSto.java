package com.mqunar.atomenv.pc.internal;

import com.mqunar.atomenv.pc.AbsCallStat;
import com.mqunar.atomenv.pc.IPhoneCall;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.storage.Storage;
import java.util.Map;

public class PhoneCallSto implements IPhoneCall {
    private final Storage a = Storage.newStorage(QApplication.getContext(), "qunar_pcst");

    public void add(AbsCallStat absCallStat) {
        AbsCallStat absCallStat2 = (AbsCallStat) this.a.getSerializable(absCallStat.key(), (AbsCallStat) null);
        if (absCallStat2 == null) {
            this.a.putSerializable(absCallStat.key(), absCallStat);
            return;
        }
        absCallStat2.inc();
        this.a.putSerializable(absCallStat2.key(), absCallStat2);
    }

    public String toMsgString() {
        Map all = this.a.getAll();
        StringBuffer stringBuffer = new StringBuffer();
        if (all != null) {
            for (String str : all.keySet()) {
                String str2;
                Object obj = all.get(str2);
                if (obj != null && (obj instanceof AbsCallStat)) {
                    str2 = ((AbsCallStat) obj).toCallString();
                    if (str2 != null && str2.length() > 0) {
                        stringBuffer.append(str2);
                        stringBuffer.append("|*|");
                    }
                }
            }
        }
        clean();
        return stringBuffer.toString();
    }

    public void clean() {
        this.a.clean();
    }
}
