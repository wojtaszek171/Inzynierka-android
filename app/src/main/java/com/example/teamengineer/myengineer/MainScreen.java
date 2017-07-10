package com.example.teamengineer.myengineer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuMainScreen);
        

    }

    public void logInto(View view) {
        Intent intent = new Intent(MainScreen.this,LogInto.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(MainScreen.this,Register.class);
        startActivity(intent);
    }
}
