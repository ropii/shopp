package com.example.shop.activities;

import static android.app.PendingIntent.getActivity;

import static com.example.shop.functions.Functions.closeKeyboard;
import static com.example.shop.functions.Functions.generalConnectedPerson;
import static com.example.shop.functions.Functions.returnConnectedPerson;
import static com.example.shop.functions.Functions.setPerson;

import android.animation.Animator;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shop.functions.Functions;
import com.example.shop.functions.NetworkChangeReceiver;
import com.example.shop.functions.NotificationService;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Person;
import com.example.shop.fragments.ProductsFragment;
import com.example.shop.R;
import com.example.shop.fragments.UploadItemFragment;
import com.example.shop.archives.BasicActivity;
import com.example.shop.fragments.AboutFragment;
import com.example.shop.fragments.AccountFragment;
import com.example.shop.fragments.CartFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.checkerframework.checker.nullness.qual.NonNull;


public class MainActivity extends BasicActivity implements View.OnClickListener {
    public static Person p = null;
    TextView tv;
    public static ChipNavigationBar chipNavigationBar;

    public DrawerLayout drawerLayout;//
    public ActionBarDrawerToggle actionBarDrawerToggle;//
    Button btn_confirm, btn_reset;
    Drawable currten_drawable;
    public static String product_name, product_category;
    public static int product_limit_price;
    EditText et_priceLimit, et_productSearch, et_categorySearch;
    Intent serviceIntent;
    NetworkChangeReceiver networkChangeReceiver;
    FrameLayout fragment_container_thing;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipNavigationBar = findViewById(R.id.chipNavBar);
        setPerson();
        p = returnConnectedPerson();
        serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        drawerLayout = findViewById(R.id.my_drawer_layout);//
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);//
        drawerLayout.addDrawerListener(actionBarDrawerToggle);//
        actionBarDrawerToggle.syncState();//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // display drawer icon
        currten_drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_product, null);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        et_priceLimit = findViewById(R.id.et_priceLimit);
        et_categorySearch = findViewById(R.id.et_categorySearch);
        et_productSearch = findViewById(R.id.et_productSearch);
        product_name = sharedPreferences.getString("product_name", "");
        product_category = sharedPreferences.getString("product_category", "");
        product_limit_price = sharedPreferences.getInt("product_price", Integer.MAX_VALUE);
        et_productSearch.setText(product_name);
        et_categorySearch.setText(product_category);
        if (product_limit_price != Integer.MAX_VALUE) {
            et_priceLimit.setText(product_limit_price+"");
        }
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        fragment_container_thing = findViewById(R.id.fragment_container);
        /*
        btn_musicOf = findViewById(R.id.btn_musicOf);
        btn_musicOf = findViewById(R.id.btn_musicOf);
        btn_musicOf.setOnClickListener(this);
        btn_musicOn.setOnClickListener(this);
        btn_musicOf.setVisibility(View.GONE);
        btn_musicOn.setVisibility(View.GONE);*/


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.menu_products, true);


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {


            // הפעולה בודקת האם לחצו על בר הניווט ומעבירה פרגמאנט בהתאם לכך
            @Override
            public void onItemSelected(int i) {

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_white, null);
                Drawable drawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_account, null);
                Drawable drawable3 = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_product, null);
                Drawable drawable4 = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_cart, null);
                Drawable drawable5 = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_yellow, null);

                setPerson();
                Fragment fragment = null;
                switch (i) {
                    case R.id.menu_about:
                        fragment = new AboutFragment();
                        drawerLayout.setBackground(currten_drawable);
                        currten_drawable = drawable;

                        break;
                    case R.id.menu_products:
                        fragment = new ProductsFragment();
                        drawerLayout.setBackground(currten_drawable);
                        currten_drawable = drawable3;
                        break;
                    case R.id.menu_accSettings:
                        fragment = new AccountFragment();
                        drawerLayout.setBackground(currten_drawable);
                        currten_drawable = drawable2;
                        break;
                    case R.id.menu_cart:
                        if (generalConnectedPerson == null) {
                            unauthorizedAccess("login/ signup");

                        } else {
                            fragment = new CartFragment();
                            drawerLayout.setBackground(currten_drawable);
                            currten_drawable = drawable4;
                        }

                        break;
                    case R.id.menu_upload:
                        if (generalConnectedPerson == null || !(generalConnectedPerson instanceof Partner)) {
                            //chipNavigationBar.setItemEnabled(R.id.menu_upload,false);
                            if (generalConnectedPerson == null) {
                                unauthorizedAccess("login/ signup");
                            } else {
                                {
                                    unauthorizedAccess("add extra info");
                                }
                            }
                        } else {
                            fragment = new UploadItemFragment();
                            drawerLayout.setBackground(currten_drawable);
                            currten_drawable = drawable5;
                            // drawerLayout.setBackground(drawable);
                        }
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    anim();
                    ft.replace(R.id.fragment_container, fragment).commit();
                }
            }
        });
    }

    public void anim() {
        int x = fragment_container_thing.getRight();
        int y = fragment_container_thing.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(fragment_container_thing.getWidth(), fragment_container_thing.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(fragment_container_thing, x, y, startRadius, endRadius);
        anim.setDuration(600);
        fragment_container_thing.setVisibility(View.VISIBLE);
        anim.start();

    }

    @Override
    public void onClick(View view) {
        if (view == btn_reset) { // when the user want to reset and arise the filters
            resetFilterParams();
            closeKeyboard(MainActivity.this, view);
            drawerLayout.close();
            handleFilter();

        }
        if (view == btn_confirm) { // when the user want to search a product based on the filters
            String price = et_priceLimit.getText().toString().trim();
            if (price.length() <= 6) {
                closeKeyboard(MainActivity.this, view);
                drawerLayout.close();
                product_name = et_productSearch.getText().toString().trim();
                product_category = et_categorySearch.getText().toString().trim();
                if (!price.equals("")) {
                    product_limit_price = Integer.parseInt(price);

                }
                if (price.equals("")) {
                    product_limit_price = Integer.MAX_VALUE;
                }
                setInfoSp();
                handleFilter();
            } else {
                et_priceLimit.setError("price must be less than 1M");
                product_limit_price = Integer.MAX_VALUE;
            }
        }

    }

    // the function save the filters in the SP
    public void setInfoSp(){
        editor.putString("product_name", product_name);
        editor.putString("product_category", product_category);
        editor.putInt("product_price", product_limit_price);
        editor.apply();

    }

    // this function reload the product fragment base on the selected item in the NAV bar
    public void handleFilter() {
        if (chipNavigationBar.getSelectedItemId() == R.id.menu_products) {  // already in the product fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new ProductsFragment()).commit();
        } else { // not in the product fragment
            chipNavigationBar.setItemSelected(R.id.menu_products, true);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }


    // handle the network connections & notifications
    @Override
    protected void onResume() {
        super.onResume();
        callNetworkCheck();
        serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }


    public void callNetworkCheck() {
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }


    //dialog that appears when the user try to start a fragment that he doesn't have the premonitions to.
    public void unauthorizedAccess(String reason) {
        AlertDialog.Builder unauthorizedAccessDialog = new AlertDialog.Builder(MainActivity.this);
        unauthorizedAccessDialog.setTitle("Unauthorized Access")
                .setMessage("In order to get access you need to " + reason)
                .setIcon(R.drawable.icon_person)
                .setCancelable(true)
                .setPositiveButton("Ok, I'm ready", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chipNavigationBar.setItemSelected(R.id.menu_accSettings, true);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        chipNavigationBar.setItemSelected(R.id.menu_products, true);

                    }
                });
        unauthorizedAccessDialog.show();
    }


    // reset the filter params so all the products will appear.
    public void resetFilterParams() {
        et_productSearch.setText("");
        et_priceLimit.setText("");
        et_categorySearch.setText("");
        product_limit_price = Integer.MAX_VALUE;
        product_name = "";
        product_category = "";
        setInfoSp();
    }

    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
