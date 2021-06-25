package com.example.aliatsimactivation;

public class SimRegOfflineDataListView {
    private String TITLE;

    public SimRegOfflineDataListView(String TITLE) {
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
        return "SimRegOfflineDataListView{" +
                "TITLE='" + TITLE + '\'' +
                '}';
    }
}
