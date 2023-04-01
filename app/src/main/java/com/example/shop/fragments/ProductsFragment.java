package com.example.shop.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.activities.MainActivity;
import com.example.shop.adapters.ProductAdapter;
import com.example.shop.functions.Functions;
import com.example.shop.functions.onProductClick;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ProductsFragment extends Fragment implements AdapterView.OnItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    ArrayList<Product> productArrayList = new ArrayList<>();
    Dialog dialog_product;
    ListView lvProduct;
    ProductAdapter productAdapter;


    public ProductsFragment() {
    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragmentStart", "on create");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        lvProduct = view.findViewById(R.id.lvProduct);
        createArLs();
        lvProduct.setOnItemClickListener(this);
        Log.d("fragmentStart", "on create view");

        ImageView iv_warehouse = view.findViewById(R.id.iv_warehouse);
        TextView textView_products = view.findViewById(R.id.textView_products);

        Animation popInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        iv_warehouse.startAnimation(popInAnimation);
        textView_products.startAnimation(popInAnimation);

        return view;
    }


    // Create the product list from data in Firestore
    public void createArLs() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product temp = document.toObject(Product.class);
                                filterProducts(temp);

                            }

                            // Sort products by price
                            Collections.sort(productArrayList, new Comparator<Product>() {
                                @Override
                                public int compare(Product p1, Product p2) {
                                    return p1.getPrice() - p2.getPrice();
                                }
                            });

                            // Create and set the product adapter for the list view
                            createAdapter();
                            progressDialog.dismiss();
                        } else {
                            Log.d("aaccvv", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // filter the products in "products fragment"
    private void filterProducts(Product temp) {
        // Filter products by price , category and name
        if (Functions.generalConnectedPerson instanceof Partner) { // the user wont be able to see the products that he have uploaded
            if (!(Functions.generalConnectedPerson.getEmail().equals(temp.getUploader_email())) &&
                    MainActivity.product_limit_price >= temp.getPrice() && //price
                    (MainActivity.product_name.equals("") || MainActivity.product_name.equals(temp.getName())) && // name
                    (MainActivity.product_category.equals("") || MainActivity.product_category.equals(temp.getCategory())))// category
            {
                productArrayList.add(temp);
            }
        } else {
            if (MainActivity.product_limit_price >= temp.getPrice() && //price
                    (MainActivity.product_name.equals("") || MainActivity.product_name.equals(temp.getName())) && // name
                    (MainActivity.product_category.equals("") || MainActivity.product_category.equals(temp.getCategory()))) { // category
                productArrayList.add(temp);
            }
        }
    }

    // Create the adapter for the product list
    public void createAdapter() {

        productAdapter = new ProductAdapter(getContext(), 0, 0, productArrayList);
        lvProduct.setAdapter(productAdapter);
        lvProduct.setOnScrollListener(productAdapter);

    }

    // product have been pressed
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product selectedProductInListView = productAdapter.getItem(i);
        dialog_product = onProductClick.productClicked(selectedProductInListView,getContext());
        setDialogButtons(selectedProductInListView, dialog_product);
    }


    // set the dialog buttons
    private void setDialogButtons(Product selectedProductInListView, Dialog dialog_product) {
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);

        if (Functions.generalConnectedPerson == null) {
            btn_productDialog.setVisibility(View.GONE);
            btn_contact.setVisibility(View.GONE);
        } else {
            setContactButton(selectedProductInListView, btn_contact);
            setAddToCartButton(selectedProductInListView, btn_productDialog, dialog_product);
        }
    }

    private void setContactButton(Product selectedProductInListView, Button btn_contact) {
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.openEmail(selectedProductInListView, getContext());
            }
        });
    }

    private void setAddToCartButton(Product selectedProductInListView, Button btn_productDialog, Dialog dialog_product) {
        btn_productDialog.setText("ADD TO CART");
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_add_shopping_cart_24);
        btn_productDialog.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        btn_productDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(selectedProductInListView);
                dialog_product.cancel();
            }
        });
    }

    //add the product to the cart if he isn't there
    private void addToCart(Product selectedProductInListView) {
        boolean inCart = false;
        for (int j = 0; j < Functions.generalConnectedPerson.getCart().size(); j++) {
            inCart = Functions.generalConnectedPerson.getCart().get(j).getProductId().equals(selectedProductInListView.getProductId());
            if (inCart) {
                break;
            }
        }
        if (!inCart) { // add the product to the user's cart and update it in the fire base
            Functions.generalConnectedPerson.getCart().add(selectedProductInListView);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
            Toast.makeText(getContext(), selectedProductInListView.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            Functions.setPerson();
        } else {
            Toast.makeText(getContext(), selectedProductInListView.getName() + " already in cart", Toast.LENGTH_SHORT).show();

        }
    }


}