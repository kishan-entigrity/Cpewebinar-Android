package com.entigrity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.model.viewprofile.ViewProfileModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
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
    public TopicsofinterestEditProfileAdapter TopicsofinterestEditProfileAdapteradapter;
    public ArrayList<Integer> arraylistselectedtopicsofinterest = new ArrayList<Integer>();
    public TextView tv_apply, tv_cancel;
    private static final String TAG = EditProfileActivity.class.getName();
    private ArrayList<String> arrayLististusertype = new ArrayList<String>();
    private ArrayList<TopicsofInterest> mListrtopicsofinterest = new ArrayList<TopicsofInterest>();


    public ArrayList<String> getcountryarraylist = new ArrayList<String>();
    public ArrayList<CountryItem> getcountryarray = new ArrayList<CountryItem>();


    public ArrayList<String> getstatearralist = new ArrayList<String>();
    public ArrayList<StateItem> getstatearray = new ArrayList<>();

    public ArrayList<String> getcityarraylist = new ArrayList<String>();
    public ArrayList<CityItem> getcityarray = new ArrayList<CityItem>();

    ArrayList<String> saveselectedlist = new ArrayList<String>();

    public Dialog myDialog_popup;
    public TextView tv_popup_msg, tv_popup_submit;
    private int country_id = 0;
    private int state_id = 0;
    private int city_id = 0;
    private int country_pos = 0;
    private int state_pos = 0;
    private int city_pos = 0;
    private int who_you_are_pos = 0;
    private String who_you_are = "";

    ProgressDialog progressDialog;


    public boolean boolean_country_spinner = true;
    public boolean boolean_state_spinner = true;
    public boolean boolean_city_spinner = true;
    public boolean boolean_usertype_spinner = true;


    private int user_type = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        context = EditProfileActivity.this;
        mAPIService = ApiUtils.getAPIService();


        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_country_spinner) {
                    boolean_country_spinner = false;
                } else {
                    country_id = position;

                    if (country_id > 0) {
                        if (Constant.isNetworkAvailable(context)) {
                            GetState(country_id);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.str_country), context);
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
                    state_id = position;

                    if (state_id > 0) {
                        if (Constant.isNetworkAvailable(context)) {
                            GetCity(state_id);
                        } else {
                            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                        }

                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.str_select_state), context);
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
                    city_id = position;

                    if (city_id == 0) {
                        Constant.ShowPopUp(getResources().getString(R.string.str_select_city), context);
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
                    user_type = position;

                    if (user_type == 0) {
                        Constant.ShowPopUp(getResources().getString(R.string.val_user_type), context);
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
                                        .toString(), arraylistselectedtopicsofinterest, user_type);
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


    public void GetProfile() {
        mAPIService.GetProfile(getResources().getString(R.string.bearer) + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewProfileModel>() {
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
                                for (int k = 0; k < viewProfileModel.getPayload().getData().getTags().size(); k++) {
                                    saveselectedlist.add(viewProfileModel.getPayload().getData().getTags().get(k).toString());
                                }
                                for (int i = 0; i < mListrtopicsofinterest.size(); i++) {
                                    for (int j = 0; j < saveselectedlist.size(); j++) {
                                        if (mListrtopicsofinterest.get(i).getPayload().getTags().get(i).getTag().equals(saveselectedlist.get(j))) {
                                            arraylistselectedtopicsofinterest.add(mListrtopicsofinterest.get(i).getPayload().getTags().get(i).getId());
                                        }
                                    }
                                }
                            }

                            if (viewProfileModel.getPayload().getData().getCountry() != null
                                    && !viewProfileModel.getPayload().getData().getCountry().equalsIgnoreCase("")) {
                                country_pos = Integer.parseInt(viewProfileModel.getPayload().getData().getCountryId());
                                country_id = country_pos;
                            }
                            if (viewProfileModel.getPayload().getData().getState() != null
                                    && !viewProfileModel.getPayload().getData().getState().equalsIgnoreCase("")) {
                                state_pos = Integer.parseInt(viewProfileModel.getPayload().getData().getStateId());
                                state_id = state_pos;
                            }

                            if (viewProfileModel.getPayload().getData().getCity() != null
                                    && !viewProfileModel.getPayload().getData().getCity().equalsIgnoreCase("")) {
                                city_pos = Integer.parseInt(viewProfileModel.getPayload().getData().getCityId());
                                city_id = city_pos;

                            }

                            if (viewProfileModel.getPayload().getData().getZipcode() != null
                                    && !viewProfileModel.getPayload().getData().getZipcode().equalsIgnoreCase("")) {
                                binding.edtZipcode.setText(viewProfileModel.getPayload().getData().getZipcode());
                            }


                            if (viewProfileModel.getPayload().getData().getUserType() != null
                                    && !viewProfileModel.getPayload().getData().getUserType().equalsIgnoreCase("")) {

                                who_you_are = viewProfileModel.getPayload().getData().getUserType();

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
                            ShowPopUpSucess(editProfileModel.getMessage(), context);

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

    public void ShowPopUpSucess(String message, final Context context) {
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

                        for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                            arrayLististusertype.add(userTypeModel.getPayload().getUserType().get(i).getName());
                        }

                        for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                            if (userTypeModel.getPayload().getUserType().get(i).getName().equals(who_you_are)) {
                                who_you_are_pos = i;
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
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayLististusertype);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(aa);


        }
    }


    public void Show_Country_Adapter() {
        if (getcountryarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //binding.spinnerCountry.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getcountryarraylist);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getcityarraylist);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getstatearralist);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerState.setAdapter(aa);
            binding.spinnerState.setSelection(state_pos);

        }


    }


    public void GetTopicsOfInterset() {
        mAPIService.GetTopicsofInterest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopicsofInterest>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetProfile();
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
                        mListrtopicsofinterest.add(topicsofInterest);


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
        TopicsofinterestEditProfileAdapteradapter = new TopicsofinterestEditProfileAdapter(context, mListrtopicsofinterest, saveselectedlist);
        tv_apply = (TextView) myDialog.findViewById(R.id.tv_apply);
        tv_cancel = (TextView) myDialog.findViewById(R.id.tv_cancel);


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TopicsofinterestEditProfileAdapteradapter.arraylistselectedtag.size() > 0) {
                    TopicsofinterestEditProfileAdapteradapter.arraylistselectedtag.clear();
                }


                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

            }
        });

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arraylistselectedtopicsofinterest.clear();

                if (TopicsofinterestEditProfileAdapteradapter.arraylistselectedtag.size() > 0) {
                    arraylistselectedtopicsofinterest.addAll(TopicsofinterestEditProfileAdapteradapter.arraylistselectedtag);
                    TopicsofinterestEditProfileAdapteradapter.arraylistselectedtag.clear();
                }


                if (myDialog.isShowing()) {
                    myDialog.dismiss();

                }

            }
        });


        if (mListrtopicsofinterest.size() > 0) {
            recyclerview_topics_interest.setAdapter(TopicsofinterestEditProfileAdapteradapter);
        }

        myDialog.show();


    }

    public void GetCountry() {

        mAPIService.GetCountry().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CountryModel>() {
                    @Override
                    public void onCompleted() {
                        Show_Country_Adapter();
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

                        getcountryarraylist.add(getResources().getString(R.string.str_select_country));


                        for (int i = 0; i < CountryModel.getPayload().getCountry().size(); i++) {
                            getcountryarraylist.add(CountryModel.getPayload().getCountry().get(i).getName());
                        }

                        for (int j = 0; j < CountryModel.getPayload().getCountry().size(); j++) {
                            com.entigrity.model.country.CountryItem countryItem = new com.entigrity.model.country.CountryItem();
                            for (int i = 0; i < CountryModel.getPayload().getCountry().size(); i++) {
                                countryItem.setName(CountryModel.getPayload().getCountry().get(j).getName());
                                countryItem.setId(CountryModel.getPayload().getCountry().get(j).getId());
                            }
                            getcountryarray.add(countryItem);
                        }


                    }
                });

    }


    public void GetCity(int state_id) {

        mAPIService.GetCity(state_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CityModel>() {
                    @Override
                    public void onCompleted() {
                        Show_City_Adapter();

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
                    }
                });

    }

    public void GetState(int country_id) {

        mAPIService.GetState(country_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StateModel>() {
                    @Override
                    public void onCompleted() {
                        Show_State_Adapter();

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
        } else if (binding.edtZipcode.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_zipcode), context);
            return false;
        } else if (arraylistselectedtopicsofinterest.size() == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.val_topics), context);
            return false;
        } else {
            return true;
        }

    }


}
