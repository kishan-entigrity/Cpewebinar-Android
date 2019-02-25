package com.entigrity.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_live, null, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvhomelive.setLayoutManager(layoutManager);

        binding.rvhomelive.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        adapter = new LiveHomeAdapter(getActivity());

        if (adapter != null) {
            binding.rvhomelive.setAdapter(adapter);
        }

        binding.swipeRefreshLayouthomelive.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
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
        binding.swipeRefreshLayouthomelive.setRefreshing(false);
    }
}
