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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.activities.MainActivity;
import com.example.shop.adapters.ProductAdapter;
import com.example.shop.functions.Functions;
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



        return view;
    }


    // Create the product list from data in Firestore
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

                                // Filter products by price and name
                                if (MainActivity.product_limit_price>= temp.getPrice() && (MainActivity.product_name.equals("") ||MainActivity.product_name.equals(temp.getName()))){
                                    productArrayList.add(temp);
                                }
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

    // Create the adapter for the product list
    public void createAdapter() {

        productAdapter = new ProductAdapter(getContext(), 0, 0, productArrayList);
        lvProduct.setAdapter(productAdapter);
        lvProduct.setOnScrollListener(productAdapter);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product selectedProductInListView = productAdapter.getItem(i);
        Dialog dialog_product = new Dialog(getContext());
        dialog_product.setContentView(R.layout.dialog_product);
        dialog_product.setCancelable(true);
        TextView tv_price = dialog_product.findViewById(R.id.tv_price);
        TextView tv_description = dialog_product.findViewById(R.id.tv_description);
        TextView tv_category = dialog_product.findViewById(R.id.tv_category);
        TextView tv_name = dialog_product.findViewById(R.id.tv_name);
        ImageView product_img = dialog_product.findViewById(R.id.iv_product);
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
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
        if (Functions.generalConnectedPerson==null){
            btn_productDialog.setVisibility(View.GONE);
        }
        else if (Functions.generalConnectedPerson.getEmail().equals(selectedProductInListView.getUploader_email())){
            btn_productDialog.setText("Edit");
            btn_productDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog_product_edit = new Dialog(getContext());
                    dialog_product_edit.setContentView(R.layout.dialog_product_edit);
                    dialog_product_edit.setCancelable(true);
                    dialog_product_edit.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
                    EditText et_category = dialog_product_edit.findViewById(R.id.et_category);
                    TextView tv_name = dialog_product_edit.findViewById(R.id.tv_name);
                    EditText et_description = dialog_product_edit.findViewById(R.id.et_description);
                    EditText et_price = dialog_product_edit.findViewById(R.id.et_price);
                    Button btn_save = dialog_product_edit.findViewById(R.id.btn_save);
                    Button btn_remove = dialog_product_edit.findViewById(R.id.btn_remove);
                    ImageView iv_product = dialog_product_edit.findViewById(R.id.iv_product);
                    tv_name.setText(selectedProductInListView.getName());
                    et_category.setText(selectedProductInListView.getCategory());
                    et_description.setText(selectedProductInListView.getDescription());
                    et_price.setText(selectedProductInListView.getPrice()+"");
                    Glide.with(getContext()).load(selectedProductInListView.getImgUrl()).into(iv_product);
                    btn_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            productAdapter.remove(selectedProductInListView);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("products").document(selectedProductInListView.getProductId()).delete();
                            Partner p = (Partner) Functions.generalConnectedPerson;
                            p.removeItem(selectedProductInListView);
                            db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(p);
                            Functions.generalConnectedPerson=p;
                            dialog_product_edit.dismiss();

                        }
                    });

                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (et_category.getText().toString().equals("") ||et_price.getText().toString().equals("") ||et_price.getText().toString().length()>= 7){
                                Toast.makeText(getContext(), "Insert Valid Data", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String category = et_category.getText().toString();
                                String description = et_description.getText().toString()+"";
                                int price = Integer.parseInt(et_price.getText().toString());
                                String imgId = selectedProductInListView.getImgUrl();
                                String productId = selectedProductInListView.getProductId();
                                String uploader_email = selectedProductInListView.getUploader_email();

                                Product editedProduct = new Product(selectedProductInListView.getName(),category,imgId,price,productId,description,uploader_email);
                                productAdapter.remove(selectedProductInListView);
                                productAdapter.insert(editedProduct,i);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("products").document(selectedProductInListView.getProductId()).set(editedProduct);
                                Partner p = (Partner) Functions.generalConnectedPerson;
                                p.removeItem(selectedProductInListView);
                                p.getItems().add(editedProduct);
                                db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(p);
                                Functions.generalConnectedPerson = p;
                                dialog_product_edit.dismiss();
                            }
                        }
                    });


                    dialog_product.dismiss();

                    dialog_product_edit.create();
                    dialog_product_edit.show();

                }
            });
        }
        else {
            btn_productDialog.setText("ADD TO CART");
            btn_productDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean inCart= false;
                    for (int j = 0; j < Functions.generalConnectedPerson.getCart().size(); j++) {
                        inCart= Functions.generalConnectedPerson.getCart().get(j).getProductId().equals(selectedProductInListView.getProductId());
                        if (inCart){
                            break;
                        }
                    }
                    if (!inCart) {
                        Functions.generalConnectedPerson.getCart().add(selectedProductInListView);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(Functions.generalConnectedPerson);
                        Toast.makeText(getContext(), selectedProductInListView.getName()+" added to cart", Toast.LENGTH_SHORT).show();
                        Functions.setPerson();
                    }
                    else {
                        Toast.makeText(getContext(), selectedProductInListView.getName()+" already in cart", Toast.LENGTH_SHORT).show();

                    }
                    dialog_product.cancel();
                }
            });
        }
        dialog_product.create();
        dialog_product.show();


    }

}