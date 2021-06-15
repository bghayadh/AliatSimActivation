package com.example.aliatsimactivation;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRegister extends AppCompatActivity {
    private static final String FILE_NAME="example.txt";
    private Button verify,register,BtnData;
    private String myText,RegisterResult;
    private String Code="112233";
    private EditText edtfname,edtlname,edtregion,edtaddress,edtphonenbr,edtpin;
    private TextView tv;
    private String file = "MSISDN.txt";
    private String secondfile = "Offlinedata.txt";
    private String fileContents,fileContents2;
    private String secondfileContents,secondfileContents2,secondfileContents3,secondfileContents4,secondfileContents5,secondfileContents6;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        verify=findViewById(R.id.verify);
        register=findViewById(R.id.register);
        BtnData=findViewById(R.id.BtnData);
        edtfname=findViewById(R.id.edtfname);
        String Fname=edtfname.getText().toString();
        edtlname=findViewById(R.id.edtlname);
        String Lname=edtlname.getText().toString();
        edtregion=findViewById(R.id.edtregion);
        String Region=edtregion.getText().toString();
        edtaddress=findViewById(R.id.edtaddress);
        String Address=edtaddress.getText().toString();
        edtphonenbr=findViewById(R.id.edtphone);
        String MSISDN=edtphonenbr.getText().toString();
        edtpin=findViewById(R.id.edtpin);
        String PIN=edtpin.getText().toString();
        tv=findViewById(R.id.text_view);








        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //verification button
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permit the user to verify if one edittext is empty
                if (TextUtils.isEmpty(edtfname.getText()) || TextUtils.isEmpty(edtlname.getText()) || TextUtils.isEmpty(edtregion.getText()) || TextUtils.isEmpty(edtaddress.getText()) || TextUtils.isEmpty(edtphonenbr.getText()) || TextUtils.isEmpty(edtpin.getText())) {
                    edtfname.setError("Enter First Name");
                    edtlname.setError("Enter Last Name");
                    edtregion.setError("Enter Region");
                    edtaddress.setError("Enter Address");
                    edtphonenbr.setError("Enter Phone Number");
                    edtpin.setError("Enter PIN");

                } else {

                    //open dialog
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(UserRegister.this);
                    mydialog.setTitle("Enter The Code");

                    final EditText input = new EditText(UserRegister.this);
                    input.setInputType(InputType.TYPE_CLASS_PHONE);
                    mydialog.setView(input);
                    //verify the given code and change from verify to register
                    mydialog.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myText = input.getText().toString();
                            Toast.makeText(UserRegister.this, "Text is: " + myText, Toast.LENGTH_LONG).show();

                            if(Code.equalsIgnoreCase(myText))
                            {

                                System.out.println("Succeed");
                                verify.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);

                            }
                            else {
                                AlertDialog.Builder mydialog1 = new AlertDialog.Builder(UserRegister.this);
                                mydialog1.setTitle("Oopss");
                                mydialog1.setMessage("Invalid Code Entered Please Try Again Later !! ");
                                mydialog1.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                mydialog1.show();

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

                    //sending notification with a verification code
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(UserRegister.this, "My Notification");
                    builder.setContentTitle("Enter This Code to Verify your Registration");
                    builder.setContentText(Code);
                    builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UserRegister.this);
                    managerCompat.notify(1, builder.build());


                }
            }


        });

        //register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert into database
                connecttoDB();

                try {


                    PreparedStatement stmtinsert1 = null;

                    try {

                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,REGION,ADDRESS,CREATION_DATE) values " +
                                "('" + edtphonenbr.getText().toString() + "','" + edtpin.getText().toString() + "','" + edtfname.getText().toString() + "','" + edtlname.getText().toString() + "','" + edtregion.getText().toString() + "','" + edtaddress.getText().toString() + "',sysdate)");

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        stmtinsert1.executeUpdate();
                        Toast.makeText(getApplicationContext(), "Saving Completed", Toast.LENGTH_SHORT).show();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        stmtinsert1.close();
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    createandSaveMSISDNandPIN();
                }catch (Exception e)
                {
                    System.out.println("saving offline");
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
                tv = (TextView) findViewById(R.id.text_view);
                tv.setText(text.toString());
                tv.setVisibility(View.INVISIBLE);
                RegisterResult = String.valueOf(tv.getText());
                System.out.println("RESULT" +RegisterResult);

                connecttoDB();
                //split the line in the text file according to :
                String[] data = RegisterResult.split(":");
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
                File fileDir = new File(getFilesDir(),"Offlinedata.txt");
                File file = new File(getApplicationContext().getFilesDir(),"Offlinedata.txt");
                file.delete();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //check the existence of the file and disable the edittexts
        File fileDir = new File(getFilesDir(),"Offlinedata.txt");
        File file = new File(getApplicationContext().getFilesDir(),"Offlinedata.txt");
        if(file.exists())
        {
            BtnData.setVisibility(View.VISIBLE); //SHOW the button
            edtpin.setFocusable(false);
            edtphonenbr.setFocusable(false);
            edtaddress.setFocusable(false);
            edtregion.setFocusable(false);
            edtlname.setFocusable(false);
            edtfname.setFocusable(false);

        }


    }
    //function to create and save the msisdn and pin
    private void createandSaveMSISDNandPIN(){

        fileContents = edtphonenbr.getText().toString();
        fileContents2= edtpin.getText().toString();
        try {
            FileOutputStream fOut = openFileOutput(file, MODE_PRIVATE);
            fOut.write(fileContents.getBytes());
            fOut.write(":".getBytes());
            fOut.write(fileContents2.getBytes());
            fOut.close();
            File fileDir = new File(getFilesDir(), file);
            Toast.makeText(getBaseContext(), "File saved at" + fileDir, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //function to create and save the offlinedata
    private void createandSaveOfflinedata() {

        secondfileContents = edtfname.getText().toString();
        secondfileContents2= edtlname.getText().toString();
        secondfileContents3= edtregion.getText().toString();
        secondfileContents4= edtaddress.getText().toString();
        secondfileContents5= edtphonenbr.getText().toString();
        secondfileContents6= edtpin.getText().toString();
        try {
            FileOutputStream fOut = openFileOutput(secondfile, MODE_PRIVATE);
            fOut.write(secondfileContents.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContents2.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContents3.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContents4.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContents5.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContents6.getBytes());
            fOut.write(":".getBytes());
            fOut.close();
            File fileDir = new File(getFilesDir(), secondfile);
            Toast.makeText(getBaseContext(), "File saved at" + fileDir, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            Toast.makeText (UserRegister.this, "" + e.toString ( ), Toast.LENGTH_SHORT).show ( );
            //if there is no connection to db save offline into the created text files
            createandSaveMSISDNandPIN();
            createandSaveOfflinedata();
        }   catch (IllegalAccessException e) {
            System.out.println("error2 is: " +e.toString());
            Toast.makeText (UserRegister.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }   catch (InstantiationException e) {
            System.out.println("error3 is: " +e.toString());
            Toast.makeText (UserRegister.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
}