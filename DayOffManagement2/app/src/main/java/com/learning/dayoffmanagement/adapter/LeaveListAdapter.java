package com.learning.dayoffmanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Listener.LeaveListItemClick;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<LeaveForm> leaveForms;
    private List<LeaveForm> leaveFormsOldData;
    private LeaveListItemClick listerner;

    public LeaveListAdapter(Context context, List<LeaveForm> leaveForms, LeaveListItemClick listerner) {
        this.context = context;
        this.leaveForms = leaveForms;
        this.listerner = listerner;
        this.leaveFormsOldData = leaveForms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_form_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveForm item =  leaveForms.get(position) ;
        holder.bindData(item);
        holder.viewLeaveForm(item);
    }

    @Override
    public int getItemCount() {
        if(leaveForms == null || leaveForms.size() == 0){
            return 0;
        }
        return leaveForms.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().trim();
                if(query.isEmpty()){
                    leaveForms = leaveFormsOldData;
                }else {
                     List<LeaveForm> filteredData = new ArrayList<>();
//                     nếu đơn cần lọc là đã đc phê duyệt
                    if(query.equalsIgnoreCase("Approved")){
                        for(LeaveForm cLF : leaveFormsOldData){
                            if(!cLF.getState().equalsIgnoreCase("Waiting")){
                                if(!filteredData.contains(cLF)){
                                    filteredData.add(cLF);
                                }
                            }
                        }
//                        nếu đơn cần lọc là chưa đc phê duyệt
                    }else if(query.equalsIgnoreCase("Waiting")){
                        for(LeaveForm cLF : leaveFormsOldData){
                            if(cLF.getState().equalsIgnoreCase("Waiting")){
                                if(!filteredData.contains(cLF)){
                                    filteredData.add(cLF);
                                }
                            }
                        }
                    }
//                    lọc theo tháng của sendate - ngày gửi
                    else{
                       for(LeaveForm cLF : leaveFormsOldData){
                           StringTokenizer stk = new StringTokenizer(cLF.getSendDate(),"-");
                           stk.nextToken();
                           String getMonth = stk.nextToken();
                           if(getMonth.equalsIgnoreCase(query.trim())){
                               if(!filteredData.contains(cLF)){
                                   filteredData.add(cLF);
                               }
                           }
                       }

                    }
                    leaveForms = filteredData;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = leaveForms;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                leaveForms = (List<LeaveForm>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtIdSender,txtNameSender,txtSendDate,txtState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdSender = itemView.findViewById(R.id.txtIdSender);
            txtNameSender = itemView.findViewById(R.id.txtSenderName);
            txtSendDate = itemView.findViewById(R.id.txtSendDate);
            txtState = itemView.findViewById(R.id.txtLeaveFormState);

        }

        public void bindData(LeaveForm leaveForm){
            txtIdSender.setText(leaveForm.getUid());
            txtNameSender.setText(leaveForm.getSender());
            txtSendDate.setText("Send date: "+leaveForm.getSendDate());
            txtState.setText("State: "+leaveForm.getState());
        }

        public void viewLeaveForm(LeaveForm leaveForm){
            itemView.findViewById(R.id.leaveItemLayout).setOnClickListener(v->{
                if(listerner!=null) listerner.itemClick(leaveForm);
            });
        }

    }
}
