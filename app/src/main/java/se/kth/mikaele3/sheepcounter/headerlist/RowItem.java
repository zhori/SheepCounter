package se.kth.mikaele3.sheepcounter.headerlist;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import se.kth.mikaele3.sheepcounter.R;

/**
 * Created by Mikael on 2015-03-18.
 */
public class RowItem implements HeaderListItem {

    private final String name;
    private final String lastCompletedCount;

    public RowItem(String name, String lastCompletedCount){
        this.name = name;
        this.lastCompletedCount = lastCompletedCount;
    }

    @Override
    public int getViewType() {
        return ItemType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if(convertView == null){
            view = inflater.inflate(R.layout.row_item, null);
        } else {
            view = convertView;
        }

        TextView textView1 = (TextView) view.findViewById(R.id.list_content1);
        TextView textView2 = (TextView) view.findViewById(R.id.list_content2);
        textView1.setText(name);
        textView2.setText(lastCompletedCount);

        return view;
    }
}
