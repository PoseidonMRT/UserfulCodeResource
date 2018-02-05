package com.mqunar.atomenv.pc.internal;

import com.mqunar.atomenv.pc.AbsCallStat;
import com.mqunar.atomenv.pc.IPhoneCall;
import java.util.ArrayList;

public class PhoneCallMem implements IPhoneCall {
    private ArrayList<AbsCallStat> a = new ArrayList();

    public void add(AbsCallStat absCallStat) {
        synchronized (this.a) {
            int indexOf = this.a.indexOf(absCallStat);
            if (indexOf == -1) {
                this.a.add(absCallStat);
            } else {
                ((AbsCallStat) this.a.get(indexOf)).inc();
            }
        }
    }

    public void clean() {
        this.a.clear();
    }

    public String toMsgString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.a.size(); i++) {
            String toCallString = ((AbsCallStat) this.a.get(i)).toCallString();
            if (toCallString != null && toCallString.length() > 0) {
                stringBuffer.append(toCallString);
                stringBuffer.append("|*|");
            }
        }
        clean();
        return stringBuffer.toString();
    }
}
