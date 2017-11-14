package pl.pollub.shoppinglist.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

import pl.pollub.shoppinglist.R;

public class AddShoppingList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int isLogged = 0;
    private static int id;
    private ActionBarDrawerToggle drawerToggle;
    private Button textDate;
    private boolean template;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("LOCAL_LIST_ID", 1);
        template = getIntent().getBooleanExtra("TEMPLATE", false);
        textDate = findViewById(R.id.listDeadline);
        if(template==true){
            setTitle(R.string.addTemplate);
            textDate.setVisibility(View.GONE);
        }
        else {
            setTitle(R.string.addList);
        }
        Button saveNewList = findViewById(R.id.saveNewList);
        TextView setDeadline = findViewById(R.id.listDeadline);
        ImageButton setListImage = findViewById(R.id.setListImage);

        saveNewList.setOnClickListener(view -> {
            createShoppingList();
        });

        setListImage.setOnClickListener(view -> {

        });
        setDeadline.setOnClickListener(view -> {
            datePickerDialog();
        });
    }

    private void datePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(AddShoppingList.this, (view1, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            textDate.setText(date);
        }, yy, mm, dd);
        datePicker.show();
    }

    private void createShoppingList() {
        EditText listName = findViewById(R.id.listName);
        String listNameString = listName.getText().toString();

        ParseObject list = ParseObject.create("ShoppingList");

        list.put("name", listNameString);
        list.put("status", "0");
        list.put("deadline", textDate.getText().toString());
        list.put("isTemplate", template);


        if (ParseUser.getCurrentUser() != null) {
            String user = ParseUser.getCurrentUser().getUsername();
            list.put("belongsTo", ParseUser.getCurrentUser());
            list.put("localId",user+Integer.toString(id));
            list.saveEventually();

        }else {
            list.setObjectId(Integer.toString(id));
        }
        list.pinInBackground(e -> {if (e == null) {
            finish();
            Intent intent = new Intent(AddShoppingList.this, ShoppingListDetailsActivity.class);
            intent.putExtra("LIST_OBJECT", list);
            startActivity(intent);
        } else {
        }});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                Intent intent = new Intent(AddShoppingList.this, BuddiesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_lists: {
                Intent intent = new Intent(AddShoppingList.this, ShoppingListsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(AddShoppingList.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(AddShoppingList.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(AddShoppingList.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddShoppingList.this, MainActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }
}
