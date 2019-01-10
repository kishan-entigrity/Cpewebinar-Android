package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.entigrity.view.SimpleDividerItemDecoration;
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
    // private List<SpeakersItem> mListinstructorlist_filter = new ArrayList<SpeakersItem>();
    public InstructorAdapter instructorAdapter;
    public SearchView searchView;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructor, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);

       /* LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewInstructor.setLayoutManager(layoutManager);
        binding.recyclerviewInstructor.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));*/


        binding.recyclerviewInstructor.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        int spacing = (int) getResources().getDimension(R.dimen._2sdp); // 50px
        boolean includeEdge = false;
        binding.recyclerviewInstructor.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));

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
        //inflater.inflate(R.menu.searchview, menu);
        getActivity().getMenuInflater().inflate(R.menu.searchview, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                instructorAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                instructorAdapter.getFilter().filter(query);
                return true;
            }
        });*/



       /* final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //some operation
                    searchView.onActionViewCollapsed();
                    return false;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //some operation
                }
            });

            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));


            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                    instructorAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // use this method for auto complete search process
                    instructorAdapter.getFilter().filter(query);
                    return true;
                }
            });
*/


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


    public void GetInstructor() {

        mAPIService.GetInstructor(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InstructorModel>() {
                    @Override
                    public void onCompleted() {
                        if (mListinstructorlist.size() > 0) {
                            instructorAdapter = new InstructorAdapter(context, mListinstructorlist);
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
                            // mListinstructorlist_filter = instructorModel.getPayload().getSpeakers();


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
    public boolean onQueryTextChange(String s) {
        return true;
    }
}



