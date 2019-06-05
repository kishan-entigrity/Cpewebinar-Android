package com.entigrity.model.webinar_details_new;

import com.google.gson.annotations.SerializedName;


public class TimezonesItem {

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("timezone")
    private String timezone;

    public String getTimezoneshort() {
        return timezoneshort;
    }

    public void setTimezoneshort(String timezoneshort) {
        this.timezoneshort = timezoneshort;
    }

    @SerializedName("start_date")
    private String startDate;

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    @SerializedName("timezone_short")
    private String timezoneshort;

    @SerializedName("schedule_id")
    private int scheduleid;


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return
                "TimezonesItem{" +
                        "start_time = '" + startTime + '\'' +
                        ",timezone = '" + timezone + '\'' +
                        ",start_date = '" + startDate + '\'' +
                        ",timezone_short = '" + timezoneshort + '\'' +
                        ",schedule_id = '" + scheduleid + '\'' +
                        "}";
    }
}