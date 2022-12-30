package com.learning.dayoffmanagement.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Listener.SpecifiedOTItemClick;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.SpecifiOTAdapter;
import com.learning.dayoffmanagement.adapter.StaffAdapter;

import java.util.ArrayList;
import java.util.List;


public class SpecifiedOTFragment extends Fragment implements SpecifiedOTItemClick {
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private List<NhanVien> staffs;
    private RecyclerView recySpecifiedOT;
    private SpecifiOTAdapter adapter;
    private String id,name,departmentId;
    private int roleId;

    public SpecifiedOTFragment(String id,String name,int roleId,String departmentId) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.roleId = roleId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specified_o_t, container, false);
//        init
        init(view);
        return view;
    }

    private void init(View view){
        recySpecifiedOT = view.findViewById(R.id.recySpecifiedOT);
    }

//    Lấy dữ liệu tất cả nhân viên
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
//                        Toast.makeText(getContext(), nhanVien.getOffice(), Toast.LENGTH_SHORT).show();
                        if(!nhanVien.getId().equalsIgnoreCase("admin")) {
                            staffs.add(nhanVien);
                        }
                    });

                    adapter = new SpecifiOTAdapter(staffs,SpecifiedOTFragment.this);
                    recySpecifiedOT.setAdapter(adapter);

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
                    adapter = new SpecifiOTAdapter(staffs,SpecifiedOTFragment.this);
                    recySpecifiedOT.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

//    chỉ định tăng ca
    @Override
    public void specifiedOT(NhanVien nhanVien) {
//        chuyển sang RegisterOTFragment
        replaceFragment(new RegisterOTFragment(nhanVien.getId(),nhanVien.getName(),name,departmentId));
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle bundleRequest = new Bundle();
        bundleRequest.putString("request","specified");
        fragment.setArguments(bundleRequest);
        transaction.replace(R.id.content_admin_Frame, fragment).addToBackStack("SpecifiedOTFrag");
        transaction.commit();
    }
}