package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class PendingPictureRecViewAdapter extends RecyclerView.Adapter<PendingPictureRecViewAdapter.ViewHolder> {

    private ArrayList<PendingPictureListView> list=new ArrayList<>();
    private Context context;
    private String frontimg,backimg,signimg,globalsimid,globalMode,Front_Status,Back_Status,Sign_Status;
    private Connection conn;
    private boolean connectflag=false;
    SFTP sftp=new SFTP();
    Thread thread;


    public PendingPictureRecViewAdapter(Context context, String globalMode) {
        this.context=context;
        this.globalMode=globalMode;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_pictures_list_item,parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.client_nbr.setText(list.get(position).getClientNumer());
        holder.front_status.setText(list.get(position).getFrontStatus());
        holder.back_status.setText(list.get(position).getBackStatus());
        holder.sign_status.setText(list.get(position).getSignStatus());
        holder.globalsimid.setText(list.get(position).getGlobalSimID());

        if(holder.front_status.getText().toString().equalsIgnoreCase("1")){
            holder.front_status.setText("Y");
        }else{
            holder.front_status.setText("N");
        }
        if(holder.back_status.getText().toString().equalsIgnoreCase("1")){
            holder.back_status.setText("Y");
        }else{
            holder.back_status.setText("N");
        }
        if(holder.sign_status.getText().toString().equalsIgnoreCase("1")){
            holder.sign_status.setText("Y");
        }else{
            holder.sign_status.setText("N");
        }



        holder.picresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,holder.globalsimid.getText().toString(),Toast.LENGTH_LONG).show();

                Intent i=new Intent(context,ResendPicutres.class);
                i.putExtra("globalsimid",holder.globalsimid.getText().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setContacts(ArrayList<PendingPictureListView> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView client_nbr,front_status,back_status,sign_status,globalsimid;
        private ImageButton picresend;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client_nbr=itemView.findViewById(R.id.client_nbr);
            front_status=itemView.findViewById(R.id.frontid_status);
            back_status=itemView.findViewById(R.id.backid_status);
            sign_status=itemView.findViewById(R.id.sign_status);
            globalsimid=itemView.findViewById(R.id.simid);
            picresend=itemView.findViewById(R.id.picresend);
        }
    }


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

    public void UpdateSimRegistrationPicStatus(String vsimregid,String vcolname)
    {
        boolean flg=false;
        try {
            if((flg=connecttoDB())==true) {
                PreparedStatement stmtinsert1 = null;

                try {
                    System.out.println("update SIM_REGISTRATION set " + vcolname + "=1  where SIM_REG_ID ='" + vsimregid + "'");
                    stmtinsert1 = conn.prepareStatement("update SIM_REGISTRATION set " + vcolname + "=1  where SIM_REG_ID ='" + vsimregid + "'");
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

