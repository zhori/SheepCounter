package se.kth.mikaele3.sheepcounter;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.kth.mikaele3.sheepcounter.Model.AnimalListMetaInfo;
import se.kth.mikaele3.sheepcounter.Model.HeadcountMetaInfo;
import se.kth.mikaele3.sheepcounter.Model.Model;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderItem;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListItem;
import se.kth.mikaele3.sheepcounter.headerlist.RowItem;

/**
 * FetchListsTask is responsible for fetching meta info of all lists for a given user from the model,
 * and present them as in a list with headers usable by the view.
 *
 * Created by Mikael on 2015-03-30.
 */
public class FetchListsTask extends AsyncTask<String, Void, String> {

    private List<HeaderListItem> headerListItems;
    private AsyncTaskListener listener;

    public HeadcountMetaInfo getLatestHeadcountInfo(String listID) {
        return latestHeadcountInfo.get(listID);
    }

    private Map<String, HeadcountMetaInfo> latestHeadcountInfo;

    public FetchListsTask(AsyncTaskListener asyncTaskListener){
        super();
        this.listener = asyncTaskListener;
        headerListItems = new ArrayList<>();
        this.latestHeadcountInfo = new HashMap<>();
    }

    @Override
    protected String doInBackground(String... parameters) {
        // params comes from the execute() call: params[0] is the username to check
        String username = parameters[0];

        List<AnimalListMetaInfo> metaInfoList = new ArrayList<>();

        try {
            metaInfoList = Model.getInstance().fetchListsMetaData(username);
            // fetch info regarding the latest performed headcount for each list
            for(AnimalListMetaInfo list: metaInfoList){
                HeadcountMetaInfo headcountMetaInfo = Model.getInstance().getLastestHeadcountInfo(list.getListIdentifier());
                if(headcountMetaInfo != null){
                    latestHeadcountInfo.put(list.getListIdentifier(), headcountMetaInfo);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.headerListItems = convertToHeaderList(metaInfoList);

        return Boolean.toString(!metaInfoList.isEmpty());
    }

    private List<HeaderListItem> convertToHeaderList(List<AnimalListMetaInfo> metaInfoList) {

        HashMap<String, ArrayList<AnimalListMetaInfo>> farmMap = new HashMap<>();
        // sort all list meta info according to the farm it belongs to
        for(AnimalListMetaInfo metaInfo : metaInfoList){
            if(!farmMap.containsKey(metaInfo.getFarm())){
                ArrayList<AnimalListMetaInfo> list = new ArrayList<>();
                list.add(metaInfo);
                farmMap.put(metaInfo.getFarm(), list);

            } else {
                farmMap.get(metaInfo.getFarm()).add(metaInfo);
            }
        }
        // create a new header list with a header for each farm
        List<HeaderListItem> result = new ArrayList<>();
        for(String farm : farmMap.keySet()){
            HeaderItem headerItem = new HeaderItem(farm);
            result.add(headerItem);
            List<AnimalListMetaInfo> list = farmMap.get(farm);
            for(AnimalListMetaInfo item : list){
                RowItem rowItem = new RowItem(item.getName(), item.getCreatedBy(), item.getListIdentifier());
                result.add(rowItem);
            }
        }
        return result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        listener.postAsyncTask(this);
    }

    public List<HeaderListItem> getHeaderListItems() {
        return new ArrayList<>(headerListItems);
    }
}