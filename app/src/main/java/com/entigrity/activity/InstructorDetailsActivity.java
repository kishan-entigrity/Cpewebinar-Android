package com.entigrity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.entigrity.R;
import com.entigrity.databinding.ActivityInstructorProfileDetailsBinding;
import com.entigrity.fragments.AboutInstructorFragment;
import com.entigrity.fragments.InstructorDetailsFragment;
import com.entigrity.model.Instructorlist_details.Instructor_Details_Model;
import com.entigrity.model.instructor_follow.Instructor_Follow_Model;
import com.entigrity.model.instructor_like.Instructor_Like_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class InstructorDetailsActivity extends AppCompatActivity {
    ActivityInstructorProfileDetailsBinding binding;
    public Context context;
    private static final String TAG = InstructorDetailsActivity.class.getName();
    private static InstructorDetailsActivity instance;
    public int instructorid = 0;
    public Dialog myDialog_popup;
    ImageView ivclose;
    EditText edt_your_review;


    public String name, email, contact_no, logo, expertise, about_speaker, company, state, city, favorite_unfavorite_status, follow_unfollow_status;
    Button btn_submit;
    private APIService mAPIService;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instructor_profile_details);
        context = InstructorDetailsActivity.this;
        instance = InstructorDetailsActivity.this;
        mAPIService = ApiUtils.getAPIService();


        Intent intent = getIntent();
        if (intent != null) {

            instructorid = intent.getIntExtra(getResources().getString(R.string.pass_inst_id), 0);


            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetInstructor_Details();
            } else {
                Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
            }

        }


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkbackpressed = true;
                finish();
            }
        });


        binding.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRatingPopup(context);

            }
        });


        binding.ivfavoritestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    InstructorFavoriteStatus();
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }


            }
        });


        binding.ivfollowstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    InstructorFollowStatus();
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }

            }
        });


    }

    private void GetInstructor_Details() {


        mAPIService.GetInstructorDetails(String.valueOf(instructorid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Instructor_Details_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(Instructor_Details_Model instructor_details_model) {

                        if (instructor_details_model.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            about_speaker = instructor_details_model.getPayload().getSpeaker().getAboutSpeaker();

                            email = instructor_details_model.getPayload().getSpeaker().getEmail();
                            contact_no = instructor_details_model.getPayload().getSpeaker().getContactNo();
                            expertise = instructor_details_model.getPayload().getSpeaker().getExpertise();
                            company = instructor_details_model.getPayload().getSpeaker().getCompany();
                            state = instructor_details_model.getPayload().getSpeaker().getState();
                            city = instructor_details_model.getPayload().getSpeaker().getCity();

                            favorite_unfavorite_status = instructor_details_model.getPayload().getSpeaker().getFavoriteUnfavoriteStatus();
                            follow_unfollow_status = instructor_details_model.getPayload().getSpeaker().getFollowUnfollowStatus();


                            if (instructor_details_model.getPayload().getSpeaker().getFavoriteUnfavoriteStatus().equalsIgnoreCase(getResources().getString(R.string.Yes))) {
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite_hover);
                            } else {
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite);
                            }

                            if (instructor_details_model.getPayload().getSpeaker().getFollowUnfollowStatus().equalsIgnoreCase(getResources().getString(R.string.Yes))) {
                                binding.ivfollowstatus.setImageResource(R.mipmap.following);
                            } else {
                                binding.ivfollowstatus.setImageResource(R.mipmap.follow);
                            }

                            binding.tvInstructorfollowers.setText("" + instructor_details_model.getPayload().getSpeaker().getFollowerCount() + " " + "Followers");


                            if (!instructor_details_model.getPayload().getSpeaker().getLogo().equalsIgnoreCase("")) {
                                Picasso.with(context).load(instructor_details_model.getPayload().getSpeaker().getLogo())
                                        .placeholder(R.mipmap.placeholder)
                                        .into(binding.ivCustImage);
                            }

                            if (!instructor_details_model.getPayload().getSpeaker().getName().equalsIgnoreCase("")) {
                                binding.tvInstname.setText(instructor_details_model.getPayload().getSpeaker().getName());
                            }


                            setupViewPager(binding.viewpager);
                            binding.tabs.setupWithViewPager(binding.viewpager);


                        } else {
                            if (instructor_details_model.getPayload().getAccessToken() != null && !instructor_details_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(context)) {
                                    AppSettings.set_login_token(context, instructor_details_model.getPayload().getAccessToken());
                                    GetInstructor_Details();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                Constant.ShowPopUp(instructor_details_model.getMessage(), context);
                            }


                        }


                    }

                });


    }


    public void InstructorFavoriteStatus() {


        mAPIService.InstructorFavoriteStatus(String.valueOf(instructorid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), instructorid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Instructor_Like_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(Instructor_Like_Model instructor_like_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        if (instructor_like_model.isSuccess()) {
                            if (favorite_unfavorite_status.equalsIgnoreCase(getResources().getString(R.string.Yes))) {
                                favorite_unfavorite_status = getResources().getString(R.string.No);
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite);
                            } else if (favorite_unfavorite_status.equalsIgnoreCase(getResources().getString(R.string.No))) {
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite_hover);
                                favorite_unfavorite_status = getResources().getString(R.string.Yes);
                            }


                            Constant.ShowPopUp(instructor_like_model.getMessage(), context);


                        } else

                        {


                            if (instructor_like_model.getPayload().getAccessToken() != null && !instructor_like_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(context)) {
                                    AppSettings.set_login_token(context, instructor_like_model.getPayload().getAccessToken());
                                    InstructorFavoriteStatus();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                Constant.ShowPopUp(instructor_like_model.getMessage(), context);
                            }

                        }


                    }
                });

    }


    public void InstructorFollowStatus() {

        mAPIService.InstructorFollowStatus(String.valueOf(instructorid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), instructorid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Instructor_Follow_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(Instructor_Follow_Model instructor_follow_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        if (instructor_follow_model.isSuccess()) {
                            Constant.ShowPopUp(instructor_follow_model.getMessage(), context);


                            if (follow_unfollow_status.equalsIgnoreCase(getResources().getString(R.string.Yes))) {
                                binding.ivfollowstatus.setImageResource(R.mipmap.follow);
                                follow_unfollow_status = getResources().getString(R.string.No);
                            } else if (follow_unfollow_status.equalsIgnoreCase(getResources().getString(R.string.No))) {
                                binding.ivfollowstatus.setImageResource(R.mipmap.following);
                                follow_unfollow_status = getResources().getString(R.string.Yes);
                            }


                        } else {


                            if (instructor_follow_model.getPayload().getAccessToken() != null && !instructor_follow_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(context)) {
                                    AppSettings.set_login_token(context, instructor_follow_model.getPayload().getAccessToken());
                                    InstructorFollowStatus();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                Constant.ShowPopUp(instructor_follow_model.getMessage(), context);
                            }

                        }


                    }
                });

    }


    public void ShowRatingPopup(final Context context) {
        myDialog_popup = new Dialog(context);
        myDialog_popup.setContentView(R.layout.rating_popup);
        myDialog_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivclose = (ImageView) myDialog_popup.findViewById(R.id.ivclose);
        edt_your_review = (EditText) myDialog_popup.findViewById(R.id.edt_your_review);
        btn_submit = (Button) myDialog_popup.findViewById(R.id.btn_submit);

        edt_your_review.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_your_review.setRawInputType(InputType.TYPE_CLASS_TEXT);


        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog_popup.isShowing()) {
                    myDialog_popup.dismiss();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {

                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }

                }

            }
        });


        myDialog_popup.show();

    }


    public static InstructorDetailsActivity getInstance() {
        return instance;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  checkbackpressed = true;
        finish();
    }

    public Boolean Validation() {
        if (edt_your_review.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_review), context);
            return false;
        } else {
            return true;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutInstructorFragment(), getResources().getString(R.string.about));
        adapter.addFragment(new InstructorDetailsFragment(), getResources().getString(R.string.company_details));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
