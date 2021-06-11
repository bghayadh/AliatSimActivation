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
import java.util.List;

import oracle.jdbc.proxy.annotation.Post;

public class SIMRegViewAdapter extends RecyclerView.Adapter<SIMRegViewAdapter.ViewHolder> {

    private ArrayList<SimRegListView> list=new ArrayList<>();
    private Context context;

    public SIMRegViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public SIMRegViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sim_reg_item_list,parent,false);
        SIMRegViewAdapter.ViewHolder holder =new SIMRegViewAdapter.ViewHolder (view);
        return holder;
    }

    @Override




    public void onBindViewHolder(@NonNull SIMRegViewAdapter.ViewHolder holder, int position) {

        holder.sim_id.setText(list.get(position).getSimRegListViewId());
        holder.name_id.setText(list.get(position).getName());
        holder.lastname_id.setText(list.get(position).getLastname());
        holder.nationality_id.setText(list.get(position).getNationality());
        holder.email_id.setText(list.get (position).getEmail());




    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setContacts(ArrayList<SimRegListView> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView sim_id,name_id,lastname_id,nationality_id,email_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sim_id=itemView.findViewById(R.id.sim_id);
            name_id=itemView.findViewById(R.id.name_id);
            lastname_id=itemView.findViewById(R.id.lastname_id);
            nationality_id=itemView.findViewById(R.id.nationality_id);
            email_id=itemView.findViewById(R.id.email_id);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
