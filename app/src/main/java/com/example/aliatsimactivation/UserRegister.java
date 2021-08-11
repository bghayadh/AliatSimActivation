package com.example.aliatsimactivation;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

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
    private static final String FILE_NAME="example.txt";
    private Button verify,register,BtnData,BtnAgentImage,BtnFrontID,BtnBackID;
    private String myText,RegisterResult;
    private String Code;
    private EditText edtfname,edtlname,edtregion,edtaddress,edtphonenbr,edtpin;
    private TextView tv;
    private String file = "MSISDN.txt";
    private String secondfile = "Offlinedata.txt";
    private String fileContents,fileContents2;
    private String secondfileContents,secondfileContents2,secondfileContents3,secondfileContents4,secondfileContents5,secondfileContents6,secondfileContent7,secondfileContent8,secondfileContent9;
    private String AgentImage,AgentFrontID,AgentBackID;
    Connection conn;
    FTP ftp = new FTP();
    SFTP sftp=new SFTP();

    String server = ftp.getServer();//"ftp.ipage.com";
    int port = ftp.getPort();//21;
    String user = ftp.getUser();//"beid";
    String pass = ftp.getPass();//"10th@Loop";
    FTPClient ftpClient = new FTPClient();


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




        Code=generateSessionKey(6);
        System.out.println("result: "+Code);



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
                            myText = input.getText().toString();
                            Toast.makeText(UserRegister.this, "Text is: " + myText, Toast.LENGTH_LONG).show();

                            if(Code.equalsIgnoreCase(myText))
                            {

                                System.out.println("Succeed");
                                verify.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);
                                connecttoDB();

                                try {


                                    PreparedStatement stmtinsert1 = null;

                                    try {

                                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTER_LOGIN (MSISDN,PIN_CODE,FIRST_NAME,LAST_NAME,ADDRESS,REGION,CREATION_DATE,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID,VERIFICATION_CODE) values " +
                                                "('"+edtphonenbr.getText().toString()+"','"+edtpin.getText().toString()+"','"+edtfname.getText().toString()+"','"+edtlname.getText().toString()+"','"+edtaddress.getText().toString()+"','"+edtregion.getText().toString()+"',sysdate,'"+AgentImage+"','"+AgentFrontID+"','"+AgentFrontID+"','"+Code+"')");

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


                                thread1.start();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
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
          //  Toast.makeText(getBaseContext(), "File saved at" + fileDir, Toast.LENGTH_LONG).show();
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
        secondfileContent7=AgentImage;
        secondfileContent8=AgentFrontID;
        secondfileContent9=AgentBackID;
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
            fOut.write(secondfileContent7.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContent8.getBytes());
            fOut.write(":".getBytes());
            fOut.write(secondfileContent9.getBytes());
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

    public static String generateSessionKey(int length){
        String alphabet =
                new String("0123456789"); // 9

        int n = alphabet.length(); // 10

        String result = new String();
        Random r = new Random(); // 11

        for (int i=0; i<length; i++) // 12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {

                File myFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage + ".jpg");
                String agentimagepath = String.valueOf(myFile);
                String agentimagename = AgentImage + ".jpg";

                File myFile1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID + ".jpg");
                String agentfrontidpath = String.valueOf(myFile1);
                String agentfrontidname = AgentFrontID+ ".jpg";

                File myFile2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID + ".jpg");
                String agentbackidpath = String.valueOf(myFile2);
                String agentbackidname =AgentBackID+ ".jpg";
                //Toast.makeText(SimTest.this,"Trying to connect..",Toast.LENGTH_LONG).show();

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
                session.connect();
                System.out.println("Step Connect");
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();
                //  UPLOAD
                File agentimage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage + ".jpg");
                String file = String.valueOf(agentimage);

                File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID + ".jpg");
                String file1 = String.valueOf(agentfrontid);

                File agentbackid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID + ".jpg");
                String file2 = String.valueOf(agentbackid);

                if (myFile.exists()) {
                    // Toast.makeText(SimTest.this,"Sending ...",Toast.LENGTH_LONG).show();
                }
                channelSftp.put(file, "SIMAGENTSFTP");
                channelSftp.put(file1, "SIMAGENTSFTP");
                channelSftp.put(file2, "SIMAGENTSFTP");
                //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                channelSftp.disconnect();
                session.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });

}

