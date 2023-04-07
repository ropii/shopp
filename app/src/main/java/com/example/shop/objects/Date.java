package com.example.shop.objects;

import java.util.Calendar;

/*
 קלאס של תאריך
 כעט הוא משומש בהוספת פרטי המשתמש( החלק של כרטיס האשראי)
 אובייקט זה משומש כאשר מעלים וקונים מוצר
 בהמשך הוא ישומש על מנת לדעת מתי המשלוח אמור להגיע

קיימים שני סוגים של תאריכים-
1. רק חודש ושנה בשביל תוקף כרטיס האשראי
2. יום, חודש ושנה על מנת לדעת מתי המשלוח מגיע

 */
public class Date {
    int day;
    int month;
    int year;


    public Date(int month, int year) {
        this.month = month;
        this.year = year;
        this.day =0;
    }

    public Date() {

    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

     public static Date getCurrentDate() {
         // get the current system time
         Calendar calendar = Calendar.getInstance();
         // extract the day, month, and year from the current time
         int year = calendar.get(Calendar.YEAR);
         int month = calendar.get(Calendar.MONTH) + 1; // add 1 to get the correct month value (Jan = 0)
         int day = calendar.get(Calendar.DAY_OF_MONTH);
         return new Date(day, month, year);
     }
     public String toString(){
        return getDay() +"/" + getMonth() +"/"+ getYear();
     }
}
