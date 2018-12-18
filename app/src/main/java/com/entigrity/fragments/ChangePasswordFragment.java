package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.databinding.FragmentChangePasswordBinding;
import com.entigrity.model.changepassword.ChangePasswordModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePasswordFragment extends Fragment {

    public Context context;
    private FragmentChangePasswordBinding binding;
    private APIService mAPIService;
    private static final String TAG = ChangePasswordFragment.class.getName();
    ProgressDialog progressDialog;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        ChangePassword(AppSettings.get_login_token(context), binding.edtOldpassword.getText().toString(), binding.edtNewpassword.getText().toString(),
                                binding.edtConfirmpassword.getText().toString());
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }
                }

            }
        });


        return view = binding.getRoot();
    }

    public void ChangePassword(String Authorization, String current_password, String new_password, String confirm_password) {

        // RxJava
        mAPIService.changepassword(getResources().getString(R.string.bearer) + Authorization, current_password
                , new_password, confirm_password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordModel>() {
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
                    public void onNext(ChangePasswordModel changePasswordModel) {
                        if (changePasswordModel.isSuccess()) {
                            ClearData();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Constant.ShowPopUp(changePasswordModel.getMessage(), context);
                        } else {
                            if (changePasswordModel.getPayload().getAccessToken() != null && !changePasswordModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                if (Validation()) {
                                    if (Constant.isNetworkAvailable(context)) {

                                        AppSettings.set_login_token(context, changePasswordModel.getPayload().getAccessToken());

                                        ChangePassword(AppSettings.get_login_token(context), binding.edtOldpassword.getText().toString(), binding.edtNewpassword.getText().toString(),
                                                binding.edtConfirmpassword.getText().toString());
                                    } else {
                                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                    }
                                }
                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Constant.ShowPopUp(changePasswordModel.getMessage(), context);
                            }
                        }


                    }
                });

    }

    private void ClearData() {

        binding.edtOldpassword.setText("");
        binding.edtNewpassword.setText("");
        binding.edtConfirmpassword.setText("");
    }


    public Boolean Validation() {
        if (binding.edtOldpassword.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.validate_oldpassword), context);
            return false;
        } else if (binding.edtNewpassword.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.validate_newpassword), context);
            return false;
        } else if (binding.edtConfirmpassword.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.validate_confirmpassword), context);
            return false;
        } else if (!binding.edtNewpassword.getText().toString().equals(binding.edtConfirmpassword.getText().toString())) {
            Constant.ShowPopUp(getResources().getString(R.string.val_new_confirm_password_not_match), context);
            return false;

        } else {
            return true;
        }


    }
}
