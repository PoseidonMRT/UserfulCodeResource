package com.mqunar.atomenv.uc.model;

import java.io.Serializable;

public class GlobalUCookie implements Serializable {
    public static final long serialVersionUID = 1;
    public String qcookie = "";
    public String tcookie = "";
    public String vcookie = "";

    public String getQcookie() {
        return this.qcookie;
    }

    public String getVcookie() {
        return this.vcookie;
    }

    public String getTcookie() {
        return this.tcookie;
    }
}
