package com.entigrity.activity;

import android.app.AlertDialog;
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
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.adapter.TopicsofinterestAdapter;
import com.entigrity.databinding.ActivitySignupBinding;
import com.entigrity.model.registration.RegistrationModel;
import com.entigrity.model.topicsofinterest.TagsItem;
import com.entigrity.model.topicsofinterest.TopicsofInterest;
import com.entigrity.model.usertype.UserTypeModel;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.view.SimpleDividerItemDecoration;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpActivity extends AppCompatActivity {

    public Context context;
    ActivitySignupBinding binding;
    private APIService mAPIService;
    private static final String TAG = SignUpActivity.class.getName();
    private ArrayList<String> arrayLististusertype = new ArrayList<String>();
    public boolean checkprivacypolicystatus = false;
    public RecyclerView recyclerview_topics_interest;
    public TextView tv_apply, tv_cancel;
    public EditText edt_search;
    private ArrayList<TagsItem> mListrtopicsofinterest = new ArrayList<TagsItem>();
    private ArrayList<TagsItem> mListtopicsofinterest_filter = new ArrayList<TagsItem>();
    public Dialog myDialog;
    public TopicsofinterestAdapter topicsofinterestAdapteradapter;
    private boolean checkpasswordvisiblestatus = false;
    private boolean checkconfirmpasswordvisiblestatus = false;
    public TextView tv_popup_msg, tv_popup_submit;
    public Dialog myDialog_popup;
    ProgressDialog progressDialog;
    public boolean boolean_usertype = true;
    private int user_type = 0;
    public boolean checkedadapter = false;


    public ArrayList<Integer> arraylistselectedtopicsofinterest = new ArrayList<Integer>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = SignUpActivity.this;
        mAPIService = ApiUtils.getAPIService();


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetUserType();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        if (Constant.isNetworkAvailable(context)) {
            GetTopicsOfInterset();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
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
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        RegisterPost(binding.edtFirmname.getText().toString()
                                , binding.edtLastname.getText().toString(), binding.edtEmailid.getText().toString(),
                                binding.edtPassword.getText().toString(), binding.edtConfirmpassword.getText().toString(),
                                binding.edtFirmname.getText().toString(), binding.edtMobilenumbert.getText().toString(),
                                topicsofinterestAdapteradapter.arraylistselectedtag, user_type);
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }
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

                            Constant.ShowPopUp(getResources().getString(R.string.validate_password), context);

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

                            Constant.ShowPopUp(getResources().getString(R.string.validate_confirmpassword), context);

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

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

        binding.tvTopicsofinterset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTopicsOfInterestPopup();
            }
        });


    }

   /* public void ShowPopUpSucess(String message, final Context context) {
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


                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();


            }
        });
        myDialog_popup.show();

    }*/


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
                        topicsofinterestAdapteradapter.notifyDataSetChanged();
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
                        topicsofinterestAdapteradapter.notifyDataSetChanged();
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

                arraylistselectedtopicsofinterest=topicsofinterestAdapteradapter.arraylistselectedtag;


                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }

            }
        });


        Constant.Log(TAG, "show" + arraylistselectedtopicsofinterest.size());


        if (mListrtopicsofinterest.size() > 0) {
            topicsofinterestAdapteradapter = new TopicsofinterestAdapter(context, mListrtopicsofinterest, arraylistselectedtopicsofinterest);
            recyclerview_topics_interest.setAdapter(topicsofinterestAdapteradapter);
        }

        myDialog.show();


    }


    public void ShowAdapter() {
        if (arrayLististusertype.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it


            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayLististusertype);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(aa);
        }
    }


    public Boolean Validation() {

        if (binding.edtFirstname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_firstname), context);
            return false;
        } else if (binding.edtLastname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_lastname), context);
            return false;
        } else if (binding.edtEmailid.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_emailid), context);
            return false;
        } else if (binding.edtPassword.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_password_register), context);
            return false;
        } else if (binding.edtConfirmpassword.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_confirm_password_register), context);
            return false;
        } else if (!binding.edtPassword.getText().toString().equals(binding.edtConfirmpassword.getText().toString())) {
            Constant.ShowPopUp(getResources().getString(R.string.val_confirm_password_not_match), context);
            return false;
        } else if (binding.edtFirmname.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_firm_name_register), context);
            return false;
        } else if (binding.edtMobilenumbert.getText().toString().isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.val_mobile_number), context);
            return false;
        } else if (user_type == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.val_user_type), context);
            return false;
        } else if (topicsofinterestAdapteradapter.arraylistselectedtag.size() == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.val_topics), context);
            return false;
        } else if (!checkprivacypolicystatus) {
            Constant.ShowPopUp(getResources().getString(R.string.val_terms_and_condition), context);
            return false;

        } else {
            return true;
        }
    }

    public void GetUserType() {
        mAPIService.Getusertype().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        Constant.ShowPopUp(message, context);


                    }

                    @Override
                    public void onNext(UserTypeModel userTypeModel) {
                        arrayLististusertype.clear();
                        arrayLististusertype.add(getResources().getString(R.string.str_who_you_are));


                        for (int i = 0; i < userTypeModel.getPayload().getUserType().size(); i++) {
                            arrayLististusertype.add(userTypeModel.getPayload().getUserType().get(i).getName());
                        }

                    }
                });
    }


    public void GetTopicsOfInterset() {
        mAPIService.GetTopicsofInterest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopicsofInterest>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
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
                        //  Constant.Log(TAG, "topicsofinterset" + topicsofInterest.getPayload().getTags());
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

    public void RegisterPost(String first_name, String last_name, String email, String password, String confirm_password,
                             String firm_name, String contact_no, ArrayList<Integer> tags, int user_type
    ) {

        // RxJava
        mAPIService.Register(first_name, last_name
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
                        Constant.ShowPopUp(message, context);

                    }

                    @Override
                    public void onNext(RegistrationModel registrationModel) {
                        if (registrationModel.isSuccess()) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }


                            ShowPopUp(registrationModel.getMessage(), context);

                        } else if (registrationModel.isSuccess() == false) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Constant.ShowPopUp(registrationModel.getMessage(), context);

                        }


                    }
                });

    }

    public void ShowPopUp(String message, final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
