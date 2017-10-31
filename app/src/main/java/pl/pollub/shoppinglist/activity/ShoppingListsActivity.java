package pl.pollub.shoppinglist.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;

public class ShoppingListsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    final Context context = this;
    ListView list;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    static int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        final FloatingActionButton addNew = (FloatingActionButton) findViewById(R.id.addListButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuLists);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        setIdForLocal();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> scoreList, ParseException e) {

                if (e == null) {
                    for(ParseObject s : scoreList) {
                        names.add(s.getString("name"));
                        dates.add(s.getString("deadline"));
                    }
                        ShoppingListsAdapter listAdapter = new
                                ShoppingListsAdapter(ShoppingListsActivity.this, names, dates);
                        list=(ListView)findViewById(R.id.list);
                        list.setAdapter(listAdapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Context context = ShoppingListsActivity.this;
                                //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                                String idList = scoreList.get(position).getString("objectId");
                                String nameList = scoreList.get(position).getString("name");
                                Intent intent = new Intent(getBaseContext(), ShoppingListDetailsActivity.class);
                                intent.putExtra("LIST_ID", idList);
                                intent.putExtra("LIST_NAME", nameList);
                                intent.putExtra("LIST_OBJECT",scoreList.get(position));

                                startActivity(intent);
                            }
                        });

                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });





        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_list_dialog);
                Button addList = (Button) dialog.findViewById(R.id.addList);
                addList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ShoppingListsActivity.this, AddShoppingList.class);
                        intent.putExtra("LOCAL_LIST_ID",id);
                        startActivity(intent);
                    }
                });
                // set the custom dialog components - text, image and button

                // if button is clicked, close the custom dialog

                dialog.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setIdForLocal(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("localId");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for(ParseObject s : scoreList){
                        id = s.getInt("id");
                        s.put("id",id+1);
                        s.pinInBackground();

                    }
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    if(scoreList.size()==0){
                        ParseObject localId = new ParseObject("localId");
                        localId.put("id",1);
                        localId.pinInBackground();
                        id = 1;
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(ShoppingListsActivity.this, BuddiesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(ShoppingListsActivity.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(ShoppingListsActivity.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(ShoppingListsActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(ShoppingListsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        //mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }
}
