package se.kth.mikaele3.sheepcounter;

import android.os.AsyncTask;

/**
 * An AsyncTaskListener implements a method that can be used by an Asynchronous task to call when it is finished,
 * which can be used to inform listeners of the results of the task.
 *
 * Created by Mikael on 2015-03-31.
 */
public interface AsyncTaskListener {

    public void postAsyncTask(AsyncTask asyncTask);
}
