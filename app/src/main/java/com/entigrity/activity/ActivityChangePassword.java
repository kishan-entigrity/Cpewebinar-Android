package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.databinding.ActivityChangePasswordBinding;
import com.entigrity.model.changepassword.ChangePasswordModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityChangePassword extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    public Context context;
    private APIService mAPIService;
    private static final String TAG = ActivityChangePassword.class.getName();
    ProgressDialog progressDialog;
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mAPIService = ApiUtils.getAPIService();
        context = ActivityChangePassword.this;


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        ChangePassword(AppSettings.get_login_token(context), Constant.Trim(binding.edtOldpassword.getText().toString()),
                                Constant.Trim(binding.edtNewpassword.getText().toString()),
                                Constant.Trim(binding.edtConfirmpassword.getText().toString()));
                    } else {
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.edtOldpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!Constant.isValidPassword(Constant.Trim(binding.edtOldpassword.getText().toString()))) {
                        Constant.hideKeyboard(ActivityChangePassword.this);
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
                    } else {
                        binding.edtNewpassword.requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });


        binding.edtNewpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!Constant.isValidPassword(Constant.Trim(binding.edtNewpassword.getText().toString()))) {
                        Constant.hideKeyboard(ActivityChangePassword.this);
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
                    } else {
                        binding.edtConfirmpassword.requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });

        binding.edtConfirmpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!Constant.isValidPassword(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {
                        Constant.hideKeyboard(ActivityChangePassword.this);
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
                    } else if (!Constant.Trim(binding.edtNewpassword.getText().toString()).equals(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {
                        Constant.hideKeyboard(ActivityChangePassword.this);
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.val_new_confirm_password_not_match), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Constant.hideKeyboard(ActivityChangePassword.this);
                    }
                    return true;
                }

                return false;
            }
        });


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
                        Snackbar.make(binding.btnSubmit, message, Snackbar.LENGTH_SHORT).show();


                    }


                    @Override
                    public void onNext(ChangePasswordModel changePasswordModel) {
                        if (changePasswordModel.isSuccess()) {
                            ClearData();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.btnSubmit, changePasswordModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (changePasswordModel.getPayload().getAccessToken() != null && !changePasswordModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                if (Validation()) {
                                    if (Constant.isNetworkAvailable(context)) {

                                        AppSettings.set_login_token(context, changePasswordModel.getPayload().getAccessToken());

                                        ChangePassword(AppSettings.get_login_token(context), binding.edtOldpassword.getText().toString(), binding.edtNewpassword.getText().toString(),
                                                binding.edtConfirmpassword.getText().toString());
                                    } else {
                                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Snackbar.make(binding.btnSubmit, changePasswordModel.getMessage(), Snackbar.LENGTH_SHORT).show();
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
        if (Constant.Trim(binding.edtOldpassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.validate_oldpassword), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtNewpassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.validate_newpassword), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.isValidPassword(Constant.Trim(binding.edtNewpassword.getText().toString()))) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.validate_confirmpassword), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.isValidPassword(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.Trim(binding.edtNewpassword.getText().toString()).equals(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {
            Snackbar.make(binding.btnSubmit, getResources().getString(R.string.val_new_confirm_password_not_match), Snackbar.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }


    }
}
