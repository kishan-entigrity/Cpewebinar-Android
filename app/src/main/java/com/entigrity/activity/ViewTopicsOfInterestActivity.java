package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.R;
import com.entigrity.adapter.ViewTopicsOfInterestAdapter;
import com.entigrity.databinding.ActivityViewTopicsofinterestBinding;
import com.entigrity.model.view_topics_of_interest.TopicOfInterestsItem;
import com.entigrity.utility.Constant;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.util.ArrayList;

public class ViewTopicsOfInterestActivity extends AppCompatActivity {
    public Context context;
    private APIService mAPIService;
    public ActivityViewTopicsofinterestBinding binding;
    private static final String TAG = ViewTopicsOfInterestActivity.class.getName();
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<TopicOfInterestsItem> topicsofinterestitem = new ArrayList<TopicOfInterestsItem>();
    ViewTopicsOfInterestAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topicsofinterest);
        mAPIService = ApiUtilsNew.getAPIService();
        context = ViewTopicsOfInterestActivity.this;


        Intent intent = getIntent();
        if (intent != null) {
            topicsofinterestitem = intent.getParcelableArrayListExtra(getResources().getString(R.string.pass_view_topics_of_interest));
            Constant.Log(TAG, "size" + topicsofinterestitem.size());


            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            binding.rvtopicsOfInterest.setLayoutManager(linearLayoutManager);
            binding.rvtopicsOfInterest.addItemDecoration(new SimpleDividerItemDecoration(context));


            if (topicsofinterestitem.size() > 0) {
                adapter = new ViewTopicsOfInterestAdapter(context, topicsofinterestitem);
                binding.rvtopicsOfInterest.setAdapter(adapter);
            }


        }




      /*  if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetTopicsofInterest();
        } else {
            Snackbar.make(binding.rvtopicsOfInterest, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }*/


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

   /* private void GetTopicsofInterest() {

        mAPIService.GetViewTopicsOfInterest(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<View_Topics_Interest_Model>() {
                    @Override
                    public void onCompleted() {
                        if (topicsofinterestitem.size() > 0) {
                            adapter = new ViewTopicsOfInterestAdapter(context, topicsofinterestitem);
                            binding.rvtopicsOfInterest.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.rvtopicsOfInterest, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(View_Topics_Interest_Model view_topics_interest_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        if (view_topics_interest_model.getPayload().getTopicOfInterests().size() > 0) {

                            for (int i = 0; i < view_topics_interest_model.getPayload().getTopicOfInterests().size(); i++) {
                                TopicOfInterestsItem topicOfInterestsItem = new TopicOfInterestsItem();
                                topicOfInterestsItem.setName(view_topics_interest_model.getPayload().getTopicOfInterests().get(i).getName());
                                topicOfInterestsItem.setId(view_topics_interest_model.getPayload().getTopicOfInterests().get(i).getId());
                                if (view_topics_interest_model.getPayload().getTopicOfInterests().get(i).getTags() != null) {
                                    topicOfInterestsItem.setTags(view_topics_interest_model.getPayload().getTopicOfInterests().get(i).getTags());
                                }

                                topicsofinterestitem.add(topicOfInterestsItem);
                            }


                            Constant.Log(TAG, "size" + topicsofinterestitem.size());

                        }


                    }
                });

    }*/


}
