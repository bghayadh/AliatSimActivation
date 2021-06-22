package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClientOfflineDataRecViewAdapter extends RecyclerView.Adapter<ClientOfflineDataRecViewAdapter.ViewHolder>{
    FileInputStream fstream;

    private ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
    private Context context;
    Connection conn;

    public ClientOfflineDataRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ClientOfflineDataRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_list_item,parent,false);
        ClientOfflineDataRecViewAdapter.ViewHolder holder =new ClientOfflineDataRecViewAdapter.ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientOfflineDataRecViewAdapter.ViewHolder holder, int position) {
        holder.txttitle.setText(offlinedata.get(position).getTITLE ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,offlinedata.get(position).getTITLE () ,Toast.LENGTH_SHORT).show();
                System.out.println(offlinedata.get(position).getTITLE ());
            }
        });

    }



    @Override
    public int getItemCount() {
        return offlinedata.size();
    }

    public void setContacts(ArrayList<ClientOfflineDataListView> offlinedata) {
        this.offlinedata = offlinedata;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txttitle;
        private ImageView sendImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txttitle=itemView.findViewById(R.id.txttitle);
            sendImage=itemView.findViewById(R.id.sendImage);
            parent=itemView.findViewById(R.id.parent);
            sendImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"sending..." ,Toast.LENGTH_SHORT).show();

                    // reading the file from directory
                    String name = txttitle.getText().toString();
                    StringBuffer sbuffer = new StringBuffer();
                    try {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File myFile = new File(dir,name);
                        fstream = new FileInputStream(myFile);

                        int i;
                        while ((i = fstream.read())!= -1){
                            sbuffer.append((char)i);
                        }
                        fstream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // spliting the data
                    String details[] = sbuffer.toString().split("\n");
                    String a = details[0];
                    String b = details[1];
                    String c = details[2];
                    String d = details[3];
                    System.out.println(a);
                    System.out.println(b);
                    System.out.println(c);
                    System.out.println(d);


                    //send the data to database
                    connecttoDB();

                    // Creating a sequence mobilechargeID
                    Date date = new Date();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    String mobchargeID;
                    mobchargeID = "MOB_CHARGE_" + year + "_";
                    String globalmobchargeid = "";
                    try {
                        // if it is a new MOB_charge we will use insert
                            Statement stmt = null;
                            stmt = conn.createStatement();
                            String sqlStmt = "select MOB_CHARGE_SEQ.nextval as nbr from dual";
                            ResultSet rs = null;
                            try {
                                rs = stmt.executeQuery(sqlStmt);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            while (true) {
                                try {
                                    if (!rs.next()) break;
                                    globalmobchargeid = mobchargeID + rs.getString("nbr");

                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                            rs.close();
                            stmt.close();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                    // Insert the Offline data into MOBILE_CHARGE table
                    Statement stmt1 = null;
                    try {
                        stmt1 = conn.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    String sqlStmt = ("insert into MOBILE_CHARGE(MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS) values " +
                            "('" + globalmobchargeid + "','" + a.toString() + "','" + b.toString() + "', sysdate,'" + c.toString() + "','" + d.toString() + "')");
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
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


                    //delete the file after sending data from file into database
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File myFile = new File(dir,name);
                    myFile.delete();
                    Intent intent = new Intent(context,ClientChargeListViewActivity.class);
                    context.startActivity(intent);

                }
            });
        }
    }




    public void connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " + e.toString());
        }
    }
}




