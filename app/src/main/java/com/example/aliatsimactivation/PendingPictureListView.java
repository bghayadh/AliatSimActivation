package com.example.aliatsimactivation;

public class PendingPictureListView {

    private String globalSimID;
    private String ClientNumer;
    private String FrontStatus;
    private String BackStatus;
    private String SignStatus;

    public String getGlobalSimID() {
        return globalSimID;
    }

    public void setGlobalSimID(String globalSimID) {
        this.globalSimID = globalSimID;
    }

    public PendingPictureListView(String globalSimID, String ClientNumer, String FrontStatus, String BackStatus, String SignStatus){
        this.globalSimID=globalSimID;
        this.ClientNumer=ClientNumer;
        this.FrontStatus=FrontStatus;
        this.BackStatus=BackStatus;
        this.SignStatus=SignStatus;

    }

    public String getClientNumer() {
        return ClientNumer;
    }

    public void setClientNumer(String clientNumer) {
        ClientNumer = clientNumer;
    }

    public String getFrontStatus() {
        return FrontStatus;
    }

    public void setFrontStatus(String frontStatus) {
        FrontStatus = frontStatus;
    }

    public String getBackStatus() {
        return BackStatus;
    }

    public void setBackStatus(String backStatus) {
        BackStatus = backStatus;
    }

    public String getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(String signStatus) {
        SignStatus = signStatus;
    }



}

