package com.entigrity.webinarDetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.entigrity.R;
import com.entigrity.activity.WebinarDetailsActivity;
import com.entigrity.databinding.FragmentTestimonialBinding;

public class TestimonialFragment extends Fragment {

    FragmentTestimonialBinding binding;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_testimonial, null, false);


        if (WebinarDetailsActivity.getInstance().webinartestimonial.size() > 0) {

            binding.lvTetimonial.setVisibility(View.VISIBLE);


            for (int i = 0; i < WebinarDetailsActivity.getInstance().webinartestimonial.size(); i++) {


                LayoutInflater inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View _itemRow = inflate.inflate(R.layout.row_tetimonial, null);


                final TextView tv_username_name = (TextView) _itemRow.findViewById(R.id.tv_username_name);
                final ImageView iv_testimonial_star = (ImageView) _itemRow.findViewById(R.id.iv_testimonial_star);
                final TextView tv_review_decription = (TextView) _itemRow.findViewById(R.id.tv_review_decription);


                if (!WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getFirstName().equalsIgnoreCase("")) {

                    tv_username_name.setText(WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getFirstName()
                            + " " + WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getLastName());

                }

                if (!WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getReview().equalsIgnoreCase("")) {
                    tv_review_decription.setText(WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getReview());
                }
                if (!WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate()
                        .equalsIgnoreCase("")) {

                    if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("0")) {

                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_zero);

                    } else if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("1")) {

                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_one);
                    } else if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("2")) {
                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_two);

                    } else if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("3")) {

                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_three);

                    } else if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("4")) {

                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_four);

                    } else if (WebinarDetailsActivity.getInstance().webinartestimonial.get(i).getRate().equalsIgnoreCase("5")) {

                        iv_testimonial_star.setImageResource(R.mipmap.rev_star_five);
                    }


                }


                binding.lvTestimonialSet.addView(_itemRow);


            }
        } else {
            binding.lvTetimonial.setVisibility(View.GONE);

        }


        return view = binding.getRoot();
    }

}
