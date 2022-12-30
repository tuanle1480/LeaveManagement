package com.learning.dayoffmanagement.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.learning.dayoffmanagement.Model.ScheduleOT;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.ScheduleOTAdapter;

import java.util.ArrayList;
import java.util.List;


public class ScheduleOTFragment extends Fragment {
    private RecyclerView recyScheduleOT;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private List<ScheduleOT> scheduleOTS;
    private ScheduleOTAdapter adapter;

    private String id,name;

    public ScheduleOTFragment(String id, String name) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
    }

    public ScheduleOTFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_o_t, container, false);
//        init
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void init(View view){
        recyScheduleOT = view.findViewById(R.id.recyScheduleOT);
    }

//    Lấy dữ liệu lịch tăng ca
    private void getData(){
//        reference.child("ScheduleOT").child(id).get().addOnCompleteListener(task -> {
//            scheduleOTS = new ArrayList<>();
//            if(task.isSuccessful()){
//                ScheduleOT scheduleOT = task.getResult().getValue(ScheduleOT.class);
//                scheduleOTS.add(scheduleOT);
//            }
//            adapter = new ScheduleOTAdapter(scheduleOTS);
//            recyScheduleOT.setAdapter(adapter);
//        });

        reference.child("ScheduleOT").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(id)){
                    scheduleOTS = new ArrayList<>();
                   snapshot.getRef().child(id).get().addOnCompleteListener(task ->{
                        if(task.isSuccessful()){
                            ScheduleOT scheduleOT = task.getResult().getValue(ScheduleOT.class);
                            scheduleOTS.add(scheduleOT);
                            adapter = new ScheduleOTAdapter(scheduleOTS);
                            recyScheduleOT.setAdapter(adapter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "No has Schedule OT", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}