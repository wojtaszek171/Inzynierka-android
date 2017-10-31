package pl.pollub.shoppinglist.activity;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

import pl.pollub.shoppinglist.R;

public class ShoppingListDetailsAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private ArrayList<ParseObject> products;
    public ShoppingListDetailsAdapter(Activity context,
                                      ArrayList<String> name, ArrayList<ParseObject> products) {
        super(context, R.layout.lists_list_item, name);
        this.context = context;
        this.products = products;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ParseObject product = products.get(position);
        View rowView= inflater.inflate(R.layout.lists_items_item , null, true);
        TextView productName = (TextView) rowView.findViewById(R.id.itemName);
        productName.setText(product.getString("name"));
        TextView productAmount = (TextView) rowView.findViewById(R.id.item_amount);
        productAmount.setText(product.getString("amount"));
        TextView productMeasure = (TextView) rowView.findViewById(R.id.item_measure);
        productMeasure.setText(product.getString("measure"));
        
        return rowView;
    }

}