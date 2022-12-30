package com.learning.dayoffmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Model.Users;
import com.learning.dayoffmanagement.Until.Utility;

public class Register extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    Button regisBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        regisBtn = (Button) findViewById(R.id.signupbtn);

        regisBtn.setOnClickListener((v)-> reigsUser());
    }

    void reigsUser() {
        String nameOfUser = name.getText().toString().trim();
        String UserEmail = email.getText().toString().trim();
        String UserPassword = password.getText().toString().trim();

        if (nameOfUser.toString().isEmpty()){
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(UserEmail.toString().isEmpty()){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (UserPassword.toString().isEmpty()){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUserAccount(nameOfUser,UserEmail,UserPassword);
    }
        void registerUserAccount(String nameOfUser, String UserEmail, String UserPassword){
            FirebaseAuth fba = FirebaseAuth.getInstance();
            fba.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
//                        Users users = new Users(nameOfUser,UserEmail,UserPassword,0,uid);
                        Users users = new Users();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                        firebaseDatabase.getReference().child("Users").child(uid).setValue(users);

                        Utility.showToast(Register.this,"Register Success");
                    }else{
                        Utility.showToast(Register.this,"Cant not Create accout");
                    }
            }});
    }
}