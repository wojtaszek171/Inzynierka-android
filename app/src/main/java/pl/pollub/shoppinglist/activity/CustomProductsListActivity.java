package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.Product;
import pl.pollub.shoppinglist.util.customproductlist.CustomProductDataModel;
import pl.pollub.shoppinglist.util.customproductlist.CustomProductsAdapter;

import static pl.pollub.shoppinglist.util.Measure.Converter.measureToString;

/**
 * Created by jrwoj on 24.10.2017.
 */

public class CustomProductsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView list;
    private CustomProductsAdapter adapter;
    private List<Product> allCustomProducts;
    private ArrayList<CustomProductDataModel> dataModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_products_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PRODUKTY UŻYTKOWNIKA");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.custom_products_list_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        list = findViewById(R.id.custom_products_list_view);
        getAllCustomProducts();
    }

    ////////////////////////////////////////////
    private void getAllCustomProducts() {

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> resultList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + resultList.size() + " scores");

                    dataModels = convertAllParseObjects(resultList);
                    adapter = new CustomProductsAdapter(dataModels, CustomProductsListActivity.this);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            CustomProductDataModel dataModel = dataModels.get(position);

                            Snackbar.make(view, dataModel.getName() + "\n" + dataModel.getCategory() + " API: " + dataModel.getDescription(), Snackbar.LENGTH_SHORT)
                                    .setAction("No action", null).show();
                        }
                    });
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    private void goToCustomProductCreation(View view) {
        Intent intent = new Intent(CustomProductsListActivity.this, CustomProductAppenderActivity.class);
        startActivity(intent);
    }

    private CustomProductDataModel convertParseObject(Product parseObject) {
        String name = parseObject.getName();
        String category = parseObject.getString("category");
        String measure = measureToString(parseObject.getMeasure());
        String description = parseObject.getDescription();
//        Double pricePerUnit = Double.parseDouble(parseObject.getString("pricePerUnit"));

        return new CustomProductDataModel(name, category, description, measure, 0.0);
    }

    private ArrayList<CustomProductDataModel> convertAllParseObjects(List<Product> parseObjectList) {
        ArrayList<CustomProductDataModel> resultList = new ArrayList<>();
        for (Product parseObject : parseObjectList) {
            resultList.add(convertParseObject(parseObject));
        }
        return resultList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(CustomProductsListActivity.this, BuddiesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                Intent intent = new Intent(CustomProductsListActivity.this, ShoppingListsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(CustomProductsListActivity.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(CustomProductsListActivity.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(CustomProductsListActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(CustomProductsListActivity.this, MainActivity.class);
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
}