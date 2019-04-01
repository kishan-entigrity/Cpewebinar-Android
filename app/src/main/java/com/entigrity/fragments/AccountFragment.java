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
import com.entigrity.activity.ActivityChangePassword;
import com.entigrity.activity.ActivityContactUs;
import com.entigrity.activity.ActivityNotificationSetting;
import com.entigrity.activity.FaqActivity;
import com.entigrity.activity.LoginActivity;
import com.entigrity.activity.MyTransactionActivity;
import com.entigrity.activity.NotificationActivity;
import com.entigrity.activity.PrivacyPolicyActivity;
import com.entigrity.activity.TermsandConditionActivity;
import com.entigrity.databinding.FragmentAccountBinding;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.model.postfeedback.PostFeedback;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;
import com.entigrity.webservice.ApiUtilsNew;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountFragment extends Fragment {

    View view;
    public Context context;
    FragmentAccountBinding binding;
    private static final String TAG = AccountFragment.class.getName();
    ProgressDialog progressDialog;
    public Dialog myDialog;
    private APIService mAPIService;
    private APIService mAPIService_new;
    public ImageView ivclose;
    public EditText edt_subject, edt_review;
    public Button btn_submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, null, false);
        context = getActivity();
        mAPIService = ApiUtils.getAPIService();
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


        return view = binding.getRoot();

    }


    public void ShowFeedBackPopUp() {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.rating_popup);
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
                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
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
        mAPIService.logout(getResources().getString(R.string.bearer) + Authorization, device_id
                , device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LogoutModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //handle failure response
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.rvLogout, message, Snackbar.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onNext(LogoutModel logoutModel) {
                        if (logoutModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));


                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();


                            Snackbar.make(binding.rvLogout, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();

                        } else {
                            if (logoutModel.getPayload().getAccessToken() != null && !logoutModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, logoutModel.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                                } else {
                                    Snackbar.make(binding.rvLogout, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                }


                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Snackbar.make(binding.rvLogout, logoutModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }

                        }


                    }
                });

    }


    public void PostFeedback(String accept, String authorization, String message, String subject) {

        mAPIService_new.PostContactUsFeedback(accept, getResources().getString(R.string.bearer) + authorization, message,
                subject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostFeedback>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //handle failure response
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.rvFeedback, message, Snackbar.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onNext(PostFeedback postFeedback) {
                        if (postFeedback.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            Snackbar.make(binding.rvFeedback, postFeedback.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(binding.rvFeedback, postFeedback.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });


    }


    public Boolean Validation() {
        if (edt_subject.getText().toString().isEmpty()) {
            Snackbar.make(binding.rvFeedback, getResources().getString(R.string.val_subject), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (edt_review.getText().toString().isEmpty()) {
            Snackbar.make(binding.rvFeedback, getResources().getString(R.string.val_review), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}
