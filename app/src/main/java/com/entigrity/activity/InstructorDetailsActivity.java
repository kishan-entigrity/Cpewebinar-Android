package com.entigrity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.entigrity.R;
import com.entigrity.databinding.ActivityInstructorProfileDetailsBinding;
import com.entigrity.fragments.AboutInstructorFragment;
import com.entigrity.fragments.InstructorDetailsFragment;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class InstructorDetailsActivity extends AppCompatActivity {
    ActivityInstructorProfileDetailsBinding binding;
    public Context context;
    private static final String TAG = InstructorDetailsActivity.class.getName();
    private static InstructorDetailsActivity instance;

    public int id = 0;
    public Dialog myDialog_popup;
    ImageView ivclose;
    EditText edt_your_review;
    public String name, email, contact_no, logo, expertise, about_speaker, company, state, city;
    Button btn_submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instructor_profile_details);
        context = InstructorDetailsActivity.this;
        instance = InstructorDetailsActivity.this;

        Intent intent = getIntent();
        if (intent != null) {

            id = intent.getIntExtra(getResources().getString(R.string.pass_inst_id), 0);
            name = intent.getStringExtra(getResources().getString(R.string.pass_inst_name));
            email = intent.getStringExtra(getResources().getString(R.string.pass_inst_email));
            contact_no = intent.getStringExtra(getResources().getString(R.string.pass_inst_contact_number));
            logo = intent.getStringExtra(getResources().getString(R.string.pass_inst_logo));
            expertise = intent.getStringExtra(getResources().getString(R.string.pass_inst_expiritise));
            about_speaker = intent.getStringExtra(getResources().getString(R.string.pass_inst_about_speaker));
            company = intent.getStringExtra(getResources().getString(R.string.pass_inst_company));
            state = intent.getStringExtra(getResources().getString(R.string.pass_inst_state));
            city = intent.getStringExtra(getResources().getString(R.string.pass_inst_city));

        }


        if (!logo.equalsIgnoreCase("")) {
            Picasso.with(context).load(logo)
                    .placeholder(R.mipmap.placeholder)
                    .into(binding.ivCustImage);
        }

        if (!name.equalsIgnoreCase("")) {
            binding.tvInstname.setText(name);
        }


        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

        //  MainActivity.getInstance().rel_top_bottom.setVisibility(View.GONE);


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRatingPopup(context);

            }
        });


    }

    public void ShowRatingPopup(final Context context) {
        myDialog_popup = new Dialog(context);
        myDialog_popup.setContentView(R.layout.rating_popup);
        myDialog_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivclose = (ImageView) myDialog_popup.findViewById(R.id.ivclose);
        edt_your_review = (EditText) myDialog_popup.findViewById(R.id.edt_your_review);
        btn_submit = (Button) myDialog_popup.findViewById(R.id.btn_submit);

        edt_your_review.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_your_review.setRawInputType(InputType.TYPE_CLASS_TEXT);


        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog_popup.isShowing()) {
                    myDialog_popup.dismiss();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {

                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }

                }

            }
        });


        myDialog_popup.show();

    }


    public static InstructorDetailsActivity getInstance() {
        return instance;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Boolean Validation() {
        if (edt_your_review.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_review), context);
            return false;
        } else {
            return true;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutInstructorFragment(), getResources().getString(R.string.about));
        adapter.addFragment(new InstructorDetailsFragment(), getResources().getString(R.string.company_details));
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
