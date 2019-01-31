package com.entigrity.model.registration;

import com.google.gson.annotations.SerializedName;


public class User{

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("contact_no")
	private String contactNo;

	@SerializedName("firm_name")
	private String firmName;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("city_id")
	private String cityId;

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setStateId(String stateId){
		this.stateId = stateId;
	}

	public String getStateId(){
		return stateId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
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
			"User{" + 
			"last_name = '" + lastName + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",zipcode = '" + zipcode + '\'' + 
			",user_type = '" + userType + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",contact_no = '" + contactNo + '\'' + 
			",firm_name = '" + firmName + '\'' + 
			",state_id = '" + stateId + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",country_id = '" + countryId + '\'' + 
			",city_id = '" + cityId + '\'' + 
			"}";
		}
}