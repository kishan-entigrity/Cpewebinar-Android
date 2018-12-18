package com.entigrity.model.login;

import com.google.gson.annotations.SerializedName;


public class Payload{

	@SerializedName("token")
	private String token;

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"token = '" + token + '\'' + 
			"}";
		}
}