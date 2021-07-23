package com.example.aliatsimactivation;

public class SimRegReportListView {
    private String Msidn;
    private String Status;
    private String Verification;

    public SimRegReportListView(String msidn, String status, String verification) {
        Msidn = msidn;
        Status = status;
        Verification = verification;
    }

    public String getMsidn() {
        return Msidn;
    }

    public void setMsidn(String msidn) {
        Msidn = msidn;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getVerification() {
        return Verification;
    }

    public void setVerification(String verification) {
        Verification = verification;
    }

    @Override
    public String toString() {
        return "SimRegReportListView{" +
                "Msidn='" + Msidn + '\'' +
                ", Status='" + Status + '\'' +
                ", Verification='" + Verification + '\'' +
                '}';
    }
}
