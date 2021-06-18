package com.example.aliatsimactivation;

public class OraDB {
    private String oraurl;
    private String orausername;
    private String orapwd;



    public OraDB() {
        oraurl = "jdbc:oracle:thin:@192.168.2.117:1521:ALM";
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
