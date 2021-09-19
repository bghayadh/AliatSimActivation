package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ResendPicutres extends AppCompatActivity {
    private boolean connectflag;
    private Connection conn;
    private String globalsimid,gfrontstatus,gsigstatus,gbackstatus,gclientstatus;
    private TextView txtclientnumber,txtfrontimage,txtbackimage,txtsignature,txtclientimgname,txtmsg;
    private Button resend;
    private Button btnmain;
    private SFTP sftp = new SFTP();
    private String agentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_picutres);

        txtclientnumber=findViewById(R.id.clientnumber);
        txtfrontimage=findViewById(R.id.frontimagename);
        txtbackimage=findViewById(R.id.backimagename);
        txtsignature=findViewById(R.id.signaturename);
        txtmsg=findViewById(R.id.txtmsgresend);
        txtclientimgname=findViewById(R.id.clientimgname);

        resend=findViewById(R.id.resend);
        btnmain=findViewById(R.id.btnmain);
        Intent i = this.getIntent();
        globalsimid=i.getStringExtra("globalsimid");
        agentNumber=i.getStringExtra("agentNumber");
        System.out.println("globalimid : "+globalsimid);

        if (globalsimid != "0") {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    threadload.start();


                }
            });
        }


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File signpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtsignature.getText().toString() + ".jpg");
                File frontpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtfrontimage.getText().toString() + ".jpg");
                File backpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtbackimage.getText().toString() + ".jpg");
                File clientpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtclientimgname.getText().toString() + ".jpg");
                txtmsg.setText("Please Wait....");
                if(signpic.exists() || frontpic.exists() || backpic.exists() || clientpic.exists()) {
                    if (gsigstatus.equalsIgnoreCase("0") || gfrontstatus.equalsIgnoreCase("0") || gbackstatus.equalsIgnoreCase("0") || gclientstatus.equalsIgnoreCase("0")) {
                        thread1.start();

                    }
                }else{
                    txtmsg.setText("You don't have pictures saved on your phone, please check and try again later");
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
                intent.putExtra("agentNumber",agentNumber);
                startActivity(intent);
            }
        });

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
                conn = DriverManager.getConnection(url, userName, password);

                if (conn != null) {
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

    public void getDatatoResend() {

        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    // if it is a new Warehouse we will use insert

                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "select MOBILE_NUMBER,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO,SIGNATURE,SIGNATURE_STATUS,BACK_SIDE_ID_STATUS,FRONT_SIDE_ID_STATUS,CLIENT_PHOTO,CLIENT_PHOTO_STATUS FROM CLIENTS where CLIENT_ID = '" + globalsimid + "'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            FilldatafromDataBase(rs1.getString("MOBILE_NUMBER"),rs1.getString("ID_FRONT_SIDE_PHOTO"),rs1.getString("ID_BACK_SID_PHOTO"),rs1.getString("SIGNATURE"),rs1.getString("SIGNATURE_STATUS"),rs1.getString("BACK_SIDE_ID_STATUS"),rs1.getString("FRONT_SIDE_ID_STATUS"),rs1.getString("CLIENT_PHOTO"),rs1.getString("CLIENT_PHOTO_STATUS"));

                            //System.out.println(rs1.getString("compteur"));

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

    public void FilldatafromDataBase(String mobilenumber,String frontimage,String backimage,String signature,String signstatus,String backstatus,String frontstatus,String clientimg,String clientstatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                txtclientnumber.setText(mobilenumber);
                txtfrontimage.setText(frontimage);
                txtbackimage.setText(backimage);
                txtsignature.setText(signature);
                txtclientimgname.setText(clientimg);
                gsigstatus=signstatus;
                gfrontstatus=frontstatus;
                gbackstatus=backstatus;
                gclientstatus=clientstatus;


            }

        });
    }

    Thread threadload = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                getDatatoResend();
            }catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    });

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                //Toast.makeText(SimTest.this,"Trying to connect..",Toast.LENGTH_LONG).show();
                System.out.println("Start");
                System.out.println("globalsimid = "+globalsimid);
                String user=sftp.getUser().toString();
                String pass=sftp.getPass().toString();
                String host=sftp.getServer().toString();
                int e = sftp.getPort();

                Properties config=new Properties();
                config.put("StrictHostKeyChecking","no");

                JSch jSch = new JSch();
                Session session =jSch.getSession(user,host,e);
                System.out.println("Step1");
                session.setPassword(pass);
                session.setConfig(config);
                session.connect();
                System.out.println("Step Connect");
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();
                //check if the global status if equals zero do it
                if(gsigstatus.equalsIgnoreCase("0")) {

                    File signpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtsignature.getText().toString() + ".jpg");
                    String sign = String.valueOf(signpic);
                    channelSftp.put(sign, "SIMPICSFTP");
                    Boolean success1 = true;

                    if (success1) {
                        //txtmsg.setText("upload completed : " + sign);
                        System.out.println("upload completed : " + sign);
                        UpdateSimRegistrationPicStatus(globalsimid, "SIGNATURE_STATUS");
                        gsigstatus="1";
                        signpic.delete();
                    }else {
                        //txtmsg.setText("upload Failed : " + sign);
                    }
                }

                if(gfrontstatus.equalsIgnoreCase("0")){

                    File frontpic=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtfrontimage.getText().toString() + ".jpg");
                    String front=String.valueOf(frontpic);
                    channelSftp.put(front, "SIMPICSFTP");
                    Boolean success2 = true;

                    if(success2){

                        System.out.println("upload completed : "+front);
                        UpdateSimRegistrationPicStatus(globalsimid,"FRONT_SIDE_ID_STATUS");
                        gfrontstatus="1";
                        frontpic.delete();
                    }else{

                    }
                }
                if(gbackstatus.equalsIgnoreCase("0")) {
                    File backpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtbackimage.getText().toString() + ".jpg");
                    String back = String.valueOf(backpic);
                    channelSftp.put(back, "SIMPICSFTP");
                    Boolean success3 = true;

                    if (success3) {

                        System.out.println("upload completed : " + back);
                        UpdateSimRegistrationPicStatus(globalsimid, "BACK_SIDE_ID_STATUS");
                        gbackstatus="1";
                        backpic.delete();
                    }
                }

                if(gclientstatus.equalsIgnoreCase("0")) {

                    File clientpic = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), txtclientimgname.getText().toString() + ".jpg");
                    String client = String.valueOf(clientpic);
                    channelSftp.put(client, "SIMPICSFTP");
                    Boolean success4 = true;

                    if (success4) {
                        //txtmsg.setText("upload completed : " + sign);
                        System.out.println("upload completed : " + client);
                        UpdateSimRegistrationPicStatus(globalsimid, "CLIENT_PHOTO_STATUS");
                        gclientstatus="1";
                        clientpic.delete();
                    }else {
                        //txtmsg.setText("upload Failed : " + sign);
                    }
                }

                //   Toast.makeText(SimTest.this,"session connection"+session.isConnected(),Toast.LENGTH_LONG).show();
                channelSftp.disconnect();
                session.disconnect();

                Thread.sleep(500);
                txtmsg.setText("");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    });

    public void UpdateSimRegistrationPicStatus(String vsimregid,String vcolname)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    System.out.println("update CLIENTS set " + vcolname + "=1  where CLIENT_ID ='" + vsimregid + "'");
                    stmtinsert1 = conn.prepareStatement("update CLIENTS set " + vcolname + "=1  where CLIENT_ID ='" + vsimregid + "'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtinsert1.executeUpdate();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
