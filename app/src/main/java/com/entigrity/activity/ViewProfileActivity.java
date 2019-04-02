package com.entigrity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.entigrity.R;
import com.entigrity.databinding.ActivityViewProfileBinding;

import java.util.ArrayList;

public class ViewProfileActivity extends AppCompatActivity {

    ActivityViewProfileBinding binding;
    ProgressDialog progressDialog;
    public Context context;
    public String firstname = "", lastname = "", email = "", firmname = "", mobilenumber = "", zipcode = "", country = "";
    public int country_id = 0, state_id = 0, city_id = 0;
    public String whoyouare = "", whoyouarevalue = "";
    public String State, City;
    private int country_pos = 0;
    private int state_pos = 0;
    private int city_pos = 0;
    private int who_you_are_pos = 0;
    public ArrayList<Integer> arraylistselectedtopicsofinterest = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile);
        context = ViewProfileActivity.this;


        Intent intent = getIntent();
        if (intent != null) {
            firstname = intent.getStringExtra(getResources().getString(R.string.pass_fname));
            lastname = intent.getStringExtra(getResources().getString(R.string.pass_lname));
            email = intent.getStringExtra(getResources().getString(R.string.pass_email));
            firmname = intent.getStringExtra(getResources().getString(R.string.pass_firm_name));
            country = intent.getStringExtra(getResources().getString(R.string.pass_country_text));
            mobilenumber = intent.getStringExtra(getResources().getString(R.string.pass_mobile_number));
            State = intent.getStringExtra(getResources().getString(R.string.pass_state_text));
            City = intent.getStringExtra(getResources().getString(R.string.pass_city_text));
            zipcode = intent.getStringExtra(getResources().getString(R.string.pass_zipcode));
            country_pos = intent.getIntExtra(getResources().getString(R.string.pass_country), 0);
            state_pos = intent.getIntExtra(getResources().getString(R.string.pass_state), 0);
            city_pos = intent.getIntExtra(getResources().getString(R.string.pass_city), 0);
            whoyouarevalue = intent.getStringExtra(getResources().getString(R.string.pass_who_you_are_text));
            who_you_are_pos = intent.getIntExtra(getResources().getString(R.string.pass_who_you_are), 0);
            arraylistselectedtopicsofinterest = intent.getIntegerArrayListExtra(getResources().getString(R.string.pass_topics_of_interesr));


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // change UI elements here
                    SetData();
                }
            });


        }


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigate_EditProfile();
            }
        });

    }

    private void SetData() {

        if (!firstname.equalsIgnoreCase("") && firstname != null) {
            binding.tvFirstname.setText(firstname);
        }


        if (!lastname.equalsIgnoreCase("") && lastname != null) {
            binding.tvLastname.setText(lastname);
        }


        if (!email.equalsIgnoreCase("") && email != null) {
            binding.tvEmailname.setText(email);
        }

        if (!firmname.equalsIgnoreCase("") && firmname != null) {
            binding.tvFirmname.setText(firmname);
        }

        if (!mobilenumber.equalsIgnoreCase("") && mobilenumber != null) {
            binding.tvMobilenumber.setText(mobilenumber);
        }


        if (country_pos != 0) {
            country_id = country_pos;
        }
        if (state_pos != 0) {
            state_id = state_pos;
        }

        if (city_pos != 0) {
            city_id = city_pos;

        }


        if (!country.equalsIgnoreCase("") && country != null) {
            binding.tvContry.setText("" + country);

        }
        if (!State.equalsIgnoreCase("") && State != null) {
            binding.tvState.setText("" + State);
        }

        if (!City.equalsIgnoreCase("") && City != null) {
            binding.tvCity.setText("" + City);
        }


        if (!whoyouarevalue.equalsIgnoreCase("") && whoyouarevalue != null) {
            binding.tvWhoYouAre.setText("" + whoyouarevalue);
        }


        if (!zipcode.equalsIgnoreCase("") && zipcode != null) {
            binding.tvZipcode.setText(zipcode);
        }

    }


    public void Navigate_EditProfile() {
        Intent i = new Intent(context, EditProfileActivity.class);
        i.putExtra(getResources().getString(R.string.pass_fname), firstname);
        i.putExtra(getResources().getString(R.string.pass_lname), lastname);
        i.putExtra(getResources().getString(R.string.pass_email), email);
        i.putExtra(getResources().getString(R.string.pass_firm_name), firmname);
        i.putExtra(getResources().getString(R.string.pass_mobile_number), mobilenumber);
        i.putExtra(getResources().getString(R.string.pass_country), country_id);
        i.putExtra(getResources().getString(R.string.pass_state), state_id);
        i.putExtra(getResources().getString(R.string.pass_city), city_id);
        i.putExtra(getResources().getString(R.string.pass_state_text), State);
        i.putExtra(getResources().getString(R.string.pass_city_text), City);
        i.putExtra(getResources().getString(R.string.pass_zipcode), zipcode);
        i.putExtra(getResources().getString(R.string.pass_who_you_are), whoyouare);
        i.putExtra(getResources().getString(R.string.pass_topics_of_interesr), arraylistselectedtopicsofinterest);
        startActivity(i);
    }


}
