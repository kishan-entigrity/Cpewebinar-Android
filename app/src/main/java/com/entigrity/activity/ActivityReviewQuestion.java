package com.entigrity.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.entigrity.R;
import com.entigrity.databinding.ActivityReviewQuetionBinding;

public class ActivityReviewQuestion extends AppCompatActivity {

    ActivityReviewQuetionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_quetion);

    }
}
