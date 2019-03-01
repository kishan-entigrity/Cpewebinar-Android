package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.ActivityLoginBinding;
import com.entigrity.model.login.LoginModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    public Context context;
    private APIService mAPIService;
    private boolean checkpasswordvisiblestatus = false;
    private static final String TAG = LoginActivity.class.getName();
    public TextView tv_popup_msg, tv_popup_submit;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mAPIService = ApiUtils.getAPIService();
        context = LoginActivity.this;


        AppSettings.set_device_id(context, Constant.GetDeviceid(context));
        AppSettings.set_device_token(context, "12345");


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        LoginPost(Constant.Trim(binding.edtusername.getText().toString()), Constant.Trim(binding.edtpassword.getText()
                                .toString()), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);
                    } else {
                        Snackbar.make(binding.btnSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (Constant.isNetworkAvailable(context)) {
                                            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                                            LoginPost(Constant.Trim(binding.edtusername.getText().toString()), Constant.Trim(binding.edtpassword.getText()
                                                    .toString()), AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type);

                                        }
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.webinar_status))
                                .show();
                    }


                }
            }
        });

        binding.edtpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!Constant.isValidPassword(Constant.Trim(binding.edtpassword.getText().toString()))) {
                        Constant.ShowPopUp(getResources().getString(R.string.password_regex_validation), context);
                    } else {
                        Constant.hideKeyboard(LoginActivity.this);
                    }
                    return true;
                }

                return false;
            }
        });


        binding.tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SignUpActivity.class);
                startActivity(i);


            }
        });


        binding.strForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);

            }
        });


        binding.edtpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtpassword.getRight() - binding.edtpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if (binding.edtpassword.getText().length() > 0) {
                            if (checkpasswordvisiblestatus == false) {
                                checkpasswordvisiblestatus = true;
                                binding.edtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                binding.edtpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eye, 0);
                                binding.edtpassword.setSelection(binding.edtpassword.length());

                            } else {
                                checkpasswordvisiblestatus = false;
                                binding.edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.edtpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eys, 0);
                                binding.edtpassword.setSelection(binding.edtpassword.length());
                            }

                        } else {

                            Constant.ShowPopUp(getResources().getString(R.string.validate_password), context);

                        }


                        return true;
                    }
                }
                return false;
            }
        });


    }


    public void LoginPost(String username, String password, String device_id, String device_token, String device_type) {

        // RxJava
        mAPIService.login(username, password
                , device_id, device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginModel>() {
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
                    public void onNext(LoginModel login) {
                        if (login.isSuccess()) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            AppSettings.set_login_token(context, login.getPayload().getToken());

                            Constant.Log(TAG, "login token" + AppSettings.get_login_token(context));


                            Intent i = new Intent(context, MainActivity.class);
                            startActivity(i);
                            finish();


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Constant.ShowPopUp(login.getMessage(), context);
                        }


                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Boolean Validation() {
        if (Constant.Trim(binding.edtusername.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtusername, getResources().getString(R.string.validate_email_id), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.isValidEmailId(Constant.Trim(binding.edtusername.getText().toString()))) {
            Snackbar.make(binding.edtusername, getResources().getString(R.string.valid_email), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtpassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtpassword, getResources().getString(R.string.validate_password), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.isValidPassword(Constant.Trim(binding.edtpassword.getText().toString()))) {
            Snackbar.make(binding.edtpassword, getResources().getString(R.string.password_regex_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }


    }


}
