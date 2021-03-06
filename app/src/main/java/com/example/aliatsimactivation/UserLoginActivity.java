package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

public class UserLoginActivity extends AppCompatActivity {
    private TextView tv;
    private String RegisterResult, RegisterResult1;
    private String file = "MSISDN.txt";
    private String s0, s1, s2, s3, s4, s5, s6, s7, s8, s9;
    private String fileContents, fileContents2;
    private Button BtnExit, BtnData;
    private String secondfile = "Offlinedata.txt";
    private String secondfileContents, secondfileContents2, secondfileContents3, secondfileContents4, secondfileContents5, secondfileContents6;
    private Connection conn;
    private TextView tv2;
    private String[] data;
    private boolean connectflag = false;
    private String gimagestatus, gfrontstatus, gbackstatus;

    SFTP sftp = new SFTP();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);


        Button btnregister = findViewById(R.id.signup);
        BtnExit = findViewById(R.id.BtnExit);
        BtnData = findViewById(R.id.BtnData);
        //initialize picture sftp status
        gimagestatus = "0";
        gfrontstatus = "0";
        gbackstatus = "0";


        //read value coming from user agent if 1 means conect = true if -100 means np DB connection
        Intent intent1 = UserLoginActivity.this.getIntent();
        String str1 = intent1.getStringExtra("db-offline-to-main");


        //move to the register form
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(intent);
            }
        });

        //check if the file is found in the given directory and call the load function
        try {
            File fileDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MSISDN.txt");
            File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
            String sessionId = getIntent().getStringExtra("key");
            System.out.println(sessionId);
            TextView tv3 = findViewById(R.id.text_view3);
            tv3.setText(sessionId);

            if (file.exists()) {
                if (tv3.getText().toString() == sessionId) {
                    //LoadData();
                    btnregister.setEnabled(false);
                } else {
                    if (str1 == null) {
                        str1 = "1";
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("db-offline-to-main", str1);
                        intent.putExtra("globalMode","Online");
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("db-offline-to-main", str1);
                        intent.putExtra("globalMode","Online");
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();
            }
        });

        //insert into database through a file
        BtnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                //validation for network connection on the wifi or mobile data
                if (networkInfo != null && networkInfo.isConnected()) {
                    System.out.println("you are connected");

                    Toast.makeText(UserLoginActivity.this, "Please Wait in Process..", Toast.LENGTH_SHORT).show();

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
                    System.out.println(RegisterResult1);
                    boolean flg = false;
                    try {
                        if ((flg = connecttoDB()) == true) {
                            //split the line in the text file according to :
                            data = RegisterResult1.split(":");
                            System.out.println(data);
                            s0 = data[0];
                            s1 = data[1];
                            s2 = data[2];
                            s3 = data[3];
                            s4 = data[4];
                            s5 = data[5];
                            s6 = data[6];
                            s7 = data[7];
                            s8 = data[8];
                            s9 = data[9];

                            Statement stmt1 = null;
                            try {
                                stmt1 = conn.createStatement();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            String sqlStmt = ("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,REGION,ADDRESS,CREATION_DATE,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID,VERIFICATION_CODE,AGENT_IMAGE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS) values " +
                                    "('" + s4.toString() + "','" + s5.toString() + "','" + s0.toString() + "','" + s1.toString() + "','" + s2.toString() + "','" + s3.toString() + "',sysdate,'" + s6.toString() + "','" + s7.toString() + "','" + s8.toString() + "','" + s9.toString() + "',0,0,0)");
                            System.out.println(sqlStmt);
                            ResultSet rs1 = null;
                            try {
                                rs1 = stmt1.executeQuery(sqlStmt);
                                //delete the file after sending data from file into database

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

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        File fileDir1 = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Offlinedata.txt");
                        File file1 = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
                        // file1.delete();
                        if (gimagestatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
                            Toast.makeText(UserLoginActivity.this, "Uploading Photos started", Toast.LENGTH_LONG).show();
                            thread1.start();
                            Toast.makeText(UserLoginActivity.this, "Uploading Photos Completed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("db-offline-to-main", "1");
                    startActivity(intent);
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


    }

    public void onBackPressed() {
        finishAffinity();
    }

    //function to have the ability to load a file from the storage and fill the values in edittexts
   /* public void LoadData()
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

    }*/

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
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
                Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
            } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                System.out.println("error is: " + e.toString());
                Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectflag;
    }

    public static String generateSessionKey(int length) {
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); // 9

        int n = alphabet.length(); // 10

        String result = new String();
        Random r = new Random(); // 11

        for (int i = 0; i < length; i++) // 12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {


                System.out.println("Start");

                String user = sftp.getUser().toString();
                String pass = sftp.getPass().toString();
                String host = sftp.getServer().toString();
                int e = sftp.getPort();

                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");

                JSch jSch = new JSch();
                Session session = jSch.getSession(user, host, e);
                System.out.println("Step1");
                session.setPassword(pass);
                session.setConfig(config);
                try {
                    session.connect();

                    System.out.println("Step Connect");
                    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();

                    //check if the global status if equals zero do it
                    if (gimagestatus.equalsIgnoreCase("0")) {

                        File agentimg = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), s6.toString() + ".jpg");
                        String imgagent = String.valueOf(agentimg);
                        channelSftp.put(imgagent, "SIMAGENTSFTP");
                        Boolean success1 = true;

                        if (success1) {
                            System.out.println("upload completed : " + imgagent);
                            UpdateAgentPicStatus(s4.toString(), "AGENT_IMAGE_STATUS");

                        }
                    }

                    if (gfrontstatus.equalsIgnoreCase("0")) {

                        File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), s7.toString() + ".jpg");
                        String frontid = String.valueOf(agentfrontid);
                        channelSftp.put(frontid, "SIMAGENTSFTP");
                        Boolean success2 = true;

                        if (success2) {
                            System.out.println("upload completed : " + frontid);
                            UpdateAgentPicStatus(s4.toString(), "FRONT_SIDE_ID_STATUS");

                        }
                    }

                    if (gbackstatus.equalsIgnoreCase("0")) {

                        File agenbackid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), s8.toString() + ".jpg");
                        String backid = String.valueOf(agenbackid);
                        channelSftp.put(backid, "SIMAGENTSFTP");
                        Boolean success2 = true;

                        if (success2) {
                            System.out.println("upload completed : " + backid);
                            UpdateAgentPicStatus(s4.toString(), "BACK_SIDE_ID_STATUS");
                        }
                    }


                    //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                    channelSftp.disconnect();
                    session.disconnect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


    public void UpdateAgentPicStatus(String vmsisdn, String vcolname) {
        boolean flg = false;
        try {
            if ((flg = connecttoDB()) == true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    stmtinsert1 = conn.prepareStatement("update SIM_REGISTER_LOGIN set " + vcolname + "=1  where MSISDN ='" + vmsisdn + "'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtinsert1.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                try {
                    stmtinsert1.close();
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

