package se.kth.mikaele3.sheepcounter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.kth.mikaele3.sheepcounter.headerlist.HeaderItem;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListArrayAdapter;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListItem;
import se.kth.mikaele3.sheepcounter.headerlist.RowItem;


public class ListViewActivity extends ActionBarActivity {

    private ListView listView;
    private PopupWindow popupWindow;
    private String latestClickedHeadcount;
    private String latestClickedList;

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
        listView = (ListView) findViewById(R.id.animalLists);
        listView.setAdapter(adapter);

        // Make the ListView clickable
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                HeaderListItem item = (HeaderListItem) listView.getItemAtPosition(position);
                handleClickOn(item);
            }
        });

    }

    /**
     * Handles click on the main list, ignoring clicks on header items.
     *
     * @param item the item clicked on in a list with header items
     */
    private void handleClickOn(HeaderListItem item) {
        // check that the item is not a header item
        if (item.getViewType() == HeaderListItem.ItemType.LIST_ITEM.ordinal()) {
            RowItem rowItem = (RowItem) item;

            //TODO get the id from the rowItem, try to fetch the last unfinished headcount for this list
            String headcountID = "todoID";
            String listID = "listID";

            if (!headcountID.isEmpty()) {
                latestClickedHeadcount = headcountID;
                latestClickedList = listID;
                showPopupChoice(rowItem.getName());
            } else {
                headcountID = createNewHeadcount(listID);
                launchHeadcountActivity(headcountID);
            }
        }
    }

    private void showPopupChoice(String listName) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = inflater.inflate(R.layout.popup_join_list, null, false);
        popupWindow = new PopupWindow(popupLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(this.findViewById(R.id.animalLists), Gravity.CENTER, 0, 0);
        TextView textView1 = (TextView) popupLayout.findViewById(R.id.popup_animal_list_name1);
        textView1.setText(listName);
        TextView textView2 = (TextView) popupLayout.findViewById(R.id.popup_animal_list_name2);
        textView2.setText(listName);
    }

    /**
     * Creates a new headcount for the given list.
     *
     * @param listID
     * @return the ID of the created headcount.
     */
    private String createNewHeadcount(String listID) {
        //TODO create new headcount for the given list
        return "TODO";
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

    public void popupJoinHeadcountClicked(View view) {
        if(latestClickedHeadcount != null)
         launchHeadcountActivity(latestClickedHeadcount);
    }
    public void popupNewHeadcountClicked(View view) {
        if(latestClickedList != null) {
            String headcountID = createNewHeadcount(latestClickedList);
            launchHeadcountActivity(headcountID);
        }

    }

    private void launchHeadcountActivity(String headcountID) {
        Intent intent = new Intent(this, HeadcountActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.HEADCOUNTID", headcountID);
        startActivity(intent);

    }
}
