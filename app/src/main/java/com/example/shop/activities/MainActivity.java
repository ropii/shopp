package com.example.shop.activities;

import static android.app.PendingIntent.getActivity;

import static com.example.shop.functions.Functions.generalConnectedPerson;
import static com.example.shop.functions.Functions.returnConnectedPerson;
import static com.example.shop.functions.Functions.setPerson;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    ChipNavigationBar chipNavigationBar;

    public DrawerLayout drawerLayout;//
    public ActionBarDrawerToggle actionBarDrawerToggle;//
    ImageButton btn_musicOf, btn_musicOn;
    Boolean boolean_music;
    Button btn_confirm,btn_reset;
    public static String product_name="";
    public static int product_limit_price=Integer.MAX_VALUE;
    EditText et_priceLimit, et_productSearch;
    Intent serviceIntent;
    NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipNavigationBar = findViewById(R.id.chipNavBar);
        setPerson();
        p = returnConnectedPerson();





        drawerLayout = findViewById(R.id.my_drawer_layout);//
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);//
        drawerLayout.addDrawerListener(actionBarDrawerToggle);//
        actionBarDrawerToggle.syncState();//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        et_priceLimit = findViewById(R.id.et_priceLimit);
        et_productSearch = findViewById(R.id.et_productSearch);
        btn_reset= findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        btn_musicOf = findViewById(R.id.btn_musicOf);
        btn_musicOn = findViewById(R.id.btn_musicOn);
        btn_musicOf.setOnClickListener(this);
        btn_musicOn.setOnClickListener(this);
        btn_musicOf.setVisibility(View.GONE);
        btn_musicOn.setVisibility(View.GONE);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.menu_products, true);


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {


            // הפעולה בודקת האם לחצו על בר הניווט ומעבירה פרגמאנט בהתאם לכך
            @Override
            public void onItemSelected(int i) {

                Fragment fragment = null;
                switch (i) {
                    case R.id.menu_about:
                        fragment = new AboutFragment();
                        break;
                    case R.id.menu_products:
                        fragment = new ProductsFragment();
                        break;
                    case R.id.menu_accSettings:
                        fragment = new AccountFragment();
                        break;
                    case R.id.menu_cart:
                        if (generalConnectedPerson == null){
                            unauthorizedAccess("login/ signup");

                        }
                        else{
                        fragment = new CartFragment();}
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
                        }
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ft.replace(R.id.fragment_container, fragment).commit();

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view==btn_reset){ // when the user want to reset and arise the filters
            et_productSearch.setText("");
            et_priceLimit.setText("");
            product_limit_price=Integer.MAX_VALUE;
            product_name="";
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            drawerLayout.close();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container,  new ProductsFragment()).commit();
            //chipNavigationBar.setItemSelected(R.id.menu_products, true);

        }
        if (view==btn_confirm){ // when the user want to search a product based on the filters
            String price = et_priceLimit.getText().toString();
            if ( price.length()<=6){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            drawerLayout.close();
            product_name=et_productSearch.getText().toString();
            if (!price.equals("")) {
                product_limit_price = Integer.parseInt(price);
            }
            if (price.equals("")){
                product_limit_price=Integer.MAX_VALUE;
            }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,  new ProductsFragment()).commit();
            }
            else{
                et_priceLimit.setError("price must be less than 1M");
                product_limit_price=Integer.MAX_VALUE;
            }
        }
        if (view==btn_musicOn){

        }
        if (view==btn_musicOf){

        }

    }

    @Override//
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//
        //
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {//
            return true;//

        }//
//
//
        return super.onOptionsItemSelected(item);//
    }//




    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }


    // handle the network connections
    @Override
    protected void onResume() {
        super.onResume();
        callNetworkCheck();
    }
    public void callNetworkCheck(){
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }


    //dialog that appears when the user try to start a fragment that he doesn't have the premonitions to.
    public void unauthorizedAccess(String reason) {
        AlertDialog.Builder unauthorizedAccessDialog = new AlertDialog.Builder(MainActivity.this);
        unauthorizedAccessDialog.setTitle("Unauthorized Access")
                .setMessage("In order to get access you need to "+reason)
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
                    }
                });
        unauthorizedAccessDialog.show();
    }


}