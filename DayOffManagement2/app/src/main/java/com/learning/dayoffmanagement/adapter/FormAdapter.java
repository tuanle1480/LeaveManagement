package com.learning.dayoffmanagement.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Model.Form;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> implements Filterable {
    private List<Form> forms;
    private List<Form> formsOldData;

    public FormAdapter(List<Form> forms){
        this.forms = forms;
        this.formsOldData = forms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(forms.get(position));
    }

    @Override
    public int getItemCount() {
        if(forms == null || forms.size() == 0){
            return 0;
        }
        return forms.size();
    }
//lọc dữ liệu
    @Override
    public Filter getFilter(){
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().trim();
                List<Form> formFiltered = new ArrayList<>();
                FilterResults filterResults = new FilterResults();
                if(query.isEmpty()){
                    filterResults.count = formsOldData.size();
                    filterResults.values = formsOldData;
                }else{
//                     nếu đơn cần lọc là leave form
                    if(query.equalsIgnoreCase("Leave Form")){
                        for(Form cF : formsOldData){
                            if(cF.getFormType().equalsIgnoreCase("Leave Form")){
                                if(!formFiltered.contains(cF)) formFiltered.add(cF);
                            }
                        }
//                        nếu đơn cần lọc là ot form
                    }else if(query.equalsIgnoreCase("OT Form")){
                        for(Form cF : formsOldData){
                            if(cF.getFormType().equalsIgnoreCase("OT Form")){
                                if(!formFiltered.contains(cF)) formFiltered.add(cF);
                            }
                        }
                    }else {
//                         Lọc theo tên or id
                        for (Form cF : formsOldData) {
                           if(cF.getSender().toLowerCase().contains(charSequence.toString().toLowerCase())
                           || cF.getUid().toLowerCase().contains(charSequence.toString().toLowerCase())){
                               if(!formFiltered.contains(cF)) formFiltered.add(cF);
                           }
                        }
                    }
                    filterResults.count = formFiltered.size();
                    filterResults.values = formFiltered;
                }



                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                forms = (List<Form>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtFormSender,txtFormState,txtFormTYpe,txtFormUID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFormSender = itemView.findViewById(R.id.txtSenderForm);
            txtFormTYpe = itemView.findViewById(R.id.txtFormType);
            txtFormState = itemView.findViewById(R.id.txtFormState);
            txtFormUID = itemView.findViewById(R.id.txtFormUid);
        }

        public void bindData(Form data){
            txtFormUID.setText("UID: "+data.getUid());
            txtFormSender.setText("Sender: "+data.getSender());
            txtFormState.setText("Form type: "+data.getFormType());
            txtFormTYpe.setText("Form state: "+data.getState());
        }
    }
}
