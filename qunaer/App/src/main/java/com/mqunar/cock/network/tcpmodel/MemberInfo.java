package com.mqunar.cock.network.tcpmodel;

import java.io.Serializable;

public class MemberInfo implements Serializable {
    private static final long serialVersionUID = 1;
    public String id;
    public String img;
    public String name;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.id.equals(((MemberInfo) obj).id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}
