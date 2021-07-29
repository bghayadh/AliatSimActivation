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
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class Activate_Sim extends AppCompatActivity {
    private Button btnip,btnsms,btnexit,btnactivate;
    private String registrationStatus;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_sim);
        btnexit=findViewById(R.id.BtnExit);
        btnip=findViewById(R.id.Btnip);
        btnsms=findViewById(R.id.Btnsms);
        btnactivate=findViewById(R.id.Btnactivate);
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent get=getIntent();
                    String globalsimid=get.getStringExtra("globalsimid");
                    System.out.println("id : "+globalsimid);
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
                    SimRegistrationAPI registrationAPI= new SimRegistrationAPI(globalsimid,fname,mname,lname,msisdn,idType,idNumber,dob,gender,email,altnumber,address1,state,agentmsisdn);
                    registrationAPI.execute();



                    connecttoDB();

                    Statement stmt1=null;
                    try{
                        stmt1=conn.createStatement();
                    }catch (SQLException throwables){
                        throwables.printStackTrace();
                    }

                    String sqlstmt = "SELECT REGISTRATION_STATUS FROM SIM_REGISTRATION WHERE SIM_REG_ID='"+globalsimid+"'";
                    ResultSet rs1=null;
                    try{
                        rs1=stmt1.executeQuery(sqlstmt);
                    }catch (SQLException throwables){
                        throwables.printStackTrace();
                    }
                    while (true) {
                        try {
                            if (!rs1.next()) break;

                            registrationStatus = rs1.getString("REGISTRATION_STATUS");
                            System.out.println("status : " + registrationStatus.toString());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }try {
                        rs1.close ( );
                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }
                    try {
                        stmt1.close ( );
                        conn.close ( );
                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }

                    if(registrationStatus=="Failed")
                    {
                        btnactivate.setVisibility(View.INVISIBLE);
                    }
                    if(registrationStatus=="Success"){
                        btnactivate.setVisibility(View.VISIBLE);
                    }




                    /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
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
                    managerCompat.notify(1, builder.build());*/

                    //SimRegistrationAPI registrationAPI= new SimRegistrationAPI(fname,mname,lname,msisdn,idType,idNumber,dob,gender,email,altnumber,address1,state,agentmsisdn);


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

    public void connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            Toast.makeText (getApplicationContext(),"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " + e.toString());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (InstantiationException e) {
            System.out.println("error is: " + e.toString());
             Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}