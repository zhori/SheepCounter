package se.kth.mikaele3.sheepcounter;

import android.content.Intent;
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


public class HeadcountActivity extends ActionBarActivity {

    private ListView listView;
    private AnimalArrayAdapter adapter;
    private ArrayList<AnimalItem> animals;
    private String headcountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headcount);
        Intent intent = getIntent();
        headcountID = intent.getStringExtra("se.kth.mikaele3.sheepcounter.HEADCOUNTID");
        TextView textView = (TextView) findViewById(R.id.headcountTitle);
        textView.setText(headcountID);

        fetchAnimals();

        adapter = new AnimalArrayAdapter(this, new ArrayList<AnimalItem>(animals));
        listView = (ListView) findViewById(R.id.animalList);
        listView.setAdapter(adapter);

    }

    private void fetchAnimals() {
        //TODO hardcoded data, should be fetched from database/model
        animals = new ArrayList<>();
        animals.add(new AnimalItem("name1", false));
        animals.add(new AnimalItem("name2", false));
        animals.add(new AnimalItem("name3", true));
        animals.add(new AnimalItem("name4", false));
        animals.add(new AnimalItem("name5", false));
        animals.add(new AnimalItem("name6", true));
        animals.add(new AnimalItem("name7", false));
        animals.add(new AnimalItem("name8", false));
        animals.add(new AnimalItem("name9", true));
        animals.add(new AnimalItem("name10", false));
        animals.add(new AnimalItem("name11", false));
        animals.add(new AnimalItem("name12", true));
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
     * Synchronize the current headcount with the model, updating the list shown,
     * setting the show status to show all.
     */
    private void synchronize() {
        // TODO send the current list to the database
        fetchAnimals();
        showAll(null);
    }

    public void launchAnimalInfoActivity(AnimalItem animal) {
        Intent intent = new Intent(this, AnimalInfoActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.ANIMAL", animal.getName());
        startActivity(intent);
    }

    public ListView getListView(){
        return this.listView;
    }

    public void launchAnimalFamilyActivity(AnimalItem animal) {
        Intent intent = new Intent(this, AnimalFamilyActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.ANIMAL", animal.getName());
        startActivity(intent);
    }

    public void hideChecked(View view){
        adapter.clear();
        ArrayList<AnimalItem> nonCheckedAnimals = new ArrayList<>();

        for(AnimalItem animalItem : animals){
            if(!animalItem.isChecked())
                nonCheckedAnimals.add(animalItem);
        }
        adapter.addAll(nonCheckedAnimals);
        adapter.notifyDataSetChanged();
    }

    public void showAll(View view){
        adapter.clear();
        adapter.addAll(new ArrayList<AnimalItem>(animals));
        adapter.notifyDataSetChanged();
    }
}
