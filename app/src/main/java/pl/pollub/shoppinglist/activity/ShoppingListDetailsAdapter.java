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
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import pl.pollub.shoppinglist.R;

@Data
public class ShoppingListDetailsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<HashMap> products;
    private SparseBooleanArray mSelectedItemsIds;


    public ShoppingListDetailsAdapter(Activity context,
                                      ArrayList<String> name, ArrayList<HashMap> products) {
        super(context, R.layout.lists_list_item, name);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.products = products;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        HashMap product = products.get(position);
        View rowView = inflater.inflate(R.layout.lists_items_item, null, true);
        TextView productName = rowView.findViewById(R.id.itemName);
        productName.setText(product.get("name").toString());
        TextView productAmount = rowView.findViewById(R.id.item_amount);
        productAmount.setText(product.get("amount").toString());
        TextView productMeasure = rowView.findViewById(R.id.item_measure);
        productMeasure.setText(product.get("measure").toString());

        return rowView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<HashMap> newProducts) {
        this.products = newProducts;
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