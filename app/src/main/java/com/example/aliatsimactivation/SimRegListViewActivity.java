package com.example.aliatsimactivation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
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

public class SimRegListViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RecyclerView simregrecview;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    private Connection connsite;
    public ArrayList<SimRegListView> simA,simdb,simA1,simdb1;
    private Button btnprevious,btnnext,btnnew,btnmain,btndelete,btnselectdate;
    private TextView datet,textstatus,txtpagination;
    private boolean connectflag=false;
    private SIMRegViewAdapter adapter;
    private String datestr,text,agentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_reg_list_view);
        btnprevious = findViewById (R.id.btnprevious);
        btnnext = findViewById (R.id.btnnext);
        btnnew= findViewById (R.id.btnnew);
        btnmain=findViewById (R.id.btnmain);
        btnselectdate=findViewById(R.id.Btnselectdate);
        simregrecview = findViewById(R.id.simRecView);
        txtpagination = findViewById(R.id.txtpagination);

        Date c = Calendar.getInstance().getTime();
        datet=findViewById(R.id.textdate);
        textstatus=findViewById(R.id.textstatus);
        textstatus.setVisibility(View.VISIBLE);
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        datet.setText(df.format(c));

        Intent intent = SimRegListViewActivity.this.getIntent();
        String str = intent.getStringExtra("message_key");
        //datestr=str;
        System.out.println("str "+ str);



        try {
            FileInputStream fis = null;

            File file = new File(getFilesDir(), "MSISDN.txt");
            if (file.exists()) {
                System.out.println("file Exists");
                fis = openFileInput("MSISDN.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    System.out.println("text pre : "+text);

                    agentNumber=text;
                }

                System.out.println("agent number : "+agentNumber);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if (str.toString().matches("-100")) {
        //     textstatus.setVisibility(View.GONE);
        //     datestr=str;
        //  } //else {

            /*Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        thread1.start();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });*/
        System.out.println("STARTIN HERE 1");

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                thread1.start();
            }
        };

        handler.postDelayed(r, 1000);
        System.out.println("delayed 1 sec");

        pagination();








        //}


        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (datestr.toString().matches("-100")) {

                }else {
                    pagination=pagination-2;
                    if (pagination <=0 ) {pagination=0;}
                    //GetDataInitial((pagination *10)+1,(pagination*10)+10);
                    GetSimData((pagination *10)+1,(pagination*10)+10);
                }
            }

        });


        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (datestr.toString().matches("-100")) {

                }else {
                    //GetDataInitial((pagination*10)+1,(pagination*10)+10);
                    GetSimData((pagination*10)+1,(pagination*10)+10);
                }
            }
        });

        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("db-offline-to-main", "0");
                intent.putExtra("globalMode","Online");
                startActivity(intent);
            }
        });
        btnselectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });

        datet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (datestr.toString().matches("-100")) {

                }else {
                    GetSimData(1,10);
                }
                pagination();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), SimRegInfo.class);
                intent.putExtra("message_key","0");
                intent.putExtra("db-offline", str);
                intent.putExtra("globalMode","Online");
                startActivity(intent);
            }
        });


    }




   /* @Override
    public void onBackPressed() {
        Intent i = new Intent(SimRegListViewActivity.this, MainActivity.class);
        i.putExtra("message_key", "0");
        i.putExtra("globalMode","Online");
        startActivity(i);
    }*/

    public void GetSimData(int vfrom, int vto) {
        boolean flg=false;
        try {
            System.out.println("CONNECT HERE");
            if((flg=connecttoDB())==true) {

                System.out.println("CONNECT HERE");
                // define recyclerview of sitelistview

                simA = new ArrayList<>();
                simdb = new ArrayList<>();
                datestr="0";
                //Add data for sitelistview recyclerview
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = connsite.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("you are here");


                System.out.println("text : "+agentNumber);
                String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY CREATED_DATE DESC) row_num,CREATED_DATE,AGENT_NUMBER,CLIENT_ID,FIRST_NAME,LAST_NAME,MOBILE_NUMBER,STATUS from CLIENTS where TO_DATE(TO_CHAR(CREATED_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('" + datet.getText() + "','DD-MM-YYYY')) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "' AND AGENT_NUMBER='"+agentNumber+"'";
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
                        simdb.add(new SimRegListView(rs1.getString("CLIENT_ID"), rs1.getString("ROW_NUM"),rs1.getString("FIRST_NAME") + " " + rs1.getString("LAST_NAME"), rs1.getString("MOBILE_NUMBER"), rs1.getString("STATUS")));                    } catch (SQLException throwables) {
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
                    connsite.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                //Fill Listview
                FilllistView();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void FilllistView() {

        int i=0;
        arraysize = simdb.size();
        if (arraysize > 0) {
            //System.out.println("Array Size is : "+arraysize);
            simA.clear();
            varraysize = 0;

            for (i = varraysize; i < 10; i++) {
                if (varraysize < arraysize) {
                    simA.add(new SimRegListView(simdb.get(i).getSimRegListViewId(), simdb.get(i).getRow(), simdb.get(i).getName(), simdb.get(i).getMobile(), simdb.get(i).getStatus()));                    varraysize = varraysize + 1;
                }
            }


            pagination = pagination + 1;
            //connect data to coveragelistadapter
            adapter = new SIMRegViewAdapter(SimRegListViewActivity.this);
            adapter.setContacts(simA);
            simregrecview.setAdapter(adapter);
            simregrecview.setLayoutManager(new LinearLayoutManager(SimRegListViewActivity.this));
        }
        else {
            adapter=null;
            simregrecview.setAdapter(adapter);
        }

        //update data in Listview
        try  {
            adapter.notifyDataSetChanged();
        }catch(Exception e) {
            System.out.println(e.toString());
        }

        try  {
            textstatus.setVisibility(View.GONE);
        }catch(Exception e) {
            System.out.println(e.toString());
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
        datet.setText(date);

        // connect to DB
        if (datestr.toString().matches("-100")) {

        } else {



            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        thread1.start();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });

        }
    }


    public boolean connecttoDB() throws Exception {
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                connsite = DriverManager.getConnection(url, userName, password);

                if (connsite != null) {
                    connectflag = true;
                } else {
                    connectflag = false;
                }

                //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
            } catch (SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
                System.out.println("START NEW HERE 4");
                System.out.println("error 1 is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
                finishActivity(0);
            } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                System.out.println("error 2 is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
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
                        GetSimData(1, 10);
                    }catch(Exception e) {
                        System.out.println(e.toString());
                    }
                    if (connectflag==true) {
                        try  {
                            adapter.notifyDataSetChanged();
                            datestr="0";
                        }catch(Exception e) {
                            System.out.println(e.toString());
                        }
                    } else {
                        datestr="-100";
                        System.out.println(datestr);
                    }
                    try  {
                        textstatus.setVisibility(View.GONE);

                    }catch(Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });
        }
    };

    private void pagination() {

        boolean flg = false;
        try {
            if ((flg = connecttoDB()) == true) {
                // define recyclerview of sitelistview
                simA = new ArrayList<>();
                simdb = new ArrayList<>();
                datestr = "0";
                //Add data for sitelistview recyclerview
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = connsite.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String sqlStmt = "SELECT COUNT(*) FROM CLIENTS where TO_DATE(TO_CHAR(CREATED_DATE,'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('" + datet.getText() + "','DD-MM-YYYY')";

                ResultSet rs1 = null;

                try {
                    rs1 = stmt1.executeQuery(sqlStmt);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs1.next()) break;

                        String count = rs1.getString("COUNT(*)");
                        int page = Integer.valueOf(count);
                        int pagecount = (page/10);
                        int pagecountremain = (page%10);
                        int pagenumber;
                        final int[] currentpagenumber = {1};

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

                            if (currentpagenumber[0] == 1){
                                btnprevious.setClickable(false);
                            }

                            if (currentpagenumber[0] == pagenumber){
                                btnnext.setClickable(false);
                            } else {
                                btnnext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        btnprevious.setClickable(true);
                                        if (datestr.toString().matches("-100")) {

                                        }else {
                                            //GetDataInitial((pagination*10)+1,(pagination*10)+10);
                                            GetSimData((pagination*10)+1,(pagination*10)+10);
                                        }
                                        currentpagenumber[0]++;
                                        int finalCurrentpagenumber = currentpagenumber[0];
                                        txtpagination.setText(String.valueOf(finalCurrentpagenumber) + "/" + String.valueOf(pagenumber));
                                        if (finalCurrentpagenumber == pagenumber){
                                            btnnext.setClickable(false);
                                        }
                                    }
                                });

                                btnprevious.setOnClickListener (new View.OnClickListener ( ) {
                                    @Override
                                    public void onClick(View v) {
                                        btnnext.setClickable(true);
                                        if (datestr.toString().matches("-100")) {

                                        }else {
                                            pagination=pagination-2;
                                            if (pagination <=0 ) {pagination=0;}
                                            //GetDataInitial((pagination *10)+1,(pagination*10)+10);
                                            GetSimData((pagination *10)+1,(pagination*10)+10);
                                        }
                                        currentpagenumber[0]--;
                                        int finalCurrentpagenumber = currentpagenumber[0];
                                        txtpagination.setText(String.valueOf(finalCurrentpagenumber) + "/" + String.valueOf(pagenumber));
                                        if (finalCurrentpagenumber == 1){
                                            btnprevious.setClickable(false);
                                        }
                                    }
                                });

                            }


                        }
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
                    connsite.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

