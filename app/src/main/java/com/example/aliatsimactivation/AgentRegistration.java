package com.example.aliatsimactivation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;

import static java.lang.Thread.sleep;

public class AgentRegistration extends AppCompatActivity {
    private EditText edtfname,edtlname,edtdname,edtaddress,edtemail,edtphonenbr,edtlong,edtlat,edtstatus;
    private Spinner spregion;
    private TextView textstatus;
    private Button verify,BtnAgentImage,BtnFrontID,BtnBackID;
    private Connection conn;
    private int count=0;
    private File OfflineAgent;
    private String fileContents,fileContents2,regionName,fullname;
    private String AgentImage,AgentFrontID,AgentBackID,Code,value,globalMode,DBMode,PIN;
    private boolean connectflag=false;
    private String secondfileContents,secondfileContents2,secondfileContents3,secondfileContents4,secondfileContents5,secondfileContents6,secondfileContent7,secondfileContent8,secondfileContent9,secondfileContent10;
    private String gimagestatus,gfrontstatus,gbackstatus,globalagentID="0";
    private String regionid="0",login,emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";;
    private String file = "MSISDN.txt";
    private String secondfile = "Offlinedata.txt";
    private boolean regionflag=false;
    SFTP sftp=new SFTP();
    private LinearLayout txtlinear;
    private GpsTracker gpsTracker;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //sftp AgentImage to server
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
        //sftp AgentFrontID to server
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
        //sftp AgentBackID to server
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
    public void onBackPressed() {
        File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
        if(file.exists()) {
            login = "login";
        }else {
            login = "0";
        }
        Intent i=new Intent(getApplicationContext(),AgentLogin.class);
        i.putExtra("login",login);
        i.putExtra("globalMode",globalMode);
        i.putExtra("db-offline-to-main",DBMode);
        i.putExtra("agentNumber",edtphonenbr.getText().toString());
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_registration);


        verify=findViewById(R.id.verify);


        edtfname=findViewById(R.id.edtfname);
        edtlname=findViewById(R.id.edtlname);
        edtdname=findViewById(R.id.edtdspname);
        edtaddress=findViewById(R.id.edtaddress);
        edtemail=findViewById(R.id.edtemail);
        edtphonenbr=findViewById(R.id.edtphone);
        edtlong=findViewById(R.id.edtlongitude);
        edtlat=findViewById(R.id.edtlattitude);
        edtstatus=findViewById(R.id.edtstatus);
        spregion=findViewById(R.id.regionSpinner);
        BtnAgentImage=findViewById(R.id.btnagentimg);
        BtnFrontID=findViewById(R.id.btnagentfrontid);
        BtnBackID=findViewById(R.id.btnagentbackid);
        textstatus=findViewById(R.id.textstatus);
        txtlinear=findViewById(R.id.lineartxtmsg);

        ActivityCompat.requestPermissions(AgentRegistration.this, new String[]{
                Manifest.permission.CAMERA}, 100);
        ActivityCompat.requestPermissions(AgentRegistration.this, new String[]{
                Manifest.permission.CAMERA}, 101);
        ActivityCompat.requestPermissions(AgentRegistration.this, new String[]{
                Manifest.permission.CAMERA}, 102);

        // call class Gps to get our location
        gpsTracker = new GpsTracker (getApplicationContext ( ));
        if (gpsTracker.canGetLocation ( )) {
            double latitude = gpsTracker.getLatitude ( );
            double longitude = gpsTracker.getLongitude ( );
            edtlat.setText(String.valueOf(latitude));
            edtlong.setText(String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert ( );
            edtlat.setText("0");
            edtlong.setText("0");
        }


        OfflineAgent=new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");

        //case of resend data of agent when he save his data locally:
        Intent intent=getIntent();
        globalMode=intent.getStringExtra("globalMode");
        DBMode=intent.getStringExtra("db-offline-to-main");

        if(globalMode.equalsIgnoreCase("Offline") || DBMode.equalsIgnoreCase("-100")){
            verify.setText("SAVE");
            ArrayList<String> my_array = new ArrayList<String>();
            my_array.add("None");
            ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, my_array);
            spregion.setAdapter(my_Adapter);
        }


        if(DBMode.equalsIgnoreCase("-100")){

        }else {
            thread1.start();
        }
        System.out.println(globalMode+" "+DBMode);

        File file1 = new File(getApplicationContext().getFilesDir(), "Offlinedata.txt");
        if(file1.exists()){
            edtfname.setText(intent.getStringExtra("fname"));

            edtlname.setText(intent.getStringExtra("lname"));


            edtdname.setText(intent.getStringExtra("dname"));

            edtaddress.setText(intent.getStringExtra("address"));

            edtemail.setText(intent.getStringExtra("email"));

            edtphonenbr.setText(intent.getStringExtra("msisdn"));
            regionName=intent.getStringExtra("regionname");

            edtlong.setText(intent.getStringExtra("long"));

            edtlat.setText(intent.getStringExtra("lat"));

            AgentImage=intent.getStringExtra("img");

            AgentFrontID=intent.getStringExtra("front");

            AgentBackID=intent.getStringExtra("back");

            Code=intent.getStringExtra("code");

            if(AgentImage != ""){
                BtnAgentImage.setBackgroundColor(Color.YELLOW);
            }

            if(AgentFrontID != ""){
                BtnFrontID.setBackgroundColor(Color.YELLOW);
            }

            if(AgentBackID != ""){
                BtnBackID.setBackgroundColor(Color.YELLOW);
            }

        }else {
            regionName="None";
        }


        // initialize status of sftp to 0
        gfrontstatus="0";
        gbackstatus="0";
        gimagestatus="0";



        BtnAgentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate that all pictures should be taken no missing foto to save
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(AgentRegistration.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);}
            }
        });

        BtnFrontID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate hat all pictures shoudl be taken no missing foto to save
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(AgentRegistration.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,101);}
            }
        });

        BtnBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate hat all pictures shoudl be taken no missing foto to save
                if(edtfname.getText().toString().matches("")||edtlname.getText().toString().matches("") || edtphonenbr.getText().toString().matches("")) {
                    Toast.makeText(AgentRegistration.this, "INSERT YOUR NAME and MSISDN", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,102);}
            }
        });

        //Generate Pin
        PIN=generateSessionKey(4);

        // Send code for verification
        Code=generateSessionKey(6);
        int number = Integer.parseInt(Code);
        int a = number % 10;
        int b = (number % 100) / 10;
        int c = (number % 1000) / 100;
        int d = (number % 10000) / 1000;
        int e = (number % 100000) / 10000;
        int f = number / 100000;


        edtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // if(isValidEmail(edtemail.getText().toString())){

              //  }else {
               //     edtemail.setError("Invalid Email ");
              //  }
            }
        });

        spregion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                regionName = parent.getItemAtPosition(position).toString(); //this is your selected item
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




        verify.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // to validate if network connection are back
                if(globalMode.equalsIgnoreCase("Offline")) {
                    ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext ( )
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected() ) {
                        globalMode="Online" ;
                        DBMode="0";
                    }
                }

                if(globalMode.equalsIgnoreCase("Online")){


                    try {

                        //validat that all fiedls not to be empty

                        if (TextUtils.isEmpty(edtfname.getText())|| TextUtils.isEmpty(edtlname.getText()) || TextUtils.isEmpty(edtdname.getText()) || TextUtils.isEmpty(edtaddress.getText()) || TextUtils.isEmpty(edtemail.getText()) ||TextUtils.isEmpty(edtphonenbr.getText()) || TextUtils.isEmpty(AgentImage) || TextUtils.isEmpty(AgentFrontID) || TextUtils.isEmpty(AgentBackID) || TextUtils.isEmpty(edtlat.getText()) || TextUtils.isEmpty(edtlong.getText()) || !edtemail.getText().toString().matches(emailpattern)) {

                            if (!edtemail.getText().toString().matches(emailpattern)) {
                                edtemail.setError("Enter a Valid Email");
                            }

                            if (TextUtils.isEmpty(edtfname.getText())) {
                                edtfname.setError("Enter First Name");
                            }
                            if (TextUtils.isEmpty(edtlname.getText())) {
                                edtlname.setError("Enter Last Name");
                            }
                            if (TextUtils.isEmpty(edtdname.getText())) {
                                edtdname.setError("Enter Display Name");
                            }
                            if (TextUtils.isEmpty(edtaddress.getText())) {
                                edtaddress.setError("Enter Address");
                            }
                            if (TextUtils.isEmpty(edtemail.getText())) {
                                edtemail.setError("Enter a Valid Email");
                            }
                            if (TextUtils.isEmpty(edtphonenbr.getText())) {
                                edtphonenbr.setError("Enter Phone Number");
                            }
                            if (AgentImage==null) {
                                BtnAgentImage.setError("Take a Photo");
                            }
                            if (AgentFrontID==null) {
                                BtnFrontID.setError("Take a Photo");
                            }
                            if (AgentBackID==null) {
                                BtnBackID.setError("Take a Photo");
                            }
                            if (TextUtils.isEmpty(edtlat.getText())) {
                                edtlat.setError("Enter Latitude");
                            }
                            if (TextUtils.isEmpty(edtlong.getText())) {
                                edtlong.setError("Enter Longitude");
                            }


                        } else{

                            Date date = new Date();
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            String agentID = "AG_" + year + "_";


                            // to validate if reachability are back
                            if (DBMode.equalsIgnoreCase("-100") ) {
                                Toast.makeText(getApplicationContext(),"Please wait while saving and returning to agent login form",Toast.LENGTH_LONG).show();
                                if ((connecttoDB()) == true) {
                                    globalMode="Online" ;
                                    DBMode="0";
                                    try {
                                        conn.close();
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                            }



                            if (DBMode.equalsIgnoreCase("0")) {
                                //sending notification with a verification code
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(AgentRegistration.this, "My Notification");
                                builder.setContentTitle("Enter This Code to Verify your Registration");
                                builder.setContentText(Code);
                                builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AgentRegistration.this);
                                managerCompat.notify(1, builder.build());

                                sleep(2000);

                                AlertDialog.Builder mydialog = new AlertDialog.Builder(AgentRegistration.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.alert_dialog_verification, null);
                                mydialog.setTitle("Enter The Code");

                                sleep(1000);
                                EditText edt1 = dialogLayout.findViewById(R.id.edt1);
                                EditText edt2 = dialogLayout.findViewById(R.id.edt2);
                                EditText edt3 = dialogLayout.findViewById(R.id.edt3);
                                EditText edt4 = dialogLayout.findViewById(R.id.edt4);
                                EditText edt5 = dialogLayout.findViewById(R.id.edt5);
                                EditText edt6 = dialogLayout.findViewById(R.id.edt6);
                                String A = String.valueOf(f);
                                String B = String.valueOf(e);
                                String C = String.valueOf(d);
                                String D = String.valueOf(c);
                                String E = String.valueOf(b);
                                String F = String.valueOf(a);
                                edt1.setText(A);
                                edt2.setText(B);
                                edt3.setText(C);
                                edt4.setText(D);
                                edt5.setText(E);
                                edt6.setText(F);

                                mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fullname = edtfname.getText().toString() + " " + edtlname.getText().toString();
                                        /////Saving Data into table Agent and send photos to SFTP and save local file
                                        TextView txt1 = dialogLayout.findViewById(R.id.txt1);
                                        txt1.setVisibility(View.VISIBLE);
                                        Toast.makeText(AgentRegistration.this, "In process please wait", Toast.LENGTH_LONG).show();
                                        if (regionName.equalsIgnoreCase("None")) {
                                            regionid ="0";
                                        } else {
                                            regionid = getRegionID(spregion.getSelectedItem().toString());
                                            regionName=spregion.getSelectedItem().toString();
                                        }
                                        System.out.println(regionid);
                                        System.out.println(spregion.getSelectedItem().toString());
                                        try {

                                            PreparedStatement stmtinsert1 = null;
                                            if (globalagentID.equalsIgnoreCase("0") || OfflineAgent.exists()) {

                                                connecttoDB();
                                                Statement stmt1 = null;

                                                stmt1 = conn.createStatement();
                                                String sqlStmt = "select AGENT_SEQ.nextval as nbr from dual";
                                                ResultSet rs1 = null;
                                                try {
                                                    rs1 = stmt1.executeQuery(sqlStmt);
                                                } catch (SQLException throwables) {
                                                    throwables.printStackTrace();
                                                }

                                                while (true) {
                                                    try {
                                                        if (!rs1.next()) break;
                                                        globalagentID = agentID + rs1.getString("nbr");
                                                        System.out.println("AGENT ID"+globalagentID);

                                                    } catch (SQLException throwables) {
                                                        throwables.printStackTrace();
                                                    }
                                                }




                                                //This part needs a lot of changes
                                                //save agentlogin in Database
                                                stmtinsert1 = conn.prepareStatement("insert into AGENT (AGENT_ID,FIRST_NAME,LAST_NAME,DISPLAY_NAME,ADDRESS,EMAIL,MSISDN,CREATE_DATE,LAST_MODIFIED_DATE,STATUS,PIN_CODE,REGION_NAME,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID,VERIFICATION_CODE,AGENT_IMAGE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS,REGION_ID,LONGITUDE,LATITUDE,FULL_NAME) values " +
                                                        "('"+globalagentID+"','"+edtfname.getText().toString()+"','"+edtlname.getText().toString()+"','"+edtdname.getText().toString()+"','"+edtaddress.getText().toString()+"','"+edtemail.getText().toString()+"','"+edtphonenbr.getText().toString()+"',sysdate,sysdate,'"+edtstatus.getText().toString()+"','"+PIN+"','"+ regionName +"','"+AgentImage+"','"+AgentFrontID+"','"+AgentBackID+"','"+Code+"',0,0,0,'"+regionid+"','"+edtlong.getText().toString()+"','"+edtlat.getText().toString()+"' ,'" +fullname+"')");
                                            }else{
                                                stmtinsert1 = conn.prepareStatement("update AGENT set LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + edtfname.getText() + "',DISPLAY_NAME='" + edtdname.getText() + "',LAST_NAME='" + edtlname.getText() + "',FULL_NAME='" + fullname + "' ,MSISDN='" + edtphonenbr + "',ADDRESS='" + edtaddress.getText() + "',EMAIL='" + edtemail + "',REGION='" + regionName +"', AGENT_IMAGE='"+AgentImage+"', AGENT_FRONT_ID='"+AgentFrontID+"', AGENT_BACK_ID='"+AgentBackID+"',REGION_ID='"+regionid+"' where AGENT_ID  ='" + globalagentID + "'");

                                            }
                                            try {
                                                stmtinsert1.executeUpdate();
                                                createandSaveMSISDNandPIN();
                                                OfflineAgent.delete();
                                                Toast.makeText(getApplicationContext(), "Saving Completed", Toast.LENGTH_SHORT).show();
                                                //thread to send images to sftp
                                                if (gimagestatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
                                                    Toast.makeText(AgentRegistration.this, "Uploading Photos started", Toast.LENGTH_LONG).show();
                                                    threadimage.start();
                                                }
                                                Toast.makeText(AgentRegistration.this, "Uploading Photos Completed", Toast.LENGTH_LONG).show();
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


                                        //if we have cnnection the value sent is 1
                                        Intent intent = new Intent(getApplicationContext(), AgentLogin.class);
                                        intent.putExtra("login","login");
                                        intent.putExtra("globalMode",globalMode);
                                        intent.putExtra("db-offline-to-main",DBMode);
                                        intent.putExtra("agentNumber",edtphonenbr.getText().toString());
                                        startActivity(intent);

                                    }
                                });mydialog.setView(dialogLayout);
                                mydialog.show();

                            }else {
                                createandSaveOfflinedata();
                                createandSaveMSISDNandPIN();
                                Intent intent = new Intent(getApplicationContext(), AgentLogin.class);
                                intent.putExtra("login","login");
                                intent.putExtra("globalMode",globalMode);
                                intent.putExtra("db-offline-to-main",DBMode);
                                intent.putExtra("agentNumber",edtphonenbr.getText().toString());
                                startActivity(intent);

                            }

                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ///Offline Mode...
                }else {

                    if (TextUtils.isEmpty(edtfname.getText())|| TextUtils.isEmpty(edtlname.getText()) || TextUtils.isEmpty(edtdname.getText()) || TextUtils.isEmpty(edtaddress.getText()) || TextUtils.isEmpty(edtemail.getText()) ||TextUtils.isEmpty(edtphonenbr.getText()) || TextUtils.isEmpty(AgentImage) || TextUtils.isEmpty(AgentFrontID) || TextUtils.isEmpty(AgentBackID) || TextUtils.isEmpty(edtlat.getText()) || TextUtils.isEmpty(edtlong.getText()) || !edtemail.getText().toString().matches(emailpattern)) {

                        if (!edtemail.getText().toString().matches(emailpattern)) {
                            edtemail.setError("Enter a Valid Email");
                        }

                      if (TextUtils.isEmpty(edtfname.getText())) {
                          edtfname.setError("Enter First Name");
                      }
                      if (TextUtils.isEmpty(edtlname.getText())) {
                          edtlname.setError("Enter Last Name");
                      }
                      if (TextUtils.isEmpty(edtdname.getText())) {
                          edtdname.setError("Enter Display Name");
                      }
                      if (TextUtils.isEmpty(edtaddress.getText())) {
                          edtaddress.setError("Enter Address");
                      }
                      if (TextUtils.isEmpty(edtemail.getText())) {
                          edtemail.setError("Enter a Valid Email");
                      }
                      if (TextUtils.isEmpty(edtphonenbr.getText())) {
                          edtphonenbr.setError("Enter Phone Number");
                      }
                      if (AgentImage==null) {
                          BtnAgentImage.setError("Take a Photo");
                      }
                      if (AgentFrontID==null) {
                          BtnFrontID.setError("Take a Photo");
                      }
                      if (AgentBackID==null) {
                          BtnBackID.setError("Take a Photo");
                      }
                      if (TextUtils.isEmpty(edtlat.getText())) {
                          edtlat.setError("Enter Latitude");
                      }
                      if (TextUtils.isEmpty(edtlong.getText())) {
                          edtlong.setError("Enter Longitude");
                      }
                  } else {
                      createandSaveOfflinedata();
                      createandSaveMSISDNandPIN();
                      Intent intent = new Intent(getApplicationContext(), AgentLogin.class);
                      intent.putExtra("login", "login");
                      intent.putExtra("globalMode", globalMode);
                      intent.putExtra("db-offline-to-main", DBMode);
                      intent.putExtra("agentNumber", edtphonenbr.getText().toString());
                      startActivity(intent);
                  }
                }
            }

        });



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

    ///----- this function must change..

    //function to create and save the offlinedata
    private void createandSaveOfflinedata() {
        try {


            FileOutputStream fOut = openFileOutput(secondfile, MODE_PRIVATE);
            fOut.write(edtfname.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtlname.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtdname.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtaddress.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtemail.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtphonenbr.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(regionName.getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtlong.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(edtlat.getText().toString().getBytes());
            fOut.write(":".getBytes());
            fOut.write(AgentImage.getBytes());
            fOut.write(":".getBytes());
            fOut.write(AgentFrontID.getBytes());
            fOut.write(":".getBytes());
            fOut.write(AgentBackID.getBytes());
            File fileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), secondfile);
            System.out.println("Saved @ :"+fileDir);
            Toast.makeText(getBaseContext(), "File saved at" + fileDir, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //---return pin and assign to zero
    //function to create and save the msisdn and pin
    private void createandSaveMSISDNandPIN(){

        fileContents = edtphonenbr.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.write(":".getBytes());
            fos.write("PIN".getBytes());
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

    //return region names...
    public ArrayList<String> GetRegions()
    {
        ArrayList<String> my_array = new ArrayList<String>();
        boolean flg=false;
        if ((flg = connecttoDB()) == true) {
            Statement stmt3 = null;

            try {
                stmt3 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //select region names
            String sqlStmt1 = "SELECT REGION_NAME FROM REGION";
            ResultSet rs1 = null;

            try {
                rs1 = stmt3.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            my_array.add("None");
            while (true) {
                try {
                    if (!rs1.next()) break;
                    value = (rs1.getString("REGION_NAME"));
                    my_array.add(value);

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
                stmt3.close();
                conn.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        try  {


        }catch(Exception e) {
            System.out.println(e.toString());
        }
        return my_array;
    }

    //returns region id
    public String getRegionID(String varregioname)
    {
        String regionID = null;
        String region_ID=null;

        boolean flg=false;
        if ((flg = connecttoDB()) == true) {
            Statement stmt3 = null;

            try {
                stmt3 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //select region names
            String sqlStmt1 = "SELECT REGION_ID FROM REGION where REGION_NAME='" + varregioname + "'";
            System.out.println(sqlStmt1);
            ResultSet rs1 = null;

            try {
                rs1 = stmt3.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs1.next()) break;
                    region_ID = (rs1.getString("REGION_ID"));
                    regionID = region_ID;
                    System.out.println(regionID);
                    System.out.println();
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
                stmt3.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return regionID;

    }
    //validation of email
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void FillSpinner()
    {

        ArrayList<String> my_array = new ArrayList<String>();
        my_array = GetRegions();

        if(my_array.size()>0) {
            ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, my_array);
            spregion.setAdapter(my_Adapter);
        }

    }


    Thread thread1 = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FillSpinner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    Thread threadimage = new Thread(new Runnable() {

        @Override
        public void run() {
            try {


                //sftp send pictures

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
                    String agentSFTPpath="/usr/share/tomcat/webapps/alm/resources/";
                    channelSftp.cd(agentSFTPpath);
                    //check if the global status if equals zero do it
                    if(gimagestatus.equalsIgnoreCase("0")) {

                        File agentimg = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentImage + ".jpg");
                        String imgagent = String.valueOf(agentimg);
                        channelSftp.put(imgagent, "AgentPic");
                        Boolean success1 = true;

                        if (success1) {
                            System.out.println("upload completed : " + imgagent);
                            UpdateAgentPicStatus(globalagentID,"AGENT_IMAGE_STATUS");
                            BtnAgentImage.setBackgroundColor(Color.GREEN);
                            agentimg.delete();
                        }
                    }

                    if(gfrontstatus.equalsIgnoreCase("0")) {

                        File agentfrontid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentFrontID + ".jpg");
                        String frontid = String.valueOf(agentfrontid);
                        channelSftp.put(frontid, "AgentPic");
                        Boolean success2 = true;

                        if (success2) {
                            System.out.println("upload completed : " + frontid);
                            UpdateAgentPicStatus(globalagentID,"FRONT_SIDE_ID_STATUS");
                            BtnFrontID.setBackgroundColor(Color.GREEN);
                            agentfrontid.delete();
                        }
                    }

                    if(gbackstatus.equalsIgnoreCase("0")) {

                        File agenbackid = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), AgentBackID + ".jpg");
                        String backid = String.valueOf(agenbackid);
                        channelSftp.put(backid, "AgentPic");
                        Boolean success3 = true;

                        if (success3) {
                            System.out.println("upload completed : " + backid);
                            UpdateAgentPicStatus(globalagentID,"BACK_SIDE_ID_STATUS");
                            BtnBackID.setBackgroundColor(Color.GREEN);
                            agenbackid.delete();
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
                    stmtinsert1 = conn.prepareStatement("update AGENT set " + vcolname + "=1  where AGENT_ID ='" + vmsisdn + "'");
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

    Thread showwait = new Thread() {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("WAIT  .........");
                    textstatus=findViewById(R.id.textstatus);
                    textstatus.setText("Please wait");
                }
            });
        }
    };

}