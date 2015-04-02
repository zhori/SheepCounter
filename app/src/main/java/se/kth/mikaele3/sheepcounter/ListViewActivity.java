package se.kth.mikaele3.sheepcounter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import se.kth.mikaele3.sheepcounter.Model.HeadcountMetaInfo;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderItem;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListArrayAdapter;
import se.kth.mikaele3.sheepcounter.headerlist.HeaderListItem;
import se.kth.mikaele3.sheepcounter.headerlist.RowItem;


public class ListViewActivity extends ActionBarActivity implements AsyncTaskListener {

    private String username;

    private ListView listView;
    private TextView information;
    private PopupWindow popupWindow;
    private HeadcountMetaInfo latestClickedHeadcount;
    private String latestClickedList;

    private List<HeaderListItem> items;
    private HeaderListArrayAdapter adapter;
    private FetchListsTask fetchListsTask;
    private NewHeadcountTask newHeadcountTask;

    private boolean syncInProgress;
    private boolean newHeadcountInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        username = intent.getStringExtra("se.kth.mikaele3.sheepcounter.USERNAME");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        this.items = new ArrayList<>();
        information = (TextView) findViewById(R.id.listViewInformationString);

        //Try to fetch lists information from the model
        syncInProgress = false;
        newHeadcountInProgress = false;
        updateLists();
        adapter = new HeaderListArrayAdapter(this, new ArrayList<>(items));
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

    @Override
    public void onResume() {
        super.onResume();
        updateLists(); // try to update the lists

    }

    private void updateLists() {
        if(!syncInProgress) {
            information.setText("Updating ...");
            syncInProgress = true;
            this.fetchListsTask = new FetchListsTask(this);
            fetchListsTask.execute(username);
        }
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
            String listID = rowItem.getIdentifier();

            HeadcountMetaInfo headcountMetaInfo = fetchListsTask.getLatestHeadcountInfo(listID);

            if (headcountMetaInfo != null) {
                latestClickedHeadcount = headcountMetaInfo;
                latestClickedList = listID;
                showPopupChoice(rowItem.getName(), headcountMetaInfo);
            } else {
                createNewHeadcount(listID);
            }
        }
    }

    private void showPopupChoice(String listName, HeadcountMetaInfo headcountMetaInfo) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = inflater.inflate(R.layout.popup_join_list, null, false);
        popupWindow = new PopupWindow(popupLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(this.findViewById(R.id.animalLists), Gravity.CENTER, 0, 0);
        TextView textView1 = (TextView) popupLayout.findViewById(R.id.popup_animal_list_name1);
        textView1.setText(listName);
        TextView textView2 = (TextView) popupLayout.findViewById(R.id.popup_animal_list_name2);
        textView2.setText(listName);
        TextView textView3 = (TextView) popupLayout.findViewById(R.id.popup_started_at);
        textView3.setText("Started at " + headcountMetaInfo.getStartTime());
    }

    /**
     * Creates a new headcount for the given list.
     *
     * @param listID The id of the list to create a headcount of.
     */
    private void createNewHeadcount(String listID) {
        if(!newHeadcountInProgress){
            newHeadcountInProgress = true;
            information.setText("Creating new headcount ...");
            newHeadcountTask = new NewHeadcountTask(this);
            newHeadcountTask.execute(listID, username);
        }
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
            updateLists(); // fetch fresh info using async task
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAdapter() {
        adapter.clear();
        adapter.addAll(new ArrayList<>(items));
        adapter.notifyDataSetChanged();
    }

    public void popupJoinHeadcountClicked(View view) {
        if (latestClickedHeadcount != null)
            dismissPopup(null);
            launchHeadcountActivity(latestClickedHeadcount.getHeadcountIdentifier());
    }

    public void popupNewHeadcountClicked(View view) {
        if (latestClickedList != null) {
            dismissPopup(null);
            createNewHeadcount(latestClickedList);

        }

    }

    private void launchHeadcountActivity(String headcountID) {
        Intent intent = new Intent(this, HeadcountActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.HEADCOUNTID", headcountID);
        intent.putExtra("se.kth.mikaele3.sheepcounter.LISTNAME", latestClickedList);
        intent.putExtra("se.kth.mikaele3.sheepcounter.USERNAME", username);
        startActivity(intent);

    }

    /**
     * After completion of a an asynchronous task this method is called and handles necessary updates to the view.
     */
    @Override
    public void postAsyncTask(AsyncTask asyncTask) {
        if(asyncTask instanceof FetchListsTask) {
            if (fetchListsTask != null) {
                this.items = fetchListsTask.getHeaderListItems();
                updateAdapter();
            }
            information.setText("Sync complete");
            syncInProgress = false;
        }
        if(asyncTask instanceof NewHeadcountTask){
            if(!newHeadcountTask.didCreationFail()){
                String headcountID = newHeadcountTask.getHeadcountID();
                information.setText("Creation complete");
                launchHeadcountActivity(headcountID);
            } else {
                information.setText("Creation failed");
            }
            newHeadcountInProgress = false;
        }

    }

    public void dismissPopup(View view){
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
