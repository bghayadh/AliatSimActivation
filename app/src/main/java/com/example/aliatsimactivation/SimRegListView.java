package com.example.aliatsimactivation;

public class SimRegListView<V> {
    private String SimRegListViewId;
    private String name;
    private String lastname;
    private String nationality;
    private String email;
    public SimRegListView(String simRegListViewId, String name, String lastname,  String nationality, String email) {
        SimRegListViewId = simRegListViewId;
        this.name = name;
        this.lastname = lastname;

        this.nationality = nationality;
        this.email = email;

    }

    public SimRegListView() {
    }

    public String getSimRegListViewId() {
        return SimRegListViewId;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }


    public String getNationality() {
        return nationality;
    }

    public String getEmail() {
        return email;
    }

    public void setSimRegListViewId(String simRegListViewId) {
        SimRegListViewId = simRegListViewId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        lastname = lastname;
    }


    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
