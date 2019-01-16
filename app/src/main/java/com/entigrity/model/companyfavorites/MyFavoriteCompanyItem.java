package com.entigrity.model.companyfavorites;

import com.google.gson.annotations.SerializedName;


public class MyFavoriteCompanyItem{

	@SerializedName("about_company")
	private String aboutCompany;

	@SerializedName("no_of_speaker")
	private int noOfSpeaker;

	@SerializedName("no_of_webinar")
	private String noOfWebinar;

	@SerializedName("company_logo")
	private String companyLogo;

	@SerializedName("company_id")
	private int companyId;

	@SerializedName("city")
	private String city;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("company_website")
	private String companyWebsite;

	@SerializedName("state")
	private String state;

	@SerializedName("company_mobile")
	private String companyMobile;

	@SerializedName("favorite_unfavorite_status")
	private String favoriteUnfavoriteStatus;

	public void setAboutCompany(String aboutCompany){
		this.aboutCompany = aboutCompany;
	}

	public String getAboutCompany(){
		return aboutCompany;
	}

	public void setNoOfSpeaker(int noOfSpeaker){
		this.noOfSpeaker = noOfSpeaker;
	}

	public int getNoOfSpeaker(){
		return noOfSpeaker;
	}

	public void setNoOfWebinar(String noOfWebinar){
		this.noOfWebinar = noOfWebinar;
	}

	public String getNoOfWebinar(){
		return noOfWebinar;
	}

	public void setCompanyLogo(String companyLogo){
		this.companyLogo = companyLogo;
	}

	public String getCompanyLogo(){
		return companyLogo;
	}

	public void setCompanyId(int companyId){
		this.companyId = companyId;
	}

	public int getCompanyId(){
		return companyId;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyWebsite(String companyWebsite){
		this.companyWebsite = companyWebsite;
	}

	public String getCompanyWebsite(){
		return companyWebsite;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setCompanyMobile(String companyMobile){
		this.companyMobile = companyMobile;
	}

	public String getCompanyMobile(){
		return companyMobile;
	}

	public void setFavoriteUnfavoriteStatus(String favoriteUnfavoriteStatus){
		this.favoriteUnfavoriteStatus = favoriteUnfavoriteStatus;
	}

	public String getFavoriteUnfavoriteStatus(){
		return favoriteUnfavoriteStatus;
	}

	@Override
 	public String toString(){
		return 
			"MyFavoriteCompanyItem{" + 
			"about_company = '" + aboutCompany + '\'' + 
			",no_of_speaker = '" + noOfSpeaker + '\'' + 
			",no_of_webinar = '" + noOfWebinar + '\'' + 
			",company_logo = '" + companyLogo + '\'' + 
			",company_id = '" + companyId + '\'' + 
			",city = '" + city + '\'' + 
			",company_name = '" + companyName + '\'' + 
			",company_website = '" + companyWebsite + '\'' + 
			",state = '" + state + '\'' + 
			",company_mobile = '" + companyMobile + '\'' + 
			",favorite_unfavorite_status = '" + favoriteUnfavoriteStatus + '\'' + 
			"}";
		}
}