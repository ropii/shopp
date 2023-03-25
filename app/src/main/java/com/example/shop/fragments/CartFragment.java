package com.example.shop.fragments;

import static com.example.shop.activities.MainActivity.chipNavigationBar;
import static com.example.shop.functions.Functions.generalConnectedPerson;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.shop.adapters.CartAdapter;
import com.example.shop.adapters.ProductAdapter;
import com.example.shop.functions.Functions;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Person;
import com.example.shop.objects.Product;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;


public class CartFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lvProductCart;
    ArrayList<Product> allProducts = new ArrayList<>();
    ArrayList<Product> cartAl = new ArrayList<>();
    CartAdapter cartAdapter;
    TextView tv_cartIsEmpty;
    FloatingActionButton btn_buy;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        tv_cartIsEmpty = view.findViewById(R.id.tv_cartIsEmpty);
        btn_buy = view.findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(this);
        createArLs();
        Log.d("fragmentStart", "on create view");
        lvProductCart.setOnItemClickListener(this);
        return view;
    }

    public void createAdapter() {

        cartAdapter = new CartAdapter(getContext(), 0, 0, cartAl);
        lvProductCart.setAdapter(cartAdapter);
        lvProductCart.setOnScrollListener(cartAdapter);

    }

    public void createArLs() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
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
                        cartAl = Functions.generalConnectedPerson.getCart();
                        for (int i = 0; i < allProducts.size(); i++) {
                            for (int j = 0; j < cartAl.size(); j++) {
                                if (cartAl.get(j).isEquals(allProducts.get(i))) {
                                    cartAl.set(j, allProducts.get(i));
                                }
                            }
                        }
                        for (int i = 0; i < cartAl.size(); i++) {
                            if (!allProducts.contains(cartAl.get(i))) {
                                cartAl.remove(i);
                            }
                        }
                        Collections.sort(cartAl, new Comparator<Product>() {
                            @Override
                            public int compare(Product p1, Product p2) {
                                return p1.getPrice() - p2.getPrice();
                            }
                        });

                        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                        createAdapter();

                        if (cartAl.size() == 0) {
                            tv_cartIsEmpty.setVisibility(View.VISIBLE);
                        } else {
                            tv_cartIsEmpty.setVisibility(View.GONE);
                        }

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
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.openEmail(selectedProductInListView, getContext());
            }
        });

        btn_productDialog.setText("remove");
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_remove_shopping_cart_24);
        btn_productDialog.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        btn_productDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_product.dismiss();
                cartAl.remove(selectedProductInListView);
                Functions.generalConnectedPerson.setCart(cartAl);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                createArLs();
            }
        });
        tv_price.setText(selectedProductInListView.getPrice() + "$");
        tv_name.setText(selectedProductInListView.getName());
        if (selectedProductInListView.getDescription().equals("")) {
            tv_description.setVisibility(View.GONE);
        } else {
            tv_description.setText(selectedProductInListView.getDescription());
        }
        tv_category.setText(selectedProductInListView.getCategory());
        tv_name.setText(selectedProductInListView.getName());
        Glide.with(getContext()).load(selectedProductInListView.getImgUrl()).into(product_img);
        dialog_product.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_product.create();
        dialog_product.show();
    }


    @Override
    public void onClick(View view) {
        if (view == btn_buy) {
            if (cartAl.size() != 0) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Loading...");
                progressDialog.show();

                db.collection("products")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Product temp = document.toObject(Product.class);
                                        checkForMatchingProduct(temp);
                                    }
                                    cartAl.clear();
                                    Functions.generalConnectedPerson.setCart(cartAl);
                                    db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                                    createArLs();
                                    progressDialog.dismiss();
/*
                                    // move to product fragment so he could buy more
                                    chipNavigationBar.setItemSelected(R.id.menu_products, true);
*/
                                    // the thanks dialog:
                                    thanks();
                                }
                            }
                        });
            } else {
                Toast.makeText(getContext(), "cart is empty", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void checkForMatchingProduct(Product temp) {
        for (int i = 0; i < cartAl.size(); i++) {
            if (cartAl.get(i).isEquals(temp)) {
                db.collection("products").document(temp.getProductId()).delete();
                ((Partner) Functions.generalConnectedPerson).addsToHistory(temp);
/*   add the order
                db.collection("users").document(temp.getUploader_email())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Partner productUploader = documentSnapshot.toObject(Partner.class);
                                productUploader.addToOrders(temp.getProductId(), Functions.generalConnectedPerson.getEmail());
                                db.collection("users").document(temp.getUploader_email()).set(productUploader);

                            }
                        });
*/
            }
        }
    }
    // open a thank you dialog after buying
    private void thanks() {
        Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.dialog_after_buying);
        builder.setCancelable(true);
        VideoView videoView = builder.findViewById(R.id.videoView_afterBuying);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.thanks_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        builder.create();
        builder.show();


    }


}

