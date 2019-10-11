package com.entigrity.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.entigrity.R;
import com.entigrity.databinding.ActivitySignupNextBinding;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

public class SignUpNextActivity extends AppCompatActivity {

    ActivitySignupNextBinding binding;
    private APIService mAPIService_new;
    private static final String TAG = SignUpNextActivity.class.getName();
    public Context context;
    private String prefix = "P-";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_next);
        context = SignUpNextActivity.this;
        mAPIService_new = ApiUtilsNew.getAPIService();


        binding.edtPtinNumber.setText(prefix);
        binding.edtPtinNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(prefix)) {
                    binding.edtPtinNumber.setText(prefix);
                    binding.edtPtinNumber.setSelection(2);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(context, SignUpActivity.class);
        startActivity(i);
        finish();

    }
}
