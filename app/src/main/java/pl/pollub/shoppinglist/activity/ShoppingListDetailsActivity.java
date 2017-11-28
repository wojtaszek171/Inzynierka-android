package pl.pollub.shoppinglist.activity;

import android.app.AlertDialog;
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
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.ShoppingListDetailsAdapter;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

public class ShoppingListDetailsActivity extends BaseNavigationActivity {
    //    private String listId;
    private String listName;
    private ParseObject list;

    private ListView productListView;
    private ShoppingListDetailsAdapter productAdapter;

    private int id;
    public int selectedItemPos;

    ParseLiveQueryClient parseLiveQueryClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent intent = new Intent(getBaseContext(), AddProductToList.class);
        intent.putExtra("LIST_NAME", listName);
        intent.putExtra("LIST_OBJECT", list);
        intent.putExtra("LOCAL_ID", id);
        startActivityForResult(intent, 0);
    }

    private void createListOfProducts() {
        prepareNestedProductsAdapter();
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
            displayLiveQueryUpdateToast(event.name(), "someUser", (HashMap) object.get("estimatedData"));
        });
    }

    private void updateNestedProductsAdapter() {
        if (productAdapter != null) {
            runOnUiThread(() -> productAdapter.swapItems(getNestedProducts()));
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
                    ShoppingListDetailsAdapter(ShoppingListDetailsActivity.this, nestedProductsNames, nestedProducts);
            productListView.setAdapter(null);
            productListView.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();

            productListView.setOnItemClickListener((adapterView, view, position, id) -> {
                //Context context = ShoppingListDetailsActivity.this;
                //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AddProductToList.class);
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
        ArrayList<String> nestedProductsNames = new ArrayList<>();

        for (HashMap nestedProduct : nestedProducts) {
            nestedProductsNames.add((String) nestedProduct.get("name"));
        }
        return nestedProductsNames;
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
                        ArrayList<HashMap> selecteditems = new ArrayList<HashMap>();
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
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_list:
                checkIfUserIsAbleToShareList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
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
        if (isNetworkAvailable()) {
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
        builder.setIcon(android.R.drawable.ic_menu_share);

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
        list.unpinInBackground();

        Intent intent = new Intent(getBaseContext(), ShoppingListsActivity.class);
        finish();
        startActivity(intent);
    }

    private void displayShareListDialog(List<String> friendsUsernames) {


        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListDetailsActivity.this);
        builder.setTitle("Współdziel listę ze znajomymi:");
        builder.setIcon(android.R.drawable.ic_menu_share);

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
