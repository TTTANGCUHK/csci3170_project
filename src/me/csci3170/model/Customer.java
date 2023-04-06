package me.csci3170.model;

public class Customer {

    // Variables to store the information of Customer
    String uid, name, address;

    // Constructor method for Customer
    public Customer(String uid, String name, String address) {
        this.uid = uid;
        this.name = name;
        this.address = address;
    }

    // Function to create a new Customer instance
    public static Customer createCustomer(String[] metaData) {
        return new Customer(metaData[0], metaData[1], metaData[2]);
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
