package pl.pollub.shoppinglist.adapter;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.ShoppingListDetailsActivity;

import static pl.pollub.shoppinglist.util.MiscUtils.isNetworkAvailable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShoppingListDetailsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<HashMap> products;
    private CheckBox checkBoxStatus;
    private SparseBooleanArray selectedItemIds;
    private String[] arrayIcons;
    private String[] arrayCategories;
    private ParseObject list;


    public ShoppingListDetailsAdapter(Activity context, ArrayList<String> name,
                                      ArrayList<HashMap> products, ParseObject shoppingList) {
        super(context, R.layout.item_shopping_list, name);
        selectedItemIds = new SparseBooleanArray();
        this.context = context;
        this.products = products;
        this.list = shoppingList;
        arrayIcons = context.getResources().getStringArray(R.array.product_icons);
        arrayCategories = context.getResources().getStringArray(R.array.product_categories);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_product, null, true);

        if (position < products.size()) {
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
            checkBoxStatus = rowView.findViewById(R.id.checkBoxStatus);
            if (product.get("status").toString().equals("1") && !checkBoxStatus.isChecked()) {
                checkBoxStatus.setChecked(true);
            } else if (checkBoxStatus.isChecked()) {
                checkBoxStatus.setChecked(false);
            }
            String nameOfFile = (product.get("image") != null) ? product.get("image").toString() : "other";
            int idd = context.getResources().getIdentifier(nameOfFile, "drawable", context.getPackageName());
            image.setBackgroundResource(idd);

            for (int i = 0; i < selectedItemIds.size(); i++) {
                if (position == selectedItemIds.keyAt(i)) {
                    RelativeLayout itemRelative = rowView.findViewById(R.id.relativListItem);
                    itemRelative.setBackground(getContext().getResources().getDrawable(R.drawable.shape_item_grey));
                }
            }
        }

        checkBoxStatus.setOnCheckedChangeListener((buttonView, checked) -> {
            if (checked) {
                updateStatus(position, 1);
                //Toast.makeText(getContext(), "Zaznaczono "+position, Toast.LENGTH_SHORT).show();
            } else {
                updateStatus(position, 0);
                //Toast.makeText(getContext(), "Odznaczono "+position, Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }

    private void updateStatus(int position, int status) {
        ArrayList<HashMap> nestedProducts = (ArrayList) list.get("nestedProducts");
        nestedProducts.get(position).put("status", status);
        if (ParseUser.getCurrentUser() != null && isNetworkAvailable(getContext())) {
            list.put("nestedProducts", nestedProducts);
            list.saveEventually();
            list.pinInBackground();
        } else {
            ParseQuery<ParseObject> offlineListToUpdateQuery = ParseQuery.getQuery("ShoppingList");
            offlineListToUpdateQuery.whereEqualTo("localId", list.getString("localId"));
            offlineListToUpdateQuery.fromLocalDatastore();
            offlineListToUpdateQuery.findInBackground((resultList, e) -> {
                if (e == null) {
                    ParseObject listToUpdate = resultList.get(0);
                    listToUpdate.put("nestedProducts", nestedProducts);
                    listToUpdate.pinInBackground();
                    listToUpdate.saveEventually();
                }
            });
        }
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedItemIds.get(position));
    }

    public void removeSelection() {
        selectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<HashMap> newProducts, String sort) {
        this.products = newProducts;
        ShoppingListDetailsActivity.sortProducts(products, sort);
        notifyDataSetChanged();
        context.recreate();
    }

    public void selectView(int position, boolean value) {
        if (value) {
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