package se.kth.mikaele3.sheepcounter.headerlist;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import se.kth.mikaele3.sheepcounter.R;

/**
 * A RowItem represents a row in a list with headers, compared to the HeaderItem which represents
 * a header. They are both used by the HeaderListArrayAdapter.
 *
 * Created by Mikael on 2015-03-18.
 */
public class RowItem implements HeaderListItem {

    private final String name;
    private final String secondaryInformation;

    public RowItem(String name, String secondaryInformation) {
        this.name = name;
        this.secondaryInformation = secondaryInformation;
    }

    @Override
    public int getViewType() {
        return ItemType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_item, null);
        } else {
            view = convertView;
        }

        TextView textView1 = (TextView) view.findViewById(R.id.list_content1);
        TextView textView2 = (TextView) view.findViewById(R.id.list_content2);
        textView1.setText(name);
        textView2.setText(secondaryInformation);

        return view;
    }

    public String getName() {
        return name;
    }
}
