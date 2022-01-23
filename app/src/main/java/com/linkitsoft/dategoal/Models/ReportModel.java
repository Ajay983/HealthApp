package com.linkitsoft.dategoal.Models;

public class ReportModel {
    public String userID;
    public String ReportedBy;
    public String Title;
    public String Reason;
    public String ProofImage;

    public ReportModel() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReportedBy() {
        return ReportedBy;
    }

    public void setReportedBy(String reportedBy) {
        ReportedBy = reportedBy;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getProofImage() {
        return ProofImage;
    }

    public void setProofImage(String proofImage) {
        ProofImage = proofImage;
    }
}
