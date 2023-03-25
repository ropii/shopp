package com.example.shop.objects;

import java.util.ArrayList;


// קלאס של איש, לאיש יש שם, דרכי התחברות ועגלה.
// לאוביקט מסוג זה לא קיימת האפשרות להעלות ולקנות מוצרים
public class Person {
    private String firstName, lastName,email,password;
    private ArrayList<Product> cart = new ArrayList<>();


    public Person(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }


    public Person() {

    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Product> getCart() {
        return cart;
    }
    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }


    public void removeFromCart(Product product) {
        cart.remove(product);
        }

}
