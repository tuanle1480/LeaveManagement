package com.learning.dayoffmanagement.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.learning.dayoffmanagement.Model.Department;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.Model.Users;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.DepartmentSpinnerAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class AddStaffActivity extends AppCompatActivity {
    private EditText edtNewStaffUserName,edtNewStaffPassword,edtNewStaffId,edtNewStaffName,edtNewStaffAddress,edtNewStaffPhoneNumber;
    private EditText edtNewStaffEmail,edtNewStaffSalary;
    private TextView txtNewStaffBirthDay,txtHeader;
    private ImageView imgSelectNewStaffBirthDay,imgStaffAvt;
    private Button btAddNewStaff,btOk,btDeleteStaff,btUpdateStaff;

    private AutoCompleteTextView selectOfficeSpinner,selectDepartmentSpinner;
    private ArrayAdapter spinnerOfficeAdapter;
    private DepartmentSpinnerAdapter departmentSpinnerAdapter;

    private Calendar calendar;
    private LocalDate birthDay;

    private NhanVien getStaff;
    private List<Department> departments;

    private Department departmentSelected;

    private String officeSelected,departmentId;
    private int roleId;

    private boolean isEdit = false;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private ActivityResultLauncher<String> imagePermissionResultLauncher;

    private Uri avtURI;

    private StorageTask uploadAvtTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
//        init
        init();
//        getDepartment Data

//        events
        events();

//        get request
        getRequest();


//        permission
            imagePermissionResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if(result){
                                selectImageFromGallery();
                            }else{
                                Toast.makeText(AddStaffActivity.this, "Permission DENIED!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
    }

    private void selectImageFromGallery() {
        TedImagePicker.with(AddStaffActivity.this)
                .start(uri -> {
                    avtURI = uri;
                    imgStaffAvt.setImageURI(uri);
                });
    }


    private void init(){
        calendar = Calendar.getInstance();

        edtNewStaffUserName = findViewById(R.id.edtNewStaffUserName);
        edtNewStaffPassword = findViewById(R.id.edtNewStaffPassword);
        edtNewStaffId = findViewById(R.id.edtNewStaffId);
        edtNewStaffName = findViewById(R.id.edtNewStaffName);
        edtNewStaffAddress = findViewById(R.id.edtNewStaffAddress);
        edtNewStaffPhoneNumber = findViewById(R.id.edtNewStaffPhoneNumber);
        edtNewStaffEmail = findViewById(R.id.edtNewStaffEmail);
        edtNewStaffSalary = findViewById(R.id.edtNewStaffSalary);

        txtNewStaffBirthDay = findViewById(R.id.txtNewStaffBirthDay);
        txtHeader = findViewById(R.id.textView2);

        imgSelectNewStaffBirthDay = findViewById(R.id.imgSelectNewStaffBirthDay);
        imgStaffAvt = findViewById(R.id.imgStaffAvt);

        btAddNewStaff = findViewById(R.id.btAddNewStaff);
        btOk = findViewById(R.id.btOk);
        btDeleteStaff = findViewById(R.id.btDeleteStaff);
        btUpdateStaff = findViewById(R.id.btUpdateStaff);

        selectOfficeSpinner = findViewById(R.id.selectOffice);
        selectOfficeSpinner.setInputType(0);

        selectDepartmentSpinner = findViewById(R.id.selectDepartment);
        selectDepartmentSpinner.setInputType(0);


    }

//    bắt sự kiện cho các button
    private void events(){
//        sự kiện chọn ngày sinh
        imgSelectNewStaffBirthDay.setOnClickListener(v->{
            selectStartDate();
        });
//    sự kiện thêm nhân viên
        btAddNewStaff.setOnClickListener(v->{
            try{
                addNewStaff(); // gọi tới phương thức addNewStaff()
            }catch (NullPointerException ex){
                ex.printStackTrace();
                Toast.makeText(this, "Haven error when add", Toast.LENGTH_SHORT).show();
            }
        });
//     sự kiện kết thúc xem thông tin nhân viên
        btOk.setOnClickListener(v->{
            finish();
        });
//      sự kiện xóa nhân viên
        btDeleteStaff.setOnClickListener(v->{
            deleteStaff(getStaff);// gọi tới phương thức deleteStaff()
        });

//        cập nhật thông tin nhân viên
        btUpdateStaff.setOnClickListener(v->{
            updateStaff(getStaff);
        });

//        sự kiện chọn chức vị
        selectOfficeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                officeSelected = (String) adapterView.getItemAtPosition(i);
                selectOfficeSpinner.setText(officeSelected,false);
            }
        });

        selectDepartmentSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                departmentSelected = (Department) adapterView.getItemAtPosition(i);
                selectDepartmentSpinner.setText(departmentSelected.getName(),false);
            }
        });

//        chọn avt cho nhân viên
        imgStaffAvt.setOnClickListener(v->{
            imagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });


    }

//    lấy yêu cầu đc gửi qua từ StaffManaFragment
    private void getRequest(){
        String request = getIntent().getStringExtra("request");
        switch (request){
            case "add":
                roleId = getIntent().getIntExtra("roleId",999);
                departmentId = getIntent().getStringExtra("departmentId");
                switch (roleId){
                    case 1:
                        getDepartmentDataForAdmin();
                        spinnerOfficeAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,generateOfficeDataForAdmin());
                        break;

                    case 2:
                        getDepartmentDataForLeader();
                        spinnerOfficeAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,generateOfficeDataForLeader());
                        break;
                }
                selectOfficeSpinner.setAdapter(spinnerOfficeAdapter);
                selectOfficeSpinner.setInputType(0);
                break;

            case "view":
                getStaffData();
                disableEditView(); //  ẩn các view dùng để edit nhân viên

                roleId = getIntent().getIntExtra("roleId",999);
                departmentId = getIntent().getStringExtra("departmentId");
                switch (roleId){
                    case 1:
                        getDepartmentDataForAdmin();
                        spinnerOfficeAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,generateOfficeDataForAdmin());
                        break;

                    case 2:
                        getDepartmentDataForLeader();
                        spinnerOfficeAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,generateOfficeDataForLeader());
                        break;
                }
                selectOfficeSpinner.setAdapter(spinnerOfficeAdapter);

                break;
        }
    }

    // lấy dữ liệu của  nhân viên được gửi qua
    private void getStaffData(){
        getStaff = (NhanVien) getIntent().getSerializableExtra("staffData");
        bindData(getStaff); // gắn kết dữ liệu vào view
    }

    private List<String> generateOfficeDataForAdmin(){
        return Arrays.asList("Employee","Leader");
    }

    private List<String> generateOfficeDataForLeader(){
        return Arrays.asList("Employee");
    }

    private void getDepartmentDataForAdmin(){
        reference.child("Department").get().addOnCompleteListener(task ->{
            departments = new ArrayList<>();
            if (task.isComplete()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        departments.add(cSnap.getValue(Department.class));
                    });
                    departmentSpinnerAdapter = new DepartmentSpinnerAdapter(AddStaffActivity.this,R.layout.department_item_spinner,departments);
                    selectDepartmentSpinner.setAdapter(departmentSpinnerAdapter);
                    selectDepartmentSpinner.setInputType(0);
                }
            }
        });
    }

    private void getDepartmentDataForLeader(){
        reference.child("Department").get().addOnCompleteListener(task ->{
            departments = new ArrayList<>();
            if (task.isComplete()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    task.getResult().getChildren().forEach(cSnap ->{
                        Department department = cSnap.getValue(Department.class);
                        if(department.getId().equals(departmentId)) departments.add(department);

                    });
                    departmentSpinnerAdapter = new DepartmentSpinnerAdapter(AddStaffActivity.this,R.layout.department_item_spinner,departments);
                    selectDepartmentSpinner.setAdapter(departmentSpinnerAdapter);
                    selectDepartmentSpinner.setInputType(0);
                }
            }
        });
    }

    private void bindData(NhanVien nhanVien){
        edtNewStaffId.setText(nhanVien.getId());
        edtNewStaffUserName.setText(nhanVien.getAccount().getUserName());
        edtNewStaffPassword.setText(nhanVien.getAccount().getPassword());
        edtNewStaffName.setText(nhanVien.getName());
        edtNewStaffEmail.setText(nhanVien.getEmail());
        edtNewStaffPhoneNumber.setText(nhanVien.getPhoneNumber());
        edtNewStaffSalary.setText(nhanVien.getSalary()+"");
        edtNewStaffAddress.setText(nhanVien.getAddress());
        selectDepartmentSpinner.setText(nhanVien.getDepartment().getName(),false);

        txtNewStaffBirthDay.setText(nhanVien.getDayOfBirth());

        selectOfficeSpinner.setText(nhanVien.getOffice(),false);
        selectDepartmentSpinner.setInputType(0);

        if(nhanVien.getAvtUrl() != null) Glide.with(getApplicationContext()).load(nhanVien.getAvtUrl()).into(imgStaffAvt);

    }

    private void disableEditView(){
//        edtNewStaffId.setEnabled(false);
//        edtNewStaffUserName.setEnabled(false);
//        edtNewStaffPassword.setEnabled(false);
//        edtNewStaffName.setEnabled(false);
//        edtNewStaffEmail.setEnabled(false);
//        edtNewStaffPhoneNumber.setEnabled(false);
//        edtNewStaffSalary.setEnabled(false);
//        edtNewStaffAddress.setEnabled(false);
//
//        selectDepartmentSpinner.setEnabled(false);
//        selectOfficeSpinner.setEnabled(false);


        txtHeader.setText("VIEW OR EDIT STAFF");

//        imgSelectNewStaffBirthDay.setEnabled(false);
//        imgStaffAvt.setEnabled(false);

        btAddNewStaff.setVisibility(View.INVISIBLE);
        btOk.setVisibility(View.VISIBLE);
        btDeleteStaff.setVisibility(View.VISIBLE);
        btUpdateStaff.setVisibility(View.VISIBLE);
    }

    private void selectStartDate(){
//        hide keyboard if it is showing
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        i1 += 1;
                        birthDay = LocalDate.of(i, i1, i2);
                        txtNewStaffBirthDay.setText(i2 + "/" + i1 + "/" + i);

                    }catch (Exception e){
                        txtNewStaffBirthDay.setText(i2+"/1/"+i);
                    }
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddStaffActivity.this,dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addNewStaff(){

        if(avtURI != null) {

            //        khởi tạo tài khoản cho nhân viên
            Users account = new Users(edtNewStaffUserName.getText().toString().trim(),
                    edtNewStaffPassword.getText().toString().trim(),0,"");

        //khởi tạo đối tượng nhân viên
            NhanVien nhanVien = new NhanVien(
                    edtNewStaffId.getText().toString().trim(),edtNewStaffName.getText().toString().trim(),
                    edtNewStaffPhoneNumber.getText().toString().trim(), edtNewStaffEmail.getText().toString().trim(),
                    edtNewStaffAddress.getText().toString().trim(),departmentSelected,officeSelected,Integer.parseInt(edtNewStaffSalary.getText().toString().trim()),
                    account,txtNewStaffBirthDay.getText().toString().trim(),avtURI.toString());

            account.setStaffId(nhanVien.getId());

            if(officeSelected.equalsIgnoreCase("leader")){
                account.setRoleID(2);
                departmentSelected.setLeaderId(nhanVien.getId());
            }
//        Toast.makeText(AddStaffActivity.this, nhanVien.getDepartment().getId() +"\t"+ nhanVien.getDepartment().getName(), Toast.LENGTH_SHORT).show();

//            addNewUser(account);
            // thêm account lên database

            FirebaseAuth fba = FirebaseAuth.getInstance();

            fba.createUserWithEmailAndPassword(account.getUserName(), account.getPassword()).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    account.setUid(task.getResult().getUser().getUid());
                    reference.child("Users").child(account.getUid()).setValue(account);
                    nhanVien.setAccount(account);
                    reference.child("Staff").child(nhanVien.getId()).setValue(nhanVien); // thêm nhân viên lên database
                    storageReference.child("StaffAvts").child(nhanVien.getId());
                    uploadAvtTask = storageReference.putFile(avtURI);

                    Toast.makeText(AddStaffActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(e ->{
                Toast.makeText(this,"Happened error when add staff", Toast.LENGTH_SHORT).show();
            });


        }else{
            Toast.makeText(this, "Please choose a image", Toast.LENGTH_SHORT).show();
        }


    }


    private void updateStaff(NhanVien staff){
        String newId = edtNewStaffId.getText().toString().trim();
        String newName = edtNewStaffName.getText().toString().trim();
        String newPhoneNumber = edtNewStaffPhoneNumber.getText().toString().trim();
        String newEmail = edtNewStaffEmail.getText().toString().trim();
        String newAddress = edtNewStaffAddress.getText().toString().trim();
        Department newDepartment = departmentSelected == null ? staff.getDepartment() : departmentSelected;
        String newOffice = officeSelected == null ? staff.getOffice() : officeSelected;
        int newSalary = Integer.parseInt(edtNewStaffSalary.getText().toString().trim());
        String birthDay = txtNewStaffBirthDay.getText().toString().trim();

        HashMap newValue = new HashMap();
        newValue.put("id",newId);
        newValue.put("name",newName);
        newValue.put("phoneNumber",newPhoneNumber);
        newValue.put("email",newEmail);
        newValue.put("address",newAddress);
        newValue.put("office",newOffice);
        newValue.put("salary",newSalary);
        newValue.put("dayOfBirth",birthDay);
        newValue.put("department",newDepartment);

        if(avtURI != null){
            String newAvtUrl = avtURI.toString();
            newValue.put("avtUrl",newAvtUrl);
            storageReference.child("StaffAvts").child(staff.getId());
            uploadAvtTask = storageReference.putFile(avtURI);
        }

            reference.child("Staff").child(staff.getId()).updateChildren(newValue); // update nhân viên lên database


            Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show();
            finish();

    }



    private void deleteStaff(NhanVien staff){
        reference.child("Staff").child(staff.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddStaffActivity.this, "Remove Successful", Toast.LENGTH_SHORT).show();
                    deleteUser(staff.getAccount().getUid());
                    deleteAuth(staff.getAccount().getUserName(),staff.getAccount().getPassword());
                    finish();
                }else{
                    Toast.makeText(AddStaffActivity.this, "Remove ERROR!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    xóa user
    private void deleteUser(String uid){
        reference.child("Users").child(uid).removeValue();
    }
//  xóa tài khoản trên Firebase authenticate
    private void deleteAuth(String email,String pass){

        // lấy user đang đăng nhập
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserUid = user.getUid();
        String currentUserEmail = user.getEmail();

//        đăng nhâp vào tk cần xóa
        FirebaseAuth fba = FirebaseAuth.getInstance();
        fba.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task ->{
           if(task.isComplete()){
//               Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();

               AuthCredential credential = EmailAuthProvider
                       .getCredential(email, pass);

               user.reauthenticate(credential);

               if (user != null) {
                   user.reauthenticate(credential).addOnCompleteListener(task1 -> user.delete());
               }
           }
        });

//        Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();

    }



}