package com.entigrity.webservice;

import com.entigrity.model.Instructorlist_details.Instructor_Details_Model;
import com.entigrity.model.changepassword.ChangePasswordModel;
import com.entigrity.model.city.CityModel;
import com.entigrity.model.company.CompanyModel;
import com.entigrity.model.company_like.Company_Like_Model;
import com.entigrity.model.companyfavorites.Company_Favorite;
import com.entigrity.model.contactus.ContactUsModel;
import com.entigrity.model.country.CountryModel;
import com.entigrity.model.editProfile.EditProfileModel;
import com.entigrity.model.forgotpassword.Forgotpaawordmodel;
import com.entigrity.model.instructor.InstructorModel;
import com.entigrity.model.instructor_follow.Instructor_Follow_Model;
import com.entigrity.model.instructor_like.Instructor_Like_Model;
import com.entigrity.model.instructorfavorites.Instructor_Favorite;
import com.entigrity.model.login.LoginModel;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.state.StateModel;
import com.entigrity.model.subject.SubjectModel;
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.model.webinar_like.Webinar_Like_Model;
import com.entigrity.model.webinarfavorites.Webinar_Favorite;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface APIService {

    //retrofit
    /*@POST("/posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId);*/

    //rxjava


    //MileStone-1


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


    //MileStone-2


    //Instructor List
    @GET("speaker")
    Observable<InstructorModel> GetInstructor(@Header("Authorization") String authorization);


    //Instructor Details

    @GET("speaker/{speaker_ids}")
    Observable<Instructor_Details_Model> GetInstructorDetails(
            @Path("speaker_ids") String speaker_ids,
            @Header("Authorization") String authorization);


    //company List
    @GET("company")
    Observable<CompanyModel> GetCompany(@Header("Authorization") String authorization);


    //instructor follow status

    @POST("speaker/follow/{speaker_ids}")
    @FormUrlEncoded
    Observable<Instructor_Follow_Model> InstructorFollowStatus(
            @Path("speaker_ids") String speaker_ids,
            @Header("Authorization") String authorization,
            @Field("speaker_id") int speaker_id
    );


    //instructor like status

    @POST("speaker/like/{speaker_ids}")
    @FormUrlEncoded
    Observable<Instructor_Like_Model> InstructorFavoriteStatus(
            @Path("speaker_ids") String speaker_ids,
            @Header("Authorization") String authorization,
            @Field("speaker_id") int speaker_id
    );


    //company like status

    @POST("company/like/{company_ids}")
    @FormUrlEncoded
    Observable<Company_Like_Model> CompanyFavoriteStatus(
            @Path("company_ids") String company_ids,
            @Header("Authorization") String authorization,
            @Field("company_id") int company_id
    );


    //Webinar like status

    @POST("webinar/like/")
    @FormUrlEncoded
    Observable<Webinar_Like_Model> WebinarFavoriteStatus(
            @Header("Authorization") String authorization,
            @Field("webinar_id") int webinar_id
    );


    //company favorites list

    @POST("company/my-favorite")
    Observable<Company_Favorite> CompanyFavoriteList(
            @Header("Authorization") String authorization

    );


    //instructor favorites list

    @POST("speaker/my-favorite")
    Observable<Instructor_Favorite> InstructorFavoriteList(
            @Header("Authorization") String authorization

    );


    //webinars favorites list

    @POST("webinar/my-favorite")
    Observable<Webinar_Favorite> WebinarFavoriteList(
            @Header("Authorization") String authorization

    );


}
