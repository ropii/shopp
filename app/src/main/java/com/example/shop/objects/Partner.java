package com.example.shop.objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
/*
 הקלאס הזה הוא יורש של PERSON
 על מנת להיות שותף(מאפשר להעלות מוצרים ולקנותם) על המשתמש להוסיף פרטים נוספים:
  כרטיס אשראי( אוביקט) ומיקוד.

  תכונות נוספות אשר קיימות לשותף שאין לאיש הן:
 אריי-ליסט של היסטורית הקניות
  אריי-ליסט של המוצרים שהעלה
  מילון שבו לתג המוצר שהעלה יש את הערך של אימייל הקונה

 */
public class Partner extends Person {

    CreditCard card;
    int zip;
    private ArrayList<Product> history= new ArrayList<>();
    private ArrayList<Product> items = new ArrayList<>();
    private int money=0;
    HashMap<String, String> orders = new HashMap<String, String>();

    public HashMap<String, String> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, String> orders) {
        this.orders = orders;
    }

    public Partner(String firstName, String lastName, String email, String password, CreditCard card, int zip) {
        super(firstName, lastName, email, password);
        Log.d("spuper", "Partner: " + super.getFirstName());
        this.setCart(super.getCart());
        this.card = card;
        this.zip = zip;
    }

    public Partner(Person p, CreditCard card, int zip) {
        super(p.getFirstName(), p.getLastName(), p.getEmail(), p.getPassword());
        this.setCart(p.getCart());
        this.card = card;
        this.zip = zip;
    }
    public Partner(){

    }


    public CreditCard getCard() {
        return card;
    }
    public void setCard(CreditCard card) {
        this.card = card;
    }
    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }
    public ArrayList<Product> getHistory() {
        return history;
    }
    public void setHistory(ArrayList<Product> history) {
        this.history = history;
    }




    public ArrayList<Product> getItems() {
        return items;
    }
    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public void removeItem(Product product) {
        for (int i = 0; i <this.getItems().size() ; i++) {
            if (this.getItems().get(i).isEquals(product)){
                this.items.remove(i);
                break;
            }
        }
        }
}

