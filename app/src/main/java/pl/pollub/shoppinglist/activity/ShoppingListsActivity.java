package pl.pollub.shoppinglist.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.ShoppingListsAdapter;

public class ShoppingListsActivity extends BaseNavigationActivity {

    private final Context context = this;

    private ListView list;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<ParseObject> listsItems = new ArrayList<>();
    private ShoppingListsAdapter listAdapter;
    private ListView listView = null;

    private FloatingActionButton addNew;
    private Toolbar toolbar;

    private static int id;
    public int selectedItemPos;

    private ParseLiveQueryClient parseLiveQueryClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);

        initLayoutElements();

        subscribeToShoppingListsQuery();

        setTitle(R.string.menuLists);
        setSupportActionBar(toolbar);

        displayListsAndSetActions();
        addNewShoppingListDialog();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLayoutElements() {
        addNew = findViewById(R.id.addListButton);
        toolbar = findViewById(R.id.toolbar);
    }

    private void addNewShoppingListDialog() {
        FabSpeedDial fabSpeedDial = findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId()) {
                    case R.id.addList:
                        Intent intent = new Intent(ShoppingListsActivity.this, AddShoppingList.class);
                        intent.putExtra("LOCAL_LIST_ID", id);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.useTemplate:
                        listView = new ListView(getApplicationContext());
                        getAllTemplates();
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(ShoppingListsActivity.this);

                        builder.setCancelable(true);

                        builder.setView(listView);

                        AlertDialog dialog1 = builder.create();

                        dialog1.show();
                }

                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    private void getAllTemplates() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");

        if (ParseUser.getCurrentUser() != null) {
            query.whereEqualTo("belongsTo", ParseUser.getCurrentUser().getUsername());
            if (!isNetworkAvailable()) {
                query.fromLocalDatastore();
            }
        } else {
            query.fromLocalDatastore();
            query.whereEqualTo("belongsTo", null);
        }
        query.whereEqualTo("isTemplate", true);
        setIdForLocal();
        query.findInBackground((resultList, exception) -> {

            if (exception == null) {
                prepareTemplatesAdapterFromQueryResult(resultList);
                Log.d("templatesQuerySuccess", "Retrieved " + resultList.size() + " templates.");
            } else {
                Log.d("templatesQueryError", "Error: " + exception.getMessage());
            }
        });
    }

    private void prepareTemplatesAdapterFromQueryResult(List<ParseObject> resultList) {
        ArrayList<String> items = new ArrayList<>();

        for (ParseObject s : resultList) {
            items.add(s.getString("name"));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.string_list_item, R.id.txtitem, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            finish();
            CreateListFromTemplate(resultList, position);
        });
    }

    private void CreateListFromTemplate(List<ParseObject> scoreList, int position) {
        Intent intent = new Intent(getApplicationContext(), AddShoppingList.class);
        intent.putExtra("LIST_TEMPLATE", scoreList.get(position));
        intent.putExtra("LOCAL_LIST_ID", id);
        finish();
        startActivity(intent);
    }

    private void displayListsAndSetActions() {
        ParseUser currentlyLoggedUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.whereEqualTo("isTemplate", false);

        if (currentlyLoggedUser != null) {
            query.whereEqualTo("sharedAmong", currentlyLoggedUser.getUsername());

            if (!isNetworkAvailable()) {
                query.fromLocalDatastore();
            }
        } else {
            query.fromLocalDatastore();
            query.whereEqualTo("belongsTo", null);
        }
        setIdForLocal();


        query.findInBackground((resultList, exception) -> {
            if (exception == null) {
                prepareShoppingListsAdapterFromQueryResult(resultList);
                Log.d("listsQuerySuccess", "Retrieved " + resultList.size() + " lists.");
            } else {
                Log.d("listsQueryError", "Error: " + exception.getMessage());
            }
        });
    }

    private void updateShoppingListsAdapterWithQueryResult(ArrayList<ParseObject> resultList){
        runOnUiThread(() -> listAdapter.swapItems(resultList));
    }

    private ParseQuery getMyShoppingListsQuery(){
        ParseUser currentlyLoggedUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> updateShoppingListsQuery = ParseQuery.getQuery("ShoppingList");
        updateShoppingListsQuery.whereEqualTo("isTemplate",false);

        if (currentlyLoggedUser != null) {
            updateShoppingListsQuery.whereEqualTo("sharedAmong",currentlyLoggedUser.getUsername());
        }
        setIdForLocal();
        return updateShoppingListsQuery;
    }


    private void subscribeToShoppingListsQuery(){

        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery updateShoppingListsQuery = getMyShoppingListsQuery();

        SubscriptionHandling<ParseObject> certainListQuerysubscriptionHandling
                = parseLiveQueryClient.subscribe(updateShoppingListsQuery);
        certainListQuerysubscriptionHandling.handleEvents((query, event, object) -> {
            query.findInBackground((resultList, e) -> {
                if (e == null) {
                    updateShoppingListsAdapterWithQueryResult((ArrayList)resultList);
                    Log.d("syncListsSuccess", "Retrieved " + resultList.size() + " lists.");
                } else {
                    Log.d("syncListsError", "Error: " + e.getMessage());
                }
            });
        });

    }

    private void prepareShoppingListsAdapterFromQueryResult(List<ParseObject> resultList) {
        for (ParseObject s : resultList) {
            names.add(s.getString("name"));
            dates.add(s.getString("deadline"));
            listsItems.add(s);
        }
        listAdapter = new
                ShoppingListsAdapter(ShoppingListsActivity.this, names, dates, listsItems, false);
        list = findViewById(R.id.list);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            goToListDetails(resultList, position);
        });
        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new
                        AlertDialog.Builder(ShoppingListsActivity.this);
                LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                builder.setCancelable(true);

                builder.setView(inflater.inflate(R.layout.lists_dialog,null));

                Dialog dialog1 = builder.create();
                dialog1.show();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView selectMany = dialog1.findViewById(R.id.select_many);
                selectMany.setOnClickListener(v -> {
                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    multiChoiceForDelete(list, listAdapter, resultList);
                    list.setItemChecked(position, true);
                    dialog1.dismiss();
                });
                TextView deleteList = dialog1.findViewById(R.id.delete);
                deleteList.setOnClickListener(v -> {
                    ArrayList<ParseObject> selItem = new ArrayList<>();
                    selItem.add(resultList.get(position));
                    deleteListAction(listAdapter, selItem);
                });
                TextView editList = dialog1.findViewById(R.id.edit_list);
                editList.setOnClickListener(v -> {
                    dialog1.dismiss();
                    Intent intent = new Intent(getApplicationContext(),AddShoppingList.class);
                    intent.putExtra("LIST_OBJECT",resultList.get(position));
                    startActivity(intent);
                });
                return true;
            }
        });
    }

    private void multiChoiceForDelete(ListView list, ShoppingListsAdapter listAdapter, List<ParseObject> scoreList) {
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                final int checkedCount = list.getCheckedItemCount();
                actionMode.setTitle(checkedCount + " Zaznaczono");
                selectedItemPos = i;
                listAdapter.toggleSelection(i);
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
                        changeSelectedIdsToObjects(listAdapter, selecteditems, scoreList);
                        deleteListAction(listAdapter, selecteditems);
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                listAdapter.removeSelection();
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        });
    }

    private void changeSelectedIdsToObjects(ShoppingListsAdapter listAdapter, ArrayList<ParseObject> selecteditems, List<ParseObject> scoreList) {
        SparseBooleanArray selected = listAdapter.getSelectedIds();
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                selecteditems.add(scoreList.get(selected.keyAt(i)));
            }
        }
    }

    private void deleteListAction(ShoppingListsAdapter listAdapter, ArrayList<ParseObject> selecteditems) {
        for (int i = 0; i < selecteditems.size(); i++) {
            if (ParseUser.getCurrentUser() == null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.fromLocalDatastore();
                String ident = selecteditems.get(i).getString("localId");
                query.whereEqualTo("localId", selecteditems.get(i).get("localId"));
                query.findInBackground((scoreList, exception) -> {
                    if (exception == null) {
                        for (ParseObject s : scoreList) {
                            s.unpinInBackground(e -> {
                                if (e == null) {
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }
                            });
                            s.deleteEventually();
                        }
                    } else {

                    }
                });
            } else {
                selecteditems.get(i).deleteEventually();
                selecteditems.get(i).unpinInBackground(e -> {
                    if (e == null) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
            }

        }

    }

    private void goToListDetails(List<ParseObject> scoreList, int position) {
        Context context = ShoppingListsActivity.this;
        String idList = scoreList.get(position).getString("objectId");
        String nameList = scoreList.get(position).getString("name");
        Intent intent = new Intent(getBaseContext(), ShoppingListDetailsActivity.class);
        intent.putExtra("LIST_ID", idList);
        intent.putExtra("LIST_NAME", nameList);
        intent.putExtra("LIST_OBJECT", scoreList.get(position));
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
