package com.example.aliatsimactivation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SimRegOfflineDataRecViewAdapter extends RecyclerView.Adapter<SimRegOfflineDataRecViewAdapter.ViewHolder>{
    FileInputStream fstream;
    private String globalMode;
    private ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
    private Context context;
    Connection conn;
    public SimRegOfflineDataRecViewAdapter(Context context, String globalMode) {
        this.context=context;
        this.globalMode=globalMode;
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
                    Toast.makeText(context,"Loading..." ,Toast.LENGTH_SHORT).show();

                    // reading the file from directory
                    String name = txttitle.getText().toString();
                    StringBuffer sbuffer = new StringBuffer();
                    try {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 23);

                        File dir =new File("/storage/emulated/0/Android/data/com.example.aliatsimactivation/files/Documents",name);
                        System.out.println(dir);
                        fstream = new FileInputStream(dir);

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
                    String s = details[17];

                    String globalsimid = "0";
                    Intent intent = new Intent(context,SimRegInfo.class);
                    intent.putExtra("message_key",globalsimid);
                    intent.putExtra("globalMode",globalMode);
                    intent.putExtra("offline1",a);
                    intent.putExtra("offline2",b);
                    intent.putExtra("offline3",c);
                    intent.putExtra("offline4",n);
                    intent.putExtra("offline5",d);
                    intent.putExtra("offline6",e);
                    intent.putExtra("offline7",g);
                    intent.putExtra("offline8",h);
                    intent.putExtra("offline9",j);
                    intent.putExtra("offline10",k);
                    intent.putExtra("offline11",l);
                    intent.putExtra("offline12",f);
                    intent.putExtra("offline13",r);
                    intent.putExtra("offline14",o);
                    intent.putExtra("offline15",p);
                    intent.putExtra("offline16",q);
                    intent.putExtra("offline17",s);
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
          //  Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            //Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (InstantiationException e) {
            System.out.println("error is: " + e.toString());
        }
    }

}
