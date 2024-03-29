package com.example.shop.fragments;

import static com.example.shop.functions.Functions.generalConnectedPerson;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.shop.activities.AfterBuyActivity;
import com.example.shop.adapters.CartAdapter;
import com.example.shop.functions.Functions;
import com.example.shop.functions.OnProductClick;
import com.example.shop.objects.Date;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Product;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CartFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lvProductCart;
    ArrayList<Product> allProducts = new ArrayList<>();
    ArrayList<Product> cartAl = new ArrayList<>();
    CartAdapter cartAdapter;
    TextView tv_cartIsEmpty;
    Dialog dialog_product;
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
        manageBtnBuy();
        createArLs();
        Log.d("fragmentStart", "on create view");
        lvProductCart.setOnItemClickListener(this);
        ImageView iv_cart = view.findViewById(R.id.iv_cart);
        TextView textView_cart = view.findViewById(R.id.textView_cart);

        Animation popInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);

        iv_cart.startAnimation(popInAnimation);
        textView_cart.startAnimation(popInAnimation);
        return view;
    }

    //if the user didnt fill the info he can buy
    public void manageBtnBuy() {
        if (generalConnectedPerson instanceof Partner) {
            btn_buy.setVisibility(View.VISIBLE);
            btn_buy.setOnClickListener(this);
        } else {
            btn_buy.setVisibility(View.GONE);
        }
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
        loadCartFromFB(progressDialog);


    }

    // update the cart based on the products i got earlier
    public void updateCart() {
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
    }

    public void loadCartFromFB(ProgressDialog progressDialog) {

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {  // load all the products from the fire-base
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product temp = document.toObject(Product.class);
                                allProducts.add(temp);
                            }
                        } else {
                            Log.d("aaccvv", "Error getting documents: ", task.getException());
                        }
                        // update the cart
                        updateCart();


                        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                        createAdapter();

                        manageTV();
                        progressDialog.dismiss();

                    }
                });


    }

    private void manageTV() {
        if (cartAl.size() == 0) {
            tv_cartIsEmpty.setVisibility(View.VISIBLE);
        } else {
            tv_cartIsEmpty.setVisibility(View.GONE);
        }
    }

    // open the item in a dialog
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product selectedProductInListView = cartAdapter.getItem(i);
        dialog_product = OnProductClick.productClicked(selectedProductInListView, getContext());
        setDialogButtons(selectedProductInListView, dialog_product);
    }


    // set the dialog buttons
    private void setDialogButtons(Product selectedProductInListView, Dialog dialog_product) {
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);
        setContactButton(selectedProductInListView, btn_contact);
        removeFromCartButton(selectedProductInListView, btn_productDialog, dialog_product);
    }

    private void setContactButton(Product selectedProductInListView, Button btn_contact) {
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.openEmail(selectedProductInListView, getContext());
            }
        });
    }

    private void removeFromCartButton(Product selectedProductInListView, Button btn_productDialog, Dialog dialog_product) {
        btn_productDialog.setText("remove");
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_remove_shopping_cart_24);
        btn_productDialog.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        btn_productDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(selectedProductInListView);
                dialog_product.cancel();
            }
        });
    }

    //remove the product from the cart
    private void remove(Product selectedProductInListView) {
        Functions.generalConnectedPerson.removeFromCart(selectedProductInListView);
        cartAl = Functions.generalConnectedPerson.getCart();
        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
        createArLs();

    }
////////////////////////

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

                                    // the thanks activity:
                                    thanks();
                                }
                            }
                        });
            } else { // can get her because if the cart is empty the user cant see the button
                Toast.makeText(getContext(), "cart is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // whit this i update the product + remove in from products to sell + upload it to sold products
    private void checkForMatchingProduct(Product temp) {
        for (int i = 0; i < cartAl.size(); i++) {
            if (cartAl.get(i).isEquals(temp)) {
                db.collection("products").document(temp.getProductId()).delete();
                temp.setPurchaseDate(Date.getCurrentDate());
                temp.setBuyer_email(Functions.generalConnectedPerson.getEmail());
                temp.setBuyer_zip(((Partner) Functions.generalConnectedPerson).getZip());
                ((Partner) Functions.generalConnectedPerson).addsToHistory(temp);
                updateSeller(temp);
            }
        }
    }

    //add the product that have sold to a new document in the fire-base
    // (i don't add it the seller because of the onSuccess and onComplete take a lot of time)
    private void updateSeller(Product product) {
        db.collection("soldProducts").document(product.getProductId()).set(product);
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


    // open a thank you activity after buying
    private void thanks() {
        Intent intent = new Intent(getContext(), AfterBuyActivity.class);
        startActivity(intent);


        /*Dialog builder = new Dialog(getContext());
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
        builder.show();*/


    }


}

