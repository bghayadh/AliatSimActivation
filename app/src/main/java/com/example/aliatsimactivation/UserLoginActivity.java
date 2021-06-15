package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserLoginActivity extends AppCompatActivity {
    private TextView tv;
    private String RegisterResult,RegisterResult1;
    private String file = "MSISDN.txt";
    private String s0,s1;
    private String fileContents,fileContents2;
    private Button BtnExit,BtnData;
    private String secondfile = "Offlinedata.txt";
    private String secondfileContents,secondfileContents2,secondfileContents3,secondfileContents4,secondfileContents5,secondfileContents6;
    private Connection conn;
    private TextView tv2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);

        Button btnlogin = findViewById(R.id.login);
        Button btnregister = findViewById(R.id.signup);
        BtnExit=findViewById(R.id.BtnExit);
        BtnData=findViewById(R.id.BtnData);
        //logging into main page
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get the values of the msisdn and pin edittexts
               EditText msisdn =(EditText) findViewById(R.id.edtphnbr);
                EditText pin =(EditText) findViewById(R.id.edtloginpin);
                String msisdn1=msisdn.getText().toString();
                String pin1=pin.getText().toString();

                //check if the values are equal to the actual values
                if(msisdn1.equals(s0) && pin1.equals(s1))
                {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid MSISDN or PIN",Toast.LENGTH_LONG).show();
                }

            }
        });
        //move to the register form
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(intent);
            }
        });

    //check if the file is found in the given directory and call the load function
        File fileDir = new File(getFilesDir(),"MSISDN.txt");
        File file = new File(getApplicationContext().getFilesDir(),"MSISDN.txt");
        String sessionId = getIntent().getStringExtra("key");
        System.out.println(sessionId);
        TextView tv3=findViewById(R.id.text_view3);
        tv3.setText(sessionId);

        if(file.exists())
        {
            Toast.makeText(getApplicationContext(), "Found", Toast.LENGTH_LONG).show();
            if (tv3.getText().toString() == sessionId) {
                LoadData();
            } else  {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }


        BtnExit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                System.exit(0);
            }
        });

        //insert into database through a file
        BtnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder text = new StringBuilder();

                try {
                    FileInputStream fIn = openFileInput("Offlinedata.txt");
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
                tv2 = (TextView) findViewById(R.id.text_view2);
                tv2.setText(text.toString());
                tv2.setVisibility(View.INVISIBLE);
                RegisterResult1 = String.valueOf(tv2.getText());
                System.out.println("RESULT" +RegisterResult1);

                connecttoDB();
                //split the line in the text file according to :
                String[] data = RegisterResult1.split(":");
                String s0 = data[0];
                String s1 = data[1];
                String s2 = data[2];
                String s3 = data[3];
                String s4 = data[4];
                String s5 = data[5];

                Statement stmt1 = null;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt = ("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,REGION,ADDRESS,CREATION_DATE) values " +
                        "('" + s4.toString() + "','" + s5.toString() + "','" + s0.toString() + "','" + s1.toString() + "','" + s2.toString() + "','" + s3.toString() + "',sysdate)");
                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery(sqlStmt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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

                //delete the file after sending data from file into database
                File fileDir1 = new File(getFilesDir(),"Offlinedata.txt");
                File file1 = new File(getApplicationContext().getFilesDir(),"Offlinedata.txt");
                file1.delete();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //check the existence of the file and disable the edittexts
        File fileDir1 = new File(getFilesDir(),"Offlinedata.txt");
        File file1 = new File(getApplicationContext().getFilesDir(),"Offlinedata.txt");
        if(file1.exists())
        {
            BtnData.setVisibility(View.VISIBLE); //SHOW the button

        }


    }

    //function to have the ability to load a file from the storage and fill the values in edittexts
    public void LoadData()
    {
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
        tv = (TextView) findViewById(R.id.text_view1);
        tv.setText(text.toString());
        tv.setVisibility(View.INVISIBLE);
        RegisterResult = String.valueOf(tv.getText());

        //split the line through : and set them into the edittexts of msisdn and pin
        String[] data = RegisterResult.split(":");
        s0 = data[0];
        s1 = data[1];


        EditText msisdn =(EditText) findViewById(R.id.edtphnbr);
        EditText pin =(EditText) findViewById(R.id.edtloginpin);

        msisdn.setText(s0);
        pin.setText(s1);

    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

    public void  connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ( ).permitAll ( ).build ( );
            StrictMode.setThreadPolicy (policy);
            Class.forName ("oracle.jdbc.driver.OracleDriver").newInstance ( );
            conn = DriverManager.getConnection (url, userName, password);

        }
        catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println ("error1 is: " + e.toString ( ));
            Toast.makeText (getApplicationContext(), "" + e.toString ( ), Toast.LENGTH_SHORT).show ( );
            //if there is no connection to db save offline into the created text files
        }   catch (IllegalAccessException e) {
            System.out.println("error2 is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }   catch (InstantiationException e) {
            System.out.println("error3 is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
}