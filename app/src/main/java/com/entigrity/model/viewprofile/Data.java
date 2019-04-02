package com.entigrity.model.viewprofile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("country")
	private String country;

	@SerializedName("city")
	private String city;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("profile_picture")
	private String profilePicture;

	@SerializedName("ptin_number")
	private String ptinNumber;

	@SerializedName("tags")
	private List<TagsItem> tags;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("contact_no")
	private String contactNo;

	@SerializedName("firm_name")
	private String firmName;

	@SerializedName("user_type_id")
	private String userTypeId;

	@SerializedName("id")
	private int id;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("designation")
	private String designation;

	@SerializedName("state")
	private String state;

	@SerializedName("credit")
	private String credit;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("city_id")
	private String cityId;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setProfilePicture(String profilePicture){
		this.profilePicture = profilePicture;
	}

	public String getProfilePicture(){
		return profilePicture;
	}

	public void setPtinNumber(String ptinNumber){
		this.ptinNumber = ptinNumber;
	}

	public String getPtinNumber(){
		return ptinNumber;
	}

	public void setTags(List<TagsItem> tags){
		this.tags = tags;
	}

	public List<TagsItem> getTags(){
		return tags;
	}

	public void setZipcode(String zipcode){
		this.zipcode = zipcode;
	}

	public String getZipcode(){
		return zipcode;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
	}

	public void setContactNo(String contactNo){
		this.contactNo = contactNo;
	}

	public String getContactNo(){
		return contactNo;
	}

	public void setFirmName(String firmName){
		this.firmName = firmName;
	}

	public String getFirmName(){
		return firmName;
	}

	public void setUserTypeId(String userTypeId){
		this.userTypeId = userTypeId;
	}

	public String getUserTypeId(){
		return userTypeId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setStateId(String stateId){
		this.stateId = stateId;
	}

	public String getStateId(){
		return stateId;
	}

	public void setDesignation(String designation){
		this.designation = designation;
	}

	public String getDesignation(){
		return designation;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setCredit(String credit){
		this.credit = credit;
	}

	public String getCredit(){
		return credit;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"country = '" + country + '\'' + 
			",city = '" + city + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",profile_picture = '" + profilePicture + '\'' + 
			",ptin_number = '" + ptinNumber + '\'' + 
			",tags = '" + tags + '\'' + 
			",zipcode = '" + zipcode + '\'' + 
			",user_type = '" + userType + '\'' + 
			",contact_no = '" + contactNo + '\'' + 
			",firm_name = '" + firmName + '\'' + 
			",user_type_id = '" + userTypeId + '\'' + 
			",id = '" + id + '\'' + 
			",state_id = '" + stateId + '\'' + 
			",designation = '" + designation + '\'' + 
			",state = '" + state + '\'' + 
			",credit = '" + credit + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",country_id = '" + countryId + '\'' + 
			",city_id = '" + cityId + '\'' + 
			"}";
		}
}