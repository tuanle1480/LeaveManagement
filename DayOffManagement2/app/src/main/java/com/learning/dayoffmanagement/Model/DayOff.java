package com.learning.dayoffmanagement.Model;

import java.util.Objects;

public class DayOff {
    private String id,name;
    private double totalLeaveTime;

    public DayOff(String id,String name, double totalLeaveTime) {
        this.id = id;
        this.name = name;
        this.totalLeaveTime = totalLeaveTime;
    }

    public DayOff() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayOff dayOff = (DayOff) o;
        return totalLeaveTime == dayOff.totalLeaveTime && Objects.equals(id, dayOff.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
