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
        holder.lastname_id.setText(list.get(position).getLastname());
        holder.nationality_id.setText(list.get(position).getNationality());
        holder.email_id.setText(list.get (position).getEmail());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,list.get(position).getSimRegListViewId() ,Toast.LENGTH_SHORT).show();
                System.out.println(list.get(position).getSimRegListViewId());

                // pass on click wareid value to new activity Sitinforactivity
                Intent intent =  new Intent(context, SimRegInfo.class);
                intent.putExtra("message_key", list.get(position).getSimRegListViewId());
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
        private TextView sim_id,name_id,lastname_id,nationality_id,email_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sim_id=itemView.findViewById(R.id.sim_id);
            name_id=itemView.findViewById(R.id.name_id);
            lastname_id=itemView.findViewById(R.id.lastname_id);
            nationality_id=itemView.findViewById(R.id.nationality_id);
            email_id=itemView.findViewById(R.id.email_id);
            parent=itemView.findViewById(R.id.parent11);
        }
    }
}
