package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.entigrity.R;
import com.entigrity.databinding.ActivityForgotpasswordBinding;
import com.entigrity.model.forgotpassword.Forgotpaawordmodel;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotpasswordBinding binding;
    public Context context;
    private APIService mAPIService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgotpassword);
        context = ForgotPasswordActivity.this;
        mAPIService = ApiUtils.getAPIService();


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        ForgotPassword(binding.edtEmailId.getText().toString());
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }

                }
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public Boolean Validation() {

        if (binding.edtEmailId.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.forgot_passoword_email), context);
            return false;
        } else {
            return true;
        }


    }


    public void ForgotPassword(String emailid) {

        // RxJava
        mAPIService.forgotpassword(emailid
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Forgotpaawordmodel>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
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
                    public void onNext(Forgotpaawordmodel forgotpaawordmodel) {
                        if (forgotpaawordmodel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            binding.edtEmailId.setText("");
                            Constant.ShowPopUp(forgotpaawordmodel.getMessage(), context);
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Constant.ShowPopUp(forgotpaawordmodel.getMessage(), context);
                        }

                    }
                });

    }


}
