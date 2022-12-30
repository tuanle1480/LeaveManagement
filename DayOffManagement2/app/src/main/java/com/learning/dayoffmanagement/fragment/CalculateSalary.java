package com.learning.dayoffmanagement.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.DayOff;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.CalculateSalaryAdapter;
import com.learning.dayoffmanagement.adapter.StaffAdapter;


import java.util.ArrayList;
import java.util.List;


public class CalculateSalary extends Fragment {
    private RecyclerView recyCalSalary;
    private List<NhanVien> staffs;
    private List<DayOff> dayOffs;
    private List<OTForm> ots;
    private CalculateSalaryAdapter adapter;

    private int roleId;
    private String departmentId;


    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public CalculateSalary(){}

    public CalculateSalary(int roleId, String departmentId){
        this.roleId = roleId;
        this.departmentId = departmentId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_caculate_salary, container, false);
//        init
        init(view);
//        getData
        switch (roleId){
            case 1:
                getDataForAdmin();
                break;

            case 2:
                getDataForLeader();
                break;
        }
        getOTTimeData();
//        getDayOffData();
        return view;
    }

    private void init(View view){
        recyCalSalary = view.findViewById(R.id.recySalaryStaff);
    }

//  lấy dữ liệu của tất cả nhân viên
    private void getDataForAdmin(){
        reference.child("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                staffs = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap ->{
                        NhanVien nhanVien = cSnap.getValue(NhanVien.class);
    //                        Toast.makeText(getContext(), nhanVien.getOffice(), Toast.LENGTH_SHORT).show();
                        if(!nhanVien.getId().equalsIgnoreCase("admin")) {
                            staffs.add(nhanVien);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
    });
}

    private void getDataForLeader(){
        reference.child("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                staffs = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap ->{
                        NhanVien nhanVien = cSnap.getValue(NhanVien.class);
                        if(nhanVien.getOffice() != null && nhanVien.getOffice().equalsIgnoreCase("employee")) {
                            if (nhanVien.getDepartment().getId().equals(departmentId))
                                staffs.add(nhanVien);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//  cập nhật lại dữ liệu về tổng số thời gian nghỉ phép và ot của tất cả nhân viên
    private List<NhanVien> upDataData(List<NhanVien> data,List<DayOff> dayOffData,List<OTForm> otData){

        List<NhanVien> newData = new ArrayList<>();

        for(int i=0;i<data.size();i++){
            NhanVien cS = data.get(i);
            for(int j=0;j<dayOffData.size();j++){
                DayOff cD = dayOffData.get(j);
                if(cS.getId().equalsIgnoreCase(cD.getId())){
                    cS.setTotalLeaveTime(cD.getTotalLeaveTime());
                }
//                break;
            }

            for(int k =0;k<otData.size();k++){
                OTForm cO = otData.get(k);
                if(cS.getId().equalsIgnoreCase(cO.getUid())){
                    cS.setOtTime(cO.getOvertime());
                }
            }

            newData.add(cS);
        }
        bindData(newData);

        return newData;
    }

//    lấy dữ liệu về tổng số thời gian nghỉ phép của nhân viên
    private void getDayOffData(){
        dayOffs = new ArrayList<>();
        reference.child("DaysOff").get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        DayOff value = cSnap.getValue(DayOff.class);
                        dayOffs.add(value);
                    });
                    upDataData(staffs,dayOffs,ots);
                }
            }
        });
    }

//     lấy dữ liệu về tổng thời gian ot của nhân viên
    private void getOTTimeData(){
        ots = new ArrayList<>();
        reference.child("OT").get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        OTForm value = cSnap.getValue(OTForm.class);
                        ots.add(value);
                    });

                }
            }
        });
    }

    private void bindData(List<NhanVien> data){
        adapter = new CalculateSalaryAdapter(data);
        recyCalSalary.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getData();
        getDayOffData();
    }
}