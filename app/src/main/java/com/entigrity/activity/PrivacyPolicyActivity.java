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
import com.entigrity.databinding.ActivityPrivacypolicyBinding;
import com.entigrity.model.getfaq.GetFaq;
import com.entigrity.model.getprivacypolicy.GetPrivacyPolicy;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PrivacyPolicyActivity extends AppCompatActivity {
    ActivityPrivacypolicyBinding binding;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public Context context;
    private static final String TAG = PrivacyPolicyActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacypolicy);
        context = PrivacyPolicyActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetPrivacyPolicy();
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


    public void GetPrivacyPolicy() {
        mAPIService.GetPrivacyPolicy(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPrivacyPolicy>() {
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
                    public void onNext(GetPrivacyPolicy getPrivacyPolicy) {

                        if (getPrivacyPolicy.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            binding.webview.setWebViewClient(new PrivacyPolicyActivity.CustomWebViewClient());
                            WebSettings webSetting = binding.webview.getSettings();
                            webSetting.setJavaScriptEnabled(true);
                            webSetting.setDisplayZoomControls(true);
                            binding.webview.loadUrl(getPrivacyPolicy.getPayload().getLink());

                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivback, getPrivacyPolicy.getMessage(), Snackbar.LENGTH_SHORT).show();
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
