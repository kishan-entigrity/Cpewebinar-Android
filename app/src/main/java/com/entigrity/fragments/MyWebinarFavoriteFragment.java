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
import com.entigrity.databinding.FragmentMywebinarFavoroiteBinding;

public class MyWebinarFavoriteFragment extends Fragment {

    private FragmentMywebinarFavoroiteBinding binding;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mywebinar_favoroite, null, false);


        return view = binding.getRoot();
    }
}
