package me.csci3170;

import me.csci3170.model.Book;
import me.csci3170.model.Customer;
import me.csci3170.model.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModelInitializer {

    ModelInitializer() {

    }

    // Function to load Customers.csv and create the object
    public static List<Customer> getCustomer() {
        List<Customer> customers = new ArrayList<>();
        Path path = Paths.get("Customers.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                Customer customer = Customer.createCustomer(csvSplit(line));
                customers.add(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded customer records successfully");
        return customers;
    }

    // Function to load Orders.csv and create the object
    public static List<Order> getOrder() {
        List<Order> orders = new ArrayList<>();
        Path path = Paths.get("Orders.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                Order order = Order.createOrder(csvSplit(line));
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded order records successfully");
        return orders;
    }

    // Function to load Books.csv and create the object
    public static List<Book> getBook() {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get("Books.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                Book book = Book.createBook(csvSplit(line));
                books.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded book records successfully");
        return books;
    }

    // Function to split only by column to avoid over splitting
    private static String[] csvSplit(String string) {
        String[] oldString = string.split(",");
        boolean flag = true;
        int start = 0, end = 0;
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < oldString.length; i++) {
            if (!oldString[i].contains("\""))
                continue;
            if (flag) {
                flag = false;
                start = i;
            } else {
                flag = true;
                end = i;
            }
        }

        for (int i = start; i <= end; i++) {
            if (i == end)
                longString.append(oldString[i].replace("\"", ""));
            else
                longString.append(oldString[i].replace("\"", "")).append(",");
        }

        String[] strings = new String[oldString.length - end + start];
        int index = 0;
        for (int i = 0; i < strings.length; i++) {
            if (index == start) {
                strings[i] = longString.toString();
                index = end + 1;
            }
            else
                strings[i] = oldString[index++];
        }

        return strings;
    }


}
