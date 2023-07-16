package com.example.kasirprogram;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KasirApp extends Application {
    private List<MenuItem> menuItems;
    private DatabaseManager databaseManager;
    private ListView<String> orderHistoryListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kasir App");

        orderHistoryListView = new ListView<>();

        GridPane gridPane = createMenuGridPane();

        Button orderButton = new Button("Order");
        orderButton.setOnAction(event -> saveOrderToDatabase());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(orderHistoryListView, gridPane, orderButton);

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createMenuGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        for (MenuItem menuItem : menuItems) {
            Label nameLabel = new Label(menuItem.getName());
            Label priceLabel = new Label(String.valueOf(menuItem.getPrice()));
            Button addButton = new Button("Add");
            addButton.setOnAction(event -> addToOrderHistory(menuItem));

            gridPane.add(nameLabel, 0, row);
            gridPane.add(priceLabel, 1, row);
            gridPane.add(addButton, 2, row);

            row++;
        }

        return gridPane;
    }

    private void addToOrderHistory(MenuItem menuItem) {
        String orderText = menuItem.getName() + " - " + menuItem.getPrice() + " Rp";
        orderHistoryListView.getItems().add(orderText);
    }

    private void saveOrderToDatabase() {
        StringBuilder orderText = new StringBuilder("Order Items:\n");
        for (String item : orderHistoryListView.getItems()) {
            orderText.append(item).append("\n");
        }
        databaseManager.saveOrder(orderText.toString());
    }

    @Override
    public void init() {
        // Inisialisasi menuItems
        menuItems = new ArrayList<>();
        menuItems.add(new FoodItem("Nasi Goreng", 15000));
        menuItems.add(new FoodItem("Mie Goreng", 12000));
        menuItems.add(new DrinkItem("Es Teh", 5000));
        menuItems.add(new DrinkItem("Es Jeruk", 6000));

        // Inisialisasi databaseManager
        String dbUrl = "jdbc:mysql://localhost/kasir_db";
        String dbUsername = "root";
        String dbPassword = "";
        try {
            databaseManager = new JdbcDatabaseManager(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
