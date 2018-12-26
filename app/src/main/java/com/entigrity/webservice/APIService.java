package com.entigrity.webservice;

import com.entigrity.model.changepassword.ChangePasswordModel;
import com.entigrity.model.city.CityModel;
import com.entigrity.model.contactus.ContactUsModel;
import com.entigrity.model.country.CountryModel;
import com.entigrity.model.editProfile.EditProfileModel;
import com.entigrity.model.forgotpassword.Forgotpaawordmodel;
import com.entigrity.model.login.LoginModel;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.state.StateModel;
import com.entigrity.model.subject.SubjectModel;
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.model.viewprofile.ViewProfileModel;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface APIService {

    //retrofit
    /*@POST("/posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId);*/

    //rxjava


    //login
    @POST("login")
    @FormUrlEncoded
    Observable<LoginModel> login(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("device_id") String device_id,
                                 @Field("device_token") String device_token,
                                 @Field("device_type") String device_type);


    //user type
    @GET("user-type")
    Observable<UserTypeModel> Getusertype();


    //tags
    @GET("tags")
    Observable<TopicsofInterest> GetTopicsofInterest();


    //registration
    @POST("registration")
    @FormUrlEncoded
    Observable<RegistrationModel> Register(@Field("first_name") String first_name,
                                           @Field("last_name") String last_name,
                                           @Field("email") String email,
                                           @Field("password") String password,
                                           @Field("confirm_password") String confirm_password,
                                           @Field("firm_name") String firm_name,
                                           @Field("contact_no") String contact_no,
                                           @Field("tags[]") ArrayList<Integer> tags,
                                           @Field("user_type_id") int user_type_id
    );


    //logout
    @POST("logout")
    @FormUrlEncoded
    Observable<LogoutModel> logout(
            @Header("Authorization") String authorization,
            @Field("device_id") String device_id,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type);


    //change password
    @POST("change-password")
    @FormUrlEncoded
    Observable<ChangePasswordModel> changepassword(
            @Header("Authorization") String authorization,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password,
            @Field("confirm_password") String confirm_password);


    //forgot password


    @POST("forgot-password")
    @FormUrlEncoded
    Observable<Forgotpaawordmodel> forgotpassword(
            @Field("email") String email
    );


    //contact us
    @POST("contact-us")
    @FormUrlEncoded
    Observable<ContactUsModel> contactus(
            @Header("subject") String subject,
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact_number") String contact_number,
            @Field("message") String message);

    //get subject
    @GET("subject")
    Observable<SubjectModel> GetSubject();


    //view-profile
    @GET("view-profile")
    Observable<ViewProfileModel> GetProfile(@Header("Authorization") String authorization);


    //get country
    @GET("country")
    Observable<CountryModel> GetCountry();


    //get state
    @POST("state")
    @FormUrlEncoded
    Observable<StateModel> GetState(@Field("country_id") int country_id);


    //get city
    @POST("city")
    @FormUrlEncoded
    Observable<CityModel> GetCity(@Field("state_id") int state_id);


    //edit profile
    @POST("edit-profile")
    @FormUrlEncoded
    Observable<EditProfileModel> Ediprofile(
            @Header("Authorization") String authorization,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("firm_name") String firm_name,
            @Field("country_id") int country_id,
            @Field("state_id") int state_id,
            @Field("city_id") int city_id,
            @Field("zipcode") int zipcode,
            @Field("contact_no") String contact_no,
            @Field("tags[]") ArrayList<Integer> tags,
            @Field("user_type_id") int user_type
    );


}
