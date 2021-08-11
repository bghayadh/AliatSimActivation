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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

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
    String server = "ftp.ipage.com";
    int port = 21;
    String user = "beid";
    String pass = "10th@Loop";
    FTPClient ftpClient = new FTPClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);


        Button btnregister = findViewById(R.id.signup);
        BtnExit=findViewById(R.id.BtnExit);
        BtnData=findViewById(R.id.BtnData);

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
            if (tv3.getText().toString() == sessionId) {
                LoadData();
                btnregister.setEnabled(false);
            } else  {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }


        BtnExit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                finishAffinity();
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
                String s6 = data[6];
                String s7 = data[7];
                String s8 = data[8];

                Statement stmt1 = null;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt = ("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,REGION,ADDRESS,CREATION_DATE,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID) values " +
                        "('" + s4.toString() + "','" + s5.toString() + "','" + s0.toString() + "','" + s1.toString() + "','" + s2.toString() + "','" + s3.toString() + "',sysdate,'"+s6.toString()+"','"+s7.toString()+"','"+s8.toString()+"')");
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

                File myFile = new File("/sdcard/Pictures", s6.toString() + ".jpg");
                String agentimagepath = String.valueOf(myFile);
                String agentimagename = s6.toString() + ".jpg";


                File myFile1 = new File("/sdcard/Pictures", s7.toString() + ".jpg");
                String agentfrontidpath = String.valueOf(myFile1);
                String agentfrontidname = s7.toString()+ ".jpg";

                File myFile2 = new File("/sdcard/Pictures", s8.toString() + ".jpg");
                String agentbackidpath = String.valueOf(myFile2);
                String agentbackidname =s8.toString()+ ".jpg";

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    ftpClient.connect(server, port);
                    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                        // login using username & password
                        boolean status = ftpClient.login(user, pass);
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        String workingDir = ftpClient.printWorkingDirectory();
                        System.out.println("OUR PWD IS " + workingDir);
                        ftpClient.changeWorkingDirectory(workingDir + "AGENT");

                        //return true if the directory found
                        System.out.println(ftpClient.changeWorkingDirectory(workingDir + "AGENT"));
                        workingDir = ftpClient.printWorkingDirectory();
                        //  PathSignFTP = workingDir + "/" + SIGN + ".jpg";
                        // PathFrontFTP = workingDir + "/" + FRONT + ".jpg";
                        //  PathBackFTP = workingDir + "/" + BACK + ".jpg";
                        System.out.println("Directory: " + workingDir);
                        // upload file
                        try {

                            FileInputStream srcFileStream = new FileInputStream(agentimagepath);
                            ftpClient.storeFile(agentimagename, srcFileStream);

                            FileInputStream srcFileStream1 = new FileInputStream(agentfrontidpath);
                            ftpClient.storeFile(agentfrontidname, srcFileStream1);

                            FileInputStream srcFileStream2 = new FileInputStream(agentbackidpath);
                            ftpClient.storeFile(agentbackidname, srcFileStream2);

                            srcFileStream.close();

                            Toast.makeText(getApplicationContext(), "upload Completed", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }


                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                try {
                    ftpClient.login(user, pass);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                ftpClient.enterLocalPassiveMode();

                try {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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

    public void onBackPressed() {
        finishAffinity();
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

    public static String generateSessionKey(int length){
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); // 9

        int n = alphabet.length(); // 10

        String result = new String();
        Random r = new Random(); // 11

        for (int i=0; i<length; i++) // 12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }
}