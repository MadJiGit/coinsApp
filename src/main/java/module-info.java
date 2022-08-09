module com.example.coingame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires json.simple;

    opens com.example.coingame to javafx.fxml;
    exports com.example.coingame;
}