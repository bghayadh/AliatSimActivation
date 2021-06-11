package com.example.aliatsimactivation;

public class ClientStatusListView {


    private String MOBCHARGEID;
    private String CLIENTSUBNUM;
    private String AMOUNT;


    public ClientStatusListView(String MOBCHARGEID, String CLIENTSUBNUM, String AMOUNT) {

        this.MOBCHARGEID = MOBCHARGEID;
        this.CLIENTSUBNUM = CLIENTSUBNUM;
        this.AMOUNT = AMOUNT;

    }

    public String getMOBCHARGEID() {
        return MOBCHARGEID;
    }

    public void setMOBCHARGEID(String MOBCHARGEID) {
        this.MOBCHARGEID = MOBCHARGEID;
    }

    public String getCLIENTSUBNUM() {
        return CLIENTSUBNUM;
    }

    public void setCLIENTSUBNUM(String CLIENTSUBNUM) {
        this.CLIENTSUBNUM = CLIENTSUBNUM;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}