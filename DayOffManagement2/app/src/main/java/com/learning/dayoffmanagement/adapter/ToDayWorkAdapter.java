package com.learning.dayoffmanagement.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Model.ToDayWork;

import java.util.List;

public class ToDayWorkAdapter extends ArrayAdapter {
    private Activity context;
    private int resource;
    private List<ToDayWork> toDayWorks;

    public ToDayWorkAdapter(Activity context,int resource, List<ToDayWork> toDayWorks){
            super(context,resource,toDayWorks);
            this.context = context;
            this.resource = resource;
            this.toDayWorks = toDayWorks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = context.getLayoutInflater().inflate(resource,null);
        TextView txtTodayWorkName = view.findViewById(R.id.txtWorkName);
        TextView txtTodayWorkTime = view.findViewById(R.id.txtWorkTime);
        TextView txtTodayWorkLocation = view.findViewById(R.id.txtWorkLocation);

        ToDayWork toDayWork = toDayWorks.get(position);

        txtTodayWorkName.setText(toDayWork.getWorkName());
        txtTodayWorkTime.setText(toDayWork.getWorkTime());
        txtTodayWorkLocation.setText(toDayWork.getWorkLocation());

        return view;
    }

    @Override
    public int getCount() {
        return toDayWorks.size();
    }
}
