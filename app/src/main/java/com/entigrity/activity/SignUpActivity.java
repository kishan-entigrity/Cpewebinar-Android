package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.ActivitySignupBinding;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpActivity extends AppCompatActivity {
    public Context context;
    ActivitySignupBinding binding;
    private APIService mAPIService_new;
    private static final String TAG = SignUpActivity.class.getName();
    private boolean checkpasswordvisiblestatus = false;
    private boolean checkconfirmpasswordvisiblestatus = false;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = SignUpActivity.this;
        mAPIService_new = ApiUtilsNew.getAPIService();

        AppSettings.set_device_id(context, Constant.GetDeviceid(context));

        final String email = binding.edtEmailid.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



        binding.edtEmailid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Do you job here which you want to done through event
                    Toast.makeText(context, "Got Clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });







        binding.edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtPassword.getRight() - binding.edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if (binding.edtPassword.getText().length() > 0) {
                            if (checkpasswordvisiblestatus == false) {
                                checkpasswordvisiblestatus = true;
                                binding.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eye, 0);
                                binding.edtPassword.setSelection(binding.edtPassword.length());

                            } else {
                                checkpasswordvisiblestatus = false;
                                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eys, 0);
                                binding.edtPassword.setSelection(binding.edtPassword.length());
                            }

                        } else {
                            Snackbar.make(binding.edtPassword, getResources().getString(R.string.validate_password), Snackbar.LENGTH_SHORT).show();


                        }


                        return true;
                    }
                }


                return false;
            }
        });


        binding.edtConfirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtConfirmpassword.getRight() - binding.edtConfirmpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if (binding.edtConfirmpassword.getText().length() > 0) {
                            if (checkconfirmpasswordvisiblestatus == false) {
                                checkconfirmpasswordvisiblestatus = true;
                                binding.edtConfirmpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                binding.edtConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eye, 0);
                                binding.edtConfirmpassword.setSelection(binding.edtConfirmpassword.length());

                            } else {
                                checkconfirmpasswordvisiblestatus = false;
                                binding.edtConfirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.edtConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eys, 0);
                                binding.edtConfirmpassword.setSelection(binding.edtPassword.length());
                            }

                        } else {
                            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.validate_confirmpassword), Snackbar.LENGTH_SHORT).show();
                        }


                        return true;
                    }
                }


                return false;
            }
        });


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SignUpNextActivity.class);
                startActivity(i);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(context, LoginActivity.class);
        startActivity(i);
        finish();
    }


    public Boolean Validation() {

        if (Constant.Trim(binding.edtFirstname.getText().toString()).isEmpty()) {
            binding.edtFirstname.requestFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtFirstname, getResources().getString(R.string.val_firstname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtLastname.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.requestFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtLastname, getResources().getString(R.string.val_lastname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtEmailid.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.requestFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.requestFocus();
            binding.edtConfirmpassword.clearFocus();

            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).length() < 6) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.requestFocus();
            binding.edtConfirmpassword.clearFocus();

            Snackbar.make(binding.edtPassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.requestFocus();


            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.val_confirm_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).length() < 6) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.requestFocus();


            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.Trim(binding.edtPassword.getText().toString()).equals(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_confirm_password_not_match), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public void RegisterPost(String first_name, String last_name, String email, String password, String confirm_password,
                             String firm_name, String contact_no, String tags, int user_type, String device_id,
                             String device_token, String device_type
    ) {

        // RxJava
        mAPIService_new.Register(getResources().getString(R.string.accept), first_name, last_name
                , email, password, confirm_password, firm_name, contact_no, tags, user_type
                , device_id, device_token, device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegistrationModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {


                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.btnNext, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(RegistrationModel registrationModel) {
                        if (registrationModel.isSuccess()) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Constant.arraylistselectedvalue.clear();
                            AppSettings.set_login_token(context, registrationModel.getPayload().getUser().getToken());
                            AppSettings.set_email_id(context, registrationModel.getPayload().getUser().getEmail());


                            Intent i = new Intent(context, MainActivity.class);
                            startActivity(i);
                            finish();


                        } else if (!registrationModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            Snackbar.make(binding.btnNext, registrationModel.getMessage(), Snackbar.LENGTH_SHORT).show();


                        }


                    }
                });

    }


}
