

/**
 *                                 לא בשימוש, קיים על מנת ללמוד פייר-בייס
 */

/*
package com.example.shop;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firestore.v1.WriteResult;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFireBase {


    CollectionReference cities = db.collection("citie");

    Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

    Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

    Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

    Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

    Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);

    DocumentReference docRef = db.collection("citie").document("BJ");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            city = documentSnapshot.toObject(City.class);
        }
    });

    */
/*CollectionReference*//*

 cities = db.collection("cities");
    List<ApiFuture<WriteResult>> futures = new ArrayList<>();
                cities
                        .document("SF")
                        .set(
                                new City(
            "San Francisco",
                                        "CA",
                                        "USA",
                                        false,
                                        860000L,
                                Arrays.asList("west_coast", "norcal")));
                cities
                        .document("LA")
                        .set(
                                new City(
            "Los Angeles",
                                        "CA",
                                        "USA",
                                        false,
                                        3900000L,
                                Arrays.asList("west_coast", "socal")));
                cities
                        .document("DC")
                        .set(
                                new City(
            "Washington D.C.", null, "USA", true, 680000L, Arrays.asList("east_coast")));
                cities
                        .document("TOK")
                        .set(
                                new City(
            "Tokyo", null, "Japan", true, 9000000L, Arrays.asList("kanto", "honshu")));
                cities
                        .document("BJ")
                        .set(
                                new City(
            "Beijing",
                                        null,
                                        "China",
                                        true,
                                        21500000L,
                                Arrays.asList("jingjinji", "hebei")));



    */
/*DocumentReference*//*
 docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                    task.getResult().getData();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    });



// asynchronously retrieve the document



*/
/*

        ApiFuture<DocumentSnapshot> future = docRef.get();
// block on response
        DocumentSnapshot document = future.get();
        City city = null;
        if (document.exists()) {
            // convert document to POJO
            city = document.toObject(City.class);
            System.out.println(city);
        } else {
            System.out.println("No such document!");
        }
*//*



}


*/
