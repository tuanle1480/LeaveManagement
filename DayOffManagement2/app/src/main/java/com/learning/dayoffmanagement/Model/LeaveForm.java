package com.learning.dayoffmanagement.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class LeaveForm extends Form implements Serializable {
    private String id,startDate,endDate;
    private String reason,state;
    private String uid,sender;
    private String sendDate;
    private String startTime,endTime,feedback,departmentID;
    private double totalLeaveTime;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public LeaveForm(String id,String uid, String sender, String startDate, String endDate, String reason) {
        super(id,uid,sender);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.state = "Waiting";
        this.uid = uid;
        this.sender = sender;
        this.sendDate = LocalDate.now().toString();
    }


    public LeaveForm(String id,String uid,String sender,String startDate, String endDate, String reason, String state,  String sendDate) {
        super(id,uid,sender);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.state = state;
        this.uid = uid;
        this.sender = sender;
        this.sendDate = sendDate;
    }

    public LeaveForm(String id, String startDate, String endDate, String reason, String state, String uid, String sender, String sendDate, String startTime, String endTime, double totalLeaveTime) {
        super(id,uid,sender,startTime,endTime,state);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.state = state;
        this.uid = uid;
        this.sender = sender;
        this.sendDate = sendDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalLeaveTime = totalLeaveTime;
    }

    public LeaveForm(String id, String startDate, String endDate, String reason, String state, String uid, String sender, String sendDate, String startTime, String endTime,String feedback, double totalLeaveTime) {
        super(id,uid,sender,startTime,endTime,state);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.state = state;
        this.uid = uid;
        this.sender = sender;
        this.sendDate = sendDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalLeaveTime = totalLeaveTime;
        this.feedback = feedback;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LeaveForm(String id,String uid, String sender, String startDate, String endDate, String reason, String startTime, String endTime, double totalLeaveTime,String departmentID) {
        super(id,uid,sender,startTime,endTime,departmentID);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.uid = uid;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalLeaveTime = totalLeaveTime;
        this.state = "Waiting";
        this.sendDate = LocalDate.now().toString();
        this.departmentID = departmentID;
    }

    public LeaveForm() {
        super();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalLeaveTime() {
        return totalLeaveTime;
    }

    public void setTotalLeaveTime(double totalLeaveTime) {
        this.totalLeaveTime = totalLeaveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    @Override
    public String toString() {
        return "LeaveForm{" +
                "id='" + id + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", reason='" + reason + '\'' +
                ", state='" + state + '\'' +
                ", uid='" + uid + '\'' +
                ", sender='" + sender + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", feedback='" + feedback + '\'' +
                ", totalLeaveTime=" + totalLeaveTime +
                '}';
    }

    @Override
    public String getFormType() {
        return "Leave Form";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveForm)) return false;
        LeaveForm leaveForm = (LeaveForm) o;
        return Double.compare(leaveForm.getTotalLeaveTime(), getTotalLeaveTime()) == 0 && Objects.equals(getId(), leaveForm.getId()) && Objects.equals(getStartDate(), leaveForm.getStartDate()) && Objects.equals(getEndDate(), leaveForm.getEndDate()) && Objects.equals(getReason(), leaveForm.getReason()) && Objects.equals(getState(), leaveForm.getState()) && Objects.equals(getUid(), leaveForm.getUid()) && Objects.equals(getSender(), leaveForm.getSender()) && Objects.equals(getSendDate(), leaveForm.getSendDate()) && Objects.equals(getStartTime(), leaveForm.getStartTime()) && Objects.equals(getEndTime(), leaveForm.getEndTime()) && Objects.equals(getFeedback(), leaveForm.getFeedback());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartDate(), getEndDate(), getReason(), getState(), getUid(), getSender(), getSendDate(), getStartTime(), getEndTime(), getFeedback(), getTotalLeaveTime());
    }
}
