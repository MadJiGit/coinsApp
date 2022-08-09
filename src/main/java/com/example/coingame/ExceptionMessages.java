package com.example.coingame;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ExceptionMessages {
    public static final String CAN_NOT_SAVE_DATA_TO_FILE = "Can not save data to file!";
    public static final String DO_YOU_WANT_TO_SAVE_DATA = "Do you want to save data?";
    public static final String DATA_SAVE_SUCCESSFULLY = "Data save successfully to file!";
    public static final String VOLUME_DATA_MUST_BE_NUMBER = "Volume must be number format!";
    public static final String PRICE_DATA_MUST_BE_DOUBLE = "Price must be double format!";
    public static final String NAME_MUST_BE_NOT_EMPTY = "Name must be valid!";
    public static final String ASSET_MUST_BE_NOT_EMPTY = "Asset id must be not empty";
    public static final String ERROR_COPY_FILE = "Can not make reserve copy of file!";
    public static final String NO_COIN_WITH_ID = "No such kind of coin!";
    public static final String SUCCESSFULLY_DELETE_COIN = "Data is removed successfully!";
    public static final String DATA_IS_NOT_DELETED = "Data is not deleted!";
    public static final String DO_YOU_WANT_TO_DELETE_DATA = "Do you really want to delete that data?";

    public static void showAlertWindow(Alert.AlertType error, String msg, ButtonType bType){
        Alert alert = new Alert(error, msg , bType);
        alert.showAndWait();
        ButtonType res = alert.getResult();
        if(ButtonType.YES.equals(res)){
            alert.close();
        }
    }
}