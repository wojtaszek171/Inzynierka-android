package pl.pollub.shoppinglist.util.customproductlist;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.pollub.shoppinglist.R;

/**
 * @author Jakub
 * @since 2017-10-25
 */
public class CustomProductsAdapter extends ArrayAdapter implements View.OnClickListener {
    private ArrayList<CustomProductDataModel> dataSet;
    private SparseBooleanArray selectedItemsIds;

    Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView nameField;
        TextView categoryField;
    }

    public CustomProductsAdapter(ArrayList<CustomProductDataModel> data, Context context) {
        super(context, R.layout.item_product_custom, data);
        this.dataSet = data;
        this.context = context;
        this.selectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomProductDataModel dataModel = (CustomProductDataModel) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_product_custom, parent, false);
            viewHolder.nameField = convertView.findViewById(R.id.custom_product_item_name);
            viewHolder.categoryField = convertView.findViewById(R.id.custom_product_item_category);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameField.setText(dataModel.getName());
        viewHolder.categoryField.setText(dataModel.getCategory());
        // Return the completed view to render on screen
        return convertView;
    }


    //////////////////////////////////////////////////////////////////////////////////


    public void toggleSelection(int position) {
        selectView(position, !selectedItemsIds.get(position));

    }

    public void removeSelection() {
        selectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value) {
            selectedItemsIds.put(position, value);
        } else {
            selectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedItemsIds;
    }
}
