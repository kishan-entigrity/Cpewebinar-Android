package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.adapter.SignUpRecyclerViewSectionAdapter;
import com.entigrity.model.topicsofinterestn.TopicOfInterestsItem;
import com.entigrity.model.topicsofinterestn.Topicsofinterest;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TopicsofInterestSignUpActivity extends AppCompatActivity {
    public Context context;
    private APIService mAPIService;
    private static final String TAG = TopicsofInterestSignUpActivity.class.getName();
    ProgressDialog progressDialog;
    ImageView ivback;
    RecyclerView rvtopics_of_interest;
    TextView tv_submit;
    private ArrayList<TopicOfInterestsItem> topicsofinterestitem = new ArrayList<TopicOfInterestsItem>();
    public ArrayList<Integer> arraylistselected = new ArrayList<Integer>();
    LinearLayoutManager linearLayoutManager;
    SignUpRecyclerViewSectionAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.entigrity.R.layout.activity_signup_topicsofinterest);
        mAPIService = ApiUtilsNew.getAPIService();
        context = TopicsofInterestSignUpActivity.this;

        Init();
        Onclick();

        if (adapter.arraylistselectedtopicsofinterest.size() > 0) {
            arraylistselected = adapter.arraylistselectedtopicsofinterest;
            Constant.Log(TAG, "store_id" + arraylistselected.size());
        }


        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvtopics_of_interest.setLayoutManager(linearLayoutManager);
        rvtopics_of_interest.addItemDecoration(new SimpleDividerItemDecoration(context));

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(com.entigrity.R.string.progrees_msg));
            GetTopicsofInterest();
        } else {
            Snackbar.make(tv_submit, getResources().getString(com.entigrity.R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }

    }

    private void Onclick() {
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                }


            }
        });


    }

    private void Init() {
        ivback = (ImageView) findViewById(com.entigrity.R.id.ivback);
        rvtopics_of_interest = (RecyclerView) findViewById(com.entigrity.R.id.rvtopics_of_interest);
        tv_submit = (TextView) findViewById(com.entigrity.R.id.tv_submit);
    }

    public void GetTopicsofInterest() {
        mAPIService.GetTopicsofInterestsSignUp(getResources().getString(com.entigrity.R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Topicsofinterest>() {
                    @Override
                    public void onCompleted() {
                        if (topicsofinterestitem.size() > 0) {
                            adapter = new SignUpRecyclerViewSectionAdapter(context, topicsofinterestitem);
                            rvtopics_of_interest.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(tv_submit, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Topicsofinterest topicsofinterest) {

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


                    }
                });


    }

}
