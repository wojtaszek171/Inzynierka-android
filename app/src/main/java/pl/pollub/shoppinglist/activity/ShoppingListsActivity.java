package pl.pollub.shoppinglist.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SubscriptionHandling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import at.markushi.ui.CircleButton;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.ShoppingListsAdapter;
import pl.pollub.shoppinglist.model.User;

import static pl.pollub.shoppinglist.util.MiscUtils.isNetworkAvailable;

public class ShoppingListsActivity extends BaseNavigationActivity {

    private final Context context = this;

    private ListView list;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<ParseObject> listsItems = new ArrayList<>();
    private ShoppingListsAdapter listAdapter;
    private ListView listView = null;
    private String sort = "";
    private final User currentUser = User.getCurrentUser();

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

        setTitle(R.string.my_lists);
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
                        Intent intent = new Intent(ShoppingListsActivity.this, AddShoppingListActivity.class);
                        intent.putExtra("LOCAL_LIST_ID", id);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.useTemplate:
                        listView = new ListView(getApplicationContext());
                        getAllTemplates();
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(ShoppingListsActivity.this);

                        builder.setCancelable(true).setView(listView);
                        builder.create().show();
                }

                return true;
            }

            @Override
            public void onMenuClosed() {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_sort), Gravity.CENTER);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_sort_list, popup.getMenu());
                popup.setOnMenuItemClickListener(item1 -> {
                    switch (item1.getItemId()) {
                        case R.id.nameSort:
                            setSortForLists("name");
                            return true;
                        case R.id.dateSort:
                            setSortForLists("date");
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSortForLists(String sortMethod) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SortLists");
        query.fromLocalDatastore();
        query.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() > 0) {
                    objects.get(0).put("sortBy", sortMethod);
                    objects.get(0).pinInBackground();
                } else {
                    ParseObject sortProduct = new ParseObject("SortLists");
                    sortProduct.put("sortBy", sortMethod);
                    sortProduct.pinInBackground();
                }
                listAdapter.swapItems(listsItems, sort);
            }
        });
    }

    private void getAllTemplates() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");

        if (currentUser != null) {
            query.whereEqualTo("belongsTo", currentUser.getUsername());
            if (!isNetworkAvailable(this)) {
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
                R.layout.item_list_string, R.id.txtitem, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            finish();
            CreateListFromTemplate(resultList, position);
        });
    }

    private void CreateListFromTemplate(List<ParseObject> scoreList, int position) {
        Intent intent = new Intent(getApplicationContext(), AddShoppingListActivity.class);
        intent.putExtra("LIST_TEMPLATE", scoreList.get(position));
        intent.putExtra("LOCAL_LIST_ID", id);
        finish();
        startActivity(intent);
    }

    private void displayListsAndSetActions() {
        ParseQuery<ParseObject> querysort = ParseQuery.getQuery("SortLists");
        querysort.fromLocalDatastore();
        querysort.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() != 0)
                    sort = objects.get(0).getString("sortBy");

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.whereEqualTo("isTemplate", false);

                if (currentUser != null) {
                    query.whereEqualTo("sharedAmong", currentUser.getUsername());

                    if (!isNetworkAvailable(this)) {
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
                        if (isNetworkAvailable(this)) {
                            updateLocalStorageWith(resultList);
                        }
                        Log.d("listsQuerySuccess", "Retrieved " + resultList.size() + " lists.");
                    } else {
                        Log.d("listsQueryError", "Error: " + exception.getMessage());
                    }
                });
            }
        });
    }


    public static void sortLists(ArrayList<ParseObject> lists, String sort) {
        switch (sort) {
            case "name":
                Collections.sort(lists, (o1, o2) -> {
                    String firstValue = o1.get("name").toString().toLowerCase();
                    String secondValue = o2.get("name").toString().toLowerCase();
                    return firstValue.compareTo(secondValue);
                });
                ArrayList<ParseObject> neww = lists;
                break;
            case "date":
                Collections.sort(lists, (o1, o2) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    Date firstValue = new Date(500);
                    Date secondValue = new Date(500);
                    try {
                        if (sdf.parse(o1.getString("deadline")) == null || sdf.parse(o2.getString("deadline")) == null)
                            return 0;
                        firstValue = sdf.parse(o1.getString("deadline"));
                        secondValue = sdf.parse(o2.getString("deadline"));
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    return firstValue.compareTo(secondValue);
                });
                break;
            default:
                break;
        }
    }

    private void updateLocalStorageWith(List<ParseObject> listsFromServer) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.fromLocalDatastore();
        query.findInBackground((resultList, exception) -> {
            if (exception == null) {
                ParseObject.unpinAllInBackground(resultList);
                try {
                    ParseObject.pinAll(listsFromServer);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateShoppingListsAdapterWithQueryResult(ArrayList<ParseObject> resultList) {
        runOnUiThread(() -> {
            if(listAdapter != null){
                listAdapter.swapItems(resultList, sort);
            }
        });
    }

    private ParseQuery getMyShoppingListsQuery() {
        ParseQuery<ParseObject> updateShoppingListsQuery = ParseQuery.getQuery("ShoppingList");
        updateShoppingListsQuery.whereEqualTo("isTemplate", false);

        if (currentUser != null) {
            updateShoppingListsQuery.whereEqualTo("sharedAmong", currentUser.getUsername());
        }
        setIdForLocal();
        return updateShoppingListsQuery;
    }

    private void subscribeToShoppingListsQuery() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery updateShoppingListsQuery = getMyShoppingListsQuery();

        SubscriptionHandling<ParseObject> certainListQuerysubscriptionHandling
                = parseLiveQueryClient.subscribe(updateShoppingListsQuery);
        certainListQuerysubscriptionHandling.handleEvents((query, event, object) -> {
            query.findInBackground((resultList, e) -> {
                if (e == null) {
                    updateShoppingListsAdapterWithQueryResult((ArrayList) resultList);
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
        sortLists(listsItems, sort);
        listAdapter = new
                ShoppingListsAdapter(ShoppingListsActivity.this, names, dates, listsItems, false);
        list = findViewById(R.id.list);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            goToListDetails(listsItems, position);
        });
        list.setLongClickable(true);
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(new ContextThemeWrapper(ShoppingListsActivity.this, R.style.NewDialog));
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            builder.setCancelable(true).setView(inflater.inflate(R.layout.menu_context_list, null));

            Dialog dialog1 = builder.create();
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.getWindow().setDimAmount(0);
            dialog1.show();
            CircleButton selectMany = dialog1.findViewById(R.id.select_many);
            selectMany.setOnClickListener(v -> {
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                multiChoiceForDelete(list, listAdapter, resultList);
                list.setItemChecked(position, true);
                dialog1.dismiss();
            });
            CircleButton deleteList = dialog1.findViewById(R.id.delete);
            deleteList.setOnClickListener(v -> {
                ArrayList<ParseObject> selItem = new ArrayList<>();
                selItem.add(resultList.get(position));
                deleteListAction(listAdapter, selItem);
            });
            CircleButton editList = dialog1.findViewById(R.id.edit_list);
            editList.setOnClickListener(v -> {
                dialog1.dismiss();
                Intent intent = new Intent(getApplicationContext(), AddShoppingListActivity.class);
                intent.putExtra("LIST_OBJECT", resultList.get(position));
                startActivity(intent);
            });
            return true;
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
            if (currentUser == null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.fromLocalDatastore();
                String ident = selecteditems.get(i).getString("localId");
                query.whereEqualTo("localId", selecteditems.get(i).get("localId"));
                query.findInBackground((scoreList, exception) -> {
                    if (exception == null) {
                        for (ParseObject s : scoreList) {
                            s.unpinInBackground(e -> {
                                if (e == null) {
                                    Intent intent = new Intent(getApplicationContext(), ShoppingListsActivity.class);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
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
                        Intent intent = new Intent(getApplicationContext(), ShoppingListsActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
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
        // inflate the menu - this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        MenuItem itemToHide = menu.findItem(R.id.action_share_list);
        if (itemToHide != null) {
            itemToHide.setVisible(false);
        }

        for (int itemId : Collections.singletonList(R.id.action_sort)) {
            MenuItem menuItem = menu.findItem(itemId);
            if (menuItem == null) {
                continue;
            }

            Drawable menuItemIcon = DrawableCompat.wrap(menuItem.getIcon());
            DrawableCompat.setTint(menuItemIcon, Color.WHITE);
            menuItem.setIcon(menuItemIcon);
        }

        return true;
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
