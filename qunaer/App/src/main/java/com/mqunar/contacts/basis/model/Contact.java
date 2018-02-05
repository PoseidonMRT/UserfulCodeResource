package com.mqunar.contacts.basis.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.mqunar.xutils.dbutils.annotation.Table;
import com.mqunar.xutils.dbutils.annotation.Transient;
import java.io.Serializable;

@Table(name = "contacts")
public class Contact implements Serializable {
    public static final String NUMBER = "#";
    public static final String OTHER = "*";
    private int id;
    private String name;
    @JSONField(name = "mobile")
    private String phone;
    @Transient
    private String prefix;

    @JSONField(deserialize = false, serialize = false)
    public int getId() {
        return this.id;
    }

    @JSONField(deserialize = false, serialize = false)
    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    @JSONField(name = "mobile")
    public String getPhone() {
        return this.phone;
    }

    @JSONField(name = "mobile")
    public void setPhone(String str) {
        this.phone = str;
    }

    @JSONField(deserialize = false, serialize = false)
    public String getPrefix() {
        return this.prefix;
    }

    @JSONField(deserialize = false, serialize = false)
    public void setPrefix(String str) {
        this.prefix = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contact contact = (Contact) obj;
        if (this.name == null ? contact.name != null : !this.name.equals(contact.name)) {
            return false;
        }
        if (this.phone != null) {
            if (this.phone.equals(contact.phone)) {
                return true;
            }
        } else if (contact.phone == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        if (this.name != null) {
            hashCode = this.name.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode *= 31;
        if (this.phone != null) {
            i = this.phone.hashCode();
        }
        return hashCode + i;
    }
}
