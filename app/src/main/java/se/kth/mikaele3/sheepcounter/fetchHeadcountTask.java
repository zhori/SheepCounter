package se.kth.mikaele3.sheepcounter;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.kth.mikaele3.sheepcounter.Model.AnimalListMetaInfo;
import se.kth.mikaele3.sheepcounter.Model.HeadcountAnimal;
import se.kth.mikaele3.sheepcounter.Model.Model;
import se.kth.mikaele3.sheepcounter.animalList.AnimalItem;

/**
 * Created by Mikael on 2015-03-31.
 */
public class FetchHeadcountTask extends AsyncTask<String, Void, String>  {

    private AsyncTaskListener listener;
    private List<AnimalItem> animals;
    private String username;

    public FetchHeadcountTask(AsyncTaskListener asyncTaskListener, String username){
        super();
        this.listener = asyncTaskListener;
        this.username = username;
    }

    @Override
    protected String doInBackground(String... params) {
        String headcountID = params[0];
        List<HeadcountAnimal> headcountAnimals = new ArrayList<>();

        try {
            headcountAnimals = Model.getInstance().getHeadcount(headcountID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<AnimalItem> result = new ArrayList<>();
        for(HeadcountAnimal animal : headcountAnimals){
            boolean checked = animal.getCountedBy().contains(username);
            result.add(new AnimalItem(animal.getName(), animal.getAnimalID(), animal.getCountedBy(), checked));
        }
        this.animals = result;
        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        listener.postAsyncTask();
    }

    public List<AnimalItem> getAnimals() {
        return new ArrayList<>(animals);
    }
}
