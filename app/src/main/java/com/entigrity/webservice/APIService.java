package com.entigrity.webservice;

import com.entigrity.model.Instructorlist_details.Instructor_Details_Model;
import com.entigrity.model.changepassword.ChangePasswordModel;
import com.entigrity.model.city.CityModel;
import com.entigrity.model.company.CompanyModel;
import com.entigrity.model.company_details.Company_details_model;
import com.entigrity.model.company_like.Company_Like_Model;
import com.entigrity.model.companyfavorites.Company_Favorite;
import com.entigrity.model.contactus.ContactUsModel;
import com.entigrity.model.country.CountryModel;
import com.entigrity.model.editProfile.EditProfileModel;
import com.entigrity.model.favorites_count.Favorite_Count_Model;
import com.entigrity.model.forgotpassword.Forgotpaawordmodel;

import com.entigrity.model.getcontactusinfo.GetContactUsInfo;
import com.entigrity.model.getfaq.GetFaq;
import com.entigrity.model.getprivacypolicy.GetPrivacyPolicy;
import com.entigrity.model.gettermscondition.GetTermsCondition;
import com.entigrity.model.homewebinarlist.Webinar_Home;
import com.entigrity.model.instructor.InstructorModel;
import com.entigrity.model.instructor_follow.Instructor_Follow_Model;
import com.entigrity.model.instructor_like.Instructor_Like_Model;
import com.entigrity.model.instructorfavorites.Instructor_Favorite;
import com.entigrity.model.login.LoginModel;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.model.notification.NotificationModel;
import com.entigrity.model.postcontactus.PostContactQuery;
import com.entigrity.model.postfeedback.PostFeedback;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.savetopicsofinterest.SaveTopicsInterest;
import com.entigrity.model.state.StateModel;
import com.entigrity.model.subject.SubjectModel;
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.topicsofinterestn.Topicsofinterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.model.view_topics_of_interest.View_Topics_Interest_Model;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.model.webinar_details.Webinar_Detail_Model;
import com.entigrity.model.webinar_like.Webinar_Like_Model;
import com.entigrity.model.webinar_like_dislike.Webinar_Like_Dislike_Model;
import com.entigrity.model.webinarfavorites.Webinar_Favorite;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
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


    //company details

    @GET("company/{company_ids}")
    Observable<Company_details_model> GetCompanyDetails(
            @Path("company_ids") String company_ids,
            @Header("Authorization") String authorization);


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

    @POST("webinar/like/{webinar_ids}")
    @FormUrlEncoded
    Observable<Webinar_Like_Model> WebinarFavoriteStatus(
            @Path("webinar_ids") String webinar_ids,
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


    //Milestone-3

    @GET("webinar")
    Observable<Webinar_Home> GetHomeWebinarList(@Header("Authorization") String authorization,
                                                @Query("WebinarFor") String webinarfor,
                                                @Query("page") int page,
                                                @Query("WebinarType") String WebinarType,
                                                @Query("PerticularAreaOfinterests") String PerticularAreaOfinterests
    );


    @GET("webinar")
    Observable<Webinar_Home> GetMyWebinarList(@Header("Authorization") String authorization,
                                              @Query("WebinarFor") String webinarfor,
                                              @Query("page") int page,
                                              @Query("WebinarType") String WebinarType,
                                              @Query("PerticularAreaOfinterests") String PerticularAreaOfinterests
    );


    //get topics of interest

    @GET("topic-of-interest/list")
    Observable<Topicsofinterest> GetTopicsofInterests(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization

    );


    //get webinar details

    @GET("webinar-detail/{webinar_ids}")
    Observable<Webinar_Detail_Model> GetWebinarDetail(
            @Path("webinar_ids") String webinar_ids,
            @Header("Authorization") String authorization);


    //get Faq
    @GET("cms/faq")
    Observable<GetFaq> GetFaq(
            @Header("Accept") String accept);

    //get privacy policy
    @GET("cms/privacy_policy")
    Observable<GetPrivacyPolicy> GetPrivacyPolicy(
            @Header("Accept") String accept);

    //get terms and condition
    @GET("cms/terms_condition")
    Observable<GetTermsCondition> GetTermsandCondition(
            @Header("Accept") String accept);

    //get contact us
    @GET("contact-us/info")
    Observable<GetContactUsInfo> GetContactUsInfo(
            @Header("Accept") String accept);

    //post query
    @POST("contact-us/query")
    Observable<PostContactQuery> PostContactUsQuery(
            @Header("Accept") String accept,
            @Query("Message") String Message,
            @Query("Subject") String Subject);


    //post feedback
    @POST("contact-us/review")
    Observable<PostFeedback> PostContactUsFeedback(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Query("Message") String Message,
            @Query("Subject") String Subject);


    //Webinar like and dislike
    @POST("webinar/like-dislike")
    Observable<Webinar_Like_Dislike_Model> PostWebinarLikeDislike(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Query("webinar_id") int webinar_id);


    //notification
    @GET("notification")
    Observable<NotificationModel> GetNotificationModel(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization);


    //favorite count
    @GET("favorites")
    Observable<Favorite_Count_Model> GetFavoriteCountModel(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization);


    //get view topics of interest
    @GET("topic-of-interest/save")
    Observable<View_Topics_Interest_Model> GetViewTopicsOfInterest(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization);

    //post topics of interest
    @POST("topic-of-interest/save")
    @FormUrlEncoded
    Observable<SaveTopicsInterest> PostTopicsOfInterest(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Field("ids") String ids);


}
