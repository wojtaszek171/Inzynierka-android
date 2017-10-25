package pl.pollub.shoppinglist.activity;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pl.pollub.shoppinglist.R;

public class ShoppingListsAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> date;
    public ShoppingListsAdapter(Activity context,
                                ArrayList<String> name, ArrayList<String> date) {
        super(context, R.layout.lists_list_item, name);
        this.context = context;
        this.name = name;
        this.date = date;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.lists_list_item , null, true);
        TextView listName = (TextView) rowView.findViewById(R.id.listNameItem);
        listName.setText(name.get(position));
        TextView listDeadline = (TextView) rowView.findViewById(R.id.listDeadline);
        listDeadline.setText(date.get(position));
        return rowView;
    }

}