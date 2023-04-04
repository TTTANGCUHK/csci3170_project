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

    private Connection database;
    private Statement statement;
    private PreparedStatement preparedStatement;

    DatabaseManager() {

    }

    public Connection getDatabase() {
        return database;
    }

    public <T> void insertDatabase(List<T> tList, Class<T> tClass) throws SQLException {
        if (tClass.isAssignableFrom(Book.class)) {
            preparedStatement = database.prepareStatement("INSERT INTO Book VALUES (?, ?)");
            for (T item : tList) {
                if (item instanceof Book book) {
                    preparedStatement.setString(1, book.getIsbn());
                    preparedStatement.setString(2, book.getTitle());
                    preparedStatement.setArray(3, database.createArrayOf("String", book.getAuthors().toArray()));
                    preparedStatement.setDouble(4, book.getPrice());
                    preparedStatement.setInt(5, book.getStock());
                    preparedStatement.executeUpdate();
                }
            }
        } else if (tClass.isAssignableFrom(Order.class)) {
            preparedStatement = database.prepareStatement("INSERT INTO Order VALUES (?, ?)");
            for (T item : tList) {
                if (item instanceof Order order) {
                    preparedStatement.setString(1, order.getOid());
                    preparedStatement.setString(2, order.getUid());
                    preparedStatement.setString(3, order.getDate());
                    preparedStatement.setArray(4, database.createArrayOf("String", order.getIsbnList().toArray()));
                    // String[] vs List<String>
                    preparedStatement.setInt(5, order.getQuantity());
                    preparedStatement.setString(6, order.getShippingState());
                    preparedStatement.executeUpdate();
                }
            }
        } else if (tClass.isAssignableFrom(Customer.class)) {
            preparedStatement = database.prepareStatement("INSERT INTO Customer VALUES (?, ?)");
            for (T item : tList) {
                if (item instanceof Customer customer) {
                    preparedStatement.setString(1, customer.getUid());
                    preparedStatement.setString(2, customer.getName());
                    preparedStatement.setString(3, customer.getAddress());
                    preparedStatement.executeUpdate();
                }
            }
        }
        preparedStatement.close();
    }

    // updateDatabase("INSERT

    public void updateDatabase(String sqlQuery) throws SQLException {
        System.out.println("running update data base");
        if (getDatabase() == null){
            System.out.println("get data base is null");
            return;}
        statement = getDatabase().createStatement();
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    // queryDatabase("SELECT ISBN FROM Book")
    // ISBN:
    public ResultSet queryDatabase(String sqlQuery) throws SQLException {
        if (getDatabase() == null)
            return null;
        statement = getDatabase().createStatement();
        return statement.executeQuery(sqlQuery);
    }

    public void closeStatement() throws SQLException {
        if (statement != null && !statement.isClosed())
            statement.close();
    }

    public void startConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

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
