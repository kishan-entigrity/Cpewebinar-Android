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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.HomeALLAdapter;
import com.entigrity.databinding.FragmentAllBinding;
import com.entigrity.model.homewebinarnew.Webinar_Home_New;
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

import static com.entigrity.utility.Constant.arraylistselectedvalue;
import static com.entigrity.utility.Constant.checkmywebinardotstatusset;

public class HomeAllFragment extends Fragment {
    private FragmentAllBinding binding;
    public Context context;
    HomeALLAdapter adapter;
    private static final String TAG = HomeAllFragment.class.getName();
    private APIService mAPIService_new;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private List<com.entigrity.model.homewebinarnew.WebinarItem> arrHomelistnew = new ArrayList<com.entigrity.model.homewebinarnew.WebinarItem>();
    private String webinartype = "live";
    private String topicsofinterest = "";
    private List<Boolean> arrsavebooleanstate = new ArrayList();
    private List<String> arraysavefilter = new ArrayList<String>();
    private boolean loading = true;
    public int start = 0, limit = 10;
    public boolean islast = false;
    private static HomeAllFragment instance;
    View view;
    public boolean isprogress = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, null, false);
        mAPIService_new = ApiUtilsNew.getAPIService();
        instance = HomeAllFragment.this;
        context = getActivity();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvhome.setLayoutManager(linearLayoutManager);
        binding.rvhome.addItemDecoration(new SimpleDividerItemDecoration(context));
        binding.rvhome.setItemAnimator(new DefaultItemAnimator());
        binding.rvhome.setHasFixedSize(true);


        arrsavebooleanstate.add(0, false);
        arrsavebooleanstate.add(1, false);
        arrsavebooleanstate.add(2, false);


        arraysavefilter.add(0, "");
        arraysavefilter.add(1, "");
        arraysavefilter.add(2, "");

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetHomeListNew(webinartype, topicsofinterest, start, limit);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

        }


        binding.rvhome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (loading) {
                    if (!islast) {
                        if (isLastVisible()) {
                            loading = false;
                            start = start + 10;
                            limit = 10;
                            loadNextPage();
                        }
                    }
                }


            }
        });


        binding.swipeRefreshLayouthome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });


        binding.btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = 0;
                limit = 10;
                loading = true;


                if (arrsavebooleanstate.get(0) == false) {
                    arrsavebooleanstate.set(0, true);
                    arraysavefilter.set(0, getResources().getString(R.string.str_filter_live));
                    webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                    binding.btnLive.setBackgroundResource(R.drawable.col_three_bg_new_hover);
                    binding.btnLive.setTextColor(getResources().getColor(R.color.White));
                } else {
                    arrsavebooleanstate.set(0, false);
                    arraysavefilter.set(0, "");


                    if (arraysavefilter.get(0).equalsIgnoreCase("") &&
                            arraysavefilter.get(1).equalsIgnoreCase("") &&
                            arraysavefilter.get(2).equalsIgnoreCase("")) {
                        webinartype = "";
                    } else {
                        webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");
                    }


                    binding.btnLive.setBackgroundResource(R.drawable.col_three_bg_new);
                    binding.btnLive.setTextColor(getResources().getColor(R.color.home_tab_color_unselected));
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetHomeListNew(webinartype, topicsofinterest, start, limit);
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }


            }
        });


        binding.btnSelfstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = 0;
                limit = 10;
                loading = true;


                if (arrsavebooleanstate.get(1) == false) {
                    arrsavebooleanstate.set(1, true);
                    arraysavefilter.set(1, getResources().getString(R.string.str_filter_selfstudy));
                    webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_three_bg_new_hover);
                    binding.btnSelfstudy.setTextColor(getResources().getColor(R.color.White));
                } else {
                    arrsavebooleanstate.set(1, false);
                    arraysavefilter.set(1, "");
                    if (arraysavefilter.get(0).equalsIgnoreCase("") &&
                            arraysavefilter.get(1).equalsIgnoreCase("") &&
                            arraysavefilter.get(2).equalsIgnoreCase("")) {
                        webinartype = "";


                    } else {
                        webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");

                    }


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_three_bg_new);
                    binding.btnSelfstudy.setTextColor(getResources().getColor(R.color.home_tab_color_unselected));
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetHomeListNew(webinartype, topicsofinterest, start, limit);
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }

            }
        });


        binding.btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = 0;
                limit = 10;
                loading = true;


                if (arrsavebooleanstate.get(2) == false) {
                    arrsavebooleanstate.set(2, true);
                    arraysavefilter.set(2, getResources().getString(R.string.str_filter_archieve));
                    webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                            .replace(" ", "");
                    binding.btnArchive.setBackgroundResource(R.drawable.col_three_bg_new_hover);
                    binding.btnArchive.setTextColor(getResources().getColor(R.color.White));
                } else {
                    arrsavebooleanstate.set(2, false);
                    arraysavefilter.set(2, "");
                    if (arraysavefilter.get(0).equalsIgnoreCase("") &&
                            arraysavefilter.get(1).equalsIgnoreCase("") &&
                            arraysavefilter.get(2).equalsIgnoreCase("")) {
                        webinartype = "";
                    } else {
                        webinartype = arraysavefilter.toString().replace("[", "").replace("]", "")
                                .replace(" ", "");
                    }

                    binding.btnArchive.setBackgroundResource(R.drawable.col_three_bg_new);
                    binding.btnArchive.setTextColor(getResources().getColor(R.color.home_tab_color_unselected));
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    GetHomeListNew(webinartype, topicsofinterest, start, limit);
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }

            }
        });


        return view = binding.getRoot();
    }


    public static HomeAllFragment getInstance() {
        return instance;
    }


    @Override
    public void onResume() {
        super.onResume();
        Constant.Log(TAG, "size" + arraylistselectedvalue.size());

        if (arraylistselectedvalue.size() > 0) {
            topicsofinterest = "";

            StringBuilder commaSepValueBuilder = new StringBuilder();

            //Looping through the list
            for (int i = 0; i < arraylistselectedvalue.size(); i++) {
                //append the value into the builder
                commaSepValueBuilder.append(arraylistselectedvalue.get(i));

                //if the value is not the last element of the list
                //then append the comma(,) as well
                if (i != arraylistselectedvalue.size() - 1) {
                    commaSepValueBuilder.append(",");
                }
            }
            //System.out.println(commaSepValueBuilder.toString());
            topicsofinterest = commaSepValueBuilder.toString();

            System.out.println(topicsofinterest);

            start = 0;
            limit = 10;
            loading = true;


            if (Constant.isNetworkAvailable(context)) {
                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                GetHomeListNew(webinartype, topicsofinterest, start, limit);
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

            }

        }

    }


    private void loadNextPage() {
        if (Constant.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetHomeListNew(webinartype, topicsofinterest, start, limit);
        } else {
            Snackbar.make(binding.rvhome, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }
    }


    public void refreshItems() {
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        start = 0;
        limit = 10;
        loading = true;
        checkmywebinardotstatusset = true;

        if (Constant.isNetworkAvailable(context)) {
            GetHomeListNew(webinartype, topicsofinterest, start, limit);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }
    }


    public void GetHomeListNew(final String webinartype, final String topicsofinterest, final int start, final int limit) {

        mAPIService_new.GetHomeWebinarListNew(getResources().getString(R.string.accept),
                getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context), start, limit, webinartype, topicsofinterest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Home_New>() {
                    @Override
                    public void onCompleted() {

                        if (binding.progressBar.getVisibility() == View.VISIBLE) {
                            binding.progressBar.setVisibility(View.GONE);
                        } else if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        loading = true;

                        if (start == 0 && limit == 10) {
                            if (arrHomelistnew.size() > 0) {
                                adapter = new HomeALLAdapter(context, arrHomelistnew);
                                binding.rvhome.setAdapter(adapter);

                            }
                        } else {
                            adapter.addLoadingFooter();

                        }
                    }


                    @Override
                    public void onError(Throwable e) {


                        if (start == 0 && limit == 10) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } else {
                            if (binding.progressBar.getVisibility() == View.VISIBLE) {
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.rvhome, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(Webinar_Home_New webinar_home_new) {

                        if (webinar_home_new.isSuccess() == true) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            } else {
                                if (binding.swipeRefreshLayouthome.isRefreshing()) {
                                    binding.swipeRefreshLayouthome.setRefreshing(false);
                                }
                            }

                            arraylistselectedvalue.clear();


                            islast = webinar_home_new.getPayload().isIsLast();

                            //  isprogress = webinar_home_new.getPayload().isIsprogress();


                           /* if (topicsofinterest.equalsIgnoreCase("")) {
                                UserDashBoardFragment.getInstance().setupTabIcons(isprogress);
                            }*/


                            if (start == 0 && limit == 10) {
                                if (arrHomelistnew.size() > 0) {
                                    arrHomelistnew.clear();
                                }

                            }


                            if (start == 0 && limit == 10) {
                                arrHomelistnew = webinar_home_new.getPayload().getWebinar();

                            } else {


                              /*  if (islast) {
                                    for (int i = 0; i < arrHomelistnew.size(); i++) {
                                        if (i == arrHomelistnew.size() - 1) {
                                            arrHomelistnew.remove(i);
                                        }
                                    }

                                }*/

                                if (arrHomelistnew.size() > 20) {
                                    arrHomelistnew.remove(arrHomelistnew.size() - 1);
                                }


                                List<com.entigrity.model.homewebinarnew.WebinarItem> webinaritems = webinar_home_new.getPayload().getWebinar();
                                adapter.addAll(webinaritems);



                               /* for (int i = 0; i < arrHomelistnew.size(); i++) {
                                    if (i == arrHomelistnew.size() - 1) {
                                        arrHomelistnew.remove(i);
                                    }
                                }*/


                            }


                            if (arrHomelistnew.size() > 0) {
                                binding.swipeRefreshLayouthome.setVisibility(View.VISIBLE);
                                binding.tvNodatafound.setVisibility(View.GONE);
                            } else {
                                binding.swipeRefreshLayouthome.setVisibility(View.GONE);
                                binding.tvNodatafound.setVisibility(View.VISIBLE);
                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            } else {
                                if (binding.swipeRefreshLayouthome.isRefreshing()) {
                                    binding.swipeRefreshLayouthome.setRefreshing(false);
                                }
                            }
                            Snackbar.make(binding.rvhome, webinar_home_new.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }


                });


    }


    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.rvhome.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = binding.rvhome.getAdapter().getItemCount() - 1;

        //Constant.Log(TAG, "pos + numitem" + pos + "  " + "  " + numItems);

        return (pos >= numItems);
    }


}
