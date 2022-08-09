package resources;

import com.example.coingame.Callback;
import com.example.coingame.Coin;
import com.example.coingame.CoinDataController;
import com.example.coingame.ExceptionMessages;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class DB {
    CoinDataController coinData = CoinDataController.getInstance();
    private Callback callbackDB;
    Path path;
    String strPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\myCoins.json";
    String copyPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\reserveCopyOfMyCoinsFile.json";

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


    public boolean saveDataToFile(ArrayList<Coin> coins) throws IOException {

        // make reserve copy of file before clear data
        makeReserveCopyOfData(strPath, copyPath);

        // Try to write data to main file
        try {
           // new ObjectMapper().writeValue(new File(strPath), coins);
            testOnly();
            clearDataFile(copyPath);

        } catch (IOException e){
            // If write new data is not possible try to revert data from reserve copy
            makeReserveCopyOfData(copyPath, strPath);
            throw new FileNotFoundException("Can not save data to file " + strPath + "!");
        }

        return true;
    }


    private void makeReserveCopyOfData(String source, String destination) throws IOException {
        try{
            Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
            // if copy is successfully created
            // clear data from main file
            clearDataFile(source);
        } catch (IOException ex) {
            ExceptionMessages.showAlertWindow(Alert.AlertType.ERROR, ExceptionMessages.ERROR_COPY_FILE, ButtonType.OK);
            throw new IOException("Can not save data to file " + source + "!");
        }
    }

    private void clearDataFile(String strPath) throws IOException {
        FileWriter fw = new FileWriter(strPath, false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
    }

    private void testOnly() throws IOException {
        throw new IOException("PROBLEM!");
    }

    public void finishLoadDataDB(Callback callback) {
        this.callbackDB = callback;
    }
}
