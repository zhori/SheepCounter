package se.kth.mikaele3.sheepcounter.animalList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.kth.mikaele3.sheepcounter.HeadcountActivity;
import se.kth.mikaele3.sheepcounter.R;

/**
 * Created by Mikael on 2015-03-23.
 */
public class AnimalArrayAdapter extends ArrayAdapter<AnimalItem> {

    private static class ViewHolder {
        public CheckBox checkBox;
        public TextView animalName;
    }

    private LayoutInflater layoutInflater;
    private HeadcountActivity headcountActivity;

    /**
     * Construct a new AnimalArrayAdapter given its context and items to hold.
     *
     * @param context
     * @param items
     */
    public AnimalArrayAdapter(Context context, List<AnimalItem> items){

        super(context, 0, items);
        headcountActivity = (HeadcountActivity) context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        AnimalItem animalItem = getItem(position);

        if(convertView ==null){
            convertView = layoutInflater.inflate(R.layout.headcount_list_item, parent, false);

            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.animalCheckBox);
            holder.checkBox.setOnCheckedChangeListener(animalOnCheckedChangeListener);
            holder.animalName = (TextView) convertView.findViewById(R.id.animalName);

            ((ImageView) convertView.findViewById(R.id.familyButton)).setOnClickListener(familyButtonListener);
            ((ImageView) convertView.findViewById(R.id.infoButton)).setOnClickListener(infoButtonListener);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Changing the set status while being listened to creates an error if the view is not visible
        // when trying to get the position
        holder.checkBox.setOnCheckedChangeListener(null);
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
            getItem(position).setChecked(isChecked);
        }
    };
}
