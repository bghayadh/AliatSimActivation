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

public class SimRegOfflineDataRecViewAdapter extends RecyclerView.Adapter<SimRegOfflineDataRecViewAdapter.ViewHolder>{
    FileInputStream fstream;

    private ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
    private Context context;
    Connection conn;
    public SimRegOfflineDataRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sim_offline_list_item,parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
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

    public void setContacts(ArrayList<SimRegOfflineDataListView> offlinedata) {
        this.offlinedata = offlinedata;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout parent;
        private TextView txttitle;
        private ImageView sendImage;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            txttitle=itemView.findViewById(R.id.txttitle);
            sendImage=itemView.findViewById(R.id.sendImage);
            parent=itemView.findViewById(R.id.parent3);
            sendImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            Toast.makeText(context,"sending..." ,Toast.LENGTH_SHORT).show();

            // reading the file from directory
            String name = txttitle.getText().toString();
            StringBuffer sbuffer = new StringBuffer();
            try {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
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
            String e = details[4];
            String f = details[5];
            String g = details[6];
            String h = details[7];
            String j = details[8];
            String k = details[9];
            String l = details[10];
            String m = details[11];
            String n = details[12];
            String o = details[13];
            String p = details[14];
            String q = details[15];
            String r = details[16];
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
                    System.out.println(e);
                    System.out.println(f);
                    System.out.println(g);
                    System.out.println(h);
                    System.out.println(j);
                    System.out.println(k);
                    System.out.println(l);
                    System.out.println(m);
                    System.out.println(n);
                    System.out.println(o);
                    System.out.println(p);
                    System.out.println(q);
                    System.out.println(r);




                    //send the data to database
            connecttoDB();
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            String simID;
            simID = "REG_" + year + "_";
            String globalsimid = "";
            try {


                Statement stmt1 = null;
                stmt1 = conn.createStatement();
                String sqlStmt = "select SIM_REGISTRATION_SEQ.nextval as nbr from dual";
                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery(sqlStmt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                while (true) {
                    try {
                        if (!rs1.next()) break;
                        globalsimid = simID + rs1.getString("nbr");
                        //System.out.println(rs1.getString("compteur"));

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                rs1.close();
                stmt1.close();
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Statement stmt1 = null;
            try {
                stmt1 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
                    String sqlStmt = ("insert into SIM_REGISTRATION (SIM_REG_ID,STATUS,CREATION_DATE,LAST_MODIFIED_DATE,FIRST_NAME,MIDDLE_NAME,LAST_NAME,MOBILE_NUMBER,DATE_OF_BIRTH,NATIONALITY,ALTERNATIVE_NUMBER,EMAIL_ADDRESS,PHISICAL_LOCATION,POSTAL_ADDRESS,GENDER,AGENT_NUMBER,AGENT_ID,SIGNATURE,ID_FRONT_SIDE_PHOTO,ID_BACK_SID_PHOTO) values " +
                            "('" + globalsimid +"','"+r+"' ,sysdate, sysdate,'" + a + "','" + b + "', '" + c + "','" + d + "',TO_DATE('" + e + "','DD-MM-YYYY'),'" + f + "','" + g + "','" + h+ "','" + j + "','" +k+ "','" + l + "','" + m+ "','" + n+ "','" +o+ "','" + p +"','"+q+ "')");
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
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File myFile = new File(dir,name);
            myFile.delete();
            Intent intent = new Intent(context,SimRegInfo.class);
            intent.putExtra("message_key",globalsimid);
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
        } catch (InstantiationException e) {
            System.out.println("error is: " + e.toString());
        }
    }
}
