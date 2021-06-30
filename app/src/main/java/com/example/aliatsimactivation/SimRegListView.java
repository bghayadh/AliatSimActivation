package com.example.aliatsimactivation;

public class SimRegListView<V> {
    private String SimRegListViewId;
    private String name;
    private String mobile;
    private String status;

    public SimRegListView(String simRegListViewId, String name, String mobile, String status) {
        SimRegListViewId = simRegListViewId;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
    }

    public String getSimRegListViewId() {
        return SimRegListViewId;
    }

    public void setSimRegListViewId(String simRegListViewId) {
        SimRegListViewId = simRegListViewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
