package com.mqunar.atomenv.pc;

public interface IPhoneCall {
    void add(AbsCallStat absCallStat);

    void clean();

    String toMsgString();
}
