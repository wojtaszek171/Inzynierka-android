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
import java.util.HashMap;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.ShoppingListsActivity;

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

        if (position < shoppingLists.size()) {
            TextView listName = rowView.findViewById(R.id.listNameItem);
            listName.setText(shoppingLists.get(position).getString("name"));
            TextView listDeadline = rowView.findViewById(R.id.listDeadline);
            listDeadline.setText(shoppingLists.get(position).getString("deadline"));
            TextView listDescription = rowView.findViewById(R.id.item_description);
            listDescription.setText(shoppingLists.get(position).getString("description"));
            TextView listProgress = rowView.findViewById(R.id.list_progress);
            setProgress(position, listProgress, shoppingLists);
            RelativeLayout collaborators;
            collaborators = rowView.findViewById(R.id.collaborators_layout);
            if (ParseUser.getCurrentUser() == null) {
                collaborators.setVisibility(View.INVISIBLE);
            } else {
                setCollaborators(rowView, position);
            }
            if (template == true)
                listDeadline.setText("-");
            for (int i = 0; i < selectedItemIds.size(); i++) {
                if (position == selectedItemIds.keyAt(i)) {
                    RelativeLayout itemRelative = (RelativeLayout) rowView.findViewById(R.id.relativList);
                    itemRelative.setBackground(getContext().getResources().getDrawable(R.drawable.shape_item_grey));
                }
            }


        }
        return rowView;
    }

    private void setProgress(int position, TextView listProgress, ArrayList<ParseObject> shoppingLists) {
        ArrayList<HashMap> nestedProducts = (ArrayList) shoppingLists.get(position).get("nestedProducts");
        int i = 0;
        if (nestedProducts != null) {
            for (int j = 0; j < nestedProducts.size(); j++) {
                if (nestedProducts.get(j).get("status").toString().equals("1")) {
                    i++;
                }
            }
            listProgress.setText(i + "/" + nestedProducts.size());
        }
    }

    private void setCollaborators(View rowView, int position) {
        String currentUserName = ParseUser.getCurrentUser().getUsername();

        StringBuilder usernamesBuilder = new StringBuilder();
        List<String> sharedAmong = shoppingLists.get(position).getList("sharedAmong");
        List<String> listCollaboratorsUsernames = new ArrayList<>();

        for (String friendUsername : sharedAmong) {
            if (!friendUsername.equals(currentUserName)) {
                listCollaboratorsUsernames.add(friendUsername);
            }
        }

        for (int i = 0; i < listCollaboratorsUsernames.size(); i++) {
            usernamesBuilder.append(listCollaboratorsUsernames.get(i));
            if (i != listCollaboratorsUsernames.size() - 1) {
                usernamesBuilder.append(", ");
            }
        }

        TextView collaboratorsTextView = rowView.findViewById(R.id.collaborators);
        collaboratorsTextView.setText(usernamesBuilder.toString());
    }

    public void swapItems(ArrayList<ParseObject> newShoppingLists, String sort) {
        this.shoppingLists = newShoppingLists;
        ShoppingListsActivity.sortLists(shoppingLists, sort);
        notifyDataSetChanged();
        context.recreate();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedItemIds.get(position));

    }

    public void removeSelection() {
        selectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
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