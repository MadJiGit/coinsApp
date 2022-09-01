package com.example.coingame;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {

    @JsonProperty("base_url")
    public String baseUrl;

    @JsonProperty("api_key_value")
    public String apiKeyValue;

    private String QUESTION_MARK = "?";
    private String AND_MARK = "&";
    private String ASSETS = "assets?";

    private String API_KEY_EQUAL = "apikey=";
    private String FILTER_ID_ASSETS = "filter_asset_id=";

    public Credentials(){}

    public Credentials(String url, String apiKeyValue){
        this.baseUrl = url;
        this.apiKeyValue = apiKeyValue;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKeyValue() {
        return apiKeyValue;
    }

    public void setApiKeyValue(String apiKeyValue) {
        this.apiKeyValue = apiKeyValue;
    }

    public String getQuestionMark() {
        return QUESTION_MARK;
    }

    public String getApiKeyEqual() { return API_KEY_EQUAL; }

    public String getAndMark() {
        return AND_MARK;
    }

    public String getAssets() {
        return ASSETS;
    }

    public String getFilteredIdAssets() {
        return FILTER_ID_ASSETS;
    }
}
