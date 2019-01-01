package com.entigrity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.TopicsofinterestEditProfileAdapter;
import com.entigrity.databinding.ActivityEditProfileBinding;
import com.entigrity.model.city.CityItem;
import com.entigrity.model.city.CityModel;
import com.entigrity.model.country.CountryItem;
import com.entigrity.model.country.CountryModel;
import com.entigrity.model.editProfile.EditProfileModel;
import com.entigrity.model.state.StateItem;
import com.entigrity.model.state.StateModel;
import com.entigrity.model.topicsofinterest.TagsItem;
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    public Dialog myDialog;
    public Context context;
    private APIService mAPIService;
    public RecyclerView recyclerview_topics_interest;
    public TopicsofinterestEditProfileAdapter topicsofinterestEditProfileAdapteradapter;
    public ArrayList<Integer> arraylistselectedtopicsofinterest = new ArrayList<Integer>();
    public TextView tv_apply, tv_cancel;


    private ArrayList<String> arrayLististusertype = new ArrayList<String>();
    private ArrayList<Integer> arrayLististusertypeid = new ArrayList<Integer>();


    private ArrayList<TagsItem> mListrtopicsofinterest = new ArrayList<TagsItem>();
    private ArrayList<TagsItem> mListtopicsofinterest_filter = new ArrayList<TagsItem>();


    public ArrayList<String> getcountryarraylist = new ArrayList<String>();
    public ArrayList<CountryItem> getcountryarray = new ArrayList<CountryItem>();


    public ArrayList<String> getstatearralist = new ArrayList<String>();
    public ArrayList<StateItem> getstatearray = new ArrayList<>();

    public ArrayList<String> getcityarraylist = new ArrayList<String>();
    public ArrayList<CityItem> getcityarray = new ArrayList<CityItem>();


    public boolean checkedadapter = false;


    public Dialog myDialog_popup;
    public TextView tv_popup_msg, tv_popup_submit;
    private int country_id = 0;
    private int state_id = 0;
    private int city_id = 0;
    private int country_pos = 0;
    private int state_pos = 0;
    private int city_pos = 0;
    public String firstname = "", lastname = "", email = "", firmname = "", mobilenumber = "", zipcode = "";
    private int who_you_are_pos = 0;


    ProgressDialog progressDialog;


    public boolean boolean_country_spinner = true;
    public boolean boolean_state_spinner = true;
    public boolean boolean_city_spinner = true;
    public boolean boolean_usertype_spinner = true;
    public EditText edt_search;

    private static final String TAG = EditProfileActivity.class.getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        context = EditProfileActivity.this;
        mAPIService = ApiUtils.getAPIService();

        Intent intent = getIntent();
        if (intent != null) {
            firstname = intent.getStringExtra(getResources().getString(R.string.pass_fname));
            lastname = intent.getStringExtra(getResources().getString(R.string.pass_lname));
            email = intent.getStringExtra(getResources().getString(R.string.pass_email));
            firmname = intent.getStringExtra(getResources().getString(R.string.pass_firm_name));
            mobilenumber = intent.getStringExtra(getResources().getString(R.string.pass_mobile_number));
            zipcode = intent.getStringExtra(getResources().getString(R.string.pass_zipcode));
            country_pos = intent.getIntExtra(getResources().getString(R.string.pass_country), 0);
            state_pos = intent.getIntExtra(getResources().getString(R.string.pass_state), 0);
            city_pos = intent.getIntExtra(getResources().getString(R.string.pass_city), 0);
            who_you_are_pos = intent.getIntExtra(getResources().getString(R.string.pass_who_you_are), 0);
            arraylistselectedtopicsofinterest = intent.getIntegerArrayListExtra(getResources().getString(R.string.pass_topics_of_interesr));

            SetData();
        }


        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_country_spinner) {
                    boolean_country_spinner = false;
                } else {

                    if (position != 0) {
                        country_id = position;

                        if (Constant.isNetworkAvailable(context)) {
                            GetState(country_id);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }
                    } else {
                        country_id = position;
                        // Constant.ShowPopUp(context.getString(R.string.str_country), context);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (boolean_state_spinner) {
                    boolean_state_spinner = false;
                } else {

                    if (position != 0) {
                        state_id = position;

                        if (Constant.isNetworkAvailable(context)) {
                            GetCity(state_id);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }

                    } else {
                        state_id = position;
                        //Constant.ShowPopUp(context.getString(R.string.str_state), context);
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (boolean_city_spinner) {
                    boolean_city_spinner = false;
                } else {

                    if (position != 0) {
                        city_id = position;
                    } else {
                        city_id = position;
                        //Constant.ShowPopUp(context.getString(R.string.str_city), context);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boolean_usertype_spinner) {
                    boolean_usertype_spinner = false;
                } else {

                    if (position != 0) {
                        who_you_are_pos = arrayLististusertypeid.get(position - 1);

                        Constant.Log("value", "value" + who_you_are_pos);
                    } else {
                        who_you_are_pos = position;
                        //Constant.ShowPopUp(context.getString(R.string.val_user_type), context);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetTopicsOfInterset();


        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        EditPost(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context),
                                binding.edtFirstname.getText().toString(), binding.edtLastname.getText().toString(),
                                binding.edtEmailname.getText().toString(), binding.edtFirmname.getText().toString(), country_id, state_id, city_id, Integer.parseInt(binding.edtZipcode.getText().toString()), binding.edtMobileNumber.getText()
                                        .toString(), arraylistselectedtopicsofinterest, who_you_are_pos);
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }

                }

            }
        });
        binding.tvTopicsofinterset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTopicsOfInterestPopup();
            }
        });


    }


    public void SetData() {

        if (!firstname.equalsIgnoreCase("") && firstname != null) {
            binding.edtFirstname.setText(firstname);
        }


        if (!lastname.equalsIgnoreCase("") && lastname != null) {
            binding.edtLastname.setText(lastname);
        }


        if (!email.equalsIgnoreCase("") && email != null) {
            binding.edtEmailname.setText(email);
        }

        if (!firmname.equalsIgnoreCase("") && firmname != null) {
            binding.edtFirmname.setText(firmname);
        }

        if (!mobilenumber.equalsIgnoreCase("") && mobilenumber != null) {
            binding.edtMobileNumber.setText(mobilenumber);
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

        if (!zipcode.equalsIgnoreCase("") && zipcode != null) {
            binding.edtZipcode.setText(zipcode);
        }


    }


   /* public void GetProfile() {
        mAPIService.GetProfile(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewProfileModel>() {
                    @Override
                    public void onCompleted() {

                      *//*
     *//*

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(ViewProfileModel viewProfileModel) {

                        if (viewProfileModel.isSuccess()) {
                            if (viewProfileModel.getPayload().getData().getFirstName() != null
                                    && !viewProfileModel.getPayload().getData().getFirstName().equalsIgnoreCase("")) {
                                binding.edtFirstname.setText(viewProfileModel.getPayload().getData().getFirstName());
                            }

                            if (viewProfileModel.getPayload().getData().getLastName() != null
                                    && !viewProfileModel.getPayload().getData().getLastName().equalsIgnoreCase("")) {
                                binding.edtLastname.setText(viewProfileModel.getPayload().getData().getLastName());
                            }

                            if (viewProfileModel.getPayload().getData().getEmail() != null
                                    && !viewProfileModel.getPayload().getData().getEmail().equalsIgnoreCase("")) {
                                binding.edtEmailname.setText(viewProfileModel.getPayload().getData().getEmail());
                            }

                            if (viewProfileModel.getPayload().getData().getFirmName() != null
                                    && !viewProfileModel.getPayload().getData().getFirmName().equalsIgnoreCase("")) {
                                binding.edtFirmname.setText(viewProfileModel.getPayload().getData().getFirmName());
                            }


                            if (viewProfileModel.getPayload().getData().getContactNo() != null
                                    && !viewProfileModel.getPayload().getData().getContactNo().equalsIgnoreCase("")) {
                                binding.edtMobileNumber.setText(viewProfileModel.getPayload().getData().getContactNo());
                            }

                            if (viewProfileModel.getPayload().getData().getTags().size() > 0) {


                                for (int i = 0; i < viewProfileModel.getPayload().getData().getTags().size(); i++) {
                                    arraylistselectedtopicsofinterest.add(viewProfileModel.getPayload().getData().getTags().get(i).getId());
                                }


                            }

                            if (viewProfileModel.getPayload().getData().getCountry() != null
                                    && !viewProfileModel.getPayload().getData().getCountry().equalsIgnoreCase("")) {
                                country_pos = viewProfileModel.getPayload().getData().getCountryId();
                                country_id = country_pos;
                            }
                            if (viewProfileModel.getPayload().getData().getState() != null
                                    && !viewProfileModel.getPayload().getData().getState().equalsIgnoreCase("")) {
                                state_pos = viewProfileModel.getPayload().getData().getStateId();
                                state_id = state_pos;
                            }

                            if (viewProfileModel.getPayload().getData().getCity() != null
                                    && !viewProfileModel.getPayload().getData().getCity().equalsIgnoreCase("")) {
                                city_pos = viewProfileModel.getPayload().getData().getCityId();
                                city_id = city_pos;

                            }

                            if (viewProfileModel.getPayload().getData().getZipcode() != null
                                    && !viewProfileModel.getPayload().getData().getZipcode().equalsIgnoreCase("")) {
                                binding.edtZipcode.setText(viewProfileModel.getPayload().getData().getZipcode());
                            }


                            if (viewProfileModel.getPayload().getData().getUserTypeId() != 0) {
                                who_you_are_pos = viewProfileModel.getPayload().getData().getUserTypeId();
                            }


                        } else

                        {

                            if (viewProfileModel.getPayload().getAccess_token() != null && !viewProfileModel.getPayload().getAccess_token().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, viewProfileModel.getPayload().getAccess_token());

                                if (Constant.isNetworkAvailable(context)) {
                                    GetProfile();
                                } else {
                                    Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                }


                            } else {
                                Constant.ShowPopUp(viewProfileModel.getMessage(), context);
                            }

                        }


                    }
                });

    }*/


    public void EditPost(String Authorization, String first_name, String last_name, String email,
                         String firm_name, final int country_id, final int state_id, final int city_id,
                         int zipcode, String contact_no, ArrayList<Integer> tags, final int user_type) {


        // RxJava
        mAPIService.Ediprofile(Authorization, first_name, last_name, email
                , firm_name, country_id, state_id, city_id, zipcode, contact_no, tags, user_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditProfileModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);

                    }

                    @Override
                    public void onNext(EditProfileModel editProfileModel) {
                        if (editProfileModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            //ShowPopUpSucess(editProfileModel.getMessage(), context);

                            ShowPopUp(editProfileModel.getMessage(), context);

                        } else if (editProfileModel.isSuccess() == false) {
                            if (editProfileModel.getPayload().getAccessToken() != null && !editProfileModel.getPayload().getAccessToken().equalsIgnoreCase("")) {
                                AppSettings.set_login_token(context, editProfileModel.getPayload().getAccessToken());

                                if (Validation()) {
                                    if (Constant.isNetworkAvailable(context)) {

                                        EditPost(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context),
                                                binding.edtFirstname.getText().toString(), binding.edtLastname.getText().toString(),
                                                binding.edtEmailname.getText().toString(), binding.edtFirmname.getText().toString(), country_id, state_id, city_id, Integer.parseInt(binding.edtZipcode.getText().toString()), binding.edtMobileNumber.getText()
                                                        .toString(), arraylistselectedtopicsofinterest, user_type);
                                    } else {
                                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                                    }

                                }
                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Constant.ShowPopUp(editProfileModel.getMessage(), context);
                            }


                        }


                    }
                });

    }

    /*public void ShowPopUpSucess(String message, final Context context) {
        myDialog_popup = new Dialog(context);
        myDialog_popup.setContentView(R.layout.activity_popup);
        myDialog_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_popup_msg = (TextView) myDialog_popup.findViewById(R.id.tv_popup_msg);
        tv_popup_submit = (TextView) myDialog_popup.findViewById(R.id.tv_popup_submit);

        tv_popup_msg.setText(message);


        tv_popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog_popup.isShowing()) {
                    myDialog_popup.dismiss();
                }

                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();


            }
        });
        myDialog_popup.show();

    }*/

    public void ShowPopUp(String message, final Context context) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();


                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();


            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    public void GetUserType() {
        mAPIService.Getusertype().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserTypeModel>() {
                    @Override
                    public void onCompleted() {
                        ShowAdapter();
                        binding.spinner.setSelection(who_you_are_pos);
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(UserTypeModel userTypeModel) {

                        arrayLististusertype.clear();
                        arrayLististusertype.add(getResources().getString(R.string.str_who_you_are));

                        for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                            arrayLististusertype.add(userTypeModel.getPayload().getUserType().get(i).getName());
                            arrayLististusertypeid.add(userTypeModel.getPayload().getUserType().get(i).getId());
                        }


                    }
                });
    }

    public void ShowAdapter() {
        if (arrayLististusertype.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinner.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, arrayLististusertype);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(aa);


        }
    }


    public void Show_Country_Adapter() {
        if (getcountryarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getcountryarraylist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerCountry.setAdapter(aa);

            binding.spinnerCountry.setSelection(country_pos);


        }


    }


    public void Show_City_Adapter() {
        if (getcityarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getcityarraylist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerCity.setAdapter(aa);


            binding.spinnerCity.setSelection(city_pos);
        }


    }


    public void Show_State_Adapter() {
        if (getstatearralist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getstatearralist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerState.setAdapter(aa);
            binding.spinnerState.setSelection(state_pos);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void GetTopicsOfInterset() {
        mAPIService.GetTopicsofInterest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopicsofInterest>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetCountry();
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(TopicsofInterest topicsofInterest) {
                        mListrtopicsofinterest.clear();
                        mListtopicsofinterest_filter.clear();


                        for (int i = 0; i < topicsofInterest.getPayload().getTags().size(); i++) {
                            TagsItem tagsItem = new TagsItem();
                            tagsItem.setId(topicsofInterest.getPayload().getTags().get(i).getId());
                            tagsItem.setTag(topicsofInterest.getPayload().getTags().get(i).getTag());
                            mListrtopicsofinterest.add(tagsItem);
                            mListtopicsofinterest_filter.add(tagsItem);
                        }


                    }
                });
    }


    public void ShowTopicsOfInterestPopup() {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.activity_topics_of_interest);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recyclerview_topics_interest = (RecyclerView) myDialog.findViewById(R.id.recyclerview_topics_interest);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview_topics_interest.setLayoutManager(layoutManager);

        recyclerview_topics_interest.addItemDecoration(new SimpleDividerItemDecoration(this));

        tv_apply = (TextView) myDialog.findViewById(R.id.tv_apply);
        tv_cancel = (TextView) myDialog.findViewById(R.id.tv_cancel);
        edt_search = (EditText) myDialog.findViewById(R.id.edt_search);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


                if (charSequence != null && charSequence.toString().trim().length() > 0) {
                    mListrtopicsofinterest.clear();

                    for (int j = 0; j < mListtopicsofinterest_filter.size(); j++) {

                        if ((mListtopicsofinterest_filter.get(j).getTag().toLowerCase().contains(charSequence.toString().trim().toLowerCase()) ||
                                mListtopicsofinterest_filter.get(j).getTag().toUpperCase().contains(charSequence.toString().trim().toUpperCase()))) {
                            mListrtopicsofinterest.add(mListtopicsofinterest_filter.get(j));
                        }
                    }

                    if (mListrtopicsofinterest.size() > 0) {
                        recyclerview_topics_interest.setVisibility(View.VISIBLE);
                        topicsofinterestEditProfileAdapteradapter.notifyDataSetChanged();
                    } else {
                        recyclerview_topics_interest.setVisibility(View.GONE);
                    }


                } else {
                    mListrtopicsofinterest.clear();

                    for (int j = 0; j < mListtopicsofinterest_filter.size(); j++) {
                        mListrtopicsofinterest.add(mListtopicsofinterest_filter.get(j));

                    }

                    if (mListrtopicsofinterest.size() > 0) {
                        recyclerview_topics_interest.setVisibility(View.VISIBLE);
                        topicsofinterestEditProfileAdapteradapter.notifyDataSetChanged();
                    } else {
                        recyclerview_topics_interest.setVisibility(View.GONE);


                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
        });

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkedadapter = true;
                if (myDialog.isShowing()) {
                    myDialog.dismiss();

                }

            }
        });


        if (checkedadapter == false) {
            topicsofinterestEditProfileAdapteradapter = new TopicsofinterestEditProfileAdapter(context, mListrtopicsofinterest, arraylistselectedtopicsofinterest);
            if (topicsofinterestEditProfileAdapteradapter != null) {
                recyclerview_topics_interest.setAdapter(topicsofinterestEditProfileAdapteradapter);

            }
        } else {
            topicsofinterestEditProfileAdapteradapter = new TopicsofinterestEditProfileAdapter(context, mListrtopicsofinterest, arraylistselectedtopicsofinterest);
            if (topicsofinterestEditProfileAdapteradapter != null) {
                recyclerview_topics_interest.setAdapter(topicsofinterestEditProfileAdapteradapter);
            }

        }

        myDialog.show();


    }

    public void GetCountry() {

        mAPIService.GetCountry().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CountryModel>() {
                    @Override
                    public void onCompleted() {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (Constant.isNetworkAvailable(context)) {
                            GetState(country_pos);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(CountryModel CountryModel) {


                        getcountryarraylist.clear();
                        getcountryarray.clear();

                        getcountryarraylist.add(getResources().getString(R.string.str_select_country));


                        if (CountryModel.getPayload().getCountry().size() > 0) {
                            for (int i = 0; i < CountryModel.getPayload().getCountry().size(); i++) {
                                getcountryarraylist.add(CountryModel.getPayload().getCountry().get(i).getName());
                            }

                            for (int j = 0; j < CountryModel.getPayload().getCountry().size(); j++) {
                                CountryItem countryItem = new CountryItem();
                                for (int i = 0; i < CountryModel.getPayload().getCountry().size(); i++) {
                                    countryItem.setName(CountryModel.getPayload().getCountry().get(j).getName());
                                    countryItem.setId(CountryModel.getPayload().getCountry().get(j).getId());
                                }
                                getcountryarray.add(countryItem);
                            }

                            Show_Country_Adapter();
                        } else {
                            //Constant.ShowPopUp("Country Not Found", context);
                        }


                    }
                });

    }


    public void GetCity(int state_id) {

        mAPIService.GetCity(state_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CityModel>() {
                    @Override
                    public void onCompleted() {


                        if (Constant.isNetworkAvailable(context)) {
                            GetUserType();
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(CityModel cityModel) {

                        getcityarraylist.clear();
                        getcityarray.clear();

                        getcityarraylist.add(getResources().getString(R.string.str_select_city));


                        if (cityModel.getPayload().getCity().size() > 0) {
                            for (int i = 0; i < cityModel.getPayload().getCity().size(); i++) {
                                getcityarraylist.add(cityModel.getPayload().getCity().get(i).getName());
                            }

                            for (int j = 0; j < cityModel.getPayload().getCity().size(); j++) {
                                CityItem cityItem = new CityItem();
                                for (int i = 0; i < cityModel.getPayload().getCity().size(); i++) {
                                    cityItem.setId(cityModel.getPayload().getCity().get(i).getId());
                                    cityItem.setName(cityModel.getPayload().getCity().get(i).getName());
                                }
                                getcityarray.add(cityItem);

                            }
                            Show_City_Adapter();

                        } else {
                            //Constant.ShowPopUp("City Not Found", context);
                        }


                    }
                });

    }

    public void GetState(int country_id) {

        mAPIService.GetState(country_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StateModel>() {
                    @Override
                    public void onCompleted() {


                        if (Constant.isNetworkAvailable(context)) {
                            GetCity(state_pos);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(StateModel stateModel) {


                        getstatearralist.clear();
                        getstatearray.clear();

                        getstatearralist.add(getResources().getString(R.string.str_select_state));


                        if (stateModel.getPayload().getState().size() > 0) {
                            for (int i = 0; i < stateModel.getPayload().getState().size(); i++) {
                                getstatearralist.add(stateModel.getPayload().getState().get(i).getName());

                            }

                            for (int j = 0; j < stateModel.getPayload().getState().size(); j++) {

                                StateItem stateItem = new StateItem();

                                for (int i = 0; i < stateModel.getPayload().getState().size(); i++) {
                                    stateItem.setId(stateModel.getPayload().getState().get(j).getId());
                                    stateItem.setName(stateModel.getPayload().getState().get(j).getName());

                                }
                                getstatearray.add(stateItem);
                            }

                            Show_State_Adapter();
                        } else {
                            // Constant.ShowPopUp("State Not Found", context);
                        }


                    }
                });

    }


    public Boolean Validation() {

        if (binding.edtFirstname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_firstname), context);
            return false;
        } else if (binding.edtLastname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_lastname), context);
            return false;
        } else if (binding.edtFirmname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_firm_name_register), context);
            return false;
        } else if (binding.edtMobileNumber.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_mobile_number), context);
            return false;
        } else if (country_id == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.str_country), context);
            return false;
        } else if (state_id == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.str_state), context);
            return false;
        } else if (city_id == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.str_city), context);
            return false;
        } else if (binding.edtZipcode.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_zipcode), context);
            return false;
        } else if (who_you_are_pos == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.val_user_type), context);
            return false;
        } else if (arraylistselectedtopicsofinterest.size() == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.val_topics), context);
            return false;
        } else {
            return true;
        }


    }


}
