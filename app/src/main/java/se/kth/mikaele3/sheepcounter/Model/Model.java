package se.kth.mikaele3.sheepcounter.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mikael on 2015-03-26.
 */
public final class Model {

    private static final String SERVERURL = "http://6823b235.ngrok.com/";

    private static Model instance;

    private Model(){} // private constructor to avoid instantiation

    /**
     * @return the singleton Model of the SheepCounter application.
     */
    public static synchronized Model getInstance(){
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }


    public synchronized boolean checkUsername(String username, ConnectivityManager connectivityManager) throws IOException, JSONException {
        JSONObject jsonObject;
        boolean result = false;
        // Check if there is a connection available
        if(checkConnection(connectivityManager)){

            InputStream inputStream = null;
            try {
                URL url = new URL(SERVERURL + "does_user_exist?username=" + username);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                // perform the query
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("DEBUG_TAG", "The response is " + response);
                inputStream = connection.getInputStream();
                jsonObject = convertInputStreamToJSONObject(inputStream);
            }
            finally {
                if(inputStream != null){
                    inputStream.close();
                }
            }
        } else {
            throw  new IOException("No connection available");
        }
        result = jsonObject.getBoolean("result");
        return result;
    }

    private boolean checkConnection(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }

    private JSONObject convertInputStreamToJSONObject(InputStream inputStream)
            throws IOException, JSONException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        StringBuilder resultString = new StringBuilder();
        while((line = bufferedReader.readLine()) != null)
            resultString.append(line);

        JSONObject jsonObject = null;
            jsonObject = new JSONObject(resultString.toString());

        return jsonObject;
    }
}
