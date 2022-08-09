package com.example.coingame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    Button addButton;
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
        api = new API();
        Callback callbackApi = new CallbackImpl();
        api.finishLoadDataApi(callbackApi);
        api.makeApiConnectionAssetsIds(assetsIdList);
    }

    private void loadDataFromDB() throws FileNotFoundException {
        db = new DB();
        Callback callbackDb = new CallbackImpl();
        db.finishLoadDataDB(callbackDb);
        db.makeDBConnection();
    }

    private void fillTable() {
        table = new TableView<>();
        table.setItems(coinData.getObservableList());
        table.getColumns().addAll(nameColumn, assetIdColumn, purchasePriceUsd, priceUsdColumn, purchaseDateAndTime, volume);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    private void refreshTable() {
        table.getItems().clear();
        //table.refresh();
        fillTable();
    }

    private void drawTable(Stage stage) {
        window = stage;
        window.setTitle("COINS");

        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory( new PropertyValueFactory<>("name"));

        assetIdColumn = new TableColumn<>("ID");
        assetIdColumn.setMinWidth(100);
        assetIdColumn.setCellValueFactory( new PropertyValueFactory<>("assetId"));

        priceUsdColumn = new TableColumn<>("current price USD");
        priceUsdColumn.setMinWidth(100);
        priceUsdColumn.setCellValueFactory( new PropertyValueFactory<>("currentPriceUsd"));

        purchasePriceUsd = new TableColumn<>("purchase price USD");
        purchasePriceUsd.setMinWidth(100);
        purchasePriceUsd.setCellValueFactory( new PropertyValueFactory<>("purchasePriceUsd"));

        purchaseDateAndTime = new TableColumn<>("date purchase");
        purchaseDateAndTime.setMinWidth(100);
        purchaseDateAndTime.setCellValueFactory( new PropertyValueFactory<>("dateAndTimePurchase"));

        volume = new TableColumn<>("volume");
        volume.setMinWidth(100);
        volume.setCellValueFactory( new PropertyValueFactory<>("volumePurchase"));

//        if(db.saveDataToFile(coinData.getApiCoinsList().toArray(new Coin[0]))){
//            System.out.println("Successfully save data to file");
//        }

        nameInput = new TextField();
        nameInput.setPromptText("Name");

        assetIdInput = new TextField();
        assetIdInput.setPromptText("ID");

        volumeInput = new TextField();
        volumeInput.setPromptText("Volume");

        purchasePriceInput = new TextField();
        purchasePriceInput.setPromptText("Price");

        addButton = new Button("Add");
        addButton.setOnAction(e-> addButtonClicked(nameInput, assetIdInput, volumeInput, purchasePriceInput));

        hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput, assetIdInput, volumeInput, purchasePriceInput, addButton);

    }

    private void addButtonClicked(TextField nameInput, TextField assetIdInput, TextField volumeInput, TextField purchasePriceInput) {
        Coin c = new Coin(
                nameInput.getText(),
                assetIdInput.getText(),
                Double.valueOf(purchasePriceInput.getText()),
                Double.valueOf(volumeInput.getText())
                );

        coinData.addCoin(coinData.getMyCoinsList(), c);
        refreshTable();
    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 480, 480);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    public static void main(String[] args) {

        launch();
    }


}