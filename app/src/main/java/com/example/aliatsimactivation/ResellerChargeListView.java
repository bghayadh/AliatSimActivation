package com.example.aliatsimactivation;

public class ResellerChargeListView {
    private String ChargeID;
    private String MOBILENUMBER;
    private String DATE;
    private String AMOUNT;
    private String STATUS;
    public ResellerChargeListView(String chargeID, String MOBILENUMBER, String DATE, String AMOUNT, String STATUS) {
        ChargeID = chargeID;
        this.MOBILENUMBER = MOBILENUMBER;
        this.DATE = DATE;
        this.AMOUNT = AMOUNT;
        this.STATUS = STATUS;
    }

    public String getChargeID() {
        return ChargeID;
    }

    public void setChargeID(String chargeID) {
        ChargeID = chargeID;
    }

    public String getMOBILENUMBER() {
        return MOBILENUMBER;
    }

    public void setMOBILENUMBER(String MOBILENUMBER) {
        this.MOBILENUMBER = MOBILENUMBER;
    }

    public String getDATE() {

        return DATE.substring(0,10);
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    @Override
    public String toString() {
        return "ResellerChargeListView{" +
                "ChargeID='" + ChargeID + '\'' +
                ", MOBILENUMBER='" + MOBILENUMBER + '\'' +
                ", DATE='" + DATE + '\'' +
                ", AMOUNT='" + AMOUNT + '\'' +
                ", STATUS='" + STATUS + '\'' +
                '}';
    }
}
