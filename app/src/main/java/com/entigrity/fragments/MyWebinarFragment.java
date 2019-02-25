package com.entigrity.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.databinding.FragmentMywebinarBinding;
import com.entigrity.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class MyWebinarFragment extends Fragment {

    View view;
    private FragmentMywebinarBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mywebinar, null, false);


        setupViewPager(binding.mywebinarviewpager);
        binding.mywebinartab.setupWithViewPager(binding.mywebinarviewpager);


        return view = binding.getRoot();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyWebinarLiveFragment(), getActivity().getResources().getString(R.string.str_live));
        adapter.addFragment(new MyWebinarSelfStudyFragment(), getActivity().getResources().getString(R.string.str_self_study));
        adapter.addFragment(new MyWebinarArchiveFragment(), getActivity().getResources().getString(R.string.str_archive));
        adapter.addFragment(new MyWebinarFavoriteFragment(), getActivity().getResources().getString(R.string.str_favorite));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
