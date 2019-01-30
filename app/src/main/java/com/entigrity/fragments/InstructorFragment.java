package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.InstructorAdapter;
import com.entigrity.databinding.FragmentInstructorBinding;
import com.entigrity.model.instructor.InstructorModel;
import com.entigrity.model.instructor.SpeakersItem;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.GridSpacingItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InstructorFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentInstructorBinding binding;
    public Context context;
    private APIService mAPIService;
    private static final String TAG = InstructorFragment.class.getName();
    ProgressDialog progressDialog;
    private List<SpeakersItem> mListinstructorlist = new ArrayList<SpeakersItem>();
    public InstructorAdapter instructorAdapter;
    View view;
    InstructorFragment instructorFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructor, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);
        instructorFragment = new InstructorFragment();


        binding.recyclerviewInstructor.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        int spacing = (int) getResources().getDimension(R.dimen._5sdp); // 50px
        boolean includeEdge = true;
        binding.recyclerviewInstructor.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));

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
            GetInstructor();
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
        inflater.inflate(R.menu.searchview, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                instructorAdapter.setSearchResult(mListinstructorlist);
                return true; // Return true to collapse action view

            }
        });


    }


    public void GetInstructor() {

        mAPIService.GetInstructor(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InstructorModel>() {
                    @Override
                    public void onCompleted() {
                        if (mListinstructorlist.size() > 0) {
                            instructorAdapter = new InstructorAdapter(context, mListinstructorlist, instructorFragment);
                            binding.recyclerviewInstructor.setAdapter(instructorAdapter);
                        }


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
                    public void onNext(InstructorModel instructorModel) {

                        if (instructorModel.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            mListinstructorlist = instructorModel.getPayload().getSpeakers();


                            //  Log.e(TAG, AppSettings.get_login_token(context));


                        } else {

                            if (instructorModel.getPayload().getAccessToken() != null && !instructorModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, instructorModel.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetInstructor();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(instructorModel.getMessage(), context);

                            }


                        }


                    }


                });
    }




    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<SpeakersItem> filteredModelList = filter(mListinstructorlist, newText);
        instructorAdapter.setSearchResult(filteredModelList);
        return true;
    }


    private List<SpeakersItem> filter(List<SpeakersItem> models, String query) {
        query = query.toLowerCase();
        final List<SpeakersItem> filteredModelList = new ArrayList<>();
        for (SpeakersItem model : models) {
            final String instructorname = model.getName().toLowerCase();
            final String companyname = model.getCompany().toLowerCase();
            if (instructorname.contains(query) || companyname.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}



