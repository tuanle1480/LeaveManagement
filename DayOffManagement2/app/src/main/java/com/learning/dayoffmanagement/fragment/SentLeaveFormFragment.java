package com.learning.dayoffmanagement.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.learning.dayoffmanagement.Activity.DetailsApprovedLeaveFormActivity;
import com.learning.dayoffmanagement.Activity.DetailsLeaveFormActivity;
import com.learning.dayoffmanagement.Listener.LeaveListItemClick;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.LeaveListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


public class SentLeaveFormFragment extends Fragment implements LeaveListItemClick {
    private RecyclerView recySentLeaveList;
    private LeaveListAdapter adapter;
    private List<LeaveForm> sents;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private MaterialCheckBox cbApproved,cbWaiting;
    private AutoCompleteTextView selectMonthFilterSpinner;
    private ArrayAdapter spinnerAdapter;

    private String id,name;

    public SentLeaveFormFragment(String id,String name) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
    }

    public SentLeaveFormFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sent_leave_form, container, false);
//        init
        init(view);
//        event
        events();
        return view;
    }

    private void init(View view){
        recySentLeaveList = view.findViewById(R.id.recySentLeaveList);
        cbApproved = view.findViewById(R.id.cbApprovedLeaveForm);
        cbWaiting = view.findViewById(R.id.cbWaitingLeaveForm);
        selectMonthFilterSpinner = view.findViewById(R.id.selectMonthFilter);
        spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1,generateMonth());
        selectMonthFilterSpinner.setAdapter(spinnerAdapter);
        selectMonthFilterSpinner.setInputType(0);
        selectMonthFilterSpinner.setText("",false);
    }

//    khởi tạo dữ liệu cho việc lọc theo tháng
    private List<String> generateMonth(){
        return Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12");
    }

    private void events(){

        cbApproved.setOnClickListener(v->{
//            cbApproved.setChecked(true);
            cbWaiting.setChecked(false);
            if(cbApproved.isChecked()){
                cbApproved.setChecked(true);
                adapter.getFilter().filter("Approved");
            }else{
                cbApproved.setChecked(false);
                adapter.getFilter().filter("");
            }

        });

        cbWaiting.setOnClickListener(v->{
            cbApproved.setChecked(false);
            if(cbWaiting.isChecked()){
                cbWaiting.setChecked(true);
                adapter.getFilter().filter("Waiting");
            }else{
                cbWaiting.setChecked(false);
                adapter.getFilter().filter("");
            }

        });

        selectMonthFilterSpinner.setOnItemClickListener(((adapterView, view, i, l) -> {
            String month = (String) adapterView.getItemAtPosition(i);
            selectMonthFilterSpinner.setText(month,false);
            cbApproved.setChecked(false);
            cbWaiting.setChecked(false);
            adapter.getFilter().filter(month);
        }));
    }

// lấy dữ liệu các đơn nghỉ phép đã gửi
    private void getData(){
        reference.child("SentLeaveForm").child(id).get().addOnCompleteListener(task -> {
            sents = new ArrayList<>();
            if(task.isSuccessful()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap->{
                        LeaveForm value = cSnap.getValue(LeaveForm.class);
                        sents.add(value);
                    });
                    binData(sents);

                }
            }
        });
    }



    private void binData(List<LeaveForm> data){
        adapter = new LeaveListAdapter(getContext(),data,this);
        recySentLeaveList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1,generateMonth());
        selectMonthFilterSpinner.setAdapter(spinnerAdapter);
        selectMonthFilterSpinner.setInputType(0);
        selectMonthFilterSpinner.setText("",false);
        getData();

    }

    @Override
    public void itemClick(LeaveForm pos) {
//        gửi dữ liệu và chuyển sang DetailsApprovedLeaveFormActivity
        startActivity(new Intent(getActivity(), DetailsApprovedLeaveFormActivity.class).putExtra("LeaveFormData",pos));
    }

    @Override
    public void onPause() {
        super.onPause();
        selectMonthFilterSpinner.setText("",false);
    }

}