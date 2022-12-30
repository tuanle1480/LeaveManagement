package com.learning.dayoffmanagement.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.dayoffmanagement.Model.ApprovedLeaveForm;
import com.learning.dayoffmanagement.Model.LeaveForm;
import com.learning.dayoffmanagement.R;

public class DetailsApprovedLeaveFormActivity extends AppCompatActivity {
    private TextView txtDetailsSendDateApproved,txtDetailsStartDateApproved,txtDetailsEndDateApproved,
            txtDetailsReasonApproved,txtDetailsSFeedbackApproved,txtDetailsStartTimeApproved,
            txtDetailsEndTimeApproved,txtDetailsTotalTimeApproved;

    private Button btFinishView;
    private ApprovedLeaveForm getApprovedLeaveForm;
    private LeaveForm getLeaveForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_approved_leave_form);
//        init view
        init();
//        getData
        getData();
//        events
        events();
    }

    private void init(){
        txtDetailsSendDateApproved = findViewById(R.id.txtDetailsSendDateApproved);
        txtDetailsStartDateApproved = findViewById(R.id.txtDetailsStartDateApproved);
        txtDetailsEndDateApproved = findViewById(R.id.txtDetailsEndDateApproved);
        txtDetailsReasonApproved = findViewById(R.id.txtDetailsReasonApproved);
        txtDetailsSFeedbackApproved = findViewById(R.id.txtDetailsFeedbackApproved);
        txtDetailsStartTimeApproved = findViewById(R.id.txtDetailsStartTimeApproved);
        txtDetailsEndTimeApproved = findViewById(R.id.txtDetailsEndTimeApproved);
        txtDetailsTotalTimeApproved = findViewById(R.id.txtDetailsTotalTimeApproved);


        btFinishView = findViewById(R.id.btFinishViewDetailsApproved);
    }
// Lấy dữ liệu được gửi qua từ Activity
    private void getData(){
//        nếu dữ liệu được gửi qua là ApprovedForm - trong trường hợp dữ liệu được gửi qua từ ApprovedLeaveListFragment
        getApprovedLeaveForm = (ApprovedLeaveForm) getIntent().getSerializableExtra("approvedData");
        if(getApprovedLeaveForm!=null){
            bindData(getApprovedLeaveForm);
        }
//        nếu dữ liệu được gửi qua là LeaveForm - trong trường hợp dữ liệu được gửi qua từ SentLeaveFormFragment
        getLeaveForm = (LeaveForm) getIntent().getSerializableExtra("LeaveFormData");
        if(getLeaveForm != null){
            bindData(getLeaveForm);
        }

    }

//  gắn dữ liệu lên view
    private void bindData(ApprovedLeaveForm data){
        txtDetailsSendDateApproved.setText(data.getLeaveForm().getSendDate());
        txtDetailsStartDateApproved.setText(data.getLeaveForm().getStartDate());
        txtDetailsEndDateApproved.setText(data.getLeaveForm().getEndDate());
        txtDetailsReasonApproved.setText(data.getLeaveForm().getReason());
        txtDetailsSFeedbackApproved.setText(data.getFeedback());
        txtDetailsStartTimeApproved.setText(!data.getLeaveForm().getStartTime().isEmpty() ? data.getLeaveForm().getStartTime()
                : "null");
        txtDetailsEndTimeApproved.setText(!data.getLeaveForm().getEndTime().isEmpty() ? data.getLeaveForm().getEndTime()
                : "null");
        txtDetailsTotalTimeApproved.setText(data.getLeaveForm().getTotalLeaveTime()+"");

    }

    //  gắn dữ liệu lên view
    private void bindData(LeaveForm data){
        txtDetailsSendDateApproved.setText(data.getSendDate());
        txtDetailsStartDateApproved.setText(data.getStartDate());
        txtDetailsEndDateApproved.setText(data.getEndDate());
        txtDetailsReasonApproved.setText(data.getReason());
        txtDetailsSFeedbackApproved.setText(data.getFeedback());
        txtDetailsStartTimeApproved.setText(!data.getStartTime().isEmpty() ? data.getStartTime()
                : "null");
        txtDetailsEndTimeApproved.setText(!data.getEndTime().isEmpty() ? data.getEndTime()
                : "null");
        txtDetailsTotalTimeApproved.setText(data.getTotalLeaveTime()+"");

    }
//bắt sự kiện kết thúc xem
    private void events(){
        btFinishView.setOnClickListener(v->{
            finish();
        });
    }
}