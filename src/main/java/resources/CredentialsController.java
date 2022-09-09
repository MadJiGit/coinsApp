package resources;

import com.example.coingame.AbstractDataController;
import com.example.coingame.Credentials;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class CredentialsController extends AbstractDataController {

    static Credentials credentials;
    private static CredentialsController credentialsController = null;
    //private static final String strPath = "C:\\Users\\raykov\\IdeaProjects\\CoinGame\\src\\credentials.json";

    //public static final String BASE_URL = "https://rest.coinapi.io/v1/";
    //public static final String API_KEY = "X-CoinAPI-Key";

    //public static final String API_KEY_VALUE = "apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";


    private CredentialsController() throws FileNotFoundException {
        Path path = null;
        try {
            path = FileSystems.getDefault().getPath(Constants.CREDENTIALS_PATH_STRING);
            String result = readFileFromResources(path);

            credentials = (Credentials) parseJsonData(result, Credentials.class)[0];

        } catch (IOException e) {
            throw new FileNotFoundException("Connection with path " + path + " return with error");
        }
    }

    public static CredentialsController getInstance() throws FileNotFoundException {
        if (credentialsController == null){
            credentialsController = new CredentialsController();
        }

        return credentialsController;
    }

    public String getAssetsFilteredByIds(ArrayList<String> coinsIds) {
        String result = "";
        String temp = String.join(",", coinsIds);

        if(credentials == null){
            credentials = new Credentials();
        }

        result = credentials.getBaseUrl()
                + credentials.getAssets()
                + credentials.getFilteredIdAssets()
                + temp
                + credentials.getAndMark()
                + credentials.getApiKeyEqual()
                + credentials.getApiKeyValue();

        return result;
    }

    public String getAllAssets() {
        String result = "";

        result = credentials.getBaseUrl()
                + credentials.getAssets()
                + credentials.getApiKeyValue();

        return result;
    }

//    String filterAssets = "https://rest.coinapi.io/v1/assets?filter_asset_id={array}& apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";
//    String allAssets = "https://rest.coinapi.io/v1/assets? apikey=3CCDC3B6-F709-4544-86F9-69888FB0C543";


}
