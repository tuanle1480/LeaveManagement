package com.learning.dayoffmanagement.Model;

public class ToDayWork {
    private String workName,workTime,workLocation;

    public ToDayWork(String workName, String workTime, String workLocation) {
        this.workName = workName;
        this.workTime = workTime;
        this.workLocation = workLocation;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }
}
