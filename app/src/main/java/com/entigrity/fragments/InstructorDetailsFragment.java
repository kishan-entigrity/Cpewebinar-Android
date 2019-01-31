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
import com.entigrity.activity.InstructorDetailsActivity;
import com.entigrity.databinding.FragmentInstructorDetailsBinding;

public class InstructorDetailsFragment extends Fragment {

    FragmentInstructorDetailsBinding binding;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructor_details, null, false);

        Setdata();

        return view = binding.getRoot();
    }


    public void Setdata() {


        if (InstructorDetailsActivity.getInstance().email != null && !InstructorDetailsActivity.getInstance().email.equalsIgnoreCase("")) {
            binding.tvEmailid.setText(InstructorDetailsActivity.getInstance().email);
        }
        if (InstructorDetailsActivity.getInstance().contact_no != null && !InstructorDetailsActivity.getInstance().contact_no.equalsIgnoreCase("")) {
            binding.tvMobilenum.setText(InstructorDetailsActivity.getInstance().contact_no);
        }

        if (InstructorDetailsActivity.getInstance().expertise != null && !InstructorDetailsActivity.getInstance().expertise.equalsIgnoreCase("")) {
            String expertise = InstructorDetailsActivity.getInstance().expertise.replaceAll("\\<[^>]*>", "").replace("&nbsp;", " ");
            binding.tvExpiritise.setText(expertise);
        }
        if (InstructorDetailsActivity.getInstance().company != null && !InstructorDetailsActivity.getInstance().company.equalsIgnoreCase("")) {
            binding.tvCompanyname.setText(InstructorDetailsActivity.getInstance().company);
        }
        if (InstructorDetailsActivity.getInstance().state != null && !InstructorDetailsActivity.getInstance().state.equalsIgnoreCase("")) {
            binding.tvStat.setText(InstructorDetailsActivity.getInstance().state);
        }
        if (InstructorDetailsActivity.getInstance().city != null && !InstructorDetailsActivity.getInstance().city.equalsIgnoreCase("")) {
            binding.tvCity.setText(InstructorDetailsActivity.getInstance().city);
        }

    }
}
