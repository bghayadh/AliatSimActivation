package com.example.aliatsimactivation;

public class ClientOfflineDataListView {


    private String TITLE;


    public ClientOfflineDataListView(String TITLE) {

        this.TITLE = TITLE;

    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    @Override
    public String toString() {
        return "OfflineDataListView{" +
                "TITLE='" + TITLE + '\'' +
                '}';
    }
}

