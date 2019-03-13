package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.R;
import com.entigrity.adapter.RecyclerViewSectionAdapter;
import com.entigrity.databinding.ActivityTopicsofinterestBinding;
import com.entigrity.model.topicsofinterestn.TopicOfInterestsItem;
import com.entigrity.model.topicsofinterestn.Topicsofinterestmodel;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.DividerItemDecoration;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicsOfInterestActivity extends AppCompatActivity {
    public Context context;
    ActivityTopicsofinterestBinding binding;
    private APIService mAPIService;
    private static final String TAG = TopicsOfInterestActivity.class.getName();
    ProgressDialog progressDialog;
    private ArrayList<TopicOfInterestsItem> topicsofinterestitem = new ArrayList<TopicOfInterestsItem>();
    LinearLayoutManager linearLayoutManager;
    RecyclerViewSectionAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topicsofinterest);
        mAPIService = ApiUtils.getAPIService();
        context = TopicsOfInterestActivity.this;

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvtopicsOfInterest.setLayoutManager(linearLayoutManager);
        binding.rvtopicsOfInterest.addItemDecoration(new SimpleDividerItemDecoration(context));

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetTopicsofInterest();
        } else {
            Snackbar.make(binding.tvSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void GetTopicsofInterest() {
        mAPIService.GetTopicsofInterests().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Topicsofinterestmodel>() {
                    @Override
                    public void onCompleted() {
                        if (topicsofinterestitem.size() > 0) {
                            adapter = new RecyclerViewSectionAdapter(context, topicsofinterestitem);
                            binding.rvtopicsOfInterest.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.tvSubmit, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Topicsofinterestmodel topicsofinterestmodel) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        if (topicsofinterestmodel.getPayload().getTopicOfInterests().size() > 0) {

                            for (int i = 0; i < topicsofinterestmodel.getPayload().getTopicOfInterests().size(); i++) {
                                TopicOfInterestsItem topicOfInterestsItem = new TopicOfInterestsItem();
                                topicOfInterestsItem.setName(topicsofinterestmodel.getPayload().getTopicOfInterests().get(i).getName());
                                topicOfInterestsItem.setId(topicsofinterestmodel.getPayload().getTopicOfInterests().get(i).getId());
                                topicOfInterestsItem.setTags(topicsofinterestmodel.getPayload()
                                        .getTopicOfInterests().get(i).getTags());
                                topicsofinterestitem.add(topicOfInterestsItem);
                            }


                            Constant.Log(TAG, "size" + topicsofinterestitem.size());

                        }


                    }
                });


    }
}
