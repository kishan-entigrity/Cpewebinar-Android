package com.entigrity.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class UserDashBoardFragment extends Fragment {
    View view;
    private FragmentHomeBinding binding;
    public Context context;
    int pStatus = 0;
    private Handler handler = new Handler();
    private static final String TAG = UserDashBoardFragment.class.getName();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, null, false);
        context = getActivity();

        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    System.exit(0);

                    return true;
                }
                return false;
            }
        });

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progressbar);

        binding.cirecularprogressbar.setProgress(0);   // Main Progress
        binding.cirecularprogressbar.setSecondaryProgress(100); // Secondary Progress
        binding.cirecularprogressbar.setMax(100); // Maximum Progress
        binding.cirecularprogressbar.setProgressDrawable(drawable);


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 60) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            binding.cirecularprogressbar.setProgress(pStatus);
                            binding.progress.setText(pStatus + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(16); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        return view = binding.getRoot();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new CompletedFragment(), getActivity().getResources().getString(R.string.str_completed));
        adapter.addFragment(new OnGoingFragment(), getActivity().getResources().getString(R.string.str_ongoing));
        adapter.addFragment(new UpComingFragment(), getActivity().getResources().getString(R.string.str_upcoming));
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
