package com.learning.dayoffmanagement.Model;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {
    private String id,name,leaderId;

    public Department() {
    }

    public Department( String name) {
        this.id = RandomStringUtils.randomAlphabetic(30);
        this.name = name;
    }

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(String id, String name, String leaderId) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
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

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                '}';
    }
}
