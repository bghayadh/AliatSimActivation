package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AgentLogin extends AppCompatActivity {
    private Button btnlogin,BtnData;
    private TextView txtmsisdn,txtpin,txtwait;
    private EditText editmsisdn,editpin;
    private String agentNumber,RegisterResult,s0,GSTATUS,GPIN;
    private String login="0",agentstatus,filepincode;
    private String[] data;
    private Connection conn;
    private boolean connectflag=false;


    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        btnlogin=findViewById(R.id.signup);
        editmsisdn=findViewById(R.id.editmsisdn);
        editpin=findViewById(R.id.editpin);
        txtmsisdn=findViewById(R.id.txtmsisdn);
        txtpin=findViewById(R.id.txtpincode);
        txtwait=findViewById(R.id.txtwait);
        BtnData=findViewById(R.id.BtnData);
        Intent i=this.getIntent();
        login=i.getStringExtra("login");

        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext ( )
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected() ) {
            thread1.start();
        } else {
            if(login==null || login.equalsIgnoreCase("login")) {
                File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
                if(file.exists()){
                    login="login";
                    RegisterResult = getagentpinnumber();
                    //split the line through : and set them into the edittexts of msisdn and pin
                    String[] data = RegisterResult.split(":");
                    agentNumber= data[0];
                    filepincode = data[1];
                    System.out.println("agent Number : "+ agentNumber);
                    editmsisdn.setText(agentNumber);
                    editmsisdn.setEnabled(false);
                    if (filepincode.toString().trim().equalsIgnoreCase("PIN")) {
                         Toast.makeText(AgentLogin.this, "YOU ARE NOT APPROVED YET", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                        i1.putExtra("db-offline-to-main", "0");
                        i1.putExtra("globalMode", "Offline");
                        i1.putExtra("agentNumber", agentNumber);
                        startActivity(i1);
                    }
                }else {
                    login="0";
                }
            }

        }


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtwait.setText("Please wait");
                thread3.start();
            }
        });


        //check the existence of the file and disable the edittexts
        try {


            File fileDir1 = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Offlinedata.txt");
            File file1 = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
            if (file1.exists()) {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button

            }
        } catch (Exception e) {
            e.printStackTrace();
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
                startActivity(intent);
            }
        });

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

        }else {
            GSTATUS = "Offline";
        }

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

    Thread thread1 = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {

                    if(login==null || login.equalsIgnoreCase("login")){
                        File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");

                        if(file.exists()){
                            login="login";
                            RegisterResult = getagentpinnumber();
                            System.out.println("R.R : "+ RegisterResult);
                            //split the line through : and set them into the edittexts of msisdn and pin
                            String[] data = RegisterResult.split(":");
                            agentNumber= data[0];
                            filepincode = data[1].trim();
                            System.out.println("agent Number : "+ agentNumber);
                            editmsisdn.setText(agentNumber);
                            editmsisdn.setEnabled(false);

                            System.out.println("filepincode" +filepincode);
                            //read offline file
                            File file1 = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
                            if(file1.exists()){
                                Toast.makeText(getApplicationContext(),"You Must Complete Your Registration First",Toast.LENGTH_LONG).show();
                                btnlogin.setEnabled(false);
                            } else {
                                // check to go tomainactivity if activate and has pin
                                getStatusPin();
                                agentstatus = GSTATUS;
                                System.out.println("agentstatus" + agentstatus);
                            //    if(agentstatus.equalsIgnoreCase("Activate") && filepincode != "PIN")

                                // here if no reachability to DB
                                if (agentstatus.equalsIgnoreCase("Offline")) {
                                    Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                                    i1.putExtra("db-offline-to-main", "-100");
                                    i1.putExtra("globalMode", "Online");
                                    i1.putExtra("agentNumber", agentNumber);
                                    startActivity(i1);
                                } else {
                                     // if we have DB access
                                     if (agentstatus.equalsIgnoreCase("Activate") && ! filepincode.trim().equalsIgnoreCase("PIN") ) {
                                        Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                                        i1.putExtra("db-offline-to-main", "1");
                                        i1.putExtra("globalMode", "Online");
                                        i1.putExtra("agentNumber", agentNumber);
                                        startActivity(i1);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "You account is not activated yet", Toast.LENGTH_LONG).show();
                                    }
                                }


                            }

                        }else {
                            login = "0";

                        }
                    }

                    if(login.equalsIgnoreCase("login")){
                        btnlogin.setText("LOGIN");
                    }else{
                        editpin.setVisibility(View.GONE);
                        editmsisdn.setVisibility(View.GONE);
                        txtpin.setVisibility(View.GONE);
                        txtmsisdn.setVisibility(View.GONE);
                    }


                }
            });
        }
    };

    Thread thread3 = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {

                    getStatusPin();
                    agentstatus=GSTATUS;
                    String code = editpin.getText().toString();
                    String sts = "Activate";

                    System.out.println("login is now "+login);
                    System.out.println("code is now "+code);
                    System.out.println("agentstatus is now "+agentstatus);
                    if(login.equalsIgnoreCase("login") && code.equalsIgnoreCase(GPIN) && agentstatus.equalsIgnoreCase(sts)) {

                        try {

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
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("db-offline-to-main", "1");
                        i.putExtra("globalMode", "Online");
                        i.putExtra("agentNumber", agentNumber);
                        startActivity(i);

                    } else {
                        System.out.println("code is now "+code);
                        System.out.println("GSTATUS is now "+GSTATUS);
                        System.out.println("GPIN is now "+GPIN);
                        if (! code.equalsIgnoreCase("")) {
                            if (code.equalsIgnoreCase(GPIN)) {
                                Toast.makeText(AgentLogin.this, "YOU ARE NOT APPROVED YET", Toast.LENGTH_SHORT).show();
                                startActivity(getIntent());
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid PINCODE", Toast.LENGTH_LONG).show();
                                startActivity(getIntent());
                            }
                        }
                    }
                    if(login.equalsIgnoreCase("0")){
                        Intent i;
                        if(connectflag==false) {
                             i = new Intent(getApplicationContext(), AgentRegistration.class);
                             i.putExtra("message_key", "-100");
                        } else {
                             i = new Intent(getApplicationContext(), AgentRegistration.class);
                             i.putExtra("message_key", "1");
                        }
                        startActivity(i);
                    }

                }
            });
        }
    };
}