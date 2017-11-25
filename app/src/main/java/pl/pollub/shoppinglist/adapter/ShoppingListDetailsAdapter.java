package pl.pollub.shoppinglist.adapter;


import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    private SparseBooleanArray selectedItemIds;
    private String[] arrayIcons;
    private String[] arrayCategories;



    public ShoppingListDetailsAdapter(Activity context,
                                      ArrayList<String> name, ArrayList<HashMap> products) {
        super(context, R.layout.lists_list_item, name);
        selectedItemIds = new SparseBooleanArray();
        this.context = context;
        this.products = products;
        arrayIcons = context.getResources().getStringArray(R.array.product_icons);
        arrayCategories = context.getResources().getStringArray(R.array.product_categories);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lists_items_item, null, true);

        if(position < products.size()){
            HashMap product = products.get(position);
            TextView productName = rowView.findViewById(R.id.itemName);
            productName.setText(product.get("name").toString());
            TextView productAmount = rowView.findViewById(R.id.item_amount);
            productAmount.setText(product.get("amount").toString());
            TextView productMeasure = rowView.findViewById(R.id.item_measure);
            productMeasure.setText(product.get("measure").toString());
            ImageView image = rowView.findViewById(R.id.itemImage);
            TextView productDescription = rowView.findViewById(R.id.product_description);
            productDescription.setText(product.get("description").toString());

            String nameOfFile = (product.get("image") != null) ? product.get("image").toString() : "other";
            int idd = context.getResources().getIdentifier(nameOfFile, "drawable", context.getPackageName());

            image.setBackgroundResource(idd);
        }

        return rowView;
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedItemIds.get(position));

    }

    public void removeSelection() {
        selectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<HashMap> newProducts) {
        this.products = newProducts;
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