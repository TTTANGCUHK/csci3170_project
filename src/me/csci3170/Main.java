package me.csci3170;

import me.csci3170.model.Book;
import me.csci3170.model.Customer;
import me.csci3170.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    int bookRecords = 0, customerRecords = 0, orderRecords = 0;
    Scanner scanner = new Scanner(System.in);
    DatabaseManager databaseManager = new DatabaseManager();


    public static void main(String[] args) throws SQLException {
	// write your code here
        Main main = new Main();
//        main.databaseManager.startConnection();
        int input;
        do {
            main.printMainMenu();
            try {
                input = main.scanner.nextInt();
                switch (input) {
                    case 1 -> main.runOption1();
                    case 2 -> main.runOption2();
                    case 3 -> main.runOption3();
                    case 4 -> main.printOption4();
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException | SQLException e) {
                input = -1;
                System.out.println("Invalid input.");
                main.scanner.nextLine();
            }

        } while (input != 4);

    }

    public void printMainMenu() {
        System.out.println("===== Welcome to Book Ordering Management System =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println(" + Database Records: Books (" + bookRecords +
                "), Customers (" + customerRecords +
                "), Orders (" + orderRecords + ")");
        System.out.println("——————————————————————————");
        System.out.println("> 1. Database Initialization");
        System.out.println("> 2. Customer Operation");
        System.out.println("> 3. Bookstore Operation");
        System.out.println("> 4. Quit");
        System.out.print(">>> Please Enter Your Query: ");
    }

    public void runOption1() throws SQLException {
        int input;
        do {
            printOption1();
            try {
                input = scanner.nextInt();
                switch (input) {
                    case 1 -> runOption1_1();
                    case 2 -> runOption1_2();
                    case 3 -> {
                        databaseManager.updateDatabase("DROP TABLE Book");
                        databaseManager.updateDatabase("DROP TABLE Order");
                        databaseManager.updateDatabase("DROP TABLE Customer");
                        runOption1_1();
                        runOption1_2();
                    }
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException | SQLException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }

        } while (true);
    }

    public void runOption1_1() throws SQLException {
        databaseManager.updateDatabase(""); //CREATE TABLES
    }

    public void runOption1_2() throws SQLException {
        List<Book> books = ModelInitializer.getBook();
        List<Order> orders = ModelInitializer.getOrder();
        List<Customer> customers = ModelInitializer.getCustomer();
        databaseManager.insertDatabase(books, Book.class);
        databaseManager.insertDatabase(orders, Order.class);
        databaseManager.insertDatabase(customers, Customer.class);
    }

    public void printOption1() {
        System.out.println("===== Database Initialization =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("——————————————————————————");
        System.out.println("> 1. Initialize the database");
        System.out.println("> 2. Load initial records from local files");
        System.out.println("> 3. Reset all the existing records");
        System.out.println("> 4. Back to Main Menu");
        System.out.print(">>> Please Enter Your Query: ");
    }

    public void runOption2() throws SQLException {
        int input;
        do {
            printOption2();
            try {
                input = scanner.nextInt();
                switch (input) {
                    case 1 -> {}
                    case 2 -> {}
                    case 3 -> {}
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (true);
    }

    public void runOption2_1(String title) throws SQLException {
        ResultSet resultSet = databaseManager.queryDatabase("SELECT ISBN FROM Book WHERE Title = " + title);
        System.out.println("Search Result: " + resultSet.getString("Title"));
    }

    public void runOption2_2() throws SQLException {
        databaseManager.queryDatabase(""); // SEARCH BOOK
    }

    public void printOption2() {
        System.out.println("===== Customer Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("——————————————————————————");
        System.out.println("> 1. Search a book"); //SQL query
        System.out.println("> 2. Place an order"); // Create order
        System.out.println("> 3. Check history orders");
        System.out.println("> 4. Back to Main Menu");
        System.out.print(">>> Please Enter Your Query: ");
    }

    public void runOption3() throws SQLException {
        int input;
        do {
            printOption3();
            try {
                input = scanner.nextInt();
                switch (input) {
                    case 1 -> {}
                    case 2 -> {}
                    case 3 -> {}
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (true);
    }

    public void printOption3() {
        System.out.println("===== Bookstore Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("——————————————————————————");
        System.out.println("> 1. Update an order");
        System.out.println("> 2. Query an order");
        System.out.println("> 3. Get N most popular books");
        System.out.println("> 4. Back to Main Menu");
        System.out.print(">>> Please Enter Your Query: ");
    }

    public void printOption4() throws SQLException {
        System.out.println("Disconnecting the database...");
        databaseManager.closeConnection();
        System.out.println("Database disconnected.");
    }
}
