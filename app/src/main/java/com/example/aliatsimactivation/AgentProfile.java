package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class AgentProfile extends AppCompatActivity {
    private String agentNumber,pincode,gimgstatus,AgentImage,value,regionName,regionid="0";
    private EditText edtlong,edtlat,edtfname,edtlname,edtdname,edtaddress,edtemail;
    private ImageView agentImage;
    private ImageButton backarrow;
    private Button update;
    private Spinner spregion;
    private boolean connectflag=false;
    private TextView agentname,agentMSISDN,edit,edtstatus;
    private Connection conn;
    private String agentSFTPpath="/usr/share/tomcat/webapps/alm/resources/";
    SFTP sftp = new SFTP();
    String user = sftp.getUser();
    String pass = sftp.getPass();
    String host = sftp.getServer();
    int e = sftp.getPort();
    private Drawable ddd;
    private GpsTracker gpsTracker;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("globalMode", "Online");
        intent.putExtra("db-offline-to-main", "0");
        intent.putExtra("agentNumber", agentNumber);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);
        agentname=findViewById(R.id.fullname);
        agentImage=findViewById(R.id.agentpic);
        agentMSISDN=findViewById(R.id.agentMSISDN);
        edtlong=findViewById(R.id.edtlongitude);
        edtlat=findViewById(R.id.edtlattitude);
        edtfname=findViewById(R.id.edtfname);
        edtlname=findViewById(R.id.edtlname);
        edtdname=findViewById(R.id.edtdspname);
        edtaddress=findViewById(R.id.edtaddress);
        edtemail=findViewById(R.id.edtemail);
        spregion=findViewById(R.id.regionSpinner);
        edtstatus=findViewById(R.id.edtstatus);
        edit=findViewById(R.id.editfields);
        backarrow=findViewById(R.id.backarrow);
        update=findViewById(R.id.update);


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("globalMode", "Online");
                intent.putExtra("db-offline-to-main", "0");
                intent.putExtra("agentNumber", agentNumber);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setTextColor(Color.RED);
                edtfname.setEnabled(true);
                edtlname.setEnabled(true);
                edtdname.setEnabled(true);
                edtaddress.setEnabled(true);
                edtemail.setEnabled(true);
                spregion.setEnabled(true);
            }
        });
        // call class Gps to get our location
        gpsTracker = new GpsTracker (getApplicationContext ( ));
        if (gpsTracker.canGetLocation ( )) {
            double latitude = gpsTracker.getLatitude ( );
            double longitude = gpsTracker.getLongitude ( );
            edtlat.setText(String.valueOf(latitude));
            edtlong.setText(String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert ( );
            edtlat.setText("0");
            edtlong.setText("0");
        }

        Intent i=getIntent();
        agentNumber=i.getStringExtra("agentNumber");
        pincode=i.getStringExtra("pincode");
        agentMSISDN.setText(agentNumber);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                threadload.start();



            }
        });



        spregion.setOnTouchListener(spinnerOnTouch);
        spregion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //int valeur=spregion.getAdapter().getCount();
                regionName = parent.getItemAtPosition(position).toString(); //this is your selected item
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(),"Updating please wait..",Toast.LENGTH_LONG).show();
                    PreparedStatement stmtinsert1 = null;

                    if (regionName.equalsIgnoreCase("None")) {
                        regionid = "0";
                    } else {
                        regionid = getRegionID(spregion.getSelectedItem().toString());
                        regionName = spregion.getSelectedItem().toString();
                    }
                        String fullname = edtfname.getText().toString()+" "+edtlname.getText().toString();
                        stmtinsert1 = conn.prepareStatement("update AGENT set LAST_MODIFIED_DATE=sysdate,FIRST_NAME='" + edtfname.getText() + "',DISPLAY_NAME='" + edtdname.getText() + "',LAST_NAME='" + edtlname.getText() + "',FULL_NAME='" + fullname + "' ,ADDRESS='" + edtaddress.getText() + "',EMAIL='" + edtemail.getText().toString() + "',REGION_NAME='" + regionName + "',REGION_ID='" + regionid + "' Where MSISDN='"+agentNumber+"' AND PIN_CODE='"+pincode.trim()+"'");


                    try {
                        stmtinsert1.executeUpdate();
                        Toast.makeText(getApplicationContext(),"Updating completed",Toast.LENGTH_LONG).show();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        stmtinsert1.close();
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("globalMode", "Online");
                    intent.putExtra("db-offline-to-main", "0");
                    intent.putExtra("agentNumber", agentNumber);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                thread1.start();
            }
            return false;
        }




    };

    public void getAgent()
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    // if it is a new Warehouse we will use insert

                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "select FIRST_NAME,LAST_NAME,DISPLAY_NAME,ADDRESS,EMAIL,STATUS,REGION_NAME,AGENT_IMAGE,AGENT_FRONT_ID,AGENT_BACK_ID,AGENT_IMAGE_STATUS,FRONT_SIDE_ID_STATUS,BACK_SIDE_ID_STATUS,LONGITUDE,LATITUDE,FULL_NAME FROM AGENT where MSISDN='"+agentNumber+"' AND PIN_CODE='"+pincode.trim()+"'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            FilldatafromDataBase(rs1.getString("FIRST_NAME"),rs1.getString("LAST_NAME"),rs1.getString("DISPLAY_NAME"),rs1.getString("ADDRESS"),rs1.getString("EMAIL"),rs1.getString("REGION_NAME"),rs1.getString("AGENT_IMAGE"),rs1.getString("AGENT_FRONT_ID"),rs1.getString("AGENT_BACK_ID"),rs1.getString("AGENT_IMAGE_STATUS"),rs1.getString("FRONT_SIDE_ID_STATUS"),rs1.getString("BACK_SIDE_ID_STATUS"),rs1.getString("FULL_NAME"),rs1.getString("STATUS"));



                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    rs1.close();
                    stmt1.close();


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void FilldatafromDataBase(String firstname,String lastname,String displayname,String address,String email,String region,String agentimg,String frontid,String backid,String agentimgstatus,String frontidstatus,String backidstatus, String fullname,String status) {
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                agentname.setText(fullname);
                gimgstatus=agentimgstatus;
                AgentImage=agentimg;
                edtfname.setText(firstname);
                edtfname.setEnabled(false);
                edtlname.setText(lastname);
                edtlname.setEnabled(false);
                edtdname.setText(displayname);
                edtdname.setEnabled(false);
                edtaddress.setText(address);
                edtaddress.setEnabled(false);
                edtemail.setText(email);
                edtemail.setEnabled(false);
                ArrayList<String> my_array = new ArrayList<String>();
                my_array.add(region);
                ArrayAdapter my_Adapter = new ArrayAdapter(AgentProfile.this,R.layout.spinner_row,my_array);
                spregion.setAdapter(my_Adapter);
                spregion.setEnabled(false);
                edtstatus.setText(status);
                edtstatus.setTextColor(Color.GREEN);
                System.out.println("status : "+gimgstatus);
                System.out.println("image : "+AgentImage);

                try {
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking", "no");

                    JSch jSch = new JSch();
                    Session session = jSch.getSession(user, host, e);
                    System.out.println("Step1");
                    session.setPassword(pass);
                    session.setConfig(config);
                    session.connect();
                    System.out.println("Step Connect");
                    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();
                    System.out.println("connected");
                    String fullpath = agentSFTPpath + "AgentPic";
                    channelSftp.cd(fullpath);
                    System.out.println(channelSftp.pwd());
                    String path = channelSftp.pwd() + "/";
                    System.out.println("u are here");
                    if (channelSftp.lstat(path + AgentImage + ".jpg") != null) {
                        System.out.println("Existed");
                    }
                    byte[] buffer = new byte[1024];
                    System.out.println(buffer);
                    BufferedInputStream bis = new BufferedInputStream(channelSftp.get(path + AgentImage + ".jpg"));
                    System.out.println(bis);
                    ddd = Drawable.createFromStream(bis, "ddd");
                    System.out.println(ddd);

                    agentImage.setImageDrawable(ddd);

                    bis.close();
                    System.out.println("you are here now !!!!!!");
                    channelSftp.disconnect();
                    session.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Thread threadload = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                getAgent();

            }catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    });
    public boolean connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(url, userName, password);
                if (conn != null) {
                    connectflag = true;
                } else {
                    connectflag = false;
                }

                //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
            } catch (SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
                System.out.println("error is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;

            } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                System.out.println("error is: " + e.toString());
                //Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                connectflag = false;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return connectflag;
    }

    //return region names...
    public ArrayList<String> GetRegions()
    {
        ArrayList<String> my_array = new ArrayList<String>();
        boolean flg=false;
        if ((flg = connecttoDB()) == true) {
            Statement stmt3 = null;

            try {
                stmt3 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //select region names
            String sqlStmt1 = "SELECT REGION_NAME FROM REGION";
            ResultSet rs1 = null;

            try {
                rs1 = stmt3.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            my_array.add("None");
            while (true) {
                try {
                    if (!rs1.next()) break;
                    value = (rs1.getString("REGION_NAME"));
                    my_array.add(value);

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
                stmt3.close();
                conn.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        try  {


        }catch(Exception e) {
            System.out.println(e.toString());
        }
        return my_array;
    }

    public void FillSpinner()
    {

        ArrayList<String> my_array = new ArrayList<String>();
        my_array = GetRegions();

        if(my_array.size()>0) {
            ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, my_array);
            spregion.setAdapter(my_Adapter);
        }

    }
    Thread thread1 = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FillSpinner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    //returns region id
    public String getRegionID(String varregioname)
    {
        String regionID = null;
        String region_ID=null;

        boolean flg=false;
        if ((flg = connecttoDB()) == true) {

            Statement stmt3 = null;

            try {
                stmt3 = conn.createStatement();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //select region names
            String sqlStmt1 = "SELECT REGION_ID FROM REGION where REGION_NAME='" + varregioname + "'";
            System.out.println(sqlStmt1);
            ResultSet rs1 = null;

            try {
                rs1 = stmt3.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs1.next()) break;
                    region_ID = (rs1.getString("REGION_ID"));
                    regionID = region_ID;
                    System.out.println(regionID);
                    System.out.println();
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
                stmt3.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        return regionID;

    }

}