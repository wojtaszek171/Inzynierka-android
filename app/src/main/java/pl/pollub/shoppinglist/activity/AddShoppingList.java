package pl.pollub.shoppinglist.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseObject;

import pl.pollub.shoppinglist.R;

public class AddShoppingList extends AppCompatActivity {
    int isLogged = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Button saveNewList =(Button) findViewById(R.id.saveNewList);
        Button setDeadline =(Button) findViewById(R.id.listDeadline);
        ImageButton setListImage =(ImageButton) findViewById(R.id.setListImage);

        saveNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText listName =(EditText) findViewById(R.id.listName);
                String listNameString = listName.getText().toString();
                if(isLogged==0){
                    ParseObject list = new ParseObject("ShoppingList");
                    list.put("name", listNameString);
                    list.put("status", "0");
                    list.pinInBackground();
                    //list.saveInBackground();
                    Intent intent = new Intent(AddShoppingList.this,ShoppingListsActivity.class);
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

            }
        });



    }


}
