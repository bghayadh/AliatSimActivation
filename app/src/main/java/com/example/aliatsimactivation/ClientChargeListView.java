package com.example.aliatsimactivation;

public class ClientChargeListView {

    private String MOBCHARGEID;
    private String CLIENTSUBNUM;
    private String AMOUNT;
    private String RECHARGESTATUS;

    public ClientChargeListView(String MOBCHARGEID, String CLIENTSUBNUM, String AMOUNT, String RECHARGESTATUS) {

        this.MOBCHARGEID = MOBCHARGEID;
        this.CLIENTSUBNUM = CLIENTSUBNUM;
        this.AMOUNT = AMOUNT;
        this.RECHARGESTATUS = RECHARGESTATUS;

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

    public String getRECHARGESTATUS() {
        return RECHARGESTATUS;
    }

    public void setRECHARGESTATUS(String RECHARGESTATUS) {
        this.RECHARGESTATUS = RECHARGESTATUS;
    }
}