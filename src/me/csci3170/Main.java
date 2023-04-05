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
    public static int bookRecords = 0, customerRecords = 0, orderRecords = 0;
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
            } catch (InputMismatchException e) {
                input = -1;
                System.out.println("Invalid input.");
                main.scanner.nextLine();
            } catch (SQLException e) {
                input = -1;
                e.printStackTrace();
            }

        } while (input != 4);
        main.databaseManager.closeConnection();
    }

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

    public void runOption1() {
        int input;
        do {
            printOption1();
            try {
                input = scanner.nextInt();
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

    public void runOption1_1() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {

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

            databaseManager.updateDatabase(booksSQL);
            databaseManager.updateDatabase(customersSQL);
            databaseManager.updateDatabase(ordersSQL);//CREATE TABLES
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

    public void runOption1_3() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_BOOKS);
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_ORDERS);
        databaseManager.updateDatabase("DROP TABLE " + DatabaseManager.TABLE_CUSTOMERS);
        runOption1_1();
        runOption1_2();
    }

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
            } catch (SQLException e) {
                e.printStackTrace();
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
                scanner.nextLine();
                ResultSet resultSet;
                System.out.println("Please enter the info: ");
                String info = scanner.nextLine();

                switch (input) {
                    case 1 -> {
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_ISBN + " = \"" + info + "\"");
                        searchBookInfo(resultSet);
                        return;
                    }
                    case 2 -> {
                        System.out.println("searching with title");
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_TITLE + " LIKE \"%" + info +"%\"");
                        searchBookInfo(resultSet);
                        return;
                    }
                    case 3 -> {
                        // Caution: Author is an array and info is a string for one author
                        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                                + " WHERE " + DatabaseManager.BOOKS_AUTHORS + " LIKE \"%" + info +"%\"");
                        searchBookInfo(resultSet);
                        return;
                    }
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

    public void searchBookInfo(ResultSet resultSet) throws SQLException {
        boolean notFound = true;
        while (resultSet.next()) {
            notFound = false;
            String ISBN = resultSet.getString(DatabaseManager.BOOKS_ISBN);
            String Title = resultSet.getString(DatabaseManager.BOOKS_TITLE);
            String Authors = resultSet.getString(DatabaseManager.BOOKS_AUTHORS);
            int Price = resultSet.getInt(DatabaseManager.BOOKS_PRICE);
            int Stock = resultSet.getInt(DatabaseManager.BOOKS_STOCK);
            System.out.println("----------- Book Information -----------");
            System.out.println(DatabaseManager.BOOKS_ISBN + ": " + ISBN);
            System.out.println(DatabaseManager.BOOKS_TITLE + ": " + Title);
            System.out.println(DatabaseManager.BOOKS_AUTHORS + ": " + Authors);
            System.out.println(DatabaseManager.BOOKS_PRICE + ": " + Price);
            System.out.println(DatabaseManager.BOOKS_STOCK + ": " + Stock);
            System.out.println("------------------------------------------");
        }
        if(notFound){
            System.out.println("No Book is found");
        }
    }

    public boolean checkStock(String ISBN, List<Integer> stockList) throws SQLException {
        ResultSet resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_BOOKS
                + " WHERE " + DatabaseManager.BOOKS_ISBN + " = \"" + ISBN + "\"");
        int stock = 0;
        if (resultSet.next()) {
            stock = resultSet.getInt(DatabaseManager.BOOKS_STOCK);
            stockList.add(stock);
        }
        return stock > 0;
    }

    public void updateStock(List<String> listISBN, List<Integer> listStock) throws SQLException {
        for (int i = 0; i < listISBN.size(); i++) {
            databaseManager.updateDatabase("UPDATE " + DatabaseManager.TABLE_BOOKS + " SET "
                    + DatabaseManager.BOOKS_STOCK + " = " + (listStock.get(i) - 1) + " WHERE "
                    + DatabaseManager.BOOKS_ISBN + " = '" + listISBN.get(i) + "'");
        }
    }

    public int generateOID() throws SQLException {
        int oID;
        Random random = new Random();
        do {
            oID = random.nextInt(99999999);
            ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_OID
                    + " FROM " + DatabaseManager.TABLE_ORDERS + " WHERE " + DatabaseManager.ORDERS_OID + " = '" + oID + "'");
            if (!resultSet.next())
                break;

        } while (true);
        return oID;
    }

    public String generateUID() throws SQLException {
        StringBuilder uID = new StringBuilder("U");
        Random random = new Random();
        do {
            uID.append(random.nextInt(999));
            uID.append(Character.toString((char) 64 + random.nextInt(26)));
            ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                    + " FROM " + DatabaseManager.TABLE_CUSTOMERS
                    + " WHERE " + DatabaseManager.CUSTOMERS_UID + " = '" + uID + "'");
            if (!resultSet.next())
                break;
        } while (true);
        return uID.toString();
    }

    public void generateCustomer(String uID, String name, String address) throws SQLException {
        PreparedStatement preparedStatement = databaseManager.getDatabase().prepareStatement("INSERT INTO " + DatabaseManager.TABLE_CUSTOMERS + " VALUES (?, ?, ?)");
        preparedStatement.setString(1, uID);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, address);
        preparedStatement.executeUpdate();
        System.out.println("Account created successfully");
        customerRecords++;
    }

    public void placeOrder(List<String> listISBN) throws SQLException {
        int oID = generateOID();
        System.out.println("Please enter your UID: ");
        String uID = scanner.nextLine();
        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                + " FROM " + DatabaseManager.TABLE_CUSTOMERS
                + " WHERE " + DatabaseManager.CUSTOMERS_UID + " = '" + uID + "'");
        if (resultSet.next()) {
            if (resultSet.getString(DatabaseManager.CUSTOMERS_UID).equals(uID))
                uID = resultSet.getString(DatabaseManager.CUSTOMERS_UID);
        } else {
            System.out.println("UID does not exist, creating a new account...");
            System.out.println("Please enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Please enter your address: ");
            String address = scanner.nextLine();
            generateCustomer(uID, name, address);
        }
        StringBuilder isbns = new StringBuilder("\"");
        for (String isbn : listISBN) {
            isbns.append(isbn).append(",");
        }
        isbns.replace(isbns.lastIndexOf(","), isbns.length(), "\"");

        PreparedStatement preparedStatement = databaseManager.getDatabase().prepareStatement("INSERT INTO "
                + DatabaseManager.TABLE_ORDERS + " VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, Integer.toString(oID));
        preparedStatement.setString(2, uID);
        preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        preparedStatement.setString(4, isbns.toString());
        preparedStatement.setInt(5, listISBN.size());
        preparedStatement.setString(6, "ordered");
        preparedStatement.executeUpdate();
        orderRecords++;
    }

    public void runOption2_2() throws SQLException {
        List<String> listISBN = new ArrayList<>();
        List<Integer> listStock = new ArrayList<>();
        do {
            boolean flag;
            System.out.println("Please enter the number of book to be ordered: ");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input <= 0) {
                    System.out.println("Invalid input.");
                    scanner.nextLine();
                    continue;
                }
                flag = true;
                for (int i = 0; i < input; i++) {
                    System.out.println("Please enter the ISBN of the book (" + (i + 1) + "/" + input + "): ");
                    String isbn = scanner.nextLine();
                    listISBN.add(isbn);
                    if (!checkStock(isbn, listStock)) {
                        System.out.println("Sorry, this book is out of stock.");
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("Sorry, the order is failed because some book is out of stock.");

                } else {
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

    public void printOrderInfo(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String orderOID = resultSet.getString(DatabaseManager.ORDERS_OID);
            String orderISBN = resultSet.getString(DatabaseManager.ORDERS_ISBN);
            int orderQuantity = resultSet.getInt(DatabaseManager.ORDERS_QUANTITY);
            String shippingStatus = resultSet.getString(DatabaseManager.ORDERS_SHIPPING_STATUS);
            String orderDate = resultSet.getString(DatabaseManager.ORDERS_DATE);
            System.out.println("------------- Order History -------------");
            System.out.println(DatabaseManager.ORDERS_OID + ": " + orderOID);
            System.out.println(DatabaseManager.ORDERS_ISBN + ": " + orderISBN);
            System.out.println(DatabaseManager.ORDERS_QUANTITY + ": " + orderQuantity);
            System.out.println(DatabaseManager.ORDERS_SHIPPING_STATUS + ": " + shippingStatus);
            System.out.println(DatabaseManager.ORDERS_DATE + ": " + orderDate);
            System.out.println("------------------------------------------");
        }
    }

    public void runOption2_3() throws SQLException {
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();

        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.CUSTOMERS_UID
                + " FROM " + DatabaseManager.TABLE_CUSTOMERS + " WHERE " + DatabaseManager.CUSTOMERS_NAME + " = '" + name + "'"); // select all orders with this customer
        if (!resultSet.next()) {
            System.out.println("Account does not exist, creating a new one..");
            System.out.println("Please enter your address: ");
            String address = scanner.nextLine();
            generateCustomer(generateUID(), name, address);
            return;
        }
        String uID = resultSet.getString(DatabaseManager.CUSTOMERS_UID);

        resultSet = databaseManager.queryDatabase("SELECT * FROM " + DatabaseManager.TABLE_ORDERS
                + " WHERE " +  DatabaseManager.ORDERS_UID + " = '" + uID + "'"); // select all orders with this customer

        printOrderInfo(resultSet);
    }

    public void printOption2() {
        System.out.println("===== Customer Operation =====");
        System.out.println(" + System Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println("------------------------------------------");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public void runOption3_1() throws SQLException {
        System.out.println("Please enter the order ID to be updated: ");
        String orderID = scanner.next();

        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_SHIPPING_STATUS
                + " FROM " + DatabaseManager.TABLE_ORDERS + " WHERE " + DatabaseManager.ORDERS_OID + " = '" + orderID + "'"); // Get the shipping status first
        if(!resultSet.next()){
            System.out.println("This order does not exist.");
            return;
        }

        String shippingStatus = resultSet.getString(DatabaseManager.ORDERS_SHIPPING_STATUS);
        if (shippingStatus.contentEquals("shipped") || shippingStatus.contentEquals("received")) {
            System.out.println("Update failed! This order is already shipped.");
            return;
        }

        databaseManager.updateDatabase("UPDATE " + DatabaseManager.TABLE_ORDERS + " SET "
                + DatabaseManager.ORDERS_SHIPPING_STATUS + " = 'shipped' WHERE "
                + DatabaseManager.ORDERS_OID + " = '" + orderID + "'"); // Update the shipping status of the order (from ordered to shipped)
        System.out.println("Update success! This order is shipped.");
    }

    public void runOption3_2() throws SQLException {
        System.out.println("Please enter the shipping status: ");
        String shippingStatus = scanner.nextLine();
        ResultSet resultSet = databaseManager.queryDatabase("SELECT " + DatabaseManager.ORDERS_OID + ", "
                + DatabaseManager.CUSTOMERS_UID + ", " + DatabaseManager.ORDERS_DATE + ", "
                + DatabaseManager.ORDERS_ISBN + ", " + DatabaseManager.ORDERS_QUANTITY
                + "FROM" + DatabaseManager.TABLE_ORDERS
                + " WHERE " + DatabaseManager.ORDERS_SHIPPING_STATUS + " = '" + shippingStatus + "'"); // select all orders with shippingStatus

        printOrderInfo(resultSet);
    }

    public void runOption3_3() throws SQLException {
        try {
            System.out.println("Enter number of N: ");
            int input = scanner.nextInt();

            // TODO: SQL Query
            ResultSet resultSet = databaseManager.queryDatabase("SELECT" + DatabaseManager.ORDERS_ISBN +
                    " FROM " + DatabaseManager.TABLE_ORDERS); // use count ISBN in order? Then get the first N ISBN
            ArrayList<String> isbnList = new ArrayList<>();
            while (resultSet.next()) {
                isbnList.add(resultSet.getString(DatabaseManager.BOOKS_ISBN));
            }
            ArrayList<String> recordISBN = new ArrayList<>();
            for (String isbn : isbnList) {
                // TODO: SQL Query
                isbn = isbn.replace("\"", "");
                String[] sep_ISBN = isbn.split(",");
                //store separated book ISBN into a list
                recordISBN.addAll(Arrays.asList(sep_ISBN));

                //store book ISBNs as keys, corresponding occurrences as values
                Map<String, Long> distinctISBN = recordISBN.stream().collect(
                        Collectors.groupingBy(Function.identity(), Collectors.counting()));

                //sort the map in desc order according to occurrences
                Map<String, Long> DESC_ISBN = new LinkedHashMap<>();
                distinctISBN.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()).forEachOrdered(e -> DESC_ISBN.put(e.getKey(), e.getValue()));

                String sortedISBN[] = DESC_ISBN.keySet().toArray(new String[0]);
                String[] N_P_ISBN= new String[sortedISBN.length];

//                for (int i = sortedISBN.length -1; i );

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
