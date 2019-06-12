package com.entigrity.model.video_duration;

import com.google.gson.annotations.SerializedName;


public class Payload{

	@SerializedName("watched")
	private String watched;

	public void setWatched(String watched){
		this.watched = watched;
	}

	public String getWatched(){
		return watched;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"watched = '" + watched + '\'' + 
			"}";
		}
}