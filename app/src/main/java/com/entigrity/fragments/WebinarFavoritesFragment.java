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
import com.entigrity.adapter.WebinarFavoriteAdapter;
import com.entigrity.databinding.FragmentWebinarListBinding;
import com.entigrity.utility.Constant;
import com.entigrity.view.SimpleDividerItemDecoration;

public class WebinarFavoritesFragment extends Fragment {

    FragmentWebinarListBinding binding;
    View view;
    public Context context;
    private static final String TAG = WebinarFavoritesFragment.class.getName();

    WebinarFavoriteAdapter webinarFavoriteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webinar_list, null, false);
        context = getActivity();


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewFavoriteWebinar.setLayoutManager(layoutManager);

        binding.recyclerviewFavoriteWebinar.addItemDecoration(new SimpleDividerItemDecoration(context));


        webinarFavoriteAdapter = new WebinarFavoriteAdapter(context, FavoritesFragment.getInstance().mListfavoriteswebinar);

        if (webinarFavoriteAdapter != null) {
            binding.recyclerviewFavoriteWebinar.setAdapter(webinarFavoriteAdapter);
        }

        return view = binding.getRoot();
    }
}
