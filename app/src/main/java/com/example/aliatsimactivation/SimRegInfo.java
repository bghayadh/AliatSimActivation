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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class SimRegInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String gender = null;
    private int count;
    public Connection conn;
    private String globalsimid,simID;
    private Button submit, frontid, backid,btnlvsimreg;
    private Button sign;
    private File file,OfflineFile;
    private String nationality = "";
    private RadioButton kenya;
    private RadioButton foreign;
    private RadioButton male;
    private RadioButton female;
    private CheckBox checkBox;
    private TextView editdate, editmname;
    private Integer a;
    private long fb = 0;
    private String gsigstatus,gfrontstatus,gbackstatus;
    private String vsimid,vpic,vcol;
    private String s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,b;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText editaltnumber,editemail,editphylocation,editpost;
    private TextView editlname, editfname, textF, textB, textS;
    private String file1 = "MSISDN.txt";
    private String s0, s1, Result;
    private String FRONT, BACK, SIGN = null;
    private TextView editidagent, editagent;
    SFTP sftp = new SFTP();
    private Button Btnftp,BtnDelete,BtnMain,BtnRegandActivate;
    private ImageButton signimgIcon, frontimgIcon, backimgIcon,btndob;
    private String[] imagesource;
    FTP ftp = new FTP();
    String server = ftp.getServer();//"ftp.ipage.com";
    int port = ftp.getPort();//21;
    String user = ftp.getUser();//"beid";
    String pass =ftp.getPass();// "10th@Loop";
    FTPClient ftpClient = new FTPClient();
    private String PathSignFTP, PathFrontFTP, PathBackFTP;
    private TextView editmobile,txttest,txtmsg;
    private String stroffile;
    private Spinner sp;
    private boolean connectflag=false;
    private Drawable ddd;
    private String picsdate; /// variable for picture name
    private String SIGNorigin,SIGNnew,BACKorigin,BACKnew,gsigstatusnew,gsigstatusorigin,gbackstatusnew,gbackstatusorigin,FRONTnew,FRONTorigin,gfrontstatusnew,gfrontstatusorigin;
    private View linesign,linefront,lineback;  //line under discard
    private TextView discardsign,discardfront,discardback; // for discard

    //capture images from cam and save it on the phone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            FRONT = editfname.getText().toString() + editlname.getText().toString() + "_FRONT_" + editmobile.getText().toString() + "_" + editidagent.getText().toString()+"_"+picsdate;

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

        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("message_key " + stroffile.toString());
        Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
        i.putExtra("message_key", stroffile.toString());
        startActivity(i);
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
        stroffile= intent.getStringExtra("db-offline");

        //show arrow
        ImageButton backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SimRegInfo.this,SimRegListViewActivity.class);
                startActivity(i);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Toast.makeText(SimRegInfo.this, "Connected", Toast.LENGTH_SHORT).show();


            if(globalsimid.equalsIgnoreCase("0")){
                gsigstatus="0";
                gfrontstatus="0";
                gbackstatus="0";
            }

            if (globalsimid != "0") {
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


            //check the existence of the msisdn file to get the agent msisnd
            //after checking the existence fill it in the edittext and disable it
            try {
                FileInputStream fis = null;

                File file = new File(getFilesDir(), "MSISDN.txt");
                if (file.exists()) {
                    System.out.println("file Exists");
                    fis = openFileInput("MSISDN.txt");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String text;
                    while ((text = br.readLine()) != null) {
                        sb.append(text).append("\n");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        editagent.setText(text.toString());
                        editagent.setEnabled(false);
                    }


                } else {
                    System.out.println("login filevdon't exist");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

                String Off13 = intent.getStringExtra("offline13");
                sp.setSelection(((ArrayAdapter<String>) sp.getAdapter()).getPosition(Off13));

                String Off14 = intent.getStringExtra("offline14");
                textS.setText(Off14);
                SIGN = Off14;

                String Off15 = intent.getStringExtra("offline15");
                textF.setText(Off15);
                FRONT = Off15;

                String Off16 = intent.getStringExtra("offline16");
                textB.setText(Off16);
                BACK = Off16;

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


            //BtnData appear in case offline files exist
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                    i.putExtra("message_key", globalsimid);
                    startActivity(i);
                }
            });
            //BtnData appear in case offline files exist
            btnlvsimreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                    i.putExtra("message_key", "0");
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
                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
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
                    }
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

            //back id button to capture the id back side
            backid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 101);

                        BACKnew=BACK;

                        if(globalsimid.equalsIgnoreCase("0")) {
                            backimgIcon.setColorFilter(Color.parseColor("#4169E1"));
                            lineback.setVisibility(View.VISIBLE);
                            discardback.setVisibility(View.VISIBLE);


                        }else{
                            backimgIcon.setColorFilter(Color.parseColor("#ffa500"));
                            gbackstatusnew="0";

                        }


                        if (textB.getText().toString() != "") {

                            backimgIcon.setVisibility(View.VISIBLE);
                            backimgIcon.setBackgroundResource(0);
                            gbackstatus =gbackstatusnew;
                            discardback.setVisibility(View.VISIBLE);
                            lineback.setVisibility(View.VISIBLE);

                        }else {
                            backimgIcon.setVisibility(View.INVISIBLE);
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


                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
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



                    }
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

            BtnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    i.putExtra("db-offline-to-main","0");
                    startActivity(i);
                }
            });
            //delete on line
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimRegInfo.this, "Deleting Photos from SFTP", Toast.LENGTH_LONG).show();
                    thread2.start();
                    Toast.makeText(SimRegInfo.this, "The Photos Deleted from SFTP", Toast.LENGTH_LONG).show();

                    boolean flg = false;
                    try {
                        if ((flg = connecttoDB()) == true) {


                            Toast.makeText(SimRegInfo.this, "Starting Deletion in DataBase", Toast.LENGTH_SHORT).show();
                            PreparedStatement stmtinsert1 = null;

                            try {
                                stmtinsert1 = conn.prepareStatement("delete from SIM_REGISTRATION where SIM_REG_ID='" + globalsimid + "'");
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


                }
            });

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
            //submit ON LINE
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //gsigstatus="0";
                    //gfrontstatus="0";
                    //gbackstatus="0";

                    if (sp.getSelectedItem().toString().matches("New")) {
                        b = "In Progress";
                        sp.setSelection(1);
                    }


                    String dY[] = editdate.getText().toString().split("-");
                    fb = getAge(Integer.parseInt(dY[2]), Integer.parseInt(dY[1]), Integer.parseInt(dY[0]));
                    if (SIGN==null || FRONT==null || BACK==null) {
                        Toast.makeText(SimRegInfo.this, "Must Have Signature and Photos", Toast.LENGTH_LONG).show();
                    } else {
                        if (editfname.getText().toString().matches("") || editmname.getText().toString().matches("") || editlname.getText().toString().matches("") || editmobile.getText().toString().matches("") || editaltnumber.getText().toString().matches("") || editemail.getText().toString().matches("") || editphylocation.getText().toString().matches("") || editpost.getText().toString().matches("") || !editemail.getText().toString().matches(emailpattern) || fb < 18 || !checkBox.isChecked() || SIGN == null || FRONT == null || BACK == null || fb == 0 || editdate.getText().toString() == null) {

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
                            if (!editemail.getText().toString().matches(emailpattern)) {
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
                            if (sp.getSelectedItem().toString().matches("New")){
                                b = "In Progress";
                                sp.setSelection(1);
                            }
                        } else {

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
                            if (sp.getSelectedItem().toString().matches("New")){
                                b = "In Progress";sp.setSelection(1);}
                            //start saving
                            new AlertDialog.Builder(SimRegInfo.this)
                                    .setTitle("Submit")
                                    .setMessage("Are you sure you want to Submit this form?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {


                                            Date date = new Date();
                                            Calendar calendar = new GregorianCalendar();
                                            calendar.setTime(date);
                                            int year = calendar.get(Calendar.YEAR);
                                            simID = "REG_" + year + "_";



                                            try {
                                                b = sp.getSelectedItem().toString();
                                                if (sp.getSelectedItem().toString().matches("New")){
                                                    b = "In Progress";sp.setSelection(1);};


                                                //save on line
                                                threadload1.start();


                                                //not in use now
                                                //SavedataSIM();
                                                //Toast.makeText(SimRegInfo.this, "Saving Completed", Toast.LENGTH_SHORT).show();


                                            } catch(Exception e) {
                                                e.printStackTrace();
                                            }

                                            /*
                                            //calling to upload pictures  using stfp
                                            if (globalsimid.equalsIgnoreCase("0")) {
                                                Toast.makeText(SimRegInfo.this, "New SIM Uploading Photos started", Toast.LENGTH_LONG).show();
                                                thread1.start();
                                                Toast.makeText(SimRegInfo.this, "New SIM Upload Completed", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
                                                    Toast.makeText(SimRegInfo.this, "Exist SIM Uploading Photos started", Toast.LENGTH_LONG).show();
                                                    thread1.start();
                                                    Toast.makeText(SimRegInfo.this, "Exist SIM Upload Completed", Toast.LENGTH_LONG).show();
                                                }

                                            } */






                                            // Intent i=new Intent(getApplicationContext(),SimRegListViewActivity.class);
                                            //startActivity(i);

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


            //calling picture from SFTP server
            if(gsigstatus != "0") {
                signimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            SFTP sftp = new SFTP();
                            System.out.println("Start");

                            String user = sftp.getUser();
                            String pass = sftp.getPass();
                            String host = sftp.getServer();
                            int e = sftp.getPort();

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
                            channelSftp.cd("SIMPICSFTP");
                            System.out.println(channelSftp.pwd());
                            String path=channelSftp.pwd()+"/";
                            System.out.println("u are here");
                            if(channelSftp.lstat(path+SIGNorigin+".jpg")!=null){
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path+SIGNorigin+".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis,"ddd");
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

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            if(gfrontstatus != "0") {
                frontimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            SFTP sftp = new SFTP();
                            System.out.println("Start");

                            String user = sftp.getUser();
                            String pass = sftp.getPass();
                            String host = sftp.getServer();
                            int e = sftp.getPort();

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
                            channelSftp.cd("SIMPICSFTP");
                            System.out.println(channelSftp.pwd());
                            String path=channelSftp.pwd()+"/";
                            System.out.println("u are here");
                            if(channelSftp.lstat(path+FRONTorigin+".jpg")!=null){
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path+FRONTorigin+".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis,"ddd");
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

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            if(gbackstatus != "0") {
                backimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            SFTP sftp = new SFTP();
                            System.out.println("Start");

                            String user = sftp.getUser();
                            String pass = sftp.getPass();
                            String host = sftp.getServer();
                            int e = sftp.getPort();

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
                            channelSftp.cd("SIMPICSFTP");
                            System.out.println(channelSftp.pwd());
                            String path=channelSftp.pwd()+"/";
                            System.out.println("u are here");
                            if(channelSftp.lstat(path+BACKorigin+".jpg")!=null){
                                System.out.println("Existed");
                            }
                            byte[] buffer = new byte[1024];
                            System.out.println(buffer);
                            BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path+BACKorigin+".jpg"));
                            System.out.println(bis);
                            ddd = Drawable.createFromStream(bis,"ddd");
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

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            ////////end load picture from sftp





            Btnftp = findViewById(R.id.Btnftp);
            Btnftp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //resend picture if not sent to sftp server
                    if (globalsimid != "0") {
                        if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
                            Toast.makeText(SimRegInfo.this, "Uploading Photos started", Toast.LENGTH_LONG).show();
                            System.out.println("UPDATE HERE");
                            thread1.start();
                            Toast.makeText(SimRegInfo.this, "Upload Completed", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
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
            editdate.setText((df.format(c1)).toString());



            Intent intent1 = SimRegInfo.this.getIntent();
            String str1 = intent.getStringExtra("message_key");
            System.out.println("str1 : "+str1);
            globalsimid = str1.toString();

            gsigstatus="0";
            gfrontstatus="0";
            gbackstatus="0";


            btndob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });
            frontid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 100);
                    }
                }
            });

            //back id button to capture the id back side
            backid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 101);
                    }
                }
            });


            //signature button to capture the signature of the client
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editfname.getText().toString().matches("") || editlname.getText().toString().matches("") || editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                        SIGN = editfname.getText().toString() + editlname.getText().toString() + "_SIGNATURE_" + editagent.getText().toString() + "_" + editidagent.getText().toString();
                        a.putExtra("sign", SIGN);
                        startActivity(a);


                        textS.setText(SIGN);

                        if (textS.getText().toString() != "") {
                            signimgIcon.setVisibility(View.VISIBLE);
                            signimgIcon.setBackgroundResource(0);
                            gsigstatus = "0";
                        }

                    }
                }
            });





            try {
                FileInputStream fis = null;

                File file = new File(getFilesDir(), "MSISDN.txt");
                if (file.exists()) {
                    System.out.println("file Exists");
                    fis = openFileInput("MSISDN.txt");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String text;
                    while ((text = br.readLine()) != null) {
                        sb.append(text).append("\n");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        editagent.setText(text.toString());
                        editagent.setEnabled(false);
                    }


                } else {
                    System.out.println("login filevdon't exist");
                }
            } catch (Exception e) {
                e.printStackTrace();
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

                String Off13 = intent.getStringExtra("offline13");
                sp.setSelection(((ArrayAdapter<String>) sp.getAdapter()).getPosition(Off13));

                String Off14 = intent.getStringExtra("offline14");
                textS.setText(Off14);
                SIGN = Off14;

                String Off15 = intent.getStringExtra("offline15");
                textF.setText(Off15);
                FRONT = Off15;

                String Off16 = intent.getStringExtra("offline16");
                textB.setText(Off16);
                BACK = Off16;

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

            }

            BtnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    i.putExtra("db-offline-to-main","0");
                    startActivity(i);
                }
            });
            // delete in offline mode
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String myFileName = "SIM_" + editmobile.getText().toString() + ".txt";
                    File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    OfflineFile = new File(directory,myFileName);
                    OfflineFile.delete();
                    Toast.makeText(SimRegInfo.this,"Offline File Deleted Successfully",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                }
            });


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
                    startActivity(a);
                }
            });
           //submit OFF LINE no internet
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SIGN==null || FRONT==null || BACK==null) {
                        Toast.makeText(SimRegInfo.this, "Must Have Signature and Photos", Toast.LENGTH_LONG).show();
                    } else {
                        String dY[] = editdate.getText().toString().split("-");
                        fb = getAge(Integer.parseInt(dY[2]), Integer.parseInt(dY[1]), Integer.parseInt(dY[0]));
                        if (editfname.getText().toString().matches("") || editmname.getText().toString().matches("") || editlname.getText().toString().matches("") || editmobile.getText().toString().matches("") || editaltnumber.getText().toString().matches("") || editemail.getText().toString().matches("") || editphylocation.getText().toString().matches("") || editpost.getText().toString().matches("") || !editemail.getText().toString().matches(emailpattern) || fb < 18 || !checkBox.isChecked() || SIGN == null || FRONT == null || BACK == null || fb == 0 || editdate.getText().toString() == null) {

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
                            if (!editemail.getText().toString().matches(emailpattern)) {
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
                        } else {
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
                                                bw.write(b);
                                                bw.close();

                                                //Toast.makeText(SimRegInfo.this, fileName + " is saved to\n" + dir, Toast.LENGTH_SHORT).show();
                                                //Intent intent = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                                                //startActivity(intent);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(SimRegInfo.this,"Saving Offline",Toast.LENGTH_SHORT).show();
                                            startActivity(getIntent());
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
            Button BtnData = (Button) findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                    startActivity(i);
                }
            });
            if (count >= 5) {
                submit.setEnabled(false);
                Toast.makeText(SimRegInfo.this, "You Already Have 5 Unsubmitted Files", Toast.LENGTH_SHORT).show();
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
                 //Toast.makeText(SimTest.this,"Trying to connect..",Toast.LENGTH_LONG).show();
                System.out.println("Start");
                System.out.println("globalsimid = "+globalsimid);
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
                //check if the global status if equals zero do it
                if(gsigstatus.equalsIgnoreCase("0")) {

                    File signpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), SIGN + ".jpg");
                    String sign = String.valueOf(signpic);
                    channelSftp.put(sign, "SIMPICSFTP");
                    Boolean success1 = true;

                    if (success1) {
                        txtmsg.setText("upload completed : " + sign);
                        System.out.println("upload completed : " + sign);
                        UpdateSimRegistrationPicStatus(globalsimid, "SIGNATURE_STATUS");
                        signimgIcon.setColorFilter(Color.GREEN);
                        linesign.setVisibility(View.INVISIBLE);
                        discardsign.setVisibility(View.INVISIBLE);
                    }
                }

                if(gfrontstatus.equalsIgnoreCase("0")){

                    File frontpic=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FRONT + ".jpg");
                    String front=String.valueOf(frontpic);
                    channelSftp.put(front, "SIMPICSFTP");
                    Boolean success2 = true;

                    if(success2){
                        txtmsg.setText("upload completed : " + front);
                        System.out.println("upload completed : "+front);
                        UpdateSimRegistrationPicStatus(globalsimid,"FRONT_SIDE_ID_STATUS");
                        frontimgIcon.setColorFilter(Color.GREEN);
                        linefront.setVisibility(View.INVISIBLE);
                        discardfront.setVisibility(View.INVISIBLE);
                    }
                }
                if(gbackstatus.equalsIgnoreCase("0")) {
                    File backpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), BACK + ".jpg");
                    String back = String.valueOf(backpic);
                    channelSftp.put(back, "SIMPICSFTP");
                    Boolean success3 = true;

                    if (success3) {
                        txtmsg.setText("upload completed : " + back);
                        System.out.println("upload completed : " + back);
                        UpdateSimRegistrationPicStatus(globalsimid, "BACK_SIDE_ID_STATUS");
                        backimgIcon.setColorFilter(Color.GREEN);
                        lineback.setVisibility(View.INVISIBLE);
                        discardback.setVisibility(View.INVISIBLE);
                    }
                }

                //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                channelSftp.disconnect();
                session.disconnect();
                txtmsg.setText("Transaction completed");
                Thread.sleep(500);
                txtmsg.setText("");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });

    public void UpdateSimRegistrationPicStatus(String vsimregid,String vcolname)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    System.out.println("update SIM_REGISTRATION set " + vcolname + "=1  where SIM_REG_ID ='" + vsimregid + "'");
                    stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set " + vcolname + "=1  where SIM_REG_ID ='" + vsimregid + "'");
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
                    stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set " + vcolname + "=-1  where SIM_REG_ID ='" + vsimregid + "'");
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
                channelSftp.cd("SIMPICSFTP");
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
                    Boolean success2 = true;
                    if(success2){
                        DeleteSimRegistrationPicStatus(globalsimid,"BACK_SIDE_ID_STATUS");
                        backimgIcon.setVisibility(View.INVISIBLE);
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
                    String sqlStmt = "select FIRST_NAME,MIDDLE_NAME,LAST_NAME,STATUS,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS FROM SIM_REGISTRATION where SIM_REG_ID = '" + globalsimid + "'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            FilldatafromDataBase(true,rs1.getString("FIRST_NAME"),rs1.getString("MIDDLE_NAME"),rs1.getString("LAST_NAME"),rs1.getString("MOBILE_NUMBER"),rs1.getString("DATE_OF_BIRTH").substring(8, 10) + "-" + rs1.getString("DATE_OF_BIRTH").substring(5, 7) + "-" + rs1.getString("DATE_OF_BIRTH").substring(0, 4),rs1.getString("NATIONALITY"),rs1.getString("ALTERNATIVE_NUMBER"),rs1.getString("EMAIL_ADDRESS"),rs1.getString("PHISICAL_LOCATION"),rs1.getString("POSTAL_ADDRESS"),rs1.getString("GENDER"),rs1.getString("STATUS"),rs1.getString("AGENT_ID"),rs1.getString("ID_FRONT_SIDE_PHOTO"),rs1.getString("ID_BACK_SID_PHOTO"),rs1.getString("SIGNATURE"),rs1.getString("SIGNATURE_STATUS"),rs1.getString("FRONT_SIDE_ID_STATUS"),rs1.getString("BACK_SIDE_ID_STATUS"));

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
                    PreparedStatement stmtinsert1 = null;

                    try {
                        if (globalsimid.equalsIgnoreCase("0") || OfflineFile.exists() ) {
                            // if it is a new Warehouse we will use insert


                            Statement stmt1 = null;
                            stmt1 = conn.createStatement();
                            String sqlStmt = "select SIM_REGISTRATION_SEQ.nextval as nbr from dual";
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


                            stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,STATUS,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS) values " +
                                    "('" + globalsimid + "','" + b + "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "','" + editagent.getText() + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "',0,0,0)");

                            ///added for pass data in fragment

                        } else {
                            stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + editfname.getText() + "',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() + "',STATUS='" + b + "',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='" + editaltnumber.getText() + "',EMAIL_ADDRESS='" + editemail.getText() + "',PHISICAL_LOCATION='" + editphylocation.getText() + "',POSTAL_ADDRESS='" + editpost.getText() + "',GENDER='" + gender + "',AGENT_NUMBER='" + editagent.getText() + "',AGENT_ID='" + editidagent.getText() + "',SIGNATURE='" + SIGN + "',ID_FRONT_SIDE_PHOTO='" + FRONT + "',ID_BACK_SID_PHOTO='" + BACK + "' where SIM_REG_ID ='" + globalsimid + "'");
                        }
                        txtmsg.setText("Saving Completed,now start load pictures");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        stmtinsert1.executeUpdate();
                        OfflineFile.delete();
                        // reduce counter of local save files
                        count --;
                        Button BtnData=(Button) findViewById(R.id.BtnData);
                        if(count==0)
                        {
                            BtnData.setVisibility(View.INVISIBLE);
                        }
                        else {
                            BtnData.setText(String.valueOf(count));
                        }

                        //calling to upload pictures  using stfp
                        if (globalsimid.equalsIgnoreCase("0")) {
                            //Toast.makeText(SimRegInfo.this, "New SIM Uploading Photos started", Toast.LENGTH_LONG).show();
                            thread1.start();
                            // Toast.makeText(SimRegInfo.this, "New SIM Upload Completed", Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0")) {
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
                        txtmsg.setText("Transaction completed");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
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
                        bw.write(b);
                        bw.close();
                        txtmsg.setText("Saving offmode completed");
                        Thread.sleep(500);
                        txtmsg.setText("");
                        //Toast.makeText(SimRegInfo.this, fileName + " is saved to\n" + dir, Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                       // startActivity(intent);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(getIntent());
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
        String date= dayOfMonth+"-"+(month+1)+"-"+year;
        editdate.setText(date);

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

    public void FilldatafromDataBase(Boolean valeur,String firstname,String middlename,String lastname,String mobilenuber,String dob,String nation,String altnumber,String emailadrs,String phylocation,String postaladr,String vgender,String stat, String agentid,String txtf, String txtb, String txts,String gsigstat,String gfrontstat,String gbackstat  ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkBox.setChecked(valeur);
                editfname.setText(firstname);
                editmname.setText(middlename);
                editlname.setText(lastname);
                editmobile.setText(mobilenuber);
                editdate.setText(dob);
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
                }
                if (stat.matches("Failed")) {
                    sp.setSelection(3);
                }

                editidagent.setText(agentid);
                textF.setText(txtf);
                textB.setText(txtb);
                textS.setText(txts);
                SIGNorigin =SIGN= txts;
                FRONTorigin =FRONT= txtf;
                BACKorigin =BACK= txtb;
                gsigstatusorigin =gsigstatus= gsigstat;
                gfrontstatusorigin =gfrontstatus= gfrontstat;
                gbackstatusorigin =gbackstatus = gbackstat;

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

            }

        });
    }

    public void SavedataSIM() {
        boolean flg = false;
        try {
            if ((flg = connecttoDB()) == true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    if (globalsimid.equalsIgnoreCase("0") || OfflineFile.exists() ) {
                        // if it is a new Warehouse we will use insert


                        Statement stmt1 = null;
                        stmt1 = conn.createStatement();
                        String sqlStmt = "select SIM_REGISTRATION_SEQ.nextval as nbr from dual";
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


                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,STATUS,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS) values " +
                                "('" + globalsimid + "','" + b + "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "','" + editagent.getText() + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "',0,0,0)");

                        ///added for pass data in fragment

                    } else {
                        stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + editfname.getText() + "',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() + "',STATUS='" + b + "',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='" + editaltnumber.getText() + "',EMAIL_ADDRESS='" + editemail.getText() + "',PHISICAL_LOCATION='" + editphylocation.getText() + "',POSTAL_ADDRESS='" + editpost.getText() + "',GENDER='" + gender + "',AGENT_NUMBER='" + editagent.getText() + "',AGENT_ID='" + editidagent.getText() + "',SIGNATURE='" + SIGN + "',ID_FRONT_SIDE_PHOTO='" + FRONT + "',ID_BACK_SID_PHOTO='" + BACK + "' where SIM_REG_ID ='" + globalsimid + "'");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtinsert1.executeUpdate();
                    OfflineFile.delete();

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
            else {
                //Saving OFFLINE when no connection to DB  TURKIEH
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

