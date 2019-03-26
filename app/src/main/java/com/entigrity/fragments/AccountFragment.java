package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.FaqActivity;
import com.entigrity.activity.LoginActivity;
import com.entigrity.activity.PrivacyPolicyActivity;
import com.entigrity.activity.TermsandConditionActivity;
import com.entigrity.databinding.FragmentAccountBinding;
import com.entigrity.model.logout.LogoutModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountFragment extends Fragment {

    View view;
    public Context context;
    FragmentAccountBinding binding;
    private static final String TAG = AccountFragment.class.getName();
    ProgressDialog progressDialog;
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, null, false);
        context = getActivity();
        mAPIService = ApiUtils.getAPIService();

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
                        Constant.ShowPopUp(message, context);

                    }


                    @Override
                    public void onNext(LogoutModel logoutModel) {
                        if (logoutModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            ShowPopUp(logoutModel.getMessage(), context);
                        } else {
                            if (logoutModel.getPayload().getAccessToken() != null && !logoutModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, logoutModel.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    Logout(AppSettings.get_login_token(context), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }


                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(logoutModel.getMessage(), context);
                            }

                        }


                    }
                });

    }

    public void ShowPopUp(String message, final Context context) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();

                AppSettings.removeFromSharedPreferences(context, getResources().getString(R.string.str_token));


                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
