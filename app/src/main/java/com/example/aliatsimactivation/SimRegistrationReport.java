package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SimRegistrationReport extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;
    public ArrayList<SimRegReportListView> simA,simdb;
    private int arraysize=0;
    private int varraysize=0;
    Connection conn;
    private EditText edtdate;
    private RecyclerView simregreport;
    private TextView today_total1,today_success1,today_failed1,today_progress1;
    private TableLayout dailyreport1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_registration_report);

        Button btnmain = (Button) findViewById(R.id.btnmain);
        Button btnsearch = (Button) findViewById(R.id.btnsearchsimregreport);
        Button btndate=(Button) findViewById(R.id.btndatesearch);
        edtdate=(EditText) findViewById(R.id.editTextdate);
        TextView dailyreporttext = (TextView) findViewById(R.id.dailyreporttxt);
        TableLayout dailyreport=(TableLayout) findViewById(R.id.dailyreport);
        simregreport=(RecyclerView) findViewById(R.id.simregreport);
        TextView today_total = findViewById(R.id.today_total);
        TextView today_success = findViewById(R.id.today_success);
        TextView today_failed = findViewById(R.id.today_failed);
        TextView today_progress = findViewById(R.id.today_progress);

        dailyreport1=(TableLayout) findViewById(R.id.dailyreport1);
        today_total1 = findViewById(R.id.today_total1);
        today_success1 = findViewById(R.id.today_success1);
        today_failed1 = findViewById(R.id.today_failed1);
        today_progress1 = findViewById(R.id.today_progress1);

        btndate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

        });
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        edtdate.setText(df.format(c));

        connecttoDB();
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



        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), btnsearch);
                popup.getMenuInflater().inflate(R.menu.sim_reg_report, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.date:
                                dailyreporttext.setVisibility(View.INVISIBLE);
                                dailyreport.setVisibility(View.INVISIBLE);
                                simregreport.setVisibility(View.VISIBLE);
                                btndate.setVisibility(View.VISIBLE);
                                edtdate.setVisibility(View.VISIBLE);

                                return true;

                            case R.id.daily:
                                dailyreporttext.setVisibility(View.VISIBLE);
                                dailyreport.setVisibility(View.VISIBLE);
                                simregreport.setVisibility(View.INVISIBLE);
                                btndate.setVisibility(View.INVISIBLE);
                                edtdate.setVisibility(View.INVISIBLE);

                                connecttoDB();
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

                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popup.show();
            }
        });
        //// END SEARCH
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



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
            System.out.println ("BEFORE");
            Class.forName ("oracle.jdbc.driver.OracleDriver").newInstance ( );
            System.out.println ("AFTER 1");
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("AFTER 2");
        }
        catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println ("error1 is: " + e.toString ( ));
            Toast.makeText (getApplicationContext(), "" + e.toString ( ), Toast.LENGTH_SHORT).show ( );
            Intent intent = new Intent (getApplicationContext ( ), MainActivity.class);
            startActivity (intent);
        }   catch (IllegalAccessException e) {
            System.out.println("error2 is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }   catch (InstantiationException e) {
            System.out.println("error3 is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
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
        edtdate.setText(date);
        connecttoDB();
        simA =new ArrayList<>();
        simdb=new ArrayList<>();

        //Add data for sitelistview recyclerview
        Statement stmt12 = null;
        int j=0;
        try {
            stmt12 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt ="SELECT * FROM (select sim_registration.agent_number,MOBILE_NUMBER ,STATUS , VERIFICATION_CODE,msisdn from SIM_REGISTRATION,sim_register_login where TO_DATE(TO_CHAR(sim_registration.creation_date,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+edtdate.getText()+"','DD-MM-YYYY'))T where msisdn=AGENT_NUMBER ";

        ResultSet rs12 = null;

        try {
            rs12 = stmt12.executeQuery(sqlStmt);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs12.next()) break;
                arraysize=arraysize+1;
                simdb.add(new SimRegReportListView (rs12.getString("MOBILE_NUMBER"),rs12.getString("STATUS"),rs12.getString("VERIFICATION_CODE")));
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
            conn.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize=simdb.size ();

        if (arraysize >0) {
            //System.out.println("Array Size is : "+arraysize);
            simA.clear ( );
            varraysize = 0;

            for (j = varraysize; j < 10; j++) {
                if (varraysize < arraysize) {
                    simA.add (new SimRegReportListView (simdb.get (j).getMsidn ( ), simdb.get (j).getStatus( ), simdb.get (j).getVerification ( )));
                    varraysize = varraysize + 1;}
            }



            //connect data to coveragelistadapter
            SimRegReportRecViewAdapter adapter = new SimRegReportRecViewAdapter (SimRegistrationReport.this);
            adapter.setContacts (simA);
            simregreport.setAdapter (adapter);
            simregreport.setLayoutManager (new LinearLayoutManager(SimRegistrationReport.this));
        }


        dailyreport1.setVisibility(View.VISIBLE);

        connecttoDB();
        Statement stmt1 = null;
        int i = 0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sqlStmt1 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE from SIM_REGISTRATION order by SIM_REG_ID) T WHERE TO_DATE(TO_CHAR(CREATION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+edtdate.getText()+"','DD-MM-YYYY')";
        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                today_total1.setText(rs1.getString("COUNT(*)"));
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
        String sqlStmt2 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Success' AND  TO_DATE(TO_CHAR(CREATION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+edtdate.getText()+"','DD-MM-YYYY') ";

        ResultSet rs2 = null;
        try {
            rs2 = stmt2.executeQuery(sqlStmt2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs2.next()) break;
                today_success1.setText(rs2.getString("COUNT(*)"));
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
        String sqlStmt3 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'Failed' AND TO_DATE(TO_CHAR(CREATION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+edtdate.getText()+"','DD-MM-YYYY') ";

        ResultSet rs3 = null;
        try {
            rs3 = stmt3.executeQuery(sqlStmt3);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs3.next()) break;
                today_failed1.setText(rs3.getString("COUNT(*)"));
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
        String sqlStmt4 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY SIM_REG_ID) row_num,CREATION_DATE,STATUS from SIM_REGISTRATION order by SIM_REG_ID) T WHERE STATUS = 'In Progress' AND TO_DATE(TO_CHAR(CREATION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+edtdate.getText()+"','DD-MM-YYYY') ";

        ResultSet rs4 = null;
        try {
            rs4 = stmt4.executeQuery(sqlStmt4);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs4.next()) break;
                today_progress1.setText(rs4.getString("COUNT(*)"));
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


    }
}
