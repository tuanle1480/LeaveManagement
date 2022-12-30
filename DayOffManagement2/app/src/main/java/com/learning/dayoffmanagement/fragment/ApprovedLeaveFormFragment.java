package com.learning.dayoffmanagement.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Activity.DetailsApprovedLeaveFormActivity;
import com.learning.dayoffmanagement.Listener.ApprovedLeaveFormItemClick;
import com.learning.dayoffmanagement.Model.ApprovedLeaveForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.ApprovedLeaveFormAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ApprovedLeaveFormFragment extends Fragment implements ApprovedLeaveFormItemClick {
    private RecyclerView recyApproved;
    private List<ApprovedLeaveForm> approvedLeaveForms;
    private ApprovedLeaveFormAdapter adapter;
    private String id,name;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public ApprovedLeaveFormFragment(String id,String name) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
    }

    public ApprovedLeaveFormFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approved_leave_form, container, false);
//        init view
        init(view);
//        getData

//        spinner item selected event

        return view;
    }

    private void init(View view){
        recyApproved = view.findViewById(R.id.recyApproved);

    }

//     lấy dữ liệu các đơn đã phê duyệt trên database
    private void getData(){
        approvedLeaveForms = new ArrayList<>();
        reference.child("ApprovedLeaveForm").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap ->{
                        ApprovedLeaveForm value = cSnap.getValue(ApprovedLeaveForm.class);
                        approvedLeaveForms.add(value);
                    });
                    adapter = new ApprovedLeaveFormAdapter(getContext(),approvedLeaveForms,ApprovedLeaveFormFragment.this);
                    recyApproved.setAdapter(adapter);
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

    @Override
    public void viewApprovedLeaveForm(int pos) {
//        gửi dữ liệu và chuyển  qua DetailsApprovedLeaveFormActivity
        startActivity(new Intent(getActivity(), DetailsApprovedLeaveFormActivity.class).
                putExtra("approvedData",approvedLeaveForms.get(pos)));

    }

}