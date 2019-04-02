package com.entigrity.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.entigrity.R;
import com.entigrity.adapter.TransactionAdapter;
import com.entigrity.databinding.ActivityMytransactionBinding;
import com.entigrity.view.SimpleDividerItemDecoration;

public class MyTransactionActivity extends AppCompatActivity {

    ActivityMytransactionBinding binding;
    TransactionAdapter adapter;
    private static final String TAG = MyTransactionActivity.class.getName();
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mytransaction);
        context = MyTransactionActivity.this;


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.myTranscationlist.setLayoutManager(layoutManager);
        binding.myTranscationlist.addItemDecoration(new SimpleDividerItemDecoration(context));

        adapter = new TransactionAdapter(MyTransactionActivity.this);
        if (adapter != null) {
            binding.myTranscationlist.setAdapter(adapter);
        }

        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.myTranscationlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
