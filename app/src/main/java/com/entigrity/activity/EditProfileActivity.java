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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.entigrity.view.UsPhoneNumberFormatter;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.lang.ref.WeakReference;
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

    public boolean checkstatearray = false;
    public boolean checkcityarray = false;


    private String State, City;

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
            State = intent.getStringExtra(getResources().getString(R.string.pass_state_text));
            City = intent.getStringExtra(getResources().getString(R.string.pass_city_text));
            zipcode = intent.getStringExtra(getResources().getString(R.string.pass_zipcode));
            country_pos = intent.getIntExtra(getResources().getString(R.string.pass_country), 0);
            state_pos = intent.getIntExtra(getResources().getString(R.string.pass_state), 0);
            city_pos = intent.getIntExtra(getResources().getString(R.string.pass_city), 0);
            who_you_are_pos = intent.getIntExtra(getResources().getString(R.string.pass_who_you_are), 0);
            arraylistselectedtopicsofinterest = intent.getIntegerArrayListExtra(getResources().getString(R.string.pass_topics_of_interesr));

            SetData();
        }

        binding.edtMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(binding.edtMobileNumber));
        binding.edtMobileNumber.addTextChangedListener(addLineNumberFormatter);


        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_country_spinner) {
                    boolean_country_spinner = false;
                } else {

                    if (position != 0) {


                        country_id = getcountryarray.get(position - 1).getId();

                        State = "";

                        if (Constant.isNetworkAvailable(context)) {
                            GetState(country_id);
                        } else {
                            Snackbar.make(binding.btnsubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            GetState(country_id);
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.webinar_status))
                                    .show();
                        }
                    } else {

                        country_id = position;

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


                        state_id = getstatearray.get(position - 1).getId();


                        City = "";

                        if (Constant.isNetworkAvailable(context)) {
                            GetCity(state_id);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }

                    } else {

                        state_id = position;

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
                    } else {
                        who_you_are_pos = position;

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
            Snackbar.make(binding.btnsubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Constant.isNetworkAvailable(context)) {
                                progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                                GetTopicsOfInterset();
                            }
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.webinar_status))
                    .show();

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
                                Constant.Trim(binding.edtFirstname.getText().toString()), Constant.Trim(binding.edtLastname.getText().toString()),
                                Constant.Trim(binding.edtEmailname.getText().toString()), Constant.Trim(binding.edtFirmname.getText().toString()), country_id, state_id, city_id, Integer.parseInt(Constant.Trim(binding.edtZipcode.getText().toString())), Constant.Trim(binding.edtMobileNumber.getText()
                                        .toString()), arraylistselectedtopicsofinterest, who_you_are_pos);
                    } else {
                        Snackbar.make(binding.btnsubmit, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Constant.isNetworkAvailable(context)) {
                                            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                                            EditPost(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context),
                                                    Constant.Trim(binding.edtFirstname.getText().toString()), Constant.Trim(binding.edtLastname.getText().toString()),
                                                    Constant.Trim(binding.edtEmailname.getText().toString()), Constant.Trim(binding.edtFirmname.getText().toString()), country_id, state_id, city_id, Integer.parseInt(Constant.Trim(binding.edtZipcode.getText().toString())), Constant.Trim(binding.edtMobileNumber.getText()
                                                            .toString()), arraylistselectedtopicsofinterest, who_you_are_pos);
                                        }
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.webinar_status))
                                .show();
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
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ShowAdapter();
                        // Constant.Log(TAG, "who_you_are_pos" + who_you_are_pos);
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


                        for (int i = 0; i < arrayLististusertypeid.size(); i++) {


                            if (who_you_are_pos == arrayLististusertypeid.get(i)) {
                                who_you_are_pos = arrayLististusertypeid.indexOf(arrayLististusertypeid.get(i) + 1);


                            }
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


    public void Show_City_else_Adapter() {
        if (getcityarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getcityarraylist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerCity.setAdapter(aa);


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
            if (checkcityarray == true) {
                binding.spinnerCity.setSelection(1);
            } else {
                binding.spinnerCity.setSelection(city_pos);
            }


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

            if (checkstatearray == true) {
                binding.spinnerState.setSelection(1);
            } else {
                binding.spinnerState.setSelection(state_pos);
            }


        }


    }


    public void Show_State_else_Adapter() {
        if (getstatearralist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getstatearralist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerState.setAdapter(aa);


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
                            checkcityarray = false;

                            Show_City_Adapter();

                        } else {

                            if (!City.equalsIgnoreCase("") && City != null) {
                                checkcityarray = true;
                                getcityarraylist.add(City);
                                Show_City_Adapter();
                            } else {
                                Show_City_else_Adapter();
                            }


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

                            checkstatearray = false;


                            Show_State_Adapter();
                        } else {

                            if (!State.equalsIgnoreCase("") && State != null) {
                                checkstatearray = true;
                                getstatearralist.add(State);
                                Show_State_Adapter();
                            } else {
                                Show_State_else_Adapter();
                            }


                        }


                    }
                });

    }


    public Boolean Validation() {
        if (Constant.Trim(binding.edtFirstname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtFirstname, getResources().getString(R.string.val_firstname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtLastname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtLastname, getResources().getString(R.string.val_lastname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtFirmname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtFirmname, getResources().getString(R.string.val_firm_name_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtMobileNumber.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.val_mobile_number), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (country_id == 0) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.str_country), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (state_id == 0) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.str_state), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (city_id == 0) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.str_city), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtZipcode.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.val_zipcode), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (who_you_are_pos == 0) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.val_user_type), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (arraylistselectedtopicsofinterest.size() == 0) {
            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.val_topics), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }


    }


}
