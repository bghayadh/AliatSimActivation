package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    private boolean connectflag=false;
    private Connection conn;
    private String[] data;
    private String globalMode,DBMode="0",login,RegisterResult,agentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
                if(file.exists()){
                    login="login";
                    StringBuilder text = new StringBuilder();

                    try {
                        FileInputStream fIn = openFileInput("MSISDN.txt");
                        int c;
                        String temp = "";

                        while ((c = fIn.read()) != -1) {
                            temp = temp + Character.toString((char) c);
                        }
                        text.append(temp);
                        text.append('\n');


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    RegisterResult = text.toString();
                    data=RegisterResult.split(":");
                    agentNumber=data[0];
                }else{
                    login="0";
                    agentNumber="";
                }

                ConnectivityManager connMgr = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()){
                    globalMode="Online";

                    boolean flg=false;
                    if(flg=connecttoDB()==true){
                        DBMode="0";
                    }else{
                        DBMode="-100";
                    }
                }else{
                    globalMode="Offline";
                }
                Intent i=new Intent(getApplicationContext(),AgentLogin.class);
                i.putExtra("login",login);
                i.putExtra("globalMode",globalMode);
                i.putExtra("db-offline-to-main",DBMode);
                i.putExtra("agentNumber",agentNumber);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }


    public boolean connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(url, userName, password);
                if (conn != null) {
                    connectflag = true;
                } else {
                    connectflag = false;
                }

                //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
            } catch (SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
                System.out.println("error is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
            } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                System.out.println("error is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return connectflag;
    }
}