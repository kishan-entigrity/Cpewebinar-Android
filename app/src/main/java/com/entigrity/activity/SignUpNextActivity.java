package com.entigrity.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.adapter.AdditionalQualificationPopUpAdapter;
import com.entigrity.adapter.ProffesionalCredentialPopUpAdapter;
import com.entigrity.databinding.ActivitySignupNextBinding;
import com.entigrity.model.Job_title.ModelJobTitle;
import com.entigrity.model.Proffesional_Credential.Model_proffesional_Credential;
import com.entigrity.model.additional_qualification.Model_additional_qualification;
import com.entigrity.model.educationlist.education_list_Model;
import com.entigrity.model.industry.Model_Industry;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.utility.AppSettings;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.view.UsPhoneNumberFormatter;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpNextActivity extends AppCompatActivity {

    ActivitySignupNextBinding binding;

    private APIService mAPIService_new;
    ProgressDialog progressDialog;

    private int jobtitle_id = 0;
    private int industry_id = 0;
    private TextView tv_header, tv_submit, tv_cancel;


    public boolean boolean_jobtitle_spinner = true;
    public boolean boolean_industry_spinner = true;
    public boolean checkprivacypolicystatus = false;

    public RecyclerView rv_professional_credential;


    private ArrayList<String> arrayListjobtitle = new ArrayList<String>();
    private ArrayList<Integer> arrayListjobtitleid = new ArrayList<Integer>();

    private ArrayList<String> arrayListindustry = new ArrayList<String>();
    private ArrayList<Integer> arrayListindustryid = new ArrayList<Integer>();


    public ArrayList<Model_proffesional_Credential> arraylistModelProffesioanlCredential = new ArrayList<>();
    public ArrayList<Model_additional_qualification> arraylistModeladditionalcredential = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;


    private static final String TAG = SignUpNextActivity.class.getName();

    public Context context;

    private String prefix = "P-";

    public ProffesionalCredentialPopUpAdapter proffesionalCredentialPopUpAdapter;

    public AdditionalQualificationPopUpAdapter additionalQualificationPopUpAdapter;

    public Dialog myDialog_proffesionl_credential;
    public Dialog myDialog_additional_qualification;

    public String firstname = "", lastname = "", emailID = "", phonenumber = "", password = "", confirmpassword = "";

    public int countryid = 0, stateid = 0, cityid = 0;

    public int countrypos = 0, statepos = 0, citypos = 0;

    public String selected_proffesional_credential = "";
    public String selected_additional_qualification = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_next);
        context = SignUpNextActivity.this;
        myDialog_proffesionl_credential = new Dialog(context);
        myDialog_additional_qualification = new Dialog(context);
        mAPIService_new = ApiUtilsNew.getAPIService();

        Intent intent = getIntent();
        if (intent != null) {
            firstname = intent.getStringExtra(getResources().getString(R.string.str_signup_first_name));
            lastname = intent.getStringExtra(getResources().getString(R.string.str_signup_last_name));
            emailID = intent.getStringExtra(getResources().getString(R.string.str_signup_email_id));
            phonenumber = intent.getStringExtra(getResources().getString(R.string.str_signup_phone_number));
            password = intent.getStringExtra(getResources().getString(R.string.str_signup_password));
            confirmpassword = intent.getStringExtra(getResources().getString(R.string.str_signup_confirm_password));
            countryid = intent.getIntExtra(getResources().getString(R.string.str_signup_country_id), 0);
            stateid = intent.getIntExtra(getResources().getString(R.string.str_signup_state_id), 0);
            cityid = intent.getIntExtra(getResources().getString(R.string.str_signup_city_id), 0);
            countrypos = intent.getIntExtra(getResources().getString(R.string.str_signup_country_pos), 0);
            statepos = intent.getIntExtra(getResources().getString(R.string.str_signup_state_pos), 0);
            citypos = intent.getIntExtra(getResources().getString(R.string.str_signup_city_pos), 0);
        }


        binding.edtMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(binding.edtMobileNumber));
        binding.edtMobileNumber.addTextChangedListener(addLineNumberFormatter);

        binding.tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.arraylistselectedproffesionalcredentialID.clear();
                Constant.arraylistselectedproffesionalcredential.clear();
                Constant.hashmap_professional_credential.clear();

                Constant.arraylistselectedadditionalqualificationID.clear();
                Constant.arraylistselectedadditionalqualification.clear();
                Constant.hashmap_additional_qualification.clear();


                Intent i = new Intent(SignUpNextActivity.this, SignUpActivity.class);
                i.putExtra(context.getResources().getString(R.string.str_signup_first_name), firstname);
                i.putExtra(context.getResources().getString(R.string.str_signup_last_name), lastname);
                i.putExtra(context.getResources().getString(R.string.str_signup_email_id), emailID);
                i.putExtra(context.getResources().getString(R.string.str_signup_phone_number), phonenumber);
                i.putExtra(context.getResources().getString(R.string.str_signup_password), password);
                i.putExtra(context.getResources().getString(R.string.str_signup_confirm_password), confirmpassword);
                i.putExtra(context.getResources().getString(R.string.str_signup_country_id), countryid);
                i.putExtra(context.getResources().getString(R.string.str_signup_state_id), stateid);
                i.putExtra(context.getResources().getString(R.string.str_signup_city_id), cityid);
                i.putExtra(context.getResources().getString(R.string.str_signup_country_pos), countrypos);
                i.putExtra(context.getResources().getString(R.string.str_signup_state_pos), statepos);
                i.putExtra(context.getResources().getString(R.string.str_signup_city_pos), citypos);


                startActivity(i);
                finish();


            }
        });


        binding.edtMobileNumber.addTextChangedListener(new TextWatcher() {
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


        binding.edtPtinNumber.setText(prefix);
        binding.edtPtinNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(prefix)) {
                    binding.edtPtinNumber.setText(prefix);
                    binding.edtPtinNumber.setSelection(2);
                }

            }
        });


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetJobTitle();
        } else {
            Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.tvtermsAndCondtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, TermsandConditionActivity.class);
                startActivity(i);

            }
        });
       /* binding.tvTermsAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkprivacypolicystatus == false) {
                    checkprivacypolicystatus = true;
                    binding.ivcheckbox.setImageResource(R.mipmap.check_box_click);
                } else {
                    checkprivacypolicystatus = false;
                    binding.ivcheckbox.setImageResource(R.mipmap.check_box);
                }

            }
        });

        binding.ivcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkprivacypolicystatus == false) {
                    checkprivacypolicystatus = true;
                    binding.ivcheckbox.setImageResource(R.mipmap.check_box_click);
                } else {
                    checkprivacypolicystatus = false;
                    binding.ivcheckbox.setImageResource(R.mipmap.check_box);
                }


            }
        });*/


        binding.spinnerJobTitile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_jobtitle_spinner) {
                    boolean_jobtitle_spinner = false;
                } else {
                    if (arrayListjobtitle.get(position).equalsIgnoreCase("Job Title")) {
                        jobtitle_id = 0;
                    } else {
                        jobtitle_id = arrayListjobtitleid.get(position - 1);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (boolean_industry_spinner) {
                    boolean_industry_spinner = false;
                } else {
                    if (arrayListindustry.get(position).equalsIgnoreCase("Industry")) {
                        industry_id = 0;
                    } else {
                        industry_id = arrayListindustryid.get(position - 1);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.professionalCredential.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ShowProffesionlCredentialPopup();

                return false;
            }
        });

        binding.additionalQualification.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ShowAdditionalQualification();

                return false;
            }
        });


        binding.lvProfessionalCredential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProffesionlCredentialPopup();
            }
        });


        binding.lvAdditionalQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAdditionalQualification();
            }
        });


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.arraylistselectedproffesionalcredentialID.clear();
                Constant.arraylistselectedproffesionalcredential.clear();
                Constant.hashmap_professional_credential.clear();


                Constant.arraylistselectedadditionalqualificationID.clear();
                Constant.arraylistselectedadditionalqualification.clear();
                Constant.hashmap_additional_qualification.clear();


                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation()) {

                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        RegisterPost();
                    } else {
                        Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                    }
                }

            }
        });


    }

    public void RegisterPost(
    ) {

        // RxJava
        mAPIService_new.Register(getResources().getString(R.string.accept), firstname
                , lastname, emailID,
                password, confirmpassword,
                countryid, stateid,
                cityid, Constant.Trim(binding.edtCompanyName.getText().toString())
                , Constant.Trim(binding.edtMobileNumber.getText().toString()),
                phonenumber, Constant.Trim(binding.edtZipcode.getText().toString()),
                Constant.Trim(binding.edtPtinNumber.getText().toString()), jobtitle_id,
                industry_id, selected_proffesional_credential, selected_additional_qualification,
                AppSettings.get_device_id(context), AppSettings.get_device_token(context), Constant.device_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegistrationModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {


                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.btnRegister, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(RegistrationModel registrationModel) {
                        if (registrationModel.isSuccess()) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            Snackbar.make(binding.btnRegister, registrationModel.getMessage(), Snackbar.LENGTH_SHORT).show();

                            Constant.arraylistselectedproffesionalcredentialID.clear();
                            Constant.arraylistselectedproffesionalcredential.clear();
                            Constant.hashmap_professional_credential.clear();

                            Constant.arraylistselectedadditionalqualificationID.clear();
                            Constant.arraylistselectedadditionalqualification.clear();
                            Constant.hashmap_additional_qualification.clear();

                            AppSettings.set_login_token(context, registrationModel.getPayload().getUser().getToken());
                            AppSettings.set_email_id(context, registrationModel.getPayload().getUser().getEmail());


                            Intent i = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();


                        } else if (!registrationModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            Snackbar.make(binding.btnRegister, registrationModel.getMessage(), Snackbar.LENGTH_SHORT).show();


                        }


                    }
                });

    }


    public void GetJobTitle() {

        mAPIService_new.GetJobTitle(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelJobTitle>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetIndustry();
                        } else {
                            Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
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
                            Snackbar.make(binding.btnRegister, message, Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onNext(ModelJobTitle modelJobTitle) {

                        if (modelJobTitle.isSuccess()) {
                            arrayListjobtitle.clear();

                            arrayListjobtitle.add("Job Title/Designation");


                            for (int i = 0; i < modelJobTitle.getPayload().getJobTitle().size(); i++) {
                                arrayListjobtitle.add(modelJobTitle.getPayload().getJobTitle().get(i).getName());
                                arrayListjobtitleid.add(modelJobTitle.getPayload().getJobTitle().get(i).getId());
                            }


                            Show_JobTitle_Adapter();
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.btnRegister, modelJobTitle.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    public void ShowProffesionlCredentialPopup() {
        myDialog_proffesionl_credential.setContentView(R.layout.popup_professional_credential);
        myDialog_proffesionl_credential.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_header = (TextView) myDialog_proffesionl_credential.findViewById(R.id.tv_header);
        tv_submit = (TextView) myDialog_proffesionl_credential.findViewById(R.id.tv_submit);
        tv_cancel = (TextView) myDialog_proffesionl_credential.findViewById(R.id.tv_cancel);
        rv_professional_credential = (RecyclerView) myDialog_proffesionl_credential.findViewById(R.id.rv_professional_credential);

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_professional_credential.setLayoutManager(linearLayoutManager);
        rv_professional_credential.addItemDecoration(new SimpleDividerItemDecoration(context));
        tv_header.setText(context.getResources().getString(R.string.str_profestional_credential));

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (myDialog_proffesionl_credential.isShowing()) {
                    myDialog_proffesionl_credential.cancel();
                }

                ArrayList<Integer> myArrayList = new ArrayList<Integer>(new LinkedHashSet<Integer>(Constant.arraylistselectedproffesionalcredentialID));


                if (myArrayList.size() > 0) {

                    StringBuilder commaSepValueBuilder = new StringBuilder();

                    //Looping through the list
                    for (int i = 0; i < myArrayList.size(); i++) {
                        //append the value into the builder
                        commaSepValueBuilder.append(myArrayList.get(i));

                        //if the value is not the last element of the list
                        //then append the comma(,) as well
                        if (i != myArrayList.size() - 1) {
                            commaSepValueBuilder.append(",");
                        }
                    }
                    //System.out.println(commaSepValueBuilder.toString());
                    selected_proffesional_credential = commaSepValueBuilder.toString();

                    System.out.println(selected_proffesional_credential);


                } else {
                    selected_proffesional_credential = "";
                    Snackbar.make(tv_submit, context.getResources().getString(R.string.validation_professional_credential), Snackbar.LENGTH_SHORT).show();
                }

                if (Constant.arraylistselectedproffesionalcredential.size() > 0) {
                    binding.professionalCredential.setVisibility(View.GONE);
                    binding.lvProfessionalCredential.setVisibility(View.VISIBLE);
                    binding.tvProfessionalCredential.setVisibility(View.VISIBLE);
                    binding.tvProfessionalCredential.setText(Constant.arraylistselectedproffesionalcredential.get(0));
                    if (Constant.arraylistselectedproffesionalcredential.size() > 1) {
                        int more = Constant.arraylistselectedproffesionalcredential.size() - 1;
                        binding.tvProfessionalCredentialMore.setVisibility(View.VISIBLE);
                        binding.tvProfessionalCredentialMore.setText(("+" + more
                                + " more"));
                    } else {
                        binding.tvProfessionalCredentialMore.setVisibility(View.GONE);
                    }
                } else {
                    binding.professionalCredential.setVisibility(View.VISIBLE);
                    binding.lvProfessionalCredential.setVisibility(View.GONE);
                    binding.tvProfessionalCredential.setVisibility(View.GONE);
                    binding.tvProfessionalCredentialMore.setVisibility(View.GONE);
                }


            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog_proffesionl_credential.isShowing()) {
                    myDialog_proffesionl_credential.dismiss();
                }

            }
        });


        myDialog_proffesionl_credential.show();


        if (arraylistModelProffesioanlCredential.size() > 0) {
            proffesionalCredentialPopUpAdapter = new ProffesionalCredentialPopUpAdapter(context,
                    arraylistModelProffesioanlCredential);
            rv_professional_credential.setAdapter(proffesionalCredentialPopUpAdapter);
        }


    }

    public void ShowAdditionalQualification() {


        myDialog_additional_qualification.setContentView(R.layout.popup_professional_credential);
        myDialog_additional_qualification.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_header = (TextView) myDialog_additional_qualification.findViewById(R.id.tv_header);
        tv_submit = (TextView) myDialog_additional_qualification.findViewById(R.id.tv_submit);
        tv_cancel = (TextView) myDialog_additional_qualification.findViewById(R.id.tv_cancel);
        rv_professional_credential = (RecyclerView) myDialog_additional_qualification.findViewById(R.id.rv_professional_credential);

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_professional_credential.setLayoutManager(linearLayoutManager);
        rv_professional_credential.addItemDecoration(new SimpleDividerItemDecoration(context));
        tv_header.setText(context.getResources().getString(R.string.str_additional_qualification));

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (myDialog_additional_qualification.isShowing()) {
                    myDialog_additional_qualification.cancel();
                }

                ArrayList<Integer> myArrayList = new ArrayList<Integer>(new LinkedHashSet<Integer>(Constant.arraylistselectedadditionalqualificationID));


                if (myArrayList.size() > 0) {

                    StringBuilder commaSepValueBuilder = new StringBuilder();

                    //Looping through the list
                    for (int i = 0; i < myArrayList.size(); i++) {
                        //append the value into the builder
                        commaSepValueBuilder.append(myArrayList.get(i));

                        //if the value is not the last element of the list
                        //then append the comma(,) as well
                        if (i != myArrayList.size() - 1) {
                            commaSepValueBuilder.append(",");
                        }
                    }
                    //System.out.println(commaSepValueBuilder.toString());
                    selected_additional_qualification = commaSepValueBuilder.toString();

                    System.out.println(selected_additional_qualification);


                } else {
                    selected_additional_qualification = "";
                    Snackbar.make(tv_submit, context.getResources().getString(R.string.validation_additional_qualification), Snackbar.LENGTH_SHORT).show();
                }

                if (Constant.arraylistselectedadditionalqualification.size() > 0) {
                    binding.additionalQualification.setVisibility(View.GONE);
                    binding.lvAdditionalQualification.setVisibility(View.VISIBLE);
                    binding.tvAdditionalQualification.setVisibility(View.VISIBLE);
                    binding.tvAdditionalQualification.setText(Constant.arraylistselectedadditionalqualification.get(0));
                    if (Constant.arraylistselectedadditionalqualification.size() > 1) {
                        int more = Constant.arraylistselectedadditionalqualification.size() - 1;
                        binding.tvAdditionalQualificationMore.setVisibility(View.VISIBLE);
                        binding.tvAdditionalQualificationMore.setText(("+" + more
                                + " more"));
                    } else {
                        binding.tvAdditionalQualificationMore.setVisibility(View.GONE);
                    }
                } else {
                    binding.additionalQualification.setVisibility(View.VISIBLE);
                    binding.lvAdditionalQualification.setVisibility(View.GONE);
                    binding.tvAdditionalQualification.setVisibility(View.GONE);
                    binding.tvAdditionalQualificationMore.setVisibility(View.GONE);
                }


            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog_additional_qualification.isShowing()) {
                    myDialog_additional_qualification.dismiss();
                }

            }
        });


        myDialog_additional_qualification.show();


        if (arraylistModeladditionalcredential.size() > 0) {
            additionalQualificationPopUpAdapter = new AdditionalQualificationPopUpAdapter(context,
                    arraylistModeladditionalcredential);
            rv_professional_credential.setAdapter(additionalQualificationPopUpAdapter);
        }


    }


    public void GetProffesionalCredential() {
        mAPIService_new.Getproffesionalcredential(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserTypeModel>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetAdditionalQualification();
                        } else {
                            Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
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
                            Snackbar.make(binding.btnRegister, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(UserTypeModel userTypeModel) {

                        if (userTypeModel.isSuccess()) {


                            arraylistModelProffesioanlCredential.clear();

                            for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                                Model_proffesional_Credential model_proffesional_credential = new Model_proffesional_Credential();

                                model_proffesional_credential.setName(userTypeModel.getPayload().getUserType().get(i).getName());
                                model_proffesional_credential.setId(userTypeModel.getPayload().getUserType().get(i).getId());
                                model_proffesional_credential.setChecked(false);

                                Constant.hashmap_professional_credential.put(userTypeModel.getPayload().getUserType().get(i).getName(),
                                        false);

                                arraylistModelProffesioanlCredential.add(model_proffesional_credential);


                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.btnRegister, userTypeModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void GetAdditionalQualification() {

        mAPIService_new.GetAdditionalQualification(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<education_list_Model>() {
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
                            Snackbar.make(binding.btnRegister, message, Snackbar.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onNext(education_list_Model education_list_model) {

                        if (education_list_model.isSuccess()) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            arraylistModeladditionalcredential.clear();

                            for (int i = 0; i < education_list_model.getPayload().getEducationList().size(); i++) {
                                Model_additional_qualification model_additional_qualification = new Model_additional_qualification();

                                model_additional_qualification.setName(education_list_model.getPayload().getEducationList().get(i).getName());
                                model_additional_qualification.setId(education_list_model.getPayload().getEducationList().get(i).getId());
                                model_additional_qualification.setChecked(false);

                                Constant.hashmap_additional_qualification.put(education_list_model.getPayload().getEducationList().get(i).getName(),
                                        false);

                                arraylistModeladditionalcredential.add(model_additional_qualification);


                            }


                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.btnRegister, education_list_model.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    public Boolean Validation() {

        if (Constant.Trim(binding.edtZipcode.getText().toString()).isEmpty()) {
            binding.edtZipcode.requestFocus();
            binding.edtMobileNumber.clearFocus();
            binding.edtCompanyName.clearFocus();
            binding.edtPtinNumber.clearFocus();

            Snackbar.make(binding.edtZipcode, getResources().getString(R.string.val_zip_code), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtMobileNumber.getText().toString()).length() < 14) {

            binding.edtZipcode.clearFocus();
            binding.edtMobileNumber.requestFocus();
            binding.edtCompanyName.clearFocus();
            binding.edtPtinNumber.clearFocus();

            Snackbar.make(binding.edtMobileNumber, getResources().getString(R.string.val_phone_validate), Snackbar.LENGTH_SHORT).show();


            return false;
        } else if (Constant.Trim(binding.edtCompanyName.getText().toString()).isEmpty()) {

            binding.edtZipcode.clearFocus();
            binding.edtMobileNumber.clearFocus();
            binding.edtCompanyName.requestFocus();
            binding.edtPtinNumber.clearFocus();

            Snackbar.make(binding.edtCompanyName, getResources().getString(R.string.val_firm_name_register), Snackbar.LENGTH_SHORT).show();


            return false;
        } else if (jobtitle_id == 0) {

            binding.edtZipcode.clearFocus();
            binding.edtMobileNumber.clearFocus();
            binding.edtCompanyName.clearFocus();
            binding.edtPtinNumber.clearFocus();

            Snackbar.make(binding.spinnerJobTitile, getResources().getString(R.string.val_job_title), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (industry_id == 0) {

            binding.edtZipcode.clearFocus();
            binding.edtMobileNumber.clearFocus();
            binding.edtCompanyName.clearFocus();
            binding.edtPtinNumber.clearFocus();

            Snackbar.make(binding.spinnerJobTitile, getResources().getString(R.string.val_industry), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (selected_proffesional_credential.equalsIgnoreCase("")) {

            binding.edtZipcode.clearFocus();
            binding.edtMobileNumber.clearFocus();
            binding.edtCompanyName.clearFocus();
            binding.edtPtinNumber.clearFocus();
            Snackbar.make(binding.professionalCredential, getResources().getString(R.string.validation_professional_credential), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void GetIndustry() {

        mAPIService_new.GetIndustryList(getResources().getString(R.string.accept), getResources().getString(R.string.bearer) + " " + AppSettings.get_login_token(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Model_Industry>() {
                    @Override
                    public void onCompleted() {

                        if (Constant.isNetworkAvailable(context)) {
                            GetProffesionalCredential();
                        } else {
                            Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
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
                            Snackbar.make(binding.btnRegister, message, Snackbar.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNext(Model_Industry model_industry) {

                        if (model_industry.isSuccess()) {
                            arrayListindustry.clear();
                            arrayListindustry.add("Industry");


                            for (int i = 0; i < model_industry.getPayload().getIndustriesList().size(); i++) {
                                arrayListindustry.add(model_industry.getPayload().getIndustriesList().get(i).getName());
                                arrayListindustryid.add(model_industry.getPayload().getIndustriesList().get(i).getId());
                            }

                            Show_Industry_Adapter();

                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(binding.btnRegister, model_industry.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    private void Show_Industry_Adapter() {


        if (arrayListindustry.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it


            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, arrayListindustry);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerIndustry.setAdapter(aa);
            binding.spinnerIndustry.setSelection(0);


        }


    }

    private void Show_JobTitle_Adapter() {

        if (arrayListjobtitle.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it

            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, arrayListjobtitle);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerJobTitile.setAdapter(aa);

            binding.spinnerJobTitile.setSelection(0);


        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Constant.arraylistselectedproffesionalcredentialID.clear();
        Constant.arraylistselectedproffesionalcredential.clear();
        Constant.hashmap_professional_credential.clear();

        Constant.arraylistselectedadditionalqualificationID.clear();
        Constant.arraylistselectedadditionalqualification.clear();
        Constant.hashmap_additional_qualification.clear();


        Intent i = new Intent(SignUpNextActivity.this, SignUpActivity.class);
        i.putExtra(context.getResources().getString(R.string.str_signup_first_name), firstname);
        i.putExtra(context.getResources().getString(R.string.str_signup_last_name), lastname);
        i.putExtra(context.getResources().getString(R.string.str_signup_email_id), emailID);
        i.putExtra(context.getResources().getString(R.string.str_signup_phone_number), phonenumber);
        i.putExtra(context.getResources().getString(R.string.str_signup_password), password);
        i.putExtra(context.getResources().getString(R.string.str_signup_confirm_password), confirmpassword);
        i.putExtra(context.getResources().getString(R.string.str_signup_country_id), countryid);
        i.putExtra(context.getResources().getString(R.string.str_signup_state_id), stateid);
        i.putExtra(context.getResources().getString(R.string.str_signup_city_id), cityid);
        i.putExtra(context.getResources().getString(R.string.str_signup_country_pos), countrypos);
        i.putExtra(context.getResources().getString(R.string.str_signup_state_pos), statepos);
        i.putExtra(context.getResources().getString(R.string.str_signup_city_pos), citypos);
        startActivity(i);
        finish();

    }
}
