package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.pollub.shoppinglist.R;

import static pl.pollub.shoppinglist.util.MiscUtils.isNetworkAvailable;

public class AddProductToList extends AppCompatActivity {
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
    private ArrayAdapter<CharSequence> adapterCategory;
    private ArrayAdapter<CharSequence> adapterMeasure;
    private HashMap productObject;
    private String[] arrayIcons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_list);

        initLayoutElements();
        getExtras();
        fillSpinnersByData();

        arrayIcons = getResources().getStringArray(R.array.product_icons);
        setIcon();

        if (productObject != null) {
            //editing product
            editProduct();
        } else {
            createNewProduct();
        }
    }

    private void initLayoutElements() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saveProductB = findViewById(R.id.saveProductButton);
        productName = findViewById(R.id.product_name);
        productAmount = findViewById(R.id.product_amount);
        productCategory = findViewById(R.id.product_categories_spinner);
        productDescription = findViewById(R.id.product_description);
        productMeasure = findViewById(R.id.product_measure_spinner);
        icon = findViewById(R.id.item_icon);
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
        productObject = (HashMap) getIntent().getSerializableExtra("PRODUCT_OBJECT");
    }

    private void createNewProduct() {
        String title = listName + " - " + getString(R.string.newProductToList).toLowerCase();
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.whereEqualTo("localId", list.getString("localId"));

        if (ParseUser.getCurrentUser() == null || !isNetworkAvailable(this)) {
            query.fromLocalDatastore();
        }

        query.findInBackground((resultList, e) -> {
            if (e == null && resultList.size() > 0) {
                list = resultList.get(0);
                saveProductB.setOnClickListener(view -> {
                    HashMap product = new HashMap();
                    product.put("name", productName.getText().toString());
                    product.put("status", "0"); //status wykupienia produktu
                    product.put("amount", productAmount.getText().toString());
                    product.put("category", productCategory.getSelectedItem().toString());
                    product.put("image", arrayIcons[productCategory.getSelectedItemPosition()]);
                    product.put("description", productDescription.getText().toString());
                    product.put("measure", productMeasure.getSelectedItem().toString());


                    if (ParseUser.getCurrentUser() != null) {
                        String user = ParseUser.getCurrentUser().getUsername();
                        product.put("belongsTo", list.getString("localId"));
                        product.put("localId", user + localId);

                        list.add("nestedProducts", product);
                        list.saveEventually();
                    } else {
                        product.put("belongsTo", list.getString("localId"));
                        product.put("localId", localId);
                        list.add("nestedProducts", product);
                    }
                    list.pinInBackground(innerE -> {
                        if (e == null) {
                            finish();
                            Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                            intent.putExtra("LIST_OBJECT", list);
                            startActivity(intent);
                        } else {
                        }
                    });
                });

            } else {
            }
        });

    }

    private void editProduct() {
        String title = "Edytuj " + productObject.get("name").toString();
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillInTheForm(productObject);

        ArrayList<HashMap> nestedProducts = (ArrayList) list.get("nestedProducts");
        int indexOfProduct = nestedProducts.indexOf(productObject);


        if (indexOfProduct != -1) {
            HashMap productToUpdate = nestedProducts.get(indexOfProduct);

            saveProductB.setOnClickListener(view -> {
                productToUpdate.put("name", productName.getText().toString());
                productToUpdate.put("amount", productAmount.getText().toString());
                productToUpdate.put("category", productCategory.getSelectedItem().toString());
                productToUpdate.put("description", productDescription.getText().toString());
                productToUpdate.put("measure", productMeasure.getSelectedItem().toString());
                productToUpdate.put("image", arrayIcons[productCategory.getSelectedItemPosition()]);

                if (isNetworkAvailable(this) && ParseUser.getCurrentUser().getUsername() != null) {
                    list.put("nestedProducts", nestedProducts);
                    list.saveEventually();
                    list.pinInBackground(ex -> {
                        if (ex == null) {
                            finish();
                        }
                    });
                } else {
                    ParseQuery<ParseObject> offlineListToUpdateQuery = ParseQuery.getQuery("ShoppingList");
                    offlineListToUpdateQuery.whereEqualTo("localId", list.getString("localId"));
                    offlineListToUpdateQuery.fromLocalDatastore();
                    offlineListToUpdateQuery.findInBackground((List<ParseObject> resultList, ParseException e) -> {
                        if (e == null) {
                            if (resultList.size() > 0) {
                                ParseObject offlineListToUpdate = (ParseObject) resultList.get(0);
                                offlineListToUpdate.put("nestedProducts", nestedProducts);
                                offlineListToUpdate.pinInBackground(ex -> {
                                    if (ex == null) {
                                        Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                                        intent.putExtra("LIST_OBJECT", list);
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                                if (ParseUser.getCurrentUser().getUsername() != null) {
                                    offlineListToUpdate.saveEventually();
                                }
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Błąd edycji produktu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillInTheForm(HashMap productObject) {
        productName.setText(productObject.get("name").toString());
        productAmount.setText(productObject.get("amount").toString());
        int spinnerPositionCategory = adapterCategory.getPosition(productObject.get("category").toString());
        productCategory.setSelection(spinnerPositionCategory);
        productDescription.setText(productObject.get("description").toString());
        int spinnerPositionMeasure = adapterMeasure.getPosition(productObject.get("measure").toString());
        productMeasure.setSelection(spinnerPositionMeasure);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
