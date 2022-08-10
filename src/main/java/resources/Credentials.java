package resources;

import java.util.ArrayList;

public class Credentials {
    public static final String BASE_URL = "https://rest.coinapi.io/v1/";
    public static final String API_KEY = "X-CoinAPI-Key";
    public static final String QUESTION_MARK = "?";
    public static final String AND_MARK = "&";
    public static final String ASSETS = "assets?";
    public static final String FILTER_ID_ASSETS = "filter_asset_id=";
    public static final String API_KEY_VALUE = "apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";

    public static String getAssetsFilteredByIds(ArrayList<String> coinsIds) {
        String result = "";
        String temp = String.join(",", coinsIds);

        result = BASE_URL
                + ASSETS
                + FILTER_ID_ASSETS
                + temp
                + AND_MARK
                + API_KEY_VALUE;

        return result;
    }

    public static String getAllAssets() {
        String result = "";

        result = BASE_URL
                + ASSETS
                + API_KEY_VALUE;

        return result;
    }

//    String filterAssets = "https://rest.coinapi.io/v1/assets?filter_asset_id={array}& apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";
//    String allAssets = "https://rest.coinapi.io/v1/assets? apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";


}
