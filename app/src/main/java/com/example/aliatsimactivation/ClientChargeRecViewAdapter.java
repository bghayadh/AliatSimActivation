package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientChargeRecViewAdapter extends RecyclerView.Adapter<ClientChargeRecViewAdapter.ViewHolder> {


    private ArrayList<ClientChargeListView> mobilecharge=new ArrayList<>();
    private Context context;

    public ClientChargeRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ClientChargeRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mob_charge_list_item,parent,false);
        ClientChargeRecViewAdapter.ViewHolder holder =new ClientChargeRecViewAdapter.ViewHolder (view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ClientChargeRecViewAdapter.ViewHolder holder, int position) {
        holder.txtmobchargeid.setText(mobilecharge.get(position).getMOBCHARGEID ());
        holder.txtagentsubnumber.setText(mobilecharge.get(position).getAGENTSUBNUM ());
        holder.txtclientsubnumber.setText(mobilecharge.get(position).getCLIENTSUBNUM ());
        holder.txtamount.setText(mobilecharge.get(position).getAMOUNT ());
        holder.txtrechargestatus.setText(mobilecharge.get(position).getRECHARGESTATUS ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,mobilecharge.get(position).getMOBCHARGEID () ,Toast.LENGTH_SHORT).show();
                System.out.println(mobilecharge.get(position).getMOBCHARGEID ());

                // pass on click mobilechargeid value to new activity MobileChargeinfoactivity
                Intent intent =  new Intent(context, ClientChargeInfoActivity.class);
                intent.putExtra("message_key", mobilecharge.get(position).getMOBCHARGEID ());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mobilecharge.size();
    }

    public void setContacts(ArrayList<ClientChargeListView> mobilecharge) {
        this.mobilecharge = mobilecharge;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtmobchargeid,txtagentsubnumber,txtclientsubnumber,txtamount,txtrechargestatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtmobchargeid=itemView.findViewById(R.id.txtmobchargeid);
            txtagentsubnumber=itemView.findViewById(R.id.txtagentsubnumber);
            txtclientsubnumber=itemView.findViewById(R.id.txtclientsubnumber);
            txtamount=itemView.findViewById(R.id.txtamount);
            txtrechargestatus=itemView.findViewById(R.id.txtrechargestatus);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
