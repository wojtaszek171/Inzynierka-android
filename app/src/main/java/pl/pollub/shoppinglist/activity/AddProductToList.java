package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;

public class AddProductToList extends BaseNavigationActivity {
    private String listId;
    private String listName;
    private String productId;
    private EditText productName;
    private EditText productAmount;
    private Spinner productCategory;
    private EditText productDescription;
    private Spinner productMeasure;
    private Button productIcon;
    private Button saveProductB;
    private ParseObject list;
    private String localId;
    private ArrayAdapter<CharSequence> adapterCategory;
    private ArrayAdapter<CharSequence> adapterMeasure;
    private ParseObject productObject;

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

        getExtras();

        fillSpinnersByData();

        if (productObject != null) {//editing product
            editProduct();
        } else {
            createNewProduct();
        }

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
            product.put("description", productDescription.getText().toString());
            product.put("measure", productMeasure.getSelectedItem().toString());
            product.put("icon", productIcon.getText().toString());

            if (ParseUser.getCurrentUser() != null) {
                String user = ParseUser.getCurrentUser().getUsername();
                product.put("localId", user + localId);
                product.put("belongsTo", list.getString("localId"));
                product.saveEventually();
            } else {
                product.put("belongsTo", list.getString("localId"));
                product.put("localId", localId);
            }
            product.pinInBackground(e -> {
                if (e == null) {
                    finish();
                    Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                    intent.putExtra("LIST_OBJECT", list);
                    startActivity(intent);
                } else {
                }
            });
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
            query.findInBackground((scoreList, exception) -> {
                if (exception == null) {
                    for (ParseObject s : scoreList) {
                        s.put("name", productName.getText().toString());
                        s.put("amount", productAmount.getText().toString());
                        s.put("category", productCategory.getSelectedItem().toString());
                        s.put("description", productDescription.getText().toString());
                        s.put("measure", productMeasure.getSelectedItem().toString());
                        s.put("icon", productIcon.getText().toString());
                        s.pinInBackground(ex -> {
                            if (ex == null) {
                                finish();
                                Intent intent = new Intent(AddProductToList.this, ShoppingListDetailsActivity.class);
                                intent.putExtra("LIST_OBJECT", list);
                                startActivity(intent);
                            } else {
                            }
                        });
                        s.saveEventually();
                    }
                    Log.d("score", "Retrieved " + scoreList.size());
                } else {
                    Log.d("score", "Error: " + exception.getMessage());
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
        productIcon.setText(productObject.getString("icon"));
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        // return drawerlayout here
        return null;
    }

    @Override
    protected NavigationView getNavigationView() {
        // return nav view here
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }
}
