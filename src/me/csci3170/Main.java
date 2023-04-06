package me.csci3170;

import me.csci3170.model.Book;
import me.csci3170.model.Customer;
import me.csci3170.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    // Variables to count the records from different tables
    public static int bookRecords = 0, customerRecords = 0, orderRecords = 0;
    Scanner scanner = new Scanner(System.in);
    DatabaseManager databaseManager = new DatabaseManager();

    // Main Function
    public static void main(String[] args) {
        // Create the main instance
        Main main = new Main();
        // Start the database connection
        main.databaseManager.startConnection();
        // Variable to store the input
        int input;
        do {
            // Print the menu
            main.printMainMenu();
            try {
                // Get the user input
                input = main.scanner.nextInt();
                main.scanner.nextLine();
                // Run the functions depend on the input
                switch (input) {
                    case 1 -> main.runOption1();
                    case 2 -> main.runOption2();
                    case 3 -> main.runOption3();
                    case 4 -> main.printOption4();
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException e) {
                // Catch if the input are not integer
                input = -1;
                System.out.println("Invalid input.");
                main.scanner.nextLine();
            } catch (SQLException e) {
                // Print error message for SQL query
                input = -1;
                e.printStackTrace();
            }

        } while (input != 4);
    }

    // Function to print the instruction for main menu
    public void printMainMenu() {
        System.out.println("===== Welcome to Book Ordering Management System =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println(" + Database Records: Books (" + bookRecords +
                "), Customers (" + customerRecords +
                "), Orders (" + orderRecords + ")");
        System.out.println("------------------------------------------");
        System.out.println("> 1. Database Initialization");
        System.out.println("> 2. Customer Operation");
        System.out.println("> 3. Bookstore Operation");
        System.out.println("> 4. Quit");
        System.out.print(">>> Please Enter Your Query: ");
    }

    // Function to run option 1
    public void runOption1() {
        int input;
        do {
            // Print the menu
            printOption1();
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> runOption1_1();
                    case 2 -> runOption1_2();
                    case 3 -> runOption1_3();
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (InputMismatchException | SQLException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }

        } while (true);
    }

    // Function to run option 1.1
    public void runOption1_1() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // Prepare the SQL query for creating tables
            String booksSQL = "CREATE TABLE " + DatabaseManager.TABLE_BOOKS + " (" +
                    DatabaseManager.BOOKS_ISBN + " CHAR(13) not NULL, " +
                    DatabaseManager.BOOKS_TITLE + " VARCHAR(100) not NULL, " +
                    DatabaseManager.BOOKS_AUTHORS + " VARCHAR(100) not NULL, " +
                    DatabaseManager.BOOKS_PRICE + " int NOT NULL CHECK(" +
                    DatabaseManager.BOOKS_PRICE + ">=0), " +
                    DatabaseManager.BOOKS_STOCK + " int NOT NULL CHECK(" +
                    DatabaseManager.BOOKS_STOCK + ">=0), " +
                    "PRIMARY KEY(" + DatabaseManager.BOOKS_ISBN + ") )";

            String ordersSQL = "CREATE TABLE " + DatabaseManager.TABLE_ORDERS + " (" +
                    DatabaseManager.ORDERS_OID + " VARCHAR(8) not NULL, " +
                    DatabaseManager.ORDERS_UID + " VARCHAR(10) not NULL, " +
                    DatabaseManager.ORDERS_DATE + " VARCHAR(10) not NULL, " +
                    DatabaseManager.ORDERS_ISBN + " CHAR(200) not NULL, " +
                    DatabaseManager.ORDERS_QUANTITY + " int NOT NULL CHECK(" +
                    DatabaseManager.ORDERS_QUANTITY + ">=0), " +
                    DatabaseManager.ORDERS_SHIPPING_STATUS + " VARCHAR(8) not NULL, " +
                    "PRIMARY KEY(" + DatabaseManager.ORDERS_OID + ") )";

            String customersSQL = "CREATE TABLE " + DatabaseManager.TABLE_CUSTOMERS + " ("
                    + DatabaseManager.CUSTOMERS_UID + " VARCHAR(10) not NULL, "
                    + DatabaseManager.CUSTOMERS_NAME + " VARCHAR(50) not NULL, "
                    + DatabaseManager.CUSTOMERS_ADDRESS + " VARCHAR(200) not NULL, "
                    + "PRIMARY KEY(" + DatabaseManager.CUSTOMERS_UID + ") )";

            // Creating tables for books, orders and customers
            databaseManager.updateDatabase(booksSQL);
            databaseManager.updateDatabase(customersSQL);
            databaseManager.updateDatabase(ordersSQL);
            System.out.println("Tables created successfully...");

        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }


    }

    // Function to run option 1.2
    public void runOption1_2() throws SQLException {
        // Load the data from csv files
        List<Book> books = ModelInitializer.getBook();
        List<Order> orders = ModelInitializer.getOrder();
        List<Customer> customers = ModelInitializer.getCustomer();

        // Insert the data to database server
        databaseManager.insertDatabase(books, Book.class);
        databaseManager.insertDatabase(orders, Order.class);
        databaseManager.insertDatabase(customers, Customer.class);
        System.out.println("Records loaded successfully.");
    }

    // Function to run option 1.3
    public void runOption1_3() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // Drop all the tables
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_BOOKS);
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_ORDERS);
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_CUSTOMERS);

        // Re-run option 1.1 & 1.2
        runOption1_1();
        runOption1_2();
    }

    // Function to print the instruction for option 1
    public void printOption1() {
        System.out.println("===== Database Initialization =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("------------------------------------------");
        System.out.println("> 1. Initialize the database");
        System.out.println("> 2. Load initial records from local files");
        System.out.println("> 3. Reset all the existing records");
        System.out.println("> 4. Back to Main Menu");
        System.out.print(">>> Please Enter Your Query: ");
    }

    // Function to run option 2
    public void runOption2() throws SQLException {
        int input;
        do {
            printOption2();
            try {
                input = scanner.nextInt();
                scanner.nextLine();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    // Function to run option 2.1
    public void runOption2_1() throws SQLException {
        do {
            System.out.println("Please choose either one way to search the book.");
            System.out.println("> 1. ISBN");
            System.out.println("> 2. Title");
            System.out.println("> 3. Author");

            try {
                // Selecting which way to search the book
                int input = scanner.nextInt();
                scanner.nextLine();
                ResultSet resultSet;
                System.out.println("Please enter the info: ");
                String info = scanner.nextLine();

                switch (input) {
                    case 1 -> { // Search by ISBN
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_ISBN + " = \"" + info + "\"");
                        searchBookInfo(resultSet);
                        return;
                    }
                    case 2 -> { // Search by Title
                        System.out.println("searching with title");
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_TITLE + " LIKE \"%" + info +"%\"");
                        searchBookInfo(resultSet);
                        return;
                    }
                    case 3 -> { // Search by Author
                        // Caution: Author is an array and info is a string for one author
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_AUTHORS + " LIKE \"%" + info +"%\"");
                        searchBookInfo(resultSet);
                        return;
                    }
                    case 4 -> { // Back to option 2 menu
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

    // Function to get the database result for book
    public void searchBookInfo(ResultSet resultSet) throws SQLException {
        boolean notFound = true;
        while (resultSet.next()) {
            notFound = false;
            // Receive data from database
            String ISBN = resultSet.getString(DatabaseManager.BOOKS_ISBN);
            String Title = resultSet.getString(DatabaseManager.BOOKS_TITLE);
            String Authors = resultSet.getString(DatabaseManager.BOOKS_AUTHORS);
            int Price = resultSet.getInt(DatabaseManager.BOOKS_PRICE);
            int Stock = resultSet.getInt(DatabaseManager.BOOKS_STOCK);

            // Formatting the print message for Book Information
            System.out.println("----------- Book Information -----------");
            System.out.println(DatabaseManager.BOOKS_ISBN + ": " + ISBN);
            System.out.println(DatabaseManager.BOOKS_TITLE + ": " + Title);
            System.out.println(DatabaseManager.BOOKS_AUTHORS + ": " + Authors);
            System.out.println(DatabaseManager.BOOKS_PRICE + ": " + Price);
            System.out.println(DatabaseManager.BOOKS_STOCK + ": " + Stock);
            System.out.println("------------------------------------------");
        }
        if(notFound){
            // Print message if no book is found
            System.out.println("No Book is found");
        }
    }

    // Function to check the stock
    public boolean checkStock(String ISBN, List<Integer> stockList) throws SQLException {
        // SQL to get the stock
        ResultSet resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                + " WHERE " + DatabaseManager.BOOKS_ISBN + " = \"" + ISBN + "\"");
        int stock = 0;
        if (resultSet.next()) {
            // Add the result into the stockList
            stock = resultSet.getInt(DatabaseManager.BOOKS_STOCK);
            stockList.add(stock);
        }
        // Return true if stock > 0, false otherwise
        return stock > 0;
    }

    // Function to update the stock, used whenever someone placed an order
    public void updateStock(List<String> listISBN, List<Integer> listStock) throws SQLException {
        // SQL to update the stock
        for (int i = 0; i < listISBN.size(); i++) {
            databaseManager.updateDatabase("UPDATE " + DatabaseManager.TABLE_BOOKS + " SET "
                    + DatabaseManager.BOOKS_STOCK + " = " + (listStock.get(i) - 1) + " WHERE "
                    + DatabaseManager.BOOKS_ISBN + " = '" + listISBN.get(i) + "'");
        }
    }

    // Function to generate an OID
    public int generateOID() throws SQLException {
        int oID;
        Random random = new Random();
        do {
            // Using random to generate the OID
            oID = random.nextInt(99999999);
            // SQL query to check if the OID exist or not
            ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_OID
                    + " FROM " + DatabaseManager.TABLE_ORDERS + " WHERE " + DatabaseManager.ORDERS_OID + " = '" + oID + "'");
            // Try again if OID is already exist, else just break and return the OID
            if (!resultSet.next())
                break;

        } while (true);
        return oID;
    }

    // Function to generate an UID
    public String generateUID() throws SQLException {
        // All UID starts with "U"
        StringBuilder uID = new StringBuilder("U");
        Random random = new Random();
        do {
            // Use random to get the 3 digits
            uID.append(random.nextInt(999));
            // Use random to get one uppercase letter
            uID.append(Character.toString((char) 64 + random.nextInt(26)));
            // SQL query to check if the UID exist or not
            ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                    + " FROM " + DatabaseManager.TABLE_CUSTOMERS
                    + " WHERE " + DatabaseManager.CUSTOMERS_UID + " = '" + uID + "'");
            // Try again if UID is already exist, else just break and return the UID
            if (!resultSet.next())
                break;
        } while (true);
        return uID.toString();
    }

    // Function to generate a customer record
    public void generateCustomer(String uID, String name, String address) throws SQLException {
        // SQL statement to insert a new customer record
        PreparedStatement preparedStatement = databaseManager.getDatabase().prepareStatement("INSERT INTO " + DatabaseManager.TABLE_CUSTOMERS + " VALUES (?, ?, ?)");
        preparedStatement.setString(1, uID);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, address);
        preparedStatement.executeUpdate();
        System.out.println("Account created successfully");
        // Update the counter
        customerRecords++;
    }

    // Function to place an order
    public void placeOrder(List<String> listISBN) throws SQLException {
        // Generate OID
        int oID = generateOID();
        System.out.println("Please enter your UID: ");
        // Try to get user UID
        String uID = scanner.nextLine();

        // SQL query to check if the UID exist or not
        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                + " FROM " + DatabaseManager.TABLE_CUSTOMERS
                + " WHERE " + DatabaseManager.CUSTOMERS_UID + " = '" + uID + "'");
        // If user UID exist, just get the information from database
        if (resultSet.next()) {
            if (resultSet.getString(DatabaseManager.CUSTOMERS_UID).equals(uID))
                uID = resultSet.getString(DatabaseManager.CUSTOMERS_UID);
        } else { // Else, enter the data for creating an account
            System.out.println("UID does not exist, creating a new account...");
            System.out.println("Please enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Please enter your address: ");
            String address = scanner.nextLine();
            generateCustomer(uID, name, address);
        }

        // Reformat the isbn list
        StringBuilder isbns = new StringBuilder("\"");
        for (String isbn : listISBN) {
            isbns.append(isbn).append(",");
        }
        isbns.replace(isbns.lastIndexOf(","), isbns.length(), "\"");

        // SQL query to insert a new order records
        PreparedStatement preparedStatement = databaseManager.getDatabase().prepareStatement("INSERT INTO "
                + DatabaseManager.TABLE_ORDERS + " VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, Integer.toString(oID));
        preparedStatement.setString(2, uID);
        preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(4, isbns.toString());
        preparedStatement.setInt(5, listISBN.size());
        preparedStatement.setString(6, "ordered");
        preparedStatement.executeUpdate();
        // Update the counter
        orderRecords++;
    }

    // Function to run option 2.2
    public void runOption2_2() throws SQLException {
        List<String> listISBN = new ArrayList<>();
        List<Integer> listStock = new ArrayList<>();
        do {
            // Enter the number of books to be ordered
            boolean flag;
            System.out.println("Please enter the number of books to be ordered: ");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input <= 0) {
                    System.out.println("Invalid input.");
                    scanner.nextLine();
                    continue;
                }
                flag = true;
                // Ask ISBN for each book
                for (int i = 0; i < input; i++) {
                    System.out.println("Please enter the ISBN of the book (" + (i + 1) + "/" + input + "): ");
                    String isbn = scanner.nextLine();
                    listISBN.add(isbn);
                    // Return if any book is out of stock
                    if (!checkStock(isbn, listStock)) {
                        System.out.println("Sorry, this book is out of stock.");
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("Sorry, the order is failed because some book is out of stock.");

                } else {
                    // Place an order and update the stock
                    updateStock(listISBN, listStock);
                    placeOrder(listISBN);
                    System.out.println("Thank you! The order is placed successfully.");
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }

        } while (true);
    }

    // Function to get the database result for order
    public void printOrderInfo(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            // Receive data from database
            String orderOID = resultSet.getString(DatabaseManager.ORDERS_OID);
            String orderISBN = resultSet.getString(DatabaseManager.ORDERS_ISBN);
            int orderQuantity = resultSet.getInt(DatabaseManager.ORDERS_QUANTITY);
            String shippingStatus = resultSet.getString(DatabaseManager.ORDERS_SHIPPING_STATUS);
            String orderDate = resultSet.getString(DatabaseManager.ORDERS_DATE);

            // Formatting the print message for Book Information
            System.out.println("------------- Order History -------------");
            System.out.println(DatabaseManager.ORDERS_OID + ": " + orderOID);
            System.out.println(DatabaseManager.ORDERS_ISBN + ": " + orderISBN);
            System.out.println(DatabaseManager.ORDERS_QUANTITY + ": " + orderQuantity);
            System.out.println(DatabaseManager.ORDERS_SHIPPING_STATUS + ": " + shippingStatus);
            System.out.println(DatabaseManager.ORDERS_DATE + ": " + orderDate);
            System.out.println("------------------------------------------");
        }
    }

    // Function to run option 2.3
    public void runOption2_3() throws SQLException {
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();

        // SQL query to ask for the user's name
        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                + " FROM " + DatabaseManager.TABLE_CUSTOMERS + " WHERE " + DatabaseManager.CUSTOMERS_NAME + " = '" + name + "'"); // select all orders with this customer
        if (!resultSet.next()) {
            // If account not exist, ask if the user want to create a new account
            System.out.println("Account does not exist. Creating a new account? (Y/N)");
            String input = scanner.nextLine();
            // If yes, enter customer information and update the database
            if (input.equalsIgnoreCase("Y")) {
                System.out.println("Please enter your address: ");
                String address = scanner.nextLine();
                generateCustomer(generateUID(), name, address);
                // Else, re-run option 2.3
            } else if (input.equalsIgnoreCase("N")) {
                runOption2_3();
                return;
            }
        }
        // If account exist, get the UID of the user
        String uID = resultSet.getString(DatabaseManager.CUSTOMERS_UID);

        // SQL query to get all orders for this user
        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_ORDERS
                + " WHERE " +  DatabaseManager.ORDERS_UID + " = '" + uID + "'"); // select all orders with this customer

        printOrderInfo(resultSet);
    }

    // Function to print the instruction for option 2
    public void printOption2() {
        System.out.println("===== Customer Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("------------------------------------------");
        System.out.println("> 1. Search a book");
        System.out.println("> 2. Place an order");
        System.out.println("> 3. Check history orders");
        System.out.println("> 4. Back to Main Menu");
        System.out.print(">>> Please Enter Your Query: ");
    }

    // Function to run option 3
    public void runOption3() throws SQLException {
        int input;
        do {
            printOption3();
            try {
                input = scanner.nextInt();
                scanner.nextLine();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    // Function to run option 3.1
    public void runOption3_1() throws SQLException {
        System.out.println("Please enter the order ID to be updated: ");
        // Ask for OID to update
        String orderID = scanner.nextLine();

        // SQL to select the order information
        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_SHIPPING_STATUS
                + " FROM " + DatabaseManager.TABLE_ORDERS + " WHERE " + DatabaseManager.ORDERS_OID + " = '" + orderID + "'"); // Get the shipping status first
        if(!resultSet.next()){
            System.out.println("This order does not exist.");
            return;
        }

        // Check if the shipping status is "shipped" or "received
        String shippingStatus = resultSet.getString(DatabaseManager.ORDERS_SHIPPING_STATUS);
        if (shippingStatus.contentEquals("shipped") || shippingStatus.contentEquals("received")) {
            System.out.println("Update failed! This order is already shipped.");
            return;
        }

        // If neither of them, use SQL query to update the shipping status to "shipped" from "ordered"
        databaseManager.updateDatabase("UPDATE " + DatabaseManager.TABLE_ORDERS + " SET "
                + DatabaseManager.ORDERS_SHIPPING_STATUS + " = 'shipped' WHERE "
                + DatabaseManager.ORDERS_OID + " = '" + orderID + "'"); // Update the shipping status of the order (from ordered to shipped)
        System.out.println("Update success! This order is shipped.");
    }

    // Function to run option 3.2
    public void runOption3_2() throws SQLException {
        // Get all the orders by shipping status using SQL query
        System.out.println("Please enter the shipping status: ");
        String shippingStatus = scanner.nextLine();
        ResultSet resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_ORDERS
                + " WHERE " + DatabaseManager.ORDERS_SHIPPING_STATUS + " = '" + shippingStatus + "'"); // select all orders with shippingStatus

        // Print the order information
        printOrderInfo(resultSet);
    }

    // Function to run option 3.3
    public void runOption3_3() throws SQLException {
        try {
            // Request the number
            System.out.println("Enter number of N: ");
            int input = scanner.nextInt();
            scanner.nextLine();

            // If not positive, return
            if (input < 0) {
                System.out.println("Invalid input.");
                return;
            }

            // Use SQL query to get all the ISBN in orders table
            ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_ISBN +
                    " FROM " + DatabaseManager.TABLE_ORDERS);
            ArrayList<String> isbnList = new ArrayList<>();
            // Get all the ISBN
            while (resultSet.next()) {
                isbnList.add(resultSet.getString(DatabaseManager.ORDERS_ISBN));
            }
            // Splitting the ISBN by ","
            ArrayList<String> recordISBN = new ArrayList<>();
            for (String isbn : isbnList) {
                isbn = isbn.replace("\"", "");
                String[] sep_ISBN = isbn.split(",");
                //store separated book ISBN into a list
                recordISBN.addAll(Arrays.asList(sep_ISBN));
            }

            // Store book ISBNs as keys, corresponding occurrences as values
            Map<String, Long> distinctISBN = recordISBN.stream().collect(
                    Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // Sort the map in desc order according to occurrences
            Map<String, Long> DESC_ISBN = new LinkedHashMap<>();
            distinctISBN.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()
                    .reversed()).forEachOrdered(e -> DESC_ISBN.put(e.getKey(), e.getValue()));

            List<String> newISBN = DESC_ISBN.keySet().stream().toList();

            // SQL query to get all book information by the ISBN list
            StringBuilder query = new StringBuilder("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                    + " WHERE " + DatabaseManager.BOOKS_ISBN
                    + " IN (");
            if (newISBN.size() >= input) {
                for (int i = 0; i < input; i++) {
                    query.append("\"").append(newISBN.get(i)).append("\",");
                }
                query.replace(query.lastIndexOf(","), query.length(), ")");
            } else {
                for (String isbn : newISBN) {
                    query.append("\"").append(isbn).append("\",");
                }
                query.replace(query.lastIndexOf(","), query.length(), ")");
            }

            resultSet = databaseManager.queryDatabase(query.toString());
            // Print the book information
            searchBookInfo(resultSet);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }

    }

    // Function to print the instruction for option 3
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

    // Function to print the message for option 4 and close the connection
    public void printOption4() throws SQLException {
        System.out.println("Disconnecting the database...");
        databaseManager.closeConnection();
        System.out.println("Database disconnected.");
    }
}
