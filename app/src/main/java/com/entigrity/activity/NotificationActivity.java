package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.R;
import com.entigrity.adapter.NotificationAdapter;
import com.entigrity.databinding.ActivityNotificationBinding;
import com.entigrity.model.notification.NotificationListItem;
import com.entigrity.model.notification.NotificationModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding binding;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public Context context;
    private static final String TAG = NotificationActivity.class.getName();
    LinearLayoutManager linearLayoutManager;
    private NotificationAdapter adapter;
    private List<NotificationListItem> mListnotificationlist = new ArrayList<NotificationListItem>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        context = NotificationActivity.this;
        mAPIService = ApiUtilsNew.getAPIService();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvNotificationlist.setLayoutManager(linearLayoutManager);
        binding.rvNotificationlist.addItemDecoration(new SimpleDividerItemDecoration(context));
        binding.rvNotificationlist.setItemAnimator(new DefaultItemAnimator());


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetNotificationList();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


    }

    private void GetNotificationList() {

        mAPIService.GetNotificationModel(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NotificationModel>() {
                    @Override
                    public void onCompleted() {
                        if (mListnotificationlist.size() > 0) {
                            adapter = new NotificationAdapter(context, mListnotificationlist);
                            binding.rvNotificationlist.setAdapter(adapter);
                        }


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
                    public void onNext(NotificationModel notificationModel) {

                        if (notificationModel.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            mListnotificationlist = notificationModel.getPayload().getNotificationList();

                            Constant.Log(TAG, "size" + mListnotificationlist.size());

                            if (mListnotificationlist.size() > 0) {
                                binding.rvNotificationlist.setVisibility(View.VISIBLE);
                                binding.tvNodatafound.setVisibility(View.GONE);
                            } else {
                                binding.tvNodatafound.setVisibility(View.VISIBLE);
                                binding.rvNotificationlist.setVisibility(View.GONE);
                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivback, notificationModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }


                });

    }
}
