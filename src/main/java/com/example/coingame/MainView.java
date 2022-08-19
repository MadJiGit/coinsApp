package com.example.coingame;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.Constants;
import resources.DB;

import java.io.IOException;

public class MainView {

    Stage mainStage;
    TableColumn<Coin, String> nameColumn, assetIdColumn, priceUsdColumn, purchasePriceUsd, purchaseDateAndTime, volume;
    TextField nameInput, assetIdInput, volumeInput, purchasePriceInput;
    Button addButton, deleteButton, editButton;
    TableView<Coin> table;
    VBox vBox;
    HBox hBox;
    Scene scene;
    Menu mainMenu;
    Menu apiMenu;
    Menu dbMenu;
    MenuBar menuBar;
    BorderPane headerPanel;
    CoinDataController coinData;
    DB db;

    public MainView(Stage mainStage, DB db) {
        this.mainStage = mainStage;
        this.db = db;
        this.coinData = CoinDataController.getInstance();
        this.vBox = new VBox();
        this.hBox = new HBox();
        this.mainMenu = new Menu(Constants.MAIN_MENU_NAME);
        this.apiMenu = new Menu(Constants.API_MENU_NAME);
        this.dbMenu = new Menu(Constants.DB_MENU_NAME);
        menuBar = new MenuBar();
        headerPanel = new BorderPane();
        this.drawTableBody();
        this.drawTableFooter();
        this.generateHBox();
        drawTableHeaderPanel();
        this.fillTable();
    }

    private void drawTableHeaderPanel() {
        mainMenu.getItems().add(apiMenu);
        mainMenu.getItems().add(dbMenu);

        menuBar.getMenus().addAll(mainMenu);
        headerPanel.setTop(menuBar);
    }

    private void generateHBox() {
        this.hBox.setPadding(new Insets(10, 10, 10, 10));
        this.hBox.setSpacing(10);
        this.hBox.getChildren().addAll(
                this.nameInput,
                this.assetIdInput,
                this.volumeInput,
                this.purchasePriceInput,
                this.addButton,
                this.deleteButton
        );
    }

    private void fillTable() {
        this.table = new TableView<>();
        this.table.setItems(this.coinData.getObservableList());
        this.table.getColumns().addAll(
                this.nameColumn,
                this.assetIdColumn,
                this.volume,
                this.purchasePriceUsd,
                this.priceUsdColumn,
                this.purchaseDateAndTime
        );

        // Event for mouse click, if select row make edit available
        // if click on empty row -> make add available
        this.table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {

                ObservableList<Coin> selected;
                selected = this.table.getSelectionModel().getSelectedItems();
                if (!selected.isEmpty()) {
                    this.onEdit(selected);
                }
            } else {
                this.onAdd();
            }
        });

        // Add all parts of view
        this.vBox.getChildren().addAll(this.headerPanel, this.table, this.hBox);

        this.scene = new Scene(this.vBox);
        this.mainStage.setScene(this.scene);
        this.mainStage.show();

    }
    private void drawTableFooter() {
        this.nameInput = new TextField();
        this.nameInput.setPromptText("Name");

        this.assetIdInput = new TextField();
        this.assetIdInput.setPromptText("ID");

        this.volumeInput = new TextField();
        this.volumeInput.setPromptText("Volume");

        this.purchasePriceInput = new TextField();
        this.purchasePriceInput.setPromptText("Price");

        this.addButton = new Button("Add");
        this.addButton.setOnAction(e -> addButtonClicked(nameInput, assetIdInput, volumeInput, purchasePriceInput));

        this.deleteButton = new Button("Delete");
        this.deleteButton.setOnAction(e -> deleteButtonClicked());
    }

    private boolean isValidateDoubleFail(TextField data) {

        try {
            Double.parseDouble(data.getText());
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isValidateInputDataFromFieldsFail(TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {
        boolean isDataValid = true;
        if (isValidateDoubleFail(volumeInput)) {
            isDataValid = false;
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.VOLUME_DATA_MUST_BE_NUMBER, ButtonType.OK);
        }
        if (isValidateDoubleFail(purchasePriceInput)) {
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

        return !isDataValid;
    }

    private void clearFields() {
        nameInput.clear();
        assetIdInput.clear();
        volumeInput.clear();
        purchasePriceInput.clear();
    }

    private void editButtonClicked(long orderId, TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {
        System.out.println("Edit button is clicked");

        if (isValidateInputDataFromFieldsFail(nameInput, assetIdInput, volumeInput, purchasePriceInput)) {
            return;
        }

        Coin c = coinData.findCoinWithId(orderId);
        c.setAssetId(assetIdInput.getText());
        c.setName(nameInput.getText());
        c.setPurchasePriceUsd(Double.valueOf(purchasePriceInput.getText()));
        c.setVolume(Double.valueOf(volumeInput.getText()));

        this.refreshTable();
        this.onAdd();
    }

    private void onAdd() {
        clearFields();
        // convert Add button to Add and
        // attach add method
        this.addButton.setText("Add");
        this.addButton.setOnAction(e -> this.addButtonClicked(
                this.nameInput,
                this.assetIdInput,
                this.volumeInput,
                this.purchasePriceInput));
    }

    private void onEdit(ObservableList<Coin> selected) {

        // Get Coin from selected row
        Coin cc = selected.get(0);
        this.nameInput.setText(cc.getName());
        this.assetIdInput.setText(cc.getAssetId());
        this.volumeInput.setText(String.valueOf(cc.getVolumePurchase()));
        this.purchasePriceInput.setText(String.valueOf(cc.getPurchasePriceUsd()));

        // convert Add button to edit and
        // attach edit method
        this.addButton.setText("Edit");
        this.addButton.setOnAction(e -> editButtonClicked(cc.getOrderId(),
                this.nameInput,
                this.assetIdInput,
                this.volumeInput,
                this.purchasePriceInput));
    }
    private void deleteButtonClicked() {
        ObservableList<Coin> selected;
        selected = this.table.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            return;
        }

        Coin cc = selected.get(0);
        // ASK user for deleting data
        String msg = String.format("Do you want to delete >>  %s  << coin?", cc.getName());
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if(a.getResult() == ButtonType.NO){
            return;
        }

        if (!this.coinData.deleteCoin(cc.getOrderId())) {
            ExceptionMessages.showAlertWindow(Alert.AlertType.WARNING, ExceptionMessages.DATA_IS_NOT_DELETED, ButtonType.OK);
            return;
        }

        this.refreshTable();
        ExceptionMessages.showAlertWindow(Alert.AlertType.INFORMATION, ExceptionMessages.SUCCESSFULLY_DELETE_COIN, ButtonType.OK);

    }
    private void addButtonClicked(TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {

        // validate data input
        if (isValidateInputDataFromFieldsFail(nameInput, assetIdInput, volumeInput, purchasePriceInput)) {
            return;
        }

        Coin c = new Coin(
                nameInput.getText(),
                assetIdInput.getText(),
                Double.valueOf(purchasePriceInput.getText()),
                Double.valueOf(volumeInput.getText())
        );

        // add new entry to data table
        this.coinData.addCoin(this.coinData.getMyCoinsList(), c);

        // refresh view
        this.refreshTable();

        // Clear input fields
        this.clearFields();

    }

    private void drawTableBody(){
        this.mainStage.setTitle(Constants.TABLE_NAME);

        // Logic for close button
        // Prevent close program before save data to file
        this.mainStage.setOnCloseRequest(e -> {
            e.consume();
            try {
                this.closeProgram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Table view
        this.nameColumn = new TableColumn<>(Constants.TABLE_COLUMN_NAME);
        this.nameColumn.setMinWidth(Constants.COLUMNS_WIDTH);
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.assetIdColumn = new TableColumn<>(Constants.TABLE_COLUMN_ID);
        this.assetIdColumn.setMinWidth(Constants.COLUMNS_WIDTH);
        this.assetIdColumn.setCellValueFactory(new PropertyValueFactory<>("assetId"));

        this.volume = new TableColumn<>(Constants.TABLE_COLUMN_VOLUME);
        this.volume.setMinWidth(Constants.COLUMNS_WIDTH);
        this.volume.setCellValueFactory(new PropertyValueFactory<>("volumePurchase"));

        this.priceUsdColumn = new TableColumn<>(Constants.TABLE_COLUMN_CURRENT_PRICE_USD);
        this.priceUsdColumn.setMinWidth(Constants.COLUMNS_WIDTH);
        this.priceUsdColumn.setCellValueFactory(new PropertyValueFactory<>("currentPriceUsd"));

        this.purchasePriceUsd = new TableColumn<>(Constants.TABLE_COLUMN_PURCHASE_PRICE_USD);
        this.purchasePriceUsd.setMinWidth(Constants.COLUMNS_WIDTH);
        this.purchasePriceUsd.setCellValueFactory(new PropertyValueFactory<>("purchasePriceUsd"));

        this.purchaseDateAndTime = new TableColumn<>(Constants.TABLE_COLUMN_DATE_PURCHASE);
        this.purchaseDateAndTime.setMinWidth(Constants.COLUMNS_WIDTH);
        this.purchaseDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateAndTimePurchase"));
    }

    private void closeProgram() throws IOException {

        Alert alert = new Alert(Alert.AlertType.NONE, ExceptionMessages.DO_YOU_WANT_TO_SAVE_DATA, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        ButtonType result = alert.getResult();
        // When use want to save data
        if (ButtonType.YES.equals(result)) {
            try {
                if (this.db.saveDataToFile(coinData.getMyCoinsList())) {
                    ExceptionMessages.showAlertWindow(Alert.AlertType.INFORMATION, ExceptionMessages.DATA_SAVE_SUCCESSFULLY, ButtonType.CLOSE);
                    this.mainStage.close();
                }
            } catch (IOException ex) {
                ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.CAN_NOT_SAVE_DATA_TO_FILE, ButtonType.CLOSE);
                throw new IOException(ExceptionMessages.CAN_NOT_SAVE_DATA_TO_FILE);
            }
            // When user DO not want to save data
        } else if (ButtonType.NO.equals(result)) {
            this.mainStage.close();
            // When tap cancel button return to table without change
        } else if (ButtonType.CANCEL.equals(result)) {
            alert.close();
        }
    }
    private void refreshTable() {

        // clear table data
        this.table.getItems().clear();

        // prepare updated data to show
        this.coinData.prepareObservableList();

        // prepare table
        this.fillTable();
    }
}
