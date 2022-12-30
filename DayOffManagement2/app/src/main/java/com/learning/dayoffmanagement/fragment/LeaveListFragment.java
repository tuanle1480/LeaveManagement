package com.learning.dayoffmanagement.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Activity.DetailsLeaveFormActivity;
import com.learning.dayoffmanagement.Listener.LeaveListItemClick;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.LeaveListAdapter;

import java.util.ArrayList;
import java.util.List;


public class LeaveListFragment extends Fragment implements LeaveListItemClick {
    private RecyclerView recyLeaveList;
    private LeaveListAdapter adapter;
    private List<LeaveForm> leaveForms;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private String departmentId;
    private int roleID;

    public LeaveListFragment() {
        // Required empty public constructor
    }

    public LeaveListFragment(int roleID, String departmentId) {
        this.roleID = roleID;
        this.departmentId = departmentId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_list, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        recyLeaveList = view.findViewById(R.id.recyLeaveList);
    }

//    Lấy dữ liệu các đơn đk nghỉ phép đang chờ phê duyệt
    private void getData(){
        switch (roleID){
            case 1:
                getDataForAdmin();
                break;

            case 2:
                getDataForLeader();
                break;
        }

    }

    private void getDataForAdmin(){
        reference.child("WaitingLeaveForm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    leaveForms = new ArrayList<>();
                    snapshot.getChildren().forEach(cSnap ->{
                        LeaveForm leaveForm = cSnap.getValue(LeaveForm.class);
                        leaveForms.add(leaveForm);
                    });
                    binData(leaveForms);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataForLeader(){
        reference.child("WaitingLeaveForm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    leaveForms = new ArrayList<>();
                    snapshot.getChildren().forEach(cSnap ->{
                        LeaveForm leaveForm = cSnap.getValue(LeaveForm.class);

                        if(leaveForm.getDepartmentID()!=null ){
                            if(leaveForm.getDepartmentID().equals(departmentId)){
                                leaveForms.add(leaveForm);
                            }
                        }

                    });
                    binData(leaveForms);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void binData(List<LeaveForm> data){
        adapter = new LeaveListAdapter(getContext(),data,this);
        recyLeaveList.setAdapter(adapter);
    }

//     phương thức này ko có dùng - xóa đi cũng được
    private void getLeaveFormData(String id){
        reference.child("WaitingLeaveForm").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap ->{
                        String leaveForm = cSnap.getValue(String.class);
                        Toast.makeText(getContext(), leaveForm, Toast.LENGTH_SHORT).show();
//                        String key = cSnap.getKey();
//                        String value = cSnap.getValue(String.class);
//                        Toast.makeText(getActivity(), key+" - "+value, Toast.LENGTH_SHORT).show();
//                        leaveForms.add(leaveForm);
                    });
//

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void itemClick(LeaveForm pos) {
//        gửi dữ liệu và chuyển qua DetailsLeaveFormActivity
        startActivity(new Intent(getActivity(), DetailsLeaveFormActivity.class).putExtra("data",pos));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}