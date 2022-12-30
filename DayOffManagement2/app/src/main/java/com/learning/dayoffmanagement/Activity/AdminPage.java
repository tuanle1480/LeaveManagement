package com.learning.dayoffmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Model.NhanVien;
import com.learning.dayoffmanagement.Model.Users;
import com.learning.dayoffmanagement.fragment.OTFragment;
import com.learning.dayoffmanagement.fragment.SpecifiedOTFragment;
import com.learning.dayoffmanagement.fragment.CalculateSalary;
import com.learning.dayoffmanagement.MeNuModel.ExpandedMenuModel;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.fragment.StaffManaFragment;
import com.learning.dayoffmanagement.adapter.ExpandableListAdapter;
import com.learning.dayoffmanagement.fragment.LeaveListFragment;
import com.learning.dayoffmanagement.fragment.StaticitisFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminPage extends AppCompatActivity {
    private String getUID,getEmail;
    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    private NhanVien getNhanVien;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private TextView userNameHeader,emailHeader;
    private int getRoleID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
//      getIntent Data
        getUID = getIntent().getStringExtra("uid");
        getEmail = getIntent().getStringExtra("email");
        getRoleID = getIntent().getIntExtra("roleID",1);


//
        final ActionBar ab = getSupportActionBar();
        /* to set the menu icon image*/
        ab.setHomeAsUpIndicator(R.drawable.ic_navi);
        ab.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_View_admin);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
            View headerView = navigationView.getHeaderView(0);
            userNameHeader = headerView.findViewById(R.id.txtUserNameHeader);
            emailHeader = headerView.findViewById(R.id.txtEmailHeader);
        }

//  lấy dữ liệu user bằng id
        getUSerByUid(getUID);
//  nội dung trên ExpandableList
        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);


//        bắt sự kiện trên các menu cha của ExpandableList
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                switch (i){
//                    Sự kiện Logout
                    case 2:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        break;
                }
                return false;
            }
        });

//        switch (getRoleID){
//            case 1:
//                replaceFragment(new LeaveListFragment(getRoleID,null));
//                break;
//
//            case 2:
////                replaceFragment(new LeaveListFragment(getRoleID,getNhanVien.getDepartment().getId()));
//                Toast.makeText(this, nvArr[0].getDepartment().getId(), Toast.LENGTH_SHORT).show();
//                break;
//        }
    }

    private void handleLeaveManaParent(int i,NhanVien nhanVien){
        switch (i){
            case 0:
//                Danh sách nghỉ phép

                switch (getRoleID){
                    case 1:
                        replaceFragment(new LeaveListFragment(getRoleID,null));
                        break;

                    case 2:
                        replaceFragment(new LeaveListFragment(getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }
                mDrawerLayout.closeDrawers();
                break;

            case 1:

                break;
        }
    }

    private void handleStaffManaParent(int i,NhanVien nhanVien){
        switch (i){
            case 0:
//                danh sách nhân viên

                switch (getRoleID){
                    case 1:
                        replaceFragment(new StaffManaFragment(getRoleID,null));
                        break;

                    case 2:

                        replaceFragment(new StaffManaFragment(getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }
                mDrawerLayout.closeDrawers();
                break;

            case 1:
//                Tính lương nhân viên
                switch (getRoleID){
                    case 1:
                        replaceFragment(new CalculateSalary(getRoleID,null));
                        break;

                    case 2:

                        replaceFragment(new CalculateSalary(getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }

                mDrawerLayout.closeDrawers();
                break;

            case 2:
//                Duyệt đơn OT
                switch (getRoleID){
                    case 1:
                        replaceFragment(new OTFragment(nhanVien.getId(),nhanVien.getName(),getRoleID,null));
                        break;

                    case 2:

                        replaceFragment(new OTFragment(nhanVien.getId(),nhanVien.getName(),getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }

                mDrawerLayout.closeDrawers();
                break;

            case 3:
//                Chỉ định OT
                switch (getRoleID){
                    case 1:
                        replaceFragment(new SpecifiedOTFragment(nhanVien.getId(),nhanVien.getName(),getRoleID,null));
                        break;

                    case 2:

                        replaceFragment(new SpecifiedOTFragment(nhanVien.getId(),nhanVien.getName(),getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }

                mDrawerLayout.closeDrawers();
                break;

            case 4:
//                Thống kê
                switch (getRoleID){
                    case 1:
                        replaceFragment(new StaticitisFragment(getRoleID,null));
                        break;

                    case 2:
                        replaceFragment(new StaticitisFragment(getRoleID,nhanVien.getDepartment().getId()));
                        break;
                }

                mDrawerLayout.closeDrawers();
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_admin_Frame, fragment);
        transaction.commit();
    }

    //  nội dung trên ExpandableList
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Quản lý Nghỉ Phép");
        item1.setIconImg(R.drawable.ic_calendar);
        // Adding data header
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Quản lý nhân viên");
        item2.setIconImg(R.drawable.ic_people);
        listDataHeader.add(item2);


        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Logout");
        item3.setIconImg(R.drawable.ic_logout);
        listDataHeader.add(item3);

        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        heading1.add("Danh sách nghỉ phép");
//        heading1.add("Thêm nghỉ phép");
//        heading1.add("Chỉnh sửa nghỉ phép");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Danh sách nhân viên");
//        heading2.add("thêm, sửa, xoá tt nhân viên");
        heading2.add("Tính lương nhân viên ");
        heading2.add("Duyệt OT");
        heading2.add("Chỉ định OT");
        heading2.add("Thống kê");
        //Logout


        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

//    Lấy dữ liệu user theo id
    private void getUSerByUid(String uid){
        reference.child("Users").child(uid.trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
//                Toast.makeText(AdminPage.this, users.getStaffId(), Toast.LENGTH_SHORT).show();
                getStaffById(users.getStaffId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    Lấy dữ liệu nhân viên theo id
    private void getStaffById(String id){
        reference.child("Staff").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    snapshot.getChildren().forEach(cSnap ->{
//                        Toast.makeText(UserPage.this, cSnap.getValue().toString(), Toast.LENGTH_SHORT).show();
//                    });
                    getNhanVien = snapshot.getValue(NhanVien.class);
//                    gán dữ liệu lên  Drawer
                    userNameHeader.setText(getNhanVien.getName());
                    emailHeader.setText(getNhanVien.getEmail());
                    switch (getRoleID){
                        case 1:
                            replaceFragment(new LeaveListFragment(getRoleID,null));
                            break;

                        case 2:
                             replaceFragment(new LeaveListFragment(getRoleID,getNhanVien.getDepartment().getId()));
//                            Toast.makeText(this, nvArr[0].getDepartment().getId(), Toast.LENGTH_SHORT).show();
                            break;
                    }

                    //  bắt sự kiện click trên ExpandableList
                    expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                            //Log.d("DEBUG", "submenu item clicked");
//                i1 = l , i !=
                            switch (i){
//                    Quản lí nghỉ phép
                                case 0:
                                    handleLeaveManaParent(i1,getNhanVien);
                                    break;
//                      Quản lí nhân viên
                                case 1:
                                    handleStaffManaParent(i1,getNhanVien);
                                    break;

                            }
                            return true;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}