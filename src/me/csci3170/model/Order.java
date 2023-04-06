package me.csci3170.model;

public class Order {

    // Variables to store the information of Order
    String oid, uid, date, shippingState;
    String isbnList;
    int quantity;

    // Constructor method for Order
    public Order(String oid, String uid, String date, String isbnList, int quantity, String shippingState) {
        this.oid = oid;
        this.uid = uid;
        this.date = date;
        this.isbnList = isbnList;
        this.quantity = quantity;
        this.shippingState = shippingState;
    }

    // Function to create a new Order instance
    public static Order createOrder(String[] metaData) {
        return new Order(metaData[0], metaData[1], metaData[2],
                metaData[3], Integer.parseInt(metaData[4]), metaData[5]);
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

    public String getIsbnList() {
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

    public void setIsbnList(String isbnList) {
        this.isbnList = isbnList;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }
}
