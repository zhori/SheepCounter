package se.kth.mikaele3.sheepcounter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.kth.mikaele3.sheepcounter.animalList.AnimalArrayAdapter;
import se.kth.mikaele3.sheepcounter.animalList.AnimalItem;




public class HeadcountActivity extends ActionBarActivity implements AsyncTaskListener {

    private ListView listView;
    private TextView information;
    private AnimalArrayAdapter adapter;
    private List<AnimalItem> animals;
    private String headcountID;
    private String listName;
    private String username;

    private boolean syncInProgress;
    private boolean closeInProgress;


    private FetchHeadcountTask fetchHeadcountTask;
    private CloseHeadcountTask closeHeadcountTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headcount);
        Intent intent = getIntent();
        headcountID = intent.getStringExtra("se.kth.mikaele3.sheepcounter.HEADCOUNTID");
        listName = intent.getStringExtra("se.kth.mikaele3.sheepcounter.LISTNAME");
        username = intent.getStringExtra("se.kth.mikaele3.sheepcounter.USERNAME");
        TextView textView = (TextView) findViewById(R.id.headcountTitle);
        textView.setText(listName);
        // perform an async task to update the animal list
        syncInProgress = false;
        closeInProgress = false;
        information = (TextView) findViewById(R.id.informationString);
        synchronize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Action bar, add the synchronization item
        getMenuInflater().inflate(R.menu.synchronization_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            synchronize();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @return the main listView of this activity.
     */
    public ListView getListView() {
        return this.listView;
    }

    /**
     * Synchronize the current headcount with the model, updating the list shown,
     * setting the show status to show all.
     */
    private void synchronize() {
        if(!syncInProgress) {
            syncInProgress = true;
            information.setText("Updating ...");
            List<AnimalItem> updatedAnimals = new ArrayList<>();
            if(adapter != null) {
                updatedAnimals.addAll(adapter.getChangedCheckBoxes());
            }
            // use adapter to get changed animal list items
            // clear the adapters changed checkboxes
            // send them in the async task
            fetchHeadcountTask = new FetchHeadcountTask(this, username, updatedAnimals);
            fetchHeadcountTask.execute(headcountID);
        }
    }

    public void launchAnimalInfoActivity(AnimalItem animal) {
        Intent intent = new Intent(this, AnimalInfoActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.ANIMAL", animal.getName());
        startActivity(intent);
    }

    public void launchAnimalFamilyActivity(AnimalItem animal) {
        Intent intent = new Intent(this, AnimalFamilyActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.ANIMAL", animal.getName());
        startActivity(intent);
    }

    public void hideChecked(View view) {
        adapter.clear();
        ArrayList<AnimalItem> nonCheckedAnimals = new ArrayList<>();

        for (AnimalItem animalItem : animals) {
            if (!animalItem.isChecked() && !animalItem.isCountedBySomeoneElse(username))
                nonCheckedAnimals.add(animalItem);
        }
        adapter.addAll(nonCheckedAnimals);
        adapter.notifyDataSetChanged();
    }

    public void showAll(View view) {
        updateAdapter();
    }

    private void updateAdapter() {
        adapter.clear();
        adapter.addAll(new ArrayList<>(animals));
        adapter.notifyDataSetChanged();
    }

    /**
     * Called when the asynchronous task of updating the animal list has been completed,
     * to reflect the changes of the model upon the view.
     */
    @Override
    public void postAsyncTask(AsyncTask asyncTask) {
        if (asyncTask instanceof FetchHeadcountTask) {
            if(!fetchHeadcountTask.isProcessFailed()) {
                adapter.clearChangedCheckBoxes();
                this.animals = fetchHeadcountTask.getAnimals();
                // Create a new adapter if this is the first synchronization
                if (this.adapter == null) {
                    adapter = new AnimalArrayAdapter(this, new ArrayList<>(animals), username);
                    listView = (ListView) findViewById(R.id.animalList);
                    listView.setAdapter(adapter);
                } else {
                    updateAdapter();
                }
                if (fetchHeadcountTask.isHeadcountFinished()) {
                    information.setText("Headcount finished.");
                } else {
                    information.setText("Sync complete!");
                }
            } else {
                // synchronization failed, display message
                information.setText(fetchHeadcountTask.getFailMessage());
            }
            syncInProgress = false;
        }
     if(asyncTask instanceof CloseHeadcountTask){
         if(closeHeadcountTask.isProcessFailed()){
             information.setText("Finishing failed");
         } else {
             information.setText("Headcount now finished.");
         }

     }

    }

    public void closeHeadcount(View view){
        if(!closeInProgress){
            closeInProgress = true;
            information.setText("Finishing ...");
            closeHeadcountTask = new CloseHeadcountTask(this);
            closeHeadcountTask.execute(headcountID);
        }

    }
}
