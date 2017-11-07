package pl.pollub.shoppinglist.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;

public class ShoppingListsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private final Context context = this;
    private ListView list;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private static int id;
    public int selectedItemPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        final FloatingActionButton addNew = findViewById(R.id.addListButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuLists);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        if (ParseUser.getCurrentUser() != null) {
            String user = ParseUser.getCurrentUser().getUsername();
            View hView = navigationView.getHeaderView(0);
            TextView username = hView.findViewById(R.id.user_pseudonym);
            username.setText(user);
            query.whereEqualTo("belongsTo",ParseUser.getCurrentUser());
            if(!isNetworkAvailable()){
                query.fromLocalDatastore();
            }
        }else {
            query.fromLocalDatastore();
        }
        setIdForLocal();
        query.findInBackground((scoreList, exception) -> {

            if (exception == null) {
                for (ParseObject s : scoreList) {
                    names.add(s.getString("name"));
                    dates.add(s.getString("deadline"));
                }
                ShoppingListsAdapter listAdapter = new
                        ShoppingListsAdapter(ShoppingListsActivity.this, names, dates);
                list = findViewById(R.id.list);
                list.setAdapter(listAdapter);
                list.setOnItemClickListener((adapterView, view, position, id) -> {
                    Context context = ShoppingListsActivity.this;
                    //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                    String idList = scoreList.get(position).getString("objectId");
                    String nameList = scoreList.get(position).getString("name");
                    Intent intent = new Intent(getBaseContext(), ShoppingListDetailsActivity.class);
                    intent.putExtra("LIST_ID", idList);
                    intent.putExtra("LIST_NAME", nameList);
                    intent.putExtra("LIST_OBJECT", scoreList.get(position));

                    startActivity(intent);
                });
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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
                        switch (menuItem.getItemId()){
                            case R.id.delete:
                                ArrayList<ParseObject> selecteditems = new ArrayList<ParseObject>();
                                SparseBooleanArray selected = listAdapter.getSelectedIds();
                                for(int i = (selected.size() - 1); i>=0; i--){
                                    if (selected.valueAt(i)){
                                        selecteditems.add(scoreList.get(selected.keyAt(i)));
                                    }
                                }
                                for(int i=0; i< selecteditems.size();i++){
                                    if(selecteditems.get(i).getObjectId()==null){
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                                                    query.fromLocalDatastore();
                                                    query.whereEqualTo("localId",selecteditems.get(i).getString("localId"));
                                                    query.findInBackground((scoreList, exception) -> {
                                                        if (exception == null) {
                                                            for (ParseObject s : scoreList) {
                                                                s.unpinInBackground();
                                                            }
                                                        } else {

                                                        }
                                                    });
                                    }else {
                                        selecteditems.get(i).deleteEventually();
                                        selecteditems.get(i).unpinInBackground();
                                    }

                                }
                                actionMode.finish();
                                finish();
                                overridePendingTransition( 0, 0);
                                startActivity(getIntent());
                                overridePendingTransition( 0, 0);
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {
                        listAdapter.removeSelection();
                    }
                });

                Log.d("score", "Retrieved " + scoreList.size() + " scores");
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });

        addNew.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_list_dialog);
            Button addList = dialog.findViewById(R.id.addList);
            addList.setOnClickListener(view1 -> {

                Intent intent = new Intent(ShoppingListsActivity.this, AddShoppingList.class);
                intent.putExtra("LOCAL_LIST_ID", id);
                startActivity(intent);
            });
            // set the custom dialog components - text, image and button

            // if button is clicked, close the custom dialog

            dialog.show();
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(getApplicationContext(), BuddiesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(getApplicationContext(), TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(getApplicationContext(), CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
}
