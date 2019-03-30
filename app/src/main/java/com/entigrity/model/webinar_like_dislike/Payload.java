package com.entigrity.model.webinar_like_dislike;

import com.google.gson.annotations.SerializedName;


public class Payload{

	@SerializedName("is_like")
	private String isLike;

	public void setIsLike(String isLike){
		this.isLike = isLike;
	}

	public String getIsLike(){
		return isLike;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"is_like = '" + isLike + '\'' + 
			"}";
		}
}