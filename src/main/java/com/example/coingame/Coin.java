package com.example.coingame;

import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

    // for both JSON objects
    @JsonProperty("name")
    public String name;
    @JsonProperty("asset_id")
    public String assetId;
    @JsonProperty("price_usd")
    public Double currentPriceUsd;

    // for my coins JSON object
    @JsonProperty("purchase_price_usd")
    public Double purchasePriceUsd;
    @JsonProperty("date_and_time_purchase")
    public String purchaseDateAndTime;
    @JsonProperty("volume")
    public Double volume;


    public Coin() {}

    public Coin(String name, String assetId, Double purchasePriceUsd, Double volume) {
        this.setName(name);
        this.setAssetId(assetId);
        this.setPurchasePriceUsd(purchasePriceUsd);
        this.setPurchaseDateAndTime();
        this.setVolume(volume);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null){
            name = "";
        }
        this.name = name;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        if(assetId == null){
            assetId = "";
        }
        this.assetId = assetId;
    }

    public Double getCurrentPriceUsd() {
        return this.currentPriceUsd;
    }

    public void setCurrentPriceUsd(Double currentPriceUsd) {
        if(currentPriceUsd == null){
            currentPriceUsd = 0.0;
        }
        this.currentPriceUsd = currentPriceUsd;
    }

    public Double getPurchasePriceUsd() {
        return this.purchasePriceUsd;
    }

    public void setPurchasePriceUsd(Double purchasePriceUsd) {
        if(purchasePriceUsd == null){
            purchasePriceUsd = 0.0;
        }
        this.purchasePriceUsd = purchasePriceUsd;
    }

    public String getDateAndTimePurchase() {
        return purchaseDateAndTime;
    }

    public void setPurchaseDateAndTime() {
        this.purchaseDateAndTime = DataAndTime.getFormattedDateAndTimeNow();
    }

    public Double getVolumePurchase() {
        return volume;
    }

    public void setVolume(Double volume) {
        if(volume == null){
            volume = 0.0;
        }
        this.volume = volume;
    }
}
