package com.learning.dayoffmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.ApprovedLeaveForm;
import com.learning.dayoffmanagement.Model.DayOff;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Until.Utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class DetailsLeaveFormActivity extends AppCompatActivity {
    private TextView txtSenderId,txtSenderName,txtSendDate,txtReason,txtStartTime,txtEndTime,txtStartDate,txtEndDate;
    private EditText edtResponse,edtTotalLeaveTime;
    private Button btAccept,btDeny;

    private Calendar calendar;
    private LeaveForm getLeaveForm;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_leave_form);
//        init view
        init();
//        getData
        getDataIntent();

//        events
        events();

    }

    private void init(){
        calendar = Calendar.getInstance();

        txtSenderId = findViewById(R.id.txtSenderIdDetails);
        txtSenderName = findViewById(R.id.txtSenderDetails);
        txtSendDate = findViewById(R.id.txtSendDateDetails);
        txtStartDate = findViewById(R.id.txtStartDateDetails);
        txtEndDate = findViewById(R.id.txtEndDateDetails);
        txtReason = findViewById(R.id.txtReasonDetails);
        txtStartTime = findViewById(R.id.txtStartLeaveTimeDetails);
        txtEndTime = findViewById(R.id.txtEndLeaveTimeDetails);

        edtTotalLeaveTime = findViewById(R.id.edtTotalLeaveTime);
        edtResponse = findViewById(R.id.edtResponse);

        btAccept = findViewById(R.id.btAccept);
        btDeny = findViewById(R.id.btDeny);

    }

//    L???y d??? li???u ???????c g???i qua t??? LeaveListFragment
    private void getDataIntent(){
        getLeaveForm = (LeaveForm) getIntent().getSerializableExtra("data");
        bindData(getLeaveForm);
    }

//  g???n d??? li???u l??n view
    private void bindData(LeaveForm leaveForm){
        txtSenderId.setText(leaveForm.getUid());
        txtSenderName.setText(leaveForm.getSender());
        txtSendDate.setText(leaveForm.getSendDate());
        txtStartDate.setText(leaveForm.getStartDate());
        txtEndDate.setText(leaveForm.getEndDate());
        txtReason.setText(leaveForm.getReason());
        txtStartTime.setText(!leaveForm.getStartTime().isEmpty() ? leaveForm.getStartTime() : "null");
        txtEndTime.setText(!leaveForm.getEndTime().isEmpty() ? leaveForm.getEndTime(): "null" );
        edtTotalLeaveTime.setText(leaveForm.getTotalLeaveTime()+"");

        if(leaveForm.getStartDate().equalsIgnoreCase(leaveForm.getEndDate())){
            enableEditTimeView(true);
        }else{
            enableEditTimeView(false);
        }
    }

//  k??ch ho???t ho???c v?? hi???u h??a edit time view
    private void enableEditTimeView(boolean isEnable){
        edtTotalLeaveTime.setEnabled(isEnable);
        txtEndDate.setEnabled(!isEnable);
    }

//  b???t s??? ki???n
    private void events(){
//        deny leave form
        btDeny.setOnClickListener(v->{
            denyLeaveForm();
        });

//        accept leave form
        btAccept.setOnClickListener(v->{
            acceptLeaveForm();
        });

//  s??? ki???n s???a t???ng th???i gian ngh??? ph??p n???u edtTotalLeaveTime ???????c k??ch ho???t
       if(edtTotalLeaveTime.isEnabled()){
           edtTotalLeaveTime.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   calEndTime(charSequence.toString());
               }

               @Override
               public void afterTextChanged(Editable editable) {

               }
           });
       }
//b???t s??? ki???n s???a ng??y k???t th??c ngh??? ph??p n???u txtEndDate ???????c k??ch ho???t
        if(txtEndDate.isEnabled()){
            txtEndDate.setOnClickListener(v->{
                selectEndDate();
            });
        }

    }

    private void denyLeaveForm(){
        reference.child("WaitingLeaveForm").child(getLeaveForm.getId()).removeValue().addOnCompleteListener(task->{
            if(task.isSuccessful()){
//                set l???i d??? li???u cho leave form ( b???i v?? d??? li???u c?? th??? ??c thay ?????i b???i admin)
                getLeaveForm.setStartDate(txtStartDate.getText().toString().trim());
                getLeaveForm.setEndDate(txtEndDate.getText().toString().trim());
                getLeaveForm.setEndTime(txtEndTime.getText().toString().trim());
                getLeaveForm.setFeedback(edtResponse.getText().toString().isEmpty() ? "": edtResponse.getText().toString());
                getLeaveForm.setState("Denied");
//                kh???i t???o approved leave form
                ApprovedLeaveForm approvedLeaveForm = new ApprovedLeaveForm(RandomStringUtils.randomAlphabetic(30)
                        ,getLeaveForm,edtResponse.getText().toString().isEmpty() ? "": edtResponse.getText().toString());
                approvedLeaveForm(approvedLeaveForm); // th??m approved leave form tr??n database
                updateSentLeaveForm(getLeaveForm,"Denied"); // c???p nh???t l???i th??ng tin c???a leaveform n???m trong sent leave from - c??c ????n ngh??? ph??p ???? g???i
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                finish(); // k???t th??c m??n h??nh
            }
        });
    }

//    ch???p nh???n ????n ngh??? ph??p
    private void acceptLeaveForm(){

        reference.child("WaitingLeaveForm").child(getLeaveForm.getId()).removeValue().addOnCompleteListener(task->{
            if(task.isComplete()){
//                set l???i d??? li???u cho leave form ( b???i v?? d??? li???u c?? th??? ??c thay ?????i b???i admin)
                getLeaveForm.setStartDate(txtStartDate.getText().toString().trim());
                getLeaveForm.setEndDate(txtEndDate.getText().toString().trim());
                getLeaveForm.setEndTime(txtEndTime.getText().toString().trim());
                getLeaveForm.setFeedback(edtResponse.getText().toString().isEmpty() ? "": edtResponse.getText().toString());
                getLeaveForm.setTotalLeaveTime(Double.parseDouble(edtTotalLeaveTime.getText().toString().trim()
                        .substring(0,edtTotalLeaveTime.getText().toString().trim().length()-1)));
                getLeaveForm.setState("Accepted");
//                t????ng t??? nh?? tr??n ph????ng th???c deny
                ApprovedLeaveForm approvedLeaveForm = new ApprovedLeaveForm(RandomStringUtils.randomAlphabetic(30)
                ,getLeaveForm,edtResponse.getText().toString().isEmpty() ? "": edtResponse.getText().toString());
                approvedLeaveForm(approvedLeaveForm);
                addDayOff(getLeaveForm);
                updateSentLeaveForm(getLeaveForm,"Accepted");
                Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
//        Toast.makeText(this, getLeaveForm.getUid(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, addDayOff(getLeaveForm), Toast.LENGTH_SHORT).show();
//        addDayOff(getLeaveForm);
    }
// th??m approved leave form tr??n database
    private void approvedLeaveForm(ApprovedLeaveForm approvedLeaveForm){
        reference.child("ApprovedLeaveForm").child(getLeaveForm.getSender()).child(approvedLeaveForm.getId()).setValue(approvedLeaveForm);
    }

    // c???p nh???t l???i th??ng tin c???a leaveform n???m trong sent leave from - c??c ????n ngh??? ph??p ???? g???i
    private void updateSentLeaveForm(LeaveForm leaveForm,String newState){
        HashMap data = new HashMap();
        data.put("state",newState);
        data.put("endDate",leaveForm.getEndDate());
        data.put("endTime",leaveForm.getEndTime());
        data.put("totalLeaveTime",leaveForm.getTotalLeaveTime());
        data.put("feedback",leaveForm.getFeedback());
        reference.child("SentLeaveForm").child(leaveForm.getUid()).child(leaveForm.getId()).updateChildren(data);

    }

// th??m d??? li???u v??o ng??y ngh???
    private void addDayOff(LeaveForm leaveForm){
        double res = leaveForm.getTotalLeaveTime();

        reference.child("DaysOff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                N???u nh??n vi??n ???? c?? xin ngh??? tr?????c ???? th?? c???p nh???t l???i t???ng s??? gi??? ngh??? ph??p c???a nh??n vi??n ????
                if(snapshot.hasChild(leaveForm.getUid())){
                    updateDaysOffNumber(leaveForm.getUid(),res);
                    Toast.makeText(DetailsLeaveFormActivity.this, "has child -> update", Toast.LENGTH_SHORT).show();
                }else{
//                    Ng?????c l???i n???u nh??n vi??n ???? ch??a ??k ngh??? ph??p trc ???? th?? th??m m???i d??? li???u l??n database
                    DayOff dayOff = new DayOff(leaveForm.getUid(),leaveForm.getSender(),res);
                    reference.child("DaysOff").child(leaveForm.getUid()).setValue(dayOff);
                    Toast.makeText(DetailsLeaveFormActivity.this, "not found -> add new", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//      c???p nh???t l???i t???ng s??? gi??? ngh??? ph??p c???a nh??n vi??n
    private void updateDaysOffNumber(String id, double newData){
        reference.child("DaysOff").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double oldData = snapshot.getValue(DayOff.class).getTotalLeaveTime();
                double finalData = oldData + newData;
                HashMap data = new HashMap();
                data.put("totalLeaveTime",finalData);
                reference.child("DaysOff").child(id).updateChildren(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//  t??nh l???i th???i gian k???t th??c ngh??? ph??p trong tr?????ng h???p admin ch???nh s???a l???i t???ng s??? gi??? ngh??? ph??p c???a ng??y ???? ( ch??? ??p d???ng trong tr?????ng h???p ngh??? trong ng??y)
    private void calEndTime(String totalTime){
        double getTotalTime = Double.parseDouble(totalTime);

            String getStartTime = txtStartTime.getText().toString().trim();
            StringTokenizer stkStartTime = new StringTokenizer(getStartTime,":");

            String getStartHour = stkStartTime.nextToken();
            String getStartMinute = stkStartTime.nextToken();

            int convertToMinute = (int)(getTotalTime * 60);
            String newEndTime ;
            if(convertToMinute < 60){
                int totalMinute = convertToMinute + Integer.parseInt(getStartMinute);
                if(totalMinute < 60){
                    newEndTime = getStartHour+":"+totalMinute;
                }else if(totalMinute == 60){
                    newEndTime = String.valueOf(Integer.parseInt(getStartHour)+1)+":"+"00";
                }else{
                    int newHour = totalMinute / 60;
                    int newMinute = totalMinute % 60;
                    newEndTime = String.valueOf(Integer.parseInt(getStartHour)+newHour) + ":"+newMinute;
                }

            }else if(convertToMinute == 60){
                newEndTime = String.valueOf(Integer.parseInt(getStartHour)+1) + ":" + getStartMinute;
            }else{

                int newHour = convertToMinute / 60;
                int newMinute = convertToMinute % 60 + Integer.parseInt(getStartMinute);
                newEndTime = String.valueOf(Integer.parseInt(getStartHour)+newHour) + ":"+newMinute;
            }

            txtEndTime.setText(newEndTime);

    }
// ch???n ng??y k???t th??c ngh??? ph??p - ch??? ??p d???ng trong tr?????ng h???p xin ngh??? ph??p nhi???u ng??y v?? admin mu???n s???a l???i ng??y k???t th??c ngh??? ph??p
    private void selectEndDate(){
//        hide keyboard if it showing
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        txtEndDate.setText(i2+"/"+i1+"/"+i);

                    }catch (Exception e){
                        txtEndDate.setText(i2+"/"+"1"+"/"+i);
                    }

                    double calLeaveDayNumber = Utility.calTime(txtStartDate.getText().toString().trim(),
                            txtEndDate.getText().toString().trim());

                        double newTotalLeaveTime = calLeaveDayNumber * 8;
                        edtTotalLeaveTime.setText(String.valueOf(newTotalLeaveTime));

                    }

                }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}