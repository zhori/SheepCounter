package se.kth.mikaele3.sheepcounter;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import se.kth.mikaele3.sheepcounter.Model.Model;

/**
 * Created by Mikael on 2015-04-02.
 */
public class NewHeadcountTask extends AsyncTask<String, Void, String> {

    private boolean failed;
    private AsyncTaskListener listener;

    private String headcountID;

    public NewHeadcountTask(AsyncTaskListener asyncTaskListener){
        super();
        this.listener = asyncTaskListener;
        this.failed = false;

    }

    public String getHeadcountID() {
        return headcountID;
    }

    public boolean didCreationFail() {
        return failed;
    }

    @Override
    protected String doInBackground(String... params) {
        String listID = params[0];
        String username = params[1];
        try {
            headcountID = Model.getInstance().newHeadcount(listID, username);
        } catch (IOException e) {
            failed = true;
            e.printStackTrace();
        } catch (JSONException e) {
            failed = true;
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
