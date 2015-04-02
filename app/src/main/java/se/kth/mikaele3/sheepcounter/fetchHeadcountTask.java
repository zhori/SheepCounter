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
    private List<AnimalItem> updatedAnimals;
    private boolean isFinished;

    private boolean processFailed;
    private String failMessage;



    public FetchHeadcountTask(AsyncTaskListener asyncTaskListener, String username, List<AnimalItem> updatedAnimals){
        super();
        this.listener = asyncTaskListener;
        this.username = username;
        this.updatedAnimals = updatedAnimals;
        isFinished = false;
        processFailed = false;
        failMessage = "";
    }

    public boolean isHeadcountFinished() {
        return isFinished;
    }


    public String getFailMessage() {
        return failMessage;
    }

    public boolean isProcessFailed() {
        return processFailed;
    }

    @Override
    protected String doInBackground(String... params) {
        String headcountID = params[0];
        List<HeadcountAnimal> headcountAnimals = new ArrayList<>();

        try {
            // check if the given headcount is considered finished
            isFinished = Model.getInstance().isFinished(headcountID);

            // send updates to model
            for(AnimalItem animalToUpdate : updatedAnimals){
                Model.getInstance().updateHeadcount(username, animalToUpdate.getAnimalID(), headcountID, animalToUpdate.isChecked());
            }
            // get the full data set from the model
            headcountAnimals = Model.getInstance().getHeadcount(headcountID);
        } catch (IOException e) {
            processFailed = true;
            failMessage = "Connection Error";
            e.printStackTrace();
        } catch (JSONException e) {
            processFailed = true;
            failMessage = "Format Error";
            e.printStackTrace();
        }

        List<AnimalItem> result = new ArrayList<>();
        for(HeadcountAnimal animal : headcountAnimals){
            boolean checked = animal.getCountedBy().contains(username);
            result.add(new AnimalItem(animal.getName(), animal.getAnimalID(), animal.getCountedBy(), checked));
        }
        this.animals = result;

        if(animals.isEmpty()){
            processFailed = true;
            failMessage = "No animals found";
        }

        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        listener.postAsyncTask(this);
    }

    public List<AnimalItem> getAnimals() {
        return new ArrayList<>(animals);
    }
}
