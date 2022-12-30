package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Model.ScheduleOT;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class ScheduleOTAdapter extends RecyclerView.Adapter<ScheduleOTAdapter.ViewHolder> {
    private List<ScheduleOT> scheduleOTS;

    public ScheduleOTAdapter(List<ScheduleOT> scheduleOTS){
        this.scheduleOTS = scheduleOTS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mana_ot_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(scheduleOTS.get(position));

    }

    @Override
    public int getItemCount() {
        return scheduleOTS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtOTStaffID,txtOTStaffName,txtOTStaffTime,txtOTTime;
        ImageView imgDenyOT,imgAcceptOT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOTStaffID = itemView.findViewById(R.id.txtRegisterOTStaffID);
            txtOTStaffName = itemView.findViewById(R.id.txtRegisterOTStaffName);
            txtOTStaffTime = itemView.findViewById(R.id.txtRegisterOTTIme);
            txtOTTime = itemView.findViewById(R.id.txtOTTime);

            imgAcceptOT = itemView.findViewById(R.id.imgAcceptOT);
            imgDenyOT = itemView.findViewById(R.id.imgDenyOT);

            imgAcceptOT.setVisibility(View.GONE);
            imgDenyOT.setVisibility(View.GONE);

        }

        public void bindData(ScheduleOT scheduleOT){
            txtOTStaffID.setText("OT Date: "+scheduleOT.getDate());
            txtOTStaffName.setText("From: "+scheduleOT.getStartTime()+ " to: "+scheduleOT.getEndTime());
            txtOTStaffTime.setText("Total time: "+scheduleOT.getTotalOTTime());
            txtOTTime.setText("By: "+scheduleOT.getAdmin());
        }
    }
}
