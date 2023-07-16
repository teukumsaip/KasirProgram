package com.example.kasirprogram;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

interface MenuItem {
    String getName();
    double getPrice();
}

class FoodItem implements MenuItem {
    private String name;
    private double price;

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }
}

class DrinkItem implements MenuItem {
    private String name;
    private double price;

    public DrinkItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }
}

interface DatabaseManager {
    void saveOrder(String order);
    List<String> getOrderHistory();
}

class JdbcDatabaseManager implements DatabaseManager {
    private Connection connection;

    public JdbcDatabaseManager(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        createOrderTableIfNotExists();
    }

    private void createOrderTableIfNotExists() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS orders (id INT AUTO_INCREMENT PRIMARY KEY, order_text VARCHAR(255))";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }

    @Override
    public void saveOrder(String order) {
        String insertQuery = "INSERT INTO orders (order_text) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, order);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getOrderHistory() {
        List<String> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders")) {
            while (resultSet.next()) {
                orders.add(resultSet.getString("order_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}

public class Main {
    public static void main(String[] args) {
        launch(KasirApp.class, args);
    }
}
