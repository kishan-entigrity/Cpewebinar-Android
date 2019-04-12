package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.activity.TopicsOfInterestActivity;
import com.entigrity.databinding.FragmentMyfavoritescreenBinding;
import com.entigrity.model.favorites_count.Favorite_Count_Model;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;
import com.squareup.picasso.Picasso;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyFavoriteScreenFragment extends Fragment {
    View view;
    public Context context;
    private APIService mAPIService;
    ProgressDialog progressDialog;
    FragmentMyfavoritescreenBinding binding;
    private static final String TAG = MyFavoriteScreenFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myfavoritescreen, null, false);
        context = getActivity();
        mAPIService = ApiUtilsNew.getAPIService();

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ConfirmationPopup();
                    return true;
                }
                return false;
            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetFavoriteDetails();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }

        binding.lvTopicsofinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TopicsOfInterestActivity.class);
                startActivity(i);

            }
        });

        binding.lvWebinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().SetFavoriteTab();

            }
        });


        return view = binding.getRoot();

    }


    private void GetFavoriteDetails() {

        mAPIService.GetFavoriteCountModel(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Favorite_Count_Model>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.ivbanner, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Favorite_Count_Model favorite_count_model) {

                        if (favorite_count_model.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            if (!AppSettings.get_profile_picture(context).equalsIgnoreCase("")
                                    && AppSettings.get_profile_picture(context) != null) {

                                Picasso.with(context).load(AppSettings.get_profile_picture(context))
                                        .placeholder(R.mipmap.placeholder)
                                        .into(binding.ivProfilepicture);

                                Constant.Log(TAG, "profile picture" + AppSettings.get_profile_picture(context));


                            }


                            if (!AppSettings.get_profile_username(context).equalsIgnoreCase("")
                                    && AppSettings.get_profile_username(context) != null) {
                                binding.profilename.setText("" + AppSettings.get_profile_username(context));


                            }


                            if (!favorite_count_model.getPayload().getBannerImage().equalsIgnoreCase("")) {
                                Picasso.with(context).load(favorite_count_model.getPayload().getBannerImage())
                                        .placeholder(R.mipmap.placeholder)
                                        .into(binding.ivbanner);
                            }

                            if (favorite_count_model.getPayload().getWebinarCount() == 0) {
                                binding.webinarcount.setText("" + "(" + 0 + ")");
                            } else {
                                binding.webinarcount.setText("" + "( " + favorite_count_model.getPayload().getWebinarCount() + " )");
                            }


                            if (favorite_count_model.getPayload().getTopicsOfInterestCount() == 0) {
                                binding.webinartopicsofinterest.setText("" + "(" + 0 + ")");
                            } else {
                                binding.webinartopicsofinterest.setText("" + "( " + favorite_count_model.getPayload().getTopicsOfInterestCount() + " )");
                            }

                            if (favorite_count_model.getPayload().getCompanyCount() == 0) {
                                binding.companycount.setText("" + "(" + 0 + ")");
                            } else {
                                binding.companycount.setText("" + "( " + favorite_count_model.getPayload().getCompanyCount() + " )");
                            }


                            if (favorite_count_model.getPayload().getSpeakerCount() == 0) {
                                binding.speakercount.setText("" + "(" + 0 + ")");
                            } else {
                                binding.companycount.setText("" + "( " + favorite_count_model.getPayload().getSpeakerCount() + " )");
                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.ivbanner, favorite_count_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }


                });


    }

    public void ConfirmationPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        // alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.exit_validation));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                dialog.cancel();
                getActivity().finish();


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }
}
