package se.kth.mikaele3.sheepcounter.headerlist;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Interface for items in to be contained within a list,
 * where each item either is a row (data entry) or a header.
 *
 * Created by Mikael on 2015-03-17.
 */
public interface HeaderListItem {

    public enum ItemType {
        LIST_ITEM, HEADER_ITEM
    }

    /**
     * @return the ItemType of this HeaderListItem
     */
    public int getViewType();

    /**
     * @return the view for this item
     */
    public View getView(LayoutInflater inflater, View convertView);





}
