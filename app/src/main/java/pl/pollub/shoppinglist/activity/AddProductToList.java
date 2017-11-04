package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;

public class AddProductToList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String listId;
    private String listName;
    private EditText productName;
    private EditText productAmount;
    private Spinner productCategory;
    private EditText productDescription;
    private Spinner productMeasure;
    private Button productIcon;
    private Button saveProductB;
    private ParseObject list;
    private String localId;
    private ActionBarDrawerToggle drawerToggle;

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
        productIcon = findViewById(R.id.product_icon);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategory.setAdapter(adapterCategory);
        ArrayAdapter<CharSequence> adapterMeasure = ArrayAdapter.createFromResource(this,
                R.array.product_measure, android.R.layout.simple_spinner_item);
        adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productMeasure.setAdapter(adapterMeasure);
        listName = getIntent().getStringExtra("LIST_NAME");
        list = getIntent().getParcelableExtra("LIST_OBJECT");
        localId = Integer.toString(getIntent().getIntExtra("LOCAL_ID", 1));
        final ParseObject productObject = getIntent().getParcelableExtra("PRODUCT_OBJECT");
        if (productObject != null) {//editing product
            String title = "Edytuj " + productObject.getString("name");
            setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            productName.setText(productObject.getString("name"));
            productAmount.setText(productObject.getString("amount"));
            int spinnerPositionCategory = adapterCategory.getPosition(productObject.getString("category"));
            productCategory.setSelection(spinnerPositionCategory);
            productDescription.setText(productObject.getString("description"));
            int spinnerPositionMeasure = adapterMeasure.getPosition(productObject.getString("measure"));
            productMeasure.setSelection(spinnerPositionMeasure);
            productIcon.setText(productObject.getString("icon"));

            saveProductB.setOnClickListener(view -> {
                productObject.put("name", productName.getText().toString());
                productObject.put("amount", productAmount.getText().toString());
                productObject.put("category", productCategory.getSelectedItem().toString());
                productObject.put("description", productDescription.getText().toString());
                productObject.put("measure", productMeasure.getSelectedItem().toString());
                productObject.put("icon", productIcon.getText().toString());
                productObject.pinInBackground();

                Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                intent.putExtra("LIST_OBJECT", list);
                startActivity(intent);
            });
        } else {
            String title = listName + " " + R.string.newProductToList;
            setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            saveProductB.setOnClickListener(view -> {
                ParseObject product = new ParseObject("ProductOfList");
                product.put("name", productName.getText().toString());
                product.put("status", "0"); //status wykupienia produktu
                product.put("amount", productAmount.getText().toString());
                product.put("category", productCategory.getSelectedItem().toString());
                product.put("description", productDescription.getText().toString());
                product.put("measure", productMeasure.getSelectedItem().toString());
                product.put("icon", productIcon.getText().toString());
                product.put("belongsTo", list);
                product.pinInBackground();
                product.setObjectId(localId);
                Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LIST_OBJECT", list);
                startActivity(intent);
            });
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(AddProductToList.this, BuddiesActivity.class);
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
