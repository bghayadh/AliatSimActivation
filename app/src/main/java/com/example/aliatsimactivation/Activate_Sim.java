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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

public class Activate_Sim extends AppCompatActivity {
    private Button btnip,btnsubip,btnexit,btnrechargeip,Btnregisterviaussd;
    private String registrationStatus,globalsimid,mainstatus,vagentussdid,msisdnussdid;
    private int clicks=0;
    private TextView txtrescode,txtresmessage,txtussd;
    private String stroffile;
    private String registerflag="0";
    private String rechargeflag="0";
    private String activateflag="0";
    String agentNumber;
    private String globalMode,ussddate,txtussdstatus;
    Connection conn;
    private LocalTime time,start,end;//to validate to time to access switch
    private int flagstart,flagend;//flag
    private boolean connectflag=false;




    @Override
    public void onBackPressed() {
        Intent i = new Intent(Activate_Sim.this, SimRegInfo.class);
        i.putExtra("message_key",stroffile);
        i.putExtra("globalMode",globalMode);
        i.putExtra("agentNumber",agentNumber);
        i.putExtra("db-offline-to-main",stroffile);
        startActivity(i);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
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


        Intent i=Activate_Sim.this.getIntent();
        agentNumber=i.getStringExtra("agentNumber");
        stroffile=i.getStringExtra("globalsimid");
        globalMode=i.getStringExtra("globalMode");
        vagentussdid=i.getStringExtra("agentmsisdn");
        msisdnussdid=i.getStringExtra("msisdn");
        mainstatus = i.getStringExtra("mainstatus");
        globalsimid=stroffile;
        System.out.println("test : "+stroffile);
        //initial register ,reahrge, activation flag to 0
        registerflag="0";
        rechargeflag="0";
        activateflag="0";


        //Fill txtussd with default command
        if (msisdnussdid.equalsIgnoreCase("")) {
             // no msisdn to fill txtussd
        }else {
            txtussd.setText("*111*"+msisdnussdid+"*1*"+"#");
        }


        txtussdstatus="SENT_USSD";
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Btnregisterviaussd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainstatus.toString().matches("Success")) {
                    Toast.makeText(getApplicationContext(), "Already Success", Toast.LENGTH_LONG).show();
                }else{
                    if (clicks == 0 && globalMode.equalsIgnoreCase("Online")) {
                        Toast.makeText(getApplicationContext(), "You must try once with registration via IP", Toast.LENGTH_LONG).show();
                    }
                    else{
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
                            // now update in DB or in Offline mode
                            if (networkInfo != null && networkInfo.isConnected() && globalMode.equalsIgnoreCase("Online")) {
                                thread1.start();
                            }else{

                                try {
                                    String filename = i.getStringExtra("msisdn");
                                    String myFileName = "SIM_" + filename + ".txt";
                                    File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                    File OfflineFile = new File(directory, myFileName);

                                    FileInputStream is;
                                    BufferedReader reader;
                                    is = new FileInputStream(OfflineFile);
                                    reader = new BufferedReader(new InputStreamReader(is));
                                    String line = reader.readLine();
                                    String OldContent=null;
                                    while(line != null){
                                        if (OldContent==null) {
                                            OldContent = line+System.lineSeparator();
                                        } else {
                                        OldContent = OldContent+line+System.lineSeparator(); }
                                        line = reader.readLine();

                                            if(OldContent.contains("SENT_USSD")) {

                                            } else {
                                                if(OldContent.contains("USSD")) {
                                                    System.out.println("Found it 1 " + txtussdstatus);
                                                    String newContent = OldContent.replaceAll("USSD", txtussdstatus);
                                                    FileWriter fw = new FileWriter(OfflineFile.getAbsoluteFile());
                                                    BufferedWriter bw = new BufferedWriter(fw);
                                                    bw.write(newContent.toString());
                                                    bw.close();
                                                }
                                            }



                                    }
                                    reader.close();


                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                            }
                        }else {
                            System.out.println("Please enter a valid ussd code");
                            Toast.makeText(Activate_Sim.this, "Please enter a valid ussd code", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }


        });




        if (networkInfo != null && networkInfo.isConnected() && globalMode.equalsIgnoreCase("Online")) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //setting time
                    time=LocalTime.now();
                    start=LocalTime.of(6,00,00);
                    end=LocalTime.of(22,00,00);

                    //comparing times
                    flagstart=time.compareTo(start);
                    flagend=time.compareTo(end);
                    System.out.println(flagend+" "+flagstart);
                    clicks=1;



                    System.out.println("STATUS IS HERE NOW "+ mainstatus);
                    if (mainstatus.toString().matches("Success")) {
                        globalsimid = i.getStringExtra("globalsimid");
                        Toast.makeText(getApplicationContext(), "Already Success cannot resend command", Toast.LENGTH_SHORT).show();
                    } else {
                        if(flagstart==1 && flagend==-1)
                        {
                            if (globalsimid.equalsIgnoreCase("0")) {
                                Toast.makeText(getApplicationContext(), "Save your data first", Toast.LENGTH_LONG).show();
                            } else
                            {
                                Toast.makeText(getApplicationContext(), "Please wait while registration until getting response back", Toast.LENGTH_LONG).show();
                                registerflag="1";
                                globalsimid = i.getStringExtra("globalsimid");
                                //System.out.println("id : " + globalsimid);

                                String fname = i.getStringExtra("fname");
                                String mname = i.getStringExtra("mname");
                                String lname = i.getStringExtra("lname");
                                String msisdn = i.getStringExtra("msisdn");
                                String idType = i.getStringExtra("idType");
                                String idNumber = i.getStringExtra("idNumber");
                                String dob = i.getStringExtra("dob");
                                String gender = i.getStringExtra("gender");
                                String email = i.getStringExtra("email");
                                String altnumber = i.getStringExtra("altnumber");
                                String address1 = i.getStringExtra("address1");
                                String state = i.getStringExtra("state");
                                String agentmsisdn = i.getStringExtra("agentmsisdn");
                                Toast.makeText(getApplicationContext(), fname+":"+lname+":"+msisdn+":"+idNumber+":"+agentmsisdn, Toast.LENGTH_LONG).show();
                                SimRegistrationAPI registrationAPI = new SimRegistrationAPI(globalsimid, fname, mname, lname, msisdn, idType, idNumber, dob, gender, email, altnumber, address1, state, agentmsisdn);

                                try {
                                    String res = registrationAPI.execute().get();
                                    if (res != null) {

                                        String[] data = res.split("!!");
                                        txtrescode.setText(data[0]);
                                        txtresmessage.setText(data[1]);
                                     } else {
                                        Toast.makeText(getApplicationContext(), "Error Occured Please Try Again Later", Toast.LENGTH_LONG).show();
                                    }

                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }// end else 2nd

                        }else {
                            Toast.makeText(getApplicationContext(),"Please Try again between 6AM and 10PM",Toast.LENGTH_LONG).show();
                        }

                    }





                }
            });
            btnrechargeip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(registerflag.equalsIgnoreCase("0")){
                        Toast.makeText(getApplicationContext(),"Register via IP first",Toast.LENGTH_LONG).show();
                    } else {
                        rechargeflag="1";
                    }
                }
            });
            btnsubip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(registerflag.equalsIgnoreCase("0")){
                        Toast.makeText(getApplicationContext(),"Register via IP first",Toast.LENGTH_LONG).show();
                    } else {
                        activateflag="1";
                    }
                }
            });
            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //finish();
                    Intent intent= new Intent(getApplicationContext(), SimRegInfo.class);
                    intent.putExtra("message_key",globalsimid);
                    intent.putExtra("globalMode",globalMode);
                    intent.putExtra("agentNumber",agentNumber);
                    intent.putExtra("db-offline","1");
                    startActivity(intent);
                }
            });
        }else {

            btnip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Activate_Sim.this, "Off line mode", Toast.LENGTH_SHORT).show();
                }
            });

            btnrechargeip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Activate_Sim.this, "Off line mode", Toast.LENGTH_SHORT).show();
                }
            });
            btnsubip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Activate_Sim.this, "Off line mode", Toast.LENGTH_SHORT).show();
                }
            });
            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();
                    Intent intent= new Intent(getApplicationContext(), SimRegInfo.class);
                    intent.putExtra("message_key",globalsimid);
                    intent.putExtra("globalMode",globalMode);
                    intent.putExtra("db-offline","1");
                    intent.putExtra("agentNumber",agentNumber);
                    startActivity(intent);
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

    public boolean connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
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

                connectflag = false;
            } /*catch (IllegalAccessException e) {
        System.out.println("error is: " +e.toString());
        Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        connectflag=false;
    }*/ catch (Exception e) {
                System.out.println("error is: " + e.toString());

                connectflag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return connectflag;
    }

    public void SaveUpdatedUssdStatus() {

        try {


            PreparedStatement stmtinsert1 = null;

            try {
                stmtinsert1 = conn.prepareStatement("UPDATE CLIENTS" +
                        " SET " +
                        " USSD_STATUS='"+txtussdstatus.toString()+"'"+
                        "WHERE CLIENT_ID ='" + globalsimid + "'");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmtinsert1.executeUpdate();
                //Toast.makeText(getApplicationContext(), "Saving Completed", Toast.LENGTH_SHORT).show();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmtinsert1.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            boolean flg=false;
            try {
                if ((flg = connecttoDB()) == true) {
                    SaveUpdatedUssdStatus();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    });



}

