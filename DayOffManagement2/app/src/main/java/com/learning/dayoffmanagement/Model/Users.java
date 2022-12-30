package com.learning.dayoffmanagement.Model;

import java.io.Serializable;
import java.util.Objects;

public class Users implements Serializable {

    private String userName;
    private String Password;
    private int RoleID;
    private  String Uid;
    private String staffId;

    public Users(String userName, String password, int roleID, String uid) {
        this.userName = userName;
        Password = password;
        RoleID = roleID;
        Uid = uid;
    }


    public Users(String userName, String password, int roleID, String uid, String staffId) {
        this.userName = userName;
        this.Password = password;
        this.RoleID = roleID;
        this.Uid = uid;
        this.staffId = staffId;
    }

    public Users() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return RoleID == users.RoleID && Objects.equals(userName, users.userName) && Objects.equals(Uid, users.Uid) && Objects.equals(staffId, users.staffId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, RoleID, Uid, staffId);
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", Password='" + Password + '\'' +
                ", RoleID=" + RoleID +
                ", Uid='" + Uid + '\'' +
                ", staffId='" + staffId + '\'' +
                '}';
    }
}
