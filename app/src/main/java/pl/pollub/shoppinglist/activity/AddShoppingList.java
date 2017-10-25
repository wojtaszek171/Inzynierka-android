package pl.pollub.shoppinglist.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;

import com.parse.ParseObject;

import pl.pollub.shoppinglist.R;

public class AddShoppingList extends AppCompatActivity {
    int isLogged = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.addList);

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
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(AddShoppingList.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear)
                                +"-"+String.valueOf(dayOfMonth);
                        textDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });



    }


}
