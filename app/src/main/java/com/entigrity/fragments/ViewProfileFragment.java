package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.EditProfileActivity;
import com.entigrity.databinding.FragmentViewprofileBinding;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;

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
    public String firstname = "", lastname = "", email = "", firmname = "", mobilenumber = "", zipcode = "";
    public int country_id = 0, state_id = 0, city_id = 0, whoyouare = 0;

    public String State, City;
    public ArrayList<Integer> arraylistselectedtopicsofinterest = new ArrayList<Integer>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_viewprofile, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetProfile();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    // ConfirmationPopup();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();


                    return true;
                }
                return false;
            }
        });


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
        i.putExtra(getResources().getString(R.string.pass_fname), firstname);
        i.putExtra(getResources().getString(R.string.pass_lname), lastname);
        i.putExtra(getResources().getString(R.string.pass_email), email);
        i.putExtra(getResources().getString(R.string.pass_firm_name), firmname);
        i.putExtra(getResources().getString(R.string.pass_mobile_number), mobilenumber);
        i.putExtra(getResources().getString(R.string.pass_country), country_id);
        i.putExtra(getResources().getString(R.string.pass_state), state_id);
        i.putExtra(getResources().getString(R.string.pass_city), city_id);
        i.putExtra(getResources().getString(R.string.pass_state_text), State);
        i.putExtra(getResources().getString(R.string.pass_city_text), City);
        i.putExtra(getResources().getString(R.string.pass_zipcode), zipcode);
        i.putExtra(getResources().getString(R.string.pass_who_you_are), whoyouare);
        i.putExtra(getResources().getString(R.string.pass_topics_of_interesr), arraylistselectedtopicsofinterest);
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
                                firstname = viewProfileModel.getPayload().getData().getFirstName();

                            }


                            if (viewProfileModel.getPayload().getData().getLastName() != null
                                    && !viewProfileModel.getPayload().getData().getLastName().equalsIgnoreCase("")) {
                                binding.tvLastname.setText(viewProfileModel.getPayload().getData().getLastName());
                                lastname = viewProfileModel.getPayload().getData().getLastName();

                            }

                            if (viewProfileModel.getPayload().getData().getEmail() != null
                                    && !viewProfileModel.getPayload().getData().getEmail().equalsIgnoreCase("")) {
                                binding.tvEmailname.setText(viewProfileModel.getPayload().getData().getEmail());
                                email = viewProfileModel.getPayload().getData().getEmail();
                            }

                            if (viewProfileModel.getPayload().getData().getFirmName() != null
                                    && !viewProfileModel.getPayload().getData().getFirmName().equalsIgnoreCase("")) {
                                binding.tvFirmname.setText(viewProfileModel.getPayload().getData().getFirmName());
                                firmname = viewProfileModel.getPayload().getData().getFirmName();
                            }


                            if (viewProfileModel.getPayload().getData().getContactNo() != null
                                    && !viewProfileModel.getPayload().getData().getContactNo().equalsIgnoreCase("")) {
                                binding.tvMobilenumber.setText(viewProfileModel.getPayload().getData().getContactNo());
                                mobilenumber = viewProfileModel.getPayload().getData().getContactNo();
                            }


                            if (viewProfileModel.getPayload().getData().getCountry() != null
                                    && !viewProfileModel.getPayload().getData().getCountry().equalsIgnoreCase("")) {
                                binding.tvContry.setText(viewProfileModel.getPayload().getData().getCountry());
                                country_id = Integer.parseInt(viewProfileModel.getPayload().getData().getCountryId());
                            }
                            if (viewProfileModel.getPayload().getData().getState() != null
                                    && !viewProfileModel.getPayload().getData().getState().equalsIgnoreCase("")) {
                                binding.tvState.setText(viewProfileModel.getPayload().getData().getState());
                                state_id = Integer.parseInt(viewProfileModel.getPayload().getData().getStateId());
                                State = viewProfileModel.getPayload().getData().getState();
                            }

                            if (viewProfileModel.getPayload().getData().getCity() != null
                                    && !viewProfileModel.getPayload().getData().getCity().equalsIgnoreCase("")) {
                                binding.tvCity.setText(viewProfileModel.getPayload().getData().getCity());
                                city_id = Integer.parseInt(viewProfileModel.getPayload().getData().getCityId());
                                City = viewProfileModel.getPayload().getData().getCity();
                            }


                            if (viewProfileModel.getPayload().getData().getTags().size() > 0) {
                                for (int i = 0; i < viewProfileModel.getPayload().getData().getTags().size(); i++) {
                                    arraylistselectedtopicsofinterest.add(viewProfileModel.getPayload().getData().getTags().get(i).getId());
                                }
                            }
                            if (viewProfileModel.getPayload().getData().getZipcode() != null
                                    && !viewProfileModel.getPayload().getData().getZipcode().equalsIgnoreCase("")) {
                                binding.tvZipcode.setText(viewProfileModel.getPayload().getData().getZipcode());
                                zipcode = viewProfileModel.getPayload().getData().getZipcode();
                            }


                            if (viewProfileModel.getPayload().getData().getUserType() != null
                                    && !viewProfileModel.getPayload().getData().getUserType().equalsIgnoreCase("")) {
                                binding.tvWhoYouAre.setText(viewProfileModel.getPayload().getData().getUserType());
                                whoyouare = viewProfileModel.getPayload().getData().getUserTypeId();
                            }

                        } else {
                            if (viewProfileModel.getPayload().getAccessToken() != null && !viewProfileModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, viewProfileModel.getPayload().getAccessToken());

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
