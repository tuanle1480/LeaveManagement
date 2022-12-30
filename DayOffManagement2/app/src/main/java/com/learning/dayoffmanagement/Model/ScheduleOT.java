package com.learning.dayoffmanagement.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class ScheduleOT {
    private String id,date,startTime,endTime,admin;
    private double totalOTTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ScheduleOT(String id,String startTime, String endTime, double totalOTTime) {
        this.id = id;
        this.date = LocalDate.now().toString();
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalOTTime = totalOTTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ScheduleOT(String id,String startTime, String endTime, double totalOTTime,String admin) {
        this.id = id;
        this.date = LocalDate.now().toString();
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalOTTime = totalOTTime;
        this.admin = admin;
    }

    public ScheduleOT(String id, String startTime, String endTime,String date, String admin, double totalOTTime) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.admin = admin;
        this.totalOTTime = totalOTTime;
    }

    public ScheduleOT() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public double getTotalOTTime() {
        return totalOTTime;
    }

    public void setTotalOTTime(double totalOTTime) {
        this.totalOTTime = totalOTTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
