package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.entigrity.R;
import com.entigrity.databinding.ActivityPaymentBinding;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    public Context context;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    private static final String TAG = PaymentActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        context = PaymentActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.webview.canGoBack()) {
            binding.webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(url);
            System.out.println("when you click on any interlink on webview that time you got url :-" + url);
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}
