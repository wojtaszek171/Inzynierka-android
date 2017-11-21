package pl.pollub.shoppinglist.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;

import bolts.Task;
import pl.pollub.shoppinglist.R;

public class AddShoppingList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int isLogged = 0;
    private static int id;
    private ActionBarDrawerToggle drawerToggle;
    private Button textDate;
    private boolean template;
    private ParseObject listTemplate;
    private ParseObject list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("LOCAL_LIST_ID", 1);
        template = getIntent().getBooleanExtra("TEMPLATE", false);
        listTemplate = getIntent().getParcelableExtra("LIST_TEMPLATE");

        textDate = findViewById(R.id.listDeadline);
        if(template==true){
            setTitle(R.string.addTemplate);
            textDate.setVisibility(View.GONE);
        }else if(listTemplate!=null) {
            setTitle("Podaj szczegóły listy");
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

        list = ParseObject.create("ShoppingList");

        list.put("name", listNameString);
        list.put("status", "0");
        list.put("deadline", textDate.getText().toString());
        list.put("isTemplate", template);


        if (ParseUser.getCurrentUser() != null) {
            String user = ParseUser.getCurrentUser().getUsername();
            list.put("belongsTo", ParseUser.getCurrentUser().getUsername());
            list.put("localId",user+Integer.toString(id));
            list.saveEventually(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        // No error, the object was saved
                        Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                    } else {
                        // Error saving object, print the logs
                        e.printStackTrace();
                    }
                }
            });

        }else {
            list.put("localId",Integer.toString(id));
        }
        list.pinInBackground(e -> {if (e == null) {
            if(listTemplate!=null){
                recoveryTempalte(listTemplate,list.get("localId"));
            }else {
                finish();
                Intent intent = new Intent(AddShoppingList.this, ShoppingListDetailsActivity.class);
                intent.putExtra("LIST_OBJECT", list);
                startActivity(intent);
            }
        } });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void setIdForLocal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("localId");
        query.fromLocalDatastore();
        query.findInBackground((scoreList, exception) -> {
            if (exception == null) {
                for (ParseObject s : scoreList) {
                    id = s.getInt("id");
                    s.increment("id");
                    s.pinInBackground();
                }
                Log.d("score", "Retrieved " + scoreList.size() + " scores");
                if (scoreList.size() == 0) {
                    ParseObject localId = new ParseObject("localId");
                    localId.put("id", 1);
                    localId.pinInBackground();
                    id = 1;
                }
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });
    }
    private void recoveryTempalte(ParseObject listTemplate, Object localIdd) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProductOfList");
        if(ParseUser.getCurrentUser()==null){
            query.fromLocalDatastore();
        }else
        if(!isNetworkAvailable()){
            query.fromLocalDatastore();
        }
        query.whereEqualTo("belongsTo", listTemplate.getString("localId"));

        query.findInBackground((scoreList, exception) -> {
            if (exception == null) {
                ArrayList<ParseObject> products = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                for (ParseObject s : scoreList) {
                    id++;
                    ParseObject product = new ParseObject("ProductOfList");
                    product.put("name", s.get("name"));
                    product.put("status", "0"); //status wykupienia produktu
                    product.put("amount", s.get("amount"));
                    product.put("category", s.get("category"));
                    product.put("description", s.get("description"));
                    product.put("measure", s.get("measure"));
                    product.put("icon", s.get("icon"));
                    if (ParseUser.getCurrentUser() != null) {
                        String user = ParseUser.getCurrentUser().getUsername();
                        product.put("localId",user + id);
                        product.put("belongsTo", localIdd);
                        product.saveEventually();
                    }else {
                        product.put("belongsTo", localIdd);
                        product.put("localId",id);
                    }
                    product.pinInBackground();

                    ParseQuery<ParseObject> queryy = ParseQuery.getQuery("localId");
                    queryy.fromLocalDatastore();
                    queryy.findInBackground((scoreListt, e) -> {
                        if (e == null) {
                            for (ParseObject item : scoreListt) {
                                item.put("id",id);
                                item.pinInBackground();
                            }
                        }
                    });
                }
                finish();
                Intent intent = new Intent(getApplicationContext(), ShoppingListDetailsActivity.class);
                intent.putExtra("LIST_OBJECT", list);
                startActivity(intent);
                Log.d("score", "Retrieved " + scoreList.size() + " scores");
            } else {
                Log.d("score", "Error: " + exception.getMessage());
            }
        });
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
                Intent intent = new Intent(AddShoppingList.this, FriendsActivity.class);
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
