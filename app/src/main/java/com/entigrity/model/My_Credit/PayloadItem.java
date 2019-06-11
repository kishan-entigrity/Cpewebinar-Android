package com.entigrity.model.My_Credit;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PayloadItem {

    @SerializedName("pending_count")
    private int pendingCount;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("upcoming_count")
    private int upcomingCount;

    @SerializedName("my_credits")
    private List<MyCreditsItem> myCredits;

    @SerializedName("completed_count")
    private int completedCount;

    @SerializedName("email")
    private String email;

    public boolean isIslast() {
        return islast;
    }

    public void setIslast(boolean islast) {
        this.islast = islast;
    }

    @SerializedName("is_last")
    private boolean islast;


    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setUpcomingCount(int upcomingCount) {
        this.upcomingCount = upcomingCount;
    }

    public int getUpcomingCount() {
        return upcomingCount;
    }

    public void setMyCredits(List<MyCreditsItem> myCredits) {
        this.myCredits = myCredits;
    }

    public List<MyCreditsItem> getMyCredits() {
        return myCredits;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return
                "PayloadItem{" +
                        "pending_count = '" + pendingCount + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",upcoming_count = '" + upcomingCount + '\'' +
                        ",my_credits = '" + myCredits + '\'' +
                        ",completed_count = '" + completedCount + '\'' +
                        ",email = '" + email + '\'' +
                        ",is_last = '" + islast + '\'' +
                        "}";
    }
}