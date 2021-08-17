package com.example.aliatsimactivation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Activate_Sim extends AppCompatActivity {
    private Button btnip,btnsubip,btnexit,btnrechargeip,Btnregisterviaussd;
    private String registrationStatus,globalsimid;
    private int clicks=0;
    private TextView txtrescode,txtresmessage,txtussd;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_sim);
        btnexit=findViewById(R.id.BtnExit);
        btnip=findViewById(R.id.Btnip);
        btnsubip=findViewById(R.id.Btnsubscribeviaip);
        btnrechargeip=findViewById(R.id.Btnrechargeviaip);
        Btnregisterviaussd=findViewById(R.id.Btnregisterviaussd);
        txtussd=(EditText)findViewById(R.id.txtussd);

        txtrescode=findViewById(R.id.responsecode);
        txtresmessage=findViewById(R.id.responsemessage);



        Btnregisterviaussd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UssdCode = txtussd.getText().toString();
                if (UssdCode.startsWith("*") && UssdCode.endsWith("#")) {
                    System.out.println("start with " + UssdCode);
                    //we want to remove the last # from the ussd code as we need to encode it. so *555# becomes *555
                    UssdCode = UssdCode.substring(0, UssdCode.length() - 1);

                    String UssdCodeNew = UssdCode + Uri.encode("#");

                    //request for permission
                    if (ActivityCompat.checkSelfPermission(Activate_Sim.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Activate_Sim.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        System.out.println("NO ACTION");
                    } else {
                        System.out.println("start sending " + UssdCode);
                        //dial Ussd code
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UssdCodeNew)));

                    }


                } else {
                    System.out.println("Please enter a valid ussd code");
                    Toast.makeText(Activate_Sim.this, "Please enter a valid ussd code", Toast.LENGTH_LONG).show();
                }
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent get = getIntent();
                    String mainstatus = get.getStringExtra("mainstatus");
                    System.out.println("STATUS IS HERE NOW "+ mainstatus);
                    if (mainstatus.toString().matches("Success")) {
                        Toast.makeText(getApplicationContext(), "Already Success cannot resend command", Toast.LENGTH_SHORT).show();
                    } else {
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

                        SimRegistrationAPI registrationAPI = new SimRegistrationAPI(globalsimid, fname, mname, lname, msisdn, idType, idNumber, dob, gender, email, altnumber, address1, state, agentmsisdn);

                        try {
                            String res=registrationAPI.execute().get();
                            if(res != null)
                            {

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

                    //finish();
                    Intent intent= new Intent(getApplicationContext(), SimRegInfo.class);
                    intent.putExtra("message_key",globalsimid);
                    startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(),
                "USSD: " + requestCode + "  " + resultCode + " ", Toast.LENGTH_LONG).show();

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                // String result=data.getStringExtra("result");
                String dd = data.toString();
                Toast.makeText(getApplicationContext(), dd, Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "No response comes from USSD the code returned is : "+ requestCode, Toast.LENGTH_LONG).show();
        }
    }



}