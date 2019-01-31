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
import com.entigrity.databinding.FragmentCompanyDetailsBinding;

public class CompanyDetailsFragment extends Fragment {
    FragmentCompanyDetailsBinding binding;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_details, null, false);


        Setdata();


        return view = binding.getRoot();
    }

    public void Setdata() {

        if (CompanyDetailsActivity.getInstance().name != null && !CompanyDetailsActivity.getInstance().name.equalsIgnoreCase("")) {
            binding.tvCompanyname.setText(CompanyDetailsActivity.getInstance().name);
        }
        if (CompanyDetailsActivity.getInstance().website != null && !CompanyDetailsActivity.getInstance().website.equalsIgnoreCase("")) {
            binding.tvWebsiteName.setText(CompanyDetailsActivity.getInstance().website);
        }
        if (CompanyDetailsActivity.getInstance().contact_number != null && !CompanyDetailsActivity.getInstance().contact_number.equalsIgnoreCase("")) {
            binding.tvMobileNumber.setText(CompanyDetailsActivity.getInstance().contact_number);
        }

        binding.tvNuSpeaker.setText("" + CompanyDetailsActivity.getInstance().number_of_speaker);
        binding.tvNuWebinar.setText("" + CompanyDetailsActivity.getInstance().number_of_webinar);


    }
}
