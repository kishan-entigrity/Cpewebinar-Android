package com.entigrity.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entigrity.R;
import com.entigrity.activity.ActivityNotificationSetting;
import com.entigrity.activity.NotificationActivity;
import com.entigrity.adapter.MyCreditAdapter;
import com.entigrity.databinding.FragmentMycreditBinding;
import com.entigrity.view.SimpleDividerItemDecoration;

public class MyCreditsFragment extends Fragment {

    View view;
    public Context context;
    FragmentMycreditBinding binding;
    private static final String TAG = MyCreditsFragment.class.getName();
    MyCreditAdapter adapter;
    Typeface font;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mycredit, null, false);
        context = getActivity();

        font = Typeface.createFromAsset(getActivity().getAssets(), "Montserrat-Light.ttf");

        binding.btnAll.setTypeface(font);
        binding.btnDate.setTypeface(font);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recyclerviewMycredit.setLayoutManager(layoutManager);

        binding.recyclerviewMycredit.addItemDecoration(new SimpleDividerItemDecoration(context));


        adapter = new MyCreditAdapter(getActivity());


        if (adapter != null) {
            binding.recyclerviewMycredit.setAdapter(adapter);
        }

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ConfirmationPopup();
                    return true;
                }
                return false;
            }
        });


        binding.ivnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), NotificationActivity.class);
                getActivity().startActivity(i);

            }
        });


        return view = binding.getRoot();
    }

    public void ConfirmationPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        // alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.exit_validation));


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                dialog.cancel();
                getActivity().finish();


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }
}
