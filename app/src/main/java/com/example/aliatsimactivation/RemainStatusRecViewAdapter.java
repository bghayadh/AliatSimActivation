package com.example.aliatsimactivation;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class RemainStatusRecViewAdapter extends RecyclerView.Adapter<RemainStatusRecViewAdapter.ViewHolder> {


    private ArrayList<RemainStatus> mobilecharge = new ArrayList<>();
    private Context context;
    Connection conn;

    public RemainStatusRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public RemainStatusRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.remain_status_list_item,parent,false);
        RemainStatusRecViewAdapter.ViewHolder holder =new RemainStatusRecViewAdapter.ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RemainStatusRecViewAdapter.ViewHolder holder, int position) {
        holder.txtmobchargeid.setText(mobilecharge.get(position).getMOBCHARGEID ());
        holder.txtclientsubnumber.setText(mobilecharge.get(position).getCLIENTSUBNUM ());
        holder.txtamount.setText(mobilecharge.get(position).getAMOUNT ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,mobilecharge.get(position).getMOBCHARGEID () ,Toast.LENGTH_SHORT).show();
                System.out.println(mobilecharge.get(position).getMOBCHARGEID ());
            }
        });

    }



    @Override
    public int getItemCount() {
        return mobilecharge.size();
    }

    public void setContacts(ArrayList<RemainStatus> mobilecharge) {
        this.mobilecharge = mobilecharge;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtmobchargeid,txtclientsubnumber,txtamount;
        private ImageView sendImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtmobchargeid=itemView.findViewById(R.id.txtmobchargeid);
            txtclientsubnumber=itemView.findViewById(R.id.txtclientsubnumber);
            txtamount=itemView.findViewById(R.id.txtamount);
            sendImage=itemView.findViewById(R.id.sendImage);
            parent=itemView.findViewById(R.id.parent);
            sendImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"sending..." ,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
