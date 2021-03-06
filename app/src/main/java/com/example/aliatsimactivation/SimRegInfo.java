package com.example.aliatsimactivation;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class SimRegInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String gender = null;
    private int count,counter=0;
    public Connection conn;
    private String globalsimid,simID,vartext="";
    private Button submit, frontid, backid,btnlvsimreg,btnclientimg,BtnModedata,BtnModesave;
    private Button sign;
    private File file,OfflineFile;
    private String nationality = "";
    private RadioButton kenya;
    private RadioButton foreign;
    private RadioButton male;
    private RadioButton female;
    private CheckBox checkBox,checkSim;
    private TextView editdate, editmname;
    private Integer a;
    private long fb = 0;
    private String gsigstatus,gfrontstatus,gbackstatus,agentNumber;
    private String vsimid,vpic,vcol;
    private String s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,b;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",emailpattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    private EditText editaltnumber,editemail,editphylocation,editpost,editussdstatus;
    private TextView editlname, editfname, textF, textB, textS,txtmode,txtmodedata,txtmodesave;
    private String file1 = "MSISDN.txt";
    private String s0, s1, Result,CLIENT,gclientstatus,Cameraresult;
    private String FRONT, BACK, SIGN = null;
    private TextView editidagent, editagent,textC,txtussd;
    SFTP sftp = new SFTP();
    String user = sftp.getUser();
    String pass = sftp.getPass();
    String host = sftp.getServer();
    int e = sftp.getPort();
    String clientSFTPpath="/usr/share/tomcat/webapps/alm/resources/";
    private Button Btnftp,BtnDelete,BtnMain,BtnRegandActivate,btnMode;
    private ImageButton signimgIcon, frontimgIcon, backimgIcon,btndob,clientimgIcon;
    private String[] imagesource;
    FTPClient ftpClient = new FTPClient();
    private String PathSignFTP, PathFrontFTP, PathBackFTP;
    private TextView editmobile,txttest,txtmsg;
    private String stroffile;
    private Spinner sp;
    private boolean connectflag=false;
    private Drawable ddd;
    private String picsdate; /// variable for picture name
    private String SIGNorigin,SIGNnew,BACKorigin,BACKnew,gsigstatusnew,gsigstatusorigin,gbackstatusnew,gbackstatusorigin,FRONTnew,FRONTorigin,gfrontstatusnew,gfrontstatusorigin;
    private View linesign,linefront,lineback,lineclient,ussdview;  //line under discard
    private TextView discardsign,discardfront,discardback,discardclient; // for discard
    private String globalMode,gclientstatusnew,gclientstatusorigin,CLIENTorigin,CLIENTnew;
    private ProgressBar progressBar;
    private GpsTracker gpsTracker;
    private TextView txtlong,txtlat,txtselllong,txtselllat;
    private EditText edtlong,edtlat,edtselllong,edtselllat;

    //capture images from cam and save it on the phone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

            FRONT = editfname.getText().toString() + editlname.getText().toString()+ "_FRONT_" + editmobile.getText().toString() + "_" + editidagent.getText().toString()+"_"+picsdate;
            Bitmap bmp = (Bitmap) data.getExtras().get("data");

            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FRONT + ".jpg");

            Log.d("path", file.toString());
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                textF.setText(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + FRONT);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }


            File frontsave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FRONT + ".jpg");
            if (frontsave.exists()) {
                frontimgIcon.setVisibility(View.VISIBLE);
                frontimgIcon.setBackgroundResource(0);
                gfrontstatus="0";
            }

        }

        /*
        if (requestCode == 101) {
            BACK = editfname.getText().toString() + editlname.getText().toString() + "_BACK_" + editmobile.getText().toString() + "_" + editidagent.getText().toString()+"_"+picsdate;
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");

            Log.d("path", file.toString());
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                textB.setText(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + BACK);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }


            File backsave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
            if (backsave.exists()) {
                backimgIcon.setVisibility(View.VISIBLE);
                backimgIcon.setBackgroundResource(0);
                gbackstatus="0";
            }

        } */

        if (requestCode == 102) {
            CLIENT = editfname.getText().toString() + editlname.getText().toString() + "_CLIENT_" + editmobile.getText().toString() + "_" + editidagent.getText().toString()+"_"+picsdate;

            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), CLIENT + ".jpg");

            Log.d("path", file.toString());
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                textC.setText(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + CLIENT);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }


            File clientsave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), CLIENT + ".jpg");
            if (clientsave.exists()) {
                clientimgIcon.setVisibility(View.VISIBLE);
                clientimgIcon.setBackgroundResource(0);
                gclientstatus="0";
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (globalMode.equalsIgnoreCase("Online")) {
            System.out.println("message_key " + stroffile.toString());
            Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
            i.putExtra("message_key", stroffile.toString());
            i.putExtra("globalMode", globalMode);
            i.putExtra("agentNumber", agentNumber);
            startActivity(i);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simactivityview);
        editfname = (TextView) findViewById(R.id.efirstname);
        editmname = (TextView) findViewById(R.id.emiddlename);
        editlname = (TextView) findViewById(R.id.elastname);
        editmobile = (TextView) findViewById(R.id.emobilenumber);
        editdate = (TextView) findViewById(R.id.edateofbirth);
        kenya = findViewById(R.id.ekenian);
        foreign = findViewById(R.id.eforien);
        male = findViewById(R.id.emale);
        female = findViewById(R.id.efemale);
        editaltnumber = (EditText) findViewById(R.id.ealtirnativenumber);
        editemail = (EditText) findViewById(R.id.email);
        editphylocation = (EditText) findViewById(R.id.ephysicallocation);
        editpost = (EditText) findViewById(R.id.epostaladdress);
        sp = (Spinner) findViewById(R.id.statusSpinner);
        textB = findViewById(R.id.backpath);
        textF = findViewById(R.id.frontpath);
        textS = findViewById(R.id.sigpath);
        editagent = findViewById(R.id.eagentnum);
        editidagent =  findViewById(R.id.eagentid);
        signimgIcon = findViewById(R.id.signimgIcon);
        frontimgIcon = findViewById(R.id.frontimgIcon);
        backimgIcon = findViewById(R.id.backimgIcon);
        BtnDelete=findViewById(R.id.BtnDelete);
        BtnMain=findViewById(R.id.BtnMainn);
        BtnRegandActivate=findViewById(R.id.activatesim);
        sign = findViewById(R.id.bsigniture);
        submit = findViewById(R.id.submitbtn);
        frontid = findViewById(R.id.bfront);
        backid = findViewById(R.id.bback);
        checkBox = findViewById(R.id.chterms);
        btndob=findViewById(R.id.btncalender);
        discardsign=findViewById(R.id.signstatus);
        linesign=findViewById(R.id.line);
        discardfront=findViewById(R.id.frontstatus);
        linefront=findViewById(R.id.line1);
        discardback=findViewById(R.id.backstatus);
        lineback=findViewById(R.id.line2);
        btnlvsimreg=findViewById(R.id.simreglistview);
        txtmsg=findViewById(R.id.txtmsg);
        editussdstatus=findViewById(R.id.edtussdstatus);
        btnclientimg=findViewById(R.id.btnclientimg);
        discardclient=findViewById(R.id.clientimgstatus);
        lineclient=findViewById(R.id.lineclient);
        textC=findViewById(R.id.clientimgpath);
        clientimgIcon=findViewById(R.id.clientimgIcon);
        txtussd=findViewById(R.id.txtussd);
        ussdview=findViewById(R.id.ussdview);
        btnMode=findViewById(R.id.btnMode);
        BtnModesave=findViewById(R.id.btnModesave);
        checkSim=findViewById(R.id.simulation);
        BtnModedata=findViewById(R.id.btnModedata);
        txtmode=findViewById(R.id.txtmode);
        txtmodedata=findViewById(R.id.txtmodedata);
        txtmodesave=findViewById(R.id.txtmodesave);
        sp.setEnabled(false);
        progressBar=findViewById(R.id.progressbar);
        edtlat=findViewById(R.id.edtlat);
        edtlong=findViewById(R.id.edtlong);
        edtselllat=findViewById(R.id.edtselllat);
        edtselllong=findViewById(R.id.edtselllong);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        editdate.setText(df.format(c));

        // get picture file name using date day month hour min and sec
        LocalDateTime picsdT= LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = picsdT.format(format);
        System.out.println("After Formatting: " + formatDateTime);
        picsdate=formatDateTime;



        Intent intent = SimRegInfo.this.getIntent();
        String str = intent.getStringExtra("message_key");
        globalsimid = str.toString();
        stroffile= intent.getStringExtra("db-offline-to-main");
        globalMode=intent.getStringExtra("globalMode");
        agentNumber=intent.getStringExtra("agentNumber");
        String Off55 = intent.getStringExtra("offline5");
        String myFileName1 = "SIM_" + Off55 + ".txt";
        File directory1 = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File OfflineFile1 = new File(directory1,myFileName1);



        //read value and result from camera
        String Cameravalue = intent.getStringExtra("key_value");
        System.out.println("reading value: "+ Cameravalue );
        if (Cameravalue==null) {

        } else {
            Cameraresult = intent.getStringExtra("key_result");
            System.out.println("reading result: " + Cameraresult);
            editidagent.setText(getIntent().getStringExtra("keyIDnb"));
            editfname.setText(getIntent().getStringExtra("keyFirstName"));
            editmname.setText(getIntent().getStringExtra("keyMiddleName"));
            editlname.setText(getIntent().getStringExtra("keyLastName"));
            editdate.setText(getIntent().getStringExtra("keyDate"));
            Cameraresult = Cameravalue;
            textB.setText(Cameraresult);
            BACK = Cameraresult;
            BACKnew=BACK;

            if (textB.getText().toString() != "") {

                backimgIcon.setVisibility(View.VISIBLE);
                backimgIcon.setBackgroundResource(0);
                gbackstatus = gbackstatusnew;
                discardback.setVisibility(View.VISIBLE);
                lineback.setVisibility(View.VISIBLE);

            } else {
                backimgIcon.setVisibility(View.INVISIBLE);
            }
            System.out.println("textB : " + textB.getText().toString());

            if (globalsimid.equalsIgnoreCase("0")) {
                backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                lineback.setVisibility(View.VISIBLE);
                discardback.setVisibility(View.VISIBLE);

            } else {
                backimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                gbackstatusnew = "0";

            }

            BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
            txtmodesave.setText("Not Saved");
        }

        //in case agent was null to get it from local file
        if (agentNumber==null || agentNumber=="") {
            getagentnumber();
        }

        if (globalMode.equalsIgnoreCase("Online")) {
            btnMode.setBackgroundColor(Color.GREEN);
            BtnModedata.setBackgroundColor(Color.BLUE);
            txtmode.setText("Online");
            txtmodedata.setText("Online Data");
            if(stroffile.equalsIgnoreCase("-100") || OfflineFile1.exists()){
                BtnModedata.setBackgroundColor(Color.GRAY);
                txtmodedata.setText("Offline Data");
            }else{
                BtnModedata.setBackgroundColor(Color.BLUE);
                txtmodedata.setText("Online Data");
            }
        }else {
            btnMode.setBackgroundColor(Color.RED);
            BtnModedata.setBackgroundColor(Color.GRAY);
            txtmode.setText("Offline");
            txtmodedata.setText("Offline Data");
            BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
        }


        //show arrow
        ImageButton backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalMode.equalsIgnoreCase("Online")) { //go to listview if in On line mode
                    Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                    i.putExtra("message_key", stroffile);
                    i.putExtra("agentNumber", agentNumber);
                    startActivity(i);
                }else { // move to main directly if in off line mode
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    i.putExtra("db-offline-to-main",stroffile);
                    i.putExtra("globalMode",globalMode);
                    i.putExtra("agentNumber",agentNumber);
                    startActivity(i);
                }
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 27);
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        //back id button to capture the id back side
        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalMode.equalsIgnoreCase("Online")) {
                    if (globalsimid.equalsIgnoreCase("0")) {
                        backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                        backimgIcon.setVisibility(View.VISIBLE);
                        backimgIcon.setVisibility(View.VISIBLE);
                        discardback.setVisibility(View.VISIBLE);
                        lineback.setVisibility(View.VISIBLE);
                    } else {
                        frontimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                        gfrontstatusnew = "0";

                    }
                }
                Intent intent = new Intent(SimRegInfo.this, CameraActivity.class);
                intent.putExtra("message_key",globalsimid);
                intent.putExtra("db-offline-to-main",stroffile);
                intent.putExtra("globalMode",globalMode);
                intent.putExtra("agentNumber",agentNumber);
                startActivity(intent);

            }

        });
        //online validation if we chnaged in radios,textboxes
        //on text change for first name
        editfname.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editfname.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editfname.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editmname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editmname.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editmname.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editlname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editlname.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editlname.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editidagent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editidagent.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editidagent.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        editmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editmobile.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editmobile.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editaltnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editaltnumber.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editaltnumber.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editemail.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editemail.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editphylocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editphylocation.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editphylocation.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editpost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vartext=editpost.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!vartext.equalsIgnoreCase("")) {
                    if (vartext.equalsIgnoreCase(editpost.getText().toString())) {

                    } else {
                        BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                        txtmodesave.setText("Not Saved");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!globalsimid.equalsIgnoreCase("0") && male.isChecked()){

                }else {
                    BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                    txtmodesave.setText("Not Saved");
                }

            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!globalsimid.equalsIgnoreCase("0") && female.isChecked()){

                }else {
                    BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                    txtmodesave.setText("Not Saved");
                }
            }
        });

        kenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!globalsimid.equalsIgnoreCase("0") && kenya.isChecked()) {

                } else {
                    BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                    txtmodesave.setText("Not Saved");
                }
            }
        });

        foreign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!globalsimid.equalsIgnoreCase("0") && foreign.isChecked()) {

                } else {
                    BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
                    txtmodesave.setText("Not Saved");
                }
            }
        });

        ////////////////////////////////////////////////////////





        if (networkInfo != null && networkInfo.isConnected() && globalMode.equalsIgnoreCase("Online")) {

            Toast.makeText(SimRegInfo.this, "Connected", Toast.LENGTH_SHORT).show();
            System.out.println("globalsimid "+ globalsimid);

            if(globalsimid.equalsIgnoreCase("0")){
                System.out.println("globalsimid 11"+ globalsimid);
                gsigstatus="0";
                gfrontstatus="0";
                gbackstatus="0";
                gclientstatus="0";
                BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                txtmodesave.setText("New");

                // call class Gps to get our location
                gpsTracker = new GpsTracker (getApplicationContext ( ));
                if (gpsTracker.canGetLocation ( )) {
                    double latitude = gpsTracker.getLatitude ( );
                    double longitude = gpsTracker.getLongitude ( );
                    edtlat.setText(String.valueOf(latitude));
                    edtlong.setText(String.valueOf(longitude));
                    edtselllat.setText(String.valueOf(latitude));
                    edtselllong.setText(String.valueOf(longitude));
                } else {
                    gpsTracker.showSettingsAlert ( );
                    edtlat.setText("0");
                    edtlong.setText("0");
                    edtselllat.setText("0");
                    edtselllong.setText("0");
                }

            }

            if (!globalsimid.equalsIgnoreCase("0")) {
                System.out.println("globalsimid 22"+ globalsimid);
                BtnModesave.setBackgroundColor(Color.GREEN);
                txtmodesave.setText("Saved");
                // Handler handler = new Handler();
                //  handler.post(new Runnable() {
                //     @Override
                //      public void run() {
                //      threadload.start();
                //}
                //  });

                //getDataforSimfromDB();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //getDataforSimfromDB();

                        // ExampleRunnable runnable =new ExampleRunnable();
                        // runnable.run();

                        // run in offline very good
                        threadload.start();


                    }
                });
            }


            editagent.setText(agentNumber);






            // add dsicard and button left when changing pictures and signature
            String Off5 = intent.getStringExtra("offline5");
            String myFileName = "SIM_" + Off5 + ".txt";
            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            OfflineFile = new File(directory,myFileName);
            if (OfflineFile.exists())
            {
                String Off1 = intent.getStringExtra("offline1");
                editfname.setText(Off1);
                System.out.println("fname :"+Off1);

                String Off2 = intent.getStringExtra("offline2");
                editmname.setText(Off2);
                System.out.println("lname :"+Off2);

                String Off3 = intent.getStringExtra("offline3");
                editlname.setText(Off3);

                String Off4 = intent.getStringExtra("offline4");
                editidagent.setText(Off4);

                Off5 = intent.getStringExtra("offline5");
                editmobile.setText(Off5);

                String Off6 = intent.getStringExtra("offline6");
                editdate.setText(Off6);

                String Off7 = intent.getStringExtra("offline7");
                editaltnumber.setText(Off7);

                String Off8 = intent.getStringExtra("offline8");
                editemail.setText(Off8);

                String Off9 = intent.getStringExtra("offline9");
                editphylocation.setText(Off9);

                String Off10 = intent.getStringExtra("offline10");
                editpost.setText(Off10);

                String Off11 = intent.getStringExtra("offline11");
                String ahmadd = "Male";
                if (Off11 != null) {
                    if (Off11.equals(ahmadd)) {
                        male.setChecked(true);
                    } else {
                        female.setChecked(true);
                    }
                }

                String Off12 = intent.getStringExtra("offline12");
                String ahmad = "Kenyan";
                if (Off12 != null) {
                    if (Off12.equals(ahmad)) {
                        kenya.setChecked(true);
                    } else {
                        foreign.setChecked(true);
                    }
                }
                BtnModesave.setBackgroundColor(Color.GREEN);
                txtmodesave.setText("Saved");
                String Off17 = intent.getStringExtra("offline17");
                sp.setSelection(((ArrayAdapter<String>) sp.getAdapter()).getPosition(Off17));

                String Off14 = intent.getStringExtra("offline14");
                textS.setText(Off14);
                SIGN = Off14;

                String Off15 = intent.getStringExtra("offline15");
                textF.setText(Off15);
                FRONT = Off15;

                String Off16 = intent.getStringExtra("offline16");
                textB.setText(Off16);
                BACK = Off16;

                String Off13 = intent.getStringExtra("offline13");
                textC.setText(Off13);
                CLIENT = Off13;

                String Off18 = intent.getStringExtra("offline18");
                editussdstatus.setText(Off18);

                checkBox.setChecked(true);

                if (textF.getText().toString() != "") {
                    frontimgIcon.setVisibility(View.VISIBLE);
                    frontimgIcon.setBackgroundResource(0);
                    gfrontstatus = "0";
                    discardfront.setVisibility(View.VISIBLE);
                    linefront.setVisibility(View.VISIBLE);
                }

                if (textB.getText().toString() != "") {
                    backimgIcon.setVisibility(View.VISIBLE);
                    backimgIcon.setBackgroundResource(0);
                    gbackstatus = "0";
                    discardback.setVisibility(View.VISIBLE);
                    lineback.setVisibility(View.VISIBLE);
                }

                if (textS.getText().toString() != "") {
                    signimgIcon.setVisibility(View.VISIBLE);
                    signimgIcon.setBackgroundResource(0);
                    gsigstatus = "0";
                    discardsign.setVisibility(View.VISIBLE);
                    linesign.setVisibility(View.VISIBLE);
                }

                if (textC.getText().toString() != "") {
                    clientimgIcon.setVisibility(View.VISIBLE);
                    clientimgIcon.setBackgroundResource(0);
                    gclientstatus = "0";
                    discardclient.setVisibility(View.VISIBLE);
                    lineclient.setVisibility(View.VISIBLE);
                }

            }


            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
            count = 0;
            for (File f : files) {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" + count);
            }
            Button BtnData = (Button) findViewById(R.id.BtnData);
            if (count != 0) {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }


            //BtnData appear in case online files exist
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                    i.putExtra("message_key", globalsimid);
                    i.putExtra("globalMode",globalMode);
                    i.putExtra("db-offline-to-main",stroffile);
                    i.putExtra("agentNumber",agentNumber);
                    startActivity(i);
                }
            });

            //BtnData appear in case online files exist
            btnlvsimreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("agent number for listview "+ agentNumber);
                    Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                    i.putExtra("message_key", stroffile);
                    i.putExtra("agentNumber", agentNumber);
                    startActivity(i);
                }
            });


            btndob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });


            //front id button to capture the id front side
            frontid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                    FRONTnew=FRONT;


                    if(globalsimid.equalsIgnoreCase("0")) {
                        frontimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                        frontimgIcon.setVisibility(View.VISIBLE);
                        frontimgIcon.setVisibility(View.VISIBLE);
                        discardfront.setVisibility(View.VISIBLE);
                        linefront.setVisibility(View.VISIBLE);

                    }else{
                        frontimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                        gfrontstatusnew="0";

                    }


                    if (textF.getText().toString() != "") {

                        frontimgIcon.setVisibility(View.VISIBLE);
                        frontimgIcon.setBackgroundResource(0);
                        gfrontstatus =gfrontstatusnew;
                        discardfront.setVisibility(View.VISIBLE);
                        linefront.setVisibility(View.VISIBLE);

                    }else {
                        frontimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });

            //discarding picture from saving
            discardfront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (globalsimid.equalsIgnoreCase("0")) {
                        textF.setText("");
                        File frontimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),FRONT+".jpg");
                        frontimage.delete();
                        frontimgIcon.setVisibility(View.INVISIBLE);
                        discardfront.setVisibility(View.INVISIBLE);
                        linefront.setVisibility(View.INVISIBLE);
                    }

                    else{
                        if (gfrontstatusorigin.equalsIgnoreCase("1")) {
                            discardfront.setVisibility(View.INVISIBLE);
                            linefront.setVisibility(View.INVISIBLE);
                            File frontimage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FRONT + ".jpg");
                            frontimage.delete();
                            frontimgIcon.setVisibility(View.VISIBLE);
                            frontimgIcon.setColorFilter(Color.GREEN);
                            textF.setText(FRONTorigin);
                            FRONT = FRONTorigin;
                            gfrontstatus=gfrontstatusorigin;
                        }
                        if(gfrontstatusorigin.equalsIgnoreCase("0")){
                            gfrontstatus=gfrontstatusorigin;
                            frontimgIcon.setVisibility(View.VISIBLE);
                            frontimgIcon.setBackgroundResource(0);
                            frontimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                            discardfront.setVisibility(View.INVISIBLE);
                            linefront.setVisibility(View.INVISIBLE);
                            File frontimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),FRONTnew+".jpg");
                            frontimage.delete();
                            textF.setText(FRONTorigin);
                            FRONT = FRONTorigin;
                        }
                    }



                }
            });









            //discarding picture from saving
            discardback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (globalsimid.equalsIgnoreCase("0")) {
                        textB.setText("");
                        File backimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),BACK+".jpg");
                        backimage.delete();
                        System.out.println("hello");
                        backimgIcon.setVisibility(View.INVISIBLE);
                        discardback.setVisibility(View.INVISIBLE);
                        lineback.setVisibility(View.INVISIBLE);
                        System.out.println("bye");
                    }else {
                        if (gbackstatusorigin.equalsIgnoreCase("1")) {
                            discardback.setVisibility(View.INVISIBLE);
                            lineback.setVisibility(View.INVISIBLE);
                            File backimage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
                            backimage.delete();
                            backimgIcon.setVisibility(View.VISIBLE);
                            backimgIcon.setBackgroundResource(0);
                            backimgIcon.setColorFilter(Color.GREEN);
                            textB.setText(BACKorigin);
                            BACK = BACKorigin;
                            gbackstatus=gbackstatusorigin;
                        }

                        if (gbackstatusorigin.equalsIgnoreCase("0")){
                            gbackstatus=gbackstatusorigin;
                            backimgIcon.setVisibility(View.VISIBLE);
                            backimgIcon.setBackgroundResource(0);
                            backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                            discardback.setVisibility(View.INVISIBLE);
                            lineback.setVisibility(View.INVISIBLE);
                            File backimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),BACKnew+".jpg");
                            backimage.delete();
                            textB.setText(BACKorigin);
                            BACK = BACKorigin;
                        }

                    }


                }


            });


            //signature button to capture the signature of the client
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                    SIGN = editfname.getText().toString() + editlname.getText().toString() + "SIGNATURE" + editmobile.getText().toString() + "" + editidagent.getText().toString()+""+picsdate;
                    SIGNnew=SIGN;
                    a.putExtra("sign", SIGN);
                    startActivity(a);
                    if(globalsimid.equalsIgnoreCase("0")) {
                        signimgIcon.setColorFilter(Color.parseColor("#4169E1"));


                    }else{
                        signimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                        gsigstatusnew="0";

                    }
                    textS.setText(SIGN);

                    if (textS.getText().toString() != "") {
                        signimgIcon.setVisibility(View.VISIBLE);
                        signimgIcon.setBackgroundResource(0);
                        gsigstatus = "0";
                        discardsign.setVisibility(View.VISIBLE);
                        linesign.setVisibility(View.VISIBLE);
                    }else{
                        signimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });

            //discarding picture from saving
            discardsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(globalsimid.equalsIgnoreCase("0")){
                        System.out.println("hello");
                        textS.setText("");
                        File signimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SIGN+".jpg");
                        signimage.delete();
                        signimgIcon.setVisibility(View.INVISIBLE);
                        discardsign.setVisibility(View.INVISIBLE);
                        linesign.setVisibility(View.INVISIBLE);
                        System.out.println("bye");
                    }
                    else{
                        System.out.println(gsigstatusorigin);
                        if(gsigstatusorigin.equalsIgnoreCase("1")){
                            discardsign.setVisibility(View.INVISIBLE);
                            linesign.setVisibility(View.INVISIBLE);
                            File signimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SIGN+".jpg");
                            signimage.delete();
                            signimgIcon.setVisibility(View.VISIBLE);
                            signimgIcon.setColorFilter(Color.GREEN);
                            textS.setText(SIGNorigin);
                            SIGN=SIGNorigin;
                            System.out.println(SIGN);
                            gsigstatus=gsigstatusorigin;

                        }
                        else{
                            gsigstatus=gsigstatusorigin;
                            signimgIcon.setVisibility(View.VISIBLE);
                            signimgIcon.setBackgroundResource(0);
                            signimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                            discardsign.setVisibility(View.INVISIBLE);
                            linesign.setVisibility(View.INVISIBLE);
                            File signimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SIGNnew+".jpg");
                            signimage.delete();
                            textS.setText(SIGNorigin);
                            SIGN=SIGNorigin;

                        }

                    }
                }
            });

            //agentimage  button to capture the agent image
            btnclientimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 102);
                    CLIENTnew=CLIENT;


                    if(globalsimid.equalsIgnoreCase("0")) {
                        clientimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                        clientimgIcon.setVisibility(View.VISIBLE);
                        clientimgIcon.setVisibility(View.VISIBLE);
                        discardclient.setVisibility(View.VISIBLE);
                        lineclient.setVisibility(View.VISIBLE);

                    }else{
                        clientimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                        gclientstatusnew="0";

                    }


                    if (textC.getText().toString() != "") {

                        clientimgIcon.setVisibility(View.VISIBLE);
                        clientimgIcon.setBackgroundResource(0);
                        gclientstatus =gclientstatusnew;
                        discardclient.setVisibility(View.VISIBLE);
                        lineclient.setVisibility(View.VISIBLE);

                    }else {
                        clientimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });

            //discarding picture from saving
            discardclient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (globalsimid.equalsIgnoreCase("0")) {
                        textC.setText("");
                        File clientimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),CLIENT+".jpg");
                        clientimage.delete();
                        clientimgIcon.setVisibility(View.INVISIBLE);
                        discardclient.setVisibility(View.INVISIBLE);
                        lineclient.setVisibility(View.INVISIBLE);
                    }

                    else{
                        if (gclientstatusorigin.equalsIgnoreCase("1")) {
                            discardclient.setVisibility(View.INVISIBLE);
                            lineclient.setVisibility(View.INVISIBLE);
                            File clientimage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), CLIENT + ".jpg");
                            clientimage.delete();
                            clientimgIcon.setVisibility(View.VISIBLE);
                            clientimgIcon.setColorFilter(Color.GREEN);
                            textC.setText(CLIENTorigin);
                            CLIENT = CLIENTorigin;
                            gclientstatus=gclientstatusorigin;
                        }
                        if(gclientstatusorigin.equalsIgnoreCase("0")){
                            gclientstatus=gclientstatusorigin;
                            clientimgIcon.setVisibility(View.VISIBLE);
                            clientimgIcon.setBackgroundResource(0);
                            clientimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                            discardclient.setVisibility(View.INVISIBLE);
                            lineclient.setVisibility(View.INVISIBLE);
                            File clientimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),CLIENTnew+".jpg");
                            clientimage.delete();
                            textC.setText(CLIENTorigin);
                            CLIENT = CLIENTorigin;
                        }
                    }



                }
            });


            BtnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("stroffile "+ stroffile);
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    i.putExtra("db-offline-to-main",stroffile);
                    i.putExtra("globalMode",globalMode);
                    i.putExtra("agentNumber",agentNumber);
                    startActivity(i);
                }
            });
            //delete on line
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sp.getSelectedItem().toString().toString().equalsIgnoreCase("New")) {
                        Toast.makeText(SimRegInfo.this, "Set offline mode to delete offline files", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (sp.getSelectedItem().toString().toString().equalsIgnoreCase("In Progress")) {
                        Toast.makeText(SimRegInfo.this, "Deleting Photos from SFTP", Toast.LENGTH_LONG).show();
                        thread2.start();
                        Toast.makeText(SimRegInfo.this, "The Photos Deleted from SFTP", Toast.LENGTH_LONG).show();

                        boolean flg = false;
                        try {
                            if ((flg = connecttoDB()) == true) {

                                Toast.makeText(SimRegInfo.this, "Starting Deletion in DataBase", Toast.LENGTH_SHORT).show();
                                PreparedStatement stmtinsert1 = null;

                                try {
                                    stmtinsert1 = conn.prepareStatement("delete from CLIENTS where CLIENT_ID ='" + globalsimid + "'");
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                try {
                                    stmtinsert1.executeUpdate();
                                    Toast.makeText(SimRegInfo.this, "Deleted Completed from DataBase", Toast.LENGTH_LONG).show();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }


                                try {
                                    stmtinsert1.close();
                                    conn.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                                i.putExtra("agentNumber",agentNumber);
                                i.putExtra("message_key",stroffile);
                                startActivity(i);




                            } else {
                                String myFileName = "SIM_" + editmobile.getText().toString() + ".txt";
                                File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                OfflineFile = new File(directory,myFileName);
                                OfflineFile.delete();
                                Toast.makeText(SimRegInfo.this,"Offline File Deleted Successfully",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(SimRegInfo.this,"Delete valid for in progress status",Toast.LENGTH_LONG).show();
                    }

                }

            });

            BtnRegandActivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txtmodesave.getText().toString().equalsIgnoreCase("Saved")) {
                        Toast.makeText(SimRegInfo.this, "Complete your data saving before registrering", Toast.LENGTH_LONG).show();
                        return;
                    } else {


                        if (!sp.getSelectedItem().toString().toString().equalsIgnoreCase("Success")) {
                            if (male.isChecked()) {
                                gender = "Male";
                            }
                            if (female.isChecked()) {
                                gender = "Female";
                            }
                            String fname = editfname.getText().toString();
                            String mname = editmname.getText().toString();
                            String lname = editlname.getText().toString();
                            String msisdn = editmobile.getText().toString();
                            String idType = "IDCARD";
                            String idNumber = editidagent.getText().toString();
                            String dob = editdate.getText().toString();
                            String email = editemail.getText().toString();
                            String altnumber = editaltnumber.getText().toString();
                            String address1 = editphylocation.getText().toString();
                            String state = editpost.getText().toString();
                            String agentmsisdn = editagent.getText().toString();


                            System.out.println("fname" + fname);
                            System.out.println("mname" + mname);
                            System.out.println("lname" + lname);
                            System.out.println("msisdn" + msisdn);
                            System.out.println("idType" + idType);
                            System.out.println("idNumber" + idNumber);
                            System.out.println("dob" + dob);
                            System.out.println("email" + email);
                            System.out.println("gender" + gender);
                            System.out.println("altnumber" + altnumber);
                            System.out.println("address1" + address1);
                            System.out.println("state" + state);
                            System.out.println("agentmsisdn" + agentmsisdn);
                            System.out.println("agentNumber" + agentNumber);


                            Intent a = new Intent(SimRegInfo.this, Activate_Sim.class);
                            a.putExtra("globalsimid", globalsimid);
                            a.putExtra("globalMode", globalMode);
                            a.putExtra("fname", fname);
                            a.putExtra("mname", mname);
                            a.putExtra("lname", lname);
                            a.putExtra("msisdn", msisdn);
                            a.putExtra("idType", idType);
                            a.putExtra("idNumber", idNumber);
                            a.putExtra("dob", dob);
                            a.putExtra("email", email);
                            a.putExtra("gender", gender);
                            a.putExtra("altnumber", altnumber);
                            a.putExtra("address1", address1);
                            a.putExtra("state", state);
                            a.putExtra("agentmsisdn", agentmsisdn);
                            a.putExtra("mainstatus", sp.getSelectedItem().toString().toString());
                            a.putExtra("agentNumber", agentNumber);
                            startActivity(a);
                        } else {
                            Toast.makeText(SimRegInfo.this, "Success status cannot be modified", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });



            //submit ON LINE
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sp.getSelectedItem().toString().toString().equalsIgnoreCase("Success")) {
                        if (sp.getSelectedItem().toString().matches("New")) {
                            b = "In Progress";
                            sp.setSelection(1);
                        }


                        String dY[] = editdate.getText().toString().split("-");
                        fb = getAge(Integer.parseInt(dY[2]), Integer.parseInt(dY[1]), Integer.parseInt(dY[0]));
                        if (SIGN == null || FRONT == null || BACK == null || CLIENT == null) {
                            Toast.makeText(SimRegInfo.this, "Must Have Signature and Photos", Toast.LENGTH_LONG).show();
                        } else {
                            if (editfname.getText().toString().matches("") || editmname.getText().toString().matches("") || editlname.getText().toString().matches("") || editmobile.getText().toString().matches("") || editaltnumber.getText().toString().matches("") || editemail.getText().toString().matches("") || editphylocation.getText().toString().matches("") || editpost.getText().toString().matches("") || fb < 18 || !checkBox.isChecked() || SIGN == null || FRONT == null || BACK == null || fb == 0 || editdate.getText().toString() == null || CLIENT == null) {
                                Toast.makeText(SimRegInfo.this, "Check your fields", Toast.LENGTH_LONG).show();
                                if (editfname.getText().toString().matches("")) {
                                    editfname.setError("Empty Field");
                                }
                                if (editmname.getText().toString().matches("")) {
                                    editmname.setError("Empty Field");
                                }
                                if (editlname.getText().toString().matches("")) {
                                    editlname.setError("Empty Field");
                                }
                                if (editmobile.getText().toString().matches("")) {
                                    editmobile.setError("Empty Field");
                                }
                                if (editmobile.getText().toString().startsWith("0") ) {
                                    editmobile.setError("Remove the 0 at the beginning");
                                    return;
                                }
                                if(editmobile.getText().toString().length() == 10) {
                                    editmobile.setError("Client number must be 9 numbers and not start with 0");
                                    return;
                                }
                                if (editaltnumber.getText().toString().matches("")) {
                                    editaltnumber.setError("Empty Field");
                                }
                                if (editemail.getText().toString().matches("")) {
                                    editemail.setError("Empty Field");
                                }
                                if (editphylocation.getText().toString().matches("")) {
                                    editphylocation.setError("Empty Field");
                                }
                                if (editpost.getText().toString().matches("")) {
                                    editpost.setError("Empty Field");
                                }
                                if (!editemail.getText().toString().matches(emailpattern) && !editemail.getText().toString().matches(emailpattern1)) {
                                    editemail.setError("Invalid email address");
                                }

                                if (fb < 18 && fb >= 0) {
                                    editdate.setError("Under Age");
                                } else if (fb >= 18) {
                                    editdate.setError(null);
                                }
                                if (!checkBox.isChecked()) {
                                    Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                                }
                                if (male.isChecked()) {
                                    gender = "Male";
                                }
                                if (female.isChecked()) {
                                    gender = "Female";
                                }
                                if (kenya.isChecked()) {
                                    nationality = "Kenyan";
                                }
                                if (foreign.isChecked()) {
                                    nationality = "Foreign";
                                }
                                b = sp.getSelectedItem().toString();
                                if (sp.getSelectedItem().toString().matches("New")) {
                                    b = "In Progress";
                                    sp.setSelection(1);
                                }
                            } else {
                                if (editmobile.getText().toString().startsWith("0") ) {
                                    editmobile.setError("Remove the 0 at the beginning");
                                    return;
                                }
                                if(editmobile.getText().toString().length() == 10) {
                                    editmobile.setError("Client number must be 9 numbers and not start with 0");
                                    return;
                                }

                                if (!editemail.getText().toString().matches(emailpattern) && !editemail.getText().toString().matches(emailpattern1)) {
                                    editemail.setError("Invalid email address");
                                    Toast.makeText(getApplicationContext(), "Check your email", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (male.isChecked()) {
                                    gender = "Male";
                                }
                                if (female.isChecked()) {
                                    gender = "Female";
                                }
                                if (kenya.isChecked()) {
                                    nationality = "Kenyan";
                                }
                                if (foreign.isChecked()) {
                                    nationality = "Foreign";
                                }

                                if (checkSim.isChecked()) {
                                    e = 0;
                                } else {
                                    e = 22;
                                }

                                b = sp.getSelectedItem().toString();
                                if (sp.getSelectedItem().toString().matches("New")) {
                                    b = "In Progress";
                                    sp.setSelection(1);
                                }
                                //start saving
                                new AlertDialog.Builder(SimRegInfo.this)
                                        .setTitle("Submit")
                                        .setMessage("Are you sure you want to Submit this form?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressBar.setVisibility(View.VISIBLE);
                                                progressBar.setProgress(0);
                                                Date date = new Date();
                                                Calendar calendar = new GregorianCalendar();
                                                calendar.setTime(date);
                                                int year = calendar.get(Calendar.YEAR);
                                                simID = "CL_" + year + "_";


                                                try {
                                                    b = sp.getSelectedItem().toString();
                                                    if (sp.getSelectedItem().toString().matches("New")) {
                                                        b = "In Progress";
                                                        sp.setSelection(1);
                                                    }
                                                    ;


                                                    //save on line
                                                    threadload1.start();
                                                    BtnModedata.setBackgroundColor(Color.BLUE);
                                                    txtmodedata.setText("Online Data");
                                                    BtnModesave.setBackgroundColor(Color.GREEN);
                                                    txtmodesave.setText("Saved");
                                                    // reduce counter of local save files
                                                    if (OfflineFile.exists()) {
                                                        // reduce counter of local save files
                                                        count--;
                                                        Button BtnData = (Button) findViewById(R.id.BtnData);
                                                        if (count == 0) {
                                                            BtnData.setVisibility(View.INVISIBLE);
                                                        } else {
                                                            BtnData.setText(String.valueOf(count));
                                                        }
                                                    }

                                                    discardback.setVisibility(View.INVISIBLE);
                                                    discardfront.setVisibility(View.INVISIBLE);
                                                    discardsign.setVisibility(View.INVISIBLE);
                                                    discardclient.setVisibility(View.INVISIBLE);
                                                    lineback.setVisibility(View.INVISIBLE);
                                                    linefront.setVisibility(View.INVISIBLE);
                                                    lineclient.setVisibility(View.INVISIBLE);
                                                    linesign.setVisibility(View.INVISIBLE);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }


                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }


                        }
                    }else

                    {
                        Toast.makeText(getApplicationContext(),"Success status canot be modified",Toast.LENGTH_LONG).show();
                    }
                }
            });



            signimgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("gsigstatus "+ gsigstatus);
                    System.out.println("SIGNorigin "+ SIGNorigin);

                    if(gsigstatus != "0") {

                        try {
                            Properties config = new Properties();
                            config.put("StrictHostKeyChecking", "no");

                            JSch jSch = new JSch();
                            Session session = jSch.getSession(user, host, e);
                            System.out.println("Step1");
                            session.setPassword(pass);
                            session.setConfig(config);
                            session.connect();
                            System.out.println("Step Connect");
                            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                            channelSftp.connect();
                            System.out.println("connected");
                            String fullpath=clientSFTPpath+"ClientPic";
                            channelSftp.cd(fullpath);
                            System.out.println(channelSftp.pwd());
                            String path = channelSftp.pwd() + "/";
                            System.out.println("u are here");
                            if (channelSftp.lstat(path + SIGNorigin + ".jpg") != null) {
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path + SIGNorigin + ".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis, "ddd");
                            System.out.println(ddd);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SimRegInfo.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
                            builder.setPositiveButton("BACK", null);
                            ImageView imageView = dialogLayout.findViewById(R.id.imageView);
                            imageView.setImageDrawable(ddd);
                            builder.setView(dialogLayout);
                            builder.show();
                            bis.close();
                            System.out.println("you are here now !!!!!!");
                            channelSftp.disconnect();
                            session.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } //end if gpictstus=1
                }
            });


            frontimgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gfrontstatus != "0") {
                        try {

                            Properties config = new Properties();
                            config.put("StrictHostKeyChecking", "no");

                            JSch jSch = new JSch();
                            Session session = jSch.getSession(user, host, e);
                            System.out.println("Step1");
                            session.setPassword(pass);
                            session.setConfig(config);
                            session.connect();
                            System.out.println("Step Connect");
                            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                            channelSftp.connect();
                            System.out.println("connected");
                            String fullpath=clientSFTPpath+"ClientPic";
                            channelSftp.cd(fullpath);
                            System.out.println(channelSftp.pwd());
                            String path = channelSftp.pwd() + "/";
                            System.out.println("u are here");
                            if (channelSftp.lstat(path + FRONTorigin + ".jpg") != null) {
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path + FRONTorigin + ".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis, "ddd");
                            System.out.println(ddd);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SimRegInfo.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
                            builder.setPositiveButton("BACK", null);
                            ImageView imageView = dialogLayout.findViewById(R.id.imageView);
                            imageView.setImageDrawable(ddd);
                            builder.setView(dialogLayout);
                            builder.show();
                            bis.close();
                            System.out.println("you are here now !!!!!!");
                            channelSftp.disconnect();
                            session.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } // end if gstatus
                }
            });



            backimgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gbackstatus != "0") {
                        try {

                            Properties config = new Properties();
                            config.put("StrictHostKeyChecking", "no");

                            JSch jSch = new JSch();
                            Session session = jSch.getSession(user, host, e);
                            System.out.println("Step1");
                            session.setPassword(pass);
                            session.setConfig(config);
                            session.connect();
                            System.out.println("Step Connect");
                            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                            channelSftp.connect();
                            System.out.println("connected");
                            String fullpath=clientSFTPpath+"ClientPic";
                            channelSftp.cd(fullpath);
                            System.out.println(channelSftp.pwd());
                            String path = channelSftp.pwd() + "/";
                            System.out.println("u are here");
                            if (channelSftp.lstat(path + BACKorigin + ".jpg") != null) {
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path + BACKorigin + ".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis, "ddd");
                            System.out.println(ddd);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SimRegInfo.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
                            builder.setPositiveButton("BACK", null);
                            ImageView imageView = dialogLayout.findViewById(R.id.imageView);
                            imageView.setImageDrawable(ddd);
                            builder.setView(dialogLayout);
                            builder.show();
                            bis.close();
                            System.out.println("you are here now !!!!!!");
                            channelSftp.disconnect();
                            session.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }// end if gstatus
                }
            });


            clientimgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gclientstatus != "0") {
                        try {
                            Properties config = new Properties();
                            config.put("StrictHostKeyChecking", "no");

                            JSch jSch = new JSch();
                            Session session = jSch.getSession(user, host, e);
                            System.out.println("Step1");
                            session.setPassword(pass);
                            session.setConfig(config);
                            session.connect();
                            System.out.println("Step Connect");
                            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                            channelSftp.connect();
                            System.out.println("connected");
                            String fullpath=clientSFTPpath+"ClientPic";
                            channelSftp.cd(fullpath);
                            System.out.println(channelSftp.pwd());
                            String path = channelSftp.pwd() + "/";
                            System.out.println("u are here");
                            if (channelSftp.lstat(path + CLIENTorigin + ".jpg") != null) {
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path + CLIENTorigin + ".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis, "ddd");
                            System.out.println(ddd);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SimRegInfo.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
                            builder.setPositiveButton("BACK", null);
                            ImageView imageView = dialogLayout.findViewById(R.id.imageView);
                            imageView.setImageDrawable(ddd);
                            builder.setView(dialogLayout);
                            builder.show();
                            bis.close();
                            System.out.println("you are here now !!!!!!");
                            channelSftp.disconnect();
                            session.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }// end if gstatus
                }
            });




            ////////end load picture from sftp

            Btnftp = findViewById(R.id.Btnftp);
            Btnftp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sp.getSelectedItem().toString().toString().equalsIgnoreCase("Success")) {
                        if (checkSim.isChecked()) {
                            e = 0;
                        } else {
                            e = 22;
                        }
                        //resend picture if not sent to sftp server
                        if (e != 0) {
                            if (globalsimid != "0") {
                                if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0") || gclientstatus.equalsIgnoreCase("0")) {
                                    Toast.makeText(SimRegInfo.this, "Uploading Photos started", Toast.LENGTH_LONG).show();
                                    System.out.println("UPDATE HERE");
                                    thread1.start();
                                    Toast.makeText(SimRegInfo.this, "Upload Completed", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(SimRegInfo.this, "You cannot reupload pictures in simulation mmode", Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(SimRegInfo.this, "Success status cannot be modified", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }//offline mode SIMREGINFO:


        else{
            Toast.makeText(SimRegInfo.this,"Offline Mode",Toast.LENGTH_SHORT).show();

            editfname = (TextView) findViewById(R.id.efirstname);
            editmname = (TextView) findViewById(R.id.emiddlename);
            editlname = (TextView) findViewById(R.id.elastname);
            editmobile = (TextView) findViewById(R.id.emobilenumber);
            editdate = (TextView) findViewById(R.id.edateofbirth);
            kenya = findViewById(R.id.ekenian);
            foreign = findViewById(R.id.eforien);
            male = findViewById(R.id.emale);
            female = findViewById(R.id.efemale);
            editaltnumber = (EditText) findViewById(R.id.ealtirnativenumber);
            editemail = (EditText) findViewById(R.id.email);
            editphylocation = (EditText) findViewById(R.id.ephysicallocation);
            editpost = (EditText) findViewById(R.id.epostaladdress);
            sp = (Spinner) findViewById(R.id.statusSpinner);
            textB = findViewById(R.id.backpath);
            textF = findViewById(R.id.frontpath);
            textS = findViewById(R.id.sigpath);
            editagent = (TextView) findViewById(R.id.eagentnum);
            editidagent = (TextView) findViewById(R.id.eagentid);
            signimgIcon = findViewById(R.id.signimgIcon);
            frontimgIcon = findViewById(R.id.frontimgIcon);
            backimgIcon = findViewById(R.id.backimgIcon);
            BtnDelete=findViewById(R.id.BtnDelete);
            BtnMain=findViewById(R.id.BtnMainn);
            BtnRegandActivate=findViewById(R.id.activatesim);
            sign = findViewById(R.id.bsigniture);
            submit = findViewById(R.id.submitbtn);
            frontid = findViewById(R.id.bfront);
            backid = findViewById(R.id.bback);
            checkBox = findViewById(R.id.chterms);
            btndob=findViewById(R.id.btncalender);
            Date c1 = Calendar.getInstance().getTime();
            SimpleDateFormat df1=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
          //  editdate.setText((df.format(c1)).toString());
            Intent intent1 = SimRegInfo.this.getIntent();
            String str1 = intent.getStringExtra("message_key");
            System.out.println("str1 : "+str1);
            globalsimid = str1.toString();

            gsigstatus="0";
            gfrontstatus="0";
            gbackstatus="0";
            gclientstatus="0";


            btndob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });


            //front id button to capture the id front side
            frontid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                    FRONTnew=FRONT;


                    frontimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    frontimgIcon.setVisibility(View.VISIBLE);
                    frontimgIcon.setVisibility(View.VISIBLE);
                    discardfront.setVisibility(View.VISIBLE);
                    linefront.setVisibility(View.VISIBLE);


                    if (textF.getText().toString() != "") {

                        frontimgIcon.setVisibility(View.VISIBLE);
                        frontimgIcon.setBackgroundResource(0);
                        gfrontstatus =gfrontstatusnew;
                        discardfront.setVisibility(View.VISIBLE);
                        linefront.setVisibility(View.VISIBLE);

                    }else {
                        frontimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });

//discarding picture from saving
            discardfront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    textF.setText("");
                    File frontimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),FRONT+".jpg");
                    frontimage.delete();
                    frontimgIcon.setVisibility(View.INVISIBLE);
                    discardfront.setVisibility(View.INVISIBLE);
                    linefront.setVisibility(View.INVISIBLE);
                }
            });
//back id button to capture the id back side
           /* backid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       // startActivityForResult(intent, 101);

                    Intent intent = new Intent(SimRegInfo.this, CameraActivity.class);
                    intent.putExtra("message_key",globalsimid);
                    intent.putExtra("db-offline-to-main",stroffile);
                    intent.putExtra("globalMode",globalMode);
                    intent.putExtra("agentNumber",agentNumber);
                    startActivity(intent);

                        BACKnew=BACK;


                        backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                        lineback.setVisibility(View.VISIBLE);
                        discardback.setVisibility(View.VISIBLE);
                        System.out.println("textB.getText().toString() "+textB.getText().toString() );
                        if (textB.getText().toString() != "") {

                            backimgIcon.setVisibility(View.VISIBLE);
                            backimgIcon.setBackgroundResource(0);
                            gbackstatus =gbackstatusnew;
                            discardback.setVisibility(View.VISIBLE);
                            lineback.setVisibility(View.VISIBLE);

                        }else {
                            backimgIcon.setVisibility(View.INVISIBLE);
                        }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });*/

            //discarding picture from saving
            discardback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    textB.setText("");
                    File backimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),BACK+".jpg");
                    backimage.delete();
                    backimgIcon.setVisibility(View.INVISIBLE);
                    discardback.setVisibility(View.INVISIBLE);
                    lineback.setVisibility(View.INVISIBLE);
                }
            });



            //signature button to capture the signature of the client
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                    SIGN = editfname.getText().toString() + editlname.getText().toString() + "SIGNATURE" + editmobile.getText().toString() + "" + editidagent.getText().toString()+""+picsdate;
                    SIGNnew=SIGN;
                    a.putExtra("sign", SIGN);
                    startActivity(a);

                    signimgIcon.setColorFilter(Color.parseColor("#4169E1"));

                    textS.setText(SIGN);

                    if (textS.getText().toString() != "") {
                        signimgIcon.setVisibility(View.VISIBLE);
                        signimgIcon.setBackgroundResource(0);
                        gsigstatus = "0";
                        discardsign.setVisibility(View.VISIBLE);
                        linesign.setVisibility(View.VISIBLE);
                    }else{
                        signimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });
            //discarding picture from saving
            discardsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textS.setText("");
                    File signimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),SIGN+".jpg");
                    signimage.delete();
                    signimgIcon.setVisibility(View.INVISIBLE);
                    discardsign.setVisibility(View.INVISIBLE);
                    linesign.setVisibility(View.INVISIBLE);


                }

            });


            //client image button to capture the client photo
            btnclientimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 102);
                    CLIENTnew=CLIENT;


                    clientimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    lineclient.setVisibility(View.VISIBLE);
                    discardclient.setVisibility(View.VISIBLE);

                    if (textC.getText().toString() != "") {

                        clientimgIcon.setVisibility(View.VISIBLE);
                        clientimgIcon.setBackgroundResource(0);
                        gclientstatus =gclientstatusnew;
                        discardclient.setVisibility(View.VISIBLE);
                        lineclient.setVisibility(View.VISIBLE);

                    }else {
                        clientimgIcon.setVisibility(View.INVISIBLE);
                    }
                    BtnModesave.setBackgroundColor(Color.rgb(255,102,0));
                    txtmodesave.setText("Not Saved");
                }
            });

            //discarding picture from saving
            discardclient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textC.setText("");
                    File clientimage=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),CLIENT+".jpg");
                    clientimage.delete();
                    clientimgIcon.setVisibility(View.INVISIBLE);
                    discardclient.setVisibility(View.INVISIBLE);
                    lineclient.setVisibility(View.INVISIBLE);
                }
            });

            editagent.setText(agentNumber);
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
            count = 0;
            for (File f : files) {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" + count);
            }



            // add dsicard and button left when changing pictures and signature
            String Off5 = intent.getStringExtra("offline5");
            String myFileName = "SIM_" + Off5 + ".txt";
            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            OfflineFile = new File(directory,myFileName);
            if (OfflineFile.exists())
            {

                BtnModesave.setBackgroundColor(Color.GREEN);
                txtmodesave.setText("Saved");
                String Off1 = intent.getStringExtra("offline1");
                editfname.setText(Off1);
                System.out.println("fname :"+Off1);

                String Off2 = intent.getStringExtra("offline2");
                editmname.setText(Off2);
                System.out.println("lname :"+Off2);

                String Off3 = intent.getStringExtra("offline3");
                editlname.setText(Off3);

                String Off4 = intent.getStringExtra("offline4");
                editidagent.setText(Off4);

                Off5 = intent.getStringExtra("offline5");
                editmobile.setText(Off5);

                String Off6 = intent.getStringExtra("offline6");
                editdate.setText(Off6);

                String Off7 = intent.getStringExtra("offline7");
                editaltnumber.setText(Off7);

                String Off8 = intent.getStringExtra("offline8");
                editemail.setText(Off8);

                String Off9 = intent.getStringExtra("offline9");
                editphylocation.setText(Off9);

                String Off10 = intent.getStringExtra("offline10");
                editpost.setText(Off10);

                String Off11 = intent.getStringExtra("offline11");
                String ahmadd = "Male";
                if (Off11 != null) {
                    if (Off11.equals(ahmadd)) {
                        male.setChecked(true);
                    } else {
                        female.setChecked(true);
                    }
                }

                String Off12 = intent.getStringExtra("offline12");
                String ahmad = "Kenyan";
                if (Off12 != null) {
                    if (Off12.equals(ahmad)) {
                        kenya.setChecked(true);
                    } else {
                        foreign.setChecked(true);
                    }
                }

                String Off17 = intent.getStringExtra("offline17");
                sp.setSelection(((ArrayAdapter<String>) sp.getAdapter()).getPosition(Off17));

                String Off14 = intent.getStringExtra("offline14");
                textS.setText(Off14);
                SIGN = Off14;

                String Off15 = intent.getStringExtra("offline15");
                textF.setText(Off15);
                FRONT = Off15;

                String Off16 = intent.getStringExtra("offline16");
                textB.setText(Off16);
                BACK = Off16;

                String Off13 = intent.getStringExtra("offline13");
                textC.setText(Off13);
                CLIENT = Off13;

                String Off18 = intent.getStringExtra("offline18");
                editussdstatus.setText(Off18);

                checkBox.setChecked(true);

                if (textF.getText().toString() != "") {
                    frontimgIcon.setVisibility(View.VISIBLE);
                    frontimgIcon.setBackgroundResource(0);
                    gfrontstatus = "0";
                    discardfront.setVisibility(View.VISIBLE);
                    linefront.setVisibility(View.VISIBLE);
                }

                if (textB.getText().toString() != "") {
                    backimgIcon.setVisibility(View.VISIBLE);
                    backimgIcon.setBackgroundResource(0);
                    gbackstatus = "0";
                    discardback.setVisibility(View.VISIBLE);
                    lineback.setVisibility(View.VISIBLE);
                }

                if (textS.getText().toString() != "") {
                    signimgIcon.setVisibility(View.VISIBLE);
                    signimgIcon.setBackgroundResource(0);
                    gsigstatus = "0";
                    discardsign.setVisibility(View.VISIBLE);
                    linesign.setVisibility(View.VISIBLE);
                }

                if (textC.getText().toString() != "") {
                    clientimgIcon.setVisibility(View.VISIBLE);
                    clientimgIcon.setBackgroundResource(0);
                    gclientstatus = "0";
                    discardclient.setVisibility(View.VISIBLE);
                    lineclient.setVisibility(View.VISIBLE);
                }

            }


            BtnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    i.putExtra("db-offline-to-main",stroffile);
                    i.putExtra("agentNumber",agentNumber);
                    i.putExtra("globalMode",globalMode);
                    startActivity(i);
                }
            });
            // delete in offline mode
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sp.getSelectedItem().toString().toString().equalsIgnoreCase("In Progress") || sp.getSelectedItem().toString().toString().equalsIgnoreCase("New")) {
                        String myFileName = "SIM_" + editmobile.getText().toString() + ".txt";
                        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        OfflineFile = new File(directory,myFileName);
                        OfflineFile.delete();
                        Toast.makeText(SimRegInfo.this,"Offline File Deleted Successfully",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }else {
                        Toast.makeText(SimRegInfo.this,"Delete function valid for in progress status",Toast.LENGTH_LONG).show();
                    }

                }
            });

            //OFF LINE
            BtnRegandActivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (male.isChecked()) {
                        gender = "Male";
                    }
                    if (female.isChecked()) {
                        gender = "Female";
                    }
                    String fname = editfname.getText().toString();
                    String mname = editmname.getText().toString();
                    String lname = editlname.getText().toString();
                    String msisdn = editmobile.getText().toString();
                    String idType = "IDCARD";
                    String idNumber = editidagent.getText().toString();
                    String dob = editdate.getText().toString();
                    String email = editemail.getText().toString();
                    String altnumber = editaltnumber.getText().toString();
                    String address1 = editphylocation.getText().toString();
                    String state = editpost.getText().toString();
                    String agentmsisdn = editagent.getText().toString();


                    Intent a = new Intent(SimRegInfo.this, Activate_Sim.class);
                    a.putExtra("globalsimid", globalsimid);
                    a.putExtra("globalMode",globalMode);
                    a.putExtra("fname", fname);
                    a.putExtra("mname", mname);
                    a.putExtra("lname", lname);
                    a.putExtra("msisdn", msisdn);
                    a.putExtra("idType", idType);
                    a.putExtra("idNumber", idNumber);
                    a.putExtra("dob", dob);
                    a.putExtra("email", email);
                    a.putExtra("gender", gender);
                    a.putExtra("altnumber", altnumber);
                    a.putExtra("address1", address1);
                    a.putExtra("state", state);
                    a.putExtra("agentmsisdn", agentmsisdn);
                    a.putExtra("mainstatus", sp.getSelectedItem().toString().toString());

                    startActivity(a);
                }
            });

            //submit OFF LINE no internet
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SIGN==null || FRONT==null || BACK==null || CLIENT==null)  {
                        Toast.makeText(SimRegInfo.this, "Must Have Signature and Photos", Toast.LENGTH_LONG).show();
                    } else {
                        String dY[] = editdate.getText().toString().split("-");
                        fb = getAge(Integer.parseInt(dY[2]), Integer.parseInt(dY[1]), Integer.parseInt(dY[0]));
                        if (editfname.getText().toString().matches("") || editmname.getText().toString().matches("") || editlname.getText().toString().matches("") || editmobile.getText().toString().matches("") || editaltnumber.getText().toString().matches("") || editemail.getText().toString().matches("") || editphylocation.getText().toString().matches("") || editpost.getText().toString().matches("") || fb < 18 || !checkBox.isChecked() || SIGN == null || FRONT == null || BACK == null || fb == 0 || editdate.getText().toString() == null || CLIENT==null) {
                            if (editfname.getText().toString().matches("")) {
                                editfname.setError("Empty Field");
                            }
                            if (editmname.getText().toString().matches("")) {
                                editmname.setError("Empty Field");
                            }
                            if (editlname.getText().toString().matches("")) {
                                editlname.setError("Empty Field");
                            }
                            if (editmobile.getText().toString().matches("")) {
                                editmobile.setError("Empty Field");
                            }

                            if (editaltnumber.getText().toString().matches("")) {
                                editaltnumber.setError("Empty Field");
                            }
                            if (editemail.getText().toString().matches("")) {
                                editemail.setError("Empty Field");
                            }
                            if (editphylocation.getText().toString().matches("")) {
                                editphylocation.setError("Empty Field");
                            }
                            if (editpost.getText().toString().matches("")) {
                                editpost.setError("Empty Field");
                            }
                            if (editmobile.getText().toString().matches("")) {
                                editmobile.setError("Empty Field");
                            }


                            if (fb < 18 && fb >= 0) {
                                editdate.setError("Under Age");
                            } else if (fb >= 18) {
                                editdate.setError(null);
                            }
                            if (!checkBox.isChecked()) {
                                Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (editmobile.getText().toString().startsWith("0") ) {
                                editmobile.setError("Remove the 0 at the beginning");
                                return;
                            }
                            if(editmobile.getText().toString().length() == 10) {
                                editmobile.setError("Client number must be 9 numbers and not start with 0");
                                return;
                            }

                            if (!editemail.getText().toString().matches(emailpattern) && !editemail.getText().toString().matches(emailpattern1)) {
                                editemail.setError("Invalid email address");
                                Toast.makeText(getApplicationContext(),"Check your email",Toast.LENGTH_LONG).show();
                                return ;
                            }

                            //start saving
                            new AlertDialog.Builder(SimRegInfo.this)
                                    .setTitle("Submit")
                                    .setMessage("Are you sure you want to Submit this form?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (male.isChecked()) {
                                                gender = "Male";
                                            }
                                            if (female.isChecked()) {
                                                gender = "Female";
                                            }
                                            if (kenya.isChecked()) {
                                                nationality = "Kenyan";
                                            }
                                            if (foreign.isChecked()) {
                                                nationality = "Foreign";
                                            }
                                            String b = "New";

                                            try {
                                                ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);
                                                File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                                dir.mkdirs();
                                                String fileName = "SIM_" + editmobile.getText().toString() + ".txt";
                                                File file = new File(dir, fileName);
                                                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                                BufferedWriter bw = new BufferedWriter(fw);
                                                bw.write(editfname.getText().toString() + "\n");
                                                bw.write(editmname.getText().toString() + "\n");
                                                bw.write(editfname.getText().toString() + "\n");
                                                bw.write(editmobile.getText().toString() + "\n");
                                                bw.write(editdate.getText().toString() + "\n");
                                                bw.write(nationality.toString() + "\n");
                                                bw.write(editaltnumber.getText().toString() + "\n");
                                                bw.write(editemail.getText().toString() + "\n");
                                                bw.write(editphylocation.getText().toString() + "\n");
                                                bw.write(editpost.getText().toString() + "\n");
                                                bw.write(gender.toString() + "\n");
                                                bw.write(editagent.getText().toString() + "\n");
                                                bw.write(editidagent.getText().toString() + "\n");
                                                bw.write(SIGN.toString() + "\n");
                                                bw.write(FRONT.toString() + "\n");
                                                bw.write(BACK.toString() + "\n");
                                                bw.write(CLIENT.toString() + "\n");
                                                bw.write(b + "\n");
                                                bw.write(editussdstatus.getText().toString());
                                                bw.close();
                                                BtnModesave.setBackgroundColor(Color.GREEN);
                                                txtmodesave.setText("Saved");
                                                // to refresh pade and keep our current file
                                                //////count++;
                                                ////////////////////
                                                Button BtnData = (Button) findViewById(R.id.BtnData);

                                                File dir1 = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                                File[] files = dir1.listFiles();
                                                count = 0;
                                                for (File f : files) {
                                                    String name = f.getName();
                                                    if (name.startsWith("SIM") && name.endsWith(".txt"))
                                                        count++;
                                                    System.out.println("COUNT IS:" + count);
                                                }
                                                ////////
                                                if (count != 0) {
                                                    BtnData.setVisibility(View.VISIBLE); //SHOW the button
                                                    BtnData.setText(String.valueOf(count));
                                                }

                                                //Toast.makeText(SimRegInfo.this, fileName + " is saved to\n" + dir, Toast.LENGTH_SHORT).show();
                                                //Intent intent = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                                                //startActivity(intent);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(SimRegInfo.this,"Saving Offline",Toast.LENGTH_SHORT).show();
                                            //startActivity(getIntent());
                                        }


                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }


                    }
                }
            });

            //BtnData appear in case offline files exist
            //offline
            Button BtnData = (Button) findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                    i.putExtra("globalMode",globalMode);
                    i.putExtra("db-offline-to-main",stroffile);
                    i.putExtra("agentNumber",agentNumber);
                    startActivity(i);
                }
            });

            if (count >= 40) {
                submit.setEnabled(false);
                Toast.makeText(SimRegInfo.this, "You Already Have 40 files been submitted", Toast.LENGTH_SHORT).show();
            }
            if (count != 0) {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }

            Btnftp = findViewById(R.id.Btnftp);
            Btnftp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

    }
   /* public void SendTOftp() {

        File myFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SIGN + ".jpg");
        String signpath = String.valueOf(myFile);
        String signname = String.valueOf(textS.getText() + ".jpg");


        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ftpClient.connect(server, port);
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

                // login using username & password
                boolean status = ftpClient.login(user, pass);
                Toast.makeText(SimRegInfo.this,"Connected Successfully to FTP",Toast.LENGTH_LONG).show();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                String workingDir = ftpClient.printWorkingDirectory();
                System.out.println("OUR PWD IS " + workingDir);
                ftpClient.changeWorkingDirectory(workingDir + "Sim");

                //return true if the directory found
                System.out.println(ftpClient.changeWorkingDirectory(workingDir + "Sim"));
                //System.out.println(ftpClient.changeWorkingDirectory (workingDir + "Sim"+workingDir+"REG_2021_211"));


                ftpClient.makeDirectory(globalsimid);
                ftpClient.changeWorkingDirectory(workingDir + "Sim" + "/" + globalsimid);
                // check if the directory correctly created
                System.out.println(ftpClient.changeWorkingDirectory(workingDir + "Sim" + "/" + globalsimid));
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                workingDir = ftpClient.printWorkingDirectory();
                //PathSignFTP = workingDir + "/" + SIGN + ".jpg";
                //PathFrontFTP = workingDir + "/" + FRONT + ".jpg";
                // PathBackFTP = workingDir + "/" + BACK + ".jpg";
                System.out.println("Directory: " + workingDir);
                // upload file
                try {
                    Toast.makeText(SimRegInfo.this,signpath,Toast.LENGTH_LONG).show();
                    FileInputStream srcFileStream = new FileInputStream(signpath);
                    ftpClient.storeFile(signname, srcFileStream);


                    srcFileStream.close();
                    //srcFileStream1.close();
                    //srcFileStream2.close();

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

    }

    private void SignImageStatus() {

        signimgIcon = findViewById(R.id.signimgIcon);
        if (textS.getText().toString() != "") {

            String SIGNPATH = "Sim" + "/" + globalsimid +"/" + SIGN+ ".jpg";
            FTP ftp =new FTP();
            server = ftp.getServer().toString();
            port=ftp.getPort();
            user=ftp.getUser().toString();
            pass=ftp.getPass().toString();

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ( ).permitAll ( ).build ( );
                StrictMode.setThreadPolicy (policy);
                ftpClient.connect (server, port);
                if (FTPReply.isPositiveCompletion (ftpClient.getReplyCode ( ))) {
                    // login using username & password
                    boolean success = ftpClient.login(user, pass);
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    InputStream inStream = ftpClient.retrieveFileStream(SIGNPATH);
                    if(inStream != null) {
                        signimgIcon.setVisibility(View.VISIBLE);
                        signimgIcon.setBackgroundResource(0);
                        signimgIcon.setColorFilter(Color.GREEN);
                       signimgIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SimRegInfo.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
                                builder.setPositiveButton("BACK", null);
                                ImageView imageView = dialogLayout.findViewById(R.id.imageView);

                                Bitmap bitmap = null;
                                bitmap = BitmapFactory.decodeStream(inStream);
                                imageView.setImageBitmap(bitmap);
                                builder.setView(dialogLayout);
                                builder.show();
                            }
                        });

                        File signsave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SIGN + ".jpg");
                        signsave.delete();

                    } else {
                        signimgIcon.setVisibility(View.VISIBLE);
                        signimgIcon.setBackgroundResource(0);
                        signimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                        signimgIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(SimRegInfo.this, "Failed In Sending image to FTP", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            } catch (IOException e) {
                Toast.makeText (getApplicationContext(),e.toString (),Toast.LENGTH_SHORT).show ();
                e.printStackTrace ( );
            }

        }

    }*/

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


    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                ChannelSftp channelSftp=null;
                Session session=null;
                //Toast.makeText(SimTest.this,"Trying to connect..",Toast.LENGTH_LONG).show();
                System.out.println("Start");
                System.out.println("globalsimid = "+globalsimid);
                if(e!=0) {
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking", "no");
                    JSch jSch = new JSch();
                    session = jSch.getSession(user, host, e);
                    System.out.println("Step1");
                    session.setPassword(pass);
                    session.setConfig(config);
                    session.connect();
                    System.out.println("Step Connect");
                    channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();
                    channelSftp.cd(clientSFTPpath);
                }
                //check if the global status if equals zero do it
                if(gsigstatus.equalsIgnoreCase("0")) {
                    if(e!=0) {
                        File signpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SIGN + ".jpg");
                        String sign = String.valueOf(signpic);
                        channelSftp.put(sign, "ClientPic");
                        Boolean success1 = true;

                        if (success1) {
                            //txtmsg.setText("upload completed : " + sign);
                            System.out.println("upload completed : " + sign);
                            UpdateSimRegistrationPicStatus(globalsimid, "SIGNATURE_STATUS",1);
                            signimgIcon.setColorFilter(Color.GREEN);
                            gsigstatus = "1";
                            SIGNorigin = SIGN;
                            System.out.println("gsigstatus " + gsigstatus);
                            System.out.println("SIGNorigin " + SIGNorigin);
                            signpic.delete();
                        }
                    }
                    else {
                        UpdateSimRegistrationPicStatus(globalsimid, "SIGNATURE_STATUS",0);
                    }
                }
                progressBar.setProgress(42);
                if(gfrontstatus.equalsIgnoreCase("0")){
                    if(e!=0) {
                        File frontpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FRONT + ".jpg");
                        String front = String.valueOf(frontpic);
                        channelSftp.put(front, "ClientPic");
                        Boolean success2 = true;

                        if (success2) {
                            //txtmsg.setText("upload completed : " + front);
                            System.out.println("upload completed : " + front);
                            UpdateSimRegistrationPicStatus(globalsimid, "FRONT_SIDE_ID_STATUS",1);
                            frontimgIcon.setColorFilter(Color.GREEN);
                            gfrontstatus = "1";
                            FRONTorigin = FRONT;
                            frontpic.delete();
                        }
                    }
                    else {
                        UpdateSimRegistrationPicStatus(globalsimid, "FRONT_SIDE_ID_STATUS",0);
                    }
                }
                progressBar.setProgress(45);
                if(gbackstatus.equalsIgnoreCase("0")) {
                    if(e!=0) {
                        File backpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
                        String back = String.valueOf(backpic);
                        channelSftp.put(back, "ClientPic");
                        Boolean success3 = true;

                        if (success3) {
                            //txtmsg.setText("upload completed : " + back);
                            System.out.println("upload completed : " + back);
                            UpdateSimRegistrationPicStatus(globalsimid, "BACK_SIDE_ID_STATUS",1);
                            backimgIcon.setColorFilter(Color.GREEN);
                            gbackstatus = "1";
                            BACKorigin = BACK;
                            backpic.delete();
                        }
                    }
                    else {
                        UpdateSimRegistrationPicStatus(globalsimid, "BACK_SIDE_ID_STATUS",0);
                    }
                }
                progressBar.setProgress(47);
                if(gclientstatus.equalsIgnoreCase("0")) {
                    if (e != 0) {
                        File clientpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), CLIENT + ".jpg");
                        String client = String.valueOf(clientpic);
                        channelSftp.put(client, "ClientPic");
                        Boolean success4 = true;

                        if (success4) {
                            //txtmsg.setText("upload completed : " + client);
                            System.out.println("upload completed : " + client);
                            UpdateSimRegistrationPicStatus(globalsimid, "CLIENT_PHOTO_STATUS", 1);
                            clientimgIcon.setColorFilter(Color.GREEN);
                            gclientstatus = "1";
                            CLIENTorigin = CLIENT;
                            clientpic.delete();
                        }
                    } else {
                        UpdateSimRegistrationPicStatus(globalsimid, "CLIENT_PHOTO_STATUS", 0);
                    }
                }
                if(e!=0) {
                    channelSftp.disconnect();
                    session.disconnect();
                }
                progressBar.setProgress(100);
                txtmsg.setText("Transaction completed");
                Thread.sleep(2000);
                txtmsg.setText("");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });

    public void UpdateSimRegistrationPicStatus(String vsimregid,String vcolname,int val1)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    System.out.println("update CLIENTS set " + vcolname + "="+ val1 +"  where CLIENT_ID ='" + vsimregid + "'");
                    stmtinsert1 = conn.prepareStatement("update CLIENTS set " + vcolname + "="+ val1 +"  where CLIENT_ID ='" + vsimregid + "'");
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



    public void DeleteSimRegistrationPicStatus(String vsimregid,String vcolname)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    stmtinsert1 = conn.prepareStatement("update CLIENTS  set " + vcolname + "=-1  where CLIENT_ID  ='" + vsimregid + "'");
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

    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {


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
                String fullpath =clientSFTPpath+"ClientPic";
                channelSftp.cd(fullpath);
                String path=channelSftp.pwd();

                //check if the global status if equals zero do it
                if(gsigstatus.equalsIgnoreCase("1")) {

                    channelSftp.rm(path+"/"+SIGN+".jpg");
                    Boolean success1 = true;

                    if (success1) {

                        DeleteSimRegistrationPicStatus(globalsimid, "SIGNATURE_STATUS");
                        signimgIcon.setVisibility(View.INVISIBLE);
                    }
                }

                if(gfrontstatus.equalsIgnoreCase("1")){

                    channelSftp.rm(path+"/"+FRONT+".jpg");
                    Boolean success2 = true;
                    if(success2){
                        DeleteSimRegistrationPicStatus(globalsimid,"FRONT_SIDE_ID_STATUS");
                        frontimgIcon.setVisibility(View.INVISIBLE);
                    }
                }
                if(gbackstatus.equalsIgnoreCase("1")) {


                    channelSftp.rm(path+"/"+BACK+".jpg");
                    Boolean success3 = true;
                    if(success3){
                        DeleteSimRegistrationPicStatus(globalsimid,"BACK_SIDE_ID_STATUS");
                        backimgIcon.setVisibility(View.INVISIBLE);
                    }
                }

                if(gclientstatus.equalsIgnoreCase("1")) {


                    channelSftp.rm(path+"/"+CLIENT+".jpg");
                    Boolean success4 = true;
                    if(success4){
                        DeleteSimRegistrationPicStatus(globalsimid,"CLIENT_PHOTO_STATUS");
                        clientimgIcon.setVisibility(View.INVISIBLE);
                    }
                }

                //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                channelSftp.disconnect();
                session.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });


    private Integer getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();


        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if ((today.get(Calendar.DAY_OF_YEAR) + 30) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        Integer ageInt = new Integer(age);


        return ageInt;
    }


    public void getDataforSimfromDB() {

        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    // if it is a new Warehouse we will use insert

                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "select FIRST_NAME,MIDDLE_NAME,LAST_NAME,STATUS,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHYSICAL_LOCATION,POSTAL_ADDRESS,GENDER,CLIENT_ID_NUMBER,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS,CLIENT_PHOTO,CLIENT_PHOTO_STATUS,USSD_STATUS,LONGITUDE,LATITUDE,SELLING_LONGITUDE,SELLING_LATITUDE FROM CLIENTS where CLIENT_ID = '" + globalsimid + "'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            FilldatafromDataBase(true,rs1.getString("FIRST_NAME"),rs1.getString("MIDDLE_NAME"),rs1.getString("LAST_NAME"),rs1.getString("MOBILE_NUMBER"),rs1.getString("DATE_OF_BIRTH").substring(8, 10) + "-" + rs1.getString("DATE_OF_BIRTH").substring(5, 7) + "-" + rs1.getString("DATE_OF_BIRTH").substring(0, 4),rs1.getString("NATIONALITY"),rs1.getString("ALTERNATIVE_NUMBER"),rs1.getString("EMAIL_ADDRESS"),rs1.getString("PHYSICAL_LOCATION"),rs1.getString("POSTAL_ADDRESS"),rs1.getString("GENDER"),rs1.getString("STATUS"),rs1.getString("CLIENT_ID_NUMBER"),rs1.getString("ID_FRONT_SIDE_PHOTO"),rs1.getString("ID_BACK_SID_PHOTO"),rs1.getString("SIGNATURE"),rs1.getString("SIGNATURE_STATUS"),rs1.getString("FRONT_SIDE_ID_STATUS"),rs1.getString("BACK_SIDE_ID_STATUS"),rs1.getString("CLIENT_PHOTO"),rs1.getString("CLIENT_PHOTO_STATUS"),rs1.getString("USSD_STATUS"),rs1.getString("LONGITUDE"),rs1.getString("LATITUDE"),rs1.getString("SELLING_LONGITUDE"),rs1.getString("SELLING_LATITUDE"));

                            //System.out.println(rs1.getString("compteur"));

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    rs1.close();
                    stmt1.close();


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Thread threadload = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                getDataforSimfromDB();

            }catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    });

    Thread threadload1 = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean flg = false;
            try {
                txtmsg.setText("Please wait ...");
                if ((flg = connecttoDB()) == true) {
                    if (getAgentStatus()==true) {
                        PreparedStatement stmtinsert1 = null;
                        try {
                            if (globalsimid.equalsIgnoreCase("0") || OfflineFile.exists()) {
                                // if it is a new Warehouse we will use insert

                                progressBar.setProgress(10);
                                Statement stmt1 = null;
                                stmt1 = conn.createStatement();
                                String sqlStmt = "select CLIENTS_SEQ.nextval as nbr from dual";
                                ResultSet rs1 = null;
                                try {
                                    rs1 = stmt1.executeQuery(sqlStmt);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                while (true) {
                                    try {
                                        if (!rs1.next()) break;
                                        globalsimid = simID + rs1.getString("nbr");
                                        //System.out.println(rs1.getString("compteur"));

                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                                rs1.close();
                                stmt1.close();


                                // send data from fragment to super activity
                                String ussd_status = "USSD";
                                stmtinsert1 = conn.prepareStatement("insert into CLIENTS (CLIENT_ID,DISPLAY_NAME,CREATED_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHYSICAL_LOCATION,STATUS,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,CLIENT_ID_NUMBER,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,CLIENT_PHOTO,SIGNATURE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS,CLIENT_PHOTO_STATUS,USSD_STATUS,DEPARTMENT,DESCREPTION,LONGITUDE,LATITUDE,SELLING_LONGITUDE,SELLING_LATITUDE) values " +
                                        "('" + globalsimid + "',0,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + b + "','" + editpost.getText() + "','" + gender + "','" + agentNumber + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "','" + CLIENT + "',0,0,0,0,'" + editussdstatus.getText().toString() + "',0,0,'"+edtlong.getText().toString()+"','"+edtlat.getText().toString()+"','"+edtselllong.getText().toString()+"','"+edtselllat.getText().toString()+"')");
                            } else {
                                stmtinsert1 = conn.prepareStatement("update CLIENTS set LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + editfname.getText() + "',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() + "',STATUS='" + b + "',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='" + editaltnumber.getText() + "',EMAIL_ADDRESS='" + editemail.getText() + "',PHYSICAL_LOCATION='" + editphylocation.getText() + "',POSTAL_ADDRESS='" + editpost.getText() + "',GENDER='" + gender + "',AGENT_NUMBER='" + editagent.getText() + "',CLIENT_ID_NUMBER='" + editidagent.getText() + "',SIGNATURE='" + SIGN + "',ID_FRONT_SIDE_PHOTO='" + FRONT + "',ID_BACK_SID_PHOTO='" + BACK + "',USSD_STATUS='" + editussdstatus.getText().toString() + "',CLIENT_PHOTO='" + CLIENT + "', LONGITUDE='"+edtlong.getText().toString()+"',LATITUDE='"+edtlat.getText().toString()+"',SELLING_LONGITUDE='"+edtselllong.getText().toString()+"',SELLING_LATITUDE='"+edtselllat.getText().toString()+"' where CLIENT_ID  ='" + globalsimid + "'");
                            }
                            txtmsg.setText("DB Data Saving completed");
                            progressBar.setProgress(30);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            stmtinsert1.executeUpdate();
                            OfflineFile.delete();
                            progressBar.setProgress(40);

                            txtmsg.setText("Start upload pictures");
                            //calling to upload pictures  using stfp
                            if (globalsimid.equalsIgnoreCase("0")) {
                                //Toast.makeText(SimRegInfo.this, "New SIM Uploading Photos started", Toast.LENGTH_LONG).show();
                                thread1.start();
                                // Toast.makeText(SimRegInfo.this, "New SIM Upload Completed", Toast.LENGTH_LONG).show();
                            } else {
                                if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0") || gclientstatus.equalsIgnoreCase("0")) {
                                    //Toast.makeText(SimRegInfo.this, "Exist SIM Uploading Photos started", Toast.LENGTH_LONG).show();
                                    thread1.start();
                                    // Toast.makeText(SimRegInfo.this, "Exist SIM Upload Completed", Toast.LENGTH_LONG).show();
                                }

                            }


                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }


                        try {
                            stmtinsert1.close();
                            conn.close();
                            if (!gsigstatus.equalsIgnoreCase("0") && !gfrontstatus.equalsIgnoreCase("0") && !gbackstatus.equalsIgnoreCase("0") && !gclientstatus.equalsIgnoreCase("0")) {
                                progressBar.setProgress(100);
                                txtmsg.setText("Transaction completed");
                                Thread.sleep(2000);
                                txtmsg.setText("");
                                progressBar.setProgress(0);
                                Intent intent =  new Intent(getApplicationContext(), SimRegInfo.class);
                                intent.putExtra("message_key", globalsimid);
                                intent.putExtra("db-offline", "1");
                                intent.putExtra("globalMode","Online");
                                intent.putExtra("db-offline-to-main","1");
                                intent.putExtra("agentNumber",agentNumber);
                                startActivity(intent);
                            } else {

                                //recall form
                                Thread.sleep(20000);
                                Intent intent = new Intent(getApplicationContext(), SimRegInfo.class);
                                intent.putExtra("message_key", globalsimid);
                                intent.putExtra("db-offline", "1");
                                intent.putExtra("globalMode", "Online");
                                intent.putExtra("db-offline-to-main", "1");
                                intent.putExtra("agentNumber", agentNumber);
                                startActivity(intent);
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                    else {
                        txtmsg.setText("Access denied");
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
                else {
                    //Saving OFFLINE when no connection to DB  TURKIEH
                    System.out.println ("SAVE WHEN DB not reachable");

                    try {
                        txtmsg.setText("DB not reachable start saving offmode");
                        ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);
                        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        dir.mkdirs();
                        String fileName = "SIM_" + editmobile.getText().toString() + ".txt";
                        File file = new File(dir, fileName);
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(editfname.getText().toString() + "\n");
                        bw.write(editmname.getText().toString() + "\n");
                        bw.write(editlname.getText().toString() + "\n");
                        bw.write(editmobile.getText().toString() + "\n");
                        bw.write(editdate.getText().toString() + "\n");
                        bw.write(nationality.toString() + "\n");
                        bw.write(editaltnumber.getText().toString() + "\n");
                        bw.write(editemail.getText().toString() + "\n");
                        bw.write(editphylocation.getText().toString() + "\n");
                        bw.write(editpost.getText().toString() + "\n");
                        bw.write(gender.toString() + "\n");
                        bw.write(editagent.getText().toString() + "\n");
                        bw.write(editidagent.getText().toString() + "\n");
                        bw.write(SIGN.toString() + "\n");
                        bw.write(FRONT.toString() + "\n");
                        bw.write(BACK.toString() + "\n");
                        bw.write(CLIENT.toString() + "\n");
                        bw.write(b + "\n");
                        bw.write(editussdstatus.getText().toString());
                        bw.close();
                        txtmsg.setText("Saving offmode completed");
                        Thread.sleep(500);
                        txtmsg.setText("");
                        // to call to add count files
                        threadsave.start();
                        /*
                        // to refresh pade and keep our current file
                        count++;
                        Button BtnData = (Button) findViewById(R.id.BtnData);
                        if (count != 0) {
                            BtnData.setVisibility(View.VISIBLE); //SHOW the button
                            BtnData.setText(String.valueOf(count));
                        }
                        BtnModedata.setBackgroundColor(Color.GRAY);
                        */
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //startActivity(getIntent());
                    System.out.println ("SAVE COMPLETED  WHEN DB not reachable");



                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });




    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog= new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();

    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String vday=null,vmonth=null;
        if ((dayOfMonth) <10 ) {
            vday="0"+(dayOfMonth);
        }else {
            vday= String.valueOf((dayOfMonth));
        }

        if ((month+1) <10 ) {
            vmonth="0"+(month+1);
        }else {
            vmonth= String.valueOf((month+1));
        }

        String date= vday+"-"+vmonth+"-"+year;
        if (date.equalsIgnoreCase(editdate.getText().toString())) {

        }else {
            editdate.setText(date);
            BtnModesave.setBackgroundColor(Color.rgb(255, 102, 0));
            txtmodesave.setText("Not Saved");
        }
    }
    //not in use
    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            try {
                getDataforSimfromDB();
            } catch(Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public void FilldatafromDataBase(Boolean valeur,String firstname,String middlename,String lastname,String mobilenuber,String dob,String nation,String altnumber,String emailadrs,String phylocation,String postaladr,String vgender,String stat, String agentid,String txtf, String txtb, String txts,String gsigstat,String gfrontstat,String gbackstat,String txtc,String gclientstat,String ussdstatus,String longt,String lat,String selllong,String selllat  ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkBox.setChecked(valeur);
                editfname.setText(firstname);
                editmname.setText(middlename);
                editlname.setText(lastname);
                editmobile.setText(mobilenuber);
                editdate.setText(dob);
                edtlong.setText(longt);
                edtlat.setText(lat);
                edtselllong.setText(selllong);
                edtselllat.setText(selllat);
                if (nation.matches("Foreign")) {
                    foreign.setChecked(true);
                    kenya.setChecked(false);
                } else {
                    kenya.setChecked(true);
                    foreign.setChecked(false);
                }

                editaltnumber.setText(altnumber);
                editemail.setText(emailadrs);
                editphylocation.setText(phylocation);
                editpost.setText(postaladr);
                editussdstatus.setText(ussdstatus);

                if (vgender.matches("Male")) {
                    male.setChecked(true);
                    female.setChecked(false);
                } else {
                    female.setChecked(true);
                    male.setChecked(false);
                }

                if (stat.matches("In Progress")) {
                    sp.setSelection(1);
                }

                if (stat.matches("Success")) {
                    sp.setSelection(2);
                    ///freeze items when staus ==success
                    checkBox.setEnabled(false);
                    editfname.setEnabled(false);
                    editmname.setEnabled(false);
                    editlname.setEnabled(false);
                    editmobile.setEnabled(false);
                    editdate.setEnabled(false);
                    foreign.setEnabled(false);
                    kenya.setEnabled(false);
                    editaltnumber.setEnabled(false);
                    editemail.setEnabled(false);
                    editphylocation.setEnabled(false);
                    editpost.setEnabled(false);
                    editussdstatus.setEnabled(false);
                    male.setEnabled(false);
                    female.setEnabled(false);
                    editidagent.setEnabled(false);
                    btndob.setEnabled(false);
                    frontid.setEnabled(false);
                    backid.setEnabled(false);
                    sign.setEnabled(false);
                    btnclientimg.setEnabled(false);
                    checkSim.setEnabled(false);
                    Button BtnData=findViewById(R.id.BtnData);
                    BtnData.setEnabled(false);
                    BtnDelete.setEnabled(false);
                }
                if (stat.matches("Failed")) {
                    sp.setSelection(3);
                }

                editidagent.setText(agentid);
                textF.setText(txtf);
                textB.setText(txtb);
                textS.setText(txts);
                textC.setText(txtc);
                SIGNorigin =SIGN= txts;
                FRONTorigin =FRONT= txtf;
                BACKorigin =BACK= txtb;
                CLIENTorigin =CLIENT= txtc;
                gsigstatusorigin =gsigstatus= gsigstat;
                gfrontstatusorigin =gfrontstatus= gfrontstat;
                gbackstatusorigin =gbackstatus = gbackstat;
                gclientstatusorigin =gclientstatus = gclientstat;

                System.out.println(gbackstatus+" "+gfrontstatus+" "+gsigstatus);


                if (gsigstatusorigin.equalsIgnoreCase("1")) {
                    signimgIcon.setVisibility(View.VISIBLE);
                    signimgIcon.setColorFilter(Color.GREEN);
                    signimgIcon.setBackgroundColor(0);
                } else {
                    signimgIcon.setVisibility(View.VISIBLE);
                    signimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    signimgIcon.setBackgroundColor(0);
                }

                if (gfrontstatusorigin.equalsIgnoreCase("1")) {
                    frontimgIcon.setVisibility(View.VISIBLE);
                    frontimgIcon.setColorFilter(Color.GREEN);
                    frontimgIcon.setBackgroundColor(0);
                } else {
                    frontimgIcon.setVisibility(View.VISIBLE);
                    frontimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    frontimgIcon.setBackgroundColor(0);
                }

                if (gbackstatusorigin.equalsIgnoreCase("1")) {
                    backimgIcon.setVisibility(View.VISIBLE);
                    backimgIcon.setColorFilter(Color.GREEN);
                    backimgIcon.setBackgroundColor(0);
                } else {
                    backimgIcon.setVisibility(View.VISIBLE);
                    backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    backimgIcon.setBackgroundColor(0);
                }

                if (gclientstatusorigin.equalsIgnoreCase("1")) {
                    clientimgIcon.setVisibility(View.VISIBLE);
                    clientimgIcon.setColorFilter(Color.GREEN);
                    clientimgIcon.setBackgroundColor(0);
                } else {
                    clientimgIcon.setVisibility(View.VISIBLE);
                    clientimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                    clientimgIcon.setBackgroundColor(0);
                }

                if(editussdstatus.getText().toString().matches("USSD")){
                    editussdstatus.setText("NOT SENT");
                }
                if(editussdstatus.getText().toString().matches("USSD_SENT")){
                    editussdstatus.setText("SENT");
                    txtussd.setVisibility(View.VISIBLE);
                    editussdstatus.setVisibility(View.VISIBLE);
                    ussdview.setVisibility(View.VISIBLE);
                }


            }

        });
    }

    Thread threadsave = new Thread() {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // to refresh pade and keep our current file
                    count++;
                    Button BtnData = (Button) findViewById(R.id.BtnData);
                    if (count != 0) {
                        BtnData.setVisibility(View.VISIBLE); //SHOW the button
                        BtnData.setText(String.valueOf(count));
                    }
                    BtnModedata.setBackgroundColor(Color.GRAY);
                    txtmodedata.setText("Offline Data");
                }
            });
        }
    };


    public void getagentnumber() {
        File file = new File(getApplicationContext().getFilesDir(), "MSISDN.txt");
        if(file.exists()){
            StringBuilder text = new StringBuilder();
            String[] data;
            String RegisterResult="";
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
            data=RegisterResult.split(":");
            agentNumber=data[0];
            System.out.println("Bassam Agent is : "+agentNumber);
        }
    }

    public boolean getAgentStatus()
    {
        String Agentstatus = null;
        boolean statusflg = false;

        Statement stmtagent = null;

        try {
            stmtagent = conn.createStatement();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sqlStmtagent = "SELECT STATUS FROM AGENT WHERE MSISDN = '"+ agentNumber+"' ";
        ResultSet rsagent = null;

        try {
            rsagent = stmtagent.executeQuery(sqlStmtagent);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rsagent.next()) break;
                Agentstatus = (rsagent.getString("STATUS"));
                if (Agentstatus.equalsIgnoreCase("Activated")){
                    statusflg = true;
                } else {
                    statusflg = false;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        try {
            rsagent.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            stmtagent.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return statusflg;

    }

}

