package com.example.aliatsimactivation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    //define buttons
    private Button BtnMobCharge,BtnLogin,BtnSIMReg,BtnSimReport,BtnSimSwap,BtnSubscription,BtnSimStatus,BtnExit;
    Connection conn;
    private boolean connectflag=false;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        //initialize objects on Load
        BtnSIMReg=findViewById(R.id.Btnsimreg);
        BtnMobCharge=findViewById(R.id.Btnmobcharge);
        BtnSimReport=findViewById(R.id.Btnsimreport);
        BtnSimSwap=findViewById(R.id.Btnsimswap);
        BtnSubscription=findViewById(R.id.Btnsubscription);
        BtnSimStatus=findViewById(R.id.Btnsimstatus);
        BtnLogin=findViewById(R.id.BtnLogin);
        BtnExit=findViewById(R.id.BtnExit);

        TextView today_total = findViewById(R.id.today_total);
        TextView today_success = findViewById(R.id.today_success);
        TextView today_failed = findViewById(R.id.today_failed);
        TextView today_progress = findViewById(R.id.today_progress);
        TextView week_total = findViewById(R.id.week_total);
        TextView week_success = findViewById(R.id.week_success);
        TextView week_failed = findViewById(R.id.week_failed);
        TextView week_progress = findViewById(R.id.week_progress);
        TextView month_total = findViewById(R.id.month_total);
        TextView month_success = findViewById(R.id.month_success);
        TextView month_failed = findViewById(R.id.month_failed);
        TextView month_progress = findViewById(R.id.month_progress);
        boolean flg=false;
        System.out.println("start openning");

        System.out.println("flag : "+connecttoDB());
            if((flg=connecttoDB())==true) {

                System.out.println("read from database");
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt1 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE from SIM_REGISTRATION order by SIM_REG_ID) T WHERE CREATION_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery(sqlStmt1);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs1.next()) break;
                        today_total.setText(rs1.getString("COUNT(*)"));
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
                    stmt1.close();
                    //conn.close ( );
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                //Today success report
                Statement stmt2 = null;
                try {
                    stmt2 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt2 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Success' AND CREATION_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

                ResultSet rs2 = null;
                try {
                    rs2 = stmt2.executeQuery(sqlStmt2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs2.next()) break;
                        today_success.setText(rs2.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                //Today failed report
                Statement stmt3 = null;
                try {
                    stmt3 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt3 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Failed' AND CREATION_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

                ResultSet rs3 = null;
                try {
                    rs3 = stmt3.executeQuery(sqlStmt3);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs3.next()) break;
                        today_failed.setText(rs3.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs3.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt3.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                //Today In Progress report
                Statement stmt4 = null;
                try {
                    stmt4 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt4 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'In Progress' AND CREATION_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

                ResultSet rs4 = null;
                try {
                    rs4 = stmt4.executeQuery(sqlStmt4);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs4.next()) break;
                        today_progress.setText(rs4.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs4.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt4.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Week Total Report
                Statement stmt5 = null;
                try {
                    stmt5 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt5 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE from SIM_REGISTRATION order by SIM_REG_ID) T WHERE CREATION_DATE  >= sysdate -7 ";

                ResultSet rs5 = null;
                try {
                    rs5 = stmt5.executeQuery(sqlStmt5);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs5.next()) break;
                        week_total.setText(rs5.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs5.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt5.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Week Success Report
                Statement stmt6 = null;
                try {
                    stmt6 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt6 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Success' AND CREATION_DATE  >= sysdate -7 ";

                ResultSet rs6 = null;
                try {
                    rs6 = stmt6.executeQuery(sqlStmt6);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs6.next()) break;
                        week_success.setText(rs6.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs6.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt6.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Week Failed Report
                Statement stmt7 = null;
                try {
                    stmt7 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt7 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Failed' AND CREATION_DATE  >= sysdate -7 ";

                ResultSet rs7 = null;
                try {
                    rs7 = stmt7.executeQuery(sqlStmt7);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs7.next()) break;
                        week_failed.setText(rs7.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs7.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt7.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Week In Progress Report
                Statement stmt8 = null;
                try {
                    stmt8 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt8 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'In Progress' AND CREATION_DATE  >= sysdate -7 ";

                ResultSet rs8 = null;
                try {
                    rs8 = stmt8.executeQuery(sqlStmt8);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs8.next()) break;
                        week_progress.setText(rs8.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs8.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt8.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Month Total Report
                Statement stmt9 = null;
                try {
                    stmt9 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt9 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE CREATION_DATE  >= sysdate -30  ";

                ResultSet rs9 = null;
                try {
                    rs9 = stmt9.executeQuery(sqlStmt9);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs9.next()) break;
                        month_total.setText(rs9.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs9.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt9.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Month Success Report
                Statement stmt10 = null;
                try {
                    stmt10 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt10 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Success' AND CREATION_DATE  >= sysdate -30 ";

                ResultSet rs10 = null;
                try {
                    rs10 = stmt10.executeQuery(sqlStmt10);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs10.next()) break;
                        month_success.setText(rs10.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs10.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt10.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Month Failed Report
                Statement stmt11 = null;
                try {
                    stmt11 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt11 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Failed' AND CREATION_DATE  >= sysdate -30 ";

                ResultSet rs11 = null;
                try {
                    rs11 = stmt11.executeQuery(sqlStmt11);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs11.next()) break;
                        month_failed.setText(rs11.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs11.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt11.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                // Month In Progress Report
                Statement stmt12 = null;
                try {
                    stmt12 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sqlStmt12 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'In Progress' AND CREATION_DATE  >= sysdate -30 ";

                ResultSet rs12 = null;
                try {
                    rs12 = stmt12.executeQuery(sqlStmt12);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs12.next()) break;
                        month_progress.setText(rs12.getString("COUNT(*)"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs12.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt12.close();
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                System.out.println("database not connected");
            }





        // check if we have permission to get our location in manifest xml file
        try {
            if (ContextCompat.checkSelfPermission (getApplicationContext ( ), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        // click to move to Sim Registration List
        BtnSIMReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext ( )
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Toast.makeText(MainActivity.this,  "Welcome to Sim Registration page",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(MainActivity.this, SimRegListViewActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,  "Not Connected",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),SimRegInfo.class);
                    startActivity(i);
                }
            }

        });



        // click to move to Mobile Charge List
        BtnMobCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check network connection
                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext ( )
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                 //   Toast.makeText(MainActivity.this,  "Welcome to Mobile Charge page",Toast.LENGTH_SHORT).show();
                   // Intent intent =new Intent(MainActivity.this, ClientChargeListViewActivity.class);
                    //startActivity(intent);
                } else {
                   // Toast.makeText(MainActivity.this,  "No Connection",Toast.LENGTH_SHORT).show();
                    //Intent i=new Intent(getApplicationContext(),ClientChargeInfoActivity.class);
                    //startActivity(i);
                }


            }
        });


        //open sim registration reports
        BtnSimReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Welcome To Sim Registration Reports",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MainActivity.this,SimRegistrationReport.class);
                startActivity(intent);
            }
        });
        // exit button
        BtnLogin.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(MainActivity.this,UserLoginActivity.class);
                intent.putExtra("key","Back");
                startActivity(intent);
            }
        });


        //// return to main page
        BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();
            }
        });

    }

    public boolean connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url,userName,password);
            if(conn != null){
                connectflag=true;
            }
            else{connectflag=false;}

            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }
        return connectflag;
    }

}