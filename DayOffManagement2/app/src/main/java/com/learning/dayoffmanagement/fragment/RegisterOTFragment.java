package com.learning.dayoffmanagement.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.Model.ScheduleOT;
import com.learning.dayoffmanagement.R;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RegisterOTFragment extends Fragment {
    private Calendar calendar;
    private TextView txtStartOTTime,txtEndOTTime,txtID,txtName;
    private ImageView imgSelectStartOTTime, imgSelectEndOTTime;
    private Button btRegisterOT,btSpecifiedOT;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private String id, name,admin,departmentId;

    public RegisterOTFragment(String id, String name,String admin,String departmentId) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.departmentId = departmentId;
    }

//    public RegisterOTFragment(String id, String name,String admin) {
//        this.id = id;
//        this.name = name;
//        this.admin = admin;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_register_o_t, container, false);
//        init view
        init(view);
//        set data
        setData();
//        event
        events();
//        get request
        getBundleRequestData();
        return view;
    }

    private void init(View view){
        calendar = Calendar.getInstance();
        txtStartOTTime = view.findViewById(R.id.txtStartOTTime);
        txtEndOTTime = view.findViewById(R.id.txtEndOTTime);
        txtID = view.findViewById(R.id.txtOTStaffID);
        txtName = view.findViewById(R.id.txtOTStaffName);

        imgSelectStartOTTime = view.findViewById(R.id.imgSelectStartOTTime);
        imgSelectEndOTTime = view.findViewById(R.id.imgSelectEndOTTime);

        btRegisterOT = view.findViewById(R.id.btRegisterOT);
        btSpecifiedOT = view.findViewById(R.id.btSpecifiedOT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
//    l???y request ???????c g???i t???i
    private void getBundleRequestData(){
        Bundle getRequest = getArguments();
        if(getRequest!=null){
            String request = getRequest.getString("request");
            switch (request){
//                tr?????ng h???p ch??? ?????nh t??ng ca
                case "specified":
                    btSpecifiedOT.setVisibility(View.VISIBLE);
                    btRegisterOT.setVisibility(View.INVISIBLE);
                    specifiedOT();
                    break;
            }
        }
    }

    private void setData(){
        txtName.setText(name);
        txtID.setText(id);
    }

    private void events(){
        imgSelectStartOTTime.setOnClickListener(v->{
            handleSelectTime(txtStartOTTime);
        });

        imgSelectEndOTTime.setOnClickListener(v->{
            handleSelectTime(txtEndOTTime);
        });

        btRegisterOT.setOnClickListener(v->{
            registerOT();
        });

    }

//      ch??? ?????nh t??ng ca
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void specifiedOT(){

        btSpecifiedOT.setOnClickListener(v->{
            String startOTTime = txtStartOTTime.getText().toString().trim();
            String endOTTime = txtEndOTTime.getText().toString().trim();

            double otTime = (handleTime(endOTTime) - handleTime(startOTTime))/60; // t??nh t???ng th???i gi???n t??ng ca
            OTForm ot = new OTForm(RandomStringUtils.randomAlphabetic(30),id,name,startOTTime,endOTTime,admin,otTime,departmentId);
            ot.setOvertime(otTime);
// th??m d??? li???u l??n database
            reference.child("OT").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(id)){
//                        N???u nh??n vi??n ???? ???? c?? t??ng ca tr?????c ???? th?? c???p nh???t l???i t???ng tgian t??ng ca
                        updateOTTime(id,otTime);
                    }else{ // ng?????c l???i n???u ch??a c?? t??ng ca trc ???? th?? th??m m???i d??? li???u
                        reference.child("OT").child(id).setValue(ot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            ScheduleOT scheduleOT = new ScheduleOT(id,startOTTime,endOTTime,otTime,admin);
            reference.child("ScheduleOT").child(id).setValue(scheduleOT);
            Toast.makeText(getContext(), "SPECIFIED", Toast.LENGTH_SHORT).show();
        });


    }
//c???p nh???t t???ng tgian t??ng ca
    private void updateOTTime(String id, double newData){
        reference.child("OT").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double oldValue = snapshot.getValue(OTForm.class).getOvertime();
                double newValue = oldValue+newData;
                HashMap data = new HashMap();
                data.put("overtime",newValue);
                reference.child("OT").child(id).updateChildren(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    ????ng k?? t??ng ca
    private void registerOT(){
        String startOTTime = txtStartOTTime.getText().toString().trim();
        String endOTTime = txtEndOTTime.getText().toString().trim();

        double otTime = (handleTime(endOTTime) - handleTime(startOTTime))/60;
        OTForm ot = new OTForm(RandomStringUtils.randomAlphabetic(30),id,name,startOTTime,endOTTime,otTime,departmentId);
        ot.setOvertime(otTime);

        reference.child("WaitingAcceptOT").child(ot.getId()).setValue(ot);
        addToSentOTForm(ot);

    }

//    th??m d??? li???u v??o c??c ????n t??ng ca ???? g???i
    private void addToSentOTForm(OTForm ot){
        reference.child("SentOTForm").child(ot.getUid()).child(ot.getId()).setValue(ot).addOnCompleteListener(task -> {
            if(task.isComplete()){
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                SentOTFormFragment fragment = new SentOTFormFragment(id,name);
                transaction.replace(R.id.content_Frame, fragment);
                transaction.commit();
            }
        });
    }


// t??nh t???ng s??? gi??? gi???a 2 tgian ( gi???)
    private int handleTime(String time){
        StringTokenizer stk = new StringTokenizer(time,":");
        String hour = stk.nextToken();
        String minute = time.substring(time.indexOf(":")+1);

        return Integer.parseInt(hour)*60 + Integer.parseInt(minute);
    }

//     ch???n gi???
    private void handleSelectTime(TextView textView){
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
}