package me.csci3170.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Order {
    String oid, uid, date, shippingState;
    List<String> isbnList;
    int quantity;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Order() {
        date = dateFormat.format(new Date());
    }

    public Order(String oid, String uid, List<String> isbnList) {
        this.oid = oid;
        this.uid = uid;
        date = dateFormat.format(new Date());
        this.isbnList = isbnList;
        quantity = isbnList.size();
        shippingState = "ordered";
    }

    public Order(String oid, String uid, String date, List<String> isbnList, int quantity, String shippingState) {
        this.oid = oid;
        this.uid = uid;
        this.date = date;
        this.isbnList = isbnList;
        this.quantity = quantity;
        this.shippingState = shippingState;
    }

    public static Order createOrder(String[] metaData) {
        return new Order(metaData[0], metaData[1], metaData[2],
                Arrays.stream(metaData[3].split(", ")).toList(), Integer.parseInt(metaData[4]), metaData[5]);
        // String : "3-7503-0256-1, 2-0208-4796-3" -> ["3-7503-0256-1", "2-0208-4796-3"]
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

    public List<String> getIsbnList() {
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
