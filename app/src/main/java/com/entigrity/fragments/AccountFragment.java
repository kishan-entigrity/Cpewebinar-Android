package com.entigrity.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.ActivityAddCard;
import com.entigrity.activity.ActivityChangePassword;
import com.entigrity.activity.ActivityContactUs;
import com.entigrity.activity.ActivityNotificationSetting;
import com.entigrity.activity.FaqActivity;
import com.entigrity.activity.MyTransactionActivity;
import com.entigrity.activity.PreLoginActivity;
import com.entigrity.activity.PrivacyPolicyActivity;
import com.entigrity.activity.TermsandConditionActivity;
import com.entigrity.activity.TopicsOfInterestActivity;
import com.entigrity.activity.ViewProfileActivity;
import com.entigrity.databinding.FragmentAccountBinding;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.model.postfeedback.PostFeedback;
import com.entigrity.model.view_topics_of_interest.TopicOfInterestsItem;
import com.entigrity.model.view_topics_of_interest.View_Topics_Interest_Model;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.model.viewprofile_proffesional_credential.modelViewProfileProfesional;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountFragment extends Fragment {

    View view;
    public Context context;
    FragmentAccountBinding binding;
    ProgressDialog progressDialog;
    public Dialog myDialog;
    private APIService mAPIService_new;
    public ImageView ivclose;
    public EditText edt_subject, edt_review;
    public Button btn_submit;
    public String firstname = "", lastname = "", email = "", firmname = "", mobilenumber = "", zipcode = "", country = "", ptin_number = "";
    public int country_id = 0, state_id = 0, city_id = 0, jobtitle_id = 0, industry_id = 0;
    public String job_titile = "", industry = "";
    public ArrayList<modelViewProfileProfesional> professionalcredential = new
            ArrayList<>();
    public int whoyouare = 0;
    public String state = "", city = "";
    private ArrayList<TopicOfInterestsItem> topicsofinterestitem = new ArrayList<TopicOfInterestsItem>();
    private static final String TAG = AccountFragment.class.getName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, null, false);
        context = getActivity();
        mAPIService_new = ApiUtilsNew.getAPIService();

        OnClick();

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ConfirmationPopup();
                    return true;
                }
                return false;
            }
        });

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetProfile();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        return view = binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (Constant.isdataupdate) {
            Constant.isdataupdate = false;
            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetProfile();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    private void GetTopicsofInterest() {

        mAPIService_new.GetViewTopicsOfInterest(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<View_Topics_Interest_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.tvTopicsOfInterest, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(View_Topics_Interest_Model view_topics_interest_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (topicsofinterestitem.size() > 0) {
                            topicsofinterestitem.clear();
                        }


                        if (view_topics_interest_model.isSuccess() == true) {
                            if (view_topics_interest_model.getPayload().getTopicOfInterests().size() > 0) {
                                for (int i = 0; i < view_topics_interest_model.getPayload().getTopicOfInterests().size(); i++) {
                                    topicsofinterestitem.add(view_topics_interest_model.getPayload().getTopicOfInterests().get(i));
                                }
                            }
                        } else {
                            Snackbar.make(binding.tvTopicsOfInterest, view_topics_interest_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    public void GetProfile() {

        mAPIService_new.GetProfile(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewProfileModel>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetTopicsofInterest();
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.rvFeedback, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(ViewProfileModel viewProfileModel) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (viewProfileModel.isSuccess() == true) {

                            if (viewProfileModel.getPayload().getData().getFirstName() != null
                                    && !viewProfileModel.getPayload().getData().getFirstName().equalsIgnoreCase("")) {
                                firstname = viewProfileModel.getPayload().getData().getFirstName();

                            }

                            if (!viewProfileModel.getPayload().getData().getProfilePicture().equalsIgnoreCase("")
                                    && viewProfileModel.getPayload().getData().getProfilePicture() != null) {
                                Picasso.with(context).load(viewProfileModel.getPayload().getData().getProfilePicture())
                                        .placeholder(R.drawable.profile_place_holder)
                                        .into(binding.ivprofilepicture);
                            } else {

                                binding.ivprofilepicture.setImageResource(R.drawable.profile_place_holder);
                            }


                            if (viewProfileModel.getPayload().getData().getFirstName() != null
                                    && !viewProfileModel.getPayload().getData().getFirstName().equalsIgnoreCase("")) {
                                binding.tvUsername.setText(viewProfileModel.getPayload().getData().getFirstName());
                            }


                            if (viewProfileModel.getPayload().getData().getLastName() != null
                                    && !viewProfileModel.getPayload().getData().getLastName().equalsIgnoreCase("")) {
                                lastname = viewProfileModel.getPayload().getData().getLastName();

                            }

                            if (viewProfileModel.getPayload().getData().getEmail() != null
                                    && !viewProfileModel.getPayload().getData().getEmail().equalsIgnoreCase("")) {
                                email = viewProfileModel.getPayload().getData().getEmail();
                            }

                            if (viewProfileModel.getPayload().getData().getFirmName() != null
                                    && !viewProfileModel.getPayload().getData().getFirmName().equalsIgnoreCase("")) {
                                firmname = viewProfileModel.getPayload().getData().getFirmName();
                            }


                            if (viewProfileModel.getPayload().getData().getContactNo() != null
                                    && !viewProfileModel.getPayload().getData().getContactNo().equalsIgnoreCase("")) {
                                mobilenumber = viewProfileModel.getPayload().getData().getContactNo();
                            }


                            if (viewProfileModel.getPayload().getData().getPtinNumber() != null
                                    && !viewProfileModel.getPayload().getData().getPtinNumber().equalsIgnoreCase("")) {
                                ptin_number = viewProfileModel.getPayload().getData().getPtinNumber();
                            }


                            if (viewProfileModel.getPayload().getData().getJobtitleName() != null
                                    && !viewProfileModel.getPayload().getData().getJobtitleName().equalsIgnoreCase("")) {

                                jobtitle_id = viewProfileModel.getPayload().getData().getJobtitleId();
                                job_titile = viewProfileModel.getPayload().getData().getJobtitleName();
                            }

                            if (viewProfileModel.getPayload().getData().getIndustryName() != null
                                    && !viewProfileModel.getPayload().getData().getIndustryName().equalsIgnoreCase("")) {
                                industry_id = viewProfileModel.getPayload().getData().getIndustryId();
                                industry = viewProfileModel.getPayload().getData().getIndustryName();
                            }


                            if (viewProfileModel.getPayload().getData().getCountry() != null
                                    && !viewProfileModel.getPayload().getData().getCountry().equalsIgnoreCase("")) {
                                country_id = viewProfileModel.getPayload().getData().getCountryId();
                                country = viewProfileModel.getPayload().getData().getCountry();
                            }
                            if (viewProfileModel.getPayload().getData().getState() != null
                                    && !viewProfileModel.getPayload().getData().getState().equalsIgnoreCase("")) {
                                state_id = viewProfileModel.getPayload().getData().getStateId();
                                state = viewProfileModel.getPayload().getData().getState();
                            }

                            if (viewProfileModel.getPayload().getData().getCity() != null
                                    && !viewProfileModel.getPayload().getData().getCity().equalsIgnoreCase("")) {
                                city_id = viewProfileModel.getPayload().getData().getCityId();
                                city = viewProfileModel.getPayload().getData().getCity();
                            }


                            if (viewProfileModel.getPayload().getData().getZipcode() != null
                                    && !viewProfileModel.getPayload().getData().getZipcode().equalsIgnoreCase("")) {
                                zipcode = viewProfileModel.getPayload().getData().getZipcode();
                            }


                            if (viewProfileModel.getPayload().getData().getUserType() != null) {
                                //whoyouare = Integer.parseInt(viewProfileModel.getPayload().getData().getUserTypeId());


                                for (int i = 0; i < viewProfileModel.getPayload().getData().getUserType().size(); i++) {
                                    modelViewProfileProfesional modelViewProfileProfesionalcredential = new
                                            modelViewProfileProfesional(
                                            viewProfileModel.getPayload().getData().getUserType().get(i).getName(),
                                            viewProfileModel.getPayload().getData().getUserType().get(i).getId());


                                    modelViewProfileProfesionalcredential.setId(
                                            viewProfileModel.getPayload().getData().getUserType().get(i).getId());

                                    modelViewProfileProfesionalcredential.setName(
                                            viewProfileModel.getPayload().getData().getUserType().get(i).getName());

                                    professionalcredential.add(modelViewProfileProfesionalcredential);


                                }


                            }

                        } else {

                            Snackbar.make(binding.rvFeedback, viewProfileModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    public void ShowFeedBackPopUp() {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.feedback_popup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivclose = (ImageView) myDialog.findViewById(R.id.ivclose);
        edt_subject = (EditText) myDialog.findViewById(R.id.edt_subject);
        edt_review = (EditText) myDialog.findViewById(R.id.edt_review);
        btn_submit = (Button) myDialog.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        PostFeedback(getResources().getString(R.string.accept), AppSettings.get_login_token(context),
                                edt_subject.getText().toString(), edt_review.getText().toString());
                    } else {
                        Snackbar.make(binding.rvFeedback, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });


        edt_subject.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edt_subject.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edt_review.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_review.setRawInputType(InputType.TYPE_CLASS_TEXT);


        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

            }
        });


        myDialog.show();


    }


    public void ConfirmationPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        // alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.exit_validation));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                dialog.cancel();
                getActivity().finish();


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }


    public void OnClick() {

        binding.rvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFeedBackPopUp();
            }
        });


        binding.tvPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ActivityAddCard.class);
                context.startActivity(i);

            }
        });


        binding.rvNameProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigate_ViewProfile();

            }
        });


        binding.rvTopicsOfInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TopicsOfInterestActivity.class);
                i.putExtra(getResources().getString(R.string.str_get_key_screen_key), getResources().getString(R.string.from_account_screen));
                startActivity(i);
            }
        });


        binding.rvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityNotificationSetting.class);
                getActivity().startActivity(i);
            }
        });

        binding.tvMytransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyTransactionActivity.class);
                getActivity().startActivity(i);
            }
        });


        binding.rvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityChangePassword.class);
                getActivity().startActivity(i);
            }
        });


        binding.rvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityContactUs.class);
                getActivity().startActivity(i);
            }
        });


        binding.rvCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.getInstance().SetCreditScreen();

            }
        });


        binding.rvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), PrivacyPolicyActivity.class);
                getActivity().startActivity(i);


            }
        });

        binding.rvTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), TermsandConditionActivity.class);
                getActivity().startActivity(i);

            }
        });

        binding.rvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FaqActivity.class);
                getActivity().startActivity(i);

            }
        });

        binding.rvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutPoPup();

            }
        });

    }

    public void Navigate_ViewProfile() {
        Intent i = new Intent(context, ViewProfileActivity.class);
        i.putExtra(getResources().getString(R.string.pass_fname), firstname);
        i.putExtra(getResources().getString(R.string.pass_lname), lastname);
        i.putExtra(getResources().getString(R.string.pass_email), email);
        i.putExtra(getResources().getString(R.string.pass_firm_name), firmname);
        i.putExtra(getResources().getString(R.string.pass_mobile_number), mobilenumber);
        i.putExtra(getResources().getString(R.string.pass_ptin_number), ptin_number);
        i.putExtra(getResources().getString(R.string.pass_country), country_id);
        i.putExtra(getResources().getString(R.string.pass_state), state_id);
        i.putExtra(getResources().getString(R.string.pass_city), city_id);
        i.putExtra(getResources().getString(R.string.pass_job_title), jobtitle_id);
        i.putExtra(getResources().getString(R.string.pass_industry), industry_id);
        i.putExtra(getResources().getString(R.string.pass_job_title_text), job_titile);
        i.putExtra(getResources().getString(R.string.pass_industry_text), industry);
        i.putExtra(getResources().getString(R.string.pass_country_text), country);
        i.putExtra(getResources().getString(R.string.pass_state_text), state);
        i.putExtra(getResources().getString(R.string.pass_city_text), city);
        i.putExtra(getResources().getString(R.string.pass_zipcode), zipcode);
        /*i.putExtra(getResources().getString(R.string.pass_who_you_are), whoyouare);*/
        i.putParcelableArrayListExtra(getResources().getString(R.string.pass_who_you_are_text), professionalcredential);
        i.putParcelableArrayListExtra(getResources().getString(R.string.pass_view_topics_of_interest), topicsofinterestitem);
        startActivity(i);
    }

    public void LogOutPoPup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.logout_text));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event

                dialog.cancel();

                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    Logout(getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

    public void Logout(String Authorization, String device_id, String device_token, String device_type) {

        // RxJava
        mAPIService_new.logout(getResources().getString(R.string.accept), Authorization, device_id
                , device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LogoutModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.rvLogout, message, Snackbar.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onNext(LogoutModel logoutModel) {
                        if (logoutModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));
                            AppSettings.set_login_token(context, "");
                            AppSettings.set_device_id(context, "");
                            AppSettings.set_email_id(context, "");


                            Intent i = new Intent(getActivity(), PreLoginActivity.class);
                            startActivity(i);
                            getActivity().finish();


                            Snackbar.make(binding.rvLogout, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();

                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Snackbar.make(binding.rvLogout, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    public void PostFeedback(String accept, String authorization, String message, String subject) {

        mAPIService_new.PostContactUsFeedback(accept, getResources().getString(R.string.bearer) + " " + authorization, message,
                subject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostFeedback>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.rvFeedback, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }


                    @Override
                    public void onNext(PostFeedback postFeedback) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }


                        if (postFeedback.isSuccess()) {
                            Snackbar.make(binding.rvFeedback, postFeedback.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.rvFeedback, postFeedback.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });


    }


    public Boolean Validation() {
        edt_subject.setFocusable(true);
        edt_subject.setFocusableInTouchMode(true);

        edt_review.setFocusable(true);
        edt_review.setFocusableInTouchMode(true);

        if (edt_subject.getText().toString().isEmpty()) {
            Constant.toast(context, getResources().getString(R.string.val_subject));

            return false;
        } else if (edt_review.getText().toString().isEmpty()) {
            Constant.toast(context, getResources().getString(R.string.val_review));
            return false;
        } else {
            return true;
        }
    }


}
