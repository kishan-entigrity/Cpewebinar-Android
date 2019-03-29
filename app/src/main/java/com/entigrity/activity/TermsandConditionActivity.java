package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.entigrity.R;
import com.entigrity.databinding.ActivityTermsandconditionBinding;
import com.entigrity.model.getprivacypolicy.GetPrivacyPolicy;
import com.entigrity.model.gettermscondition.GetTermsCondition;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TermsandConditionActivity extends AppCompatActivity {

    ActivityTermsandconditionBinding binding;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public Context context;
    private static final String TAG = TermsandConditionActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_termsandcondition);
        context = TermsandConditionActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetTermsandCondition();
        } else {
            Snackbar.make(binding.ivback, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    private void GetTermsandCondition() {

        mAPIService.GetTermsandCondition(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetTermsCondition>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.ivback, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(GetTermsCondition getTermsCondition) {

                        if (getTermsCondition.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            binding.webview.setWebViewClient(new TermsandConditionActivity.CustomWebViewClient());
                            WebSettings webSetting = binding.webview.getSettings();
                            webSetting.setJavaScriptEnabled(true);
                            webSetting.setDisplayZoomControls(true);
                            binding.webview.loadUrl(getTermsCondition.getPayload().getLink());

                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivback, getTermsCondition.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }

                });


    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
