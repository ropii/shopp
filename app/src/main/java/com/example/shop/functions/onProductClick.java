package com.example.shop.functions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.objects.Product;

//because i open the same dialog several time i wrote this code here to not duplicate it
public class onProductClick {
    static Product selectedProductInListViewClass;
    static Context contextClass;
    static Dialog dialog_product;

    public static Dialog productClicked(Product selectedProductInListView, Context context) {
        selectedProductInListViewClass = selectedProductInListView;
        contextClass = context;
        openProductDialog();
        return dialog_product;
    }

    // manage and open the product dialog
    private static void openProductDialog() {
        dialog_product = new Dialog(contextClass);
        dialog_product.setContentView(R.layout.dialog_product);
        dialog_product.setCancelable(true);

        TextView tv_price = dialog_product.findViewById(R.id.tv_price);
        TextView tv_description = dialog_product.findViewById(R.id.tv_description);
        TextView tv_category = dialog_product.findViewById(R.id.tv_category);
        TextView tv_name = dialog_product.findViewById(R.id.tv_name);
        ImageView product_img = dialog_product.findViewById(R.id.iv_product);
        TextView tv_purchaseDate = dialog_product.findViewById(R.id.tv_purchaseDate);
        TextView tv_uploadDate = dialog_product.findViewById(R.id.tv_uploadDate);
        setProductDetails(selectedProductInListViewClass, tv_price, tv_description, tv_category, tv_name, product_img, tv_purchaseDate, tv_uploadDate);
        dialog_product.create();
        dialog_product.show();
    }

    // set the products details inside the dialog
    private static void setProductDetails(Product selectedProductInListView, TextView tv_price, TextView tv_description, TextView tv_category, TextView tv_name, ImageView product_img, TextView tv_purchaseDate, TextView tv_uploadDate) {
        tv_price.setText(selectedProductInListView.getPrice() + "$");
        tv_name.setText(selectedProductInListView.getName());
        if (selectedProductInListView.getPurchaseDate()==null){
            tv_purchaseDate.setVisibility(View.GONE);
        }
        else{
            tv_purchaseDate.setText("purchased: " + selectedProductInListView.getPurchaseDate());
        }
        tv_uploadDate.setText("uploaded: " + selectedProductInListView.getUploadDate());
        if (selectedProductInListView.getDescription().equals("")) {
            tv_description.setVisibility(View.GONE);
        } else {
            tv_description.setText(selectedProductInListView.getDescription());
        }
        tv_category.setText(selectedProductInListView.getCategory());
        Glide.with(contextClass).load(selectedProductInListView.getImgUrl()).into(product_img);
        dialog_product.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }



}
