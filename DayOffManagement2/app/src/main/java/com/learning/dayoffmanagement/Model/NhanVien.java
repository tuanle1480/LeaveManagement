package com.learning.dayoffmanagement.Model;

import java.io.Serializable;
import java.util.List;

public class NhanVien implements Serializable {
    private String id,name,phoneNumber,email,address,office;
    private int salary;
    private Users account;
    private List<LeaveForm> leaveForm;
    private String dayOfBirth;
    private double totalLeaveTime;
    private double otTime;
    private Department department;
    private String avtUrl;


    public NhanVien(String id,String name, String phoneNumber, String email, String address,Department department,String office, int salary, Users account, String dayOfBirth,String avtUrl) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.account = account;
        this.dayOfBirth = dayOfBirth;
        this.department = department;
        this.office = office;
        this.avtUrl = avtUrl;
    }

    public NhanVien(String id, String name, String phoneNumber, String email, String address, int salary, String dayOfBirth) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.dayOfBirth = dayOfBirth;
    }

    public NhanVien(String id,String name, String phoneNumber, String email, String address, Users account, String dayOfBirth) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.account = account;
        this.dayOfBirth = dayOfBirth;
    }

    public NhanVien(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Users getAccount() {
        return account;
    }

    public void setAccount(Users account) {
        this.account = account;
    }

    public List<LeaveForm> getLeaveForm() {
        return leaveForm;
    }

    public void setLeaveForm(List<LeaveForm> leaveForm) {
        this.leaveForm = leaveForm;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalLeaveTime() {
        return totalLeaveTime;
    }

    public void setTotalLeaveTime(double totalLeaveTime) {
        this.totalLeaveTime = totalLeaveTime;
    }

    public double getOtTime() {
        return otTime;
    }

    public void setOtTime(double otTime) {
        this.otTime = otTime;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getAvtUrl() {
        return avtUrl;
    }

    public void setAvtUrl(String avtUrl) {
        this.avtUrl = avtUrl;
    }

    public double calSalary(){
        if(this.totalLeaveTime > 16) {
            return ((30 - 8) * 8 - totalLeaveTime) * this.salary + (this.salary * 3 * otTime);
        }
        return (30-8) * 8 * this.salary + (this.salary * 3 * otTime);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                ", account=" + account +
                ", leaveForm=" + leaveForm +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", leaveDayNumber=" + totalLeaveTime +
                '}';
    }
}
