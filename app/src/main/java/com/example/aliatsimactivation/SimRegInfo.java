package com.example.aliatsimactivation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button submit;
    private Button sign;
    private String nationality="";
    private RadioButton kenya,foreign;
    private TextView editdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simactivityview);
        TextView editfname=(TextView)findViewById(R.id.efirstname);
        TextView editmname=(TextView)findViewById(R.id.emiddlename);
        TextView editlname=(TextView)findViewById(R.id.elastname);
        TextView editmobile=(TextView)findViewById(R.id.emobilenumber);
         editdate=(TextView)findViewById(R.id.edateofbirth);
        kenya=findViewById(R.id.ekenian);
        foreign=findViewById(R.id.eforien);
        TextView editaltnumber=(TextView)findViewById(R.id.ealtirnativenumber);
        TextView editemail=(TextView)findViewById(R.id.email);
        TextView editphylocation=(TextView)findViewById(R.id.ephysicallocation);
        TextView editpost=(TextView)findViewById(R.id.epostaladdress);
        sign= findViewById(R.id.bsigniture);
        submit=findViewById(R.id.submitbtn);
        Spinner s = (Spinner)findViewById(R.id.spinner);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(), SimRegSignature.class);
                startActivity(a);
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


                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER) values " +
                                "('" + globalsimid + "' ,sysdate, sysdate,'" + editfname.getText() + "','" + editmname.getText() + "', '" + editlname.getText() + "','" + editmobile.getText() + "',TO_DATE('"+editdate.getText()+"','DD-MM-YYYY'),'" + nationality + "','" + editaltnumber.getText() + "','" + editemail.getText() + "','" + editphylocation.getText() + "','" + editpost.getText() + "','" + gender + "')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                        ///added for pass data in fragment


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
            } });

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