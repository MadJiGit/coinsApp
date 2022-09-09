package resources;

import com.example.coingame.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.ArrayList;

public class DB extends AbstractDataController {
    CoinDataController coinData = CoinDataController.getInstance();
    private Callback callbackDB;
    Path path;
    public void makeDBConnection() throws FileNotFoundException {

        try {
            String result;
            path = FileSystems.getDefault().getPath(Constants.MY_COIN_PATH_STRING);
            result = readFileFromResources(path);
            if(result.isEmpty()){
                System.out.println("file is empty");
            } else {
                Coin[] coins = (Coin[]) parseJsonData(result, Coin.class);
                coinData.addList(coinData.getMyCoinsList(), coins);
            }
            if(callbackDB != null){
//                callbackDB.getDataFromApi();
                callbackDB.getDataFromApi();
            }
        } catch (IOException ex) {
            throw new FileNotFoundException("File with path " + Constants.MY_COIN_PATH_STRING + " is not read properly!");
        }
    }

    public boolean saveDataToFile(ArrayList<Coin> coins) throws IOException {

        // make reserve copy of file before clear data
        makeReserveCopyOfData(Constants.MY_COIN_PATH_STRING, Constants.MY_COIN_PATH_STRING_COPY);

        // Try to write data to main file
        try {
           // new ObjectMapper().writeValue(new File(strPath), coins);
            testOnly();
            clearDataFile(Constants.MY_COIN_PATH_STRING_COPY);

        } catch (IOException e){
            // If write new data is not possible try to revert data from reserve copy
            makeReserveCopyOfData(Constants.MY_COIN_PATH_STRING_COPY, Constants.MY_COIN_PATH_STRING);
            throw new FileNotFoundException("Can not save data to file " + Constants.MY_COIN_PATH_STRING + "!");
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

    public void loadDataFromDB() throws FileNotFoundException {
        Callback callbackDb = new CallbackImpl();
        finishLoadDataDB(callbackDb);
        makeDBConnection();
    }
}
