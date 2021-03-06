package com.example.aliatsimactivation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ClientChargeListViewActivity extends AppCompatActivity{
    private RecyclerView mobilechargeRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection conn;
    public ArrayList<ClientChargeListView> mobilecharge,mobilechargedb;
    private Button btnprevious,btnnext,btnnew,btndate,btnmain;
    TextView editTextdate;
    DatePickerDialog datePickerDialog;
    private boolean connectflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_mobile_charge_list_view);
        btnprevious = findViewById (R.id.btnprevious);
        btnnext = findViewById (R.id.btnnext);
        btnnew = findViewById (R.id.btnnew);
        btndate = findViewById (R.id.btndate);
        btnmain = findViewById (R.id.btnmain);
        editTextdate = findViewById (R.id.editTextdate);

        try {
            GetMobileChargeData(1, 10);
        }catch (Exception e){
            e.printStackTrace();
        }

        // button new
        btnnew.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // call bla(Vnk new page of siteinfo
                openMobileChargeInfoActivity ( );

            }

            public void openMobileChargeInfoActivity() {
                Intent intent = new Intent (ClientChargeListViewActivity.this, ClientChargeInfoActivity.class);
                intent.putExtra ("message_key", "0");
                startActivity (intent);
            }

        });

        //button previuos
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination = pagination - 2;
                if (pagination <= 0) {
                    pagination = 0;
                }
                GetMobileChargeData (( pagination * 10 ) + 1, ( pagination * 10 ) + 10);
            }

        });

        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetMobileChargeData (( pagination * 10 ) + 1, ( pagination * 10 ) + 10);
            }
        });

        // getting the current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        editTextdate.setText(df.format(c));

        //button Date
        btndate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance ( );
                int year = c.get (Calendar.YEAR);
                int month = c.get (Calendar.MONTH);
                int dayOfMonth = c.get (Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog (ClientChargeListViewActivity.this,
                        new DatePickerDialog.OnDateSetListener ( ) {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10 && month < 9) {
                                    editTextdate.setText (year + "-0" + ( month + 1 ) + "-0" + day);
                                }
                                if (day > 10 && month < 9) {
                                    editTextdate.setText (year + "-0" + ( month + 1 ) + "-" + day);
                                }
                                if (day < 10 && month > 9) {
                                    editTextdate.setText (year + "-" + ( month + 1 ) + "-0" + day);
                                }
                                if (day > 10 && month > 9) {
                                    editTextdate.setText (year + "-" + ( month + 1 ) + "-" + day);
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show ( );

            }

        });

        // On TextChanged action
        editTextdate = (TextView) findViewById (R.id.editTextdate);
        editTextdate.addTextChangedListener (new TextWatcher ( ) {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                boolean flg=false;
                try {
                    if((flg=connecttoDB())==true) {
                        mobilechargeRecView = findViewById(R.id.mobilechargeRecView);
                        mobilecharge = new ArrayList<>();
                        mobilechargedb = new ArrayList<>();

                        //Add data for moobilechargelistview recyclerview
                        Statement stmt2 = null;
                        int i = 0;
                        try {
                            stmt2 = conn.createStatement();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        String sqlStmt2 = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE TO_CHAR(CHARGED_DATE, 'YYYY-MM-DD') = '" + editTextdate.getText() + "' ";

                        ResultSet rs2 = null;
                        try {
                            rs2 = stmt2.executeQuery(sqlStmt2);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        while (true) {
                            try {
                                if (!rs2.next()) break;
                                arraysize = arraysize + 1;
                                mobilechargedb.add(new ClientChargeListView(rs2.getString("MOB_CHARGE_ID"), rs2.getString("CLIENT_SUB_NUMBER"), rs2.getString("AMOUNT"), rs2.getString("RECHARGE_STATUS")));
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
                            conn.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        arraysize = mobilechargedb.size();
                        if (arraysize > 0) {
                            //System.out.println("Array Size is : "+arraysize);
                            mobilecharge.clear();
                            varraysize = 0;
                            for (i = varraysize; i < 10; i++) {
                                if (varraysize < arraysize) {
                                    mobilecharge.add(new ClientChargeListView(mobilechargedb.get(i).getMOBCHARGEID(), mobilechargedb.get(i).getCLIENTSUBNUM(), mobilechargedb.get(i).getAMOUNT(), mobilechargedb.get(i).getRECHARGESTATUS()));
                                    varraysize = varraysize + 1;
                                }
                            }
                            pagination = pagination + 1;
                            //connect data to coveragelistadapter
                            ClientChargeRecViewAdapter adapter = new ClientChargeRecViewAdapter(ClientChargeListViewActivity.this);
                            adapter.setContacts(mobilecharge);
                            mobilechargeRecView.setAdapter(adapter);
                            mobilechargeRecView.setLayoutManager(new LinearLayoutManager(ClientChargeListViewActivity.this));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        //// return to main page
        btnmain.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (getApplicationContext ( ), MainActivity.class);
                intent.putExtra("db-offline-to-main", "0");
                startActivity (intent);
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
            //Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
            System.out.println("error is: " +e.toString());
           // Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }
        return connectflag;
        }



    private void GetMobileChargeData(int vfrom, int vto) {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {

                // define recyclerview of mobilechargelistview
                mobilechargeRecView = findViewById(R.id.mobilechargeRecView);
                mobilecharge = new ArrayList<>();
                mobilechargedb = new ArrayList<>();

                //Add data for moobilechargelistview recyclerview
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER, AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";

                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery(sqlStmt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs1.next()) break;
                        arraysize = arraysize + 1;
                        mobilechargedb.add(new ClientChargeListView(rs1.getString("MOB_CHARGE_ID"), rs1.getString("CLIENT_SUB_NUMBER"), rs1.getString("AMOUNT"), rs1.getString("RECHARGE_STATUS")));
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
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                arraysize = mobilechargedb.size();

                if (arraysize > 0) {
                    //System.out.println("Array Size is : "+arraysize);
                    mobilecharge.clear();
                    varraysize = 0;
                    for (i = varraysize; i < 10; i++) {
                        if (varraysize < arraysize) {
                            mobilecharge.add(new ClientChargeListView(mobilechargedb.get(i).getMOBCHARGEID(), mobilechargedb.get(i).getCLIENTSUBNUM(), mobilechargedb.get(i).getAMOUNT(), mobilechargedb.get(i).getRECHARGESTATUS()));
                            varraysize = varraysize + 1;
                        }
                    }
                    pagination = pagination + 1;
                    //connect data to coveragelistadapter
                    ClientChargeRecViewAdapter adapter = new ClientChargeRecViewAdapter(ClientChargeListViewActivity.this);
                    adapter.setContacts(mobilecharge);
                    mobilechargeRecView.setAdapter(adapter);
                    mobilechargeRecView.setLayoutManager(new LinearLayoutManager(ClientChargeListViewActivity.this));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }


}



