package com.example.aliatsimactivation;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResellerStatusRecViewAdapter extends RecyclerView.Adapter<ResellerStatusRecViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<com.example.aliatsimactivation.ResellerStatusListView> status=new ArrayList<>();
    public ResellerStatusRecViewAdapter(Context context) {
        this.context=context;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reseller_status_item,parent,false);
        ViewHolder holder =new ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        holder.chargeid.setText(status.get(position).getChargeID());
        holder.mobilenb.setText(status.get(position).getMOBILENUMBER());
        holder.amount.setText(status.get(position).getAMOUNT());
        holder.img.setImageResource(status.get(position).getImageIcon());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Sending" ,Toast.LENGTH_LONG).show();

            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"Deleting" ,Toast.LENGTH_LONG).show();


                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return status.size();
    }
    public void setContacts(ArrayList<com.example.aliatsimactivation.ResellerStatusListView> status) {
        this.status = status;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TableLayout parent;
        TextView chargeid,mobilenb,amount;
        ImageView img;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            chargeid=itemView.findViewById(R.id.chargeid_id1);
            mobilenb=itemView.findViewById(R.id.mobile_id1);
            amount=itemView.findViewById(R.id.amount_id1);
            img=itemView.findViewById(R.id.img);
            parent=itemView.findViewById(R.id.parent12);
        }
    }
}

