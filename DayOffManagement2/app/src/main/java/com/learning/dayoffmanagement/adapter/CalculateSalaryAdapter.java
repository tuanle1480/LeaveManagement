package com.learning.dayoffmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class CalculateSalaryAdapter extends RecyclerView.Adapter<CalculateSalaryAdapter.ViewHolder> {

    private List<NhanVien> staffs;

    public CalculateSalaryAdapter(List<NhanVien> staffs){
        this.staffs = staffs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_staff_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(staffs.get(position));
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtStaffIdSalary,txtStaffNameSalary,txtLeaveDay,txtOTTime,txtTotalSalary;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStaffIdSalary = itemView.findViewById(R.id.txtStaffIdSalary);
            txtStaffNameSalary = itemView.findViewById(R.id.txtStaffNameSalary);
            txtLeaveDay = itemView.findViewById(R.id.txtLeaveDay);
            txtOTTime = itemView.findViewById(R.id.txtOtTime);
            txtTotalSalary = itemView.findViewById(R.id.txtTotalSalary);
        }

        public void bindData(NhanVien nhanVien){
            txtStaffIdSalary.setText(nhanVien.getId());
            txtStaffNameSalary.setText(nhanVien.getName());
            txtLeaveDay.setText("Total leave time: "+nhanVien.getTotalLeaveTime());
            txtOTTime.setText("Total ot time:" + nhanVien.getOtTime());
            txtTotalSalary.setText("Total salary: "+nhanVien.calSalary());
        }
    }
}
