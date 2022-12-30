package com.learning.dayoffmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Listener.ApprovedLeaveFormItemClick;
import com.learning.dayoffmanagement.Model.ApprovedLeaveForm;
import com.learning.dayoffmanagement.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ApprovedLeaveFormAdapter extends RecyclerView.Adapter<ApprovedLeaveFormAdapter.ViewHolder> {
    private List<ApprovedLeaveForm> approvedLeaveForms,approvedLeaveFormsFiltered;
    private ApprovedLeaveFormItemClick listener;
    private Context context;
    public ApprovedLeaveFormAdapter(Context context, List<ApprovedLeaveForm> approvedLeaveForms, ApprovedLeaveFormItemClick listener) {
        this.approvedLeaveForms = approvedLeaveForms;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dayof_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(approvedLeaveForms.get(position));
    }

    @Override
    public int getItemCount() {
        return approvedLeaveForms.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSendDate,txtTimeLeave,txtState;
        ConstraintLayout dayoffItemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSendDate= itemView.findViewById(R.id.txtLeaveFormSendDateApproved);
            txtTimeLeave = itemView.findViewById(R.id.txtLeaveTimeApproved);
            txtState = itemView.findViewById(R.id.txtLeaveFormStateApproved);

            dayoffItemLayout = itemView.findViewById(R.id.dayoffItemLayout);

            dayoffItemLayout.setOnClickListener(v->{
                listener.viewApprovedLeaveForm(getAdapterPosition());
            });
        }

        public void bindData(ApprovedLeaveForm form) {
            txtSendDate.setText(form.getLeaveForm().getSendDate());
            txtTimeLeave.setText(form.getLeaveForm().getStartDate()+" - "+form.getLeaveForm().getEndDate());
            txtState.setText(form.getLeaveForm().getState());

            if(form.getLeaveForm().getState().equalsIgnoreCase("Accepted")){
                dayoffItemLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            }else{
                dayoffItemLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}
