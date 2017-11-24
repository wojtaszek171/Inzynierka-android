package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.util.customproductlist.CustomProductDataModel;
import pl.pollub.shoppinglist.util.customproductlist.CustomProductsAdapter;


/**
 * @author Jakub
 * @since 2017-10-24
 */
public class CustomProductsListActivity extends BaseNavigationActivity {

    private ListView list;
    private CustomProductsAdapter adapter;

    private List<ParseObject> allCustomProducts = null;
    private ArrayList<CustomProductDataModel> dataModels;

    private int selectedItemPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_products_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PRODUKTY UŻYTKOWNIKA");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.custom_products_list_view);
        getAllCustomProducts();
    }

    ////////////////////////////////////////////
    public void getAllCustomProducts() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomProduct");
        query.fromLocalDatastore();
        query.findInBackground((resultList, e) -> {
            if (e == null) {
                allCustomProducts = resultList;
                dataModels = convertAllParseObjects(resultList);
                adapter = new CustomProductsAdapter(dataModels, CustomProductsListActivity.this);
                list.setAdapter(adapter);


                list.setOnItemClickListener((adapterView, view, position, id) -> {
                    ParseObject productToEdit = allCustomProducts.get(position);

                    Intent editProductIntent = new Intent(CustomProductsListActivity.this, CustomProductAppenderActivity.class);
                    editProductIntent.putExtra("EDIT_MODE_ENABLED", true);
                    editProductIntent.putExtra("PRODUCT_TO_EDIT", productToEdit);
                    startActivity(editProductIntent);
                });
                //TODO do dodania multiListener:
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                        final int checkedCount = list.getCheckedItemCount();
                        actionMode.setTitle(checkedCount + " Zaznaczono");
                        selectedItemPos = i;
                        adapter.toggleSelection(i);
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
                                ArrayList<ParseObject> selecteditems = new ArrayList<>();
                                SparseBooleanArray selected = adapter.getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        selecteditems.add(allCustomProducts.get(selected.keyAt(i)));
                                    }
                                }
                                for (int i = 0; i < selecteditems.size(); i++) {
                                    if (selecteditems.get(i).getObjectId() == null) {
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomProduct");
                                        query.fromLocalDatastore();
                                        query.whereEqualTo("localId", selecteditems.get(i).get("localId"));
                                        query.findInBackground((resultList, exception) -> {
                                            if (exception == null) {
                                                for (ParseObject result : resultList) {
                                                    result.unpinInBackground();
                                                    result.deleteEventually();
                                                }
                                            } else {
                                                //TODO: exception handling
                                            }
                                        });
                                    } else {
                                        selecteditems.get(i).deleteEventually();
                                        selecteditems.get(i).unpinInBackground();
                                    }

                                }
                                actionMode.finish();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);

                                refreshListView();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {
                        adapter.removeSelection();
                    }
                });

            } else {
                Log.d("score", "Error: " + e.getMessage());
            }
        });

    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    public void goToCustomProductCreation(View view) {
        Intent intent = new Intent(CustomProductsListActivity.this, CustomProductAppenderActivity.class);
        startActivity(intent);
    }

    public CustomProductDataModel convertParseObject(ParseObject parseObject) {
        String name = parseObject.getString("name");
        String category = parseObject.getString("category");
        String description = parseObject.getString("description");

        return new CustomProductDataModel(name, category, description);
    }

    public ArrayList<CustomProductDataModel> convertAllParseObjects(List<ParseObject> parseObjectList) {
        ArrayList<CustomProductDataModel> resultList = new ArrayList<>();
        for (ParseObject parseObject : parseObjectList) {
            resultList.add(convertParseObject(parseObject));
        }
        return resultList;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return findViewById(R.id.custom_products_list_drawer_layout);
    }

    @Override
    protected NavigationView getNavigationView() {
        return findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulistswithfriends, menu);
        return true;
    }
}
