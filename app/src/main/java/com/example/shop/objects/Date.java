package com.example.shop.objects;
 /*
 קלאס של תאריך
 כעט בוא משומש בהוספת פרטי המשתמש( החלק של כרטיס האשראי)
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
}
