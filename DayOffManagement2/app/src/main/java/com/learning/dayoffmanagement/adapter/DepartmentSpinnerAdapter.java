package com.learning.dayoffmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.learning.dayoffmanagement.Model.Department;
import com.learning.dayoffmanagement.R;

import java.util.List;

public class DepartmentSpinnerAdapter extends ArrayAdapter<Department> {

    public DepartmentSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Department> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_item_spinner,parent,false);
        TextView txtAlbumName = (TextView) convertView.findViewById(R.id.txtDepartmentNameSpinner);
        Department department = this.getItem(position);
        if(department!=null){
            txtAlbumName.setText(department.getName().trim());
        }
        return convertView;
    }


}
