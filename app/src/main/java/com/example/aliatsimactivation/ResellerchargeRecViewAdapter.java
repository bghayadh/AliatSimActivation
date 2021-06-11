package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResellerchargeRecViewAdapter extends RecyclerView.Adapter<ResellerchargeRecViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<com.example.aliatsimactivation.ResellerChargeListView> resellers=new ArrayList<>();
    public ResellerchargeRecViewAdapter(Context context) {
        this.context=context;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reseller_charge_item_list,parent,false);
        ViewHolder holder =new ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        holder.chargeid.setText(resellers.get(position).getChargeID());
        holder.mobilenb.setText(resellers.get(position).getMOBILENUMBER());
        holder.date.setText(resellers.get(position).getDATE());
        holder.amount.setText(resellers.get(position).getAMOUNT());
        holder.status.setText(resellers.get(position).getSTATUS());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,resellers.get(position).getChargeID() ,Toast.LENGTH_SHORT).show();
                System.out.println(resellers.get(position).getChargeID());

                // pass on click wareid value to new activity Sitinforactivity
                Intent intent =  new Intent(context, ResellerChargeInfoActivity.class);
                intent.putExtra("message_key", resellers.get(position).getChargeID());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return resellers.size();
    }
    public void setContacts(ArrayList<com.example.aliatsimactivation.ResellerChargeListView> resellers) {
        this.resellers = resellers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TableLayout parent;
        TextView chargeid,mobilenb,date,amount,status;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            chargeid=itemView.findViewById(R.id.chargeid_id1);
            mobilenb=itemView.findViewById(R.id.mobile_id1);
            date=itemView.findViewById(R.id.date_id);
            amount=itemView.findViewById(R.id.amount_id1);
            status=itemView.findViewById(R.id.status_id);
            parent=itemView.findViewById(R.id.parent12);
        }
    }
}
