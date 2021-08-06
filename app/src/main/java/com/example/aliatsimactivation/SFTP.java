package com.example.aliatsimactivation;

public class SFTP {

    private String server;
    private String user;
    private String pass;
    private int port;



    public SFTP()
    {
        server="10.22.28.38";
        user="root";
        pass="TKLdev#2021$";
        port=22;

    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }
}
