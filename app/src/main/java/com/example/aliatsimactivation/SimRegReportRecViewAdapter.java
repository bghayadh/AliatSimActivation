package com.example.aliatsimactivation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimRegReportRecViewAdapter extends RecyclerView.Adapter<SimRegReportRecViewAdapter.ViewHolder> {
    private Context context;
    ArrayList<SimRegReportListView> reports = new ArrayList<>();

    public SimRegReportRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SimRegReportRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sim_report_list_item,parent,false);
        SimRegReportRecViewAdapter.ViewHolder holder =new SimRegReportRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  SimRegReportRecViewAdapter.ViewHolder holder, int position) {
        holder.msidn.setText(reports.get(position).getMsidn());
        holder.status.setText(reports.get(position).getStatus());
        holder.verification.setText(reports.get(position).getVerification());


    }

    @Override
    public int getItemCount() {
        return reports.size();
    }
    public void setContacts(ArrayList<com.example.aliatsimactivation.SimRegReportListView> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TableLayout parent;
        TextView msidn,status,verification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msidn=itemView.findViewById(R.id.msidn);
            status=itemView.findViewById(R.id.status);
            verification=itemView.findViewById(R.id.verification);

        }
    }
}
