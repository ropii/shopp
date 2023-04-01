package com.example.shop.objects;


import java.sql.Time;

// אוביקט מסוג מוצר חייב להכיל: שם, קטגוריה, תמונה( כתתובת של תמנונה לאחסון בענן), מחיר ותג
// לאוביקט מסוג זה יש אופציה להכיל תיאור( לא חובה להיות בעל תיאור)
public class Product {
    private String name;
    private String category;
    private String description;
    private String imgUrl;
    private String productId;
    private int price;
    private String uploader_email;
    private Date purchaseDate, uploadDate;

    public Product(String name, String category, String imgId, int price,String productId, String uploader_email) {
        this.name = name;
        this.category = category;
        this.imgUrl = imgId;
        this.price = price;
        this.description="";
        this.productId = productId;
        this.uploader_email = uploader_email;
        this.uploadDate = Date.getCurrentDate();
    }
    public Product(String name,String category, String  imgId, int price,String productId,String description, String uploader_email) {
        this.name = name;
        this.category = category;
        this.imgUrl = imgId;
        this.price = price;
        this.productId = productId;
        this.description = description;
        this.uploader_email = uploader_email;
        this.uploadDate = Date.getCurrentDate();

    }
    public Product(){}

    public boolean isEquals(Product p){
        return p.getProductId().equals(this.productId);

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUploader_email() {
        return uploader_email;
    }

    public void setUploader_email(String uploader_email) {
        this.uploader_email = uploader_email;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
