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
import java.util.Arrays;
import java.util.List;

public class ModelInitializer {

    ModelInitializer() {

    }

    public static List<Customer> getCustomer() {
        List<Customer> customers = new ArrayList<>();
        Path path = Paths.get("Customers.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                //System.out.print(Arrays.toString(csvSplit(line)));
                Customer customer = Customer.createCustomer(csvSplit(line));
                //System.out.print(customer);
                customers.add(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded customer records successfully");
        return customers;
    }

    public static List<Order> getOrder() {
        List<Order> orders = new ArrayList<>();
        Path path = Paths.get("Orders.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                //System.out.print(Arrays.toString(csvSplit(line)));
                Order order = Order.createOrder(csvSplit(line));
                //System.out.print(order);
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded order records successfully");
        return orders;
    }

    public static List<Book> getBook() {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get("Books.csv");

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line == null)
                    break;
                //System.out.print(Arrays.toString(csvSplit(line)));
                Book book = Book.createBook(csvSplit(line));
                /*
                System.out.println(book);
                System.out.println("=====================");
                System.out.println(book.getAuthors());
                for (String author : book.getAuthors()) {
                    System.out.println(author);
                }
                System.out.println("=====================");
                */
                books.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded book records successfully");
        return books;
    }

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
