package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.ShoppingListDetailsAdapter;

public class ShoppingListDetailsActivity extends BaseNavigationActivity {
    private String listId;
    private String listName;
    private ParseObject list;
    private ListView product;
    private int id;
    public int selectedItemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = getIntent().getParcelableExtra("LIST_OBJECT");
        listName = list.getString("name");
        //   listId = getIntent().getStringExtra("LIST_ID");
        setTitle(listName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createListOfProducts();

        FloatingActionButton addProductB = findViewById(R.id.addProductButton);
        addProductB.setOnClickListener(view -> {
            addNewProduct();
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addNewProduct() {
        Intent intent = new Intent(getBaseContext(), AddProductToList.class);
        intent.putExtra("LIST_NAME", listName);
        intent.putExtra("LIST_OBJECT", list);
        intent.putExtra("LOCAL_ID", id);
        startActivity(intent);
    }

    private void createListOfProducts() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProductOfList");
        if (ParseUser.getCurrentUser() != null) {
            query.whereEqualTo("belongsTo", list.getString("localId"));
            if (!isNetworkAvailable()) {
                query.fromLocalDatastore();
            }
        } else {
            query.fromLocalDatastore();
            query.whereEqualTo("belongsTo", list.getString("localId"));
        }

        setIdForLocal();
        query.findInBackground((scoreList, exception) -> {
            if (exception == null) {
                ArrayList<ParseObject> products = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                for (ParseObject s : scoreList) {
                    products.add(s);
                    names.add(s.getString("name"));
                    Toast.makeText(ShoppingListDetailsActivity.this, "ilość produktów = " + scoreList.size(), Toast.LENGTH_SHORT).show();
                }

                ShoppingListDetailsAdapter productAdapter = new
                        ShoppingListDetailsAdapter(ShoppingListDetailsActivity.this, names, products);
                product = findViewById(R.id.list);
                product.setAdapter(productAdapter);
                product.setOnItemClickListener((adapterView, view, position, id) -> {
                    //Context context = ShoppingListDetailsActivity.this;
                    //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), AddProductToList.class);
                    intent.putExtra("PRODUCT_OBJECT", scoreList.get(position));
                    intent.putExtra("PRODUCT_OBJECT_ID", scoreList.get(position).getString("localId"));
                    intent.putExtra("LIST_NAME", listName);
                    intent.putExtra("LIST_OBJECT", list);

                    startActivity(intent);
                });
                multiChoiceForDelete(product, productAdapter, scoreList);
                Log.d("score", "Retrieved " + scoreList.size() + " scores");
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });
    }

    private void multiChoiceForDelete(ListView list, ShoppingListDetailsAdapter productAdapter, List<ParseObject> scoreList) {
        product.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        product.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                final int checkedCount = product.getCheckedItemCount();
                actionMode.setTitle(checkedCount + " Zaznaczono");
                selectedItemPos = i;
                productAdapter.toggleSelection(i);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                actionMode.getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        ArrayList<ParseObject> selecteditems = new ArrayList<ParseObject>();
                        changeSelectedIdsToObjects(productAdapter, selecteditems, scoreList);
                        deleteListAction(productAdapter, selecteditems, actionMode);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                productAdapter.removeSelection();
            }
        });
    }

    private void deleteListAction(ShoppingListDetailsAdapter productAdapter, ArrayList<ParseObject> selecteditems, ActionMode actionMode) {
        for (int i = 0; i < selecteditems.size(); i++) {
            if (selecteditems.get(i).getObjectId() == null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ProductOfList");
                query.fromLocalDatastore();
                query.whereEqualTo("localId", selecteditems.get(i).get("localId"));
                query.findInBackground((scoreList, exception) -> {
                    if (exception == null) {
                        for (ParseObject s : scoreList) {
                            s.unpinInBackground();
                            s.deleteEventually();
                        }
                    } else {

                    }
                });
            } else {
                selecteditems.get(i).deleteInBackground();
                selecteditems.get(i).unpinInBackground();
            }
        }
        actionMode.finish();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void changeSelectedIdsToObjects(ShoppingListDetailsAdapter productAdapter, ArrayList<ParseObject> selecteditems, List<ParseObject> scoreList) {
        SparseBooleanArray selected = productAdapter.getSelectedIds();
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                selecteditems.add(scoreList.get(selected.keyAt(i)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }

    public void setIdForLocal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("localId");
        query.fromLocalDatastore();
        query.findInBackground((scoreList, exception) -> {
            if (exception == null) {
                for (ParseObject s : scoreList) {
                    id = s.getInt("id");
                    s.put("id", id + 1);
                    s.pinInBackground();

                }
                Log.d("score", "Retrieved " + scoreList.size() + " scores");
                if (scoreList.size() == 0) {
                    ParseObject localId = new ParseObject("localId");
                    localId.put("id", 1);
                    localId.pinInBackground();
                    id = 1;
                }
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return findViewById(R.id.drawer_layout);
    }

    @Override
    protected NavigationView getNavigationView() {
        return findViewById(R.id.nav_view);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
