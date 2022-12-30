package com.learning.dayoffmanagement.fragment;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.learning.dayoffmanagement.Activity.AddStaffActivity;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;

import java.util.HashMap;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class UserFrag extends Fragment {
    private NhanVien nhanVien;
    private TextView txtStaffId,txtStaffName,txtStaffDateOfBirth,txtStaffEmail,txtDepartment;
    private EditText edtStaffPhoneNumber,edtStaffAddress;
    private Button btUpdate;
    private ImageView imgStaffAvt;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private ActivityResultLauncher<String> imagePermissionResultLauncher;
    private Uri avtURI;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageTask uploadAvtTask;

    public UserFrag(NhanVien nhanVien){
        this.nhanVien = nhanVien;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user, container, false);
        init(view);
        bindData(nhanVien);

        //        permission
        imagePermissionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            selectImageFromGallery();
                        }else{
                            Toast.makeText(requireActivity(), "Permission DENIED!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

//        sự kiện chọn hình ảnh
        imgStaffAvt.setOnClickListener(v->{
            imagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        return view;
    }

    private void init(View view){
        txtStaffId = view.findViewById(R.id.txtStaffId);
        txtStaffName = view.findViewById(R.id.txtStaffName);
        txtStaffEmail = view.findViewById(R.id.txtStaffEmail);
        txtStaffDateOfBirth = view.findViewById(R.id.txtStaffDateOfBirth);
        txtDepartment = view.findViewById(R.id.txtStaffDepartment);

        edtStaffAddress = view.findViewById(R.id.edtStaffAddress);
        edtStaffPhoneNumber = view.findViewById(R.id.edtStaffPhoneNumber);

        btUpdate = view.findViewById(R.id.btAccept);

        imgStaffAvt = view.findViewById(R.id.imgViewStaffAvt);

        btUpdate.setOnClickListener(v->{
            upDateStaff(nhanVien.getId());
        });
    }

    private void bindData(NhanVien nhanVien){
        txtStaffId.setText(nhanVien.getId());
        txtStaffName.setText(nhanVien.getName());
        txtStaffEmail.setText(nhanVien.getEmail());
        txtStaffDateOfBirth.setText(nhanVien.getDayOfBirth());
        txtDepartment.setText(nhanVien.getDepartment().getName());

        edtStaffAddress.setText(nhanVien.getAddress());
        edtStaffPhoneNumber.setText(nhanVien.getPhoneNumber());

        if(nhanVien.getAvtUrl() != null) Glide.with(requireContext()).load(nhanVien.getAvtUrl()).into(imgStaffAvt);
    }

//    cập nhật lại dữ liệu của nhân viên - chỉ cập nhật đc address và phonenumber

    private void upDateStaff(String id){
        if(avtURI != null){
            String newAddress = edtStaffAddress.getText().toString().trim();
            String newPhoneNumber = edtStaffPhoneNumber.getText().toString().trim();
            String newAvtUrl = avtURI.toString();

            storageReference.child("StaffAvts").child(id);

            nhanVien.setAddress(newAddress);
            nhanVien.setPhoneNumber(newPhoneNumber);
            nhanVien.setAvtUrl(newAvtUrl);

            HashMap newStaff  = new HashMap();
            newStaff.put("address",newAddress);
            newStaff.put("phoneNumber",newPhoneNumber);
            newStaff.put("avtUrl",newAvtUrl);

            reference.child("Staff").child(id).updateChildren(newStaff).addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Update Fail", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(getContext(), "Please choose a image", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectImageFromGallery() {
        TedImagePicker.with(requireActivity())
                .start(uri -> {
                    avtURI = uri;
                    imgStaffAvt.setImageURI(uri);
                });
    }
}
