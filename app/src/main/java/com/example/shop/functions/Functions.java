package com.example.shop.functions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.shop.objects.Partner;
import com.example.shop.objects.Person;
import com.example.shop.objects.CreditCard;
import com.example.shop.objects.Date;
import com.example.shop.objects.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *                             מופיעות פה פעולות שחוזרות על עצמן
 */
public class Functions {

    public static Person generalConnectedPerson = null;


    // הפעולה בודקת את תקינות האימייל
    public static boolean isValidEmailAddress(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    //הפעולה מחזירה אמת אם המשתמש מחובר ושקר אחרת
    public static boolean isSignIn() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null;
    }

    // הפעולה מחזירה את המשתמש המחובר(אם המשתמש לא מחובר מחזיר NULL)
    public static FirebaseUser returnUser() {
        if (isSignIn()) {
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            return currentUser;
        } else {
            return null;
        }
    }


    // מחזיר את "האיש הכללי המחובר"(משתנה ב FUNCTIONS) ובעזרתו אפשר לגשת לנתונים של המשתמש שהובאו מהפייר-סטור
    public static Person returnConnectedPerson() {
        setPerson();
        return generalConnectedPerson;
    }


    //מעדכן את "האיש הכללי המחובר"(משתנה ב FUNCTIONS) לאיש שמחובר על מנת לגשת לנתוניו
    public static void setPerson() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (isSignIn()) {

            DocumentReference docRef = db.collection("users").document(returnUser().getEmail());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("user_data", ""+documentSnapshot.get("money"));
                    if (documentSnapshot.get("money")==null){ // במקרה והמשתמש לא הוסיף פרטים נוספים
                        Functions.generalConnectedPerson = documentSnapshot.toObject(Person.class);}
                    else {// במקרה והמשתמש כן הוסיף פרטים נוספים
                        Functions.generalConnectedPerson = documentSnapshot.toObject(Partner.class);
                        Log.d("user_data_kind", ""+generalConnectedPerson.getClass());


                    }
                }
            });
        }
        else {
            Functions.generalConnectedPerson = null; // מופיע פה NULL למקרה והמשתמש מתנתק
        }
    }
    // open the email in order to talk about a product
    public static void openEmail(Product selectedProduct, Context context){
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{selectedProduct.getUploader_email()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "SHOP, " + selectedProduct.getName());
            intent.putExtra(Intent.EXTRA_TEXT, "Hi, I'm interested in a product that you have uploaded - " + selectedProduct.getName() + ". \n"+Functions.generalConnectedPerson.getFirstName() +"." );
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No email app found on your device", Toast.LENGTH_SHORT).show();
        }
    }
    // open the email in order to talk about a product aget you bought it
    public static void openEmailAfterBuy(Product selectedProduct, Context context){
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{selectedProduct.getUploader_email()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "SHOP, " + selectedProduct.getName());
            intent.putExtra(Intent.EXTRA_TEXT, "Hi, I bought a product that you have uploaded - " + selectedProduct.getName() +" at the date- " +selectedProduct.getPurchaseDate()+". \n"+Functions.generalConnectedPerson.getFirstName() +"." );
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No email app found on your device", Toast.LENGTH_SHORT).show();
        }
    }


    // the functions close the keyboard
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // בדיקת תכנות
    public void test() {
        Date date = new Date(12, 2022);
        CreditCard card = new CreditCard(1, 2, date);
        Person p = new Person("name", "family", "gmail", "pass");
        Person p2 = new Person("name2", "family", "gmail", "pass");
        Person p3 = new Person("name3", "family", "gmail", "pass");
        Partner b1 = new Partner(p, card, 123);
        Partner b2 = new Partner(p2, card, 234);
        Person pArr[] = new Person[]{p2, b1};
        Object tr = new Person("name", "family", "gmail", "pass");
        if (tr.getClass() == Partner.class) {
            ((Partner) tr).getOrders();
        }


    }

}
