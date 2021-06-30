package com.example.aliatsimactivation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

public class SimRegInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private String gender=null;
    private int count;
    public Connection conn;
    private String globalsimid;
    private Button submit,frontid,backid,activatesim;
    private Button sign;
    private File file;
    private String nationality="";
    private RadioButton kenya,foreign;
    private CheckBox checkBox;
    private TextView editdate, editmname;
    private Integer a;
    private Integer fb=0;

    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView editlname,editfname,textF,textB,textS;
    private String file1 = "MSISDN.txt";
    private String s0,s1,Result;
    private String FRONT,BACK,SIGN=null;
    TextView editidagent,editagent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            FRONT=editfname.getText().toString()+editlname.getText().toString()+"_FRONT_"+editagent.getText().toString()+"_"+editidagent.getText().toString();

            Bitmap bmp=(Bitmap)data.getExtras().get("data");

            file = new File("/sdcard/Pictures", FRONT+ ".jpg");

            Log.d("path", file.toString());
            FileOutputStream fileOutputStream =null;
            try {
                fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                textF.setText("/sdcard/Pictures/"+FRONT);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }
        }
        if(requestCode==101){
            BACK=editfname.getText().toString()+editlname.getText().toString()+"_BACK_"+editagent.getText().toString()+"_"+editidagent.getText().toString();

            Bitmap bmp=(Bitmap)data.getExtras().get("data");
            file = new File("/sdcard/Pictures", BACK + ".jpg");

            Log.d("path", file.toString());
            FileOutputStream fileOutputStream =null;
            try {
                fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();
                textB.setText("/sdcard/Pictures/"+BACK);

            } catch (Exception e) {
                Log.v("ID Gestures", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(SimRegInfo.this, "Connected", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_simactivityview);
        editfname=(TextView)findViewById(R.id.efirstname);
         editmname=(TextView)findViewById(R.id.emiddlename);
         editlname=(TextView)findViewById(R.id.elastname);
        TextView editmobile=(TextView)findViewById(R.id.emobilenumber);
         editdate=(TextView)findViewById(R.id.edateofbirth);
        kenya=findViewById(R.id.ekenian);
        foreign=findViewById(R.id.eforien);
        TextView editaltnumber=(TextView)findViewById(R.id.ealtirnativenumber);
        TextView editemail=(TextView)findViewById(R.id.email);
        TextView editphylocation=(TextView)findViewById(R.id.ephysicallocation);
        TextView editpost=(TextView)findViewById(R.id.epostaladdress);
            Spinner sp =(Spinner)findViewById(R.id.statusSpinner);
        textB=findViewById(R.id.backpath);
        textF=findViewById(R.id.frontpath);
        textS=findViewById(R.id.sigpath);
            Date cc = Calendar.getInstance().getTime();
            System.out.println("Current time=> "+cc);
            SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            editdate.setText("20-6-2020");
        editagent=(TextView)findViewById(R.id.eagentnum);
         editidagent=(TextView)findViewById(R.id.eagentid);
        Intent intent = SimRegInfo.this.getIntent();
        String str = intent.getStringExtra("message_key");

        globalsimid = str.toString();
        sign= findViewById(R.id.bsigniture);
        submit=findViewById(R.id.submitbtn);
        frontid=findViewById(R.id.bfront);
        backid=findViewById(R.id.bback);
        activatesim=findViewById(R.id.activatesim);
        checkBox=findViewById(R.id.chterms);
            Spinner s = (Spinner)findViewById(R.id.spinner);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(SimRegInfo.this,new String[]{
                    Manifest.permission.CAMERA},100);
            ActivityCompat.requestPermissions(SimRegInfo.this,new String[]{
                    Manifest.permission.CAMERA},101);

        }

        File file = new File(SimRegInfo.this.getFilesDir(),"MSISDN.txt");
        if(file.exists()) {

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


            Result=text.toString();
            System.out.println("RESULT" +Result);
            String[] data = Result.split(":");
            String s0 = data[0];

            editagent.setText(s0);
        }
        else
        {
            System.out.println("login filevdon't exist");
        }
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
            count =0;
            for (File f: files)
            {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" +count);
            }


        connecttoDB();

        PreparedStatement stmtinsert1 = null;

        try {
            // if it is a new Warehouse we will use insert
            if (globalsimid!="0") {
                Statement stmt1 = null;
                stmt1 = conn.createStatement ( );
                String sqlStmt = "select FIRST_NAME,MIDDLE_NAME,LAST_NAME,STATUS,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO FROM SIM_REGISTRATION where SIM_REG_ID = '"+globalsimid+"'";
                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery (sqlStmt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                while (true) {
                    try {
                        if (!rs1.next ( )) break;
                        editfname.setText(rs1.getString("FIRST_NAME"));
                        editmname.setText(rs1.getString("MIDDLE_NAME"));
                        editlname.setText(rs1.getString("LAST_NAME"));
                        editmobile.setText(rs1.getString("MOBILE_NUMBER"));
                        editdate.setText(rs1.getString("DATE_OF_BIRTH").substring(8,10)+"-"+rs1.getString("DATE_OF_BIRTH").substring(5,7)+"-"+rs1.getString("DATE_OF_BIRTH").substring(0,4));
                        if(rs1.getString("NATIONALITY").matches("Foreign")){
                            foreign.setChecked(true);
                            kenya.setChecked(false);
                        }
                        else {
                            kenya.setChecked(true);
                            foreign.setChecked(false);
                        }
                        editaltnumber.setText(rs1.getString("ALTERNATIVE_NUMBER"));
                        editemail.setText(rs1.getString("EMAIL_ADDRESS"));
                        editphylocation.setText(rs1.getString("PHISICAL_LOCATION"));
                        editpost.setText(rs1.getString("POSTAL_ADDRESS"));
                        if(rs1.getString("GENDER").matches("Male")){
                            s.setSelection(0);
                        }
                        if(rs1.getString("GENDER").matches("Female")){
                            s.setSelection(1);
                        }
                        if(rs1.getString("STATUS").matches("New")){
                            sp.setSelection(0);
                        }
                        if(rs1.getString("STATUS").matches("In Progress")){
                            sp.setSelection(1);
                        }
                        if(rs1.getString("STATUS").matches("Success")){
                            sp.setSelection(2);
                        }
                        if(rs1.getString("STATUS").matches("Not Success")){
                            sp.setSelection(3);
                        }

                        editidagent.setText(rs1.getString("AGENT_ID"));
                        textF.setText(rs1.getString("ID_FRONT_SIDE_PHOTO"));
                        textB.setText(rs1.getString("ID_BACK_SID_PHOTO"));
                        textS.setText(rs1.getString("SIGNATURE"));
                        SIGN=rs1.getString("SIGNATURE");
                        FRONT=rs1.getString("ID_FRONT_SIDE_PHOTO");
                        BACK=rs1.getString("ID_BACK_SID_PHOTO");

                        checkBox.setChecked(true);







                        //System.out.println(rs1.getString("compteur"));

                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }
                }
                rs1.close();
                stmt1.close();
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                    Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                }
                else {
                Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                SIGN=editfname.getText().toString()+editlname.getText().toString()+"_SIGNATURE_"+editagent.getText().toString()+"_"+editidagent.getText().toString();
                a.putExtra("sign",SIGN);
                startActivity(a);
                    textS.setText(SIGN);}
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
                String dY[]=editdate.getText().toString().split("-");
                fb=getAge(Integer.parseInt(dY[2]),Integer.parseInt(dY[1]),Integer.parseInt(dY[0]));
                System.out.println(fb);
                if(editfname.getText().toString().matches("")||editmname.getText().toString().matches("")||editlname.getText().toString().matches("")||editmobile.getText().toString().matches("")||editaltnumber.getText().toString().matches("")||editemail.getText().toString().matches("")||editphylocation.getText().toString().matches("")||editpost.getText().toString().matches("")||!editemail.getText().toString().matches(emailpattern)||fb<18||!checkBox.isChecked()||SIGN==null||FRONT==null||BACK==null||fb==0||editdate.getText().toString()==null) {

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
                    if (fb < 18 && fb>=0) {
                        editdate.setError("Under Age");
                    }
                    else if(fb>=18){
                        editdate.setError(null);
                    }
                    if(count>5){
                        Toast.makeText(SimRegInfo.this, "You Already Have 5 Unsubmitted Files", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkBox.isChecked()){
                        Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                    }
                    if(SIGN==null){
                        sign.setError("Please Sign");
                    }
                    if(FRONT==null){
                        frontid.setError("Please Sign");
                    }
                    if(BACK==null){
                        backid.setError("Please Sign");
                    }



                }else{
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
                                         if (globalsimid.equalsIgnoreCase("0")) {
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
                                                     "('" + globalsimid +"','"+b+ "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "','" + editagent.getText() + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                                             ///added for pass data in fragment
                                         }else{

                                             stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set CREATION_DATE=sysdate,LAST_MODIFIED_DATE=sysdate,FIRST_NAME='"+editfname.getText()+"',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() +"',STATUS='"+b+"',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='"+editaltnumber.getText()+"',EMAIL_ADDRESS='"+editemail.getText()+"',PHISICAL_LOCATION='"+editphylocation.getText()+"',POSTAL_ADDRESS='"+editpost.getText()+"',GENDER='"+gender+"',AGENT_NUMBER='"+editagent.getText()+"',AGENT_ID='"+editidagent.getText()+"',SIGNATURE='"+SIGN+"',ID_FRONT_SIDE_PHOTO='"+FRONT+"',ID_BACK_SID_PHOTO='"+BACK+"' where SIM_REG_ID ='"+globalsimid+"'");
                                         }
                                     } catch (SQLException throwables) {
                                         throwables.printStackTrace();
                                     }
                                     try {
                                         stmtinsert1.executeUpdate();
                                         Toast.makeText(SimRegInfo.this, "Saving Completed", Toast.LENGTH_SHORT).show();
                                         Intent a = new Intent(SimRegInfo.this, SimRegListViewActivity.class);
                                         startActivity(a);
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
            } });
        frontid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                    Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                }
                else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);}
            }
        });
        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                    Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                }
                else {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,101);}
            }
        });


       activatesim.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent a=new Intent(SimRegInfo.this,Activate_Sim.class);
                startActivity(a);
           }
       });



            //BtnData appear in case offline files exist
            Button BtnData = (Button)findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this,SimRegOfflineDataActivity.class);
                    i.putExtra("message_key",globalsimid);
                    startActivity(i);
                }
            });
            Button Btnmain = (Button)findViewById(R.id.BtnMainn);
            Btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this,MainActivity.class);
                    startActivity(i);
                }
            });

            if (count !=0)
            {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }






        }else {
            Toast.makeText(SimRegInfo.this, "Not Connected", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_simactivityview);
            editfname=(TextView)findViewById(R.id.efirstname);
            editmname=(TextView)findViewById(R.id.emiddlename);
            editlname=(TextView)findViewById(R.id.elastname);
            TextView editmobile=(TextView)findViewById(R.id.emobilenumber);
            editdate=(TextView)findViewById(R.id.edateofbirth);
            kenya=findViewById(R.id.ekenian);
            foreign=findViewById(R.id.eforien);
            TextView editaltnumber=(TextView)findViewById(R.id.ealtirnativenumber);
            TextView editemail=(TextView)findViewById(R.id.email);
            TextView editphylocation=(TextView)findViewById(R.id.ephysicallocation);
            TextView editpost=(TextView)findViewById(R.id.epostaladdress);
            textB=findViewById(R.id.backpath);
            textF=findViewById(R.id.frontpath);
            textS=findViewById(R.id.sigpath);
            Spinner sp =(Spinner)findViewById(R.id.statusSpinner);
            Date cc = Calendar.getInstance().getTime();
            System.out.println("Current time=> "+cc);
            SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            editdate.setText(df.format(cc));
            editagent=(TextView)findViewById(R.id.eagentnum);
            editidagent=(TextView)findViewById(R.id.eagentid);
            Intent intent = SimRegInfo.this.getIntent();

            sign= findViewById(R.id.bsigniture);
            submit=findViewById(R.id.submitbtn);
            frontid=findViewById(R.id.bfront);
            backid=findViewById(R.id.bback);
            activatesim=findViewById(R.id.activatesim);
            checkBox=findViewById(R.id.chterms);
            Spinner s = (Spinner)findViewById(R.id.spinner);
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(SimRegInfo.this,new String[]{
                        Manifest.permission.CAMERA},100);
                ActivityCompat.requestPermissions(SimRegInfo.this,new String[]{
                        Manifest.permission.CAMERA},101);

            }

            File file = new File(SimRegInfo.this.getFilesDir(),"MSISDN.txt");
            if(file.exists()) {

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


                Result=text.toString();
                System.out.println("RESULT" +Result);
                String[] data = Result.split(":");
                String s0 = data[0];

                editagent.setText(s0);
            }
            else
            {
                System.out.println("login filevdon't exist");
            }
            //counting the number of files
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File[] files = dir.listFiles();
             count =0;
            for (File f: files)
            {
                String name = f.getName();
                if (name.startsWith("SIM") && name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" +count);
            }
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                        SIGN=editfname.getText().toString()+editlname.getText().toString()+"_SIGNATURE_"+editagent.getText().toString()+"_"+editidagent.getText().toString();
                        a.putExtra("sign",SIGN);
                        startActivity(a);
                        textS.setText(SIGN);}
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
                    String dY[]=editdate.getText().toString().split("-");
                    fb=getAge(Integer.parseInt(dY[2]),Integer.parseInt(dY[1]),Integer.parseInt(dY[0]));
                    if(editfname.getText().toString().matches("")||editmname.getText().toString().matches("")||editlname.getText().toString().matches("")||editmobile.getText().toString().matches("")||editaltnumber.getText().toString().matches("")||editemail.getText().toString().matches("")||editphylocation.getText().toString().matches("")||editpost.getText().toString().matches("")||!editemail.getText().toString().matches(emailpattern)||fb<18||!checkBox.isChecked()||SIGN==null||FRONT==null||BACK==null||fb==0||editdate.getText().toString()==null) {

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
                        if (fb < 18 && fb>=0) {
                            editdate.setError("Under Age");
                        }
                        else if(fb>=18){
                            editdate.setError(null);
                        }

                        if(count>5){
                            Toast.makeText(SimRegInfo.this, "You Already Have 5 Unsubmitted Files", Toast.LENGTH_SHORT).show();
                        }
                        if(!checkBox.isChecked()){
                            Toast.makeText(SimRegInfo.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                        }
                        if(SIGN==null){
                            sign.setError("Please Sign");
                        }
                        if(FRONT==null){
                            frontid.setError("Please Sign");
                        }
                        if(BACK==null){
                            backid.setError("Please Sign");
                        }



                    }else{
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

                                        if(sp.getSelectedItemPosition()==0){
                                            b="New";
                                        }
                                        if(sp.getSelectedItemPosition()==1){
                                            b="In Progress";
                                        }
                                        if(sp.getSelectedItemPosition()==2){
                                            b="Success";
                                        }
                                        if(sp.getSelectedItemPosition()==3){
                                            b="Not Success";
                                        }
                                        try {
                                            ActivityCompat.requestPermissions(SimRegInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},23);
                                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                                            dir.mkdirs();
                                            String fileName = "SIM_" + editidagent.getText().toString() + ".txt";
                                            File file = new File(dir,fileName);
                                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                            BufferedWriter bw = new BufferedWriter(fw);
                                            bw.write(editfname.getText().toString()+"\n");
                                            bw.write(editmname.getText().toString()+"\n");
                                            bw.write(editfname.getText().toString()+"\n");
                                            bw.write(editmobile.getText().toString()+"\n");
                                            bw.write(editdate.getText().toString()+"\n");
                                            bw.write(nationality.toString()+"\n");
                                            bw.write(editaltnumber.getText().toString()+"\n");
                                            bw.write(editemail.getText().toString()+"\n");
                                            bw.write(editphylocation.getText().toString()+"\n");
                                            bw.write(editpost.getText().toString()+"\n");
                                            bw.write(gender.toString()+"\n");
                                            bw.write(editagent.getText().toString()+"\n");
                                            bw.write(editidagent.getText().toString()+"\n");
                                            bw.write(SIGN.toString()+"\n");
                                            bw.write(FRONT.toString()+"\n");
                                            bw.write(BACK.toString()+"\n");
                                            bw.write(b);
                                            bw.close();
                                            Toast.makeText(SimRegInfo.this, fileName+" is saved to\n" +dir, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SimRegInfo.this,SimRegOfflineDataActivity.class);
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
                } });
            frontid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    }
                    else {Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);}
                }
            });
            backid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editfname.getText().toString().matches("")||editlname.getText().toString().matches("")||editidagent.getText().toString().matches("")) {
                        Toast.makeText(SimRegInfo.this, "INSERT YOUR NAME and  ID NUMBER", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,101);}
                }
            });


            activatesim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a=new Intent(SimRegInfo.this,Activate_Sim.class);
                    startActivity(a);
                }
            });
            //BtnData appear in case offline files exist
            Button BtnData = (Button)findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this,SimRegOfflineDataActivity.class);
                    startActivity(i);
                }
            });
            Button Btnmain = (Button)findViewById(R.id.BtnMainn);
            Btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SimRegInfo.this,MainActivity.class);
                    startActivity(i);
                }
            });

            if (count !=0)
            {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }



        }}


    public void connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url,userName,password);
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog= new DatePickerDialog(
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
        String date= dayOfMonth+"-"+(month+1)+"-"+year;
        editdate.setText(date);


    }
    private Integer getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();


        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if ((today.get(Calendar.DAY_OF_YEAR)+30) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }



        Integer ageInt = new Integer(age);


        return ageInt;
    }
}