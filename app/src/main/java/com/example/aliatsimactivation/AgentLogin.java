package com.example.aliatsimactivation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.os.PowerManager.SCREEN_DIM_WAKE_LOCK;

public class AgentLogin extends AppCompatActivity {
    private Button btnlogin, BtnData,BtnExit;
    private TextView txtmsisdn, txtpin,txtcheck;
    private EditText editmsisdn, editpin;
    private String agentNumber, RegisterResult, DBMode,globalMode,GSTATUS,GPIN;
    private String login = "0", agentstatus, filepincode;
    private String[] data;
    private Connection conn;
    private boolean connectflag = false;
    protected PowerManager.WakeLock mWakeLock;
    private View underline;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


   /* /// for lock and unlock power button
    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        } else {
            screenOn = pm.isScreenOn();
        }
        if (!screenOn) {    //Screen off by lock or power
            Intent checkingIntent = new Intent(this, AgentLogin.class);
            //checkingIntent.putExtra("checking",true);
            checkingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(checkingIntent);
            finish();
        }
    }*/


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        /*// to fix locker issue of mobile
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();*/


        btnlogin = findViewById(R.id.signup);
        editmsisdn = findViewById(R.id.editmsisdn);
        editpin = findViewById(R.id.editpin);
        txtmsisdn = findViewById(R.id.txtmsisdn);
        txtpin = findViewById(R.id.txtpincode);
        BtnData = findViewById(R.id.BtnData);
        BtnExit= findViewById(R.id.BtnExit);
        txtcheck=findViewById(R.id.txtexists);
        underline = findViewById(R.id.underline);


        Intent i = this.getIntent();
        login = i.getStringExtra("login");
        agentNumber=i.getStringExtra("agentNumber");
        globalMode=i.getStringExtra("globalMode");
        DBMode=i.getStringExtra("db-offline-to-main");

        System.out.println("login : "+login);
        System.out.println("agentNumber : "+agentNumber);
        System.out.println("globalMode : "+globalMode);
        System.out.println("DBMode : "+DBMode);


        // check if we have permission to get our location in manifest xml file
        try {
            if (ContextCompat.checkSelfPermission (getApplicationContext ( ), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }


        if(globalMode.equalsIgnoreCase("Online")) {
            if (DBMode.equalsIgnoreCase("0")){
                getStatusPin();
                if(GSTATUS==null && GPIN==null){
                    GSTATUS="0";
                    GPIN="0";
                }
                System.out.println("GSTATUS :" + GSTATUS);
                System.out.println("GPIN :" + GPIN);


                File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
                if (file.exists()) {
                    RegisterResult = getagentpinnumber();

                    //split the line through : and set them into the edittexts of msisdn and pin
                    String[] data = RegisterResult.split(":");
                    filepincode = data[1];

                    /////////////////////////////////////////
                    System.out.println("filepincode : "+filepincode);
                    if(GPIN.equalsIgnoreCase("0") && !filepincode.equalsIgnoreCase("PIN"))
                    {
                        Intent intent = new Intent(getApplicationContext(),AgentRegistration.class);
                        intent.putExtra("globalMode",globalMode);
                        intent.putExtra("db-offline-to-main",DBMode);
                        intent.putExtra("msisdn",agentNumber);
                        startActivity(intent);
                        return;
                    }
                    /////////////////////////////////////////

                    if (GSTATUS.equalsIgnoreCase("Activated") && filepincode.toString().trim().equalsIgnoreCase(GPIN)) {
                        Toast.makeText(getApplicationContext(),"You are logged using agent number: "+agentNumber,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("globalMode", globalMode);
                        intent.putExtra("db-offline-to-main", DBMode);
                        intent.putExtra("agentNumber", agentNumber);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "You are not activated", Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
                if (file.exists()) {
                    String RegisterResult = getagentpinnumber();
                    //split the line through : and set them into the edittexts of msisdn and pin
                    String[] data = RegisterResult.split(":");
                    filepincode = data[1];
                    Toast.makeText(getApplicationContext(), "You Don't have reachability", Toast.LENGTH_LONG).show();
                    if (!filepincode.trim().toString().equalsIgnoreCase("PIN")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("globalMode", globalMode);
                        intent.putExtra("db-offline-to-main", DBMode);
                        intent.putExtra("agentNumber", agentNumber);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Not Activated yet !",Toast.LENGTH_LONG).show();

                    }
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"You are offline",Toast.LENGTH_LONG).show();
            File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
            if (file.exists()) {
                String RegisterResult = getagentpinnumber();
                //split the line through : and set them into the edittexts of msisdn and pin
                String[] data = RegisterResult.split(":");
                filepincode = data[1];
                if (!filepincode.trim().toString().equalsIgnoreCase("PIN")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("globalMode", globalMode);
                    intent.putExtra("db-offline-to-main", DBMode);
                    intent.putExtra("agentNumber", agentNumber);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Not Activated yet !",Toast.LENGTH_LONG).show();

                }
            }
        }

        if(login.equalsIgnoreCase("0")){
            txtmsisdn.setVisibility(View.INVISIBLE);
            txtpin.setVisibility(View.INVISIBLE);
            editmsisdn.setVisibility(View.INVISIBLE);
            editpin.setVisibility(View.INVISIBLE);
        }else{
            btnlogin.setText("LOGIN");
            editmsisdn.setText(agentNumber);
            editmsisdn.setEnabled(false);
            txtcheck.setVisibility(View.INVISIBLE);
            underline.setVisibility(View.GONE);
        }

        BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.equalsIgnoreCase("0")){
                    Intent intent = new Intent(getApplicationContext(),AgentRegistration.class);
                    intent.putExtra("globalMode",globalMode);
                    intent.putExtra("db-offline-to-main",DBMode);
                    intent.putExtra("msisdn",agentNumber);
                    startActivity(intent);
                }else{
                    getStatusPin();
                    if(GSTATUS.equalsIgnoreCase("Activated") && editpin.getText().toString().equalsIgnoreCase(GPIN)){
                        try{
                            File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");

                            FileInputStream is;
                            BufferedReader reader;
                            is = new FileInputStream(file);
                            reader = new BufferedReader(new InputStreamReader(is));
                            String line = reader.readLine();
                            String OldContent=null;
                            while(line != null){
                                if (OldContent==null) {
                                    OldContent = line+System.lineSeparator();
                                } else {
                                    OldContent = OldContent+line+System.lineSeparator(); }
                                line = reader.readLine();

                                if(OldContent.contains("PIN")) {
                                    String newContent = OldContent.replaceAll("PIN",editpin.getText().toString());
                                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(newContent.toString());
                                    bw.close();
                                }


                            }
                            reader.close();


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("globalMode",globalMode);
                        intent.putExtra("db-offline-to-main",DBMode);
                        intent.putExtra("agentNumber",agentNumber);
                        startActivity(intent);
                    }else{
                        if(! editpin.getText().toString().trim().equalsIgnoreCase(GPIN)){
                            Toast.makeText(getApplicationContext(),"Invalid Pin",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not Activated yet !",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

        File file = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
        if(file.exists()){
            BtnData.setVisibility(View.VISIBLE);
        }

        BtnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fstream=null;
                StringBuffer sbuffer=new StringBuffer();
                try {
                    File file = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
                    fstream = new FileInputStream(file);
                    int i;
                    while ((i = fstream.read())!= -1){
                        sbuffer.append((char)i);
                    }
                    fstream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String details[] = sbuffer.toString().split(":");
                String a = details[0];
                String b = details[1];
                String c = details[2];
                String d = details[3];
                String e = details[4];
                String f = details[5];
                String g = details[6];
                String h = details[7];
                String i = details[8];
                String j = details[9];
                String k = details[10];
                String l = details[11];


                System.out.println(a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g+" "+h+" "+i+" "+j+" "+k+" ");
                Intent intent = new Intent(getApplicationContext(),AgentRegistration.class);
                intent.putExtra("fname",a);
                intent.putExtra("lname",b);
                intent.putExtra("dname",c);
                intent.putExtra("address",d);
                intent.putExtra("email",e);
                intent.putExtra("msisdn",f);
                intent.putExtra("regionname",g);
                intent.putExtra("long",h);
                intent.putExtra("lat",i);
                intent.putExtra("img",j);
                intent.putExtra("front",k);
                intent.putExtra("back",l);
                intent.putExtra("message_key", "1");
                intent.putExtra("globalMode", globalMode);
                intent.putExtra("db-offline-to-main", DBMode);
                startActivity(intent);
            }
        });

        txtcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(AgentLogin.this);
                mydialog.setTitle("Enter your MSISDN");

                final EditText msisdn = new EditText(AgentLogin.this);
                msisdn.setInputType(InputType.TYPE_CLASS_PHONE);
                mydialog.setView(msisdn);

                mydialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Please wait.. Trying to login",Toast.LENGTH_LONG).show();
                        if(globalMode.equalsIgnoreCase("Online")) {
                            if(DBMode.equalsIgnoreCase("-100")){
                                Toast.makeText(getApplicationContext(),"Reachability issue, no connection to database please try again later",Toast.LENGTH_LONG).show();

                            }else {
                                agentNumber = msisdn.getText().toString();

                                if (validateagentnumber(agentNumber.toString()) == true) {
                                    System.out.println("Existed");
                                    getStatusPin();
                                    Toast.makeText(getApplicationContext(),"Logging in successfully, please wait",Toast.LENGTH_LONG).show();
                                    System.out.println("PINCODE= " + GPIN);
                                    createandSaveMSISDNandPIN(agentNumber.toString(), GPIN);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("globalMode", globalMode);
                                    intent.putExtra("db-offline-to-main", DBMode);
                                    intent.putExtra("agentNumber", agentNumber.toString());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Not Found, create new Account",Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"You are Offline please try again later",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mydialog.show();
            }
        });

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

    public void getStatusPin()
    {
        System.out.println("MY NUMBEER IS " +agentNumber);
        String PIN = null;
        String pin = null;
        boolean flg=false;
        if ((flg = connecttoDB()) == true) {
            Statement stmt1 = null;

            try {
                stmt1 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String sqlStmt1 = "SELECT PIN_CODE FROM AGENT WHERE MSISDN = '"+ agentNumber+"' ";
            ResultSet rs1 = null;

            try {
                rs1 = stmt1.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs1.next()) break;
                    GPIN = (rs1.getString("PIN_CODE"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            ///////////////////////////////
            Statement stmt2 = null;

            try {
                stmt2 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String sqlStmt2 = "SELECT STATUS FROM AGENT WHERE MSISDN = '"+ agentNumber+"'";
            ResultSet rs2 = null;

            try {
                rs2 = stmt2.executeQuery(sqlStmt2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs2.next()) break;
                    GSTATUS = (rs2.getString("STATUS"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            try {
                rs2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }


    }

    public String getagentpinnumber() {
        String result = null;
        File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
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
        result = text.toString();
        return result;
    }

    //function to create and save the msisdn and pin
    private void createandSaveMSISDNandPIN(String vmsisdn,String vpin){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("MSISDN.txt", MODE_PRIVATE);
            fos.write(vmsisdn.getBytes());
            fos.write(":".getBytes());
            fos.write(vpin.getBytes());
            Toast.makeText(this, "Data is saved "+ getFilesDir(), Toast.LENGTH_SHORT).show();
            System.out.println(getFilesDir());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos!= null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean validateagentnumber(String vagentNumber) {
        boolean agentNbr=false;
        String agentNmbr;
        Statement stmt1 = null;
        if ((connecttoDB()) == true) {
            try {
                stmt1 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt = "select * from AGENT where MSISDN='" + vagentNumber + "' AND STATUS='Activated' ";
            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery(sqlStmt);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs1.next()) break;
                    agentNmbr = rs1.getString("MSISDN");
                    agentNbr = true;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs1.close();
                stmt1.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return agentNbr;

    }

}
