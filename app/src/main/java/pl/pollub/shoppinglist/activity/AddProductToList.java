package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import pl.pollub.shoppinglist.R;

public class AddProductToList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String listId;
    private String listName;
    private String productId;
    private EditText productName;
    private EditText productAmount;
    private Spinner productCategory;
    private EditText productDescription;
    private Spinner productMeasure;
    private Button saveProductB;
    private ParseObject list;
    private ImageView icon;
    private String localId;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayAdapter<CharSequence> adapterCategory;
    private ArrayAdapter<CharSequence> adapterMeasure;
    private ParseObject productObject;
    private String[] arrayIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saveProductB = findViewById(R.id.saveProductButton);
        productName = findViewById(R.id.product_name);
        productAmount = findViewById(R.id.product_amount);
        productCategory = findViewById(R.id.product_categories_spinner);
        productDescription = findViewById(R.id.product_description);
        productMeasure = findViewById(R.id.product_measure_spinner);
        icon = findViewById(R.id.item_icon);

        getExtras();

        fillSpinnersByData();

        arrayIcons = getResources().getStringArray(R.array.product_icons);

        setIcon();

        if (productObject != null) {//editing product
            editProduct();
        } else {
            createNewProduct();
        }

    }

    private void setIcon() {
        productCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = arrayIcons[position];
                int idd = getResources().getIdentifier(name, "drawable", getPackageName());
                icon.setBackgroundResource(idd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillSpinnersByData() {
        adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategory.setAdapter(adapterCategory);
        adapterMeasure = ArrayAdapter.createFromResource(this,
                R.array.product_measure, android.R.layout.simple_spinner_item);
        adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productMeasure.setAdapter(adapterMeasure);
    }

    private void getExtras() {
        listName = getIntent().getStringExtra("LIST_NAME");
        list = getIntent().getParcelableExtra("LIST_OBJECT");
        localId = Integer.toString(getIntent().getIntExtra("LOCAL_ID", 1));
        productId = getIntent().getStringExtra("PRODUCT_OBJECT_ID");
        productObject = getIntent().getParcelableExtra("PRODUCT_OBJECT");
    }

    private void createNewProduct() {
        String title = listName + " " + R.string.newProductToList;
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveProductB.setOnClickListener(view -> {
            ParseObject product = new ParseObject("ProductOfList");
            product.put("name", productName.getText().toString());
            product.put("status", "0"); //status wykupienia produktu
            product.put("amount", productAmount.getText().toString());
            product.put("category", productCategory.getSelectedItem().toString());
            product.put("image", arrayIcons[productCategory.getSelectedItemPosition()]);
            product.put("description", productDescription.getText().toString());
            product.put("measure", productMeasure.getSelectedItem().toString());


            if (ParseUser.getCurrentUser() != null) {
                String user = ParseUser.getCurrentUser().getUsername();
                product.put("localId",user + localId);
                product.put("belongsTo", list.getString("localId"));
                product.saveEventually();
            }else {
                product.put("belongsTo", list.getString("localId"));
                product.put("localId",localId);
            }
            product.pinInBackground(e -> {if (e == null) {
                finish();
                Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                intent.putExtra("LIST_OBJECT", list);
                startActivity(intent);
            } else {
            }});
        });
    }

    private void editProduct() {
        String title = "Edytuj " + productObject.getString("name");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillInTheForm(productObject);

        saveProductB.setOnClickListener(view -> {
                //String user = ParseUser.getCurrentUser().getUsername();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ProductOfList");
                query.whereEqualTo("localId", productObject.get("localId"));
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList,
                                     ParseException e) {
                        if (e == null) {
                            for (ParseObject s : scoreList) {
                                s.put("name", productName.getText().toString());
                                s.put("amount", productAmount.getText().toString());
                                s.put("category", productCategory.getSelectedItem().toString());
                                s.put("image", arrayIcons[productCategory.getSelectedItemPosition()]);
                                s.put("description", productDescription.getText().toString());
                                s.put("measure", productMeasure.getSelectedItem().toString());
                                s.pinInBackground(ex -> {if (ex == null) {
                                    finish();
                                    Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                                    intent.putExtra("LIST_OBJECT", list);
                                    startActivity(intent);
                                } else {
                                }});
                                s.saveEventually();
                            }
                            Log.d("score", "Retrieved " + scoreList.size());
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
        });
    }

    private void fillInTheForm(ParseObject productObject) {
        productName.setText(productObject.getString("name"));
        productAmount.setText(productObject.getString("amount"));
        int spinnerPositionCategory = adapterCategory.getPosition(productObject.getString("category"));
        productCategory.setSelection(spinnerPositionCategory);
        productDescription.setText(productObject.getString("description"));
        int spinnerPositionMeasure = adapterMeasure.getPosition(productObject.getString("measure"));
        productMeasure.setSelection(spinnerPositionMeasure);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(AddProductToList.this, FriendsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                Intent intent = new Intent(AddProductToList.this, ShoppingListsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(AddProductToList.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(AddProductToList.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(AddProductToList.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProductToList.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
        }
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

