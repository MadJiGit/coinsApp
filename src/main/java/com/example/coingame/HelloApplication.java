package com.example.coingame;

import javafx.application.Application;
import javafx.stage.Stage;
import resources.API;
import resources.DB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    Stage window;
    MainView mainView;
    CoinDataController coinData = CoinDataController.getInstance();
    DB db;
    API api;

    @Override
    public void start(Stage stage) throws IOException, Exception, RuntimeException {

        loadDataFromDB();
        // fill data arrays
        coinData.setAssetsIdList();

        // for tests
        ArrayList<String> c = coinData.getAssetsIdList();

        // get data from api with my coins ids
        loadDataFromApi(coinData.getAssetsIdList());

        // update price data from apiCoinList into myCoinList data
        coinData.updateDataInMyCoinList();

        // fill observableList for table view
        coinData.prepareObservableList();

        // prepare table view
        mainView = new MainView(stage, db);
    }

    private void loadDataFromApi(ArrayList<String> assetsIdList) throws Exception {
        // Load api data but not ready yet TODO
        api = new API();
        Callback callbackApi = new CallbackImpl();
        api.finishLoadDataApi(callbackApi);
        api.makeApiConnectionForAssetsIds(assetsIdList);
    }

    private void loadDataFromDB() throws FileNotFoundException {
        // Read file with coins
        db = new DB();
        Callback callbackDb = new CallbackImpl();
        db.finishLoadDataDB(callbackDb);
        db.makeDBConnection();
    }
    public static void main(String[] args) {
        launch();
    }
}