package resources;

import com.example.coingame.Callback;
import com.example.coingame.Coin;
import com.example.coingame.CoinDataController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class DB {
    CoinDataController coinData = CoinDataController.getInstance();
    private Callback callbackDB;
    Path path;
    String strPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\myCoins.json";
    String copyPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\temp_myCoins.json";

    public void makeDBConnection() throws FileNotFoundException {

        try {
            String result;
            path = FileSystems.getDefault().getPath(strPath);
            result = readFileFromResources(path);
            if(result.isEmpty()){
                System.out.println("file is empty");
            } else {
                Coin[] coins = parseJsonData(result);
                coinData.addList(coinData.getMyCoinsList(), coins);
            }
            if(callbackDB != null){
                callbackDB.getDataFromApi();
            }
        } catch (IOException ex) {
            throw new FileNotFoundException("File with path " + strPath + " is not read properly!");
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


    public boolean saveDataToFile(Coin[] coins) throws IOException {


        try{
           Files.copy(Paths.get(strPath), Paths.get(copyPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Can not create temp file!");
        }

        FileWriter fw = new FileWriter(strPath, false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();

        try {
            new ObjectMapper().writeValue(new File(strPath), coins);
        } catch (IOException e){

            throw new FileNotFoundException("Can not save data to file " + strPath + "!");
        }

        return true;
    }

    public void finishLoadDataDB(Callback callback) {
        this.callbackDB = callback;
    }
}
