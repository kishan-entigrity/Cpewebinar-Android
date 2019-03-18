package com.entigrity.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.databinding.FragmentMycreditBinding;

public class MyCreditsFragment extends Fragment {

    View view;
    public Context context;
    FragmentMycreditBinding binding;
    private static final String TAG = MyCreditsFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mycredit, null, false);
        context = getActivity();


        return view = binding.getRoot();
    }
}
