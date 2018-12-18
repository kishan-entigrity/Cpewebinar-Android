package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.activity.EditProfileActivity;
import com.entigrity.databinding.FragmentViewprofileBinding;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewProfileFragment extends Fragment {
    View view;
    public Context context;
    private APIService mAPIService;
    private FragmentViewprofileBinding binding;
    private static final String TAG = ViewProfileFragment.class.getName();
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_viewprofile, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetProfile();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        return view = binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Navigate_EditProfile();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Navigate_EditProfile() {
        Intent i = new Intent(context, EditProfileActivity.class);
        startActivity(i);
    }


    public void GetProfile() {

        mAPIService.GetProfile(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewProfileModel>() {
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
                    public void onNext(ViewProfileModel viewProfileModel) {

                        if (viewProfileModel.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (viewProfileModel.getPayload().getData().getFirstName() != null
                                    && !viewProfileModel.getPayload().getData().getFirstName().equalsIgnoreCase("")) {
                                binding.tvFirstname.setText(viewProfileModel.getPayload().getData().getFirstName());

                            }


                            if (viewProfileModel.getPayload().getData().getLastName() != null
                                    && !viewProfileModel.getPayload().getData().getLastName().equalsIgnoreCase("")) {
                                binding.tvLastname.setText(viewProfileModel.getPayload().getData().getLastName());
                            }

                            if (viewProfileModel.getPayload().getData().getEmail() != null
                                    && !viewProfileModel.getPayload().getData().getEmail().equalsIgnoreCase("")) {
                                binding.tvEmailname.setText(viewProfileModel.getPayload().getData().getEmail());
                            }

                            if (viewProfileModel.getPayload().getData().getFirmName() != null
                                    && !viewProfileModel.getPayload().getData().getFirmName().equalsIgnoreCase("")) {
                                binding.tvFirmname.setText(viewProfileModel.getPayload().getData().getFirmName());
                            }


                            if (viewProfileModel.getPayload().getData().getContactNo() != null
                                    && !viewProfileModel.getPayload().getData().getContactNo().equalsIgnoreCase("")) {
                                binding.tvMobilenumber.setText(viewProfileModel.getPayload().getData().getContactNo());
                            }


                            if (viewProfileModel.getPayload().getData().getCountry() != null
                                    && !viewProfileModel.getPayload().getData().getCountry().equalsIgnoreCase("")) {
                                binding.tvContry.setText(viewProfileModel.getPayload().getData().getCountry());
                            }
                            if (viewProfileModel.getPayload().getData().getState() != null
                                    && !viewProfileModel.getPayload().getData().getState().equalsIgnoreCase("")) {
                                binding.tvState.setText(viewProfileModel.getPayload().getData().getState());
                            }

                            if (viewProfileModel.getPayload().getData().getCity() != null
                                    && !viewProfileModel.getPayload().getData().getCity().equalsIgnoreCase("")) {
                                binding.tvCity.setText(viewProfileModel.getPayload().getData().getCity());
                            }


                            if (viewProfileModel.getPayload().getData().getZipcode() != null
                                    && !viewProfileModel.getPayload().getData().getZipcode().equalsIgnoreCase("")) {
                                binding.tvZipcode.setText(viewProfileModel.getPayload().getData().getZipcode());
                            }


                            if (viewProfileModel.getPayload().getData().getUserType() != null
                                    && !viewProfileModel.getPayload().getData().getUserType().equalsIgnoreCase("")) {
                                binding.tvWhoYouAre.setText(viewProfileModel.getPayload().getData().getUserType());
                            }

                        } else {
                            if (viewProfileModel.getPayload().getAccess_token() != null && !viewProfileModel.getPayload().getAccess_token().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, viewProfileModel.getPayload().getAccess_token());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetProfile();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }


                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(viewProfileModel.getMessage(), context);
                            }


                        }


                    }
                });

    }


}
