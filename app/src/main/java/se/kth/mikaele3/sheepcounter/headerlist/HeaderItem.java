package se.kth.mikaele3.sheepcounter.headerlist;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import se.kth.mikaele3.sheepcounter.R;

/**
 * A HeaderItem serves a header row in a list that can contain both data rows and headers.
 * <p/>
 * Created by Mikael on 2015-03-18.
 */
public class HeaderItem implements HeaderListItem {

    private final String name;

    public HeaderItem(String name) {
        this.name = name;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.header_item, null);
        } else {
            view = convertView;
        }

        TextView textView = (TextView) view.findViewById(R.id.separator);
        textView.setText(name);

        return view;
    }

    @Override
    public int getViewType() {
        return ItemType.HEADER_ITEM.ordinal();
    }
}
