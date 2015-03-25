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
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListArrayAdapter;


public class HeadcountActivity extends ActionBarActivity {

    private ListView listView;
    private AnimalArrayAdapter adapter;
    private ArrayList<AnimalItem> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headcount);
        Intent intent = getIntent();
        String headcountID = intent.getStringExtra("se.kth.mikaele3.sheepcounter.HEADCOUNTID");
        TextView textView = (TextView) findViewById(R.id.headcountTitle);
        textView.setText(headcountID);

        animals = new ArrayList<AnimalItem>();
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

        adapter = new AnimalArrayAdapter(this, (List<AnimalItem>) animals.clone());
        listView = (ListView) findViewById(R.id.animalList);
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_headcount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        adapter.addAll((java.util.Collection<? extends AnimalItem>) animals.clone());
        adapter.notifyDataSetChanged();
    }
}
