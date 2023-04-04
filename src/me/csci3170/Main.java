package me.csci3170;

import me.csci3170.model.Book;
import me.csci3170.model.Customer;
import me.csci3170.model.Order;


import java.sql.PreparedStatement;
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
        main.databaseManager.startConnection();
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
                e.printStackTrace();
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
        System.out.println("--------------------------");
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
                    case 2 -> {
                        System.out.println("RUN CASE 1_2");
                        runOption1_2();
                    }
                    case 3 -> {
                        databaseManager.updateDatabase("DROP TABLE books");
                        databaseManager.updateDatabase("DROP TABLE orders");
                        databaseManager.updateDatabase("DROP TABLE customers");
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
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } while (true);
    }

    public void runOption1_1() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {

            String BooksSql = "CREATE TABLE " + DatabaseManager.TABLE_BOOKS + " (ISBN CHAR(13) not NULL, Title VARCHAR(100) not NULL, Authors VARCHAR(100) not NULL," +
                    "Price int NOT NULL CHECK(Price>=0), Inventory_Quantity int NOT NULL CHECK(Inventory_Quantity>=0), PRIMARY KEY(ISBN) )";
            String CustomersSql = "CREATE TABLE " + DatabaseManager.TABLE_CUSTOMERS + " (UID VARCHAR(10) not NULL, Name VARCHAR(50) not NULL, Address VARCHAR(200) not NULL, PRIMARY KEY(UID) )";
            String OrdersSql = "CREATE TABLE " + DatabaseManager.TABLE_ORDERS + " (OID VARCHAR(8) not NULL, UID VARCHAR(10) not NULL, Order_Date VARCHAR(10) not NULL, " +
                    "Order_ISBN CHAR(200) not NULL, Order_Quantity int NOT NULL CHECK(Order_Quantity>=0), Shipping_Status VARCHAR(8) not NULL, PRIMARY KEY(OID) )";

            databaseManager.updateDatabase(BooksSql);
            databaseManager.updateDatabase(CustomersSql);
            databaseManager.updateDatabase(OrdersSql);//CREATE TABLES
            System.out.println("Tables created successfully...");


            // Clean-up environment

        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }


    }

    public void runOption1_2() throws SQLException {
        List<Book> books = ModelInitializer.getBook();
        List<Order> orders = ModelInitializer.getOrder();
        List<Customer> customers = ModelInitializer.getCustomer();
        databaseManager.insertDatabase(books, Book.class);
        databaseManager.insertDatabase(orders, Order.class);
        databaseManager.insertDatabase(customers, Customer.class);
        System.out.println("Records loaded successfully.");
    }

    public void printOption1() {
        System.out.println("===== Database Initialization =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("--------------------------");
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
                    case 1 -> runOption2_1();
                    case 2 -> runOption2_2();
                    case 3 -> runOption2_3();
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("No such option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (true);
    }

    public void runOption2_1() throws SQLException {
        do {
            System.out.println("Please choose either one way to search the book.");
            System.out.println("> 1. ISBN"); //SQL query
            System.out.println("> 2. Title"); // Create order
            System.out.println("> 3. Author");

            try {
                int input = scanner.nextInt();
                ResultSet resultSet;
                System.out.println("Please enter the info: ");
                String info = scanner.next();

                switch (input) {
                    case 1 -> {
                        // TODO: SQL Query
                        resultSet = databaseManager.queryDatabase("SELECT * FROM Books WHERE ISBN = \"" + info + "\"");
                        Boolean notFound = true;
                        do {
                            if (resultSet.next()){
                                notFound = false;
                                String ISBN = resultSet.getString("ISBN");
                                String Title = resultSet.getString("Title");
                                String Authors = resultSet.getString("Authors");
                                int Price = resultSet.getInt("Price");
                                int InventoryQuantity = resultSet.getInt("Inventory_Quantity");
                                System.out.println("--------------------------");
                                System.out.println("ISBN: " + ISBN);
                                System.out.println("Title: " + Title);
                                System.out.println("Authors: " + Authors);
                                System.out.println("Price: " + Price);
                                System.out.println("Inventory Quantity: " + InventoryQuantity);
                                System.out.println("--------------------------");
                            }
                            else if(notFound){
                                System.out.println("No Book is found");
                            }
                        }
                        while (resultSet.next());
                        return;
                    }
                    case 2 -> {
                        // TODO: SQL Query
                        System.out.println("searching with title");
                        resultSet = databaseManager.queryDatabase("SELECT * FROM Books WHERE Title LIKE \"%" + info +"%\"");
                        Boolean notFound = true;
                        do {
                            if (resultSet.next()){
                                notFound = false;
                                String ISBN = resultSet.getString("ISBN");
                                String Title = resultSet.getString("Title");
                                String Authors = resultSet.getString("Authors");
                                int Price = resultSet.getInt("Price");
                                int InventoryQuantity = resultSet.getInt("Inventory_Quantity");
                                System.out.println("--------------------------");
                                System.out.println("ISBN: " + ISBN);
                                System.out.println("Title: " + Title);
                                System.out.println("Authors: " + Authors);
                                System.out.println("Price: " + Price);
                                System.out.println("Inventory Quantity: " + InventoryQuantity);
                                System.out.println("--------------------------");
                            }
                            else if(notFound){
                                System.out.println("No Book is found");
                            }
                        }
                        while (resultSet.next());
                        return;
                    }
                    case 3 -> {
                        // TODO: SQL Query
                        // Caution: Author is an array and info is a string for one author

                        resultSet = databaseManager.queryDatabase("SELECT * FROM Books WHERE Authors LIKE \"%" + info +"%\"");
                        Boolean notFound = true;
                        do {
                            if (resultSet.next()){
                                notFound = false;
                                String ISBN = resultSet.getString("ISBN");
                                String Title = resultSet.getString("Title");
                                String Authors = resultSet.getString("Authors");
                                int Price = resultSet.getInt("Price");
                                int InventoryQuantity = resultSet.getInt("Inventory_Quantity");
                                System.out.println("--------------------------");
                                System.out.println("ISBN: " + ISBN);
                                System.out.println("Title: " + Title);
                                System.out.println("Authors: " + Authors);
                                System.out.println("Price: " + Price);
                                System.out.println("Inventory Quantity: " + InventoryQuantity);
                                System.out.println("--------------------------");
                            }
                            else if(notFound){
                                System.out.println("No Book is found");
                            }
                        }
                        while (resultSet.next());

                        System.out.println(resultSet);
                        return;
                    }
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }

                // TODO: Print resultSet with format

            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }

        } while (true);
    }

    // TODO: SQL Query
    public boolean checkStock(String ISBN) throws SQLException {
        ResultSet resultSet = databaseManager.queryDatabase("SELECT * FROM Books WHERE ISBN = \"" + ISBN + "\"");
        int stock = resultSet.getInt("Inventory_Quantity");
        return stock > 0;
    }

    public void runOption2_2() throws SQLException {
        do {
            boolean flag;
            System.out.println("Please enter the number of book to be ordered: ");
            try {
                int input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Invalid input.");
                    scanner.nextLine();
                    continue;
                }
                flag = true;
                for (int i = 0; i < input; i++) {
                    System.out.println("Please enter the ISBN of the book: ");
                    String isbn = scanner.nextLine();
                    if (!checkStock(isbn)) {
                        System.out.println("Sorry, this book is out of stock.");
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("Sorry, the order is failed because some book is out of stock.");

                } else {
                    System.out.println("Thank you! The order is placed successfully.");
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }

        } while (true);
    }

    public void runOption2_3() throws SQLException {
        System.out.println("Please enter your name: ");
        String name = scanner.next();
        ResultSet resultSet = databaseManager.queryDatabase("SELECT UID FROM Customers WHERE Name = '" + name + "'"); // select all orders with this customer
        resultSet.next();
        String uID = resultSet.getString("UID");
        // TODO: SQL Query
        resultSet = databaseManager.queryDatabase("SELECT * FROM Orders WHERE UID = '" + uID + "'"); // select all orders with this customer
        while (resultSet.next()) {
            String orderOID = resultSet.getString("OID");
            String orderISBN = resultSet.getString("Order_ISBN");
            int orderQuantity = resultSet.getInt("Order_Quantity");
            String shippingStatus = resultSet.getString("Shipping_Status");
            Date orderDate = resultSet.getDate("Order_Date");
            System.out.println("Order ID: " + orderOID);
            System.out.println("ISBN: " + orderISBN);
            System.out.println("Quantity: " + orderQuantity);
            System.out.println("Shipping Status: " + shippingStatus);
            System.out.println("Order Date: " + orderDate);
            System.out.println("-----------------------");
        }
        // TODO: Print result
    }

    public void printOption2() {
        System.out.println("===== Customer Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("--------------------------");
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
                    case 1 -> runOption3_1();
                    case 2 -> runOption3_2();
                    case 3 -> runOption3_3();
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

    public void runOption3_1() throws SQLException {
        System.out.println("Please enter the order ID to be updated: ");
        String orderID = scanner.next();
        // TODO: SQL Query
        ResultSet resultSet = databaseManager.queryDatabase("SELECT Shipping_Status FROM Orders WHERE OID = '" + orderID + "'"); // Get the shipping status first
        resultSet.next();
        String shippingStatus = resultSet.getString("Shipping_Status");
        if (shippingStatus.contentEquals("shipped")) {
            System.out.println("Update failed! This order is already shipped.");
            return;
        }
        // TODO: SQL Query
        databaseManager.updateDatabase("UPDATE Orders SET Shipping_Status = 'shipped' WHERE OID = '" + orderID + "'"); // Update the shipping status of the order (from ordered to shipped)
        System.out.println("Update success! This order is shipped.");
        // TODO: Print result
    }

    public void runOption3_2() throws SQLException {
        System.out.println("Please enter the shipping status: ");
        String shippingStatus = scanner.nextLine();
        // TODO: SQL Query
        ResultSet resultSet = databaseManager.queryDatabase(""); // select all orders with shippingStatus
        // TODO: Print result
    }

    public void runOption3_3() throws SQLException {
        try {
            System.out.println("Enter number of N: ");
            int input = scanner.nextInt();

            // TODO: SQL Query
            ResultSet resultSet = databaseManager.queryDatabase(""); // use count ISBN in order? Then get the first N ISBN
            String[] isbnList = (String[]) resultSet.getArray("").getArray();

            for (String isbn : isbnList) {
                // TODO: SQL Query
                resultSet = databaseManager.queryDatabase(""); // search book by isbn
                // TODO: Print the book info same as option2_1
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }

    }

    public void printOption3() {
        System.out.println("===== Bookstore Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("--------------------------");
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
