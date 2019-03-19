package com.entigrity.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.adapter.MyCreditAdapter;
import com.entigrity.databinding.FragmentMycreditBinding;
import com.entigrity.view.SimpleDividerItemDecoration;

public class MyCreditsFragment extends Fragment {

    View view;
    public Context context;
    FragmentMycreditBinding binding;
    private static final String TAG = MyCreditsFragment.class.getName();
    MyCreditAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mycredit, null, false);
        context = getActivity();


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewMycredit.setLayoutManager(layoutManager);

        binding.recyclerviewMycredit.addItemDecoration(new SimpleDividerItemDecoration(context));


        adapter = new MyCreditAdapter(getActivity());


        if (adapter != null) {
            binding.recyclerviewMycredit.setAdapter(adapter);
        }


        return view = binding.getRoot();
    }
}
