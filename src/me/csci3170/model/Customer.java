package me.csci3170.model;

public class Customer {
    static int customerCounter = 0;
    String uid, name, address;

    public Customer() {
        customerCounter++;
    }

    public Customer(String uid, String name, String address) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        customerCounter++;
    }

    // Getter-Method
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    // Setter-Method
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
