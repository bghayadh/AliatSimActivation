package com.example.aliatsimactivation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SimRegInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String gender = null;
    private int count;
    public Connection conn;
    private String globalsimid;
    private Button submit, frontid, backid, activatesim;
    private Button sign;
    private File file,OfflineFile;
    private String nationality = "";
    private RadioButton kenya;
    private RadioButton foreign;
    private CheckBox checkBox;
    private TextView editdate, editmname;
    private Integer a;
    private Integer fb = 0;

    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView editlname, editfname, textF, textB, textS;
    private String file1 = "MSISDN.txt";
    private String s0, s1, Result;
    private String FRONT, BACK, SIGN = null;
    TextView editidagent, editagent;

    private Button Btnftp,BtnDelete;
    private ImageButton signimgIcon, frontimgIcon, backimgIcon;
    private String[] imagesource;
    String server = "ftp.ipage.com";
    int port = 21;
    String user = "beid";
    String pass = "10th@Loop";
    FTPClient ftpClient = new FTPClient();
    private String PathSignFTP, PathFrontFTP, PathBackFTP;

    private static final long START_TIME_IN_MILLIS = 120000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private TextView mTextViewCountDown;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            FRONT = editfname.getText().toString() + editlname.getText().toString() + "_FRONT_" + editagent.getText().toString() + "_" + editidagent.getText().toString();

            Bitmap bmp = (Bitmap) data.getExtras().get("data");

            file = new File("/sdcard/Pictures", FRONT + ".jpg");

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
                textF.setText("/sdcard/Pictures/" + FRONT);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }
        }
        if (requestCode == 101) {
            BACK = editfname.getText().toString() + editlname.getText().toString() + "_BACK_" + editagent.getText().toString() + "_" + editidagent.getText().toString();

            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            file = new File("/sdcard/Pictures", BACK + ".jpg");

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
                textB.setText("/sdcard/Pictures/" + BACK);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(SimRegInfo.this, "Connected", Toast.LENGTH_SHORT).show();


            setContentView(R.layout.activity_simactivityview);
            editfname = (TextView) findViewById(R.id.efirstname);
            editmname = (TextView) findViewById(R.id.emiddlename);
            editlname = (TextView) findViewById(R.id.elastname);
            TextView editmobile = (TextView) findViewById(R.id.emobilenumber);
            editdate = (TextView) findViewById(R.id.edateofbirth);
            kenya = findViewById(R.id.ekenian);
            foreign = findViewById(R.id.eforien);
            TextView editaltnumber = (TextView) findViewById(R.id.ealtirnativenumber);
            TextView editemail = (TextView) findViewById(R.id.email);
            TextView editphylocation = (TextView) findViewById(R.id.ephysicallocation);
            TextView editpost = (TextView) findViewById(R.id.epostaladdress);
            Spinner sp = (Spinner) findViewById(R.id.statusSpinner);
            textB = findViewById(R.id.backpath);
            textF = findViewById(R.id.frontpath);
            textS = findViewById(R.id.sigpath);
            Date cc = Calendar.getInstance().getTime();
            System.out.println("Current time=> " + cc);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            editdate.setText("20-6-2020");
            editagent = (TextView) findViewById(R.id.eagentnum);
            editidagent = (TextView) findViewById(R.id.eagentid);

            Intent intent = SimRegInfo.this.getIntent();
            String str = intent.getStringExtra("message_key");

            globalsimid = str.toString();
            sign = findViewById(R.id.bsigniture);
            submit = findViewById(R.id.submitbtn);
            frontid = findViewById(R.id.bfront);
            backid = findViewById(R.id.bback);
            activatesim = findViewById(R.id.activatesim);
            checkBox = findViewById(R.id.chterms);
            Spinner s = (Spinner) findViewById(R.id.spinner);
            mTextViewCountDown = findViewById(R.id.txttimer);



            String Off4 = intent.getStringExtra("offline4");
            editidagent.setText(Off4);
            String myFileName = "SIM_" + editidagent.getText().toString() + ".txt";
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            OfflineFile = new File(directory,myFileName);
            if (OfflineFile.exists())
            {
                String Off1 = intent.getStringExtra("offline1");
                editfname.setText(Off1);

                String Off2 = intent.getStringExtra("offline2");
                editmname.setText(Off2);

                String Off3 = intent.getStringExtra("offline3");
                editlname.setText(Off3);

                String Off5 = intent.getStringExtra("offline5");
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
                s.setSelection(((ArrayAdapter<String>) s.getAdapter()).getPosition(Off11));

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

            }




            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{
                        Manifest.permission.CAMERA}, 100);
                ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{
                        Manifest.permission.CAMERA}, 101);

            }


            File file = new File(SimRegInfo.this.getFilesDir(), "MSISDN.txt");
            if (file.exists()) {

                StringBuilder text = new StringBuilder();

                try {
                    FileInputStream fIn = SimRegInfo.this.openFileInput("MSISDN.txt");
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


                Result = text.toString();
                System.out.println("RESULT" + Result);
                String[] data = Result.split(":");
                String s0 = data[0];

                editagent.setText(s0);
                editagent.setEnabled(false);
            } else {
                System.out.println("login filevdon't exist");
            }
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
            count = 0;
            for (File f : files) {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" + count);
            }


            connecttoDB();

            PreparedStatement stmtinsert1 = null;

            try {
                // if it is a new Warehouse we will use insert
                if (globalsimid != "0") {
                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "select FIRST_NAME,MIDDLE_NAME,LAST_NAME,STATUS,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO FROM SIM_REGISTRATION where SIM_REG_ID = '" + globalsimid + "'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            editfname.setText(rs1.getString("FIRST_NAME"));
                            editmname.setText(rs1.getString("MIDDLE_NAME"));
                            editlname.setText(rs1.getString("LAST_NAME"));
                            editmobile.setText(rs1.getString("MOBILE_NUMBER"));
                            editdate.setText(rs1.getString("DATE_OF_BIRTH").substring(8, 10) + "-" + rs1.getString("DATE_OF_BIRTH").substring(5, 7) + "-" + rs1.getString("DATE_OF_BIRTH").substring(0, 4));
                            if (rs1.getString("NATIONALITY").matches("Foreign")) {
                                foreign.setChecked(true);
                                kenya.setChecked(false);
                            } else {
                                kenya.setChecked(true);
                                foreign.setChecked(false);
                            }
                            editaltnumber.setText(rs1.getString("ALTERNATIVE_NUMBER"));
                            editemail.setText(rs1.getString("EMAIL_ADDRESS"));
                            editphylocation.setText(rs1.getString("PHISICAL_LOCATION"));
                            editpost.setText(rs1.getString("POSTAL_ADDRESS"));
                            if (rs1.getString("GENDER").matches("Male")) {
                                s.setSelection(0);
                            }
                            if (rs1.getString("GENDER").matches("Female")) {
                                s.setSelection(1);
                            }
                            if (rs1.getString("STATUS").matches("New")) {
                                sp.setSelection(0);
                            }
                            if (rs1.getString("STATUS").matches("In Progress")) {
                                sp.setSelection(1);
                            }
                            if (rs1.getString("STATUS").matches("Success")) {
                                sp.setSelection(2);
                            }
                            if (rs1.getString("STATUS").matches("Not Success")) {
                                sp.setSelection(3);
                            }

                            editidagent.setText(rs1.getString("AGENT_ID"));
                            textF.setText(rs1.getString("ID_FRONT_SIDE_PHOTO"));
                            textB.setText(rs1.getString("ID_BACK_SID_PHOTO"));
                            textS.setText(rs1.getString("SIGNATURE"));
                            SIGN = rs1.getString("SIGNATURE");
                            FRONT = rs1.getString("ID_FRONT_SIDE_PHOTO");
                            BACK = rs1.getString("ID_BACK_SID_PHOTO");

                            checkBox.setChecked(true);


                            //System.out.println(rs1.getString("compteur"));

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    rs1.close();
                    stmt1.close();
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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
                    }
                }
            });

            editdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();


                }
            });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dY[] = editdate.getText().toString().split("-");
                    fb = getAge(Integer.parseInt(dY[2]), Integer.parseInt(dY[1]), Integer.parseInt(dY[0]));
                    System.out.println(fb);
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
                        if (count > 5) {
                            Toast.makeText(SimRegInfo.this, "You Already Have 5 Unsubmitted Files", Toast.LENGTH_SHORT).show();
                        }
                        if (!checkBox.isChecked()) {
                            Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                        }
                        if (SIGN == null) {
                            sign.setError("Please Sign");
                        }
                        if (FRONT == null) {
                            frontid.setError("Please Sign");
                        }
                        if (BACK == null) {
                            backid.setError("Please Sign");
                        }


                    } else {
                        new AlertDialog.Builder(SimRegInfo.this)
                                .setTitle("Submit")
                                .setMessage("Are you sure you want to Submit this form?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (s.getSelectedItemPosition() == 0) {

                                            gender = "Male";
                                        }
                                        if (s.getSelectedItemPosition() == 1) {

                                            gender = "Female";
                                        }
                                        if (kenya.isChecked()) {
                                            nationality = "Kenyan";
                                        }
                                        if (foreign.isChecked()) {
                                            nationality = "Foreign";
                                        }
                                        String b = "In Progress";

                                        Date date = new Date();
                                        Calendar calendar = new GregorianCalendar();
                                        calendar.setTime(date);
                                        int year = calendar.get(Calendar.YEAR);
                                        String simID;
                                        simID = "REG_" + year + "_";
                                        connecttoDB();

                                        PreparedStatement stmtinsert1 = null;

                                        try {
                                            if (globalsimid.equalsIgnoreCase("0") || OfflineFile.exists()) {
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


                                                stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,STATUS,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO) values " +
                                                        "('" + globalsimid + "','" + b + "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "','" + editagent.getText() + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "')");

                                                ///added for pass data in fragment

                                            } else {

                                                stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set CREATION_DATE=sysdate,LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + editfname.getText() + "',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() + "',STATUS='" + b + "',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='" + editaltnumber.getText() + "',EMAIL_ADDRESS='" + editemail.getText() + "',PHISICAL_LOCATION='" + editphylocation.getText() + "',POSTAL_ADDRESS='" + editpost.getText() + "',GENDER='" + gender + "',AGENT_NUMBER='" + editagent.getText() + "',AGENT_ID='" + editidagent.getText() + "',SIGNATURE='" + SIGN + "',ID_FRONT_SIDE_PHOTO='" + FRONT + "',ID_BACK_SID_PHOTO='" + BACK + "' where SIM_REG_ID ='" + globalsimid + "'");
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                        try {
                                            stmtinsert1.executeUpdate();
                                            Toast.makeText(SimRegInfo.this, "Saving Completed", Toast.LENGTH_SHORT).show();
                                            //Intent a = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                                            //startActivity(a);
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
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
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


            activatesim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gender=s.getSelectedItem().toString();
                    String fname=editfname.getText().toString();
                    String mname=editmname.getText().toString();
                    String lname=editlname.getText().toString();
                    String msisdn=editmobile.getText().toString();
                    String idType="IDCARD";
                    String idNumber = editidagent.getText().toString();
                    String dob=editdate.getText().toString();
                    String email=editemail.getText().toString();
                    String altnumber=editaltnumber.getText().toString();
                    String address1=editphylocation.getText().toString();
                    String state=editpost.getText().toString();
                    String agentmsisdn = editagent.getText().toString();

                    //new SimRegistrationAPI(fname,mname,lname,msisdn,idType,idNumber,dob,gender,email,altnumber,address1,state,agentmsisdn).execute();



                    Intent a=new Intent(SimRegInfo.this,Activate_Sim.class);
                    a.putExtra("fname",fname);
                    a.putExtra("mname",mname);
                    a.putExtra("lname",lname);
                    a.putExtra("msisdn",msisdn);
                    a.putExtra("idType",idType);
                    a.putExtra("idNumber",idNumber);
                    a.putExtra("dob",dob);
                    a.putExtra("email",email);
                    a.putExtra("gender",gender);
                    a.putExtra("altnumber",altnumber);
                    a.putExtra("address1",address1);
                    a.putExtra("state",state);
                    a.putExtra("agentmsisdn",agentmsisdn);
                    startActivity(a);
                }
            });

            //BtnData appear in case offline files exist
            Button BtnData = (Button) findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                    i.putExtra("message_key", globalsimid);
                    startActivity(i);
                }
            });
            Button Btnmain = (Button) findViewById(R.id.BtnMainn);
            Btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    startActivity(i);
                }
            });

            if (count != 0) {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }


            signimgIcon = findViewById(R.id.signimgIcon);
            File signsave = new File("/sdcard/Pictures", SIGN + ".jpg");
            if (signsave.exists()) {
                signimgIcon.setVisibility(View.VISIBLE);
                signimgIcon.setBackgroundResource(0);
                signimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SimRegInfo.this, "Send your image to FTP", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                SignImageStatus();
            }


            frontimgIcon = findViewById(R.id.frontimgIcon);
            File frontsave = new File("/sdcard/Pictures", FRONT + ".jpg");
            if (frontsave.exists()) {
                frontimgIcon.setVisibility(View.VISIBLE);
                frontimgIcon.setBackgroundResource(0);
                frontimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SimRegInfo.this, "Send your image to FTP", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                FrontImageStatus();
            }


            backimgIcon = findViewById(R.id.backimgIcon);
            File backsave = new File("/sdcard/Pictures", BACK + ".jpg");
            if (backsave.exists()) {
                backimgIcon.setVisibility(View.VISIBLE);
                backimgIcon.setBackgroundResource(0);
                backimgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SimRegInfo.this, "Send your image to FTP", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                BackImageStatus();
            }


            Btnftp = findViewById(R.id.Btnftp);
            Btnftp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendTOftp();
                    SignImageStatus();
                    FrontImageStatus();
                    BackImageStatus();
                    OfflineFile.delete();
                }
            });


            BtnDelete = findViewById(R.id.BtnDelete);
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteFromDB(globalsimid);
                    DeleteFromFTP();
                }
            });

/*
            if(signsave.exists() || frontsave.exists() || backsave.exists()){
                startTimer();
            } else {
                mTextViewCountDown.setVisibility(View.INVISIBLE);
            }
*/


        } else {
            Toast.makeText(SimRegInfo.this, "Not Connected", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_simactivityview);
            editfname = (TextView) findViewById(R.id.efirstname);
            editmname = (TextView) findViewById(R.id.emiddlename);
            editlname = (TextView) findViewById(R.id.elastname);
            TextView editmobile = (TextView) findViewById(R.id.emobilenumber);
            editdate = (TextView) findViewById(R.id.edateofbirth);
            kenya = findViewById(R.id.ekenian);
            foreign = findViewById(R.id.eforien);
            TextView editaltnumber = (TextView) findViewById(R.id.ealtirnativenumber);
            TextView editemail = (TextView) findViewById(R.id.email);
            TextView editphylocation = (TextView) findViewById(R.id.ephysicallocation);
            TextView editpost = (TextView) findViewById(R.id.epostaladdress);
            textB = findViewById(R.id.backpath);
            textF = findViewById(R.id.frontpath);
            textS = findViewById(R.id.sigpath);
            Spinner sp = (Spinner) findViewById(R.id.statusSpinner);
            Date cc = Calendar.getInstance().getTime();
            System.out.println("Current time=> " + cc);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            editdate.setText(df.format(cc));
            editagent = (TextView) findViewById(R.id.eagentnum);
            editidagent = (TextView) findViewById(R.id.eagentid);
            Intent intent = SimRegInfo.this.getIntent();

            sign = findViewById(R.id.bsigniture);
            submit = findViewById(R.id.submitbtn);
            frontid = findViewById(R.id.bfront);
            backid = findViewById(R.id.bback);
            activatesim = findViewById(R.id.activatesim);
            checkBox = findViewById(R.id.chterms);
            Spinner s = (Spinner) findViewById(R.id.spinner);

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{
                        Manifest.permission.CAMERA}, 100);
                ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{
                        Manifest.permission.CAMERA}, 101);

            }

            File file = new File(SimRegInfo.this.getFilesDir(), "MSISDN.txt");
            if (file.exists()) {

                StringBuilder text = new StringBuilder();

                try {
                    FileInputStream fIn = SimRegInfo.this.openFileInput("MSISDN.txt");
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


                Result = text.toString();
                System.out.println("RESULT" + Result);
                String[] data = Result.split(":");
                String s0 = data[0];

                editagent.setText(s0);
            } else {
                System.out.println("login filevdon't exist");
            }
            //counting the number of files
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
            count = 0;
            for (File f : files) {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" + count);
            }
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
                    }
                }
            });

            editdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();

                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

                        if (count > 5) {
                            Toast.makeText(SimRegInfo.this, "You Already Have 5 Unsubmitted Files", Toast.LENGTH_SHORT).show();
                        }
                        if (!checkBox.isChecked()) {
                            Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                        }
                        if (SIGN == null) {
                            sign.setError("Please Sign");
                        }
                        if (FRONT == null) {
                            frontid.setError("Please Sign");
                        }
                        if (BACK == null) {
                            backid.setError("Please Sign");
                        }


                    } else {
                        new AlertDialog.Builder(SimRegInfo.this)
                                .setTitle("Submit")
                                .setMessage("Are you sure you want to Submit this form?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (s.getSelectedItemPosition() == 0) {

                                            gender = "Male";
                                        }
                                        if (s.getSelectedItemPosition() == 1) {

                                            gender = "Female";
                                        }
                                        if (kenya.isChecked()) {
                                            nationality = "Kenyan";
                                        }
                                        if (foreign.isChecked()) {
                                            nationality = "Foreign";
                                        }
                                        String b = "New";

                                        if (sp.getSelectedItemPosition() == 0) {
                                            b = "New";
                                        }
                                        if (sp.getSelectedItemPosition() == 1) {
                                            b = "In Progress";
                                        }
                                        if (sp.getSelectedItemPosition() == 2) {
                                            b = "Success";
                                        }
                                        if (sp.getSelectedItemPosition() == 3) {
                                            b = "Not Success";
                                        }
                                        try {
                                            ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);
                                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                                            dir.mkdirs();
                                            String fileName = "SIM_" + editidagent.getText().toString() + ".txt";
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
                                            Toast.makeText(SimRegInfo.this, fileName + " is saved to\n" + dir, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SimRegInfo.this, SimRegOfflineDataActivity.class);
                                            startActivity(intent);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
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


            activatesim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(SimRegInfo.this, Activate_Sim.class);
                    startActivity(a);
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
            Button Btnmain = (Button) findViewById(R.id.BtnMainn);
            Btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this, MainActivity.class);
                    startActivity(i);
                }
            });

            if (count != 0) {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }


        }
    }


    public void SendTOftp() {

        File myFile = new File("/sdcard/Pictures", SIGN + ".jpg");
        String signpath = String.valueOf(myFile);
        String signname = String.valueOf(textS.getText() + ".jpg");


        File myFile1 = new File("/sdcard/Pictures", FRONT + ".jpg");
        String frontpath = String.valueOf(myFile1);
        String frontname = String.valueOf(textF.getText() + ".jpg");

        File myFile2 = new File("/sdcard/Pictures", BACK + ".jpg");
        String backpath = String.valueOf(myFile2);
        String backname = String.valueOf(textB.getText() + ".jpg");


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
                PathSignFTP = workingDir + "/" + SIGN + ".jpg";
                PathFrontFTP = workingDir + "/" + FRONT + ".jpg";
                PathBackFTP = workingDir + "/" + BACK + ".jpg";
                System.out.println("Directory: " + workingDir);
                // upload file
                try {

                    FileInputStream srcFileStream = new FileInputStream(signpath);
                    ftpClient.storeFile(signname, srcFileStream);

                    FileInputStream srcFileStream1 = new FileInputStream(frontpath);
                    ftpClient.storeFile(frontname, srcFileStream1);

                    FileInputStream srcFileStream2 = new FileInputStream(backpath);
                    ftpClient.storeFile(backname, srcFileStream2);

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

        File signsave = new File("/sdcard/Pictures", SIGN + ".jpg");
        File frontsave = new File("/sdcard/Pictures", FRONT + ".jpg");
        File backsave = new File("/sdcard/Pictures", BACK + ".jpg");
        signsave.delete();
        frontsave.delete();
        backsave.delete();

    }


    public void connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " + e.toString());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (InstantiationException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        editdate.setText(date);


    }

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



    private void DeleteFromDB(String SimRegId){
        connecttoDB();

        PreparedStatement stmtinsert1 = null;
        try {
            // Delete Ware_id

            stmtinsert1 = conn.prepareStatement("delete SIM_REGISTRATION   where SIM_REG_ID ='" + SimRegId +"' ");

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

        try {
            stmtinsert1.executeUpdate();
            Toast.makeText (SimRegInfo.this,"Delete Completed",Toast.LENGTH_SHORT).show ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }


        try {
            stmtinsert1.close();
            conn.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

        Intent intent = new Intent(SimRegInfo.this,SimRegListViewActivity.class);
        startActivity(intent);
    }


    private void DeleteFromFTP(){
        //calling FTP cradantials
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
                boolean status = ftpClient.login (user, pass);
                ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode ( );
                String SIGNPATH = "Sim" + "/" + globalsimid +"/" + SIGN+ ".jpg";
                String FRONTPATH = "Sim" + "/" + globalsimid +"/" + FRONT+ ".jpg";
                String BACKPATH = "Sim" + "/" + globalsimid +"/" + BACK+ ".jpg";
                ftpClient.deleteFile(SIGNPATH);
                ftpClient.deleteFile(FRONTPATH);
                ftpClient.deleteFile(BACKPATH);
                ftpClient.removeDirectory("/Sim" + "/" + globalsimid +"/");
                Toast.makeText (getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show ();
            }


        } catch (IOException e) {
            Toast.makeText (SimRegInfo.this,e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
        }
        try {
            ftpClient.login (user, pass);
        } catch (IOException e) {
            Toast.makeText (SimRegInfo.this,e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
        }
        ftpClient.enterLocalPassiveMode ( );

        try {
            ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            Toast.makeText (SimRegInfo.this,e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
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

                    } else {
                        signimgIcon.setVisibility(View.VISIBLE);
                        signimgIcon.setBackgroundResource(0);
                        signimgIcon.setColorFilter(Color.RED);
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

    }



    private void FrontImageStatus() {

        frontimgIcon = findViewById(R.id.frontimgIcon);
        if (textF.getText().toString() != "") {

            String FRONTPATH = "Sim" + "/" + globalsimid +"/" + FRONT+ ".jpg";
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
                    InputStream inStream = ftpClient.retrieveFileStream(FRONTPATH);
                    if(inStream != null) {
                        frontimgIcon.setVisibility(View.VISIBLE);
                        frontimgIcon.setBackgroundResource(0);
                        frontimgIcon.setColorFilter(Color.GREEN);
                        frontimgIcon.setOnClickListener(new View.OnClickListener() {
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

                    } else {
                        frontimgIcon.setVisibility(View.VISIBLE);
                        frontimgIcon.setBackgroundResource(0);
                        frontimgIcon.setColorFilter(Color.RED);
                        frontimgIcon.setOnClickListener(new View.OnClickListener() {
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

    }



    private void BackImageStatus() {

        backimgIcon = findViewById(R.id.backimgIcon);
        if (textB.getText().toString() != "") {

            String BACKPATH = "Sim" + "/" + globalsimid +"/" + BACK+ ".jpg";
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
                    InputStream inStream = ftpClient.retrieveFileStream(BACKPATH);
                    if(inStream != null) {
                        backimgIcon.setVisibility(View.VISIBLE);
                        backimgIcon.setBackgroundResource(0);
                        backimgIcon.setColorFilter(Color.GREEN);
                        backimgIcon.setOnClickListener(new View.OnClickListener() {
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

                    } else {
                        backimgIcon.setVisibility(View.VISIBLE);
                        backimgIcon.setBackgroundResource(0);
                        backimgIcon.setColorFilter(Color.RED);
                        backimgIcon.setOnClickListener(new View.OnClickListener() {
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

    }

/*

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            public void onFinish() {
                File signsave = new File("/sdcard/Pictures", SIGN + ".jpg");
                File frontsave = new File("/sdcard/Pictures", FRONT + ".jpg");
                File backsave = new File("/sdcard/Pictures", BACK + ".jpg");
                if(signsave.exists() || frontsave.exists() || backsave.exists()){
                    SendTOftp();
                    SignImageStatus();
                    FrontImageStatus();
                    BackImageStatus();
                    signsave.delete();
                    frontsave.delete();
                    backsave.delete();
                }
                resetTimer();
            }
        }.start();
        mTimerRunning = true;
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        startTimer();
    }

    private void updateCountDownText() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            mTextViewCountDown.setText(timeLeftFormatted);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        resetTimer();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                resetTimer();
            } else {
                startTimer();
            }
        }
    }
*/
}