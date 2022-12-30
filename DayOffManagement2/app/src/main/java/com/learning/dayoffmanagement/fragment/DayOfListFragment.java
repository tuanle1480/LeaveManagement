package com.learning.dayoffmanagement.fragment;

import android.os.Build;
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
import com.learning.dayoffmanagement.Listener.DayOffItemClick;
import com.learning.dayoffmanagement.Model.DayOff;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.DayOffAdapter;

import java.util.ArrayList;
import java.util.List;

public class DayOfListFragment extends Fragment implements DayOffItemClick {
    private List<DayOff> dayOffs;
    private DayOffAdapter adapter;

    private RecyclerView recyDaysOff;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private String id,name;

    public DayOfListFragment(String id,String name) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
    }

    public DayOfListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_of_list, container, false);
//        init
        init(view);

        getData();
        return view;
    }

    private void init(View view){
        recyDaysOff = view.findViewById(R.id.recyDaysOff);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getData();
    }
//lấy dữ liệu về tổng số thời gian nghỉ phép của nhân viên cụ thể - lấy theo id của nhân viên đó khi đăg nhập
    private void getData(){
        dayOffs = new ArrayList<>();
        reference.child("DaysOff").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        snapshot.getChildren().forEach(cSnap ->{
                            DayOff dayOff = snapshot.getValue(DayOff.class);
                            if(!dayOffs.contains(dayOff) )dayOffs.add(dayOff);
                        });
                        adapter = new DayOffAdapter(dayOffs,DayOfListFragment.this);
                        recyDaysOff.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteDayOff(DayOff pos) {
        reference.child("DaysOff").child(pos.getId().trim()).removeValue().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                adapter.notifyDataSetChanged();
            }
        });
    }
}