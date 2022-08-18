package com.example.coingame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    public Label goodBayText;
    @FXML
    public Label welcomeRightText;
    @FXML
    public Label goodBayRightText;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void onGoodBayButtonClick() {
        goodBayText.setText("Welcome to smth!");
    }

    public void setWelcomeText(String new_text) {
        this.welcomeText.setText(new_text);
    }
}