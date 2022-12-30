package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Listener.SpecifiedOTItemClick;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class SpecifiOTAdapter extends RecyclerView.Adapter<SpecifiOTAdapter.ViewHolder> {
    private List<NhanVien> staffs;
    private SpecifiedOTItemClick listener;

    public SpecifiOTAdapter(List<NhanVien> staffs, SpecifiedOTItemClick listener){
        this.staffs = staffs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specified_ot_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(staffs.get(position));
        holder.specifiedOT(staffs.get(position));
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSpecifiedOTStaffId,txtSpecifiedOTStaffName,txtSpecifiedOTStaffPhoneNumber;
        ImageView imgSpecifiedOT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSpecifiedOTStaffId = itemView.findViewById(R.id.txtSpecifiedStaffID);
            txtSpecifiedOTStaffName = itemView.findViewById(R.id.txtSpecifiedStaffName);
            txtSpecifiedOTStaffPhoneNumber = itemView.findViewById(R.id.txtSpecifiedStaffPhoneNumber);

            imgSpecifiedOT = itemView.findViewById(R.id.imgSpecifiOT);
        }

        public void bindData(NhanVien staff){
            txtSpecifiedOTStaffId.setText(staff.getId());
            txtSpecifiedOTStaffName.setText(staff.getName());
            txtSpecifiedOTStaffPhoneNumber.setText(staff.getPhoneNumber());
        }

        public void specifiedOT(NhanVien staff){
            imgSpecifiedOT.setOnClickListener(v->{
                listener.specifiedOT(staff);
            });
        }
    }
}
