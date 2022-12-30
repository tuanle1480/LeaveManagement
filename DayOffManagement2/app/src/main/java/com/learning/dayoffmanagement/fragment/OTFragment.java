package com.learning.dayoffmanagement.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.dayoffmanagement.Listener.OTItemClick;
import com.learning.dayoffmanagement.Model.OTForm;
import com.learning.dayoffmanagement.R;
import com.learning.dayoffmanagement.adapter.OTAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OTFragment extends Fragment implements OTItemClick {
    private RecyclerView recyOTManage;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private OTAdapter adapter;
    private List<OTForm> ots;

    private String id,name,departmentId;
    private int roleId;

    public OTFragment(String id,String name,int roleId,String departmentId) {
        // Required empty public constructor
        this.id = id;
        this.name = name;
        this.roleId = roleId;
        this.departmentId = departmentId;
    }

   public OTFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_t, container, false);
//        init
        init(view);
        return view;
    }

    private void init(View view){
        recyOTManage = view.findViewById(R.id.recyOTManage);
    }

//     Lấy dữ liệu các đơn đk tăng ca đang chờ phê duyệt
    private void getData(){
        switch (roleId){
            case 1:
                getDataForAdmin();
                break;

            case 2:
                getDataForLeader();
                break;
        }
    }

    private void getDataForAdmin(){
        reference.child("WaitingAcceptOT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ots = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap->{
                        OTForm value = cSnap.getValue(OTForm.class);
                        ots.add(value);
                    });

                    bindData(ots);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataForLeader(){
        reference.child("WaitingAcceptOT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ots = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(cSnap->{
                        OTForm value = cSnap.getValue(OTForm.class);
                        if(value.getDepartmentID().equals(departmentId))
                            ots.add(value);
                    });

                    bindData(ots);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bindData(List<OTForm> data){
        adapter = new OTAdapter(data,OTFragment.this);
        recyOTManage.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

//    từ chố tăng ca
    @Override
    public void denyOT(OTForm pos) {
        reference.child("WaitingAcceptOT").child(pos.getId()).removeValue();
        int removeIndex = ots.indexOf(pos);
        ots.remove(pos);
        adapter.notifyItemRemoved(removeIndex);
        updateSentOT(pos,"Denied");
        Toast.makeText(getContext(), "DENIED OT", Toast.LENGTH_SHORT).show();
    }

//    chấp thuận tăng ca
    @Override
    public void acceptOT(OTForm pos) {
        int removeIndex = ots.indexOf(pos);
        ots.remove(pos);
        adapter.notifyItemRemoved(removeIndex);
        reference.child("OT").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                nếu nhân viên đã có tăng ca trước đó -> cập nhật lại tổng số giờ tăng ca của nhân viên đó
                    if(snapshot.hasChild(pos.getId())){
                        updateOTTime(pos.getId(), pos.getOvertime());
//                        ngược lại nếu nhân viên chưa tăng ca trước đó -> thêm mới số giờ tăng ca của nhân viên đó
                    }else{
                        reference.child("OT").child(pos.getId()).setValue(pos);
                    }
//                    xóa dữ liệu khỏi các đơn tăng ca đang chờ phê duyệt
                reference.child("WaitingAcceptOT").child(pos.getId()).removeValue();
                    updateSentOT(pos,"Accepted");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(getContext(), "ACCEPTED OT", Toast.LENGTH_SHORT).show();
    }

//    cập nhật lại dữ liệu của đơn tăng ca
    public void updateSentOT(OTForm ot, String newState){
        HashMap data = new HashMap();
        data.put("admin",name); // đc chấp thuận bởi admin nào
        data.put("state",newState);// trạng thái của đơn
        reference.child("SentOTForm").child(ot.getUid()).child(ot.getId()).updateChildren(data);
    }
//cập nhật lại tổng số giờ tăng ca
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


}