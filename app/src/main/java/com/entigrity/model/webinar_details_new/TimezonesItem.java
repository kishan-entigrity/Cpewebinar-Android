package com.entigrity.model.webinar_details_new;

import com.google.gson.annotations.SerializedName;


public class TimezonesItem{

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("start_date")
	private String startDate;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"TimezonesItem{" + 
			"start_time = '" + startTime + '\'' + 
			",timezone = '" + timezone + '\'' + 
			",start_date = '" + startDate + '\'' + 
			"}";
		}
}