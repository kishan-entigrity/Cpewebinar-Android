package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.RecyclerViewSectionAdapter;
import com.entigrity.databinding.ActivityTopicsofinterestBinding;
import com.entigrity.model.savetopicsofinterest.SaveTopicsInterest;
import com.entigrity.model.topicsofinterestn.TopicOfInterestsItem;
import com.entigrity.model.topicsofinterestn.Topicsofinterest;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

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
    public ArrayList<Integer> arraylistselected = new ArrayList<Integer>();
    LinearLayoutManager linearLayoutManager;
    RecyclerViewSectionAdapter adapter;
    private String fromscreen = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topicsofinterest);
        mAPIService = ApiUtilsNew.getAPIService();
        context = TopicsOfInterestActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            fromscreen = intent.getStringExtra(getResources().getString(R.string.str_get_key_screen_key));

        }


        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvtopicsOfInterest.setLayoutManager(linearLayoutManager);
        binding.rvtopicsOfInterest.addItemDecoration(new SimpleDividerItemDecoration(context));

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetTopicsofInterest();
        } else {
            Snackbar.make(binding.tvSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (adapter.arraylistselectedtopicsofinterest.size() > 0) {
                    arraylistselected = adapter.arraylistselectedtopicsofinterest;
                    Constant.Log(TAG, "store_id" + arraylistselected.size());
                }


                if (arraylistselected.size() > 0) {

                    StringBuilder commaSepValueBuilder = new StringBuilder();

                    //Looping through the list
                    for (int i = 0; i < arraylistselected.size(); i++) {
                        //append the value into the builder
                        commaSepValueBuilder.append(arraylistselected.get(i));

                        //if the value is not the last element of the list
                        //then append the comma(,) as well
                        if (i != arraylistselected.size() - 1) {
                            commaSepValueBuilder.append(", ");
                        }
                    }
                    //System.out.println(commaSepValueBuilder.toString());
                    String selectedlist = commaSepValueBuilder.toString();

                    System.out.println(selectedlist);


                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        SaveTopicsofInterest(selectedlist);
                    } else {
                        Snackbar.make(binding.tvSubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(binding.tvSubmit, getResources().getString(R.string.val_topics), Snackbar.LENGTH_SHORT).show();
                }


            }
        });


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void GetTopicsofInterest() {
        mAPIService.GetTopicsofInterests(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Topicsofinterest>() {
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
                    public void onNext(Topicsofinterest topicsofinterest) {


                        if (topicsofinterest.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            if (topicsofinterest.getPayload().getTopicOfInterests().size() > 0) {

                                for (int i = 0; i < topicsofinterest.getPayload().getTopicOfInterests().size(); i++) {
                                    TopicOfInterestsItem topicOfInterestsItem = new TopicOfInterestsItem();
                                    topicOfInterestsItem.setName(topicsofinterest.getPayload().getTopicOfInterests().get(i).getName());
                                    topicOfInterestsItem.setId(topicsofinterest.getPayload().getTopicOfInterests().get(i).getId());
                                    if (topicsofinterest.getPayload().getTopicOfInterests().get(i).getTags() != null) {
                                        topicOfInterestsItem.setTags(topicsofinterest.getPayload().getTopicOfInterests().get(i).getTags());
                                    }

                                    topicsofinterestitem.add(topicOfInterestsItem);
                                }


                                //Constant.Log(TAG, "size" + topicsofinterestitem.size());

                            }


                        } else {
                            Snackbar.make(binding.tvSubmit, topicsofinterest.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }


    public void SaveTopicsofInterest(String selectedlist) {
        mAPIService.PostTopicsOfInterest(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), selectedlist).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveTopicsInterest>() {
                    @Override
                    public void onCompleted() {


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
                    public void onNext(SaveTopicsInterest saveTopicsInterest) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (saveTopicsInterest.isSuccess() == true) {
                            Snackbar.make(binding.tvSubmit, saveTopicsInterest.getMessage(), Snackbar.LENGTH_SHORT).show();

                            if (fromscreen.equalsIgnoreCase(getResources().getString(R.string.from_home_screen))) {
                                Intent i = new Intent(context, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(context, MainActivity.class);
                                startActivity(i);
                                finish();
                            }


                        } else {
                            Snackbar.make(binding.tvSubmit, saveTopicsInterest.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
    }


}
