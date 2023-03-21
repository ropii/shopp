package com.example.shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.shop.R;

public class SliderAdapter extends PagerAdapter implements View.OnClickListener {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    ImageView arrowLeft;
    ImageView arrowRight;

    //Arrays
    public int[] slide_images = {
            R.drawable.ic_baseline_account_circle_24,
            R.drawable.ic_baseline_price_change_24,
            R.drawable.icon_search
    };


    public static String[] slide_headings = {
            "Account:",
            "Prices:",
            "Search:"


    };

    public String[] slide_descs = {
            "You can open an account in order to save items in your cart. To upload items, you'll need to provide some additional information",
            "We understand the economic constraints many people face, so we strive to keep our product prices under one million dollars.\n",
            "You can swipe left to find the perfect item by searching using price and name filters.\n\n"



    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slideimages);
        TextView slideHeading = view.findViewById(R.id.slideheading);
        TextView slideDescription = view.findViewById(R.id.slidedesc);
         arrowLeft = view.findViewById(R.id.arrowLeft);
         arrowRight = view.findViewById(R.id.arrowRight);

        arrowLeft.setOnClickListener(this);
        arrowRight.setOnClickListener(this);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        Animation popInAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        slideImageView.startAnimation(popInAnimation);

        container.addView(view);
        if (position==0){
            arrowLeft.setVisibility(View.GONE);
            arrowRight.setVisibility(View.VISIBLE);

        }
        else if(position==slide_headings.length-1){
            arrowLeft.setVisibility(View.VISIBLE);
            arrowRight.setVisibility(View.GONE);

        }
        else {
            arrowLeft.setVisibility(View.VISIBLE);
            arrowRight.setVisibility(View.VISIBLE);

        }
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        LinearLayout slideLayout = (LinearLayout) object;
        ImageView slideImageView = slideLayout.findViewById(R.id.slideimages);
        slideImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_in));
        container.removeView(slideLayout);
    }

    @Override
    public void onClick(View view) {
        if (view==arrowLeft){

        }
        if (view==arrowRight){

        }
    }
}
