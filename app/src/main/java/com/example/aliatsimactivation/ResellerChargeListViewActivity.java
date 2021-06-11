package com.example.aliatsimactivation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

public class ResellerChargeListViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private RecyclerView resellerRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection connsite;
    public ArrayList<com.example.aliatsimactivation.ResellerChargeListView>resellers,resellerdb,resellers1,resellerdb1;
    private Button btnprevious,btnnext,btnnew,btnmain,btndel,btndate;
    private TextView datetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseller_charge_list_view);
        btnprevious=findViewById(R.id.btnPrevious);
        btnnext=findViewById(R.id.btnNext);
        btnnew=findViewById(R.id.btnNew);
        btnmain=findViewById(R.id.BtnMain);
        btndel=findViewById(R.id.BtnDel);
        btndate=findViewById(R.id.BtnDate);
        datetext=findViewById(R.id.DATE);
        Date c = Calendar.getInstance().getTime();

        System.out.println("Current time=> "+c);
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        datetext.setText(df.format(c));
        GetResellerData(1,5);
        //button previous
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;

                if (pagination <=0 ) {pagination=0;}

                GetResellerData((pagination *5)+1,(pagination*5)+5);

            }

        });
        //button next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                GetResellerData((pagination*5)+1,(pagination*5)+5);

            }
        });
        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResellerChargeListViewActivity.this, ResellerChargeInfoActivity.class);
                intent.putExtra("message_key", "0");
                startActivity(intent);
            }
        });

    }
    public void GetResellerData(int vfrom, int vto) {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            connsite = DriverManager.getConnection(url,userName,password);
            // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (ResellerChargeListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (ResellerChargeListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (ResellerChargeListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }



        // define recyclerview of sitelistview
        resellerRecView=findViewById(R.id.resellerrecview);
        resellers =new ArrayList<>();
        resellerdb=new ArrayList<>();
        //Add data for sitelistview recyclerview
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY RES_CHARGE_ID) row_num,RES_CHARGE_ID,RESELLER_SUB_NUMBER, TO_DATE(TO_CHAR(CHARGED_DATE ,'DD-MM-YYYY'),'DD_MM_YYYY') as CHARGED_DATE ,AMOUNT, RECHARGE_STATUSE from RESELLER_CHARGE where TO_DATE(TO_CHAR(CHARGED_DATE, 'DD-MM-YYYY'),'DD-MM-YYYY') =TO_DATE('"+datetext.getText()+"','DD-MM-YYYY')) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";
        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize=arraysize+1;
                resellerdb.add(new com.example.aliatsimactivation.ResellerChargeListView(rs1.getString("RES_CHARGE_ID"),rs1.getString("RESELLER_SUB_NUMBER"),rs1.getString("CHARGED_DATE"),rs1.getString("AMOUNT"),rs1.getString("RECHARGE_STATUSE")));
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
            connsite.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize=resellerdb.size ();

        if (arraysize >0) {
            //System.out.println("Array Size is : "+arraysize);
            resellers.clear ( );
            varraysize = 0;
            for (i = varraysize; i < 5; i++) {
                if (varraysize < arraysize) {
                    resellers.add (new com.example.aliatsimactivation.ResellerChargeListView(resellerdb.get (i).getChargeID ( ), resellerdb.get (i).getMOBILENUMBER ( ), resellerdb.get (i).getDATE ( ), resellerdb.get (i).getAMOUNT ( ), resellerdb.get (i).getSTATUS ( )));
                    varraysize = varraysize + 1;
                }
            }
            pagination = pagination + 1;
            //connect data to coveragelistadapter
            com.example.aliatsimactivation.ResellerchargeRecViewAdapter adapter = new com.example.aliatsimactivation.ResellerchargeRecViewAdapter(ResellerChargeListViewActivity.this);
            adapter.setContacts(resellers);
            resellerRecView.setAdapter (adapter);
            resellerRecView.setLayoutManager (new LinearLayoutManager(ResellerChargeListViewActivity.this));
        }
    }
    private void showDatePickerDialog(){
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
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth+"-"+(month+1)+"-"+year;
        datetext.setText(date);
        GetResellerData(1,5);

}}