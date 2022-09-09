package com.example.coingame;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import resources.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Submenu extends Menu {

    private TextField baseUrl;
    private TextField apiKeyValue;
    private Button addButton;
    private Button closeButton;
    private final Menu menu;

    private final Stage mainStage;

    Submenu(String name, Stage mainStage){
        this.menu = new Menu(name);
        this.mainStage = mainStage;
        addListener();
    }

    private void addListener() {
        menu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Stage stage = new Stage();
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(10, 10, 10, 10));
                vBox.setSpacing(10);
                Scene scene = new Scene(vBox, 300, 300);

                Label baseUrlLabel = new Label("URL");
                baseUrl = new TextField();
                baseUrl.setPromptText("URL");


                Label baseApiKeyValueLabel = new Label("Api Key");
                apiKeyValue = new TextField();
                apiKeyValue.setPromptText("API Key Value");

                addButton = new Button("Save data");
                addButton.setOnAction(e -> addButtonClicked(baseUrl, apiKeyValue));

                closeButton = new Button("Close");
                closeButton.setOnAction(e -> closeButtonClicked(stage));

                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setPadding(new Insets(10, 10, 10, 10));
                hBox.getChildren().addAll(addButton, closeButton);

                vBox.getChildren().addAll(baseUrlLabel, baseUrl, baseApiKeyValueLabel, apiKeyValue, hBox);
                stage.setTitle("Add API credentials.");
                stage.setScene(scene);
                // need to prevent close application and this popup stay open
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(mainStage);
                stage.requestFocus();
//                stage.showAndWait();
                stage.show();
            }
        });
    }

    private void closeButtonClicked(Stage stage) {
        stage.close();
    }

    private void addButtonClicked(TextField baseUrl, TextField apiKeyValue) {
        if (isValidateInputDataFromFieldsFail(baseUrl, apiKeyValue)) {
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.CREDENTIALS_NOT_SAVED, ButtonType.OK);
            return;
        }

        try {
            saveDataToFile(baseUrl.getText(), apiKeyValue.getText());
            ExceptionMessages.showAlertWindow(Alert.AlertType.CONFIRMATION, ExceptionMessages.CREDENTIALS_SAVED_SUCCESSFUL, ButtonType.OK);
        } catch (IOException e) {
            throw new RuntimeException("Can not save credentials data to file.");
        }
    }

    private void saveDataToFile(String baseUrl, String apiKeyValue) throws IOException {

        // make reserve copy of file before clear data
        // Try to write data to main file

        makeReserveCopyOfData(Constants.CREDENTIALS_PATH_STRING, Constants.CREDENTIALS_PATH_STRING_COPY);

        Credentials c = new Credentials(baseUrl, apiKeyValue);
        ArrayList<Credentials> cArray = new ArrayList<>();
        cArray.add(c);

        try {
            new ObjectMapper().writeValue(new File(Constants.CREDENTIALS_PATH_STRING), cArray);
            clearDataFile(Constants.CREDENTIALS_PATH_STRING_COPY);
        } catch (IOException e){
            // If write new data is not possible try to revert data from reserve copy
            //makeReserveCopyOfData(copyPath, strPath);
            makeReserveCopyOfData(Constants.CREDENTIALS_PATH_STRING_COPY, Constants.CREDENTIALS_PATH_STRING);
            throw new FileNotFoundException("Can not save data to file " + Constants.CREDENTIALS_PATH_STRING + "!");
        }

    }

    private void makeReserveCopyOfData(String source, String destination) throws IOException {
        try{
            Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
            // if copy is successfully created
            // clear data from main file
            clearDataFile(source);
        } catch (IOException ex) {
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.ERROR_COPY_FILE, ButtonType.OK);
            throw new IOException("Can not save data to file " + source + "!");
        }
    }

    private void clearDataFile(String strPath) throws IOException {
        FileWriter fw = new FileWriter(strPath, false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
    }

    private boolean isValidateInputDataFromFieldsFail(TextField baseUrl, TextField apiKeyValue) {

        boolean isDataValid = true;

        if (baseUrl.getText().trim().isEmpty()) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.NAME_MUST_BE_NOT_EMPTY, ButtonType.OK);
        }

        if (apiKeyValue.getText().trim().isEmpty()) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.NAME_MUST_BE_NOT_EMPTY, ButtonType.OK);
        }

        return !isDataValid;
    }

    public Menu getMenu(){
        return this.menu;
    }
}
