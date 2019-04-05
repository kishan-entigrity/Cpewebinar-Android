package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.FragmentFavoritesBinding;
import com.entigrity.model.companyfavorites.Company_Favorite;
import com.entigrity.model.companyfavorites.MyFavoriteCompanyItem;
import com.entigrity.model.instructorfavorites.Instructor_Favorite;
import com.entigrity.model.instructorfavorites.MyFavoriteSpeakerItem;
import com.entigrity.model.webinarfavorites.MyFavoriteWebinarItem;
import com.entigrity.model.webinarfavorites.Webinar_Favorite;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FavoritesFragment extends Fragment {
    View view;
    public Context context;
    private static final String TAG = FavoritesFragment.class.getName();
    FragmentFavoritesBinding binding;
    ProgressDialog progressDialog;

    private static FavoritesFragment instance;


    public List<MyFavoriteSpeakerItem> mListfavoritesSpeaker = new ArrayList<MyFavoriteSpeakerItem>();
    public List<MyFavoriteCompanyItem> mListfavoritesCompany = new ArrayList<MyFavoriteCompanyItem>();
    public List<MyFavoriteWebinarItem> mListfavoriteswebinar = new ArrayList<MyFavoriteWebinarItem>();


    private APIService mAPIService;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, null, false);
        context = getActivity();
        instance = FavoritesFragment.this;
        mAPIService = ApiUtils.getAPIService();


        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {


                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetSpeakerFavoritesList();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        return view = binding.getRoot();
    }


    public static FavoritesFragment getInstance() {
        return instance;

    }


    public void GetSpeakerFavoritesList() {

        mAPIService.InstructorFavoriteList(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Instructor_Favorite>() {
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
                    public void onNext(Instructor_Favorite instructor_favorite) {

                        if (instructor_favorite.isSuccess()) {


                            mListfavoritesSpeaker = instructor_favorite.getPayload().getMyFavoriteSpeaker();


                            if (Constant.isNetworkAvailable(context)) {
                                GetCompanyFavoritesList();
                            } else {
                                Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                            }


                        } else {

                            if (instructor_favorite.getPayload().getAccessToken() != null && !instructor_favorite.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, instructor_favorite.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetSpeakerFavoritesList();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(instructor_favorite.getMessage(), context);

                            }

                        }


                    }


                });

    }


    public void GetCompanyFavoritesList() {

        mAPIService.CompanyFavoriteList(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Company_Favorite>() {
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
                    public void onNext(Company_Favorite company_favorite) {

                        if (company_favorite.isSuccess()) {

                            mListfavoritesCompany = company_favorite.getPayload().getMyFavoriteCompany();


                            if (Constant.isNetworkAvailable(context)) {
                                GetWebinarFavoritesList();
                            } else {
                                Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                            }


                        } else {
                            if (company_favorite.getPayload().getAccessToken() != null && !company_favorite.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, company_favorite.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetCompanyFavoritesList();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(company_favorite.getMessage(), context);

                            }


                        }


                    }


                });


    }


    public void GetWebinarFavoritesList() {

        mAPIService.WebinarFavoriteList(getResources().getString(R.string.bearer) +
                AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Favorite>() {
                    @Override
                    public void onCompleted() {
                        setupViewPager(binding.viewpagerFavorite);
                        binding.tabsFavorite.setupWithViewPager(binding.viewpagerFavorite);
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
                    public void onNext(Webinar_Favorite webinar_favorite) {

                        if (webinar_favorite.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            mListfavoriteswebinar = webinar_favorite.getPayload().getMyFavoriteWebinar();


                        } else {

                            if (webinar_favorite.getPayload().getAccessToken() != null && !webinar_favorite.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, webinar_favorite.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetWebinarFavoritesList();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(webinar_favorite.getMessage(), context);

                            }

                        }


                    }


                });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new InstructorFavoritesFragment(), getActivity().getResources().getString(R.string.str_speaker));
        adapter.addFragment(new CompanyFavoritesFragment(), getActivity().getResources().getString(R.string.str_company));
        adapter.addFragment(new WebinarFavoritesFragment(), getActivity().getResources().getString(R.string.str_webinars));
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
