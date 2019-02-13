package com.entigrity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.entigrity.MainActivity;
import com.entigrity.R;
import com.entigrity.databinding.FragmentContactusBinding;
import com.entigrity.model.contactus.ContactUsModel;
import com.entigrity.model.subject.SubjectModel;
import com.entigrity.utility.Constant;
import com.entigrity.view.DialogsUtils;
import com.entigrity.webservice.APIService;
import com.entigrity.webservice.ApiUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactUsFragment extends Fragment {
    private FragmentContactusBinding binding;
    public Context context;
    private static final String TAG = ContactUsFragment.class.getName();
    private APIService mAPIService;
    private ArrayList<String> arrayListsubject = new ArrayList<String>();
    ProgressDialog progressDialog;
    private int subject = 0;
    View view;
    public boolean boolean_subject = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contactus, null, false);
        mAPIService = ApiUtils.getAPIService();
        context = getActivity();

        MainActivity.getInstance().rel_top_bottom.setVisibility(View.VISIBLE);


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {


                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });


        binding.spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (boolean_subject) {
                    boolean_subject = false;
                } else {
                    subject = position;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        binding.edtmessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.edtmessage.setRawInputType(InputType.TYPE_CLASS_TEXT);


        if (Constant.isNetworkAvailable(context)) {
            progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
            GetSubject();
        } else {
            Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
        }


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (Constant.isNetworkAvailable(context)) {
                        progressDialog = DialogsUtils.showProgressDialog(context, getResources().getString(R.string.progrees_msg));
                        Contactus(Constant.Trim(binding.spinnerSubject.getSelectedItem().toString()), Constant.Trim(binding.edtname.getText().toString()),
                                Constant.Trim(binding.edtemailid.getText().toString()), Constant.Trim(binding.edtcontactNumber.getText().toString()),
                                Constant.Trim(binding.edtmessage.getText().toString()));
                    } else {
                        Constant.ShowPopUp(getResources().getString(R.string.please_check_internet_condition), context);
                    }
                }
            }
        });


        return view = binding.getRoot();
    }


    public void GetSubject() {
        mAPIService.GetSubject().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SubjectModel>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
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
                    public void onNext(SubjectModel subjectModel) {
                        arrayListsubject.clear();
                        arrayListsubject.add(getResources().getString(R.string.subject_hint));

                        for (int i = 0; i < subjectModel.getPayload().getSubject().size(); i++) {
                            arrayListsubject.add(subjectModel.getPayload().getSubject().get(i).getSubject());
                        }


                    }
                });
    }


    public void ShowAdapter() {
        if (arrayListsubject.size() > 0) {
            //Getting the instance of Spinner and applying OnItemSelectedListener on it


            //Creating the ArrayAdapter instance having the user type list
            ArrayAdapter aa = new ArrayAdapter(context, R.layout.spinner_item, arrayListsubject);
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinnerSubject.setAdapter(aa);
        }

    }


    public Boolean Validation() {

        if (Constant.Trim(binding.edtname.getText().toString()).isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.cus_valname), context);
            return false;
        } else if (Constant.Trim(binding.edtcontactNumber.getText().toString()).isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.cus_valnumber), context);
            return false;
        } else if (subject == 0) {
            Constant.ShowPopUp(getResources().getString(R.string.cus_valsubject), context);
            return false;
        } else if (Constant.Trim(binding.edtemailid.getText().toString()).isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.cus_valemailid), context);
            return false;
        } else if (!Constant.isValidEmailId(Constant.Trim(binding.edtemailid.getText().toString()))) {
            Constant.ShowPopUp(getResources().getString(R.string.valid_email), context);
            return false;
        } else if (Constant.Trim(binding.edtmessage.getText().toString()).isEmpty()) {
            Constant.ShowPopUp(getResources().getString(R.string.cus_valmsg), context);
            return false;
        } else {
            return true;
        }


    }


    public void Contactus(String subject, String name, String email, String contact_number, String message) {

        // RxJava
        mAPIService.contactus(subject, name, email, contact_number, message).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContactUsModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //handle failure response
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String message = Constant.GetReturnResponse(context, e);
                        Constant.ShowPopUp(message, context);


                    }


                    @Override
                    public void onNext(ContactUsModel contactUsModel) {

                        if (contactUsModel.isSuccess()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Constant.ShowPopUp(contactUsModel.getMessage(), context);
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Constant.ShowPopUp(contactUsModel.getMessage(), context);
                        }


                    }
                });

    }


}
