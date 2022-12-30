package com.learning.dayoffmanagement.Model;

import java.util.Objects;

public abstract class Form {
    protected String id,uid,sender,startTime,endTime,state,departmentId;

    public Form(String id, String uid, String sender, String startTime, String endTime, String state, String departmentId) {
        this.id = id;
        this.uid = uid;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.departmentId = departmentId;
    }

    public Form(String id,String uid,String sender){
        this.id = id;
        this.uid = uid;
        this.sender = sender;
    }

    public Form(String id, String uid, String sender, String startTime, String endTime,String departmentId) {
        this.id = id;
        this.uid = uid;
        this.sender = sender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = "Waiting";
        this.departmentId = departmentId;
    }


    public Form(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public abstract String getFormType();



    @Override
    public String toString() {
        return "id "+id + ","+"uid: "+uid+","+"sender: "+sender+","+"start time:"+startTime+","+"end time:"+endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Form)) return false;
        Form form = (Form) o;
        return Objects.equals(getId(), form.getId()) && Objects.equals(getUid(), form.getUid()) && Objects.equals(getSender(), form.getSender()) && Objects.equals(getStartTime(), form.getStartTime()) && Objects.equals(getEndTime(), form.getEndTime()) && Objects.equals(getState(), form.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUid(), getSender(), getStartTime(), getEndTime(), getState());
    }
}

