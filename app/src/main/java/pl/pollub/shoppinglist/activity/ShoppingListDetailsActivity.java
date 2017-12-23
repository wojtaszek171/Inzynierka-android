package pl.pollub.shoppinglist.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.ShoppingListDetailsAdapter;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;
import pl.pollub.shoppinglist.util.MiscUtils;

public class ShoppingListDetailsActivity extends BaseNavigationActivity {
    //    private String listId;
    private String listName;
    private ParseObject list;
    private String sort = "";

    private ListView productListView;
    private ShoppingListDetailsAdapter productAdapter;

    private int id;
    public int selectedItemPos;

    ParseLiveQueryClient parseLiveQueryClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getIntent().setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        setContentView(R.layout.activity_shopping_list_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = getIntent().getParcelableExtra("LIST_OBJECT");
        listName = list.getString("name");
        setupLiveQueryProductsSubscriptions();

        setTitle(listName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createListOfProducts();

        FloatingActionButton addProductB = findViewById(R.id.addProductButton);
        addProductB.bringToFront();
        addProductB.setOnClickListener(view -> {
            addNewProduct();
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addNewProduct() {
        Intent intent = new Intent(getBaseContext(), AddProductToListActivity.class);
        intent.putExtra("LIST_NAME", listName);
        intent.putExtra("LIST_OBJECT", list);
        intent.putExtra("LOCAL_ID", id);
        startActivityForResult(intent, 0);
    }

    private void createListOfProducts() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SortProducts");
        query.fromLocalDatastore();
        query.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() != 0)
                    sort = objects.get(0).getString("sortBy");
                prepareNestedProductsAdapter();
            }
        });

    }

    private void setupLiveQueryProductsSubscriptions() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ParseObject> certainListQuery = ParseQuery.getQuery("ShoppingList");
        certainListQuery.whereEqualTo("localId", list.getString("localId"));
        SubscriptionHandling<ParseObject> certainListQuerysubscriptionHandling
                = parseLiveQueryClient.subscribe(certainListQuery);
        certainListQuerysubscriptionHandling.handleEvents((query, event, object) -> {
            // HANDLING all events
            updateNestedProductsAdapter();
            //displayLiveQueryUpdateToast(event.name(), "someUser", (HashMap) object.get("estimatedData"));
        });
    }

    private void updateNestedProductsAdapter() {
        if (productAdapter != null) {
            runOnUiThread(() -> {
                productAdapter.swapItems(getNestedProducts(), sort);
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
            });
        }
    }

    private void displayLiveQueryUpdateToast(String eventName, String modificationAuthorLogin, HashMap recentData) {
        Context context = getApplicationContext();
        CharSequence text = eventName + ": made by " + modificationAuthorLogin;
        int duration = Toast.LENGTH_SHORT;

        runOnUiThread(() -> Toast.makeText(context, text, duration).show());
    }

    private void prepareNestedProductsAdapter() {
        ArrayList<HashMap> nestedProducts = getNestedProducts();
        productListView = findViewById(R.id.list);


        if (nestedProducts != null && nestedProducts.size() > 0) {
            ArrayList<String> nestedProductsNames = getNestedProductNames();

            productAdapter = new
                    ShoppingListDetailsAdapter(ShoppingListDetailsActivity.this, nestedProductsNames, nestedProducts, list);
            productListView.setAdapter(null);
            productListView.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();

            productListView.setOnItemClickListener((adapterView, view, position, id) -> {
                //Context context = ShoppingListDetailsActivity.this;
                //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AddProductToListActivity.class);
                intent.putExtra("PRODUCT_OBJECT", nestedProducts.get(position));
                intent.putExtra("PRODUCT_OBJECT_ID", nestedProducts.get(position).get("localId").toString());
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("LIST_OBJECT", list);

                startActivity(intent);
            });
            multiChoiceForDelete(productListView, productAdapter, nestedProducts);
        } else {
            productListView.setAdapter(null);
        }
    }

    private ArrayList<String> getNestedProductNames() {
        ArrayList<HashMap> nestedProducts = getNestedProducts();
        sortProducts(nestedProducts, sort);
        ArrayList<String> nestedProductsNames = new ArrayList<>();

        for (HashMap nestedProduct : nestedProducts) {
            nestedProductsNames.add((String) nestedProduct.get("name"));
        }
        return nestedProductsNames;
    }

    public static void sortProducts(ArrayList<HashMap> nestedProducts, String sort) {
        switch (sort) {
            case "name":
                Collections.sort(nestedProducts, (o1, o2) -> {
                    String firstValue = o1.get("name").toString().toLowerCase();
                    String secondValue = o2.get("name").toString().toLowerCase();
                    return firstValue.compareTo(secondValue);
                });
                break;
            case "category":
                Collections.sort(nestedProducts, (o1, o2) -> {
                    String firstValue = o1.get("category").toString().toLowerCase();
                    String secondValue = o2.get("category").toString().toLowerCase();
                    return firstValue.compareTo(secondValue);
                });
                break;
            case "status":
                Collections.sort(nestedProducts, (o1, o2) -> {
                    String firstValue = o1.get("status").toString().toLowerCase();
                    String secondValue = o2.get("status").toString().toLowerCase();
                    return firstValue.compareTo(secondValue);
                });
                break;
            default:
                break;
        }
    }

    private ArrayList<HashMap> getNestedProducts() {
        return (ArrayList) list.get("nestedProducts");
    }

    private void multiChoiceForDelete(ListView list, ShoppingListDetailsAdapter productAdapter, List<HashMap> scoreList) {
        productListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        productListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                final int checkedCount = productListView.getCheckedItemCount();
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
                        ArrayList<HashMap> selecteditems = new ArrayList<>();
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

    private void deleteListAction(ShoppingListDetailsAdapter productAdapter, ArrayList<HashMap> selecteditems, ActionMode actionMode) {
        ArrayList<HashMap> products = (ArrayList<HashMap>) list.get("nestedProducts");

        for (HashMap selectedItem : selecteditems) {
            if (selectedItem != null) {
                products.remove(products.indexOf(selectedItem));
            }
        }

        if (ParseUser.getCurrentUser() == null) {
            ParseQuery<ParseObject> offlineListToUpdateQuery = ParseQuery.getQuery("ShoppingList");
            offlineListToUpdateQuery.whereEqualTo("localId", list.get("localId").toString());
            offlineListToUpdateQuery.fromLocalDatastore();
            offlineListToUpdateQuery.findInBackground((resultList, e) -> {
                if (e == null) {
                    ParseObject offlineListToUpdate = resultList.get(0);
                    offlineListToUpdate.put("nestedProducts", products);
                    offlineListToUpdate.pinInBackground();
                } else {
                    Log.d("deleteOfflineProductErr", "Error: " + e.getMessage());
                }
            });
        } else {
            list.put("nestedProducts", products);
            list.pinInBackground();
            list.saveEventually();
        }
        actionMode.finish();

        runOnUiThread(() -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });
    }

    private void changeSelectedIdsToObjects(ShoppingListDetailsAdapter productAdapter, ArrayList<HashMap> selecteditems, List<HashMap> scoreList) {
        SparseBooleanArray selected = productAdapter.getSelectedIds();
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                selecteditems.add(scoreList.get(selected.keyAt(i)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu - this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        for (int itemId : Arrays.asList(R.id.action_sort, R.id.action_share_list)) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_list:
                checkIfUserIsAbleToShareList();
                return true;
            case R.id.action_sort:
                PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_sort), Gravity.CENTER);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_sort_product, popup.getMenu());
                popup.setOnMenuItemClickListener(item1 -> {
                    switch (item1.getItemId()) {
                        case R.id.statusSort:
                            setSortForProducts("status");
                            return true;
                        case R.id.categorySort:
                            setSortForProducts("category");
                            return true;
                        case R.id.nameSort:
                            setSortForProducts("name");
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

    private void setSortForProducts(String sortMethod) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SortProducts");
        query.fromLocalDatastore();
        query.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() > 0) {
                    objects.get(0).put("sortBy", sortMethod);
                    objects.get(0).pinInBackground();
                } else {
                    ParseObject sortProduct = new ParseObject("SortProducts");
                    sortProduct.put("sortBy", sortMethod);
                    sortProduct.pinInBackground();
                }
                productAdapter.swapItems(getNestedProducts(), sort);
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void getFriendsUsernamesAndDisplayDialog() {
        List<String> friendsUsernames = new ArrayList<>();
        User.getCurrentUser().getUserData().fetchInBackground((userData, ex) -> {
            ParseObject.fetchAllInBackground(((UserData) userData).getFriends(), (updatedFriends, innerEx) -> {
                if (innerEx == null) {
                    for (User friend : updatedFriends) {
                        friendsUsernames.add(friend.getUsername());
                    }
                    displayShareListDialog(friendsUsernames);
                } else {
                    Toast.makeText(ShoppingListDetailsActivity.this,
                            "Błąd pobierania znajomych!", Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    private void checkIfUserIsAbleToShareList() {
        if (MiscUtils.isNetworkAvailable(this)) {
            String ownersUsername = list.getString("belongsTo");
            if (ParseUser.getCurrentUser().getUsername().equals(ownersUsername)) {
                getFriendsUsernamesAndDisplayDialog();
            } else {
                displayUnsubscribeListDialog();
            }
        } else {
            displayNoConnectionWithInternetToast();
        }

    }

    private void displayNoConnectionWithInternetToast() {
        String warningMsg = "Brak połączenia z internetem - brak możliwości udostępniania!";
        Toast warningToast = Toast.makeText(ShoppingListDetailsActivity.this, warningMsg, Toast.LENGTH_LONG);
        warningToast.show();
    }

    private void displayUnsubscribeListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListDetailsActivity.this);
        builder.setTitle("Czy chcesz odsubskrybować listę?");
        builder.setMessage("Nie jesteś właścicielem listy, więc nie możesz udostępnić jej swoim znajomym." +
                " Możesz ją natomiast odsubskrybować, aby już jej więcej nie widzieć.");
        builder.setIcon(R.drawable.ic_share_24dp);

        builder.setPositiveButton("Odsubskrybuj", (dialog, which) -> {
            unsubscribeList();
        });
        builder.setNegativeButton("Anuluj", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void unsubscribeList() {
        List<String> sharedAmong = (List<String>) list.get("sharedAmong");
        sharedAmong.remove(sharedAmong.indexOf(ParseUser.getCurrentUser().getUsername()));
        list.put("sharedAmong", sharedAmong);
        list.saveEventually();
        list.unpinInBackground(ex -> {
            if (ex == null) {
                Intent intent = new Intent(getBaseContext(), ShoppingListsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void displayShareListDialog(List<String> friendsUsernames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListDetailsActivity.this);
        builder.setTitle("Współdziel listę ze znajomymi");
        builder.setIcon(R.drawable.ic_share_24dp);

        String[] friendsArr = friendsUsernames.toArray(new String[0]);
        boolean[] checkedItems = new boolean[friendsArr.length];
        List<String> sharedAmongToUpdate = (List<String>) list.get("sharedAmong");

        for (int i = 0; i < friendsArr.length; i++) {
            if (sharedAmongToUpdate.contains(friendsArr[i])) {
                checkedItems[i] = true;
            }
        }

        builder.setMultiChoiceItems(friendsArr, checkedItems, (dialog, which, isChecked) -> {
            System.out.print(checkedItems.length);
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            List<String> sharedAmongUsernames = getCheckedValues(checkedItems, friendsArr);
            shareListAmongUsers(sharedAmongUsernames);
        });
        builder.setNegativeButton("Anuluj", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<String> getCheckedValues(boolean[] checkedItems, String[] arrayToMap) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i] == true) {
                result.add(arrayToMap[i]);
            }
        }
        return result;
    }

    private void shareListAmongUsers(List<String> usernames) {
        usernames.add(ParseUser.getCurrentUser().getUsername());
        list.put("sharedAmong", usernames);
        list.saveEventually();
        list.pinInBackground();
    }
}
