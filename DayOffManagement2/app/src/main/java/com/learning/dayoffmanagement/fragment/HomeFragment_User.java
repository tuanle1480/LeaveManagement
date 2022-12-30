package com.learning.dayoffmanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Model.ToDayWork;
import com.learning.dayoffmanagement.adapter.ToDayWorkAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment_User extends Fragment {
    private ListView recyTodayWork;
    private String uid;
    private ToDayWorkAdapter adapter;
    private List<ToDayWork> toDayWorks;
    public HomeFragment_User(String uid){
        this.uid = uid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        recyTodayWork = view.findViewById(R.id.todayWorkListView);
        generateData();

        adapter = new ToDayWorkAdapter(getActivity(),R.layout.today_work_item,toDayWorks);
        recyTodayWork.setAdapter(adapter);
        return view;
    }


    private void generateData(){
        toDayWorks = new ArrayList<>();
        toDayWorks.add(new ToDayWork("Work A","09:00","Department A"));
        toDayWorks.add(new ToDayWork("Work B","09:30","Department B"));
        toDayWorks.add(new ToDayWork("Work C","10:00","Department C"));
        toDayWorks.add(new ToDayWork("Work D","11:00","Department D"));
        toDayWorks.add(new ToDayWork("Work E","12:00","Department E"));
        toDayWorks.add(new ToDayWork("Work F","13:30","Department F"));
        toDayWorks.add(new ToDayWork("Work G","14:00","Department G"));
    }


}
