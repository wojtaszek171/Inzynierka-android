package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuMainScreen);

        if (ParseUser.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), ShoppingListsActivity.class);
            startActivity(intent);
        }
    }

    public void logInto(View view) {
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void Lists(View view) {
        Intent intent = new Intent(MainActivity.this, ShoppingListsActivity.class);
        startActivity(intent);
    }
}
