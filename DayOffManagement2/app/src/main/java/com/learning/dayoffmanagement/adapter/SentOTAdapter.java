package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class SentOTAdapter extends RecyclerView.Adapter<SentOTAdapter.ViewHolder> implements Filterable {
    private List<OTForm> OTForms;
    private List<OTForm> otDataOld;

    public SentOTAdapter(List<OTForm> OTForms){
        this.OTForms = OTForms;
        this.otDataOld = OTForms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mana_ot_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(OTForms.get(position));
    }

    @Override
    public int getItemCount() {
        if(OTForms == null || OTForms.size() == 0){
            return 0;
        }
        return OTForms.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                if(query.isEmpty()){
                    OTForms = otDataOld;
                }else{
                    List<OTForm> filteredData = new ArrayList<>();
                    if(query.equalsIgnoreCase("Approved")){
                        for(OTForm cOT : otDataOld){
                            if(!cOT.getState().equalsIgnoreCase("Waiting")){
                                if(!filteredData.contains(cOT)){
                                    filteredData.add(cOT);
                                }
                            }
                        }
                    }else if(query.equalsIgnoreCase("Waiting")){
                        for(OTForm cOT : otDataOld){
                            if(cOT.getState().equalsIgnoreCase("Waiting")){
                                if(!filteredData.contains(cOT)){
                                    filteredData.add(cOT);
                                }
                            }
                        }
                    }
                    OTForms = filteredData;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = OTForms;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                OTForms = (List<OTForm>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

            imgAcceptOT.setVisibility(View.INVISIBLE);
            imgDenyOT.setVisibility(View.INVISIBLE);
        }

        public void bindData(OTForm OTForm){
            txtOTStaffID.setText(OTForm.getSender());
            txtOTStaffName.setText(OTForm.getStartTime()+ " - "+ OTForm.getEndTime());
            txtOTStaffTime.setText("OT : "+ OTForm.getTotalOTTime()+"H");

            if(OTForm.getState().equalsIgnoreCase("Waiting")){
                txtOTTime.setText("State: "+ OTForm.getState());
            }else{
                txtOTTime.setText("State: "+ OTForm.getState() + "\nBy: "+ OTForm.getAdmin() );
            }
        }
    }
}
