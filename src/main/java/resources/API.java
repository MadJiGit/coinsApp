package resources;


import com.example.coingame.Callback;
import com.example.coingame.Coin;
import com.example.coingame.CoinDataController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class API {

    CoinDataController coinData = CoinDataController.getInstance();
    private Callback callbackApi;

    public void makeApiConnectionForAssetsIds(ArrayList<String> coinsIds) throws MalformedURLException, FileNotFoundException {

        String result;
        String strURL = Credentials.getAssetsFilteredByIds(coinsIds);

        URL url = new URL(strURL);

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                result = getUrlData(connection.getInputStream());
                Coin[] coins = parseJsonData(result);
                coinData.addList(coinData.getApiCoinsList(), coins);
                if (callbackApi != null) {
                    callbackApi.getDataFromApi();
                }
            }

        } catch (IOException ex) {
            throw new FileNotFoundException("Connection with url " + url + " return with error");
        }
    }

    public void makeApiConnectionWithAllAssets() throws Exception {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String result;
//        URL url = new URL("https://rest.coinapi.io/v1/assets?apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543");
        String strPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\file.json";
        Path path;

        String strURL = Credentials.getAllAssets();

        URL url = new URL(strURL);

        try {
            path = FileSystems.getDefault().getPath(strPath);
            result = readFileFromResources(path);

            Coin[] coins = parseJsonData(result);
            coinData.addList(coinData.getApiCoinsList(), coins);
            if (callbackApi != null) {
                callbackApi.getDataFromApi();
            }


            //URL url = new URL("https://rest.coinapi.io/v1/exchanges?apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543");
/*
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                result = getUrlData(connection.getInputStream());
                Coin[] coins = parseJsonData(result);
                coinData.addList(coinData.getApiCoinsList(), coins);
                if (callbackApi != null) {
                    callbackApi.getDataFromApi();
                }
            }
*/
        } catch (IOException ex) {
            throw new FileNotFoundException("Connection with url " + url + " return with error");
        }
    }

    private static String readFileFromResources(Path path) throws IOException {

        StringBuilder sb = new StringBuilder();

        try {
            final BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String str;
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new IOException("Can not read file with Buff reader " + path + "!");
        }
        return sb.toString();
    }


    private Coin[] parseJsonData(String result) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Coin[] coins;

        return coins = objectMapper.readValue(result, Coin[].class);
    }

    public String getUrlData(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new IOException("Can not read from buffer!");
            }
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    public void finishLoadDataApi(Callback callback) {
        this.callbackApi = callback;
    }

}

