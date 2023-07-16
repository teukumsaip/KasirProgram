module com.example.kasirprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.kasirprogram to javafx.fxml;
    exports com.example.kasirprogram;
}