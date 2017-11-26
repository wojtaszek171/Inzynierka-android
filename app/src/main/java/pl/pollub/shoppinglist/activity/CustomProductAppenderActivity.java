package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.Arrays;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.util.customproductlist.CustomProductDataModel;

/**
 * Created by jrwoj on 24.10.2017.
 */

public class CustomProductAppenderActivity extends BaseNavigationActivity implements
        Validator.ValidationListener,
        LabelledSpinner.OnItemChosenListener {

    @NotEmpty(message = "Nazwa nie może być pusta.")
    private TextView productNameField;

    private LabelledSpinner productCategoryField;
    private String productCategoryValue;

    @Length(max = 100, message = "Max 100 znaków.")
    private EditText productDescriptionField;

    private Validator validator;

    private boolean editModeEnabled = false;
    private List<String> categoriesList;
    private ParseObject productToEdit;

    private Button clearCancelBtn;
    private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product_appender);

        categoriesList = Arrays.asList(getResources().getStringArray(R.array.product_categories));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.customUserProducts));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFormInputComponents();
        validator = new Validator(this);
        validator.setValidationListener(this);

        Bundle editProductBundle = getIntent().getExtras();
        editModeEnabled = editProductBundle != null && editProductBundle.getBoolean("EDIT_MODE_ENABLED");

        if (editModeEnabled) {
            productToEdit = (ParseObject) editProductBundle.get("PRODUCT_TO_EDIT");
            initFormInEditMode();
        } else {
            initFormInAddMode();
        }
    }

    //////////////////////////////////////////

    private void cancelProductEditing() {
        Intent cancelEditIntent = new Intent(CustomProductAppenderActivity.this, CustomProductsListActivity.class);
        startActivity(cancelEditIntent);
    }

    private void initFormInEditMode() {
        int productCategoryIdx = categoriesList.indexOf(productToEdit.get("category"));

        clearCancelBtn.setText(R.string.cancel);
        clearCancelBtn.setOnClickListener(view -> cancelProductEditing());

//        saveBtn.setOnClickListener(view -> updateProduct(productToEdit));

        productNameField.setText(productToEdit.get("name").toString());
        productDescriptionField.setText(productToEdit.get("description").toString());
        productCategoryField.setSelection(productCategoryIdx);
    }

    private void updateProduct() {

        ParseQuery<ParseObject> productToUpdateQuery = ParseQuery.getQuery("CustomProduct");
        productToUpdateQuery.fromLocalDatastore();
        productToUpdateQuery.whereEqualTo("localId", productToEdit.get("localId"));
        productToUpdateQuery.findInBackground((resultList, e) -> {
            if (e == null) {
                for (ParseObject objectToUpdate : resultList) {
                    if (objectToUpdate != null) {
                        objectToUpdate.put("name", productNameField.getText().toString());
                        objectToUpdate.put("description", productDescriptionField.getText().toString());
                        objectToUpdate.put("category", productCategoryValue);

                        objectToUpdate.pinInBackground();
                        testQuery();
                    }
                }
            }
        });
    }

    private void initFormInAddMode() {
        clearCancelBtn.setText(R.string.clear);
    }

    private void initFormInputComponents() {
        productNameField = findViewById(R.id.custom_products_name);
        productDescriptionField = findViewById(R.id.custom_products_description);
        productCategoryField = findViewById(R.id.custom_products_category);

        saveBtn = findViewById(R.id.custom_products_add_button);
        clearCancelBtn = findViewById(R.id.custom_products_reset_button);

        productCategoryField.setItemsArray(R.array.product_categories);
        productCategoryField.setOnItemChosenListener(this);
    }

    public void resetFormValues(View view) {
        productNameField.setText(null);
        productDescriptionField.setText(null);
        productCategoryField.setSelection(0);
    }

    public void submitCustomProduct(View view) {
        validator.validate();
    }

    public void commitCustomProduct() {
        CustomProductDataModel productModel = new CustomProductDataModel(
                productNameField.getText().toString(),
                productCategoryValue,
                productDescriptionField.getText().toString()
        );


        // TODO: UŻYĆ MODELU ADRIANA
        // ADRIAN: MÓJ MODEL NIE JEST ZGODNY NA TEN MOMENT Z TWOIM PROJEKTEM
        ParseObject newCustomProduct = ParseObject.create("CustomProduct");
        newCustomProduct.put("localId", productModel.getLocalId());
        newCustomProduct.put("name", productModel.getName());
        newCustomProduct.put("description", productModel.getDescription());
        newCustomProduct.put("category", productModel.getCategory());

        newCustomProduct.pinInBackground();
        testQuery();
    }

    public void testQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomProduct");
        query.fromLocalDatastore();
        query.whereEqualTo("name", productNameField.getText().toString());
        query.findInBackground((resultList, e) -> {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "";

            if (e == null) {
                text = "Retrieved " + resultList.size() + " products.";
                goToCustomProductsList();

            } else {
                text = e.getMessage();
            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
    }

    public void goToCustomProductsList() {
        Intent intent = new Intent(CustomProductAppenderActivity.this, CustomProductsListActivity.class);
        startActivity(intent);
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return findViewById(R.id.custom_products_appender_drawer_layout);
    }

    @Override
    protected NavigationView getNavigationView() {
        return findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public void onValidationSucceeded() {
        if (editModeEnabled) {
            updateProduct();
        } else {
            commitCustomProduct();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.custom_products_category:
                productCategoryValue = adapterView.getItemAtPosition(position).toString();
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }
}


