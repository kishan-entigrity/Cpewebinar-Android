package com.entigrity.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.databinding.FragmentContactusBinding;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

public class ContactUsFragment extends Fragment {
    private FragmentContactusBinding binding;
    public Context context;
    private static final String TAG = ContactUsFragment.class.getName();
    private APIService mAPIService;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contactus, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();


        return view = binding.getRoot();
    }
}
