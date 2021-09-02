package com.example.aliatsimactivation;

public class PendingPictureListView {

    private String globalSimID;
    private String ClientNumer;
    private String FrontStatus;
    private String BackStatus;
    private String SignStatus;
    private String FrontImage;
    private String BackImage;
    private String SignImage;

    public String getFrontImage() {
        return FrontImage;
    }

    public void setFrontImage(String frontImage) {
        FrontImage = frontImage;
    }

    public String getBackImage() {
        return BackImage;
    }

    public void setBackImage(String backImage) {
        BackImage = backImage;
    }

    public String getSignImage() {
        return SignImage;
    }

    public void setSignImage(String signImage) {
        SignImage = signImage;
    }

    public String getGlobalSimID() {
        return globalSimID;
    }

    public void setGlobalSimID(String globalSimID) {
        this.globalSimID = globalSimID;
    }

    public PendingPictureListView(String globalSimID, String ClientNumer, String FrontStatus, String BackStatus, String SignStatus, String FrontImage, String BackImage, String SignImage){
        this.globalSimID=globalSimID;
        this.ClientNumer=ClientNumer;
        this.FrontStatus=FrontStatus;
        this.BackStatus=BackStatus;
        this.SignStatus=SignStatus;
        this.FrontImage=FrontImage;
        this.BackImage=BackImage;
        this.SignImage=SignImage;
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
