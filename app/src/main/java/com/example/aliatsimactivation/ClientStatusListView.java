package com.example.aliatsimactivation;

public class ClientStatusListView {


    private String MOBCHARGEID;
    private String CLIENTSUBNUM;
    private String RECHARGESTATUS;


    public ClientStatusListView(String MOBCHARGEID, String CLIENTSUBNUM, String RECHARGESTATUS) {

        this.MOBCHARGEID = MOBCHARGEID;
        this.CLIENTSUBNUM = CLIENTSUBNUM;
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

    public String getRECHARGESTATUS() {
        return RECHARGESTATUS;
    }

    public void setRECHARGESTATUS(String AMOUNT) {
        this.RECHARGESTATUS = RECHARGESTATUS;
    }
}