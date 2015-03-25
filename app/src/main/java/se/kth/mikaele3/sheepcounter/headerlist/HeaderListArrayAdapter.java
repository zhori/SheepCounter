package se.kth.mikaele3.sheepcounter.headerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * An ArrayAdapter adjusted to handle two types of items,
 * either row items or header items to be displayed in a ListView.
 * <p/>
 * Created by Mikael on 2015-03-18.
 */
public class HeaderListArrayAdapter extends ArrayAdapter<HeaderListItem> {

    private LayoutInflater layoutInflater;

    /**
     * Construct a new HeaderListArrayAdapter given its context and items to hold.
     *
     * @param context the context of this adapter.
     * @param items the items to be held by this adapter.
     */
    public HeaderListArrayAdapter(Context context, List<HeaderListItem> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return HeaderListItem.ItemType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(layoutInflater, convertView);
    }
}
