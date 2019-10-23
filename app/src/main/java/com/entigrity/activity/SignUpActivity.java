package com.entigrity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.ActivitySignupBinding;
import com.entigrity.model.EmailValidation.emailvalidationmodel;
import com.entigrity.model.city.CityItem;
import com.entigrity.model.city.CityModel;
import com.entigrity.model.country.CountryItem;
import com.entigrity.model.country.CountryModel;
import com.entigrity.model.state.StateItem;
import com.entigrity.model.state.StateModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.UsPhoneNumberFormatter;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpActivity extends AppCompatActivity {
    public Context context;
    ActivitySignupBinding binding;
    private APIService mAPIService_new;
    private static final String TAG = SignUpActivity.class.getName();
    private boolean checkpasswordvisiblestatus = false;
    private boolean checkconfirmpasswordvisiblestatus = false;

    ProgressDialog progressDialog;
    public boolean boolean_country_spinner = true;
    public boolean boolean_state_spinner = true;
    public boolean boolean_city_spinner = true;
    public ArrayList<String> getcountryarraylist = new ArrayList<String>();
    public ArrayList<CountryItem> getcountryarray = new ArrayList<CountryItem>();


    public ArrayList<String> getstatearralist = new ArrayList<String>();
    public ArrayList<StateItem> getstatearray = new ArrayList<>();


    public ArrayList<String> getcityarraylist = new ArrayList<String>();
    public ArrayList<CityItem> getcityarray = new ArrayList<CityItem>();

    private int country_id = 0;
    private int state_id = 0;
    private int city_id = 0;

    private int country_id_pos = 0;
    private int state_id_pos = 0;
    private int city_id_pos = 0;

    public String firstname = "", lastname = "", emailID = "", phonenumber = "", password = "", confirmpassword = "";

    private String isemailexist = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = SignUpActivity.this;
        mAPIService_new = ApiUtilsNew.getAPIService();

        AppSettings.set_device_id(context, Constant.GetDeviceid(context));


        Intent intent = getIntent();
        if (intent != null) {
            firstname = intent.getStringExtra(getResources().getString(R.string.str_signup_first_name));
            lastname = intent.getStringExtra(getResources().getString(R.string.str_signup_last_name));
            emailID = intent.getStringExtra(getResources().getString(R.string.str_signup_email_id));
            phonenumber = intent.getStringExtra(getResources().getString(R.string.str_signup_phone_number));
            password = intent.getStringExtra(getResources().getString(R.string.str_signup_password));
            confirmpassword = intent.getStringExtra(getResources().getString(R.string.str_signup_confirm_password));
            country_id = intent.getIntExtra(getResources().getString(R.string.str_signup_country_id), 0);
            state_id = intent.getIntExtra(getResources().getString(R.string.str_signup_state_id), 0);
            city_id = intent.getIntExtra(getResources().getString(R.string.str_signup_city_id), 0);

            country_id_pos = intent.getIntExtra(getResources().getString(R.string.str_signup_country_pos), 0);
            state_id_pos = intent.getIntExtra(getResources().getString(R.string.str_signup_state_pos), 0);
            city_id_pos = intent.getIntExtra(getResources().getString(R.string.str_signup_city_pos), 0);


            binding.edtFirstname.setText(firstname);
            binding.edtLastname.setText(lastname);
            binding.edtEmailid.setText(emailID);
            binding.edtPhoneNumber.setText(phonenumber);
            binding.edtPassword.setText(password);
            binding.edtConfirmpassword.setText(confirmpassword);


        }


        binding.edtPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(binding.edtPhoneNumber));
        binding.edtPhoneNumber.addTextChangedListener(addLineNumberFormatter);


        binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }


                if (s.length() == 14) {
                    Constant.hideKeyboard((Activity) context);
                }

            }
        });


        binding.edtEmailid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Do you job here which you want to done through event

                    Constant.hideKeyboard((Activity) context);

                    if (!Constant.Trim(binding.edtEmailid.getText().toString()).isEmpty()) {
                        if (Constant.isNetworkAvailable(context)) {
                            CheckEmail(Constant.Trim(binding.edtEmailid.getText().toString()));
                        } else {
                            Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }

                    } else {
                        Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid), Snackbar.LENGTH_SHORT).show();
                    }


                }
                return false;
            }
        });

        binding.edtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (!Constant.Trim(binding.edtEmailid.getText().toString()).isEmpty()) {
                    if (Constant.isNetworkAvailable(context)) {
                        CheckEmail(Constant.Trim(binding.edtEmailid.getText().toString()));
                    } else {
                        Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid), Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetCountry();
        } else {
            Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtPassword.getRight() - binding.edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if (binding.edtPassword.getText().length() > 0) {
                            if (checkpasswordvisiblestatus == false) {
                                checkpasswordvisiblestatus = true;
                                binding.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eye, 0);
                                binding.edtPassword.setSelection(binding.edtPassword.length());

                            } else {
                                checkpasswordvisiblestatus = false;
                                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eys, 0);
                                binding.edtPassword.setSelection(binding.edtPassword.length());
                            }

                        } else {
                            Snackbar.make(binding.edtPassword, getResources().getString(R.string.validate_password), Snackbar.LENGTH_SHORT).show();


                        }


                        return true;
                    }
                }


                return false;
            }
        });


        binding.edtConfirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtConfirmpassword.getRight() - binding.edtConfirmpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if (binding.edtConfirmpassword.getText().length() > 0) {
                            if (checkconfirmpasswordvisiblestatus == false) {
                                checkconfirmpasswordvisiblestatus = true;
                                binding.edtConfirmpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                binding.edtConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eye, 0);
                                binding.edtConfirmpassword.setSelection(binding.edtConfirmpassword.length());

                            } else {
                                checkconfirmpasswordvisiblestatus = false;
                                binding.edtConfirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.edtConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.password_eys, 0);
                                binding.edtConfirmpassword.setSelection(binding.edtPassword.length());
                            }

                        } else {
                            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.validate_confirmpassword), Snackbar.LENGTH_SHORT).show();
                        }


                        return true;
                    }
                }


                return false;
            }
        });


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_country_spinner) {
                    boolean_country_spinner = false;
                } else {
                    if (getcountryarraylist.get(position).equalsIgnoreCase("Country")) {
                        country_id = 0;
                    } else {
                        country_id = getcountryarray.get(position - 1).getId();
                        country_id_pos = position;

                        Log.e("countryid", "countryid" + country_id_pos);

                        if (Constant.isNetworkAvailable(context)) {
                            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                            GetState(country_id);
                        } else {
                            Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }


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
                    if (getstatearralist.get(position).equalsIgnoreCase("State")) {
                        state_id = 0;
                    } else {
                        state_id = getstatearray.get(position - 1).getId();

                        state_id_pos = position;


                        if (Constant.isNetworkAvailable(context)) {
                            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                            GetCity(state_id);
                        } else {
                            Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                        }
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
                    if (getcityarraylist.get(position).equalsIgnoreCase("City")) {
                        city_id = 0;
                    } else {
                        city_id = getcityarray.get(position - 1).getId();

                        city_id_pos = position;
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.isNetworkAvailable(context)) {
                    if (Validation()) {

                        Intent i = new Intent(SignUpActivity.this, SignUpNextActivity.class);
                        i.putExtra(context.getResources().getString(R.string.str_signup_first_name), Constant.Trim(binding.edtFirstname.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_last_name), Constant.Trim(binding.edtLastname.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_email_id), Constant.Trim(binding.edtEmailid.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_phone_number), Constant.Trim(binding.edtPhoneNumber.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_password), Constant.Trim(binding.edtPassword.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_confirm_password), Constant.Trim(binding.edtConfirmpassword.getText().toString()));
                        i.putExtra(context.getResources().getString(R.string.str_signup_country_id), country_id);
                        i.putExtra(context.getResources().getString(R.string.str_signup_state_id), state_id);
                        i.putExtra(context.getResources().getString(R.string.str_signup_city_id), city_id);
                        i.putExtra(context.getResources().getString(R.string.str_signup_country_pos), country_id_pos);
                        i.putExtra(context.getResources().getString(R.string.str_signup_state_pos), state_id_pos);
                        i.putExtra(context.getResources().getString(R.string.str_signup_city_pos), city_id_pos);

                        startActivity(i);
                        finish();

                    }
                } else {
                    Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void GetCity(int state_id) {

        mAPIService_new.GetCity(getResources().getString(R.string.accept), state_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CityModel>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.btnNext, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(CityModel cityModel) {


                        if (cityModel.isSuccess() == true) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            getcityarraylist.clear();
                            getcityarray.clear();

                            getcityarraylist.add("City");


                            if (cityModel.getPayload().getCity().size() > 0) {
                                for (int i = 0; i < cityModel.getPayload().getCity().size(); i++) {
                                    getcityarraylist.add(cityModel.getPayload().getCity().get(i).getName());
                                }

                                for (int j = 0; j < cityModel.getPayload().getCity().size(); j++) {
                                    CityItem cityItem = new CityItem();
                                    for (int i = 0; i < cityModel.getPayload().getCity().size(); i++) {
                                        cityItem.setId(cityModel.getPayload().getCity().get(j).getId());
                                        cityItem.setName(cityModel.getPayload().getCity().get(j).getName());
                                    }
                                    getcityarray.add(cityItem);

                                }


                                Show_City_Adapter();

                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Snackbar.make(binding.btnNext, cityModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    private void Show_City_Adapter() {

        if (getcityarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getcityarraylist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerCity.setAdapter(aa);
            binding.spinnerCity.setSelection(city_id_pos);


        }


    }

    private void GetState(int country_id) {

        mAPIService_new.GetState(getResources().getString(R.string.accept), country_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StateModel>() {
                    @Override
                    public void onCompleted() {
                        if (state_id_pos != 0) {
                            if (Constant.isNetworkAvailable(context)) {
                                GetCity(state_id_pos);
                            } else {
                                Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.btnNext, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(StateModel stateModel) {


                        if (stateModel.isSuccess() == true) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            getstatearralist.clear();
                            getstatearray.clear();

                            getstatearralist.add("State");

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
                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Snackbar.make(binding.btnNext, stateModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }


    private void CheckEmail(final String email) {

        mAPIService_new.CheckEmailValidation(getResources().getString(R.string.accept), email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<emailvalidationmodel>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {


                        isemailexist = "";

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } /*else {
                            Snackbar.make(binding.btnNext, message, Snackbar.LENGTH_SHORT).show();
                        }*/


                    }

                    @Override
                    public void onNext(emailvalidationmodel emailvalidationmodel) {


                        if (emailvalidationmodel.isSuccess() == true) {

                            Constant.hideKeyboard((Activity) context);

                            isemailexist = emailvalidationmodel.getMessage();

                            Snackbar.make(binding.btnNext, emailvalidationmodel.getMessage(), Snackbar.LENGTH_SHORT).show();

                        } else {


                            isemailexist = "";


                        }


                    }
                });
    }


    private void Show_State_Adapter() {

        if (getstatearralist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getstatearralist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerState.setAdapter(aa);

            binding.spinnerState.setSelection(state_id_pos);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(context, LoginActivity.class);
        startActivity(i);
        finish();
    }


    public void GetCountry() {

        mAPIService_new.GetCountry(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CountryModel>() {
                    @Override
                    public void onCompleted() {

                        if (country_id_pos != 0) {
                            if (Constant.isNetworkAvailable(context)) {
                                GetState(country_id_pos);
                            } else {
                                Snackbar.make(binding.btnNext, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            getstatearralist.add("State");
                            getcityarraylist.add("City");
                            Show_State_Adapter();
                            Show_City_Adapter();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);

                        if (Constant.status_code == 401) {
                            MainActivity.getInstance().AutoLogout();
                        } else {
                            Snackbar.make(binding.btnNext, message, Snackbar.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onNext(CountryModel CountryModel) {


                        if (CountryModel.isSuccess() == true) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            getcountryarraylist.clear();
                            getcountryarray.clear();

                            getcountryarraylist.add("Country");


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
                            }
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Snackbar.make(binding.btnNext, CountryModel.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    public void Show_Country_Adapter() {
        if (getcountryarraylist.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, getcountryarraylist);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerCountry.setAdapter(aa);
            binding.spinnerCountry.setSelection(country_id_pos);


        }


    }


    public Boolean Validation() {

        if (Constant.Trim(binding.edtFirstname.getText().toString()).isEmpty()) {
            binding.edtFirstname.requestFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtFirstname, getResources().getString(R.string.val_firstname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtLastname.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.requestFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtLastname, getResources().getString(R.string.val_lastname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtEmailid.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.requestFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.isValidEmailId(Constant.Trim(binding.edtEmailid.getText().toString()))) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.requestFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid_pattern), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPhoneNumber.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.requestFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_phone_number), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPhoneNumber.getText().toString()).length() < 14) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.requestFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtPhoneNumber, getResources().getString(R.string.val_phone_validate), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.requestFocus();
            binding.edtConfirmpassword.clearFocus();

            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).length() < 6) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.requestFocus();
            binding.edtConfirmpassword.clearFocus();

            Snackbar.make(binding.edtPassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).isEmpty()) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.requestFocus();


            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.val_confirm_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).length() < 6) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.requestFocus();


            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.Trim(binding.edtPassword.getText().toString()).equals(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_confirm_password_not_match), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (country_id == 0) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.spinnerCountry, getResources().getString(R.string.str_country), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (state_id == 0) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.spinnerState, getResources().getString(R.string.str_state), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (city_id == 0) {

            binding.edtFirstname.clearFocus();
            binding.edtLastname.clearFocus();
            binding.edtEmailid.clearFocus();
            binding.edtPhoneNumber.clearFocus();
            binding.edtPassword.clearFocus();
            binding.edtConfirmpassword.clearFocus();


            Snackbar.make(binding.spinnerCity, getResources().getString(R.string.str_city), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!isemailexist.equalsIgnoreCase("")) {
            Snackbar.make(binding.edtEmailid, isemailexist, Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}
