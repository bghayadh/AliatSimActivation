package com.example.aliatsimactivation;

public class PendingPictureListView {

    private String globalSimID;
    private String ClientNumer;
    private String FrontStatus;
    private String BackStatus;
    private String SignStatus;
    private String ClientStatus;


    public PendingPictureListView(String globalSimID, String clientNumer, String frontStatus, String backStatus, String signStatus, String clientStatus) {
        this.globalSimID = globalSimID;
        ClientNumer = clientNumer;
        FrontStatus = frontStatus;
        BackStatus = backStatus;
        SignStatus = signStatus;
        ClientStatus = clientStatus;
    }

    public String getGlobalSimID() {
        return globalSimID;
    }

    public void setGlobalSimID(String globalSimID) {
        this.globalSimID = globalSimID;
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

    public String getClientStatus() {
        return ClientStatus;
    }

    public void setClientStatus(String clientStatus) {
        ClientStatus = clientStatus;
    }
}

