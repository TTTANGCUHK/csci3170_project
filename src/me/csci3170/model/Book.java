package me.csci3170.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Book {
    static int bookCounter = 0;
    String isbn, title;
    String authors;
    double price;
    int stock;

    // Empty constructor
    public Book() {
        bookCounter++;
    }

    // Constructor with initializing the data
    public Book(String isbn, String title, String authors, double price, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.stock = stock;
        bookCounter++;
    }

    public static Book createBook(String[] metaData) {
        return new Book(metaData[0], metaData[1], metaData[2]
                , Double.parseDouble(metaData[3]), Integer.parseInt(metaData[4]));
    }

    @Override
    public String toString() {
        return "[ISBN = " + this.isbn
                + ", Title = " + this.title
                + ", Authors = " + this.authors
                + ", Price = " + this.price
                + ", Stock = " + this.stock + "]";
    }

    // Getter-Method
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public double getPrice() {
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

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
