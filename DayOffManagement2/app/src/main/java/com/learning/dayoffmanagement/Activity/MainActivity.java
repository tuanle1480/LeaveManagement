package com.learning.dayoffmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.Department;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.Model.Users;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Until.Utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginBtn;
    TextView register;
    FirebaseAuth fbA = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pass1);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        register = (TextView) findViewById(R.id.register);

        loginBtn.setOnClickListener((v)-> loginUser());

//        reference.child("SentOTForm").removeValue();
//        reference.child("SentLeaveForm").removeValue();
//        reference.child("WaitingAcceptOT").removeValue();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

//        reference.child("Users").removeValue();
//        reference.child("Staff").removeValue();
//        reference.child("Department").removeValue();
//        reference.child("OT").removeValue();
//        reference.child("ScheduleOT").removeValue();

//        Department department = new Department("Phòng kế toán");
//        reference.child("Department").child(department.getId()).setValue(department);
//
//        Users users = new Users("admin@gmail.com","123456",1,"408u4hCTVJcSo80GzJT3qy8oaau2");
//
//
//        NhanVien nhanVien = new NhanVien(
//                "admin","Admin",
//                "123456789", "admin@gmail.com",
//                "TP HCM",
//                users,"01/01/1995");
//
//        users.setStaffId(nhanVien.getId());
//
//        reference.child("Users").child(users.getUid()).setValue(users);
//        reference.child("Staff").child(nhanVien.getId()).setValue(nhanVien);



    }


    void loginUser(){
        String email = username.getText().toString().trim();
        String passEmail = password.getText().toString().trim();

//        if (email.toString().isEmpty()){
//            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(passEmail.toString().isEmpty()){
//            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(email.isEmpty() || passEmail.isEmpty()){
            Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
        }else{
            loginAccountInFB(email,passEmail);
        }

//        Intent in = new Intent(MainActivity.this,AdminPage.class);
//        startActivity(in);


    }
    void loginAccountInFB(String email, String passEmail){

        fbA.signInWithEmailAndPassword(email,passEmail).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid= task.getResult().getUser().getUid();
                    String email = task.getResult().getUser().getEmail();
//                    Toast.makeText(MainActivity.this, uid, Toast.LENGTH_SHORT).show();
                    reference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users getUser = snapshot.getValue(Users.class);
                            if (getUser != null) {
                                Utility.showToast(MainActivity.this,"Success");
                                int roleID = getUser.getRoleID();
                                switch (roleID) {
                                    case 0:
                                        Intent in = new Intent(MainActivity.this, UserPage.class);
                                        in.putExtra("uid", uid);
                                        in.putExtra("email", email);
                                        startActivity(in);
                                        Utility.showToast(MainActivity.this, "USer");
                                        break;

                                    case 1:
                                        Intent intent = new Intent(MainActivity.this, AdminPage.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("email", email);
                                        intent.putExtra("roleID", roleID);
                                        startActivity(intent);
                                        Utility.showToast(MainActivity.this, "Admin");
                                        break;

                                    case 2:
                                        Intent intent1 = new Intent(MainActivity.this, AdminPage.class);
                                        intent1.putExtra("uid", uid);
                                        intent1.putExtra("email", email);
                                        intent1.putExtra("roleID", roleID);
                                        startActivity(intent1);
                                        Utility.showToast(MainActivity.this, "Leader");
                                        break;
                                }

                            }else{
                                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else
                {
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}