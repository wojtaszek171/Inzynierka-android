package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;

public class LoginActivity extends AppCompatActivity {
    private TextView loginTextView;
    private TextView loginPassword;
    private Button loginButton;

    private String login, password;
    private StringBuilder feedbackMessage = new StringBuilder();

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

        loginButton.setOnClickListener(view -> {
            getLoginFormData();
            attemptUserLogin();

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
        ParseUser.logInInBackground(login, password, (user, exception) -> {
            Context context = getApplicationContext();
            feedbackMessage.setLength(0);

            if (user != null && exception == null) {
                feedbackMessage.append(login).append(" - pomyślnie zalogowano!");
                Intent intent = new Intent(LoginActivity.this, ShoppingListsActivity.class);
                startActivity(intent);
            } else if (user == null) {
                feedbackMessage.append("Nieprawidłowy login i/lub hasło!");
            } else {
                feedbackMessage.append(exception.getMessage());
            }
            Toast.makeText(context, feedbackMessage, Toast.LENGTH_SHORT).show();
        });
    }

    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
