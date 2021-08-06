package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Activate_Sim extends AppCompatActivity {
    private Button btnip,btnsubip,btnexit,btnrechargeip;
    private String registrationStatus,globalsimid;
    private int clicks=0;
    private TextView txtrescode,txtresmessage;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_sim);
        btnexit=findViewById(R.id.BtnExit);
        btnip=findViewById(R.id.Btnip);
        btnsubip=findViewById(R.id.Btnsubscribeviaip);
        btnrechargeip=findViewById(R.id.Btnrechargeviaip);


        txtrescode=findViewById(R.id.responsecode);
        txtresmessage=findViewById(R.id.responsemessage);


        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            Intent get = getIntent();
                            globalsimid = get.getStringExtra("globalsimid");
                            System.out.println("id : " + globalsimid);
                            String fname = get.getStringExtra("fname");
                            String mname = get.getStringExtra("mname");
                            String lname = get.getStringExtra("lname");
                            String msisdn = get.getStringExtra("msisdn");
                            String idType = get.getStringExtra("idType");
                            String idNumber = get.getStringExtra("idNumber");
                            String dob = get.getStringExtra("dob");
                            String gender = get.getStringExtra("gender");
                            String email = get.getStringExtra("email");
                            String altnumber = get.getStringExtra("altnumber");
                            String address1 = get.getStringExtra("address1");
                            String state = get.getStringExtra("state");
                            String agentmsisdn = get.getStringExtra("agentmsisdn");
                            System.out.println("test: " + fname + " " + mname + " " + lname + " " + msisdn + " " + idType + " " + idNumber + " " + dob + " " + gender + " " + email + " " + altnumber + " " + address1 + " " + state + " " + agentmsisdn);
                            SimRegistrationAPI registrationAPI = new SimRegistrationAPI(globalsimid, fname, mname, lname, msisdn, idType, idNumber, dob, gender, email, altnumber, address1, state, agentmsisdn);

                    try {
                        String res=registrationAPI.execute().get();
                        if(res != null)
                        {
                        System.out.println("res : "+res);
                        String[] data=res.split("!!") ;
                        txtrescode.setText(data[0]);
                        txtresmessage.setText(data[1]);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error Occured Please Try Again Later",Toast.LENGTH_LONG).show();
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }





                }
            });
            btnrechargeip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnsubip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {

            btnrechargeip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            btnsubip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }



}