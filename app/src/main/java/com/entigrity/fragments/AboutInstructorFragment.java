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
import com.entigrity.databinding.FragmentAboutBinding;

public class AboutInstructorFragment extends Fragment {
    FragmentAboutBinding binding;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, null, false);

        if (InstructorDetailsActivity.getInstance().about_speaker != null && !InstructorDetailsActivity.getInstance().about_speaker.equalsIgnoreCase("")) {
            binding.tvAboutus.setText(InstructorDetailsActivity.getInstance().about_speaker);
        }


        return view = binding.getRoot();
    }
}
