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
import com.entigrity.adapter.CompanyFavoriteAdapter;
import com.entigrity.databinding.FragmentCompanyListBinding;
import com.entigrity.utility.Constant;
import com.entigrity.view.SimpleDividerItemDecoration;

public class CompanyFavoritesFragment extends Fragment {


    View view;
    public Context context;
    private static final String TAG = CompanyFavoritesFragment.class.getName();
    CompanyFavoriteAdapter companyFavoriteAdapter;

    FragmentCompanyListBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_list, null, false);
        context = getActivity();


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewFavoriteCompany.setLayoutManager(layoutManager);

        binding.recyclerviewFavoriteCompany.addItemDecoration(new SimpleDividerItemDecoration(context));


        companyFavoriteAdapter = new CompanyFavoriteAdapter(context, FavoritesFragment.getInstance().mListfavoritesCompany);

        if (companyFavoriteAdapter != null) {
            binding.recyclerviewFavoriteCompany.setAdapter(companyFavoriteAdapter);
        }


        Constant.Log(TAG, "company size" + FavoritesFragment.getInstance().mListfavoritesCompany.size());


        return view = binding.getRoot();
    }
}
