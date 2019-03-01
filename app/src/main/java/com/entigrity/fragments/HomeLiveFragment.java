package com.entigrity.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.adapter.LiveHomeAdapter;
import com.entigrity.databinding.FragmentHomeLiveBinding;
import com.entigrity.view.SimpleDividerItemDecoration;

public class HomeLiveFragment extends Fragment {

    private FragmentHomeLiveBinding binding;
    View view;
    LiveHomeAdapter adapter;
    boolean isLoading = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_live, null, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvhomelive.setLayoutManager(layoutManager);

        binding.rvhomelive.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        if (getActivity() != null) {
            adapter = new LiveHomeAdapter(getActivity());
            if (adapter != null) {
                binding.rvhomelive.setAdapter(adapter);
            }
        }


        initScrollListener();

        binding.swipeRefreshLayouthomelive.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


        return view = binding.getRoot();
    }


    private void initScrollListener() {
        binding.rvhomelive.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == 9) {
                        //bottom of list!

                        Log.e("load more count", "load more count" + linearLayoutManager.findLastCompletelyVisibleItemPosition());

                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    adapter = new LiveHomeAdapter(getActivity());
                    if (adapter != null) {
                        binding.rvhomelive.setAdapter(adapter);
                    }
                }

            }
        }, 2000);


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
        binding.swipeRefreshLayouthomelive.setRefreshing(false);
    }


}
