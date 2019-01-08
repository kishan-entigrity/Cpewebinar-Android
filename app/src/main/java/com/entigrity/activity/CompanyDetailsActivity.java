package com.entigrity.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.entigrity.R;
import com.entigrity.databinding.ActivityCompanyProfileDetailsBinding;
import com.entigrity.fragments.AboutCompanyFragment;
import com.entigrity.fragments.CompanyDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompanyDetailsActivity extends AppCompatActivity {

    ActivityCompanyProfileDetailsBinding binding;
    public Context context;
    private static final String TAG = CompanyDetailsActivity.class.getName();
    private static CompanyDetailsActivity instance;


    public int id = 0, number_of_speaker = 0, number_of_webinar = 0;
    public String name, website, contact_number, logo, description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile_details);
        context = CompanyDetailsActivity.this;
        instance = CompanyDetailsActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(getResources().getString(R.string.pass_company_id), 0);
            name = intent.getStringExtra(getResources().getString(R.string.pass_company_name));
            website = intent.getStringExtra(getResources().getString(R.string.pass_company_website));
            contact_number = intent.getStringExtra(getResources().getString(R.string.pass_company_contact_number));
            logo = intent.getStringExtra(getResources().getString(R.string.pass_company_logo));
            description = intent.getStringExtra(getResources().getString(R.string.pass_company_description));
            number_of_speaker = intent.getIntExtra(getResources().getString(R.string.pass_company_number_of_speaker), 0);
            number_of_webinar = intent.getIntExtra(getResources().getString(R.string.pass_company_number_of_webinar), 0);

        }

        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!logo.equalsIgnoreCase("")) {
            Picasso.with(context).load(logo)
                    .placeholder(R.mipmap.placeholder)
                    .into(binding.ivCompanylogo);
        }

        if (!name.equalsIgnoreCase("")) {
            binding.tvCompanyname.setText(name);
        }

        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

    }

    public static CompanyDetailsActivity getInstance() {
        return instance;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutCompanyFragment(), getResources().getString(R.string.about));
        adapter.addFragment(new CompanyDetailsFragment(), getResources().getString(R.string.company_details));
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
