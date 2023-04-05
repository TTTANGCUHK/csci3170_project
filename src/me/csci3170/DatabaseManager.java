package me.csci3170;

import me.csci3170.model.Book;
import me.csci3170.model.Customer;
import me.csci3170.model.Order;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.List;

public class DatabaseManager {

//    String USERNAME = "csci3170", PASSWORD = "testfor3170";
    String USERNAME = "root", PASSWORD = "csci3170" , DB_URL = "jdbc:mysql://localhost:3306/csci3170";

    public static String TABLE_BOOKS = "Books", TABLE_ORDERS = "Orders", TABLE_CUSTOMERS = "Customers";
    public static String BOOKS_ISBN = "ISBN", BOOKS_TITLE = "Title", BOOKS_AUTHORS = "Authors"
            , BOOKS_PRICE = "Price", BOOKS_STOCK = "Stock";
    public static String ORDERS_OID = "OID", ORDERS_UID = "UID", ORDERS_DATE = "Order_Date"
            , ORDERS_ISBN = "Order_ISBN", ORDERS_QUANTITY = "Order_Quantity", ORDERS_SHIPPING_STATUS = "Shipping_Status";
    public static String CUSTOMERS_UID = "UID", CUSTOMERS_NAME = "Name", CUSTOMERS_ADDRESS = "Address";

    private Connection database;
    private Statement statement;
    private PreparedStatement preparedStatement;

    DatabaseManager() {

    }

    public Connection getDatabase() {
        return database;
    }

    public <T> void insertDatabase(List<T> tList, Class<T> tClass) throws SQLException {
        int i;
        if (tClass.isAssignableFrom(Book.class)) {
            i = 0;
            System.out.println("Uploading Book Records");
            preparedStatement = database.prepareStatement("INSERT INTO " + TABLE_BOOKS + " VALUES (?, ?, ?, ?, ?)");
            for (T item : tList) {
                if (item instanceof Book book) {
                    i++;
                    System.out.println("Uploading " + i + "/" + tList.size() + " Book Records");
                    preparedStatement.setString(1, book.getIsbn());
                    preparedStatement.setString(2, book.getTitle());
                    preparedStatement.setString(3, book.getAuthors());
                    preparedStatement.setDouble(4, book.getPrice());
                    preparedStatement.setInt(5, book.getStock());
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Upload Book Records Finished, inserted " + i + " records");
            Main.bookRecords = i;
        } else if (tClass.isAssignableFrom(Order.class)) {
            i = 0;
            System.out.println("Uploading Order Records");
            preparedStatement = database.prepareStatement("INSERT INTO " + TABLE_ORDERS + " VALUES (?, ?, ?, ?, ?, ?)");
            for (T item : tList) {
                if (item instanceof Order order) {
                    i++;
                    System.out.println("Uploading " + i + "/" + tList.size() + " Order Records");
                    preparedStatement.setString(1, order.getOid());
                    preparedStatement.setString(2, order.getUid());
                    preparedStatement.setString(3, order.getDate());
                    preparedStatement.setString(4, order.getIsbnList());
                    preparedStatement.setInt(5, order.getQuantity());
                    preparedStatement.setString(6, order.getShippingState());
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Upload Order Records Finished, inserted " + i + " records");
            Main.orderRecords = i;
        } else if (tClass.isAssignableFrom(Customer.class)) {
            i = 0;
            System.out.println("Uploading Customer Records");
            preparedStatement = database.prepareStatement("INSERT INTO " + TABLE_CUSTOMERS + " VALUES (?, ?, ?)");
            for (T item : tList) {
                if (item instanceof Customer customer) {
                    i++;
                    System.out.println("Uploading " + i + "/" + tList.size() + " Customer Records");
                    preparedStatement.setString(1, customer.getUid());
                    preparedStatement.setString(2, customer.getName());
                    preparedStatement.setString(3, customer.getAddress());
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Upload Customer Records Finished, inserted " + i + " records");
            Main.customerRecords = i;
        }
        System.out.println("Uploaded records to database successfully");
        preparedStatement.close();
    }


    public void updateDatabase(String sqlQuery) throws SQLException {
        if (getDatabase() == null){
            System.out.println("getDatabase is null");
            return;
        }
        statement = getDatabase().createStatement();
        statement.executeUpdate(sqlQuery);

    }

    // queryDatabase("SELECT ISBN FROM Book")
    // ISBN:
    public ResultSet queryDatabase(String sqlQuery) throws SQLException {
        if (getDatabase() == null)
            return null;
        preparedStatement = getDatabase().prepareStatement(sqlQuery);

        return preparedStatement.executeQuery();

//        return preparedStatement.executeQuery();
//        statement = getDatabase().createStatement();
//        return statement.executeQuery(sqlQuery);
    }

    public void closeStatement() throws SQLException {
        if (statement != null && !statement.isClosed())
            statement.close();
    }

    public void startConnection() throws SQLException {
        try {
            // Register JDBC driver
            // Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to database...");
            database = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);



            // Clean-up environment

        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }


////        database = DriverManager.getConnection("jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/" + USERNAME + "?autoRe\n" +
////                "connect=true&useSSL=false", USERNAME, PASSWORD);

    }

    public void closeConnection() throws SQLException {
        if (getDatabase() == null)
            return;
        statement.close();
        database.close();
    }

}
