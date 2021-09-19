package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private TextView txtmsisdn,txtpin;
    private EditText editmsisdn,editpin;
    private String agentNumber,RegisterResult,s0;
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
        BtnData=findViewById(R.id.BtnData);
        Intent i=this.getIntent();
        login=i.getStringExtra("login");


        if(login==null || login.equalsIgnoreCase("login")){
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
                System.out.println("R.R : "+ RegisterResult);
                //split the line through : and set them into the edittexts of msisdn and pin
                String[] data = RegisterResult.split(":");
                agentNumber= data[0];
                filepincode = data[1];
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
                        agentstatus = getStatus();
                        System.out.println("agentstatus" +agentstatus);
                        if (agentstatus.equalsIgnoreCase("Activate") && filepincode != "PIN") {
                                Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                            i1.putExtra("db-offline-to-main", "1");
                            i1.putExtra("globalMode", "Online");
                            i1.putExtra("agentNumber", agentNumber);
                            startActivity(i1);
                        } else {
                            Toast.makeText(getApplicationContext(), "You account is not activated yet", Toast.LENGTH_LONG).show();
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

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pincode = getPin();
                String agentstatus = getStatus();
                String code = editpin.getText().toString();
                String sts = "Activate";
                if(login.equalsIgnoreCase("login") && code.equalsIgnoreCase(pincode) && agentstatus.equalsIgnoreCase(sts)) {

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
                    if(code.equalsIgnoreCase(pincode)){
                        Toast.makeText(AgentLogin.this, "YOU ARE NOT APPROVED YET", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid PINCODE",Toast.LENGTH_LONG).show();
                    }
                }
                if(login.equalsIgnoreCase("0")){
                    Intent i = new Intent(getApplicationContext(), AgentRegistration.class);
                    startActivity(i);
                }
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
                startActivity(intent);
            }
        });

    }


    public String getPin()
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
                    PIN = (rs1.getString("PIN_CODE"));
                    pin = PIN;
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

        }
        return pin;
    }



    public String getStatus()
    {
        String STATUS = null;
        String status = null;
        boolean flg=false;
        if ((flg = connecttoDB()) == true) {
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
                    STATUS = (rs2.getString("STATUS"));
                    status = STATUS;
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
        return status;
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