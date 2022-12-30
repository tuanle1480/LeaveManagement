package com.learning.dayoffmanagement.Model;

import java.util.Objects;

public class OTForm extends Form{
    private String uid,id,sender,startTime,endTime,state,admin,departmentID;
    private double overtime,totalOTTime;

    public OTForm(String id, String uid, String sender, String startTime, String endTime, double totalOTTime,String departmentID) {
        super(id,uid,sender,startTime,endTime,departmentID);
        this.id = id;
        this.uid = uid;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = "Waiting";
        this.totalOTTime = totalOTTime;
        this.departmentID = departmentID;
    }

    public OTForm(String id, String uid, String sender, String startTime, String endTime, String state, String admin, double overtime) {
        super(id,uid,sender,startTime,endTime,state);
        this.id = id;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.overtime = overtime;
        this.admin = admin;
    }

    public OTForm(String id, String uid, String sender, String startTime, String endTime, String admin, double overtime,String departmentID) {
        super(id,uid,sender,startTime,endTime,departmentID);
        this.id = id;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = "Waiting";
        this.overtime = overtime;
        this.admin = admin;
        this.departmentID = departmentID;
    }



    public OTForm(String id, String uid, String sender, String startTime, String endTime, String state) {
        super(id,uid,sender,startTime,endTime,state);
        this.uid = uid;
        this.id = id;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public OTForm() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getOvertime() {
        return overtime;
    }

    public void setOvertime(double overtime) {
        this.overtime = overtime;
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

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public double getTotalOTTime() {
        return totalOTTime;
    }

    public void setTotalOTTime(double totalOTTime) {
        this.totalOTTime = totalOTTime;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String getFormType() {
        return "OT Form";
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OTForm OTForm = (OTForm) o;
        return Objects.equals(id, OTForm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OTForm{" +
                "uid='" + uid + '\'' +
                ", id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", state='" + state + '\'' +
                ", admin='" + admin + '\'' +
                ", overtime=" + overtime +
                ", totalOTTime=" + totalOTTime +
                '}';
    }
}
