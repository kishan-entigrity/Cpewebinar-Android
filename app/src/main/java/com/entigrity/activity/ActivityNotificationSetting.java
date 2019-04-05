package com.entigrity.activity;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.entigrity.R;
import com.entigrity.databinding.ActivityNotificationSettingBinding;

public class ActivityNotificationSetting extends AppCompatActivity {

    ActivityNotificationSettingBinding binding;
    public boolean checkexpand_pushnoti_webinar = true;
    public boolean checkexpand_pushnoti_pending_action = true;
    public boolean checkexpand_text_webinar = true;
    public boolean checkexpand_text_pending_action = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_setting);


        binding.relWebinarParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkexpand_pushnoti_webinar) {
                    checkexpand_pushnoti_webinar = false;
                    collapse(binding.relRegisterNotification, 500, 0);
                    collapse(binding.relReminderNotification, 500, 0);

                } else {
                    checkexpand_pushnoti_webinar = true;
                    expand(binding.relRegisterNotification, 500, 50);
                    expand(binding.relReminderNotification, 500, 50);
                }


            }
        });

        binding.relPendingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkexpand_pushnoti_pending_action) {
                    checkexpand_pushnoti_pending_action = false;
                    collapse(binding.relPushQuizPending, 500, 0);
                    collapse(binding.relPushPendingEvolution, 500, 0);

                } else {
                    checkexpand_pushnoti_pending_action = true;
                    expand(binding.relPushQuizPending, 500, 50);
                    expand(binding.relPushPendingEvolution, 500, 50);
                }

            }
        });


        binding.relWebinarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkexpand_text_webinar) {
                    checkexpand_text_webinar = false;
                    collapse(binding.relTextRegister, 500, 0);
                    collapse(binding.relTextReminder, 500, 0);

                } else {
                    checkexpand_text_webinar = true;
                    expand(binding.relTextRegister, 500, 50);
                    expand(binding.relTextReminder, 500, 50);
                }

            }
        });

        binding.relTextPendingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkexpand_text_pending_action) {
                    checkexpand_text_pending_action = false;
                    collapse(binding.quizTextPending, 500, 0);
                    collapse(binding.relTextPendingEvolution, 500, 0);

                } else {
                    checkexpand_text_pending_action = true;
                    expand(binding.quizTextPending, 500, 50);
                    expand(binding.relTextPendingEvolution, 500, 50);
                }

            }
        });


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
}
