package com.example.aliatsimactivation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SIMRegViewAdapter extends RecyclerView.Adapter<SIMRegViewAdapter.ViewHolder> {

    private ArrayList<SimRegListView> list=new ArrayList<>();
    private Context context;

    public SIMRegViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sim_reg_item_list,parent,false);
        ViewHolder holder =new ViewHolder (view);
        return holder;
    }

    @Override




    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.sim_id.setText(list.get(position).getSimRegListViewId());
        holder.name_id.setText(list.get(position).getName());
        holder.mobile_id.setText(list.get(position).getMobile());
        holder.simstatus_id.setText(list.get (position).getStatus());


        holder.name_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,list.get(position).getSimRegListViewId() ,Toast.LENGTH_SHORT).show();
                System.out.println(list.get(position).getSimRegListViewId());

                // show line in green when select row
                holder.name_id.setBackgroundColor(Color.GREEN);
                holder.mobile_id.setBackgroundColor(Color.GREEN);
                holder.simstatus_id.setBackgroundColor(Color.GREEN);

                // pass on click wareid value to new activity Sitinforactivity
                Intent intent =  new Intent(context, SimRegInfo.class);
                intent.putExtra("message_key", list.get(position).getSimRegListViewId());
                intent.putExtra("db-offline", "1");
                intent.putExtra("globalMode","Online");
                context.startActivity(intent);

            }
        });



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
        private TableLayout parent;
        private TextView sim_id,name_id,mobile_id,simstatus_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sim_id=itemView.findViewById(R.id.sim_id);
            name_id=itemView.findViewById(R.id.name_id);
            mobile_id=itemView.findViewById(R.id.mobile_id);
            simstatus_id=itemView.findViewById(R.id.simstatus_id);

        }
    }
}
