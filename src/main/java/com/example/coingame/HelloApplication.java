package com.example.coingame;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.API;
import resources.DB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    Stage window;
    TableView<Coin> table;
    CoinDataController coinData = CoinDataController.getInstance();
    TableColumn<Coin, String> nameColumn, assetIdColumn, priceUsdColumn, purchasePriceUsd, purchaseDateAndTime, volume;
    TextField nameInput, assetIdInput, volumeInput, purchasePriceInput;
    Button addButton, deleteButton, editButton;
    HBox hBox;
    DB db;
    API api;

    @Override
    public void start(Stage stage) throws IOException, Exception, RuntimeException {

        loadDataFromDB();
        // fill data arrays
        coinData.setAssetsIdList();

        // get data from api with my coins ids
        loadDataFromApi(coinData.getAssetsIdList());

        // update price data from apiCoinList into myCoinList data
        coinData.updateDataInMyCoinList();

        // fill observableList for table view
        coinData.prepareObservableList();

        // prepare table view
        drawTable(stage);

        // fill table with data
        fillTable();
    }

    private void loadDataFromApi(ArrayList<String> assetsIdList) throws Exception {

        // Load api data but not ready yet TODO
        api = new API();
        Callback callbackApi = new CallbackImpl();
        api.finishLoadDataApi(callbackApi);
        api.makeApiConnectionAssetsIds(assetsIdList);
    }

    private void loadDataFromDB() throws FileNotFoundException {
        // Read file with coins
        db = new DB();
        Callback callbackDb = new CallbackImpl();
        db.finishLoadDataDB(callbackDb);
        db.makeDBConnection();
    }

    private void fillTable() {
        table = new TableView<>();
        table.setItems(coinData.getObservableList());
        table.getColumns().addAll(nameColumn, assetIdColumn, volume, purchasePriceUsd, priceUsdColumn, purchaseDateAndTime);

        // Event for mouse click, if select row make edit available
        // if click on empty row -> make add available
        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {

                ObservableList<Coin> selected;
                selected = table.getSelectionModel().getSelectedItems();
                if (!selected.isEmpty()) {
                    onEdit(selected);
                }
            } else {
                onAdd();
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    private void onAdd() {
        clearFields();
        // convert Add button to Add and
        // attach add method
        addButton.setText("Add");
        addButton.setOnAction(e -> addButtonClicked(nameInput, assetIdInput, volumeInput, purchasePriceInput));
    }

    private void onEdit(ObservableList<Coin> selected) {

        // Get Coin from selected row
        Coin cc = selected.get(0);
        nameInput.setText(cc.getName());
        assetIdInput.setText(cc.getAssetId());
        volumeInput.setText(String.valueOf(cc.getVolumePurchase()));
        purchasePriceInput.setText(String.valueOf(cc.getPurchasePriceUsd()));

        // convert Add button to edit and
        // attach edit method
        addButton.setText("Edit");
        addButton.setOnAction(e -> editButtonClicked(cc.getOrderId(), nameInput, assetIdInput, volumeInput, purchasePriceInput));
    }

    private void deleteButtonClicked() {
        ObservableList<Coin> selected;
        selected = table.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            return;
        }

        Alert a = new Alert(Alert.AlertType.WARNING, ExceptionMessages.DO_YOU_WANT_TO_DELETE_DATA, ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if(a.getResult() == ButtonType.NO){
            return;
        }

        Coin cc = selected.get(0);
        if (!coinData.deleteCoin(cc.getOrderId())) {
            ExceptionMessages.showAlertWindow(Alert.AlertType.WARNING, ExceptionMessages.DATA_IS_NOT_DELETED, ButtonType.OK);
            return;
        }

        refreshTable();
        ExceptionMessages.showAlertWindow(Alert.AlertType.INFORMATION, ExceptionMessages.SUCCESSFULLY_DELETE_COIN, ButtonType.OK);

    }

    private void editButtonClicked(long orderId, TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {
        System.out.println("Edit button is clicked");

        if (!validateInputDataFromFields(nameInput, assetIdInput, volumeInput, purchasePriceInput)) {
            return;
        }

        Coin c = coinData.findCoinWithId(orderId);
        c.setAssetId(assetIdInput.getText());
        c.setName(nameInput.getText());
        c.setPurchasePriceUsd(Double.valueOf(purchasePriceInput.getText()));
        c.setVolume(Double.valueOf(volumeInput.getText()));

        refreshTable();
        onAdd();
    }

    private void clearFields() {
        nameInput.clear();
        assetIdInput.clear();
        volumeInput.clear();
        purchasePriceInput.clear();
    }

    private boolean validateInputDataFromFields(TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {
        boolean isDataValid = true;
        if (!validateDouble(volumeInput)) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.VOLUME_DATA_MUST_BE_NUMBER, ButtonType.OK);
        }
        if (!validateDouble(purchasePriceInput)) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.PRICE_DATA_MUST_BE_DOUBLE, ButtonType.OK);
        }
        if (nameInput.getText().trim().isEmpty()) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.NAME_MUST_BE_NOT_EMPTY, ButtonType.OK);
        }
        if (assetIdInput.getText().trim().isEmpty()) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.ASSET_MUST_BE_NOT_EMPTY, ButtonType.OK);
        }

        return isDataValid;
    }

    private void addButtonClicked(TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {

        // validate data input
        if (!validateInputDataFromFields(nameInput, assetIdInput, volumeInput, purchasePriceInput)) {
            return;
        }

        Coin c = new Coin(
                nameInput.getText(),
                assetIdInput.getText(),
                Double.valueOf(purchasePriceInput.getText()),
                Double.valueOf(volumeInput.getText())
        );

        // add new entry to data table
        coinData.addCoin(coinData.getMyCoinsList(), c);

        // refresh view
        refreshTable();

        // Clear input fields
        clearFields();

    }

    private boolean validateDouble(TextField data) {

        try {
            Double.parseDouble(data.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void refreshTable() {

        // clear table data
        table.getItems().clear();

        // prepare updated data to show
        coinData.prepareObservableList();

        // prepare table
        fillTable();
    }

    private void drawTable(Stage stage) {
        window = stage;
        window.setTitle("COINS");

        // Logic for close button
        // Prevent close program before save data to file
        window.setOnCloseRequest(e -> {
            e.consume();
            try {
                closeProgram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Table view
        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        assetIdColumn = new TableColumn<>("ID");
        assetIdColumn.setMinWidth(100);
        assetIdColumn.setCellValueFactory(new PropertyValueFactory<>("assetId"));

        volume = new TableColumn<>("volume");
        volume.setMinWidth(100);
        volume.setCellValueFactory(new PropertyValueFactory<>("volumePurchase"));

        priceUsdColumn = new TableColumn<>("current price USD");
        priceUsdColumn.setMinWidth(100);
        priceUsdColumn.setCellValueFactory(new PropertyValueFactory<>("currentPriceUsd"));

        purchasePriceUsd = new TableColumn<>("purchase price USD");
        purchasePriceUsd.setMinWidth(100);
        purchasePriceUsd.setCellValueFactory(new PropertyValueFactory<>("purchasePriceUsd"));

        purchaseDateAndTime = new TableColumn<>("date purchase");
        purchaseDateAndTime.setMinWidth(100);
        purchaseDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateAndTimePurchase"));


        // END

        // footer fields for adding new data
        nameInput = new TextField();
        nameInput.setPromptText("Name");

        assetIdInput = new TextField();
        assetIdInput.setPromptText("ID");

        volumeInput = new TextField();
        volumeInput.setPromptText("Volume");

        purchasePriceInput = new TextField();
        purchasePriceInput.setPromptText("Price");

        addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked(nameInput, assetIdInput, volumeInput, purchasePriceInput));

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteButtonClicked());


        // END

        hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput, assetIdInput, volumeInput, purchasePriceInput, addButton, deleteButton);

    }

    private void closeProgram() throws IOException {

        Alert alert = new Alert(Alert.AlertType.NONE, ExceptionMessages.DO_YOU_WANT_TO_SAVE_DATA, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        ButtonType result = alert.getResult();
        // When use want to save data
        if (ButtonType.YES.equals(result)) {
            try {
                if (db.saveDataToFile(coinData.getMyCoinsList())) {
                    ExceptionMessages.showAlertWindow(Alert.AlertType.INFORMATION, ExceptionMessages.DATA_SAVE_SUCCESSFULLY, ButtonType.CLOSE);
                    window.close();
                }
            } catch (IOException ex) {
                ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.CAN_NOT_SAVE_DATA_TO_FILE, ButtonType.CLOSE);
                throw new IOException("Can not save data!");
            }
            // When user DO not want to save data
        } else if (ButtonType.NO.equals(result)) {
            window.close();
            // When tap cancel button return to table without change
        } else if (ButtonType.CANCEL.equals(result)) {
            alert.close();
        }
    }

//    private void showAlertWindow(Alert.AlertType error, String msg, ButtonType bType){
//        Alert alert = new Alert(error, msg , bType);
//        alert.showAndWait();
//        ButtonType res = alert.getResult();
//        if(ButtonType.YES.equals(res)){
//            alert.close();
//        }
//    }

    public static void main(String[] args) {

        launch();
    }


}