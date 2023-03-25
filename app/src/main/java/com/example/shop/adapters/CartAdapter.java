package com.example.shop.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shop.objects.Product;
import com.example.shop.R;

import java.util.List;

//      המתאם של המוצר בעגלה
public class CartAdapter extends ArrayAdapter<Product> implements AbsListView.OnScrollListener{


    Context context;
    List<Product> objects;
    int lastVisibleItem = 0;
    boolean isScrollingUp = false;
    public CartAdapter(Context context, int resource, int textViewResourceId, List<Product> objects){
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_list_view_row_layout,parent,false);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        ImageView ivProduct=view.findViewById(R.id.ivProduct);
        LinearLayout LL_listViewRow = view.findViewById(R.id.LL_listViewRow);

        //LL_listViewRow.setAnimation(animation);
        Product temp = objects.get(position);

        if (isScrollingUp) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_list_view);
            LL_listViewRow.setAnimation(animation);
        } else {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_list_view);
            LL_listViewRow.setAnimation(animation);
        }

        Glide.with(getContext()).load(temp.getImgUrl()).into(ivProduct);
        tvPrice.setText(temp.getPrice()+"$");
        tvName.setText(temp.getName());
        tvCategory.setText(temp.getCategory());


        return view;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Not used
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > lastVisibleItem) {
            isScrollingUp = false;
        } else if (firstVisibleItem < lastVisibleItem) {
            isScrollingUp = true;
        }
        lastVisibleItem = firstVisibleItem;
    }



}
