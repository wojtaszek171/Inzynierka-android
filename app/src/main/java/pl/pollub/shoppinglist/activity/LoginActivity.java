package pl.pollub.shoppinglist.activity;

import android.content.Context;
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

    TextView loginTextView;
    TextView loginPassword;
    Button loginButton;

    String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuLogInto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginTextView = (TextView) findViewById(R.id.login_login);
        loginPassword = (TextView) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getLoginFormData();
                attemptUserLogin();
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

    ////////////////////////////////////////////////////////
    public void getLoginFormData(){
        login = loginTextView.getText().toString();
        password = loginPassword.getText().toString();
    }

    public void attemptUserLogin(){
        ParseUser.logInInBackground(login, password, new LogInCallback() {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "";

            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    text = login + " - pomyślnie zalogowano!";
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    text = e.getMessage();
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }


}
