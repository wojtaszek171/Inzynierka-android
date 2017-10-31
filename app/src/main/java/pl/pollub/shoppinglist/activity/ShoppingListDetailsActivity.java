package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;

public class ShoppingListDetailsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    String listId;
    String listName;
    ParseObject list;
    ListView product;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = getIntent().getParcelableExtra("LIST_OBJECT");
        listName = list.getString("name");
     //   listId = getIntent().getStringExtra("LIST_ID");
        setTitle(listName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setIdForLocal();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProductOfList");
        query.fromLocalDatastore();
        query.whereEqualTo("belongsTo",list);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    ArrayList<ParseObject> products = new ArrayList<ParseObject>();
                    ArrayList<String> names = new ArrayList<String>();
                    for(ParseObject s : scoreList){
                        products.add(s);
                        names.add(s.getString("name"));
                        Toast.makeText(ShoppingListDetailsActivity.this, "ilość produktów = "+scoreList.size(), Toast.LENGTH_SHORT).show();
                    }

                    ShoppingListDetailsAdapter productAdapter = new
                            ShoppingListDetailsAdapter(ShoppingListDetailsActivity.this, names, products);
                    product=(ListView)findViewById(R.id.list);
                    product.setAdapter(productAdapter);
                    product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            //Context context = ShoppingListDetailsActivity.this;
                            //Toast.makeText(context, "Position: "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), AddProductToList.class);
                            intent.putExtra("PRODUCT_OBJECT",scoreList.get(position));
                            intent.putExtra("LIST_NAME", listName);
                            intent.putExtra("LIST_OBJECT", list);

                            startActivity(intent);
                        }
                    });

                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        FloatingActionButton addProductB = (FloatingActionButton) findViewById(R.id.addProductButton);
        addProductB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), AddProductToList.class);
                intent.putExtra("LIST_NAME", listName);
                intent.putExtra("LIST_OBJECT", list);
                intent.putExtra("LOCAL_ID", id);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
        return true;
    }

    public void setIdForLocal(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("localId");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for(ParseObject s : scoreList){
                        id = s.getInt("id");
                        s.put("id",id+1);
                        s.pinInBackground();

                    }
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    if(scoreList.size()==0){
                        ParseObject localId = new ParseObject("localId");
                        localId.put("id",1);
                        localId.pinInBackground();
                        id = 1;
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
