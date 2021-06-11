package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimRegInfo extends AppCompatActivity {
    private String gender=null;
    public Connection conn;
    private String globalsimid;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simactivityview);
        TextView editfname=(TextView)findViewById(R.id.efirstname);
        TextView editmname=(TextView)findViewById(R.id.emiddlename);
        TextView editlname=(TextView)findViewById(R.id.ellastname);
        TextView editmobile=(TextView)findViewById(R.id.emobilenumber);
        TextView editdate=(TextView)findViewById(R.id.edateofbirth);
        TextView editnationality=(TextView)findViewById(R.id.enationality);
        TextView editaltnumber=(TextView)findViewById(R.id.ealtirnativenumber);
        TextView editemail=(TextView)findViewById(R.id.email);
        TextView editphylocation=(TextView)findViewById(R.id.ephysicallocation);
        TextView editpost=(TextView)findViewById(R.id.epostaladdress);
        submit=findViewById(R.id.submitbtn);
        Spinner s = (Spinner)findViewById(R.id.spinner);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s.getSelectedItemPosition()==0){

                    gender="Male";
                }
                if(s.getSelectedItemPosition()==1){

                    gender="Female";
                }
                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String simID;
                simID= "SIMREG_"+year+"_" ;
                connecttoDB();

                PreparedStatement stmtinsert1 = null;
                try {
                    // if it is a new Warehouse we will use insert

                        Statement stmt1 = null;
                        stmt1 = conn.createStatement ( );
                        String sqlStmt = "select SIM_REG_SEQ.nextval as nbr from dual";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery (sqlStmt);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        while (true) {
                            try {
                                if (!rs1.next ( )) break;
                                globalsimid=simID+rs1.getString ("nbr");
                                //System.out.println(rs1.getString("compteur"));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        // send data from fragment to super activity


                        stmtinsert1 = conn.prepareStatement("insert into SIM_REGISTRATION (SIM_REG_ID,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER) values " +
                                "('"+globalsimid +"' ,sysdate, sysdate,'"+ editfname.getText()  +"','"+ editmname.getText ()  +"', '"+ editlname.getText ()  +"','" + editmobile.getText ()  +"','"+ editdate.getText ()  +"','"+editnationality.getText()+"','"+editaltnumber.getText()+"','"+editemail.getText()+"','"+editphylocation.getText()+"','"+editpost.getText()+"','"+gender+"')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                        ///added for pass data in fragment





                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText (SimRegInfo.this,"Saving Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtinsert1.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
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
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }


}