package me.csci3170.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    String oid, uid, date, shippingState;
    ArrayList<String> isbnList;
    int quantity;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Order() {
        date = dateFormat.format(new Date());
    }

    public Order(String oid, String uid, ArrayList<String> isbnList) {
        this.oid = oid;
        this.uid = uid;
        date = dateFormat.format(new Date());
        this.isbnList = isbnList;
        quantity = isbnList.size();
        shippingState = "ordered";
    }

    // Getter-Method
    public String getOid() {
        return oid;
    }

    public String getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getIsbnList() {
        return isbnList;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getShippingState() {
        return shippingState;
    }


    // Setter-Method
    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsbnList(ArrayList<String> isbnList) {
        this.isbnList = isbnList;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }
}
