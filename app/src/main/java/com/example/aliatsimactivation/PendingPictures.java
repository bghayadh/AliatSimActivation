package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PendingPictures extends AppCompatActivity {
    private Connection conn;
    private boolean connectflag=false;
    public ArrayList<PendingPictureListView> simA,simdb;
    private int arraysize=0;
    private int varraysize=0;
    private RecyclerView pendingpicrec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_pictures);

        pendingpicrec=(RecyclerView) findViewById(R.id.pendingpicrecview);


        GetClientPendingPictures(1,100);
    }



    private void GetClientPendingPictures(int vfrom, int vto) {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
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

                String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY CREATION_DATE) row_num,SIM_REG_ID,CREATION_DATE,MOBILE_NUMBER,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS,SIGNATURE_STATUS,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE from SIM_REGISTRATION where FRONT_SIDE_ID_STATUS=0 or BACK_SIDE_ID_STATUS=0 or SIGNATURE_STATUS=0 ) T WHERE row_num >='" + vfrom + "' AND row_num <='" + vto + "'" +
                        " ORDER BY CREATION_DATE DESC" ;
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
                        simdb.add(new PendingPictureListView(rs1.getString("SIM_REG_ID"),rs1.getString("MOBILE_NUMBER"), rs1.getString("FRONT_SIDE_ID_STATUS"), rs1.getString("BACK_SIDE_ID_STATUS"), rs1.getString("SIGNATURE_STATUS"),rs1.getString("ID_FRONT_SIDE_PHOTO"),rs1.getString("ID_BACK_SID_PHOTO"),rs1.getString("SIGNATURE")));


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

                arraysize = simdb.size();

                if (arraysize > 0) {
                    //System.out.println("Array Size is : "+arraysize);
                    simA.clear();
                    varraysize = 0;

                    for (i = varraysize; i < 10; i++) {
                        if (varraysize < arraysize) {
                            simA.add(new PendingPictureListView(simdb.get(i).getGlobalSimID(),simdb.get(i).getClientNumer(), simdb.get(i).getFrontStatus(), simdb.get(i).getBackStatus(), simdb.get(i).getSignStatus(),simdb.get(i).getFrontImage(),simdb.get(i).getBackImage(),simdb.get(i).getSignImage()));
                            varraysize = varraysize + 1;
                        }
                    }



                    //connect data to coveragelistadapter
                    PendingPictureRecViewAdapter adapter = new PendingPictureRecViewAdapter(PendingPictures.this,getIntent().getStringExtra("globalMode"));
                    adapter.setContacts(simA);
                    pendingpicrec.setAdapter(adapter);
                    pendingpicrec.setLayoutManager(new LinearLayoutManager(PendingPictures.this));

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

}