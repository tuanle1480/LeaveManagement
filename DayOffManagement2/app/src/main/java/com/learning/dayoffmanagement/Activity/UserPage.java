package com.learning.dayoffmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.fragment.SentLeaveFormFragment;
import com.learning.dayoffmanagement.fragment.SentOTFormFragment;
import com.learning.dayoffmanagement.fragment.ScheduleOTFragment;
import com.learning.dayoffmanagement.fragment.RegisterOTFragment;
import com.learning.dayoffmanagement.fragment.ApprovedLeaveFormFragment;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.Model.Users;
import com.learning.dayoffmanagement.fragment.DayOfListFragment;
import com.learning.dayoffmanagement.fragment.RegisterLeaveFrag;
import com.learning.dayoffmanagement.fragment.HomeFragment_User;
import com.learning.dayoffmanagement.fragment.UserFrag;

public class UserPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_USER = 1;
    private static final int FRAGMENT_CAL = 2;

    private int mCurrentFragment = FRAGMENT_HOME;
    private String getUID,getEmail;
    private DrawerLayout mDrawerLayout;
    private NhanVien getNhanVien;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private TextView userNameHeader,emailHeader;
    private ImageView imgStaffAvtHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
//  lấy uid và email đc gửi từ MainActivity
        getUID = getIntent().getStringExtra("uid");
        getEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_View);
        View headerView = navigationView.getHeaderView(0);
         userNameHeader = headerView.findViewById(R.id.txtUserNameHeader);
         emailHeader = headerView.findViewById(R.id.txtEmailHeader);
         imgStaffAvtHeader = headerView.findViewById(R.id.imgStaffAvtHeader);


//       Lấy dữ liệu user theo ID
        getUSerByUid(getUID);

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment_User(getUID));
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.nav_home){
////            if(mCurrentFragment != FRAGMENT_HOME){
////                replaceFragment(new HomeFragment());
////                mCurrentFragment = FRAGMENT_HOME;
////            }
//            replaceFragment(new HomeFragment());
//        }else if(id == R.id.nav_info){
////            if(mCurrentFragment != FRAGMENT_USER){
////                replaceFragment(new UserFrag());
////                mCurrentFragment = FRAGMENT_USER;
////            }
//            replaceFragment(new UserFrag());
//        }else if(id == R.id.nav_cal){
//            if(mCurrentFragment != FRAGMENT_CAL){
//                replaceFragment(new CalFrag());
//            }
//        }
        switch (id){
            case R.id.nav_home:
//                home
                replaceFragment(new HomeFragment_User(getUID));
                mCurrentFragment = FRAGMENT_HOME;
                break;

            case R.id.nav_info:
//                user - thông tin nhân viên
                replaceFragment(new UserFrag(getNhanVien));
                mCurrentFragment = FRAGMENT_USER;
                break;

            case R.id.nav_cal:
//                register leave - đăng kí nghỉ phép
                replaceFragment(new RegisterLeaveFrag(getNhanVien.getId(),getNhanVien.getName(),getNhanVien.getDepartment().getId()));
                mCurrentFragment = FRAGMENT_CAL;
                break;

            case R.id.nav_dof:
//                day off - tổng số thời gian nghỉ phép
                replaceFragment(new DayOfListFragment(getNhanVien.getId(),getNhanVien.getName()));
                break;

            case R.id.nav_approved:
//                approved leave form - các đơn nghỉ phép đã được phê duyệt
                replaceFragment(new ApprovedLeaveFormFragment(getNhanVien.getId(),getNhanVien.getName()));
                break;

            case R.id.nav_register_ot:
//                register ot - đăng kí tăng ca
                replaceFragment(new RegisterOTFragment(getNhanVien.getId(),getNhanVien.getName(),null,getNhanVien.getDepartment().getId()));
                break;


            case R.id.nav_schedule_ot:
//                schedule ot - lịch tăng ca
                replaceFragment(new ScheduleOTFragment(getNhanVien.getId(),getNhanVien.getName()));
                break;

            case R.id.nav_sent_leave:
//                sent leave form - các đơn nghỉ phép đã gửi ( bao gồm cả đơn chưa được phê duyệt)
                replaceFragment(new SentLeaveFormFragment(getNhanVien.getId(),getNhanVien.getName()));
                break;

            case R.id.nav_sent_ot:
//                sent ot - các đơn ot đã gửi(bao gồm các đơn chưa đc phê duyệt)
                replaceFragment(new SentOTFormFragment(getNhanVien.getId(),getNhanVien.getName()));
                break;


            case R.id.nav_logout:
//                logout
//                super.onBackPressed();
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    sự kiện khi ng dùng back về trên điện thoại
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_Frame, fragment);
        transaction.commit();
    }

// lấy dữ liệu user theo id
    private void getUSerByUid(String uid){
        reference.child("Users").child(uid.trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                getStaffById(users.getStaffId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
// lấy dữ liệu nhân viên theo id
    private void getStaffById(String id){
        reference.child("Staff").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    snapshot.getChildren().forEach(cSnap ->{
//                        Toast.makeText(UserPage.this, cSnap.getValue().toString(), Toast.LENGTH_SHORT).show();
//                    });
                    getNhanVien = snapshot.getValue(NhanVien.class);
                    userNameHeader.setText(getNhanVien.getName());
                    emailHeader.setText(getNhanVien.getEmail());
                    if(getNhanVien.getAvtUrl() != null) Glide.with(getApplicationContext()).load(getNhanVien.getAvtUrl()).into(imgStaffAvtHeader);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}