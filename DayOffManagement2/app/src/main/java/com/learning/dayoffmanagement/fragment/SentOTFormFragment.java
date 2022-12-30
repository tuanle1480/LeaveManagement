package com.learning.dayoffmanagement.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.SentOTAdapter;

import java.util.ArrayList;
import java.util.List;


public class SentOTFormFragment extends Fragment {
    private String id,sender;
    private RecyclerView recySentOTList;
    private List<OTForm> ots;
    private SentOTAdapter adapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private MaterialCheckBox cbApproved,cbWaiting;

    public SentOTFormFragment(String id,String sender) {
        // Required empty public constructor
        this.id = id;
        this.sender = sender;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sent_o_t_form, container, false);
//        init
        init(view);
//        events
        events();
        return view;
    }



    private void init(View view){
        recySentOTList = view.findViewById(R.id.recySentOTList);
        cbApproved = view.findViewById(R.id.cbApprovedOTForm);
        cbWaiting = view.findViewById(R.id.cbWaitingOTForm);
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
    }
// lấy dữ liệu các đơn tăng ca đã gửi
    private void getData(){
        reference.child("SentOTForm").child(id).get().addOnCompleteListener(task->{
            ots = new ArrayList<>();
            if(task.isComplete()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        OTForm value = cSnap.getValue(OTForm.class);
                        ots.add(value);
                    });

                    Log.d("sentotform",ots.toString());

                    bindData(ots);
                }
            }
        });
    }

    private void bindData(List<OTForm> data){
        adapter = new SentOTAdapter(data);
        recySentOTList.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


}