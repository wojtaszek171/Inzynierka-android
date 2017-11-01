package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.Product;
import pl.pollub.shoppinglist.util.Measure;

import static pl.pollub.shoppinglist.util.Measure.Converter.stringToMeasure;

/**
 * Created by jrwoj on 24.10.2017.
 */

public class CustomProductAppenderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private AutoCompleteTextView measureSpinner;
    private Button resetButton, addButton;
    private TextView productNameField;
    private EditText productDescriptionField;
    private EditText productCategoryField;
    private EditText productPricePerUnitField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product_appender);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PRODUKTY UŻYTKOWNIKA");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.custom_products_appender_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFormInputComponents();

    }

    private void initFormInputComponents() {
        resetButton = findViewById(R.id.custom_products_reset_button);
        addButton = findViewById(R.id.custom_products_add_button);
        productNameField = findViewById(R.id.custom_products_name);
        productDescriptionField = findViewById(R.id.custom_products_description);
        productCategoryField = findViewById(R.id.custom_products_category);
        productPricePerUnitField = findViewById(R.id.custom_products_price_per_unit);
        initMeasureAutocomplete();
    }

    private void initMeasureAutocomplete() {
        measureSpinner = findViewById(R.id.custom_products_measure_autocomplete);
        measureSpinner.setAdapter(new ArrayAdapter<Measure>(this, android.R.layout.simple_list_item_1, Measure.values()));
    }

    public void resetFormValues(View view) {
        productNameField.setText("");
        productDescriptionField.setText("");
        measureSpinner.setSelection(0);
    }

    public void storeCustomProduct(View view) throws ParseException {
        Product newCustomProduct = new Product();
        newCustomProduct.setName(productNameField.getText().toString());
        newCustomProduct.setMeasure(stringToMeasure(measureSpinner.getText().toString()));
        newCustomProduct.setDescription(productDescriptionField.getText().toString());
        //newCustomProduct.setCategory(); TODO: KATEGORIA MUSI NAJPIERW ISTNIEC
        newCustomProduct.put("category", productCategoryField.getText().toString());
        // Price nie powinno byc w produkcie a w klasie 'przejsciowej' ProductShoppingList
        newCustomProduct.put("pricePerUnit", productPricePerUnitField.getText().toString());

        newCustomProduct.pinInBackground();
        testQuery();
    }

    public void testQuery() {
        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.fromLocalDatastore();
        query.whereEqualTo("name", productNameField.getText().toString());
        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> resultList, ParseException e) {
                Context context = getApplicationContext();
                String text;

                if (e == null) {
                    text = "Retrieved " + resultList.size() + " scores";
                    Log.d("score", "Retrieved " + resultList.size() + " scores");
                    goToCustomProductsList();

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    text = e.getMessage();
                }
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void goToCustomProductsList() {
        Intent intent = new Intent(CustomProductAppenderActivity.this, CustomProductsListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(CustomProductAppenderActivity.this, BuddiesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(CustomProductAppenderActivity.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(CustomProductAppenderActivity.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(CustomProductAppenderActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(CustomProductAppenderActivity.this, MainActivity.class);
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
