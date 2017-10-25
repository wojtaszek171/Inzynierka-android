package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.pollub.shoppinglist.R;

/**
 * Created by jrwoj on 24.10.2017.
 */

public class CustomProductsListActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter ;
    private List<ParseObject> allCustomProducts = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_products_list);


        list = (ListView) findViewById(R.id.custom_products_list_view);

//        adapter = new ArrayAdapter<>(this, R.layout.custom_products_list_item, getAllCustomProductsNames());

//        list.setAdapter(adapter);
        getAllCustomProducts();
    }

    ////////////////////////////////////////////
    public void getAllCustomProducts(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomProduct");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> resultList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + resultList.size() + " scores");
                    List<String> allProductsNames = new ArrayList<>();

                    for(ParseObject po : resultList){
                        allProductsNames.add(po.getString("name"));
                    }
                    createAdapter(allProductsNames);
                    list.setAdapter(adapter);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

//    public List<String> getAllCustomProductsNames(){
//        List<ParseObject> allProducts = getAllCustomProducts();
//        List<String> allProductsNames = new ArrayList<>();
//
//        for(ParseObject po : allProducts){
//            allProductsNames.add(po.getString("name"));
//        }
//        return allProductsNames;
//    }

    public void createAdapter(List<String> allProductsNames){
        adapter = new ArrayAdapter<>(this, R.layout.custom_products_list_item, allProductsNames);
    }

    public void goToCustomProductCreation(View view){
        Intent intent = new Intent(CustomProductsListActivity.this, CustomProductAppenderActivity.class);
        startActivity(intent);
    }
}
