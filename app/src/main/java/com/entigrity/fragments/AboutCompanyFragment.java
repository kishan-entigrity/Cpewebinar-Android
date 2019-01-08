package com.entigrity.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.activity.CompanyDetailsActivity;
import com.entigrity.databinding.FragmentAboutCompanyBinding;

public class AboutCompanyFragment extends Fragment {
    FragmentAboutCompanyBinding binding;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_company, null, false);

        if (CompanyDetailsActivity.getInstance().description != null && !CompanyDetailsActivity.getInstance().description.equalsIgnoreCase("")) {
            binding.tvAboutus.setText(CompanyDetailsActivity.getInstance().description);
        }


        return view = binding.getRoot();
    }
}
