package com.learning.dayoffmanagement.fragment;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Activity.AddStaffActivity;
import com.learning.dayoffmanagement.Listener.StaffItemClick;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.StaffAdapter;

import java.util.ArrayList;
import java.util.List;


public class StaffManaFragment extends Fragment implements StaffItemClick {
    private RecyclerView recyStaffList;
    private StaffAdapter adapter;
    private List<NhanVien> staffs;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FloatingActionButton fabAddStaff;
    private String departmentID;
    private int roleId;

    public StaffManaFragment(int roleId,String departmentID) {
        // Required empty public constructor
        this.roleId = roleId;
        this.departmentID = departmentID;
    }

    public StaffManaFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff_mana, container, false);
//        init view
        init(view);

//        events
        events();

        return view;
    }

//    lấy dữ liệu tất cả nhân viên

    private void getData(){
        switch (roleId){
            case 1:
                getDataForAdmin();
                break;

            case 2:
                getDataForLeader();
                break;
        }
    }

    private void getDataForAdmin(){
        reference.child("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                staffs = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap ->{
                        NhanVien nhanVien = cSnap.getValue(NhanVien.class);
                        if(!nhanVien.getId().equalsIgnoreCase("admin")) {
                            staffs.add(nhanVien);
                        }
                    });

                    adapter = new StaffAdapter(getContext(),staffs,StaffManaFragment.this);
                    recyStaffList.setAdapter(adapter);

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
                            if (nhanVien.getDepartment().getId().equals(departmentID))
                                staffs.add(nhanVien);
                        }
                    });
                    adapter = new StaffAdapter(getContext(),staffs,StaffManaFragment.this);
                    recyStaffList.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(View view){
        recyStaffList = view.findViewById(R.id.recyStaffList);
        fabAddStaff = view.findViewById(R.id.fabAddStaff);
    }

    private void events(){
//        add staff event
        fabAddStaff.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(),AddStaffActivity.class);
            intent.putExtra("request","add");
            intent.putExtra("roleId",roleId);
            intent.putExtra("departmentId",departmentID);
            startActivity(intent);
        });
    }

    @Override
    public void viewStaff(int pos) {
//        gửi dữ liệu và chuyển sang màn hình AddStaffActivity - áp dụng với trường hợp xem thông tin chi tiết của nhân viên
        Intent intent = new Intent(getActivity(),AddStaffActivity.class);
        intent.putExtra("request","view");
        intent.putExtra("staffData",staffs.get(pos));
        intent.putExtra("roleId",roleId);
        intent.putExtra("departmentId",departmentID);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        //get Data
        getData();
    }

}