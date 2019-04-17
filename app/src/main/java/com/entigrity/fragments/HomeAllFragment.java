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

import com.entigrity.R;
import com.entigrity.adapter.HomeALLAdapter;
import com.entigrity.databinding.FragmentAllBinding;
import com.entigrity.model.homewebinarlist.WebinarItem;
import com.entigrity.model.homewebinarnew.Webinar_Home_New;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;
import com.entigrity.webservice.ApiUtilsNew;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeAllFragment extends Fragment {
    private FragmentAllBinding binding;
    public Context context;
    HomeALLAdapter adapter;
    private static final String TAG = HomeAllFragment.class.getName();
    private APIService mAPIService;
    private APIService mAPIService_new;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private List<WebinarItem> arrHomelist = new ArrayList<WebinarItem>();
    private List<com.entigrity.model.homewebinarnew.WebinarItem> arrHomelistnew = new ArrayList<com.entigrity.model.homewebinarnew.WebinarItem>();
    //private int pagenumber = 1;
    private String webinartype = "";
    private String topicsofinterest = "";
    private List<Boolean> arrsavebooleanstate = new ArrayList();
    private List<String> arraysavefilter = new ArrayList<String>();
    public int total_record = 0;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public int start = 0, limit = 10;

    public boolean islast = false;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, null, false);
        mAPIService = ApiUtils.getAPIService();
        mAPIService_new = ApiUtilsNew.getAPIService();

        context = getActivity();


        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvhome.setLayoutManager(linearLayoutManager);
        binding.rvhome.addItemDecoration(new SimpleDividerItemDecoration(context));
        binding.rvhome.setItemAnimator(new DefaultItemAnimator());


        arrsavebooleanstate.add(0, false);
        arrsavebooleanstate.add(1, false);
        arrsavebooleanstate.add(2, false);


        arraysavefilter.add(0, "");
        arraysavefilter.add(1, "");
        arraysavefilter.add(2, "");

        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            //GetHomeList(pagenumber, webinartype);
            GetHomeListNew(webinartype, topicsofinterest, start, limit);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

        }


        binding.rvhome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                // Constant.Log(TAG, "tags" + "   " + visibleItemCount + "    " + firstVisibleItemPosition + "    " + totalItemCount);
                // Constant.Log(TAG, "isloading" + loading);
               /* Constant.Log(TAG, "boolean" + ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0));*/


                if (loading) {
                    if (!islast) {
                        Log.v("...", "++++" + isLastVisible());
                        if (isLastVisible()) {
                            Log.v("...", "++++" + isLastVisible());
                            loading = false;
                            start = start + 10;
                            limit = 10;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            loadNextPage();
                        }
                    }
                }


             /*   if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    //  totalItemCount = linearLayoutManager.getItemCount();

                    totalItemCount = adapter.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();


                    Constant.Log(TAG, "tags" + "   " + visibleItemCount + "    " + pastVisiblesItems + "    " + totalItemCount);


                    Constant.Log(TAG, "isloading" + loading);
                    //Constant.Log(TAG, "start" + start);

                    if (loading) {
                        if (!islast) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                // getActivity().findViewById(android.R.id.content).setVisibility(View.VISIBLE);
                                loading = false;
                                start = start + 10;
                                limit = 10;
                                Log.v("...", "Last Item Wow !");
                                //Do pagination.. i.e. fetch new data
                                loadNextPage();
                            }
                        }

                    }
                }*/
            }
        });


        binding.swipeRefreshLayouthome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
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
                    binding.btnLive.setBackgroundResource(R.drawable.col_three_bg_hover);
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


                    binding.btnLive.setBackgroundResource(R.drawable.col_three_bg);
                }

                // Constant.Log(TAG, "webinar_type" + webinartype);


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    // GetHomeList(pagenumber, webinartype);
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


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_three_bg_hover);
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


                    binding.btnSelfstudy.setBackgroundResource(R.drawable.col_three_bg);
                }


                //Constant.Log(TAG, "webinar_type" + webinartype);


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    //   GetHomeList(pagenumber, webinartype);
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
                    binding.btnArchive.setBackgroundResource(R.drawable.col_three_bg_hover);
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

                    binding.btnArchive.setBackgroundResource(R.drawable.col_three_bg);
                }


                if (Constant.isNetworkAvailable(context)) {
                    progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                    // GetHomeList(pagenumber, webinartype);
                    GetHomeListNew(webinartype, topicsofinterest, start, limit);
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                }

            }
        });


        return view = binding.getRoot();
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
        // Load items
       /* start = 0;
        limit = 10;*/
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        start = 0;
        limit = 10;
        loading = true;

        if (Constant.isNetworkAvailable(context)) {
            // progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            //GetHomeList(pagenumber, webinartype);
            GetHomeListNew(webinartype, topicsofinterest, start, limit);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

        }


    }


    /*  public void GetHomeList(final int pagenumber, final String webinartype) {

          mAPIService_new.GetHomeWebinarList(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)
                  , getResources().getString(R.string.str_all), pagenumber, webinartype, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<Webinar_Home>() {
                      @Override
                      public void onCompleted() {

                          if (pagenumber == 1) {
                              if (arrHomelist.size() > 0) {
                                  adapter = new HomeALLAdapter(context, arrHomelist);
                                  binding.rvhome.setAdapter(adapter);
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
                              getActivity().findViewById(android.R.id.content).setVisibility(View.GONE);
                          }


                          String message = Constant.GetReturnResponse(context, e);
                          Snackbar.make(binding.rvhome, message, Snackbar.LENGTH_SHORT).show();


                      }

                      @Override
                      public void onNext(Webinar_Home webinar_home) {

                          if (webinar_home.isSuccess() == true) {
                              if (pagenumber == 1) {
                                  if (arrHomelist.size() > 0) {
                                      arrHomelist.clear();
                                  }
                                  if (progressDialog.isShowing()) {
                                      progressDialog.dismiss();
                                  }
                              } else {
                                  getActivity().findViewById(android.R.id.content).setVisibility(View.GONE);
                              }


                              Constant.Log(TAG, "pagenumber" + pagenumber);

                              if (pagenumber == 1) {
                                  arrHomelist = webinar_home.getPayload().getWebinar();
                              } else {
                                  List<WebinarItem> webinaritems = webinar_home.getPayload().getWebinar();
                                  adapter.addAll(webinaritems);


                              }

                              if (arrHomelist.size() > 0) {
                                  total_record = webinar_home.getPayload().getWebinar().get(0).getTotalRecord();
                              }


                              // Constant.Log(TAG, "comma" + webinartype);

                              if (arrHomelist.size() > 0) {
                                  binding.swipeRefreshLayouthome.setVisibility(View.VISIBLE);
                                  binding.tvNodatafound.setVisibility(View.GONE);
                              } else {
                                  binding.swipeRefreshLayouthome.setVisibility(View.GONE);
                                  binding.tvNodatafound.setVisibility(View.VISIBLE);
                              }


                          } else {

                              if (webinar_home.getPayload().getAccessToken() != null && !webinar_home.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                  AppSettings.set_login_token(context, webinar_home.getPayload().getAccessToken());

                                  if (Constant.isNetworkAvailable(context)) {
                                      GetHomeList(pagenumber, webinartype);
                                  } else {
                                      Snackbar.make(binding.rvhome, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                                  }

                              } else {
                                  if (pagenumber == 1) {
                                      if (progressDialog.isShowing()) {
                                          progressDialog.dismiss();
                                      }
                                  } else {
                                      getActivity().findViewById(android.R.id.content).setVisibility(View.GONE);
                                  }


                                  Snackbar.make(binding.rvhome, webinar_home.getMessage(), Snackbar.LENGTH_SHORT).show();

                              }


                          }


                      }


                  });


      }
  */
    public void GetHomeListNew(final String webinartype, final String topicsofinterest, final int start, final int limit) {

        mAPIService_new.GetHomeWebinarListNew(getResources().getString(R.string.accept),
                getResources().getString(R.string.bearer) + AppSettings.get_login_token(context), start, limit, webinartype, topicsofinterest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Webinar_Home_New>() {
                    @Override
                    public void onCompleted() {

                        if (binding.progressBar.getVisibility() == View.VISIBLE) {
                            binding.progressBar.setVisibility(View.GONE);
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
                        Snackbar.make(binding.rvhome, message, Snackbar.LENGTH_SHORT).show();
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


                            islast = webinar_home_new.getPayload().isIsLast();


                            if (start == 0 && limit == 10) {
                                if (arrHomelistnew.size() > 0) {
                                    arrHomelistnew.clear();
                                }

                            }


                            if (start == 0 && limit == 10) {
                                arrHomelistnew = webinar_home_new.getPayload().getWebinar();
                            } else {

                                for (int i = 0; i < arrHomelistnew.size(); i++) {

                                    if (i == arrHomelistnew.size() - 1) {
                                        arrHomelistnew.remove(i);
                                    }


                                }


                                List<com.entigrity.model.homewebinarnew.WebinarItem> webinaritems = webinar_home_new.getPayload().getWebinar();
                                adapter.addAll(webinaritems);
                            }

                            Constant.Log(TAG, "start" + start + "   " + "limit" + limit);
                            Constant.Log(TAG, "size" + arrHomelistnew.size());


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

        Constant.Log(TAG, "pos + numitem" + pos + "  " + "  " + numItems);

        return (pos >= numItems);
    }


}
