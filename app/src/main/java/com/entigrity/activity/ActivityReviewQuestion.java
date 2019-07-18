package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.ReviewQuestionAdapter;
import com.entigrity.databinding.ActivityReviewQuetionBinding;
import com.entigrity.model.SubmitReviewAnswer.SubmitAnswerModel;
import com.entigrity.model.review_question.ReviewQuestionsItem;
import com.entigrity.model.review_question.Review_Question;
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

import static com.entigrity.utility.Constant.arraylistselectedquestionreview;
import static com.entigrity.utility.Constant.arraylistselectedreviewanswerreview;

public class ActivityReviewQuestion extends AppCompatActivity {

    ActivityReviewQuetionBinding binding;
    public List<ReviewQuestionsItem> reviewquestion = new ArrayList<>();
    public List<Boolean> arrayboolean = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    private static final String TAG = ActivityReviewQuestion.class.getName();
    public Context context;
    public int webinar_id = 0;
    public ReviewQuestionAdapter adapter;
    private static ActivityReviewQuestion instance;
    public String webinar_type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_quetion);
        context = ActivityReviewQuestion.this;
        mAPIService = ApiUtilsNew.getAPIService();
        instance = ActivityReviewQuestion.this;

        Intent intent = getIntent();
        if (intent != null) {
            webinar_id = intent.getIntExtra(getResources().getString(R.string.pass_who_you_are_list_review_question), 0);
            webinar_type = intent.getStringExtra(getResources().getString(R.string.pass_webinar_type));
        }

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewReviewQuestion.setLayoutManager(linearLayoutManager);
        binding.recyclerviewReviewQuestion.addItemDecoration(new SimpleDividerItemDecoration(context));
        binding.recyclerviewReviewQuestion.setItemAnimator(new DefaultItemAnimator());

        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, WebinarDetailsActivity.class);
                i.putExtra(getResources().getString(R.string.pass_webinar_id), webinar_id);
                i.putExtra(getResources().getString(R.string.pass_webinar_type), webinar_type);
                startActivity(i);
                finish();
            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetReviewQuestion();
        } else {
            Snackbar.make(binding.recyclerviewReviewQuestion, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reviewquestion = android.text.TextUtils.join(",", arraylistselectedquestionreview);
                System.out.println(reviewquestion);

                String reviewanswer = android.text.TextUtils.join(",", arraylistselectedreviewanswerreview);
                System.out.println(reviewanswer);


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetSubmitAnswer(reviewquestion, reviewanswer);
                } else {
                    Snackbar.make(binding.recyclerviewReviewQuestion, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(context, WebinarDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.pass_webinar_id), webinar_id);
        i.putExtra(getResources().getString(R.string.pass_webinar_type), webinar_type);
        startActivity(i);
        finish();
    }

    private void GetSubmitAnswer(String reviewquestion, String reviewanswer) {

        mAPIService.SubmitReviewAnswer(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context), webinar_id
                , reviewquestion, reviewanswer).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SubmitAnswerModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.ivback, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(SubmitAnswerModel submitAnswerModel) {

                        if (submitAnswerModel.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            arraylistselectedquestionreview.clear();
                            arraylistselectedreviewanswerreview.clear();
                            finish();


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivback, submitAnswerModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                });

    }


    public void SubmitButtonVisible() {
        binding.relbottom.setVisibility(View.VISIBLE);
    }

    public void SubmitButtonInVisible() {
        binding.relbottom.setVisibility(View.GONE);
    }

    public static ActivityReviewQuestion getInstance() {
        return instance;

    }

    private void GetReviewQuestion() {
        arraylistselectedreviewanswerreview.clear();
        arraylistselectedquestionreview.clear();

        mAPIService.ReviewQuestion(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context), webinar_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Review_Question>() {
                    @Override
                    public void onCompleted() {
                        if (reviewquestion.size() > 0) {
                            adapter = new ReviewQuestionAdapter(context, reviewquestion, arrayboolean);
                            binding.recyclerviewReviewQuestion.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.ivback, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(Review_Question review_question) {

                        if (review_question.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            reviewquestion = review_question.getPayload().getReviewQuestions();


                            for (int i = 0; i < reviewquestion.size(); i++) {
                                if (reviewquestion.get(i).getA().getIsAnswer().equalsIgnoreCase("true")) {
                                    arraylistselectedreviewanswerreview.add("a");
                                    arraylistselectedquestionreview.add(reviewquestion.get(i).getId());
                                } else if (reviewquestion.get(i).getB().getIsAnswer().equalsIgnoreCase("true")) {
                                    arraylistselectedreviewanswerreview.add("b");
                                    arraylistselectedquestionreview.add(reviewquestion.get(i).getId());
                                } else if (reviewquestion.get(i).getC().getIsAnswer().equalsIgnoreCase("true")) {
                                    arraylistselectedreviewanswerreview.add("c");
                                    arraylistselectedquestionreview.add(reviewquestion.get(i).getId());
                                } else if (reviewquestion.get(i).getD().getIsAnswer().equalsIgnoreCase("true")) {
                                    arraylistselectedreviewanswerreview.add("d");
                                    arraylistselectedquestionreview.add(reviewquestion.get(i).getId());
                                }

                            }


                            for (int i = 0; i < reviewquestion.size(); i++) {
                                arrayboolean.add(false);
                            }


                            if (reviewquestion.size() > 0) {
                                binding.recyclerviewReviewQuestion.setVisibility(View.VISIBLE);
                                binding.tvNodatafound.setVisibility(View.GONE);
                            } else {
                                binding.tvNodatafound.setVisibility(View.VISIBLE);
                                binding.recyclerviewReviewQuestion.setVisibility(View.GONE);
                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivback, review_question.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }


                });


    }
}
