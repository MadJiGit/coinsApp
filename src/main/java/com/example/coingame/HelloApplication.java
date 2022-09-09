package com.example.coingame;

import javafx.application.Application;
import javafx.stage.Stage;
import resources.API;
import resources.DB;

import java.io.IOException;

public class HelloApplication extends Application {

    Stage window;
    MainView mainView;
    CoinDataController coinData = CoinDataController.getInstance();
    DB db;
    API api;

    @Override
    public void start(Stage stage) throws IOException, Exception, RuntimeException {
        db = new DB();

        db.loadDataFromDB();
        // fill data arrays
        coinData.setAssetsIdList();

        // get data from api with my coins ids
        api = new API();

        api.loadDataFromApi();

        // update price data from apiCoinList into myCoinList data
        coinData.updateDataInMyCoinList();

        // fill observableList for table view
        coinData.prepareObservableList();

        // prepare table view
        mainView = new MainView(stage, db);
    }

    public static void main(String[] args) {
        launch();
    }
}
// 0885905653
// 0896645580 Krasio 06 sled 4-5 ot Mitko