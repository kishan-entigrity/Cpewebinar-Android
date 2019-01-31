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
import com.entigrity.activity.CompanyDetailsActivity;
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.adapter.CompanyAdapter;
import com.entigrity.databinding.FragmentCompanyBinding;
import com.entigrity.model.company.CompanyItem;
import com.entigrity.model.company.CompanyModel;
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

public class CompanyFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentCompanyBinding binding;
    public Context context;
    private APIService mAPIService;
    private static final String TAG = CompanyFragment.class.getName();
    ProgressDialog progressDialog;
    private List<CompanyItem> mListcompanylist = new ArrayList<CompanyItem>();
    public CompanyAdapter companyAdapter;
    CompanyFragment companyFragment;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        companyFragment = new CompanyFragment();

        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);

        binding.recyclerviewCompany.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        int spacing = (int) getResources().getDimension(R.dimen._5sdp); // 50px
        boolean includeEdge = true;
        binding.recyclerviewCompany.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    //  ConfirmationPopup();

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
            GetCompany();
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
                companyAdapter.setSearchResult(mListcompanylist);
                return true; // Return true to collapse action view

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GetCompany() {

        mAPIService.GetCompany(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyModel>() {
                    @Override
                    public void onCompleted() {
                        if (mListcompanylist.size() > 0) {
                            companyAdapter = new CompanyAdapter(context, mListcompanylist);
                            binding.recyclerviewCompany.setAdapter(companyAdapter);
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
                    public void onNext(CompanyModel companyModel) {

                        if (companyModel.isSuccess() == true) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            mListcompanylist = companyModel.getPayload().getCompany();


                        } else {

                            if (companyModel.getPayload().getAccessToken() != null && !companyModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, companyModel.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetCompany();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }

                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Constant.ShowPopUp(companyModel.getMessage(), context);

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
        final List<CompanyItem> filteredModelList = filter(mListcompanylist, newText);
        companyAdapter.setSearchResult(filteredModelList);
        return true;
    }

    private List<CompanyItem> filter(List<CompanyItem> models, String query) {
        query = query.toLowerCase();
        final List<CompanyItem> filteredModelList = new ArrayList<>();
        for (CompanyItem model : models) {
            final String companyname = model.getName().toLowerCase();

            if (companyname.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
