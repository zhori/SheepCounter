package se.kth.mikaele3.sheepcounter.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikael on 2015-03-26.
 */
public final class Model {

    private static final String SERVERURL = "http://4565eed1.ngrok.com/";

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


    public synchronized boolean checkUsername(String username) throws IOException, JSONException {
        JSONObject jsonObject;
        boolean result = false;
        String request = "does_user_exist?username=" + username;
        jsonObject = performHttpRequest(request);
        result = jsonObject.getBoolean("result");
        return result;
    }

    private JSONObject performHttpRequest(String request) throws IOException, JSONException {
        JSONObject jsonObject;// Check if there is a connection available
            InputStream inputStream = null;
            try {
                URL url = new URL(SERVERURL + request);
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
        return jsonObject;
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

    public List<AnimalListMetaInfo> fetchListsMetaData(String username) throws IOException, JSONException {
        JSONObject jsonObject;
        List<AnimalListMetaInfo> metaInfoList = new ArrayList<>();

        String request = "get_list_meta_data?username=" + username;
        jsonObject = performHttpRequest(request);

        JSONArray jsonArray = jsonObject.getJSONArray("result");
        int length = jsonArray.length();
        for(int i = 0; i < length; i++){
            JSONObject row = jsonArray.getJSONObject(i);
            String listIdentifier = row.getString("list_identifier");
            String farmName = row.getString("farm_name");
            String listName = row.getString("list_name");
            String createdBy = row.getString("created_by");

            JSONArray usersJSON = row.getJSONArray("users");
            int numberOfUsers = usersJSON.length();
            ArrayList<String> users = new ArrayList<>();

            for(int j = 0; j < numberOfUsers; j++){
                users.add(usersJSON.getString(j));
            }

            AnimalListMetaInfo animalListMetaInfo = new AnimalListMetaInfo(listIdentifier,farmName,listName,createdBy,users);
            metaInfoList.add(animalListMetaInfo);
        }
        return metaInfoList;
    }

    /**
     * Return the latest headcount for the given list, or null if no headcount has been done yet.
     */
    public HeadcountMetaInfo getLastestHeadcountInfo(String listIdentifier) throws IOException, JSONException {
        HeadcountMetaInfo headcountMetaInfo = null;
        String request = "get_latest_head_count?animal_list_id=" + listIdentifier;
        JSONObject jsonObject = performHttpRequest(request);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        int length = jsonArray.length();
        if(length > 0){
            JSONObject infoJSON = jsonArray.getJSONObject(0);
            headcountMetaInfo = new HeadcountMetaInfo(infoJSON.getString("head_count_identifier"), infoJSON.getString("start_time"), infoJSON.getString("created_by"));
        }
        return headcountMetaInfo;
    }

    public List<HeadcountAnimal> getHeadcount(String headcountID) throws IOException, JSONException {
        List<HeadcountAnimal> list = new ArrayList<>();
        String request = "get_head_count?head_count_id=" + headcountID;
        JSONObject jsonObject = performHttpRequest(request);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        int length = jsonArray.length();
        if(length == 0){
            return null;
        }
        for(int i = 0; i < length; i++){
            JSONObject animal = jsonArray.getJSONObject(i);
            JSONArray countedByJSON = animal.getJSONArray("counted_by");
            int numberOfCounters = countedByJSON.length();
            List<String> countedBy = new ArrayList<>();

            for(int j = 0; j < numberOfCounters; j++){
                countedBy.add(countedByJSON.getString(j));
            }
            list.add(new HeadcountAnimal(animal.getString("ear_number"), animal.getString("animal_id"), countedBy));

        }

        return list;
    }
}
