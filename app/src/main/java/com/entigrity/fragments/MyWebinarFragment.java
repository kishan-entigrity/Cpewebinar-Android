package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.HomeMyWebinarAdapter;
import com.entigrity.databinding.FragmentMywebinarBinding;
import com.entigrity.model.homewebinarlist.WebinarItem;
import com.entigrity.model.homewebinarlist.Webinar_Home;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyWebinarFragment extends Fragment {

    View view;
    private FragmentMywebinarBinding binding;
    private APIService mAPIService;
    HomeMyWebinarAdapter adapter;
    private static final String TAG = MyWebinarFragment.class.getName();
    public Context context;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private List<WebinarItem> arrHomeMyWebinarlist = new ArrayList<WebinarItem>();
    private int pagenumber = 1;
    private String webinartypemywebinar = "";
    private List<Boolean> arrsavebooleanstateMyWebinar = new ArrayList();
    private List<String> arraysavefilterMyWebinar = new ArrayList<String>();
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public int total_record = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mywebinar, null, false);
        context = getActivity();
        mAPIService = ApiUtils.getAPIService();

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvhomewebinar.setLayoutManager(linearLayoutManager);
        binding.rvhomewebinar.addItemDecoration(new SimpleDividerItemDecoration(context));
        binding.rvhomewebinar.setItemAnimator(new DefaultItemAnimator());


        arrsavebooleanstateMyWebinar.add(0, false);
        arrsavebooleanstateMyWebinar.add(1, false);
        arrsavebooleanstateMyWebinar.add(2, false);
        arrsavebooleanstateMyWebinar.add(3, false);


        arraysavefilterMyWebinar.add(0, "");
        arraysavefilterMyWebinar.add(1, "");
        arraysavefilterMyWebinar.add(2, "");
        arraysavefilterMyWebinar.add(3, "");


        binding.rvhomewebinar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (total_record >= 10) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                binding.progressBar.setVisibility(View.VISIBLE);
                                loading = false;
                                pagenumber = pagenumber + 1;
                                Log.v("...", "Last Item Wow !");
                                //Do pagination.. i.e. fetch new data
                                loadNextPage();
                            }
                        }

                    }
                }
            }
        });


        if (MainActivity.getInstance().setselectedtab == 1) {
            MainActivity.getInstance().setselectedtab = 0;

            pagenumber = 1;
            loading = true;


            if (arrsavebooleanstateMyWebinar.get(3) == false) {
                arrsavebooleanstateMyWebinar.set(3, true);
                arraysavefilterMyWebinar.set(3, getResources().getString(R.string.str_filter_favourite));
                webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                        .replace(" ", "");
                binding.btnFavorite.setBackgroundResource(R.drawable.col_four_bg_hover);
            } else {
                arrsavebooleanstateMyWebinar.set(3, false);
                arraysavefilterMyWebinar.set(3, "");
                if (arraysavefilterMyWebinar.get(0).equalsIgnoreCase("") &&
                        arraysavefilterMyWebinar.get(1).equalsIgnoreCase("") &&
                        arraysavefilterMyWebinar.get(2).equalsIgnoreCase("")
                        && arraysavefilterMyWebinar.get(3).equalsIgnoreCase("")) {
                    webinartypemywebinar = "";
                } else {
                    webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                }

                binding.btnFavorite.setBackgroundResource(R.drawable.col_four_bg);
            }


            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetMyWebinarList(pagenumber, webinartypemywebinar);
            } else {
                Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

            }


        } else {
            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetMyWebinarList(pagenumber, webinartypemywebinar);
            } else {
                Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

            }
        }


        binding.swipeRefreshLayouthomemywebinar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


        binding.btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagenumber = 1;
                loading = true;

                if (arrsavebooleanstateMyWebinar.get(0) == false) {
                    arrsavebooleanstateMyWebinar.set(0, true);
                    arraysavefilterMyWebinar.set(0, getResources().getString(R.string.str_filter_live));
                    webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                    binding.btnLive.setBackgroundResource(R.drawable.col_four_bg_hover);
                } else {
                    arrsavebooleanstateMyWebinar.set(0, false);
                    arraysavefilterMyWebinar.set(0, "");


                    if (arraysavefilterMyWebinar.get(0).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(1).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(2).equalsIgnoreCase("") && arraysavefilterMyWebinar.get(3).equalsIgnoreCase("")) {
                        webinartypemywebinar = "";
                    } else {
                        webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");
                    }


                    binding.btnLive.setBackgroundResource(R.drawable.col_four_bg);
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetMyWebinarList(pagenumber, webinartypemywebinar);
                } else {
                    Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }


            }
        });

        binding.btnSelfstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagenumber = 1;
                loading = true;

                if (arrsavebooleanstateMyWebinar.get(1) == false) {
                    arrsavebooleanstateMyWebinar.set(1, true);
                    arraysavefilterMyWebinar.set(1, getResources().getString(R.string.str_filter_selfstudy));
                    webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_four_bg_hover);
                } else {
                    arrsavebooleanstateMyWebinar.set(1, false);
                    arraysavefilterMyWebinar.set(1, "");
                    if (arraysavefilterMyWebinar.get(0).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(1).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(2).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(3).equalsIgnoreCase("")) {
                        webinartypemywebinar = "";


                    } else {
                        webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");

                    }


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_four_bg);
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetMyWebinarList(pagenumber, webinartypemywebinar);
                } else {
                    Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }


            }
        });


        binding.btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagenumber = 1;
                loading = true;


                if (arrsavebooleanstateMyWebinar.get(2) == false) {
                    arrsavebooleanstateMyWebinar.set(2, true);
                    arraysavefilterMyWebinar.set(2, getResources().getString(R.string.str_filter_archieve));
                    webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                    binding.btnArchive.setBackgroundResource(R.drawable.col_four_bg_hover);
                } else {
                    arrsavebooleanstateMyWebinar.set(2, false);
                    arraysavefilterMyWebinar.set(2, "");
                    if (arraysavefilterMyWebinar.get(0).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(1).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(2).equalsIgnoreCase("")
                            && arraysavefilterMyWebinar.get(3).equalsIgnoreCase("")) {
                        webinartypemywebinar = "";
                    } else {
                        webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");
                    }

                    binding.btnArchive.setBackgroundResource(R.drawable.col_four_bg);
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetMyWebinarList(pagenumber, webinartypemywebinar);
                } else {
                    Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }


            }
        });


        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagenumber = 1;
                loading = true;

                if (arrsavebooleanstateMyWebinar.get(3) == false) {
                    arrsavebooleanstateMyWebinar.set(3, true);
                    arraysavefilterMyWebinar.set(3, getResources().getString(R.string.str_filter_favourite));
                    webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                    binding.btnFavorite.setBackgroundResource(R.drawable.col_four_bg_hover);
                } else {
                    arrsavebooleanstateMyWebinar.set(3, false);
                    arraysavefilterMyWebinar.set(3, "");
                    if (arraysavefilterMyWebinar.get(0).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(1).equalsIgnoreCase("") &&
                            arraysavefilterMyWebinar.get(2).equalsIgnoreCase("")
                            && arraysavefilterMyWebinar.get(3).equalsIgnoreCase("")) {
                        webinartypemywebinar = "";
                    } else {
                        webinartypemywebinar = arraysavefilterMyWebinar.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");
                    }

                    binding.btnFavorite.setBackgroundResource(R.drawable.col_four_bg);
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetMyWebinarList(pagenumber, webinartypemywebinar);
                } else {
                    Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }


            }
        });


        return view = binding.getRoot();
    }


    public void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        binding.swipeRefreshLayouthomemywebinar.setRefreshing(false);
    }


    private void loadNextPage() {
        if (Constant.isNetworkAvailable(context)) {
            GetMyWebinarList(pagenumber, webinartypemywebinar);
        } else {
            Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

        }

    }


    public void GetMyWebinarList(final int pagenumber, final String webinartype) {

        mAPIService.GetMyWebinarList(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)
                , getResources().getString(R.string.str_filter_webinar), pagenumber, webinartype, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Home>() {
                    @Override
                    public void onCompleted() {


                        if (pagenumber == 1) {
                            if (arrHomeMyWebinarlist.size() > 0) {
                                adapter = new HomeMyWebinarAdapter(context, arrHomeMyWebinarlist);
                                binding.rvhomewebinar.setAdapter(adapter);
                            }
                        } else {
                            adapter.addLoadingFooter();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        if (pagenumber == 1) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }


                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.rvhomewebinar, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(Webinar_Home webinar_home) {

                        if (webinar_home.isSuccess() == true) {

                            if (pagenumber == 1) {
                                if (arrHomeMyWebinarlist.size() > 0) {
                                    arrHomeMyWebinarlist.clear();
                                }
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                            }


                            if (pagenumber == 1) {
                                arrHomeMyWebinarlist = webinar_home.getPayload().getWebinar();
                                Constant.Log(TAG, "size" + arrHomeMyWebinarlist.size());

                            } else {
                                List<WebinarItem> webinaritems = webinar_home.getPayload().getWebinar();
                                adapter.addAll(webinaritems);

                            }


                            if (arrHomeMyWebinarlist.size() > 0) {
                                total_record = webinar_home.getPayload().getWebinar().get(0).getTotalRecord();
                            }


                            if (arrHomeMyWebinarlist.size() > 0) {
                                binding.swipeRefreshLayouthomemywebinar.setVisibility(View.VISIBLE);
                                binding.tvNodatafound.setVisibility(View.GONE);
                            } else {
                                binding.swipeRefreshLayouthomemywebinar.setVisibility(View.GONE);
                                binding.tvNodatafound.setVisibility(View.VISIBLE);
                            }


                        } else {

                            if (webinar_home.getPayload().getAccessToken() != null && !webinar_home.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, webinar_home.getPayload().getAccessToken());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetMyWebinarList(pagenumber, webinartype);
                                } else {
                                    Snackbar.make(binding.rvhomewebinar, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                }

                            } else {

                                if (pagenumber == 1) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    binding.progressBar.setVisibility(View.GONE);
                                }
                                Snackbar.make(binding.rvhomewebinar, webinar_home.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }


                        }


                    }


                });


    }


}
