package com.learning.dayoffmanagement.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.Form;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.FormAdapter;
import com.learning.dayoffmanagement.adapter.StaffAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StaticitisFragment extends Fragment {
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private SearchView searchView;
    private RecyclerView recyForm;
    private MaterialCheckBox cbLeaveForm, cbOTForm;

    private FormAdapter adapter;
    private List<Form> forms;
    private List<OTForm> ots;
    private List<LeaveForm> leaveForms;
    private List<NhanVien> staffs;

    private String departmentID;
    private int roleId;


    public StaticitisFragment() {
        // Required empty public constructor
    }

    public StaticitisFragment(int roleId,String departmentID) {
        this.roleId = roleId;
        this.departmentID = departmentID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staticitis, container, false);
//        init
        init(view);

//        events
        events();
//

        return view;
    }

    private void init(View view){
        searchView = view.findViewById(R.id.searchFormBar);
        recyForm = view.findViewById(R.id.recyForm);
        cbLeaveForm = view.findViewById(R.id.cbLeaveFormFilter);
        cbOTForm = view.findViewById(R.id.cbOTFormFilter);

        ots = new ArrayList<>();
        leaveForms = new ArrayList<>();
        forms = new ArrayList<>();
    }

    private void events(){

        cbLeaveForm.setOnClickListener(v->{
//            cbApproved.setChecked(true);
            cbOTForm.setChecked(false);
            if(cbLeaveForm.isChecked()){
                cbLeaveForm.setChecked(true);
                adapter.getFilter().filter("Leave Form");
            }else{
                cbLeaveForm.setChecked(false);
                adapter.getFilter().filter("");
            }

        });

        cbOTForm.setOnClickListener(v->{
            cbLeaveForm.setChecked(false);
            if(cbOTForm.isChecked()){
                cbOTForm.setChecked(true);
                adapter.getFilter().filter("OT Form");
            }else{
                cbOTForm.setChecked(false);
                adapter.getFilter().filter("");
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

//  L???y d??? li???u t???t c??? nh??n vi??n
    /*
    V?? firebase ch???y theo quy t???c async - b???t ?????ng b???, n??n kh??ng th??? l???y d??? tr??n nhi???u b???ng trong c??ng m???t l??c
     */
    private void getStaffDataForAdmin(){

        reference.child("Staff").get().addOnCompleteListener(task->{
//            forms = new ArrayList<>();
//            ots = new ArrayList<>();
//            leaveForms = new ArrayList<>();

            if(task.isComplete()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        NhanVien nhanVien = cSnap.getValue(NhanVien.class);
                        if(!nhanVien.getId().equalsIgnoreCase("admin")
                                && nhanVien.getOffice().equalsIgnoreCase("employee")) {
                            forms.clear();

                            // l???y d??? li???u t??ng ca c???a nh??n vi??n theo id
                                    getOTFormData(nhanVien.getId());
//                         l???y d??? li???u ngh??? ph??p c???a nh??n vi??n theo id
                                    getLeaveFormData(nhanVien.getId());
//                            Toast.makeText(getContext(), nhanVien.toString() , Toast.LENGTH_SHORT).show();
                        }

                    });

                    mergeData();
                    bindData(forms);
                }
            }
        });

    }

    private void getStaffDataForLeader(){

        reference.child("Staff").get().addOnCompleteListener(task->{
//            forms = new ArrayList<>();
//            ots = new ArrayList<>();
//            leaveForms = new ArrayList<>();
            if(task.isComplete()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        NhanVien nhanVien = cSnap.getValue(NhanVien.class);
                        if(nhanVien.getOffice() != null && nhanVien.getOffice().equalsIgnoreCase("employee")) {
                            if (nhanVien.getDepartment().getId().equals(departmentID)){
                                forms.clear();
                            // l???y d??? li???u t??ng ca c???a nh??n vi??n theo id
                                getOTFormData(nhanVien.getId());
//                         l???y d??? li???u ngh??? ph??p c???a nh??n vi??n theo id
                                getLeaveFormData(nhanVien.getId());
//                                Toast.makeText(getContext(), nhanVien.toString() , Toast.LENGTH_SHORT).show();
                                }
                        }

                    });

                    mergeData();
                    bindData(forms);
                }
            }
        });

    }

//     l???y d??? li???u t??ng ca c???a nh??n vi??n theo id
    private void getOTFormData(String id){
            reference.child("SentOTForm").child(id).get().addOnCompleteListener(task->{
                if(task.isComplete()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        task.getResult().getChildren().forEach(cSnap ->{
                            OTForm value = cSnap.getValue(OTForm.class);
                            if(!ots.contains(value)) ots.add(value);
                        });

                    }
                }
            });

    }

// l???y d??? li???u ngh??? ph??p c???a nh??n vi??n theo id
    private void getLeaveFormData(String id){

        reference.child("SentLeaveForm").child(id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap->{
                        LeaveForm value = cSnap.getValue(LeaveForm.class);
                        if(!leaveForms.contains(value)) leaveForms.add(value);
                    });
//                    forms.addAll(leaveForms);
                }
            }
        });
    }

    private void mergeData(){
        forms.addAll(leaveForms);
        forms.addAll(ots);
    }


    private void bindData(List<Form> data){
        adapter = new FormAdapter(data);
        recyForm.setAdapter(adapter);
    }



    @Override
    public void onResume() {
        super.onResume();
//        getOTFormData();
//        bindData(forms);
//        Log.d("message",forms.toString());

        switch (roleId){
            case 1:
                getStaffDataForAdmin();
                break;

            case 2:
                getStaffDataForLeader();
                break;
        }

    }

}