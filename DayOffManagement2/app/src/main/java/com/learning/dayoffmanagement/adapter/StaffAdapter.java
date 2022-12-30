package com.learning.dayoffmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.learning.dayoffmanagement.Listener.StaffItemClick;

import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private Context context;
    private List<NhanVien> staffs;
    private StaffItemClick listerner;

    public StaffAdapter(Context context, List<NhanVien> staffs, StaffItemClick listerner) {
        this.context = context;
        this.staffs = staffs;
        this.listerner = listerner;
    }

    @NonNull
    @Override
    public StaffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_mana_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffAdapter.ViewHolder holder, int position) {
        NhanVien item = staffs.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtStaffItemId,txtStaffItemName,txtStaffItemPhone;
        private ImageView imgStaffAvtItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStaffItemId = itemView.findViewById(R.id.txtStaffIdItem);
            txtStaffItemName = itemView.findViewById(R.id.txtStaffNameItem);
            txtStaffItemPhone = itemView.findViewById(R.id.txtStaffPhoneNumberItem);
            imgStaffAvtItem = itemView.findViewById(R.id.imgStaffAvtItem);

            itemView.findViewById(R.id.staffItemLayout).setOnClickListener(v->{
                listerner.viewStaff(getAdapterPosition());
            });
        }

        public void bindData(NhanVien staff){
            txtStaffItemId.setText(staff.getId());
            txtStaffItemName.setText(staff.getName());
            txtStaffItemPhone.setText(staff.getDepartment().getName());
            if(staff.getAvtUrl() != null ){
                Glide.with(this.itemView.getContext()).load(staff.getAvtUrl()).into(imgStaffAvtItem);
            }
        }
    }

}
