package com.mqunar.atomenv.uc.model;

import java.io.Serializable;

public class GlobalUCDisplayAndValue implements Serializable {
    public static final long serialVersionUID = 1;
    public String display;
    public String value;

    public String getDisplay() {
        return this.display;
    }

    public String getValue() {
        return this.value;
    }
}
