package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseObject;

import pl.pollub.shoppinglist.R;

public class AddProductToList extends AppCompatActivity {
        String listId;
        String listName;
    EditText productName;
    EditText productAmount;
    Spinner productCategory;
    EditText productDescription;
    Spinner productMeasure;
    Button productIcon;
    Button saveProductB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listId = getIntent().getStringExtra("LIST_ID_FOR_PRODUCT");
        listName = getIntent().getStringExtra("LIST_NAME");
        String title = listName + " " + R.string.newProductToList;
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveProductB = (Button) findViewById(R.id.saveProductButton);
        productName = (EditText) findViewById(R.id.product_name);
        productAmount = (EditText) findViewById(R.id.product_amount);
        productCategory = (Spinner) findViewById(R.id.product_categories_spinner);
        productDescription = (EditText) findViewById(R.id.product_description);
        productMeasure = (Spinner) findViewById(R.id.product_measure_spinner);
        productIcon = (Button) findViewById(R.id.product_icon);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        productCategory.setAdapter(adapterCategory);
        ArrayAdapter<CharSequence> adapterMeasure = ArrayAdapter.createFromResource(this,
                R.array.product_measure, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        productMeasure.setAdapter(adapterMeasure);
        saveProductB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject product = new ParseObject("ProductOfList");
                product.put("id_list", listId);
                product.put("name", productName.getText().toString());
                product.put("status", "0"); //status wykupienia produktu
                product.put("amount", productAmount.getText().toString());
                product.put("category", productCategory.getSelectedItem().toString());
                product.put("description", productDescription.getText().toString());
                product.put("measure", productMeasure.getSelectedItem().toString());
                product.put("icon", productIcon.getText().toString());
                product.saveInBackground();

                Intent intent = new Intent(AddProductToList.this,ShoppingListDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
