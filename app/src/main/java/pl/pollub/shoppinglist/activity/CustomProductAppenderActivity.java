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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.util.Measure;

/**
 * Created by jrwoj on 24.10.2017.
 */

public class CustomProductAppenderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    final Context context = this;
    
    AutoCompleteTextView measureSpinner;
    Button resetButton, addButton;
    TextView productNameField;
    EditText productDescriptionField;
    EditText productCategoryField;
    EditText productPricePerUnitField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product_appender);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PRODUKTY UŻYTKOWNIKA");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.custom_products_appender_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        
        initFormInputComponents();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //////////////////////////////////////////
    private void initFormInputComponents(){
        resetButton = (Button) findViewById(R.id.custom_products_reset_button);
        addButton = (Button) findViewById(R.id.custom_products_add_button);
        productNameField = (TextView) findViewById(R.id.custom_products_name);
        productDescriptionField = (EditText) findViewById(R.id.custom_products_description);
        productCategoryField = (EditText) findViewById(R.id.custom_products_category);
        productPricePerUnitField = (EditText) findViewById(R.id.custom_products_price_per_unit);
        initMeasureAutocomplete();
    }

    private void initMeasureAutocomplete(){
        measureSpinner = (AutoCompleteTextView) findViewById(R.id.custom_products_measure_autocomplete);
        measureSpinner.setAdapter(new ArrayAdapter<Measure>(this, android.R.layout.simple_list_item_1, Measure.values()));
    }

    public void resetFormValues(View view){
        productNameField.setText("");
        productDescriptionField.setText("");
        measureSpinner.setSelection(0);
    }

    public void storeCustomProduct(View view) throws ParseException {
        ParseObject newCustomProduct = new ParseObject("CustomProduct");
        newCustomProduct.put("name", productNameField.getText().toString());
        newCustomProduct.put("measure", measureSpinner.getText().toString());
        newCustomProduct.put("description", productDescriptionField.getText().toString());
        newCustomProduct.put("category", productCategoryField.getText().toString());
        newCustomProduct.put("pricePerUnit", productPricePerUnitField.getText().toString());

        newCustomProduct.pinInBackground();
        testQuery();
    }

    public void testQuery(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomProduct");
        query.fromLocalDatastore();
        query.whereEqualTo("name", productNameField.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> resultList, ParseException e) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                CharSequence text = "";

                if (e == null) {
                    text = "Retrieved " + resultList.size() + " scores";
                    Log.d("score", "Retrieved " + resultList.size() + " scores");
                    goToCustomProductsList();

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    text = e.getMessage();
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    public void goToCustomProductsList(){
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
                mDrawerLayout.closeDrawer(GravityCompat.START);
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
