package com.example.shop.fragments;

import static android.content.ContentValues.TAG;
import static com.example.shop.functions.Functions.generalConnectedPerson;
import static com.example.shop.functions.Functions.isSignIn;
import static com.example.shop.functions.Functions.isValidEmailAddress;
import static com.example.shop.functions.Functions.setPerson;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shop.adapters.ProductAdapter;
import com.example.shop.functions.Functions;
import com.example.shop.functions.OnProductClick;
import com.example.shop.objects.CreditCard;
import com.example.shop.objects.Date;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Person;
import com.example.shop.R;
import com.example.shop.objects.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class AccountFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button btn_signUp_acc, btn_signIn_acc, btn_signOut_acc, btn_AccountSettings_acc, btn_history_acc, btn_items_acc,btn_orders_acc;
    ImageView iv_gear;
    TextView textView;
    ProductAdapter productAdapter;
    public static ArrayList<Product> uploadedProducts = new ArrayList<>();
    Dialog dialog_product, dialog_product_edit;

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
        Log.d("roi", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        btn_signUp_acc = view.findViewById(R.id.btn_signUp_acc);
        btn_orders_acc = view.findViewById(R.id.btn_orders_acc);
        btn_signIn_acc = view.findViewById(R.id.btn_signIn_acc);
        btn_signOut_acc = view.findViewById(R.id.btn_signOut_acc);
        btn_AccountSettings_acc = view.findViewById(R.id.btn_AccountSettings_acc);
        btn_items_acc = view.findViewById(R.id.btn_items_acc);
        btn_history_acc = view.findViewById(R.id.btn_history_acc);
        iv_gear = view.findViewById(R.id.iv_gear);
        textView = view.findViewById(R.id.textView);
        btn_signUp_acc.setOnClickListener(this);
        btn_orders_acc.setOnClickListener(this);
        btn_history_acc.setOnClickListener(this);
        btn_signIn_acc.setOnClickListener(this);
        btn_items_acc.setOnClickListener(this);
        btn_signOut_acc.setOnClickListener(this);
        btn_AccountSettings_acc.setOnClickListener(this);
        setVisibility();
        Animation popInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_in);

        iv_gear.startAnimation(popInAnimation);
        textView.startAnimation(popInAnimation);


        return view;
    }

    @Override
    public void onClick(View view) {

        if (view == btn_signOut_acc) {
            openDisconnectDialog();
        }
        if (view == btn_signIn_acc) {
            openSignInDialog();
        }
        if (view == btn_signUp_acc) {
            openSignUpDialog();
        }
        if (view == btn_AccountSettings_acc) {
            openAccInfoDialog();
        }
        if (view == btn_history_acc) {
            openHistory();
        }
        if (view == btn_items_acc) {
            openUploadedProducts();
        }
        if (view == btn_orders_acc) {
            openOrders();
        }
    }

    private void openOrders() {
        ArrayList<Product> orders = new ArrayList<>();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("soldProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product temp = document.toObject(Product.class);
                                if (temp.getUploader_email().equals(generalConnectedPerson.getEmail())) { //get the products that the user have uploaded
                                    orders.add(temp);
                                }
                            }

                            Dialog ordersDialog = new Dialog(getContext());
                            ordersDialog.setContentView(R.layout.dialog_history);
                            ordersDialog.setCancelable(true);
                            TextView tv_noHistory = ordersDialog.findViewById(R.id.tv_noHistory);
                            ListView lvHistory = ordersDialog.findViewById(R.id.lvHistory);
                            if (orders.size() == 0) {
                                tv_noHistory.setText("No \nOrders");
                                tv_noHistory.setVisibility(View.VISIBLE);
                            } else {
                                tv_noHistory.setText("orders from you");
                            }
                            //create the adapter
                            productAdapter = new ProductAdapter(getContext(), 0, 0, orders);
                            lvHistory.setAdapter(productAdapter);
                            lvHistory.setOnScrollListener(productAdapter);
                            lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    dialog_product = OnProductClick.productClicked(productAdapter.getItem(position), getContext());
                                    setDialogButtonsOrders(productAdapter.getItem(position), dialog_product);
                                    dialog_product.create();
                                    dialog_product.show();
                                    ordersDialog.dismiss();

                                }
                            });

                            ordersDialog.create();
                            ordersDialog.show();


                            progressDialog.dismiss();
                        }
                    }
                });

    }



    private void openUploadedProducts() {
        uploadedProducts.clear();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product temp = document.toObject(Product.class);
                                if (temp.getUploader_email().equals(generalConnectedPerson.getEmail())) { //get the products that the user have uploaded
                                    uploadedProducts.add(temp);
                                }
                            }

                            // Sort products by price
                            Collections.sort(uploadedProducts, new Comparator<Product>() {
                                @Override
                                public int compare(Product p1, Product p2) {
                                    return p1.getPrice() - p2.getPrice();
                                }
                            });
                            Dialog uploadedProductsDialog = new Dialog(getContext());
                            uploadedProductsDialog.setContentView(R.layout.dialog_history);
                            uploadedProductsDialog.setCancelable(true);
                            TextView tv_noHistory = uploadedProductsDialog.findViewById(R.id.tv_noHistory);
                            ListView lvHistory = uploadedProductsDialog.findViewById(R.id.lvHistory);
                            if (uploadedProducts.size() == 0) {
                                tv_noHistory.setText("No \nItems");
                                tv_noHistory.setVisibility(View.VISIBLE);
                            } else {
                                tv_noHistory.setText("your products");
                            }
                            //create the adapter
                            productAdapter = new ProductAdapter(getContext(), 0, 0, uploadedProducts);
                            lvHistory.setAdapter(productAdapter);
                            lvHistory.setOnScrollListener(productAdapter);
                            lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    dialog_product = OnProductClick.productClicked(productAdapter.getItem(position), getContext());
                                    setDialogButtonsItems(productAdapter.getItem(position), dialog_product);
                                    dialog_product.create();
                                    dialog_product.show();
                                    uploadedProductsDialog.dismiss();

                                }
                            });

                            uploadedProductsDialog.create();
                            uploadedProductsDialog.show();


                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void openHistory() {
        Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.dialog_history);
        builder.setCancelable(true);
        TextView tv_noHistory = builder.findViewById(R.id.tv_noHistory);
        ListView lvHistory = builder.findViewById(R.id.lvHistory);
        if (((Partner) generalConnectedPerson).getHistory().size() == 0) {
            tv_noHistory.setVisibility(View.VISIBLE);
        } else {
            tv_noHistory.setText("your history");
        }
        //create the adapter
        productAdapter = new ProductAdapter(getContext(), 0, 0, ((Partner) generalConnectedPerson).getHistory());
        lvHistory.setAdapter(productAdapter);
        lvHistory.setOnScrollListener(productAdapter);
        lvHistory.setOnItemClickListener(this);

        builder.create();
        builder.show();
    }


    // דיאלוגים

    // הדיאלוג מאפשר לקונה להוסיף פרטים אודותיו ולשנותם
    private void openAccInfoDialog() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.dialog_update_info);
        builder.setCancelable(true);

        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Integer[] years = new Integer[12];
        int this_year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < years.length; i++) {
            years[i] = this_year + i;
        }

        Button btn_update = builder.findViewById(R.id.btn_update);
        Button btn_cancel = builder.findViewById(R.id.btn_cancel);
        EditText et_firstName = builder.findViewById(R.id.et_fistName);
        EditText et_lastName = builder.findViewById(R.id.et_lastName);
        EditText et_password = builder.findViewById(R.id.et_password);
        EditText et_email = builder.findViewById(R.id.et_email);
        EditText et_zip = builder.findViewById(R.id.et_zip);
        EditText et_cardNumber = builder.findViewById(R.id.et_cardNumber);
        EditText et_cvc = builder.findViewById(R.id.et_cvc);
        EditText et_current_password = builder.findViewById(R.id.et_current_password);


        Spinner spinner_mm = builder.findViewById(R.id.spinner_mm);
        Spinner spinner_yy = builder.findViewById(R.id.spinner_yy);

        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, months);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mm.setPrompt("mm");  // doesnt work
        spinner_mm.setAdapter(ad);

        ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, years);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_yy.setPrompt("yy");  // doesnt work
        spinner_yy.setAdapter(ad);


        et_firstName.setText(generalConnectedPerson.getFirstName());
        et_lastName.setText(generalConnectedPerson.getLastName());
        et_password.setText(generalConnectedPerson.getPassword());
        et_email.setText(generalConnectedPerson.getEmail());

        et_email.setEnabled(false);
        et_email.setInputType(InputType.TYPE_NULL);

        if (generalConnectedPerson instanceof Partner) {
            int zip = ((Partner) generalConnectedPerson).getZip();
            et_zip.setText(zip + "");
            long card_number = ((Partner) generalConnectedPerson).getCard().getNumber();
            et_cardNumber.setText(card_number + "");
            int cvc = ((Partner) generalConnectedPerson).getCard().getCvc();
            et_cvc.setText(cvc + "");
            int month = ((Partner) generalConnectedPerson).getCard().getValidation().getMonth();
            spinner_mm.setSelection(month - 1);
            int year = ((Partner) generalConnectedPerson).getCard().getValidation().getYear();
            spinner_yy.setSelection(year - this_year);
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_firstName = et_firstName.getText().toString();
                String str_lastName = et_lastName.getText().toString();
                String str_password = et_password.getText().toString();
                String str_email = et_email.getText().toString();
                String str_zip = et_zip.getText().toString();
                String str_cardNumber = et_cardNumber.getText().toString();
                String str_cvc = et_cvc.getText().toString();

                //check if et is empty
                if (str_zip.length() > 8) {
                    et_zip.setError("zip must be less than 8 digits");
                }
                if (str_zip.equals("")) {
                    et_zip.setError("ENTER a ZIP");
                }
                if (str_cardNumber.length() != 16) {
                    et_cardNumber.setError("credit card must be 16 digits");
                }
                if (str_cardNumber.equals("")) {
                    et_cardNumber.setError("ENTER CARD NUMBER");
                }
                if (str_cvc.length() != 3) {
                    et_cvc.setError(" CVC must be 3 digits");
                }
                if (str_cvc.equals("")) {
                    et_cvc.setError("ENTER CVC");
                }


                if (str_firstName.equals("")) {
                    et_firstName.setError("ENTER FIRST NAME");
                }

                if (str_lastName.equals("")) {
                    et_lastName.setError("ENTER LAST NAME");
                }
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") || str_password.length() < 6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length() >= 6 && isValidEmailAddress(str_email) && !str_firstName.equals("") && !str_lastName.equals("") && !str_email.equals("") && !str_password.equals("") && !str_zip.equals("") && !str_cardNumber.equals("") && !str_cvc.equals("") && str_cvc.length() == 3 && str_zip.length() <= 8 && str_cardNumber.length() == 16;
                if (validInfo) {//info is valid

                    int int_zip = Integer.parseInt(str_zip);
                    long long_cardNumber = Long.parseLong(str_cardNumber);
                    int int_cvc = Integer.parseInt(str_cvc);


                    if (et_current_password.getText().toString().equals(generalConnectedPerson.getPassword())) {


                        int spinner_pos = spinner_mm.getSelectedItemPosition();
                        int int_month = months[spinner_pos];
                        spinner_pos = spinner_yy.getSelectedItemPosition();
                        int int_year = years[spinner_pos];

                        Date date = new Date(int_month, int_year);
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Partner newPartner = new Partner(str_firstName, str_lastName, str_email, str_password, (new CreditCard(int_cvc, long_cardNumber, date)), int_zip);
                        newPartner.setCart(generalConnectedPerson.getCart());
                        db.collection("users").document(str_email).set(newPartner);
                        if (!str_password.equals(generalConnectedPerson.getPassword())) {


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(str_email, generalConnectedPerson.getPassword()); // Current Login Credentials \\
                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "User re-authenticated.");
                                            //Now change your email address \\
                                            //----------------Code for Changing Email Address----------\\
                                            user.updatePassword(str_password)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("change password tag", "User password address updated.");
                                                            }
                                                        }
                                                    });
                                            //----------------------------------------------------------\\
                                        }
                                    });
                        }

                        setPerson();
                        builder.cancel();
                        Toast.makeText(getContext(), "info saved", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "pls enter the password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }


    //הפעולה פותחת דיאלוג המאפשר למשתמש להתנתק
    private void openDisconnectDialog() {
        Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.dialog_disconnect);
        builder.setCancelable(true);


        Button btn_cancel = builder.findViewById(R.id.btn_cancel);
        Button btn_disconnect = builder.findViewById(R.id.btn_disconnect);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // מנתק
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "disconnected.", Toast.LENGTH_SHORT).show();
                setVisibility();
                setPerson();
                builder.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    //הפעולה פותחת דיאלוג המאפשר למשתמש להתחבר דרך אימייל וסיסמא
    void openSignInDialog() {


        Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.dialog_sign_in);
        builder.setCancelable(true);


        EditText et_password = builder.findViewById(R.id.et_password);
        Button btn_cancel = builder.findViewById(R.id.btn_cancel);
        EditText et_email = builder.findViewById(R.id.et_email);
        Button btn_logIn = builder.findViewById(R.id.btn_logIn);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_password = et_password.getText().toString();
                String str_email = et_email.getText().toString();

                //check if et is empty
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") || str_password.length() < 6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length() >= 6 && isValidEmailAddress(str_email) && !str_email.equals("") && !str_password.equals("");
                if (validInfo) {   //info is valid

                    logIn(str_email, str_password,builder);
                }
            }
        });

        builder.create();
        builder.show();


    }

    // דיאלוג המאפשר למשתמש להירשם בעזרת מילוי פרטים
    void openSignUpDialog() {

        Dialog builder = new Dialog(getActivity());
        builder.setContentView(R.layout.dialog_sign_up);
        builder.setCancelable(true);


        Button btn_signUp = builder.findViewById(R.id.btn_signUp);
        Button btn_cancel = builder.findViewById(R.id.btn_cancel);
        EditText et_firstName = builder.findViewById(R.id.et_fistName);
        EditText et_lastName = builder.findViewById(R.id.et_lastName);
        EditText et_password = builder.findViewById(R.id.et_password);
        EditText et_email = builder.findViewById(R.id.et_email);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_firstName = et_firstName.getText().toString();
                String str_lastName = et_lastName.getText().toString();
                String str_password = et_password.getText().toString();
                String str_email = et_email.getText().toString();

                //check if et is empty
                if (str_firstName.equals("")) {
                    et_firstName.setError("ENTER FIRST NAME");
                }

                if (str_lastName.equals("")) {
                    et_lastName.setError("ENTER LAST NAME");
                }
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") || str_password.length() < 6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length() >= 6 && isValidEmailAddress(str_email) && !str_firstName.equals("") && !str_lastName.equals("") && !str_email.equals("") && !str_password.equals("");
                if (validInfo) {   //info is valid
                    setVisibility();
                    Person p = new Person(str_firstName, str_lastName, str_email, str_password);
                    registerUser(p, builder);
                    generalConnectedPerson = p;

                }


            }
        });

        builder.create();
        builder.show();
    }

    // הפעולה מחברת את המשתמש בעזרת אימייל וסיסמא שהיא מקבלת מהדיאלוג
    private void logIn(String str_email, String str_password, Dialog builder) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getActivity(), "logged in.", Toast.LENGTH_SHORT).show();
                            setVisibility();
                            setPerson();
                            builder.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "logged in failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    //   שליחת אימייל וסיסמא לפייר-בייס (הרשמה) והוספה לפייר- סטור(אחסון נתונים)
    public void registerUser(Person p, Dialog builder) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth.createUserWithEmailAndPassword(p.getEmail(), p.getPassword()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    db.collection("users").document(p.getEmail() + "").set(p);          // הוספת הפרטים למשתמש
                    Toast.makeText(getActivity(), "SIGN UP SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    setVisibility();
                    builder.cancel();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    builder.cancel();

                }
            }
        });

    }

    //הפעולה המעדכנת את נראות הכפתורים הקשורים למצב ההתחברות של המשתמש
    public void setVisibility() {
        if (isSignIn()) {
            btn_signUp_acc.setVisibility(View.GONE);
            btn_signIn_acc.setVisibility(View.GONE);
            btn_signOut_acc.setVisibility(View.VISIBLE);
            btn_AccountSettings_acc.setVisibility(View.VISIBLE);
            btn_history_acc.setVisibility(View.VISIBLE);
            btn_items_acc.setVisibility(View.VISIBLE);
            btn_orders_acc.setVisibility(View.VISIBLE);

        } else {
            btn_signUp_acc.setVisibility(View.VISIBLE);
            btn_signIn_acc.setVisibility(View.VISIBLE);
            btn_signOut_acc.setVisibility(View.GONE);
            btn_AccountSettings_acc.setVisibility(View.GONE);
            btn_history_acc.setVisibility(View.GONE);
            btn_items_acc.setVisibility(View.GONE);
            btn_orders_acc.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    // onItemClick for the history()
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialog_product = OnProductClick.productClicked(productAdapter.getItem(i), getContext());
        setDialogButtonsHistory(productAdapter.getItem(i), dialog_product);

    }

    //set the dialog's button when the user want to see his history
    private void setDialogButtonsHistory(Product selectedProductInListView, Dialog dialog_product) {
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        btn_productDialog.setVisibility(View.GONE);
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);
        setContactButton(selectedProductInListView, btn_contact);
    }

    //set the dialog's button when the user want to see his uploaded items
    private void setDialogButtonsItems(Product selectedProductInListView, Dialog dialog_product) {
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);
        btn_contact.setVisibility(View.GONE);
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        setEditButton(selectedProductInListView, btn_productDialog);
    }

    //set the dialog's button when the user want to see his orders
    private void setDialogButtonsOrders(Product item, Dialog dialog_product) {
        Button btn_productDialog = dialog_product.findViewById(R.id.btn_productDialog);
        btn_productDialog.setVisibility(View.GONE);
        Button btn_contact = dialog_product.findViewById(R.id.btn_contact);
        setContactButtonForOrders(item, btn_contact);

    }
    // when you press the "remove" button in the editProductDialog
    private void btnRemoveListener(Product selectedProductInListView) {
        productAdapter.remove(selectedProductInListView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(selectedProductInListView.getProductId()).delete();
        Partner p = (Partner) Functions.generalConnectedPerson;
        p.removeItem(selectedProductInListView);
        db.collection("users").document(Functions.generalConnectedPerson.getEmail()).set(p);
        Functions.generalConnectedPerson = p;
        dialog_product_edit.dismiss();
    }

    // create the editProductDialog
    private void createEditDialog(Product selectedProductInListView) {
        dialog_product_edit = new Dialog(getContext());
        dialog_product_edit.setContentView(R.layout.dialog_product_edit);
        dialog_product_edit.setCancelable(true);
        dialog_product_edit.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        setProductEditInfo(selectedProductInListView);
        dialog_product.dismiss();
        dialog_product_edit.create();
        dialog_product_edit.show();
    }

    // set the info in the editProductDialog
    private void setProductEditInfo(Product selectedProductInListView) {
        EditText et_category = dialog_product_edit.findViewById(R.id.et_category);
        TextView tv_name = dialog_product_edit.findViewById(R.id.tv_name);
        EditText et_description = dialog_product_edit.findViewById(R.id.et_description);
        EditText et_price = dialog_product_edit.findViewById(R.id.et_price);
        ImageView iv_product = dialog_product_edit.findViewById(R.id.iv_product);
        tv_name.setText(selectedProductInListView.getName());
        et_category.setText(selectedProductInListView.getCategory());
        et_description.setText(selectedProductInListView.getDescription());
        et_price.setText(selectedProductInListView.getPrice() + "");
        Glide.with(getContext()).load(selectedProductInListView.getImgUrl()).into(iv_product);
        Button btn_save = dialog_product_edit.findViewById(R.id.btn_save);
        Button btn_remove = dialog_product_edit.findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRemoveListener(selectedProductInListView);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaveListener(selectedProductInListView, et_category, et_price, et_description);
            }
        });
    }

    // when you press the "save" after editing a product
    private void btnSaveListener(Product selectedProductInListView, EditText et_category, EditText et_price, EditText et_description) {
        if (et_category.getText().toString().equals("") || et_price.getText().toString().equals("") || et_price.getText().toString().length() >= 7) {
            Toast.makeText(getContext(), "Insert Valid Data", Toast.LENGTH_SHORT).show();

        } else {
            String category = et_category.getText().toString();
            String description = et_description.getText().toString() + "";
            int price = Integer.parseInt(et_price.getText().toString());
            String imgId = selectedProductInListView.getImgUrl();
            String productId = selectedProductInListView.getProductId();
            String uploader_email = selectedProductInListView.getUploader_email();

            Product editedProduct = new Product(selectedProductInListView.getName(), category, imgId, price, productId, description, uploader_email);
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


    // set the edit buttom in the products you have uploaded dialog
    private void setEditButton(Product selectedProductInListView, Button btn_productDialog) {
        btn_productDialog.setText("Edit");
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_edit_24);
        btn_productDialog.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        btn_productDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditDialog(selectedProductInListView);
            }
        });
    }

    // set the contact button in the history
    private void setContactButton(Product selectedProductInListView, Button btn_contact) {
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.openEmailAfterBuy(selectedProductInListView, getContext());
            }
        });
    }

    // set the contact button in the orders
    private void setContactButtonForOrders(Product selectedProductInListView, Button btn_contact) {
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.openEmailForOrders(selectedProductInListView, getContext());
            }
        });
    }
}



