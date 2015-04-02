package se.kth.mikaele3.sheepcounter;

import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import se.kth.mikaele3.sheepcounter.Model.Model;

/**
 * Created by Mikael on 2015-04-02.
 */
public class CloseHeadcountTask extends AsyncTask<String, Void, String> {

    private final AsyncTaskListener listener;

    private boolean processFailed;

    public CloseHeadcountTask(AsyncTaskListener listener){
        this.listener = listener;
        this.processFailed = false;
    }

    public boolean isProcessFailed() {
        return processFailed;
    }

    @Override
    protected String doInBackground(String... params) {
        String headcountID = params[0];

        try {
            Model.getInstance().finishHeadcount(headcountID);
        } catch (IOException e) {
            processFailed = true;
            e.printStackTrace();
        } catch (JSONException e) {
            processFailed = true;
            e.printStackTrace();
        }
        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        listener.postAsyncTask(this);
    }
}
