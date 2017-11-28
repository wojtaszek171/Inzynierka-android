package pl.pollub.shoppinglist.adapter;


import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import pl.pollub.shoppinglist.R;

public class ShoppingListsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> date;
    private ArrayList<ParseObject> shoppingLists;
    private SparseBooleanArray selectedItemIds;
    private boolean template;

    public ShoppingListsAdapter(Activity context,
                                ArrayList<String> name, ArrayList<String> date, ArrayList<ParseObject> listsItems, Boolean template) {
        super(context, R.layout.lists_list_item, name);

        selectedItemIds = new SparseBooleanArray();
        this.context = context;
        this.name = name;
        this.date = date;
        this.shoppingLists = listsItems;
        this.template = template;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lists_list_item, null, true);

        if( position < shoppingLists.size() ){
            TextView listName = rowView.findViewById(R.id.listNameItem);
            listName.setText(shoppingLists.get(position).getString("name"));
            TextView listDeadline = rowView.findViewById(R.id.listDeadline);
            listDeadline.setText(shoppingLists.get(position).getString("deadline"));
            TextView listFriends = rowView.findViewById(R.id.collaborators);
            TextView listDescription = rowView.findViewById(R.id.item_description);
            listDescription.setText(shoppingLists.get(position).getString("description"));
            RelativeLayout collaborators;
            collaborators = rowView.findViewById(R.id.collaborators_layout);
            if(ParseUser.getCurrentUser() == null){
                collaborators.setVisibility(View.INVISIBLE);
            }
            if(template==true)
                listDeadline.setText("-");


        }
        return rowView;
    }

    public void swapItems(ArrayList<ParseObject> newShoppingLists) {
        this.shoppingLists = newShoppingLists;
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedItemIds.get(position));

    }

    public void removeSelection() {
        selectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if(value) {
            selectedItemIds.put(position, value);
        } else {
            selectedItemIds.delete(position);
        }
        notifyDataSetChanged();
    }
    public SparseBooleanArray getSelectedIds() {
        return selectedItemIds;
    }


}