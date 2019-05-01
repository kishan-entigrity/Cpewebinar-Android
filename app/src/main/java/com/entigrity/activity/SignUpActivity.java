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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.databinding.ActivitySignupBinding;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtilsNew;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpActivity extends AppCompatActivity {

    public Context context;
    ActivitySignupBinding binding;
    private APIService mAPIService_new;
    private static final String TAG = SignUpActivity.class.getName();
    private ArrayList<String> arrayLististusertype = new ArrayList<String>();
    public boolean checkprivacypolicystatus = false;
    private boolean checkpasswordvisiblestatus = false;
    private boolean checkconfirmpasswordvisiblestatus = false;
    ProgressDialog progressDialog;
    public boolean boolean_usertype = true;
    private int user_type = 0;
    public Dialog myDialog;
    public TextView tv_skip, tv_yes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = SignUpActivity.this;
        mAPIService_new = ApiUtilsNew.getAPIService();


       /* binding.edtMobilenumbert.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(binding.edtMobilenumbert));
        binding.edtMobilenumbert.addTextChangedListener(addLineNumberFormatter);*/


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetUserType();
        } else {
            Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();
        }


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boolean_usertype) {
                    boolean_usertype = false;
                } else {
                    user_type = position;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    ShowTopicsOfInterestPopup();
                }


            }
        });


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
        });


        binding.tvterms.setOnClickListener(new View.OnClickListener() {
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

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });


    }


    public void ShowTopicsOfInterestPopup() {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.topics_popup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_skip = (TextView) myDialog.findViewById(R.id.tv_skip);
        tv_yes = (TextView) myDialog.findViewById(R.id.tv_yes);

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Constant.arraylistselectedvalue.clear();

                Intent i = new Intent(SignUpActivity.this, TopicsofInterestSignUpActivity.class);
                i.putExtra(getResources().getString(R.string.reg_firstname), Constant.Trim(binding.edtFirmname.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_lastname), Constant.Trim(binding.edtLastname.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_email), Constant.Trim(binding.edtEmailid.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_password), Constant.Trim(binding.edtPassword.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_confirm_password), Constant.Trim(binding.edtConfirmpassword.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_firmname), Constant.Trim(binding.edtFirmname.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_mobilenumber), Constant.Trim(binding.edtMobilenumbert.getText().toString()));
                i.putExtra(getResources().getString(R.string.reg_whoyouare), user_type);
                i.putExtra(getResources().getString(R.string.str_get_key_screen_key), getResources().getString(R.string.from_sign_up_screen));
                i.putExtra(getResources().getString(R.string.reg_isaccepted), checkprivacypolicystatus);

                startActivity(i);

            }
        });


        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        RegisterPost(Constant.Trim(binding.edtFirmname.getText().toString())
                                , Constant.Trim(binding.edtLastname.getText().toString()), Constant.Trim(binding.edtEmailid.getText().toString()),
                                Constant.Trim(binding.edtPassword.getText().toString()), Constant.Trim(binding.edtConfirmpassword.getText().toString()),
                                Constant.Trim(binding.edtFirmname.getText().toString()), Constant.Trim(binding.edtMobilenumbert.getText().toString()),
                                Constant.arraylistselectedvalue, user_type);
                    } else {
                        Snackbar.make(binding.btnRegister, getResources().getString(R.string.please_check_internet_condition), Snackbar.LENGTH_SHORT).show();

                    }
                }


            }
        });


        myDialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.arraylistselectedvalue.clear();
        finish();
    }


    public void ShowAdapter() {
        if (arrayLististusertype.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it


            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, arrayLististusertype);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(aa);
        }
    }


    public Boolean Validation() {

        if (Constant.Trim(binding.edtFirstname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtFirstname, getResources().getString(R.string.val_firstname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtLastname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtLastname, getResources().getString(R.string.val_lastname), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtEmailid.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtEmailid, getResources().getString(R.string.val_emailid), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtPassword.getText().toString()).length() < 6) {
            Snackbar.make(binding.edtPassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.val_confirm_password_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtConfirmpassword.getText().toString()).length() < 6) {
            Snackbar.make(binding.edtConfirmpassword, getResources().getString(R.string.password_length), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!Constant.Trim(binding.edtPassword.getText().toString()).equals(Constant.Trim(binding.edtConfirmpassword.getText().toString()))) {
            Snackbar.make(binding.edtPassword, getResources().getString(R.string.val_confirm_password_not_match), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtFirmname.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtFirmname, getResources().getString(R.string.val_firm_name_register), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Constant.Trim(binding.edtMobilenumbert.getText().toString()).isEmpty()) {
            Snackbar.make(binding.edtMobilenumbert, getResources().getString(R.string.val_mobile_number), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (user_type == 0) {
            Snackbar.make(binding.edtMobilenumbert, getResources().getString(R.string.val_user_type), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!checkprivacypolicystatus) {
            Snackbar.make(binding.edtMobilenumbert, getResources().getString(R.string.val_terms_and_condition), Snackbar.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    public void GetUserType() {
        mAPIService_new.Getusertype(getResources().getString(R.string.accept)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserTypeModel>() {
                    @Override
                    public void onCompleted() {
                        ShowAdapter();

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Snackbar.make(binding.edtConfirmpassword, message, Snackbar.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNext(UserTypeModel userTypeModel) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (userTypeModel.isSuccess()) {
                            arrayLististusertype.clear();
                            arrayLististusertype.add(getResources().getString(R.string.str_who_you_are));


                            for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                                arrayLististusertype.add(userTypeModel.getPayload().getUserType().get(i).getName());
                            }
                        } else {
                            Snackbar.make(binding.btnRegister, userTypeModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    public void RegisterPost(String first_name, String last_name, String email, String password, String confirm_password,
                             String firm_name, String contact_no, ArrayList<Integer> tags, int user_type
    ) {

        // RxJava
        mAPIService_new.Register(getResources().getString(R.string.accept), first_name, last_name
                , email, password, confirm_password, firm_name, contact_no, tags, user_type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            Constant.arraylistselectedvalue.clear();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
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


}
