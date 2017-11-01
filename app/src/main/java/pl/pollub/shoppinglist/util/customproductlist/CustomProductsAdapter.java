package pl.pollub.shoppinglist.util.customproductlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.pollub.shoppinglist.R;

/**
 * Created by jrwoj on 25.10.2017.
 */

public class CustomProductsAdapter extends ArrayAdapter implements View.OnClickListener {
    private ArrayList<CustomProductDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView nameField;
        TextView categoryField;
        TextView descriptionField;
        TextView measureField;
        TextView pricePerUnitField;
    }

    public CustomProductsAdapter(ArrayList<CustomProductDataModel> data, Context context) {
        super(context, R.layout.custom_products_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        CustomProductDataModel CustomProductDataModel=(CustomProductDataModel)object;
//
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +CustomProductDataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomProductDataModel dataModel = (CustomProductDataModel) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_products_list_item, parent, false);
            viewHolder.nameField = (TextView) convertView.findViewById(R.id.custom_product_item_name);
            viewHolder.categoryField = (TextView) convertView.findViewById(R.id.custom_product_item_category);
//            viewHolder.descriptionField = (TextView) convertView.findViewById(R.id.desc);
//            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;

        viewHolder.nameField.setText(dataModel.getName());
        viewHolder.categoryField.setText(dataModel.getCategory());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}
