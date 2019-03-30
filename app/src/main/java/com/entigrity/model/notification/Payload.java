package com.entigrity.model.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Payload{

	@SerializedName("notification_list")
	private List<NotificationListItem> notificationList;

	public void setNotificationList(List<NotificationListItem> notificationList){
		this.notificationList = notificationList;
	}

	public List<NotificationListItem> getNotificationList(){
		return notificationList;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"notification_list = '" + notificationList + '\'' + 
			"}";
		}
}