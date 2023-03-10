package com.example.shop.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shop.adapters.CartAdapter;
import com.example.shop.functions.Functions;
import com.example.shop.objects.Product;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CartFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lvProductCart;
    ArrayList<Product> allProducts= new ArrayList<>();
    ArrayList<Product> cart= new ArrayList<>();
    CartAdapter cartAdapter;

    private String mParam1;
    private String mParam2;

    public CartFragment() {
    }


    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        lvProductCart = view.findViewById(R.id.lvProductCart);
         createArLs();
        Log.d("fragmentStart", "on create view");
        lvProductCart.setOnItemClickListener(this);
        return view;
    }
    public void createAdapter() {

        cartAdapter = new CartAdapter(getContext(), 0, 0, cart);
        lvProductCart.setAdapter(cartAdapter);
        lvProductCart.setOnScrollListener(cartAdapter);

    }
    public void createArLs(){
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
                                allProducts.add(temp);
                            }
                        } else {
                            Log.d("aaccvv", "Error getting documents: ", task.getException());
                        }
                        // update the cart based on the products
                        cart= Functions.generalConnectedPerson.getCart();
                        for (int i = 0; i <allProducts.size() ; i++) {
                            for (int j = 0; j <cart.size() ; j++) {
                                if (cart.get(j).isEquals(allProducts.get(i))){
                                    cart.set(j,allProducts.get(i));
                                }
                            }
                        }
                        for (int i = 0; i <cart.size() ; i++) {
                            if (!allProducts.contains(cart.get(i))){
                                cart.remove(i);
                            }
                        }
                        Collections.sort(cart, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return p1.getPrice() - p2.getPrice();
                            }
                        });

                        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                        createAdapter();


                    }
                });



        progressDialog.dismiss();




    }

    // open the item in a dialog
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product selectedProductInListView = cartAdapter.getItem(i);
        Dialog dialog_product = new Dialog(getContext());
        dialog_product.setContentView(R.layout.dialog_product);
        dialog_product.setCancelable(true);
        TextView tv_price = dialog_product.findViewById(R.id.tv_price);
        TextView tv_description = dialog_product.findViewById(R.id.tv_description);
        TextView tv_category = dialog_product.findViewById(R.id.tv_category);
        TextView tv_name = dialog_product.findViewById(R.id.tv_name);
        ImageView product_img = dialog_product.findViewById(R.id.iv_product);
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        btn_productDialog.setVisibility(View.GONE);
        tv_price.setText(selectedProductInListView.getPrice()+"$");
        tv_name.setText(selectedProductInListView.getName());
        if (selectedProductInListView.getDescription().equals("")){
            tv_description.setVisibility(View.GONE);
        }
        else{
            tv_description.setText(selectedProductInListView.getDescription());}
        tv_category.setText(selectedProductInListView.getCategory());
        tv_name.setText(selectedProductInListView.getName());
        Glide.with(getContext()).load(selectedProductInListView.getImgUrl()).into(product_img);
        dialog_product.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_product.create();
        dialog_product.show();
    }
}