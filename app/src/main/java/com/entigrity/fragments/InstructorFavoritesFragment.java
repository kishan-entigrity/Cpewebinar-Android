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
import com.entigrity.adapter.InstructorFavoriteAdapter;
import com.entigrity.databinding.FragmentInstructorListBinding;
import com.entigrity.utility.Constant;
import com.entigrity.view.SimpleDividerItemDecoration;

public class InstructorFavoritesFragment extends Fragment {
    FragmentInstructorListBinding binding;
    View view;
    public Context context;
    private static final String TAG = InstructorFavoritesFragment.class.getName();
    InstructorFavoriteAdapter instructorFavoriteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructor_list, null, false);
        context = getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewFavoriteInstructor.setLayoutManager(layoutManager);

        binding.recyclerviewFavoriteInstructor.addItemDecoration(new SimpleDividerItemDecoration(context));


        instructorFavoriteAdapter = new InstructorFavoriteAdapter(context, FavoritesFragment.getInstance().mListfavoritesSpeaker);

        if (instructorFavoriteAdapter != null) {
            binding.recyclerviewFavoriteInstructor.setAdapter(instructorFavoriteAdapter);
        }


        Constant.Log(TAG, "speaker size" + FavoritesFragment.getInstance().mListfavoritesSpeaker.size());


        return view = binding.getRoot();
    }


}
