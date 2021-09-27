package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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


public class PendingPictures extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    private Connection conn;
    private Button btnmain,btnselectdate,btnnext,btnprevious;
    private boolean connectflag=false;
    private TextView txtdate,txtpagination;
    public ArrayList<PendingPictureListView> simA,simdb;
    private int arraysize=0,pagination=0;
    private int varraysize=0;
    private RecyclerView pendingpicrec;
    private String text,agentNumber;
    private PendingPictureRecViewAdapter adapter;
    String count="0";
    int page=0;
    int pagecount = 0;
    int pagecountremain = 0;
    int pagenumber=0;
    final int[] currentpagenumber = {1};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_pictures);



        pendingpicrec=(RecyclerView) findViewById(R.id.pendingpicrecview);
        btnmain=findViewById(R.id.btnmain);
        btnselectdate=findViewById(R.id.Btnselectdate);
        txtdate=findViewById(R.id.textdate);
        btnnext=findViewById(R.id.btnnext);
        btnprevious=findViewById(R.id.btnprevious);
        txtpagination = findViewById(R.id.txtpagination);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        txtdate.setText(df.format(c));


        //get the agentNumber
        Intent i= this.getIntent();
        agentNumber=i.getStringExtra("agentNumber");
        thread1.start();

        //button main
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("db-offline-to-main", "1");
                intent.putExtra("globalMode","Online");
                intent.putExtra("agentNumber",agentNumber);
                startActivity(intent);
            }
        });

        btnselectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

       btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                int finalCurrentpagenumber = currentpagenumber[0];
                 if (finalCurrentpagenumber == 1){
                    //btnprevious.setClickable(false);
                }else {
                    btnnext.setClickable(true);
                    pagination=pagination-2;
                    if (pagination <=0 ) {pagination=0;}
                    GetClientPendingPictures((pagination *10)+1,(pagination*10)+10);
                    currentpagenumber[0]--;
                    finalCurrentpagenumber = currentpagenumber[0];
                    txtpagination.setText(String.valueOf(finalCurrentpagenumber) + "/" + String.valueOf(pagenumber));
                }

            }

        });

        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                int finalCurrentpagenumber = currentpagenumber[0];
                if (finalCurrentpagenumber == pagenumber){
                    btnnext.setClickable(false);

                } else {
                    //btnprevious.setClickable(true);
                    System.out.println("pagination "+ pagination);
                    GetClientPendingPictures((pagination*10)+1,(pagination*10)+10);
                    currentpagenumber[0]++;
                    finalCurrentpagenumber = currentpagenumber[0];
                    txtpagination.setText(String.valueOf(finalCurrentpagenumber) + "/" + String.valueOf(pagenumber));
                }

            }
        });


    }

    //filling text date according to date picker dialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        txtdate.setText(date);
        pagination=0;
        GetClientPendingPictures(1,10);
    }


    //date picker dialog
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


    //back pressed arrow
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("db-offline-to-main", "0");
        intent.putExtra("globalMode","Online");
        intent.putExtra("agentNumber",agentNumber);
        startActivity(intent);
    }


    private void GetClientPendingPictures(int vfrom, int vto) {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                if (getAgentStatus()==true) {
                // define recyclerview of sitelistview
                simA = new ArrayList<>();
                simdb = new ArrayList<>();

                //Add data for sitelistview recyclerview
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY CLIENT_ID) row_num,CREATED_DATE,CLIENT_ID,AGENT_NUMBER,MOBILE_NUMBER,FRONT_SIDE_ID_STATUS, BACK_SIDE_ID_STATUS,SIGNATURE_STATUS,CLIENT_PHOTO_STATUS from CLIENTS where ( FRONT_SIDE_ID_STATUS='0' or BACK_SIDE_ID_STATUS='0' or SIGNATURE_STATUS='0' or CLIENT_PHOTO_STATUS='0') AND AGENT_NUMBER='"+agentNumber+"' AND TO_DATE(TO_CHAR(CREATED_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('" + txtdate.getText().toString() +"','DD-MM-YYYY') ) T WHERE  row_num >='" + vfrom + "' AND row_num <='" + vto + "' ";
                System.out.println("query is :" +sqlStmt);

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
                        simdb.add(new PendingPictureListView(rs1.getString("CLIENT_ID"),rs1.getString("MOBILE_NUMBER"), rs1.getString("FRONT_SIDE_ID_STATUS"), rs1.getString("BACK_SIDE_ID_STATUS"), rs1.getString("SIGNATURE_STATUS"),rs1.getString("CLIENT_PHOTO_STATUS")));


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
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                arraysize = simdb.size();

                if (arraysize > 0) {
                    //System.out.println("Array Size is : "+arraysize);
                    simA.clear();
                    varraysize = 0;

                    for (i = varraysize; i < 10; i++) {
                        if (varraysize < arraysize) {
                            simA.add(new PendingPictureListView(simdb.get(i).getGlobalSimID(),simdb.get(i).getClientNumer(), simdb.get(i).getFrontStatus(), simdb.get(i).getBackStatus(), simdb.get(i).getSignStatus(),simdb.get(i).getClientStatus()));
                            varraysize = varraysize + 1;
                        }
                    }

                    //connect data to coveragelistadapter
                    System.out.println("pagination before "+ pagination);
                    pagination = pagination + 1;
                    System.out.println("pagination after "+ pagination);
                    adapter = new PendingPictureRecViewAdapter(PendingPictures.this,getIntent().getStringExtra("globalMode"),getIntent().getStringExtra("agentNumber"));
                    adapter.setContacts(simA);
                    pendingpicrec.setAdapter(adapter);
                    pendingpicrec.setLayoutManager(new LinearLayoutManager(PendingPictures.this));

                    Statement stmt2 = null;
                    try {
                        stmt2 = conn.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    String sqlStmt1 = "SELECT COUNT(*) FROM CLIENTS where ( FRONT_SIDE_ID_STATUS='0' or BACK_SIDE_ID_STATUS='0' or SIGNATURE_STATUS='0' or CLIENT_PHOTO_STATUS='0')AND AGENT_NUMBER='"+agentNumber+"' AND TO_DATE(TO_CHAR(CREATED_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('" + txtdate.getText() + "','DD-MM-YYYY') ";
                    ResultSet rs2 = null;

                    try {
                        rs2 = stmt2.executeQuery(sqlStmt1);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while(true){
                        try{
                            if (!rs2.next()) break;
                             count = rs2.getString("COUNT(*)");
                             page = Integer.valueOf(count);
                             pagecount = (page/10);
                             pagecountremain = (page%10);
                             int[] currentpagenumber = {1};

                            if (page == 0){
                                txtpagination.setVisibility(View.INVISIBLE);
                            } else {
                                txtpagination.setVisibility(View.VISIBLE);
                                if (pagecountremain != 0) {
                                    pagenumber = pagecount + 1;
                                } else {
                                    pagenumber = pagecount;
                                }
                                txtpagination.setText(String.valueOf(currentpagenumber[0]) + "/" + String.valueOf(pagenumber));

                                if (currentpagenumber[0] == 1) {
                                    //btnprevious.setClickable(false);
                                }

                                if (currentpagenumber[0] == pagenumber) {
                                    btnnext.setClickable(false);
                                } else {
                                    btnnext.setClickable(true);
                                }

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }try {
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




                }else
                {
                    adapter=null;
                    pendingpicrec.setAdapter(adapter);
                }

            }
            else {
                Toast.makeText(getApplicationContext(),"Access denied",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

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

    Thread thread1 = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    try {
                        System.out.println("Call GetSimData");
                        GetClientPendingPictures(1, 10);
                    }catch(Exception e) {
                        System.out.println(e.toString());
                    }
                    if (connectflag==true) {
                        try  {
                            adapter.notifyDataSetChanged();
                        }catch(Exception e) {
                            System.out.println(e.toString());
                        }
                    } else {

                    }
                    try  {


                    }catch(Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });
        }
    };

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
