package com.example.aliatsimactivation;

public class OraDB {
    private String oraurl;
    private String orausername;
    private String orapwd;



    public OraDB() {
        oraurl = "jdbc:oracle:thin:@10.22.28.33:1523:ALM";
        orausername = "alm";
        orapwd = "alm";
    }


    public String getoraurl() {
        return oraurl;
    }

    public String getorausername() {
        return orausername;
    }

    public String getorapwd() {
        return orapwd;
    }



}
