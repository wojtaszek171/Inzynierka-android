package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;

public class LoginActivity extends AppCompatActivity {
    private TextView loginTextView;
    private TextView loginPassword;
    private Button loginButton;

    private String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuLogInto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginTextView = findViewById(R.id.login_login);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLoginFormData();
                attemptUserLogin();
                Intent intent = new Intent(LoginActivity.this,ShoppingListsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLoginFormData() {
        login = loginTextView.getText().toString();
        password = loginPassword.getText().toString();
    }

    private void attemptUserLogin() {
        ParseUser.logInInBackground(login, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                Context context = getApplicationContext();
                String text;

                if (user != null) {
                    // Hooray! The user is logged in.
                    text = login + " - pomyślnie zalogowano!";
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    text = e.getMessage();
                }
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
