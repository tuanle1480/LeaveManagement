package com.learning.dayoffmanagement.Model;

import java.io.Serializable;

public class ApprovedLeaveForm implements Serializable {
    private LeaveForm leaveForm;
    private String feedback,id;

    public ApprovedLeaveForm(String id,LeaveForm leaveForm, String feedback) {
        this.leaveForm = leaveForm;
        this.feedback = feedback;
        this.id = id;
    }

    public ApprovedLeaveForm() {
    }

    public LeaveForm getLeaveForm() {
        return leaveForm;
    }

    public void setLeaveForm(LeaveForm leaveForm) {
        this.leaveForm = leaveForm;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
