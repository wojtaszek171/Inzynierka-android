package pl.pollub.shoppinglist.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import pl.pollub.shoppinglist.R;

public class AddShoppingList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int isLogged = 0;
    static int id;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.addList);
        id = getIntent().getIntExtra("LOCAL_LIST_ID",1);
        Button saveNewList =(Button) findViewById(R.id.saveNewList);
        TextView setDeadline =(TextView) findViewById(R.id.listDeadline);
        ImageButton setListImage =(ImageButton) findViewById(R.id.setListImage);
        final Button textDate = (Button) findViewById(R.id.listDeadline);

        saveNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText listName =(EditText) findViewById(R.id.listName);
                String listNameString = listName.getText().toString();
                if(isLogged==0){

                    ParseObject list = new ParseObject("ShoppingList");
                    list.put("name", listNameString);
                    list.put("status", "0");
                    list.put("deadline",textDate.getText().toString());
                    //list.put("localId", id);
                    list.setObjectId(Integer.toString(id));
                    list.pinInBackground();
                    //id = list.getObjectId();
                    //list.saveInBackground();
                    Intent intent = new Intent(AddShoppingList.this,ShoppingListDetailsActivity.class);
                    intent.putExtra("LIST_OBJECT",list);
                    startActivity(intent);
                }else{

                }
            }
        });

        setListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        setDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(AddShoppingList.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear + 1)
                                +"-"+String.valueOf(dayOfMonth);
                        textDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
