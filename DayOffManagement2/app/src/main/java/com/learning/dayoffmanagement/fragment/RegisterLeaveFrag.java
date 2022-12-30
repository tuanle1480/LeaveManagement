package com.learning.dayoffmanagement.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Until.Utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.StringTokenizer;

public class RegisterLeaveFrag extends Fragment {
    private Calendar calendar;
    private TextView txtStartDate,txtEndDate,txtRegisterLeaveStartTime,txtRegisterLeaveEndTime;
    private EditText edtReason,edtTotalLeaveTime;
    private ImageView imgSelectStartDate, imgSelectEndDate,imgSelectStartTime,imgSelectEndTime;
    private Button btSend;
    private LocalDate startDate,endDate;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String id;
    private String sender,departmentId;
    private double totalLeaveTime = 0.0;

    public RegisterLeaveFrag(String id,String sender,String departmentId){
        this.id = id;
        this.sender = sender;
        this.departmentId = departmentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_leave, container, false);

//       init
        init(view);

//
        enableSelectTimeView(false);

//        events
        imgSelectStartDate.setOnClickListener(v->{
            selectStartDate();
        });

        imgSelectEndDate.setOnClickListener(v->{
            selectEndDate();
        });

        imgSelectStartTime.setOnClickListener(v->{
            handleSelectTime(v,txtRegisterLeaveStartTime);
        });

        imgSelectEndTime.setOnClickListener(v->{
            handleSelectTime(v,txtRegisterLeaveEndTime);
        });

        btSend.setOnClickListener(v->{
            String getReason = edtReason.getText().toString();
            LeaveForm leaveForm = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                leaveForm = new LeaveForm(RandomStringUtils.randomAlphabetic(30),id,sender,
                        txtStartDate.getText().toString(),
                        txtEndDate.getText().toString(),getReason,txtRegisterLeaveStartTime.getText().toString().trim(),
                        txtRegisterLeaveEndTime.getText().toString().trim(),totalLeaveTime,departmentId);
                LeaveForm finalLeaveForm = leaveForm;
                reference.child("WaitingLeaveForm").child(leaveForm.getId()).setValue(leaveForm).addOnCompleteListener(task->{
                    if(task.isComplete()){
                        addToSentForm(finalLeaveForm);
                    }
                });

            }
        });
        return view;
    }

    private void init(View view){
        calendar = Calendar.getInstance();
        txtStartDate = (TextView) view.findViewById(R.id.txtStartDate);
        txtEndDate = (TextView) view.findViewById(R.id.txtEndDate);
        txtRegisterLeaveStartTime = (TextView) view.findViewById(R.id.txtRegisterLeaveStartTime);
        txtRegisterLeaveEndTime = (TextView) view.findViewById(R.id.txtRegisterLeaveEndTime);

        imgSelectStartDate = (ImageView) view.findViewById(R.id.imgSelectStartDate);
        imgSelectEndDate = (ImageView) view.findViewById(R.id.imgSelectEndDate);
        imgSelectStartTime = (ImageView) view.findViewById(R.id.imgSelectStartTime);
        imgSelectEndTime = (ImageView) view.findViewById(R.id.imgSelectEndTime);

        btSend = (Button) view.findViewById(R.id.btSend);

        edtReason = (EditText) view.findViewById(R.id.edtReson);
        edtTotalLeaveTime = view.findViewById(R.id.edtTotalRegisterLeaveTime);

    }

//    chọn ngày bắt đầu nghỉ phép
    private void selectStartDate(){
//        hide keyboard if it showing
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                calendar.set(Calendar.YEAR,i);
//                calendar.set(Calendar.MONTH,i1);
//                calendar.set(Calendar.DAY_OF_MONTH,i2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try{
                        i1+=1;
                        startDate = LocalDate.of(i,i1,i2);
                        txtStartDate.setText(i2+"/"+i1+"/"+i);
                    }catch (Exception e){
                        txtStartDate.setText(i2+"/"+"1"+"/"+i);
                    }

                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

//    chọn ngày kết thúc nghỉ phép
    private void selectEndDate(){
//        hide keyboard if it showing
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                calendar.set(Calendar.YEAR,i);
//                calendar.set(Calendar.MONTH,i1);
//                calendar.set(Calendar.DAY_OF_MONTH,i2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try{
                        i1+=1; // cộng thêm 1 bởi vì ko cộng tháng sẽ bị lùi lại 1 đơn vị
                        endDate = LocalDate.of(i,i1,i2);
                        txtEndDate.setText(i2+"/"+i1+"/"+i);

                    }catch (Exception e){
                        txtEndDate.setText(i2+"/"+"1"+"/"+i);
                    }

//                    tính tổng số ngày nghỉ phép
                    double calLeaveDayNumber = Utility.calTime(txtStartDate.getText().toString().trim(),
                            txtEndDate.getText().toString().trim());

                    if(calLeaveDayNumber > 0.0) { // nếu lớn hơn 0 -> nghỉ nhiều ngày
                        totalLeaveTime = calLeaveDayNumber * 8;
                        edtTotalLeaveTime.setText(totalLeaveTime+"H");
                        enableSelectTimeView(false);
                    }else{ // nếu = 0 -> nghỉ trong ngày
                        enableSelectTimeView(true);
                    }

                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void enableSelectTimeView(boolean isEnable){
        imgSelectStartTime.setEnabled(isEnable);
        imgSelectEndTime.setEnabled(isEnable);
        txtRegisterLeaveStartTime.setText("");
        txtRegisterLeaveEndTime.setText("");
    }

//    chọn giờ
    private void handleSelectTime(View viewClicked , TextView textView){
//         hide keyboard if it showing
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(hourOfDay+":"+minute);

                if(viewClicked.getId() == R.id.imgSelectEndTime){
                    String startOTTime = txtRegisterLeaveStartTime.getText().toString().trim();
                    String endOTTime = txtRegisterLeaveEndTime.getText().toString().trim();
                    totalLeaveTime = (handleTime(endOTTime) - handleTime(startOTTime))/60;
                    edtTotalLeaveTime.setText(totalLeaveTime+"h");
                }

            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true
        );


//           Show
        timePickerDialog.show();
    }

//    tính khoảng cách giữa 2 thời gian ( giờ)
    private double handleTime(String time){
        StringTokenizer stk = new StringTokenizer(time,":");
        String hour = stk.nextToken();
        String minute = time.substring(time.indexOf(":")+1);

        return (double) Math.ceil((double) (Double.parseDouble(hour)*60 + Double.parseDouble(minute)) * 10)/10;
    }

//    thêm dữ liệu vào SentLeaveForm
    private void addToSentForm(LeaveForm leaveForm){
        reference.child("SentLeaveForm").child(id).child(leaveForm.getId()).setValue(leaveForm).addOnCompleteListener(task->{
            if(task.isComplete()){
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                SentLeaveFormFragment fragment = new SentLeaveFormFragment(id,sender);
                transaction.replace(R.id.content_Frame, fragment);
                transaction.commit();
            }
        });

    }
}
