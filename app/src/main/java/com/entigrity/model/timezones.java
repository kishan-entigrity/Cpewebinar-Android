package com.entigrity.model;

public class timezones {
    private String timezone = "";

    public String getTimezone_short() {
        return timezone_short;
    }

    public void setTimezone_short(String timezone_short) {
        this.timezone_short = timezone_short;
    }

    private String timezone_short = "";

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    private String start_date = "";
    private String start_time = "";

    private int schedule_id = 0;
}
