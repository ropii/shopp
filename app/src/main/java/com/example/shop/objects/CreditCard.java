package com.example.shop.objects;

/*
 הקלאס משומש שהמשתמש מוסיף פרטים

 */

public class CreditCard {
    int cvc;
    long number;
    Date validation;

    public CreditCard(int cvc, long number, Date validation) {
        this.cvc = cvc;
        this.number = number;
        this.validation = validation;
    }
    public CreditCard() {
    }
    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getValidation() {
        return validation;
    }

    public void setValidation(Date validation) {
        this.validation = validation;
    }
}
