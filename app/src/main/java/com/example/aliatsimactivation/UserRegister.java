package com.example.aliatsimactivation;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.telnet.EchoOptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class UserRegister extends AppCompatActivity {
    private Button verify,BtnData,BtnAgentImage,BtnFrontID,BtnBackID;
    private String myText,RegisterResult;
    private String Code;
    private EditText edtfname,edtlname,edtregion,edtaddress,edtphonenbr,edtpin;
    private TextView tv;
    private String file = "MSISDN.txt";
    private String secondfile = "Offlinedata.txt";
    private String fileContents,fileContents2;
    private String secondfileContents,secondfileContents2,secondfileContents3,secondfileContents4,secondfileContents5,secondfileContents6,secondfileContent7,secondfileContent8,secondfileContent9,secondfileContent10;
    private String AgentImage,AgentFrontID,AgentBackID;
    Connection conn;
    private boolean connectflag=false;
    private String gimagestatus,gfrontstatus,gbackstatus;

    SFTP sftp=new SFTP();



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            AgentImage=edtfname.getText().toString()+"_"+edtlname.getText().toString()+"_AGENT_"+edtphonenbr.getText().toString();

            Bitmap bmp=(Bitmap)data.getExtras().get("data");

           File agentimage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage+ ".jpg");


            FileOutputStream fileOutputStream =null;
            try {
                fileOutputStream = new FileOutputStream(agentimage);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
               // textF.setText("/sdcard/Pictures/"+FRONT);

            } catch (Exception e) {
                e.printStackTrace();
            }

            File imgagent = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage + ".jpg");
            if (imgagent.exists()) {
                BtnAgentImage.setBackgroundColor(Color.YELLOW);
                gimagestatus="0";
            }
        }
        if(requestCode==101)
        {
            AgentFrontID=edtfname.getText().toString()+"_"+edtlname.getText().toString()+"_FRONT_"+edtphonenbr.getText().toString();

            Bitmap bmp=(Bitmap)data.getExtras().get("data");

            File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID+ ".jpg");

            FileOutputStream fileOutputStream =null;
            try {
                fileOutputStream = new FileOutputStream(agentfrontid);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                // textF.setText("/sdcard/Pictures/"+FRONT);

            } catch (Exception e) {
                e.printStackTrace();
            }

            File front = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID + ".jpg");
            if (front.exists()) {
                BtnFrontID.setBackgroundColor(Color.YELLOW);
                gfrontstatus="0";
            }

        }
        if (requestCode==102)
        {
            AgentBackID=edtfname.getText().toString()+"_"+edtlname.getText().toString()+"_Back_"+edtphonenbr.getText().toString();

            Bitmap bmp=(Bitmap)data.getExtras().get("data");

            File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID+ ".jpg");

            FileOutputStream fileOutputStream =null;
            try {
                fileOutputStream = new FileOutputStream(agentfrontid);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                // textF.setText("/sdcard/Pictures/"+FRONT);

            } catch (Exception e) {
                e.printStackTrace();
            }

            File back = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID + ".jpg");
            if (back.exists()) {
                BtnBackID.setBackgroundColor(Color.YELLOW);
                gbackstatus="0";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        verify=findViewById(R.id.verify);
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
        BtnAgentImage=findViewById(R.id.btnagentimg);
        BtnFrontID=findViewById(R.id.btnagentfrontid);
        BtnBackID=findViewById(R.id.btnagentbackid);
        tv=findViewById(R.id.text_view);





        gfrontstatus="0";
        gbackstatus="0";
        gimagestatus="0";

        BtnAgentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(UserRegister.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);}
            }
        });

        BtnFrontID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(UserRegister.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,101);}
            }
        });

        BtnBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(UserRegister.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,102);}
            }
        });



        //code for verification
        Code=generateSessionKey(6);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UserRegister.this, new String[]{
                    Manifest.permission.CAMERA}, 100);
            ActivityCompat.requestPermissions(UserRegister.this, new String[]{
                    Manifest.permission.CAMERA}, 101);

        }

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

                System.out.println("in process please wait");

                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                //validation for network connection on the wifi or mobile data
                if (networkInfo != null && networkInfo.isConnected()) {
                    System.out.println("you are connected");
                    //here we have connection to the internet
                    //permit the user to verify if one edittext is empty
                    try {
                        if (TextUtils.isEmpty(edtfname.getText()) || TextUtils.isEmpty(edtlname.getText()) || TextUtils.isEmpty(edtregion.getText()) || TextUtils.isEmpty(edtaddress.getText()) || TextUtils.isEmpty(edtphonenbr.getText()) || TextUtils.isEmpty(edtpin.getText()) || TextUtils.isEmpty(AgentImage) || TextUtils.isEmpty(AgentFrontID) || TextUtils.isEmpty(AgentBackID)) {
                            edtfname.setError("Enter First Name");
                            edtlname.setError("Enter Last Name");
                            edtregion.setError("Enter Region");
                            edtaddress.setError("Enter Address");
                            edtphonenbr.setError("Enter Phone Number");
                            edtpin.setError("Enter PIN");
                            BtnAgentImage.setError("Take a Photo");
                            BtnFrontID.setError("Take a Photo");
                            BtnBackID.setError("Take a Photo");

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

                                    Toast.makeText(UserRegister.this,"In process please wait",Toast.LENGTH_SHORT).show();
                                    myText = input.getText().toString();

                                    if (Code.equalsIgnoreCase(myText)) {
                                        boolean flg=false;


                                            if((flg=connecttoDB())==true) {
                                                System.out.println("connect in dialog");
                                                try {

                                                    PreparedStatement stmtinsert1 = null;

                                                    try {

                                                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,ADDRESS,REGION,CREATION_DATE,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID,VERIFICATION_CODE,AGENT_IMAGE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS) values " +
                                                                "('" + edtphonenbr.getText().toString() + "','" + edtpin.getText().toString() + "','" + edtfname.getText().toString() + "','" + edtlname.getText().toString() + "','" + edtaddress.getText().toString() + "','" + edtregion.getText().toString() + "',sysdate,'" + AgentImage + "','" + AgentFrontID + "','" + AgentFrontID + "','" + Code + "',0,0,0)");

                                                    } catch (SQLException throwables) {
                                                        throwables.printStackTrace();
                                                    }
                                                    try {
                                                        stmtinsert1.executeUpdate();
                                                        createandSaveMSISDNandPIN();
                                                        Toast.makeText(getApplicationContext(), "Saving Completed", Toast.LENGTH_SHORT).show();
                                                        //thread to send images to sftp
                                                        if(gimagestatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
                                                            Toast.makeText(UserRegister.this, "Uploading Photos started", Toast.LENGTH_LONG).show();
                                                            thread1.start();
                                                        }
                                                        Toast.makeText(UserRegister.this, "Uploading Photos Completed", Toast.LENGTH_LONG).show();
                                                    } catch (SQLException throwables) {
                                                        throwables.printStackTrace();
                                                    }
                                                    try {
                                                        stmtinsert1.close();
                                                        conn.close();
                                                    } catch (SQLException throwables) {
                                                        throwables.printStackTrace();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }



                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra("db-offline-to-main","1");
                                                startActivity(intent);
                                            }else
                                            {
                                                System.out.println("offline in dialog");
                                                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                                                intent.putExtra("db-offline-to-main","-100");
                                                startActivity(intent);

                                            }





                                    } else {
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

                            System.out.println("end of else for dialog");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    System.out.println("finish if for network");
                }
                //in case where no wifi or mobile internet connection
                else{

                    if (TextUtils.isEmpty(edtfname.getText()) || TextUtils.isEmpty(edtlname.getText()) || TextUtils.isEmpty(edtregion.getText()) || TextUtils.isEmpty(edtaddress.getText()) || TextUtils.isEmpty(edtphonenbr.getText()) || TextUtils.isEmpty(edtpin.getText()) || TextUtils.isEmpty(AgentImage) || TextUtils.isEmpty(AgentFrontID) || TextUtils.isEmpty(AgentBackID)) {
                        edtfname.setError("Enter First Name");
                        edtlname.setError("Enter Last Name");
                        edtregion.setError("Enter Region");
                        edtaddress.setError("Enter Address");
                        edtphonenbr.setError("Enter Phone Number");
                        edtpin.setError("Enter PIN");
                        BtnAgentImage.setError("Take a Photo");
                        BtnFrontID.setError("Take a Photo");
                        BtnBackID.setError("Take a Photo");

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

                                Toast.makeText(UserRegister.this,"In process please wait",Toast.LENGTH_SHORT).show();
                                myText = input.getText().toString();

                                if (Code.equalsIgnoreCase(myText)) {

                                    createandSaveMSISDNandPIN();
                                    createandSaveOfflinedata();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("db-offline-to-main","-100");
                                    startActivity(intent);

                                } else {
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

                        System.out.println("end of else for dialog");
                    }

                }
            }


        });

    }
    //function to create and save the msisdn and pin
    private void createandSaveMSISDNandPIN(){

        fileContents = edtphonenbr.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            fos.write(fileContents.getBytes());
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


    //function to create and save the offlinedata
    private void createandSaveOfflinedata() {
        try {
        secondfileContents = edtfname.getText().toString();
        secondfileContents2= edtlname.getText().toString();
        secondfileContents3= edtregion.getText().toString();
        secondfileContents4= edtaddress.getText().toString();
        secondfileContents5= edtphonenbr.getText().toString();
        secondfileContents6= edtpin.getText().toString();
        secondfileContent7=AgentImage;
        secondfileContent8=AgentFrontID;
        secondfileContent9=AgentBackID;
        secondfileContent10=Code;

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
            fOut.write(secondfileContent7.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContent8.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContent9.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContent10.getBytes());
            File fileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), secondfile);
            System.out.println("Saved @ :"+fileDir);
            Toast.makeText(getBaseContext(), "File saved at" + fileDir, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                    Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                    connectflag = false;
                    createandSaveMSISDNandPIN();
                    createandSaveOfflinedata();

                } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                    System.out.println("error is: " + e.toString());
                    Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                    connectflag = false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return connectflag;
    }

    public static String generateSessionKey(int length){

            String alphabet =
                    new String("0123456789"); // 9

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

                String user=sftp.getUser().toString();
                String pass=sftp.getPass().toString();
                String host=sftp.getServer().toString();
                int e = sftp.getPort();

                Properties config=new Properties();
                config.put("StrictHostKeyChecking","no");

                JSch jSch = new JSch();
                Session session =jSch.getSession(user,host,e);
                System.out.println("Step1");
                session.setPassword(pass);
                session.setConfig(config);
                try {
                    session.connect();

                    System.out.println("Step Connect");
                    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();

                    //check if the global status if equals zero do it
                    if(gimagestatus.equalsIgnoreCase("0")) {

                        File agentimg = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage + ".jpg");
                        String imgagent = String.valueOf(agentimg);
                        channelSftp.put(imgagent, "SIMAGENTSFTP");
                        Boolean success1 = true;

                        if (success1) {
                            System.out.println("upload completed : " + imgagent);
                            UpdateAgentPicStatus(edtphonenbr.getText().toString(),"AGENT_IMAGE_STATUS");
                            BtnAgentImage.setBackgroundColor(Color.GREEN);
                        }
                    }

                    if(gfrontstatus.equalsIgnoreCase("0")) {

                        File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID + ".jpg");
                        String frontid = String.valueOf(agentfrontid);
                        channelSftp.put(frontid, "SIMAGENTSFTP");
                        Boolean success2 = true;

                        if (success2) {
                            System.out.println("upload completed : " + frontid);
                            UpdateAgentPicStatus(edtphonenbr.getText().toString(),"FRONT_SIDE_ID_STATUS");
                            BtnFrontID.setBackgroundColor(Color.GREEN);
                        }
                    }

                    if(gbackstatus.equalsIgnoreCase("0")) {

                        File agenbackid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID + ".jpg");
                        String backid = String.valueOf(agenbackid);
                        channelSftp.put(backid, "SIMAGENTSFTP");
                        Boolean success2 = true;

                        if (success2) {
                            System.out.println("upload completed : " + backid);
                            UpdateAgentPicStatus(edtphonenbr.getText().toString(),"BACK_SIDE_ID_STATUS");
                            BtnBackID.setBackgroundColor(Color.GREEN);
                        }
                    }


                    //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                    channelSftp.disconnect();
                    session.disconnect();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });


    public void UpdateAgentPicStatus(String vmsisdn,String vcolname)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

