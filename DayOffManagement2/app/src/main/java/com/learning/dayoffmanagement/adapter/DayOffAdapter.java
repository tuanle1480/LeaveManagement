package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Listener.DayOffItemClick;
import com.learning.dayoffmanagement.Model.DayOff;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class DayOffAdapter extends RecyclerView.Adapter<DayOffAdapter.ViewHolder> {

    private List<DayOff> dayOffs;
    private DayOffItemClick listener;

    public DayOffAdapter(List<DayOff> dayOffs,DayOffItemClick listener) {
        this.listener = listener;
        this.dayOffs = dayOffs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dayof_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(dayOffs.get(position));
        holder.layout.setOnLongClickListener(v->{
            listener.deleteDayOff(dayOffs.get(position));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return dayOffs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSendDate,txtTimeLeave,txtState;
        ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSendDate= itemView.findViewById(R.id.txtLeaveFormSendDateApproved);
            txtTimeLeave = itemView.findViewById(R.id.txtLeaveTimeApproved);
            txtState = itemView.findViewById(R.id.txtLeaveFormStateApproved);

            layout = itemView.findViewById(R.id.dayoffItemLayout);
        }

        public void bindData(DayOff dayOff){
            txtTimeLeave.setText("Staff: "+dayOff.getId());
            txtState.setText("Total leave time: "+dayOff.getTotalLeaveTime()+"");
            txtSendDate.setText(dayOff.getName());
        }
    }
}
