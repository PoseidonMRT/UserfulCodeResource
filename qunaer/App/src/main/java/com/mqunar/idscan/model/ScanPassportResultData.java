package com.mqunar.idscan.model;

import java.io.Serializable;

public class ScanPassportResultData implements Serializable {
    private static final long serialVersionUID = 1;
    public String birthday;
    public String certs_cardlssuePlace2Code;
    public String certs_cardlssuePlaceName;
    public String certs_invalidday;
    public String certs_name;
    public String certs_number;
    public String certs_type;
    public String cvName;
    public String gender;
    public boolean isPP;
    public String name;
    public String nationality2Code;
    public String nationalityName;
    public boolean ok = true;
    public String scanResult;
}
