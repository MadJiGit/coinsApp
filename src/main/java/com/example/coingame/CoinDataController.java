package com.example.coingame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Objects;

public class CoinDataController {

    private static CoinDataController coinData = null;
    private final ArrayList<Coin> apiCoins;
    private final ArrayList<Coin> myCoins;
    private final ArrayList<String> assetsIdList;
    private final ObservableList<Coin> observableList;

    private CoinDataController() {
        apiCoins = new ArrayList<>();
        myCoins = new ArrayList<>();
        observableList = FXCollections.observableArrayList();
        assetsIdList = new ArrayList<>();
    }

    public static CoinDataController getInstance() {
        if (coinData == null) {
            coinData = new CoinDataController();
        }

        return coinData;
    }

    public void addCoin(ArrayList<Coin> destination, Coin c) {
        destination.add(c);
    }

    public void addList(ArrayList<Coin> destination, Coin[] source) {
        for (Coin c : source) {
            addCoin(destination, c);
        }
    }

    public ArrayList<String> getAssetsIdList() {
        return this.assetsIdList;
    }

    public void setAssetsIdList() {
        for (Coin c : apiCoins) {
            assetsIdList.add(c.assetId);
        }
    }

    public int getApiCoinsCount() {
        return apiCoins.size();
    }

    public int getMyCoinsCount() {
        return myCoins.size();
    }

    public ArrayList<Coin> getApiCoinsList() {
        return this.apiCoins;
    }

    public ArrayList<Coin> getMyCoinsList() {
        return this.myCoins;
    }

    public ObservableList<Coin> getObservableList() {
        return this.observableList;
    }

    public void updateDataInMyCoinList() {
        for (Coin c : myCoins) {
            for (Coin cc : apiCoins) {
                if (Objects.equals(c.assetId, cc.assetId)) {
                    c.setCurrentPriceUsd(cc.getCurrentPriceUsd());
                }
            }
        }
    }


    public void prepareObservableList() {
        observableList.addAll(myCoins);
    }

    public Coin findCoinWithId(long id) {
        for (Coin c : myCoins) {
            if (c.getOrderId() == id) {
                return c;
            }
        }

        ExceptionMessages.showAlertWindow(Alert.AlertType.WARNING, ExceptionMessages.NO_COIN_WITH_ID, ButtonType.OK);

        return null;
    }

    public void setObservableList() {
        int counter = 0;

        for (Coin apiCoin : apiCoins) {
            String tempId = apiCoin.assetId;
            for (Coin myCoin : myCoins) {
                if (Objects.equals(tempId, myCoin.assetId)) {
                    myCoin.setCurrentPriceUsd(apiCoin.getCurrentPriceUsd());
                    if (myCoin.purchaseDateAndTime == null) {
                        myCoin.purchaseDateAndTime = "";
                    }
                    if (myCoin.purchasePriceUsd == null) {
                        myCoin.purchasePriceUsd = 0.0;
                    }
                    if (myCoin.volume == null) {
                        myCoin.volume = 0.0;
                    }
                    observableList.add(myCoin);
                    counter++;
                }
                if (counter == coinData.getMyCoinsCount()) {
                    coinData.deleteArray(coinData.getApiCoinsList());
                    return;
                }
            }
        }
    }

    public boolean deleteCoin(long coinId) {
        return myCoins.removeIf(c -> c.getOrderId() == coinId);
    }

    public void deleteArray(ArrayList<Coin> coins) {
        if (new String(String.valueOf(coins)).equals("apiCoins")) {
            apiCoins.clear();
        }
    }
}
