package se.kth.mikaele3.sheepcounter;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.kth.mikaele3.sheepcounter.headerlist.HeaderItem;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListArrayAdapter;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListItem;
import se.kth.mikaele3.sheepcounter.headerlist.RowItem;


public class ListViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // TODO hardcoded example to be removed
        List<HeaderListItem> items = new ArrayList<HeaderListItem>();
        items.add(new HeaderItem("Farm 1"));
        items.add(new RowItem("Animal list 1", "last completed at"));
        items.add(new RowItem("Animal list 2", "Last completed at"));
        items.add(new HeaderItem("Farm 2"));
        items.add(new RowItem("Animal list 1", "Last completed at"));
        items.add(new RowItem("Animal list 2", "Last completed at"));
        HeaderListArrayAdapter adapter = new HeaderListArrayAdapter(this, items);
        ListView listView = (ListView) findViewById(R.id.animalLists);
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
