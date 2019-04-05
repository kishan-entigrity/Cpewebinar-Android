package com.entigrity.model.favorites_count;

import com.google.gson.annotations.SerializedName;


public class Payload{

	@SerializedName("speaker_count")
	private int speakerCount;

	@SerializedName("company_count")
	private int companyCount;

	@SerializedName("webinar_count")
	private int webinarCount;

	@SerializedName("banner_image")
	private String bannerImage;

	@SerializedName("topics_of_interest_count")
	private int topicsOfInterestCount;

	public void setSpeakerCount(int speakerCount){
		this.speakerCount = speakerCount;
	}

	public int getSpeakerCount(){
		return speakerCount;
	}

	public void setCompanyCount(int companyCount){
		this.companyCount = companyCount;
	}

	public int getCompanyCount(){
		return companyCount;
	}

	public void setWebinarCount(int webinarCount){
		this.webinarCount = webinarCount;
	}

	public int getWebinarCount(){
		return webinarCount;
	}

	public void setBannerImage(String bannerImage){
		this.bannerImage = bannerImage;
	}

	public String getBannerImage(){
		return bannerImage;
	}

	public void setTopicsOfInterestCount(int topicsOfInterestCount){
		this.topicsOfInterestCount = topicsOfInterestCount;
	}

	public int getTopicsOfInterestCount(){
		return topicsOfInterestCount;
	}

	@Override
 	public String toString(){
		return 
			"Payload{" + 
			"speaker_count = '" + speakerCount + '\'' + 
			",company_count = '" + companyCount + '\'' + 
			",webinar_count = '" + webinarCount + '\'' + 
			",banner_image = '" + bannerImage + '\'' + 
			",topics_of_interest_count = '" + topicsOfInterestCount + '\'' + 
			"}";
		}
}