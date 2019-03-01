package com.entigrity.model.homewebinarlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Payload{

	@SerializedName("webinar")
	private List<WebinarItem> webinar;

	public void setWebinar(List<WebinarItem> webinar){
		this.webinar = webinar;
	}

	public List<WebinarItem> getWebinar(){
		return webinar;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"webinar = '" + webinar + '\'' + 
			"}";
		}
}