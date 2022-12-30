package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Listener.OTItemClick;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class OTAdapter extends RecyclerView.Adapter<OTAdapter.ViewHolder> {

    private List<OTForm> ots;
    private OTItemClick listener;

    public OTAdapter(List<OTForm> ots, OTItemClick listener){
        this.ots = ots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mana_ot_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(ots.get(position));
        holder.denyOT(ots.get(position));
        holder.acceptOT(ots.get(position));

    }

    @Override
    public int getItemCount() {
        return ots.size();
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
        }

        public void bindData(OTForm OTForm){
            txtOTStaffID.setText(OTForm.getUid());
            txtOTStaffName.setText(OTForm.getSender());
            txtOTStaffTime.setText(OTForm.getStartTime()+ " - "+ OTForm.getEndTime());
            txtOTTime.setText("OT : "+ OTForm.getTotalOTTime()+"H");
        }

        public void denyOT(OTForm OTForm){
            imgDenyOT.setOnClickListener(v->{
                listener.denyOT(OTForm);
            });
        }

        public void acceptOT(OTForm OTForm){
            imgAcceptOT.setOnClickListener(v->{
                listener.acceptOT(OTForm);
            });
        }


    }
}
