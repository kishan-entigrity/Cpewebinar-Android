package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.entigrity.R;
import com.entigrity.databinding.ActivityCompanyProfileDetailsBinding;
import com.entigrity.fragments.AboutCompanyFragment;
import com.entigrity.fragments.CompanyDetailsFragment;
import com.entigrity.model.company_details.Company_details_model;
import com.entigrity.model.company_like.Company_Like_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompanyDetailsActivity extends AppCompatActivity {

    ActivityCompanyProfileDetailsBinding binding;
    public Context context;
    private static final String TAG = CompanyDetailsActivity.class.getName();
    private static CompanyDetailsActivity instance;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    public int companyid = 0, number_of_speaker = 0, number_of_webinar = 0, favorite_unfavorite_status = 0;
    public String name, website, contact_number, description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile_details);
        context = CompanyDetailsActivity.this;
        instance = CompanyDetailsActivity.this;
        mAPIService = ApiUtils.getAPIService();

        Intent intent = getIntent();
        if (intent != null) {
            companyid = intent.getIntExtra(getResources().getString(R.string.pass_company_id), 0);

            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetCompany_Details();
            } else {
                Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
            }


        }

        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.ivfavoritestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));

                if (Constant.isNetworkAvailable(context)) {
                    CompanyFavoriteStatus();
                } else {
                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                }

            }
        });


        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

    }

    private void GetCompany_Details() {


        mAPIService.GetCompanyDetails(String.valueOf(companyid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Company_details_model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(Company_details_model company_details_model) {

                        if (company_details_model.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            name = company_details_model.getPayload().getCompany().getName();
                            website = company_details_model.getPayload().getCompany().getWebsite();
                            contact_number = company_details_model.getPayload().getCompany().getContactNumber();


                            number_of_speaker = company_details_model.getPayload().getCompany().getNumberOfSpeaker();
                            number_of_webinar = company_details_model.getPayload().getCompany().getNumberOfWebinar();


                            description = company_details_model.getPayload().getCompany().getDescription();

                            favorite_unfavorite_status = company_details_model.getPayload().getCompany().getFavouriteUnfavoriteStatus();


                            if (!company_details_model.getPayload().getCompany().getLogo().equalsIgnoreCase("")) {
                                Picasso.with(context).load(company_details_model.getPayload().getCompany().getLogo())
                                        .placeholder(R.mipmap.placeholder)
                                        .into(binding.ivCompanylogo);
                            }

                            if (!company_details_model.getPayload().getCompany().getName().equalsIgnoreCase("")) {
                                binding.tvCompanyname.setText(company_details_model.getPayload().getCompany().getName());
                            }

                            if (company_details_model.getPayload().getCompany().getFavouriteUnfavoriteStatus() == 1) {
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite_hover);
                            } else {
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite);
                            }

                            if (!company_details_model.getPayload().getCompany().getState().equalsIgnoreCase("")) {
                                binding.tvState.setText(company_details_model.getPayload().getCompany().getState() + ", ");
                            }


                            if (!company_details_model.getPayload().getCompany().getCountry().equalsIgnoreCase("")) {
                                binding.tvCountry.setText(company_details_model.getPayload().getCompany().getCountry());
                            }


                            setupViewPager(binding.viewpager);
                            binding.tabs.setupWithViewPager(binding.viewpager);


                        } else {
                            if (company_details_model.getPayload().getAccessToken() != null && !company_details_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(context)) {
                                    AppSettings.set_login_token(context, company_details_model.getPayload().getAccessToken());
                                    GetCompany_Details();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                Constant.ShowPopUp(company_details_model.getMessage(), context);
                            }


                        }


                    }

                });


    }


    public void CompanyFavoriteStatus() {
        mAPIService.CompanyFavoriteStatus(String.valueOf(companyid), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), companyid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Company_Like_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(Company_Like_Model company_like_model) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        if (company_like_model.isSuccess()) {

                            if (favorite_unfavorite_status == 1) {
                                favorite_unfavorite_status = 0;
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite);

                            } else if (favorite_unfavorite_status == 0) {
                                favorite_unfavorite_status = 1;
                                binding.ivfavoritestatus.setImageResource(R.mipmap.profile_favorite_hover);

                            }


                            Constant.ShowPopUp(company_like_model.getMessage(), context);
                        } else {


                            if (company_like_model.getPayload().getAccessToken() != null && !company_like_model.getPayload().getAccessToken()
                                    .equalsIgnoreCase("")) {

                                if (Constant.isNetworkAvailable(context)) {
                                    AppSettings.set_login_token(context, company_like_model.getPayload().getAccessToken());
                                    CompanyFavoriteStatus();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                Constant.ShowPopUp(company_like_model.getMessage(), context);
                            }

                        }


                    }
                });

    }


    public static CompanyDetailsActivity getInstance() {
        return instance;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutCompanyFragment(), getResources().getString(R.string.about));
        adapter.addFragment(new CompanyDetailsFragment(), getResources().getString(R.string.company_details));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
