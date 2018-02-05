package com.mqunar.necro.agent.bean;

public class NecroParam extends BaseParam {
    public static final String NECRO_EMPTY_ARRAY = "[]";
    private static final long serialVersionUID = 1;
    public int cLogCount;
    public String network;

    public boolean hasData() {
        return (this.network == null || this.network.length() <= 0 || this.network.equals(NECRO_EMPTY_ARRAY)) ? false : true;
    }
}
