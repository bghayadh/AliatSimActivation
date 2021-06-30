package com.example.aliatsimactivation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.util.ArrayList;

public class ClientStatusRecViewAdapter extends RecyclerView.Adapter<ClientStatusRecViewAdapter.ViewHolder> {


    private ArrayList<ClientStatusListView> mobilecharge = new ArrayList<>();
    private Context context;
    Connection conn;

    public ClientStatusRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.remain_status_list_item,parent,false);
        ViewHolder holder =new ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtmobchargeid.setText(mobilecharge.get(position).getMOBCHARGEID ());
        holder.txtclientsubnumber.setText(mobilecharge.get(position).getCLIENTSUBNUM ());
        holder.txtstatus.setText(mobilecharge.get(position).getRECHARGESTATUS ());

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

    public void setContacts(ArrayList<ClientStatusListView> mobilecharge) {
        this.mobilecharge = mobilecharge;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtmobchargeid,txtclientsubnumber,txtstatus;
        private ImageView sendImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtmobchargeid=itemView.findViewById(R.id.txtmobchargeid);
            txtclientsubnumber=itemView.findViewById(R.id.txtclientsubnumber);
            txtstatus=itemView.findViewById(R.id.txtstatus);
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
