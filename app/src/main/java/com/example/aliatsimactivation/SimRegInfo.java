package com.example.aliatsimactivation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimRegInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private String gender=null;
    public Connection conn;
    private String globalsimid;
    private Button submit,frontid,backid,activatesim;
    private Button sign;
    private File file;
    private String nationality="";
    private RadioButton kenya,foreign;
    private CheckBox checkBox;
    private TextView editdate, editmname;
    TextView editlname,editfname,textF,textB,textS;
    private String file1 = "MSISDN.txt";
    private String s0,s1,Result,FRONT,BACK,SIGN;
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

        editagent=(TextView)findViewById(R.id.eagentnum);
         editidagent=(TextView)findViewById(R.id.eagentid);
        Intent intent = SimRegInfo.this.getIntent();
        String str = intent.getStringExtra("message_key");
        globalsimid = str.toString();
        sign= findViewById(R.id.bsigniture);
        submit=findViewById(R.id.submitbtn);
        submit.setEnabled(false);
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
        connecttoDB();

        PreparedStatement stmtinsert1 = null;

        try {
            // if it is a new Warehouse we will use insert
            if (globalsimid!="0") {

                Statement stmt1 = null;
                stmt1 = conn.createStatement ( );
                String sqlStmt = "select FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO FROM SIM_REGISTRATION where SIM_REG_ID = '"+globalsimid+"'";
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
                        editdate.setText(rs1.getString("DATE_OF_BIRTH").substring(0,10));
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
                        editidagent.setText(rs1.getString("AGENT_ID"));
                        textF.setText(rs1.getString("ID_FRONT_SIDE_PHOTO"));
                        textB.setText(rs1.getString("ID_BACK_SID_PHOTO"));
                        textS.setText(rs1.getString("SIGNATURE"));







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
                textS.setText(SIGN);
                a.putExtra("sign",SIGN);
                startActivity(a);}
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

                if (editfname.getText().toString().matches("")){
                    editfname.setError("Empty Field");
                }
                 if (editmname.getText().toString().matches("")) {
                    editmname.setError("Empty Field");
                }
                 if(editlname.getText().toString().matches("")){
                    editlname.setError("Empty Field");
                }
                 if(editmobile.getText().toString().matches("")){
                    editmobile.setError("Empty Field");
                }
                 if(editaltnumber.getText().toString().matches("")){
                    editaltnumber.setError("Empty Field");
                }
                 if(editemail.getText().toString().matches("")) {
                    editemail.setError("Empty Field");
                }
                 if(editphylocation.getText().toString().matches("")){
                    editphylocation.setError("Empty Field");
                }
                 if(editpost.getText().toString().matches("")) {
                    editpost.setError("Empty Field");
                }
                else{
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
                                     Date date = new Date();
                                     Calendar calendar = new GregorianCalendar();
                                     calendar.setTime(date);
                                     int year = calendar.get(Calendar.YEAR);
                                     String simID;
                                     simID = "SIMREG_" + year + "_";
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


                                             stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO) values " +
                                                     "('" + globalsimid + "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('" + editdate.getText() + "','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "','" + editagent.getText() + "','" + editidagent.getText() + "','" + SIGN + "','" + FRONT + "','" + BACK + "')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                                             ///added for pass data in fragment
                                         }else{

                                             stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set CREATION_DATE=sysdate,LAST_MODIFIED_DATE=sysdate,FIRST_NAME='"+editfname.getText()+"',MIDDLE_NAME='" + editmname.getText() + "',LAST_NAME='" + editlname.getText() +"',MOBILE_NUMBER='" + editmobile.getText() + "',NATIONALITY='" + nationality + "',ALTERNATIVE_NUMBER='"+editaltnumber.getText()+"',EMAIL_ADDRESS='"+editemail.getText()+"',PHISICAL_LOCATION='"+editphylocation.getText()+"',POSTAL_ADDRESS='"+editpost.getText()+"',GENDER='"+gender+"',AGENT_NUMBER='"+editagent.getText()+"',AGENT_ID='"+editidagent.getText()+"',SIGNATURE='"+SIGN+"',ID_FRONT_SIDE_PHOTO='"+FRONT+"',ID_BACK_SID_PHOTO='"+BACK+"' where SIM_REG_ID ='"+globalsimid+"'");
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
       checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   submit.setEnabled(true);
               }
               else {
                   submit.setEnabled(false);
               }
           }
       });
       activatesim.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent a=new Intent(SimRegInfo.this,Activate_Sim.class);
                startActivity(a);
           }
       });




    }


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
}