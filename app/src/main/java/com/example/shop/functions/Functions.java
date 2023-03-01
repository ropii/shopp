package com.example.shop.functions;

import android.util.Log;

import com.example.shop.objects.Partner;
import com.example.shop.objects.Person;
import com.example.shop.objects.CreditCard;
import com.example.shop.objects.Date;
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
        Functions.generalConnectedPerson = null; // מופיע פה NULL למקרה והמשתמש מתנתק

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
