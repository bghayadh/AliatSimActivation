package com.example.aliatsimactivation;

public class ResellerStatusListView {
    private String ChargeID;
    private String MOBILENUMBER;
    private String AMOUNT;
    private int imageIcon;

    public ResellerStatusListView(String chargeID, String MOBILENUMBER, String AMOUNT, int imageIcon) {
        ChargeID = chargeID;
        this.MOBILENUMBER = MOBILENUMBER;
        this.AMOUNT = AMOUNT;

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

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public String toString() {
        return "ResellerStatusListView{" +
                "ChargeID='" + ChargeID + '\'' +
                ", MOBILENUMBER='" + MOBILENUMBER + '\'' +
                ", AMOUNT='" + AMOUNT + '\'' +
                ", imageIcon=" + imageIcon +
                '}';
    }
}
