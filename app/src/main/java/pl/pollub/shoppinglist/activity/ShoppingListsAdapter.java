package pl.pollub.shoppinglist.activity;


import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

import pl.pollub.shoppinglist.R;

public class ShoppingListsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> date;
    private final ArrayList<ParseObject> item;
    private SparseBooleanArray mSelectedItemsIds;
    private boolean template;

    public ShoppingListsAdapter(Activity context,
                                ArrayList<String> name, ArrayList<String> date, ArrayList<ParseObject> listsItems, Boolean template) {
        super(context, R.layout.lists_list_item, name);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.name = name;
        this.date = date;
        this.item = listsItems;
        this.template = template;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lists_list_item, null, true);
        TextView listName = rowView.findViewById(R.id.listNameItem);
        listName.setText(item.get(position).getString("name"));
        TextView listDeadline = rowView.findViewById(R.id.listDeadline);
        listDeadline.setText(item.get(position).getString("deadline"));
        TextView listFriends = rowView.findViewById(R.id.collaborators);
        if(template==true)
            listDeadline.setText("-");

        return rowView;
    }





    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if(value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}