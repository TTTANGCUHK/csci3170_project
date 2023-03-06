package me.csci3170.model;

import java.util.ArrayList;

public class Book {
    static int bookCounter = 0;
    String isbn, title;
    ArrayList<String> authors;
    int price, stock;

    // Empty constructor
    public Book() {
        bookCounter++;
    }

    // Constructor with initializing the data
    public Book(String isbn, String title, ArrayList<String> authors, int price, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.stock = stock;
        bookCounter++;
    }

    // Getter-Method
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }


    // Setter-Method
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
