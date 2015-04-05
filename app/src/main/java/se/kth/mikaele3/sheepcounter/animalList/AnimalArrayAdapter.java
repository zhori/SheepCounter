package se.kth.mikaele3.sheepcounter.animalList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.kth.mikaele3.sheepcounter.HeadcountActivity;
import se.kth.mikaele3.sheepcounter.R;

/**
 * AnimalArrayAdapter serves as an ArrayAdapter for a ListView that displays a list of animals,
 * handling the clicks on the different elements representing one animal.
 *
 * Created by Mikael on 2015-03-23.
 */
public class AnimalArrayAdapter extends ArrayAdapter<AnimalItem> {

    private static class ViewHolder {
        public CheckBox checkBox;
        public TextView animalName;
    }

    private LayoutInflater layoutInflater;
    private HeadcountActivity headcountActivity;
    private String username;
    private List<AnimalItem> changedCheckBoxes;
    private final String headcountId;

    /**
     * Construct a new AnimalArrayAdapter given its context and items to hold.
     *
     * @param context the context of the adapter
     * @param items the items to be contained in the adapter
     */
    public AnimalArrayAdapter(Context context, List<AnimalItem> items, String username,String headcountID) {

        super(context, 0, items);
        headcountActivity = (HeadcountActivity) context;
        layoutInflater = LayoutInflater.from(context);
        changedCheckBoxes = new ArrayList<>();
        this.username = username;
        this.headcountId =headcountID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        AnimalItem animalItem = getItem(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.headcount_list_item, parent, false);

            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.animalCheckBox);
            holder.checkBox.setOnCheckedChangeListener(animalOnCheckedChangeListener);
            holder.animalName = (TextView) convertView.findViewById(R.id.animalName);

            (convertView.findViewById(R.id.familyButton)).setOnClickListener(familyButtonListener);
            (convertView.findViewById(R.id.infoButton)).setOnClickListener(infoButtonListener);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Changing the set status while being listened to creates an error if the view is not visible
        // when trying to get the position
        holder.checkBox.setOnCheckedChangeListener(null);
        // if the animal is counted by another user change the graphics of the checkbox
        if(animalItem.isCountedBySomeoneElse(username)){
            holder.checkBox.setButtonDrawable(R.drawable.bigger_checkbox_grey);
        } else {
            holder.checkBox.setButtonDrawable(R.drawable.bigger_checkbox);
        }
        holder.checkBox.setChecked(animalItem.isChecked());
        holder.checkBox.setOnCheckedChangeListener(animalOnCheckedChangeListener);

        holder.animalName.setText(animalItem.getName());

        return convertView;
    }

    private View.OnClickListener familyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Tell the headcountActivity to handle the family button click for this particular animal
            int position = headcountActivity.getListView().getPositionForView(v);
            headcountActivity.launchAnimalFamilyActivity(getItem(position));
        }
    };

    private View.OnClickListener infoButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Tell the headcountActivity to handle the info button click for this particular animal
            int position = headcountActivity.getListView().getPositionForView(v);
            headcountActivity.launchAnimalInfoActivity(getItem(position));
        }
    };

    private CompoundButton.OnCheckedChangeListener animalOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = headcountActivity.getListView().getPositionForView(buttonView);
            AnimalItem animalItem = getItem(position);
            animalItem.setChecked(isChecked);
            // record the change of the checkbox
            if(changedCheckBoxes.contains(animalItem)){
                changedCheckBoxes.remove(animalItem);
            } else {
                changedCheckBoxes.add(animalItem);
            }
        }
    };

    public List<AnimalItem> getChangedCheckBoxes(){
        return new ArrayList<>(changedCheckBoxes);
    }

    public String getHeadcountId(){return headcountId;}

    public void clearChangedCheckBoxes(){
        changedCheckBoxes.clear();
    }
}
