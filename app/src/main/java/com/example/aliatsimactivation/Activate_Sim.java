package com.example.aliatsimactivation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.ExecutionException;

public class Activate_Sim extends AppCompatActivity implements ActivationResponse {
    private Button btnip,btnsms,btnexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_sim);
        btnexit=findViewById(R.id.BtnExit);
        btnip=findViewById(R.id.Btnip);
        btnsms=findViewById(R.id.Btnsms);

        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent get=getIntent();
                    String fname=get.getStringExtra("fname");
                    String mname=get.getStringExtra("mname");
                    String lname=get.getStringExtra("lname");
                    String msisdn=get.getStringExtra("msisdn");
                    String idType=get.getStringExtra("idType");
                    String idNumber=get.getStringExtra("idNumber");
                    String dob=get.getStringExtra("dob");
                    String gender=get.getStringExtra("gender");
                    String email=get.getStringExtra("email");
                    String altnumber=get.getStringExtra("altnumber");
                    String address1=get.getStringExtra("address1");
                    String state=get.getStringExtra("state");
                    String agentmsisdn=get.getStringExtra("agentmsisdn");
                    System.out.println("test: "+fname+" "+mname+" "+lname+" "+msisdn+" "+idType+" "+idNumber+" "+dob+" "+gender+" "+email+" "+altnumber+" "+address1+" "+state+" "+agentmsisdn);
                    new SimRegistrationAPI(fname,mname,lname,msisdn,idType,idNumber,dob,gender,email,altnumber,address1,state,agentmsisdn).execute();


                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_HIGH);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
                    //sending notification with a verification code
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Activate_Sim.this, "My Notification");
                    builder.setContentTitle("TELEKOM");
                    builder.setContentText("Dear Subscriber "+msisdn+" ,please note that your SIM has been activated successfully");
                    builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Activate_Sim.this);
                    managerCompat.notify(1, builder.build());
                }
            });
            btnsms.setOnClickListener(new View.OnClickListener() {
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
                btnip.setEnabled(false);

            btnsms.setOnClickListener(new View.OnClickListener() {
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
    public void SuccessData(String data) {
        Toast.makeText(getApplicationContext(),"Response : "+data,Toast.LENGTH_LONG).show();
    }

    @Override
    public void FailedData() {
        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();

    }
}