package com.entigrity.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.entigrity.R;
import com.entigrity.databinding.ActivityStripeBinding;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class StripePaymentActivity extends AppCompatActivity {
    ActivityStripeBinding binding;
    Card cardToSave;
    Context context;
    Card card;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stripe);
        context = StripePaymentActivity.this;


        cardToSave = binding.cardInputWidget.getCard();
        if (cardToSave == null) {
            Snackbar.make(binding.cardInputWidget, "Invalid Card Data", Snackbar.LENGTH_SHORT).show();
        } else {
            cardToSave.setName("Customer Name");
            cardToSave.setAddressZip("12345");


            if (binding.cardInputWidget.getCard() != null) {
                String cvv = binding.cardInputWidget.getCard().getCVC();
                int exp = binding.cardInputWidget.getCard().getExpMonth();
                int exp_year = binding.cardInputWidget.getCard().getExpYear();
                String card_num = binding.cardInputWidget.getCard().getNumber();

                card = new Card(card_num, exp, exp_year, cvv);
            }

            /*card = new Card("4242424242424242", 12, 2020, "123");
            // Remember to validate the card object before you use it to save time.
            if (!card.validateCard()) {
                // Do not continue token creation.
            }*/
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stripe stripe = new Stripe(context, "pk_test_TYooMQauvdEDq54NiTphI7jx");
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server
                            }

                            public void onError(Exception error) {
                                // Show localized error message
                                Snackbar.make(binding.cardInputWidget, error.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });


    }
}
